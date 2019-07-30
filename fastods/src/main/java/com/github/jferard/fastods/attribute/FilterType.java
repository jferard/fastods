package com.github.jferard.fastods.attribute;

import com.github.jferard.fastods.attribute.AttributeValue;

import java.util.Locale;

/**
 * 19.611.2<table:filter-condition>
 */
public enum FilterType implements AttributeValue {
    /**
     * comparison as numeric values
     */
    NUMBER,
    /**
     * comparison as text values
     */
    TEXT;

    @Override
    public CharSequence getValue() {
        return this.toString().toLowerCase(Locale.US);
    }
}
