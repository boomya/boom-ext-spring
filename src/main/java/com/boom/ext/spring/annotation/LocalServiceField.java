package com.boom.ext.spring.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jiangshan on 15/4/2.
 */
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface LocalServiceField {
}
