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
import com.umeng.analytics.MobclickAgent;
//import com.umeng.socialize.PlatformConfig;

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
public class App extends android.support.multidex.MultiDexApplication {
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
//todo Application.onCreate中，首先执行这部分代码，以下代码固定在此处，不要改动，这里return是为了退出Application.onCreate！！！
        if(mustRunFirstInsideApplicationOnCreate()){
            //todo 如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
            return;
        }

        //初始化云旺SDK
        InitHelper.initYWSDK(this);

        Log.setEnabled(true);
        Log.setLog2ConsoleEnabled(true);

        SMSSDK.initSDK(this, "1098959928c6e", "7028d351c316b5bd360232ae00e67efb");

        msgApi = WXAPIFactory.createWXAPI(this, null);
//
//        // 将该app注册到微信
        msgApi.registerApp(AppConfig.WX_APP_ID);

        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

//        PlatformConfig.setWeixin("wxfcdc5b3ad4fb99f4", "008ca4059daadb4395fa319ca743ac45");
        //微信 appid appsecret
//        Fabric.with(this, new Crashlytics());
    }
    private boolean mustRunFirstInsideApplicationOnCreate() {
        //必须的初始化
        SysUtil.setApplication(this);

        return SysUtil.isTCMSServiceProcess(sContext);
    }

}
