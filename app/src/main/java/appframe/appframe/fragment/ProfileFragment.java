package appframe.appframe.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

import appframe.appframe.R;
import appframe.appframe.activity.SplashActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/21.
 */
public class ProfileFragment extends BaseFragment {
    static int MENU_LOGOUT = 0x1123;

    private static final int SELECT_PHOTO = 100;

    TextView name;
    com.android.volley.toolbox.NetworkImageView avatar;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null);
        name = (TextView)v.findViewById(R.id.name);
        avatar = (com.android.volley.toolbox.NetworkImageView)v.findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        avatar.setDefaultImageResId(R.drawable.ic_launcher);
        return v;
    }

    @Override
    protected void onLoadData() {
        super.onLoadData();
        name.setText("载入中");
        Http.request(getActivity(), API.USER_PROFILE, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<UserDetail>(){
            @Override
            public void onSuccess(UserDetail result) {
                super.onSuccess(result);

                name.setText(result.Name);
                ImageUtils.setImageUrl(avatar, result.Avatar);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == Activity.RESULT_OK){

                    File f = Utils.uriToFile(data.getData());

                    UploadUtils.uploadImage(f, new UploadUtils.Callback() {
                        @Override
                        public void done(String id) {
                            if (TextUtils.isEmpty(id)) {
                                // 上传失败
                                return;
                            }
                            Http.request(getActivity(), API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                                    "Avatar", id
                            ), new Http.RequestListener<UserDetail>(){
                                @Override
                                public void onSuccess(UserDetail result) {
                                    super.onSuccess(result);
                                    // 上传成功

                                    ImageUtils.setImageUrl(avatar, result.Avatar);
                                }
                            });
                        }
                    });
                }
        }
    }

    @Override
    public void createOptionsMenu(Menu menu) {
        createMenuItem(menu, MENU_LOGOUT, "退出");
        super.createOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == MENU_LOGOUT) {
            Auth.login(null, null);

            // 进首页
            SplashActivity.startRootActivity(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
