package com.james.calcite.JamesCalcite;

import java.util.Date;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelFieldCollation;
import org.apache.calcite.rel.RelFieldCollation.Direction;
import org.apache.calcite.rel.RelFieldCollation.NullDirection;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.schema.Schema.TableType;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;
import org.apache.calcite.schema.StreamableTable;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;
import org.apache.calcite.util.ImmutableBitSet;

import com.google.common.collect.ImmutableList;

public class BeamSqlExplainer {
    public static final JavaTypeFactory typeFactory = new JavaTypeFactoryImpl(RelDataTypeSystem.DEFAULT);
    public static final SchemaPlus defaultSchema = Frameworks.createRootSchema(false);

    private FrameworkConfig config = Frameworks.newConfigBuilder().defaultSchema(defaultSchema).build();
    private Planner planner = Frameworks.getPlanner(config);

    public BeamSqlExplainer() {
        addTableSchema();
    }

    public void addTableSchema() {
        defaultSchema.add("ORDER_DETAILS", new StreamableTable() {
            public RelDataType getRowType(RelDataTypeFactory typeFactory) {
                RelDataTypeFactory.FieldInfoBuilder b = typeFactory.builder();
                b.add("CK_TIME", typeFactory.createJavaType(Date.class));
                b.add("ITEM_ID", typeFactory.createJavaType(Long.class));
                b.add("ITEM_PRICE", typeFactory.createJavaType(Double.class));
                b.add("BUYER_NAME", typeFactory.createJavaType(String.class));
                b.add("QUANTITY", typeFactory.createJavaType(Integer.class));

                return b.build();
            }

            public Statistic getStatistic() {
//        return Statistics.of(100, ImmutableList.<ImmutableBitSet>of());
                Direction dir = Direction.ASCENDING;
                RelFieldCollation collation = new RelFieldCollation(0, dir, NullDirection.UNSPECIFIED);
                return Statistics.of(5, ImmutableList.of(ImmutableBitSet.of(0)),
                        ImmutableList.of(RelCollations.of(collation)));
            }

            public TableType getJdbcTableType() {
                return TableType.STREAM;
            }

            public Table stream() {
                return null;
            }

            @Override
            public boolean isRolledUp(String column) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean rolledUpColumnValidInsideAgg(String column, SqlCall call, SqlNode parent,
                    CalciteConnectionConfig config) {
                // TODO Auto-generated method stub
                return false;
            }

        });
    }

    public void explain(String sql) throws SqlParseException, ValidationException, RelConversionException {
        SqlNode parse = planner.parse(sql);
        System.out.println(parse.toString());

        SqlNode validate = planner.validate(parse);
        RelNode tree = planner.convert(validate);

        String plan = RelOptUtil.toString(tree); // explain(tree, SqlExplainLevel.ALL_ATTRIBUTES);
        System.out.println("plan>");
        System.out.println(plan);

    }

}