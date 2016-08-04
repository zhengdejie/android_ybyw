package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyMissionAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXUser;

/**
 * Created by Administrator on 2015/12/25.
 */
public class SearchFriendsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_back;
    private EditText et_search;
    private ListView lv_mysearch ;
    SwipeRefreshX swipeRefresh;
    int page = 1;
    SwipeRefreshXUser swipeRefreshXUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchfriends);
        init();
    }
    private  void init()
    {
        et_search = (EditText)findViewById(R.id.et_search);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_mysearch = (ListView)findViewById(R.id.lv_mysearch);
        tb_back.setText("添加好友");
        tb_back.setOnClickListener(this);
        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        lv_mysearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(SearchFriendsActivity.this, FriendsInfoActivity.class);
                UserDetail userDetail = (UserDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("UserDetail", userDetail);
                bundle.putBoolean("AddFriend",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        et_search.addTextChangedListener(textWatcher);

        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("KeyWord", URLEncoder.encode(et_search.getText().toString()));
                Http.request(SearchFriendsActivity.this, API.SEARCH_USER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
                    @Override
                    public void onSuccess(final List<UserDetail> result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        swipeRefreshXUser = new SwipeRefreshXUser(SearchFriendsActivity.this, result);
                        lv_mysearch.setAdapter(swipeRefreshXUser);

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
                map.put("KeyWord", URLEncoder.encode(et_search.getText().toString()));
                Http.request(SearchFriendsActivity.this, API.SEARCH_USER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
                    @Override
                    public void onSuccess(final List<UserDetail> result) {
                        super.onSuccess(result);
                        if (result != null) {

                            loadMore(swipeRefreshXUser, result);
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

    private void loadMore(SwipeRefreshXUser adapater, List<UserDetail> orderDetailses) {
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
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("KeyWord", URLEncoder.encode(et_search.getText().toString()));
                Http.request(SearchFriendsActivity.this, API.SEARCH_USER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
                    @Override
                    public void onSuccess(final List<UserDetail> result) {
                        super.onSuccess(result);

                        swipeRefreshXUser = new SwipeRefreshXUser(SearchFriendsActivity.this, result);
                        lv_mysearch.setAdapter(swipeRefreshXUser);

                    }
                });
            }
            else
            {
                List<UserDetail> result = new ArrayList<UserDetail>();
                swipeRefreshXUser = new SwipeRefreshXUser(SearchFriendsActivity.this, result);
                lv_mysearch.setAdapter(swipeRefreshXUser);
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

