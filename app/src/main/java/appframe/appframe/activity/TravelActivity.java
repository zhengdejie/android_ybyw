package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;

/**
 * Created by Administrator on 2017/5/11.
 */

public class TravelActivity extends BaseActivity implements View.OnClickListener{
    private TextView tb_title,tb_back,tv_topup,tv_withdraw,tv_deposit,tv_revenue,tv_cost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

        MobclickAgent.onPageStart("我的钱包页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    protected  void initData()
    {

    }

    protected void initViews()
    {



    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_withdraw:
                startActivity(new Intent(this,WithdrawActivity.class));
                break;
            case R.id.tv_topup:
                startActivity(new Intent(this,TopupActivity.class));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的钱包页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
