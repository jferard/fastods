package com.github.jferard.fastods.attribute;


/**
 * 19.643table:function
 *
 * @author J. Férard
 */
public enum PilotStandardFunction implements PilotFunction {
    /**
     * the average of all numeric values.
     */
    AVERAGE("average"),

    /**
     * the count of all non-empty values, including text.
     */
    COUNT("count"),

    /**
     * the count of all numeric values.
     */
    COUNT_NUMS("countnums"),

    /**
     * the maximum of all numeric values.
     */
    MAX("max"),

    /**
     * the minimum of all numeric values.
     */
    MIN("min"),

    /**
     * the product of all numeric values.
     */
    PRODUCT("product"),

    /**
     * the standard deviation, treating all numeric values as a sample from a population.
     */
    ST_DEV("stdev"),

    /**
     * the standard deviation, treating all numeric values as a whole population.
     */
    ST_DEVP("stdevp"),

    /**
     * the sum of all numeric values.
     */
    SUM("sum"),

    /**
     * the variance, treating all numeric values as a sample from a population.
     */
    VAR("var"),

    /**
     * the variance, treating all numeric values as a whole population.
     */
    VARP("varp"),

    /**
     * no function is applied to that category field.
     */
    AUTO("auto");

    private final String attr;

    /**
     * @param attr the attribute
     */
    PilotStandardFunction(final String attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public String getValue() {
        return this.attr;
    }
}
