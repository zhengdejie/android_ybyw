package appframe.appframe.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.dto.AuthResult;
import appframe.appframe.dto.UserContact;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/21.
 */
public class LoginActivity extends BaseActivity {
    EditText  password,et_mobile;
//    Button ok,btn_forgetpassword;
    TextView tb_back,tb_action,tb_title,tv_progress_content,ok,btn_forgetpassword,regiter;
    LinearLayout progress_bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);
        regiter = (TextView)findViewById(R.id.regiter);

//        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        et_mobile = (EditText)findViewById(R.id.et_mobile);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        tv_progress_content = (TextView)findViewById(R.id.tv_progress_content);
        tv_progress_content.setText("正在登入");
        //Cursor cursor = null;
        //List<UserContact> contactsList = new ArrayList<UserContact>();


        ok = (TextView)findViewById(R.id.ok);
        btn_forgetpassword = (TextView)findViewById(R.id.btn_forgetpassword);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(et_mobile.getText().toString().equals("") || et_mobile.getText() == null)
                {
                    Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(password.getText().toString().equals("") || password.getText() == null)
                    {
                        Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progress_bar.setVisibility(View.VISIBLE);
                        Http.request(LoginActivity.this, API.USER_LOGIN, Http.map(

                                "Password", password.getText().toString(),
                                "Mobile", et_mobile.getText().toString()

                        ), new Http.RequestListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult result) {
                                super.onSuccess(result);


                                Auth.login(result.Token, result.User);

//                        IYWLoginService loginService = App.mIMKit.getLoginService();
//                        YWLoginParam loginParam = YWLoginParam.createLoginParam(String.valueOf(result.User.Id), "1");
//                        loginService.login(loginParam, new IWxCallback() {
//
//                            @Override
//                            public void onSuccess(Object... arg0) {
//                                // 进首页
//                                SplashActivity.startRootActivity(LoginActivity.this);
//                            }
//
//                            @Override
//                            public void onProgress(int arg0) {
//                                // TODO Auto-generated method stub
//                            }
//
//                            @Override
//                            public void onError(int errCode, String description) {
//                                //如果登录失败，errCode为错误码,description是错误的具体描述信息
//                            }
//                        });


//                        // 进首页
                                SplashActivity.startRootActivity(LoginActivity.this);
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
        });

        btn_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        tb_back.setVisibility(View.GONE);
//        tb_action.setText("注册");

        tb_title.setText("登入");
        regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

//    static final int MENU_REGISTER = 1;
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        createMenuItem(menu, MENU_REGISTER, "注册");
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case MENU_REGISTER:
//                finish();
//                startActivity(new Intent(this, RegisterActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }



}
