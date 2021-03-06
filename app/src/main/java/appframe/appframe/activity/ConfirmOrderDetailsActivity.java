package appframe.appframe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.PayResult;
import appframe.appframe.dto.OnlinePay;
import appframe.appframe.dto.Question;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.widget.swiperefresh.OrderDetailsGridViewAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderComment;

import static appframe.appframe.R.layout.item;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ConfirmOrderDetailsActivity extends BaseActivity implements View.OnClickListener{

    //private ImageView img_avatar;
//    private TextView tv_name,tv_title,tv_money,tv_time,tv_location,tv_type,tv_status,tv_content,tv_range,tv_deadline,tv_require,tv_paymethod,tb_back,tb_action,tb_title,tv_comment,tv_moneyunit,tv_confirmorderid;
    private TextView tb_back,tb_action,tb_title,tv_status,tv_title,tv_money,tv_location,tv_serverprovider,tv_serverreceiver,tv_time,tv_confirmorderid,tv_paymethod,tv_finish,tv_estimate,tv_refundreason,tv_rejectreason;
//    private ImageButton imgbtn_conversation,imgbtn_call;
    com.android.volley.toolbox.NetworkImageView iv_avatar;
    private RelativeLayout rl_center,rl_refund,rl_reject;
//    private Button btn_finish,btn_estimate;
    private String OrderID,Tel;
    int MessageUserID;
    private ListView lv_ordercomment;
    private ImageView iv_phone,iv_message;

    //    private LinearLayout lly_photos;
//    private GridView gridView;
//    private RatingBar rb_totalvalue;
    ConfirmedOrderDetail confirmedOrderDetail;
    private LinearLayout ll_button;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    String From;
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ConfirmOrderDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ConfirmOrderDetailsActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ConfirmOrderDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorderdetails);
        if(this.getIntent().getStringExtra("ConfirmOrderIdFromPushDemoReceiver") != null)
        {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("Id", getintent.getStringExtra("OrderIdFromPushDemoReceiver"));
            Http.request(this, API.GET_CONFIRMEDORDERBYID, new Object[]{this.getIntent().getStringExtra("ConfirmOrderIdFromPushDemoReceiver")},

                    new Http.RequestListener<ConfirmedOrderDetail>() {
                        @Override
                        public void onSuccess(ConfirmedOrderDetail result) {
                            super.onSuccess(result);

                            confirmedOrderDetail = result;
                            init();
                        }
                    });

        }
        else
        {
            init();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.iv_avatar:
//                intent.setClass(ConfirmOrderDetailsActivity.this, FriendsInfoActivity.class);
//                bundle.putSerializable("OrderDetails", confirmedOrderDetail.getOrder());
//                intent.putExtras(bundle);
//                startActivity(intent);
//                break;
//            case R.id.imgbtn_conversation:
//                LoginSampleHelper ls = LoginSampleHelper.getInstance();
//                String target = String.valueOf(confirmedOrderDetail.getOrder().getOrderer().getId());
//                intent = ls.getIMKit().getChattingActivityIntent(target);
//                startActivity(intent);
//                break;
//            case R.id.imgbtn_call:
//                if(!Tel.equals("")) {
//                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Tel)); //直接拨打电话android.intent.action.CALL
//                    startActivity(phoneIntent);
//                }
//                else
//                {
//                    Toast.makeText(ConfirmOrderDetailsActivity.this, "该用户不是用手机注册", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.tb_action:
//                new PopupWindows(ConfirmOrderDetailsActivity.this,tb_action);
//                break;
            case R.id.tb_back:
                finish();
                break;

            case R.id.tv_estimate:
                if(tv_estimate.getText() == getResources().getString(R.string.cancel_appley))
                {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("UserId", String.valueOf(Auth.getCurrentUserId()));

                    Http.request(this, API.ORDER_CANCEL, new Object[]{String.valueOf(confirmedOrderDetail.getId()), Http.getURL(map)},
                            new Http.RequestListener<ConfirmedOrderDetail>() {
                                @Override
                                public void onSuccess(ConfirmedOrderDetail result) {
                                    super.onSuccess(result);
                                    updateOrderStatus(result,AppConfig.ORDERSTATUS_APPLY);
                                }

                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);
                                }
                            });

                }
                if(tv_estimate.getText() == getResources().getString(R.string.estimate))
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    if(confirmedOrderDetail.getServiceReceiver().getId() == Auth.getCurrentUserId()) {
                        bundle.putString("Estimate", "1");
                    }
                    else
                    {
                        bundle.putString("Estimate", "2");
                    }
                    bundle.putString("ConfirmedOrderId",String.valueOf(confirmedOrderDetail.getId()));
                    intent.setClass(ConfirmOrderDetailsActivity.this, OrderCommentActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if(tv_estimate.getText() == getResources().getString(R.string.refund_apply))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOrderDetailsActivity.this);
                    LayoutInflater inflater =LayoutInflater.from(ConfirmOrderDetailsActivity.this);
                    View layout = inflater.inflate(R.layout.dialog_rejectrefundreason, (ViewGroup) findViewById(R.id.dialog));
                    final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                    builder.setTitle("请填写退款理由").setView(
                            layout).setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!comment.getText().toString().equals("")) {
                                Http.request(ConfirmOrderDetailsActivity.this, API.REJECT_PAYMENT, new Object[]{String.valueOf(confirmedOrderDetail.getId())}, Http.map(
                                        "UserId", String.valueOf(Auth.getCurrentUserId()),
                                        "RefundReason",comment.getText().toString()

                                ), new Http.RequestListener<ConfirmedOrderDetail>() {
                                    @Override
                                    public void onSuccess(ConfirmedOrderDetail result) {
                                        super.onSuccess(result);
                                        updateOrderStatus(result,AppConfig.ORDERSTATUS_PROGRESS);
                                        Toast.makeText(ConfirmOrderDetailsActivity.this, "申请退款提交成功", Toast.LENGTH_SHORT).show();
//                                        MyOrderFragment.tab_progess.performClick();
                                    }
                                });
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(ConfirmOrderDetailsActivity.this,"退款理由不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

//                    Http.request(this, API.REJECT_PAYMENT, new Object[]{String.valueOf(confirmedOrderDetail.getId())}, Http.map(
//                            "UserId", String.valueOf(Auth.getCurrentUserId())
//                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
//                        @Override
//                        public void onSuccess(ConfirmedOrderDetail result) {
//                            super.onSuccess(result);
//                            updateOrderStatus(result,null);
//                            Toast.makeText(ConfirmOrderDetailsActivity.this, "申请退款提交成功", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                }

                if(tv_estimate.getText() == getResources().getString(R.string.disagree))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                    LayoutInflater inflater =LayoutInflater.from(this);
                    View layout = inflater.inflate(R.layout.dialog_rejectrefundreason, (ViewGroup) findViewById(R.id.dialog));
                    final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                    builder.setTitle("是否确认拒绝退款并进入纠纷处理环节？您可能需要等待1-30天，让我们客服介入并解决").setView(
                            layout).setPositiveButton("仍然拒绝退款", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!comment.getText().toString().equals("")) {
                                Http.request(ConfirmOrderDetailsActivity.this, API.REFUND_DISAGREE, new Object[]{String.valueOf(confirmedOrderDetail.getId())}, Http.map(
                                        "UserId", String.valueOf(Auth.getCurrentUserId()),
                                        "RejectRefundReason",comment.getText().toString()
                                ), new Http.RequestListener<ConfirmedOrderDetail>() {
                                    @Override
                                    public void onSuccess(ConfirmedOrderDetail result) {
                                        super.onSuccess(result);
                                        updateOrderStatus(result,AppConfig.ORDERSTATUS_PROGRESS);
                                        Toast.makeText(ConfirmOrderDetailsActivity.this, "不同意退款提交成功", Toast.LENGTH_SHORT).show();


                                    }
                                });
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(ConfirmOrderDetailsActivity.this,"拒绝退款理由不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();



                }

                if(tv_estimate.getText() == getResources().getString(R.string.service_stop))
                {
                    Http.request(ConfirmOrderDetailsActivity.this, API.ORDER_CLOSE,
                            Http.map("ConfirmedOrderId", String.valueOf(confirmedOrderDetail.getId()),
                                    "UserId",String.valueOf(Auth.getCurrentUserId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    updateOrderStatus(null,AppConfig.ORDERSTATUS_CLOSE);
                                }
                            });
//                    Http.request(ConfirmOrderDetailsActivity.this, API.CHANGE_STATUS, new Object[]{String.valueOf(confirmedOrderDetail.getId())},
//                            Http.map("Status", "0"), new Http.RequestListener<ConfirmedOrderDetail>() {
//                                @Override
//                                public void onSuccess(ConfirmedOrderDetail result) {
//                                    super.onSuccess(result);
//                                    updateOrderStatus(result,null);
//                                }
//                            });
                }
//                if(tv_estimate.getText() == getResources().getString(R.string.delete))
//                {
//                    Http.request(ConfirmOrderDetailsActivity.this, API.CLOSE_ORDER, Http.map("Id", String.valueOf(confirmedOrderDetail.getId())), new Http.RequestListener<String>() {
//                        @Override
//                        public void onSuccess(String result) {
//                            super.onSuccess(result);
//
//                        }
//                    });
//                }
                if(tv_estimate.getText() == getResources().getString(R.string.dispute))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //final EditText comment = new EditText(this);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_dispute, (ViewGroup) findViewById(R.id.dialog));
                    final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                    builder.setTitle("申诉").setView(
                            layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!comment.getText().toString().equals("")) {
                                Http.request(ConfirmOrderDetailsActivity.this, API.ORDER_DISPUTE, new Object[]{String.valueOf(confirmedOrderDetail.getId())},Http.map("Content",comment.getText().toString()), new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
//                                        updateOrderStatus(result,AppConfig.ORDERSTATUS_PROGRESS);
                                        Toast.makeText(ConfirmOrderDetailsActivity.this, "申诉成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(ConfirmOrderDetailsActivity.this,"申诉内容不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

                }
                break;

            case R.id.tv_finish:
                if(tv_finish.getText() == "支付")
                {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.setClass(ConfirmOrderDetailsActivity.this, PayActivity.class);
                    bundle.putSerializable("ConfirmedOrderDetailPay", confirmedOrderDetail);
                    intent.putExtras(bundle);
                    startActivity(intent);
//                    Http.request(ConfirmOrderDetailsActivity.this, API.CONFIRMEDORDERPAY, Http.map(
//                                    "PlatformType", "1",
//                                    "Amount", String.valueOf(confirmedOrderDetail.getBid()),
//                                    "ConfirmedOrderId",String.valueOf(confirmedOrderDetail.getId()),
//                                    "orderId",String.valueOf(confirmedOrderDetail.getOrder().getId())),
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
//                                            PayTask alipay = new PayTask(ConfirmOrderDetailsActivity.this);
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
                if(tv_finish.getText() == getResources().getString(R.string.service_done))
                {
                    Http.request(ConfirmOrderDetailsActivity.this, API.ORDER_COMPLETE,new Object[]{String.valueOf(confirmedOrderDetail.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
                        @Override
                        public void onSuccess(ConfirmedOrderDetail result) {
                            super.onSuccess(result);
                            updateOrderStatus(result,AppConfig.ORDERSTATUS_PROGRESS);
                            Toast.makeText(ConfirmOrderDetailsActivity.this, "完成提交成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                if(tv_finish.getText() == "催单")
                {
                    Http.request(ConfirmOrderDetailsActivity.this, API.ORDER_HURRYUP,new Object[]{String.valueOf(confirmedOrderDetail.getId())},
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);

                                    Toast.makeText(ConfirmOrderDetailsActivity.this, "对方已收到您的催单通知", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
                if(tv_finish.getText() == "付款")
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmOrderDetailsActivity.this);

                    LayoutInflater inflater =LayoutInflater.from(ConfirmOrderDetailsActivity.this);
                    View layout = inflater.inflate(R.layout.dialog_confirmpayment, (ViewGroup)findViewById(R.id.dialog));
                    final CheckBox rb_share = (CheckBox)layout.findViewById(R.id.rb_share);
                    builder.setTitle("是否确认付款").setView(
                            layout).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Http.request(ConfirmOrderDetailsActivity.this, API.ORDER_CONFIRMCOMPLETE,new Object[]{String.valueOf(confirmedOrderDetail.getId())}, Http.map(
                                    "UserId", String.valueOf(Auth.getCurrentUserId()),
                                    "ShareRecord",String.valueOf(rb_share.isChecked())
                            ), new Http.RequestListener<ConfirmedOrderDetail>() {
                                @Override
                                public void onSuccess(ConfirmedOrderDetail result) {
                                    super.onSuccess(result);
                                    updateOrderStatus(result,AppConfig.ORDERSTATUS_DONE);
                                    Toast.makeText(ConfirmOrderDetailsActivity.this, "确认完成", Toast.LENGTH_SHORT).show();

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
//                    Http.request(ConfirmOrderDetailsActivity.this, API.ORDER_CONFIRMCOMPLETE,new Object[]{String.valueOf(confirmedOrderDetail.getId())}, Http.map(
//                            "UserId", String.valueOf(Auth.getCurrentUserId())
//                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
//                        @Override
//                        public void onSuccess(ConfirmedOrderDetail result) {
//                            super.onSuccess(result);
//                            updateOrderStatus(result,null);
//                            Toast.makeText(ConfirmOrderDetailsActivity.this, "确认完成", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                }
                if(tv_finish.getText() == getResources().getString(R.string.agree))
                {
                    Http.request(ConfirmOrderDetailsActivity.this, API.REFUND_AGREE,new Object[]{String.valueOf(confirmedOrderDetail.getId())}, Http.map(
                            "UserId", String.valueOf(Auth.getCurrentUserId())
                    ), new Http.RequestListener<ConfirmedOrderDetail>() {
                        @Override
                        public void onSuccess(ConfirmedOrderDetail result) {
                            super.onSuccess(result);
                            updateOrderStatus(result,AppConfig.ORDERSTATUS_PROGRESS);
                            Toast.makeText(ConfirmOrderDetailsActivity.this, "您已同意退款", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                break;
            case R.id.iv_phone:
                if(confirmedOrderDetail.getOrder().getPhoneAnonymity() == 1)
                {
                    Toast.makeText(ConfirmOrderDetailsActivity.this,"对方没提供电话号码",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Tel)); //直接拨打电话android.intent.action.CALL
                    startActivity(phoneIntent);
                }
                break;
            case R.id.iv_message:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = String.valueOf(MessageUserID);
                intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.rl_center:
                intent.setClass(ConfirmOrderDetailsActivity.this, OrderDetailsActivity.class);
                OrderDetails orderDetails = confirmedOrderDetail.getOrder();

                bundle.putSerializable("OrderDetails", orderDetails);
                if (hasTopOrder() && orderDetails.getId() == getTopOrder().getId()) {
                    bundle.putString("hasTopOrder", "1");
                }
                bundle.putString("From", "Order");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }

    //判断有没置顶单
    protected boolean hasTopOrder()
    {
        SharedPreferences sp = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE);
        String OrderDetails = sp.getString("OrderDetails", null);
        if (!TextUtils.isEmpty(OrderDetails)) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails = GsonHelper.getGson().fromJson(OrderDetails, OrderDetails.class);
            return true;
//            if(orderDetails.getType() == Type)
//            {
//                return true;
//            }
//            else {
//                return false;
//            }
        }
        else {
            return false;
        }
    }

    //获取置顶单子
    protected  OrderDetails  getTopOrder()
    {
        OrderDetails orderDetails = new OrderDetails();
        SharedPreferences sp = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE);
        String OrderDetails = sp.getString("OrderDetails", null);
        if (!TextUtils.isEmpty(OrderDetails)) {
            orderDetails = GsonHelper.getGson().fromJson(OrderDetails, OrderDetails.class);
        }

        return orderDetails;
    }

    public void init()
    {

//        gridView =(GridView)findViewById(R.id.gridview);
//        imgbtn_conversation = (ImageButton)findViewById(R.id.imgbtn_conversation);
//        imgbtn_call = (ImageButton)findViewById(R.id.imgbtn_call);
        tv_title =(TextView)findViewById(R.id.tv_title);
        tv_money =(TextView)findViewById(R.id.tv_money);
        tv_location =(TextView)findViewById(R.id.tv_location);
//        tv_type =(TextView)findViewById(R.id.tv_type);
//        tv_content =(TextView)findViewById(R.id.tv_content);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_status = (TextView)findViewById(R.id.tv_status);
//        tv_require = (TextView)findViewById(R.id.tv_require);
        tv_paymethod = (TextView)findViewById(R.id.tv_paymethod);
//        tv_deadline = (TextView)findViewById(R.id.tv_deadline);
//        tv_name = (TextView)findViewById(R.id.tv_name);
        lv_ordercomment = (ListView)findViewById(R.id.lv_ordercomment);
        rl_center = (RelativeLayout)findViewById(R.id.rl_center);
        rl_refund = (RelativeLayout)findViewById(R.id.rl_refund);
        rl_reject = (RelativeLayout)findViewById(R.id.rl_reject);
        tv_refundreason = (TextView)findViewById(R.id.tv_refundreason);
        tv_rejectreason = (TextView)findViewById(R.id.tv_rejectreason);
//        tv_comment = (TextView)findViewById(R.id.tv_comment);
//        tv_moneyunit = (TextView)findViewById(R.id.tv_moneyunit);
//        tv_range = (TextView)findViewById(R.id.tv_range);
        iv_avatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_avatar);
        tv_serverprovider = (TextView)findViewById(R.id.tv_serverprovider);
        tv_serverreceiver = (TextView)findViewById(R.id.tv_serverreceiver);
        tv_finish = (TextView)findViewById(R.id.tv_finish);
        tv_estimate = (TextView)findViewById(R.id.tv_estimate);
//        rb_totalvalue = (RatingBar)findViewById(R.id.rb_totalvalue);
//        btn_finish = (Button)findViewById(R.id.btn_finish);
//        btn_estimate = (Button)findViewById(R.id.btn_estimate);
        ll_button = (LinearLayout)findViewById(R.id.ll_button);
        tv_confirmorderid = (TextView)findViewById(R.id.tv_confirmorderid);

        iv_phone = (ImageView)findViewById(R.id.iv_phone);
        iv_message = (ImageView)findViewById(R.id.iv_message);

        iv_avatar.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_estimate.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        iv_phone.setOnClickListener(this);
        rl_center.setOnClickListener(this);
//        imgbtn_conversation.setOnClickListener(this);
//        imgbtn_call.setOnClickListener(this);
//        btn_finish.setOnClickListener(this);
//        btn_estimate.setOnClickListener(this);

        if(this.getIntent().getStringExtra("ConfirmOrderIdFromPushDemoReceiver") != null)
        {
            //orderDetails之前赋值过了
        }
        else {
            Intent intent = this.getIntent();
            confirmedOrderDetail = (ConfirmedOrderDetail) intent.getSerializableExtra("ConfirmOrderDetails");
        }
        From = this.getIntent().getStringExtra("From");
        if(From == null)
        {
            if(confirmedOrderDetail.getStatus() == 3 || confirmedOrderDetail.getStatus() == 11) {
                From = AppConfig.ORDERSTATUS_APPLY;
            }
            else if(confirmedOrderDetail.getStatus() == 1 || confirmedOrderDetail.getStatus() == 5 || confirmedOrderDetail.getStatus() == 7 || confirmedOrderDetail.getStatus() == 8)
            {
                From = AppConfig.ORDERSTATUS_PROGRESS;
            }
            else if(confirmedOrderDetail.getStatus() == 2)
            {
                From = AppConfig.ORDERSTATUS_DONE;
            }
            else if (confirmedOrderDetail.getStatus() == 0 || confirmedOrderDetail.getStatus() == 6 || confirmedOrderDetail.getStatus() == 4 )
            {
                From = AppConfig.ORDERSTATUS_CLOSE;
            }
            else
            {

            }
        }
        updateOrderStatus(confirmedOrderDetail, From);

        if(confirmedOrderDetail.getServiceProvider().getId() == Auth.getCurrentUserId()) {
            tv_serverprovider.setText(confirmedOrderDetail.getServiceProvider().getName() +"(我)");
        }
        else {
            if(confirmedOrderDetail.getOrder().getNameAnonymity() ==1)
            {
                tv_serverprovider.setText("匿名");
                MessageUserID = confirmedOrderDetail.getServiceProvider().getId();
                Tel = confirmedOrderDetail.getServiceProvider().getMobile() == null ? "" : confirmedOrderDetail.getServiceProvider().getMobile().toString();
            }
            else {
                tv_serverprovider.setText(confirmedOrderDetail.getServiceProvider().getName());
                MessageUserID = confirmedOrderDetail.getServiceProvider().getId();
                Tel = confirmedOrderDetail.getServiceProvider().getMobile() == null ? "" : confirmedOrderDetail.getServiceProvider().getMobile().toString();
            }
        }
        if(confirmedOrderDetail.getServiceReceiver().getId() == Auth.getCurrentUserId())
        {
            tv_serverreceiver.setText(confirmedOrderDetail.getServiceReceiver().getName() +"(我)");
        }
        else {
            if(confirmedOrderDetail.getOrder().getNameAnonymity() ==1)
            {
                tv_serverreceiver.setText("匿名");
                MessageUserID = confirmedOrderDetail.getServiceReceiver().getId();
                Tel = confirmedOrderDetail.getServiceReceiver().getMobile() == null ? "" : confirmedOrderDetail.getServiceReceiver().getMobile().toString();
            } else {
                tv_serverreceiver.setText(confirmedOrderDetail.getServiceReceiver().getName());
                MessageUserID = confirmedOrderDetail.getServiceReceiver().getId();
                Tel = confirmedOrderDetail.getServiceReceiver().getMobile() == null ? "" : confirmedOrderDetail.getServiceReceiver().getMobile().toString();
            }
        }
//        if( confirmedOrderDetail.getOrder().getType() == 1 )
//        {
//            tv_moneyunit.setText("索 ￥");
//        }
//        else
//        {
//            tv_moneyunit.setText("赏 ￥");
//        }

        tv_title.setText(confirmedOrderDetail.getOrder().getTitle().toString());
        tv_money.setText("￥" + String.valueOf(confirmedOrderDetail.getOrder().getBounty()));
        if(confirmedOrderDetail.getOrder().getLocationAnonymity() == 1) {
            tv_location.setText("");
        }
        else
        {
            tv_location.setText(confirmedOrderDetail.getOrder().getAddress().toString());
        }
//        tv_type.setText(confirmedOrderDetail.getOrder().getCategory().toString());
//        tv_content.setText(confirmedOrderDetail.getOrder().getContent().toString());
        tv_time.setText(confirmedOrderDetail.getOrder().getCreatedAt().toString());
        tv_status.setText(setComfirmOrderStatus(confirmedOrderDetail));
        tv_confirmorderid.setText(confirmedOrderDetail.getConfirmedOrderNumber());
//        tv_require.setText(confirmedOrderDetail.getOrder().getRequest() == null ? "" : confirmedOrderDetail.getOrder().getRequest().toString());
        tv_paymethod.setText(confirmedOrderDetail.getOrder().getPaymentMethod().toString());
//        tv_deadline.setText(confirmedOrderDetail.getOrder().getDeadline().toString());
//        if(confirmedOrderDetail.getOrder().getOrderer().getFNickName() != null && !confirmedOrderDetail.getOrder().getOrderer().getFNickName().equals(""))
//        {
//            tv_name.setText(confirmedOrderDetail.getOrder().getOrderer().getFNickName());
//        }
//        else {
//            tv_name.setText(confirmedOrderDetail.getOrder().getOrderer().getName().toString());
//        }
//        if(confirmedOrderDetail.getOrder().getType() == 1) {
//            rb_totalvalue.setRating((float) confirmedOrderDetail.getOrder().getOrderer().getTotalWorkerPoint());
//        }
//        if(confirmedOrderDetail.getOrder().getType() == 2) {
//            rb_totalvalue.setRating((float) confirmedOrderDetail.getOrder().getOrderer().getTotalBossPoint());
//        }
//        if(confirmedOrderDetail.getOrder().getOrderer().getAvatar() != null) {
//            ImageUtils.setImageUrl(iv_avatar, confirmedOrderDetail.getOrder().getOrderer().getAvatar().toString());
//        }
        if(confirmedOrderDetail.getOrder().getPhotos() != null && !confirmedOrderDetail.getOrder().getPhotos().equals(""))
        {
            ImageUtils.setImageUrl(iv_avatar, confirmedOrderDetail.getOrder().getPhotos().split(",")[0]);
        }
        else
        {
            iv_avatar.setDefaultImageResId(R.drawable.defaultphoto);
        }
        if(confirmedOrderDetail.getRefundReason() != null)
        {
            rl_refund.setVisibility(View.VISIBLE);
            tv_refundreason.setText(confirmedOrderDetail.getRefundReason());
        }
        if(confirmedOrderDetail.getRejectRefundReason() != null)
        {
            rl_reject.setVisibility(View.VISIBLE);
            tv_rejectreason.setText(confirmedOrderDetail.getRejectRefundReason());
        }

//        if(confirmedOrderDetail.getOrder().getPhotos() != null && confirmedOrderDetail.getOrder().getPhotos() != "") {
//            List<String> photoPath = new ArrayList<String>();
//            for (String photsCount : confirmedOrderDetail.getOrder().getPhotos().toString().split(",")) {
//                photoPath.add(photsCount);
//            }
//            gridView.setAdapter(new OrderDetailsGridViewAdapater(ConfirmOrderDetailsActivity.this,photoPath));
//            gridView.setVisibility(View.VISIBLE);
//            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent();
//                    intent.setClass(ConfirmOrderDetailsActivity.this, AvatarZoomActivity.class);
//                    intent.putExtra("Avatar", (String)parent.getAdapter().getItem(position));
//                    startActivity(intent);
//                }
//            });
//        }
//        Tel = confirmedOrderDetail.getOrder().getOrderer().getMobile() == null ? "" : confirmedOrderDetail.getOrder().getOrderer().getMobile().toString();
        OrderID = String.valueOf(confirmedOrderDetail.getOrder().getId());
//        tv_range.setText(setRange(confirmedOrderDetail.getOrder().getVisibility()));
        //Log.i("OrderDetailsID--",String.valueOf(orderDetails.getId()));
        tb_title.setText("订单详情");
        tb_back.setText("订单");
//        Drawable drawable = getResources().getDrawable(R.drawable.ic_moreoverflow_normal_holo_light);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        tb_action.setCompoundDrawables(drawable, null, null, null);
//        tb_action.setOnClickListener(this);
        tb_action.setVisibility(View.GONE);
        tb_back.setOnClickListener(this);

//        Http.request(ConfirmOrderDetailsActivity.this, API.ORDER_GETCOOMENT, new Object[]{OrderID},
//                new Http.RequestListener<List<OrderComment>>() {
//                    @Override
//                    public void onSuccess(List<OrderComment> result) {
//                        super.onSuccess(result);
//                        if (Auth.getCurrentUserId() == confirmedOrderDetail.getOrder().getOrderer().getId()) {
//                            lv_ordercomment.setAdapter(new SwipeRefreshXOrderComment(ConfirmOrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), true));
//                        } else {
//                            lv_ordercomment.setAdapter(new SwipeRefreshXOrderComment(ConfirmOrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), false));
//                        }
//                        setListViewHeightBasedOnChildren(lv_ordercomment);
//                        tv_comment.setText(String.format("留言（%d条）",result != null ? result.size() : 0));
//                    }
//                });

    }

    private String setComfirmOrderStatus(ConfirmedOrderDetail confirmedOrderDetail)
    {
        StringBuilder sb = new StringBuilder();
        if(confirmedOrderDetail.getStatus() == 0) {
            sb.append("已关闭");
        }
        else if(confirmedOrderDetail.getStatus() == 1) {
            sb.append("服务进行中");
        }
        else if(confirmedOrderDetail.getStatus() == 2) {
            sb.append("待评价");
        }
        else if(confirmedOrderDetail.getStatus() == 3) {
            sb.append("申请中");
        }
        else if(confirmedOrderDetail.getStatus() == 4) {
            sb.append("取消申请");
        }
        else if(confirmedOrderDetail.getStatus() == 5)
        {
            if(confirmedOrderDetail.getServiceReceiver().getId() == Auth.getCurrentUserId())
            {
                sb.append("服务已完成请您付款");
            }
            else {
                sb.append("服务已完成待对方付款");
            }
        }
        else if(confirmedOrderDetail.getStatus() == 6)
        {
            sb.append("已关闭");
        }
        else if(confirmedOrderDetail.getStatus() == 7)
        {
            sb.append("申请退款中");
        }
        else if(confirmedOrderDetail.getStatus() == 8)
        {
            sb.append("拒绝退款待客服解决");
        }
        else if(confirmedOrderDetail.getStatus() == 9)
        {
            sb.append("待被服务方评价");
        }
        else if(confirmedOrderDetail.getStatus() == 10)
        {
            sb.append("待服务方评价");
        }
        else if(confirmedOrderDetail.getStatus() == 11)
        {
            sb.append("待被服务方支付");
        }
        return sb.toString();
    }

    private String setRange(int range)
    {
        StringBuilder sb = new StringBuilder();

        if( (range & 1) == 1)
        {
            sb.append(",").append("一度");
        }
        if((range & 2) == 2)
        {
            sb.append(",").append("二度");
        }
        if((range & 4) == 4)
        {
            sb.append(",").append("陌生人");
        }

        return sb.deleteCharAt(0).toString();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void updateOrderStatus(ConfirmedOrderDetail confirmedOrderDetail,String from)
    {
        if(from.equals(AppConfig.ORDERSTATUS_APPLY))
        {
            if(confirmedOrderDetail.getOrder().getType() == 1 && confirmedOrderDetail.getStatus() == 11) {
                tv_finish.setVisibility(View.VISIBLE);
                tv_finish.setText("支付");
            }
            else
            {
                tv_finish.setVisibility(View.GONE);
            }

            tv_estimate.setText(getResources().getString(R.string.cancel_appley));
        }
        else if(from.equals(AppConfig.ORDERSTATUS_PROGRESS)){
            tv_finish.setVisibility(View.VISIBLE);
            if (confirmedOrderDetail.getServiceProvider().getId() == Auth.getCurrentUserId())
            {

                if(confirmedOrderDetail.getStatus() == 1) {
                    tv_finish.setText(getResources().getString(R.string.service_done));
                    tv_estimate.setText(getResources().getString(R.string.service_stop));
                }
                if(confirmedOrderDetail.getStatus() == 5)
                {
                    tv_finish.setVisibility(View.GONE);
                    tv_estimate.setVisibility(View.GONE);


                }
                if(confirmedOrderDetail.getStatus() == 7)
                {
                    tv_finish.setText(getResources().getString(R.string.agree));
                    tv_estimate.setText(getResources().getString(R.string.disagree));
                }
                if(confirmedOrderDetail.getStatus() == 8)
                {
                    tv_finish.setVisibility(View.GONE);
                    tv_estimate.setVisibility(View.GONE);

                }
            }
            if(confirmedOrderDetail.getServiceReceiver().getId() == Auth.getCurrentUserId())
            {
                if(confirmedOrderDetail.getStatus() == 1) {
                    tv_finish.setText("催单");
                    tv_estimate.setText(getResources().getString(R.string.refund_apply));
                }
                if(confirmedOrderDetail.getStatus() == 5) {
                    tv_finish.setText("付款");
                    tv_estimate.setText(getResources().getString(R.string.refund_apply));
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
                if(confirmedOrderDetail.getStatus() == 7)
                {
                    tv_finish.setVisibility(View.GONE);
                    tv_estimate.setVisibility(View.GONE);

                }
                if(confirmedOrderDetail.getStatus() == 8)
                {
                    tv_finish.setVisibility(View.GONE);
                    tv_estimate.setVisibility(View.GONE);
//                    tv_finish.setVisibility(View.GONE);
//                    tv_estimate.setVisibility(View.VISIBLE);
//                    tv_estimate.setText(getResources().getString(R.string.dispute));

                }

            }
        }
        else if(from.equals(AppConfig.ORDERSTATUS_DONE))
        {
            tv_finish.setVisibility(View.GONE);
            tv_estimate.setVisibility(View.VISIBLE);
            tv_estimate.setText(getResources().getString(R.string.estimate));
        }
        else if(from.equals(AppConfig.ORDERSTATUS_CLOSE))
        {
            tv_finish.setVisibility(View.GONE);
            tv_estimate.setVisibility(View.GONE);
        }
        else
        {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("订单详情页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("订单详情页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
//    public class PopupWindows extends PopupWindow
//    {
//
//        public PopupWindows(Context mContext, View parent)
//        {
//
//            View view = View.inflate(mContext, R.layout.popupwindow_order, null);
//            view.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.fade_ins));
//            LinearLayout ll_popup = (LinearLayout) view
//                    .findViewById(R.id.ll_popup);
//            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
//                    R.anim.push_bottom_in_2));
//
//            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//            setFocusable(true);
//            setBackgroundDrawable(new BitmapDrawable());
//            setOutsideTouchable(true);
//            setContentView(view);
//            showAsDropDown(parent, 0,0);
//            update();
//
//            Button btn_viewfdinfo = (Button) view
//                    .findViewById(R.id.item_popupwindows_viewfdinfo);
//            final Button btn_collectorder = (Button) view
//                    .findViewById(R.id.item_popupwindows_collectorder);
//            final Button btn_toporder = (Button) view
//                    .findViewById(R.id.item_popupwindows_toporder);
//            Button btn_reportorder = (Button) view
//                    .findViewById(R.id.item_popupwindows_reportorder);
//            if(hasTopOrder != null && hasTopOrder.equals("1"))
//            {
//                btn_toporder.setText("取消置顶");
//            }
//            else
//            {
//                btn_toporder.setText("置顶本单");
//            }
//            if(Entrance != null && Entrance.equals("mycollect"))
//            {
//                btn_collectorder.setText("取消收藏");
//            }
//            else
//            {
//                btn_collectorder.setText("收藏本单");
//            }
////            Button btn_viewrelationnet = (Button) view
////                    .findViewById(R.id.item_popupwindows_viewrelationnet);
//
//            btn_viewfdinfo.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    //startActivity(new Intent(OrderDetailsActivity.this, FriendsInfoActivity.class));
//                    intent.setClass(ConfirmOrderDetailsActivity.this, FriendsInfoActivity.class);
//                    bundle.putSerializable("OrderDetails", orderDetails);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    dismiss();
//                }
//            });
//            btn_collectorder.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    if(btn_collectorder.getText().equals("收藏本单")) {
//                        Http.request(ConfirmOrderDetailsActivity.this, API.ADD_FAVORITEORDER, Http.map(
//                                "UserId", String.valueOf(Auth.getCurrentUserId()),
//                                "OrderId", OrderID
//                        ), new Http.RequestListener<String>() {
//                            @Override
//                            public void onSuccess(String result) {
//                                super.onSuccess(result);
//
//                                Toast.makeText(ConfirmOrderDetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//                    else
//                    {
//                        Http.request(ConfirmOrderDetailsActivity.this, API.DELETE_FAVORITEORDER, new Object[]{OrderID}, new Http.RequestListener<String>() {
//                            @Override
//                            public void onSuccess(String result) {
//                                super.onSuccess(result);
//
//                                Toast.makeText(ConfirmOrderDetailsActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        });
//                    }
//
//                    dismiss();
//                }
//            });
//            btn_toporder.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//                    if(btn_toporder.getText().equals("取消置顶"))
//                    {
//                        SharedPreferences.Editor e = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE).edit();
//                        e.remove("OrderDetails").commit();
//                        btn_toporder.setText("置顶本单");
//                        Toast.makeText(ConfirmOrderDetailsActivity.this, "取消置顶成功", Toast.LENGTH_SHORT).show();
//
//                    }
//                    else {
//                        SharedPreferences sp = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE);
//                        String OrderDetails = sp.getString("OrderDetails", null);
//                        if (!TextUtils.isEmpty(OrderDetails))
//                        {
//                            btn_toporder.setEnabled(false);
//                        }
//                        else
//                        {
//                            SharedPreferences.Editor e = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE).edit();
//                            e.putString("OrderDetails", GsonHelper.getGson().toJson(orderDetails)).commit();
//                            Toast.makeText(ConfirmOrderDetailsActivity.this, "置顶成功", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    dismiss();
//                }
//            });
//
//            btn_reportorder.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
//
//                    dismiss();
//                }
//            });
////            btn_viewrelationnet.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    startActivity(new Intent(OrderDetailsActivity.this, RelativenetActivity.class));
////                    dismiss();
////                }
////            });
//        }
//    }

}

