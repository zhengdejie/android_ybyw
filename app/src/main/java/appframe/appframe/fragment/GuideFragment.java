package appframe.appframe.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.GuideOrderActivity;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.dto.Recommend;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXGuideAdapater;

import static appframe.appframe.R.id.tv_focus;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends BaseFragment implements View.OnClickListener {
    View root;
    TextView tb_back,tv_travel,tv_recommand;
    View v_recommand,v_travel;
    SwipeRefreshX swipeRefresh;
    ListView listView;
    int page = 1;
    SwipeRefreshXGuideAdapater swipeRefreshXGuideAdapater;


    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root  = inflater.inflate(R.layout.fragment_guide, null);
        init();
        return root;
    }

    @Override
    protected void onLoadData() {

    }

    public void init() {

        tv_travel = (TextView) root.findViewById(R.id.tv_travel);
        tv_recommand = (TextView) root.findViewById(R.id.tv_recommand);
        tb_back = (TextView) root.findViewById(R.id.tb_back);
        v_recommand = (View) root.findViewById(R.id.v_recommand);
        v_travel = (View) root.findViewById(R.id.v_travel);


        tb_back.setOnClickListener(this);
        tv_travel.setOnClickListener(this);
        tv_recommand.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX) root.findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView) root.findViewById(R.id.lv_recommendations);

        Http.request(getActivity(), API.GET_ALLRECOMMENDATION,
                new Http.RequestListener<List<Recommend>>() {
                    @Override
                    public void onSuccess(List<Recommend> result) {
                        super.onSuccess(result);
                        swipeRefreshXGuideAdapater = new SwipeRefreshXGuideAdapater(getActivity(), result);
                        listView.setAdapter(swipeRefreshXGuideAdapater);
                    }
                });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recommend recommend = (Recommend) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(getActivity(), GuideOrderActivity.class);
                bundle.putSerializable("Recommend", recommend);
                intent.putExtras(bundle);
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), GuideOrderActivity.class);
                startActivity(intent);
            }
        });
    }


//        // 设置下拉刷新监听器
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Page", "1");
//                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
//                map.put("UserId", String.valueOf(Auth.getCurrentUserId()));
//
//                Http.request(getActivity(), API.GET_FRIENDTRACE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetailWithFriend>>() {
//                    @Override
//                    public void onSuccess(List<ConfirmedOrderDetailWithFriend> result) {
//                        super.onSuccess(result);
//                        page = 1;
//                        swipeRefresh.setRefreshing(false);
//                        listView.setAdapter(new SwipeRefreshXFriendShopsAdapater(getActivity(), result));
//                        if (result != null && result.size() != 0) {
//                            tv_empty.setVisibility(View.GONE);
//                        } else {
//                            tv_empty.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(String code) {
//                        super.onFail(code);
//                        swipeRefresh.setRefreshing(false);
//                    }
//                });
//
//            }
//        });
//        // 加载监听器
//        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {
//
//            @Override
//            public void onLoad() {
//                page++;
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Page", String.valueOf(page));
//                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
//                map.put("UserId", String.valueOf(Auth.getCurrentUserId()));
//
//                Http.request(getActivity(), API.GET_FRIENDTRACE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetailWithFriend>>() {
//                    @Override
//                    public void onSuccess(List<ConfirmedOrderDetailWithFriend> result) {
//                        super.onSuccess(result);
//                        swipeRefresh.setLoading(false);
//                        if (result != null) {
//
//                            loadMore(swipeRefreshXFriendShopsAdapater, result);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(String code) {
//                        super.onFail(code);
//                        swipeRefresh.setLoading(false);
//                    }
//                });
//
//
//            }
//        });
//
//    }
//
//    private void loadMore(SwipeRefreshXFriendShopsAdapater adapater, List<ConfirmedOrderDetailWithFriend> orderDetailses) {
//        adapater.addItems(orderDetailses);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                getActivity().finish();
                break;
            case R.id.tv_recommand:
                v_recommand.setVisibility(View.VISIBLE);
                v_travel.setVisibility(View.GONE);
                break;
            case R.id.tv_travel:
                v_recommand.setVisibility(View.GONE);
                v_travel.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("引导消费页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("引导消费页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}

