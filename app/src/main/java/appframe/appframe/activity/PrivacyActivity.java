package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/8/24.
 */
public class PrivacyActivity extends BaseActivity implements View.OnClickListener{

    private TextView tb_title,tb_back,btn_seemyinfo,btn_sentmeinfo;
    LinearLayout ll_seemyinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        init();
    }
    protected  void init()
    {
        btn_seemyinfo = (TextView)findViewById(R.id.btn_seemyinfo);
        btn_sentmeinfo = (TextView)findViewById(R.id.btn_sentmeinfo);
        ll_seemyinfo = (LinearLayout)findViewById(R.id.ll_seemyinfo);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("个人中心");
        tb_title.setText("隐私设置");
        tb_back.setOnClickListener(this);
        ll_seemyinfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ll_seemyinfo:
                startActivity(new Intent(this,WhoCanSeeActivity.class));
                break;

            case R.id.tb_back:
                finish();
                break;

        }
    }
}
