package appframe.appframe.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.viewpagerindicator.PageIndicator;

import appframe.appframe.R;
import appframe.appframe.fragment.BaseFragment;
import appframe.appframe.fragment.EstimateFragment;
import appframe.appframe.fragment.HomeFragment;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.fragment.PersonFragment;
import appframe.appframe.fragment.ProfileFragment;

/**
 * Created by dashi on 15/6/20.
 */
public class HomeActivity extends BaseFrameActivity{

    String[] titles;
    BaseFragment[] fragments;
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



        titles = new String[]{
                "友帮",
                "我的挂单",
                "我的口碑",
                "会员中心"
        };

        fragments = new BaseFragment[]{
                new HomeFragment(),
                new MyOrderFragment(),
                new EstimateFragment(),
                new PersonFragment(),
        };

        pager = (ViewPager) findViewById(R.id.pager);
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
}
