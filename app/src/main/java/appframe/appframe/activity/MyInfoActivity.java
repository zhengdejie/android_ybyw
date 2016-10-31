package appframe.appframe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

import java.io.File;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.utils.Utils;
import appframe.appframe.widget.photopicker.model.ImageItem;
import appframe.appframe.widget.photopicker.util.CustomConstants;
import appframe.appframe.widget.photopicker.util.IntentConstants;
import appframe.appframe.widget.photopicker.view.ImageBucketChooseActivity;

/**
 * Created by Administrator on 2015/8/24.
 */
public class MyInfoActivity extends BaseActivity implements View.OnClickListener{

    private TextView tb_title,tb_back,tv_avatar,tv_nickname,tv_shownickname,tv_sex,tv_showsex,tv_qrcode,tv_district,tv_showdistrict,tv_selfestimate,tv_showselfestimate;
    private com.android.volley.toolbox.NetworkImageView iv_showavatar;
    PopupWindows popupWindows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_shownickname.setText(Auth.getCurrentUser().getName());
        tv_showsex.setText(Auth.getCurrentUser().getGender());
        tv_showdistrict.setText(Auth.getCurrentUser().getLocation());
        tv_showselfestimate.setText(Auth.getCurrentUser().getSignature());
        MobclickAgent.onPageStart("个人信息页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人信息页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_avatar = (TextView)findViewById(R.id.tv_avatar);
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);
        tv_shownickname = (TextView)findViewById(R.id.tv_shownickname);
        tv_sex = (TextView)findViewById(R.id.tv_sex);
        tv_qrcode = (TextView)findViewById(R.id.tv_qrcode);
        tv_district = (TextView)findViewById(R.id.tv_district);
        tv_showdistrict = (TextView)findViewById(R.id.tv_showdistrict);
        tv_selfestimate = (TextView)findViewById(R.id.tv_selfestimate);
        tv_showsex = (TextView)findViewById(R.id.tv_showsex);
        iv_showavatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_showavatar);
        tv_showselfestimate = (TextView)findViewById(R.id.tv_showselfestimate);
//        tv_author = (TextView)findViewById(R.id.tv_author);
        //iv_showavatar.setDefaultImageResId(R.drawable.ic_launcher);
        tb_back.setText("我的");
        tb_title.setText("个人信息");

        tb_back.setOnClickListener(this);
        tv_avatar.setOnClickListener(this);
        tv_nickname.setOnClickListener(this);
        tv_sex.setOnClickListener(this);
        tv_qrcode.setOnClickListener(this);
        tv_district.setOnClickListener(this);
        tv_selfestimate.setOnClickListener(this);
        iv_showavatar.setOnClickListener(this);
//        tv_author.setOnClickListener(this);

        tv_shownickname.setText(Auth.getCurrentUser().getName());
        tv_showsex.setText(Auth.getCurrentUser().getGender());

        if(Auth.getCurrentUser().getAvatar() != null && !Auth.getCurrentUser().getAvatar().equals(""))
        {
            ImageUtils.setImageUrl(iv_showavatar, Auth.getCurrentUser().getAvatar());
        }
        else
        {
            if(Auth.getCurrentUser().getGender().equals(getResources().getString(R.string.male).toString()))
            {
                iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
            }
            else
            {
                iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
            }

        }
        tv_showdistrict.setText(Auth.getCurrentUser().getLocation());
        tv_showselfestimate.setText(Auth.getCurrentUser().getSignature());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_avatar:
                new PopupWindows_Picture(this, tv_avatar);
                break;
            case R.id.tv_nickname:
                startActivity(new Intent(this,EditNickNameActivity.class));
                break;
            case R.id.tv_sex:
                popupWindows = new PopupWindows(this, tv_sex);
                break;
            case R.id.tv_qrcode:
                startActivity(new Intent(this,QRCodeActivity.class));
                break;
            case R.id.tv_district:
                startActivity(new Intent(this,CitySelectActivity.class));
                break;
            case R.id.tv_selfestimate:
                startActivity(new Intent(this,EditSignatureActivity.class));
                break;
            case R.id.iv_showavatar:
                if(Auth.getCurrentUser().getAvatar() != null && !Auth.getCurrentUser().getAvatar().equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(this, AvatarZoomActivity.class);
                    intent.putExtra("Avatar", Auth.getCurrentUser().getAvatar());
                    startActivity(intent);
                }
                else
                {
                    new PopupWindows_Picture(this, tv_avatar);
                }
                break;
//            case R.id.tv_author:
//                startActivity(new Intent(this,CertificateActivity.class));
//                break;

        }

    }

    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_sexchoose, null);
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
            showAtLocation(parent, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            update();
            //View mask = (View)view.findViewById(R.id.mask);
            //mask.bringToFront();
            TextView tv_sexmale = (TextView) view
                    .findViewById(R.id.tv_sexmale);
            TextView tv_sexfemale = (TextView) view
                    .findViewById(R.id.tv_sexfemale);
            Drawable icon = getResources().getDrawable(R.drawable.ic_task_status_list_check);
            if(Auth.getCurrentUser().getGender() == null ? false : Auth.getCurrentUser().getGender().equals(getResources().getString(R.string.male).toString()))
            {
                tv_sexmale.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            }

            else if (Auth.getCurrentUser().getGender()  == null ? false : Auth.getCurrentUser().getGender().equals(getResources().getString(R.string.female).toString()))
            {
                tv_sexfemale.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            }
            else
            {

            }
            ll_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindows.dismiss();
                }
            });
            tv_sexmale.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(MyInfoActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "Gender", getResources().getString(R.string.male).toString()
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);
                            // 上传成功
                            Auth.updateCurrentUser(result);
                            tv_showsex.setText(Auth.getCurrentUser().getGender());
                        }
                    });
                    dismiss();
                }
            });
            tv_sexfemale.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(MyInfoActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "Gender", getResources().getString(R.string.female).toString()
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);
                            // 上传成功
                            Auth.updateCurrentUser(result);
                            tv_showsex.setText(Auth.getCurrentUser().getGender());
                        }
                    });
                    dismiss();
                }
            });

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

                    Http.request(MyInfoActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
                        @Override
                        public void onSuccess(Token result) {
                            super.onSuccess(result);

                            UploadUtils.uploadImage(f, new UploadUtils.Callback() {
                                @Override
                                public void done(String id) {
                                    Http.request(MyInfoActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                            "Avatar", id
                                    ), new Http.RequestListener<UserDetail>() {
                                        @Override
                                        public void onSuccess(UserDetail result) {
                                            super.onSuccess(result);
                                            // 上传成功
                                            Auth.updateCurrentUser(result);
                                            ImageUtils.setImageUrl(iv_showavatar, result.Avatar);
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

                    Http.request(MyInfoActivity.this, API.GetQINIUUploadToken, new Http.RequestListener<Token>() {
                        @Override
                        public void onSuccess(Token result) {
                            super.onSuccess(result);

                            UploadUtils.uploadImage(f, new UploadUtils.Callback() {
                                @Override
                                public void done(String id) {
                                    Http.request(MyInfoActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                            "Avatar", id
                                    ), new Http.RequestListener<UserDetail>() {
                                        @Override
                                        public void onSuccess(UserDetail result) {
                                            super.onSuccess(result);
                                            // 上传成功
                                            Auth.updateCurrentUser(result);
                                            ImageUtils.setImageUrl(iv_showavatar, result.Avatar);
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
