package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.OrderCommentActivity;
import appframe.appframe.activity.PayActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.PayResult;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.CompletedCountDown;

/**
 * Created by Administrator on 2015/11/25.
 */
public class SwipeRefreshXConfirmedOrderAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<ConfirmedOrderDetail> orderDetails = new ArrayList<ConfirmedOrderDetail>();
    CompletedCountDown mc;
    String from;
    Drawable serviceProviderDrawable,serviceReceiverDrawable;
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
//                case SDK_CHECK_FLAG: {
//                    Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
//                    break;
//                }
                default:
                    break;
            }
        };
    };

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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_confirmedorder, null);
            mHolder = new ViewHolder();
            mHolder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            mHolder.txt_bounty = (TextView) convertView.findViewById(R.id.txt_bounty);
//            mHolder.txt_type = (TextView)convertView.findViewById(R.id.txt_type);
            mHolder.txt_location = (TextView)convertView.findViewById(R.id.txt_location);
//            mHolder.btn_estimate = (Button)convertView.findViewById(R.id.btn_estimate);
//            mHolder.btn_finish = (Button)convertView.findViewById(R.id.btn_finish);
            mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            mHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
//            mHolder.tv_pay = (TextView)convertView.findViewById(R.id.tv_pay);
//            mHolder.tv_showserviceprovider = (TextView)convertView.findViewById(R.id.tv_showserviceprovider);
//            mHolder.tv_showservicereceiver = (TextView)convertView.findViewById(R.id.tv_showservicereceiver);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
//            mHolder.rb_totalvalue = (RatingBar)convertView.findViewById(R.id.rb_totalvalue);
//            mHolder.ll_button = (LinearLayout)convertView.findViewById(R.id.ll_button);
//            mHolder.imgbtn_conversation =(ImageButton)convertView.findViewById(R.id.imgbtn_conversation);
//            mHolder.imgbtn_call =(ImageButton)convertView.findViewById(R.id.imgbtn_call);
            mHolder.tv_showstatus = (TextView) convertView.findViewById(R.id.tv_showstatus);
//            mHolder.tv_numofconforder = (TextView) convertView.findViewById(R.id.tv_numofconforder);
//            mHolder.tv_countdown = (TextView) convertView.findViewById(R.id.tv_countdown);

            mHolder.tv_service = (TextView)convertView.findViewById(R.id.tv_service);
            mHolder.tv_bid = (TextView)convertView.findViewById(R.id.tv_bid);
            mHolder.tv_total = (TextView)convertView.findViewById(R.id.tv_total);
            mHolder.tv_estimate = (TextView)convertView.findViewById(R.id.tv_estimate);
            mHolder.tv_finish = (TextView)convertView.findViewById(R.id.tv_finish);
            mHolder.iv_service = (ImageView)convertView.findViewById(R.id.iv_service);
            mHolder.iv_photos = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_photos);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final ConfirmedOrderDetail item = orderDetails.get(position);
        mHolder.txt_title.setText(item.getOrder().getTitle());
        mHolder.tv_bid.setText("￥" + String.valueOf(item.getBid()));
        mHolder.txt_bounty.setText("￥" + String.valueOf(item.getOrder().getBounty()));
        mHolder.tv_total.setText("￥" + String.valueOf(item.getBid()));
//        if(item.getType() == 1)
//        {
//            mHolder.txt_bounty.setTextColor(context.getResources().getColor(R.color.green));
//        }
//        else
//        {
//            mHolder.txt_bounty.setTextColor(Color.RED);
//        }
//        SpannableString ss = new SpannableString( "￥" + String.valueOf(item.getOrder().getBounty()));
//        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mHolder.txt_bounty.setText(ss);
//        mHolder.txt_type.setText("类别:" + item.getOrder().getCategory());
        mHolder.txt_location.setText(item.getOrder().getAddress());
        mHolder.tv_time.setText(item.getOrder().getCreatedAt());
//        mHolder.tv_countdown.setVisibility(View.GONE);


        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);
        serviceProviderDrawable = context.getResources().getDrawable( R.drawable.servicereprovider);
        serviceReceiverDrawable = context.getResources().getDrawable( R.drawable.servicereceiver);
        if(item.getServiceProvider().getId() == Auth.getCurrentUserId()) {
            ImageUtils.setImageUrl(mHolder.iv_avatar, item.getServiceReceiver().getAvatar());
            mHolder.tv_service.setText("被服务方");
            mHolder.iv_service.setImageDrawable(serviceReceiverDrawable);
            if(item.getServiceReceiver().getFNickName() != null && !item.getServiceReceiver().getFNickName().equals(""))
            {
                mHolder.tv_name.setText(item.getServiceReceiver().getFNickName());
            }
            else {
                mHolder.tv_name.setText(item.getServiceReceiver().getName());
            }
        }
        else {
            ImageUtils.setImageUrl(mHolder.iv_avatar, item.getServiceProvider().getAvatar());
            mHolder.tv_service.setText("被服务方");
            mHolder.iv_service.setImageDrawable(serviceProviderDrawable);
            if(item.getServiceProvider().getFNickName() != null && !item.getServiceProvider().getFNickName().equals(""))
            {
                mHolder.tv_name.setText(item.getServiceProvider().getFNickName());
            }
            else {
                mHolder.tv_name.setText(item.getServiceProvider().getName());
            }
        }
//        if(item.getServiceReceiver().getId() == Auth.getCurrentUserId())
//        {
//            mHolder.tv_showservicereceiver.setText(item.getServiceReceiver().getName() +"(我)");
//        }
//        else {
//            mHolder.tv_showservicereceiver.setText(item.getServiceReceiver().getName());
//        }



//        if(item.getOrder().getType() == 1) {
//            mHolder.rb_totalvalue.setRating((float)item.getOrder().getOrderer().getTotalWorkerPoint());
//        }
//        else if(item.getOrder().getType() == 2) {
//            mHolder.rb_totalvalue.setRating((float) item.getOrder().getOrderer().getTotalBossPoint());
//        }
//
//
//        if(item.getOrder().getPaymentMethod().equals("线上支付"))
//        {
//            mHolder.tv_pay.setText("已支付");
//        }
//        else
//        {
//            mHolder.tv_pay.setText("未支付");
//        }
//        mHolder.tv_numofconforder.setText(String.format("友帮了%d次",item.getOrder().getOrderer().getCompletedNumberOfOrder()));
        switch (from)
        {
            case AppConfig.ORDERSTATUS_APPLY:
                if(item.getOrder().getType() == 1 && item.getOrder().getBossPaid() != 1) {
                    mHolder.tv_finish.setVisibility(View.VISIBLE);
                    mHolder.tv_finish.setText("支付");
                }
                else
                {
                    mHolder.tv_finish.setVisibility(View.GONE);
                }
                mHolder.tv_estimate.setText(context.getResources().getString(R.string.cancel_appley));
                break;
            case AppConfig.ORDERSTATUS_PROGRESS:
                mHolder.tv_finish.setVisibility(View.VISIBLE);
                if (item.getServiceProvider().getId() == Auth.getCurrentUserId())
                {

                    if(item.getStatus() == 1) {
                        mHolder.tv_finish.setText(context.getResources().getString(R.string.service_done));
                        mHolder.tv_estimate.setText(context.getResources().getString(R.string.service_stop));
                    }
                    if(item.getStatus() == 5)
                    {
                        mHolder.tv_finish.setVisibility(View.GONE);
                        mHolder.tv_estimate.setVisibility(View.GONE);
//                        mHolder.ll_button.setVisibility(View.GONE);
                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
                        mHolder.tv_showstatus.setText("等待对方付款,您收到的费用将扣除1%为平台服务费");
                    }
                    if(item.getStatus() == 7)
                    {
                        mHolder.tv_finish.setText(context.getResources().getString(R.string.agree));
                        mHolder.tv_estimate.setText(context.getResources().getString(R.string.disagree));
                    }
                    if(item.getStatus() == 8)
                    {
                        mHolder.tv_finish.setVisibility(View.GONE);
                        mHolder.tv_estimate.setVisibility(View.GONE);
//                        mHolder.ll_button.setVisibility(View.GONE);
                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
                        mHolder.tv_showstatus.setText("已拒绝退款申请，客服正在处理中，请耐心等待");
                    }
                }
                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId())
                {
                    if(item.getStatus() == 1) {
                        mHolder.tv_finish.setText("催单");
                        mHolder.tv_estimate.setText(context.getResources().getString(R.string.service_stop));
                    }
                    if(item.getStatus() == 5) {
                        mHolder.tv_finish.setText("付款");
                        mHolder.tv_estimate.setText(context.getResources().getString(R.string.refund_apply));
//                        mHolder.tv_countdown.setVisibility(View.VISIBLE);
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        Calendar rightNow = Calendar.getInstance();
//                        try {
//                            String[] date= item.getCompleteDate().toString().split(" ");
//                            String runTime = date[0] + " 01:00:00";
//                            Date completedRunTime = dateFormat.parse(runTime);
//                            Date completedDate = dateFormat.parse(item.getCompleteDate().toString());
//                            rightNow.setTime(completedDate);
//                            if(completedDate.before(completedRunTime))
//                            {
//                                rightNow.add(Calendar.DAY_OF_YEAR, 7);//日期加7天
//                            }
//                            else
//                            {
//                                rightNow.add(Calendar.DAY_OF_YEAR, 8);//日期加8天
//                            }
//
//                            String countDown = simpleDateFormat.format(rightNow.getTime());
//                            Date countDownTime = dateFormat.parse(countDown + " 01:00:00");
//                            Date nowdate = new Date();
//                            mc = new CompletedCountDown(countDownTime.getTime() -nowdate.getTime(), 1000, mHolder.tv_countdown);
//                            mc.start();
//                        }
//                        catch (ParseException e)
//                        {
//                            Log.e("ParseException", e.getMessage());
//                        }
                    }
                    if(item.getStatus() == 7)
                    {
                        mHolder.tv_finish.setVisibility(View.GONE);
                        mHolder.tv_estimate.setVisibility(View.GONE);
//                        mHolder.ll_button.setVisibility(View.GONE);
                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
                        mHolder.tv_showstatus.setText("已发送退款申请，等待对方确认");
                    }
                    if(item.getStatus() == 8)
                    {
                        mHolder.tv_finish.setVisibility(View.GONE);
                        mHolder.tv_estimate.setVisibility(View.VISIBLE);
                        mHolder.tv_estimate.setText(context.getResources().getString(R.string.dispute));
//                        mHolder.btn_finish.setVisibility(View.GONE);
//                        mHolder.btn_estimate.setVisibility(View.GONE);
//                        mHolder.ll_button.setVisibility(View.GONE);
//                        mHolder.tv_showstatus.setVisibility(View.VISIBLE);
//                        mHolder.tv_showstatus.setText("对方拒绝退款申请，客服正在处理中，请耐心等待");
                    }

                }


                break;
            case AppConfig.ORDERSTATUS_DONE:
                mHolder.tv_finish.setVisibility(View.GONE);
                mHolder.tv_estimate.setVisibility(View.VISIBLE);
                mHolder.tv_estimate.setText(context.getResources().getString(R.string.estimate));
//                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId()) {
//                    mHolder.btn_finish.setVisibility(View.INVISIBLE);
//                    mHolder.btn_estimate.setVisibility(View.VISIBLE);
//                    mHolder.btn_estimate.setText(context.getResources().getString(R.string.estimate));
//                }
//                else
//                {
//                    mHolder.btn_finish.setVisibility(View.GONE);
//                    mHolder.btn_estimate.setVisibility(View.GONE);
//                    mHolder.ll_button.setVisibility(View.GONE);
//                }
                break;
            case AppConfig.ORDERSTATUS_CLOSE:
                mHolder.tv_finish.setVisibility(View.GONE);
                mHolder.tv_estimate.setVisibility(View.GONE);
//                mHolder.ll_buttotton.setVisibility(View.GONE);
                break;
            case AppConfig.ORDERSTATUS_DELETE:
                mHolder.tv_finish.setVisibility(View.GONE);
                mHolder.tv_estimate.setText(context.getResources().getString(R.string.delete));
                break;
            case AppConfig.ORDERSTATUS_MAIN:
                mHolder.tv_finish.setVisibility(View.GONE);
                mHolder.tv_estimate.setVisibility(View.GONE);
                break;
        }

        mHolder.tv_estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.tv_estimate.getText() == context.getResources().getString(R.string.cancel_appley))
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
                if(mHolder.tv_estimate.getText() == context.getResources().getString(R.string.estimate))
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ConfirmedOrderId",String.valueOf(item.getId()));
                    if(item.getServiceReceiver().getId() == Auth.getCurrentUserId()) {
                        bundle.putString("Estimate", "1");
                    }
                    else
                    {
                        bundle.putString("Estimate", "2");
                    }
                    intent.setClass(context, OrderCommentActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                if(mHolder.tv_estimate.getText() == context.getResources().getString(R.string.refund_apply))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater =LayoutInflater.from(context);
                    View layout = inflater.inflate(R.layout.dialog_rejectrefundreason, (ViewGroup) parent.findViewById(R.id.dialog));
                    final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                    builder.setTitle("请填写退款理由").setView(
                            layout).setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!comment.getText().toString().equals("")) {
                                Http.request((Activity) context, API.REJECT_PAYMENT, new Object[]{String.valueOf(item.getId())}, Http.map(
                                        "UserId", String.valueOf(Auth.getCurrentUserId()),
                                        "RefundReason",comment.getText().toString()

                                ), new Http.RequestListener<ConfirmedOrderDetail>() {
                                    @Override
                                    public void onSuccess(ConfirmedOrderDetail result) {
                                        super.onSuccess(result);

                                        Toast.makeText(context, "申请退款提交成功", Toast.LENGTH_SHORT).show();
                                        MyOrderFragment.tab_progess.performClick();
                                    }
                                });
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(context,"退款理由不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }

                if(mHolder.tv_estimate.getText() == context.getResources().getString(R.string.disagree))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage("是否确认拒绝退款并进入纠纷处理环节？您可能需要等待1-30天，让我们客服介入并解决")
//                            .setCancelable(false)
//                            .setPositiveButton("仍然拒绝退款",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog,
//                                                            int id) {
//                                            Http.request((Activity) context, API.REFUND_DISAGREE, new Object[]{String.valueOf(item.getId())}, Http.map(
//                                                    "UserId", String.valueOf(Auth.getCurrentUserId())
//                                            ), new Http.RequestListener<ConfirmedOrderDetail>() {
//                                                @Override
//                                                public void onSuccess(ConfirmedOrderDetail result) {
//                                                    super.onSuccess(result);
//
//                                                    Toast.makeText(context, "不同意退款提交成功", Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            });
//                                        }
//                                    })
//                            .setNegativeButton("返回",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog,
//                                                            int id) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                    AlertDialog dialog = builder.create();
//                    if (!dialog.isShowing()){
//                        dialog.show();
//                    }

                    LayoutInflater inflater =LayoutInflater.from(context);
                    View layout = inflater.inflate(R.layout.dialog_rejectrefundreason, (ViewGroup) parent.findViewById(R.id.dialog));
                    final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                    builder.setTitle("是否确认拒绝退款并进入纠纷处理环节？您可能需要等待1-30天，让我们客服介入并解决").setView(
                            layout).setPositiveButton("仍然拒绝退款", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!comment.getText().toString().equals("")) {
                                Http.request((Activity) context, API.REFUND_DISAGREE, new Object[]{String.valueOf(item.getId())}, Http.map(
                                        "UserId", String.valueOf(Auth.getCurrentUserId()),
                                        "RejectRefundReason",comment.getText().toString()
                                ), new Http.RequestListener<ConfirmedOrderDetail>() {
                                    @Override
                                    public void onSuccess(ConfirmedOrderDetail result) {
                                        super.onSuccess(result);

                                        Toast.makeText(context, "不同意退款提交成功", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(context,"拒绝退款理由不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }

                if(mHolder.tv_estimate.getText() == context.getResources().getString(R.string.service_stop))
                {
                    Http.request((Activity) context, API.ORDER_CLOSE,
                            Http.map("ConfirmedOrderId", String.valueOf(item.getId()),
                                    "UserId",String.valueOf(Auth.getCurrentUserId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    MyOrderFragment.tab_progess.performClick();
                                }
                            });
                }
                if(mHolder.tv_estimate.getText() == context.getResources().getString(R.string.delete))
                {
                    Http.request((Activity) context, API.CLOSE_ORDER, Http.map("Id", String.valueOf(item.getId())), new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);
                            MyOrderFragment.tab_close.performClick();
                        }
                    });
                }
                if(mHolder.tv_estimate.getText() == context.getResources().getString(R.string.dispute))
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

        mHolder.tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHolder.tv_finish.getText() == "支付")
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.setClass(context, PayActivity.class);
                    bundle.putSerializable("ConfirmedOrderDetailPay", item);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
//                    Http.request((Activity)context, API.CONFIRMEDORDERPAY, Http.map(
//                                    "PlatformType", "1",
//                                    "Amount", String.valueOf(item.getBid()),
//                                    "ConfirmedOrderId",String.valueOf(item.getId()),
//                                    "orderId",String.valueOf(item.getOrder().getId())),
//                            new Http.RequestListener<OnlinePay>() {
//                                @Override
//                                public void onSuccess(final OnlinePay result) {
//                                    super.onSuccess(result);
//                                    /**
//                                     * 完整的符合支付宝参数规范的订单信息
//                                     */
////                        final String payInfo = AliPay.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01", "1") + "&sign=\"" + result.getSign() + "\"&" + AliPay.getSignType();
//
//
//                                    Runnable payRunnable = new Runnable() {
//
//                                        @Override
//                                        public void run() {
//                                            // 构造PayTask 对象
//                                            PayTask alipay = new PayTask((Activity)context);
//                                            // 调用支付接口，获取支付结果
//                                            String resultInfo = alipay.pay(result.getSign(), true);
//
//                                            Message msg = new Message();
//                                            msg.what = SDK_PAY_FLAG;
//                                            msg.obj = resultInfo;
//                                            mHandler.sendMessage(msg);
//                                        }
//                                    };
//
//                                    // 必须异步调用
//                                    Thread payThread = new Thread(payRunnable);
//                                    payThread.start();
//                                }
//
//                                @Override
//                                public void onFail(String code) {
//                                    super.onFail(code);
//                                }
//                            });
                }
                if(mHolder.tv_finish.getText() == context.getResources().getString(R.string.service_done))
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
                if(mHolder.tv_finish.getText() == "催单")
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
                if(mHolder.tv_finish.getText() == "付款")
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    LayoutInflater inflater =LayoutInflater.from(context);
                    View layout = inflater.inflate(R.layout.dialog_confirmpayment, (ViewGroup) parent.findViewById(R.id.dialog));
                    final RadioButton rb_share = (RadioButton)layout.findViewById(R.id.rb_share);
                    builder.setTitle("是否确认付款").setView(
                            layout).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Http.request((Activity)context, API.ORDER_CONFIRMCOMPLETE,new Object[]{String.valueOf(item.getId())}, Http.map(
                                    "UserId", String.valueOf(Auth.getCurrentUserId()),
                                    "ShareRecord",String.valueOf(rb_share.isChecked())
                                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
                                @Override
                                public void onSuccess(ConfirmedOrderDetail result) {
                                    super.onSuccess(result);

                                    Toast.makeText(context, "确认完成", Toast.LENGTH_SHORT).show();
                                    MyOrderFragment.tab_close.performClick();
                                }
                            });
                            dialog.dismiss();
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
                if(mHolder.tv_finish.getText() == context.getResources().getString(R.string.agree))
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

//        mHolder.imgbtn_conversation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginSampleHelper ls = LoginSampleHelper.getInstance();
//                String target = String.valueOf(item.getServiceProvider().getId() == Auth.getCurrentUserId() ? item.getServiceReceiver().getId() : item.getServiceProvider().getId());
//                Intent intent = ls.getIMKit().getChattingActivityIntent(target);
//                context.startActivity(intent);
//            }
//        });
//
//        mHolder.imgbtn_call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(item.getServiceReceiver().getId() == Auth.getCurrentUserId())
//                {
//                    if(!item.getServiceProvider().getMobile().equals("")) {
//                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getServiceProvider().getMobile())); //直接拨打电话android.intent.action.CALL
//                        context.startActivity(phoneIntent);
//                    }
//                    else
//                    {
//                        Toast.makeText(context,"该用户不是用手机注册",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else
//                {
//                    if(!item.getServiceReceiver().getMobile().equals("")) {
//                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.getServiceReceiver().getMobile())); //直接拨打电话android.intent.action.CALL
//                        context.startActivity(phoneIntent);
//                    }
//                    else
//                    {
//                        Toast.makeText(context,"该用户不是用手机注册",Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        });



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
//        private TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name,tv_showserviceprovider,tv_showservicereceiver,tv_pay,tv_showstatus,tv_numofconforder,tv_countdown;
//        private Button btn_estimate,btn_finish;
//        private com.android.volley.toolbox.NetworkImageView iv_avatar;
//        private RatingBar rb_totalvalue;
//        private LinearLayout ll_button;
//        private ImageButton imgbtn_conversation,imgbtn_call;
        private TextView tv_time,tv_name,tv_service,txt_title,txt_location,tv_bid,txt_bounty,tv_total,tv_estimate,tv_finish,tv_showstatus;
        private ImageView iv_service;
        private com.android.volley.toolbox.NetworkImageView iv_avatar,iv_photos;
    }



}

