package appframe.appframe.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.com.google.zxing.BarcodeFormat;
import appframe.appframe.com.google.zxing.MultiFormatWriter;
import appframe.appframe.com.google.zxing.WriterException;
import appframe.appframe.com.google.zxing.common.BitMatrix;
import appframe.appframe.utils.Auth;

/**
 * Created by Administrator on 2015/11/27.
 */
public class QRCodeActivity extends BaseActivity implements View.OnClickListener{

    private TextView tb_title,tb_back;
    private ImageButton ib_qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        ib_qrcode = (ImageButton)findViewById(R.id.ib_qrcode);

        tb_back.setText("个人信息");
        tb_title.setText("二维码名片");

        try {
            ib_qrcode.setImageBitmap(Create2DCode("appframe_" + String.valueOf(Auth.getCurrentUserId()),ib_qrcode));
        }
        catch (WriterException e)
        {}

        tb_back.setOnClickListener(this);

    }

        public Bitmap Create2DCode(String str, ImageButton ib_qrcode) throws WriterException {
            DisplayMetrics displayMetrics = ib_qrcode.getContext().getResources()
                    .getDisplayMetrics();

            ViewGroup.LayoutParams lp = ib_qrcode.getLayoutParams();

            int width = ib_qrcode.getWidth();// 获取niv的实际宽度
            if (width <= 0)
            {
                width = lp.width;// 获取niv在layout中声明的宽度
            }
            if (width <= 0)
            {
                // width = imageView.getMaxWidth();// 检查最大值
                //width = niv.getMaxWidth();
                width = ib_qrcode.getMeasuredWidth();
            }
            if (width <= 0)
            {
                width = displayMetrics.widthPixels;
            }
            int height = ib_qrcode.getHeight();// 获取imageview的实际高度
            if (height <= 0)
            {
                height = lp.height;// 获取imageview在layout中声明的宽度
            }
            if (height <= 0)
            {
                //height = getImageViewFieldValue(imageView, "mMaxHeight");// 检查最大值
                //height = niv.getMaxHeight();
                height = ib_qrcode.getMeasuredHeight();
            }
            if (height <= 0)
            {
                height = displayMetrics.heightPixels;
            }
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height);
         width = matrix.getWidth();
         height = matrix.getHeight();
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;


        }

    }



}

