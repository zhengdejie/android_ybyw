package appframe.appframe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title, tb_back;
    private EditText et_password,et_passwordconfirm;
    private Button btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypassword);
        init();
    }

    protected void init() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        et_password = (EditText) findViewById(R.id.et_password);
        et_passwordconfirm = (EditText) findViewById(R.id.et_passwordconfirm);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        tb_back.setText("账号信息");
        tb_title.setText("设置密码");
        tb_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_ok:
                if(!et_password.getText().toString().equals("")) {
                    if (et_password.getText().toString().equals(et_passwordconfirm.getText().toString())) {
                        Http.request(ModifyPasswordActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                "Password", et_password.getText().toString()
                        ), new Http.RequestListener<UserDetail>() {
                            @Override
                            public void onSuccess(UserDetail result) {
                                super.onSuccess(result);
                                // 上传成功
                                Auth.updateCurrentUser(result);
                                finish();
                            }

                            @Override
                            public void onFail(String code) {
                                super.onFail(code);
                                //Toast.makeText(ModifyPasswordActivity.this, code, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(ModifyPasswordActivity.this, "两次填写的密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ModifyPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
