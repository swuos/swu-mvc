package com.swuos.example.jmvc.api;

import com.swuos.mobile.jmvclibrary.http.RouteInterface;

/**
 * 路由配置
 * Created by wangyu on 2018/3/27.
 */

public enum RouteEnum implements RouteInterface {

    ROUTE_LOGIN("api/login", 1);

    RouteEnum(String route, int logId) {
        this.route = route;
        this.logId = logId;
    }

    private String route;

    private int logId;

    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public int getLogId() {
        return logId;
    }
}
