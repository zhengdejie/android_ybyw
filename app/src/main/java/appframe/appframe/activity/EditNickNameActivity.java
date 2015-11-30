package appframe.appframe.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/26.
 */
public class EditNickNameActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_title, tb_back,tb_action;
    private EditText et_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnickname);
        init();
    }

    private void init() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tb_action = (TextView) findViewById(R.id.tb_action);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        tb_back.setText("个人中心");
        tb_title.setText("个人信息");
        tb_action.setText("保存");
        et_nickname.setText(Auth.getCurrentUser().getName());
        tb_action.setEnabled(false);
        tb_back.setOnClickListener(this);
        tb_action.setOnClickListener(this);
        et_nickname.addTextChangedListener(textWatcher);
        Selection.setSelection(et_nickname.getText(), et_nickname.getText().length());
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tb_action.setBackgroundColor(Color.GREEN);
            tb_action.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_action:
                Http.request(EditNickNameActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                        "Name", et_nickname.getText().toString()
                ), new Http.RequestListener<UserDetail>(){
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);
                            // 上传成功
                            Auth.updateCurrentUser(result);
                            finish();
                        }
                    });
                break;

        }

    }
}
