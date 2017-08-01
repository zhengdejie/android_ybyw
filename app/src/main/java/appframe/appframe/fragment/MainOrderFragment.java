package appframe.appframe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import appframe.appframe.activity.ConfirmOrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.ConfirmedOrderUndoCount;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXConfirmedOrderAdapater;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Administrator on 2017/5/18.
 */

public class MainOrderFragment extends BaseFragment implements View.OnClickListener {
    ListView proListView;
    TextView tb_title,tb_back,tv_apply,tv_progess,tv_done,tv_empty;
    public static TextView tab_progess,tab_done,tab_close,tab_apply;
    View root,bottomLine_progress,bottomLine_done,bottomLine_close,bottomLine_apply;
    LinearLayout tabtop;
    LinearLayout progress_bar;
    SwipeRefreshX swipeRefresh;
    SwipeRefreshXConfirmedOrderAdapater swipeRefreshXConfirmedOrderAdapater;
    String from,Status ="3";
    int page = 1;
    Map<String, String> map = new HashMap<String, String>();

//    private List<String> titleList;//viewpager的标题
    BaseFragment[] fragments;
//    boolean require_selected =true;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_mainorder, null);
        init();
        initViewPager();
        return root;
    }



    private void initViewPager()
    {

        // init view pager
//        titleList = new ArrayList<String>();// 每个页面的Title数据
//        titleList.add("友帮");
//        titleList.add("乐游");
//        titleList.add("俄罗斯心动之旅");

//        fragments = new BaseFragment[]{
//                new MainOrderFragment(),
//                new TourFragment(),
//                new RussianTravelFragment()
//
//
//        };



    }

//    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
//
//        public ViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//
//        }
//
//        public Fragment getItem(int num) {
//            return fragments[num];
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.length;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titleList.get(position);
//        }
//
//    }

    public void init()
    {
        proListView = (ListView) root.findViewById(R.id.proListView);
        tab_progess = (TextView) root.findViewById(R.id.tab_progess);
        tab_done = (TextView) root.findViewById(R.id.tab_done);
        tab_close = (TextView) root.findViewById(R.id.tab_close);
        tab_apply = (TextView) root.findViewById(R.id.tab_apply);
//        tb_back = (TextView) root.findViewById(R.id.tb_back);
//        tb_title = (TextView) root.findViewById(R.id.tb_title);
        tv_apply = (TextView) root.findViewById(R.id.tv_apply);
        tv_empty = (TextView) root.findViewById(R.id.tv_empty);
        tv_progess = (TextView) root.findViewById(R.id.tv_progess);
        tv_done = (TextView) root.findViewById(R.id.tv_done);

        swipeRefresh = (SwipeRefreshX)root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        tv_recommand = (TextView) root.findViewById(R.id.tv_recommand);
//        tv_action = (TextView) root.findViewById(R.id.tv_action);
        bottomLine_progress = (View) root.findViewById(R.id.bottomLine_progress);
        bottomLine_done = (View) root.findViewById(R.id.bottomLine_done);
        bottomLine_close = (View) root.findViewById(R.id.bottomLine_close);
        bottomLine_apply = (View) root.findViewById(R.id.bottomLine_apply);
        tabtop = (LinearLayout) root.findViewById(R.id.tabtop);
        progress_bar = (LinearLayout)root.findViewById(R.id.progress_bar);

//        tb_title.setText("订单");
//        tv_recommand.setText("任务");
//        tb_back.setVisibility(View.GONE);
//        tv_action.setVisibility(View.GONE);

//        tv_require.setBackgroundResource(R.drawable.titlebar_order_left_selected);
//        tv_recommand.setBackgroundResource(R.drawable.titlebar_order_right_unselected);

        tab_apply.setOnClickListener(this);
        tab_progess.setOnClickListener(this);
        tab_done.setOnClickListener(this);
        tab_close.setOnClickListener(this);
//        tv_require.setOnClickListener(this);
//
//        tv_recommand.setOnClickListener(this);
        proListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ConfirmOrderDetailsActivity.class);
                ConfirmedOrderDetail orderDetails = (ConfirmedOrderDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ConfirmOrderDetails", orderDetails);
                bundle.putString("From",from);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        closeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), OrderDetailsActivity.class);
//                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("OrderDetails", orderDetails);
//                bundle.putString("From", "MyOrder");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                map.put("Status", Status);
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        swipeRefresh.setRefreshing(false);
                        page = 1;
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXConfirmedOrderAdapater = new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, from);
                        proListView.setAdapter(swipeRefreshXConfirmedOrderAdapater);
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
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                Http.request(getActivity(), API.GET_ORDER_NUM, new Http.RequestListener<ConfirmedOrderUndoCount>() {
                    @Override
                    public void onSuccess(ConfirmedOrderUndoCount result) {
                        super.onSuccess(result);

                        tv_apply.setText(String.valueOf(result.getPendingCount()));
                        tv_progess.setText(String.valueOf(result.getOngoingCount()));
                        tv_done.setText(String.valueOf(result.getUnReviewedCount()));

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        tv_apply.setText("0");
                        tv_progess.setText("0");
                        tv_done.setText("0");
                    }
                });

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                page++;
                map.put("Status", Status);
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map)},
                        new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                            @Override
                            public void onSuccess(List<ConfirmedOrderDetail> result) {
                                super.onSuccess(result);
                                if (result != null) {

                                    loadMore(swipeRefreshXConfirmedOrderAdapater, result);
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

    private void loadMore(SwipeRefreshXConfirmedOrderAdapater adapater, List<ConfirmedOrderDetail> orderDetailses) {
        adapater.addItems(orderDetailses);
    }

    @Override
    protected void onLoadData() {
//        Http.request(getActivity(), API.GET_ORDER_NUM, new Http.RequestListener<ConfirmedOrderUndoCount>() {
//            @Override
//            public void onSuccess(ConfirmedOrderUndoCount result) {
//                super.onSuccess(result);
//
//                tv_apply.setText(String.valueOf(result.getPendingCount()));
//                tv_progess.setText(String.valueOf(result.getOngoingCount()));
//                tv_done.setText(String.valueOf(result.getUnReviewedCount()));
//
//            }
//
//            @Override
//            public void onFail(String code) {
//                super.onFail(code);
//            }
//        });
//
//        tab_apply.performClick();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.tv_require:
//                tv_require.setBackgroundResource(R.drawable.titlebar_order_left_selected);
//                tv_recommand.setBackgroundResource(R.drawable.titlebar_order_right_unselected);
//                tabtop.setVisibility(View.VISIBLE);
//                require_selected = true;
//                tab_apply.performClick();
//
//                break;
//            case R.id.tv_recommand:
//                tv_require.setBackgroundResource(R.drawable.titlebar_order_left_unselected);
//                tv_recommand.setBackgroundResource(R.drawable.titlebar_order_right_selected);
//                tabtop.setVisibility(View.GONE);
//                proListView.setAdapter(null);
//                progress_bar.setVisibility(View.VISIBLE);
//                require_selected =false;
//
//                Http.request(getActivity(), API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
//                    @Override
//                    public void onSuccess(List<OrderDetails> result) {
//                        super.onSuccess(result);
//                        progress_bar.setVisibility(View.GONE);
//                        proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_DELETE));
//
//
//                    }
//
//                    @Override
//                    public void onFail(String code) {
//                        super.onFail(code);
//                        progress_bar.setVisibility(View.GONE);
//                    }
//                });
//
//                break;
            case R.id.tab_apply:

                map.clear();
                setTabApply(true);
                setTabPrgess(false);
                setTabDone(false);
                setTabClose(false);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                from = AppConfig.ORDERSTATUS_APPLY;
                Status = "3";
                map.put("Status", Status);
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        page = 1;
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXConfirmedOrderAdapater = new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_APPLY);
                        proListView.setAdapter(swipeRefreshXConfirmedOrderAdapater);
                        if(result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        }
                        else {
                            tv_empty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                Http.request(getActivity(), API.GET_ORDER_NUM, new Http.RequestListener<ConfirmedOrderUndoCount>() {
                    @Override
                    public void onSuccess(ConfirmedOrderUndoCount result) {
                        super.onSuccess(result);

                        tv_apply.setText(String.valueOf(result.getPendingCount()));
                        tv_progess.setText(String.valueOf(result.getOngoingCount()));
                        tv_done.setText(String.valueOf(result.getUnReviewedCount()));

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                    }
                });

                break;
            case R.id.tab_progess:
                map.clear();
                setTabApply(false);
                setTabPrgess(true);
                setTabDone(false);
                setTabClose(false);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                from = AppConfig.ORDERSTATUS_PROGRESS;
//                Map<String, String> map = new HashMap<String, String>();
                Status = "1";
                map.put("Status", Status);
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        page = 1;
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXConfirmedOrderAdapater = new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_PROGRESS);
                        proListView.setAdapter(swipeRefreshXConfirmedOrderAdapater);
                        if(result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        }
                        else {
                            tv_empty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                Http.request(getActivity(), API.GET_ORDER_NUM, new Http.RequestListener<ConfirmedOrderUndoCount>() {
                    @Override
                    public void onSuccess(ConfirmedOrderUndoCount result) {
                        super.onSuccess(result);

                        tv_apply.setText(String.valueOf(result.getPendingCount()));
                        tv_progess.setText(String.valueOf(result.getOngoingCount()));
                        tv_done.setText(String.valueOf(result.getUnReviewedCount()));

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                    }
                });
                break;
            case R.id.tab_done:
                map.clear();
                setTabApply(false);
                setTabPrgess(false);
                setTabDone(true);
                setTabClose(false);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                from = AppConfig.ORDERSTATUS_DONE;
//                Map<String, String> map_done = new HashMap<String, String>();
                Status = "2";
                map.put("Status", Status);
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        page = 1;
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXConfirmedOrderAdapater = new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_DONE);
                        proListView.setAdapter(swipeRefreshXConfirmedOrderAdapater);
                        if(result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        }
                        else {
                            tv_empty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                Http.request(getActivity(), API.GET_ORDER_NUM, new Http.RequestListener<ConfirmedOrderUndoCount>() {
                    @Override
                    public void onSuccess(ConfirmedOrderUndoCount result) {
                        super.onSuccess(result);

                        tv_apply.setText(String.valueOf(result.getPendingCount()));
                        tv_progess.setText(String.valueOf(result.getOngoingCount()));
                        tv_done.setText(String.valueOf(result.getUnReviewedCount()));

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                    }
                });
                break;
            case R.id.tab_close:
                map.clear();
                setTabApply(false);
                setTabPrgess(false);
                setTabDone(false);
                setTabClose(true);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                from = AppConfig.ORDERSTATUS_CLOSE;
//                Map<String, String> map_close = new HashMap<String, String>();
                Status = "0";
                map.put("Status", Status);
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        page = 1;
                        progress_bar.setVisibility(View.GONE);
                        swipeRefreshXConfirmedOrderAdapater = new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_CLOSE);
                        proListView.setAdapter(swipeRefreshXConfirmedOrderAdapater);
                        if(result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        }
                        else {
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
//            tab_apply.setTextColor(Color.rgb(56, 171, 228));
            bottomLine_apply.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_apply.setTextColor(getResources().getColor(R.color.font_999));
            bottomLine_apply.setVisibility(View.INVISIBLE);

        }
    }

    protected void setTabPrgess(boolean isSelected)
    {
        if(isSelected)
        {
            tab_progess.setTextColor(getResources().getColor(R.color.blue));
//            tab_progess.setTextColor(Color.rgb(56, 171, 228));
            bottomLine_progress.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_progess.setTextColor(getResources().getColor(R.color.font_999));
            bottomLine_progress.setVisibility(View.INVISIBLE);

        }
    }
    protected void setTabDone(boolean isSelected)
    {
        if(isSelected)
        {
            tab_done.setTextColor(getResources().getColor(R.color.blue));
//            tab_done.setTextColor(Color.rgb(56, 171, 228));
            bottomLine_done.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_done.setTextColor(getResources().getColor(R.color.font_999));
            bottomLine_done.setVisibility(View.INVISIBLE);

        }
    }
    protected void setTabClose(boolean isSelected)
    {
        if(isSelected)
        {
            tab_close.setTextColor(getResources().getColor(R.color.blue));
//            tab_close.setTextColor(Color.rgb(56, 171, 228));
            bottomLine_close.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_close.setTextColor(getResources().getColor(R.color.font_999));
            bottomLine_close.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Status.equals("0"))
        {
            tab_close.performClick();
        }
        else if(Status.equals("1"))
        {
            tab_progess.performClick();
        }
        else if(Status.equals("2"))
        {
            tab_done.performClick();
        }
        else if(Status.equals("3"))
        {
            tab_apply.performClick();
        }
        else
        {

        }
        MobclickAgent.onPageStart("订单页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("订单页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}
