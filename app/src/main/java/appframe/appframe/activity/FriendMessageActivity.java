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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.AnswerDetailWithQuestionDetail;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendMessageAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXSystemMessageAdapater;

/**
 * Created by Administrator on 2015/11/17.
 */
public class FriendMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back,tv_empty;
    ListView lv_friendmessage;
    LinearLayout progress_bar;
    int page = 1;
    public SwipeRefreshX swipeRefresh;
    SwipeRefreshXFriendMessageAdapater swipeRefreshXFriendMessageAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendmessage);
        init();
        initdata();
    }
    private  void init()
    {
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        lv_friendmessage = (ListView)findViewById(R.id.lv_friendmessage);
        tb_back.setText("我的消息");
        tb_title.setText("好友通知");
        tb_back.setOnClickListener(this);

        lv_friendmessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(FriendMessageActivity.this, FriendsInfoActivity.class);

                PushMessage pushMessage = (PushMessage) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PushMessage", pushMessage);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        map.put("Type", "2");
        Http.request(FriendMessageActivity.this, API.GET_PUSHMESSAGE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<PushMessage>>() {
            @Override
            public void onSuccess(List<PushMessage> result) {
                super.onSuccess(result);
                progress_bar.setVisibility(View.GONE);
                swipeRefreshXFriendMessageAdapater = new SwipeRefreshXFriendMessageAdapater(FriendMessageActivity.this, result);
                lv_friendmessage.setAdapter(swipeRefreshXFriendMessageAdapater);

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

        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", "2");
                Http.request(FriendMessageActivity.this, API.GET_PUSHMESSAGE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<PushMessage>>() {
                    @Override
                    public void onSuccess(List<PushMessage> result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        swipeRefreshXFriendMessageAdapater = new SwipeRefreshXFriendMessageAdapater(FriendMessageActivity.this, result);
                        lv_friendmessage.setAdapter(swipeRefreshXFriendMessageAdapater);
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
                map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", "2");
                Http.request(FriendMessageActivity.this, API.GET_PUSHMESSAGE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<PushMessage>>() {
                    @Override
                    public void onSuccess(List<PushMessage> result) {
                        super.onSuccess(result);
                        if (result != null) {

                            loadMore(swipeRefreshXFriendMessageAdapater, result);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;

        }

    }

    private void loadMore(SwipeRefreshXFriendMessageAdapater adapater, List<PushMessage> orderDetailses) {
        adapater.addItems(orderDetailses);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("好友通知页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("好友通知页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
