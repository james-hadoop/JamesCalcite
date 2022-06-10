package com.james.calcite.driver;

import com.james.calcite.JamesCalcite.*;
import com.james.calcite.driver.demo.SqlMyOperatorTable;
import com.james.calcite.utils.JamesUtil;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.RelDistributionTraitDef;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.rel.rel2sql.SqlImplementor;
import org.apache.calcite.rel.rules.PruneEmptyRules;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.rel.type.RelDataTypeSystemImpl;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.type.BasicSqlType;
import org.apache.calcite.sql.type.SqlTypeFactoryImpl;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.apache.calcite.sql2rel.RelDecorrelator;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.RelBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JamesCalciteDemo {
    static String sql1 = "SELECT t1.id, t1.name FROM ( SELECT student.id, student.name FROM school.student WHERE student.create_time > to_date('2021-03-04 12:00:00', 'yyyy-mm-dd hh24:mi:ss') ) t1";

    public static void main(String[] args) throws SqlParseException {
        String sql = sql1;

        System.out.println(String.format("originSql = \n\t%s", sql));
        JamesUtil.printDivider("JamesCalciteDemo");

        List<Table> tables = new ArrayList<>();
        List<Table.Field> fields = new ArrayList<>();
        fields.add(new Table.Field("id", DataTypeEnum.NUMBER.getType()));
        fields.add(new Table.Field("name", DataTypeEnum.STRING.getType()));
        fields.add(new Table.Field("create_time", DataTypeEnum.DATE.getType()));

        tables.add(new Table("school", "student", fields));

        SchemaPlus rootSchema = registerRootSchema(tables);

        // 1. parse
        SqlParser parser = SqlParser.create(sql, SqlParser.Config.DEFAULT);
        SqlNode sqlNode = parser.parseStmt();

        // 2. validate
        SqlTypeFactoryImpl factory = new SqlTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
        CalciteCatalogReader calciteCatalogReader = new CalciteCatalogReader(CalciteSchema.from(rootSchema), CalciteSchema.from(rootSchema).path(null), factory, new CalciteConnectionConfigImpl(new Properties()));
        SqlValidator validator = SqlValidatorUtil.newValidator(SqlMyOperatorTable.instance(), calciteCatalogReader, factory, SqlValidator.Config.DEFAULT);
        SqlNode validatedSqlNode = validator.validate(sqlNode);

        // 3. optimize
        final FrameworkConfig fromworkConfig = Frameworks.newConfigBuilder()
                .parserConfig(SqlParser.Config.DEFAULT)
                .defaultSchema(rootSchema)
                .traitDefs(ConventionTraitDef.INSTANCE, RelDistributionTraitDef.INSTANCE)
                .build();

        HepProgramBuilder builder = new HepProgramBuilder();
        builder.addRuleInstance(PruneEmptyRules.PROJECT_INSTANCE);
        HepPlanner planner = new HepPlanner(builder.build());

        final RexBuilder rexBuilder = new RexBuilder(factory);
        final RelOptCluster cluster = RelOptCluster.create(planner, rexBuilder);

        // init SqlToRelConverter config
        final SqlToRelConverter.Config config = SqlToRelConverter.config();

        // SqlNode转换为RelNode
        final SqlToRelConverter sqlToRelConverter = new SqlToRelConverter(
                new ViewExpanderImpl(),
                validator,
                calciteCatalogReader,
                cluster,
                fromworkConfig.getConvertletTable(),
                config
        );

        RelRoot root = sqlToRelConverter.convertQuery(validatedSqlNode, false, true);

        root = root.withRel(sqlToRelConverter.flattenTypes(root.rel, true));
        final RelBuilder relBuilder = config.getRelBuilderFactory().create(cluster, null);
        root = root.withRel(RelDecorrelator.decorrelateQuery(root.rel, relBuilder));
        RelNode relNode = root.rel;

        // 寻找优化路径
        planner.setRoot(relNode);
        relNode = planner.findBestExp();

        // 转换为需要的数据库类型的sql
        RelToSqlConverter relToSqlConverter = new RelToSqlConverter(SqlDialect.DatabaseProduct.HIVE.getDialect());
        SqlImplementor.Result visit = relToSqlConverter.visitRoot(relNode);
        SqlNode transformedSql = visit.asStatement();
        System.out.println(String.format("transformedSql = \n\t%s", transformedSql));

        // 4. execute
    } // main()

    /***
     * 注册表的字段信息
     * @author test
     * @date 2021/3/1 19:17
     * @param tables 表名
     * @return org.apache.calcite.schema.SchemaPlus
     **/
    private static SchemaPlus registerRootSchema(List<Table> tables) {
        SchemaPlus rootSchema = Frameworks.createRootSchema(true);

        List<Table> noSchemaTables = tables.stream().filter(t -> StringUtils.isEmpty(t.getSchema())).collect(Collectors.toList());
        for (Table table : noSchemaTables) {
            rootSchema.add(table.getTable().toUpperCase(), new AbstractTable() {
                @Override
                public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
                    RelDataTypeFactory.Builder builder = relDataTypeFactory.builder();

                    table.getFields().forEach(field -> {
                        builder.add(
                                field.getName().toUpperCase(),
                                new BasicSqlType(new RelDataTypeSystemImpl() {}, SqlTypeName.VARCHAR)
                        );
                    });

                    return builder.build();
                }
            });
        }

        Map<String, List<Table>> map = tables.stream().filter(t -> StringUtils.isNotEmpty(t.getSchema())).collect(Collectors.groupingBy(Table::getSchema));
        for (String key : map.keySet()) {
            List<Table> tableList = map.get(key);
            SchemaPlus schema = Frameworks.createRootSchema(true);
            for (Table table : tableList) {
                schema.add(table.getTable().toUpperCase(), new AbstractTable() {
                    @Override
                    public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
                        RelDataTypeFactory.Builder builder = relDataTypeFactory.builder();

                        table.getFields().forEach(field -> {
                            builder.add(
                                    field.getName().toUpperCase(),
                                    new BasicSqlType(new RelDataTypeSystemImpl() {}, getSqlTypeName(field.getType()))
                            );
                        });

                        return builder.build();
                    }
                });
            }

            rootSchema.add(key.toUpperCase(), schema);
        }

        return rootSchema;
    }

    /***
     * 根据DataTypeEnum获取对应的SqlTypeName
     * @author test
     * @date 2021/3/6 10:55
     * @param type DataTypeEnum的类型
     * @return org.apache.calcite.sql.type.SqlTypeName
     **/
    private static SqlTypeName getSqlTypeName(String type) {
        DataTypeEnum dataType = DataTypeEnum.getByCode(type);
        if (null == dataType) {
            return SqlTypeName.VARCHAR;
        }

        switch (dataType) {
            case CHAR:
            case CLOB:
            case TEXT:
            case BLOB:
            case STRING: return SqlTypeName.VARCHAR;
            case DECIMAL:
            case NUMBER: return SqlTypeName.DECIMAL;
            case INTEGER: return SqlTypeName.INTEGER;
            case DATE: return SqlTypeName.DATE;
            case TIMESTAMP: return SqlTypeName.DATE;
            default: return SqlTypeName.VARCHAR;
        }
    }

    private static class ViewExpanderImpl implements RelOptTable.ViewExpander {
        public ViewExpanderImpl() {
        }

        @Override
        public RelRoot expandView(RelDataType rowType, String queryString, List<String> schemaPath, List<String> viewPath) {
            return null;
        }
    }
}