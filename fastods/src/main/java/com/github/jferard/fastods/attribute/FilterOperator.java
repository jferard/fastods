package com.github.jferard.fastods.attribute;

/**
 * 19.684table:operator
 */
public enum FilterOperator implements AttributeValue {
    /**
     * matches
     */
    MATCHES("match"),

    /**
     * does not match
     */
    N_MATCH("!match"),

    /**
     * Equal to
     */
    EQ("="),

    /**
     * Not equal to
     */
    N_EQ("!="),

    /**
     * Less than
     */
    LT("<"),

    /**
     * Greater than
     */
    GT(">"),

    /**
     * Less than or equal to
     */
    LTE("<="),

    /**
     * Greater than or equal to
     */
    GTE(">="),

    /**
     * begins with
     */
    BEGINS("begins"),

    /**
     * contains
     */
    CONTAINS("contains"),

    /**
     * does not contain
     */
    N_CONTAINS("!contains"),

    /**
     * ends with
     */
    ENDS("ends"),

    /**
     * does not begin with
     */
    N_BEGINS("!begins"),

    /**
     * does not end with
     */
    N_ENDS("!ends"),

    /**
     * like bottom values, except that the office:value attribute specifies the number of
     * cells for which the condition is true as a percentage
     */
    BOTTOM_PERCENT("bottom percent"),

    /**
     * true for the n cells that have the smallest value, where n is the value of the
     * office:value attribute
     */
    BOTTOM_VALUES("bottom values"),

    /**
     * true for empty cells
     */
    EMPTY("empty"),

    /**
     * true for non-empty cells
     */
    N_EMPTY("!empty"),

    /**
     * like bottom percent, but for the largest values
     */
    TOP_PERCENT("top percent"),

    /**
     * like bottom values, but for the largest values
     */
    TOP_VALUES("top values");

    private final String operatorValue;

    FilterOperator(final String operatorValue) {
        this.operatorValue = operatorValue;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public String getValue() {
        return this.operatorValue;
    }
}
