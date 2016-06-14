package appframe.appframe.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AuthResult;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back,tv_code,btn_ok;
    private EditText et_mobile,et_code,et_password;
    private LinearLayout progress_bar;
    private MyCount mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        init();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_code = (TextView)findViewById(R.id.tv_code);
        et_mobile = (EditText)findViewById(R.id.et_mobile);
        et_code = (EditText)findViewById(R.id.et_code);
        et_password = (EditText)findViewById(R.id.et_password);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);

        btn_ok = (TextView)findViewById(R.id.btn_ok);
        tb_back.setText("登入");
        tb_title.setText("忘记密码");
        tb_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        tv_code.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_ok:
                if(et_mobile.getText().toString().equals("") || et_mobile.getText() == null)
                {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (et_password.getText().toString().equals("") || et_password.getText() == null)
                    {
                        Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (et_code.getText().toString().equals("") || et_code.getText() == null)
                        {
                            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progress_bar.setVisibility(View.VISIBLE);
                            Http.request(ForgetPasswordActivity.this, API.FORGET_PASSWORD, Http.map(

                                    "Password", et_password.getText().toString(),
                                    "Mobile",et_mobile.getText().toString(),
                                    "Code",et_code.getText().toString()

                            ), new Http.RequestListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult result) {
                                    super.onSuccess(result);

                                    Auth.login(result.Token, result.User);

                                    // 进首页
                                    SplashActivity.startRootActivity(ForgetPasswordActivity.this);
                                    progress_bar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);
                                    progress_bar.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                }

                break;
            case R.id.tv_code:
                tv_code.setEnabled(false);
                mc = new MyCount(60000, 1000);
                mc.start();
//                SMSSDK.registerEventHandler(eh); //注册短信回调
//                SMSSDK.getVerificationCode("86", et_mobile.getText().toString());
                break;
        }

    }

    EventHandler eh=new EventHandler(){

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
//                    SMSSDK.submitVerificationCode("86",et_mobile.getText().toString(),et_code.getText().toString());
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
//                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    String country = (String) phoneMap.get("country");

                }
            }else{
                ((Throwable)data).printStackTrace();
            }
        }
    };

    /*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            tv_code.setEnabled(true);
            tv_code.setText("获取验证码");
        }
        @Override
        public void onTick(long millisUntilFinished) {
            SimpleDateFormat sdf = new SimpleDateFormat("ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            Date date = new Date(millisUntilFinished);
            String text = sdf.format(date);

            tv_code.setText("(" + text +"）"+ "获取验证码");

        }
    }
}
