package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.AvatarZoomActivity;
import appframe.appframe.activity.CandidateActivity;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.MyMissionActivity;
import appframe.appframe.activity.MyQuestionActivity;
import appframe.appframe.activity.OrderCommentActivity;
import appframe.appframe.activity.PayActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2016/4/25.
 */
public class SwipeRefreshXMyMissionAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
    String imgURL;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();



    public SwipeRefreshXMyMissionAdapater(Context context, List<OrderDetails> orderDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderDetails = orderDetails;
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
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_mymission, null);
            mHolder = new ViewHolder();
            mHolder.txt_title = (TextView)convertView.findViewById(R.id.txt_title);
            mHolder.txt_bounty = (TextView)convertView.findViewById(R.id.txt_bounty);
            mHolder.txt_type = (TextView)convertView.findViewById(R.id.txt_type);
            mHolder.txt_location = (TextView)convertView.findViewById(R.id.txt_location);
            mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
            mHolder.tv_pay = (TextView)convertView.findViewById(R.id.tv_pay);
            mHolder.tv_delete = (TextView)convertView.findViewById(R.id.tv_delete);
            mHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
            mHolder.rl_bottom = (RelativeLayout)convertView.findViewById(R.id.rl_bottom);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.drawable.listview_item_pressed);
        final OrderDetails item = orderDetails.get(position);

//        if(item.getPhotos() != null && item.getPhotos() != "") {
//            List<String> photoPath = new ArrayList<String>();
//            for (String photsCount : item.getPhotos().toString().split(",")) {
//                photoPath.add(photsCount);
//            }
//
//        }
        mHolder.iv_avatar.setDefaultImageResId(R.drawable.defaultphoto);

        if(item.getPhotos() != null && !item.getPhotos().equals(""))
        {
            imgURL = item.getPhotos().split(",")[0];

        }
        else
        {
            imgURL = "";
        }
        ImageUtils.setImageUrl(mHolder.iv_avatar, imgURL);
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
        mHolder.txt_type.setText("所属类目 : " + item.getCategory());

        mHolder.txt_location.setText("");
        if(item.getLocationAnonymity() == 1) {
            mHolder.txt_location.setText("");
        }
        else
        {
            mHolder.txt_location.setText("地址:" + item.getAddress());
            if(item.getUserLocation() != null && item.getUserLocation().getProvince() != null && item.getUserLocation().getCity() != null && item.getUserLocation().getDistrict() != null) {
                mHolder.txt_location.setText("地址:" + item.getUserLocation().getProvince() + item.getUserLocation().getCity() + item.getUserLocation().getDistrict());
            }
            else
            {
                mHolder.txt_location.setText("地址:未知");
            }
        }
        mHolder.tv_time.setText(item.getCreatedAt());

//        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);

        mHolder.tv_delete.setText("删除");
        mHolder.tv_pay.setText("接单候选人");
        mHolder.tv_pay.setEnabled(false);
        mHolder.rl_bottom.setVisibility(View.VISIBLE);
        if(item.getCandidate() != null && item.getCandidate().size() != 0)
        {
            mHolder.tv_pay.setEnabled(true);
            mHolder.tv_pay.setText("接单候选人");
            mHolder.tv_delete.setText("删除");
            mHolder.tv_status.setText("有人接单");
        }
        else
        {
            mHolder.tv_status.setText("等待接单");
        }

        if(item.getPaymentMethod().equals("线上支付") == true && item.getType() == 2 && item.getBossPaid() == 0) {
            mHolder.tv_pay.setEnabled(true);
            if(item.getCandidate() != null && item.getCandidate().size() != 0)
            {
                mHolder.tv_pay.setText("付款");
                mHolder.tv_delete.setText("取消付款");
                mHolder.tv_status.setText("等待支付");
            }
            else
            {
                mHolder.tv_pay.setText("发单付款");
                mHolder.tv_delete.setText("删除");
                mHolder.tv_status.setText("等待支付");
            }
        }

        if(item.getOrderStatus().equals("1"))
        {
            mHolder.rl_bottom.setVisibility(View.GONE);
            mHolder.tv_status.setText("已关闭");
        }
        //保留单子却没付款  接单人没有接单成功
        if(item.getOrderStatus().equals("2"))
        {
            mHolder.tv_pay.setText("保留单子付款");
            mHolder.tv_delete.setText("取消付款");
            mHolder.tv_status.setText("等待支付");
        }
        //保留单子却没付款  接单人已经接单成功
        if(item.getOrderStatus().equals("4"))
        {
            mHolder.tv_pay.setText("保留单子付款");
            mHolder.tv_delete.setText("删除");
            mHolder.tv_status.setText("等待支付");
        }



//        mHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(context, FriendsInfoActivity.class);
//                bundle.putSerializable("OrderDetails", item);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });



        mHolder.tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.tv_pay.getText().equals("发单付款")) {
                    intent.setClass(context, PayActivity.class);
                    bundle.putSerializable("OrderDetailsPay", item);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else if(mHolder.tv_pay.getText().equals("付款"))
                {
                    intent.setClass(context, PayActivity.class);
                    bundle.putSerializable("Candidate", item);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else if(mHolder.tv_pay.getText().equals("保留单子付款"))
                {
                    intent.setClass(context, PayActivity.class);
                    bundle.putSerializable("Candidate", item);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                else
                {
                    intent.setClass(context, CandidateActivity.class);
                    bundle.putSerializable("Candidate", item);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

        mHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.tv_delete.getText().equals("删除")) {
                    Http.request((Activity) context, API.CLOSE_ORDER, Http.map("Id", String.valueOf(item.getId())), new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);
                            context.startActivity(new Intent(context, context.getClass()));
//                            orderDetails.remove(item);
//                            notifyDataSetChanged();
                        }
                    });
                }

                else
                {
                    Http.request((Activity) context, API.CANCEL_PAY, Http.map("OrderId", String.valueOf(item.getId())), new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);
                            context.startActivity(new Intent(context, context.getClass()));
//                                    orderDetails.remove(item);
//                                    notifyDataSetChanged();
                        }
                    });
                }
            }
        });

//        mHolder.btn_candidate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(context, CandidateActivity.class);
//                bundle.putSerializable("Candidate", item);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });



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
        private TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_pay,tv_delete,tv_status;
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
        private RelativeLayout rl_bottom;

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

