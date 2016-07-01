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
import appframe.appframe.dto.SearchOrderTagResponse;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyMissionAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderTagAdapater;

/**
 * Created by Administrator on 2016/2/24.
 */
public class SearchTagActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    int page = 1;
    private TextView tb_back,tv_empty;
    private EditText et_search;
    private ListView lv_mysearch ;
    SwipeRefreshXOrderTagAdapater swipeRefreshXOrderTagAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchtag);
        init();
    }
    private  void init()
    {
        et_search = (EditText)findViewById(R.id.et_search);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        lv_mysearch = (ListView)findViewById(R.id.lv_mysearch);
        tb_back.setText("友帮");
        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        tb_back.setOnClickListener(this);
        lv_mysearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(SearchTagActivity.this, OrderSendActivity.class);
                String searchOrderTagResponse = (String) parent.getAdapter().getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("SearchOrderTagResponse", searchOrderTagResponse);
                if (position == 1) {
                    intent.putExtra("TagName", searchOrderTagResponse.substring(6));
                } else {
                    intent.putExtra("TagName", searchOrderTagResponse);
                }
                startActivity(intent);
            }
        });
        et_search.addTextChangedListener(textWatcher);


        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("TagName", URLEncoder.encode(et_search.getText().toString()));
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(SearchTagActivity.this, API.SEARCH_ORDERTAG, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchOrderTagResponse>() {
                    @Override
                    public void onSuccess(final SearchOrderTagResponse result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        if(result != null && result.getTagNames().size() !=0) {
                            List<String> tagName = new ArrayList<String>();
                            tagName.add("点击添加: " + et_search.getText().toString());
                            for(String tag : result.getTagNames())
                            {
                                tagName.add(tag);
                            }
                            swipeRefreshXOrderTagAdapater = new SwipeRefreshXOrderTagAdapater(SearchTagActivity.this, tagName);
                            lv_mysearch.setAdapter(swipeRefreshXOrderTagAdapater);
                            tv_empty.setVisibility(View.GONE);
                        }
                        else
                        {
                            List<String> tagName = new ArrayList<String>();
                            tagName.add("点击添加: " + et_search.getText().toString());
                            swipeRefreshXOrderTagAdapater = new SwipeRefreshXOrderTagAdapater(SearchTagActivity.this, tagName);
                            lv_mysearch.setAdapter(swipeRefreshXOrderTagAdapater);
                            tv_empty.setVisibility(View.GONE);
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
                map.put("TagName", URLEncoder.encode(et_search.getText().toString()));
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(SearchTagActivity.this, API.SEARCH_ORDERTAG, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchOrderTagResponse>() {
                    @Override
                    public void onSuccess(final SearchOrderTagResponse result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setLoading(false);
                        if(result != null && result.getTagNames().size() !=0) {
                            loadMore(swipeRefreshXOrderTagAdapater, result.getTagNames());
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
    private void loadMore(SwipeRefreshXOrderTagAdapater adapater, List<String> orderDetailses) {
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
                map.put("TagName", URLEncoder.encode(et_search.getText().toString()));
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(SearchTagActivity.this, API.SEARCH_ORDERTAG, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchOrderTagResponse>() {
                    @Override
                    public void onSuccess(final SearchOrderTagResponse result) {
                        super.onSuccess(result);

                        if(result != null && result.getTagNames().size() !=0) {
                            List<String> tagName = new ArrayList<String>();
                            tagName.add("点击添加: " + et_search.getText().toString());
                            for(String tag : result.getTagNames())
                            {
                                tagName.add(tag);
                            }
                            swipeRefreshXOrderTagAdapater = new SwipeRefreshXOrderTagAdapater(SearchTagActivity.this, tagName);
                            lv_mysearch.setAdapter(swipeRefreshXOrderTagAdapater);
                            tv_empty.setVisibility(View.GONE);
                        }
                        else
                        {
                            List<String> tagName = new ArrayList<String>();
                            tagName.add("点击添加: " + et_search.getText().toString());
                            swipeRefreshXOrderTagAdapater = new SwipeRefreshXOrderTagAdapater(SearchTagActivity.this, tagName);
                            lv_mysearch.setAdapter(swipeRefreshXOrderTagAdapater);
                            tv_empty.setVisibility(View.GONE);
                        }

                    }
                });
            }
            else
            {
                List<String> result = new ArrayList<String>();
                swipeRefreshXOrderTagAdapater = new SwipeRefreshXOrderTagAdapater(SearchTagActivity.this, result);
                lv_mysearch.setAdapter(swipeRefreshXOrderTagAdapater);
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

