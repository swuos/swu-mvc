package com.swuos.mobile.jmvclibrary.view.webview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;

import com.swuos.mobile.jmvclibrary.utils.Logger;
import com.swuos.mobile.jmvclibrary.utils.ToastUtil;

/**
 * js交互基类，使用：window.JavaScriptInterface.logI('日志')
 * Created by wangyu on 2017/12/14.
 */

public abstract class JsInterface {

    private Context context;
    private static String TAG;

    public JsInterface(Context context) {
        this.context = context;
        TAG = this.getClass().getSimpleName();
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
     * 打印日志 info级别
     * @param msg
     */
    @JavascriptInterface
    public void logI(String msg) {
        Logger.i(TAG, msg);
    }

    /**
     * 打印日志 error级别
     * @param msg
     */
    @JavascriptInterface
    public void logE(String msg) {
        Logger.e(TAG, msg);
    }

    /**
     * 关闭页面
     */
    @JavascriptInterface
    public void finish() {
        ((Activity) context).finish();
    }
}
