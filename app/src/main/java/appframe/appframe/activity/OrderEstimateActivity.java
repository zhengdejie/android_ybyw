package appframe.appframe.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.dto.OrderReviewDetailAndCount;
import appframe.appframe.dto.UserBrief;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.chart.MyMarkerView;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderEstimateAdapater;
import appframe.appframe.widget.tagview.Tag;
import appframe.appframe.widget.tagview.TagView;

/**
 * Created by Administrator on 2015/9/8.
 */
public class OrderEstimateActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_addtag,tv_totalnum,tv_all,tv_good,tv_medium,tv_bad;;
    private double AvgServicePoint = 0.0;
    private double AvgAttitudePoint = 0.0;
    private double AvgCharacterPoint = 0.0;
//    private int TotalNumberOfOrder = 0;
    RadarChart radarchart;
    String userID;
    private int maxOrderReviewID = 0,Type = 0, Page =1;
    Map<String, String> map = new HashMap<String, String>();
    UserBrief userBrief;
    SwipeRefreshXOrderEstimateAdapater swipeRefreshXOrderEstimateAdapater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderestimate);
        init();


    }

    public void init()
    {
        //edit_tag = (EditText)findViewById(R.id.edit_tag);
        //tagView = (TagView)findViewById(R.id.tagview);
        //tv_addtag = (TextView)findViewById(R.id.tv_addtag);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        radarchart = (RadarChart)findViewById(R.id.Radarchart);
        tv_totalnum = (TextView)findViewById(R.id.tv_totalnum);
        tv_all = (TextView)findViewById(R.id.tv_all);
        tv_good = (TextView)findViewById(R.id.tv_good);
        tv_medium = (TextView)findViewById(R.id.tv_medium);
        tv_bad = (TextView)findViewById(R.id.tv_bad);
        tb_back.setText("我的口碑");
        tb_title.setText("交易评价");
        tb_back.setOnClickListener(this);
        tv_all.setOnClickListener(this);
        tv_good.setOnClickListener(this);
        tv_medium.setOnClickListener(this);
        tv_bad.setOnClickListener(this);
        //tv_addtag.setOnClickListener(this);


        //userID = getIntent().getStringExtra("UserID");
        userBrief = (UserBrief)getIntent().getSerializableExtra("userBrief");
//        AvgServicePoint = userBrief.getAvgServicePoint();
//        AvgAttitudePoint = userBrief.getAvgAttitudePoint();
//        AvgCharacterPoint = userBrief.getAvgCharacterPoint();


        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_orderestimate);

        tv_all.performClick();

//        initRadarChart();

        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Page = 1;
                map.put("Type", String.valueOf(Type));
                Http.request(OrderEstimateActivity.this, API.GET_ORDEREVALUATIONBYUSER, new Object[]{String.valueOf(userBrief.getId()), Http.getURL(map)}, new Http.RequestListener<OrderReviewDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderReviewDetailAndCount result) {
                        super.onSuccess(result);
                        if (result != null) {
                            swipeRefreshXOrderEstimateAdapater = new SwipeRefreshXOrderEstimateAdapater(OrderEstimateActivity.this, result.getOrderReviewDetails());
                            listView.setAdapter(swipeRefreshXOrderEstimateAdapater);
                            tv_all.setText(String.format("全部(%d)", result.getAllCount()));
                            tv_good.setText(String.format("好评(%d)", result.getGoodCount()));
                            tv_medium.setText(String.format("中评(%d)", result.getMediumCount()));
                            tv_bad.setText(String.format("差评(%d)", result.getBadCount()));
                            maxOrderReviewID = result.getCount();
                            swipeRefresh.setRefreshing(false);
                        }
                        else
                        {

                            tv_all.setText("全部(0)");
                            tv_good.setText("好评(0)");
                            tv_medium.setText("中评(0)");
                            tv_bad.setText("差评(0)");
                        }
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setRefreshing(false);
                    }
                });
                map.clear();

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {
                Page++;
                map.put("Page", String.valueOf(Page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", String.valueOf(Type));
                map.put("OrderReviewCount", String.valueOf(maxOrderReviewID));
                Http.request(OrderEstimateActivity.this, API.GET_ORDEREVALUATIONBYUSER, new Object[]{String.valueOf(userBrief.getId()), Http.getURL(map)}, new Http.RequestListener<OrderReviewDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderReviewDetailAndCount result) {
                        super.onSuccess(result);

                        loadMore(swipeRefreshXOrderEstimateAdapater, result.getOrderReviewDetails());

                        swipeRefresh.setLoading(false);
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setLoading(false);
                    }
                });
                map.clear();

            }
        });
    }

    public void initRadarChart()
    {
        // 描述，在底部
        radarchart.setDescription("");
        // 绘制线条宽度，圆形向外辐射的线条
        radarchart.setWebLineWidth(1.5f);
        // 内部线条宽度，外面的环状线条
        radarchart.setWebLineWidthInner(1.0f);
        // 所有线条WebLine透明度
        radarchart.setWebAlpha(100);

        radarchart.setSkipWebLineCount(0);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        // 点击的时候弹出对应的布局显示数据
        //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        //radarchart.setMarkerView(mv);

        setData();

        XAxis xAxis = radarchart.getXAxis();
        // X坐标值字体样式
        //xAxis.setTypeface(tf);
        // X坐标值字体大小
        xAxis.setTextSize(12f);

        YAxis yAxis = radarchart.getYAxis();
        // Y坐标值字体样式
        //yAxis.setTypeface(tf);
        // Y坐标值标签个数
        yAxis.setLabelCount(0, false);
        // Y坐标值字体大小
        yAxis.setTextSize(8f);
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true);

//        Legend l = radarchart.getLegend();
//        // 图例位置
//        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
//        // 图例字体样式
//        //l.setTypeface(tf);
//        // 图例X间距
//        l.setXEntrySpace(2f);
//        // 图例Y间距
//        l.setYEntrySpace(1f);
    }

    private String[] mParties = new String[] {
            "专业熟练度", "服务态度", "诚信值"
    };


    public void setData() {

        int cnt = 3; // 不同的维度Party A、B、C...总个数

        // Y的值，数据填充
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        //ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
//        for (int i = 0; i < cnt; i++) {
//            yVals1.add(new Entry((float) 28, i));
//        }


//        for (int i = 0; i < cnt; i++) {
//            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
//        }

        // Party A、B、C..
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);

        String content = "";
        if(AvgServicePoint == 0.0 && AvgAttitudePoint == 0.0 && AvgCharacterPoint == 0.0) {

            yVals1.add(new Entry((float) -1, 0));
            yVals1.add(new Entry((float) -1, 1));
            yVals1.add(new Entry((float) -1, 2));
            content = "默认分数,暂时没有交易评价";
        }
        else
        {

            yVals1.add(new Entry((float) AvgServicePoint, 0));
            yVals1.add(new Entry((float) AvgAttitudePoint, 1));
            yVals1.add(new Entry((float) AvgCharacterPoint, 2));
            content = "评价的分数";
        }
        RadarDataSet set1 = new RadarDataSet(yVals1, content);
        // Y数据颜色设置
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        // 是否实心填充区域
        set1.setDrawFilled(true);
        // 数据线条宽度
        set1.setLineWidth(2f);

//        RadarDataSet set2 = new RadarDataSet(yVals2, "Set 2");
//        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
//        set2.setDrawFilled(true);
//        set2.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        //sets.add(set2);

        RadarData data = new RadarData(xVals, sets);
        // 数据字体样式
        //data.setValueTypeface(tf);
        // 数据字体大小
        data.setValueTextSize(8f);
        // 是否绘制Y值到图表
        data.setDrawValues(true);

        radarchart.getYAxis().setEnabled(false);

        radarchart.setData(data);

        radarchart.invalidate();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_all:
                setTv_all(true);
                setTv_good(false);
                setTv_medium(false);
                setTv_bad(false);
                Type = 0;
                Page = 1;
                map.put("Type", String.valueOf(Type));
                Http.request(this, API.GET_ORDEREVALUATIONBYUSER, new Object[]{String.valueOf(userBrief.getId()), Http.getURL(map)}, new Http.RequestListener<OrderReviewDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderReviewDetailAndCount result) {
                        super.onSuccess(result);
                        if(result != null) {
                            swipeRefreshXOrderEstimateAdapater = new SwipeRefreshXOrderEstimateAdapater(OrderEstimateActivity.this, result.getOrderReviewDetails());
                            listView.setAdapter(swipeRefreshXOrderEstimateAdapater);
                            tv_all.setText(String.format("全部(%d)", result.getAllCount()));
                            tv_good.setText(String.format("好评(%d)", result.getGoodCount()));
                            tv_medium.setText(String.format("中评(%d)", result.getMediumCount()));
                            tv_bad.setText(String.format("差评(%d)", result.getBadCount()));
                            maxOrderReviewID = result.getCount();
                            AvgServicePoint = result.getUser().getAvgServicePoint();
                            AvgAttitudePoint = result.getUser().getAvgAttitudePoint();
                            AvgCharacterPoint = result.getUser().getAvgCharacterPoint();

                        }
                        else
                        {

                            tv_all.setText("全部(0)");
                            tv_good.setText("好评(0)");
                            tv_medium.setText("中评(0)");
                            tv_bad.setText("差评(0)");
                        }
                        initRadarChart();
                    }
                });
                map.clear();
                break;
            case R.id.tv_good:
                setTv_all(false);
                setTv_good(true);
                setTv_medium(false);
                setTv_bad(false);
                Type = 1;
                Page = 1;
                map.put("Type", String.valueOf(Type));
                Http.request(this, API.GET_ORDEREVALUATIONBYUSER, new Object[]{String.valueOf(userBrief.getId()), Http.getURL(map)}, new Http.RequestListener<OrderReviewDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderReviewDetailAndCount result) {
                        super.onSuccess(result);
                        if(result != null) {
                            swipeRefreshXOrderEstimateAdapater = new SwipeRefreshXOrderEstimateAdapater(OrderEstimateActivity.this, result.getOrderReviewDetails());
                            listView.setAdapter(swipeRefreshXOrderEstimateAdapater);
                            tv_all.setText(String.format("全部(%d)", result.getAllCount()));
                            tv_good.setText(String.format("好评(%d)", result.getGoodCount()));
                            tv_medium.setText(String.format("中评(%d)", result.getMediumCount()));
                            tv_bad.setText(String.format("差评(%d)", result.getBadCount()));
                            maxOrderReviewID = result.getCount();
                        }
                        else
                        {

                            tv_all.setText("全部(0)");
                            tv_good.setText("好评(0)");
                            tv_medium.setText("中评(0)");
                            tv_bad.setText("差评(0)");
                        }
                    }
                });
                map.clear();
                break;
            case R.id.tv_medium:
                setTv_all(false);
                setTv_good(false);
                setTv_medium(true);
                setTv_bad(false);
                Type = 2;
                Page = 1;
                map.put("Type", String.valueOf(Type));
                Http.request(this, API.GET_ORDEREVALUATIONBYUSER, new Object[]{String.valueOf(userBrief.getId()), Http.getURL(map)}, new Http.RequestListener<OrderReviewDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderReviewDetailAndCount result) {
                        super.onSuccess(result);
                        if(result != null) {
                            swipeRefreshXOrderEstimateAdapater = new SwipeRefreshXOrderEstimateAdapater(OrderEstimateActivity.this, result.getOrderReviewDetails());
                            listView.setAdapter(swipeRefreshXOrderEstimateAdapater);
                            tv_all.setText(String.format("全部(%d)", result.getAllCount()));
                            tv_good.setText(String.format("好评(%d)", result.getGoodCount()));
                            tv_medium.setText(String.format("中评(%d)", result.getMediumCount()));
                            tv_bad.setText(String.format("差评(%d)", result.getBadCount()));
                            maxOrderReviewID = result.getCount();
                        }
                        else
                        {

                            tv_all.setText("全部(0)");
                            tv_good.setText("好评(0)");
                            tv_medium.setText("中评(0)");
                            tv_bad.setText("差评(0)");
                        }
                    }
                });
                map.clear();
                break;
            case R.id.tv_bad:
                setTv_all(false);
                setTv_good(false);
                setTv_medium(false);
                setTv_bad(true);
                Type = 3;
                Page = 1;
                map.put("Type", String.valueOf(Type));
                Http.request(this, API.GET_ORDEREVALUATIONBYUSER, new Object[]{String.valueOf(userBrief.getId()), Http.getURL(map)}, new Http.RequestListener<OrderReviewDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderReviewDetailAndCount result) {
                        super.onSuccess(result);
                        if(result != null) {
                            swipeRefreshXOrderEstimateAdapater = new SwipeRefreshXOrderEstimateAdapater(OrderEstimateActivity.this, result.getOrderReviewDetails());
                            listView.setAdapter(swipeRefreshXOrderEstimateAdapater);
                            tv_all.setText(String.format("全部(%d)", result.getAllCount()));
                            tv_good.setText(String.format("好评(%d)", result.getGoodCount()));
                            tv_medium.setText(String.format("中评(%d)", result.getMediumCount()));
                            tv_bad.setText(String.format("差评(%d)", result.getBadCount()));
                            maxOrderReviewID = result.getCount();
                        }
                        else
                        {

                            tv_all.setText("全部(0)");
                            tv_good.setText("好评(0)");
                            tv_medium.setText("中评(0)");
                            tv_bad.setText("差评(0)");
                        }
                    }
                });
                map.clear();
                break;
//            case R.id.tv_addtag:
//                if (edit_tag.getText().toString()!=null&&!edit_tag.getText().toString().equals("")) {
//                    String tagTitle= edit_tag.getText().toString();
//                    Tag tag = new Tag(tagTitle);
//                    tag.isDeletable=true;
//                    tagView.addTag(tag);
//                }
//                break;
        }

    }

    public void setTv_all(boolean flag)
    {
        if(flag) {
            tv_all.setBackgroundResource(R.drawable.textview_clicked);
        }
        else
        {
            tv_all.setBackgroundResource(R.drawable.textview_unclicked);
        }
    }
    public void setTv_good(boolean flag)
    {
        if(flag) {
            tv_good.setBackgroundResource(R.drawable.textview_clicked);
        }
        else
        {
            tv_good.setBackgroundResource(R.drawable.textview_unclicked);
        }
    }
    public void setTv_medium(boolean flag)
    {
        if(flag) {
            tv_medium.setBackgroundResource(R.drawable.textview_clicked);
        }
        else
        {
            tv_medium.setBackgroundResource(R.drawable.textview_unclicked);
        }
    }
    public void setTv_bad(boolean flag)
    {
        if(flag) {
            tv_bad.setBackgroundResource(R.drawable.textview_clicked);
        }
        else
        {
            tv_bad.setBackgroundResource(R.drawable.textview_unclicked);
        }
    }

    private void loadMore(SwipeRefreshXOrderEstimateAdapater adapater, List<OrderReviewDetail> orderReviewDetails) {
        adapater.addItems(orderReviewDetails);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("查看交易评价页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("查看交易评价页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
