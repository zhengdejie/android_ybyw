package appframe.appframe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.SelfEvaluationDetail;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.utils.Utils;

/**
 * Created by Administrator on 2015/12/8.
 */
public class EditSignatureActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title, tb_back,tb_action;
    private com.android.volley.toolbox.NetworkImageView iv_showavatar;
    private EditText et_signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsignature);
        init();
        inidata();
    }

    private void init() {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tb_action = (TextView) findViewById(R.id.tb_action);
        et_signature = (EditText) findViewById(R.id.et_signature);
        iv_showavatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_showavatar);
        tb_back.setText("个人中心");
        tb_title.setText("个人签名");
        tb_action.setText("保存");

        tb_action.setEnabled(false);
        tb_back.setOnClickListener(this);
        tb_action.setOnClickListener(this);
        iv_showavatar.setOnClickListener(this);
        iv_showavatar.setDefaultImageResId(R.drawable.icon_addpic_focused);
        et_signature.addTextChangedListener(textWatcher);
        Selection.setSelection(et_signature.getText(), et_signature.getText().length());
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
                    ImageUtils.setImageUrl(iv_showavatar, result.getPhotos());
                }
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tb_action.setBackgroundColor(getResources().getColor(R.color.green));
            tb_action.setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_action:
                Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
                        "Description", et_signature.getText().toString()
                ), new Http.RequestListener<UserDetail>() {
                    @Override
                    public void onSuccess(UserDetail result) {
                        super.onSuccess(result);
                        // 上传成功
//                        Auth.updateCurrentUser(result);
                        finish();
                    }
                });
                break;
            case R.id.iv_showavatar:
                new PopupWindows_Picture(this, iv_showavatar);
            break;

        }

    }

    private static final int TAKE_PICTURE = 0x000300;
    private static final int SELECT_PHOTO = 0x000100;
    private String path = "";
    public class PopupWindows_Picture extends PopupWindow
    {

        public PopupWindows_Picture(final Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            RelativeLayout rl_photopopup = (RelativeLayout)view.findViewById(R.id.rl_photopopup);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            rl_photopopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
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
                    Intent photoPickerIntent = new Intent(Intent.CATEGORY_OPENABLE);
                    photoPickerIntent.setType("image/*");
                    if (Build.VERSION.SDK_INT <19) {
                        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    }else {
                        photoPickerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    }
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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

    public void takePhoto()
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory()
                , "tempAvatar.jpg");
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
    protected void  onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    //Bitmap bm = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    final File f = new File(Environment.getExternalStorageDirectory()
                            , "tempAvatar.jpg");

                    Http.request(EditSignatureActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
                        @Override
                        public void onSuccess(Token result) {
                            super.onSuccess(result);

                            UploadUtils.uploadImage(f, new UploadUtils.Callback() {
                                @Override
                                public void done(final String id) {

                                    Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                            "Photos", id
                                    ), new Http.RequestListener<UserDetail>() {
                                        @Override
                                        public void onSuccess(UserDetail result) {
                                            super.onSuccess(result);
                                            // 上传成功
                                            ImageUtils.setImageUrl(iv_showavatar, id);
                                        }
                                    });
                                }
                            }, result.getUpToken());
                        }
                    });
                    //ImageUtils.setImageUrl(iv_showavatar, result.Avatar);//想图像显示在ImageView视图上，private ImageView img;
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                    final File f = Utils.uriToFile(imageReturnedIntent.getData());

                    Http.request(EditSignatureActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
                        @Override
                        public void onSuccess(Token result) {
                            super.onSuccess(result);

                            UploadUtils.uploadImage(f, new UploadUtils.Callback() {
                                @Override
                                public void done(final String id) {
                                    Http.request(EditSignatureActivity.this, API.POST_SELFEVALUATION, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                            "Photos", id
                                    ), new Http.RequestListener<UserDetail>() {
                                        @Override
                                        public void onSuccess(UserDetail result) {
                                            super.onSuccess(result);
                                            // 上传成功
                                            ImageUtils.setImageUrl(iv_showavatar, id);
                                        }
                                    });
                                }
                            }, result.getUpToken());
                        }
                    });
                }
                break;
        }
    }

}
