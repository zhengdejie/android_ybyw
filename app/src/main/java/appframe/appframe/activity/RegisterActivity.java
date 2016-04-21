package appframe.appframe.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AuthResult;
import appframe.appframe.dto.UserContact;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.utils.Utils;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by dashi on 15/6/21.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private static final int SELECT_PHOTO = 100;
    TextView tb_back,tb_action,tb_title,tv_progress_content,tv_code;
    EditText  password, name,et_mobile,et_code;
    View ok;
    ImageButton avatar;
    List<UserContact> contactsList = new ArrayList<UserContact>();
    LinearLayout progress_bar;
    String uploadedAvatarId;
    private MyCount mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tv_code = (TextView)findViewById(R.id.tv_code);
//        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        et_mobile = (EditText)findViewById(R.id.et_mobile);
        et_code = (EditText)findViewById(R.id.et_code);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        tv_progress_content = (TextView)findViewById(R.id.tv_progress_content);
        tv_progress_content.setText("正在注册");

        ok = findViewById(R.id.ok);
        avatar = (ImageButton)findViewById(R.id.avatar);

        tb_action.setOnClickListener(this);
        avatar.setOnClickListener(this);
        ok.setOnClickListener(this);
        tv_code.setOnClickListener(this);



        tb_back.setVisibility(View.GONE);
        tb_action.setText("登入");
        tb_title.setText("友帮");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.avatar:
                uploadedAvatarId = null;
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                //Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            case R.id.ok:
                if(et_mobile.getText().toString().equals("") || et_mobile.getText() == null)
                {
                    Toast.makeText(this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(name.getText().toString().equals("") || name.getText() == null)
                    {
                        Toast.makeText(this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(password.getText().toString().equals("") || password.getText() == null)
                        {
                            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(et_code.getText().toString().equals("") || et_code.getText() == null)
                            {
                                Toast.makeText(this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progress_bar.setVisibility(View.VISIBLE);
                                Http.request(RegisterActivity.this, API.USER_REGISTER, Http.map(

                                        "Password", password.getText().toString(),
                                        "Avatar", uploadedAvatarId,
                                        "Name", name.getText().toString(),
                                        "Mobile", et_mobile.getText().toString(),
                                        "Code",et_code.getText().toString()

                                ), new Http.RequestListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult result) {
                                        super.onSuccess(result);
                                        try {

                                            contactsList = UploadUtils.uploadContact(RegisterActivity.this);
                                            Auth.login(result.Token, result.User);

                                            Http.request(RegisterActivity.this, API.USER_CONTACT_UPLOAD, Http.map("Contact", GsonHelper.getGson().toJson(contactsList),
                                                    "Id", String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
                                                @Override
                                                public void onSuccess(String result) {
                                                    super.onSuccess(result);
                                                    SplashActivity.startRootActivity(RegisterActivity.this);
                                                    SMSSDK.unregisterEventHandler(eh);
                                                    progress_bar.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onFail(String code) {
                                                    super.onFail(code);
                                                    Toast.makeText(RegisterActivity.this,code,Toast.LENGTH_SHORT).show();
                                                    SplashActivity.startRootActivity(RegisterActivity.this);
                                                    progress_bar.setVisibility(View.GONE);
                                                }
                                            });

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                        Toast.makeText(RegisterActivity.this,code,Toast.LENGTH_SHORT).show();
                                        progress_bar.setVisibility(View.GONE);
                                    }
                                });//HTTP
                            }
                        }
                    }
                }

                break;
            case R.id.tv_code:
                if(!et_mobile.getText().toString().equals("") && et_mobile.getText() != null) {
                    tv_code.setEnabled(false);
                    mc = new MyCount(60000, 1000);
                    mc.start();
                    SMSSDK.registerEventHandler(eh); //注册短信回调
                    SMSSDK.getVerificationCode("86", et_mobile.getText().toString());
                }
                else
                {
                    Toast.makeText(this,"请填写手机号",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tb_action:
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;

        }
    }

    EventHandler eh=new EventHandler(){

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
//                    SMSSDK.submitVerificationCode("86",et_mobile.getText().toString(),et_code.getText().toString());
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
//                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    String country = (String) phoneMap.get("country");

                }
            }else{
                ((Throwable)data).printStackTrace();
            }
        }
    };

//    private String getPhoneNumber(){
//        TelephonyManager mTelephonyMgr;
//        mTelephonyMgr = (TelephonyManager)  getSystemService(Context.TELEPHONY_SERVICE);
//        return mTelephonyMgr.getLine1Number();
//    }

    /*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            tv_code.setEnabled(true);
            tv_code.setText("获取验证码");
        }
        @Override
        public void onTick(long millisUntilFinished) {
            SimpleDateFormat sdf = new SimpleDateFormat("ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            Date date = new Date(millisUntilFinished);
            String text = sdf.format(date);

            tv_code.setText("(" + text +"）"+ "获取验证码");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
//                    ContentResolver cr = this.getContentResolver();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(imageReturnedIntent.getData()));
//                    }
//                    catch (FileNotFoundException e)
//                    {
//
//                    }
                    File f = Utils.uriToFile(imageReturnedIntent.getData());
                    Bitmap bm = Utils.getResizedBitmap(f, Utils.dpToPx(100), Utils.dpToPx(100));
                    avatar.setImageBitmap(bm);
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
                }
        }
    }


//    static final int MENU_LOGIN = 1;
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        createMenuItem(menu, MENU_LOGIN, "登录");
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case MENU_LOGIN:
//                finish();
//                startActivity(new Intent(this, LoginActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
