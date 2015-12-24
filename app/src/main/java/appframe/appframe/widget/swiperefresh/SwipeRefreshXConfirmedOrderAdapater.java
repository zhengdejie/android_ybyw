package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.OrderCommentActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/11/25.
 */
public class SwipeRefreshXConfirmedOrderAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<ConfirmedOrderDetail> orderDetails = new ArrayList<ConfirmedOrderDetail>();
    String from;


    public SwipeRefreshXConfirmedOrderAdapater(Context context, List<ConfirmedOrderDetail> orderDetails, String from)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderDetails = orderDetails;
        this.from = from;
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
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_confirmedorder, null);
            mHolder = new ViewHolder();
            mHolder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            mHolder.txt_bounty = (TextView) convertView.findViewById(R.id.txt_bounty);
            mHolder.txt_type = (TextView)convertView.findViewById(R.id.txt_type);
            mHolder.txt_location = (TextView)convertView.findViewById(R.id.txt_location);
            mHolder.btn_estimate = (Button)convertView.findViewById(R.id.btn_estimate);
            mHolder.btn_finish = (Button)convertView.findViewById(R.id.btn_finish);
            mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            mHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            mHolder.tv_showserviceprovider = (TextView)convertView.findViewById(R.id.tv_showserviceprovider);
            mHolder.tv_showservicereceiver = (TextView)convertView.findViewById(R.id.tv_showservicereceiver);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
            mHolder.rb_totalvalue = (RatingBar)convertView.findViewById(R.id.rb_totalvalue);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final ConfirmedOrderDetail item = orderDetails.get(position);
        mHolder.txt_title.setText(item.getOrder().getTitle());
        mHolder.txt_bounty.setText("￥" + String.valueOf(item.getOrder().getBounty()));
        mHolder.txt_type.setText("类别：" + item.getOrder().getCategory());
        mHolder.txt_location.setText(item.getOrder().getAddress());
        mHolder.tv_time.setText(item.getOrder().getCreatedAt());
        mHolder.tv_name.setText(item.getOrder().getOrderer().getName());
        mHolder.tv_showserviceprovider.setText(item.getServiceProvider().getName());
        mHolder.tv_showservicereceiver.setText(item.getServiceReceiver().getName());
        ImageUtils.setImageUrl(mHolder.iv_avatar, item.getOrder().getOrderer().getAvatar());
        mHolder.rb_totalvalue.setRating((float)item.getOrder().getOrderer().getTotalPoint());

        switch (from)
        {
            case AppConfig.ORDERSTATUS_APPLY:
                mHolder.btn_finish.setVisibility(View.INVISIBLE);
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
                break;
            case AppConfig.ORDERSTATUS_PROGRESS:
                mHolder.btn_finish.setVisibility(View.VISIBLE);
                if (item.getServiceProvider().getId() == Auth.getCurrentUserId())
                {
                    mHolder.btn_finish.setText("完成");
                }
                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId())
                {
                    if(item.getStatus() == 1) {
                        mHolder.btn_finish.setText("催单");
                    }
                    if(item.getStatus() == 5) {
                        mHolder.btn_finish.setText("付款");
                    }
                }

                mHolder.btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
                break;
            case AppConfig.ORDERSTATUS_DONE:
                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId()) {
                    mHolder.btn_finish.setVisibility(View.INVISIBLE);
                    mHolder.btn_estimate.setVisibility(View.VISIBLE);
                    mHolder.btn_estimate.setText(context.getResources().getString(R.string.estimate));
                }
                else
                {
                    mHolder.btn_finish.setVisibility(View.INVISIBLE);
                    mHolder.btn_estimate.setVisibility(View.INVISIBLE);
                }
                break;
            case AppConfig.ORDERSTATUS_CLOSE:
                mHolder.btn_finish.setVisibility(View.INVISIBLE);
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.close));
                break;
            case AppConfig.ORDERSTATUS_DELETE:
                mHolder.btn_finish.setVisibility(View.INVISIBLE);
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.close));
                break;
            case AppConfig.ORDERSTATUS_MAIN:
                mHolder.btn_finish.setVisibility(View.INVISIBLE);
                mHolder.btn_estimate.setVisibility(View.GONE);
                break;
        }

        mHolder.btn_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (from)
                {
                    case AppConfig.ORDERSTATUS_APPLY:
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("UserId", String.valueOf(Auth.getCurrentUserId()));

                        Http.request((Activity) context, API.ORDER_CANCEL, new Object[]{String.valueOf(item.getId()),Http.getURL(map)},
                                new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_PROGRESS:
                        Map<String, String> map_progress = new HashMap<String, String>();
                        map_progress.put("UserId", String.valueOf(Auth.getCurrentUserId()));
                        Http.request((Activity) context, API.ORDER_CANCEL, new Object[]{String.valueOf(item.getId()),Http.getURL(map_progress)},
                                new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        orderDetails.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });
                        break;
                    case AppConfig.ORDERSTATUS_DONE:
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("ConfirmedOrderId",String.valueOf(item.getId()));
                        intent.setClass(context, OrderCommentActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
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

        mHolder.btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.btn_finish.getText() == "完成")
                {
                    Http.request((Activity)context, API.ORDER_COMPLETE,new Object[]{String.valueOf(item.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);

                            Toast.makeText(context, "完成提交成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                if(mHolder.btn_finish.getText() == "催单")
                {

                }
                if(mHolder.btn_finish.getText() == "付款")
                {
                    Http.request((Activity)context, API.ORDER_CONFIRMCOMPLETE,new Object[]{String.valueOf(item.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);

                            Toast.makeText(context, "确认完成", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
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

    static class ViewHolder
    {
        private TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name,tv_showserviceprovider,tv_showservicereceiver;
        private Button btn_estimate,btn_finish;
        com.android.volley.toolbox.NetworkImageView iv_avatar;
        private RatingBar rb_totalvalue;
    }

}

