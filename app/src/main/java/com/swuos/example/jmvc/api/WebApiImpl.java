package com.swuos.example.jmvc.api;

import com.swuos.mobile.jmvclibrary.http.ApiInterface;

/**
 * api配置
 * Created by wangyu on 2018/3/27.
 */

public class WebApiImpl implements ApiInterface {

    public static final ServerConfig server = ServerConfig.OFFICIAL;

    public static String WEB_URL;

    static {
        switch (server) {
            case TEST:
                WEB_URL = "http://xxx.xxx.com";
                break;
            case OFFICIAL:
                WEB_URL = "http://xxx.xxx.com";
                break;
            default:
                throw new RuntimeException("请配置服务器地址");
        }
    }

    @Override
    public String getApiUrl() {
        return WEB_URL;
    }

    public enum ServerConfig {
        OFFICIAL,//正式服
        TEST//测试服
    }

}
