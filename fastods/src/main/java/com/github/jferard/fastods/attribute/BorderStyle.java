package com.github.jferard.fastods.attribute;

/**
 * Extensible Stylesheet Language (XSL)
 * Version 1.0, 7.7.20 "border-top-style"
 * <p>
 * The style of the border
 */
public enum BorderStyle implements AttributeValue {
    /**
     * No border
     */
    NONE("none"),

    /**
     * Same as 'none', with a little exception
     */
    HIDDEN("hidden"),

    /**
     * Series of dots
     */
    DOTTED("dotted"),

    /**
     * Series of dashes
     */
    DASHED("dashed"),

    /**
     * Solid border
     */
    SOLID("solid"),

    /**
     * Double lined border
     */
    DOUBLE("double"),

    /**
     * Carved in the canvas
     */
    GROOVE("groove"),

    /**
     * Coming out of the canvas
     */
    RIDGE("ridge"),

    /**
     * Box carved in the canvas
     */
    INSET("inset"),

    /**
     * Box coming out of the canvas
     */
    OUTSET("outset");

    private final String attrValue;

    BorderStyle(final String attrValue) {
        this.attrValue = attrValue;
    }


    /**
     * @return the value of the attribute for XML use
     */
    @Override
    public String getValue() {
        return this.attrValue;
    }
}
