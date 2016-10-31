package appframe.appframe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyMissionAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/11/2.
 */
public class MyCollectActivity extends BaseActivity implements View.OnClickListener  {
    SwipeRefreshX swipeRefresh;
    SwipeRefreshXOrderAdapater swipeRefreshXOrderAdapater;
    private TextView tb_title,tb_back,tv_empty;
    private ListView lv_mycollect ;
    private List<OrderDetails> topOrderDetails = new ArrayList<OrderDetails>();
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        init();

    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        lv_mycollect = (ListView)findViewById(R.id.lv_mycollect);
        tb_back.setText("我的");
        tb_title.setText("我的收藏");
        tb_back.setOnClickListener(this);
        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        lv_mycollect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MyCollectActivity.this, OrderDetailsActivity.class);
                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                bundle.putString("Entrance", "mycollect");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private  void initData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        Http.request(MyCollectActivity.this, API.GET_FAVORITEORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
            @Override
            public void onSuccess(List<OrderDetails> result) {
                super.onSuccess(result);
                swipeRefreshXOrderAdapater = new SwipeRefreshXOrderAdapater(MyCollectActivity.this, result, AppConfig.ORDERSTATUS_PROGRESS);
                lv_mycollect.setAdapter(swipeRefreshXOrderAdapater);
                if (result != null && result.size() != 0) {
                    tv_empty.setVisibility(View.GONE);
                } else {
                    tv_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(String code) {
                super.onFail(code);
            }
        });


        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));

                Http.request(MyCollectActivity.this, API.GET_FAVORITEORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(List<OrderDetails> result) {
                        super.onSuccess(result);
                        swipeRefresh.setRefreshing(false);
                        page = 1;
                        swipeRefreshXOrderAdapater = new SwipeRefreshXOrderAdapater(MyCollectActivity.this, result, AppConfig.ORDERSTATUS_PROGRESS);
                        lv_mycollect.setAdapter(swipeRefreshXOrderAdapater);
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
                Http.request(MyCollectActivity.this, API.GET_FAVORITEORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(List<OrderDetails> result) {
                        super.onSuccess(result);
                        swipeRefresh.setLoading(false);
                        if (result != null) {

                            loadMore(swipeRefreshXOrderAdapater, result);
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

    private void loadMore(SwipeRefreshXOrderAdapater adapater, List<OrderDetails> orderDetailses) {
        adapater.addItems(orderDetailses);
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
        MobclickAgent.onPageStart("我的收藏页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的收藏页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
