package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.utils.LoginSampleHelper;

/**
 * Created by Administrator on 2017/5/12.
 */

public class TourPersonActivity  extends BaseActivity implements View.OnClickListener{
//    SwipeRefreshX swipeRefresh;
    ListView listView;
//    int page = 1;
    TextView tb_title,tb_back,btn_ask,btn_sendorder;
    Intent intent = new Intent();
//    LinearLayout progress_bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourperson);
        initViews();
        initData();

    }



    protected  void initData()
    {

    }

    protected void initViews()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        btn_ask = (TextView)findViewById(R.id.btn_ask);
//        listView = (ListView)findViewById(R.id.lv_order);
        btn_sendorder = (TextView)findViewById(R.id.btn_sendorder);



        tb_back.setText("");
        tb_title.setText("");
        tb_back.setOnClickListener(this);
        btn_ask.setOnClickListener(this);
        btn_sendorder.setOnClickListener(this);

//        List<TourGuid>list = new ArrayList<TourGuid>();
//        TourGuid tourGuid = new TourGuid();
//        tourGuid.setAge("23岁");
//        tourGuid.setName("叶问看");
//        tourGuid.setPersonality("熟悉走西湖路线");
//        tourGuid.setPrice("价格：250/小时");
//        tourGuid.setSex("男");
//        TourGuid tourGuid2 = new TourGuid();
//        tourGuid2.setAge("21岁");
//        tourGuid2.setName("叶问");
//        tourGuid2.setPersonality("熟悉走西湖路线");
//        tourGuid2.setPrice("价格：210/小时");
//        tourGuid2.setSex("女");
//        list.add(tourGuid);
//        list.add(tourGuid2);
//        swipeRefreshXTourGuidOrderAdapater = new SwipeRefreshXTourGuidOrderAdapater(TourPersonActivity.this, list);
//        listView.setAdapter(swipeRefreshXTourGuidOrderAdapater);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setClass(TourPersonActivity.this, TourPersonDetailsActivity.class);
//
//                TourGuid tourGuid = (TourGuid) parent.getAdapter().getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("TourGuid", tourGuid);
//                intent.putExtras(bundle);
//
//                startActivity(intent);
//            }
//        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_ask:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = "3000";
                intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.btn_sendorder:
                startActivity(new Intent(TourPersonActivity.this,TourOrderSendActivity.class));
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("导游个人页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("导游个人页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

