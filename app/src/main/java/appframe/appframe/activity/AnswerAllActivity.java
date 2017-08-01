package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.AnswerDetail;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.QuestionWithAnswers;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXAnswerAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;

/**
 * Created by Administrator on 2016-06-28.
 */
public class AnswerAllActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_empty;
    LinearLayout progress_bar;
    Question question;
    SwipeRefreshXAnswerAdapater swipeRefreshXAnswerAdapater;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerall);
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

        tb_back.setText("问答");
        tb_title.setText("答案");
        tb_back.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_nearby);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(AnswerAllActivity.this, AnswerDetailsActivity.class);
                AnswerDetail answerDetails = (AnswerDetail) parent.getAdapter().getItem(position);
                bundle.putSerializable("AnswerDetail", answerDetails);
                bundle.putSerializable("Question", question);
//                bundle.putBoolean("hasAccept",hasAccept);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Intent getintent = this.getIntent();
        if(getintent.getSerializableExtra("Question") != null)
        {
            question = (Question) getintent.getSerializableExtra("Question");

        }


    }



    public void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));

        Http.request(AnswerAllActivity.this, API.GET_ANSWERS, new Object[]{question.getId(), Http.getURL(map)},
                new Http.RequestListener<QuestionWithAnswers>() {
                    @Override
                    public void onSuccess(QuestionWithAnswers result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            tv_empty.setVisibility(View.GONE);
                            swipeRefreshXAnswerAdapater = new SwipeRefreshXAnswerAdapater(AnswerAllActivity.this, result.getAnswerDetails());
                            listView.setAdapter(swipeRefreshXAnswerAdapater);


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



        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));

                Http.request(AnswerAllActivity.this, API.GET_ANSWERS, new Object[]{question.getId(), Http.getURL(map)},
                        new Http.RequestListener<QuestionWithAnswers>() {
                            @Override
                            public void onSuccess(QuestionWithAnswers result) {
                                super.onSuccess(result);
                                page = 1;
                                swipeRefresh.setRefreshing(false);
                                if (result != null) {
                                    tv_empty.setVisibility(View.GONE);
                                    swipeRefreshXAnswerAdapater = new SwipeRefreshXAnswerAdapater(AnswerAllActivity.this, result.getAnswerDetails());
                                    listView.setAdapter(swipeRefreshXAnswerAdapater);


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

                Http.request(AnswerAllActivity.this, API.GET_ANSWERS, new Object[]{question.getId(), Http.getURL(map)},
                        new Http.RequestListener<QuestionWithAnswers>() {
                            @Override
                            public void onSuccess(QuestionWithAnswers result) {
                                super.onSuccess(result);

                                swipeRefresh.setLoading(false);
                                if (result != null) {
                                    loadMore(swipeRefreshXAnswerAdapater, result.getAnswerDetails());

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

    private void loadMore(SwipeRefreshXAnswerAdapater adapater, List<AnswerDetail> orderDetailses) {
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
        MobclickAgent.onPageStart("查看所有答案页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("查看所有答案页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}


