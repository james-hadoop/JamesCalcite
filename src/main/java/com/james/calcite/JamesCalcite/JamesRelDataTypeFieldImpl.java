package com.james.calcite.JamesCalcite;

import lombok.Getter;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFieldImpl;

public class JamesRelDataTypeFieldImpl extends RelDataTypeFieldImpl {

    enum ColumnType {
        CC_FIELD, ORIGIN_FILED, PRE_CALC_FIELD
    }

    @Getter
    private ColumnType columnType;

    public JamesRelDataTypeFieldImpl(String name, int index, RelDataType type, ColumnType fieldType) {
        super(name, index, type);
        this.columnType = fieldType;
    }
}