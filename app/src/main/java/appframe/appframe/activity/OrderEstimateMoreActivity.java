package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
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
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderEstimateAdapater;

/**
 * Created by Administrator on 2016/1/12.
 */
public class OrderEstimateMoreActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_all,tv_good,tv_medium,tv_bad;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderestimatemore);
        init();


    }

    public void init()
    {

        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_all = (TextView)findViewById(R.id.tv_all);
        tv_good = (TextView)findViewById(R.id.tv_good);
        tv_medium = (TextView)findViewById(R.id.tv_medium);
        tv_bad = (TextView)findViewById(R.id.tv_bad);

        tb_back.setText("交易评价");
//        tb_title.setText("交易评价");

        tb_back.setOnClickListener(this);

        userID = getIntent().getStringExtra("UserID");

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_orderestimate);





//        // 设置下拉刷新监听器
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//
//                Toast.makeText(OrderEstimateActivity.this, "refresh", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        // 加载监听器
//        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {
//
//            @Override
//            public void onLoad() {
//
//                Toast.makeText(OrderEstimateActivity.this, "load", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        tv_all.performClick();
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

                break;
            case R.id.tv_good:
                setTv_all(false);
                setTv_good(true);
                setTv_medium(false);
                setTv_bad(false);
                break;
            case R.id.tv_medium:
                setTv_all(false);
                setTv_good(false);
                setTv_medium(true);
                setTv_bad(false);
                break;
            case R.id.tv_bad:
                setTv_all(false);
                setTv_good(false);
                setTv_medium(false);
                setTv_bad(true);
                break;

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

}

