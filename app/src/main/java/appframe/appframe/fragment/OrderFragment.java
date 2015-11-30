package appframe.appframe.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.FeedbackActivity;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.activity.OrderSendActivity;
import appframe.appframe.activity.SearchActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.BaiduLocation;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.dropdownmenu.DropdownButton;
import appframe.appframe.widget.dropdownmenu.DropdownItemObject;
import appframe.appframe.widget.dropdownmenu.DropdownListView;
import appframe.appframe.widget.dropdownmenu.TopicLabelObject;
import appframe.appframe.widget.photopicker.util.IntentConstants;
import appframe.appframe.widget.photopicker.view.ImageBucketChooseActivity;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;

/**
 * Created by dashi on 15/6/20.
 */
public class OrderFragment extends BaseFragment  {

    View test_button,UploadContact_button;
    //类别
    private static final int ID_TYPE_ALL = 0;
    private static final int ID_TYPE_CLOSE = 1;
    private static final int ID_TYPE_FOOD = 2;
    private static final int ID_TYPE_HOUSE = 3;
    private static final int ID_TYPE_WALK = 4;
    private static final int ID_TYPE_ACADEMIC = 5;
    private static final int ID_TYPE_WORK = 6;
    private static final int ID_TYPE_LIFE = 7;
    private static final int ID_TYPE_PERSON = 8;
    private static final int ID_TYPE_SECOND = 9;
    private static final int ID_TYPE_SHARE = 10;
    private static final int ID_TYPE_COOP = 11;
    private static final int ID_TYPE_ACTIVITY = 12;
    private static final String TYPE_ALL = "类别";
    private static final String TYPE_CLOSE = "衣";
    private static final String TYPE_FOOD = "食";
    private static final String TYPE_HOUSE = "住";
    private static final String TYPE_WALK = "行";
    private static final String TYPE_ACADEMIC = "学术/艺术";
    private static final String TYPE_WORK = "工作/商务";
    private static final String TYPE_LIFE = "生活/娱乐";
    private static final String TYPE_PERSON = "找人";
    private static final String TYPE_SECOND = "二手/转让";
    private static final String TYPE_SHARE = "资源共享";
    private static final String TYPE_COOP = "合作/推广";
    private static final String TYPE_ACTIVITY = "活动";
    //智能
    private static final int ID_MULTI_ALL = 50;
    private static final int ID_MULTI_DISTANCE = 51;
    private static final int ID_MULTI_INTEGRITY = 52;
    private static final int ID_MULTI_TIME = 53;
    private static final int ID_MULTI_Ass_TIME = 54;
    private static final String MULTI_ALL = "智能";
    private static final String MULTI_DISTANCE = "距离最近";
    private static final String MULTI_INTEGRITY = "诚信度高";
    private static final String MULTI_TIME = "发布时间近";
    private static final String MULTI_Ass_TIME = "截止时间近";
    //赏金
    private static final int ID_MONEY_ALL = 100;
    private static final int ID_MONEY_MONEY = 101;
    private static final int ID_MONEY_MONEY_LOW = 102;
    private static final String MONEY_ALL = "金额";
    private static final String MONEY_MONEY = "任务赏金高";
    private static final String MONEY_MONEY_LOW = "任务赏金低";
    //private static final String MONEY_TIME = "任务时间长";
    //筛选
    private static final int ID_SELECT_ALL = 150;
    private static final int ID_SELECT_FIRST = 151;
    private static final int ID_SELECT_SECOND = 152;
    private static final int ID_SELECT_BOTH = 153;
    private static final int ID_SELECT_OFFLINE = 154;
    private static final int ID_SELECT_ONLINE = 155;
//    private static final String SELECT_ALL = "筛选";
    private static final String SELECT_FIRST = "一度朋友";
    private static final String SELECT_SECOND = "二度朋友";
    private static final String SELECT_BOTH = "陌生人";
    private static final String SELECT_OFFLINE = "线下支付";
    private static final String SELECT_ONLINE = "线上支付";

    public static final int SCAN_CODE = 1;
    BDLocation bdLocation = new BDLocation();
    int Type;  //1=老板单， 2=打工单
    ListView listView;
    View mask;
    DropdownButton chooseType, chooseMulti, chooseMoney, chooseSelect;
    DropdownListView dropdownType, dropdownMulti, dropdownMoney, dropdownSelect;
    SwipeRefreshX swipeRefresh;
    Animation dropdown_in, dropdown_out, dropdown_mask_out;
    View root;
    TextView tv_back,tv_require,tv_recommand,tv_action,tv_latitude,tv_longitude;
    public OrderDetails topOrderDetails;

    private List<TopicLabelObject> labels = new ArrayList<>();

    private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();

//    public static OrderFragment newInstance(String type) {
//
//        OrderFragment fragment = new OrderFragment();
//        fragment.type = type;
//        return fragment;
//    }

    protected int TransferOrderBy(String type)
    {
        if(type.equals(MULTI_ALL))
        {
            return 3;
        }
        else if(type.equals(MULTI_DISTANCE))
        {
            return 1;
        }
        else if(type.equals(MULTI_INTEGRITY))
        {
            return 2;
        }
        else if(type.equals(MULTI_TIME))
        {
            return 3;
        }
        else
        {
            return 4;
        }
    }

    protected int TransferMoney(String type)
    {
        if(type.equals(MONEY_ALL))
        {
            return 0;
        }
        else if(type.equals(MONEY_MONEY))
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }

    protected String TransferFriendsFilter(List<DropdownItemObject> DDIO)
    {
        StringBuilder friendsFilter = new StringBuilder();
        for(DropdownItemObject currentMulti : dropdownSelect.currentMulti)
        {
            if(currentMulti.id == ID_SELECT_FIRST) {
                friendsFilter.append("," + currentMulti.text);
            }
            if(currentMulti.id == ID_SELECT_SECOND) {
                friendsFilter.append("," + currentMulti.text);
            }
            if(currentMulti.id == ID_SELECT_BOTH) {
                friendsFilter.append("," + currentMulti.text);
            }

        }
        if(friendsFilter.length()!=0) {
            friendsFilter.deleteCharAt(0);
        }
        return friendsFilter.toString();

    }

    protected String TransferPaymentFilter(List<DropdownItemObject> DDIO)
    {
        StringBuilder paymentMethodFilter = new StringBuilder();
        for(DropdownItemObject currentMulti : dropdownSelect.currentMulti)
        {
            if(currentMulti.id == ID_SELECT_OFFLINE) {
                paymentMethodFilter.append("," + currentMulti.text);
            }
            if(currentMulti.id == ID_SELECT_ONLINE) {
                paymentMethodFilter.append("," + currentMulti.text);
            }
        }

        if(paymentMethodFilter.length()!=0) {
            paymentMethodFilter.deleteCharAt(0);
        }
        return paymentMethodFilter.toString();
    }

    //获取置顶单子
    protected  OrderDetails  getTopOrder()
    {
        OrderDetails orderDetails = new OrderDetails();
        SharedPreferences sp = getActivity().getSharedPreferences("TOPORDER", Context.MODE_PRIVATE);
        String OrderDetails = sp.getString("OrderDetails", null);
        if (!TextUtils.isEmpty(OrderDetails)) {
            orderDetails = GsonHelper.getGson().fromJson(OrderDetails, OrderDetails.class);
        }

        return orderDetails;
    }

    //判断有没置顶单
    protected boolean hasTopOrder()
    {
        SharedPreferences sp = getActivity().getSharedPreferences("TOPORDER", Context.MODE_PRIVATE);
        String OrderDetails = sp.getString("OrderDetails", null);
        if (!TextUtils.isEmpty(OrderDetails)) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails = GsonHelper.getGson().fromJson(OrderDetails, OrderDetails.class);
            if(orderDetails.getType() == Type)
            {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    protected  List<OrderDetails> getOrders(List<OrderDetails> result,OrderDetails topOrder)
    {
        OrderDetails tempOrder = new OrderDetails();
        for(OrderDetails od : result)
        {
            if(od.getId() == topOrder.getId())
            {
                tempOrder = od;
            }
        }
        result.remove(tempOrder);
        result.add(0, topOrder);

        return result;
    }


    @Override
    protected void onLoadData() {

        tv_require.performClick();

    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case SCAN_CODE:
//                TextView scanResult = (TextView) root.findViewById(R.id.txt_scanresult);
//                if (resultCode == getActivity().RESULT_OK) {
//                    String result = data.getStringExtra("scan_result");
//                    scanResult.setText(result);
//                } else if (resultCode == getActivity().RESULT_CANCELED) {
//                    scanResult.setText("扫描出错");
//                }
//                break;
//            default:
//                break;
//        }
//    }

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_order,null);

        tv_back = (TextView)root.findViewById(R.id.tv_back);
        tv_require = (TextView)root.findViewById(R.id.tv_require);
        tv_recommand = (TextView)root.findViewById(R.id.tv_recommand);
        tv_action = (TextView)root.findViewById(R.id.tv_action);
        tv_latitude = (TextView)root.findViewById(R.id.tv_latitude);
        tv_longitude = (TextView)root.findViewById(R.id.tv_longitude);

        BaiduLocation baiduLocation = new BaiduLocation(getActivity());
        baiduLocation.tv_latitude = tv_latitude;
        baiduLocation.tv_longitude = tv_longitude;
        baiduLocation.setOption();
        baiduLocation.mLocationClient.start();

        tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopupWindows(getActivity(), tv_action);
            }
        });
        tv_require.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = 2;
                tv_require.setBackgroundColor(Color.GREEN);
                tv_recommand.setBackgroundColor(Color.WHITE);
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", String.valueOf(Type));
                map.put("Category", dropdownType.current.text.toString().equals("类别") ? "" : URLEncoder.encode(dropdownType.current.text.toString()));
                map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
                map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
                map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
                map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
                map.put("lantitude", tv_latitude.getText().toString());
                map.put("longtitude", tv_longitude.getText().toString());


                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(List<OrderDetails> result) {
                        super.onSuccess(result);

                        if (hasTopOrder()) {
                            OrderDetails topOrder = getTopOrder();
                            List<OrderDetails> list_OD = getOrders(result, topOrder);
                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true));

                        } else {
                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_MAIN, false));
                        }


                    }
                });
            }
        });

        tv_recommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = 1;
                tv_require.setBackgroundColor(Color.WHITE);
                tv_recommand.setBackgroundColor(Color.GREEN);

                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", String.valueOf(Type));
                map.put("Category", dropdownType.current.text.toString().equals("类别") ? "" :URLEncoder.encode(dropdownType.current.text.toString()));
                map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
                map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
                map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
                map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
                map.put("lantitude", tv_latitude.getText().toString());
                map.put("longtitude", tv_longitude.getText().toString());



                Http.request(getActivity(), API.GET_ORDER,new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess( List<OrderDetails> result) {
                        super.onSuccess(result);

                        if(hasTopOrder())
                        {
                            OrderDetails topOrder = getTopOrder();
                            List<OrderDetails> list_OD = getOrders(result, topOrder);
                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true));
                        }
                        else
                        {
                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result,AppConfig.ORDERSTATUS_MAIN, false));
                        }

                    }
                });
            }
        });


        swipeRefresh = (SwipeRefreshX)root.findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView) root.findViewById(R.id.lv_order);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), OrderDetailsActivity.class);
                OrderDetails orderDetails = (OrderDetails)parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                if( hasTopOrder() && orderDetails.getId() == getTopOrder().getId())
                {
                    bundle.putString("hasTopOrder","1");
                }
                bundle.putString("From","Order");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", String.valueOf(Type));
                map.put("Category", dropdownType.current.text.toString().equals("类别") ? "" :URLEncoder.encode(dropdownType.current.text.toString()));
                map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
                map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
                map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
                map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
                map.put("lantitude", tv_latitude.getText().toString());
                map.put("longtitude", tv_longitude.getText().toString());


                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(final List<OrderDetails> result) {
                        super.onSuccess(result);

                        if(hasTopOrder())
                        {
                            OrderDetails topOrder = getTopOrder();
                            List<OrderDetails> list_OD = getOrders(result,topOrder);
                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), list_OD,AppConfig.ORDERSTATUS_MAIN, true));
                        }
                        else
                        {
                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result,AppConfig.ORDERSTATUS_MAIN, false));
                        }

                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                Toast.makeText(getActivity(), "load", Toast.LENGTH_SHORT).show();

            }
        });

        mask = root.findViewById(R.id.mask);
        chooseType = (DropdownButton) root.findViewById(R.id.chooseType);
        chooseMulti = (DropdownButton) root.findViewById(R.id.chooseMulti);
        chooseMoney = (DropdownButton) root.findViewById(R.id.chooseMoney);
        chooseSelect = (DropdownButton) root.findViewById(R.id.chooseSelect);
        dropdownType = (DropdownListView) root.findViewById(R.id.dropdownType);
        dropdownMulti = (DropdownListView) root.findViewById(R.id.dropdownMulti);
        dropdownMoney = (DropdownListView) root.findViewById(R.id.dropdownMoney);
        dropdownSelect = (DropdownListView) root.findViewById(R.id.dropdownSelect);

        dropdown_in = AnimationUtils.loadAnimation(getActivity(), R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_mask_out);

        dropdownButtonsController.init();





        //id count name
//        TopicLabelObject topicLabelObject1 =  new TopicLabelObject(1,1,"Fragment");
//        labels.add(topicLabelObject1);
//        TopicLabelObject topicLabelObject2 =new TopicLabelObject(2,1,"CustomView");
//        labels.add(topicLabelObject2);
//        TopicLabelObject topicLabelObject3 =new TopicLabelObject(2,1,"Service");
//        labels.add(topicLabelObject3);
//        TopicLabelObject topicLabelObject4 =new TopicLabelObject(2,1,"BroadcastReceiver");
//        labels.add(topicLabelObject4);
//        TopicLabelObject topicLabelObject5 =new TopicLabelObject(2,1,"Activity");
//        labels.add(topicLabelObject5);




        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsController.hide();
            }
        });



        return root;
    }


//    public void onClick(View view)
//    {
//        switch (type)
//        {
//            case "需求单":
//                Toast.makeText(getActivity(),"需求单",Toast.LENGTH_SHORT).show();
//            case"自荐单":
//                Toast.makeText(getActivity(),"自荐单",Toast.LENGTH_SHORT).show();
//
//        }
//    }

    private class DropdownButtonsController implements DropdownListView.Container {
        private DropdownListView currentDropdownList;
        private List<DropdownItemObject> datasetType = new ArrayList<>();//类别
        private List<DropdownItemObject> datasetMulti = new ArrayList<>();//智能
        private List<DropdownItemObject> datasetMoney = new ArrayList<>();//赏金
        private List<DropdownItemObject> datasetSelect = new ArrayList<>();//筛选


        @Override
        public void show(DropdownListView view) {
            if (currentDropdownList != null) {
                currentDropdownList.clearAnimation();
                currentDropdownList.startAnimation(dropdown_out);
                currentDropdownList.setVisibility(View.GONE);
                currentDropdownList.button.setChecked(false);
            }
            currentDropdownList = view;
            mask.clearAnimation();
            mask.setVisibility(View.VISIBLE);
            currentDropdownList.clearAnimation();
            currentDropdownList.startAnimation(dropdown_in);
            currentDropdownList.setVisibility(View.VISIBLE);
            currentDropdownList.button.setChecked(true);
            mask.bringToFront();
            currentDropdownList.bringToFront();

        }

        @Override
        public void hide() {
            if (currentDropdownList != null) {
                currentDropdownList.clearAnimation();
                currentDropdownList.startAnimation(dropdown_out);
                currentDropdownList.button.setChecked(false);
                mask.clearAnimation();
                mask.startAnimation(dropdown_mask_out);
            }
            currentDropdownList = null;
        }

        @Override
        public void onSelectionChanged(DropdownListView view) {

            Map<String, String> map = new HashMap<String, String>();
            map.put("Category", dropdownType.current.text.toString().equals("类别") ? "" :URLEncoder.encode(dropdownType.current.text.toString()));
            map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
            map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
            map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
            map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
            map.put("lantitude", tv_latitude.getText().toString());
            map.put("longtitude", tv_longitude.getText().toString());
            map.put("Page", "1");
            map.put("Size", "10");
            map.put("Type", String.valueOf(Type));


            if (view == dropdownType) {

                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(final List<OrderDetails> result) {
                        super.onSuccess(result);

                        listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result,AppConfig.ORDERSTATUS_MAIN));


                    }
                });
            }
            else if ( view == dropdownMulti)
            {
                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(final List<OrderDetails> result) {
                        super.onSuccess(result);

                        listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result,AppConfig.ORDERSTATUS_MAIN));


                    }
                });

            }
            else if(view == dropdownMoney)
            {
                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(final List<OrderDetails> result) {
                        super.onSuccess(result);

                        listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result,AppConfig.ORDERSTATUS_MAIN));


                    }
                });
            }
            else
            {
                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(final List<OrderDetails> result) {
                        super.onSuccess(result);

                        listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result,AppConfig.ORDERSTATUS_MAIN));


                    }
                });
            }

        }

        void reset() {
            chooseType.setChecked(false);
            chooseMulti.setChecked(false);
            chooseMoney.setChecked(false);
            chooseSelect.setChecked(false);

            dropdownType.setVisibility(View.GONE);
            dropdownMulti.setVisibility(View.GONE);
            dropdownMoney.setVisibility(View.GONE);
            dropdownSelect.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownType.clearAnimation();
            dropdownMulti.clearAnimation();
            dropdownMoney.clearAnimation();
            dropdownSelect.clearAnimation();
            mask.clearAnimation();

            datasetType.clear();
            datasetMulti.clear();
            datasetMoney.clear();
            datasetSelect.clear();
        }

        void init() {
            reset();
            //类别
            datasetType.add(new DropdownItemObject(TYPE_ALL, ID_TYPE_ALL, "ALL"));
            datasetType.add(new DropdownItemObject(TYPE_CLOSE, ID_TYPE_CLOSE, "CLOSE"));
            datasetType.add(new DropdownItemObject(TYPE_FOOD, ID_TYPE_FOOD, "FOOD"));
            datasetType.add(new DropdownItemObject(TYPE_HOUSE, ID_TYPE_HOUSE, "HOUSE"));
            datasetType.add(new DropdownItemObject(TYPE_WALK, ID_TYPE_WALK, "WALK"));
            datasetType.add(new DropdownItemObject(TYPE_ACADEMIC, ID_TYPE_ACADEMIC, "ACADEMIC"));
            datasetType.add(new DropdownItemObject(TYPE_WORK, ID_TYPE_WORK, "WORK"));
            datasetType.add(new DropdownItemObject(TYPE_LIFE, ID_TYPE_LIFE, "LIFE"));
            datasetType.add(new DropdownItemObject(TYPE_PERSON, ID_TYPE_PERSON, "PERSON"));
            datasetType.add(new DropdownItemObject(TYPE_SECOND, ID_TYPE_SECOND, "SECOND"));
            datasetType.add(new DropdownItemObject(TYPE_SHARE, ID_TYPE_SHARE, "SHARE"));
            datasetType.add(new DropdownItemObject(TYPE_COOP, ID_TYPE_COOP, "COOP"));
            datasetType.add(new DropdownItemObject(TYPE_ACTIVITY, ID_TYPE_ACTIVITY, "ACTIVITY"));

            dropdownType.bind(datasetType, chooseType, this, ID_TYPE_ALL,0);

            //智能
            datasetMulti.add(new DropdownItemObject(MULTI_ALL, ID_MULTI_ALL,"ALL"));
            datasetMulti.add(new DropdownItemObject(MULTI_DISTANCE, ID_MULTI_DISTANCE,"DISTANCE"));
            datasetMulti.add(new DropdownItemObject(MULTI_INTEGRITY, ID_MULTI_INTEGRITY,"INTEGRITY"));
            datasetMulti.add(new DropdownItemObject(MULTI_TIME, ID_MULTI_TIME, "TIME"));
            datasetMulti.add(new DropdownItemObject(MULTI_Ass_TIME, ID_MULTI_Ass_TIME, "AssTIME"));

            dropdownMulti.bind(datasetMulti, chooseMulti, this, ID_MULTI_ALL,0);

            //赏金
            datasetMoney.add(new DropdownItemObject(MONEY_ALL, ID_MONEY_ALL,"ALL"));
            datasetMoney.add(new DropdownItemObject(MONEY_MONEY, ID_MONEY_MONEY,"MONEY"));
            datasetMoney.add(new DropdownItemObject(MONEY_MONEY_LOW, ID_MONEY_MONEY_LOW,"MONEYLOW"));
            //datasetMoney.add(new DropdownItemObject(MONEY_TIME, ID_MONEY_TIME, "TIME"));

            dropdownMoney.bind(datasetMoney, chooseMoney, this, ID_MONEY_ALL,0);

            //筛选
            //datasetSelect.add(new DropdownItemObject(SELECT_ALL, ID_SELECT_ALL,"SELECT"));
            datasetSelect.add(new DropdownItemObject(SELECT_FIRST, ID_SELECT_FIRST,"DISTANCE"));
            datasetSelect.add(new DropdownItemObject(SELECT_SECOND, ID_SELECT_SECOND,"SECOND"));
            datasetSelect.add(new DropdownItemObject(SELECT_BOTH, ID_SELECT_BOTH, "BOTH"));
            datasetSelect.add(new DropdownItemObject(SELECT_OFFLINE, ID_SELECT_OFFLINE, "OFFLINE"));
            datasetSelect.add(new DropdownItemObject(SELECT_ONLINE, ID_SELECT_ONLINE, "ONLINE"));

            dropdownSelect.bind(datasetSelect, chooseSelect, this, ID_SELECT_ALL,1);

//            datasetAllLabel.add(new DropdownItemObject(LABEL_ALL, ID_LABEL_ALL, null) {
//                @Override
//                public String getSuffix() {
//                    return dropdownType.current == null ? "" : dropdownType.current.getSuffix();
//                }
//            });
//            datasetMyLabel.add(new DropdownItemObject(LABEL_ALL, ID_LABEL_ALL, null));
//            datasetLabel = datasetAllLabel;
//            dropdownLabel.bind(datasetLabel, chooseLabel, this, ID_LABEL_ALL);


            dropdown_mask_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentDropdownList == null) {
                        reset();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

//        private List<DropdownItemObject> getCurrentLabels() {
//            return dropdownType.current != null && dropdownType.current.id == ID_TYPE_MY ? datasetMyLabel : datasetAllLabel;
//        }
//
//        void updateLabels(List<DropdownItemObject> targetList) {
//            if (targetList == getCurrentLabels()) {
//                datasetLabel = targetList;
//                dropdownLabel.bind(datasetLabel, chooseLabel, this, dropdownLabel.current.id);
//            }
//        }
//
//
//
//        public void flushCounts() {
//
//            datasetType.get(ID_TYPE_ALL).setSuffix(" (" + "5" + ")");
//            datasetType.get(ID_TYPE_MY).setSuffix(" (" + "3" + ")");
//            dropdownType.flush();
//            dropdownLabel.flush();
//        }
//
//        void flushAllLabels() {
//            flushLabels(datasetAllLabel);
//        }
//
//        void flushMyLabels() {
//            flushLabels(datasetMyLabel);
//        }
//
//        private void flushLabels(List<DropdownItemObject> targetList) {
//
//            while (targetList.size() > 1) targetList.remove(targetList.size() - 1);
//            for (int i = 0, n = 5; i < n; i++) {
//
//                int id = labels.get(i).getId();
//                String name = labels.get(i).getName();
//                if (TextUtils.isEmpty(name)) continue;
//                int topicsCount = labels.get(i).getCount();
//                // 只有all才做0数量过滤，因为my的返回数据总是0
//                if (topicsCount == 0 && targetList == datasetAllLabel) continue;
//                DropdownItemObject item = new DropdownItemObject(name, id, String.valueOf(id));
//                if (targetList == datasetAllLabel)
//                    item.setSuffix("(5)");
//                targetList.add(item);
//            }
//            updateLabels(targetList);
//        }
    }

    @Override
    public void createOptionsMenu(Menu menu) {
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_main,menu);
//        MenuItem addItem = menu.findItem(R.id.action_add);
        super.createOptionsMenu(menu);
    }

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

            Button btn_scan = (Button) view
                    .findViewById(R.id.item_popupwindows_scan);
            Button btn_require = (Button) view
                    .findViewById(R.id.item_popupwindows_send_require_order);
            Button btn_recommand = (Button) view
                    .findViewById(R.id.item_popupwindows_send_recommand_order);
            Button btn_search = (Button) view
                    .findViewById(R.id.item_popupwindows_search);
            Button btn_feedback = (Button) view
                    .findViewById(R.id.item_popupwindows_feedback);
            btn_scan.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    ((FragmentActivity)getActivity()).startActivityForResult(intent, SCAN_CODE);
                    dismiss();
                }
            });
            btn_require.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent();
                    intent.putExtra("demand","demand");
                    intent.setClass(getActivity(), OrderSendActivity.class);
                    getActivity().startActivity(intent);
                    dismiss();
                }
            });
            btn_recommand.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent();
                    intent.putExtra("self","self");
                    intent.setClass(getActivity(), OrderSendActivity.class);
                    getActivity().startActivity(intent);
                    dismiss();
                }
            });

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
}
