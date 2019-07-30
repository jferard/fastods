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
    BOOLEAN("office:boolean-value", "boolean"),
    /**
     * a currency: "Numeric value and currency symbol"
     */
    CURRENCY("office:value", "currency"),
    /**
     * a date: "Date value as specified in §3.2.9 of [xmlschema-2], or date and time value as
     * specified in §3.2.7 of [xmlschema-2]"
     */
    DATE("office:date-value", "date"),
    /**
     * a float: "Numeric value"
     */
    FLOAT("office:value", "float"),
    /**
     * a percentage: "Numeric value"
     */
    PERCENTAGE("office:value", "percentage"),
    /**
     * a string: "String"
     */
    STRING("office:string-value", "string"),
    /**
     * a time: "Duration, as specified in §3.2.6 of [xmlschema-2]"
     */
    TIME("office:time-value", "time"),
    /**
     * a void cell value: nothing.
     */
    VOID("office-value", "");

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
     * @return the value type
     */
    public String getValueType() {
        return this.valueType;
    }

    /**
     * @return the value attribute
     */
    @Override
    public String getValue() {
        return this.valueAttribute;
    }
}
