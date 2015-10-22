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
    List<Nearby> nearby = new ArrayList<Nearby>();
    TextView tv_content,tv_title,tv_name;

    public SwipeRefreshXNearbyAdapater(Context context,List<Nearby> nearby)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.nearby = nearby;
    }
    @Override
    public int getCount() {
        return nearby.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.swiperefreshx_nearby, null);
//        tv_title = (TextView)convertView.findViewById(R.id.tv_title);
//        tv_content = (TextView)convertView.findViewById(R.id.tv_content);
//        tv_name = (TextView)convertView.findViewById(R.id.tv_name);
//        tv_title.setText(orderReviewDetails.get(position).getTitle());
//        tv_content.setText(orderReviewDetails.get(position).getContent());
//        tv_name.setText(orderReviewDetails.get(position).getCommontatorName());

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
}
