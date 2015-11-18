package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.BaseActivity;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderReviewDetail;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SwipeRefreshXNearbyAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Nearby> nearby;

    public SwipeRefreshXNearbyAdapater(Context context,List<Nearby> nearby)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.nearby = nearby;
    }
    @Override
    public int getCount() {
        if(nearby == null)
        {
            return 0;
        }
        return nearby.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.swiperefreshx_nearby, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final Nearby item = nearby.get(position);
        mHolder.tv_name.setText(item.getName());
        mHolder.tv_distance.setText(String.valueOf(item.getDistance()));
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return nearby.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name;
        private TextView tv_distance;
    }
}
