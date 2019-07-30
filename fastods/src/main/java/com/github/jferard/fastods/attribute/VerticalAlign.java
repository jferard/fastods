package com.github.jferard.fastods.attribute;

import com.github.jferard.fastods.attribute.AttributeValue;

/**
 * A vertical alignment
 * 20.386 style:vertical-align
 */
public enum VerticalAlign implements AttributeValue {
    /**
     * "bottom: to the bottom of the line."
     */
    BOTTOM("bottom"),

    /**
     * "middle: to the center of the line."
     */
    MIDDLE("middle"),

    /**
     * "top: to the top of the line."
     */
    TOP("top");

    private final String attrValue;

    /**
     * A new vertical align
     *
     * @param attrValue the align attribute
     */
    VerticalAlign(final String attrValue) {
        this.attrValue = attrValue;
    }

    @Override
    public CharSequence getValue() {
        return this.attrValue;
    }
}
