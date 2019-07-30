package com.github.jferard.fastods.attribute;

/**
 * 20.394 style:writing-mode
 * see See §7.27.7 of [XSL] (https://www.w3.org/TR/2001/REC-xsl-20011015/slice7
 * .html#writing-mode-related)
 */
public enum PageWritingMode implements AttributeValue {
    /**
     * "Shorthand for lr-tb"
     */
    LR("lr"),
    /**
     * left to right then top to bottom
     */
    LRTB("lr-tb"),
    /**
     * page means inherit
     */
    PAGE("page"),
    /**
     * "Shorthand for rl-tb"
     */
    RL("rl"),
    /**
     * right to left then top to bottom
     */
    RLTB("rl-tb"),
    /**
     * "Shorthand for tb-rl"
     */
    TB("tb"),
    /**
     * top to bottom then left to right
     */
    TBLR("tb-lr"),
    /**
     * top to bottom then right to left
     */
    TBRL("tb-rl");

    private final String attrValue;

    /**
     * @param attrValue the value See §7.27.7 of [XSL]
     */
    PageWritingMode(final String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * @return the value See §7.27.7 of [XSL]
     */
    @Override
    public String getValue() {
        return this.attrValue;
    }

}
