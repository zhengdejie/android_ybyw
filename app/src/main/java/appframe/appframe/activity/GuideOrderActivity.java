package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Recommend;
import appframe.appframe.dto.RecommendOrder;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXGuideOrderAdapater;

public class GuideOrderActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back;
    SwipeRefreshX swipeRefresh;
    ListView listView;
    int page = 1;
    SwipeRefreshXGuideOrderAdapater swipeRefreshXGuideOrderAdapater;
    Intent getIntent;
    Recommend recommend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_order);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);

        tb_title.setText("公安局");
        tb_back.setOnClickListener(this);


        swipeRefresh = (SwipeRefreshX) findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView) findViewById(R.id.lv_recommendations);
        getIntent = this.getIntent();
        recommend = (Recommend)getIntent.getSerializableExtra("Recommend");

        Http.request(GuideOrderActivity.this, API.GET_RECOMMENDORDERBYID, new Object[]{recommend.getId()},
                new Http.RequestListener<List<RecommendOrder>>() {
                    @Override
                    public void onSuccess(List<RecommendOrder> result) {
                        super.onSuccess(result);
                        swipeRefreshXGuideOrderAdapater = new SwipeRefreshXGuideOrderAdapater(GuideOrderActivity.this, result);
                        listView.setAdapter(swipeRefreshXGuideOrderAdapater);
                    }
                });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RecommendOrder recommendOrder = (RecommendOrder) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(GuideOrderActivity.this, GuideOrderDetailsActivity.class);
                bundle.putSerializable("RecommendOrder", recommendOrder);
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
        MobclickAgent.onPageStart("公安局列表页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("公安局列表页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
