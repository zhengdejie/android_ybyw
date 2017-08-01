package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.TourGuid;

/**
 * Created by Administrator on 2017/6/2.
 */

public class SwipeRefreshXMapAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<PoiInfo> tourGuids ;

    public SwipeRefreshXMapAdapater(Context context,List<PoiInfo> tourGuids)
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

        final SwipeRefreshXMapAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.swiperefreshx_map, null);
            mHolder = new SwipeRefreshXMapAdapater.ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);


            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXMapAdapater.ViewHolder) convertView.getTag();
        }

        final PoiInfo item = tourGuids.get(position);
        mHolder.tv_name.setText(item.name);
        mHolder.tv_address.setText(item.address);


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
    public  void addItems(List<PoiInfo> items ){
        for(PoiInfo tourGuid : items)
        {
            tourGuids.add(tourGuid);
        }
        notifyDataSetChanged();

    }
    static class ViewHolder
    {
        private TextView tv_name,tv_address;
    }
}
