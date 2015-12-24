package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXUserBlock;

/**
 * Created by Administrator on 2015/8/25.
 */
public class WhoCannotSeeActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back;
    ListView lv_whocannotsee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whocannotsee);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_whocannotsee = (ListView)findViewById(R.id.lv_whocannotsee);
        tb_back.setText("隐私设置");
        tb_title.setText("");
        tb_back.setOnClickListener(this);

        lv_whocannotsee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(WhoCannotSeeActivity.this, FriendsInfoActivity.class);
                UserDetail userDetail = (UserDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserDetail", userDetail);
                bundle.putString("From", "WHOCANNOTSEE");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Map<String, String> map = new HashMap<String, String>();
        map.put("Id", String.valueOf(Auth.getCurrentUserId()));

        Http.request(WhoCannotSeeActivity.this, API.GET_NOTLETBSEE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
            @Override
            public void onSuccess(List<UserDetail> result) {
                super.onSuccess(result);

                lv_whocannotsee.setAdapter(new SwipeRefreshXUserBlock(WhoCannotSeeActivity.this, result));

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
}
