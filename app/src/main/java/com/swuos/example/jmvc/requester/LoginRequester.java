package com.swuos.example.jmvc.requester;

import android.support.annotation.NonNull;

import com.swuos.example.jmvc.api.HttpRequester;
import com.swuos.example.jmvc.api.Route;
import com.swuos.example.jmvc.api.RouteEnum;
import com.swuos.mobile.jmvclibrary.http.HttpMethod;
import com.swuos.mobile.jmvclibrary.http.OnResultListener;
import com.swuos.mobile.jmvclibrary.http.annotation.BodyCreator;
import com.swuos.mobile.jmvclibrary.http.annotation.RequestMethod;
import com.swuos.mobile.jmvclibrary.http.creator.FormBodyCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 登录请求
 * Created by wangyu on 2018/3/27.
 */
@Route(RouteEnum.ROUTE_LOGIN)
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
}
