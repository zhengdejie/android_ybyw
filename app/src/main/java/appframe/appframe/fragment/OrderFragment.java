package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.widget.dropdownmenu.DropdownButton;
import appframe.appframe.widget.dropdownmenu.DropdownItemObject;
import appframe.appframe.widget.dropdownmenu.DropdownListView;
import appframe.appframe.widget.dropdownmenu.TopicLabelObject;
import appframe.appframe.widget.swiperefresh.SwipeRefreshListViewAdapater;
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
    private static final String MULTI_ALL = "智能";
    private static final String MULTI_DISTANCE = "距离最近";
    private static final String MULTI_INTEGRITY = "诚信度最高";
    private static final String MULTI_TIME = "发布时间";
    //赏金
    private static final int ID_MONEY_ALL = 100;
    private static final int ID_MONEY_MONEY = 101;
    private static final int ID_MONEY_TIME = 102;
    private static final String MONEY_ALL = "赏金";
    private static final String MONEY_MONEY = "任务赏金高";
    private static final String MONEY_TIME = "任务时间长";
    //筛选
    private static final int ID_SELECT_ALL = 150;
    private static final int ID_SELECT_FIRST = 151;
    private static final int ID_SELECT_SECOND = 152;
    private static final int ID_SELECT_BOTH = 153;
    private static final int ID_SELECT_OFFLINE = 154;
    private static final int ID_SELECT_ONLINE = 155;
    private static final String SELECT_ALL = "筛选";
    private static final String SELECT_FIRST = "一度朋友";
    private static final String SELECT_SECOND = "二度朋友";
    private static final String SELECT_BOTH = "一度和二度朋友";
    private static final String SELECT_OFFLINE = "线下支付";
    private static final String SELECT_ONLINE = "线上支付";

    String type;
    ListView listView;
    View mask;
    DropdownButton chooseType, chooseMulti, chooseMoney, chooseSelect;
    DropdownListView dropdownType, dropdownMulti, dropdownMoney, dropdownSelect;
    SwipeRefreshX swipeRefresh;
    Animation dropdown_in, dropdown_out, dropdown_mask_out;


    private List<TopicLabelObject> labels = new ArrayList<>();

    private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();

    public static OrderFragment newInstance(String type) {

        OrderFragment fragment = new OrderFragment();
        fragment.type = type;
        return fragment;
    }

//    @Override
//    protected void onLoadData() {
//        super.onLoadData();
//        Toast.makeText(getActivity(),type,Toast.LENGTH_SHORT).show();
//    }

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order,null);

//        // 模拟一些数据
//        final List<String> datas = new ArrayList<String>();
//        for (int i = 0; i < 20; i++) {
//            datas.add("item - " + i);
//        }
//
//        // 构造适配器
//        final BaseAdapter adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1,
//                datas);
//        // 获取listview实例
//        listView = (ListView) root.findViewById(R.id.lv_order);
//        listView.setAdapter(adapter);
//
//        // 获取RefreshLayout实例
//        final SwipeRefreshX myRefreshListView = (SwipeRefreshX)
//                root.findViewById(R.id.swipeRefresh);
//        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
//        myRefreshListView.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        // 设置下拉刷新监听器
//        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//
//                Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();
//
//                myRefreshListView.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // 更新数据
//                        datas.add(new Date().toGMTString());
//                        adapter.notifyDataSetChanged();
//                        // 更新完后调用该方法结束刷新
//                        myRefreshListView.setRefreshing(false);
//                    }
//                }, 1000);
//            }
//        });
//        // 加载监听器
//        myRefreshListView.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {
//
//            @Override
//            public void onLoad() {
//
//                Toast.makeText(getActivity(), "load", Toast.LENGTH_SHORT).show();
//
//                myRefreshListView.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        datas.add(new Date().toGMTString());
//                        adapter.notifyDataSetChanged();
//                        // 加载完后调用该方法
//                        myRefreshListView.setLoading(false);
//                    }
//                }, 1500);
//
//            }
//        });

        swipeRefresh = (SwipeRefreshX)root.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView) root.findViewById(R.id.lv_order);
//        //ArrayAdapter mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatas);
//        //listView.setAdapter(mAdapter);
        listView.setAdapter(new SwipeRefreshListViewAdapater(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "df", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
            }
        });

        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();

//                myRefreshListView.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // 更新数据
//                        datas.add(new Date().toGMTString());
//                        adapter.notifyDataSetChanged();
//                        // 更新完后调用该方法结束刷新
//                        myRefreshListView.setRefreshing(false);
//                    }
//                }, 1000);
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


//        dropdownButtonsController.flushCounts();
//        dropdownButtonsController.flushAllLabels();
//        dropdownButtonsController.flushMyLabels();

//        View root = inflater.inflate(R.layout.fragment_order, null);
//        test_button = root.findViewById(R.id.test_button);
//        UploadContact_button = root.findViewById(R.id.UploadContact_button);
//        test_button.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                UriHandler.openWebActivity(getActivity(), "/public/js/webframe-test.html");
//            }
//        });
//        UploadContact_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Cursor cursor = null;
//                List<UserContact> contactsList = new ArrayList<UserContact>();
//                try {
//                    //Get Contacts
//                    Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//                    while (cursor.moveToNext()) {
//                        //At least one phone number
//                        if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0)
//                        {
//                            int ContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                            String ContactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                            Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactID, null, null);
//                            while(cursorPhone.moveToNext())
//                            {
//                                String ContactMobile = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                Pattern pattern = Pattern.compile("^\\d{11}$");
//                                Matcher matcher = pattern.matcher(ContactMobile);
//                                if(matcher.matches()) {
//                                    UserContact userContact = new UserContact(ContactName, ContactMobile);
//                                    contactsList.add(userContact);
//                                }
//                            }
//                            cursorPhone.close();
//                        }
//
//                    }
//                    cursor.close();
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                /*finally {
//                    if (cursor != null) {
//                        cursor.close();
//                    }
//                    if (cursorPhone != null) {
//                        cursorPhone.close();
//                    }
//                }*/
//                //SharedPreferences sp = getActivity().getSharedPreferences("Auth", getActivity().MODE_PRIVATE);
//
//                Http.request(getActivity(), API.USER_CONTACT_UPLOAD, Http.map(
//                        "Mobile", String.valueOf(Auth.getCurrentUserMobile()),
//                        "UserContact", GsonHelper.getGson().toJson(contactsList)
//                ), new Http.RequestListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult result) {
//                        super.onSuccess(result);
//                        //
//
//
//                    }
//                });
//            }
//        });
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
//            if (view == dropdownType) {
//                updateLabels(getCurrentLabels());
//            }

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

            dropdownType.bind(datasetType, chooseType, this, ID_TYPE_ALL);

            //智能
            datasetMulti.add(new DropdownItemObject(MULTI_ALL, ID_MULTI_ALL,"ALL"));
            datasetMulti.add(new DropdownItemObject(MULTI_DISTANCE, ID_MULTI_DISTANCE,"DISTANCE"));
            datasetMulti.add(new DropdownItemObject(MULTI_INTEGRITY, ID_MULTI_INTEGRITY,"INTEGRITY"));
            datasetMulti.add(new DropdownItemObject(MULTI_TIME, ID_MULTI_TIME, "TIME"));

            dropdownMulti.bind(datasetMulti, chooseMulti, this, ID_MULTI_ALL);

            //赏金
            datasetMoney.add(new DropdownItemObject(MONEY_ALL, ID_MONEY_ALL,"ALL"));
            datasetMoney.add(new DropdownItemObject(MONEY_MONEY, ID_MONEY_MONEY,"MONEY"));
            datasetMoney.add(new DropdownItemObject(MONEY_TIME, ID_MONEY_TIME, "TIME"));

            dropdownMoney.bind(datasetMoney, chooseMoney, this, ID_MONEY_ALL);

            //筛选
            datasetSelect.add(new DropdownItemObject(SELECT_ALL, ID_SELECT_ALL,"SELECT"));
            datasetSelect.add(new DropdownItemObject(SELECT_FIRST, ID_SELECT_FIRST,"DISTANCE"));
            datasetSelect.add(new DropdownItemObject(SELECT_SECOND, ID_SELECT_SECOND,"SECOND"));
            datasetSelect.add(new DropdownItemObject(SELECT_BOTH, ID_SELECT_BOTH, "BOTH"));
            datasetSelect.add(new DropdownItemObject(SELECT_OFFLINE, ID_SELECT_OFFLINE, "OFFLINE"));
            datasetSelect.add(new DropdownItemObject(SELECT_ONLINE, ID_SELECT_ONLINE, "ONLINE"));

            dropdownSelect.bind(datasetSelect, chooseSelect, this, ID_SELECT_ALL);

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
}
