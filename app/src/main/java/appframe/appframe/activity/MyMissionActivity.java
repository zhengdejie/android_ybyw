package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MyMissionActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back;
    LinearLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymission);
        init();
        initdata();
    }

    public void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);

        tb_back.setText("我的");
        tb_title.setText("我的任务");
        tb_back.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_mymission);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MyMissionActivity.this, OrderDetailsActivity.class);

                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                bundle.putString("From", "MyOrder");
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });



    }



    public void initdata()
    {
        Http.request(MyMissionActivity.this, API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
            @Override
            public void onSuccess(List<OrderDetails> result) {
                super.onSuccess(result);
                progress_bar.setVisibility(View.GONE);
                listView.setAdapter(new SwipeRefreshXOrderAdapater(MyMissionActivity.this, result, AppConfig.ORDERSTATUS_DELETE));
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

//                Page = 1;
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Page", "1");
//                map.put("Limit", String.valueOf(AppConfig.ORDER_SIZE));
//                map.put("Latitude", latitude);
//                map.put("Longitude", longitude);
//                map.put("UserId", String.valueOf(Auth.getCurrentUserId()));
//
//                Http.request(NearByActivity.this, API.GET_USERNEARBY, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<Nearby>>() {
//                    @Override
//                    public void onSuccess(List<Nearby> result) {
//                        super.onSuccess(result);
//
//                        adapater = new SwipeRefreshXNearbyAdapater(NearByActivity.this, result);
//                        listView.setAdapter(adapater);
//
//                    }
//                });
                swipeRefresh.setRefreshing(false);

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

//                Page++;
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Page", String.valueOf(Page));
//                map.put("Limit", String.valueOf(AppConfig.ORDER_SIZE));
//                map.put("Latitude", latitude);
//                map.put("Longitude", longitude);
//                map.put("UserId", String.valueOf(Auth.getCurrentUserId()));
//
//                Http.request(NearByActivity.this, API.GET_USERNEARBY, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<Nearby>>() {
//                    @Override
//                    public void onSuccess(List<Nearby> result) {
//                        super.onSuccess(result);
//                        if (result != null) {
//
//                            adapater.addItems(result);
//                        }
//                        //listView.setAdapter(adapater);
//
//                    }
//                });
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

