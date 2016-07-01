package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.SelfEvaluationDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.photopicker.adapter.ImagePublishAdapter;
import appframe.appframe.widget.photopicker.model.ImageItem;
import appframe.appframe.widget.photopicker.util.CustomConstants;
import appframe.appframe.widget.photopicker.util.IntentConstants;
import appframe.appframe.widget.photopicker.view.ImageBucketChooseActivity;
import appframe.appframe.widget.photopicker.view.ImageZoomActivity;
import appframe.appframe.widget.swiperefresh.OrderDetailsGridViewAdapater;

/**
 * Created by Administrator on 2015/9/7.
 */
public class SelfEstimateActivity extends BaseActivity implements View.OnClickListener {
    TextView tb_title,tb_back,tv_selfestimate;
    private GridView mGridView;
    private ImagePublishAdapter mAdapter;
    private static final int PHOTO_RESOULT = 301;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private String userID;
    Gson gson = new Gson();
    //private TextView sendTv;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfestimate);
        init();

    }
    protected void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_selfestimate = (TextView)findViewById(R.id.tv_selfestimate);
        mGridView = (GridView)findViewById(R.id.gridview);
        tb_back.setText("返回");
        tb_title.setText("自我评价");
        tb_back.setOnClickListener(this);
        userID = getIntent().getStringExtra("UserID");
        Http.request(SelfEstimateActivity.this, API.GET_SEVALUATION, new Object[]{userID},
                new Http.RequestListener<SelfEvaluationDetail>() {
                    @Override
                    public void onSuccess(SelfEvaluationDetail result) {
                        super.onSuccess(result);

                        if(result != null) {
                            tv_selfestimate.setText(result.getDescription());
                            if(result.getPhotos() != null && result.getPhotos() != "") {
                                List<String> photoPath = new ArrayList<String>();
                                for (String photsCount : result.getPhotos().toString().split(",")) {
                                    photoPath.add(photsCount);
                                }
                                mGridView.setAdapter(new OrderDetailsGridViewAdapater(SelfEstimateActivity.this,photoPath));
                                mGridView.setVisibility(View.VISIBLE);
                                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent();
                                        intent.setClass(SelfEstimateActivity.this, AvatarZoomActivity.class);
                                        intent.putExtra("Avatar", (String)parent.getAdapter().getItem(position));
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
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
        }

    }

//    protected void onPause()
//    {
//        super.onPause();
//        saveTempToPref();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState)
//    {
//        super.onSaveInstanceState(outState);
//        saveTempToPref();
//    }
//    private void saveTempToPref()
//    {
//        SharedPreferences sp = getSharedPreferences(
//                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
//        String prefStr = gson.toJson(mDataList);
//        sp.edit().putString(CustomConstants.PREF_TEMP_IMAGES, prefStr).commit();
//
//    }
//
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
//    }
//
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.i("onDestroy", "");
//        removeTempFromPref();
//        mDataList.clear();
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        getTempFromPref();
//        List<ImageItem> incomingDataList = (List<ImageItem>) intent
//                .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//        Log.i("onNewIntent", "123");
//        if (incomingDataList != null)
//        {
//            mDataList.addAll(incomingDataList);
//        }
//        initView();
//    }
//
//    private void notifyDataChanged()
//    {
//        mAdapter.notifyDataSetChanged();
//    }
//
//
//    private void getTempFromPref()
//    {
//        SharedPreferences sp = getSharedPreferences(
//                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
//        String prefStr = sp.getString(CustomConstants.PREF_TEMP_IMAGES, null);
//        if (!TextUtils.isEmpty(prefStr))
//        {
//
//            List<ImageItem> tempImages = gson.fromJson(prefStr,
//                    new TypeToken<List<ImageItem>>() {
//                    }.getType());
//
//            mDataList = tempImages;
//        }
//    }
//
//    private void removeTempFromPref()
//    {
//        SharedPreferences sp = getSharedPreferences(
//                CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
//        sp.edit().remove(CustomConstants.PREF_TEMP_IMAGES).commit();
//    }
//
//    @SuppressWarnings("unchecked")
//    private void initData()
//    {
//        getTempFromPref();
//        List<ImageItem> incomingDataList = (List<ImageItem>) getIntent()
//                .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
//        if (incomingDataList != null)
//        {
//            mDataList.addAll(incomingDataList);
//        }
//    }
//
//    public void initView()
//    {
//
//        mGridView = (GridView) findViewById(R.id.gridview);
//        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        mAdapter = new ImagePublishAdapter(this, mDataList);
//        mGridView.setAdapter(mAdapter);
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id)
//            {
//                if (position == getDataSize())
//                {
//                    new PopupWindows(SelfEstimateActivity.this, mGridView);
//                }
//                else
//                {
//                    Intent intent = new Intent(SelfEstimateActivity.this,
//                            ImageZoomActivity.class);
//                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
//                            (Serializable) mDataList);
//                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
//                    intent.putExtra("from",
//                            "SelfEstimateActivity");
//                    startActivity(intent);
//                }
//            }
//        });
//
//    }
//
//    private int getDataSize()
//    {
//        return mDataList == null ? 0 : mDataList.size();
//    }
//
//    private int getAvailableSize()
//    {
//        int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
//        if (availSize >= 0)
//        {
//            return availSize;
//        }
//        return 0;
//    }
//
//    public String getString(String s)
//    {
//        String path = null;
//        if (s == null) return "";
//        for (int i = s.length() - 1; i > 0; i++)
//        {
//            s.charAt(i);
//        }
//        return path;
//    }
//
//    public class PopupWindows extends PopupWindow
//    {
//
//        public PopupWindows(Context mContext, View parent)
//        {
//
//            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
//            view.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.fade_ins));
//            LinearLayout ll_popup = (LinearLayout) view
//                    .findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.push_bottom_in_2));
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
//            Button bt1 = (Button) view
//                    .findViewById(R.id.item_popupwindows_camera);
//            Button bt2 = (Button) view
//                    .findViewById(R.id.item_popupwindows_Photo);
//            Button bt3 = (Button) view
//                    .findViewById(R.id.item_popupwindows_cancel);
//            bt1.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    takePhoto();
//                    dismiss();
//                }
//            });
//            bt2.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    Intent intent = new Intent(SelfEstimateActivity.this,
//                            ImageBucketChooseActivity.class);
//                    intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
//                            getAvailableSize());
//                    intent.putExtra(IntentConstants.EXTRA_IMAGE_CLASS,
//                            SelfEstimateActivity.class);
//                    startActivity(intent);
//                    dismiss();
//                }
//            });
//            bt3.setOnClickListener(new View.OnClickListener()
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
//    private static final int TAKE_PICTURE = 0x000000;
//    private String path = "";
//
//    public void takePhoto()
//    {
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        File vFile = new File(Environment.getExternalStorageDirectory()
//                + "/myimage/", String.valueOf(System.currentTimeMillis())
//                + ".jpg");
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
//    protected void  onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
//    {
//        switch (requestCode)
//        {
//            case TAKE_PICTURE:
//                if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
//                        && resultCode == -1 && !TextUtils.isEmpty(path))
//                {
//                    ImageItem item = new ImageItem();
//                    item.sourcePath = path;
//                    mDataList.add(item);
//                }
//                break;
//        }
//
//    }
//
//    /**
//     * 收缩图片
//     *
//     * @param uri
//     */
//    public void startPhotoZoom(Uri uri) {
//        Intent intent = new Intent();
//        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 500);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, PHOTO_RESOULT);
//    }

}
