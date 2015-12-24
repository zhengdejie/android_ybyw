package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/8/24.
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back,tv_mobile,tv_modifypassword;
    PopupWindows popupWindows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
    }

    protected void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_mobile = (TextView)findViewById(R.id.tv_mobile);
        tv_modifypassword = (TextView)findViewById(R.id.tv_modifypassword);
        tb_back.setText("设置");
        tb_title.setText("账号信息");
        tv_mobile.setText(Auth.getCurrentUser().getMobile());
        tb_back.setOnClickListener(this);
        tv_modifypassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_modifypassword:
                new PopupWindows(AccountActivity.this,tv_modifypassword);
                break;
        }
    }

    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_modifypassword, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            showAtLocation(parent, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            update();
            TextView tv_cancel = (TextView) view
                    .findViewById(R.id.tv_cancel);
            TextView tv_ok = (TextView) view
                    .findViewById(R.id.tv_ok);
            final EditText et_password = (EditText) view
                    .findViewById(R.id.et_password);


            ll_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_cancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });
            tv_ok.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(!et_password.getText().toString().equals("" ))
                    {
                        Http.request(AccountActivity.this, API.CHECK_OLDPASSWORD,  Http.map(
                                "Password", et_password.getText().toString()
                        ), new Http.RequestListener<UserDetail>() {
                            @Override
                            public void onSuccess(UserDetail result) {
                                super.onSuccess(result);
                                // 上传成功
                                startActivity(new Intent(AccountActivity.this,ModifyPasswordActivity.class));
                            }

                            @Override
                            public void onFail(String code) {
                                super.onFail(code);
                                //Toast.makeText(AccountActivity.this, code, Toast.LENGTH_SHORT).show();
                            }
                        });
                        dismiss();
                    }
                    else
                    {
                        Toast.makeText(AccountActivity.this,"密码不能为空，请输入密码",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
