package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/8/24.
 */
public class PrivacyActivity extends BaseActivity implements View.OnClickListener{

    private TextView tb_title,tb_back,tv_cannotsee,tv_whocannotsee,tv_blacklist;
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
        tb_back.setText("个人中心");
        tb_title.setText("隐私设置");
        tb_back.setOnClickListener(this);
        tv_cannotsee.setOnClickListener(this);
        tv_whocannotsee.setOnClickListener(this);
        tv_blacklist.setOnClickListener(this);
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
}
