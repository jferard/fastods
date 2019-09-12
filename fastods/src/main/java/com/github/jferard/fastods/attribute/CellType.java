package com.github.jferard.fastods.attribute;

import com.github.jferard.fastods.attribute.AttributeValue;

/**
 * 19.385 office:value-type.
 * Javadoc text below is taken from Open Document Format for Office Applications
 * (OpenDocument) Version 1.2
 */
public enum CellType implements AttributeValue {
    /**
     * a boolean: "true or false"
     */
    BOOLEAN("boolean", "office:boolean-value"),
    /**
     * a currency: "Numeric value and currency symbol"
     */
    CURRENCY("currency", "office:value"),
    /**
     * a date: "Date value as specified in §3.2.9 of [xmlschema-2], or date and time value as
     * specified in §3.2.7 of [xmlschema-2]"
     */
    DATE("date", "office:date-value"),
    /**
     * a float: "Numeric value"
     */
    FLOAT("float", "office:value"),
    /**
     * a percentage: "Numeric value"
     */
    PERCENTAGE("percentage", "office:value"),
    /**
     * a string: "String"
     */
    STRING("string", "office:string-value"),
    /**
     * a time: "Duration, as specified in §3.2.6 of [xmlschema-2]"
     */
    TIME("time", "office:time-value"),
    /**
     * a void cell value: nothing.
     */
    VOID("", "office-value");

    private final String valueType;
    private final String valueAttribute;

    /**
     * @param valueType      the value type. Will produce office:value-type="float"
     * @param valueAttribute the value attribute. Will store the value as in:
     *                       office:string-value="xyz".
     */
    CellType(final String valueType, final String valueAttribute) {
        this.valueAttribute = valueAttribute;
        this.valueType = valueType;
    }

    /**
     * @return the value attribute ("office:xxx-value")
     */
    public String getValueAttribute() {
        return this.valueAttribute;
    }

    /**
     * @return the value type attribute ("float", ...)
     */
    @Override
    public String getValue() {
        return this.valueType;
    }
}
