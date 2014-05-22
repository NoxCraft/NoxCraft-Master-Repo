package com.noxpvp.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This method is thread blocking!
 * <p><b>Use with caution if you must not block certain threads.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(value = {ElementType.METHOD})
public @interface Blocking {

}
