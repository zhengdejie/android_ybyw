package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.FriendEstimateActivity;
import appframe.appframe.activity.FriendShopsActivity;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.OrderEstimateActivity;
import appframe.appframe.activity.SelfEstimateActivity;
import appframe.appframe.dto.Nearby;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendEstimateAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;

/**
 * Created by Administrator on 2015/10/22.
 */
public class DiscoveryFragment extends BaseFragment implements View.OnClickListener {
    View root;
    TextView tb_title,tb_back,tv_topfs;
    SwipeRefreshX swipeRefresh;
    ListView listView;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root  = inflater.inflate(R.layout.fragment_discovery, null);
        init();
        return root;
    }

    @Override
    protected void onLoadData() {

    }

    public void init()
    {
        tv_topfs = (TextView) root.findViewById(R.id.tv_topfs);
        tv_topfs.setOnClickListener(this);

        tb_title = (TextView)root.findViewById(R.id.tb_title);
        tb_back = (TextView)root.findViewById(R.id.tb_back);
        tb_title.setText("发现");
        tb_back.setVisibility(View.GONE);

        swipeRefresh = (SwipeRefreshX)root.findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)root.findViewById(R.id.lv_nearby);
        List<Nearby> list = new ArrayList<Nearby>();
        listView.setAdapter(new SwipeRefreshXNearbyAdapater(getActivity(),list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), FriendsInfoActivity.class));
            }
        });


        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                Toast.makeText(getActivity(), "load", Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_topfs:
                startActivity(new Intent(getActivity(),FriendShopsActivity.class));
                break;


        }
    }
}
