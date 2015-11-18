package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.WxLog;
import com.alibaba.mobileim.login.YWLoginCode;
import com.alibaba.mobileim.login.YWLoginState;
import com.igexin.sdk.PushManager;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
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
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/20.
 */
public class HomeActivity extends BaseFrameActivity{

    String[] titles;
    BaseFragment[] fragments;
    public static final int SCAN_CODE = 1;
    private LoginSampleHelper loginHelper;
    class TabsAdapter extends FragmentPagerAdapter  {

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

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

//        private  final int[] ICONS = new int[] {
//                R.drawable.ic_add_black_24dp,
//                R.drawable.ic_add_black_24dp,
//                R.drawable.ic_add_black_24dp,
//                R.drawable.ic_add_black_24dp, };
//
//        @Override
//        public int getIconResId(int index) {
//            return ICONS[index];
//        }
    }


    ViewPager pager;
    PageIndicator tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_home);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);  //titlebar为自己标题栏的布局
        Log.i("onCreateHomeActivity", "123456789");
//        if(!App.mIMKit.getIMCore().getLoginState().equals("success")) {
//
//            Utils.IMLogin(String.valueOf(Auth.getCurrentUserId()), "1", this);
//
//            PushManager.getInstance().initialize(this);
//            Log.i("-----ClientID:------", String.format("%s", PushManager.getInstance().getClientid(this)));
//            Http.request(this, API.USER_PROFILE_UPDATE, new Object[]{Auth.getCurrentUserId()}, Http.map(
//                    "GTClientID", PushManager.getInstance().getClientid(this)
//            ), new Http.RequestListener<UserDetail>() {
//                @Override
//                public void onSuccess(UserDetail result) {
//                    super.onSuccess(result);
//
//
//
//                }
//            });
//        }
        loginHelper = LoginSampleHelper.getInstance();
        if(loginHelper.getIMKit().getIMCore().getLoginState() != YWLoginState.success)
        {
            loginHelper.login_Sample(String.valueOf(Auth.getCurrentUserId()), "1", new IWxCallback() {

                @Override
                public void onSuccess(Object... arg0) {
                    PushManager.getInstance().initialize(HomeActivity.this);
                    Log.i("-----ClientID:------", String.format("%s", PushManager.getInstance().getClientid(HomeActivity.this)));
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
                            || errorCode == YWLoginCode.LOGON_FAIL_INVALIDSERVER ) {
                        Log.e("IM ERROR", "登录失败 错误码：" + errorCode + "  错误信息：" + errorMessage);
                    } else {
                        Log.e("IM ERROR", "登录失败 错误码：" + errorCode + "  错误信息：" + errorMessage);
                    }


                }
            });

        }


        titles = new String[]{
                "友帮",
                "我的发单",
                "发现",
                "个人中心"
        };

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

        tabs = (PageIndicator) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    BaseFragment getCurrentFragment(){
        return fragments[pager.getCurrentItem() % fragments.length];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getCurrentFragment().createOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getCurrentFragment().onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_CODE:
                //TextView scanResult = (TextView) root.findViewById(R.id.txt_scanresult);
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    //scanResult.setText(result);
                } else if (resultCode == RESULT_CANCELED) {
                    //scanResult.setText("扫描出错");
                }
                break;
            default:
                break;
        }
    }

}
