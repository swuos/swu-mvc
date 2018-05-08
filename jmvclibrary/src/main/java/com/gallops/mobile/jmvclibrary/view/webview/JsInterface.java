package com.gallops.mobile.jmvclibrary.view.webview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;

import com.gallops.mobile.jmvclibrary.utils.ToastUtil;

/**
 * js交互基类，使用：window.JavaScriptInterface.getUserId()
 * Created by wangyu on 2017/12/14.
 */

public abstract class JsInterface {

    private Context context;

    public JsInterface(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    /**
     * 获取手机系统版本
     * @return
     */
    @JavascriptInterface
    public final int getMobileVersion() {
        return Build.VERSION.SDK_INT;
    }

    @JavascriptInterface
    public final String getMobilePlatform() {
        return "android";
    }

    /**
     * 弹出toast
     * @param msg
     */
    @JavascriptInterface
    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    /**
     * 关闭页面
     */
    @JavascriptInterface
    public void finish() {
        ((Activity) context).finish();
    }
}
