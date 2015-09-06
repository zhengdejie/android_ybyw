package appframe.appframe.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.com.google.zxing.BarcodeFormat;
import appframe.appframe.com.google.zxing.MultiFormatWriter;
import appframe.appframe.com.google.zxing.WriterException;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.com.google.zxing.common.BitMatrix;
import appframe.appframe.utils.Auth;

/**
 * Created by Administrator on 2015/8/24.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{
    Button btn_about,btn_account,btn_newmessage,btn_exit;
    public static final int SCAN_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }
    protected void init()
    {
        btn_about = (Button)findViewById(R.id.btn_about);
        btn_account = (Button)findViewById(R.id.btn_account);
        btn_newmessage = (Button)findViewById(R.id.btn_newmessage);
        btn_exit =(Button)findViewById(R.id.btn_exit);
        btn_about.setOnClickListener(this);
        btn_account.setOnClickListener(this);
        btn_newmessage.setOnClickListener(this);
        btn_exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_about:
                //startActivity(new Intent(this,AboutActivity.class));
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_CODE);
                break;
            case R.id.btn_account:
                startActivity(new Intent(this,AccountActivity.class));
                break;
            case R.id.btn_newmessage:
                ImageView imageView = (ImageView)findViewById(R.id.img_QR);
                try {
                    imageView.setImageBitmap(Create2DCode("12"));
                }
                catch (WriterException e)
                {}
                //startActivity(new Intent(this,NewMessageActivity.class));
                break;
            case R.id.btn_exit:
                Auth.login(null, null);

                // 进首页
                SplashActivity.startRootActivity(this);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_CODE:
                TextView scanResult = (TextView) findViewById(R.id.txt_scanresult);
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    scanResult.setText(result);
                } else if (resultCode == RESULT_CANCELED) {
                    scanResult.setText("扫描出错");
                }
                break;
            default:
                break;
        }
    }

    public Bitmap Create2DCode(String str) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(matrix.get(x, y)){
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
