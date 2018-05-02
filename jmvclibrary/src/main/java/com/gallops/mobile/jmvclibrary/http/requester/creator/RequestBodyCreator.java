package com.gallops.mobile.jmvclibrary.http.requester.creator;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * 请求参数解析器
 * Created by wangyu on 2018/5/2.
 */

public interface RequestBodyCreator {

    RequestBody onCreate(Map<String, Object> params);
}
