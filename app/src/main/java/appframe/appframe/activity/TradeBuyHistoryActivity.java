package appframe.appframe.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXConfirmedOrderAdapater;

/**
 * Created by Administrator on 2016/5/23.
 */
public class TradeBuyHistoryActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    int page = 1;
    TextView tb_title,tb_back,tv_empty;
    LinearLayout progress_bar;
//    Map<String, String> map_apply = new HashMap<String, String>();
    SwipeRefreshXConfirmedOrderAdapater swipeRefreshXConfirmedOrderAdapater;
    String Type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradebuyhistory);
        init();
        initdata();
    }


    public void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);

        tb_back.setText("返回");
//        tb_title.setText("购买的服务");
        tb_back.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_tradehistory);

        if (getIntent().getStringExtra("buyservice") != null && !getIntent().getStringExtra("buyservice").toString().equals("")) {
            Type = getIntent().getStringExtra("buyservice").toString();
//            map_apply.put("Type", getIntent().getStringExtra("buyservice").toString());
            if (getIntent().getStringExtra("name") != null && !getIntent().getStringExtra("name").toString().equals("")) {
                tb_title.setText(String.format("%s购买的服务", getIntent().getStringExtra("name").toString()));
            }
        }
//        if (getIntent().getStringExtra("sellservice") != null && !getIntent().getStringExtra("sellservice").toString().equals("")) {
//            Type = getIntent().getStringExtra("sellservice").toString();
////            map_apply.put("Type", getIntent().getStringExtra("sellservice").toString());
//            if (getIntent().getStringExtra("name") != null && !getIntent().getStringExtra("name").toString().equals("")) {
//                tb_title.setText(String.format("%s出售的服务", getIntent().getStringExtra("name").toString()));
//            }
//        }

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setClass(MyQuestionActivity.this, OrderDetailsActivity.class);
//
//                Question question = (Question) parent.getAdapter().getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("Question", question);
//                intent.putExtras(bundle);
//
//                startActivity(intent);
//            }
//        });



    }



    public void initdata()
    {
        progress_bar.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<String, String>();
        map.put("Type",Type);
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
//        from = AppConfig.ORDERSTATUS_APPLY;


        Http.request(this, API.GET_CONFIRMEDORDERHISTORY, new Object[]{Integer.parseInt(getIntent().getStringExtra("userID").toString()), Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
            @Override
            public void onSuccess(List<ConfirmedOrderDetail> result) {
                super.onSuccess(result);
                progress_bar.setVisibility(View.GONE);
                swipeRefreshXConfirmedOrderAdapater = new SwipeRefreshXConfirmedOrderAdapater(TradeBuyHistoryActivity.this, result, AppConfig.HISTORYORDER);
                listView.setAdapter(swipeRefreshXConfirmedOrderAdapater);
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("Type", Type);
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));

                Http.request(TradeBuyHistoryActivity.this, API.GET_CONFIRMEDORDERHISTORY, new Object[]{Integer.parseInt(getIntent().getStringExtra("userID").toString()), Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXConfirmedOrderAdapater = new SwipeRefreshXConfirmedOrderAdapater(TradeBuyHistoryActivity.this, result, AppConfig.HISTORYORDER);
                        listView.setAdapter(swipeRefreshXConfirmedOrderAdapater);
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
                map.put("Type", Type);
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));

                Http.request(TradeBuyHistoryActivity.this, API.GET_CONFIRMEDORDERHISTORY, new Object[]{Integer.parseInt(getIntent().getStringExtra("userID").toString()), Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        if (result != null) {
                            loadMore(swipeRefreshXConfirmedOrderAdapater, result);
                        }
                        swipeRefresh.setLoading(false);

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

    private void loadMore(SwipeRefreshXConfirmedOrderAdapater adapater, List<ConfirmedOrderDetail> orderDetailses) {
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
        MobclickAgent.onPageStart("查看购买服务历史页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("查看购买服务历史页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}



