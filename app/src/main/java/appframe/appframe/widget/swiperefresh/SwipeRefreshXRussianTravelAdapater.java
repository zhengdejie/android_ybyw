package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.DateActivity;
import appframe.appframe.activity.PayActivity;
import appframe.appframe.dto.AnswerDetail;
import appframe.appframe.dto.MRussianTour;
import appframe.appframe.dto.RussianTour;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SwipeRefreshXRussianTravelAdapater  extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<MRussianTour> russianTourList = new ArrayList<MRussianTour>();



    public SwipeRefreshXRussianTravelAdapater(Context context, List<MRussianTour> russianTourList)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.russianTourList = russianTourList;

    }
    @Override
    public int getCount() {
        if(russianTourList == null)
        {
            return 0;
        }
        return russianTourList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SwipeRefreshXRussianTravelAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_russiantravel, null);
            mHolder = new SwipeRefreshXRussianTravelAdapater.ViewHolder();
            mHolder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            mHolder.tv_pay = (TextView) convertView.findViewById(R.id.tv_pay);
            mHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);


            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXRussianTravelAdapater.ViewHolder) convertView.getTag();
        }

        final MRussianTour item = russianTourList.get(position);

        mHolder.tv_money.setText(item.getApplicationDeposit());

        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        if(item.getUser().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));

        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));

        }
        if(item.getUser().getAvatar() != null && !item.getUser().getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getUser().getAvatar());

        if(item.getStatus().equals("101"))
        {
            mHolder.tv_status.setText("等待支付");
            mHolder.tv_pay.setVisibility(View.VISIBLE);
        }
        else if(item.getStatus().equals("103"))
        {
            mHolder.tv_status.setText("等待支付");
            mHolder.tv_pay.setVisibility(View.VISIBLE);
        }
        else if(item.getStatus().equals("104"))
        {
            mHolder.tv_status.setText("等待支付");
            mHolder.tv_pay.setVisibility(View.VISIBLE);
        }
        else if(item.getStatus().equals("102"))
        {
            mHolder.tv_status.setText("信息审核中");
            mHolder.tv_pay.setVisibility(View.GONE);
        }
        else if(item.getStatus().equals("200"))
        {
            mHolder.tv_status.setText("报名成功");
            mHolder.tv_pay.setVisibility(View.GONE);
        }
        else if(item.getStatus().equals("300"))
        {
            mHolder.tv_status.setText("报名失败");
            mHolder.tv_pay.setVisibility(View.GONE);
        }

        mHolder.tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newintent = new Intent();
                Bundle bundle = new Bundle();
                newintent.setClass(context, PayActivity.class);
                bundle.putSerializable("MRussianTour", item);
                newintent.putExtras(bundle);

                context.startActivity(newintent);
            }
        });
//        mHolder.tv_name.setText(item.getAnswerer().getName());
//        mHolder.tv_time.setText(item.getCreatedAt());
//        mHolder.tv_ordercomment.setText(item.getContent());


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return russianTourList.get(position);
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<MRussianTour> items ){
        for(MRussianTour order : items)
        {
            russianTourList.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_status,tv_money,tv_pay;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}

