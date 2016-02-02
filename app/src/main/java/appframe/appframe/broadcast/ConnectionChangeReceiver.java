package appframe.appframe.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.github.mrengineer13.snackbar.SnackBar;
import com.igexin.sdk.PushManager;

import appframe.appframe.activity.HomeActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.fragment.OrderFragment;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.NotificationInitSampleHelper;

/**
 * Created by Administrator on 2016/1/5.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver{

    private LoginSampleHelper loginHelper;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            this.context = context;
            ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);

            //Wifi
            NetworkInfo wifiState = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI);
            //3G
            NetworkInfo mobState = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE);

            if (!mobState.isConnected() && !wifiState.isConnected()) {
                // unconnect network

                OrderFragment of= new OrderFragment();
                new SnackBar.Builder(of.getActivity())
                        .withMessage("网络连接不可用")
                        .withStyle(SnackBar.Style.ALERT)
                        .withDuration(SnackBar.PERMANENT_SNACK)
                        .withBackgroundColorId(android.R.color.holo_red_dark)
                        .show();
            } else {
                // connect network
                OrderFragment of= new OrderFragment();
                new SnackBar.Builder(of.getActivity()).show().hide();
                IMLogin();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void IMLogin()
    {
        //IMCore.getLoginState
        //初始化imkit
        loginHelper = LoginSampleHelper.getInstance();
        loginHelper.initIMKit(String.valueOf(Auth.getCurrentUserId()), loginHelper.APP_KEY);
        //自定义头像和昵称回调初始化(如果不需要自定义头像和昵称，则可以省去)
        //UserProfileSampleHelper.initProfileCallback();
        //通知栏相关的初始化
        NotificationInitSampleHelper.init();
        if (YWChannel.getInstance().getNetWorkState().isNetWorkNull()) {
//            IMLogin();
            return;
        }
        Log.i("-----ClientID:------", String.format("%s", loginHelper.getIMKit().getIMCore().getLoginState()));
        if (loginHelper.getIMKit().getIMCore().getLoginState() != YWLoginState.success) {

            loginHelper.login_Sample(String.valueOf(Auth.getCurrentUserId()), "1", loginHelper.APP_KEY, new IWxCallback() {

                @Override
                public void onSuccess(Object... arg0) {
                    PushManager.getInstance().initialize(context);

                    Http.request((Activity)App.getContext(), API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "GTClientID", PushManager.getInstance().getClientid(context)
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);


                        }
                    });

                }

                @Override
                public void onProgress(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int errorCode, String errorMessage) {
                    if (errorCode == YWLoginCode.LOGON_FAIL_INVALIDUSER || errorCode == YWLoginCode.LOGON_FAIL_INVALIDPWD
                            || errorCode == YWLoginCode.LOGON_FAIL_EMPTY_ACCOUNT || errorCode == YWLoginCode.LOGON_FAIL_EMPTY_PWD
                            || errorCode == YWLoginCode.LOGON_FAIL_INVALIDSERVER) {
                        Log.e("IM ERROR", "登录失败 错误码：" + errorCode + "  错误信息：" + errorMessage);
                    } else {
                        Log.e("IM ERROR", "登录失败 错误码：" + errorCode + "  错误信息：" + errorMessage);
                    }
                    IMLogin();
                }
            });
        }
    }

}
