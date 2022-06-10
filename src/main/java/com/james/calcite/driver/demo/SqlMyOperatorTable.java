package com.james.calcite.driver.demo;

import org.apache.calcite.sql.SqlFunction;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;

/**
 * <p>标题: </p>
 * <p>功能描述: </p>
 *
 *
 * <p>创建时间: 2021/3/6 14:05</p>
 * <p>作者：test</p>
 * <p>修改历史记录：</p>
 * ====================================================================<br>
 **/
public class SqlMyOperatorTable extends SqlStdOperatorTable {

    private static SqlMyOperatorTable instance;

    public static final SqlFunction TO_DATE = new SqlToDateFunction();

    public static synchronized SqlMyOperatorTable instance() {
        if (instance == null) {
            // Creates and initializes the standard operator table.
            // Uses two-phase construction, because we can't initialize the
            // table until the constructor of the sub-class has completed.
            instance = new SqlMyOperatorTable();
            instance.init();
        }

        return instance;
    }
}
