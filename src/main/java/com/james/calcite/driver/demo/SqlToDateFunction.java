package com.james.calcite.driver.demo;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.type.*;
import org.apache.calcite.sql.validate.SqlMonotonicity;
import org.apache.calcite.sql.validate.SqlValidator;

/**
 * <p>标题: </p>
 * <p>功能描述: </p>
 *
 *
 * <p>创建时间: 2021/3/6 13:42</p>
 * <p>作者：test</p>
 * <p>修改历史记录：</p>
 * ====================================================================<br>
 **/
public class SqlToDateFunction extends SqlFunction {
    //~ Constructors -----------------------------------------------------------

    public SqlToDateFunction() {
        super(
                "TO_DATE",
                SqlKind.OTHER_FUNCTION,
                ReturnTypes.DATE,
                null,
                OperandTypes.ANY,
                SqlFunctionCategory.TIMEDATE);
    }

    //~ Methods ----------------------------------------------------------------

    public SqlSyntax getSyntax() {
        return SqlSyntax.FUNCTION_ID;
    }

    @Override public SqlMonotonicity getMonotonicity(SqlOperatorBinding call) {
        return SqlMonotonicity.INCREASING;
    }

    // Plans referencing context variables should never be cached
    public boolean isDynamicFunction() {
        return true;
    }

    @Override
    public SqlOperandCountRange getOperandCountRange() {
        /**
         * 返回参数个数信息
         **/
        return SqlOperandCountRanges.of(2);
    }

    @Override
    protected void checkOperandCount(SqlValidator validator, SqlOperandTypeChecker argType, SqlCall call) {
        /**
         * 参数个数校验
         **/
        assert call.operandCount() == 2;
    }
}