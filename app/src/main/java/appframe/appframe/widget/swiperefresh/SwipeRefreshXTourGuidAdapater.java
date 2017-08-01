package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.GuideTour;
import appframe.appframe.dto.TourGuid;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SwipeRefreshXTourGuidAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<GuideTour> tourGuids ;

    public SwipeRefreshXTourGuidAdapater(Context context,List<GuideTour> tourGuids)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.tourGuids = tourGuids;
    }
    @Override
    public int getCount() {
        return tourGuids == null ? 0 : tourGuids.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SwipeRefreshXTourGuidAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.swiperefreshx_tourguid, null);
            mHolder = new SwipeRefreshXTourGuidAdapater.ViewHolder();
            mHolder.tv_sex = (TextView) convertView.findViewById(R.id.tv_sex);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_personality = (TextView) convertView.findViewById(R.id.tv_personality);
            mHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            mHolder.civ_avatar = (appframe.appframe.utils.CircleImageViewCustomer) convertView.findViewById(R.id.civ_avatar);
            mHolder.iv_avatar = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.iv_avatar);
            mHolder.iv_coupon = (ImageView) convertView.findViewById(R.id.iv_coupon);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXTourGuidAdapater.ViewHolder) convertView.getTag();
        }

        final GuideTour item = tourGuids.get(position);
        mHolder.tv_name.setText(item.getUser().getName());
        mHolder.tv_sex.setText(item.getGender());
        mHolder.tv_personality.setText(item.getItinerary());
//        int total = (int)(item.getHourlyRatePrice() * item.getMinimumHours());
        StringBuilder sb = new StringBuilder();
        sb.append(item.getHourlyRatePrice()).append("元/小时");
        mHolder.tv_price.setText(sb);
        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.civ_avatar.setVisibility(View.INVISIBLE);
        if(item.getHourlyRatePrice() == 58 || item.getHourlyRatePrice() == 68)
        {
            mHolder.iv_coupon.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_coupon.setVisibility(View.GONE);
        }

        if(item.getUser().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
        }
        if(item.getUser().getAvatar() != null)
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.civ_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.civ_avatar.setVisibility(View.INVISIBLE);
        }

        ImageUtils.setImageUrl(mHolder.civ_avatar, item.getUser().getAvatar());

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return tourGuids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<GuideTour> items ){
        for(GuideTour tourGuid : items)
        {
            tourGuids.add(tourGuid);
        }
        notifyDataSetChanged();

    }
    static class ViewHolder
    {
        private TextView tv_name,tv_sex,tv_personality,tv_price;
        private de.hdodenhof.circleimageview.CircleImageView iv_avatar;
        private appframe.appframe.utils.CircleImageViewCustomer civ_avatar;
        private ImageView iv_coupon;
    }
}
