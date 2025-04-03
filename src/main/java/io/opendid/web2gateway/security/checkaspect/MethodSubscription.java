package io.opendid.web2gateway.security.checkaspect;

import java.lang.annotation.*;

/**
 * @author SolomonC
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodSubscription {

  String name() default "";
}
