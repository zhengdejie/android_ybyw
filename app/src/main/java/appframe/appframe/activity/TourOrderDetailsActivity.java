package appframe.appframe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXTourGuidOrderAdapater;

/**
 * Created by Administrator on 2017/5/19.
 */

public class TourOrderDetailsActivity extends BaseActivity implements View.OnClickListener{

    ListView listView;
    TextView tb_title,tb_back,btn_ask,btn_pay,btn_reject,tv_name,tv_age,tv_sex,tv_personality,tv_price,tv_timedetails,tv_feedetails,tv_total,tv_status,tv_map,tv_userrequest,tv_pricetxt;
    Intent intent = new Intent();
    appframe.appframe.utils.CircleImageViewCustomer civ_avatar;
    GuideTourOrder guideTourOrderFromUSER,guideTourOrderFromGuide,guideTourOrder,guideAll;
    Boolean ifguide = false;
    public static Activity instance = null;
    int UserID = 0;
    RelativeLayout rl_top;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourorderdetails);
        instance = this;
        initViews();
        initData();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    protected  void initData()
    {
        guideTourOrder = (GuideTourOrder) getIntent().getSerializableExtra("GuideTourOrder");
        guideTourOrderFromUSER = (GuideTourOrder) getIntent().getSerializableExtra("GuideTourOrderFromUser");
        guideTourOrderFromGuide = (GuideTourOrder) getIntent().getSerializableExtra("GuideTourOrderFromGuide");

        if(guideTourOrder != null) {
            guideAll = guideTourOrder;
            UserID = guideTourOrder.getGuide().getUser().getId();

            tv_name.setText(guideTourOrder.getGuide().getUser().getName());
            tv_sex.setText(guideTourOrder.getGuide().getUser().getGender());

            if(guideTourOrder.getGuide().getUser().getAvatar() == null) {
                if (guideTourOrder.getGuide().getUser().getGender().equals(getResources().getString(R.string.male).toString())) {
                    civ_avatar.setDefaultImageResId(R.drawable.maleavatar);
                } else {
                    civ_avatar.setDefaultImageResId(R.drawable.femaleavatar);
                }
            }
            else {
                ImageUtils.setImageUrl(civ_avatar, guideTourOrder.getGuide().getUser().getAvatar());
            }

            tv_personality.setText(guideTourOrder.getGuide().getItinerary());
            tv_price.setText(String.valueOf(guideTourOrder.getGuide().getHourlyRatePrice()) + "元/小时");
            String Buyguidemeals = "";
            if (guideTourOrder.getBuyGuideMeals() == 0) {
                Buyguidemeals = "不请导游吃饭";
            } else if (guideTourOrder.getBuyGuideMeals() == 1) {
                Buyguidemeals = "请导游吃午餐";
            } else if (guideTourOrder.getBuyGuideMeals() == 2) {
                Buyguidemeals = "请导游吃晚餐";
            } else if (guideTourOrder.getBuyGuideMeals() == 3) {
                Buyguidemeals = "请导游吃午餐和晚餐";
            }

            tv_timedetails.setText(String.valueOf(guideTourOrder.getStartTime()) + "-" + guideTourOrder.getEndTime().toString().split(" ")[1] + "\n" + Buyguidemeals);
            if(guideTourOrder.getRendezvous().getAddressInString().contains(","))
            {
                tv_map.setText(guideTourOrder.getRendezvous().getAddressInString().replace(",","\n"));
            }
            else
            {
                tv_map.setText(guideTourOrder.getRendezvous().getAddressInString());
            }
            tv_feedetails.setText(guideTourOrder.getGuide().getHourlyRatePrice() + "元 * " + guideTourOrder.getHours() + "小时");
            tv_total.setText("合计：" + guideTourOrder.getGuide().getHourlyRatePrice() * guideTourOrder.getHours() + "元");
            if(guideTourOrder.getUserRequest().equals("") || guideTourOrder.getUserRequest() == null)
            {
                tv_userrequest.setText("用户没有啥要求~");
            }
            else
            {
                tv_userrequest.setText(guideTourOrder.getUserRequest());
            }


            ifguide = false;
            UpdateStatus(guideTourOrder.getStatus());

            Intent newintent = new Intent();
            Bundle bundle = new Bundle();
            newintent.setClass(TourOrderDetailsActivity.this, PayActivity.class);
            bundle.putSerializable("GuideTourOrder", guideAll);
            newintent.putExtras(bundle);

            startActivity(newintent);
        }
        if(guideTourOrderFromUSER != null) {
            guideAll = guideTourOrderFromUSER;
            UserID = guideTourOrderFromUSER.getGuide().getUser().getId();

            tv_name.setText(guideTourOrderFromUSER.getGuide().getUser().getName());
            tv_sex.setText(guideTourOrderFromUSER.getGuide().getUser().getGender());
            if(guideTourOrderFromUSER.getGuide().getUser().getAvatar() == null) {
                if (guideTourOrderFromUSER.getGuide().getUser().getGender().equals(getResources().getString(R.string.male).toString())) {
                    civ_avatar.setDefaultImageResId(R.drawable.maleavatar);
                } else {
                    civ_avatar.setDefaultImageResId(R.drawable.femaleavatar);
                }
            }
            else {
                ImageUtils.setImageUrl(civ_avatar, guideTourOrderFromUSER.getGuide().getUser().getAvatar());
            }

            tv_personality.setText(guideTourOrderFromUSER.getGuide().getItinerary());
            tv_price.setText(String.valueOf(guideTourOrderFromUSER.getGuide().getHourlyRatePrice()) + "元/小时");
            String Buyguidemeals = "";
            if (guideTourOrderFromUSER.getBuyGuideMeals() == 0) {
                Buyguidemeals = "不请导游吃饭";
            } else if (guideTourOrderFromUSER.getBuyGuideMeals() == 1) {
                Buyguidemeals = "请导游吃午餐";
            } else if (guideTourOrderFromUSER.getBuyGuideMeals() == 2) {
                Buyguidemeals = "请导游吃晚餐";
            } else if (guideTourOrderFromUSER.getBuyGuideMeals() == 3) {
                Buyguidemeals = "请导游吃午餐和晚餐";
            }

            tv_timedetails.setText(String.valueOf(guideTourOrderFromUSER.getStartTime()) + "-" + guideTourOrderFromUSER.getEndTime().toString().split(" ")[1] + "\n" + Buyguidemeals);
            if(guideTourOrderFromUSER.getRendezvous().getAddressInString().contains(","))
            {
                tv_map.setText(guideTourOrderFromUSER.getRendezvous().getAddressInString().replace(",","\n"));
            }
            else
            {
                tv_map.setText(guideTourOrderFromUSER.getRendezvous().getAddressInString());
            }
            tv_feedetails.setText(guideTourOrderFromUSER.getGuide().getHourlyRatePrice() + "元 * " + guideTourOrderFromUSER.getHours() + "小时");
            tv_total.setText("合计：" + guideTourOrderFromUSER.getGuide().getHourlyRatePrice() * guideTourOrderFromUSER.getHours() + "元");
            if(guideTourOrderFromUSER.getUserRequest().equals("") || guideTourOrderFromUSER.getUserRequest() == null)
            {
                tv_userrequest.setText("用户没有啥要求~");
            }
            else
            {
                tv_userrequest.setText(guideTourOrderFromUSER.getUserRequest());
            }

            ifguide = false;
            UpdateStatus(guideTourOrderFromUSER.getStatus());
        }
        if(guideTourOrderFromGuide != null)
        {
            guideAll = guideTourOrderFromGuide;
            UserID = guideTourOrderFromGuide.getApplicant().getId();

            tv_name.setText(guideTourOrderFromGuide.getApplicant().getName());
            tv_sex.setText(guideTourOrderFromGuide.getApplicant().getGender());
            if(guideTourOrderFromGuide.getApplicant().getAvatar() == null) {
                if (guideTourOrderFromGuide.getApplicant().getGender().equals(getResources().getString(R.string.male).toString())) {
                    civ_avatar.setDefaultImageResId(R.drawable.maleavatar);
                } else {
                    civ_avatar.setDefaultImageResId(R.drawable.femaleavatar);
                }
            }
            else {
                ImageUtils.setImageUrl(civ_avatar, guideTourOrderFromGuide.getApplicant().getAvatar());
            }

            tv_personality.setVisibility(View.INVISIBLE);
            tv_price.setVisibility(View.INVISIBLE);
            tv_pricetxt.setVisibility(View.INVISIBLE);
            String Buyguidemeals = "";
            if (guideTourOrderFromGuide.getBuyGuideMeals() == 0) {
                Buyguidemeals = "不请导游吃饭";
            } else if (guideTourOrderFromGuide.getBuyGuideMeals() == 1) {
                Buyguidemeals = "请导游吃午餐";
            } else if (guideTourOrderFromGuide.getBuyGuideMeals() == 2) {
                Buyguidemeals = "请导游吃晚餐";
            } else if (guideTourOrderFromGuide.getBuyGuideMeals() == 3) {
                Buyguidemeals = "请导游吃午餐和晚餐";
            }

            tv_timedetails.setText(String.valueOf(guideTourOrderFromGuide.getStartTime()) + "-" + guideTourOrderFromGuide.getEndTime().toString().split(" ")[1] + "\n" + Buyguidemeals);
            if(guideTourOrderFromGuide.getRendezvous().getAddressInString().contains(","))
            {
                tv_map.setText(guideTourOrderFromGuide.getRendezvous().getAddressInString().replace(",","\n"));
            }
            else
            {
                tv_map.setText(guideTourOrderFromGuide.getRendezvous().getAddressInString());
            }

            tv_feedetails.setText(guideTourOrderFromGuide.getGuide().getHourlyRatePrice() + "元 * " + guideTourOrderFromGuide.getHours() + "小时");
            tv_total.setText("合计：" + guideTourOrderFromGuide.getGuide().getHourlyRatePrice() * guideTourOrderFromGuide.getHours() + "元");
            if(guideTourOrderFromGuide.getUserRequest().equals("") || guideTourOrderFromGuide.getUserRequest() == null)
            {
                tv_userrequest.setText("用户没有啥要求~");
            }
            else
            {
                tv_userrequest.setText(guideTourOrderFromGuide.getUserRequest());
            }

            ifguide = true;
            UpdateStatus(guideTourOrderFromGuide.getStatus());
        }
    }

    protected void initViews()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        btn_ask = (TextView)findViewById(R.id.btn_ask);
        btn_pay = (TextView)findViewById(R.id.btn_pay);
        btn_reject = (TextView)findViewById(R.id.btn_reject);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_age = (TextView)findViewById(R.id.tv_age);
        tv_sex = (TextView)findViewById(R.id.tv_sex);
        tv_personality = (TextView)findViewById(R.id.tv_personality);
        tv_price = (TextView)findViewById(R.id.tv_price);
        tv_pricetxt = (TextView)findViewById(R.id.tv_pricetxt);
        tv_timedetails = (TextView)findViewById(R.id.tv_timedetails);
        tv_feedetails = (TextView)findViewById(R.id.tv_feedetails);
        tv_total = (TextView)findViewById(R.id.tv_total);
        tv_status = (TextView)findViewById(R.id.tv_status);
        tv_map = (TextView)findViewById(R.id.tv_map);
        civ_avatar = (appframe.appframe.utils.CircleImageViewCustomer)findViewById(R.id.civ_avatar);
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);
        tv_userrequest = (TextView)findViewById(R.id.tv_userrequest);


        tb_back.setText("返回");
        tb_title.setText("订单详情");
        tb_back.setOnClickListener(this);
        btn_ask.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        btn_reject.setOnClickListener(this);
        rl_top.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.rl_top:
                if( !ifguide )
                {
                    Intent newintent = new Intent();
                    Bundle bundle = new Bundle();
                    newintent.setClass(TourOrderDetailsActivity.this, TourPersonDetailsActivity.class);
                    bundle.putSerializable("GuideTourOrder", guideAll);
                    newintent.putExtras(bundle);

                    startActivity(newintent);
                }
                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_ask:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                intent = ls.getIMKit().getChattingActivityIntent(String.valueOf(UserID));
                startActivity(intent);
                break;
            case R.id.btn_pay:
                if(btn_pay.getText().equals("继续支付"))
                {
                    Intent newintent = new Intent();
                    Bundle bundle = new Bundle();
                    newintent.setClass(TourOrderDetailsActivity.this, PayActivity.class);
                    bundle.putSerializable("GuideTourOrder", guideAll);
                    newintent.putExtras(bundle);

                    startActivity(newintent);
                }
                else if(btn_pay.getText().equals("完成"))
                {
                    Http.request(TourOrderDetailsActivity.this, API.GUIDECOMPLETE, new Object[]{String.valueOf(guideTourOrderFromUSER.getId())},
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(final String result) {
                                    super.onSuccess(result);
                                    UpdateStatus(500);
                                    Toast.makeText(TourOrderDetailsActivity.this,"订单完成",Toast.LENGTH_SHORT).show();
                                }


                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);
                                }
                            });
                }
                else if(btn_pay.getText().equals("评价"))
                {
                    Intent newintent = new Intent();
                    Bundle bundle = new Bundle();
                    newintent.setClass(TourOrderDetailsActivity.this, TourGuideEstimateActivity.class);
                    bundle.putSerializable("GuideTourOrder", guideTourOrderFromUSER);
                    newintent.putExtras(bundle);

                    startActivity(newintent);
                }
                else if(btn_pay.getText().equals("接单"))
                {
                    Http.request(TourOrderDetailsActivity.this, API.GUIDEACCEPT, new Object[]{String.valueOf(guideTourOrderFromGuide.getId())},
                                    new Http.RequestListener<String>() {
                                        @Override
                                        public void onSuccess(final String result) {
                                            super.onSuccess(result);
                                            UpdateStatus(400);
                                            Toast.makeText(TourOrderDetailsActivity.this,"接单成功",Toast.LENGTH_SHORT).show();
                                        }


                                        @Override
                                        public void onFail(String code) {
                                            super.onFail(code);
                                        }
                                    });
                }
                else
                {

                }
                break;
            case R.id.btn_reject:
                Http.request(TourOrderDetailsActivity.this, API.GUIDEREJECT, new Object[]{String.valueOf(guideTourOrderFromGuide.getId())},
                        new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(final String result) {
                                super.onSuccess(result);
                                UpdateStatus(401);
                                Toast.makeText(TourOrderDetailsActivity.this,"已拒绝该订单",Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onFail(String code) {
                                super.onFail(code);
                            }
                        });
                break;

            default:
                break;


        }
    }

    private void UpdateStatus(int status)
    {
        //用户
        if(!ifguide) {
            btn_reject.setVisibility(View.GONE);
            if (status == 100 || status == 201 || status == 202) {
                tv_status.setText("订单等待支付...");
                btn_ask.setVisibility(View.VISIBLE);
                btn_pay.setText("继续支付");
                btn_pay.setVisibility(View.VISIBLE);

            } else if (status == 300) {
                tv_status.setText("等待导游确认...");
                btn_ask.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(View.GONE);
//            btn_pay.setText("继续支付");
            } else if (status == 400) {
                tv_status.setText("订单进行中...");
                btn_ask.setVisibility(View.VISIBLE);
                btn_pay.setText("完成");
                btn_pay.setVisibility(View.VISIBLE);

            } else if (status == 401) {
                tv_status.setText("订单被取消...");
                btn_ask.setVisibility(View.INVISIBLE);
                btn_pay.setVisibility(View.INVISIBLE);
//            btn_pay.setText("完成");
            } else if (status == 500) {
                tv_status.setText("等待评价...");
                btn_ask.setVisibility(View.GONE);
                btn_pay.setText("评价");
                btn_pay.setVisibility(View.VISIBLE);

            } else if (status == 600) {
                tv_status.setText("已完成...");
                btn_ask.setVisibility(View.INVISIBLE);
                btn_pay.setVisibility(View.INVISIBLE);
//            btn_pay.setText("评价");
            } else {

            }
        }
        //导游
        else
        {
            btn_reject.setVisibility(View.GONE);
            if (status == 100 || status == 201 || status == 202) {
                tv_status.setText("订单等待支付...");
                btn_ask.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(View.GONE);

            } else if (status == 300) {
                tv_status.setText("等待您确认...");
                btn_ask.setVisibility(View.VISIBLE);
                btn_pay.setText("接单");
                btn_pay.setVisibility(View.VISIBLE);
                btn_reject.setVisibility(View.VISIBLE);
//            btn_pay.setText("继续支付");
            } else if (status == 400) {
                tv_status.setText("订单进行中...");
                btn_ask.setVisibility(View.VISIBLE);
                btn_pay.setVisibility(View.GONE);
            } else if (status == 401) {
                tv_status.setText("订单已取消...");
                btn_ask.setVisibility(View.INVISIBLE);
                btn_pay.setVisibility(View.INVISIBLE);
//            btn_pay.setText("完成");
            } else if (status == 500) {
                tv_status.setText("等待评价...");
                btn_ask.setVisibility(View.GONE);
                btn_pay.setVisibility(View.GONE);
            } else if (status == 600) {
                tv_status.setText("已完成...");
                btn_ask.setVisibility(View.INVISIBLE);
                btn_pay.setVisibility(View.INVISIBLE);
//            btn_pay.setText("评价");
            } else {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("导游订单详情页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("导游订单详情页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}


