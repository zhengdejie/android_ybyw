package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.dto.UserBrief;
import appframe.appframe.utils.Auth;

/**
 * Created by Administrator on 2015/12/25.
 */
public class AddFriendsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_title,tb_back,tv_scan,tv_search;
    public static final int SCAN_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_scan = (TextView)findViewById(R.id.tv_scan);
        tv_search = (TextView)findViewById(R.id.tv_search);
        tb_back.setText("友帮");
        tb_title.setText("添加好友");
        tb_back.setOnClickListener(this);
        tv_scan.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_scan:
                Intent intent = new Intent(AddFriendsActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_CODE);
                break;
            case R.id.tv_search:
                startActivity(new Intent(AddFriendsActivity.this,SearchFriendsActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_CODE:
                //TextView scanResult = (TextView) root.findViewById(R.id.txt_scanresult);
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.setClass(AddFriendsActivity.this, FriendsInfoActivity.class);
//                    bundle.putString("UserID", String.valueOf(Auth.getCurrentUserId()));
                    bundle.putBoolean("AddFriend",true);
//                    intent.putExtra("UserID", result.substring(9).toString());
//                    intent.putExtra("AddFriend", true);
                    UserBrief ub = new UserBrief();
                    ub.setId(Integer.parseInt(result.substring(9)));
                    bundle.putSerializable("UserID",ub);
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    Http.request(HomeActivity.this, API.ADD_FDF, new Object[]{Auth.getCurrentUserId()},
//                            Http.map("FriendId",result.substring(9).toString()),
//                            new Http.RequestListener<String>() {
//                                @Override
//                                public void onSuccess(String result) {
//                                    super.onSuccess(result);
//                                    Toast.makeText(HomeActivity.this, "已发送好友申请", Toast.LENGTH_SHORT).show();
//                                }
//                            });

                    //scanResult.setText(result);
                } else if (resultCode == RESULT_CANCELED) {
                    //scanResult.setText("扫描出错");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("添加好友页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("添加好友页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
