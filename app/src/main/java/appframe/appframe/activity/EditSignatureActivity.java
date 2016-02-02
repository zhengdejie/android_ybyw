package appframe.appframe.activity;

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
 * Created by Administrator on 2015/12/8.
 */
public class EditSignatureActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title, tb_back,tb_action;
    private EditText et_signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsignature);
        init();
    }

    private void init() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tb_action = (TextView) findViewById(R.id.tb_action);
        et_signature = (EditText) findViewById(R.id.et_signature);
        tb_back.setText("个人中心");
        tb_title.setText("个人签名");
        tb_action.setText("保存");
        et_signature.setText(Auth.getCurrentUser().getSignature());
        tb_action.setEnabled(false);
        tb_back.setOnClickListener(this);
        tb_action.setOnClickListener(this);
        et_signature.addTextChangedListener(textWatcher);
        Selection.setSelection(et_signature.getText(), et_signature.getText().length());
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
                Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
                        "Description", et_signature.getText().toString()
                ), new Http.RequestListener<UserDetail>() {
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
