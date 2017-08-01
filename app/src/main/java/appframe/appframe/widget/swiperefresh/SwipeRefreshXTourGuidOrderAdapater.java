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
import appframe.appframe.dto.TourGuid;

/**
 * Created by Administrator on 2017/5/12.
 */

public class SwipeRefreshXTourGuidOrderAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<TourGuid> tourGuids ;

    public SwipeRefreshXTourGuidOrderAdapater(Context context,List<TourGuid> tourGuids)
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

        final SwipeRefreshXTourGuidOrderAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.swiperefreshx_tourguidorder, null);
            mHolder = new SwipeRefreshXTourGuidOrderAdapater.ViewHolder();
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            mHolder.tv_personality = (TextView) convertView.findViewById(R.id.tv_personality);
            mHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXTourGuidOrderAdapater.ViewHolder) convertView.getTag();
        }

        final TourGuid item = tourGuids.get(position);
        mHolder.tv_title.setText(item.getName());
        mHolder.tv_num.setText(item.getAge());
        mHolder.tv_personality.setText(item.getPersonality());
        mHolder.tv_price.setText(item.getPrice());
//        ImageUtils.setImageUrl(mHolder.civ_avatar, recommendOrder.getUserDetail().getAvatar());

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
    public  void addItems(List<TourGuid> items ){
        for(TourGuid tourGuid : items)
        {
            tourGuids.add(tourGuid);
        }
        notifyDataSetChanged();

    }
    static class ViewHolder
    {
        private TextView tv_title,tv_personality,tv_price,tv_num;
        private ImageView iv_avatar;
    }
}
