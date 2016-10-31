package appframe.appframe.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.app.AppConfig;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2015/8/24.
 */
public class NewMessageActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView tb_title,tb_back;
    private Switch switch_message;
    YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
    SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(NewMessageActivity.this,"Receive_Notification");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmessage);
        init();
    }
    protected void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        switch_message = (Switch)findViewById(R.id.switch_message);
        tb_back.setText("设置");
        tb_title.setText("新消息通知");
        tb_back.setOnClickListener(this);
        switch_message.setOnCheckedChangeListener(this);
        Boolean Notification = (Boolean)sharedPreferencesUtils.getParam("Notification", true);
        switch_message.setChecked(Notification);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.switch_message:
                if(isChecked == true) {
                    AppConfig.RECEIVE_NOTIFICATION = true;
                    imKit.setEnableNotification(true);

                    sharedPreferencesUtils.removeParam("Notification");
                    sharedPreferencesUtils.setParam("Notification",true);
                }
                else
                {
                    AppConfig.RECEIVE_NOTIFICATION = false;
                    imKit.setEnableNotification(false);

                    sharedPreferencesUtils.removeParam("Notification");
                    sharedPreferencesUtils.setParam("Notification",false);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新消息通知页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新消息通知页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
