package appframe.appframe.activity;

import android.app.Activity;
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
import android.text.Editable;
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
import appframe.appframe.dto.OrderCategory;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.Token;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.widget.photopicker.adapter.ImagePublishAdapter;
import appframe.appframe.widget.photopicker.model.ImageItem;
import appframe.appframe.widget.photopicker.util.CustomConstants;
import appframe.appframe.widget.photopicker.util.IntentConstants;
import appframe.appframe.widget.photopicker.view.ImageBucketChooseActivity;
import appframe.appframe.widget.photopicker.view.ImageZoomActivity;

/**
 * Created by Administrator on 2016/5/17.
 */
public class QuestionSendActivity extends BaseActivity implements View.OnClickListener{

    private TextView btn_send,tb_back,tb_title,tv_titlecount,tv_contentcount;
    private EditText edit_title,edit_bounty,edit_content;
    private GridView mGridView;
    private ImagePublishAdapter mAdapter;
    Gson gson = new Gson();
    //private TextView sendTv;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    StringBuilder sb = new StringBuilder();
    public int upload_iamge_num = 0;
    private LinearLayout progress_bar;
    public static Activity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionsend);
        instance = this;
        init();
        initView();
        initData();
    }

    private void init()
    {
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title = (TextView)findViewById(R.id.tb_title);
        btn_send = (TextView)findViewById(R.id.btn_send);
        edit_title = (EditText)findViewById(R.id.edit_title);
        edit_bounty = (EditText)findViewById(R.id.edit_bounty);
        edit_content = (EditText)findViewById(R.id.edit_content);
        mGridView = (GridView)findViewById(R.id.gridview);
        tv_titlecount = (TextView)findViewById(R.id.tv_titlecount);
        tv_contentcount = (TextView)findViewById(R.id.tv_contentcount);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);

        edit_title.addTextChangedListener(titleWatcher);
        edit_content.addTextChangedListener(contentWatcher);
        edit_bounty.addTextChangedListener(textWatcher);

        tb_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        tb_title.setText("提问");
        tb_back.setText("取消");

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
                    new PopupWindows(QuestionSendActivity.this, mGridView);
                }
                else
                {
                    Intent intent = new Intent(QuestionSendActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);

                    intent.putExtra("from",
                            "QuestionSendActivity");
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
    protected void onPause()
    {
        super.onPause();
        saveTempToPref();
        MobclickAgent.onPageEnd("提问页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
        MobclickAgent.onPageStart("提问页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
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
        instance = null;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                removeTempFromPref();
                mDataList.clear();
                finish();
                break;
            case R.id.btn_send:
                if(edit_title.getText().toString().equals(""))
                {
                    Toast.makeText(QuestionSendActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
                }
                else {

                    if (edit_bounty.getText().toString().equals("")) {
                        Toast.makeText(QuestionSendActivity.this, "金额不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (edit_content.getText().toString().equals("")) {
                            Toast.makeText(QuestionSendActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Double.parseDouble(edit_bounty.getText().toString()) <= 0.00) {
                                Toast.makeText(QuestionSendActivity.this, "金额不能小于0.01元", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                progress_bar.setVisibility(View.VISIBLE);
                                if (mDataList.size() == 0) {
                                    Http.request(QuestionSendActivity.this, API.POST_QUESTION, Http.map(
                                            "Title", edit_title.getText().toString(),
                                            "Content", edit_content.getText().toString(),
                                            "Bounty", edit_bounty.getText().toString(),
                                            "Photos", ""
                                    ), new Http.RequestListener<Question>() {
                                        @Override
                                        public void onSuccess(Question result) {
                                            super.onSuccess(result);
                                            progress_bar.setVisibility(View.GONE);
//                                            mDataList.clear();
//                                            removeTempFromPref();
//                                            Toast.makeText(QuestionSendActivity.this, "提问成功", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent();
                                            Bundle bundle = new Bundle();
                                            intent.setClass(QuestionSendActivity.this, PayActivity.class);
                                            bundle.putSerializable("Question", result);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
//                                            finish();

                                        }

                                        @Override
                                        public void onFail(String code) {
                                            super.onFail(code);
                                            progress_bar.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    for (ImageItem dl : mDataList) {


                                        final File f = new File(dl.sourcePath);
                                        Http.request(QuestionSendActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
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
                                                            Http.request(QuestionSendActivity.this, API.POST_QUESTION, Http.map(
                                                                    "Title", edit_title.getText().toString(),
                                                                    "Content", edit_content.getText().toString(),
                                                                    "Bounty", edit_bounty.getText().toString(),
                                                                    "Photos", sb.deleteCharAt(0).toString()
                                                            ), new Http.RequestListener<Question>() {
                                                                @Override
                                                                public void onSuccess(Question result) {
                                                                    super.onSuccess(result);
                                                                    progress_bar.setVisibility(View.GONE);
//                                                                    mDataList.clear();
//                                                                    removeTempFromPref();
//                                                                    Toast.makeText(QuestionSendActivity.this, "提问成功", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent();
                                                                    Bundle bundle = new Bundle();
                                                                    intent.setClass(QuestionSendActivity.this, PayActivity.class);
                                                                    bundle.putSerializable("Question", result);
                                                                    intent.putExtras(bundle);
                                                                    startActivity(intent);

                                                                }

                                                                @Override
                                                                public void onFail(String code) {
                                                                    super.onFail(code);
                                                                    progress_bar.setVisibility(View.GONE);
                                                                }
                                                            });

                                                        }
                                                    }
                                                }, result.getUpToken());
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
                break;

        }
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

    private TextWatcher titleWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            tv_titlecount.setText(String.format("%d/10",s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    edit_bounty.setText(s);
                    edit_bounty.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                edit_bounty.setText(s);
                edit_bounty.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    edit_bounty.setText(s.subSequence(0, 1));
                    edit_bounty.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
                    Intent intent = new Intent(QuestionSendActivity.this,
                            ImageBucketChooseActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                            getAvailableSize());
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_CLASS,
                            QuestionSendActivity.class);
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

    }

}
