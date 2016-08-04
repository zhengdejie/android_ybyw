package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.github.mrengineer13.snackbar.SnackBar;

import com.igexin.sdk.PushManager;


import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.dto.APKVersion;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.fragment.BaseFragment;
import appframe.appframe.fragment.DiscoveryFragment;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.fragment.OrderFragment;
import appframe.appframe.fragment.PersonFragment;
import appframe.appframe.fragment.ProfileFragment;
import appframe.appframe.service.UpdateAPKService;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.NetworkUtils;
import appframe.appframe.utils.NotificationInitSampleHelper;
import appframe.appframe.utils.PackageUtils;
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/20.
 */
public class HomeActivity extends BaseFrameActivity implements View.OnClickListener {

    String[] titles;
    BaseFragment[] fragments;
    public static final int SCAN_CODE = 1;
//    FrameLayout fl_bottom;
    private LoginSampleHelper loginHelper;
//    private FrameLayout btn_ck;
    private ImageView iv_center;
    public boolean isReverse=false;
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
    ConnectionChangeReceiver connectionChangeReceiver=new ConnectionChangeReceiver();
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
//        btn_ck = (FrameLayout)findViewById(R.id.btn_ck);
        iv_center = (ImageView)findViewById(R.id.iv_center);
//        fl_bottom = (FrameLayout)findViewById(R.id.fl_bottom);
//        fl_bottom.getBackground().setAlpha(0);
        tv_ubang.setOnClickListener(this);
        tv_discovery.setOnClickListener(this);
        tv_myorder.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
//        btn_ck.setOnClickListener(this);
        iv_center.setOnClickListener(this);
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
        checkUpdate();
        IMLogin();
        initConversationServiceAndListener();

        //initConversationServiceAndListener();
//        if(getIntent().getStringExtra("pushmessage") != null && getIntent().getStringExtra("pushmessage").equals("push"))
//        {
//            tv_unread.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");//要接收的广播
        registerReceiver(connectionChangeReceiver, intentFilter);//注册接收者
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

    @Override
    protected void onPause() {
        super.onPause();

        //在Tab栏删除会话未读消息变化的全局监听器
        mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
        //mIMKit.getTribeService().removeTribeListener(mTribeChangedListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectionChangeReceiver);
    }

    private void initdata(final boolean hasmessage)
    {
        Http.request(HomeActivity.this, API.HAS_UNREAD, new Http.RequestListener<MessageTypeCount>() {
            @Override
            public void onSuccess(MessageTypeCount result) {
                super.onSuccess(result);
                Log.i("HAS_UNREAD", String.valueOf(result.getCount()));
                if (result.getCount() > 0 && hasmessage || result.getCount() > 0 || hasmessage) {

                    tv_unread.setVisibility(View.VISIBLE);
                } else {
                    //initConversationServiceAndListener(false);
                    tv_unread.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void checkUpdate()
    {
        final PackageUtils packageUtils = new PackageUtils(this);
        Http.request(HomeActivity.this, API.GET_APKVERSION, new Http.RequestListener<APKVersion>(){
            @Override
            public void onSuccess(APKVersion result) {
                super.onSuccess(result);

                if (packageUtils.isUpgradeVersion(packageUtils.getVersion(),result.getVersionCode()) ) {

                    // 发现新版本，提示用户更新
                    AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                    alert.setTitle("软件升级")
                            .setMessage("发现新版本,建议立即更新使用.")
                            .setPositiveButton("更新",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // 开启更新服务UpdateService
                                            // 这里为了把update更好模块化，可以传一些updateService依赖的值
                                            // 如布局ID，资源ID，动态获取的标题,这里以app_name为例
                                            Intent updateIntent = new Intent(
                                                    HomeActivity.this,
                                                    UpdateAPKService.class);
//                                            updateIntent.putExtra(
//                                                    "app_name",
//                                                    getResources().getString(
//                                                            R.string.app_name));
                                            startService(updateIntent);
                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                    alert.create().show();

                }//if
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
                        R.drawable.home);

            }
            drawable = tv_ubang_Pressed;
        } else {
            tv_ubang.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_ubang_Normal == null) {
                tv_ubang_Normal = getResources().getDrawable(
                        R.drawable.homegray);
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
                        R.drawable.order);
            }
            drawable = tv_myorder_Pressed;
        } else {
            tv_myorder.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_myorder_Normal == null) {
                tv_myorder_Normal = getResources().getDrawable(
                        R.drawable.ordergray);
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
                        R.drawable.discovery);
            }
            drawable = tv_discovery_Pressed;
        } else {
            tv_discovery.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_discovery_Normal == null) {
                tv_discovery_Normal = getResources().getDrawable(
                        R.drawable.discoverygray);
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
                        R.drawable.my);
            }
            drawable = tv_setting_Pressed;
        } else {
            tv_setting.setTextColor(getResources().getColor(
                    R.color.tab_normal_color));
            if (tv_setting_Normal == null) {
                tv_setting_Normal = getResources().getDrawable(
                        R.drawable.mygray);
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
            case R.id.iv_center:
                new PopupWindows_Picture(this,iv_center);
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case SCAN_CODE:
//                //TextView scanResult = (TextView) root.findViewById(R.id.txt_scanresult);
//                if (resultCode == RESULT_OK) {
//                    String result = data.getStringExtra("scan_result");
//                    Intent intent = new Intent();
//                    intent.setClass(HomeActivity.this,FriendsInfoActivity.class);
//                    intent.putExtra("UserID",result.substring(9).toString());
//                    startActivity(intent);
////                    Http.request(HomeActivity.this, API.ADD_FDF, new Object[]{Auth.getCurrentUserId()},
////                            Http.map("FriendId",result.substring(9).toString()),
////                            new Http.RequestListener<String>() {
////                                @Override
////                                public void onSuccess(String result) {
////                                    super.onSuccess(result);
////                                    Toast.makeText(HomeActivity.this, "已发送好友申请", Toast.LENGTH_SHORT).show();
////                                }
////                            });
//
//                    //scanResult.setText(result);
//                } else if (resultCode == RESULT_CANCELED) {
//                    //scanResult.setText("扫描出错");
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
//            IMLogin();
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
//                    IMLogin();
//                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            });
        }
    }

    class ConnectionChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

                //Wifi
                NetworkInfo wifiState = connManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI);
                //3G
                NetworkInfo mobState = connManager.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE);


                if (!mobState.isConnected() && !wifiState.isConnected()) {
                    // unconnect network
                    new SnackBar.Builder(HomeActivity.this)
                            .withMessage("网络连接不可用")
                            .withStyle(SnackBar.Style.ALERT)
                            .withDuration(SnackBar.PERMANENT_SNACK)
                            .withBackgroundColorId(android.R.color.holo_red_dark).show();
                } else {
                    // connect network
                    SnackBar snackBar = new SnackBar(HomeActivity.this);
                    snackBar.hide();
                    IMLogin();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PopupWindows_Picture extends PopupWindow
    {

        public PopupWindows_Picture(final Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_makeorder, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            RelativeLayout rl_photopopup = (RelativeLayout)view.findViewById(R.id.rl_photopopup);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            rl_photopopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            ImageView iv_require = (ImageView) view
                    .findViewById(R.id.iv_require);
            ImageView iv_recommend = (ImageView) view
                    .findViewById(R.id.iv_recommend);
            ImageView iv_question = (ImageView) view
                    .findViewById(R.id.iv_question);
            ImageView iv_center = (ImageView) view
                    .findViewById(R.id.iv_center);
            iv_center.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dismiss();
                }
            });
            iv_require.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent();
                    intent.putExtra("demand","demand");
                    intent.setClass(HomeActivity.this, OrderSendActivity.class);
                    startActivity(intent);

                    dismiss();
                }
            });
            iv_recommend.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent();
                    intent.putExtra("self", "self");
                    intent.setClass(HomeActivity.this, OrderSendActivity.class);
                    startActivity(intent);

                    dismiss();
                }
            });
            iv_question.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent();

                    intent.setClass(HomeActivity.this, QuestionSendActivity.class);
                    startActivity(intent);

                    dismiss();
                }
            });

        }
    }

}
