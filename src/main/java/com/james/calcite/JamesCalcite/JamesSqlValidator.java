package com.james.calcite.JamesCalcite;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.validate.SqlValidatorImpl;

public class JamesSqlValidator extends SqlValidatorImpl {
    public JamesSqlValidator(SqlValidatorImpl calciteSqlValidator) {
        super(calciteSqlValidator.getOperatorTable(), calciteSqlValidator.getCatalogReader(),
                calciteSqlValidator.getTypeFactory(), calciteSqlValidator.config());
    }

    @Override
    protected RelDataType getLogicalSourceRowType(RelDataType sourceRowType, SqlInsert insert) {
        final RelDataType superType = super.getLogicalSourceRowType(sourceRowType, insert);
        return ((JavaTypeFactory) typeFactory).toSql(superType);
    }

    @Override
    protected RelDataType getLogicalTargetRowType(RelDataType targetRowType, SqlInsert insert) {
        final RelDataType superType = super.getLogicalTargetRowType(targetRowType, insert);
        return ((JavaTypeFactory) typeFactory).toSql(superType);
    }
}
