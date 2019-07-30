package com.github.jferard.fastods.attribute;

/**
 * 19.686.2<table:data-pilot-field>
 *
 * @author J. Férard
 */
public enum FieldOrientation implements AttributeValue {
    /**
     * field specifies a category column
     */
    COLUMN("column"),

    /**
     * field specifies a data column
     */
    DATA("data"),

    /**
     * field has a corresponding column in a data pilot's source but are not visible in the data
     * pilot table.
     */
    HIDDEN("hidden"),

    /**
     * specifies that an automatic filter (one that allows to choose one of the values that are
     * contained in the column) should be generated for the corresponding column. In that case,
     * an additional field with row, column or data orientation shall exist for the column. The
     * table:selected-page attribute specifies which value is selected for the filter.
     */
    PAGE("page"),

    /**
     * specifies a category row
     */
    ROW("row");

    private final String attr;

    /**
     * @param attr the attribute
     */
    FieldOrientation(final String attr) {
        this.attr = attr;
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }

    @Override
    public CharSequence getValue() {
        return this.attr;
    }
}
