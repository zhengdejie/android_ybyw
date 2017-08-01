package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.login.YWLoginState;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;


import appframe.appframe.R;
import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;
import appframe.appframe.com.google.zxing.BarcodeFormat;
import appframe.appframe.com.google.zxing.MultiFormatWriter;
import appframe.appframe.com.google.zxing.WriterException;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.com.google.zxing.common.BitMatrix;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.Utils;

/**
 * Created by Administrator on 2015/8/24.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    TextView tb_title,tb_back,tv_account,tv_newmessage,tv_privacy,tv_about,tv_exit,tv_share;
    private YWIMKit mIMKit;
    private IWXAPI api;
    //public static final int SCAN_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        mIMKit = LoginSampleHelper.getInstance().getIMKit();
        if (mIMKit == null) {
            return;
        }
        init();
    }
    protected void init()
    {
        tv_about = (TextView)findViewById(R.id.tv_about);
        tv_account = (TextView)findViewById(R.id.tv_account);
        tv_newmessage = (TextView)findViewById(R.id.tv_newmessage);
        tv_exit =(TextView)findViewById(R.id.tv_exit);
        tv_privacy = (TextView)findViewById(R.id.tv_privacy);
        tv_share = (TextView)findViewById(R.id.tv_share);
        tv_about.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        tv_newmessage.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        tv_privacy.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("我的");
        tb_title.setText("设置");
        tb_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_about:
                startActivity(new Intent(this,AboutActivity.class));
//                Intent intent = new Intent(this, CaptureActivity.class);
//                startActivityForResult(intent, SCAN_CODE);
                break;
            case R.id.tv_account:
                startActivity(new Intent(this,AccountActivity.class));
                break;
            case R.id.tv_newmessage:
//                ImageView imageView = (ImageView)findViewById(R.id.img_QR);
//                try {
//                    imageView.setImageBitmap(Create2DCode("12"));
//                }
//                catch (WriterException e)
//                {}
                startActivity(new Intent(this,NewMessageActivity.class));
                break;
            case R.id.tv_exit:
                showAlertDialog();

                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_privacy:
                startActivity(new Intent(this, PrivacyActivity.class));
                break;
            case R.id.tv_share:
                new PopupWindows_Share(this,tv_share);

                break;
        }

    }

    private void sentWebToWEIXING(int type)
    {
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = "1";
//
//        // ��WXTextObject�����ʼ��һ��WXMediaMessage����
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        // �����ı����͵���Ϣʱ��title�ֶβ�������
//        // msg.title = "Will be ignored";
//        msg.description = "1";
//
//        // ����һ��Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("text"); // transaction�ֶ�����Ψһ��ʶһ������
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//
//        // ����api�ӿڷ������ݵ�΢��
//        api.sendReq(req);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.ubangwang.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "友问友帮快来加入吧";
        msg.description = "友问友帮-友的需求友来帮";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo50);
        msg.thumbData = Utils.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        //发送到聊天界面——WXSceneSession
        //发送到朋友圈——WXSceneTimeline
        if(type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        else
        {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void showAlertDialog(){
        AlertDialog.Builder builder = new YWAlertDialog.Builder(SettingActivity.this);
        builder.setMessage("退出后您将收不到新消息通知，是否确认退出？")
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                logout();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog dialog = builder.create();
        if (!dialog.isShowing()){
            dialog.show();
        }
    }

    public void logout() {
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();
        mLoginService.logout(new IWxCallback() {
            //此时logout已关闭所有基于IMBaseActivity的OpenIM相关Actiivity，s
            @Override
            public void onSuccess(Object... arg0) {
                Toast.makeText(SettingActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.disconnect);
                Auth.login(null, null);
                MobclickAgent.onProfileSignOff();
                // 进首页
                SplashActivity.startRootActivity(SettingActivity.this);
            }

            @Override
            public void onProgress(int arg0) {

            }

            @Override
            public void onError(int arg0, String arg1) {
                Toast.makeText(SettingActivity.this, "退出失败,请重新登录", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class PopupWindows_Share extends PopupWindow
    {

        public PopupWindows_Share(final Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_share, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
//            LinearLayout ll_popup = (LinearLayout) view
//                    .findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.push_bottom_in_2));
            RelativeLayout rl_photopopup = (RelativeLayout)view.findViewById(R.id.rl_photopopup);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            rl_photopopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            LinearLayout ll_wx = (LinearLayout) view.findViewById(R.id.ll_wx);
            LinearLayout ll_wxcircle = (LinearLayout) view.findViewById(R.id.ll_wxcircle);

            ll_wx.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    sentWebToWEIXING(1);
                    dismiss();
                }
            });
            ll_wxcircle.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    sentWebToWEIXING(2);
                    dismiss();
                }
            });


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("设置页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
