package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.OrderCommentActivity;
import appframe.appframe.dto.OrderDetails;

/**
 * Created by Administrator on 2015/8/7.
 */
public class SwipeRefreshXOrderAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
    TextView txt_title,txt_bounty,txt_type,txt_location;
    Button btn_estimate;

    public SwipeRefreshXOrderAdapater(Context context, List<OrderDetails> orderDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderDetails = orderDetails;
    }
    @Override
    public int getCount() {
        return orderDetails.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.swiperefreshx_order, null);
        txt_title = (TextView)convertView.findViewById(R.id.txt_title);
        txt_bounty = (TextView)convertView.findViewById(R.id.txt_bounty);
        txt_type = (TextView)convertView.findViewById(R.id.txt_type);
        txt_location = (TextView)convertView.findViewById(R.id.txt_location);
        btn_estimate = (Button)convertView.findViewById(R.id.btn_estimate);
        txt_title.setText(orderDetails.get(position).getTitle());
        txt_bounty.setText("￥" + String.valueOf(orderDetails.get(position).getBounty()));
        txt_type.setText("类别：" + orderDetails.get(position).getCategory());
        txt_location.setText(orderDetails.get(position).getPosition());
        btn_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("OrderId",orderDetails.get(position).getId());
                context.startActivity(new Intent(context, OrderCommentActivity.class));
            }
        });
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return orderDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    private View makeItemView() {
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        // 使用View的对象itemView与R.layout.item关联
//        View itemView = inflater.inflate(R.layout.swiperefreshx_order, null);
//
//
//        return itemView;
//    }
}
