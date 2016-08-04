package appframe.appframe.widget.swiperefresh;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.CandidateActivity;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Arith;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/11/9.
 */
public class SwipeRefreshXOrderCandidate extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
//    CheckBox cb_candidate;
    List<ConfirmedOrderDetail> confirmedOrderDetail = new ArrayList<ConfirmedOrderDetail>();
    boolean isshowcb = true;
//    TextView tv_content,tv_title,tv_name;

    public SwipeRefreshXOrderCandidate(Context context,List<ConfirmedOrderDetail> confirmedOrderDetail)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.confirmedOrderDetail = confirmedOrderDetail;
        //initData();
    }

    public SwipeRefreshXOrderCandidate(Context context,List<ConfirmedOrderDetail> confirmedOrderDetail,boolean isshowcb)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.confirmedOrderDetail = confirmedOrderDetail;
        this.isshowcb = isshowcb;
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
        if(confirmedOrderDetail == null)
        {
            return 0;
        }
        return confirmedOrderDetail.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_candidate, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            mHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            mHolder.cb_candidate = (CheckBox)convertView.findViewById(R.id.cb_candidate);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final ConfirmedOrderDetail item = confirmedOrderDetail.get(position);
//        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
//        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);
        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        if(item.getReceiver().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
        }

        if(item.getReceiver().getAvatar() != null && !item.getReceiver().getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getReceiver().getAvatar());




        mHolder.tv_name.setText(item.getReceiver().getName());
        mHolder.tv_price.setText(String.valueOf(item.getBid()));
        if(isshowcb)
        {
            mHolder.cb_candidate.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.cb_candidate.setVisibility(View.INVISIBLE);
        }

        mHolder.cb_candidate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item.getReceiver().setCheck("Checked");
                    CandidateActivity.tv_showtotal.setText(String.valueOf(Arith.add(Double.parseDouble(CandidateActivity.tv_showtotal.getText().toString()),item.getBid())));
                } else {
                    item.getReceiver().setCheck("UnCheck");
                    CandidateActivity.tv_showtotal.setText(String.valueOf(Arith.sub(Double.parseDouble(CandidateActivity.tv_showtotal.getText().toString()),item.getBid())));
                }

            }
        });

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return confirmedOrderDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name,tv_price;
        private CheckBox cb_candidate;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}
