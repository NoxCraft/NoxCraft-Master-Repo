package com.noxpvp.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates this is temporary
 * <p>
 * Most commonly used for quick releases of partial features and workarounds. <br>
 * This is used to mark stuff that should not be heavily relied on either.
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.ANNOTATION_TYPE, 
		ElementType.CONSTRUCTOR, 
		ElementType.FIELD, 
		ElementType.LOCAL_VARIABLE,
		ElementType.METHOD,
		ElementType.PACKAGE,
		ElementType.PARAMETER,
		ElementType.TYPE})
public @interface Temporary {

}
