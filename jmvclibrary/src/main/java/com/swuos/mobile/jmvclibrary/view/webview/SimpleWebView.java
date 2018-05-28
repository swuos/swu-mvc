package com.swuos.mobile.jmvclibrary.view.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.swuos.mobile.jmvclibrary.R;
import com.swuos.mobile.jmvclibrary.utils.ToastUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * webView封装
 * Created by wangyu on 2018/5/8.
 */

public class SimpleWebView extends LinearLayout {
    private boolean showProgress;

    private ProgressBar mProgressBar;
    private LinearLayout mNetErrorLayout;
    private WebView mWebView;

    private WebViewChangeListener webViewChangeListener;
    private String titleText = "";
    private String webUrl = "";

    private List<Map<String, String>> mUrls;
    private String mCurrentUrl;
    private OnTitleChange mOnTitleChange;

    private String originUrl = ""; //最开始打开界面时的url

    private String errorUrl;
    private JsInterface jsInterface;

    public SimpleWebView(@NonNull Context context) {
        this(context, null);
    }

    public SimpleWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
        initView(context);
        initData();
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleWebView, defStyleAttr, 0);
        showProgress = typedArray.getBoolean(R.styleable.SimpleWebView_showProgress, true);
        Drawable drawable = typedArray.getDrawable(R.styleable.SimpleWebView_progressDrawable);
        if (drawable != null) {
            setProgressDrawable(drawable);
        }
        typedArray.recycle();
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.view_simple_web_view, this);
        mProgressBar = view.findViewById(R.id.custom_webView_progress);
        mNetErrorLayout = view.findViewById(R.id.net_error_layout);
        mWebView = view.findViewById(R.id.custom_webView);
        mProgressBar.setVisibility(showProgress ? VISIBLE : GONE);
        mUrls = new ArrayList<>();
        mNetErrorLayout.setOnClickListener(v -> {
            mWebView.setVisibility(VISIBLE);
            setReload();
        });
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initData() {
        if (isInEditMode()) {
            return;
        }
        if (jsInterface == null) {
            jsInterface = new EmptyJsInterface(getContext());
        }
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(jsInterface, "JavaScriptInterface");
        mWebView.setDownloadListener(new MyWebViewDownloadListener());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mCurrentUrl = url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                notifyTitleChange();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (matchRegUrl(url)) {
                    view.loadUrl(url);
                    errorUrl = "";
                    if (!webUrl.equals(url)) {
                        if (webViewChangeListener != null) {
                            webViewChangeListener.urlChange(url);
                        }
                        webUrl = url;
                    }
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.setVisibility(GONE);
                errorUrl = failingUrl;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgressBar == null) {
                    return;
                }
                mProgressBar.setProgress(newProgress > 5 ? newProgress : 5);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!titleText.equals(title)) {
                    if (webViewChangeListener != null) webViewChangeListener.titleChange(title);
                    titleText = title;
                }
                Map<String, String> urlTitle = new HashMap<>();
                urlTitle.put(mCurrentUrl, title);
                mUrls.add(urlTitle);
            }
        });
    }

    /**
     * 判断浏览器是否可返回
     */
    public boolean judgeWebViewIsGoBack() {
        return mWebView.canGoBack();
    }

    public void getGoBack() {
        mWebView.goBack();
    }

    public void setReload() {
        if (TextUtils.isEmpty(errorUrl)) {
            mWebView.reload();
        } else {
            mWebView.loadUrl(errorUrl);
        }
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setProgressDrawable(@NonNull Drawable d) {
        mProgressBar.setProgressDrawable(d);
    }

    public boolean judgeWebViewCanGoForward() {
        return mWebView.canGoForward();
    }

    public void setGoForward() {
        mWebView.goForward();
    }

    /**
     * js接口开关，默认加载EmptyJsInterface
     *
     * @param isOpenJs
     */
    public void setIsOpenJs(boolean isOpenJs) {
        setIsOpenJs(isOpenJs, new EmptyJsInterface(getContext()));
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void setIsOpenJs(boolean isOpenJs, JsInterface newJsInterface) {
        if (newJsInterface != null) jsInterface = newJsInterface;
        if (isOpenJs) {
            mWebView.addJavascriptInterface(jsInterface, "JavaScriptInterface");
        } else {
            mWebView.removeJavascriptInterface("JavaScriptInterface");
        }
    }

    /**
     * 设置webView变化监听
     */
    public void setWebViewChangeListener(WebViewChangeListener webViewChangeListener) {
        this.webViewChangeListener = webViewChangeListener;
    }

    /**
     * 加载网页
     *
     * @param webUrl 网址
     */
    @SuppressLint("addJavascriptInterface")
    public void loadWebUrl(String webUrl) {
        originUrl = webUrl;
        if (webUrl == null) {
            ToastUtil.showToast(R.string.url_wrong_check_you_url);
            if (webViewChangeListener != null) {
                webViewChangeListener.urlIsEmpty(true);
            }
            return;
        }
        mWebView.loadUrl(webUrl);
    }


    public void setCallHiddenWebViewMethod(String name) {
        try {
            Method method = WebView.class.getMethod(name);
            method.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentTitle() {
        for (Map<String, String> map : mUrls) {
            if (map.containsKey(mCurrentUrl)) {
                return map.get(mCurrentUrl);
            }
        }
        return "";
    }

    private void notifyTitleChange() {
        if (mOnTitleChange != null) {
            mOnTitleChange.titleChange(getCurrentTitle());
        }
    }

    public void setOnTitleChange(OnTitleChange mOnTitleChange) {
        this.mOnTitleChange = mOnTitleChange;
    }

    /**
     * 正则匹配url是否可用
     *
     * @param url url
     */
    private boolean matchRegUrl(String url) {
        return Pattern.compile("^http(s?)://").matcher(url).find();
    }

    public void destroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    /**
     * webView变化监听
     */
    public interface WebViewChangeListener {
        /**
         * 标题变化了
         *
         * @param title 标题
         */
        void titleChange(String title);

        /**
         * 当前网址是否为空
         */
        void urlIsEmpty(boolean isEmpty);

        void urlChange(String webUrl);
    }

    public interface OnTitleChange {
        void titleChange(String title);
    }

    /**
     * 加载网页的监听
     */
    public class MyWebViewDownloadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            Context context = getContext();
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }
}
