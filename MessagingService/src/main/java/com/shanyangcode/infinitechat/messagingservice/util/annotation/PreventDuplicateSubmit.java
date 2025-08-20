package com.shanyangcode.infinitechat.messagingservice.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//注解应用于方法上
@Retention(RetentionPolicy.RUNTIME)//注解在运行时可用
public @interface PreventDuplicateSubmit {
    long timeout() default 5000;
}
