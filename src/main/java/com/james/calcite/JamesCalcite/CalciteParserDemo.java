package com.james.calcite.JamesCalcite;

import com.james.calcite.utils.JamesUtil;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.parser.SqlParserUtil;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;

import java.util.List;

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
                ") at_a " +
                "    LEFT JOIN ( " +
                "        SELECT b_key, MAX(b_a) AS b_a, MAX(b_b) AS b_b " +
                "            , MAX(b_c) AS b_c, MAX(same) AS b_same " +
                "        FROM t_b " +
                "        GROUP BY b_key " +
                "    ) at_b " +
                "    ON at_a.a_key = at_b.b_key";

        SqlNode sqlNode = CalciteParser.parse(sql);
        SqlParserPos sqlParserPos = sqlNode.getParserPosition();

        // SqlParserUtil

        // Select
        SqlSelect sqlSelect = ((SqlSelect) sqlNode);
        SqlNodeList sqlNodeList = sqlSelect.getSelectList();
        // JOIN
        SqlKind sqlKind = sqlSelect.getFrom().getKind();
        SqlJoin sqlJoin = (SqlJoin) sqlSelect.getFrom();
        System.out.println(String.format("sqlJoin.getJoinType().name() = %s, \nsqlJoin.getLeft() = %s, \nsqlJoin.getRight() = %s", sqlJoin.getJoinType().name(), sqlJoin.getLeft(), sqlJoin.getRight()));

        JamesUtil.printDivider();
        System.out.println(sqlNodeList);
        JamesUtil.printDivider();
        System.out.println(sqlKind);
        JamesUtil.printDivider();

        JamesUtil.printDivider();
        SqlNode left = sqlJoin.getLeft();
        SqlNode right = sqlJoin.getRight();
        System.out.println(left.getKind());

        System.out.println(" -------------------------------- ");
        List<SqlNode> sqlNodes = ((SqlCall) left).getOperandList();
        System.out.println(sqlNodes.size());
        for (SqlNode node : sqlNodes) {
            System.out.println(node.getKind() + " -> " + node.toString());
            System.out.println();
        }
        System.out.println(" -------------------------------- ");
        System.out.println(sqlNodes);

        JamesUtil.printDivider();


//        SqlNode nodeInsert = ((SqlInsert) CalciteParser.parse(sql)).getTargetColumnList();
//        System.out.println("nodeInsert:\n" + nodeInsert);
//        JamesUtil.printDivider();

//        SqlValidatorImpl validator=new JamesSqlValidator((SqlValidatorImpl) SqlValidatorUtil
//                .newValidator(sqlOperatorTable, catalogReader, javaTypeFactory(), connectionConfig.conformance()));


//        Frameworks.ConfigBuilder configBuilder = Frameworks.newConfigBuilder();
//        SchemaPlus schemaPlus = Frameworks.createRootSchema(true);
//        schemaPlus.add("T", new ReflectiveSchema(new TestSchema()));
//        configBuilder.defaultSchema(schemaPlus);
//        FrameworkConfig frameworkConfig = configBuilder.build();
//        Planner planner = Frameworks.getPlanner(frameworkConfig);
//        RelRoot relRoot = planner.rel(node2);
//        relRoot.rel.getRowType();
//        RelNode relNode = relRoot.project();
//        System.out.print(RelOptUtil.toString(relNode));
//        JamesUtil.printDivider();
//
//        SqlNode node3 = ((SqlSelect) CalciteParser.parse(sql)).getFetch();
//        System.out.println("node3:\n" + node3);
//        JamesUtil.printDivider();
//
//        SqlNode node4 = ((SqlSelect) CalciteParser.parse(sql)).getWhere();
//        System.out.println("node4:\n" + node4);
//        JamesUtil.printDivider();
//
//        SqlNode node5 = ((SqlSelect) CalciteParser.parse(sql)).getWindowList();
//        System.out.println("node5:\n" + node5);
//        JamesUtil.printDivider();
//
//        List<SqlNode> operList = ((SqlSelect) CalciteParser.parse(sql)).getOperandList();
//        System.out.println("operList:\n");
//        for (SqlNode oper : operList) {
//            System.out.println("\t" + oper);
//        }
//        JamesUtil.printDivider();
    }
}
