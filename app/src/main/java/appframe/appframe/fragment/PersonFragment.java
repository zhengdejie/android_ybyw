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

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.MyCollectActivity;
import appframe.appframe.activity.MyInfoActivity;
import appframe.appframe.activity.MyMessageActivity;
import appframe.appframe.activity.PrivacyActivity;
import appframe.appframe.activity.SettingActivity;
import appframe.appframe.activity.WalletActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.dto.UserContact;
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
    TextView tb_back,tb_title,tv_name,tv_contact,tv_collect,tv_wallet,tv_mymessage,tv_updatecontact,tv_expandhr,tv_setting,tv_tel;
    public static TextView tv_unread;
    com.android.volley.toolbox.NetworkImageView iv_avater;
    View root;
    LinearLayout ll_person;
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
                android.util.Log.i("HAS_UNREAD", String.valueOf(result.getCount()));
                if(result.getCount() > 0 && hasmessage || result.getCount() > 0 || hasmessage)
                {

                    tv_unread.setVisibility(View.VISIBLE);
                }
                else
                {
                    //initConversationServiceAndListener(false);
                    tv_unread.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_name.setText(Auth.getCurrentUser().getName());
        LoginSampleHelper loginHelper = LoginSampleHelper.getInstance();
        final YWIMKit imKit = loginHelper.getIMKit();
        mConversationService = imKit.getConversationService();

        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();

        //在Tab栏增加会话未读消息变化的全局监听器
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }

    @Override
    public void onPause() {
        super.onDestroyView();
        //在Tab栏删除会话未读消息变化的全局监听器
        mConversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
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
            case R.id.tv_contact:
                startActivity(new Intent(getActivity(), MyContact.class));
                break;
            case R.id.tv_expandhr:
                startActivity(new Intent(getActivity(), SortListViewActivity.class));
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
                List<UserContact> contactsList = UploadUtils.uploadContact(getActivity());
                Http.request(getActivity(), API.USER_CONTACT_UPLOAD, Http.map("Contact", GsonHelper.getGson().toJson(contactsList),
                        "Id",String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        Toast.makeText(getActivity(),"上传通讯录成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        Toast.makeText(getActivity(),"上传通讯录失败",Toast.LENGTH_SHORT).show();
                    }
                });
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
        ll_person = (LinearLayout)root.findViewById(R.id.ll_person);

        tv_contact.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        iv_avater.setOnClickListener(this);
        tv_wallet.setOnClickListener(this);
        tv_mymessage.setOnClickListener(this);
        tv_updatecontact.setOnClickListener(this);
        tv_expandhr.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        ll_person.setOnClickListener(this);

        tb_title = (TextView)root.findViewById(R.id.tb_title);
        tb_back = (TextView)root.findViewById(R.id.tb_back);
        tb_back.setVisibility(View.GONE);
        tb_title.setText("个人中心");
        tv_name.setText(Auth.getCurrentUser().getName());
        tv_tel.setText(tv_tel.getText() + Auth.getCurrentUser().getMobile());
        ImageUtils.setImageUrl(iv_avater, Auth.getCurrentUser().getAvatar());

        initConversationServiceAndListener();
    }

}
