package com.james.calcite.JamesCalcite;

import org.apache.calcite.adapter.java.ReflectiveSchema;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;

/**
 * Created with calcite-test.
 * User: hzyuqi1
 * Date: 2017/7/14
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */

public class TestOne {
    private final static String sql91 = "insert into table t_dwd_asn_video_audit_submit_daily select 20190430 as dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, f_claim_duration, f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, data_src, audit_pool from (select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_pgc_submit' as data_src, '视频pgc安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_pgc_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_mcn_submit' as data_src, '视频mcn安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_mcn_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_other_submit' as data_src, '视频其他安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_other_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_ugc_submit' as data_src, '视频ugc安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_ugc_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_green_submit' as data_src, '视频绿色通道安全审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_green_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_remarkonly_submit' as data_src, '视频标注审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_remarkonly_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_enableremark_submit' as data_src, '视频启标审核' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_enableremark_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_pop_video_top_submit' as data_src, '视频复核领单_爆款池' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_pop_video_top_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430 union all select dt, f_rowkey, f_create_time, f_update_time, f_init_time, f_claim_time, unix_timestamp(f_claim_time)-unix_timestamp(f_init_time) as f_claim_duration, unix_timestamp(f_update_time)-unix_timestamp(f_claim_time) as f_audit_duration, f_type, f_review_type, f_status, f_audit_status, f_source, f_sec_source, f_chann_id, f_sec_chann_id, f_trd_chann_id, f_staff_id, f_account_id, f_account_name, f_title, f_url, 't_ods_asn_video_audit_pop_video_submit' as data_src, '视频复核领单_其他审核池' as audit_pool from sng_mp_etldata.t_ods_asn_video_audit_pop_video_submit where dt=20190430 and cast(to_char(f_update_time,'yyyymmdd') as bigint) = 20190430) t";


    public static class TestSchema {
        public final Triple[] rdf = {new Triple("s", "p", "o")};

      //  public final Triple rdf = new Triple("s", "p", "o");
    }

    public static void main(String[] args) {
        SchemaPlus schemaPlus = Frameworks.createRootSchema(true);

        schemaPlus.add("T", new ReflectiveSchema(new TestSchema()));
        Frameworks.ConfigBuilder configBuilder = Frameworks.newConfigBuilder();
        configBuilder.defaultSchema(schemaPlus);

        FrameworkConfig frameworkConfig = configBuilder.build();

        SqlParser.ConfigBuilder paresrConfig = SqlParser.configBuilder(frameworkConfig.getParserConfig());

        paresrConfig.setCaseSensitive(false).setConfig(paresrConfig.build());

        Planner planner = Frameworks.getPlanner(frameworkConfig);

        SqlNode sqlNode = null;
        RelRoot relRoot = null;
        try {
//            sqlNode = planner.parse("select * from \"s\".\"rdf\" \"a\", \"s\".\"rdf\" \"b\"" +
//                    "where \"a\".\"s\" = 5 and \"b\".\"s\" = 5 limit 5, 1000");


            //sqlNode = planner.parse("select \"a\".\"s\", count(\"a\".\"s\") from \"T\".\"rdf\" \"a\" group by \"a\".\"s\"");
//            sqlNode = planner.parse("select distinct cast(\"a\".\"s\" as INT) from \"T\".\"rdf\" \"a\"");
            sqlNode = planner.parse(sql91);
            
            planner.validate(sqlNode);
            relRoot = planner.rel(sqlNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RelNode relNode = relRoot.project();
        System.out.print(RelOptUtil.toString(relNode));
    }
}
