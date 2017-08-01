package appframe.appframe.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.search.core.PoiInfo;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.GuideTour;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.dto.RussianPay;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.UriHandler;
import appframe.appframe.widget.photopicker.util.IntentConstants;
import appframe.appframe.widget.photopicker.view.ImageBucketChooseActivity;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXTourGuidOrderAdapater;

/**
 * Created by Administrator on 2017/5/12.
 */

public class TourOrderSendActivity extends BaseActivity implements View.OnClickListener{

    ListView listView;
    TextView tb_title,tb_back,btn_createorder,tv_startdate,tv_hours,tv_contentcount,tv_map,tv_total,tv_fee,tv_protocol;
    EditText edit_content;
    Intent intent = new Intent();
    CheckBox cb_agree;
    RelativeLayout rl_startdate,rl_hours,rl_location;
    //获取日期格式器对象
    SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
//    SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm");
    private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    public static final int MAP_LOCATION = 1;
    GuideTour guideTour;
    String startTime,endTime;
    int BuyGuideMeal = 0;
    double Longitude = 0.0,Latitude = 0.0;
    public static Activity instance = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourordersend);
        instance = this;
        initViews();
        initData();

    }



    protected  void initData()
    {
        guideTour = (GuideTour)getIntent().getSerializableExtra("TourGuid");
        tv_fee.setText(String.valueOf(guideTour.getHourlyRatePrice()) + "元 * 1");
        tv_total.setText(String.valueOf(guideTour.getHourlyRatePrice()) + "元");
    }

    protected void initViews()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        btn_createorder = (TextView)findViewById(R.id.btn_createorder);
        tv_startdate = (TextView)findViewById(R.id.tv_startdate);
        rl_startdate = (RelativeLayout) findViewById(R.id.rl_startdate);
        tv_hours = (TextView)findViewById(R.id.tv_hours);
        rl_hours = (RelativeLayout) findViewById(R.id.rl_hours);
        rl_location = (RelativeLayout) findViewById(R.id.rl_location);
        tv_contentcount = (TextView)findViewById(R.id.tv_contentcount);
        edit_content = (EditText) findViewById(R.id.edit_content);
        tv_map = (TextView)findViewById(R.id.tv_map);
        tv_total = (TextView)findViewById(R.id.tv_total);
        tv_fee = (TextView)findViewById(R.id.tv_fee);
        cb_agree = (CheckBox) findViewById(R.id.cb_agree);
        tv_protocol = (TextView)findViewById(R.id.tv_protocol);


        tb_back.setText("返回");
        tb_title.setText("创建订单");
        tb_back.setOnClickListener(this);
        btn_createorder.setOnClickListener(this);
        rl_startdate.setOnClickListener(this);
//        tv_hours.setOnClickListener(this);
        rl_hours.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
        edit_content.addTextChangedListener(contentWatcher);


    }

    private TextWatcher contentWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            tv_contentcount.setText(String.format("%d/140",s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_tourordersend, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
            TextView tv_finish = (TextView) view.findViewById(R.id.tv_finish);
            final CheckBox cb_morning = (CheckBox) view.findViewById(R.id.cb_morning);
            final CheckBox cb_lunch = (CheckBox) view.findViewById(R.id.cb_lunch);
            final CheckBox cb_evening = (CheckBox) view.findViewById(R.id.cb_evening);
            final CheckBox cb_supper = (CheckBox) view.findViewById(R.id.cb_supper);

            cb_morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cb_lunch.setClickable(true);
                    } else {
                        cb_lunch.setChecked(false);
                        cb_lunch.setClickable(false);
                    }
                }
            });

            cb_evening.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        cb_supper.setClickable(true);
                    } else {
                        cb_supper.setChecked(false);
                        cb_supper.setClickable(false);
                    }
                }
            });

            tv_close.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });

            tv_finish.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    StringBuilder sb = new StringBuilder();
                    Boolean enter = false;
                    int hours = 0;

                    if(cb_morning.isChecked() == true || cb_evening.isChecked() == true)
                    {
                        tv_hours.setTextColor(getResources().getColor(R.color.font_333));
                        if(cb_morning.isChecked() == true)
                        {
                            enter = true;
                            sb.append("08:00-12:00");
                            if(cb_lunch.isChecked() == true)
                            {
                                sb.append(" 请导游吃午餐");
                            }
                            else
                            {
                                sb.append(" 不请导游吃午餐");
                            }
                            hours += 4;
                            startTime = "08:00:00";
                            endTime = "12:00:00";
                        }

                        if(cb_evening.isChecked() == true)
                        {
                            if(enter)
                            {
                                sb.append("\n");
                                endTime = "18:00:00";
                            }
                            else
                            {
                                startTime = "14:00:00";
                            }
                            sb.append("14:00-18:00");
                            if(cb_supper.isChecked() == true)
                            {
                                sb.append(" 请导游吃晚餐");
                            }
                            else
                            {
                                sb.append(" 不请导游吃晚餐");
                            }
                            hours += 4;
                        }
                    }
                    else
                    {
                        sb.delete(0,sb.length());
                        sb.append("选择时间");
                    }

                    tv_hours.setText(sb.toString());
                    if(hours != 0)
                    {
                        tv_fee.setText(String.valueOf(guideTour.getHourlyRatePrice()) + "元 * " + hours);
                        tv_total.setText(String.valueOf(guideTour.getHourlyRatePrice() * hours) + "元");
                    }
                    else
                    {
                        tv_fee.setText(String.valueOf(guideTour.getHourlyRatePrice()) + "元 * 1");
                        tv_total.setText(String.valueOf(guideTour.getHourlyRatePrice()) + "元");
                    }
                    if(cb_lunch.isChecked() == true && cb_supper.isChecked() == false)
                    {
                        BuyGuideMeal = 1;
                    }
                    else if(cb_supper.isChecked() == true && cb_lunch.isChecked() == false)
                    {
                        BuyGuideMeal = 2;
                    }
                    else if(cb_lunch.isChecked() == true && cb_supper.isChecked() == true)
                    {
                        BuyGuideMeal = 3;
                    }
                    else if(cb_lunch.isChecked() == false && cb_supper.isChecked() == false)
                    {
                        BuyGuideMeal = 0;
                    }
                    dismiss();
                }
            });

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.rl_startdate:
                new DatePickerDialog(TourOrderSendActivity.this, datepicker, dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.rl_hours:
                new PopupWindows(TourOrderSendActivity.this, tv_hours);
                break;
            case R.id.rl_location:

                intent.setClass(this,MapActivity.class);
                startActivityForResult(intent, MAP_LOCATION);
                break;
            case R.id.btn_createorder:
                if(tv_startdate.getText().equals("") || tv_startdate.getText() == null)
                {
                    Toast.makeText(TourOrderSendActivity.this,"请选择开始日期",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(tv_hours.getText().equals("") || tv_hours.getText() == null)
                    {
                        Toast.makeText(TourOrderSendActivity.this,"请选择时间",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(tv_map.getText().equals("") || tv_map.getText() == null)
                        {
                            Toast.makeText(TourOrderSendActivity.this,"请选择地点",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(cb_agree.isChecked() == false)
                            {
                                Toast.makeText(TourOrderSendActivity.this,"请同意友帮服务协议",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String PlaceToMeet = "{Longitude:" + Longitude + ",Latitude:" + Latitude + ",AddressInString:\"" + tv_map.getText().toString().replace("\n",",") + "\"}";
                                Http.request(TourOrderSendActivity.this, API.GUIDEAPPLY, new Object[]{guideTour.getId()}, Http.map(
                                        "StartTime",tv_startdate.getText() + " " + startTime,
                                        "EndTime",tv_startdate.getText() + " " + endTime,
                                        "BuyGuideMeal",String.valueOf(BuyGuideMeal),
                                        "PlaceToMeet",PlaceToMeet,
                                        "UserRequest",edit_content.getText().toString()

                                        ),
                                        new Http.RequestListener<GuideTourOrder>() {
                                            @Override
                                            public void onSuccess(final GuideTourOrder result) {
                                                super.onSuccess(result);
                                                Intent intent = new Intent();
                                                intent.setClass(TourOrderSendActivity.this, TourOrderDetailsActivity.class);

                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("GuideTourOrder", result);
                                                intent.putExtras(bundle);

                                                startActivity(intent);

                                            }


                                            @Override
                                            public void onFail(String code) {
                                                super.onFail(code);
                                            }
                                        });
                            }
                        }
                    }
                }

                break;

            case R.id.tv_protocol:
                UriHandler.openWebActivity(TourOrderSendActivity.this, "http://www.ubangwang.com/docs/zhulong_youbang_software_license_and_services_agreement.html");
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MAP_LOCATION:
                Bundle bundle = data.getExtras();
                PoiInfo poiInfo = bundle.getParcelable("PoiInfo");
                if(poiInfo != null) {
                    tv_map.setText(poiInfo.name + "\n" + poiInfo.address);
                    tv_map.setTextColor(getResources().getColor(R.color.font_333));
                    Longitude = poiInfo.location.longitude;
                    Latitude = poiInfo.location.latitude;
                }
//                Log.v("string=", testBundleString);
//                Log.v("name=", parcelableData.getName());
                break;
            default:
                break;
        }
    }

    //当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //修改日历控件的年，月，日
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //将页面TextView的显示更新为最新时间
            updateDeadlineDate();
        }

    };

    private void updateDeadlineDate() {

        Date toDay = new Date();
        if(toDay.before(dateAndTime.getTime()) || fmtDate.format(toDay.getTime()).equals(fmtDate.format(dateAndTime.getTime())))
        {
            tv_startdate.setText(fmtDate.format(dateAndTime.getTime()));
            tv_startdate.setTextColor(getResources().getColor(R.color.font_333));
        }
        else
        {
            Toast.makeText(TourOrderSendActivity.this,"选择时间有误，请选择今天之后的日期",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("导游创建订单页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("导游创建订单页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}


