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
    private TextView tb_title,tb_back,tv_mobile,tv_modifypassword,tv_ybnum,tv_ybnumshow;
    PopupWindows popupWindows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_ybnumshow.setText(Auth.getCurrentUser().getYBAccount());

    }

    protected void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_mobile = (TextView)findViewById(R.id.tv_mobile);
        tv_ybnum = (TextView)findViewById(R.id.tv_ybnum);
        tv_ybnumshow = (TextView)findViewById(R.id.tv_ybnumshow);
        tv_modifypassword = (TextView)findViewById(R.id.tv_modifypassword);
        tb_back.setText("设置");
        tb_title.setText("账号信息");
        tv_mobile.setText(Auth.getCurrentUser().getMobile());
        tv_ybnumshow.setText(Auth.getCurrentUser().getYBAccount());
        tb_back.setOnClickListener(this);
        tv_ybnum.setOnClickListener(this);
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
            case R.id.tv_ybnum:
                if(Auth.getCurrentUser().getYBAccount() == null || Auth.getCurrentUser().getYBAccount().equals("")) {
                    new PopupWindowsYBNUM(AccountActivity.this, tv_ybnum);
                }
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

    public class PopupWindowsYBNUM extends PopupWindow
    {

        public PopupWindowsYBNUM(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_modifyybnum, null);
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
            final EditText et_ybnum = (EditText) view
                    .findViewById(R.id.et_ybnum);


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
                    if(!et_ybnum.getText().toString().equals("" ))
                    {
                        Http.request(AccountActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                "YBAccount", et_ybnum.getText().toString()
                        ), new Http.RequestListener<UserDetail>() {
                            @Override
                            public void onSuccess(UserDetail result) {
                                super.onSuccess(result);
                                // 上传成功
                                Auth.updateCurrentUser(result);
                                tv_ybnumshow.setText(Auth.getCurrentUser().getYBAccount());
                            }
                        });
                        dismiss();
                    }
                    else
                    {
                        Toast.makeText(AccountActivity.this,"友帮账号不能为空",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}
