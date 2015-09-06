package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AuthResult;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by dashi on 15/6/21.
 */
public class LoginActivity extends BaseActivity {
    EditText email, password;
    View ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);


        ok = findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Http.request(LoginActivity.this, API.USER_LOGIN, Http.map(
                        "Email", email.getText().toString(),
                        "Password", password.getText().toString()

                ), new Http.RequestListener<AuthResult>(){
                    @Override
                    public void onSuccess(AuthResult result) {
                        super.onSuccess(result);


                        Auth.login(result.Token, result.User);
                        String date = result.User.CreatedAt.toString();
                        Date date2 = result.User.CreatedAt;
                        // 进首页
                        SplashActivity.startRootActivity(LoginActivity.this);
                    }
                });
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
