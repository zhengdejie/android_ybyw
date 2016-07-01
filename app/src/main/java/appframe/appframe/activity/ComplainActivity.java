package appframe.appframe.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016-06-20.
 */
public class ComplainActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back,bt_send,tv_contentcount;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        init();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        et_content = (EditText)findViewById(R.id.et_content);
        bt_send = (TextView)findViewById(R.id.bt_send);
        tv_contentcount = (TextView)findViewById(R.id.tv_contentcount);
        tb_back.setText("关于友帮");
        tb_title.setText("投诉与建议");
        tb_back.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        et_content.addTextChangedListener(contentWatcher);
    }

    private TextWatcher contentWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            tv_contentcount.setText(String.format("%d/250",s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.bt_send:
                Http.request(ComplainActivity.this, API.USER_FEEDBACK, new Object[]{Auth.getCurrentUserId()}, Http.map(
                        "Content", et_content.getText().toString()
                ), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                        Toast.makeText(ComplainActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });
                break;
        }

    }
}

