package appframe.appframe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.OpenOrderActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.MRussianTour;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.RussianTour;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.hoverscrollview.HoverScrollView;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXConfirmedOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXRussianTravelAdapater;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;

import static appframe.appframe.R.id.default_activity_button;
import static appframe.appframe.R.id.tabHost;

/**
 * Created by Administrator on 2017/5/18.
 */

public class RussianTravelFragment extends BaseFragment implements View.OnClickListener {
    ListView proListView;
    TextView tv_empty,tab_apply,tab_done;
    View bottomLine_apply,bottomLine_done;
    View root;
    LinearLayout progress_bar;
//    SwipeRefreshX swipeRefresh;
    SwipeRefreshXRussianTravelAdapater swipeRefreshXRussianTravelAdapater;
    String Status = "1";
    Map<String, String> mapHttp = new HashMap<String, String>();




    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_russiantravel, null);
        initViews();
        initDatas();
        return root;
    }



    protected  void initDatas()
    {
        tab_apply.performClick();
//        progress_bar.setVisibility(View.VISIBLE);
//        Map<String, String> map = new HashMap<String, String>();
//
//        if(Status.equals("1"))
//        {
//            map.put("Type", "101");
//        }
//        else if(Status.equals("2"))
//        {
//            map.put("Category", "200");
//        }
//
//        Http.request(getActivity(), API.MYRUSSIANTOUR, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<MRussianTour>>() {
//            @Override
//            public void onSuccess(List<MRussianTour> result) {
//                super.onSuccess(result);
//                progress_bar.setVisibility(View.GONE);
//                swipeRefreshXRussianTravelAdapater = new SwipeRefreshXRussianTravelAdapater(getActivity(), result);
//                proListView.setAdapter(swipeRefreshXRussianTravelAdapater);
//                if (result != null && result.size() != 0) {
//                    tv_empty.setVisibility(View.GONE);
//                } else {
//                    tv_empty.setVisibility(View.VISIBLE);
//                }
//
//            }
//
//            @Override
//            public void onFail(String code) {
//                super.onFail(code);
//                progress_bar.setVisibility(View.GONE);
//            }
//        });
    }

    protected  void initViews()
    {
        tab_apply = (TextView) root.findViewById(R.id.tab_apply);
        tab_done = (TextView) root.findViewById(R.id.tab_done);
//        tab_fail = (TextView) root.findViewById(R.id.tab_fail);
        bottomLine_apply = (View) root.findViewById(R.id.bottomLine_apply);
        bottomLine_done = (View) root.findViewById(R.id.bottomLine_done);
//        bottomLine_fail = (View) root.findViewById(R.id.bottomLine_fail);
        proListView = (ListView) root.findViewById(R.id.proListView);
//        swipeRefresh = (SwipeRefreshX)root.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        progress_bar = (LinearLayout)root.findViewById(R.id.progress_bar);
        tv_empty = (TextView) root.findViewById(R.id.tv_empty);
        tab_apply.setOnClickListener(this);
        tab_done.setOnClickListener(this);
//        tab_fail.setOnClickListener(this);



        tab_apply.performClick();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                getActivity().finish();
                break;

            case R.id.tab_apply:
                progress_bar.setVisibility(View.VISIBLE);
                setTabApply(true);
                setTabDone(false);

                mapHttp.put("Type", "101,103,104");

                Http.request(getActivity(), API.MYRUSSIANTOUR, new Object[]{Http.getURL(mapHttp)}, new Http.RequestListener<List<MRussianTour>>() {
                    @Override
                    public void onSuccess(List<MRussianTour> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXRussianTravelAdapater = new SwipeRefreshXRussianTravelAdapater(getActivity(), result);
                        proListView.setAdapter(swipeRefreshXRussianTravelAdapater);
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
//                setTabFail(false);
                break;
            case R.id.tab_done:
                progress_bar.setVisibility(View.VISIBLE);
                setTabApply(false);
                setTabDone(true);

                mapHttp.put("Type", "102,200,300");

                Http.request(getActivity(), API.MYRUSSIANTOUR, new Object[]{Http.getURL(mapHttp)}, new Http.RequestListener<List<MRussianTour>>() {
                    @Override
                    public void onSuccess(List<MRussianTour> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXRussianTravelAdapater = new SwipeRefreshXRussianTravelAdapater(getActivity(), result);
                        proListView.setAdapter(swipeRefreshXRussianTravelAdapater);
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
//                setTabFail(false);
                break;
//            case R.id.tab_fail:
//                setTabApply(false);
//                setTabDone(false);
//                setTabFail(true);
//                break;
            default:
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

//    protected void setTabFail(boolean isSelected)
//    {
//        if(isSelected)
//        {
//            tab_fail.setTextColor(getResources().getColor(R.color.blue));
//            bottomLine_fail.setVisibility(View.VISIBLE);
//
//        }
//        else
//        {
//            tab_fail.setTextColor(getResources().getColor(R.color.font_999));
//            bottomLine_fail.setVisibility(View.INVISIBLE);
//
//        }
//    }
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
        tab_apply.performClick();
        MobclickAgent.onPageStart("俄罗斯伴游订单页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("俄罗斯伴游订单页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}
