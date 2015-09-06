package appframe.appframe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import appframe.appframe.R;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.UriHandler;
import appframe.appframe.utils.Utils;
import appframe.appframe.utils.WebViewCommonHandlers;

/**
 * Created by dashi on 15/6/21.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if(Auth.isLoggedIn()){
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else{

                    Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                finish();
            }
        };
        if(getIntent().getBooleanExtra("no-delay", false)){
            r.run();
            return;
        }
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(r, 500);
    }
    public static Intent getIntentForRootActivity(Context a){
        Intent i = new Intent(a, SplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("no-delay", true);
        return i;
    }
    public static void startRootActivity(Activity a){
        a.startActivity(getIntentForRootActivity(a));
    }
}

