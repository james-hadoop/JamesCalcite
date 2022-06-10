package com.james.calcite.driver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.james.calcite.JamesCalcite.CalciteParser;
import com.james.calcite.JamesCalcite.JamesCalciteConfig;
import com.james.calcite.JamesCalcite.JamesSqlValidator;
import com.james.calcite.JamesCalcite.Pair;
import com.james.calcite.kylin.SQLConverter;
import com.james.calcite.utils.JamesUtil;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.util.SqlBasicVisitor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.calcite.sql.SqlDialect.EMPTY_CONTEXT;

public class SqlNodeExtractor extends SqlBasicVisitor<SqlNode> {
    final static String sql_11 = "SELECT id, text FROM user_behavior WHERE SOURCE = 1 AND action = 2 AND (text LIKE 'com.autonavi.minimap' OR text LIKE 'com.baidu.BaiduMap')";

    final static String sql_1 = "SELECT a, b FROM t_a WHERE a > 10 GROUP BY a, b";


    private List<SqlIdentifier> allSqlIdentifier = Lists.newArrayList();

    public static List<SqlIdentifier> getAllSqlIdentifier(String sql) throws SqlParseException {
        SqlNode parsed = CalciteParser.parse(sql);
        SqlNodeExtractor sqlNodeExtractor = new SqlNodeExtractor();
        parsed.accept(sqlNodeExtractor);
        return sqlNodeExtractor.allSqlIdentifier;
    }

    public static Map<SqlIdentifier, Pair<Integer, Integer>> getIdentifierPos(String sql) throws SqlParseException {
        List<SqlIdentifier> identifiers = getAllSqlIdentifier(sql);
        Map<SqlIdentifier, Pair<Integer, Integer>> identifierAndPositionMap = Maps.newHashMap();
        for (SqlIdentifier identifier : identifiers) {
            Pair<Integer, Integer> identifyPosition = CalciteParser.getReplacePos(identifier, sql);
            identifierAndPositionMap.put(identifier, identifyPosition);
        }
        return identifierAndPositionMap;
    }

    @Override
    public SqlNode visit(SqlIdentifier id) {
        allSqlIdentifier.add(id);
        return null;
    }

    public static void main(String[] args) throws SqlParseException {
        String sql = sql_1;

        SqlParser.Config parserConfig = SqlParser.config().withIdentifierMaxLength(50);

        SqlParser parser = SqlParser.create(sql, parserConfig);

        SqlNode sqlNode = parser.parseQuery();
        String sqlNodeStr = sqlNode.toSqlString(new SqlDialect(EMPTY_CONTEXT.withCaseSensitive(false)
                .withDatabaseProductName(SqlDialect.DatabaseProduct.HIVE.name())))
//                .withIdentifierQuoteString("\"")))
                .toString();
        System.out.println(String.format("sqlNodeStr = \n%s", sqlNodeStr));

        SqlParserPos sqlParserPos = sqlNode.getParserPosition();
        System.out.println(String.format("sqlParserPos = %s", sqlParserPos));

        SqlKind sqlKind = sqlNode.getKind();
        System.out.println(String.format("sqlKind = %s", sqlKind));

        JamesUtil.printDivider();
        List<SqlNode> sqlNodeList = ((SqlSelect) sqlNode).getSelectList().getList();
        for (SqlNode node : sqlNodeList) {
            System.out.println(node.toSqlString(new SqlDialect(EMPTY_CONTEXT.withCaseSensitive(false)
                    .withDatabaseProductName(SqlDialect.DatabaseProduct.HIVE.name())
                    .withIdentifierQuoteString("\""))).toString());
        }

        JamesCalciteConfig config = JamesCalciteConfig.current();
        CalciteSchema rootSchema = CalciteSchema.createRootSchema(true);

//        JavaTypeFactory javaTypeFactory = new JavaTypeFactoryImpl();
//        CalciteCatalogReader catalogReader = new CalciteCatalogReader(rootSchema, Collections.singletonList(defaultSchemaName), javaTypeFactory,
//                config);
//
//        VolcanoPlanner planner = new VolcanoPlanner();
//        SQLConverter sqlConverter = SQLConverter.createConverter(config, planner, catalogReader);
//        JamesSqlValidator sqlValidator = sqlConverter;


//        JamesUtil.printDivider();
//        SqlAbstractParserImpl.Metadata meta = parser.getMetadata();
//        String jdbcKeywords = meta.getJdbcKeywords();
//        System.out.println(String.format("jdbcKeywords = %s", jdbcKeywords));
//        for (String token : meta.getTokens()) {
//            System.out.println(String.format("token = %s", token));
//        }

//        for(SqlNode node : nodes){
//            SqlSelect selectNode = (SqlSelect)node;
//            System.out.println(selectNode);
//        }

//        Map<SqlIdentifier, Pair<Integer, Integer>> map = getIdentifierPos(sql);
//
//        for (Map.Entry<SqlIdentifier, Pair<Integer, Integer>> entry : map.entrySet()) {
//            SqlIdentifier id = entry.getKey();
//            Pair<Integer, Integer> pair = entry.getValue();
//
//            System.out.println(id + "\t" + id.getParserPosition());
//            System.out.println("\t" + pair.getFirst() + " -> " + pair.getSecond());
//        }

    }
}
