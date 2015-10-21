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
import appframe.appframe.activity.MyInfoActivity;
import appframe.appframe.activity.PrivacyActivity;
import appframe.appframe.activity.SettingActivity;
import appframe.appframe.activity.WalletActivity;
import appframe.appframe.widget.sortlistview.SortListViewActivity;

/**
 * Created by Administrator on 2015/8/8.
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener{
    TextView txt_myinfo,txt_privacy,tb_back,tb_title;
    Button btn_setting,btn_contact,btn_expandhr,btn_wallet;
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
            case R.id.btn_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.btn_contact:
                startActivity(new Intent(getActivity(), SortListViewActivity.class));
                break;
            case R.id.btn_expandhr:
                startActivity(new Intent(getActivity(), SortListViewActivity.class));
                break;
            case R.id.btn_wallet:
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
        }
    }
    private  void init()
    {
        txt_myinfo = (TextView)root.findViewById(R.id.txt_myinfo);
        btn_setting = (Button)root.findViewById(R.id.btn_setting);
        btn_contact = (Button)root.findViewById(R.id.btn_contact);
        btn_expandhr = (Button)root.findViewById(R.id.btn_expandhr);
        iv_avater = (ImageView)root.findViewById(R.id.iv_avater);
        btn_wallet = (Button)root.findViewById(R.id.btn_wallet);
        txt_myinfo.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_contact.setOnClickListener(this);
        iv_avater.setOnClickListener(this);
        btn_expandhr.setOnClickListener(this);
        btn_wallet.setOnClickListener(this);
        tb_title = (TextView)root.findViewById(R.id.tb_title);
        tb_back = (TextView)root.findViewById(R.id.tb_back);
        tb_back.setVisibility(View.GONE);
        tb_title.setText("个人中心");
    }
}
