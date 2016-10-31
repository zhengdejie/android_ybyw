package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXUserBlock;

/**
 * Created by Administrator on 2015/12/10.
 */
public class CannotSeeActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back;
    ListView lv_cannotsee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cannotsee);
        init();

    }
    protected  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_cannotsee = (ListView)findViewById(R.id.lv_cannotsee);
        tb_back.setText("隐私设置");
        tb_title.setText("");
        tb_back.setOnClickListener(this);

        lv_cannotsee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CannotSeeActivity.this, FriendsInfoActivity.class);
                UserDetail userDetail = (UserDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserDetail", userDetail);
                bundle.putString("From", "CANNOTSEE");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    protected void initData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Id", String.valueOf(Auth.getCurrentUserId()));

        Http.request(CannotSeeActivity.this, API.GET_NOTSEEB, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
            @Override
            public void onSuccess(List<UserDetail> result) {
                super.onSuccess(result);

                lv_cannotsee.setAdapter(new SwipeRefreshXUserBlock(CannotSeeActivity.this, result));

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("不看TA发布的列表页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("不看TA发布的列表页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

