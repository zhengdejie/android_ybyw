package appframe.appframe.app;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.TextView;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.wxlib.util.SysUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
//import com.crashlytics.android.Crashlytics;
import com.github.snowdream.android.util.Log;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.ConfigCache;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.InitHelper;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.MemoryCache;
import appframe.appframe.utils.Utils;
import cn.smssdk.SMSSDK;
//import io.fabric.sdk.android.Fabric;

/**
 * Created by dashi on 15/6/11.
 */
public class App extends Application {
    public static App instance;
    //    public final String APP_KEY = "23243435";
//    public static YWIMKit mIMKit;
    private static Context sContext;

    public  static IWXAPI msgApi;

    public static Context getContext(){
        return sContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Http.init(this);
        Http.registerUrlPrefix("::", API.API_BASE);
        Http.setOfficalHosts(API.OFFICAL_HOSTS);
        Auth.init(this);

        ConfigCache.init(this);

        sContext = getApplicationContext();
//		YWEnvManager.prepare(sContext, YWEnvType.PRE);
        //SDK初始化
        //LoginSampleHelper.getInstance().initSDK_Sample(this);
        //Application.onCreate中，首先执行这部分代码, 因为，如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
        //特别注意:这段代码不能封装到其他方法中，必须在onCreate顶层代码中!
        //以下代码固定在此处，不要改动
        SysUtil.setApplication(this);
        if(SysUtil.isTCMSServiceProcess(this)){
            return;  //特别注意：此处return是退出onCreate函数，因此不能封装到其他任何方法中!
        }
        //以上代码固定在这个位置，不要改动

        //初始化云旺SDK
        InitHelper.initYWSDK(this);

//        //IM聊天配置
//        YWAPI.init(this,APP_KEY);
//        mIMKit = YWAPI.getIMKitInstance();
//        //IM
//        // 通知推送
//        mIMKit.setEnableNotification(true);
//        //开启通知栏提示
//        mIMKit.setAppName("友问友帮");
//        // 通知栏显示的名称
//        mIMKit.setResId(R.drawable.aliwx_notification_bg);
//        //开发者可以换成自定义的Icon
//        Intent intent = mIMKit.getConversationActivityIntent();
//        //开发者可以使用openIM提供的intent也可以使用自定义的intent
//        mIMKit.setNotificationIntent(intent); //通知栏点击跳转

        Log.setEnabled(true);
        Log.setLog2ConsoleEnabled(true);

        SMSSDK.initSDK(this, "1098959928c6e", "7028d351c316b5bd360232ae00e67efb");

        msgApi = WXAPIFactory.createWXAPI(this, null);
//
//        // 将该app注册到微信
        msgApi.registerApp(AppConfig.WX_APP_ID);

//        Fabric.with(this, new Crashlytics());
    }


}
