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
import appframe.appframe.dto.OrderDetails;

/**
 * Created by Administrator on 2015/9/8.
 */
public class SwipeRefreshXFriendEstimateAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    //List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();

    public SwipeRefreshXFriendEstimateAdapater(Context context)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        //this.orderDetails = orderDetails;
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.swiperefreshx_friendestimate, null);
//        txt_title = (TextView)convertView.findViewById(R.id.txt_title);
//        txt_bounty = (TextView)convertView.findViewById(R.id.txt_bounty);
//        txt_type = (TextView)convertView.findViewById(R.id.txt_type);
//        txt_location = (TextView)convertView.findViewById(R.id.txt_location);
//        txt_title.setText(orderDetails.get(position).getTitle());
//        txt_bounty.setText("￥" + String.valueOf(orderDetails.get(position).getBounty()));
//        txt_type.setText("类别：" + orderDetails.get(position).getCategory());
//        txt_location.setText(orderDetails.get(position).getPosition());
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}