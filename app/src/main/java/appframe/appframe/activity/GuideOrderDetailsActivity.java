package appframe.appframe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.Recommend;
import appframe.appframe.dto.RecommendOrder;
import appframe.appframe.dto.RecommendOrderQuestion;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXGuideOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXGuideOrderQuestionAdapater;

import static appframe.appframe.R.id.tv_message;
import static appframe.appframe.R.layout.item;

/**
 * Created by Administrator on 2017-01-09.
 */

public class GuideOrderDetailsActivity extends BaseActivity implements View.OnClickListener {

    TextView tb_title,tb_back,tv_introduction,tv_name,tv_focus,tv_servicecontent,tv_personality,tv_selfestimate;
    ImageView iv_workpic,iv_message;
    RatingBar rb_totalvalue;
//    SwipeRefreshX swipeRefresh;
    ListView listView;
//    int page = 1;
    SwipeRefreshXGuideOrderQuestionAdapater swipeRefreshXGuideOrderQuestionAdapater;
    List<String> questionList;
    appframe.appframe.utils.CircleImageViewCustomer civ_avatar;
    Intent getIntent;
    RecommendOrder recommendOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_orderdetails);
        init();
    }
    protected  void init()
    {
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_focus = (TextView) findViewById(R.id.tv_focus);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        tv_servicecontent = (TextView) findViewById(R.id.tv_servicecontent);
        tv_personality = (TextView) findViewById(R.id.tv_personality);
        tv_selfestimate = (TextView) findViewById(R.id.tv_selfestimate);
        rb_totalvalue = (RatingBar) findViewById(R.id.rb_totalvalue);
        iv_workpic = (ImageView) findViewById(R.id.iv_workpic);
        civ_avatar = (appframe.appframe.utils.CircleImageViewCustomer) findViewById(R.id.civ_avatar);


        tv_focus.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        tb_back.setOnClickListener(this);

        getIntent = this.getIntent();
        recommendOrder = (RecommendOrder)getIntent.getSerializableExtra("RecommendOrder");

        ImageUtils.setImageUrl(civ_avatar, recommendOrder.getUserDetail().getAvatar());

        if(recommendOrder.getUserDetail().getName().equals(Auth.getCurrentUser().getName()))
        {
            tv_focus.setVisibility(View.INVISIBLE);
        }
        else
        {
            tv_focus.setVisibility(View.VISIBLE);
        }

        if(recommendOrder.getUserDetail().isFans())
        {
            tv_focus.setEnabled(false);
            tv_focus.setText("已关注");
        }
        else
        {
            tv_focus.setEnabled(true);
            tv_focus.setText("+关注");
        }
        tb_title.setText("");

        tv_name.setText(recommendOrder.getUserDetail().getName());
        tv_servicecontent.setText(recommendOrder.getServiceContent());
        tv_personality.setText(recommendOrder.getContent());
        tv_selfestimate.setText(recommendOrder.getSelfevaluation());

//        swipeRefresh = (SwipeRefreshX) findViewById(R.id.swipeRefresh);
//
//        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView) findViewById(R.id.lv_question);


        Http.request(GuideOrderDetailsActivity.this, API.GET_RECOMMENDORDERQUESTIONBYID, new Object[]{recommendOrder.getRecommendId()},
                new Http.RequestListener<List<RecommendOrderQuestion>>() {
                    @Override
                    public void onSuccess(List<RecommendOrderQuestion> result) {
                        super.onSuccess(result);
                        swipeRefreshXGuideOrderQuestionAdapater = new SwipeRefreshXGuideOrderQuestionAdapater(GuideOrderDetailsActivity.this, result);
                        listView.setAdapter(swipeRefreshXGuideOrderQuestionAdapater);
                        setListViewHeightBasedOnChildren(listView);
                    }
                });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RecommendOrderQuestion recommendOrderQuestion = (RecommendOrderQuestion) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(GuideOrderDetailsActivity.this, GuideOrderQuestionActivity.class);
                bundle.putSerializable("RecommendOrderQuestion", recommendOrderQuestion);
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
            case R.id.tv_focus:
                if(tv_focus.getText().equals("+关注"))
                {
                    Http.request( GuideOrderDetailsActivity.this, API.ADDFANS,
                            Http.map("Fans", String.valueOf(Auth.getCurrentUserId()),
                                    "Celebrity", String.valueOf(recommendOrder.getUserDetail().getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
//                                    Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                                    tv_focus.setEnabled(false);
                                    tv_focus.setText("已关注");
                                    recommendOrder.getUserDetail().setIsFans(true);
//                                    item.getOrderer().setIsFans(true);
                                }

                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);
                                }
                            });
                }
                break;
            case R.id.iv_message:
                startActivity(new Intent(GuideOrderDetailsActivity.this,GuideOrderAskActivity.class));
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("公安局详情页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("公安局详情页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
//        sv_main.fullScroll(View.FOCUS_UP);
    }
}

