package com.james.calcite.JamesCalcite;

import java.util.List;

import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlJoin;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.validate.SqlValidatorImpl;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;

import com.james.calcite.JamesCalcite.TestOne.TestSchema;
import com.james.common.util.JamesUtil;

public class CalciteParserDemo {
    public static void main(String[] args) throws SqlParseException, ValidationException, RelConversionException {
//        final String sql = "select a,b,c,d from t_";
        String sql = "SELECT tt.a_a AS f_a_a, tt.b_b AS f_b_b, tt.b_c AS f_b_c, at_b.b_same " +
                "FROM (" +
                "    SELECT a_key, MAX(a_a) AS a_a, MAX(a_b) AS a_b " +
                "        , MAX(a_c) AS a_c, MAX(same) AS same " +
                "    FROM t_a " +
                "    WHERE a_c = 3 " +
                "    GROUP BY a_key " +
                "    ORDER BY a_a " +
                ") at_a " +
                "    LEFT JOIN ( " +
                "        SELECT b_key, MAX(b_a) AS b_a, MAX(b_b) AS b_b " +
                "            , MAX(b_c) AS b_c, MAX(same) AS b_same " +
                "        FROM t_b " +
                "        GROUP BY b_key " +
                "        ORDER BY b_b " +
                "    ) at_b " +
                "    ON at_a.a_key = at_b.b_key";

        SqlNode node = ((SqlSelect) CalciteParser.parse(sql)).getSelectList();
        System.out.println("node:\n" + node);
        JamesUtil.printDivider();

        SqlNode nodeInsert = ((SqlSelect) CalciteParser.parse(sql)).getSelectList();
        System.out.println("node:\n" + node);
        JamesUtil.printDivider();

        SqlNode node2 = ((SqlSelect) CalciteParser.parse(sql)).getFrom();
        System.out.println("node2:\n" + node2);

//        SqlValidatorImpl validator=new SqlValidatorImpl();


        Frameworks.ConfigBuilder configBuilder = Frameworks.newConfigBuilder();
        SchemaPlus schemaPlus = Frameworks.createRootSchema(true);
        schemaPlus.add("T", new ReflectiveSchema(new TestSchema()));
        configBuilder.defaultSchema(schemaPlus);
        FrameworkConfig frameworkConfig = configBuilder.build();
        Planner planner = Frameworks.getPlanner(frameworkConfig);
        RelRoot relRoot = planner.rel(node2);
        relRoot.rel.getRowType();
        RelNode relNode = relRoot.project();
        System.out.print(RelOptUtil.toString(relNode));
        JamesUtil.printDivider();

        SqlNode node3 = ((SqlSelect) CalciteParser.parse(sql)).getFetch();
        System.out.println("node3:\n" + node3);
        JamesUtil.printDivider();

        SqlNode node4 = ((SqlSelect) CalciteParser.parse(sql)).getWhere();
        System.out.println("node4:\n" + node4);
        JamesUtil.printDivider();

        SqlNode node5 = ((SqlSelect) CalciteParser.parse(sql)).getWindowList();
        System.out.println("node5:\n" + node5);
        JamesUtil.printDivider();

        List<SqlNode> operList = ((SqlSelect) CalciteParser.parse(sql)).getOperandList();
        System.out.println("operList:\n");
        for (SqlNode oper : operList) {
            System.out.println("\t" + oper);
        }
        JamesUtil.printDivider();
    }
}
