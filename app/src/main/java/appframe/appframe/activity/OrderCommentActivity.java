package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.widget.photopicker.adapter.ImagePublishAdapter;
import appframe.appframe.widget.photopicker.model.ImageItem;
import appframe.appframe.widget.photopicker.util.CustomConstants;
import appframe.appframe.widget.photopicker.util.IntentConstants;
import appframe.appframe.widget.photopicker.view.ImageBucketChooseActivity;
import appframe.appframe.widget.photopicker.view.ImageZoomActivity;
import appframe.appframe.widget.tagview.Tag;
import appframe.appframe.widget.tagview.TagView;

/**
 * Created by Administrator on 2015/9/8.
 */
public class OrderCommentActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_back,tb_title,btn_evaluate,tv_progress_content;
    private EditText et_content;
    private LinearLayout progress_bar;
//    private TagView tagView;

    private RatingBar rb_service,rb_attitude,rb_personality;
    private LinearLayout ll_skill,ll_service;
    private GridView mGridView;
    private ImagePublishAdapter mAdapter;
    Gson gson = new Gson();
    //private TextView sendTv;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();
    StringBuilder sb = new StringBuilder();
    public int upload_iamge_num = 0;
    private String ConfirmedOrderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercomment);
        init();
        initView();
        initData();
    }
    protected void init()
    {
        rb_attitude = (RatingBar)findViewById(R.id.rb_attitude);
//        tv_addtag = (TextView)findViewById(R.id.tv_addtag);
//        edit_tag = (EditText)findViewById(R.id.edit_tag);
//        tagView = (TagView)findViewById(R.id.tagview);
        btn_evaluate = (TextView)findViewById(R.id.btn_evaluate);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        tv_progress_content = (TextView)findViewById(R.id.tv_progress_content);
        et_content = (EditText)findViewById(R.id.et_content);
        rb_service = (RatingBar)findViewById(R.id.rb_service);
        rb_attitude = (RatingBar)findViewById(R.id.rb_attitude);
        rb_personality = (RatingBar)findViewById(R.id.rb_personality);
        ll_skill = (LinearLayout)findViewById(R.id.ll_skill);
        ll_service = (LinearLayout)findViewById(R.id.ll_service);
        mGridView = (GridView)findViewById(R.id.gridview);
//        tv_addtag.setOnClickListener(this);
        btn_evaluate.setOnClickListener(this);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        rb_service.setRating(5);
        rb_attitude.setRating(5);
        rb_personality.setRating(5);
        tb_title.setText("评价");
        tb_back.setText("订单");
        tv_progress_content.setText("正在评价");
        tb_back.setOnClickListener(this);
        Log.i("sdfsd", getIntent().getStringExtra("Estimate"));
        if(getIntent().getStringExtra("Estimate") != null && getIntent().getStringExtra("Estimate").equals("1"))
        {

        }
        else if (getIntent().getStringExtra("Estimate") != null && getIntent().getStringExtra("Estimate").equals("2"))
        {
            ll_skill.setVisibility(View.GONE);
            ll_service.setVisibility(View.GONE);
        }
        else
        {

        }
        if(getIntent().getStringExtra("ConfirmedOrderId") != null)
        {
            ConfirmedOrderId = getIntent().getStringExtra("ConfirmedOrderId").toString();
        }
//        rb_attitude.getRating(true);
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
                    new PopupWindows(OrderCommentActivity.this, mGridView);
                }
                else
                {
                    Intent intent = new Intent(OrderCommentActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);

                    intent.putExtra("from",
                            "OrderCommentActivity");
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
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
        switch (v.getId())
        {
//            case R.id.tv_addtag:
//                if (edit_tag.getText().toString()!=null&&!edit_tag.getText().toString().equals("")) {
//                    String tagTitle= edit_tag.getText().toString();
//                    Tag tag = new Tag(tagTitle);
//                    tag.isDeletable=true;
//                    tagView.addTag(tag);
//
//                }
//                break;
            case R.id.btn_evaluate:

                if(et_content.getText().toString().equals(""))
                {
                    Toast.makeText(OrderCommentActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    progress_bar.setVisibility(View.VISIBLE);
                    if (mDataList.size() == 0) {
                        Http.request(this, API.EVALUATION_ORDER, Http.map(
                                "ConfirmedOrderId", ConfirmedOrderId,
                                "Content", et_content.getText().toString(),
                                "Commentator", String.valueOf(Auth.getCurrentUserId()),
                                "ServicePoint", String.valueOf((int) (rb_service.getRating())),
                                "AttitudePoint", String.valueOf((int) (rb_attitude.getRating())),
                                "CharacterPoint", String.valueOf((int) (rb_personality.getRating())),
//                        "Tags", "",
                                "Photos", ""
                        ), new Http.RequestListener<UserDetail>() {
                            @Override
                            public void onSuccess(UserDetail result) {
                                super.onSuccess(result);
                                progress_bar.setVisibility(View.GONE);
                                Toast.makeText(OrderCommentActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFail(String code) {
                                super.onFail(code);
                                progress_bar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        for (ImageItem dl : mDataList) {


                            final File f = new File(dl.sourcePath);
                            Http.request(OrderCommentActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
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
                                                Http.request(OrderCommentActivity.this, API.EVALUATION_ORDER, Http.map(
                                                        "ConfirmedOrderId", ConfirmedOrderId,
                                                        "Content", et_content.getText().toString(),
                                                        "Commentator", String.valueOf(Auth.getCurrentUserId()),
                                                        "ServicePoint", String.valueOf((int) (rb_service.getRating())),
                                                        "AttitudePoint", String.valueOf((int) (rb_attitude.getRating())),
                                                        "CharacterPoint", String.valueOf((int) (rb_personality.getRating())),
//                        "Tags", "",
                                                        "Photos", sb.deleteCharAt(0).toString()
                                                ), new Http.RequestListener<UserDetail>() {
                                                    @Override
                                                    public void onSuccess(UserDetail result) {
                                                        super.onSuccess(result);
                                                        mDataList.clear();
                                                        removeTempFromPref();
                                                        progress_bar.setVisibility(View.GONE);
                                                        Toast.makeText(OrderCommentActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFail(String code) {
                                                        super.onFail(code);
                                                        upload_iamge_num = 0;
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
                break;
            case R.id.tb_back:
                finish();
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
                    Intent intent = new Intent(OrderCommentActivity.this,
                            ImageBucketChooseActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
                            getAvailableSize());
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_CLASS,
                            OrderCommentActivity.class);
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
