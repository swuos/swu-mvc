# 网络请求模块BaseRequester
## 0.快速使用
```java
@BodyCreator(JsonBodyCreator.class)
public class QuickUseRequester extends BaseRequester {

    public QuickUseRequester(@NonNull OnResultListener listener) {
        super(listener);
    }

    //设置请求方法 HttpMethod.POST 或者HttpMethod.GET
    @NonNull
    @Override
    protected HttpMethod setMethod() {
        return HttpMethod.POST;
    }

    //在GET请求时会将params拼接作为query参数
    //在POST请求时则会将params构造为form表单参数或者json参数
    //      在类前使用注解 @BodyCreator(FormBodyCreator.class)则会构建出表单
    //      在类前使用注解 @BodyCreator(JsonBodyCreator.class)则会构建json参数
    @Override
    protected void onPutParams(@NonNull Map params) {
        params.put("name", "张三");
        params.put("age", "13");
    }
    //获得接口地址,推荐使用枚举进行集中管理
    @Override
    protected ApiInterface getApi() {
        return new ApiInterface() {
            @Override
            public String getApiUrl() {
                return "127.0.0.1";
            }
        };
    }
    //获得接口路由,推荐使用枚举进行集中管理
    @NonNull
    @Override
    protected RouteInterface setRoute() {
        return new RouteInterface() {
            @Override
            public String getRoute() {
                return "/info/add";
            }

            //此处的LogId用来输出日志作为标记
            @Override
            public int getLogId() {
                return 100;
            }
        };


    }
      //网络框架会自动把数据解析成JSONObject,在onDumpData里可以把jsonObject转换成需要的对象,可以手动转换,本框架也提供了自动的方法.
    @Override
    protected Object onDumpData(@NonNull JSONObject jsonObject) throws JSONException {
        return jsonObject;
    }
}


```

## 1.api 介绍
### ``void onPutParams(@NonNull Map<String, Object> params)``
在发送GET请求时会使用params的参数拼接出query参数
在发送POST请求时则有两种情况





