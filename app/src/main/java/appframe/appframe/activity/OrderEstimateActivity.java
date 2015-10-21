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

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderReviewDetail;
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
    TextView tb_title,tb_back,tv_addtag;
    TagView tagView;
    EditText edit_tag;
    RadarChart radarchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderestimate);
        init();
        initRadarChart();

    }

    public void init()
    {
        edit_tag = (EditText)findViewById(R.id.edit_tag);
        tagView = (TagView)findViewById(R.id.tagview);
        tv_addtag = (TextView)findViewById(R.id.tv_addtag);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        radarchart = (RadarChart)findViewById(R.id.Radarchart);
        tb_back.setText("我的口碑");
        tb_title.setText("交易评价");
        tb_back.setOnClickListener(this);
        tv_addtag.setOnClickListener(this);

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
            "专业熟练度", "诚信值", "服务态度"
    };

    public void setData() {

        int cnt = 3; // 不同的维度Party A、B、C...总个数

        // Y的值，数据填充
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        //ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) 28, i));
        }

//        for (int i = 0; i < cnt; i++) {
//            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
//        }

        // Party A、B、C..
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "你的分数");
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
        data.setDrawValues(false);

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
            case R.id.tv_addtag:
                if (edit_tag.getText().toString()!=null&&!edit_tag.getText().toString().equals("")) {
                    String tagTitle= edit_tag.getText().toString();
                    Tag tag = new Tag(tagTitle);
                    tag.isDeletable=true;
                    tagView.addTag(tag);
                }
                break;
        }

    }

}
