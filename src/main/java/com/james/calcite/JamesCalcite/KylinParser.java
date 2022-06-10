package com.james.calcite.JamesCalcite;

import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlDataTypeSpec;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlIntervalQualifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.util.SqlBasicVisitor;
import org.apache.calcite.sql.util.SqlShuttle;
import org.apache.calcite.sql.util.SqlVisitor;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;

import com.james.calcite.JamesCalcite.SqlToRelConverterDemo.TestSchema;
import com.james.calcite.utils.JamesUtil;

public class KylinParser {
    public static void main(String[] args) throws SqlParseException, RelConversionException {
        final String sql = "select a,b from t_a where a>10";

        SqlNode sqlNode = CalciteParser.parse(sql);
//        System.out.println(sqlNode);
        System.out.println(sqlNode.getKind());
        JamesUtil.printDivider();

        SqlVisitor<Object> sqlVisitor = new SqlBasicVisitor<Object>() {
            @Override
            public Object visit(SqlIdentifier id) {
//                System.out.println(id);
                // System.out.println(CalciteParser.getLastNthName(id, 1));

                if (id.names.size() > 1) {
                    throw new IllegalArgumentException(
                            "Column Identifier in the computed column expression should only contain COLUMN");
                }

                System.out.println("id: " + id);

                return null;
            }

            @Override
            public Object visit(SqlDataTypeSpec type) {
                System.out.println("type: " + type);

                return null;
            }

            @Override
            public Object visit(SqlDynamicParam param) {
                System.out.println("param: " + param);

                return null;
            }

            @Override
            public Object visit(SqlIntervalQualifier intervalQualifier) {
                System.out.println("intervalQualifier" + intervalQualifier);

                return null;
            }
        };

        SqlShuttle sqlShuttle = new SqlShuttle();

        SqlNode child = (SqlNode) sqlNode.accept(sqlVisitor);
//        System.out.println("child: " + child.getKind());

//        SqlNode grandChild = (SqlNode) child.accept(sqlVisitor);
//        System.out.println("grandChild: " + grandChild.getKind());
        JamesUtil.printDivider();

        System.out.println(child);
//        System.out.println(grandChild);
        JamesUtil.printDivider();

        SchemaPlus schemaPlus = Frameworks.createRootSchema(true);

        // 给schema T中添加表
        schemaPlus.add("T", new ReflectiveSchema(new TestSchema()));
        Frameworks.ConfigBuilder configBuilder = Frameworks.newConfigBuilder();
        // 设置默认schema
        configBuilder.defaultSchema(schemaPlus);

        FrameworkConfig frameworkConfig = configBuilder.build();

        SqlParser.ConfigBuilder paresrConfig = SqlParser.configBuilder(frameworkConfig.getParserConfig());

        // SQL 大小写不敏感
        paresrConfig.setCaseSensitive(false).setConfig(paresrConfig.build());

        Planner planner = Frameworks.getPlanner(frameworkConfig);
        SqlNode sqlNodeSelect = CalciteParser.getOnlySelectNode(sql);
        RelRoot relRoot = planner.rel(sqlNodeSelect);
        RelNode relNode = relRoot.project();
        System.out.print(RelOptUtil.toString(relNode));
    }
}
