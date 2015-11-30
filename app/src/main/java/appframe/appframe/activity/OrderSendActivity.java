package appframe.appframe.activity;

import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import appframe.appframe.R;

import appframe.appframe.app.API;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.utils.Utils;
import appframe.appframe.widget.photopicker.adapter.ImagePublishAdapter;
import appframe.appframe.widget.photopicker.model.ImageItem;
import appframe.appframe.widget.photopicker.util.CustomConstants;
import appframe.appframe.widget.photopicker.util.IntentConstants;
import appframe.appframe.widget.photopicker.view.ImageBucketChooseActivity;
import appframe.appframe.widget.photopicker.view.ImageZoomActivity;
import appframe.appframe.widget.popupwindow.SelectPicPopupWindow;

/**
 * Created by Administrator on 2015/8/12.
 */
public class OrderSendActivity extends BaseActivity{
    private TextView txt_deadlinedate,txt_deadlinetime,txt_location,tb_back,tb_title,tv_latitude,tv_longitude;
    private EditText edit_title,edit_bounty,edit_content,edit_require;
    private Spinner spinner_category,spinner_range;
    private RadioButton radio_online,radio_offline;
    private CheckBox checkBox_anonymous,checkBox_donotshowphonenum,checkBox_donotshowlocation,checkBox_oneclass,checkBox_twoclass,checkBox_stranger;
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
    private boolean fileupload_success = false ;
    Gson gson = new Gson();
    //获取日期格式器对象
    SimpleDateFormat fmtDate = new SimpleDateFormat("yy-MM-dd");
    SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm");
    private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    BDLocation bdLocation = new BDLocation();

    private GridView mGridView;
    private ImagePublishAdapter mAdapter;
    //private TextView sendTv;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    private String Type="0" ;
    StringBuilder sb = new StringBuilder();
    public int upload_iamge_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate","savedInstanceState");
        setContentView(R.layout.activity_ordersend);
        init();
        initData();
        initView();
        if(savedInstanceState != null)
        {
            edit_title.setText(savedInstanceState.getString("Title"));
            edit_content.setText(savedInstanceState.getString("Content"));
            spinner_category.setSelection(savedInstanceState.getInt("Category"));
            String[] dataArr = savedInstanceState.getString("Deadline").split(" ");
            txt_deadlinedate.setText(dataArr[0]);
            txt_deadlinetime.setText(dataArr[1]);
            if(savedInstanceState.getString("PaymentMethod").equals("线上支付"))
            {
                radio_online.setChecked(true);
                radio_offline.setChecked(false);
            }
            else
            {
                radio_online.setChecked(false);
                radio_offline.setChecked(true);
            }
            edit_bounty.setText(savedInstanceState.getString("Bounty"));
            if(savedInstanceState.getString("NameAnonymity").equals("1"))
            {
                checkBox_anonymous.setChecked(true);
            }
            else
            {
                checkBox_anonymous.setChecked(false);
            }
            if(savedInstanceState.getString("LocationAnonymity").equals("1"))
            {
                checkBox_donotshowlocation.setChecked(true);
            }
            else
            {
                checkBox_donotshowlocation.setChecked(false);
            }
            if(savedInstanceState.getString("PhoneAnonymity").equals("1"))
            {
                checkBox_donotshowphonenum.setChecked(true);
            }
            else
            {
                checkBox_donotshowphonenum.setChecked(false);
            }
            edit_require.setText(savedInstanceState.getString("Request"));
        }
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
        dateAndTime.add(Calendar.DAY_OF_MONTH, 7);
        txt_deadlinedate.setText(fmtDate.format(dateAndTime.getTime()));
        updateDeadlineTime();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDataList.size() == 0)
                {
                    Http.request(OrderSendActivity.this, API.ORDER_SEND, Http.map(
                            "Id", String.valueOf(Auth.getCurrentUserId()),
                            "Title", edit_title.getText().toString(),
                            "Address", txt_location.getText().toString(),
                            "Content", edit_content.getText().toString(),
                            "latitude", tv_latitude.getText().toString(),
                            "longitude", tv_longitude.getText().toString(),
                            "Category", spinner_category.getSelectedItem().toString(),
                            "Visibility", TransferVisibility(checkBox_oneclass.isChecked(),checkBox_twoclass.isChecked(),checkBox_stranger.isChecked()),
                            "Deadline", txt_deadlinedate.getText() + " " + txt_deadlinetime.getText(),
                            "PaymentMethod", radio_online.isChecked() ? "线上支付" : "线下支付",
                            "Bounty", edit_bounty.getText().toString(),
                            "NameAnonymity", checkBox_anonymous.isChecked() ? "1" : "0",
                            "LocationAnonymity", checkBox_donotshowlocation.isChecked() ? "1" : "0",
                            "PhoneAnonymity", checkBox_donotshowphonenum.isChecked() ? "1" : "0",
                            "Photos", "",
                            "Request", edit_require.getText().toString(),
                            "Type", Type
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);
                            Toast.makeText(OrderSendActivity.this,"发单成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                    removeTempFromPref();
                    mDataList.clear();
                    finish();
                }
                else
                {
                    //boolean success = false ;
                    Log.i("mDataList",String.valueOf(mDataList.size()));

                    for( ImageItem dl : mDataList)
                    {


                        final File f = new File(dl.sourcePath);
                        Http.request(OrderSendActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
                            @Override
                            public void onSuccess(Token result) {
                                super.onSuccess(result);

                                UploadUtils.uploadImage(f, new UploadUtils.Callback() {
                                    @Override
                                    public void done(String id) {
                                        if (TextUtils.isEmpty(id)) {
                                            // 上传失败
                                            upload_iamge_num = 0;
                                            return;
                                        }
                                        upload_iamge_num++;
                                        sb.append(",").append(id);
                                        if(upload_iamge_num == mDataList.size())
                                        {
                                            Http.request(OrderSendActivity.this, API.ORDER_SEND, Http.map(
                                                    "Id", String.valueOf(Auth.getCurrentUserId()),
                                                    "Title", edit_title.getText().toString(),
                                                    "Address", txt_location.getText().toString(),
                                                    "Content", edit_content.getText().toString(),
                                                    "latitude", tv_latitude.getText().toString(),
                                                    "longitude", tv_longitude.getText().toString(),
                                                    "Category", spinner_category.getSelectedItem().toString(),
                                                    "Visibility", TransferVisibility(checkBox_oneclass.isChecked(),checkBox_twoclass.isChecked(),checkBox_stranger.isChecked()),
                                                    "Deadline", txt_deadlinedate.getText() + " " + txt_deadlinetime.getText(),
                                                    "PaymentMethod", radio_online.isChecked() ? "线上支付" : "线下支付",
                                                    "Bounty", edit_bounty.getText().toString(),
                                                    "NameAnonymity", checkBox_anonymous.isChecked() ? "1" : "0",
                                                    "LocationAnonymity", checkBox_donotshowlocation.isChecked() ? "1" : "0",
                                                    "PhoneAnonymity", checkBox_donotshowphonenum.isChecked() ? "1" : "0",
                                                    "Photos", sb.deleteCharAt(0).toString(),
                                                    "Request", edit_require.getText().toString(),
                                                    "Type", Type
                                            ), new Http.RequestListener<UserDetail>() {
                                                @Override
                                                public void onSuccess(UserDetail result) {
                                                    super.onSuccess(result);
                                                    Toast.makeText(OrderSendActivity.this,"发单成功",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            upload_iamge_num = 0;
                                            removeTempFromPref();
                                            mDataList.clear();
                                            finish();
                                        }

                                    }
                                },result.getUpToken());
                            }
                        });

                    }//for

                }//else


            }
        });

    }

    protected void onPause()
    {
        Log.i("onPause","");
        super.onPause();
        saveTempToPref();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.i("onSaveInstanceState",edit_title.getText().toString());
        outState.putString("Title", edit_title.getText().toString());
        outState.putString("Content", edit_content.getText().toString());
        outState.putInt("Category", spinner_category.getSelectedItemPosition());
        outState.putString("Deadline", txt_deadlinedate.getText() + " " + txt_deadlinetime.getText());
        outState.putString("PaymentMethod", radio_online.isChecked() ? "线上支付" : "线下支付");
        outState.putString("Bounty", edit_bounty.getText().toString());
        outState.putString("NameAnonymity", checkBox_anonymous.isChecked() ? "1" : "0");
        outState.putString("LocationAnonymity", checkBox_donotshowlocation.isChecked() ? "1" : "0");
        outState.putString("PhoneAnonymity", checkBox_donotshowphonenum.isChecked() ? "1" : "0");
        outState.putString("Request", edit_require.getText().toString());
        super.onSaveInstanceState(outState);
        saveTempToPref();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("onRestoreInstanceState", savedInstanceState.getString("Title"));
        edit_title.setText(savedInstanceState.getString("Title"));
        edit_content.setText(savedInstanceState.getString("Content"));
        spinner_category.setSelection(savedInstanceState.getInt("Category"));
        String[] dataArr = savedInstanceState.getString("Deadline").split(" ");
        txt_deadlinedate.setText(dataArr[0]);
        txt_deadlinetime.setText(dataArr[1]);
        if(savedInstanceState.getString("PaymentMethod").equals("线上支付"))
        {
            radio_online.setChecked(true);
            radio_offline.setChecked(false);
        }
        else
        {
            radio_online.setChecked(false);
            radio_offline.setChecked(true);
        }
        edit_bounty.setText(savedInstanceState.getString("Bounty"));
        if(savedInstanceState.getString("NameAnonymity").equals("1"))
        {
            checkBox_anonymous.setChecked(true);
        }
        else
        {
            checkBox_anonymous.setChecked(false);
        }
        if(savedInstanceState.getString("LocationAnonymity").equals("1"))
        {
            checkBox_donotshowlocation.setChecked(true);
        }
        else
        {
            checkBox_donotshowlocation.setChecked(false);
        }
        if(savedInstanceState.getString("PhoneAnonymity").equals("1"))
        {
            checkBox_donotshowphonenum.setChecked(true);
        }
        else
        {
            checkBox_donotshowphonenum.setChecked(false);
        }
        edit_require.setText(savedInstanceState.getString("Request"));

    }

    private void saveTempToPref()
    {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = gson.toJson(mDataList);
        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();

    }

    @Override
    protected void onResume()
    {Log.i("onResume","");
        super.onResume();
        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy","");
        removeTempFromPref();
        mDataList.clear();
    }

    private void notifyDataChanged()
    {
        mAdapter.notifyDataSetChanged();
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

    private void getTempFromPref()
    {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
        if (!TextUtils.isEmpty(prefStr))
        {
//            List<ImageItem> tempImages = JSON.parseArray(prefStr,
//                    ImageItem.class);
            List<ImageItem> tempImages = gson.fromJson(prefStr,
                    new TypeToken<List<ImageItem>>() {
                    }.getType());

            mDataList = tempImages;
        }
    }

    private void removeTempFromPref()
    {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).commit();
    }

    @SuppressWarnings("unchecked")
    private void initData()
    {
        getTempFromPref();
        List<ImageItem> incomingDataList = (List<ImageItem>) getIntent()
                .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
        if (incomingDataList != null)
        {
            mDataList.addAll(incomingDataList);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getTempFromPref();
        List<ImageItem> incomingDataList = (List<ImageItem>) intent
                .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
        Log.i("onNewIntent","123");
        if (incomingDataList != null)
        {
            mDataList.addAll(incomingDataList);
        }
        initView();
    }

    public void initView()
    {
//        TextView titleTv  = (TextView) findViewById(R.id.title);
//        titleTv.setText("");
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if (position == getDataSize())
                {
                    new PopupWindows(OrderSendActivity.this, mGridView);
                }
                else
                {
                    Intent intent = new Intent(OrderSendActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);

                    intent.putExtra("from",
                            "OrderSendActivity");
                    startActivity(intent);
                }
            }
        });
//        sendTv = (TextView) findViewById(R.id.action);
//        sendTv.setText("发送");
//        sendTv.setOnClickListener(new View.OnClickListener()
//        {
//
//            public void onClick(View v)
//            {
//                removeTempFromPref();
//                System.exit(0);
//                //TODO 这边以mDataList为来源做上传的动作
//            }
//        });
    }

    private int getDataSize()
    {
        return mDataList == null ? 0 : mDataList.size();
    }

    private int getAvailableSize()
    {
        int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
        if (availSize >= 0)
        {
            return availSize;
        }
        return 0;
    }

    public String getString(String s)
    {
        String path = null;
        if (s == null) return "";
        for (int i = s.length() - 1; i > 0; i++)
        {
            s.charAt(i);
        }
        return path;
    }

    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
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
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button btn_camera = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button btn_photo = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button btn_cancel = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            btn_camera.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    takePhoto();
                    dismiss();
                }
            });
            btn_photo.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(OrderSendActivity.this,
                            ImageBucketChooseActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                            getAvailableSize());
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_CLASS,
                             OrderSendActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void takePhoto()
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory()
                + "/myimage/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        if (!vFile.exists())
        {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        }
        else
        {
            if (vFile.exists())
            {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
    {
        switch (requestCode)
        {
            case TAKE_PICTURE:
                if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
                        && resultCode == -1 && !TextUtils.isEmpty(path))
                {
                    ImageItem item = new ImageItem();
                    item.sourcePath = path;
                    mDataList.add(item);
                }
                break;
        }
//        switch(requestCode) {
//            case img1_SELECT_PHOTO:
//                if(resultCode == RESULT_OK){
//                    ContentResolver cr = this.getContentResolver();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageReturnedIntent.getData()));
//                    }
//                    catch (FileNotFoundException e)
//                    {
//
//                    }
//
//                    img_addimg1.setImageBitmap(bitmap);
//                    menuWindow.dismiss();
//
//                };
//                break;
//            case img2_SELECT_PHOTO:
//                if(resultCode == RESULT_OK){
//                    ContentResolver cr = this.getContentResolver();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageReturnedIntent.getData()));
//                    }
//                    catch (FileNotFoundException e)
//                    {
//
//                    }
//
//                    img_addimg2.setImageBitmap(bitmap);
//                    menuWindow.dismiss();
//
//                };
//                break;
//            case img3_SELECT_PHOTO:
//                if(resultCode == RESULT_OK){
//                    ContentResolver cr = this.getContentResolver();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageReturnedIntent.getData()));
//                    }
//                    catch (FileNotFoundException e)
//                    {
//
//                    }
//
//                    img_addimg3.setImageBitmap(bitmap);
//                    menuWindow.dismiss();
//
//                };
//                break;
//            case PHOTO_GRAPH+img1_SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    // 设置文件保存路径
//                    File picture = new File(Environment.getExternalStorageDirectory()
//                            + "/temp.jpg");
////                startPhotoZoom(Uri.fromFile(picture));
//                    Bitmap bm = Utils.getResizedBitmap(picture, Utils.dpToPx(100), Utils.dpToPx(100));
//                    // Uri uri = imageReturnedIntent.getData();
//                    img_addimg1.setImageBitmap(bm);
//                    menuWindow.dismiss();
//                }
//                break;
//            case PHOTO_GRAPH+img2_SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    // 设置文件保存路径
//                    File picture = new File(Environment.getExternalStorageDirectory()
//                            + "/temp.jpg");
////                startPhotoZoom(Uri.fromFile(picture));
//                    Bitmap bm = Utils.getResizedBitmap(picture, Utils.dpToPx(100), Utils.dpToPx(100));
//                    // Uri uri = imageReturnedIntent.getData();
//                    img_addimg2.setImageBitmap(bm);
//                    menuWindow.dismiss();
//                }
//                break;
//            case PHOTO_GRAPH+img3_SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    // 设置文件保存路径
//                    File picture = new File(Environment.getExternalStorageDirectory()
//                            + "/temp.jpg");
////                startPhotoZoom(Uri.fromFile(picture));
//                    Bitmap bm = Utils.getResizedBitmap(picture, Utils.dpToPx(100), Utils.dpToPx(100));
//                    // Uri uri = imageReturnedIntent.getData();
//                    img_addimg3.setImageBitmap(bm);
//                    menuWindow.dismiss();
//                }
//                break;
//            case PHOTO_RESOULT:
//                Bundle extras = imageReturnedIntent.getExtras();
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
//                    //此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
//                    img_addimg1.setImageBitmap(photo); //把图片显示在ImageView控件上
//                }
//                break;
//        }
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
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tv_latitude = (TextView)findViewById(R.id.tv_latitude);
        tv_longitude = (TextView)findViewById(R.id.tv_longitude);
        checkBox_oneclass = (CheckBox)findViewById(R.id.checkBox_oneclass);
        checkBox_twoclass = (CheckBox)findViewById(R.id.checkBox_twoclass);
        checkBox_stranger = (CheckBox)findViewById(R.id.checkBox_stranger);

        checkBox_oneclass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked && !checkBox_twoclass.isChecked() && !checkBox_stranger.isChecked())
                {
                    checkBox_oneclass.setChecked(true);
                    Toast.makeText(OrderSendActivity.this,"请至少勾选一项",Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox_twoclass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked && !checkBox_oneclass.isChecked() && !checkBox_stranger.isChecked())
                {
                    checkBox_twoclass.setChecked(true);
                    Toast.makeText(OrderSendActivity.this,"请至少勾选一项",Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox_stranger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked && !checkBox_twoclass.isChecked() && !checkBox_oneclass.isChecked())
                {
                    checkBox_stranger.setChecked(true);
                    Toast.makeText(OrderSendActivity.this,"请至少勾选一项",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tb_back.setText("取消");
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTempFromPref();
                mDataList.clear();
                finish();
            }
        });

        if(getIntent().getStringExtra("self") != null && getIntent().getStringExtra("self").equals("self"))
        {
            edit_bounty.setHint("索酬");
            tb_title.setText("自荐单");
            Type = "1";
        }
        if(getIntent().getStringExtra("demand")!=null && getIntent().getStringExtra("demand").equals("demand"))
        {
            edit_bounty.setHint("赏金");
            tb_title.setText("需求单");
            Type = "2";
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
//        spinner_range = (Spinner)findViewById(R.id.spinner_range);
//        spinner_range.setAdapter(getAdapter(range));
//        spinner_range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (((TextView) view).getText().toString().equals("全平台可见")) {
//                    Toast.makeText(OrderSendActivity.this, "全平台一天只能发5次", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        edit_require = (EditText) findViewById(R.id.edit_require);
        edit_content = (EditText)findViewById(R.id.edit_content);
        radio_online = (RadioButton)findViewById(R.id.radio_online);
        radio_offline = (RadioButton)findViewById(R.id.radio_offline);
        checkBox_anonymous = (CheckBox)findViewById(R.id.checkBox_anonymous);
        checkBox_donotshowlocation = (CheckBox)findViewById(R.id.checkBox_donotshowlocation);
        checkBox_donotshowphonenum = (CheckBox)findViewById(R.id.checkBox_donotshowphonenum);
        btn_send = (Button)findViewById(R.id.btn_send);
//        img_addimg1 = (ImageButton)findViewById(R.id.img_addimg1);
//        img_addimg2 = (ImageButton)findViewById(R.id.img_addimg2);
//        img_addimg3 = (ImageButton)findViewById(R.id.img_addimg3);

        //百度地图定位
        BaiduLocation baiduLocation = new BaiduLocation(getApplicationContext());
        baiduLocation.txt_location = txt_location;
        baiduLocation.tv_latitude = tv_latitude;
        baiduLocation.tv_longitude = tv_longitude;
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

    private String TransferVisibility(boolean oneIsChecked,boolean twoIsChecked,boolean strangerIsChecked)
    {
        int i = 0;
        if(oneIsChecked)
        {
            i += 1;
        }

        if(twoIsChecked)
        {
            i += 2;
        }

        if(strangerIsChecked)
        {
            i += 4;
        }


        return String.valueOf(i);
    }
}
