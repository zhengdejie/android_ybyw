package appframe.appframe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/11/2.
 */
public class MyCollectActivity extends BaseActivity implements View.OnClickListener  {
    private TextView tb_title,tb_back;
    private ListView lv_mycollect ;
    private List<OrderDetails> topOrderDetails = new ArrayList<OrderDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        init();
        initData();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_mycollect = (ListView)findViewById(R.id.lv_mycollect);
        tb_back.setText("个人中心");
        tb_title.setText("我的收藏");
        tb_back.setOnClickListener(this);
        lv_mycollect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MyCollectActivity.this, OrderDetailsActivity.class);
                OrderDetails orderDetails = (OrderDetails)parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                bundle.putString("Entrance", "mycollect");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private  void initData()
    {

        Http.request(MyCollectActivity.this, API.GET_FAVORITEORDER, new Http.RequestListener<List<OrderDetails>>() {
            @Override
            public void onSuccess(List<OrderDetails> result) {
                super.onSuccess(result);

                lv_mycollect.setAdapter(new SwipeRefreshXOrderAdapater(MyCollectActivity.this, result, AppConfig.ORDERSTATUS_PROGRESS));

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
}
