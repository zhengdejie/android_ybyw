package appframe.appframe.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import appframe.appframe.R;
import appframe.appframe.dto.TourGuid;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMapAdapater;



import static appframe.appframe.activity.TourOrderSendActivity.MAP_LOCATION;

/**
 * Created by Administrator on 2017/5/22.
 */

public class MapActivity extends BaseActivity implements View.OnClickListener,OnGetPoiSearchResultListener{


    TextView tb_title,tb_back,tv_cancel;
    com.baidu.mapapi.map.MapView bmapView;
    EditText et_search;
    ImageView iv_search_clear;
    BaiduMap mBaiduMap;
    private double latitude = 0.0,longitude = 0.0;
    private float radius=0;
    BaiduLocation baiduLocation;
    PoiSearch mPoiSearch;
    List<PoiInfo> dataList = new ArrayList<PoiInfo>();
    ListView lv_location;
    SwipeRefreshXMapAdapater swipeRefreshXMapAdapater;
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    /***
     * 是否是第一次定位
     */
    private volatile boolean isFristLocation = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        initViews();
        initBaiduLocation();
        initData();

    }



    protected  void initData()
    {

    }

    protected  void initBaiduLocation()
    {
        baiduLocation = new BaiduLocation(getApplicationContext(),new MyLocationListener());
        baiduLocation.setOption();
        baiduLocation.mLocationClient.start();





    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
            // map view 销毁后不在处理新接收的位置
            if (location == null || mBaiduMap == null)
                return;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            radius = location.getRadius();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            // 设置自定义图标
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_locationmark);
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfiguration(config);
            // 第一次定位时，将地图位置移动到当前位置
            if (isFristLocation)
            {
                isFristLocation = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }

//            //定义Maker坐标点
//            LatLng point = new LatLng(latitude, longitude);
//            //构建Marker图标
//            BitmapDescriptor bitmap = BitmapDescriptorFactory
//                    .fromResource(R.drawable.icon_art);
//            //构建MarkerOption，用于在地图上添加Marker
//            OverlayOptions option = new MarkerOptions()
//                    .position(point)
//                    .icon(bitmap);
//            //在地图上添加Marker，并显示
//            mBaiduMap.addOverlay(option);
        }
    }

    protected void initViews()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        bmapView = (com.baidu.mapapi.map.MapView)findViewById(R.id.bmapView);
        et_search = (EditText) findViewById(R.id.et_search);
        iv_search_clear = (ImageView) findViewById(R.id.iv_search_clear);
        tv_cancel = (TextView)findViewById(R.id.tv_cancel);
        lv_location = (ListView) findViewById(R.id.lv_location);
        mBaiduMap = bmapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                PoiInfo poiInfo = (PoiInfo) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("PoiInfo", poiInfo);
                intent.putExtras(bundle);

                setResult(MAP_LOCATION, intent);
                finish();

            }
        });

        tb_back.setText("返回");
        tb_title.setText("地点");
        tb_back.setOnClickListener(this);
        iv_search_clear.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        et_search.addTextChangedListener(contentWatcher);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    tv_cancel.setVisibility(View.VISIBLE);
                    mPoiSearch = PoiSearch.newInstance();
                    mPoiSearch.setOnGetPoiSearchResultListener(MapActivity.this);
                    PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
                    poiNearbySearchOption.keyword(String.valueOf(et_search.getText()));
                    poiNearbySearchOption.location(new LatLng(latitude, longitude));
                    poiNearbySearchOption.radius(100);  // 检索半径，单位是米
                    poiNearbySearchOption.pageCapacity(20);  // 默认每页10条
                    mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求

//                    Toast.makeText(AddMemberActivity.this,"呵呵",Toast.LENGTH_SHORT).show();
                    // search pressed and perform your functionality.
                }
                return false;
            }
        });

    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        // 获取POI检索结果
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
//            Toast.makeText(MainActivity.this, "未找到结果",Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//          mBaiduMap.clear();
            if(result != null){
                if(result.getAllPoi()!= null && result.getAllPoi().size()>0){
                    lv_location.setVisibility(View.VISIBLE);
                    dataList.clear();
                    dataList.addAll(result.getAllPoi());
                    swipeRefreshXMapAdapater = new SwipeRefreshXMapAdapater(MapActivity.this, dataList);
                    lv_location.setAdapter(swipeRefreshXMapAdapater);

                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    private TextWatcher contentWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Drawable drawable = null;
            if(s.length() > 0)
            {
                iv_search_clear.setVisibility(View.VISIBLE);
            }
            else
            {
                iv_search_clear.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {


            case R.id.tb_back:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("PoiInfo","");
//                bundle.putParcelable("PoiInfo", poiInfo);
                intent.putExtras(bundle);

                setResult(MAP_LOCATION, intent);

                finish();
                break;
            case R.id.iv_search_clear:
                et_search.setText("");
                et_search.setHint("你要去哪里？");
                break;
            case R.id.tv_cancel:
                tv_cancel.setVisibility(View.GONE);
                lv_location.setAdapter(null);
                lv_location.setVisibility(View.GONE);
                et_search.setText("");
                et_search.setHint("你要去哪里？");
                break;



        }
    }

    @Override
    protected void onStart() {
        // 开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!baiduLocation.mLocationClient.isStarted())
        {
            baiduLocation.mLocationClient.start();
        }

        super.onStart();
    }

    @Override
    protected void onStop()
    {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        baiduLocation.mLocationClient.stop();

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("地图页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("地图页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
