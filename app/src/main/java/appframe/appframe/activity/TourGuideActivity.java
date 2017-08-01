package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.GuideTour;
import appframe.appframe.dto.MRussianTour;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.TourGuid;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.dropdownnew.DropMenuAdapter;
import appframe.appframe.widget.dropdownnew.entity.FilterUrl;
import appframe.appframe.widget.filter.interfaces.OnFilterDoneListener;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXRussianTravelAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXTourGuidAdapater;

/**
 * Created by Administrator on 2017/5/12.
 */

public class TourGuideActivity  extends BaseActivity implements View.OnClickListener,OnFilterDoneListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    int page = 1;
    TextView tb_title,tb_back,tv_empty,mFilterContentView;
    LinearLayout progress_bar;
    SwipeRefreshXTourGuidAdapater swipeRefreshXTourGuidAdapater;
    appframe.appframe.widget.filter.DropDownMenu dropDownMenu;
    Map<String, String> map = new HashMap<String, String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourguide);
        initViews();
        initData();
        initFilterDropDownView();

    }

    private void initFilterDropDownView() {
        String[] titleList = new String[]{"性别", "金额"};
        dropDownMenu.setMenuAdapter(new DropMenuAdapter(this, titleList, this));
    }

    @Override
    public void onFilterDone(int position, String positionTitle, String urlValue) {
        dropDownMenu.setPositionIndicatorText(FilterUrl.instance().position, FilterUrl.instance().positionTitle);
        if(FilterUrl.instance().position == 0)
        {
            if(!FilterUrl.instance().positionTitle.equals("性别")) {
                map.put("FilterByGender", URLEncoder.encode(FilterUrl.instance().positionTitle));
            }
            else
            {
                map.remove("FilterByGender");
            }
        }
        else if(FilterUrl.instance().position == 1)
        {
            if(!FilterUrl.instance().positionTitle.equals("金额")) {
                if(FilterUrl.instance().positionTitle.equals("从高到低")) {
                    map.put("FilterByHourlyRate", "1");
                }
                else
                {
                    map.put("FilterByHourlyRate", "2");
                }
            }
            else
            {
                map.remove("FilterByHourlyRate");
            }
        }
        else
        {
            map.clear();
        }

        dropDownMenu.close();
//        mFilterContentView.setText(FilterUrl.instance().toString());

        progress_bar.setVisibility(View.VISIBLE);
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        Http.request(TourGuideActivity.this, API.GETALLGUIDE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTour>>() {
            @Override
            public void onSuccess(List<GuideTour> result) {
                super.onSuccess(result);
                progress_bar.setVisibility(View.GONE);
                swipeRefreshXTourGuidAdapater = new SwipeRefreshXTourGuidAdapater(TourGuideActivity.this, result);
                listView.setAdapter(swipeRefreshXTourGuidAdapater);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterUrl.instance().clear();
    }

    protected  void initData()
    {
        progress_bar.setVisibility(View.VISIBLE);

        Http.request(TourGuideActivity.this, API.GETALLGUIDE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTour>>() {
            @Override
            public void onSuccess(List<GuideTour> result) {
                super.onSuccess(result);
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                progress_bar.setVisibility(View.GONE);
                swipeRefreshXTourGuidAdapater = new SwipeRefreshXTourGuidAdapater(TourGuideActivity.this, result);
                listView.setAdapter(swipeRefreshXTourGuidAdapater);
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

        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(TourGuideActivity.this, API.GETALLGUIDE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTour>>() {
                    @Override
                    public void onSuccess(List<GuideTour> result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        swipeRefreshXTourGuidAdapater = new SwipeRefreshXTourGuidAdapater(TourGuideActivity.this, result);
                        listView.setAdapter(swipeRefreshXTourGuidAdapater);
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
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {
                page++;
//                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));

                Http.request(TourGuideActivity.this, API.GETALLGUIDE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<GuideTour>>() {
                    @Override
                    public void onSuccess(List<GuideTour> result) {
                        super.onSuccess(result);
                        swipeRefresh.setLoading(false);
                        if (result != null) {

                            loadMore(swipeRefreshXTourGuidAdapater, result);

                        }


                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setLoading(false);
                    }

                    @Override
                    public void onMessage(String result) {
                        super.onMessage(result);
                        swipeRefresh.setLoading(false);
                    }
                });

            }
        });
    }

    private void loadMore(SwipeRefreshXTourGuidAdapater adapater, List<GuideTour> orderDetailses) {
        adapater.addItems(orderDetailses);
    }

    protected void initViews()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        dropDownMenu = (appframe.appframe.widget.filter.DropDownMenu)findViewById(R.id.dropDownMenu);
        mFilterContentView = (TextView)findViewById(R.id.mFilterContentView);
//        progress_bar.setVisibility(View.VISIBLE);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_tradehistory);

        tb_back.setText("返回");
        tb_title.setText("找导游");
        tb_back.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(TourGuideActivity.this, TourPersonDetailsActivity.class);

                GuideTour tourGuid = (GuideTour) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("TourGuid", tourGuid);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

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

        MobclickAgent.onPageStart("找导游页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("找导游页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
