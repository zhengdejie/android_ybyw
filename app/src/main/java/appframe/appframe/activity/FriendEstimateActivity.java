package appframe.appframe.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import appframe.appframe.R;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendEstimateAdapater;

/**
 * Created by Administrator on 2015/9/8.
 */
public class FriendEstimateActivity extends BaseActivity {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendestimate);
        init();
    }

    public void init()
    {
        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_friendestimate);
        listView.setAdapter(new SwipeRefreshXFriendEstimateAdapater(this));

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

                Toast.makeText(FriendEstimateActivity.this, "refresh", Toast.LENGTH_SHORT).show();

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                Toast.makeText(FriendEstimateActivity.this, "load", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
