package com.github.jferard.fastods.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For elements (classes, methods, ...) that are not recognized by LibreOffice.
 *
 * This won't have any effect.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NoLibreOffice {
    /**
     * @return the reason why the element is unsafe
     */
    String until();
}
