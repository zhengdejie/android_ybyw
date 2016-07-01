package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.dto.UserBrief;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/8/7.
 */
public class EstimateActivity extends BaseActivity implements View.OnClickListener{
    RelativeLayout rl_selfestimate,rl_friendestimate,rl_orderestimate;
    de.hdodenhof.circleimageview.CircleImageView clv_avatar;
    com.android.volley.toolbox.NetworkImageView iv_avatar;
    TextView tb_title,tb_back;
    String userID;
    Intent myIntent = new Intent();
    UserBrief userBrief;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);
        init();
    }


    public void init()
    {
        rl_selfestimate = (RelativeLayout) findViewById(R.id.rl_selfestimate);
        rl_friendestimate = (RelativeLayout) findViewById(R.id.rl_friendestimate);
        rl_orderestimate = (RelativeLayout) findViewById(R.id.rl_orderestimate);
//        clv_avatar = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.clv_avatar);
        iv_avatar = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_avatar);

        rl_selfestimate.setOnClickListener(this);
        rl_friendestimate.setOnClickListener(this);
        rl_orderestimate.setOnClickListener(this);

        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("返回");
        tb_back.setOnClickListener(this);
        //userID = getIntent().getStringExtra("UserID");
        userBrief = (UserBrief)getIntent().getSerializableExtra("userBrief");
        ImageUtils.setImageUrl(iv_avatar, userBrief.getAvatar());
        if(userBrief.getGender().equals(getResources().getString(R.string.male).toString()))
        {
            iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
        }
        else
        {
            iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
        }
        tb_title.setText(String.format("%s的口碑", userBrief.getName()));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rl_selfestimate:
                myIntent.setClass(EstimateActivity.this, SelfEstimateActivity.class);
                myIntent.putExtra("UserID", String.valueOf(userBrief.getId()));
                startActivity(myIntent);
                break;
            case R.id.rl_friendestimate:
                myIntent.setClass(EstimateActivity.this,FriendEstimateActivity.class);
                myIntent.putExtra("UserID",String.valueOf(userBrief.getId()));
                startActivity(myIntent);
                break;
            case R.id.rl_orderestimate:
                myIntent.setClass(EstimateActivity.this, OrderEstimateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userBrief", userBrief);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                break;
            case R.id.tb_back:
                finish();
                break;

        }
    }
}
