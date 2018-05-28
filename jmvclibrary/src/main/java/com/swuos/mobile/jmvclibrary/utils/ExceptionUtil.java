package com.swuos.mobile.jmvclibrary.utils;

import com.swuos.mobile.jmvclibrary.app.JApp;

/**
 * 异常抛出
 * Created by wangyu on 2018/3/30.
 */

public class ExceptionUtil {

    public static void throwException(Exception exception) {
        if (JApp.isDebug()) {
            throw new RuntimeException(exception);
        }
    }
}
