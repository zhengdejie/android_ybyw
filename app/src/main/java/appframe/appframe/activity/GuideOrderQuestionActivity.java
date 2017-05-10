package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.dto.RecommendOrder;
import appframe.appframe.dto.RecommendOrderQuestion;

/**
 * Created by Administrator on 2017-01-11.
 */

public class GuideOrderQuestionActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back,tv_title,tv_answer,tv_count,tv_joinmember,tv_information;
    RecommendOrderQuestion recommendOrderQuestion;
    Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_order_question);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_answer = (TextView) findViewById(R.id.tv_answer);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_joinmember = (TextView) findViewById(R.id.tv_joinmember);
        tv_information = (TextView) findViewById(R.id.tv_information);

        tb_title.setText("回答");
        tb_back.setOnClickListener(this);
        tv_information.setOnClickListener(this);

        getIntent = this.getIntent();
        recommendOrderQuestion = (RecommendOrderQuestion)getIntent.getSerializableExtra("RecommendOrderQuestion");
        tv_title.setText(recommendOrderQuestion.getQuestion());
        tv_answer.setText(recommendOrderQuestion.getAnswer());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_information:
                startActivity(new Intent(GuideOrderQuestionActivity.this, MembershipActivity.class));
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("公安局问题回答页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("公安局问题回答页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

