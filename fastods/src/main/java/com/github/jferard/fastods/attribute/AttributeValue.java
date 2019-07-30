package com.github.jferard.fastods.attribute;

/**
 * Classes that represents attributes should inherit this class
 */
public interface AttributeValue {
    /**
     * @return the string representation of the attribute
     */
    CharSequence getValue();
}
