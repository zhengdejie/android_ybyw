package appframe.appframe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Created by dashi on 15/6/21.
 */
public class RegisterActivity extends BaseActivity {
    private static final int SELECT_PHOTO = 100;
    TextView tb_back,tb_action,tb_title;
    EditText email, password, name,et_mobile;
    View ok;
    ImageButton avatar;
    List<UserContact> contactsList = new ArrayList<UserContact>();

    String uploadedAvatarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        et_mobile = (EditText)findViewById(R.id.et_mobile);

        ok = findViewById(R.id.ok);
        avatar = (ImageButton)findViewById(R.id.avatar);

        ok.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Http.request(RegisterActivity.this, API.USER_REGISTER, Http.map(
                        "Email", email.getText().toString(),
                        "Password", password.getText().toString(),
                        "Avatar", uploadedAvatarId,
                        "Name", name.getText().toString(),
                        "Mobile",et_mobile.getText().toString()

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
                                }
                            });

                            // 进首页
                            SplashActivity.startRootActivity(RegisterActivity.this);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFail(String code) {
                        super.onFail(code);

                    }
                });
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadedAvatarId = null;
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                //Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        tb_back.setVisibility(View.GONE);
        tb_action.setText("登入");
        tb_title.setText("友帮");
        tb_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
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

    static final int MENU_LOGIN = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        createMenuItem(menu, MENU_LOGIN, "登录");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case MENU_LOGIN:
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
