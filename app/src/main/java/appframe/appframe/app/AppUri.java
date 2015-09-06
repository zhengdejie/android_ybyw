package appframe.appframe.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import appframe.appframe.activity.SplashActivity;
import appframe.appframe.utils.Auth;

/**
 * Created by dashi on 15/6/21.
 */
public final class AppUri {
    // webview 里可以调用的 url
    public static Intent handleUri(Context context, Uri uri, int flag){
        if(uri == null || !"app".equalsIgnoreCase(uri.getScheme())) return null;
        if("logout".equalsIgnoreCase(uri.getHost())){
            // app://logout
            Auth.login(null, null);
            return SplashActivity.getIntentForRootActivity(context);
        }else if("home".equalsIgnoreCase(uri.getHost())){
            // app://home
            return SplashActivity.getIntentForRootActivity(context);
        }
        return null;
    }
}
