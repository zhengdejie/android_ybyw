package appframe.appframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import appframe.appframe.activity.FramedWebViewActivity;
import appframe.appframe.activity.SplashActivity;
import appframe.appframe.app.AppUri;


/**
 * Created by dashi on 15/4/28.
 */
public class UriHandler {

    static String APP_SCHEMA = "app";
    public static final String URL_WF_PREFIX = "wf-";
    public static final String URL_NEW_WEBVIEW_PREFIX = "wf-";
    public static final String URL_NEW_MODAL_WEBVIEW_PREFIX = "wf-m-";
    public static final String URL_REPLACE_WEBVIEW_PREFIX = "wf-r-";
    public static boolean isAppSchema(Uri uri){
        return uri.getScheme().equalsIgnoreCase(APP_SCHEMA) || uri.getScheme().startsWith(URL_WF_PREFIX);
    }
    public static boolean isNewWebView(Uri uri){
        String lower = uri.getScheme().toLowerCase();
        return lower.startsWith(URL_NEW_WEBVIEW_PREFIX);
    }
    public static boolean isNewModalWebView(Uri uri){
        String lower = uri.getScheme().toLowerCase();
        return lower.startsWith(URL_NEW_MODAL_WEBVIEW_PREFIX);
    }
    public static boolean isReplaceWebView(Uri uri){
        String lower = uri.getScheme().toLowerCase();
        return lower.startsWith(URL_REPLACE_WEBVIEW_PREFIX);

    }

    public static void openWebActivity(Context context, String url){
        url = Http.formatUrl(url);
        handleUrl(context, Uri.parse(URL_NEW_WEBVIEW_PREFIX + url));
    }
    public static void handleUrl(Context context, Uri uri){
        handleUrl(context, uri, true, 0);
    }
    public static void handleUrl(Context context, Uri uri, boolean allowExternalUri, int flag){
        if(isAppSchema(uri)){
            Intent i = null;
            Class<?> clazz = null;
            if(uri.getScheme().startsWith(URL_WF_PREFIX)){
                clazz = FramedWebViewActivity.class;
            }else{
                i = AppUri.handleUri(context, uri, flag);
                if(i == null) {
                    Toast.makeText(context, "不认得这个uri: " + uri.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(i == null)
                i = new Intent(context, clazz);
            if(flag != 0)
                i.setFlags(flag);

            if(!(context instanceof Activity))
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if(isNewModalWebView(uri)){
                i.setData(Uri.parse(uri.toString().substring(URL_NEW_MODAL_WEBVIEW_PREFIX.length())));
                if(context instanceof Activity)
                    ((Activity)context).startActivityForResult(i, WebViewCommonHandlers.REQUEST_CODE_START_NEW_WEBVIEW);
                else
                    context.startActivity(i);
            }else if(isReplaceWebView(uri)){
                i.setData(Uri.parse(uri.toString().substring(URL_REPLACE_WEBVIEW_PREFIX.length())));
                if(context instanceof Activity) {
                    ((Activity) context).startActivityForResult(i, WebViewCommonHandlers.REQUEST_CODE_START_NEW_WEBVIEW);
                    ((Activity) context).finish();
                }else
                    context.startActivity(i);

            } else if(isNewWebView(uri)){
                i.setData(Uri.parse(uri.toString().substring(URL_NEW_WEBVIEW_PREFIX.length())));
                if(context instanceof Activity)
                    ((Activity)context).startActivityForResult(i, WebViewCommonHandlers.REQUEST_CODE_START_NEW_WEBVIEW);
                else
                    context.startActivity(i);
            }else {
                i.setData(uri);
                context.startActivity(i);
            }

            return;
        }
        if(allowExternalUri){
            try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(browserIntent);
            }catch(Exception e){
                Toast.makeText(context, "打开失败: " + uri.toString(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "打开出错: " + uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
