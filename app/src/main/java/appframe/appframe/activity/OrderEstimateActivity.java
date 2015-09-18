package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderEstimateAdapater;

/**
 * Created by Administrator on 2015/9/8.
 */
public class OrderEstimateActivity extends BaseActivity {
    SwipeRefreshX swipeRefresh;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderestimate);
        init();
    }

    public void init()
    {


        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_orderestimate);

        Http.request(this, API.GET_ORDEREVALUATION,new Object[]{"11"}, new Http.RequestListener<List<OrderReviewDetail>>() {
            @Override
            public void onSuccess(List<OrderReviewDetail> result) {
                super.onSuccess(result);

                listView.setAdapter(new SwipeRefreshXOrderEstimateAdapater(OrderEstimateActivity.this, result));
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        //Toast.makeText(getActivity(), "df", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
//                    }
//                });

            }
        });


        //listView.setAdapter(new SwipeRefreshXOrderEstimateAdapater(this));

//        Http.request(this, API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
//            @Override
//            public void onSuccess(List<OrderDetails> result) {
//                super.onSuccess(result);
//
//                listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result));
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        //Toast.makeText(getActivity(), "df", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
//                    }
//                });
//
//            }
//        });

        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Toast.makeText(OrderEstimateActivity.this, "refresh", Toast.LENGTH_SHORT).show();

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                Toast.makeText(OrderEstimateActivity.this, "load", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
