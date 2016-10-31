package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AnswerDetailWithQuestionDetail;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyAnswerAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyMissionAdapater;

/**
 * Created by Administrator on 2016/5/23.
 */
public class MyAnswerActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_empty;
    LinearLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myanswer);
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

        tb_back.setText("我的");
        tb_title.setText("我回答的");
        tb_back.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_myanswer);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MyAnswerActivity.this, QuestionDetailsActivity.class);

                AnswerDetailWithQuestionDetail answerDetailWithQuestionDetail = (AnswerDetailWithQuestionDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MyAnswer", answerDetailWithQuestionDetail.getQuestion());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });



    }



    public void initdata()
    {
        Http.request(MyAnswerActivity.this, API.GET_MYANSWER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<AnswerDetailWithQuestionDetail>>() {
            @Override
            public void onSuccess(List<AnswerDetailWithQuestionDetail> result) {
                super.onSuccess(result);
                progress_bar.setVisibility(View.GONE);
                listView.setAdapter(new SwipeRefreshXMyAnswerAdapater(MyAnswerActivity.this, result));
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
                progress_bar.setVisibility(View.GONE);
            }
        });


        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Http.request(MyAnswerActivity.this, API.GET_MYANSWER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<AnswerDetailWithQuestionDetail>>() {
                    @Override
                    public void onSuccess(List<AnswerDetailWithQuestionDetail> result) {
                        super.onSuccess(result);
                        swipeRefresh.setRefreshing(false);
                        listView.setAdapter(new SwipeRefreshXMyAnswerAdapater(MyAnswerActivity.this, result));
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
                        swipeRefresh.setRefreshing(false);
                    }
                });


            }
        });
        // 加载监听器
//        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {
//
//            @Override
//            public void onLoad() {
//
////                Page++;
////                Map<String, String> map = new HashMap<String, String>();
////                map.put("Page", String.valueOf(Page));
////                map.put("Limit", String.valueOf(AppConfig.ORDER_SIZE));
////                map.put("Latitude", latitude);
////                map.put("Longitude", longitude);
////                map.put("UserId", String.valueOf(Auth.getCurrentUserId()));
////
////                Http.request(NearByActivity.this, API.GET_USERNEARBY, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<Nearby>>() {
////                    @Override
////                    public void onSuccess(List<Nearby> result) {
////                        super.onSuccess(result);
////                        if (result != null) {
////
////                            adapater.addItems(result);
////                        }
////                        //listView.setAdapter(adapater);
////
////                    }
////                });
//                swipeRefresh.setLoading(false);
//
//            }
//        });
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
        MobclickAgent.onPageStart("我回答的页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我回答的页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}



