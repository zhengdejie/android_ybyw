package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.snowdream.android.util.Log;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.MyCollectActivity;
import appframe.appframe.activity.MyInfoActivity;
import appframe.appframe.activity.MyMessageActivity;
import appframe.appframe.activity.PrivacyActivity;
import appframe.appframe.activity.SettingActivity;
import appframe.appframe.activity.WalletActivity;
import appframe.appframe.utils.Auth;
import appframe.appframe.widget.sortlistview.MyContact;
import appframe.appframe.widget.sortlistview.SortListViewActivity;

/**
 * Created by Administrator on 2015/8/8.
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener{
    TextView tb_back,tb_title,tv_name,tv_contact,tv_collect,tv_wallet,tv_mymessage,tv_updatecontact,tv_expandhr,tv_setting;
    ImageView iv_avater;
    View root;
    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_person, null);
        init();

        return root;
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_avater:
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
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
        }
    }
    private  void init()
    {
        tv_name = (TextView)root.findViewById(R.id.tv_name);
        tv_contact = (TextView)root.findViewById(R.id.tv_contact);
        tv_collect = (TextView)root.findViewById(R.id.tv_collect);
        tv_wallet = (TextView)root.findViewById(R.id.tv_wallet);
        iv_avater = (ImageView)root.findViewById(R.id.iv_avater);
        tv_mymessage = (TextView)root.findViewById(R.id.tv_mymessage);
        tv_updatecontact = (TextView)root.findViewById(R.id.tv_updatecontact);
        tv_expandhr = (TextView)root.findViewById(R.id.tv_expandhr);
        tv_setting = (TextView)root.findViewById(R.id.tv_setting);

        tv_contact.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        iv_avater.setOnClickListener(this);
        tv_wallet.setOnClickListener(this);
        tv_mymessage.setOnClickListener(this);
        tv_updatecontact.setOnClickListener(this);
        tv_expandhr.setOnClickListener(this);
        tv_setting.setOnClickListener(this);

        tb_title = (TextView)root.findViewById(R.id.tb_title);
        tb_back = (TextView)root.findViewById(R.id.tb_back);
        tb_back.setVisibility(View.GONE);
        tb_title.setText("个人中心");
        tv_name.setText(Auth.getCurrentUser().getName());
    }
}
