package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyfocusAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXUser;

/**
 * Created by Administrator on 2016-07-28.
 */
public class MyfocusActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_empty;
    LinearLayout progress_bar;
    SwipeRefreshXMyfocusAdapater adapater;
    int Page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfocus);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    public void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);

        tb_back.setText("我的");
        tb_title.setText("我的关注");
        tb_back.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_nearby);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetail nearby = (UserDetail) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(MyfocusActivity.this, FriendsInfoActivity.class);
                bundle.putSerializable("UserDetail", nearby);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



    }



    public void initdata()
    {
        Page = 1;
        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        map.put("Id", String.valueOf(Auth.getCurrentUserId()));

        Http.request(MyfocusActivity.this, API.GETALLFOCUS, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
            @Override
            public void onSuccess(List<UserDetail> result) {
                super.onSuccess(result);
                progress_bar.setVisibility(View.GONE);
                adapater = new SwipeRefreshXMyfocusAdapater(MyfocusActivity.this, result);
                listView.setAdapter(adapater);
                if (result != null && result.size() != 0) {
                    tv_empty.setVisibility(View.GONE);
                } else {
                    tv_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(String code) {
                super.onFail(code);
                progress_bar.setVisibility(View.GONE);
            }
        });



        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Page = 1;
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Id", String.valueOf(Auth.getCurrentUserId()));

                Http.request(MyfocusActivity.this, API.GETALLFOCUS, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
                    @Override
                    public void onSuccess(List<UserDetail> result) {
                        super.onSuccess(result);

                        adapater = new SwipeRefreshXMyfocusAdapater(MyfocusActivity.this, result);
                        listView.setAdapter(adapater);
                        if (result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        } else {
                            tv_empty.setVisibility(View.VISIBLE);
                        }
                    }
                });
                swipeRefresh.setRefreshing(false);

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                Page++;
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", String.valueOf(Page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Id", String.valueOf(Auth.getCurrentUserId()));

                Http.request(MyfocusActivity.this, API.GETALLFOCUS, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
                    @Override
                    public void onSuccess(List<UserDetail> result) {
                        super.onSuccess(result);
                        if (result != null) {

                            adapater.addItems(result);
                        }
                        //listView.setAdapter(adapater);

                    }
                });
                swipeRefresh.setLoading(false);

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

