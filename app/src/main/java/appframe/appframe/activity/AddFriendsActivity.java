package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;

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
        tb_back.setText("添加好友");
        tb_title.setText("友帮");
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
                    intent.setClass(AddFriendsActivity.this,FriendsInfoActivity.class);
                    intent.putExtra("UserID", result.substring(9).toString());
                    intent.putExtra("AddFriend", true);
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

}
