package com.swuos.mobile.jmvclibrary.http;

/**
 * 网络请求结果处理
 * Created by wangyu on 2018/3/6.
 */

public abstract class HttpResultParser implements OnResultListener<String> {

    public abstract void onError(Exception e);
}
