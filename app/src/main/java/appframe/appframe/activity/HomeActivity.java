package appframe.appframe.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.WxLog;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.igexin.sdk.PushManager;


import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.fragment.BaseFragment;
import appframe.appframe.fragment.DiscoveryFragment;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.fragment.OrderFragment;
import appframe.appframe.fragment.PersonFragment;
import appframe.appframe.fragment.ProfileFragment;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.NotificationInitSampleHelper;
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/20.
 */
public class HomeActivity extends BaseFrameActivity implements View.OnClickListener {

    String[] titles;
    BaseFragment[] fragments;
    public static final int SCAN_CODE = 1;
    private LoginSampleHelper loginHelper;
    private Drawable tv_ubang_Pressed;
    private Drawable tv_ubang_Normal;
    private Drawable tv_myorder_Pressed;
    private Drawable tv_myorder_Normal;
    private Drawable tv_discovery_Pressed;
    private Drawable tv_discovery_Normal;
    private Drawable tv_setting_Pressed;
    private Drawable tv_setting_Normal;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IYWConversationService mConversationService;
    /**
     * 底部四个按钮
     */
    private TextView tv_ubang,tv_myorder,tv_discovery,tv_setting;
    public static TextView tv_unread;


    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles[position];
//        }


    }


    ViewPager pager;

    //PageIndicator tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_home);

        tv_ubang = (TextView) findViewById(R.id.tv_ubang);
        tv_myorder = (TextView) findViewById(R.id.tv_myorder);
        tv_discovery = (TextView) findViewById(R.id.tv_discovery);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_unread = (TextView) findViewById(R.id.tv_unread);
        tv_ubang.setOnClickListener(this);
        tv_discovery.setOnClickListener(this);
        tv_myorder.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        initConversationServiceAndListener();
        setUbangText(true);
        setMyorderText(false);
        setDiscoveryText(false);
        setSettingText(false);
//        titles = new String[]{
//                "友帮",
//                "我的发单",
//                "发现",
//                "个人中心"
//        };

        fragments = new BaseFragment[]{
                new OrderFragment(),
                new MyOrderFragment(),
                new DiscoveryFragment(),
                new PersonFragment()
        };

        pager = (ViewPager) findViewById(R.id.pager);
        //默认预先加载下一个view...
        //设置预先加载几个view
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new TabsAdapter());

//        tabs = (PageIndicator) findViewById(R.id.tabs);
//        tabs.setViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentIndex;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        setUbangText(true);
                        setMyorderText(false);
                        setDiscoveryText(false);
                        setSettingText(false);
                        break;
                    case 1:
                        setUbangText(false);
                        setMyorderText(true);
                        setDiscoveryText(false);
                        setSettingText(false);
                        break;
                    case 2:
                        setUbangText(false);
                        setMyorderText(false);
                        setDiscoveryText(true);
                        setSettingText(false);
                        break;
                    case 3:
                        setUbangText(false);
                        setMyorderText(false);
                        setDiscoveryText(false);
                        setSettingText(true);
                        tv_unread.setVisibility(View.INVISIBLE);
                        break;
                }

                currentIndex = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //initdata();
        IMLogin();
        //initConversationServiceAndListener();
//        if(getIntent().getStringExtra("pushmessage") != null && getIntent().getStringExtra("pushmessage").equals("push"))
//        {
//            tv_unread.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //在Tab栏删除会话未读消息变化的全局监听器
        mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
        //mIMKit.getTribeService().removeTribeListener(mTribeChangedListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_unread.setVisibility(View.INVISIBLE);
        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
        final YWIMKit imKit = loginHelper.getIMKit();
        mConversationService = imKit.getConversationService();

        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();

        //在Tab栏增加会话未读消息变化的全局监听器
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);



    }

    private void initdata(final boolean hasmessage)
    {
        Http.request(HomeActivity.this, API.HAS_UNREAD,  new Http.RequestListener<MessageTypeCount>() {
            @Override
            public void onSuccess(MessageTypeCount result) {
                super.onSuccess(result);
                Log.i("HAS_UNREAD", String.valueOf(result.getCount()));
                if(result.getCount() > 0 && hasmessage || result.getCount() > 0 || hasmessage)
                {

                    tv_unread.setVisibility(View.VISIBLE);
                }
                else
                {
                    //initConversationServiceAndListener(false);
                    tv_unread.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initConversationServiceAndListener() {
        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {

            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
                        final YWIMKit imKit = loginHelper.getIMKit();
                        mConversationService = imKit.getConversationService();
                        int unReadCount = mConversationService.getAllUnreadCount();
                        if (unReadCount > 0 ) {
                            initdata(true);
                        } else  {
                            initdata(false);
                        }
                    }
                });
            }
        };
    }

    private void setUbangText(boolean isSelected) {
        Drawable drawable = null;
        if (isSelected) {
            tv_ubang.setTextColor(getResources().getColor(
                    R.color.tab_pressed_color));
            if (tv_ubang_Pressed == null) {
                tv_ubang_Pressed = getResources().getDrawable(
                        R.drawable.demo_tab_icon_message_pressed);
            }
            drawable = tv_ubang_Pressed;
        } else {
            tv_ubang.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_ubang_Normal == null) {
                tv_ubang_Normal = getResources().getDrawable(
                        R.drawable.demo_tab_icon_message_normal);
            }
            drawable = tv_ubang_Normal;
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            tv_ubang.setCompoundDrawables(null, drawable, null, null);
        }
    }

    private void setMyorderText(boolean isSelected) {
        Drawable drawable = null;
        if (isSelected) {
            tv_myorder.setTextColor(getResources().getColor(
                    R.color.tab_pressed_color));
            if (tv_myorder_Pressed == null) {
                tv_myorder_Pressed = getResources().getDrawable(
                        R.drawable.demo_tab_icon_contact_pressed);
            }
            drawable = tv_myorder_Pressed;
        } else {
            tv_myorder.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_myorder_Normal == null) {
                tv_myorder_Normal = getResources().getDrawable(
                        R.drawable.demo_tab_icon_contact_normal);
            }
            drawable = tv_myorder_Normal;
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            tv_myorder.setCompoundDrawables(null, drawable, null, null);
        }
    }

    private void setDiscoveryText(boolean isSelected) {
        Drawable drawable = null;
        if (isSelected) {
            tv_discovery.setTextColor(getResources().getColor(
                    R.color.tab_pressed_color));
            if (tv_discovery_Pressed == null) {
                tv_discovery_Pressed = getResources().getDrawable(
                        R.drawable.demo_tab_icon_tribe_pressed);
            }
            drawable = tv_discovery_Pressed;
        } else {
            tv_discovery.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_discovery_Normal == null) {
                tv_discovery_Normal = getResources().getDrawable(
                        R.drawable.demo_tab_icon_tribe_normal);
            }
            drawable = tv_discovery_Normal;
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            tv_discovery.setCompoundDrawables(null, drawable, null, null);
        }
    }

    private void setSettingText(boolean isSelected) {
        Drawable drawable = null;
        if (isSelected) {
            tv_setting.setTextColor(getResources().getColor(
                    R.color.tab_pressed_color));
            if (tv_setting_Pressed == null) {
                tv_setting_Pressed = getResources().getDrawable(
                        R.drawable.demo_tab_icon_setting_pressed);
            }
            drawable = tv_setting_Pressed;
        } else {
            tv_setting.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_setting_Normal == null) {
                tv_setting_Normal = getResources().getDrawable(
                        R.drawable.demo_tab_icon_setting_normal);
            }
            drawable = tv_setting_Normal;
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            tv_setting.setCompoundDrawables(null, drawable, null, null);
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ubang:
                setUbangText(true);
                setMyorderText(false);
                setDiscoveryText(false);
                setSettingText(false);
                pager.setCurrentItem(0);
                break;
            case R.id.tv_myorder:
                setUbangText(false);
                setMyorderText(true);
                setDiscoveryText(false);
                setSettingText(false);
                pager.setCurrentItem(1);
                break;
            case R.id.tv_discovery:
                setUbangText(false);
                setMyorderText(false);
                setDiscoveryText(true);
                setSettingText(false);
                pager.setCurrentItem(2);
                break;
            case R.id.tv_setting:
                setUbangText(false);
                setMyorderText(false);
                setDiscoveryText(false);
                setSettingText(true);
                pager.setCurrentItem(3);
                tv_unread.setVisibility(View.INVISIBLE);
                break;
        }
    }

    BaseFragment getCurrentFragment() {
        return fragments[pager.getCurrentItem() % fragments.length];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getCurrentFragment().createOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getCurrentFragment().onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_CODE:
                //TextView scanResult = (TextView) root.findViewById(R.id.txt_scanresult);
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this,FriendsInfoActivity.class);
                    intent.putExtra("UserID",result.substring(9).toString());
                    startActivity(intent);
//                    Http.request(HomeActivity.this, API.ADD_FDF, new Object[]{Auth.getCurrentUserId()},
//                            Http.map("FriendId",result.substring(9).toString()),
//                            new Http.RequestListener<String>() {
//                                @Override
//                                public void onSuccess(String result) {
//                                    super.onSuccess(result);
//                                    Toast.makeText(HomeActivity.this, "已发送好友申请", Toast.LENGTH_SHORT).show();
//                                }
//                            });

                    //scanResult.setText(result);
                } else if (resultCode == RESULT_CANCELED) {
                    //scanResult.setText("扫描出错");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //myUnregisterReceiver();
    }

    private void IMLogin()
    {
        //IMCore.getLoginState
        //初始化imkit
        loginHelper = LoginSampleHelper.getInstance();
        loginHelper.initIMKit(String.valueOf(Auth.getCurrentUserId()), loginHelper.APP_KEY);
        //自定义头像和昵称回调初始化(如果不需要自定义头像和昵称，则可以省去)
        //UserProfileSampleHelper.initProfileCallback();
        //通知栏相关的初始化
        NotificationInitSampleHelper.init();
        if (YWChannel.getInstance().getNetWorkState().isNetWorkNull()) {
            Toast.makeText(HomeActivity.this, "网络已断开，请稍后再试哦~", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("-----ClientID:------", String.format("%s", loginHelper.getIMKit().getIMCore().getLoginState()));
        if (loginHelper.getIMKit().getIMCore().getLoginState() != YWLoginState.success) {

            loginHelper.login_Sample(String.valueOf(Auth.getCurrentUserId()), "1", loginHelper.APP_KEY, new IWxCallback() {

                @Override
                public void onSuccess(Object... arg0) {
                    PushManager.getInstance().initialize(HomeActivity.this);

                    Http.request(HomeActivity.this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "GTClientID", PushManager.getInstance().getClientid(HomeActivity.this)
                    ), new Http.RequestListener<UserDetail>() {
                        @Override
                        public void onSuccess(UserDetail result) {
                            super.onSuccess(result);


                        }
                    });

                }

                @Override
                public void onProgress(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onError(int errorCode, String errorMessage) {
                    if (errorCode == YWLoginCode.LOGON_FAIL_INVALIDUSER || errorCode == YWLoginCode.LOGON_FAIL_INVALIDPWD
                            || errorCode == YWLoginCode.LOGON_FAIL_EMPTY_ACCOUNT || errorCode == YWLoginCode.LOGON_FAIL_EMPTY_PWD
                            || errorCode == YWLoginCode.LOGON_FAIL_INVALIDSERVER) {
                        Log.e("IM ERROR", "登录失败 错误码：" + errorCode + "  错误信息：" + errorMessage);
                    } else {
                        Log.e("IM ERROR", "登录失败 错误码：" + errorCode + "  错误信息：" + errorMessage);
                    }
                }
            });
        }
    }

}
