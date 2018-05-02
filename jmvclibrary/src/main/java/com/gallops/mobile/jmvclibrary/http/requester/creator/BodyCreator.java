package com.gallops.mobile.jmvclibrary.http.requester.creator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数解析器注解
 * Created by wangyu on 2018/5/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BodyCreator {
    Class<? extends RequestBodyCreator> value();
}
