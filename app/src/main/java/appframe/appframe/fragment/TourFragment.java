package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.TourOrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.dto.MRussianTour;
import appframe.appframe.dto.RussianTour;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXRussianTravelAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXTourAdapater;

/**
 * Created by Administrator on 2017/5/18.
 */

public class TourFragment  extends BaseFragment implements View.OnClickListener {
    ListView proListView;
    TextView tv_empty,tab_apply,tab_done,tab_fail;
    View bottomLine_apply,bottomLine_done,bottomLine_fail;
    View root;
    LinearLayout progress_bar;
    SwipeRefreshX swipeRefresh;
    SwipeRefreshXTourAdapater swipeRefreshXTourAdapater;
    Map<String, String> map = new HashMap<String, String>();
    int Type = 1;



    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tour, null);
        initViews();
        initDatas();
        return root;
    }



    protected  void initDatas()
    {
        tab_apply.performClick();

    }

    protected  void initViews()
    {
        tab_apply = (TextView) root.findViewById(R.id.tab_apply);
        tab_done = (TextView) root.findViewById(R.id.tab_done);
        tab_fail = (TextView) root.findViewById(R.id.tab_fail);
        tv_empty = (TextView) root.findViewById(R.id.tv_empty);
        bottomLine_apply = (View) root.findViewById(R.id.bottomLine_apply);
        bottomLine_done = (View) root.findViewById(R.id.bottomLine_done);
        bottomLine_fail = (View) root.findViewById(R.id.bottomLine_fail);
        proListView = (ListView) root.findViewById(R.id.proListView);
        swipeRefresh = (SwipeRefreshX)root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        progress_bar = (LinearLayout)root.findViewById(R.id.progress_bar);
        tab_apply.setOnClickListener(this);
        tab_done.setOnClickListener(this);
        tab_fail.setOnClickListener(this);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Type == 1)
                {
                    map.put("Type", "100,201,202,300");

                    Http.request(getActivity(), API.GETALLGUIDEORDERFORUSER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTourOrder>>() {
                        @Override
                        public void onSuccess(List<GuideTourOrder> result) {
                            super.onSuccess(result);
                            swipeRefresh.setRefreshing(false);
                            swipeRefreshXTourAdapater = new SwipeRefreshXTourAdapater(getActivity(), result,"User");
                            proListView.setAdapter(swipeRefreshXTourAdapater);
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
                else if(Type == 2)
                {
                    map.put("Type", "400");

                    Http.request(getActivity(), API.GETALLGUIDEORDERFORUSER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTourOrder>>() {
                        @Override
                        public void onSuccess(List<GuideTourOrder> result) {
                            super.onSuccess(result);
                            swipeRefresh.setRefreshing(false);
                            swipeRefreshXTourAdapater = new SwipeRefreshXTourAdapater(getActivity(), result,"User");
                            proListView.setAdapter(swipeRefreshXTourAdapater);
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
                else if(Type == 3)
                {
                    map.put("Type", "401,500,600");

                    Http.request(getActivity(), API.GETALLGUIDEORDERFORUSER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTourOrder>>() {
                        @Override
                        public void onSuccess(List<GuideTourOrder> result) {
                            super.onSuccess(result);
                            swipeRefresh.setRefreshing(false);
                            swipeRefreshXTourAdapater = new SwipeRefreshXTourAdapater(getActivity(), result,"User");
                            proListView.setAdapter(swipeRefreshXTourAdapater);
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
                else
                {

                }

            }
        });

        proListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(getActivity(), TourOrderDetailsActivity.class);
                GuideTourOrder guideTourOrder = (GuideTourOrder) parent.getAdapter().getItem(position);
                bundle.putSerializable("GuideTourOrderFromUser", guideTourOrder);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
//        List<RussianTour> russianTours = new ArrayList<RussianTour>();
//        RussianTour rt = new RussianTour();
//        rt.setApplicationDeposit("1000");
//        russianTours.add(rt);
//        swipeRefreshXTourAdapater = new SwipeRefreshXTourAdapater(getActivity(), russianTours);
//        proListView.setAdapter(swipeRefreshXTourAdapater);
//        tab_apply.performClick();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                getActivity().finish();
                break;

            case R.id.tab_apply:
                Type = 1;
                setTabApply(true);
                setTabDone(false);
                setTabFail(false);

                progress_bar.setVisibility(View.VISIBLE);

                map.put("Type", "100,201,202,300");

                Http.request(getActivity(), API.GETALLGUIDEORDERFORUSER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTourOrder>>() {
                    @Override
                    public void onSuccess(List<GuideTourOrder> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXTourAdapater = new SwipeRefreshXTourAdapater(getActivity(), result,"User");
                        proListView.setAdapter(swipeRefreshXTourAdapater);
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
                break;
            case R.id.tab_done:
                Type = 2;
                setTabApply(false);
                setTabDone(true);
                setTabFail(false);

                progress_bar.setVisibility(View.VISIBLE);

                map.put("Type", "400");

                Http.request(getActivity(), API.GETALLGUIDEORDERFORUSER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTourOrder>>() {
                    @Override
                    public void onSuccess(List<GuideTourOrder> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXTourAdapater = new SwipeRefreshXTourAdapater(getActivity(), result,"User");
                        proListView.setAdapter(swipeRefreshXTourAdapater);
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
                break;
            case R.id.tab_fail:
                Type = 3;
                setTabApply(false);
                setTabDone(false);
                setTabFail(true);

                progress_bar.setVisibility(View.VISIBLE);

                map.put("Type", "401,500,600");

                Http.request(getActivity(), API.GETALLGUIDEORDERFORUSER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTourOrder>>() {
                    @Override
                    public void onSuccess(List<GuideTourOrder> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXTourAdapater = new SwipeRefreshXTourAdapater(getActivity(), result,"User");
                        proListView.setAdapter(swipeRefreshXTourAdapater);
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
                break;


        }
    }
    protected void setTabApply(boolean isSelected)
    {
        if(isSelected)
        {
            tab_apply.setTextColor(getResources().getColor(R.color.blue));
            bottomLine_apply.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_apply.setTextColor(getResources().getColor(R.color.font_999));
            bottomLine_apply.setVisibility(View.INVISIBLE);

        }
    }

    protected void setTabFail(boolean isSelected)
    {
        if(isSelected)
        {
            tab_fail.setTextColor(getResources().getColor(R.color.blue));
            bottomLine_fail.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_fail.setTextColor(getResources().getColor(R.color.font_999));
            bottomLine_fail.setVisibility(View.INVISIBLE);

        }
    }
    protected void setTabDone(boolean isSelected)
    {
        if(isSelected)
        {
            tab_done.setTextColor(getResources().getColor(R.color.blue));
            bottomLine_done.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_done.setTextColor(getResources().getColor(R.color.font_999));
            bottomLine_done.setVisibility(View.INVISIBLE);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("乐游订单页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("乐游订单页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}
