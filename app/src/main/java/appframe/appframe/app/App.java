package appframe.appframe.app;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.github.snowdream.android.util.Log;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.ConfigCache;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.MemoryCache;
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/11.
 */
public class App extends Application {
    public static App instance;
    public final String APP_KEY = "23243435";
    public static YWIMKit mIMKit;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Http.init(this);
        Http.registerUrlPrefix("::", API.API_BASE);
        Http.setOfficalHosts(API.OFFICAL_HOSTS);
        Auth.init(this);

        ConfigCache.init(this);


        //IM聊天配置
        YWAPI.init(this,APP_KEY);
        mIMKit = YWAPI.getIMKitInstance();
        //IM
        // 通知推送
        mIMKit.setEnableNotification(true);
        //开启通知栏提示
        mIMKit.setAppName("友问友帮");
        // 通知栏显示的名称
        mIMKit.setResId(R.drawable.aliwx_notification_bg);
        //开发者可以换成自定义的Icon
        Intent intent = mIMKit.getConversationActivityIntent();
        //开发者可以使用openIM提供的intent也可以使用自定义的intent
        mIMKit.setNotificationIntent(intent); //通知栏点击跳转

        Log.setEnabled(true);
        Log.setLog2ConsoleEnabled(true);



    }


}
