package com.james.calcite.JamesCalcite;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.config.CalciteConnectionProperty;
import org.apache.calcite.config.NullCollation;

import java.util.Locale;
import java.util.Properties;

/**
 * wrapper for calcite configs defined in kylin props
 */
public class JamesCalciteConfig extends CalciteConnectionConfigImpl {

    static final ThreadLocal<JamesCalciteConfig> THREAD_LOCAL = new ThreadLocal<>();

    private JamesCalciteConfig(Properties properties) {
        super(properties);
    }

    @Override
    public NullCollation defaultNullCollation() {
        return NullCollation.LOW;
    }

    @Override
    public boolean caseSensitive() {
        return false;
    }

    @Override
    public Casing unquotedCasing() {
        return Casing.UNCHANGED;
    }

    public int getIdentifierMaxLength() {
        return 500;
    }

    public String[] operatorTables() {
        return CalciteConnectionProperty.FUN.wrap(properties).getString("standard").toLowerCase(Locale.ROOT).split(",");
    }

    public static JamesCalciteConfig current() {
        return THREAD_LOCAL.get();
    }
}
