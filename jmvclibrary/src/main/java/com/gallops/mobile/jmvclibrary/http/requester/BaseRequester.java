package com.gallops.mobile.jmvclibrary.http.requester;

import android.support.annotation.NonNull;

import com.gallops.mobile.jmvclibrary.app.JApp;
import com.gallops.mobile.jmvclibrary.http.ApiInterface;
import com.gallops.mobile.jmvclibrary.http.ErrorCode;
import com.gallops.mobile.jmvclibrary.http.HttpMethod;
import com.gallops.mobile.jmvclibrary.http.HttpResultParser;
import com.gallops.mobile.jmvclibrary.http.RouteInterface;
import com.gallops.mobile.jmvclibrary.models.HttpModel;
import com.gallops.mobile.jmvclibrary.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 网络请求基类
 * Created by wangyu on 2018/3/6.
 */

public abstract class BaseRequester {

    private static final String TAG = "WEB";

    private HttpResultParser httpResultParser = new HttpResultParser() {
        @Override
        public void onResult(int code, String content, String msg) {
            if (JApp.isDebug()) {
                String serverContent = content == null ? "null" : content.replace("%", "%%");
                String string = String.format(Locale.getDefault(), "response, logCode = %d, url = %s, code = %d, content = %s", setRoute().getLogId(), setReqUrl(), code, serverContent);
                Logger.i(TAG, string);
            }
            try {
                if (code == HTTP_OK) {
                    JSONObject jsonObject = new JSONObject(content);
                    BaseRequester.this.onResult(parseCode(jsonObject), jsonObject, parseMessage(jsonObject));
                } else {
                    BaseRequester.this.onResult(code, null, "");
                }
            } catch (JSONException exception) {
                onError(exception);
            }
        }

        @Override
        public void onError(Exception e) {
            if (JApp.isDebug()) {//调试模式下打印web请求日志
                String string = String.format(Locale.getDefault(), "response, logCode = %d, url = %s, error = %s", setRoute().getLogId(), setReqUrl(), e);
                Logger.i(TAG, string);
            }
            BaseRequester.this.onError(e);
        }
    };

    public void execute(long delay) {
        getHttpModel().getHandler().postDelayed(this::execute, delay);
    }

    public void execute() {
        HttpModel httpModel = getHttpModel();
        httpModel.getExecutor().execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Map<String, Object> params = new HashMap<>();
            onPutParams(params);
            RequestBody requestBody = onBuildRequestBody(params);
            Request.Builder reqBuilder = new Request.Builder()
                    .url(appendUrl(setReqUrl()))
                    .method(setMethod().getMethod(), requestBody);
            preHandleRequest(reqBuilder);
            Request request = reqBuilder.build();
            if (JApp.isDebug()) {
                String string = String.format(Locale.getDefault(), "request, logCode = %d, url = %s", setRoute().getLogId(), setReqUrl() + "?" + logParams(params));
                Logger.i(TAG, string);
            }
            try {
                Response response = client.newCall(request).execute();
                if (response == null) {
                    httpModel.getHandler().post(() -> httpResultParser.onResult(ErrorCode.RESULT_BODY_EMPTY, null, ""));
                } else {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        String result = body != null ? body.string() : "";
                        httpModel.getHandler().post(() -> httpResultParser.onResult(response.code(), result, ""));
                    } else {
                        httpModel.getHandler().post(() -> httpResultParser.onResult(ErrorCode.RESULT_NET_ERROR, "", ""));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                httpModel.getHandler().post(() -> httpResultParser.onError(e));
            }
        });
    }

    @NonNull
    protected abstract RequestBody onBuildRequestBody(Map<String, Object> params);

    protected abstract void onPutParams(Map<String, Object> params);

    /**
     * 获取要打印的请求参数
     *
     * @param params  表单
     * @return  拼接后的参数字符串
     */
    protected String logParams(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        for (String key : params.keySet()) {
            builder.append(key)
                    .append("=")
                    .append(params.get(key))
                    .append("&");
        }
        return builder.toString();
    }

    /**
     * request预处理
     *
     * @param reqBuilder    reqBuilder
     */
    protected void preHandleRequest(Request.Builder reqBuilder) {

    }

    /**
     * url拼接额外字段，一般在GET请求下会使用到
     *
     * @param url   url
     * @return  newUrl
     */
    protected String appendUrl(String url) {
        return url;
    }

    /**
     * 设置请求方式
     *
     * @return  {@link HttpMethod})
     */
    @NonNull
    protected abstract HttpMethod setMethod();

    /**
     * 设置请求url
     *
     * @return  url拼接路由得到的请求地址
     */
    protected String setReqUrl() {
        return getApi().getApiUrl() + setRoute().getRoute();
    }

    protected abstract ApiInterface getApi();

    /**
     * 设置请求路由
     *
     * @return  {@link RouteInterface}
     */
    @NonNull
    protected abstract RouteInterface setRoute();

    /**
     * 统一解析请求的code码
     *
     * @param jsonObject    jsonObject
     * @return  code
     */
    protected int parseCode(JSONObject jsonObject) {
        return jsonObject.optInt("code");
    }

    /**
     * 统一解析请求的message
     *
     * @param jsonObject    jsonObject
     * @return  msg
     */
    protected String parseMessage(JSONObject jsonObject) {
        return jsonObject.optString("msg");
    }

    /**
     * 请求成功了  服务器已经返回结果
     *
     * @param code    请求成功的HTTP返回吗，，一般code等于200表示请求成功
     * @param content 从服务器获取到的数据
     */
    protected abstract void onResult(int code, @NonNull JSONObject content, String msg) throws JSONException;

    /**
     * 请求失败了
     *
     * @param exception 失败原因
     */
    protected abstract void onError(Exception exception);

    /**
     * 获取http执行环境管理器
     *
     * @return
     */
    protected HttpModel getHttpModel() {
        return JApp.getInstance().getModel(HttpModel.class);
    }
}
