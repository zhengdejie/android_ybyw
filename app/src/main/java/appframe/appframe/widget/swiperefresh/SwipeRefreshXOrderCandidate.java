package appframe.appframe.widget.swiperefresh;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.UserDetail;

/**
 * Created by Administrator on 2015/11/9.
 */
public class SwipeRefreshXOrderCandidate extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    CheckBox cb_candidate;
    List<UserDetail> userDetails = new ArrayList<UserDetail>();
    TextView tv_content,tv_title,tv_name;

    public SwipeRefreshXOrderCandidate(Context context,List<UserDetail> userDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.userDetails = userDetails;
        //initData();
    }

//    private void initData() {
//        for (UserDetail userDetail : userDetails) {
//            Type type = Type.UnCheck;
//            typeList.add(type);
//        }
//        cb_candidate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    typeList.set(position, Type.Checked);
//                } else {
//                    typeList.set(position, Type.UnCheck);
//                }
//
//            }
//        });
//    }

        @Override
    public int getCount() {
        if(userDetails == null)
        {
            return 0;
        }
        return userDetails.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.swiperefreshx_candidate, null);
        cb_candidate = (CheckBox)convertView.findViewById(R.id.cb_candidate);
        tv_name = (TextView)convertView.findViewById(R.id.tv_name);

        tv_name.setText(userDetails.get(position).getName());

        cb_candidate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userDetails.get(position).setCheck("Checked");
                } else {
                    userDetails.get(position).setCheck("UnCheck");
                }

            }
        });

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return userDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
