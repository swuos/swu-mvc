package com.gallops.example.jmvc.requester;

import android.support.annotation.NonNull;

import com.gallops.example.jmvc.api.RouteEnum;
import com.gallops.example.jmvc.api.WebApiImpl;
import com.gallops.mobile.jmvclibrary.http.ApiInterface;
import com.gallops.mobile.jmvclibrary.http.HttpMethod;
import com.gallops.mobile.jmvclibrary.http.HttpRequester;
import com.gallops.mobile.jmvclibrary.http.OnResultListener;
import com.gallops.mobile.jmvclibrary.http.RouteInterface;
import com.gallops.mobile.jmvclibrary.http.requester.creator.BodyCreator;
import com.gallops.mobile.jmvclibrary.http.requester.creator.FormBodyCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * 登录请求
 * Created by wangyu on 2018/3/27.
 */
@BodyCreator(FormBodyCreator.class)
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

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("username", name);
        params.put("password", pwd);
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

}
