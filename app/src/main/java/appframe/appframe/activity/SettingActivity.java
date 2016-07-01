package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.login.YWLoginState;

import appframe.appframe.R;
import appframe.appframe.app.App;
import appframe.appframe.com.google.zxing.BarcodeFormat;
import appframe.appframe.com.google.zxing.MultiFormatWriter;
import appframe.appframe.com.google.zxing.WriterException;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.com.google.zxing.common.BitMatrix;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.LoginSampleHelper;

/**
 * Created by Administrator on 2015/8/24.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    TextView tb_title,tb_back,tv_account,tv_newmessage,tv_privacy,tv_about,tv_exit;
    private YWIMKit mIMKit;
    //public static final int SCAN_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
        tv_about.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        tv_newmessage.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
        tv_privacy.setOnClickListener(this);
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
        }

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
                LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.idle);
                Auth.login(null, null);

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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case SCAN_CODE:
//                TextView scanResult = (TextView) findViewById(R.id.txt_scanresult);
//                if (resultCode == RESULT_OK) {
//                    String result = data.getStringExtra("scan_result");
//                    scanResult.setText(result);
//                } else if (resultCode == RESULT_CANCELED) {
//                    scanResult.setText("扫描出错");
//                }
//                break;
//            default:
//                break;
//        }
//    }

//    public Bitmap Create2DCode(String str) throws WriterException {
//        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
//        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300);
//        int width = matrix.getWidth();
//        int height = matrix.getHeight();
//        //二维矩阵转为一维像素数组,也就是一直横着排了
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if(matrix.get(x, y)){
//                    pixels[y * width + x] = 0xff000000;
//                } else {
//                    pixels[y * width + x] = 0xffffffff;
//                }
//
//            }
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        //通过像素数组生成bitmap,具体参考api
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
//    }
}
