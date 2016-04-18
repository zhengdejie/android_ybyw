package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView tb_title,tb_back,tv_topup,tv_withdraw,tv_deposit,tv_revenue,tv_cost;
    private Switch switch_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    protected  void initdata()
    {
        Http.request(this, API.USER_PROFILE, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<UserDetail>() {
            @Override
            public void onSuccess(UserDetail result) {
                super.onSuccess(result);

                tv_deposit.setText("￥ " + result.getWalletTotal());
                tv_revenue.setText("￥ " + result.getTotalRevenue());
                tv_cost.setText("￥ " + result.getTotalExpense());
            }
        });
    }

    protected void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_topup = (TextView)findViewById(R.id.tv_topup);
        tv_withdraw = (TextView)findViewById(R.id.tv_withdraw);
        tv_deposit = (TextView)findViewById(R.id.tv_deposit);
        tv_revenue = (TextView)findViewById(R.id.tv_revenue);
        tv_cost = (TextView)findViewById(R.id.tv_cost);
        switch_cost = (Switch)findViewById(R.id.switch_cost);
        switch_cost.setChecked(Auth.getCurrentUser().isShowRevenueAndExpense());
        tb_title.setText("我的钱包");
        tb_back.setText("个人中心");

        tb_back.setOnClickListener(this);
        tv_topup.setOnClickListener(this);
        tv_withdraw.setOnClickListener(this);
        switch_cost.setOnCheckedChangeListener(this);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.switch_cost:
                if(isChecked == true) {

                    Http.request(WalletActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "ShowRevenueAndExpense", "true"
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);
                            // 上传成功
                            Auth.updateCurrentUser(result);
                        }
                    });
                }
                else
                {
                    Http.request(WalletActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "ShowRevenueAndExpense", "false"
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);
                            // 上传成功
                            Auth.updateCurrentUser(result);
                        }
                    });
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
            case R.id.tv_withdraw:
                startActivity(new Intent(this,WithdrawActivity.class));
                break;
            case R.id.tv_topup:
                startActivity(new Intent(this,TopupActivity.class));
                break;
        }
    }
}
