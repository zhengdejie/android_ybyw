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

import com.umeng.analytics.MobclickAgent;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.SearchResult;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXQuestionAdapater;

/**
 * Created by Administrator on 2016-06-29.
 */
public class QuestionMoreActivity extends BaseActivity implements View.OnClickListener{
    SwipeRefreshX swipeRefresh;
    ListView listView;
    private TextView tv_empty,tb_back;
    private EditText et_search;
    int page = 1;
    SwipeRefreshXQuestionAdapater swipeRefreshXQuestionAdapater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionmore);
        init();
    }
    private  void init()
    {
        et_search = (EditText)findViewById(R.id.et_search);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("返回");
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_mymission);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(QuestionMoreActivity.this, OrderDetailsActivity.class);

                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
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
                map.put("Type", "2");
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(QuestionMoreActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchResult>() {
                    @Override
                    public void onSuccess(final SearchResult result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        if (result.getQuestions() != null) {

                            tv_empty.setVisibility(View.GONE);
                            swipeRefreshXQuestionAdapater = new SwipeRefreshXQuestionAdapater(QuestionMoreActivity.this, result.getQuestions());
                            listView.setAdapter(swipeRefreshXQuestionAdapater);

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
                map.put("Type", "2");
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(QuestionMoreActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchResult>() {
                    @Override
                    public void onSuccess(final SearchResult result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setLoading(false);
                        if (result.getQuestions() != null) {

                            loadMore(swipeRefreshXQuestionAdapater, result.getQuestions());

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

    private void loadMore(SwipeRefreshXQuestionAdapater adapater, List<Question> orderDetailses) {
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
                map.put("Type", "2");
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(QuestionMoreActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchResult>() {
                    @Override
                    public void onSuccess(final SearchResult result) {
                        super.onSuccess(result);
                        if (result.getQuestions() != null) {
                            tv_empty.setVisibility(View.GONE);
                            swipeRefreshXQuestionAdapater = new SwipeRefreshXQuestionAdapater(QuestionMoreActivity.this, result.getQuestions());
                            listView.setAdapter(swipeRefreshXQuestionAdapater);

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
                List<Question> result = new ArrayList<Question>();
                swipeRefreshXQuestionAdapater = new SwipeRefreshXQuestionAdapater(QuestionMoreActivity.this, result);
                listView.setAdapter(swipeRefreshXQuestionAdapater);
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

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("查看更多问题页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("查看更多问题页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
