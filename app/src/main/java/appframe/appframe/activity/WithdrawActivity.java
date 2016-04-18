package appframe.appframe.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/2/22.
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_title,tb_back,tv_yuer,tv_alipay,tv_weixing;
    private EditText et_amount,et_name,et_account;
    private Button btn_send;
    Drawable icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();
        initdata();
    }
    protected  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_yuer = (TextView)findViewById(R.id.tv_yuer);
        tv_alipay = (TextView)findViewById(R.id.tv_alipay);
        tv_weixing = (TextView)findViewById(R.id.tv_weixing);
        et_amount = (EditText)findViewById(R.id.et_amount);
        et_name = (EditText)findViewById(R.id.et_name);
        et_account = (EditText)findViewById(R.id.et_account);
        btn_send = (Button)findViewById(R.id.btn_send);
        tb_back.setText("返回");
        tb_title.setText("提现");
        tb_back.setOnClickListener(this);
        tv_alipay.setOnClickListener(this);
        tv_weixing.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        icon = getResources().getDrawable(R.drawable.ic_task_status_list_check);
        et_amount.addTextChangedListener(textWatcher);
    }
    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    et_amount.setText(s);
                    et_amount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                et_amount.setText(s);
                et_amount.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    et_amount.setText(s.subSequence(0, 1));
                    et_amount.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    protected  void initdata()
    {
        Http.request(this, API.USER_PROFILE, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<UserDetail>() {
            @Override
            public void onSuccess(UserDetail result) {
                super.onSuccess(result);

                tv_yuer.setText("￥ " + result.getWalletTotal());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();

                break;
            case R.id.tv_alipay:
                tv_alipay.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                tv_weixing.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;
            case R.id.tv_weixing:
                tv_alipay.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tv_weixing.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                break;
            case R.id.btn_send:
                Http.request(this, API.WITHDRAWAPPLY, new Object[]{Auth.getCurrentUserId()},
                        Http.map("Account",et_account.getText().toString(),
                                "Amount",et_amount.getText().toString(),
                                "UserName",et_name.getText().toString()),
                        new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        finish();
                    }
                });
                break;
        }
    }
}

