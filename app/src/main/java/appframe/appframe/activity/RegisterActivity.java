package appframe.appframe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AuthResult;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/21.
 */
public class RegisterActivity extends BaseActivity {
    private static final int SELECT_PHOTO = 100;
    EditText email, password, name;
    View ok;
    ImageButton avatar;

    String uploadedAvatarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);

        ok = findViewById(R.id.ok);
        avatar = (ImageButton)findViewById(R.id.avatar);

        ok.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Http.request(RegisterActivity.this, API.USER_REGISTER, Http.map(
                        "Email", email.getText().toString(),
                        "Password", password.getText().toString(),
                        "Avatar", uploadedAvatarId,
                        "Name", name.getText().toString()

                ), new Http.RequestListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult result) {
                        super.onSuccess(result);


                        Auth.login(result.Token, result.User);

                        // 进首页
                        SplashActivity.startRootActivity(RegisterActivity.this);
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
                    UploadUtils.uploadImage(f, new UploadUtils.Callback() {
                        @Override
                        public void done(String id) {
                            if(TextUtils.isEmpty(id)){
                                // 上传失败
                                uploadedAvatarId = null;
                                avatar.setImageResource(R.drawable.ic_launcher);
                                return;
                            }
                            uploadedAvatarId = id;
                        }
                    });
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
