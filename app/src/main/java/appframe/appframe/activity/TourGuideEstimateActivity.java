package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017/6/9.
 */

public class TourGuideEstimateActivity  extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back,tv_contentcount,tv_estimate;
    EditText edit_content;
    GuideTourOrder guideTourOrder;
    RatingBar rb_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourguideestimate);
        initViews();
        initData();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected  void initData()
    {
        guideTourOrder = (GuideTourOrder)getIntent().getSerializableExtra("GuideTourOrder");
    }

    protected void initViews()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        edit_content = (EditText)findViewById(R.id.edit_content);
        tv_contentcount = (TextView)findViewById(R.id.tv_contentcount);
        tv_estimate = (TextView)findViewById(R.id.tv_estimate);
        rb_rating = (RatingBar) findViewById(R.id.rb_rating);


        tb_back.setText("返回");
        tb_title.setText("评价");
        tb_back.setOnClickListener(this);
        tv_estimate.setOnClickListener(this);

        edit_content.addTextChangedListener(contentWatcher);


    }

    private TextWatcher contentWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            tv_contentcount.setText(String.format("%d/140",s.length()));
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
            case R.id.tv_estimate:
                if(rb_rating.getRating() > 0) {
                    Http.request(TourGuideEstimateActivity.this, API.GUIDECOMMENT, new Object[]{guideTourOrder.getId()}, Http.map(
                            "Comment", edit_content.getText().toString().equals("") ? " ": edit_content.getText().toString(),
                            "Stars",String.valueOf((int)rb_rating.getRating())
                            ),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(final String result) {
                                    super.onSuccess(result);

                                    Toast.makeText(TourGuideEstimateActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }


                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);
                                }
                            });
                }
                else
                {
                    Toast.makeText(this,"请您评分",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("乐游评论页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("乐游评论页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
