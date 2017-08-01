package appframe.appframe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.FriendMessageActivity;
import appframe.appframe.activity.HomeActivity;
import appframe.appframe.activity.MyMessageActivity;
import appframe.appframe.activity.NearByActivity;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.activity.OrderMessageActivity;
import appframe.appframe.activity.QuestionMessageActivity;
import appframe.appframe.activity.SystemMessageActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.OpenConversationSampleHelper;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendShopsAdapater;

/**
 * Created by Administrator on 2017/7/26.
 */

public class MymessageFragment extends BaseFragment implements View.OnClickListener {
    View root;
    private TextView tb_back,tv_message,tv_notification;
    Fragment[] fragments;
    android.support.v4.view.ViewPager pager;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root  = inflater.inflate(R.layout.activity_mymessage, null);
        init();
        initdata();
        return root;
    }


    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter() {
            super(getChildFragmentManager());
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
    protected void onLoadData() {

    }

    public void init()
    {

        tb_back = (TextView)root.findViewById(R.id.tb_back);
        tv_message = (TextView)root.findViewById(R.id.tv_message);
        tv_notification = (TextView)root.findViewById(R.id.tv_notification);
        pager = (android.support.v4.view.ViewPager)root.findViewById(R.id.pager);
        tb_back.setVisibility(View.GONE);

        tv_message.setOnClickListener(this);
        tv_notification.setOnClickListener(this);

        fragments = new Fragment[]{
//                new OpenIMFragment(),
                OpenConversationSampleHelper.getOpenConversationListFragment_Sample(getActivity()),
                new NotificationFragment(),

        };
        pager.setAdapter(new TabsAdapter());

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0)
                {
                    tv_message.setBackgroundResource(R.drawable.titlebar_order_left_selected);
                    tv_message.setTextColor(getResources().getColor(R.color.blue));
                    tv_notification.setBackgroundResource(R.drawable.titlebar_order_right_unselected);
                    tv_notification.setTextColor(Color.rgb(255, 255, 255));
                }
                else if(position == 1)
                {
                    tv_message.setBackgroundResource(R.drawable.titlebar_order_left_unselected);
                    tv_message.setTextColor(Color.rgb(255, 255, 255));
                    tv_notification.setBackgroundResource(R.drawable.titlebar_order_right_selected);
                    tv_notification.setTextColor(getResources().getColor(R.color.blue));
                }
                else
                {

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initdata()
    {


    }


    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("我的消息页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的消息页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tv_message:
                tv_message.setBackgroundResource(R.drawable.titlebar_order_left_selected);
                tv_message.setTextColor(getResources().getColor(R.color.blue));
                tv_notification.setBackgroundResource(R.drawable.titlebar_order_right_unselected);
                tv_notification.setTextColor(Color.rgb(255, 255, 255));
                pager.setCurrentItem(0,false);
                break;
            case R.id.tv_notification:
                tv_message.setBackgroundResource(R.drawable.titlebar_order_left_unselected);
                tv_message.setTextColor(Color.rgb(255, 255, 255));
                tv_notification.setBackgroundResource(R.drawable.titlebar_order_right_selected);
                tv_notification.setTextColor(getResources().getColor(R.color.blue));
                pager.setCurrentItem(1,false);
                break;
        }

    }
}
