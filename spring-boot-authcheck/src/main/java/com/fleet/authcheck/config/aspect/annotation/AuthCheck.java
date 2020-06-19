package com.fleet.authcheck.config.aspect.annotation;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping
public @interface AuthCheck {

    /**
     * 权限项
     */
    String[] permits() default {};

    /**
     * 角色
     */
    String[] roles() default {};
}
