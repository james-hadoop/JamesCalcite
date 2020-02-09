package com.james.calcite.JamesCalcite;

import java.util.List;

import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.tools.RelConversionException;
import org.apache.calcite.tools.ValidationException;

import com.james.common.util.JamesUtil;

public class SqlToRelConverterDemo {
    public static class TestSchema {
        public final T_A[] t_a = { new T_A("key", "a_a", "a_b", "a_c", "same") };
        public final T_B[] t_b = { new T_B("key", "b_a", "b_b", "b_c", "same") };
        public final TT[] tt = { new TT("", "", "", "") };
    }

    public static final String sql1 = "INSERT INTO TABLE f_tt SELECT at_a.a_a AS f_a_a, at_b.b_b AS f_b_b, at_b.b_c AS f_b_c FROM(SELECT a_key, MAX(a_a) AS a_a, MAX(a_b) AS a_b, MAX(a_c) AS a_c FROM t_a WHERE a_c = 3 GROUP BY a_key ORDER BY a_a) at_a LEFT JOIN (SELECT b_key, MAX(b_a) AS b_a, MAX(b_b) AS b_b, MAX(b_c) AS b_c FROM t_b GROUP BY b_key ORDER BY b_b) at_b ON at_a.a_key = at_b.b_key";

    private final static String sql91 = "insert into table t_dwd_asn_video_audit_submit_daily select 20190430 as dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, f_claim_duration, f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, data_src, audit_pool from (select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_pgc_submit' as data_src, '视频pgc安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_pgc_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_mcn_submit' as data_src, '视频mcn安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_mcn_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_other_submit' as data_src, '视频其他安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_other_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_ugc_submit' as data_src, '视频ugc安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_ugc_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_green_submit' as data_src, '视频绿色通道安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_green_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_remarkonly_submit' as data_src, '视频标注审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_remarkonly_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_enableremark_submit' as data_src, '视频启标审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_enableremark_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_pop_video_top_submit' as data_src, '视频复核领单_爆款池' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_pop_video_top_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_pop_video_submit' as data_src, '视频复核领单_其他审核池' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_pop_video_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430) t";
    private final static String sql92 = "INSERT INTO table t_dws_asn_video_audit_submit_dim_f_staff_id_and_audit_pool_daily SELECT 20190503 AS dt, f_staff_id, audit_pool, COUNT(DISTINCT CASE  WHEN f_audit_status = 1 THEN f_rowkey END) AS audit_passed_cnt , COUNT(DISTINCT CASE  WHEN f_audit_status = 0 THEN f_rowkey END) AS audit_refused_cnt, COUNT(DISTINCT CASE  WHEN f_audit_status IN (0, 1) THEN f_rowkey END) AS audit_cnt , SUM(f_claim_duration) AS claim_duration, SUM(f_audit_duration) AS audit_duration FROM sng_mp_etldata.t_dwd_asn_video_audit_submit_daily WHERE dt = 20190503 GROUP BY f_staff_id, audit_pool";

    
    public static final String sql2 = "SELECT \"tt\".\"a_a\" AS \"f_a_a\",\n" + "       \"tt\".\"b_b\" AS \"f_b_b\",\n"
            + "       \"tt\".\"b_c\" AS \"f_b_c\",\n" + "       \"at_b\".\"b_same\"\n" + "FROM\n"
            + "  (SELECT \"a_key\",\n" + "          MAX(\"a_a\") AS \"a_a\",\n" + "          MAX(\"a_b\") AS \"a_b\",\n"
            + "          MAX(\"a_c\") AS \"a_c\",\n" + "          MAX(\"same\") AS \"same\"\n"
            + "   FROM \"T\".\"t_a\"\n" + "   WHERE \"a_c\" = 3\n" + "   GROUP BY \"a_key\"\n"
            + "   ORDER BY \"a_a\") \"at_a\"\n" + "LEFT JOIN\n" + "  (SELECT \"b_key\",\n"
            + "          MAX(\"b_a\") AS \"b_a\",\n" + "          MAX(\"b_b\") AS \"b_b\",\n"
            + "          MAX(\"b_c\") AS \"b_c\",\n" + "          MAX(\"same\") AS \"b_same\"\n"
            + "   FROM \"T\".\"t_b\"\n" + "   GROUP BY \"b_key\"\n"
            + "   ORDER BY \"b_b\") \"at_b\" ON \"at_a\".\"a_key\" = \"at_b\".\"b_key\"";

    public static final String sql3 = "SELECT \"a_a\",\n" + "       \"b_b\",\n" + "       \"b_c\" \n" + "FROM\n"
            + "\"T\".\"t_a\" \"at_a\"\n" + "LEFT JOIN\n"
            + "\"T\".\"t_b\" \"at_b\" ON \"at_a\".\"a_key\" = \"at_b\".\"b_key\"";

    public static final String sql4="INSERT INTO TABLE t_kandian_account_video_uv_daily_new SELECT 20190226, 'aaaaa' AS s_a, C.puin puin , C.row_key , CASE WHEN SOURCE IN('1' ,'3') THEN 1 ELSE 0 END AS is_kd_source , CASE WHEN SOURCE='hello' THEN 1 ELSE 0 END AS s_kd_source , uv, vv a_vv, c.uv c_uv, d.puin d_puin FROM(SELECT puin , A.row_key , COUNT(DISTINCT A.cuin) AS uv , SUM(A.vv) AS vv FROM (SELECT case when rowkey=\\\"AAA\\\" then 'yes' else 'no' end rettt,cuin , business_id AS puin , op_cnt AS vv , rowkey AS row_key , RANK() OVER (PARTITION BY rowkey ORDER BY ftime) AS f_rank FROM sng_cp_fact.v_ty_audit_all_video_play_basic_info_check_clean WHERE fdate = 20190226 AND score < 80 AND dis_platform = 1 AND op_type = 3 AND op_cnt > 0 AND LENGTH(rowkey) = 16 AND SUBSTR(rowkey, 15, 2) IN ('ab' , 'ae' , 'af' , 'aj' , 'al' , 'ao') AND play_time>0 AND play_time/1000 BETWEEN 0 AND 3600 AND video_length>0 AND video_length/1000 BETWEEN 1 AND 7200 AND ((play_time / video_length > 0.6 AND video_length < 21000) OR (play_time > 10000 AND video_length > 20000)) AND business_id > 100) A LEFT JOIN (SELECT MAX(fdate) AS tdbank_imp_date , rowkey AS row_key , SUM(op_cnt) AS history_vv FROM sng_cp_fact.v_ty_BBBB WHERE fdate BETWEEN DATE_SUB(20190226, 90) AND DATE_SUB(20190226, 1) AND score < 80 AND dis_platform = 1 AND op_type = 3 AND op_cnt > 0 AND LENGTH(rowkey) = 16 AND SUBSTR(rowkey, 15, 2) IN ('ab' , 'ae' , 'af' , 'aj' , 'al' , 'ao') AND play_time>0 AND play_time/1000 BETWEEN 0 AND 3600 AND video_length>0 AND video_length/1000 BETWEEN 1 AND 7200 AND ((play_time / video_length > 0.6 AND video_length < 21000) OR (play_time > 10000 AND video_length > 20000)) AND business_id > 100 GROUP BY rowkey) B ON A.row_key = B.row_key WHERE ((B.history_vv IS NOT NULL AND f_rank < (3000001 - B.history_vv)) OR (f_rank < 3000001 AND B.history_vv IS NULL)) GROUP BY A.puin , A.row_key) C LEFT JOIN (SELECT puin , row_key , CASE WHEN GET_JSON_OBJECT(MAX(extra_info), '$.store_type') IS NOT NULL THEN GET_JSON_OBJECT(MAX(extra_info), '$.store_type') ELSE GET_JSON_OBJECT(MAX(extra_info), '$.src') END AS SOURCE FROM sng_tdbank . cc_dsl_content_center_rpt_fdt0 WHERE tdbank_imp_date BETWEEN DATE_SUB(20190226, 90) AND 20190226 AND op_type = '0XCC0V000' AND GET_JSON_OBJECT(extra_info, '$.renewal') NOT IN ('1') AND src IN ('2' , '5' , '6' , '10' , '12' , '15') GROUP BY puin , row_key) D ON C.row_key = D.row_key";

    public static void main(String[] args) throws SqlParseException, RelConversionException, ValidationException {
        String sql = sql3;
        System.out.println(sql);

        SchemaPlus schemaPlus = Frameworks.createRootSchema(true);

        // 给schema T中添加表
        schemaPlus.add("T", new ReflectiveSchema(new TestSchema()));

        // Planner
        SchemaPlus defaultSchema = Frameworks.createRootSchema(false);
        FrameworkConfig frameworkConfig = Frameworks.newConfigBuilder().defaultSchema(schemaPlus).build();
        Planner planner = Frameworks.getPlanner(frameworkConfig);
        SqlNode sqlNode = planner.parse(sql);

        SqlNode sqlNodeValidated = planner.validate(sqlNode);

        RelRoot relRoot = planner.rel(sqlNode);
        RelNode relNode = relRoot.project();
        System.out.print(RelOptUtil.toString(relNode));

        JamesUtil.printDivider();
        List<String> fields = relNode.getRowType().getFieldNames();
        JamesUtil.printList(fields);

        List<String> tables = null == relNode.getTable() ? null : relNode.getTable().getQualifiedName();
        JamesUtil.printList(tables);
        
        System.out.println(relNode.getDescription());
        System.out.println(relNode.getDigest());
        System.out.println(relNode.getCorrelVariable());
        System.out.println(relNode.getRelTypeName());

        // Validator
//        SqlValidatorUtil.newValidator(opTab, catalogReader, typeFactory);

//        // CalciteCatalogReader
//        CalcitePrepare context=
//        CalciteCatalogReader reader=new CalciteCatalogReader();

        // Cluster
//        VolcanoPlanner planner1 = new VolcanoPlanner();
//        RexBuilder rexBuilder = new RexBuilder(new JavaTypeFactoryImpl());
//        RelOptCluster cluster = RelOptCluster.create(planner1, rexBuilder);

        // SqlToRelConverter
//        final SqlToRelConverter.Config config = SqlToRelConverter.configBuilder()
//                .withConfig(SqlToRelConverter.Config.DEFAULT).withTrimUnusedFields(false).withConvertTableAccess(false)
//                .build();

//        final SqlToRelConverter sqlToRelConverter = new SqlToRelConverter(planner, null,
//                createCatalogReader(), cluster, StandardConvertletTable.INSTANCE, config);
    }

}
