package appframe.appframe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.SelfEvaluationDetail;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
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
import appframe.appframe.widget.swiperefresh.OrderDetailsGridViewAdapater;

/**
 * Created by Administrator on 2015/12/8.
 */
public class EditSignatureActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title, tb_back,tb_action,tv_contentcount,tv_progress_content;
    private GridView mGridView;
    private EditText et_signature;
    private ImagePublishAdapter mAdapter;
    Gson gson = new Gson();
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    StringBuilder sb = new StringBuilder();
    public int upload_iamge_num = 0;
    List<File> fileList = new ArrayList<File>();
    int count = 1 , photoCount = 0;
    LinearLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsignature);
        init();
        progress_bar.setVisibility(View.VISIBLE);
        initData();
        initView();

        inidata();

    }

    private void init() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tb_action = (TextView) findViewById(R.id.tb_action);
        et_signature = (EditText) findViewById(R.id.et_signature);
        mGridView = (GridView)findViewById(R.id.gridview);
        tv_contentcount = (TextView) findViewById(R.id.tv_contentcount);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        tv_progress_content = (TextView)findViewById(R.id.tv_progress_content);
        tv_progress_content.setText("正在加载");
        tb_back.setText("个人信息");
        tb_title.setText("自我评价");
        tb_action.setText("保存");

//        tb_action.setEnabled(false);
        tb_back.setOnClickListener(this);
        tb_action.setOnClickListener(this);

        et_signature.addTextChangedListener(contentWatcher);
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

    public void initView()
    {
//        TextView titleTv  = (TextView) findViewById(R.id.title);
//        titleTv.setText("");
//        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImagePublishAdapter(this, mDataList,false);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {
                if (position == getDataSize())
                {
                    new PopupWindows(EditSignatureActivity.this, mGridView);
                }
                else
                {
                    Intent intent = new Intent(EditSignatureActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);

                    intent.putExtra("from",
                            "EditSignatureActivity");
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
    private void removeTempFromPref()
    {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getTempFromPref();
        List<ImageItem> incomingDataList = (List<ImageItem>) intent
                .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);

        if (incomingDataList != null)
        {
            mDataList.addAll(incomingDataList);
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeTempFromPref();
        mDataList.clear();
    }

    protected void onPause()
    {
        super.onPause();
        saveTempToPref();
        MobclickAgent.onPageEnd("自我评价"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
        MobclickAgent.onPageStart("自我评价"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    private void notifyDataChanged()
    {
        mAdapter.notifyDataSetChanged();
    }

    private void saveTempToPref()
    {
        SharedPreferences sp = getSharedPreferences(
                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
        String prefStr = gson.toJson(mDataList);
        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();

    }
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

    private void inidata()
    {
        Http.request(EditSignatureActivity.this, API.GET_SEVALUATION, new Object[]{Auth.getCurrentUserId()},
                new Http.RequestListener<SelfEvaluationDetail>() {
            @Override
            public void onSuccess(SelfEvaluationDetail result) {
                super.onSuccess(result);
                if(result!=null) {
                    et_signature.setText(result.getDescription());
                    Selection.setSelection(et_signature.getText(), et_signature.getText().length());
                    tv_contentcount.setText(String.format("%d/250", result.getDescription().length()));

                    if(result.getPhotos() != null && result.getPhotos() != "") {
                        List<String> photoPath = new ArrayList<String>();
                        Bitmap bitmap;
                        int i = 0 ;
                        for (String photsCount : result.getPhotos().toString().split(",")) {
//                            photoPath.add(photsCount);
                            photoCount ++;
                            i++;
                            File file = new File(Environment.getExternalStorageDirectory()+ "/mysignature/",String.format("%s.jpg",i));

                            if (!file.exists())
                            {
                                File vDirPath = file.getParentFile();
                                vDirPath.mkdirs();
                            }
                            else
                            {
                                if (file.exists())
                                {
                                    file.delete();
                                }
                            }
                            fileList.add(file);
//                            new ImageDownLoad().execute(ImageUtils.getImageUrl(photsCount, 70, 70, "0"),"df");
                            new ImageDownLoad().execute(ImageUtils.getImageUrl(photsCount),"df");
//                            bitmap = Utils.getBitmapFromQINIUURL(ImageUtils.getImageUrl(photsCount, 70, 70));



                        }


//                        mGridView.setAdapter(new OrderDetailsGridViewAdapater(EditSignatureActivity.this,photoPath));
//                        mGridView.setVisibility(View.VISIBLE);
//                        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                Intent intent = new Intent();
//                                intent.setClass(EditSignatureActivity.this, AvatarZoomActivity.class);
//                                intent.putExtra("Avatar", (String)parent.getAdapter().getItem(position));
//                                startActivity(intent);
//                            }
//                        });
                    }
//                    ImageUtils.setImageUrl(iv_showavatar, result.getPhotos());
                }
            }
        });
    }

    class ImageDownLoad extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            showImageView.setImageBitmap(null);
//            showProgressBar();//显示进度条提示框

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Bitmap b = Utils.getBitmapFromQINIUURL(params[0]);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result!=null &&  count == 1 ){
//                iv_positive.setImageBitmap(result);
                Utils.saveBitmap(result, fileList.get(0), 100);
                ImageItem item = new ImageItem();
                item.sourcePath = fileList.get(0).getPath();
                mDataList.add(item);

//                dismissProgressBar();
//                showImageView.setImageBitmap(result);
            }
            if(result!=null && count == 2 && fileList.size() >= 2){
//                iv_positive.setImageBitmap(result);
                Utils.saveBitmap(result, fileList.get(1), 100);
                ImageItem item = new ImageItem();
                item.sourcePath = fileList.get(1).getPath();
                mDataList.add(item);

//                dismissProgressBar();
//                showImageView.setImageBitmap(result);
            }
            if(result!=null && count == 3 && fileList.size() == 3){
//                iv_positive.setImageBitmap(result);
                Utils.saveBitmap(result, fileList.get(2), 100);
                ImageItem item = new ImageItem();
                item.sourcePath = fileList.get(2).getPath();
                mDataList.add(item);

//                dismissProgressBar();
//                showImageView.setImageBitmap(result);
            }
            if(photoCount == count)
            {
                progress_bar.setVisibility(View.GONE);
            }
            count ++;

            initView();
        }



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tb_back:
                mDataList.clear();
                removeTempFromPref();
                finish();
                break;
            case R.id.tb_action:
                tv_progress_content.setText("正在保存");
                progress_bar.setVisibility(View.VISIBLE);
                if (mDataList.size() == 0) {
                    Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "Description", et_signature.getText().toString(),
                            "Photos", ""
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);
                            // 上传成功
                            mDataList.clear();
                            removeTempFromPref();
                            Auth.updateCurrentUser(result);
                            progress_bar.setVisibility(View.GONE);
                            finish();
                        }

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
                        }
                    });
                }
                else
                {
                    for (ImageItem dl : mDataList) {


                        final File f = new File(dl.sourcePath);
                        Http.request(EditSignatureActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
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
                                        if (upload_iamge_num == mDataList.size()) {
                                            Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                                    "Description", et_signature.getText().toString(),
                                                    "Photos", sb.deleteCharAt(0).toString()
                                            ), new Http.RequestListener<UserDetail>() {
                                                @Override
                                                public void onSuccess(UserDetail result) {
                                                    super.onSuccess(result);
                                                    // 上传成功
                                                    mDataList.clear();
                                                    removeTempFromPref();
                                                    Auth.updateCurrentUser(result);
                                                    progress_bar.setVisibility(View.GONE);
                                                    finish();
                                                }

                                                @Override
                                                public void onFail(String code) {
                                                    super.onFail(code);
                                                }
                                            });

                                        }
                                    }
                                }, result.getUpToken());
                            }
                        });
                    }
                }
                break;


        }

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
                    Intent intent = new Intent(EditSignatureActivity.this,
                            ImageBucketChooseActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                            getAvailableSize());
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_CLASS,
                            EditSignatureActivity.class);
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

//    private static final int TAKE_PICTURE = 0x000300;
//    private static final int SELECT_PHOTO = 0x000100;
//    private String path = "";
//    public class PopupWindows_Picture extends PopupWindow
//    {
//
//        public PopupWindows_Picture(final Context mContext, View parent)
//        {
//
//            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
//            view.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.fade_ins));
//            LinearLayout ll_popup = (LinearLayout) view
//                    .findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.push_bottom_in_2));
//            RelativeLayout rl_photopopup = (RelativeLayout)view.findViewById(R.id.rl_photopopup);
//
//            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//            setFocusable(true);
//            setBackgroundDrawable(new BitmapDrawable());
//            setOutsideTouchable(true);
//            setContentView(view);
//            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//            update();
//
//            rl_photopopup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//            Button btn_camera = (Button) view
//                    .findViewById(R.id.item_popupwindows_camera);
//            Button btn_photo = (Button) view
//                    .findViewById(R.id.item_popupwindows_Photo);
//            Button btn_cancel = (Button) view
//                    .findViewById(R.id.item_popupwindows_cancel);
//            btn_camera.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    takePhoto();
//                    dismiss();
//                }
//            });
//            btn_photo.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    Intent photoPickerIntent = new Intent(Intent.CATEGORY_OPENABLE);
//                    photoPickerIntent.setType("image/*");
//                    if (Build.VERSION.SDK_INT <19) {
//                        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
//                    }else {
//                        photoPickerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                    }
//                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//                    dismiss();
//                }
//            });
//            btn_cancel.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    dismiss();
//                }
//            });
//
//        }
//    }
//
//    public void takePhoto()
//    {
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File vFile = new File(Environment.getExternalStorageDirectory()
//                , "tempAvatar.jpg");
//        if (!vFile.exists())
//        {
//            File vDirPath = vFile.getParentFile();
//            vDirPath.mkdirs();
//        }
//        else
//        {
//            if (vFile.exists())
//            {
//                vFile.delete();
//            }
//        }
//        path = vFile.getPath();
//        Uri cameraUri = Uri.fromFile(vFile);
//        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//    }
//
//    @Override
//    protected void  onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        switch (requestCode) {
//            case TAKE_PICTURE:
//                if (resultCode == RESULT_OK) {
//                    //Bitmap bm = (Bitmap) imageReturnedIntent.getExtras().get("data");
//                    final File f = new File(Environment.getExternalStorageDirectory()
//                            , "tempAvatar.jpg");
//
//                    Http.request(EditSignatureActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
//                        @Override
//                        public void onSuccess(Token result) {
//                            super.onSuccess(result);
//
//                            UploadUtils.uploadImage(f, new UploadUtils.Callback() {
//                                @Override
//                                public void done(final String id) {
//
//                                    Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
//                                            "Photos", id
//                                    ), new Http.RequestListener<UserDetail>() {
//                                        @Override
//                                        public void onSuccess(UserDetail result) {
//                                            super.onSuccess(result);
//                                            // 上传成功
//                                            ImageUtils.setImageUrl(iv_showavatar, id);
//                                        }
//                                    });
//                                }
//                            }, result.getUpToken());
//                        }
//                    });
//                    //ImageUtils.setImageUrl(iv_showavatar, result.Avatar);//想图像显示在ImageView视图上，private ImageView img;
//                }
//                break;
//            case SELECT_PHOTO:
//                if (resultCode == Activity.RESULT_OK) {
//
//                    final File f = Utils.uriToFile(imageReturnedIntent.getData());
//
//                    Http.request(EditSignatureActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
//                        @Override
//                        public void onSuccess(Token result) {
//                            super.onSuccess(result);
//
//                            UploadUtils.uploadImage(f, new UploadUtils.Callback() {
//                                @Override
//                                public void done(final String id) {
//                                    Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
//                                            "Photos", id
//                                    ), new Http.RequestListener<UserDetail>() {
//                                        @Override
//                                        public void onSuccess(UserDetail result) {
//                                            super.onSuccess(result);
//                                            // 上传成功
//                                            ImageUtils.setImageUrl(iv_showavatar, id);
//                                        }
//                                    });
//                                }
//                            }, result.getUpToken());
//                        }
//                    });
//                }
//                break;
//        }
//    }

}
