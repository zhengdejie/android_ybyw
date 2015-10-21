package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.viewpagerindicator.PageIndicator;

import appframe.appframe.R;
import appframe.appframe.app.App;
import appframe.appframe.fragment.BaseFragment;
import appframe.appframe.fragment.EstimateFragment;
import appframe.appframe.fragment.HomeFragment;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.fragment.OrderFragment;
import appframe.appframe.fragment.PersonFragment;
import appframe.appframe.fragment.ProfileFragment;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Utils;

/**
 * Created by dashi on 15/6/20.
 */
public class HomeActivity extends BaseFrameActivity{

    String[] titles;
    BaseFragment[] fragments;
    public static final int SCAN_CODE = 1;
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

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    ViewPager pager;
    PageIndicator tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_home);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);  //titlebar为自己标题栏的布局
        Log.i("-----IM STATE:------",String.format("%s",App.mIMKit.getIMCore().getLoginState()));
        if(!App.mIMKit.getIMCore().getLoginState().equals("success")) {

            Utils.IMLogin(String.valueOf(Auth.getCurrentUserId()),"1",this);

        }


        titles = new String[]{
                "友帮",
                "我的发单",
                "我的口碑",
                "个人中心"
        };

        fragments = new BaseFragment[]{
                new OrderFragment(),
                new MyOrderFragment(),
                new EstimateFragment(),
                new PersonFragment(),
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
