package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/11/4.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back,bt_send,tv_contentcount;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        et_content = (EditText)findViewById(R.id.et_content);
        bt_send = (TextView)findViewById(R.id.bt_send);
        tv_contentcount = (TextView)findViewById(R.id.tv_contentcount);
        tb_back.setText("友帮");
        tb_title.setText("帮助与反馈");
        tb_back.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        et_content.addTextChangedListener(contentWatcher);
    }

    private TextWatcher contentWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            tv_contentcount.setText(String.format("%d/250",s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.bt_send:
                Http.request(FeedbackActivity.this, API.USER_FEEDBACK, new Object[]{Auth.getCurrentUserId()},Http.map(
                        "Content",et_content.getText().toString()
                ), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                        Toast.makeText(FeedbackActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("帮助与反馈页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("帮助与反馈页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
