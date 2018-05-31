# 网络请求模块HttpRequester

## 0.快速使用
第一步继承``HttpRequester``
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
第二步
```java
 QuickUseRequester quickUseRequester=new QuickUseRequester(new OnResultListener() {
            //code:请求状态码
            //result:从onDumpData中解析出的数据
            //msg:服务器返回的信息
            @Override
            public void onResult(int code, Object result, String msg) {

            }
        });
quickUseRequester.execute();
```
这样就完成了一次简单的网络请求
## 1.初级使用
可以看到完成一次网络请求要写那么多的代码,非常的麻烦.
下面将演示使用如何简化代码
```java
@BodyCreator(JsonBodyCreator.class) //使用注解 @BodyCreator(JsonBodyCreator.class)则会构建post的json参数
@RequestMethod(HttpMethod.POST)     //设置请求方法 HttpMethod.POST 或者HttpMethod.GET
@Route(RouteEnum.INFO_ADD) //使用注解和枚举来添加接口的路由
public class QuickUseRequester extends HttpRequester {
    private  String age;
    private  String name;

    public QuickUseRequester(String name, String age, @NonNull OnResultListener listener) {
        super(listener);
        this.name=name;
        this.age=age;
    }
    //在GET请求时会将params拼接作为query参数
    //在POST请求时则会将params构造为form表单参数或者json参数
    //      在类前使用注解 @BodyCreator(FormBodyCreator.class)则会构建出表单
    //      在类前使用注解 @BodyCreator(JsonBodyCreator.class)则会构建json参数
    @Override
    protected void onPutParams(@NonNull Map params) {
        params.put("name", age);
        params.put("age", name);
    }
    //获得接口地址
    @Override
    protected ApiInterface getApi() {
        return new ApiInterface() {
            @Override
            public String getApiUrl() {
                return "127.0.0.1";
            }
        };
    }

    //网络框架会自动把数据解析成JSONObject,在onDumpData里可以把jsonObject转换成需要的对象,可以手动转换,本框架也提供了自动的方法.
    @Override
    protected Object onDumpData(@NonNull JSONObject jsonObject) throws JSONException {
        return jsonObject;
    }
}

//使用到的枚举类
public enum RouteEnum implements RouteInterface {
    INFO_ADD("/info/add",1000);
    INFO_DELETE("/info/delete",1001);
    String route;
    int logId;
    RouteEnums( String route,int logId) {
    this.logId=logId;
    this.route=route;
    }

    @Override
    public String getRoute() {
        return route;
    }

    @Override
    public int getLogId() {
        return logId;
    }
}
```
通过使用使用注解来设置请求方式,和使用枚举来管理接口路由,使得整个代码精简了许多.
但是还可以更精简
## 2.高级使用
通常我们发送请求时的接口地址是固定的,一些发送的内容也是固定的(比如请求头),这样我们可以把这部分抽离出来
```java
@RequestMethod(HttpMethod.POST)
public abstract class HostRequester<T> extends HttpRequester<T> {
    public HostRequester(@NonNull OnResultListener<T> listener) {
        super(listener);
    }
    //统一设置请求地址
    @Override
    protected ApiInterface getApi() {
        return return new ApiInterface() {
            @Override
            public String getApiUrl() {
                return "127.0.0.1";
            }
        };
    }
    //对于每次请求时都要带上的参数可以在这里设置
    @Override
    protected void preHandleRequest(@NonNull Request.Builder reqBuilder) {
        super.preHandleRequest(reqBuilder);
        reqBuilder.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        reqBuilder.addHeader("token", "abcdefg123456");
    }
}
```
抽离之后,我们再继承``HostRequester``就这可以这么写
```java
@BodyCreator(JsonBodyCreator.class)
@RequestMethod(HttpMethod.POST)
@Route(RouteEnum.GET_AC_PROFILE)
public class QuickUseRequester extends HostRequester {

    private  String age;
    private  String name;
    public QuickUseRequester(String name, String age, @NonNull OnResultListener listener) {
        super(listener);
        this.name=name;
        this.age=age;
    }
    @Override
    protected void onPutParams(@NonNull Map params) {
        params.put("name", age);
        params.put("age", name);
    }

    @Override
    protected Object onDumpData(@NonNull JSONObject jsonObject) throws JSONException {
        return jsonObject;
    }
}
```
可以看到代码已经精简很多了.








