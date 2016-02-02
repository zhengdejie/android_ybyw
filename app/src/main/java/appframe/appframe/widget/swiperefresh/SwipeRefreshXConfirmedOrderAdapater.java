package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.OrderCommentActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;

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
            mHolder.tv_pay = (TextView)convertView.findViewById(R.id.tv_pay);
            mHolder.tv_showserviceprovider = (TextView)convertView.findViewById(R.id.tv_showserviceprovider);
            mHolder.tv_showservicereceiver = (TextView)convertView.findViewById(R.id.tv_showservicereceiver);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
            mHolder.rb_totalvalue = (RatingBar)convertView.findViewById(R.id.rb_totalvalue);
            mHolder.ll_button = (LinearLayout)convertView.findViewById(R.id.ll_button);
            mHolder.imgbtn_conversation =(ImageButton)convertView.findViewById(R.id.imgbtn_conversation);
            mHolder.imgbtn_call =(ImageButton)convertView.findViewById(R.id.imgbtn_call);
            mHolder.tv_showstatus = (TextView) convertView.findViewById(R.id.tv_showstatus);
            mHolder.tv_numofconforder = (TextView) convertView.findViewById(R.id.tv_numofconforder);



            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final ConfirmedOrderDetail item = orderDetails.get(position);
        mHolder.txt_title.setText(item.getOrder().getTitle());
        if(item.getType() == 1)
        {
            mHolder.txt_bounty.setTextColor(context.getResources().getColor(R.color.green));
        }
        else
        {
            mHolder.txt_bounty.setTextColor(Color.RED);
        }
        SpannableString ss = new SpannableString( "￥" + String.valueOf(item.getOrder().getBounty()));
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHolder.txt_bounty.setText(ss);
        mHolder.txt_type.setText("类别:" + item.getOrder().getCategory());
        mHolder.txt_location.setText("地址:" + item.getOrder().getAddress());
        mHolder.tv_time.setText(item.getOrder().getCreatedAt());
        if(item.getOrder().getOrderer().getFNickName() != null && !item.getOrder().getOrderer().getFNickName().equals(""))
        {
            mHolder.tv_name.setText(item.getOrder().getOrderer().getFNickName());
        }
        else {
            mHolder.tv_name.setText(item.getOrder().getOrderer().getName());
        }
        if(item.getServiceProvider().getId() == Auth.getCurrentUserId()) {
            mHolder.tv_showserviceprovider.setText(item.getServiceProvider().getName() +"(我)");
        }
        else {
            mHolder.tv_showserviceprovider.setText(item.getServiceProvider().getName());
        }
        if(item.getServiceReceiver().getId() == Auth.getCurrentUserId())
        {
            mHolder.tv_showservicereceiver.setText(item.getServiceReceiver().getName() +"(我)");
        }
        else {
            mHolder.tv_showservicereceiver.setText(item.getServiceReceiver().getName());
        }

        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);
        ImageUtils.setImageUrl(mHolder.iv_avatar, item.getOrder().getOrderer().getAvatar());

        mHolder.rb_totalvalue.setRating((float)item.getOrder().getOrderer().getTotalPoint());

        if(item.getOrder().getPaymentMethod().equals("线上支付"))
        {
            mHolder.tv_pay.setText("已支付");
        }
        else
        {
            mHolder.tv_pay.setText("未支付");
        }
        mHolder.tv_numofconforder.setText(String.format("友帮了%d次",item.getOrder().getOrderer().getCompletedNumberOfOrder()));
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

                    if(item.getStatus() == 1) {
                        mHolder.btn_finish.setText(context.getResources().getString(R.string.service_done));
                        mHolder.btn_estimate.setText(context.getResources().getString(R.string.service_stop));
                    }
                    if(item.getStatus() == 5)
                    {
                        mHolder.btn_finish.setVisibility(View.GONE);
                        mHolder.btn_estimate.setVisibility(View.GONE);
                        mHolder.ll_button.setVisibility(View.GONE);
                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
                        mHolder.tv_showstatus.setText("等待对方付款");
                    }
                    if(item.getStatus() == 7)
                    {
                        mHolder.btn_finish.setText(context.getResources().getString(R.string.agree));
                        mHolder.btn_estimate.setText(context.getResources().getString(R.string.disagree));
                    }
                    if(item.getStatus() == 8)
                    {
                        mHolder.btn_finish.setVisibility(View.GONE);
                        mHolder.btn_estimate.setVisibility(View.GONE);
                        mHolder.ll_button.setVisibility(View.GONE);
                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
                        mHolder.tv_showstatus.setText("已拒绝退款申请，客服正在处理中，请耐心等待");
                    }
                }
                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId())
                {
                    if(item.getStatus() == 1) {
                        mHolder.btn_finish.setText("催单");
                        mHolder.btn_estimate.setText(context.getResources().getString(R.string.service_stop));
                    }
                    if(item.getStatus() == 5) {
                        mHolder.btn_finish.setText("付款");
                        mHolder.btn_estimate.setText(context.getResources().getString(R.string.refund_apply));
                    }
                    if(item.getStatus() == 7)
                    {
                        mHolder.btn_finish.setVisibility(View.GONE);
                        mHolder.btn_estimate.setVisibility(View.GONE);
                        mHolder.ll_button.setVisibility(View.GONE);
                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
                        mHolder.tv_showstatus.setText("已发送退款申请，等待对方确认");
                    }
                    if(item.getStatus() == 8)
                    {
                        mHolder.btn_finish.setVisibility(View.INVISIBLE);
                        mHolder.btn_estimate.setVisibility(View.VISIBLE);
                        mHolder.btn_estimate.setText(context.getResources().getString(R.string.dispute));
//                        mHolder.btn_finish.setVisibility(View.GONE);
//                        mHolder.btn_estimate.setVisibility(View.GONE);
//                        mHolder.ll_button.setVisibility(View.GONE);
//                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
//                        mHolder.tv_showstatus.setText("对方拒绝退款申请，客服正在处理中，请耐心等待");
                    }

                }


                break;
            case AppConfig.ORDERSTATUS_DONE:
                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId()) {
                    mHolder.btn_finish.setVisibility(View.INVISIBLE);
                    mHolder.btn_estimate.setVisibility(View.VISIBLE);
                    mHolder.btn_estimate.setText(context.getResources().getString(R.string.estimate));
                }
                else
                {
                    mHolder.btn_finish.setVisibility(View.GONE);
                    mHolder.btn_estimate.setVisibility(View.GONE);
                    mHolder.ll_button.setVisibility(View.GONE);
                }
                break;
            case AppConfig.ORDERSTATUS_CLOSE:
                mHolder.btn_finish.setVisibility(View.GONE);
                mHolder.btn_estimate.setVisibility(View.GONE);
                mHolder.ll_button.setVisibility(View.GONE);
                break;
            case AppConfig.ORDERSTATUS_DELETE:
                mHolder.btn_finish.setVisibility(View.INVISIBLE);
                mHolder.btn_estimate.setText(context.getResources().getString(R.string.delete));
                break;
            case AppConfig.ORDERSTATUS_MAIN:
                mHolder.btn_finish.setVisibility(View.INVISIBLE);
                mHolder.btn_estimate.setVisibility(View.GONE);
                break;
        }

        mHolder.btn_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.btn_estimate.getText() == context.getResources().getString(R.string.cancel_appley))
                {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("UserId", String.valueOf(Auth.getCurrentUserId()));

                    Http.request((Activity) context, API.ORDER_CANCEL, new Object[]{String.valueOf(item.getId()), Http.getURL(map)},
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    MyOrderFragment.tab_apply.performClick();
                                }
                            });
                }
                if(mHolder.btn_estimate.getText() == context.getResources().getString(R.string.estimate))
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ConfirmedOrderId",String.valueOf(item.getId()));
                    intent.setClass(context, OrderCommentActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                if(mHolder.btn_estimate.getText() == context.getResources().getString(R.string.refund_apply))
                {
                    Http.request((Activity) context, API.REJECT_PAYMENT, new Object[]{String.valueOf(item.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
                        @Override
                        public void onSuccess(ConfirmedOrderDetail result) {
                            super.onSuccess(result);

                            Toast.makeText(context, "申请退款提交成功", Toast.LENGTH_SHORT).show();
                            MyOrderFragment.tab_progess.performClick();
                        }
                    });
                }

                if(mHolder.btn_estimate.getText() == context.getResources().getString(R.string.disagree))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否确认拒绝退款并进入纠纷处理环节？您可能需要等待1-30天，让我们客服介入并解决")
                            .setCancelable(false)
                            .setPositiveButton("仍然拒绝退款",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            Http.request((Activity) context, API.REFUND_DISAGREE, new Object[]{String.valueOf(item.getId())}, Http.map(
                                                    "UserId", String.valueOf(Auth.getCurrentUserId())
                                            ), new Http.RequestListener<ConfirmedOrderDetail>() {
                                                @Override
                                                public void onSuccess(ConfirmedOrderDetail result) {
                                                    super.onSuccess(result);

                                                    Toast.makeText(context, "不同意退款提交成功", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    })
                            .setNegativeButton("返回",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.dismiss();
                                        }
                                    });
                    AlertDialog dialog = builder.create();
                    if (!dialog.isShowing()){
                        dialog.show();
                    }

                }

                if(mHolder.btn_estimate.getText() == context.getResources().getString(R.string.service_stop))
                {
                    Http.request((Activity) context, API.CHANGE_STATUS, new Object[]{String.valueOf(item.getId())},
                            Http.map("Status", "0"), new Http.RequestListener<ConfirmedOrderDetail>() {
                                @Override
                                public void onSuccess(ConfirmedOrderDetail result) {
                                    super.onSuccess(result);
                                    MyOrderFragment.tab_progess.performClick();
                                }
                            });
                }
                if(mHolder.btn_estimate.getText() == context.getResources().getString(R.string.delete))
                {
                    Http.request((Activity) context, API.CLOSE_ORDER, Http.map("Id", String.valueOf(item.getId())), new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);
                            MyOrderFragment.tab_close.performClick();
                        }
                    });
                }
                if(mHolder.btn_estimate.getText() == context.getResources().getString(R.string.dispute))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    //final EditText comment = new EditText(this);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View layout = inflater.inflate(R.layout.dialog_dispute, null);
                    final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                    builder.setTitle("申诉").setView(
                            layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!comment.getText().toString().equals("")) {
                                Http.request((Activity) context, API.ORDER_DISPUTE, new Object[]{String.valueOf(item.getId())},Http.map("Content",comment.getText().toString()), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        Toast.makeText(context, "申诉成功", Toast.LENGTH_SHORT).show();
                                        MyOrderFragment.tab_progess.performClick();
                                    }
                                });
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(context,"申诉内容不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }


            }
        });

        mHolder.btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.btn_finish.getText() == context.getResources().getString(R.string.service_done))
                {
                    Http.request((Activity)context, API.ORDER_COMPLETE,new Object[]{String.valueOf(item.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
                        @Override
                        public void onSuccess(ConfirmedOrderDetail result) {
                            super.onSuccess(result);

                            Toast.makeText(context, "完成提交成功", Toast.LENGTH_SHORT).show();
                            MyOrderFragment.tab_progess.performClick();
                        }
                    });
                }
                if(mHolder.btn_finish.getText() == "催单")
                {
                    Http.request((Activity)context, API.ORDER_HURRYUP,new Object[]{String.valueOf(item.getId())},
                    new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);

                            Toast.makeText(context, "对方已收到您的催单通知", Toast.LENGTH_SHORT).show();
                            MyOrderFragment.tab_progess.performClick();
                        }
                    });
                }
                if(mHolder.btn_finish.getText() == "付款")
                {
                    Http.request((Activity)context, API.ORDER_CONFIRMCOMPLETE,new Object[]{String.valueOf(item.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
                        @Override
                        public void onSuccess(ConfirmedOrderDetail result) {
                            super.onSuccess(result);

                            Toast.makeText(context, "确认完成", Toast.LENGTH_SHORT).show();
                            MyOrderFragment.tab_close.performClick();
                        }
                    });
                }
                if(mHolder.btn_finish.getText() == context.getResources().getString(R.string.agree))
                {
                    Http.request((Activity)context, API.REFUND_AGREE,new Object[]{String.valueOf(item.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
                        @Override
                        public void onSuccess(ConfirmedOrderDetail result) {
                            super.onSuccess(result);

                            Toast.makeText(context, "您已同意退款", Toast.LENGTH_SHORT).show();
                            MyOrderFragment.tab_progess.performClick();
                        }
                    });
                }
            }
        });

        mHolder.imgbtn_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = String.valueOf(item.getServiceProvider().getId() == Auth.getCurrentUserId() ? item.getServiceReceiver().getId() : item.getServiceProvider().getId());
                Intent intent = ls.getIMKit().getChattingActivityIntent(target);
                context.startActivity(intent);
            }
        });

        mHolder.imgbtn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId())
                {
                    if(!item.getServiceProvider().getMobile().equals("")) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getServiceProvider().getMobile())); //直接拨打电话android.intent.action.CALL
                        context.startActivity(phoneIntent);
                    }
                    else
                    {
                        Toast.makeText(context,"该用户不是用手机注册",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(!item.getServiceReceiver().getMobile().equals("")) {
                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getServiceReceiver().getMobile())); //直接拨打电话android.intent.action.CALL
                        context.startActivity(phoneIntent);
                    }
                    else
                    {
                        Toast.makeText(context,"该用户不是用手机注册",Toast.LENGTH_SHORT).show();
                    }
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
        private TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name,tv_showserviceprovider,tv_showservicereceiver,tv_pay,tv_showstatus,tv_numofconforder;
        private Button btn_estimate,btn_finish;
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
        private RatingBar rb_totalvalue;
        private LinearLayout ll_button;
        private ImageButton imgbtn_conversation,imgbtn_call;
    }

}

