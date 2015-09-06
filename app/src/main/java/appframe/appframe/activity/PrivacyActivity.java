package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/8/24.
 */
public class PrivacyActivity extends BaseActivity implements View.OnClickListener{
    Button btn_seemyinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        init();
    }
    protected  void init()
    {
        btn_seemyinfo = (Button)findViewById(R.id.btn_seemyinfo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_seemyinfo:
                startActivity(new Intent(this,WhoCanSeeActivity.class));
                break;


        }
    }
}
