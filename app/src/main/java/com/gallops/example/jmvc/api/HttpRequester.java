package com.gallops.example.jmvc.api;

import android.support.annotation.NonNull;

import com.gallops.mobile.jmvclibrary.http.ApiInterface;
import com.gallops.mobile.jmvclibrary.http.BaseRequester;
import com.gallops.mobile.jmvclibrary.http.HttpMethod;
import com.gallops.mobile.jmvclibrary.http.OnResultListener;
import com.gallops.mobile.jmvclibrary.http.RouteInterface;
import com.gallops.mobile.jmvclibrary.http.annotation.BodyCreator;
import com.gallops.mobile.jmvclibrary.http.annotation.RequestMethod;
import com.gallops.mobile.jmvclibrary.http.creator.FormBodyCreator;

/**
 * 路由配置
 * Created by wangyu on 2018/5/4.
 */

@RequestMethod(HttpMethod.POST)
@BodyCreator(FormBodyCreator.class)
public abstract class HttpRequester<T> extends BaseRequester<T> {

    public HttpRequester(@NonNull OnResultListener<T> listener) {
        super(listener);
    }

    @NonNull
    @Override
    protected RouteInterface setRoute() {
        RouteInterface routeInterface = null;
        Class<?> cls = getClass();
        boolean hasFoundAnnotation = false;
        while (cls != null && !hasFoundAnnotation) {
            Route route = cls.getAnnotation(Route.class);
            if (route == null) {
                cls = cls.getSuperclass();
                hasFoundAnnotation = false;
            } else {
                hasFoundAnnotation = true;
                routeInterface = route.value();
            }
        }
        if (routeInterface == null) throw new RuntimeException(this.getClass().getSimpleName() + "缺少路由配置, 请确保重写了setRoute方法或者使用了@Route注解");
        return routeInterface;
    }

    @Override
    protected ApiInterface getApi() {
        return getHttpModel().get(WebApiImpl.class);
    }
}
