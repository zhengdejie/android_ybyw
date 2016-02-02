package appframe.appframe.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
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
 * Created by Administrator on 2016/1/8.
 */
public class EditFriendsNickNameActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_title, tb_back,tb_action;
    private EditText et_friendnickname;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfriendsnickname);
        init();
    }

    private void init() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tb_action = (TextView) findViewById(R.id.tb_action);
        et_friendnickname = (EditText) findViewById(R.id.et_friendnickname);
        tb_back.setText("帮友资料");
        tb_title.setText("备注信息");
        tb_action.setText("保存");
        if(getIntent().getStringExtra("FNickName") != null) {
            et_friendnickname.setText(getIntent().getStringExtra("FNickName"));
        }
        tb_action.setEnabled(false);
        tb_back.setOnClickListener(this);
        tb_action.setOnClickListener(this);
        et_friendnickname.addTextChangedListener(textWatcher);
        Selection.setSelection(et_friendnickname.getText(), et_friendnickname.getText().length());
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tb_action.setBackgroundColor(getResources().getColor(R.color.green));
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
                Http.request(EditFriendsNickNameActivity.this, API.UPDATE_FRIENDNICK, new Object[]{getIntent().getStringExtra("UserID")}, Http.map(
                        "UserId", String.valueOf(Auth.getCurrentUserId()),
                        "NickName",et_friendnickname.getText().toString()
                ), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        intent.putExtra("NickName", et_friendnickname.getText().toString());
                        intent.putExtra("Name",getIntent().getStringExtra("Name"));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
                break;

        }

    }
}

