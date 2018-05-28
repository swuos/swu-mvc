package com.swuos.example.jmvc.ui

import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.swuos.example.jmvc.R
import com.swuos.mobile.jmvclibrary.app.BaseActivity
import com.swuos.mobile.jmvclibrary.utils.kt.proxy.bindView
import com.swuos.mobile.jmvclibrary.view.webview.JsInterface
import com.swuos.mobile.jmvclibrary.view.webview.SimpleWebView

class MainActivity : BaseActivity() {

    private val webView by bindView<SimpleWebView>(R.id.webView)

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.setIsOpenJs(true, MyJsInterface(context))
//        webView.loadWebUrl("file:///android_asset/test.html")
        webView.loadWebUrl("http://172.16.2.218:8080/ioc/modules/report/1.html?reportId=1")
//        webView.loadWebUrl("https://www.baidu.com")
    }

    class MyJsInterface(context : Context): JsInterface(context) {

        @JavascriptInterface
        fun getAppToken(): String {
            return "asdasd"
        }
    }
}
