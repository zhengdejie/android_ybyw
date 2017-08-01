package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.ConversationListUICustomSample;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendShopsAdapater;

/**
 * Created by Administrator on 2017/7/26.
 */

public class DiscoveryActivity  extends BaseActivity implements View.OnClickListener{

    TextView tb_title,tb_back,tv_nearby,tv_empty;
    SwipeRefreshX swipeRefresh;
    ListView listView;
    int page = 1;
    SwipeRefreshXFriendShopsAdapater swipeRefreshXFriendShopsAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_discovery);
        init();
//        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUICustomSample.class);
    }
//    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        root  = inflater.inflate(R.layout.fragment_discovery, null);
//        init();
//        return root;
//    }
//
//    @Override
//    protected void onLoadData() {
//
//    }

    public void init()
    {
        tv_nearby = (TextView) findViewById(R.id.tv_nearby);
        tv_nearby.setOnClickListener(this);

        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        tb_title.setText("发现");
        tb_back.setText("我的");
        tb_back.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_topfs);

        Map<String, String> map = new HashMap<String, String>();
        map.put("UserId", String.valueOf(Auth.getCurrentUserId()));
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        Http.request(DiscoveryActivity.this, API.GET_FRIENDTRACE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetailWithFriend>>() {
            @Override
            public void onSuccess(List<ConfirmedOrderDetailWithFriend> result) {
                super.onSuccess(result);
                swipeRefreshXFriendShopsAdapater = new SwipeRefreshXFriendShopsAdapater(DiscoveryActivity.this, result);
                listView.setAdapter(swipeRefreshXFriendShopsAdapater);
                if(result != null && result.size() != 0) {
                    tv_empty.setVisibility(View.GONE);
                }
                else {
                    tv_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(String code) {
                super.onFail(code);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConfirmedOrderDetailWithFriend confirmedOrderDetailWithFriend = (ConfirmedOrderDetailWithFriend)parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(DiscoveryActivity.this, OrderDetailsActivity.class);
                bundle.putSerializable("ConfirmedOrderDetailWithFriend", confirmedOrderDetailWithFriend);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("UserId", String.valueOf(Auth.getCurrentUserId()));

                Http.request(DiscoveryActivity.this, API.GET_FRIENDTRACE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetailWithFriend>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetailWithFriend> result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        listView.setAdapter(new SwipeRefreshXFriendShopsAdapater(DiscoveryActivity.this, result));
                        if (result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        } else {
                            tv_empty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {
                page++;
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("UserId", String.valueOf(Auth.getCurrentUserId()));

                Http.request(DiscoveryActivity.this, API.GET_FRIENDTRACE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetailWithFriend>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetailWithFriend> result) {
                        super.onSuccess(result);
                        swipeRefresh.setLoading(false);
                        if (result != null) {

                            loadMore(swipeRefreshXFriendShopsAdapater, result);
                        }
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setLoading(false);
                    }
                });


            }
        });

    }

    private void loadMore(SwipeRefreshXFriendShopsAdapater adapater, List<ConfirmedOrderDetailWithFriend> orderDetailses) {
        adapater.addItems(orderDetailses);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;

            case R.id.tv_nearby:
                startActivity(new Intent(DiscoveryActivity.this,NearByActivity.class));
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("发现页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(DiscoveryActivity.this);          //统计时长
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发现页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(DiscoveryActivity.this);
    }
}
