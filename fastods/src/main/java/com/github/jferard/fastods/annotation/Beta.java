package com.github.jferard.fastods.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For elements (classes, methods, ...) that are still in development/test.
 *
 * Before FastODS 1.0: Do not rely on the elements annotated with Beta
 * As of FastODS 1.0: Might need some work to refactor.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD,
        ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PACKAGE, ElementType.PARAMETER,
        ElementType.TYPE})
public @interface Beta {
}
