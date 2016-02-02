package appframe.appframe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/12/11.
 */
public class AvatarZoomActivity extends BaseActivity implements View.OnClickListener {

    private com.android.volley.toolbox.NetworkImageView iv_showavatar;
    private RelativeLayout rl_avatarzoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatarzoom);
        init();
    }
    protected  void init() {
        rl_avatarzoom = (RelativeLayout) findViewById(R.id.rl_avatarzoom);
        iv_showavatar = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_showavatar);
        if (!getIntent().getStringExtra("Avatar").toString().equals("")) {
            ImageUtils.setImageUrl(iv_showavatar, getIntent().getStringExtra("Avatar").toString(), "0");
        }
        rl_avatarzoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_avatarzoom:
                finish();
                break;
        }
    }
}

