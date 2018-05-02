package com.gallops.mobile.jmvclibrary.http.requester;

import android.support.annotation.NonNull;

import com.gallops.mobile.jmvclibrary.http.requester.creator.BodyCreator;
import com.gallops.mobile.jmvclibrary.http.requester.creator.FormBodyCreator;
import com.gallops.mobile.jmvclibrary.http.requester.creator.RequestBodyCreator;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 配置请求类型
 * Created by wangyu on 2018/5/2.
 */
@BodyCreator(FormBodyCreator.class)
public abstract class BaseCustomRequester extends BaseRequester {

    @NonNull
    @Override
    protected RequestBody onBuildRequestBody(Map<String, Object> params) {
        RequestBody body = null;
        Class<?> cls = this.getClass();
        boolean hasFoundAnnotation = false;
        while (cls != null && !hasFoundAnnotation) {
            BodyCreator creator = cls.getAnnotation(BodyCreator.class);
            if (creator == null) {
                cls = cls.getSuperclass();
                hasFoundAnnotation = false;
            } else {
                hasFoundAnnotation = true;
                try {
                    RequestBodyCreator requestBodyCreator = creator.value().newInstance();
                    body = requestBodyCreator.onCreate(params);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //noinspection ConstantConditions
        return body;
    }
}
