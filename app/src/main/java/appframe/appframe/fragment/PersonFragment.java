package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.github.snowdream.android.util.Log;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.CertificateActivity;
import appframe.appframe.activity.EstimateActivity;
import appframe.appframe.activity.ExpandFriendsActivity;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.MyAnswerActivity;
import appframe.appframe.activity.MyCollectActivity;
import appframe.appframe.activity.MyInfoActivity;
import appframe.appframe.activity.MyMessageActivity;
import appframe.appframe.activity.MyMissionActivity;
import appframe.appframe.activity.MyQuestionActivity;
import appframe.appframe.activity.MyRequireActivity;
import appframe.appframe.activity.MyfansActivity;
import appframe.appframe.activity.MyfocusActivity;
import appframe.appframe.activity.PrivacyActivity;
import appframe.appframe.activity.SettingActivity;
import appframe.appframe.activity.WalletActivity;
import appframe.appframe.app.API;
import appframe.appframe.com.google.zxing.client.result.BizcardResultParser;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.dto.UserContact;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.widget.sortlistview.MyContact;
import appframe.appframe.widget.sortlistview.SortListViewActivity;

/**
 * Created by Administrator on 2015/8/8.
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener{
    TextView tb_back,tb_title,tv_name,tv_contact,tv_collect,tv_wallet,tv_mymessage,tv_updatecontact,tv_expandhr,tv_setting,tv_tel,tv_mission,tv_myestimate,tv_author,tv_question,tv_answer,tv_myhelp,tv_contactservice,tv_myfocus,tv_progress_content;
    public static TextView tv_unread;
    ImageView iv_sex,iv_member;
    com.android.volley.toolbox.NetworkImageView iv_avater;
    View root;
    LinearLayout ll_person,progress_bar;
    List<UserContact> contactsList = new ArrayList<UserContact>();
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IYWConversationService mConversationService;
    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_person, null);
        init();

        return root;
    }

    @Override
    protected void onLoadData() {


    }

    protected void initdata(final boolean hasmessage)
    {
        Http.request(getActivity(), API.HAS_UNREAD, new Http.RequestListener<MessageTypeCount>() {
            @Override
            public void onSuccess(MessageTypeCount result) {
                super.onSuccess(result);

                if (result.getCount() > 0 && hasmessage || result.getCount() > 0 || hasmessage) {

                    tv_unread.setVisibility(View.VISIBLE);
                } else {
                    //initConversationServiceAndListener(false);
                    tv_unread.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_unread.setVisibility(View.INVISIBLE);
        tv_name.setText(Auth.getCurrentUser().getName());
        if(Auth.getCurrentUser().getAvatar() != null && !Auth.getCurrentUser().getAvatar().equals("")) {
            ImageUtils.setImageUrl(iv_avater, Auth.getCurrentUser().getAvatar());
        }
        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
        final YWIMKit imKit = loginHelper.getIMKit();
        mConversationService = imKit.getConversationService();

        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        if(mConversationUnreadChangeListener != null) {
            mConversationUnreadChangeListener.onUnreadChange();

            //在Tab栏增加会话未读消息变化的全局监听器
            mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
        }
        Http.request(getActivity(), API.USER_PROFILE, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<UserDetail>(){
            @Override
            public void onSuccess(UserDetail result) {
                super.onSuccess(result);

                if(result.getGender().equals(getResources().getString(R.string.male).toString()))
                {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.male));
                }
                else
                {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.female));
                }
            }
        });
        MobclickAgent.onPageStart("我的页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(getActivity());          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        //在Tab栏删除会话未读消息变化的全局监听器
        mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
        MobclickAgent.onPageEnd("我的页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(getActivity());
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_avater:
                startActivity(new Intent(getActivity(),MyInfoActivity.class));
                break;
            case R.id.ll_person:
                startActivity(new Intent(getActivity(),MyInfoActivity.class));
                break;
//            case R.id.txt_privacy:
//                startActivity(new Intent(getActivity(), PrivacyActivity.class));
//                break;
            case R.id.tv_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.tv_myfocus:
                startActivity(new Intent(getActivity(), MyfocusActivity.class));
                break;
//            case R.id.tv_myfans:
//                startActivity(new Intent(getActivity(), MyfansActivity.class));
//                break;
            case R.id.tv_contact:
                startActivity(new Intent(getActivity(), MyContact.class));
                break;
            case R.id.tv_expandhr:
                startActivity(new Intent(getActivity(), ExpandFriendsActivity.class));
                break;
            case R.id.tv_wallet:
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
            case R.id.tv_collect:
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            case R.id.tv_mymessage:
                tv_unread.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
            case R.id.tv_updatecontact:
                progress_bar.setVisibility(View.VISIBLE);
                List<UserContact> contactsList = UploadUtils.uploadContact(getActivity());
                Http.request(getActivity(), API.USER_CONTACT_UPLOAD, Http.map("Contact", GsonHelper.getGson().toJson(contactsList),
                        "Id", String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "更新通讯录成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "上传通讯录失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.tv_myhelp:
                startActivity(new Intent(getActivity(), MyRequireActivity.class));
                break;
            case R.id.tv_mission:
                startActivity(new Intent(getActivity(), MyMissionActivity.class));
                break;
            case R.id.tv_author:
                startActivity(new Intent(getActivity(),CertificateActivity.class));
                break;
            case R.id.tv_myestimate:
                Intent intent = new Intent();
                intent.setClass(getActivity(), EstimateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userBrief", Auth.getCurrentUser());
                intent.putExtras(bundle);
//                intent.putExtra("UserID",UserID);
                startActivity(intent);
                break;
            case R.id.tv_question:
                startActivity(new Intent(getActivity(),MyQuestionActivity.class));
                break;
            case R.id.tv_answer:
                startActivity(new Intent(getActivity(),MyAnswerActivity.class));
                break;
            case R.id.tv_contactservice:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = "3000";
                intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
        }
    }
    private  void init()
    {
        tv_name = (TextView)root.findViewById(R.id.tv_name);
        tv_contact = (TextView)root.findViewById(R.id.tv_contact);
        tv_collect = (TextView)root.findViewById(R.id.tv_collect);
        tv_wallet = (TextView)root.findViewById(R.id.tv_wallet);
        iv_avater = (com.android.volley.toolbox.NetworkImageView)root.findViewById(R.id.iv_avater);
        tv_mymessage = (TextView)root.findViewById(R.id.tv_mymessage);
        tv_updatecontact = (TextView)root.findViewById(R.id.tv_updatecontact);
        tv_expandhr = (TextView)root.findViewById(R.id.tv_expandhr);
        tv_setting = (TextView)root.findViewById(R.id.tv_setting);
        tv_unread = (TextView)root.findViewById(R.id.tv_unread);
        tv_tel = (TextView)root.findViewById(R.id.tv_tel);
        tv_mission = (TextView)root.findViewById(R.id.tv_mission);
        tv_myestimate = (TextView)root.findViewById(R.id.tv_myestimate);
        tv_author = (TextView)root.findViewById(R.id.tv_author);
        ll_person = (LinearLayout)root.findViewById(R.id.ll_person);
        iv_sex = (ImageView)root.findViewById(R.id.iv_sex);
        iv_member = (ImageView)root.findViewById(R.id.iv_member);
        tv_question = (TextView)root.findViewById(R.id.tv_question);
        tv_answer = (TextView)root.findViewById(R.id.tv_answer);
        tv_myhelp = (TextView)root.findViewById(R.id.tv_myhelp);
        tv_contactservice = (TextView)root.findViewById(R.id.tv_contactservice);
        tv_myfocus = (TextView)root.findViewById(R.id.tv_myfocus);
        progress_bar = (LinearLayout)root.findViewById(R.id.progress_bar);
        tv_progress_content = (TextView)root.findViewById(R.id.tv_progress_content);
        tv_progress_content.setText("正在更新通讯录");
//        tv_myfans = (TextView)root.findViewById(R.id.tv_myfans);

        tv_contact.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        iv_avater.setOnClickListener(this);
        tv_wallet.setOnClickListener(this);
        tv_mymessage.setOnClickListener(this);
        tv_updatecontact.setOnClickListener(this);
        tv_expandhr.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        tv_mission.setOnClickListener(this);
        ll_person.setOnClickListener(this);
        tv_myestimate.setOnClickListener(this);
        tv_author.setOnClickListener(this);
        tv_question.setOnClickListener(this);
        tv_answer.setOnClickListener(this);
        tv_myhelp.setOnClickListener(this);
        tv_contactservice.setOnClickListener(this);
        tv_myfocus.setOnClickListener(this);
//        tv_myfans.setOnClickListener(this);

        tb_title = (TextView)root.findViewById(R.id.tb_title);
        tb_back = (TextView)root.findViewById(R.id.tb_back);
        tb_back.setVisibility(View.GONE);
        tb_title.setText("我的");
        tv_name.setText(Auth.getCurrentUser().getName());
        if(Auth.getCurrentUser().getYBAccount() != null && !Auth.getCurrentUser().getYBAccount().equals("")) {
            tv_tel.setText(tv_tel.getText() + Auth.getCurrentUser().getYBAccount());
        }

        if(Auth.getCurrentUser().getAvatar() != null && !Auth.getCurrentUser().getAvatar().equals("")) {
            ImageUtils.setImageUrl(iv_avater, Auth.getCurrentUser().getAvatar());
        }
        else
        {
            if(Auth.getCurrentUser().getGender().equals(getResources().getString(R.string.male).toString()))
            {
                iv_avater.setDefaultImageResId(R.drawable.maleavatar);
            }
            else
            {
                iv_avater.setDefaultImageResId(R.drawable.femaleavatar);
            }

        }
        if(Auth.getCurrentUser().getGender().equals(getResources().getString(R.string.male).toString()))
        {
            iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.male));
        }
        else
        {
            iv_sex.setImageDrawable(getResources().getDrawable(R.drawable.female));
        }
        if(Auth.getCurrentUser().getMember() == 1)
        {
            iv_member.setImageResource(R.drawable.idflag);
        }
        else if(Auth.getCurrentUser().getMember() == 2)
        {
            iv_member.setImageResource(R.drawable.shopflag);
        }
        else
        {
            iv_member.setVisibility(View.GONE);
        }
        tv_unread.setVisibility(View.INVISIBLE);
        initConversationServiceAndListener();

    }


}
