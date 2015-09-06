package appframe.appframe.app;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.github.snowdream.android.util.Log;

import java.util.List;

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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Http.init(this);
        Http.registerUrlPrefix("::", API.API_BASE);
        Http.setOfficalHosts(API.OFFICAL_HOSTS);
        Auth.init(this);

        ConfigCache.init(this);


        Log.setEnabled(true);
        Log.setLog2ConsoleEnabled(true);



    }


}
