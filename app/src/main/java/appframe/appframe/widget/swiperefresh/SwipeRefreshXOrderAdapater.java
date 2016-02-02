package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
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
//    TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name;
//    com.android.volley.toolbox.NetworkImageView iv_avatar;
    //Button btn_estimate;
    String from;
//    LinearLayout ll_receiver,ll_button;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_order, null);
            mHolder = new ViewHolder();
            mHolder.txt_title = (TextView)convertView.findViewById(R.id.txt_title);
            mHolder.txt_bounty = (TextView)convertView.findViewById(R.id.txt_bounty);
            mHolder.txt_type = (TextView)convertView.findViewById(R.id.txt_type);
            mHolder.txt_location = (TextView)convertView.findViewById(R.id.txt_location);
            mHolder.btn_estimate = (Button)convertView.findViewById(R.id.btn_estimate);
            mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            mHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
            mHolder.rb_totalvalue = (RatingBar)convertView.findViewById(R.id.rb_totalvalue);
            mHolder.tv_pay = (TextView)convertView.findViewById(R.id.tv_pay);
            mHolder.ll_button = (LinearLayout)convertView.findViewById(R.id.ll_button);
            mHolder.tv_numofconforder = (TextView)convertView.findViewById(R.id.tv_numofconforder);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.drawable.listview_item_pressed);
        final OrderDetails item = orderDetails.get(position);

        mHolder.txt_title.setText(item.getTitle());
        if(item.getType() == 1)
        {
            mHolder.txt_bounty.setTextColor(context.getResources().getColor(R.color.green));
        }
        else
        {
            mHolder.txt_bounty.setTextColor(Color.RED);
        }
        SpannableString ss = new SpannableString( "￥" + String.valueOf(item.getBounty()));
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHolder.txt_bounty.setText(ss);
        mHolder.txt_type.setText("类别:" + item.getCategory());
        mHolder.txt_location.setText("地址:" + item.getAddress());
        mHolder.tv_time.setText(item.getCreatedAt());
        if(item.getOrderer().getFNickName() != null && !item.getOrderer().getFNickName().equals(""))
        {
            mHolder.tv_name.setText(item.getOrderer().getFNickName());
        }
        else {
            mHolder.tv_name.setText(item.getOrderer().getName());
        }
        mHolder.rb_totalvalue.setRating((float) item.getOrderer().getTotalPoint());

        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);
        ImageUtils.setImageUrl(mHolder.iv_avatar, item.getOrderer().getAvatar());
//        ImageUtils.setImageUrl(mHolder.iv_avatar, item.getOrderer().getAvatar());
//        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
//        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);
        if(item.getPaymentMethod().equals("线上支付"))
        {
            mHolder.tv_pay.setText("已支付");
        }
        else
        {
            mHolder.tv_pay.setText("未支付");
        }
        mHolder.tv_numofconforder.setText(String.format("友帮了%d次",item.getOrderer().getCompletedNumberOfOrder()));
        switch (from)
        {
            case AppConfig.ORDERSTATUS_APPLY:
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
                break;
            case AppConfig.ORDERSTATUS_PROGRESS:
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
                break;
            case AppConfig.ORDERSTATUS_DONE:
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.estimate));
                break;
            case AppConfig.ORDERSTATUS_CLOSE:
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.close));
                break;
            case AppConfig.ORDERSTATUS_DELETE:
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.delete));
                break;
            case AppConfig.ORDERSTATUS_MAIN:
                mHolder.btn_estimate.setVisibility(View.GONE);
                mHolder.ll_button.setVisibility(View.GONE);
                break;
        }

        mHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(context, FriendsInfoActivity.class);
                bundle.putSerializable("OrderDetails", item);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        mHolder.btn_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (from)
                {
                    case AppConfig.ORDERSTATUS_APPLY:
                        Http.request((Activity) context, API.ORDER_CANCEL,new Object[]{String.valueOf(item.getId())},
                                Http.map("UserId",String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(item);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_PROGRESS:
                        Http.request((Activity) context, API.ORDER_CANCEL, new Object[]{String.valueOf(item.getId())},
                                Http.map("UserId", String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(item);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_DONE:
                        context.startActivity(new Intent(context, OrderCommentActivity.class));
                        break;
                    case AppConfig.ORDERSTATUS_CLOSE:
                        Http.request((Activity) context, API.CHANGE_STATUS, new Object[]{String.valueOf(item.getId())},
                                Http.map("Status", "0"), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(item);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_DELETE:
                        Http.request((Activity) context, API.CLOSE_ORDER, Http.map("Id", String.valueOf(item.getId())), new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);
                                orderDetails.remove(item);
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

        if(hasTopOrder  &&  position == 0) {
            convertView.setBackgroundColor(Color.GREEN);
        }
        else
        {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return orderDetails.get(position);
    }

    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<OrderDetails> items ){
        for(OrderDetails order : items)
        {
            orderDetails.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name,tv_pay,tv_numofconforder;
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
        private Button btn_estimate;
        private RatingBar rb_totalvalue;
        private LinearLayout ll_button;
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
