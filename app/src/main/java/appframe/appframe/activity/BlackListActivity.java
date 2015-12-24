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
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXUserBlock;

/**
 * Created by Administrator on 2015/12/10.
 */
public class BlackListActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back;
    ListView lv_blacklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_blacklist = (ListView)findViewById(R.id.lv_blacklist);
        tb_back.setText("隐私设置");
        tb_title.setText("黑名单");
        tb_back.setOnClickListener(this);

        lv_blacklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(BlackListActivity.this, FriendsInfoActivity.class);
                UserDetail userDetail = (UserDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserDetail", userDetail);
                bundle.putString("From", "BLACKLIST");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Http.request(BlackListActivity.this, API.GET_BLACKLIST, new Object[]{String.valueOf(Auth.getCurrentUserId())}, new Http.RequestListener<List<UserDetail>>() {
            @Override
            public void onSuccess(List<UserDetail> result) {
                super.onSuccess(result);

                lv_blacklist.setAdapter(new SwipeRefreshXUserBlock(BlackListActivity.this, result));

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


