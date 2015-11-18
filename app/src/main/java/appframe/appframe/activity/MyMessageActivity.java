package appframe.appframe.activity;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;

import appframe.appframe.R;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.OpenConversationSampleHelper;

/**
 * Created by Administrator on 2015/11/12.
 */
public class MyMessageActivity extends FragmentActivity implements View.OnClickListener{
    private TextView tb_title,tb_back,tv_systemmessage,tv_ordermessage,tv_friendmessage;
    private Fragment fgm_recentcontacter;
    private LinearLayout lv_fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymessage);
        init();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_systemmessage = (TextView)findViewById(R.id.tv_systemmessage);
        tv_ordermessage = (TextView)findViewById(R.id.tv_ordermessage);
        tv_friendmessage = (TextView)findViewById(R.id.tv_friendmessage);
        tb_back.setText("个人中心");
        tb_title.setText("我的消息");
        tb_back.setOnClickListener(this);
        tv_systemmessage.setOnClickListener(this);
        tv_ordermessage.setOnClickListener(this);
        tv_friendmessage.setOnClickListener(this);

        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.
                beginTransaction();
        fragmentTransaction.add(R.id.lv_fragment_container,imKit.getConversationFragment());
        fragmentTransaction.commit();
        //加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
        //后面的参数是此Fragment的Tag。相当于id
        //记住提交



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_systemmessage:
                startActivity(new Intent(MyMessageActivity.this, SystemMessageActivity.class));
                break;
            case R.id.tv_ordermessage:
                startActivity(new Intent(MyMessageActivity.this, OrderMessageActivity.class));
                break;
            case R.id.tv_friendmessage:
                startActivity(new Intent(MyMessageActivity.this, FriendMessageActivity.class));
                break;
        }

    }
}
