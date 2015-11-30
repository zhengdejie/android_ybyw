package appframe.appframe.activity;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.dto.Nearby;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.OpenConversationSampleHelper;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;

/**
 * Created by Administrator on 2015/11/12.
 */
public class MyMessageActivity extends BaseActivity implements View.OnClickListener{
    private TextView tb_title,tb_back,tv_systemmessage,tv_ordermessage,tv_friendmessage,tv_recentcontacter,tv_smunread,tv_omunread,tv_fmunread,tv_rcunread;
    private Fragment fgm_recentcontacter;
    private LinearLayout lv_fragment_container;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IYWConversationService mConversationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymessage);
        init();
        initdata();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_systemmessage = (TextView)findViewById(R.id.tv_systemmessage);
        tv_ordermessage = (TextView)findViewById(R.id.tv_ordermessage);
        tv_friendmessage = (TextView)findViewById(R.id.tv_friendmessage);
        tv_recentcontacter = (TextView)findViewById(R.id.tv_recentcontacter);
        tv_smunread = (TextView)findViewById(R.id.tv_smunread);
        tv_omunread = (TextView)findViewById(R.id.tv_omunread);
        tv_fmunread = (TextView)findViewById(R.id.tv_fmunread);
        tv_rcunread = (TextView)findViewById(R.id.tv_rcunread);
        tb_back.setText("个人中心");
        tb_title.setText("我的消息");
        tb_back.setOnClickListener(this);
        tv_systemmessage.setOnClickListener(this);
        tv_ordermessage.setOnClickListener(this);
        tv_friendmessage.setOnClickListener(this);
        tv_recentcontacter.setOnClickListener(this);

//        YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.
//                beginTransaction();
//        fragmentTransaction.add(R.id.lv_fragment_container,imKit.getConversationFragment());
//        fragmentTransaction.commit();
        //加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
        //后面的参数是此Fragment的Tag。相当于id
        //记住提交



    }

    private void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));

        Http.request(MyMessageActivity.this, API.GET_UNREAD, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<MessageTypeCount>>() {
            @Override
            public void onSuccess(List<MessageTypeCount> result) {
                super.onSuccess(result);

                for(MessageTypeCount mtc : result)
                {
                    if(mtc.getType() == 1 && mtc.getCount() != 0)
                    {
                        tv_omunread.setVisibility(View.VISIBLE);
                        tv_omunread.setText(String.valueOf(mtc.getCount()));
                    }
                    else if(mtc.getType() == 2 && mtc.getCount() != 0)
                    {
                        tv_fmunread.setVisibility(View.VISIBLE);
                        tv_fmunread.setText(String.valueOf(mtc.getCount()));
                    }
                    else
                    {

                    }
                }

            }
        });

        initConversationServiceAndListener();

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
                        if (unReadCount > 0) {
                            tv_rcunread.setVisibility(View.VISIBLE);
                            tv_rcunread.setText(String.valueOf(unReadCount));
                        } else {
                            tv_rcunread.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initdata();
        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
        final YWIMKit imKit = loginHelper.getIMKit();
        mConversationService = imKit.getConversationService();

        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();

        //在Tab栏增加会话未读消息变化的全局监听器
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_systemmessage:
                startActivity(new Intent(MyMessageActivity.this, SystemMessageActivity.class));
                break;
            case R.id.tv_ordermessage:
                tv_omunread.setVisibility(View.INVISIBLE);
                Http.request(MyMessageActivity.this, API.MARK_ALLREAD, Http.map("Type","1"), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                    }
                });
                startActivity(new Intent(MyMessageActivity.this, OrderMessageActivity.class));
                break;
            case R.id.tv_friendmessage:
                tv_fmunread.setVisibility(View.INVISIBLE);
                Http.request(MyMessageActivity.this, API.MARK_ALLREAD, Http.map("Type", "2"), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);

                    }
                });
                startActivity(new Intent(MyMessageActivity.this, FriendMessageActivity.class));
                break;
            case R.id.tv_recentcontacter:
                startActivity(OpenConversationSampleHelper.getOpenConversationListIntent_Sample(this));
                break;
        }

    }
}
