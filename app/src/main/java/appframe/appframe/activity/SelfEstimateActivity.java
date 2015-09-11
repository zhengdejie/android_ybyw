package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import appframe.appframe.R;
import appframe.appframe.utils.Auth;

/**
 * Created by Administrator on 2015/9/7.
 */
public class SelfEstimateActivity extends BaseActivity  {
    //Button btn_about,btn_account,btn_newmessage,btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfestimate);
        //init();
    }
//    protected void init()
//    {
//        btn_about = (Button)findViewById(R.id.btn_about);
//        btn_account = (Button)findViewById(R.id.btn_account);
//        btn_newmessage = (Button)findViewById(R.id.btn_newmessage);
//        btn_exit =(Button)findViewById(R.id.btn_exit);
//        btn_about.setOnClickListener(this);
//        btn_account.setOnClickListener(this);
//        btn_newmessage.setOnClickListener(this);
//        btn_exit.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.btn_about:
//                startActivity(new Intent(this,AboutActivity.class));
//
//                break;
//            case R.id.btn_account:
//                startActivity(new Intent(this,AccountActivity.class));
//                break;
//            case R.id.btn_newmessage:
//                startActivity(new Intent(this,NewMessageActivity.class));
//                break;
//            case R.id.btn_exit:
//                Auth.login(null, null);
//
//                // 进首页
//                SplashActivity.startRootActivity(this);
//                break;
//        }
//
//    }
}
