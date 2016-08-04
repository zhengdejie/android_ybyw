package appframe.appframe.fragment;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyRep;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.AddFriendsActivity;
import appframe.appframe.activity.FeedbackActivity;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.activity.OrderSendActivity;
import appframe.appframe.activity.QuestionDetailsActivity;
import appframe.appframe.activity.SearchActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.dto.OrderCategory;
import appframe.appframe.dto.OrderDetailAndCount;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
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
import appframe.appframe.widget.swiperefresh.SwipeRefreshXQuestionAdapater;

/**
 * Created by dashi on 15/6/20.
 */
public class OrderFragment extends BaseFragment  {

    View test_button,UploadContact_button;
    private List<DropdownItemObject> datasetType = new ArrayList<>();//全部分类
    LinearLayout progress_bar,tabs,question_tabs;
    String locationCity;
    double latitude =0.0, longitude = 0.0;
    int OrderCount = 0;
    //全部分类
    private static final int ID_TYPE_ALL = 0;
//    private static final int ID_TYPE_CLOSE = 1;
//    private static final int ID_TYPE_FOOD = 2;
//    private static final int ID_TYPE_HOUSE = 3;
//    private static final int ID_TYPE_WALK = 4;
//    private static final int ID_TYPE_ACADEMIC = 5;
//    private static final int ID_TYPE_WORK = 6;
//    private static final int ID_TYPE_LIFE = 7;
//    private static final int ID_TYPE_PERSON = 8;
//    private static final int ID_TYPE_SECOND = 9;
//    private static final int ID_TYPE_SHARE = 10;
//    private static final int ID_TYPE_COOP = 11;
//    private static final int ID_TYPE_ACTIVITY = 12;
    private static final String TYPE_ALL = "全部分类";
//    private static final String TYPE_CLOSE = "衣";
//    private static final String TYPE_FOOD = "食";
//    private static final String TYPE_HOUSE = "住";
//    private static final String TYPE_WALK = "行";
//    private static final String TYPE_ACADEMIC = "学术/艺术";
//    private static final String TYPE_WORK = "工作/商务";
//    private static final String TYPE_LIFE = "生活/娱乐";
//    private static final String TYPE_PERSON = "找人";
//    private static final String TYPE_SECOND = "二手/转让";
//    private static final String TYPE_SHARE = "资源共享";
//    private static final String TYPE_COOP = "合作/推广";
//    private static final String TYPE_ACTIVITY = "活动";
    //智能
    private static final int ID_MULTI_ALL = 50;
    private static final int ID_MULTI_DISTANCE = 51;
    private static final int ID_MULTI_INTEGRITY = 52;
    private static final int ID_MULTI_TIME = 53;
    private static final int ID_MULTI_Ass_TIME = 54;
    private static final String MULTI_ALL = "默认排序";
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

    //问题默认排序
    private static final int ID_QUESTION_ALL = 200;
    private static final int ID_QUESTION_MONEY = 201;
    private static final int ID_QUESTION_MONEY_LOW = 202;
    private static final String QUESTION_ALL = "默认排序";
    private static final String QUESTION_MONEY = "赏金最高";
    private static final String QUESTION_MONEY_LOW = "赏金最低";

    //问题全部排序
    private static final int ID_QUESTION_STATUS_ALL = 250;
    private static final int ID_QUESTION_STATUS = 251;
    private static final int ID_QUESTION_STATUSED = 252;
    private static final String QUESTION_STATUS_ALL = "全部";
    private static final String QUESTION_STATUS = "悬赏中";
    private static final String QUESTION_STATUSED = "已赏";
    //问题筛选
    private static final int ID_QUESTION_SELECT_ALL = 300;
    private static final int ID_QUESTION_SELECT_FIRST = 301;
    private static final int ID_QUESTION_SELECT_SECOND = 302;
    private static final int ID_QUESTION_SELECT_BOTH = 303;


    private static final String QUESTION_SELECT_FIRST = "一度朋友";
    private static final String QUESTION_SELECT_SECOND = "二度朋友";
    private static final String QUESTION_SELECT_BOTH = "陌生人";


    public static final int SCAN_CODE = 1;
    BDLocation bdLocation = new BDLocation();
    int Type = 2, Page =1;  //1=老板单， 2=打工单
    ListView listView;
    View mask;
    DropdownButton chooseType, chooseMulti, chooseMoney, chooseSelect,chooseQuestionMoney,chooseQuestionStatus,chooseQuestionSelect;
    DropdownListView dropdownType, dropdownMulti, dropdownMoney, dropdownSelect,dropdownQeustionMoney,dropdownQuestionStatus,dropdownQuestionSelect;
    SwipeRefreshX swipeRefresh;
    SwipeRefreshXOrderAdapater hasTopAdapater,hasnotTopAdapater;
    SwipeRefreshXQuestionAdapater swipeRefreshXQuestionAdapater;
    Animation dropdown_in, dropdown_out, dropdown_mask_out;
    View root;
    TextView tv_back,tv_action,tv_require,tv_recommand,tv_question,tv_empty;
//    Button tv_require,tv_recommand;
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

    protected String TransferQuestionFriendsFilter(List<DropdownItemObject> DDIO)
    {
        StringBuilder friendsFilter = new StringBuilder();
        for(DropdownItemObject currentMulti : dropdownQuestionSelect.currentMulti)
        {
            if(currentMulti.id == ID_QUESTION_SELECT_FIRST) {
                friendsFilter.append("," + currentMulti.text);
            }
            if(currentMulti.id == ID_QUESTION_SELECT_SECOND) {
                friendsFilter.append("," + currentMulti.text);
            }
            if(currentMulti.id == ID_QUESTION_SELECT_BOTH) {
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

    public Activity getActivityFromFragment()
    {
        return getActivity();
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

    protected  List<OrderDetails> getOrdersRemoveTopOrder(List<OrderDetails> result,OrderDetails topOrder)
    {
        OrderDetails tempOrder = new OrderDetails();
        for(OrderDetails od : result)
        {
            if(od.getId() == topOrder.getId())
            {
                tempOrder = od;
            }
        }
        Log.i("tempOrder.getId()",String.valueOf(tempOrder.getId()));
        if(tempOrder.getId() != 0) {
            result.remove(tempOrder);
        }

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

//        tv_back = (TextView)root.findViewById(R.id.tv_back);
        tv_require = (TextView)root.findViewById(R.id.tv_require);
        tv_recommand = (TextView)root.findViewById(R.id.tv_recommand);
        tv_empty = (TextView)root.findViewById(R.id.tv_empty);
        tv_question = (TextView)root.findViewById(R.id.tv_question);
        tv_action = (TextView)root.findViewById(R.id.tv_action);
        progress_bar = (LinearLayout)root.findViewById(R.id.progress_bar);
        tabs = (LinearLayout)root.findViewById(R.id.tabs);
        question_tabs = (LinearLayout)root.findViewById(R.id.question_tabs);

        BaiduLocation baiduLocation = new BaiduLocation(getActivity(),new MyLocationListener());
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
                dropdownButtonsController.hide();
                tabs.setVisibility(View.VISIBLE);
                question_tabs.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
                tv_empty.setText("现在还没有人发单，您快去发一单吧");
                listView.setAdapter(null);
                Type = 2;
                Page = 1;
                tv_require.setBackgroundResource(R.drawable.titlebar_order_left_selected);
                tv_require.setTextColor(Color.rgb(37, 37, 37));
                tv_recommand.setBackgroundResource(R.drawable.titlebar_order_center_unselected);
                tv_recommand.setTextColor(Color.rgb(255, 255, 255));
                tv_question.setBackgroundResource(R.drawable.titlebar_order_right_unselected);
                tv_question.setTextColor(Color.rgb(255, 255, 255));
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", String.valueOf(Type));
                map.put("Category", dropdownType.current == null ? "" : dropdownType.current.text.toString().equals("全部分类") ? "" : String.valueOf(dropdownType.current.id)); //URLEncoder.encode(dropdownType.current.text.toString())
                map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
                map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
                map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
                map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
                map.put("lantitude", String.valueOf(latitude));
                map.put("longtitude", String.valueOf(longitude));


                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderDetailAndCount result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);

                        if (result != null) {
                            OrderCount = result.getCount();
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true, Type);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false, Type);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            }
                        } else {
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> od = new ArrayList<OrderDetails>();
                                od.add(topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

            }
        });

        tv_recommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsController.hide();
                tabs.setVisibility(View.VISIBLE);
                question_tabs.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
                tv_empty.setText("现在还没有人发单，您快去发一单吧");
                listView.setAdapter(null);
                Type = 1;
                Page = 1;
                tv_require.setBackgroundResource(R.drawable.titlebar_order_left_unselected);
                tv_require.setTextColor(Color.rgb(255, 255, 255));
                tv_recommand.setBackgroundResource(R.drawable.titlebar_order_center_selected);
                tv_recommand.setTextColor(Color.rgb(37, 37, 37));
                tv_question.setBackgroundResource(R.drawable.titlebar_order_right_unselected);
                tv_question.setTextColor(Color.rgb(255, 255, 255));
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("Type", String.valueOf(Type));
                map.put("Category", dropdownType.current == null ? "" : dropdownType.current.text.toString().equals("全部分类") ? "" :String.valueOf(dropdownType.current.id));
                map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
                map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
                map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
                map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
                map.put("lantitude", String.valueOf(latitude));
                map.put("longtitude", String.valueOf(longitude));



                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                    @Override
                    public void onSuccess(OrderDetailAndCount result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            OrderCount = result.getCount();

                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true,Type);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false,Type);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.GONE);
                            }
                        } else {
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> od = new ArrayList<OrderDetails>();
                                od.add(topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

            }
        });

        tv_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsController.hide();
                tabs.setVisibility(View.GONE);
                question_tabs.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.VISIBLE);
                tv_empty.setText("现在还没有人提问，您快去提个问吧");
                listView.setAdapter(null);
                Type = 3;
                Page = 1;
                tv_require.setBackgroundResource(R.drawable.titlebar_order_left_unselected);
                tv_require.setTextColor(Color.rgb(255, 255, 255));
                tv_recommand.setBackgroundResource(R.drawable.titlebar_order_center_unselected);
                tv_recommand.setTextColor(Color.rgb(255, 255, 255));
                tv_question.setBackgroundResource(R.drawable.titlebar_order_right_selected);
                tv_question.setTextColor(Color.rgb(37, 37, 37));
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                map.put("FriendShip",URLEncoder.encode(TransferQuestionFriendsFilter(dropdownQuestionSelect.currentMulti)));
                map.put("AcceptedAnswer",dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS_ALL) ? "" : dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS) ? "false" : "true");
                map.put("SortByPriceHighToLow",dropdownQeustionMoney.current.text.toString().equals(QUESTION_ALL) ? "" : dropdownQeustionMoney.current.text.toString().equals(QUESTION_MONEY) ? "false":"true");


                Http.request(getActivity(), API.GET_QUESTION, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<Question>>() {
                    @Override
                    public void onSuccess(List<Question> result) {
                        super.onSuccess(result);
                        swipeRefresh.setRefreshing(false);
                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            swipeRefreshXQuestionAdapater = new SwipeRefreshXQuestionAdapater(getActivity(),result);
                            listView.setAdapter(swipeRefreshXQuestionAdapater);

                        }
                        if (result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        } else {
                            tv_empty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setRefreshing(false);
                        progress_bar.setVisibility(View.GONE);
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
                Bundle bundle = new Bundle();
                if(Type == 3)
                {
                    intent.setClass(getActivity(), QuestionDetailsActivity.class);
                    Question questionDetails = (Question) parent.getAdapter().getItem(position);
                    bundle.putSerializable("Question", questionDetails);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {

                    intent.setClass(getActivity(), OrderDetailsActivity.class);
                    OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);

                    bundle.putSerializable("OrderDetails", orderDetails);
                    if (hasTopOrder() && orderDetails.getId() == getTopOrder().getId()) {
                        bundle.putString("hasTopOrder", "1");
                    }
                    bundle.putString("From", "Order");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (Type == 3) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Page", "1");
                    map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                    map.put("FriendShip",URLEncoder.encode(TransferFriendsFilter(dropdownQuestionSelect.currentMulti)));
                    map.put("AcceptedAnswer",dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS_ALL) ? "" : dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS) ? "false" : "true");
                    map.put("SortByPriceHighToLow",dropdownQeustionMoney.current.text.toString().equals(QUESTION_ALL) ? "" : dropdownQeustionMoney.current.text.toString().equals(QUESTION_MONEY) ? "false":"true");

                    Http.request(getActivity(), API.GET_QUESTION, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<Question>>() {
                        @Override
                        public void onSuccess(List<Question> result) {
                            super.onSuccess(result);
                            Page = 1;
                            swipeRefresh.setRefreshing(false);
//                            progress_bar.setVisibility(View.GONE);
                            if (result != null) {

                                swipeRefreshXQuestionAdapater = new SwipeRefreshXQuestionAdapater(getActivity(), result);
                                listView.setAdapter(swipeRefreshXQuestionAdapater);
                                if (result != null && result.size() != 0) {
                                    tv_empty.setVisibility(View.GONE);
                                } else {
                                    tv_empty.setVisibility(View.VISIBLE);
                                }
                            }

                        }

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
                            swipeRefresh.setRefreshing(false);
//                            progress_bar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Page", "1");
                    map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                    map.put("Type", String.valueOf(Type));
                    map.put("Category", dropdownType.current == null ? "" : dropdownType.current.text.toString().equals(TYPE_ALL) ? "" : String.valueOf(dropdownType.current.id));
                    map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
                    map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
                    map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
                    map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
                    map.put("lantitude", String.valueOf(latitude));
                    map.put("longtitude", String.valueOf(longitude));


                    Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                        @Override
                        public void onSuccess(final OrderDetailAndCount result) {
                            super.onSuccess(result);
                            if (result != null) {
                                OrderCount = result.getCount();
                                if (hasTopOrder()) {
                                    OrderDetails topOrder = getTopOrder();
                                    List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
                                    hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true, Type);
                                    listView.setAdapter(hasTopAdapater);
                                    tv_empty.setVisibility(View.GONE);

                                } else {
                                    hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false,Type);
                                    listView.setAdapter(hasnotTopAdapater);
                                    tv_empty.setVisibility(View.GONE);
                                }
                            } else {
                                if (hasTopOrder()) {
                                    OrderDetails topOrder = getTopOrder();
                                    List<OrderDetails> od = new ArrayList<OrderDetails>();
                                    od.add(topOrder);
                                    hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
                                    listView.setAdapter(hasTopAdapater);
                                    tv_empty.setVisibility(View.GONE);

                                } else {
                                    hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
                                    listView.setAdapter(hasnotTopAdapater);
                                    tv_empty.setVisibility(View.VISIBLE);
                                }
                            }
                            Page = 1;
                            swipeRefresh.setRefreshing(false);
                        }

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                }

            } //onRefresh
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {
                if(Type == 3)
                {
                    Page++;
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Page", String.valueOf(Page));
                    map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                    map.put("FriendShip",URLEncoder.encode(TransferFriendsFilter(dropdownQuestionSelect.currentMulti)));
                    map.put("AcceptedAnswer",dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS_ALL) ? "" : dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS) ? "false" : "true");
                    map.put("SortByPriceHighToLow",dropdownQeustionMoney.current.text.toString().equals(QUESTION_ALL) ? "" : dropdownQeustionMoney.current.text.toString().equals(QUESTION_MONEY) ? "false":"true");

                    Http.request(getActivity(), API.GET_QUESTION, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<Question>>() {
                        @Override
                        public void onSuccess(List<Question> result) {
                            super.onSuccess(result);
//                            progress_bar.setVisibility(View.GONE);
                            if (result != null) {
                                loadMoreQuestion(swipeRefreshXQuestionAdapater,result);
//                                listView.setAdapter(new SwipeRefreshXQuestionAdapater(getActivity(),result));

                            }
                            swipeRefresh.setLoading(false);
                        }

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
//                            progress_bar.setVisibility(View.GONE);
                            swipeRefresh.setLoading(false);
                        }
                    });
                }
                else {
                    Page++;
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Page", String.valueOf(Page));
                    map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                    map.put("Type", String.valueOf(Type));
                    map.put("Category", dropdownType.current == null ? "" : dropdownType.current.text.toString().equals("全部分类") ? "" : String.valueOf(dropdownType.current.id));
                    map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
                    map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
                    map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
                    map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
                    map.put("lantitude", String.valueOf(latitude));
                    map.put("longtitude", String.valueOf(longitude));
                    map.put("orderCount", String.valueOf(OrderCount));


                    Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                        @Override
                        public void onSuccess(final OrderDetailAndCount result) {
                            super.onSuccess(result);

                            if (result != null) {

                                if (hasTopOrder()) {
                                    OrderDetails topOrder = getTopOrder();
                                    List<OrderDetails> list_OD = getOrdersRemoveTopOrder(result.getOrderList(), topOrder);
                                    loadMore(hasTopAdapater, list_OD);


                                } else {
                                    loadMore(hasnotTopAdapater, result.getOrderList());

                                }
                            } else {
//                                Toast.makeText(getActivity(), "没有更多信息", Toast.LENGTH_SHORT).show();

                            }
                            swipeRefresh.setLoading(false);

                        }

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
                            swipeRefresh.setLoading(false);
                        }
                    });
                }
            }//onLoad
        });

        mask = root.findViewById(R.id.mask);
        chooseType = (DropdownButton) root.findViewById(R.id.chooseType);
        chooseMulti = (DropdownButton) root.findViewById(R.id.chooseMulti);
        chooseMoney = (DropdownButton) root.findViewById(R.id.chooseMoney);
        chooseSelect = (DropdownButton) root.findViewById(R.id.chooseSelect);
        chooseQuestionMoney = (DropdownButton) root.findViewById(R.id.chooseQuestionMoney);
        chooseQuestionStatus = (DropdownButton) root.findViewById(R.id.chooseQuestionStatus);
        chooseQuestionSelect = (DropdownButton) root.findViewById(R.id.chooseQuestionSelect);
        dropdownType = (DropdownListView) root.findViewById(R.id.dropdownType);
        dropdownMulti = (DropdownListView) root.findViewById(R.id.dropdownMulti);
        dropdownMoney = (DropdownListView) root.findViewById(R.id.dropdownMoney);
        dropdownSelect = (DropdownListView) root.findViewById(R.id.dropdownSelect);
        dropdownQeustionMoney = (DropdownListView) root.findViewById(R.id.dropdownQeustionMoney);
        dropdownQuestionStatus = (DropdownListView) root.findViewById(R.id.dropdownQuestionStatus);
        dropdownQuestionSelect = (DropdownListView) root.findViewById(R.id.dropdownQuestionSelect);

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

    private void loadMore(SwipeRefreshXOrderAdapater adapater, List<OrderDetails> orderDetailses) {
        adapater.addItems(orderDetailses);
    }
    private void loadMoreQuestion(SwipeRefreshXQuestionAdapater adapater, List<Question> questionList) {
        adapater.addItems(questionList);
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

        private List<DropdownItemObject> datasetMulti = new ArrayList<>();//智能
        private List<DropdownItemObject> datasetMoney = new ArrayList<>();//赏金
        private List<DropdownItemObject> datasetSelect = new ArrayList<>();//筛选
        private List<DropdownItemObject> datasetQuestion = new ArrayList<>();//问题默认
        private List<DropdownItemObject> datasetQuestionMoney= new ArrayList<>();//问题赏金
        private List<DropdownItemObject> datasetQuestionSelect = new ArrayList<>();//问题筛选


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
            listView.setAdapter(null);
            progress_bar.setVisibility(View.VISIBLE);
            Map<String, String> map = new HashMap<String, String>();
            map.put("Category", dropdownType.current== null ? "" : dropdownType.current.text.toString().equals("全部分类") ? "" :String.valueOf(dropdownType.current.id));
            map.put("OrderBy", String.valueOf(TransferOrderBy(dropdownMulti.current.text)));
            map.put("Bounty", String.valueOf(TransferMoney(dropdownMoney.current.text)));
            map.put("FriendsFilter", URLEncoder.encode(TransferFriendsFilter(dropdownSelect.currentMulti)));
            map.put("PaymentMethodFilter", URLEncoder.encode(TransferPaymentFilter(dropdownSelect.currentMulti)));
            map.put("lantitude", String.valueOf(latitude));
            map.put("longtitude", String.valueOf(longitude));
            map.put("Page", "1");
            map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
            map.put("Type", String.valueOf(Type));


            if (view == dropdownType) {

                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                    @Override
                    public void onSuccess(final OrderDetailAndCount result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            OrderCount = result.getCount();
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true, Type);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false, Type);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            }
                        } else {
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> od = new ArrayList<OrderDetails>();
                                od.add(topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.VISIBLE);
                            }
                        }
//                        if(result != null) {
//                            OrderCount = result.getCount();
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.VISIBLE);
//                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });
            }
            else if ( view == dropdownMulti)
            {
                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                    @Override
                    public void onSuccess(final OrderDetailAndCount result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            OrderCount = result.getCount();
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true, Type);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false, Type);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            }
                        } else {
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> od = new ArrayList<OrderDetails>();
                                od.add(topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.VISIBLE);
                            }
                        }
//                        if(result != null) {
//                            OrderCount = result.getCount();
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.VISIBLE);
//                        }
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

            }
            else if(view == dropdownMoney)
            {
                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                    @Override
                    public void onSuccess(final OrderDetailAndCount result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            OrderCount = result.getCount();
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true, Type);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false, Type);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            }
                        } else {
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> od = new ArrayList<OrderDetails>();
                                od.add(topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.VISIBLE);
                            }
                        }
//                        if(result != null) {
//                            OrderCount = result.getCount();
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.VISIBLE);
//                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });
            }
            else if(view == dropdownSelect)
            {
                Http.request(getActivity(), API.GET_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<OrderDetailAndCount>() {
                    @Override
                    public void onSuccess(final OrderDetailAndCount result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            OrderCount = result.getCount();
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> list_OD = getOrders(result.getOrderList(), topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), list_OD, AppConfig.ORDERSTATUS_MAIN, true, Type);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN, false, Type);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            }
                        } else {
                            if (hasTopOrder()) {
                                OrderDetails topOrder = getTopOrder();
                                List<OrderDetails> od = new ArrayList<OrderDetails>();
                                od.add(topOrder);
                                hasTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), od, AppConfig.ORDERSTATUS_MAIN, true);
                                listView.setAdapter(hasTopAdapater);
                                tv_empty.setVisibility(View.GONE);

                            } else {
                                hasnotTopAdapater = new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN, false);
                                listView.setAdapter(hasnotTopAdapater);
                                tv_empty.setVisibility(View.VISIBLE);
                            }
                        }
//                        if(result != null) {
//                            OrderCount = result.getCount();
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result.getOrderList(), AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), null, AppConfig.ORDERSTATUS_MAIN));
//                            tv_empty.setVisibility(View.VISIBLE);
//                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });
            }
            else if (view == dropdownQeustionMoney || view == dropdownQuestionSelect || view == dropdownQuestionStatus)
            {
                Map<String, String> questionMap = new HashMap<String, String>();
                questionMap.put("Page", "1");
                questionMap.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                questionMap.put("FriendShip",URLEncoder.encode(TransferFriendsFilter(dropdownQuestionSelect.currentMulti)));
                questionMap.put("AcceptedAnswer",dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS_ALL) ? "" : dropdownQuestionStatus.current.text.toString().equals(QUESTION_STATUS) ? "false" : "true");
                questionMap.put("SortByPriceHighToLow",dropdownQeustionMoney.current.text.toString().equals(QUESTION_ALL) ? "" : dropdownQeustionMoney.current.text.toString().equals(QUESTION_MONEY) ? "true":"false");


                Http.request(getActivity(), API.GET_QUESTION, new Object[]{Http.getURL(questionMap)}, new Http.RequestListener<List<Question>>() {
                    @Override
                    public void onSuccess(List<Question> result) {
                        super.onSuccess(result);

                        progress_bar.setVisibility(View.GONE);
                        if (result != null) {
                            swipeRefreshXQuestionAdapater = new SwipeRefreshXQuestionAdapater(getActivity(),result);
                            listView.setAdapter(swipeRefreshXQuestionAdapater);
                            tv_empty.setVisibility(View.GONE);
                        }
                        else
                        {
                            swipeRefreshXQuestionAdapater = new SwipeRefreshXQuestionAdapater(getActivity(),result);
                            listView.setAdapter(swipeRefreshXQuestionAdapater);
                            tv_empty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);

                        progress_bar.setVisibility(View.GONE);
                    }
                });
            }

        }

        void reset() {
            chooseType.setChecked(false);
            chooseMulti.setChecked(false);
            chooseMoney.setChecked(false);
            chooseSelect.setChecked(false);
            chooseQuestionMoney.setChecked(false);
            chooseQuestionStatus.setChecked(false);
            chooseQuestionSelect.setChecked(false);

            dropdownType.setVisibility(View.GONE);
            dropdownMulti.setVisibility(View.GONE);
            dropdownMoney.setVisibility(View.GONE);
            dropdownSelect.setVisibility(View.GONE);
            dropdownQuestionSelect.setVisibility(View.GONE);
            dropdownQeustionMoney.setVisibility(View.GONE);
            dropdownQuestionStatus.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownType.clearAnimation();
            dropdownMulti.clearAnimation();
            dropdownMoney.clearAnimation();
            dropdownSelect.clearAnimation();
            dropdownQeustionMoney.clearAnimation();
            dropdownQuestionStatus.clearAnimation();
            dropdownQuestionSelect.clearAnimation();
            mask.clearAnimation();

            datasetType.clear();
            datasetMulti.clear();
            datasetMoney.clear();
            datasetSelect.clear();
            datasetQuestionMoney.clear();
            datasetQuestion.clear();
            datasetQuestionSelect.clear();
        }

        void init() {
            reset();
            datasetType.add(new DropdownItemObject(TYPE_ALL, ID_TYPE_ALL, "ALL"));
            dropdownType.bind(datasetType, chooseType, DropdownButtonsController.this, ID_TYPE_ALL,0);

//            chooseType.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    datasetType.add(new DropdownItemObject(TYPE_ALL, ID_TYPE_ALL, "ALL"));
//                    Http.request(getActivity(), API.GET_ORDERCATEGORY, new Http.RequestListener<List<OrderCategory>>() {
//                        @Override
//                        public void onSuccess(List<OrderCategory> result) {
//                            super.onSuccess(result);
//                            datasetType.clear();
//                            datasetType.add(new DropdownItemObject(TYPE_ALL, ID_TYPE_ALL, "ALL"));
//                            for (OrderCategory orderCategory : result) {
//                                datasetType.add(new DropdownItemObject(orderCategory.getCategoryName(), orderCategory.getId(), orderCategory.getCategoryName()));
//                            }
//
//                            dropdownType.bind(datasetType, chooseType, DropdownButtonsController.this, ID_TYPE_ALL,0);
//
//                        }
//
//                        @Override
//                        public void onFail(String code) {
//                            super.onFail(code);
//                        }
//                    });
//                }
//            });
            //全部分类
            Http.request(getActivity(), API.GET_ORDERCATEGORY, new Http.RequestListener<List<OrderCategory>>() {
                @Override
                public void onSuccess(List<OrderCategory> result) {
                    super.onSuccess(result);
                    for (OrderCategory orderCategory : result) {
                        datasetType.add(new DropdownItemObject(orderCategory.getCategoryName(), orderCategory.getId(), orderCategory.getCategoryName()));
                    }

                    dropdownType.bind(datasetType, chooseType, DropdownButtonsController.this, ID_TYPE_ALL,0);

                }

                @Override
                public void onFail(String code) {
                    super.onFail(code);
                }
            });

//            datasetType.add(new DropdownItemObject(TYPE_CLOSE, ID_TYPE_CLOSE, "CLOSE"));
//            datasetType.add(new DropdownItemObject(TYPE_FOOD, ID_TYPE_FOOD, "FOOD"));
//            datasetType.add(new DropdownItemObject(TYPE_HOUSE, ID_TYPE_HOUSE, "HOUSE"));
//            datasetType.add(new DropdownItemObject(TYPE_WALK, ID_TYPE_WALK, "WALK"));
//            datasetType.add(new DropdownItemObject(TYPE_ACADEMIC, ID_TYPE_ACADEMIC, "ACADEMIC"));
//            datasetType.add(new DropdownItemObject(TYPE_WORK, ID_TYPE_WORK, "WORK"));
//            datasetType.add(new DropdownItemObject(TYPE_LIFE, ID_TYPE_LIFE, "LIFE"));
//            datasetType.add(new DropdownItemObject(TYPE_PERSON, ID_TYPE_PERSON, "PERSON"));
//            datasetType.add(new DropdownItemObject(TYPE_SECOND, ID_TYPE_SECOND, "SECOND"));
//            datasetType.add(new DropdownItemObject(TYPE_SHARE, ID_TYPE_SHARE, "SHARE"));
//            datasetType.add(new DropdownItemObject(TYPE_COOP, ID_TYPE_COOP, "COOP"));
//            datasetType.add(new DropdownItemObject(TYPE_ACTIVITY, ID_TYPE_ACTIVITY, "ACTIVITY"));



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

            dropdownSelect.bind(datasetSelect, chooseSelect, this, ID_SELECT_ALL, 1);

            //问题默认排序
            datasetQuestion.add(new DropdownItemObject(QUESTION_ALL,ID_QUESTION_ALL,"ALL"));
            datasetQuestion.add(new DropdownItemObject(QUESTION_MONEY,ID_QUESTION_MONEY,"MONEY"));
            datasetQuestion.add(new DropdownItemObject(QUESTION_MONEY_LOW, ID_QUESTION_MONEY_LOW, "MONEYLOW"));
            dropdownQeustionMoney.bind(datasetQuestion, chooseQuestionMoney, this, ID_QUESTION_ALL, 0);

            //问题全部
            datasetQuestionMoney.add(new DropdownItemObject(QUESTION_STATUS_ALL,ID_QUESTION_STATUS_ALL,"STATUSALL"));
            datasetQuestionMoney.add(new DropdownItemObject(QUESTION_STATUS,ID_QUESTION_STATUS,"STATUS"));
            datasetQuestionMoney.add(new DropdownItemObject(QUESTION_STATUSED, ID_QUESTION_STATUSED, "STATUSED"));
            dropdownQuestionStatus.bind(datasetQuestionMoney, chooseQuestionStatus, this, ID_QUESTION_STATUS_ALL, 0);

            //问题筛选
            datasetQuestionSelect.add(new DropdownItemObject(QUESTION_SELECT_FIRST,ID_QUESTION_SELECT_FIRST,"FIRST"));
            datasetQuestionSelect.add(new DropdownItemObject(QUESTION_SELECT_SECOND,ID_QUESTION_SELECT_SECOND,"SECOND"));
            datasetQuestionSelect.add(new DropdownItemObject(QUESTION_SELECT_BOTH,ID_QUESTION_SELECT_BOTH,"BOTH"));
            dropdownQuestionSelect.bind(datasetQuestionSelect,chooseQuestionSelect,this,ID_QUESTION_SELECT_ALL,1);

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
    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            locationCity = location.getAddrStr();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(hasTopAdapater!=null) {
            hasTopAdapater.notifyDataSetChanged();
        }
        if(hasnotTopAdapater!=null) {
            hasnotTopAdapater.notifyDataSetChanged();
        }

//        if(Type == 1)
//        {
//            tv_recommand.performClick();
//
//        }
//        else if(Type == 2)
//        {
//            tv_require.performClick();
//        }
//        else if(Type == 3)
//        {
//            tv_question.performClick();
//        }
//        else
//        {
//
//        }
    }
}
