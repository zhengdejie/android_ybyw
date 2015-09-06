package appframe.appframe.fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.viewpagerindicator.PageIndicator;

import appframe.appframe.R;


/**
 * Created by Administrator on 2015/8/18.
 */
public class HomeFragment extends BaseFragment {
    String[] titles;
    Fragment[] fragments;

    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter() {
            super(HomeFragment.this.getChildFragmentManager());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, null);
        titles = new String[]{
                "需求单",
                "自荐单"
        };

        fragments = new BaseFragment[]{
                OrderFragment.newInstance("需求"),
                OrderFragment.newInstance("自荐")
        };

        pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(new TabsAdapter());

        tabs = (PageIndicator) root.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return root;
    }

//    BaseFragment getCurrentFragment(){
//        return fragments[pager.getCurrentItem() % fragments.length];
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getCurrentFragment().createOptionsMenu(menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(getCurrentFragment().onOptionsItemSelected(item)) return true;
//        return super.onOptionsItemSelected(item);
//    }

}
