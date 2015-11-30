package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.OrderCommentActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/8/7.
 */
public class SwipeRefreshXOrderAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
    TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name;
    com.android.volley.toolbox.NetworkImageView iv_avatar;
    Button btn_estimate;
    String from;
    LinearLayout ll_receiver,ll_button;
    boolean hasTopOrder;

    public SwipeRefreshXOrderAdapater(Context context, List<OrderDetails> orderDetails, String from)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderDetails = orderDetails;
        this.from = from;
    }
    public SwipeRefreshXOrderAdapater(Context context, List<OrderDetails> orderDetails, String from, boolean hasTopOrder)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderDetails = orderDetails;
        this.from = from;
        this.hasTopOrder = hasTopOrder;
    }
    @Override
    public int getCount() {
        if(orderDetails == null)
        {
            return 0;
        }
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
        tv_time = (TextView)convertView.findViewById(R.id.tv_time);
        tv_name = (TextView)convertView.findViewById(R.id.tv_name);
        iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
//        ll_receiver = (LinearLayout)convertView.findViewById(R.id.ll_receiver);
//        ll_button = (LinearLayout)convertView.findViewById(R.id.ll_button);
        txt_title.setText(orderDetails.get(position).getTitle());
        txt_bounty.setText("￥" + String.valueOf(orderDetails.get(position).getBounty()));
        txt_type.setText("类别：" + orderDetails.get(position).getCategory());
        txt_location.setText(orderDetails.get(position).getAddress());
        tv_time.setText(orderDetails.get(position).getCreatedAt());
        tv_name.setText(orderDetails.get(position).getOrderer().getName());
        ImageUtils.setImageUrl(iv_avatar, orderDetails.get(position).getOrderer().getAvatar());

        switch (from)
        {
            case AppConfig.ORDERSTATUS_APPLY:
                btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
                break;
            case AppConfig.ORDERSTATUS_PROGRESS:
                btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
                break;
            case AppConfig.ORDERSTATUS_DONE:
                btn_estimate.setText(context.getResources().getString(R.string.estimate));
                break;
            case AppConfig.ORDERSTATUS_CLOSE:
                btn_estimate.setText(context.getResources().getString(R.string.close));
                break;
            case AppConfig.ORDERSTATUS_DELETE:
                btn_estimate.setText(context.getResources().getString(R.string.close));
                break;
            case AppConfig.ORDERSTATUS_MAIN:
                btn_estimate.setVisibility(View.GONE);
                break;
        }

        btn_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (from)
                {
                    case AppConfig.ORDERSTATUS_APPLY:
                        Http.request((Activity) context, API.ORDER_CANCEL,new Object[]{String.valueOf(orderDetails.get(position).getId())},
                                Http.map("UserId",String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_PROGRESS:
                        Http.request((Activity) context, API.ORDER_CANCEL, new Object[]{String.valueOf(orderDetails.get(position).getId())},
                                Http.map("UserId", String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_DONE:
                        context.startActivity(new Intent(context, OrderCommentActivity.class));
                        break;
                    case AppConfig.ORDERSTATUS_CLOSE:
                        Http.request((Activity) context, API.CHANGE_STATUS, new Object[]{String.valueOf(orderDetails.get(position).getId())},
                                Http.map("Status", "0"), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_DELETE:
                        Http.request((Activity) context, API.CLOSE_ORDER, Http.map("Id", String.valueOf(orderDetails.get(position).getId())), new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);
                                orderDetails.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                        break;
                    case AppConfig.ORDERSTATUS_MAIN:
                        //btn_estimate.setVisibility(View.GONE);
                        break;
                }

            }
        });

        if(hasTopOrder  && position == 0) {
            convertView.setBackgroundColor(Color.GREEN);
        }
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
