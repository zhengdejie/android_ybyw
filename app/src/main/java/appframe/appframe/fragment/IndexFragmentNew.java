package appframe.appframe.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baoyz.widget.PullRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.AddFriendsActivity;
import appframe.appframe.activity.CityPickerActivity;
import appframe.appframe.activity.DateActivity;
import appframe.appframe.activity.FeedbackActivity;
import appframe.appframe.activity.OrderActivity;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.activity.QuestionDetailsActivity;
import appframe.appframe.activity.SearchActivity;
import appframe.appframe.activity.TourGuideActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderCategory;
import appframe.appframe.dto.OrderDetailAndCount;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.TourGuid;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.Utils;
import appframe.appframe.widget.SimpleViewPagerIndicator.SimpleViewPagerIndicator;
import appframe.appframe.widget.dropdownmenu.DropdownButton;
import appframe.appframe.widget.dropdownmenu.DropdownItemObject;
import appframe.appframe.widget.dropdownmenu.DropdownListView;
import appframe.appframe.widget.dropdownnew.DropMenuAdapter;
import appframe.appframe.widget.dropdownnew.entity.FilterUrl;
import appframe.appframe.widget.filter.interfaces.OnFilterDoneListener;
import appframe.appframe.widget.hoverscrollview.HoverScrollView;
import appframe.appframe.widget.swiperefresh.IndexCategoryAdapater;
import appframe.appframe.widget.swiperefresh.IndexOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXQuestionAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXTourGuidAdapater;
import appframe.appframe.widget.swiperefresh.ViewPagerAdapter;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/4/24.
 */

public class IndexFragmentNew extends BaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener {
    View root;
    ViewPager viewpager_banner;
    ViewPagerAdapter vpAdapter;
    List<View> views;
//    RecyclerView rv_category;
    ListView listView;
    GridLayoutManager gridLayoutManager;
    IndexCategoryAdapater indexCategoryAdapater;
    IndexOrderAdapater indexOrderAdapater;
    PullRefreshLayout pullRefreshLayout;
    List<String> list = new ArrayList<String>();
    TextView tv_action,tv_city,mFilterContentView;
    int firstLocation =1;
    String locationCity;
    double latitude =0.0, longitude = 0.0;
    String savedCity;
    private static final int REQUEST_CODE_PICK_CITY = 533;
//    RelativeLayout rl_city;
    SwipeRefreshXOrderAdapater hasnotTopAdapater;
    ImageView iv_guide,iv_ubang;

    SwipeRefreshXTourGuidAdapater swipeRefreshXTourGuidAdapater;


    Animation dropdown_in, dropdown_out, dropdown_mask_out;


//    android.support.v4.view.ViewPager pager;

//    MyViewPagerAdapter adapter;
    private List<String> titleList;//viewpager的标题
    BaseFragment[] fragments;

    //引导图片资源
    private static final int[] pics = {
            R.drawable.banner_travel,
            R.drawable.banner_russia,
    };

    //底部小店图片
    private ImageView[] dots ;

    //记录当前选中位置
    private int currentIndex;

    //    private String[] mTitles = new String[] { "求助", "组人", "问答" };
//    private SimpleViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
//    private TabFragment[] mFragments = new TabFragment[mTitles.length];

    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root  = inflater.inflate(R.layout.fragment_indexnew, null);
        getPersimmions();
        initViews();
        initDatas();
        initViewPager();
        return root;
    }
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

//    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {
//
//        public MyViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//
//        }
//
//        public Fragment getItem(int num) {
//            return fragments[num];
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.length;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titleList.get(position);
//        }
//
//    }

    private void initViewPager()
    {

        // init view pager
        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("友帮");
        titleList.add("乐游");
        titleList.add("俄罗斯心动之旅");

        fragments = new BaseFragment[]{
//                new MainOrderFragment(),
//                new RussianTravelFragment(),
//                new TourFragment()
                new XCTSFragment(),
                new CKXCFragment(),
                new CKXXFragment()

        };

//        adapter = new MyViewPagerAdapter(getChildFragmentManager());
////        pager.setOffscreenPageLimit(3);
//        pager.setAdapter(adapter);
//        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//
//                pager.setCurrentItem(position,false);
//            }
//        });




    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onLoadData() {

    }

    public void initViews()
    {

        pullRefreshLayout = (PullRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
//        rv_category = (RecyclerView)root.findViewById(R.id.rv_category);
        listView = (ListView) root.findViewById(R.id.rv_order);
        viewpager_banner = (ViewPager)root.findViewById(R.id.viewpager_banner);
//        gridLayoutManager = new GridLayoutManager(getActivity(),5);
//        rv_category.setLayoutManager(gridLayoutManager);
//        RecyclerView.LayoutManager linearlayoutManager = new LinearLayoutManager(getActivity());
//        rv_order.setLayoutManager(linearlayoutManager);
//        rl_city = (RelativeLayout) root.findViewById(R.id.rl_city);
        iv_guide = (ImageView) root.findViewById(R.id.iv_guide);
        iv_ubang = (ImageView) root.findViewById(R.id.iv_ubang);

        tv_action = (TextView)root.findViewById(R.id.tv_action);
//        tv_city = (TextView)root.findViewById(R.id.tv_city);
//        pager = (android.support.v4.view.ViewPager) root.findViewById(R.id.pager);



        tv_action.setOnClickListener(this);
//        rl_city.setOnClickListener(this);
        iv_guide.setOnClickListener(this);
        iv_ubang.setOnClickListener(this);

//        SharedPreferences sp = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE);
//        savedCity = sp.getString("City", null);
//        if (!TextUtils.isEmpty(savedCity)) {
//            tv_city.setText(savedCity);
//        }


//        BaiduLocation baiduLocation = new BaiduLocation(getActivity(),new MyLocationListener());
//        baiduLocation.setOption();
//        baiduLocation.mLocationClient.start();

        list.add("资源共享");
        list.add("合作/推广");
        list.add("乐游");
        list.add("活动");
        list.add("二手/转让");
        list.add("学术/艺术");
        list.add("工作/商务");
        list.add("维修/家政");
        list.add("生活/娱乐");
        list.add("找人");

        indexCategoryAdapater = new IndexCategoryAdapater(list);
//        rv_category.setAdapter(indexCategoryAdapater);

        indexCategoryAdapater.setOnItemClickLitener(new IndexCategoryAdapater.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 2)
                {
                    startActivity(new Intent(getActivity(),TourGuideActivity.class));
                }
                else
                {
                    startActivity(new Intent(getActivity(),OrderActivity.class));
                }
            }
        });


        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        views = new ArrayList<View>();
        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            views.add(iv);
        }

        vpAdapter = new ViewPagerAdapter(getActivity(),views);
        viewpager_banner.setAdapter(vpAdapter);
        //绑定回调
        viewpager_banner.setOnPageChangeListener(this);

        viewpager_banner.setOnTouchListener(new View.OnTouchListener() {
            int FLAGE = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        FLAGE = 0 ;
                        break ;
                    case MotionEvent.ACTION_MOVE:
                        FLAGE = 1 ;
                        break ;
                    case  MotionEvent.ACTION_UP :
                        if (FLAGE == 0) {
                            int item = viewpager_banner.getCurrentItem();
                            if (item == 0) {
                                startActivity(new Intent(getActivity(),TourGuideActivity.class));


                            } else if (item == 1) {
                                startActivity(new Intent(getActivity(),DateActivity.class));

                            }
                        }
                        break ;
                }
                return false;
            }
        });
        //初始化底部小点
        initDots();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                intent.setClass(getActivity(), OrderDetailsActivity.class);
                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);

                bundle.putSerializable("OrderDetails", orderDetails);

                bundle.putString("From", "Order");
                intent.putExtras(bundle);
                startActivity(intent);



            }
        });

    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) root.findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     *设置当前的引导页
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }

        viewpager_banner.setCurrentItem(position);
    }

    /**
     *这只当前引导小点的选中
     */
    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        setCurDot(arg0);
//        if(arg0 == 0)
//        {
//            startActivity(new Intent(getActivity(),TourGuideActivity.class));
//        }
//        else
//        {
//            startActivity(new Intent(getActivity(),DateActivity.class));
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
//                tv_city.setText(city);
                SharedPreferences.Editor e = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE).edit();
                e.remove("City");
                e.putString("City", city).commit();
//                if(Type == 1)
//                {
//                    tv_recommand.performClick();
//
//                }
//                else if(Type == 2)
//                {
//                    tv_require.performClick();
//                }
//                else if(Type == 3)
//                {
//                    tv_question.performClick();
//                }
//                else
//                {
//
//                }
            }
        }
    }

    private void initDatas()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        map.put("Type", "1");
        map.put("Category", "");
        map.put("OrderBy", "3");
        map.put("Bounty", "0");
        map.put("FriendsFilter", "");
        map.put("PaymentMethodFilter", "");
//        map.put("City",URLEncoder.encode(String.valueOf(tv_city.getText())));
        map.put("lantitude", String.valueOf(latitude));
        map.put("longtitude", String.valueOf(longitude));



        Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
            @Override
            public void onSuccess(OrderDetailAndCount result) {
                super.onSuccess(result);
//                progress_bar.setVisibility(View.GONE);
                if (result != null) {
                    hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false,1);
                    listView.setAdapter(hasnotTopAdapater);
                    hasnotTopAdapater.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(listView);
//                    OrderCount = result.getCount();
//
//                    if (hasTopOrder()) {
//                        OrderDetails topOrder = getTopOrder();
//                        List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
//                        hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true,Type);
//                        listView.setAdapter(hasTopAdapater);
//                        tv_empty.setVisibility(View.GONE);
//
//                    } else {
//                        hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false,Type);
//                        listView.setAdapter(hasnotTopAdapater);
//                        tv_empty.setVisibility(View.GONE);
//                    }
                } else {
//                    if (hasTopOrder()) {
//                        OrderDetails topOrder = getTopOrder();
//                        List<OrderDetails> od = new ArrayList<OrderDetails>();
//                        od.add(topOrder);
//                        hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
//                        listView.setAdapter(hasTopAdapater);
//                        tv_empty.setVisibility(View.GONE);
//
//                    } else {
//                        hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
//                        listView.setAdapter(hasnotTopAdapater);
//                        tv_empty.setVisibility(View.VISIBLE);
//                    }
                }

            }

            @Override
            public void onFail(String code) {
                super.onFail(code);
//                progress_bar.setVisibility(View.GONE);
            }
        });

        // listen refresh event
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                initDatas();
                pullRefreshLayout.setRefreshing(false);
            }
        });

        // refresh complete

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
//        // 获取ListView对应的Adapter
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount();  i++) {
//            // listAdapter.getCount()返回数据项的数目
//            View listItem = listAdapter.getView(i, null, listView);
//            // 计算子项View 的宽高
////            listItem.measure(0, 0);
//            listItem.measure( View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            // 统计所有子项的总高度
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        // listView.getDividerHeight()获取子项间分隔符占用的高度
//        // params.height最后得到整个ListView完整显示需要的高度
//        listView.setLayoutParams(params);
////        sv_main.fullScroll(View.FOCUS_UP);
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int screedWidth = wm.getDefaultDisplay().getWidth();
        int totalHeight = 0;
        int listViewWidth = screedWidth;//listView在布局时的宽度
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(widthSpec, 0);

            int itemHeight = listItem.getMeasuredHeight();
            totalHeight += itemHeight;
        }
        // 减掉底部分割线的高度
        int historyHeight = totalHeight
                + (listView.getDividerHeight() * listAdapter.getCount() - 1);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = historyHeight;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

//    private void initEvents()
//    {
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
//        {
//            @Override
//            public void onPageSelected(int position)
//            {
//                mViewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset,
//                                       int positionOffsetPixels)
//            {
//                mIndicator.scroll(position, positionOffset);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state)
//            {
//
//            }
//        });
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_action:
                new PopupWindows(getActivity(), tv_action);
                break;
//            case R.id.rl_city:
//                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
//                        REQUEST_CODE_PICK_CITY);
//                break;
            case R.id.iv_guide:
                startActivity(new Intent(getActivity(),TourGuideActivity.class));
                break;
            case R.id.iv_ubang:
                startActivity(new Intent(getActivity(),OrderActivity.class));
                break;


        }
    }

//    class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//
//            locationCity = location.getCity() ;
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//
//            //如果获取到城市
//            if(locationCity != null) {
////                String compareCity = locationCity;
//
//                if(locationCity.substring(locationCity.length() -1 ).equals("市"))
//                {
//                    locationCity = locationCity.substring(0,locationCity.length() -1);
//                }
//
//                if(!locationCity.equals(tv_city.getText()))
//                {
//                    if(firstLocation == 1) {
//                        firstLocation ++;
//                        // 定位与选择不同，提示用户切换
//                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//                        alert.setTitle("定位通知")
//                                .setMessage(String.format("当前定位城市为%s,是否切换", locationCity))
//                                .setPositiveButton("切换",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,
//                                                                int which) {
//                                                SharedPreferences.Editor e = getActivity().getSharedPreferences("Location", Context.MODE_PRIVATE).edit();
//                                                e.remove("City");
//                                                e.putString("City", locationCity).commit();
//                                                tv_city.setText(locationCity);
////                                                if (Type == 1) {
////                                                    tv_recommand.performClick();
////
////                                                } else if (Type == 2) {
////                                                    tv_require.performClick();
////                                                } else if (Type == 3) {
////                                                    tv_question.performClick();
////                                                } else {
////
////                                                }
//                                            }
//                                        })
//                                .setNegativeButton("取消",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,
//                                                                int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                        alert.create().show();
//                    }
//                }
//
//
//
//            }
//            else
//            {
//                tv_city.setText("杭州");
//            }
//        }
//    }

    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_add, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAsDropDown(parent, 0,0);
            update();

            TextView btn_addfriends = (TextView) view
                    .findViewById(R.id.item_popupwindows_addfriends);
//            Button btn_require = (Button) view
//                    .findViewById(R.id.item_popupwindows_send_require_order);
//            Button btn_recommand = (Button) view
//                    .findViewById(R.id.item_popupwindows_send_recommand_order);
            TextView btn_search = (TextView) view
                    .findViewById(R.id.item_popupwindows_search);
            TextView btn_feedback = (TextView) view
                    .findViewById(R.id.item_popupwindows_feedback);
            btn_addfriends.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
                    getActivity().startActivity(intent);
                    dismiss();
                }
            });
//            btn_require.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    Intent intent = new Intent();
//                    intent.putExtra("demand","demand");
//                    intent.setClass(getActivity(), OrderSendActivity.class);
//                    getActivity().startActivity(intent);
//                    dismiss();
//                }
//            });
//            btn_recommand.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    Intent intent = new Intent();
//                    intent.putExtra("self","self");
//                    intent.setClass(getActivity(), OrderSendActivity.class);
//                    getActivity().startActivity(intent);
//                    dismiss();
//                }
//            });

            btn_search.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                    dismiss();
                }
            });
            btn_feedback.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
                    dismiss();
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
}

