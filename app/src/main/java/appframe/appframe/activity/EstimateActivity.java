package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.snowdream.android.util.Log;

import appframe.appframe.R;
import appframe.appframe.activity.BaseActivity;
import appframe.appframe.activity.FriendEstimateActivity;
import appframe.appframe.activity.MyInfoActivity;
import appframe.appframe.activity.OrderEstimateActivity;
import appframe.appframe.activity.PrivacyActivity;
import appframe.appframe.activity.SelfEstimateActivity;
import appframe.appframe.activity.SettingActivity;

/**
 * Created by Administrator on 2015/8/7.
 */
public class EstimateActivity extends BaseActivity implements View.OnClickListener{
    Button btn_self,btn_friend,btn_order;
    TextView tb_title,tb_back;
    String userID;
    Intent myIntent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_estimate);
        init();
    }


    public void init()
    {
        btn_self = (Button) findViewById(R.id.btn_self);
        btn_friend = (Button) findViewById(R.id.btn_friend);
        btn_order = (Button) findViewById(R.id.btn_order);
        btn_self.setOnClickListener(this);
        btn_friend.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title.setText("TA的口碑");
        tb_back.setText("帮友资料");
        tb_back.setOnClickListener(this);
        userID = getIntent().getStringExtra("UserID");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_self:
                myIntent.setClass(EstimateActivity.this, SelfEstimateActivity.class);
                myIntent.putExtra("UserID", userID);
                startActivity(myIntent);
                break;
            case R.id.btn_friend:
                myIntent.setClass(EstimateActivity.this,FriendEstimateActivity.class);
                myIntent.putExtra("UserID",userID);
                startActivity(myIntent);
                break;
            case R.id.btn_order:
                myIntent.setClass(EstimateActivity.this,OrderEstimateActivity.class);
                myIntent.putExtra("UserID",userID);
                startActivity(myIntent);
                break;
            case R.id.tb_back:
                finish();
                break;

        }
    }
}
