package appframe.appframe.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.utils.UriHandler;

/**
 * Created by Administrator on 2015/8/24.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_title,tb_back,tv_score,tv_help,tv_protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_score = (TextView)findViewById(R.id.tv_score);
        tv_help = (TextView)findViewById(R.id.tv_help);
        tv_protocol = (TextView)findViewById(R.id.tv_protocol);
        tb_back.setText("设置");
        tb_title.setText("关于友帮");
        tb_back.setOnClickListener(this);
        tv_score.setOnClickListener(this);
        tv_help.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_score:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.tv_help:
                startActivity(new Intent(AboutActivity.this, ComplainActivity.class));
                break;
            case R.id.tv_protocol:
                UriHandler.openWebActivity(AboutActivity.this, "http://www.ubangwang.com/docs/zhulong_youbang_software_license_and_services_agreement.html");
                break;
        }
    }
}
