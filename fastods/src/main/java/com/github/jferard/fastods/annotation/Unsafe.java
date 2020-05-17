package com.github.jferard.fastods.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For elements (classes, methods, ...) that are unsafe.
 *
 * Do not use unsafe elements.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD,
        ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE, ElementType.PARAMETER,
        ElementType.TYPE})
public @interface Unsafe {
    /**
     * @return the reason why the element is unsafe
     */
    String why();
}
