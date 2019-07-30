package com.github.jferard.fastods.attribute;

import java.util.Locale;

/**
 * 20.353style:table-centering
 */
public enum PageCentering implements AttributeValue {
    /**
     * Center horizontally and vertically
     */
    BOTH,
    /**
     * Center horizontally
     */
    HORIZONTAL,
    /**
     * Do not center
     */
    NONE,
    /**
     * Center vertically
     */
    VERTICAL;

    @Override
    public CharSequence getValue() {
        return this.toString().toLowerCase(Locale.US);
    }
}
