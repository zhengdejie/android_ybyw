package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.NearByActivity;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendShopsAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;

/**
 * Created by Administrator on 2015/10/22.
 */
public class DiscoveryFragment extends BaseFragment implements View.OnClickListener {
    View root;
    TextView tb_title,tb_back,tv_nearby;
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
        tv_nearby = (TextView) root.findViewById(R.id.tv_nearby);
        tv_nearby.setOnClickListener(this);

        tb_title = (TextView)root.findViewById(R.id.tb_title);
        tb_back = (TextView)root.findViewById(R.id.tb_back);
        tb_title.setText("发现");
        tb_back.setVisibility(View.GONE);

        swipeRefresh = (SwipeRefreshX)root.findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)root.findViewById(R.id.lv_topfs);

        Map<String, String> map = new HashMap<String, String>();
        map.put("UserId", String.valueOf(Auth.getCurrentUserId()));

        Http.request(getActivity(), API.GET_FRIENDTRACE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetailWithFriend>>() {
            @Override
            public void onSuccess(List<ConfirmedOrderDetailWithFriend> result) {
                super.onSuccess(result);

                listView.setAdapter(new SwipeRefreshXFriendShopsAdapater(getActivity(), result));

            }

            @Override
            public void onFail(String code) {
                super.onFail(code);

            }
        });

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
            case R.id.tv_nearby:
                startActivity(new Intent(getActivity(),NearByActivity.class));
                break;


        }
    }
}
