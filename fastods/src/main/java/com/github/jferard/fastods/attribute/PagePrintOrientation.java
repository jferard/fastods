package com.github.jferard.fastods.attribute;

/**
 * 20.325style:print-orientation
 * The print orientation of the page (either landscape or portrait)
 */
public enum PagePrintOrientation implements AttributeValue {
    /**
     * "a page is printed in landscape orientation"
     */
    HORIZONTAL("landscape"),
    /**
     * "a page is printed in portrait orientation"
     */
    VERTICAL("portrait");

    private final String attrValue;

    /**
     * @param attrValue landscape|portrait
     */
    PagePrintOrientation(final String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * @return landscape|portrait
     */
    @Override
    public String getValue() {
        return this.attrValue;
    }
}
