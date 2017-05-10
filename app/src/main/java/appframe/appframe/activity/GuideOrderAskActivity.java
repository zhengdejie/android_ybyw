package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.QuestionWithAnswers;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXAnswerAdapater;

import static appframe.appframe.R.id.btn_comment;
import static appframe.appframe.R.id.iv_gender;
import static appframe.appframe.R.id.lv_ordercomment;
import static appframe.appframe.R.id.tv_comment;
import static appframe.appframe.R.id.tv_content;
import static appframe.appframe.R.string.comment;

/**
 * Created by Administrator on 2017-01-12.
 */

public class GuideOrderAskActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back,tv_ok,tv_title,tv_content;
    EditText et_ask;
    RelativeLayout rl_successresult;
    ImageView iv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_order_ask);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        et_ask = (EditText) findViewById(R.id.et_ask);
        rl_successresult = (RelativeLayout) findViewById(R.id.rl_successresult);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_title = (ImageView) findViewById(R.id.iv_title);

        tb_title.setText("咨询问题");
        tb_back.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        rl_successresult.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_ok:
                Http.request(GuideOrderAskActivity.this, API.USERASKQUESTION,  Http.map(
                        "Question", et_ask.getText().toString()),

                        new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);
                                tv_title.setText("提问成功");
                                iv_title.setImageDrawable(getResources().getDrawable(R.drawable.published_successfully));
                                tv_content.setText("对方已收到您的提问，请耐心等待~");
                                rl_successresult.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFail(String code) {
                                super.onFail(code);

                                tv_title.setText("提问失败");
                                iv_title.setImageDrawable(getResources().getDrawable(R.drawable.published_failure));
                                tv_content.setText("请重新提问~");
                                rl_successresult.setVisibility(View.VISIBLE);

                            }
                        });
                break;
            case R.id.rl_successresult:
                rl_successresult.setVisibility(View.GONE);
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

