package appframe.appframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;
import com.github.mrengineer13.snackbar.SnackBar;
import com.github.snowdream.android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by dashi on 15/6/10.
 */
public final class Http {
    static final String TAG = "Http";
    public interface RequestMethod {
        int GET = com.android.volley.Request.Method.GET;
        int POST = com.android.volley.Request.Method.POST;
        int PUT = com.android.volley.Request.Method.PUT;
        int DELETE = com.android.volley.Request.Method.DELETE;
        int HEAD = com.android.volley.Request.Method.HEAD;
        int OPTIONS = com.android.volley.Request.Method.OPTIONS;
        int TRACE = com.android.volley.Request.Method.TRACE;
        int PATCH = com.android.volley.Request.Method.PATCH;
    }
    public interface RequestCacheTime {
        int NoCache = 0;
        int AnyCacheIsOK = -1;
    }
    public static class APIResultResponseStatus{
        public String ErrorCode;
        public String Message;
        public String StackTrace;

    }
    static Gson gson;
    public static class APIResult<T>{
        public T Data;
        public APIResultResponseStatus ResponseStatus;
        public int ExecutionTime;

        JsonElement loadFromJsonAndReturnDataJsonElement(JsonObject jo, Class<?> clazz, boolean array) throws Exception{
            if(jo.has("ExecutionTime")){
                ExecutionTime = jo.get("ExecutionTime").getAsInt();
            }
            JsonElement jeResponseStatus = jo.has("ResponseStatus") ? jo.get("ResponseStatus") : null;
            if(jeResponseStatus != null && jeResponseStatus.isJsonNull()) jeResponseStatus = null;
            if(jeResponseStatus != null){
                ResponseStatus = gson.fromJson(jeResponseStatus, APIResultResponseStatus.class);
                if(!TextUtils.isEmpty(ResponseStatus.ErrorCode)){
                    // 出错了，不需要再parse 下去了
                    if(TextUtils.isEmpty(ResponseStatus.Message)){
                        // 出错时，保证Message一定要有
                        ResponseStatus.Message = "Error||处理错误";
                    }
                    return null;
                }
            }else{
                ResponseStatus = new APIResultResponseStatus();
            }
            if(jo.has("Data")){
                JsonElement jeData = jo.get("Data");
                if(jeData == null || jeData.isJsonNull()){
                    return null;
                }
                if(!array){
                    Data = (T)gson.fromJson(jeData, clazz);
                }else{
                    if(!jeData.isJsonArray()){
                        throw new Exception("API 应该返回数组，但是没有。");
                    }
                    JsonArray ja = jeData.getAsJsonArray();
                    int size = ja.size();
                    ArrayList data = new ArrayList(size);
                    for(int i=0;i<size;i++)
                        data.add(gson.fromJson(ja.get(i), clazz));
                    Data = (T)data;
                }
                return jeData;
            }
            return null;
        }
    }
    public static class BaseDto{

    }
    /*
     * 定义一个http api，包括 method， url， 返回 dto 类型
     */
    public static class API<T>{
        public int method;
        public String url;
        public Class<?> resultClass;
        public boolean resultIsList;


        public static <T extends BaseDto> API<T> get(String url, Class<T> resultClass){
            API<T> r = new API<>();
            r.url = url;
            r.method = RequestMethod.GET;
            r.resultClass = resultClass;
            r.resultIsList = false;
            return r;
        }
        public static <T extends BaseDto> API<List<T>> getList(String url, Class<T> resultClass){
            API<List<T>> r = new API<>();
            r.url = url;
            r.method = RequestMethod.GET;
            r.resultClass = resultClass;
            r.resultIsList = true;
            return r;
        }
        public static API<BaseDto> postEmpty(String url){
            return post(url, BaseDto.class);
        }
        public static <T extends BaseDto> API<T> post(String url, Class<T> resultClass){
            API<T> r = new API<>();
            r.url = url;
            r.method = RequestMethod.POST;
            r.resultClass = resultClass;
            r.resultIsList = false;
            return r;
        }
        public static <T extends BaseDto> API<List<T>> postList(String url, Class<T> resultClass){
            API<List<T>> r = new API<>();
            r.url = url;
            r.method = RequestMethod.POST;
            r.resultClass = resultClass;
            r.resultIsList = true;
            return r;
        }
    }
    /*
     * 表示一次对 http api 的请求
     */
    public static class Request<T> extends API<T>{

        public Map<String,String> params;
        public int cacheTime;
        public int delayTime;
        public String tag;
        public WeakReference<Activity> context;

        public Activity getContext(){
            if(context == null) return null;
            Activity a = context.get();
            return a;
        }
    }
    /*
     * 表示一次对 http api 的请求结果
     */
    public static class Response<T> {

        public Request<T> request;
        public String requestUrl;
        public APIResultResponseStatus resultResponseStatus;
        public T result;
        public com.android.volley.Request<?> volleyRequest;
        public boolean canceled;
    }

    /*
     * 表示一次 request 的回调
     */
    public static class RequestListener<T> implements com.android.volley.Response.Listener<String>, com.android.volley.Response.ErrorListener{
        private Response<T> response;

        protected final void showResponseMessage(Response<T> response){
            if(!TextUtils.isEmpty(response.resultResponseStatus.Message)) {
                Activity context = response.request.getContext();
                if (context != null) {
                    SnackBar.Style style;
                    String message = response.resultResponseStatus.Message;

                    int color;
                    if(TextUtils.isEmpty(response.resultResponseStatus.ErrorCode))
                        color = android.R.color.darker_gray;
                    else
                        color = android.R.color.holo_red_dark;
                    if(message.contains("||")){
                        String [] ps = message.split("\\|\\|");
                        message = ps[1];
                        if("info".equalsIgnoreCase(ps[0]))
                            color = android.R.color.background_dark;
                        else if("warning".equalsIgnoreCase(ps[0]))
                            color = android.R.color.holo_orange_dark;
                        else if("success".equalsIgnoreCase(ps[0]))
                            color = android.R.color.holo_green_dark;
                    }
                    boolean error = !TextUtils.isEmpty(response.resultResponseStatus.ErrorCode);
                    if(error)
                        style = SnackBar.Style.ALERT;
                    else
                        style = SnackBar.Style.DEFAULT;

                    // 给用户显示消息
                    new SnackBar.Builder(context)
                            .withMessage(message)
                            .withStyle(style)
                            .withDuration(SnackBar.SHORT_SNACK)
                            .withBackgroundColorId(color)
                            .show();
                }
            }
        }
        /*
         *
         */
        public void onDone(){

        }
        /*
         * request 成功
         */
        public void onSuccess(T result){
            showResponseMessage(response);
        }
        /*
         * request 失败
         */
        public void onFail(String code){
            showResponseMessage(response);
        }


        APIResult<T> parse(String responseString){
            if(response.request.resultClass == String.class && !response.request.resultIsList){
                // 直接返回 string


                APIResult<T> result = new APIResult<T>();
                result.Data = (T)responseString;
                result.ResponseStatus = new APIResultResponseStatus();
                if(response.request.method == RequestMethod.GET && result.Data != null){
                    MemoryCache.put(response.requestUrl, result.Data, null);
                }
                return result;
            }

            Request<T> request = response.request;
            try {
                JsonElement je = new JsonParser().parse(responseString);
                if(!je.isJsonObject()) throw new Exception("返回必须是 json object");

                APIResult<T> result = new APIResult<T>();
                JsonElement jeData = result.loadFromJsonAndReturnDataJsonElement(je.getAsJsonObject(), request.resultClass, request.resultIsList);
                // 写 cache
                if(response.request.method == RequestMethod.GET && result.Data != null && jeData != null){
                    MemoryCache.put(response.requestUrl, result.Data, jeData);
                }
                return result;
            }catch(Exception e){
                Log.e(TAG, "parse error", e);
                // parse error
                APIResult<T> result = new APIResult<T>();
                result.ResponseStatus = new APIResultResponseStatus();
                result.ResponseStatus.ErrorCode = "ParseError";
                result.ResponseStatus.Message = "服务器错误";
                return result;
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            APIResultResponseStatus responseStatus = new APIResultResponseStatus();
            if(error == null) responseStatus.ErrorCode = "UnknownError";
            else responseStatus.ErrorCode = error.getClass().getSimpleName();

            if(error == null){
                responseStatus.Message = "未知错误";
            } else if(error instanceof TimeoutError
                    || error instanceof NoConnectionError){
                responseStatus.Message = "网络错误";
            } else{
                responseStatus.Message = "服务器错误";
            }



            response.resultResponseStatus = responseStatus;
            String responseString = "";
            if(error.networkResponse != null){
                responseString = new String(error.networkResponse.data);
            }
            Log.e(TAG, String.format("request error: %s %s %s\n%s",
                    response.requestUrl,
                    responseStatus.ErrorCode,
                    responseStatus.Message,
                    responseString
                    ));

            if(response.canceled) return;
            onFail(responseStatus.ErrorCode);
            onDone();
        }

        @Override
        public void onResponse(String responseStr) {
            if(response.canceled) return;
            APIResult<T> result = parse(responseStr);
            if(result.ResponseStatus == null) throw new Error("程序错误 result.ResponseStatus 必须为非空");
            response.resultResponseStatus = result.ResponseStatus;
            boolean success;
            if(!TextUtils.isEmpty(result.ResponseStatus.ErrorCode)) {
                Log.e(TAG, String.format("request error: %s %s %s\n%s",
                        response.requestUrl,
                        result.ResponseStatus.ErrorCode,
                        result.ResponseStatus.Message,
                        responseStr));
                success = false;
            }else {
                success = true;
                Log.i(TAG, String.format("request done: %s\n%s", response.requestUrl, responseStr));
            }
            if(response.canceled) return;
            if(success){
                onSuccess(result.Data);
            }else{
                onFail(result.ResponseStatus.ErrorCode);
            }
            onDone();
        }

        public void onCacheHit(T cache){
            if(response.canceled) return;
            response.resultResponseStatus = new APIResultResponseStatus();
            onSuccess(cache);
            onDone();
        }
    }
    static RequestQueue requestQueue;
    static Context applicationContext;
    static RequestQueue getRequestQueue(){

        if (requestQueue == null) {


            String userAgent = "volley/0";
            try {
                String packageName = applicationContext.getPackageName();
                PackageInfo info = applicationContext.getPackageManager().getPackageInfo(packageName, 0);
                userAgent = packageName + "/" + info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
            }

            HttpStack stack;

            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }

            Network network = new BasicNetwork(stack);


            requestQueue = new RequestQueue(
                    new NoCache(),
                    network);
            requestQueue.start();

        }
        return requestQueue;
    }
    public static void init(Context _applicationContext){
        applicationContext = _applicationContext;
        gson = GsonHelper.getGson();
        getRequestQueue();
    }
    static HashMap<String,String> urlPrefixes = new HashMap<>();
    public static final String URL_PREFIX_CHAR = ":";
    public static final String URL_DEFAULT_PREFIX = URL_PREFIX_CHAR + URL_PREFIX_CHAR;
    /*
     * registerUrlPrefix("::", "http://api.example.com");
     * registerUrlPrefix(":chat:", "http://api.chat.example.com");
     */
    public static void registerUrlPrefix(String prefix, String url){
        if(prefix == null || prefix.length() < 2) throw new Error("url prefix format error");
        if(!prefix.startsWith(URL_PREFIX_CHAR) || !prefix.endsWith(URL_PREFIX_CHAR)) throw new Error("url prefix format error");

        urlPrefixes.put(prefix, url);
    }
    /*
     * formatUrl("/login") => "http://api.example.com/login"
     * formatUrl(":chat:/stat") => "http://api.chat.example.com/stat"
     * formatUrl("http://huizhe.name/") => "http://huizhe.name/"
     */
    public static String formatUrl(String url){
        if(url == null) url = "";
        if(url.length() == 0 || url.startsWith("/")){
            return urlPrefixes.get(URL_DEFAULT_PREFIX) + url;
        }
        if(url.startsWith(URL_PREFIX_CHAR)){
            int endIndex = url.indexOf(URL_PREFIX_CHAR, 1);
            String prefix = url.substring(0, endIndex);
            return urlPrefixes.get(prefix) + url.substring(prefix.length());
        }
        return url;
    }
    static String[] officalHosts;
    public static void setOfficalHosts(String[] host){
        officalHosts = host;
    }
    public static String[] getOfficalHosts(){
        return officalHosts;
    }
    static String authorizationToken;
    public static void setAuthorizationToken(String token){
        authorizationToken = token;
    }
    public static String getAuthorizationToken(){ return authorizationToken; }


    static class APIRequest<T> extends StringRequest{

        private String rawBody = null;
        private String rawBodyType = null;
        private MultipartEntity multiPartEntity = null;
        private Map<String,String> params = null;
        private Map<String,String> additionalHeaders;

        StringBuilder multipartContentLog = null;

        public APIRequest(int method, String url, Map<String,String> params, RequestListener<T> l) {
            super(method, url, l, l);
            if(params != null){

                for(String k : params.keySet()){
                    String value = params.get(k);
                    if(isFilePostField(value)){
                        // 是一个multipart post
                        multiPartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                        multipartContentLog = new StringBuilder();
                        break;
                    }
                }
                Charset charset = null;
                for(String k : params.keySet()){
                    String v = params.get(k);
                    if(v == null) v = "";
                    if(isRawParamKey(k)){
                        rawBody = v;
                    }else if(isRawTypeParamKey(k)){
                        rawBodyType = v;
                    }else if(isAdditionalHeaderParamKey(k)){
                        if(this.additionalHeaders == null) this.additionalHeaders = new HashMap<String,String>();
                        this.additionalHeaders.put(getAdditionalHeaderKey(k), v);
                    }else{

                        if(multiPartEntity != null){
                            // 是multipart
                            AbstractContentBody body = null;
                            if(isFilePostField(v)){
                                body = getFileFileNameAndMimeTypeFromFilePostField(v);


                                if(multipartContentLog != null){
                                    if(multipartContentLog.length() > 0)
                                        multipartContentLog.append("&");
                                    multipartContentLog.append(k);
                                    multipartContentLog.append("=");
                                    multipartContentLog.append(v);
                                }
                            }else{

                                try {
                                    if(charset == null) charset = Charset.forName("utf-8");
                                } catch (Exception e1) {
                                    Log.e(TAG, "cannot get charset utf-8", e1);
                                }

                                try {
                                    if(charset != null)
                                        body = new StringBody(v, charset);
                                } catch (UnsupportedEncodingException e2) {
                                    Log.e(TAG, "cannot create utf-8 string body", e2);
                                    try {
                                        body = new StringBody(v);
                                    } catch (UnsupportedEncodingException e3) {
                                        Log.e(TAG, "cannot create string body", e3);
                                    }
                                }
                                if(multipartContentLog != null){
                                    if(multipartContentLog.length() > 0)
                                        multipartContentLog.append("&");
                                    multipartContentLog.append(k);
                                    multipartContentLog.append("=");
                                    multipartContentLog.append(v);
                                }
                            }
                            if(body == null){
                                Log.e(TAG, "cannot get field body for: " + k + " " + v);
                                continue;
                            }

                            multiPartEntity.addPart(k, body);
                        }else{
                            if(this.params == null)
                                this.params = new HashMap<String,String>();
                            this.params.put(k, v);
                        }
                    }
                }
            }
        }

        @Override
        protected Map<String,String> getParams(){
            if(params != null){
                return params;
            }

            return new HashMap<String,String>();
        }

        private Map<String,String> headers = null;

        @Override
        public Map<String,String> getHeaders() throws AuthFailureError{
            if(headers == null){
                headers = Http.getHeaders(getUrl());
                if(additionalHeaders != null)
                    for(String k : additionalHeaders.keySet())
                        headers.put(k, additionalHeaders.get(k));

            }

            return headers;
        }
        public String getBodyForDebug(){
            if(multipartContentLog != null){
                return multipartContentLog.toString();
            }else{
                byte[] body = null;
                try {
                    body = getBody();
                } catch (AuthFailureError e1) {
                    e1.printStackTrace();
                }
                if(body != null && body.length > 0)
                {
                    try{
                        return new String(body, "UTF-8");
                    }catch(Exception e){
                        e.printStackTrace();
                        return "<fail to get request content>";
                    }
                }
                return "<empty request content>";
            }
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            if(multiPartEntity != null){

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try
                {
                    multiPartEntity.writeTo(bos);
                }
                catch (IOException e)
                {
                    Log.e(TAG, "IOException writing to ByteArrayOutputStream", e);
                    return new byte[0];
                }
                return bos.toByteArray();
            }
            if(rawBody != null){
                try {
                    return rawBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "cannot find utf-8 encoding", e);
                    return rawBody.getBytes();
                }
            }
            return super.getBody();
        }

        @Override
        public String getBodyContentType()
        {
            if(multiPartEntity != null)
                return multiPartEntity.getContentType().getValue();
            if(rawBodyType != null)
                return rawBodyType;
            return super.getBodyContentType();
        }
    }



    public static <T> T getCache(API<T> request){
        String url = formatUrl(request.url);
        T data = request.resultIsList ?
                (T)MemoryCache.getList(url, request.resultClass):
                (T)MemoryCache.get(url, request.resultClass);
        return data;
    }
    public static long getCacheAge(String url){
        return MemoryCache.getCacheTime(formatUrl(url));
    }
    public static <T> long getCacheAge(API<T> request){
        return getCacheAge(request.url);
    }

    static HashSet<String> delayedTags = new HashSet<>();


    public static <T> Response<T> request(Request<T> request, final RequestListener<T> listener){
        Response<T> response = new Response<>();
        response.request = request;
        listener.response = response;
        response.requestUrl = formatUrl(request.url);


        // 处理cache
        if(request.cacheTime != RequestCacheTime.NoCache){
            T data = getCache(request);
            boolean cacheOK =
                    data != null &&
                            (request.cacheTime == RequestCacheTime.AnyCacheIsOK || request.cacheTime >= getCacheAge(request));

            if(cacheOK){
                // cache 存在且未过期
                // 不需要 request
                listener.onCacheHit(data);
                return response;
            }
        }


        final com.android.volley.Request<?> volleyRequest = new APIRequest(request.method, response.requestUrl, request.params, listener);
        if(request.tag != null)
            volleyRequest.setTag(request.tag);


        // https://android.googlesource.com/platform/frameworks/volley/+/1b4858087f25a8033c5136a96b750752b87e128c/src/com/android/volley/DefaultRetryPolicy.java
        if(request.method == RequestMethod.GET){

            volleyRequest.setRetryPolicy(new DefaultRetryPolicy(
                    2200, // 这个值并不是直接等于timeout，第一次request：2500+2500=5秒timeout，第二次request：(2500+2500)×3 = 15秒timeout
                    1, // 自动重试一次
                    1.5f));
        }else{

            volleyRequest.setRetryPolicy(new DefaultRetryPolicy(
                    7000, // 这个值并不是直接等于timeout，第一次request：5000+5000=16秒timeout
                    0, // 不自动重试
                    3f));
        }


        if(request.delayTime <= 0){
            getRequestQueue().add(volleyRequest);
            return response;
        }
        if(listener.response.request.tag != null)
            delayedTags.add(listener.response.request.tag);
        listener.response.canceled = false;
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                if(listener.response.request.tag != null){
                    if(!delayedTags.contains(listener.response.request.tag)){
                        listener.response.canceled = true;
                    }
                }
                if(!listener.response.canceled){
                    getRequestQueue().add(volleyRequest);
                }
            }

        }, request.delayTime);
        return response;
    }

    public static <T> Response<T> get(Activity context, String url, Class<T> resultClass, final RequestListener<T> listener){
        Request<T> request = new Request<T>();
        request.resultClass = resultClass;
        request.url = url;
        request.method = RequestMethod.GET;
        return request(context, request, listener);
    }
    public static <T> Response<T> post(Activity context, String url, Map<String,String> params, Class<T> resultClass, final RequestListener<T> listener){
        Request<T> request = new Request<T>();
        request.resultClass = resultClass;
        request.url = url;
        request.params = params;
        request.method = RequestMethod.POST;
        return request(context, request, listener);
    }


    public static Map<String,String> getHeaders(String url){
        Map<String,String> result = new HashMap<String,String>();

        if (needAuthToken(url)) {
            if(!TextUtils.isEmpty(authorizationToken)){
                result.put("Authorization", "AppFrame "
                        + authorizationToken);
            }
            // 其他 headers
        }

        return result;
    }
    public static <T> Response<T> request(
            Activity context,
            String tag,
            API<T> request,
            Object [] urlArgs,
            Map<String, String> params,
            int cache,
            int delay,
            RequestListener<T> listener){
        Request<T> r = new Request<>();
        r.method = request.method;
        r.tag = tag;
        r.url = urlArgs == null ? request.url : String.format(request.url, urlArgs);
        r.resultClass = request.resultClass;
        r.resultIsList = request.resultIsList;
        r.cacheTime = cache;
        r.delayTime = delay;
        r.params = params;
        r.context = new WeakReference<>(context);
        if(listener == null) listener = new RequestListener<>();
        return request(r, listener);
    }
    public static <T> Response<T> request(
            Activity context,
            API<T> request,
            Object[] urlArgs,
            RequestListener<T> listener){
        return request(context, null, request, urlArgs, null, RequestCacheTime.NoCache, 0, listener);
    }
    public static <T> Response<T> request(
            Activity context,
            API<T> request,
            Object[] urlArgs,
            Map<String, String> params,
            RequestListener<T> listener){
        return request(context, null, request, urlArgs, params, RequestCacheTime.NoCache, 0, listener);
    }
    public static <T> Response<T> request(
            Activity context,
            API<T> request,
            Map<String, String> params,
            RequestListener<T> listener){
        return request(context, null, request, null, params, RequestCacheTime.NoCache, 0, listener);
    }
    public static <T> Response<T> request(
            Activity context,
            API<T> request,
            RequestListener<T> listener){
        return request(context, request, null, null, listener);
    }
    public static void cancelAll(String tag){
        getRequestQueue().cancelAll(tag);
        if(delayedTags.contains(tag)) delayedTags.remove(tag);
    }

    public static <T> void cancel(Response<T> response){
        response.canceled = true;
    }


    public static boolean needAuthToken(String url){
        if(url == null) return false;
        url = url.toLowerCase();
        if(!url.startsWith("http://") && !url.startsWith("https://")){
            // 是类似 /user/add 这种shortcut url
            return true;
        }

        Uri u = Uri.parse(url);
        String host = u.getHost();
        if(host == null) return false;
        if(host != null){
            // 干掉端口
            host = host.replaceAll("\\:\\d+", "");
        }
        for(String allowedHosts : officalHosts){

            if(allowedHosts.equals(host) || host.endsWith("." + allowedHosts)){
                return true;
            }
        }

        return false;
    }

    public static Map<String,String> map(String ... args){
        HashMap<String,String> map = new HashMap<String,String>();
        for(int i=0;i<args.length-1;i+=2)
            map.put(args[i], args[i+1]);
        return map;
    }
    final static String PREFIX_FILE_POST_FIELD = "|FILE|";
    public static String getFilePostField(File file, String filename, String mimetype){
        if(TextUtils.isEmpty(filename))
            filename = "";
        if(TextUtils.isEmpty(mimetype))
            mimetype = "";
        return PREFIX_FILE_POST_FIELD + file.getAbsolutePath() + "|" + filename + "|" + mimetype;
    }
    static boolean isFilePostField(String field){
        return field != null && field.startsWith(PREFIX_FILE_POST_FIELD);
    }
    static FileBody getFileFileNameAndMimeTypeFromFilePostField(String field){
        if(!isFilePostField(field)) return null;
        String [] parts = field.substring(PREFIX_FILE_POST_FIELD.length()).split("\\|");
        if(parts == null || parts.length == 0) return null;
        File file = new File(parts[0]);
        if(file == null || !file.exists()) return null;
        String name = file.getName();
        if(parts.length > 1 && !TextUtils.isEmpty(parts[1])) name = parts[1];
        String mimeType = "application/octet-stream";
        if(parts.length > 2 && !TextUtils.isEmpty(parts[2])) mimeType = parts[2];
        String charset = "binary";
        if(parts.length > 3 && !TextUtils.isEmpty(parts[3])) charset = parts[3];
        return new FileBody(file, name, mimeType, charset);
    }

    public static String getFilePostField(File file){
        return getFilePostField(file, null, null);
    }
    static final String RAW_FIELD_KEY = "|RAW|";
    static final String RAW_TYPE_FIELD_KEY = "|RAW_TYPE|";
    static final String ADDITIONAL_HEADER_FIELD_KEY = "|HEADER|";
    public static Map<String,String> rawBody(String raw, String type, String ... args){
        Map<String,String>  r = map(args);
        r.put(RAW_FIELD_KEY, raw);
        r.put(RAW_TYPE_FIELD_KEY, type);
        return r;
    }
    public static String additionalHeader(String key){
        return ADDITIONAL_HEADER_FIELD_KEY + key;
    }
    static boolean isRawParamKey(String k){
        return RAW_FIELD_KEY.equals(k);
    }
    static boolean isRawTypeParamKey(String k){
        return RAW_TYPE_FIELD_KEY.equals(k);
    }
    static boolean isAdditionalHeaderParamKey(String k){
        return k != null && k.startsWith(ADDITIONAL_HEADER_FIELD_KEY);
    }
    static String getAdditionalHeaderKey(String k){
        return k.substring(ADDITIONAL_HEADER_FIELD_KEY.length());
    }
}
