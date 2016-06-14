package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/8/24.
 */
public class PrivacyActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private TextView tb_title,tb_back,tv_cannotsee,tv_whocannotsee,tv_blacklist;
    private Switch switch_buyservice,switch_costservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        init();
    }
    protected  void init()
    {
        tv_cannotsee = (TextView)findViewById(R.id.tv_cannotsee);
        tv_whocannotsee = (TextView)findViewById(R.id.tv_whocannotsee);
        tv_blacklist = (TextView)findViewById(R.id.tv_blacklist);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        switch_buyservice = (Switch)findViewById(R.id.switch_buyservice);
        switch_costservice = (Switch)findViewById(R.id.switch_costservice);
        switch_buyservice.setChecked(Auth.getCurrentUser().isShowConfirmedOrderHistoryAsReceiver());
        switch_costservice.setChecked(Auth.getCurrentUser().isShowConfirmedOrderHistoryAsProvider());
        tb_back.setText("我的");
        tb_title.setText("隐私设置");
        tb_back.setOnClickListener(this);
        tv_cannotsee.setOnClickListener(this);
        tv_whocannotsee.setOnClickListener(this);
        tv_blacklist.setOnClickListener(this);
        switch_buyservice.setOnCheckedChangeListener(this);
        switch_costservice.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tv_cannotsee:
                startActivity(new Intent(this,CannotSeeActivity.class));
                break;

            case R.id.tv_whocannotsee:
                startActivity(new Intent(this,WhoCannotSeeActivity.class));
                break;

            case R.id.tv_blacklist:
                startActivity(new Intent(this,BlackListActivity.class));
                break;

            case R.id.tb_back:
                finish();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.switch_buyservice:
                if(isChecked == true) {

                    Http.request(PrivacyActivity.this, API.OPEN_TRADEHISTORY, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "Type","2",
                            "Show", "true"
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
                    Http.request(PrivacyActivity.this, API.OPEN_TRADEHISTORY, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "Type","2",
                            "Show", "false"
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

            case R.id.switch_costservice:
                if(isChecked == true) {

                    Http.request(PrivacyActivity.this, API.OPEN_TRADEHISTORY, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "Type","1",
                            "Show", "true"
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
                    Http.request(PrivacyActivity.this, API.OPEN_TRADEHISTORY, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "Type","1",
                            "Show", "false"
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

}
