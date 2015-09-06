package appframe.appframe.activity;

import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import appframe.appframe.R;

import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.Utils;
import appframe.appframe.widget.popupwindow.SelectPicPopupWindow;

/**
 * Created by Administrator on 2015/8/12.
 */
public class OrderSendActivity extends BaseActivity{
    private TextView txt_deadlinedate;
    private TextView txt_deadlinetime;
    private EditText edit_title;
    private EditText edit_bounty;
    private TextView txt_location;
    private Spinner spinner_category;
    private EditText edit_content;
    private Spinner spinner_range;
    private EditText edit_require;
    private RadioButton radio_online;
    private RadioButton radio_offline;
    private CheckBox checkBox_anonymous;
    private CheckBox checkBox_donotshowlocation;
    private Button btn_send;
    private ImageButton img_addimg1,img_addimg2,img_addimg3;
    private SelectPicPopupWindow menuWindow;
    private static final int img1_SELECT_PHOTO = 101;
    private static final int img2_SELECT_PHOTO = 102;
    private static final int img3_SELECT_PHOTO = 103;
    private static final int PHOTO_GRAPH = 10;
    private static final int PHOTO_RESOULT = 301;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private LocationClient mLocationClient;
    //public MyLocationListener mMyLocationListener;
    private double latitude = 0.0;
    private double longitude = 0.0;
    public Vibrator mVibrator;

    //获取日期格式器对象
    SimpleDateFormat fmtDate = new SimpleDateFormat("yy-MM-dd");
    SimpleDateFormat fmtTime = new SimpleDateFormat("hh:mm");
    private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersend);
        init();
        txt_deadlinedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                new DatePickerDialog(OrderSendActivity.this, datepicker, dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        txt_deadlinetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(OrderSendActivity.this, timepicker, dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE), true).show();

            }
        });
        updateDeadlineDate();
        updateDeadlineTime();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Http.request(OrderSendActivity.this, API.ORDER_SEND, new Object[]{Auth.getCurrentUserId()}, Http.map(
                        "Title", edit_title.getText().toString(),
                        "Content", edit_content.getText().toString(),
                        "Position", "12",
                        "Category", spinner_category.getSelectedItem().toString(),
                        "Visibility", "0",
                        "Deadline", txt_deadlinedate.getText() + " " + txt_deadlinetime.getText(),
                        "PaymentMethod", radio_online.isChecked() ? "online" : "offline",
                        "Bounty", "10"
                ), new Http.RequestListener<UserDetail>() {
                    @Override
                    public void onSuccess(UserDetail result) {
                        super.onSuccess(result);

                    }
                });
            }
        });
        img_addimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(OrderSendActivity.this, img1_SELECT_PHOTO);
                //显示窗口
                menuWindow.showAtLocation(OrderSendActivity.this.findViewById(R.id.ordersend), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });

        img_addimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(OrderSendActivity.this, img2_SELECT_PHOTO);
                //显示窗口
                menuWindow.showAtLocation(OrderSendActivity.this.findViewById(R.id.ordersend), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });

        img_addimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new SelectPicPopupWindow(OrderSendActivity.this, img3_SELECT_PHOTO);
                //显示窗口
                menuWindow.showAtLocation(OrderSendActivity.this.findViewById(R.id.ordersend), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
    }

//    private View.OnClickListener itemsOnClick = new View.OnClickListener(){
//
//        public void onClick(View v) {
//            menuWindow.dismiss();
//            switch (v.getId()) {
//                case R.id.btn_take_photo:
//                    break;
//                case R.id.btn_pick_photo:
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//                    break;
//                default:
//                    break;
//            }
//
//
//        }
//
//    };

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
        switch(requestCode) {
            case img1_SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageReturnedIntent.getData()));
                    }
                    catch (FileNotFoundException e)
                    {

                    }
//                    File f = Utils.uriToFile(imageReturnedIntent.getData());
//                    Bitmap bm = Utils.getResizedBitmap(f, Utils.dpToPx(100), Utils.dpToPx(100));
                    img_addimg1.setImageBitmap(bitmap);
                    menuWindow.dismiss();

//                    UploadUtils.uploadImage(f, new UploadUtils.Callback() {
//                        @Override
//                        public void done(String id) {
//                            if(TextUtils.isEmpty(id)){
//                                // 上传失败
//                                uploadedAvatarId = null;
//                                avatar.setImageResource(R.drawable.ic_launcher);
//                                return;
//                            }
//                            uploadedAvatarId = id;
//                        }
//                    });
                };
                break;
            case img2_SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageReturnedIntent.getData()));
                    }
                    catch (FileNotFoundException e)
                    {

                    }

                    img_addimg2.setImageBitmap(bitmap);
                    menuWindow.dismiss();

                };
                break;
            case img3_SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageReturnedIntent.getData()));
                    }
                    catch (FileNotFoundException e)
                    {

                    }

                    img_addimg3.setImageBitmap(bitmap);
                    menuWindow.dismiss();

                };
                break;
            case PHOTO_GRAPH+img1_SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 设置文件保存路径
                    File picture = new File(Environment.getExternalStorageDirectory()
                            + "/temp.jpg");
//                startPhotoZoom(Uri.fromFile(picture));
                    Bitmap bm = Utils.getResizedBitmap(picture, Utils.dpToPx(100), Utils.dpToPx(100));
                    // Uri uri = imageReturnedIntent.getData();
                    img_addimg1.setImageBitmap(bm);
                    menuWindow.dismiss();
                }
                break;
            case PHOTO_GRAPH+img2_SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 设置文件保存路径
                    File picture = new File(Environment.getExternalStorageDirectory()
                            + "/temp.jpg");
//                startPhotoZoom(Uri.fromFile(picture));
                    Bitmap bm = Utils.getResizedBitmap(picture, Utils.dpToPx(100), Utils.dpToPx(100));
                    // Uri uri = imageReturnedIntent.getData();
                    img_addimg2.setImageBitmap(bm);
                    menuWindow.dismiss();
                }
                break;
            case PHOTO_GRAPH+img3_SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 设置文件保存路径
                    File picture = new File(Environment.getExternalStorageDirectory()
                            + "/temp.jpg");
//                startPhotoZoom(Uri.fromFile(picture));
                    Bitmap bm = Utils.getResizedBitmap(picture, Utils.dpToPx(100), Utils.dpToPx(100));
                    // Uri uri = imageReturnedIntent.getData();
                    img_addimg3.setImageBitmap(bm);
                    menuWindow.dismiss();
                }
                break;
            case PHOTO_RESOULT:
                Bundle extras = imageReturnedIntent.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
                    img_addimg1.setImageBitmap(photo); //把图片显示在ImageView控件上
                }
                break;
        }
    }

    /**
     * 收缩图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent();
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }

    private void init()
    {
        txt_deadlinedate = (TextView)findViewById(R.id.txt_deadlinedate);
        txt_deadlinetime= (TextView)findViewById(R.id.txt_deadlinetime);
        edit_title = (EditText)findViewById(R.id.edit_title);
        edit_bounty = (EditText)findViewById(R.id.edit_bounty);
        txt_location = (TextView)findViewById(R.id.txt_location);

        if(getIntent().getStringExtra("self") != null && getIntent().getStringExtra("self").equals("self"))
        {
            edit_bounty.setHint("索酬");
        }
        if(getIntent().getStringExtra("demand")!=null && getIntent().getStringExtra("demand").equals("demand"))
        {
            edit_bounty.setHint("赏金");
        }

        final String category[]=new String[]{
                "我也不知道分哪类",
                "衣",
                "食",
                "住",
                "行",
                "学术/艺术",
                "工作/商务",
                "生活/娱乐",
                "找人",
                "二手/转让",
                "资源共享",
                "合作/推广",
                "活动"
        };
        spinner_category = (Spinner)findViewById(R.id.spinner_category);
        spinner_category.setAdapter(getAdapter(category));
        final String range[] = new String[]{
                "一度朋友",
                "二度朋友",
                "一度和二度朋友",
                "全平台可见"
        };
        spinner_range = (Spinner)findViewById(R.id.spinner_range);
        spinner_range.setAdapter(getAdapter(range));
        spinner_range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((TextView) view).getText().toString().equals("全平台可见")) {
                    Toast.makeText(OrderSendActivity.this, "全平台一天只能发5次", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edit_require = (EditText) findViewById(R.id.edit_require);
        edit_content = (EditText)findViewById(R.id.edit_content);
        radio_online = (RadioButton)findViewById(R.id.radio_online);
        radio_offline = (RadioButton)findViewById(R.id.radio_offline);
        checkBox_anonymous = (CheckBox)findViewById(R.id.checkBox_anonymous);
        checkBox_donotshowlocation = (CheckBox)findViewById(R.id.checkBox_donotshowlocation);
        btn_send = (Button)findViewById(R.id.btn_send);
        img_addimg1 = (ImageButton)findViewById(R.id.img_addimg1);
        img_addimg2 = (ImageButton)findViewById(R.id.img_addimg2);
        img_addimg3 = (ImageButton)findViewById(R.id.img_addimg3);

        //百度地图定位
        BaiduLocation baiduLocation = new BaiduLocation(getApplicationContext());
        baiduLocation.txt_location = txt_location;
        baiduLocation.setOption();
        baiduLocation.mLocationClient.start();

    }


    private ArrayAdapter<String> getAdapter(String arr[]){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        return arrayAdapter;
    }
    //当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            //修改日历控件的年，月，日
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //将页面TextView的显示更新为最新时间
            updateDeadlineDate();
        }

    };

    TimePickerDialog.OnTimeSetListener timepicker = new TimePickerDialog.OnTimeSetListener() {

        //同DatePickerDialog控件

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            updateDeadlineTime();
        }

    };

    private void updateDeadlineDate() {
        txt_deadlinedate.setText(fmtDate.format(dateAndTime.getTime()));
    }

    private void updateDeadlineTime() {
        txt_deadlinetime.setText(fmtTime.format(dateAndTime.getTime()));
    }
}
