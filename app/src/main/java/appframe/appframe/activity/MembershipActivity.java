package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;

import static appframe.appframe.R.id.tv_answer;

/**
 * Created by Administrator on 2017-01-12.
 */

public class MembershipActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back,tv_membership,tv_membershipcategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tv_membership = (TextView) findViewById(R.id.tv_membership);
        tv_membershipcategory = (TextView) findViewById(R.id.tv_membershipcategory);

        tb_back.setOnClickListener(this);
        tb_title.setText("会员说明");
//        tv_membership.setText("在引导消费板块中可以看到有关公检法的一系列问题");
        tv_membershipcategory.setText("公检法会员注册有以下几种：\n ·一个月  30天 * 1元/天 = 30元 \n ·一个月  30天 * 1元/天 = 30元");
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
        MobclickAgent.onPageStart("会员说明页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("会员说明页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

