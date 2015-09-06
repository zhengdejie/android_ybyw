package appframe.appframe.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import appframe.appframe.dto.UserDetail;

/**
 * Created by dashi on 15/6/21.
 */
public final class Auth {
    static Application app;
    static String token;
    static UserDetail user;
    public static boolean isLoggedIn(){
        return TextUtils.isEmpty(token) ? false : true;
    }
    public static String getToken(){
        return token;
    }
    public static UserDetail getCurrentUser(){
        return user;
    }
    public static int getCurrentUserId(){
        if(!isLoggedIn()) return 0;
        UserDetail ud = getCurrentUser();
        if(ud == null) return 0;
        return ud.Id;
    }
    public static String getCurrentUserMobile(){
        if(!isLoggedIn()) return null;
        UserDetail ud = getCurrentUser();
        if(ud == null) return null;
        return ud.Mobile;
    }
    public static void login(String t, UserDetail u){
        if(TextUtils.isEmpty(t) != (u == null)) throw new Error();
        token = t;
        user = u;
        Http.setAuthorizationToken(token);
        SharedPreferences.Editor e = app.getSharedPreferences("Auth", Context.MODE_PRIVATE).edit();
        if(TextUtils.isEmpty(t)){
            e.remove("token");
            e.remove("user");
        }else{
            e.putString("token", token);
            e.putString("user", GsonHelper.getGson().toJson(user));
        }
        e.commit();
    }
    public static void init(Application a){
        app = a;

        SharedPreferences sp = app.getSharedPreferences("Auth", Context.MODE_PRIVATE);
        token = sp.getString("token", null);
        if (!TextUtils.isEmpty(token)){

            String userString = sp.getString("user", null);
            if(TextUtils.isEmpty(userString)){
                // 有 token 没有 user
                token = null;
                user = null;
            }else {
                user = GsonHelper.getGson().fromJson(userString, UserDetail.class);
            }
        }
        Http.setAuthorizationToken(token);
    }
}
