package com.james.calcite.JamesCalcite;

import java.util.ArrayList;
import java.util.List;

import com.james.calcite.utils.JamesUtil;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlAbstractParserImpl.Metadata;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.tools.Frameworks;


public class Demo1 {
    public static void main(String[] args) throws SqlParseException {
        List<String> schemaPath = new ArrayList<String>();
        List<String> viewPath = new ArrayList<String>();

        final String sql = "SELECT a,b from t_a";
//        final String sql = "select a,b from t_a where a>10 group by a,b order by a desc";
//        String sql = "SELECT tt.a_a AS f_a_a, tt.b_b AS f_b_b, tt.b_c AS f_b_c, at_b.b_same " + 
//                "FROM (" + 
//                "    SELECT a_key, MAX(a_a) AS a_a, MAX(a_b) AS a_b " + 
//                "        , MAX(a_c) AS a_c, MAX(same) AS same " + 
//                "    FROM t_a " + 
//                "    WHERE a_c = 3 " + 
//                "    GROUP BY a_key " + 
//                "    ORDER BY a_a " + 
//                ") at_a " + 
//                "    LEFT JOIN ( " + 
//                "        SELECT b_key, MAX(b_a) AS b_a, MAX(b_b) AS b_b " + 
//                "            , MAX(b_c) AS b_c, MAX(same) AS b_same " + 
//                "        FROM t_b " + 
//                "        GROUP BY b_key " + 
//                "        ORDER BY b_b " + 
//                "    ) at_b " + 
//                "    ON at_a.a_key = at_b.b_key";

//        System.out.println(sql);

        SqlParser parser = SqlParser.create(sql);

        Metadata meta = parser.getMetadata();
        for (String s : meta.getTokens()) {
            System.out.println(s);
        }

        JamesUtil.printDivider();
        SqlNode node = parser.parseQuery();
        System.out.println("node: "+node.toString());
        SqlParserPos pos = node.getParserPosition();

        // SqlNode node2=parser.parseStmt();

        SqlKind kind = node.getKind();

        SchemaPlus defaultSchema = Frameworks.createRootSchema(false);
        // FrameworkConfig config =
        // Frameworks.newConfigBuilder().defaultSchema(defaultSchema).build();
//        Planner planner = Frameworks.getPlanner(config);
//        SqlNode plannNode = planner.parse(sql);

        JamesUtil.printDivider();

        VolcanoPlanner planner1 = new VolcanoPlanner();
        RexBuilder rexBuilder = new RexBuilder(new JavaTypeFactoryImpl());
        RelOptCluster cluster = RelOptCluster.create(planner1, rexBuilder);

//        SqlValidator validator = config.createSqlValidator();
//        SqlNode sqlNode1 = validator.validate(sqlNode);
//        final SqlToRelConverter.Config config = SqlToRelConverter.configBuilder()
//                .withTrimUnusedFields(true).build();
//        SqlToRelConverter sqlToRelConverter =
//            getSqlToRelConverter(validator, catalogReader, config);

//        final SqlToRelConverter.Config config = SqlToRelConverter.configBuilder()
//                .withConfig(SqlToRelConverter.Config).withTrimUnusedFields(false).withConvertTableAccess(false)
//                .build();
        
//        final RelDataTypeFactory.Builder b =
//                context.getCluster().getTypeFactory().builder();

//        final SqlToRelConverter sqlToRelConverter = new SqlToRelConverter(null, null, null, cluster, null, config);
//
//        RelRoot root = sqlToRelConverter.convertQuery(node, false, false);
//        System.out.println(root);
        System.out.println("end");
    }
}
