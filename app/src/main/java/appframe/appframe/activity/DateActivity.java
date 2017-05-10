package appframe.appframe.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.fragment.BaseFragment;
import appframe.appframe.fragment.CKXCFragment;
import appframe.appframe.fragment.CKXXFragment;
import appframe.appframe.fragment.DiscoveryFragment;
import appframe.appframe.fragment.IndexFragment;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.fragment.PersonalFragment;
import appframe.appframe.fragment.XCTSFragment;
import appframe.appframe.fragment.ZYSXFragment;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.PackageUtils;
import appframe.appframe.utils.UriHandler;
import appframe.appframe.widget.hoverscrollview.HoverScrollView;

/**
 * Created by Administrator on 2017/5/8.
 */

public class DateActivity extends BaseFrameActivity implements View.OnClickListener,HoverScrollView.OnScrollListener  {

    LinearLayout rl_title,rl_top,ll_top;
    ViewPager pager;
    TextView tb_title,tb_back,tv_xcts,tv_ckxc,tv_ckxx,tv_zysx,tv_xctstop,tv_ckxctop,tv_ckxxtop,tv_zysxtop,btn_ask,btn_enroll;
    View v_xcts,v_ckxc,v_ckxx,v_zysx,v_xctstop,v_ckxctop,v_ckxxtop,v_zysxtop;
    BaseFragment[] fragments;
    HoverScrollView hoversv;
    Intent intent = new Intent();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        initViews();
    }

    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, rl_title.getTop());
        rl_top.layout(0, mBuyLayout2ParentTop, rl_top.getWidth(), mBuyLayout2ParentTop + rl_top.getHeight());

    }

    protected  void initViews()
    {
        hoversv = (HoverScrollView)findViewById(R.id.hoversv);
        rl_title = (LinearLayout)findViewById(R.id.rl_title);
        rl_top = (LinearLayout)findViewById(R.id.rl_top);
        ll_top = (LinearLayout)findViewById(R.id.ll_top);
        pager = (ViewPager)findViewById(R.id.pager);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_xcts = (TextView)rl_title.findViewById(R.id.tv_xcts);
        tv_ckxc = (TextView)rl_title.findViewById(R.id.tv_ckxc);
        tv_ckxx = (TextView)rl_title.findViewById(R.id.tv_ckxx);
        tv_zysx = (TextView)rl_title.findViewById(R.id.tv_zysx);
        tv_xctstop = (TextView)rl_top.findViewById(R.id.tv_xcts);
        tv_ckxctop = (TextView)rl_top.findViewById(R.id.tv_ckxc);
        tv_ckxxtop = (TextView)rl_top.findViewById(R.id.tv_ckxx);
        tv_zysxtop = (TextView)rl_top.findViewById(R.id.tv_zysx);
        v_xcts = (View)rl_title.findViewById(R.id.v_xcts);
        v_ckxc = (View)rl_title.findViewById(R.id.v_ckxc);
        v_ckxx = (View)rl_title.findViewById(R.id.v_ckxx);
        v_zysx = (View)rl_title.findViewById(R.id.v_zysx);
        v_xctstop = (View)rl_top.findViewById(R.id.v_xcts);
        v_ckxctop = (View)rl_top.findViewById(R.id.v_ckxc);
        v_ckxxtop = (View)rl_top.findViewById(R.id.v_ckxx);
        v_zysxtop = (View)rl_top.findViewById(R.id.v_zysx);
        btn_ask = (TextView)findViewById(R.id.btn_ask);
        btn_enroll = (TextView)findViewById(R.id.btn_enroll);
        tb_back.setText("返回");
        tb_title.setText("俄罗斯伴游");
        setXCTSText(true);
        setCKXCText(false);
        setCKXXText(false);
        setZYSXText(false);
        tb_back.setOnClickListener(this);
        tv_xcts.setOnClickListener(this);
        tv_ckxc.setOnClickListener(this);
        tv_ckxx.setOnClickListener(this);
        tv_zysx.setOnClickListener(this);
        tv_xctstop.setOnClickListener(this);
        tv_ckxctop.setOnClickListener(this);
        tv_ckxxtop.setOnClickListener(this);
        tv_zysxtop.setOnClickListener(this);
        hoversv.setOnScrollListener(this);
        btn_ask.setOnClickListener(this);
        btn_enroll.setOnClickListener(this);

//        int viewPagerIndex = ll_top.indexOfChild(pager);
//        int childViewHeight = getChildViewHeight(); //获取ViewPager的子View的高度。
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, childViewHeight );//这里设置params的高度。
//        ll_top.removeView(pager);
//        ll_top.addView(pager, viewPagerIndex , params);//使用这个params


        findViewById(R.id.parent_ll).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                onScroll(hoversv.getScrollY());


            }
        });

        fragments = new BaseFragment[]{
                new XCTSFragment(),
                new CKXCFragment(),
                new CKXXFragment(),
                new ZYSXFragment()
        };
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new TabsAdapter());
//        indicator.setViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentIndex;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        setXCTSText(true);
                        setCKXCText(false);
                        setCKXXText(false);
                        setZYSXText(false);

                        break;
                    case 1:
                        setXCTSText(false);
                        setCKXCText(true);
                        setCKXXText(false);
                        setZYSXText(false);
                        break;
                    case 2:
                        setXCTSText(false);
                        setCKXCText(false);
                        setCKXXText(true);
                        setZYSXText(false);
                        break;
                    case 3:
                        setXCTSText(false);
                        setCKXCText(false);
                        setCKXXText(false);
                        setZYSXText(true);
                        break;
                }

                currentIndex = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setXCTSText(boolean isSelected) {

        if (isSelected) {
            tv_xctstop.setTextColor(getResources().getColor(
                    R.color.blue));
            v_xctstop.setBackgroundColor(getResources().getColor(
                    R.color.blue));
        } else {
            tv_xctstop.setTextColor(getResources().getColor(
                    R.color.black));
            v_xctstop.setBackgroundColor(getResources().getColor(
                    R.color.white));
        }

    }
    private void setCKXCText(boolean isSelected) {

        if (isSelected) {
            tv_ckxctop.setTextColor(getResources().getColor(
                    R.color.blue));
            v_ckxctop.setBackgroundColor(getResources().getColor(
                    R.color.blue));
        } else {
            tv_ckxctop.setTextColor(getResources().getColor(
                    R.color.black));
            v_ckxctop.setBackgroundColor(getResources().getColor(
                    R.color.white));
        }

    }
    private void setCKXXText(boolean isSelected) {

        if (isSelected) {
            tv_ckxxtop.setTextColor(getResources().getColor(
                    R.color.blue));
            v_ckxxtop.setBackgroundColor(getResources().getColor(
                    R.color.blue));
        } else {
            tv_ckxxtop.setTextColor(getResources().getColor(
                    R.color.black));
            v_ckxxtop.setBackgroundColor(getResources().getColor(
                    R.color.white));
        }

    }
    private void setZYSXText(boolean isSelected) {

        if (isSelected) {
            tv_zysxtop.setTextColor(getResources().getColor(
                    R.color.blue));
            v_zysxtop.setBackgroundColor(getResources().getColor(
                    R.color.blue));
        } else {
            tv_zysxtop.setTextColor(getResources().getColor(
                    R.color.black));
            v_zysxtop.setBackgroundColor(getResources().getColor(
                    R.color.white));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_ask:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = "3000";
                intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.btn_enroll:
                break;
            case R.id.tv_xcts:
                setXCTSText(true);
                setCKXCText(false);
                setCKXXText(false);
                setZYSXText(false);
                pager.setCurrentItem(0,false);
                break;
            case R.id.tv_ckxc:
                setXCTSText(false);
                setCKXCText(true);
                setCKXXText(false);
                setZYSXText(false);
                pager.setCurrentItem(1,false);
                break;
            case R.id.tv_ckxx:
                setXCTSText(false);
                setCKXCText(false);
                setCKXXText(true);
                setZYSXText(false);
                pager.setCurrentItem(2,false);
                break;
            case R.id.tv_zysx:
                setXCTSText(false);
                setCKXCText(false);
                setCKXXText(false);
                setZYSXText(true);
                pager.setCurrentItem(3,false);
                break;



        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("俄罗斯伴游页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("俄罗斯伴游页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

