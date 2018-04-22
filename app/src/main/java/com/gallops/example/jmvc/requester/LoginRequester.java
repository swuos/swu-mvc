package com.gallops.example.jmvc.requester;

import android.support.annotation.NonNull;

import com.gallops.example.jmvc.api.RouteEnum;
import com.gallops.example.jmvc.api.WebApiImpl;
import com.gallops.mobile.jmvclibrary.http.ApiInterface;
import com.gallops.mobile.jmvclibrary.http.HttpMethod;
import com.gallops.mobile.jmvclibrary.http.HttpRequester;
import com.gallops.mobile.jmvclibrary.http.OnResultListener;
import com.gallops.mobile.jmvclibrary.http.RouteInterface;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

/**
 * 登录请求
 * Created by wangyu on 2018/3/27.
 */
public class LoginRequester extends HttpRequester<JSONObject> {

    private String name;
    private String pwd;

    public LoginRequester(String userName, String password, @NonNull OnResultListener<JSONObject> listener) {
        super(listener);
        this.name = userName;
        this.pwd = password;
    }

    @Override
    protected JSONObject onDumpData(@NonNull JSONObject jsonObject) throws JSONException {
        return jsonObject;
    }

    @NonNull
    @Override
    protected HttpMethod setMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected ApiInterface getApi() {
        return getHttpModel().get(WebApiImpl.class);
    }

    @NonNull
    @Override
    protected RouteInterface setRoute() {
        return RouteEnum.ROUTE_LOGIN;
    }

    @NonNull
    @Override
    protected FormBody.Builder onPutParams(@NonNull FormBody.Builder builder) {
        return builder.add("username", name)
                .add("password", pwd);
    }
}
