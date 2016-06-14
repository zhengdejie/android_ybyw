package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderCategory;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
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
    //private TextView sendTv;
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionsend);
        init();
        initView();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_send:
                if(edit_title.getText().toString().equals(""))
                {
                    Toast.makeText(QuestionSendActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if (edit_bounty.getText().toString().equals(""))
                    {
                        Toast.makeText(QuestionSendActivity.this, "金额不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (edit_content.getText().toString().equals(""))
                        {
                            Toast.makeText(QuestionSendActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Http.request(QuestionSendActivity.this, API.POST_QUESTION, Http.map(
                                    "Title", edit_title.getText().toString(),
                                    "Content", edit_content.getText().toString(),
                                    "Bounty", edit_bounty.getText().toString(),
                                    "Photos",""
                            ), new Http.RequestListener<Question>() {
                                @Override
                                public void onSuccess(Question result) {
                                    super.onSuccess(result);

                                    Toast.makeText(QuestionSendActivity.this, "提问成功", Toast.LENGTH_SHORT).show();
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
                                }
                            });
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

}
