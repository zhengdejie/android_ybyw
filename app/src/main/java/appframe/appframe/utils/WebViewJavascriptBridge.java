package appframe.appframe.utils;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.snowdream.android.util.Log;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import appframe.appframe.R;


/**
 * Created with IntelliJ IDEA. User: jack_fang Date: 13-8-15 Time: 下午6:08
 */
public class WebViewJavascriptBridge {
    static final String TAG = "WebViewJavascriptBridge";

    WebView mWebView;
    WebViewCommonHandlers.WebViewTab mContext;
    WVJBHandler _messageHandler;
    Map<String, WVJBHandler> _messageHandlers;
    Map<String, WVJBResponseCallback> _responseCallbacks;
    long _uniqueId;

    WebChromeClient mChromeClient;
    int mSteps = 0;

    Handler mHandler = new Handler();

    static Gson mGson = GsonHelper.getGson();



    @SuppressLint("SetJavaScriptEnabled")
    public WebViewJavascriptBridge(WebViewCommonHandlers.WebViewTab context, WebView webview,
                                   WebChromeClient cc) {
        this.mContext = context;
        this.mWebView = webview;
        this._messageHandler = new UserServerHandler();
        _messageHandlers = new HashMap<String, WVJBHandler>();
        _responseCallbacks = new HashMap<String, WVJBResponseCallback>();
        _uniqueId = 0;
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "_WebViewJavascriptBridge");
        mWebView.setWebViewClient(new MyWebViewClient());
        mChromeClient = cc;
        mWebView.setWebChromeClient(cc); // optional, for show console and alert
    }

    public void clearUrl(){

        WebViewCommonHandlers.webViewBeginLoad(mContext.getPage(), this, null);
    }


    public void loadUrl(String url) {
        WebViewCommonHandlers.webViewBeginLoad(mContext.getPage(), this, url);
        if (WebViewCommonHandlers.isTrustedDomain(Uri.parse(url))) {
            Log.i(TAG, "loadUrlWithHeaders: " + url);

            mWebView.loadUrl(url, WebViewCommonHandlers.getHeadersForWebViewRequest());
        } else {
            Log.i(TAG, "loadUrl: " + url);
            mWebView.loadUrl(url);
        }
        loadDocumentTitle();
    }

    class UserServerHandler implements WebViewJavascriptBridge.WVJBHandler {

        @Override
        public void handle(String data,
                           WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
            if (null != jsCallback) {
                jsCallback.callback("null", true);
            }
            WebViewJavascriptBridge.this.send("I expect a response!", false,
                    new WebViewJavascriptBridge.WVJBResponseCallback() {

                        @Override
                        public void callback(String responseData, boolean escaped) {
                        }
                    });
            WebViewJavascriptBridge.this.send("Hi", false);
        }
    }

    private void loadWebViewJavascriptBridgeJs(WebView webView) {
        InputStream is = mContext.getActivity().getResources().openRawResource(
                R.raw.webviewjavascriptbridge);
        String script = convertStreamToString(is);
        webView.loadUrl("javascript:" + script);
    }

    public int getSteps() {
        return mSteps;
    }

    public void increaseSteps() {
        mSteps++;
    }

    public void desreaseSteps() {
        mSteps--;
    }

    @SuppressWarnings("resource")
    public static String convertStreamToString(java.io.InputStream is) {
        String s = "";
        try {
            Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
            if (scanner.hasNext())
                s = scanner.next();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }
    @TargetApi(19)
    void doLoadDocumentTitleAfter19(){
        if(mChromeClient != null){
            mWebView.evaluateJavascript("document.title", new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                if(value != null)
                    mChromeClient.onReceivedTitle(mWebView, mGson.fromJson(value, String.class));
                }
            });
        }
    }

    void loadDocumentTitle(){
        doLoadDocumentTitle();
        mHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                doLoadDocumentTitle();
                mHandler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        doLoadDocumentTitle();
                    }
                }, 300);
            }
        }, 300);
    }

    void doLoadDocumentTitle(){
        if( android.os.Build.VERSION.SDK_INT >= 19){
            doLoadDocumentTitleAfter19();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed();
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if(UriHandler.isAppSchema(uri)){
                UriHandler.handleUrl(mContext.getActivity(), uri, true, 0);
                return true;
            }


            if (WebViewCommonHandlers.isTrustedDomain(uri)) {
                Log.i(TAG, "overrideLoadUrlWithHeaders: " + url);
                view.loadUrl(url, WebViewCommonHandlers.getHeadersForWebViewRequest());
                return true;
            }
            WebViewCommonHandlers.webViewBeginLoad(mContext.getPage(), WebViewJavascriptBridge.this, url);
            Log.i(TAG, "overrideLoadUrl: " + url);
            return super.shouldOverrideUrlLoading(view, url);
        }



        @Override
        public void onPageFinished(WebView webView, String url) {
            if (WebViewCommonHandlers.isTrustedDomain(Uri.parse(url))) {

                loadWebViewJavascriptBridgeJs(webView);

                // 把 headers 送给 client
                String javascriptCommand = String.format(
                        "javascript:window.wfRequestHeaders = %s;window.appCurrentUserId = %d;window.appCurrentUser = %s;",
                        mGson.toJson(WebViewCommonHandlers.getHeadersForWebViewRequest()),
                        Auth.getCurrentUserId(),
                        Auth.isLoggedIn() ? mGson.toJson(Auth.getCurrentUser()) : "null"
                        );
                mWebView.loadUrl(javascriptCommand);


            }
            mSteps++;
            loadDocumentTitle();

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            WebSettings settings = view.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            mWebView.loadData(
                    String.format("<style>div,small{text-align: center} body{padding: 10px 0; margin: 0} div {padding: 3px 0} small {color: #ccc; display: block} .footer{position: absolute; bottom: 10px; width: 100%%; color: #eee}</style><div>载入错误</div><small>请下拉刷新重试</small><small class='footer'>%s</small>",
                            description),
                    "text/html; charset=utf-8",
                    "utf-8");
        }

    }

    public interface WVJBHandler {
        public void handle(String data, WVJBResponseCallback jsCallback);
    }

    public interface WVJBResponseCallback {
        public void callback(String data, boolean escaped);
    }

    public void registerHandler(String handlerName, WVJBHandler handler) {
        _messageHandlers.put(handlerName, handler);
    }

    private class CallbackJs implements WVJBResponseCallback {
        private final String callbackIdJs;

        public CallbackJs(String callbackIdJs) {
            this.callbackIdJs = callbackIdJs;
        }

        @Override
        public void callback(String data, boolean escaped) {
            _callbackJs(callbackIdJs, data, escaped);
        }
    }

    private void _callbackJs(String callbackIdJs, String data, boolean escaped) {
        _dispatchMessage(String.format("{\"responseId\":%s,\"responseData\":%s}",
                GsonHelper.getGson().toJson(callbackIdJs),
                escaped ? data : GsonHelper.getGson().toJson(data)));
    }

    @JavascriptInterface
    public void _handleMessageFromJs(String data, String responseId,
                                     String responseData, String callbackId, String handlerName,
                                     String jsonMsg) {
        if (null != responseId) {
            WVJBResponseCallback responseCallback = _responseCallbacks
                    .get(responseId);
            responseCallback.callback(responseData, false);
            _responseCallbacks.remove(responseId);
        } else {
            WVJBResponseCallback responseCallback = null;
            if (null != callbackId) {
                responseCallback = new CallbackJs(callbackId);
            }
            WVJBHandler handler;
            if (null != handlerName) {
                handler = _messageHandlers.get(handlerName);
                if (null == handler) {
                    return;
                }
            } else {
                handler = _messageHandler;
            }
            try {
                handler.handle(jsonMsg, responseCallback);
            } catch (Exception exception) {
                Log.e(TAG, "error handling js bridge call", exception);
            }
        }
    }

    public void send(String data, boolean escaped) {
        send(data, escaped, null);
    }

    public void send(String data, boolean escaped, WVJBResponseCallback responseCallback) {
        _sendData(data, escaped, responseCallback, null);
    }

    private void _sendData(String data, boolean escaped, WVJBResponseCallback responseCallback,
                           String handlerName) {

        String callbackJSONPart = "";
        if (null != responseCallback) {
            String callbackId = "java_cb_" + (++_uniqueId);
            _responseCallbacks.put(callbackId, responseCallback);
            callbackJSONPart = String.format(",\"callbackId\":%s", GsonHelper.getGson().toJson(callbackId));
        }
        String handlerNameJSONPart = "";
        if (null != handlerName) {
            handlerNameJSONPart = String.format(",\"handlerName\":%s", GsonHelper.getGson().toJson(handlerName));
        }
        String dataJSON = escaped ? data : (data == null ? "null" : GsonHelper.getGson().toJson(data));
        _dispatchMessage(String.format("{\"data\":%s%s%s}", dataJSON, callbackJSONPart, handlerNameJSONPart));
    }

    private void _dispatchMessage(String messageJSON){
        String javascriptCommand = String
                .format("javascript:WebViewJavascriptBridge._handleMessageFromJava(%s);",
                        GsonHelper.getGson().toJson( messageJSON) );
        mWebView.loadUrl(javascriptCommand);
    }

    public void callHandler(String handlerName) {
        callHandler(handlerName, null, false, null);
    }

    public void callHandler(String handlerName, String data, boolean escaped) {
        callHandler(handlerName, data, escaped, null);
    }

    public void callHandler(String handlerName, String data, boolean escaped,
                            WVJBResponseCallback responseCallback) {
        _sendData(data, escaped, responseCallback, handlerName);
    }

}
