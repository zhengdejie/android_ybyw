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
import android.widget.EditText;
import android.widget.TextView;

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
    EditText email, password;
    View ok;
    TextView tb_back,tb_action,tb_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);


        //Cursor cursor = null;
        //List<UserContact> contactsList = new ArrayList<UserContact>();


        ok = findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Http.request(LoginActivity.this, API.USER_LOGIN, Http.map(
                        "Email", email.getText().toString(),
                        "Password", password.getText().toString()

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
                    }
                });
            }
        });

        tb_back.setVisibility(View.GONE);
        tb_action.setText("注册");

        tb_title.setText("友帮");
        tb_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    static final int MENU_REGISTER = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        createMenuItem(menu, MENU_REGISTER, "注册");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case MENU_REGISTER:
                finish();
                startActivity(new Intent(this, RegisterActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
