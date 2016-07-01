package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.SearchResult;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyMissionAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXQuestionAdapater;

/**
 * Created by Administrator on 2016-06-29.
 */
public class ServiceMoreActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    private TextView tv_empty,tb_back;
    private EditText et_search;
    int page = 1;
    SwipeRefreshXOrderAdapater swipeRefreshXOrderAdapater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicemore);
        init();
    }
    private  void init()
    {
        et_search = (EditText)findViewById(R.id.et_search);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("返回");
        tb_back.setOnClickListener(this);
        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_mymission);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ServiceMoreActivity.this, OrderDetailsActivity.class);

                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                bundle.putString("From", "MyOrder");
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        et_search.addTextChangedListener(textWatcher);

        et_search.setText(getIntent().getStringExtra("edittext"));
        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("KeyWord", URLEncoder.encode(et_search.getText().toString()));
                map.put("Type", "1");
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(ServiceMoreActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchResult>() {
                    @Override
                    public void onSuccess(final SearchResult result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        if (result.getOrders() != null) {

                            tv_empty.setVisibility(View.GONE);
                            swipeRefreshXOrderAdapater = new SwipeRefreshXOrderAdapater(ServiceMoreActivity.this, result.getOrders(), AppConfig.ORDERSTATUS_MAIN);
                            listView.setAdapter(swipeRefreshXOrderAdapater);

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
                map.put("KeyWord", URLEncoder.encode(et_search.getText().toString()));
                map.put("Type", "1");
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(ServiceMoreActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchResult>() {
                    @Override
                    public void onSuccess(final SearchResult result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setLoading(false);
                        if (result.getOrders() != null) {

                            loadMore(swipeRefreshXOrderAdapater, result.getOrders());

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

    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(!et_search.getText().toString().equals("")) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("KeyWord", URLEncoder.encode(et_search.getText().toString()));
                map.put("Type", "1");
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(ServiceMoreActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchResult>() {
                    @Override
                    public void onSuccess(final SearchResult result) {
                        super.onSuccess(result);
                        if (result.getOrders() != null) {
                            tv_empty.setVisibility(View.GONE);
                            swipeRefreshXOrderAdapater = new SwipeRefreshXOrderAdapater(ServiceMoreActivity.this, result.getOrders(), AppConfig.ORDERSTATUS_MAIN);
                            listView.setAdapter(swipeRefreshXOrderAdapater);

                        }
                        else
                        {
                            tv_empty.setVisibility(View.VISIBLE);
                        }


                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                    }
                });
            }
            else
            {
                tv_empty.setVisibility(View.VISIBLE);
                List<OrderDetails> result = new ArrayList<OrderDetails>();
                swipeRefreshXOrderAdapater = new SwipeRefreshXOrderAdapater(ServiceMoreActivity.this, result, AppConfig.ORDERSTATUS_MAIN);
                listView.setAdapter(swipeRefreshXOrderAdapater);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
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
