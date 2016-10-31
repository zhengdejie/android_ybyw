package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.CommentDetailResponseDto;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderComment;

/**
 * Created by Administrator on 2016-06-28.
 */
public class OrderCommentAllActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_empty;
    LinearLayout progress_bar;
    String latitude,longitude;
//    BDLocation bdLocation = new BDLocation();
    SwipeRefreshXOrderComment swipeRefreshXOrderComment;
    int page = 1;
    OrderDetails orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercommentall);
        init();
    }

    public void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);

        tb_back.setText("返回");
        tb_title.setText("留言");
        tb_back.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_nearby);
        Intent intent = this.getIntent();
        orderDetails=(OrderDetails)intent.getSerializableExtra("OrderDetails");
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Nearby nearby = (Nearby)parent.getAdapter().getItem(position);
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                intent.setClass(NearByActivity.this, FriendsInfoActivity.class);
//                bundle.putSerializable("NearBy", nearby);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });


        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        Http.request(OrderCommentAllActivity.this, API.ORDER_GETCOOMENT, new Object[]{orderDetails.getId(),Http.getURL(map)},
                new Http.RequestListener<CommentDetailResponseDto>() {
                    @Override
                    public void onSuccess(CommentDetailResponseDto result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        if(result != null) {
                            tv_empty.setVisibility(View.GONE);
                            if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
                                swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderCommentAllActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), true);
                                listView.setAdapter(swipeRefreshXOrderComment);
                            } else {
                                swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderCommentAllActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), false);
                                listView.setAdapter(swipeRefreshXOrderComment);
                            }
//                            setListViewHeightBasedOnChildren(lv_ordercomment);
//                            tv_comment.setText(String.format("留言%d条,点击查看全部", result.getTotalCount()));
                        }
                        else
                        {
//                            tv_comment.setText("留言0条,点击查看全部");
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
                Http.request(OrderCommentAllActivity.this, API.ORDER_GETCOOMENT, new Object[]{orderDetails.getId(),Http.getURL(map)},
                        new Http.RequestListener<CommentDetailResponseDto>() {
                            @Override
                            public void onSuccess(CommentDetailResponseDto result) {
                                super.onSuccess(result);
                                page = 1;
                                swipeRefresh.setRefreshing(false);
                                if(result!=null) {
                                    tv_empty.setVisibility(View.GONE);
                                    if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderCommentAllActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), true);
                                        listView.setAdapter(swipeRefreshXOrderComment);
                                    } else {
                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderCommentAllActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), false);
                                        listView.setAdapter(swipeRefreshXOrderComment);
                                    }
//                                    setListViewHeightBasedOnChildren(lv_ordercomment);
//                                    tv_comment.setText(String.format("留言%d条,点击查看全部", result.getTotalCount()));
                                }
                                else
                                {
//                                    tv_comment.setText("留言0条,点击查看全部");
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
                Http.request(OrderCommentAllActivity.this, API.ORDER_GETCOOMENT, new Object[]{orderDetails.getId(), Http.getURL(map)},
                        new Http.RequestListener<CommentDetailResponseDto>() {
                            @Override
                            public void onSuccess(CommentDetailResponseDto result) {
                                super.onSuccess(result);

                                swipeRefresh.setLoading(false);
                                if (result != null) {

                                    loadMore(swipeRefreshXOrderComment, result.getList());
//                                    if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
//                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), true);
//                                        lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
//                                    } else {
//                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), false);
//                                        lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
//                                    }
//                                    setListViewHeightBasedOnChildren(lv_ordercomment);
//                                    tv_comment.setText(String.format("留言%d条,点击查看全部", result.getTotalCount()));
                                }

//                                else
//                                {
//                                    tv_comment.setText("留言0条,点击查看全部");
//                                }
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

    private void loadMore(SwipeRefreshXOrderComment adapater, List<OrderComment> orderDetailses) {
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
        MobclickAgent.onPageStart("查看所有留言页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("查看所有留言页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}

