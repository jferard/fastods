package com.github.jferard.fastods.attribute;

/**
 * An horizontal alignment.
 * 20.216 fo:text-align. See https://www.w3.org/TR/2001/REC-xsl-20011015/slice7.html#text-align
 */
public enum CellAlign implements AttributeValue {
    /**
     * The text is centered
     */
    CENTER("center"),

    /**
     * The text is justified
     */
    JUSTIFY("justify"),

    /**
     * The text is left aligned
     */
    LEFT("start"),

    /**
     * The text is right aligned
     */
    RIGHT("end");

    private final String attrValue;

    /**
     * A new horizontal align
     *
     * @param attrValue the align attribute
     */
    CellAlign(final String attrValue) {
        this.attrValue = attrValue;
    }

    @Override
    public String getValue() {
        return this.attrValue;
    }
}
