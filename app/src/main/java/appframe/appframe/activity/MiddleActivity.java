package appframe.appframe.activity;

import android.os.Bundle;
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
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXExpandFriendsAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMiddleManAdapater;

import static appframe.appframe.R.id.tv_send;

/**
 * Created by Administrator on 2016-12-29.
 */

public class MiddleActivity extends BaseActivity implements View.OnClickListener {
    //    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_empty;
    LinearLayout progress_bar;
    //    String latitude,longitude;
//    BDLocation bdLocation = new BDLocation();
    SwipeRefreshXMiddleManAdapater adapater;
    int Page = 1;
    UserDetail userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middleman);
        userDetail =  (UserDetail)this.getIntent().getSerializableExtra("UserDetail");
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

        tb_back.setText("扩展人脉");
        if(userDetail != null) {

            tb_title.setText(String.format("与%s的中间好友",userDetail.getName()));
        }
        else
        {
            tb_title.setText("中间好友");
        }
        tb_back.setOnClickListener(this);
//        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);
//
//        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_middleman);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Nearby nearby = (Nearby)parent.getAdapter().getItem(position);
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                intent.setClass(ExpandFriendsActivity.this, FriendsInfoActivity.class);
//                bundle.putSerializable("NearBy", nearby);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

//        BaiduLocation baiduLocation = new BaiduLocation(getApplicationContext(), new MyLocationListener());
//        baiduLocation.setOption();
//        baiduLocation.mLocationClient.start();


    }



    public void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("FriendId", String.valueOf(userDetail.getId()));
        Http.request(MiddleActivity.this, API.GET_MIDDLEMAN, new Object[]{Auth.getCurrentUserId(),Http.getURL(map)}, new Http.RequestListener<List<UserDetail>>() {
            @Override
            public void onSuccess(final List<UserDetail> result) {
                super.onSuccess(result);
                adapater = new SwipeRefreshXMiddleManAdapater(MiddleActivity.this, result);
                listView.setAdapter(adapater);
                if(result != null && result.size() != 0) {
                    tv_empty.setVisibility(View.GONE);
                }
                else {
                    tv_empty.setVisibility(View.VISIBLE);
                }
                progress_bar.setVisibility(View.GONE);

            }

            @Override
            public void onFail(String code) {
                super.onFail(code);
                progress_bar.setVisibility(View.GONE);
            }
        });



//        // 设置下拉刷新监听器
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//
//                Http.request(ExpandFriendsActivity.this, API.GET_SECOND, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<UserDetail>>() {
//                    @Override
//                    public void onSuccess(final List<UserDetail> result) {
//                        super.onSuccess(result);
//                        adapater = new SwipeRefreshXExpandFriendsAdapater(ExpandFriendsActivity.this, result);
//                        listView.setAdapter(adapater);
//                        if(result != null && result.size() != 0) {
//                            tv_empty.setVisibility(View.GONE);
//                        }
//                        else {
//                            tv_empty.setVisibility(View.VISIBLE);
//                        }
//
//
//                    }
//                });
//                progress_bar.setVisibility(View.GONE);
//                swipeRefresh.setRefreshing(false);
//
//            }
//        });
//        // 加载监听器
//        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {
//
//            @Override
//            public void onLoad() {
//
//                Http.request(ExpandFriendsActivity.this, API.GET_SECOND, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<UserDetail>>() {
//                    @Override
//                    public void onSuccess(final List<UserDetail> result) {
//                        super.onSuccess(result);
//
//                        adapater = new SwipeRefreshXExpandFriendsAdapater(ExpandFriendsActivity.this, result);
//                        listView.setAdapter(adapater);
//
//
//                    }
//                });
//                progress_bar.setVisibility(View.GONE);
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
        MobclickAgent.onPageStart("中间好友页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("中间好友页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}


