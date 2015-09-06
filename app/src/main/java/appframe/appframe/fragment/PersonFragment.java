package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.MyInfoActivity;
import appframe.appframe.activity.PrivacyActivity;
import appframe.appframe.activity.SettingActivity;

/**
 * Created by Administrator on 2015/8/8.
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener{
    TextView txt_myinfo,txt_privacy;
    Button btn_setting;
    View root;
    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_person, null);
        init();

        return root;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txt_myinfo:
                startActivity(new Intent(getActivity(),MyInfoActivity.class));
                break;
            case R.id.txt_privacy:
                startActivity(new Intent(getActivity(), PrivacyActivity.class));
                break;
            case R.id.btn_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

        }
    }
    private  void init()
    {
        txt_myinfo = (TextView)root.findViewById(R.id.txt_myinfo);
        txt_privacy = (TextView)root.findViewById(R.id.txt_privacy);
        btn_setting = (Button)root.findViewById(R.id.btn_setting);
        txt_myinfo.setOnClickListener(this);
        txt_privacy.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
    }
}
