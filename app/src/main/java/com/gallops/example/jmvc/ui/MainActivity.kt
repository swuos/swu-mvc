package com.gallops.example.jmvc.ui

import android.os.Bundle
import android.widget.TextView
import com.gallops.example.jmvc.R
import com.gallops.example.jmvc.requester.LoginRequester
import com.gallops.mobile.jmvclibrary.app.BaseActivity
import com.gallops.mobile.jmvclibrary.http.ErrorCode
import com.gallops.mobile.jmvclibrary.http.OnResultListener
import com.gallops.mobile.jmvclibrary.utils.kt.proxy.bindView

class MainActivity : BaseActivity() {

    private val textView by bindView<TextView>(R.id.textView)

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProgressDialog()
        LoginRequester("000000", "000000", OnResultListener { code, t, msg ->
            dismissProgressDialog()
            if (code == ErrorCode.RESULT_DATA_OK) {
                textView.text = t.toString()
            }
        }).execute()
    }
}
