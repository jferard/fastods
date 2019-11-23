package com.github.jferard.fastods.odselement;

import java.util.Locale;

/**
 * 19.338 meta:value-type
 */
public enum MetaValueType {
    /** 18.3.3 boolean */
    BOOLEAN,

    /** 18.2W3C Schema Datatypes - date */
    DATE,

    /** 18.2W3C Schema Datatypes - float */
    FLOAT,

    /** 18.2W3C Schema Datatypes - time */
    TIME,

    /** 18.2W3C Schema Datatypes - string */
    STRING;

    public String getValue() {
        return this.toString().toLowerCase(Locale.US);
    }
}
