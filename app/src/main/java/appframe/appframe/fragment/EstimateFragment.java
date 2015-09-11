package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import appframe.appframe.R;
import appframe.appframe.activity.FriendEstimateActivity;
import appframe.appframe.activity.MyInfoActivity;
import appframe.appframe.activity.OrderEstimateActivity;
import appframe.appframe.activity.PrivacyActivity;
import appframe.appframe.activity.SelfEstimateActivity;
import appframe.appframe.activity.SettingActivity;

/**
 * Created by Administrator on 2015/8/7.
 */
public class EstimateFragment extends BaseFragment implements View.OnClickListener{
    Button btn_self,btn_friend,btn_order;
    View root;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root  = inflater.inflate(R.layout.fragment_estimate, null);
        init();
        return root;
    }
    public void init()
    {
        btn_self = (Button) root.findViewById(R.id.btn_self);
        btn_friend = (Button) root.findViewById(R.id.btn_friend);
        btn_order = (Button) root.findViewById(R.id.btn_order);
        btn_self.setOnClickListener(this);
        btn_friend.setOnClickListener(this);
        btn_order.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_self:
                startActivity(new Intent(getActivity(),SelfEstimateActivity.class));
                break;
            case R.id.btn_friend:
                startActivity(new Intent(getActivity(), FriendEstimateActivity.class));
                break;
            case R.id.btn_order:
                startActivity(new Intent(getActivity(), OrderEstimateActivity.class));
                break;

        }
    }
}
