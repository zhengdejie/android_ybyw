package appframe.appframe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.FriendMessageActivity;
import appframe.appframe.activity.OrderMessageActivity;
import appframe.appframe.activity.QuestionMessageActivity;
import appframe.appframe.activity.SystemMessageActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.OpenConversationSampleHelper;

/**
 * Created by Administrator on 2017/7/28.
 */

public class NotificationFragment extends BaseFragment implements View.OnClickListener {
    View root;
    private TextView tv_systemmessage,tv_ordermessage,tv_questionmessage,tv_friendmessage,tv_smunread,tv_qmunread,tv_omunread,tv_fmunread;
    private Fragment fgm_recentcontacter;
    private LinearLayout lv_fragment_container;
//    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
//    private Handler mHandler = new Handler(Looper.getMainLooper());
//    private IYWConversationService mConversationService;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root  = inflater.inflate(R.layout.fragment_notification, null);
        init();
        initdata();
        return root;
    }

    @Override
    protected void onLoadData() {

    }

    public void init()
    {

        tv_systemmessage = (TextView)root.findViewById(R.id.tv_systemmessage);
        tv_ordermessage = (TextView)root.findViewById(R.id.tv_ordermessage);
        tv_friendmessage = (TextView)root.findViewById(R.id.tv_friendmessage);

        tv_questionmessage = (TextView)root.findViewById(R.id.tv_questionmessage);
        tv_smunread = (TextView)root.findViewById(R.id.tv_smunread);
        tv_omunread = (TextView)root.findViewById(R.id.tv_omunread);
        tv_fmunread = (TextView)root.findViewById(R.id.tv_fmunread);

        tv_qmunread = (TextView)root.findViewById(R.id.tv_qmunread);



        tv_systemmessage.setOnClickListener(this);
        tv_ordermessage.setOnClickListener(this);
        tv_friendmessage.setOnClickListener(this);

        tv_questionmessage.setOnClickListener(this);


    }

    private void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));

        Http.request(getActivity(), API.GET_UNREAD, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<MessageTypeCount>>() {
            @Override
            public void onSuccess(List<MessageTypeCount> result) {
                super.onSuccess(result);

                for (MessageTypeCount mtc : result) {
                    if (mtc.getType() == 1 && mtc.getCount() != 0) {
                        tv_omunread.setVisibility(View.VISIBLE);
                        tv_omunread.setText(String.valueOf(mtc.getCount()));
                    } else if (mtc.getType() == 2 && mtc.getCount() != 0) {
                        tv_fmunread.setVisibility(View.VISIBLE);
                        tv_fmunread.setText(String.valueOf(mtc.getCount()));
                    } else if (mtc.getType() == 5 && mtc.getCount() != 0) {
                        tv_qmunread.setVisibility(View.VISIBLE);
                        tv_qmunread.setText(String.valueOf(mtc.getCount()));
                    }
                }

            }
        });

//        initConversationServiceAndListener();

    }

//    private void initConversationServiceAndListener() {
//        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {
//
//            @Override
//            public void onUnreadChange() {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
//                        final YWIMKit imKit = loginHelper.getIMKit();
//                        mConversationService = imKit.getConversationService();
//                        int unReadCount = mConversationService.getAllUnreadCount();
//                        if (unReadCount > 0) {
//                            tv_rcunread.setVisibility(View.VISIBLE);
//                            tv_rcunread.setText(String.valueOf(unReadCount));
//                        } else {
//                            tv_rcunread.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
//            }
//        };
//    }

    @Override
    public void onResume() {
        super.onResume();
        //initdata();
//        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
//        final YWIMKit imKit = loginHelper.getIMKit();
//        mConversationService = imKit.getConversationService();
//
//        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
//        mConversationUnreadChangeListener.onUnreadChange();
//
//        //在Tab栏增加会话未读消息变化的全局监听器
//        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
        MobclickAgent.onPageStart("我的消息页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    @Override
    public void onStop() {
        super.onStop();
//        mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
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

//            case R.id.tb_back:
//                getActivity().finish();
//                break;
            case R.id.tv_systemmessage:
                startActivity(new Intent(getActivity(), SystemMessageActivity.class));
                break;
            case R.id.tv_ordermessage:
                tv_omunread.setVisibility(View.INVISIBLE);
                Http.request(getActivity(), API.MARK_ALLREAD, Http.map("Type","1"), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                    }
                });
                startActivity(new Intent(getActivity(), OrderMessageActivity.class));
                break;
            case R.id.tv_questionmessage:
                tv_qmunread.setVisibility(View.INVISIBLE);
                Http.request(getActivity(), API.MARK_ALLREAD, Http.map("Type","5"), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                    }
                });
                startActivity(new Intent(getActivity(), QuestionMessageActivity.class));
                break;
            case R.id.tv_friendmessage:
                tv_fmunread.setVisibility(View.INVISIBLE);
                Http.request(getActivity(), API.MARK_ALLREAD, Http.map("Type", "2"), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                    }
                });
                startActivity(new Intent(getActivity(), FriendMessageActivity.class));
                break;
//            case R.id.tv_recentcontacter:
//                startActivity(OpenConversationSampleHelper.getOpenConversationListIntent_Sample(getActivity()));
//                break;

        }

    }
}