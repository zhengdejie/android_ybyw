package appframe.appframe.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.dto.MRussianTour;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderPay;
import appframe.appframe.dto.PayResult;
import appframe.appframe.dto.OnlinePay;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.RussianPay;
import appframe.appframe.dto.RussianTour;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Arith;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.Utils;

/**
 * Created by Administrator on 2016/2/18.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener{

    private TextView tb_title,tb_back,tv_paybyali,tv_paybyweixing,tv_paybyyb,tv_total,tv_showtotal,tv_pay;
//    private Button tv_pay;
    private Drawable icon;
    private int TypeofPay = 0;
    private final int AliPay = 1;
    private final int WeixingPay = 2;
    private final int YBPay = 3;
    private static final int SDK_PAY_FLAG = 1;
    private String PlatformType = "";
    private double wallet = 0.0;
    private IWXAPI api;
    private RussianTour russianTour;
    private MRussianTour mRussianTour;

    GuideTourOrder guideTourOrder;
    OrderDetails orderDetails,orderDetailsPay,Candidate;
    ConfirmedOrderDetail confirmedOrderDetail,confirmedOrderDetailPay;
    OrderPay orderPay;
    Question question,myQuestion;
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
                        if(OrderSendActivity.instance != null) {
                            OrderSendActivity.instance.finish();
                        }
                        if(QuestionSendActivity.instance != null) {
                            QuestionSendActivity.instance.finish();
                        }
                        if(TourOrderSendActivity.instance !=null)
                        {
                            TourOrderSendActivity.instance.finish();
                        }
                        if(TourPersonDetailsActivity.instance !=null)
                        {
                            TourPersonDetailsActivity.instance.finish();
                        }
                        if(TourOrderDetailsActivity.instance !=null)
                        {
                            TourOrderDetailsActivity.instance.finish();
                        }
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(PayActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Bundle b = msg.getData();
                        String From = b.getString("From");
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }

//                        if(From.equals("Russian"))
//                        {
//                            Http.request(PayActivity.this, API.RUSSIANTOURPREPAYFAIL, new Object[]{String.valueOf(mRussianTour.getTourId())}, Http.map(
//                                    "PaymentMethod","ALIPAY"
//
//                                    ),
//                                    new Http.RequestListener<RussianPay>() {
//                                        @Override
//                                        public void onSuccess(final RussianPay result) {
//                                            super.onSuccess(result);
//
//
//                                        }
//
//
//                                        @Override
//                                        public void onFail(String code) {
//                                            super.onFail(code);
//                                        }
//                                    });
//                        }
//                        else if(From.equals("MYRussian"))
//                        {
//                            Http.request(PayActivity.this, API.RUSSIANTOURPREPAYFAIL, new Object[]{String.valueOf(russianTour.getId())}, Http.map(
//                                    "PaymentMethod","ALIPAY"
//
//                                    ),
//                                    new Http.RequestListener<RussianPay>() {
//                                        @Override
//                                        public void onSuccess(final RussianPay result) {
//                                            super.onSuccess(result);
//
//
//                                        }
//
//
//                                        @Override
//                                        public void onFail(String code) {
//                                            super.onFail(code);
//                                        }
//                                    });
//                        }
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
        setContentView(R.layout.activity_pay);
        init();
        initdata();
    }

    protected  void initdata()
    {
        Http.request(this, API.USER_PROFILE, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<UserDetail>() {
            @Override
            public void onSuccess(UserDetail result) {
                super.onSuccess(result);

                wallet = result.getWalletTotal();
                tv_paybyyb.setText(String.format("余额支付(%.2f元)", wallet));
            }

            @Override
            public void onFail(String code) {
                super.onFail(code);
            }
        });
    }

    protected  void init()
    {
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        tv_total = (TextView)findViewById(R.id.tv_total);
        tv_showtotal = (TextView)findViewById(R.id.tv_showtotal);
        tv_paybyali = (TextView)findViewById(R.id.tv_paybyali);
        tv_paybyweixing = (TextView)findViewById(R.id.tv_paybyweixing);
        tv_paybyyb = (TextView)findViewById(R.id.tv_paybyyb);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_pay = (TextView)findViewById(R.id.tv_pay);
        tb_back.setText("返回");
        tb_title.setText("支付详情");
        tb_back.setOnClickListener(this);
        tv_paybyali.setOnClickListener(this);
        tv_paybyweixing.setOnClickListener(this);
        tv_paybyyb.setOnClickListener(this);
        tv_pay.setOnClickListener(this);
        icon = getResources().getDrawable(R.drawable.ic_task_status_list_check);
        orderDetails = (OrderDetails)getIntent().getSerializableExtra("OrderDetails");
        orderDetailsPay = (OrderDetails)getIntent().getSerializableExtra("OrderDetailsPay");
        Candidate = (OrderDetails)getIntent().getSerializableExtra("Candidate");
        confirmedOrderDetail = (ConfirmedOrderDetail)getIntent().getSerializableExtra("ConfirmedOrderDetail");
        confirmedOrderDetailPay = (ConfirmedOrderDetail)getIntent().getSerializableExtra("ConfirmedOrderDetailPay");
        question = (Question)getIntent().getSerializableExtra("Question");
        myQuestion = (Question)getIntent().getSerializableExtra("MyQuestion");
        russianTour = (RussianTour)getIntent().getSerializableExtra("RussianTour");
        mRussianTour = (MRussianTour)getIntent().getSerializableExtra("MRussianTour");
        guideTourOrder = (GuideTourOrder)getIntent().getSerializableExtra("GuideTourOrder");

        orderPay = (OrderPay)getIntent().getSerializableExtra("OrderPay");
        if(orderDetails != null) {
            tv_showtotal.setText(String.valueOf(orderDetails.getBounty()));
        }
        if(confirmedOrderDetail != null)
        {
            tv_showtotal.setText(String.valueOf(confirmedOrderDetail.getBid()));
        }
        if(confirmedOrderDetailPay != null)
        {
            tv_showtotal.setText(String.valueOf(confirmedOrderDetailPay.getBid()));
        }

        if(orderDetailsPay != null) {
            tv_showtotal.setText(String.valueOf(orderDetailsPay.getBounty()));
        }
        if(orderPay != null)
        {
            tv_showtotal.setText(String.valueOf(orderPay.getAmount()));
        }

        if(question != null)
        {
            tv_showtotal.setText(String.valueOf(question.getBounty()));
        }

        if(myQuestion != null)
        {
            tv_showtotal.setText(String.valueOf(myQuestion.getBounty()));
        }
        if(russianTour != null)
        {
            tv_showtotal.setText(String.valueOf(russianTour.getApplicationDeposit()));
        }
        if(mRussianTour != null)
        {
            tv_showtotal.setText(String.valueOf(mRussianTour.getApplicationDeposit()));
        }
        if(guideTourOrder != null)
        {
            tv_showtotal.setText(String.valueOf(guideTourOrder.getGuide().getHourlyRatePrice() * guideTourOrder.getHours()));
        }


        if(Candidate != null) {
            double Amount = 0.0;
            if(Candidate.getCandidate() != null) {
                for (ConfirmedOrderDetail confirmedOrderDetail : Candidate.getCandidate()) {
                    if (confirmedOrderDetail.getStatus() == 11) {
                        Amount = Arith.add(Amount, confirmedOrderDetail.getBid());
                    }
                }
            }
            //关闭订单
            if(Candidate.getOrderStatus().equals("1")) {

            }
            else if (Candidate.getOrderStatus().equals("0"))
            {
                Amount = Arith.add(Amount,Candidate.getBounty());
            }
            else if (Candidate.getOrderStatus().equals("4"))
            {
                Amount = Arith.sub(Candidate.getBounty(), Candidate.getPrepay());
            }
            else
            {

            }
            tv_showtotal.setText(String.valueOf(Amount));
        }
    }

    private String getCandidateID(List<ConfirmedOrderDetail> confirmedOrderDetailList)
    {
        StringBuilder ReceivedID = new StringBuilder();
        for(ConfirmedOrderDetail confirmedOrderDetail : confirmedOrderDetailList)
        {
            ReceivedID.append(",").append(confirmedOrderDetail.getId());
        }

        return ReceivedID.deleteCharAt(0).toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_paybyali:
                TypeofPay = AliPay;
                tv_paybyali.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                tv_paybyweixing.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tv_paybyyb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;

            case R.id.tv_paybyweixing:
                TypeofPay = WeixingPay ;
                tv_paybyali.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tv_paybyweixing.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                tv_paybyyb.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;

            case R.id.tv_paybyyb:
                if( wallet >= Double.parseDouble(tv_showtotal.getText().toString())) {
                    TypeofPay = YBPay;
                    tv_paybyali.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    tv_paybyweixing.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    tv_paybyyb.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                }
                else
                {
                    Toast.makeText(this,String.format("余额:%s,不足以支付该款项,请您充值余额",String.valueOf(wallet)),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_pay:
                if(TypeofPay == AliPay)
                {
                    PlatformType = "1";
                    //乐游预约支付
                    if(guideTourOrder != null) {

                        Http.request(PayActivity.this, API.GUIDEPREPAY, new Object[]{String.valueOf(guideTourOrder.getId())}, Http.map(
                                "PaymentMethod","ALIPAY"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */

                                        if (result != null) {

                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("From","GuideTourOrder");
                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    msg.setData(bundle);
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();

                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }

                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //俄罗斯继续支付
                    if(mRussianTour != null) {
                        Http.request(PayActivity.this, API.RUSSIANTOURPREPAY, new Object[]{String.valueOf(mRussianTour.getTourId())}, Http.map(
                                "PaymentMethod","ALIPAY"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */

                                        if (result != null) {

                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("From","MYRussian");
                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    msg.setData(bundle);
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();

                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }

                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //俄罗斯报名支付
                    if(russianTour != null) {
                        Http.request(PayActivity.this, API.RUSSIANTOURPREPAY, new Object[]{String.valueOf(russianTour.getId())}, Http.map(
                                "PaymentMethod","ALIPAY"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */

                                        if (result != null) {

                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("From","Russian");
                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    msg.setData(bundle);
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();

                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }

                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //发单支付
                    if(orderDetails != null) {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "2",
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderDetails.getId()),
                                        "Sign",getSign("ActionType", "2",
                                                "PlatformType",PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderDetails.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */
//                        final String payInfo = AliPay.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01", "1") + "&sign=\"" + result.getSign() + "\"&" + AliPay.getSignType();

                                        if (result != null) {

                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);
//                                                    Bundle bundle = new Bundle();
//                                                    bundle.putString("orderDetails","orderDetails");
                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
//                                                    msg.setData(bundle);
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();

                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();

                                        }

                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //未支付的任务支付
                    if(orderDetailsPay != null)
                    {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "2",
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderDetailsPay.getId()),
                                        "Sign",getSign("ActionType", "2",
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderDetailsPay.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */
//                        final String payInfo = AliPay.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01", "1") + "&sign=\"" + result.getSign() + "\"&" + AliPay.getSignType();

                                        if (result != null) {
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //接单支付
                    if(confirmedOrderDetail != null)
                    {
                        Http.request(PayActivity.this, API.CONFIRMEDORDERPAY, Http.map(
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "ConfirmedOrderId",String.valueOf(confirmedOrderDetail.getId()),
                                        "orderId",String.valueOf(confirmedOrderDetail.getOrder().getId())),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */
//                        final String payInfo = AliPay.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01", "1") + "&sign=\"" + result.getSign() + "\"&" + AliPay.getSignType();

                                        if (result != null) {
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //接单未支付的支付
                    if(confirmedOrderDetailPay != null)
                    {
                        Http.request(PayActivity.this, API.CONFIRMEDORDERPAY, Http.map(
                                    "PlatformType", PlatformType,
                                    "Amount", String.valueOf(confirmedOrderDetailPay.getBid()),
                                    "ConfirmedOrderId",String.valueOf(confirmedOrderDetailPay.getId()),
                                    "orderId",String.valueOf(confirmedOrderDetailPay.getOrder().getId())),
                            new Http.RequestListener<OnlinePay>() {
                                @Override
                                public void onSuccess(final OnlinePay result) {
                                    super.onSuccess(result);
                                    /**
                                     * 完整的符合支付宝参数规范的订单信息
                                     */
//                        final String payInfo = AliPay.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01", "1") + "&sign=\"" + result.getSign() + "\"&" + AliPay.getSignType();


                                    Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            // 构造PayTask 对象
                                            PayTask alipay = new PayTask(PayActivity.this);
                                            // 调用支付接口，获取支付结果
                                            String resultInfo = alipay.pay(result.getSign(), true);

                                            Message msg = new Message();
                                            msg.what = SDK_PAY_FLAG;
                                            msg.obj = resultInfo;
                                            mHandler.sendMessage(msg);
                                        }
                                    };

                                    // 必须异步调用
                                    Thread payThread = new Thread(payRunnable);
                                    payThread.start();
                                    finish();
                                }

                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);
                                }
                            });
                    }
                    //候选人补充付款
                    if(orderPay != null)
                    {
                        String ActionType = "";
                        //关闭订单
//                        if(orderPay.getOrderStatus().equals("1")) {
//                            ActionType = "3";
//                        }
//                        else if (orderPay.getOrderStatus().equals("0"))
//                        {
//                            ActionType = "5";
//                        }

                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", orderPay.getOrderStatus().toString(),
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderPay.getOrderId()),
                                        "CandidateId", String.valueOf(orderPay.getCandidateId()),
                                        "Sign",getSign("ActionType", orderPay.getOrderStatus().toString(),
                                                "PlatformType",PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderPay.getOrderId()),
                                                "CandidateId", String.valueOf(orderPay.getCandidateId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */

                                        if (result != null) {
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });



                    }//候选人补充付款
                    //候选人未支付补充付款
                    if(Candidate != null)
                    {
                        String ActionType = "";


                        if(Candidate.getOrderStatus().equals("2")) {
                            ActionType = "5";
                        }
                        else if (Candidate.getOrderStatus().equals("3"))
                        {
                            ActionType = "3";
                        }
                        else if (Candidate.getOrderStatus().equals("4"))
                        {
                            ActionType = "11";
                        }
                        else
                        {

                        }
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", ActionType,
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(Candidate.getId()),
                                        "CandidateId", Candidate.getCandidate() == null ? "" : getCandidateID(Candidate.getCandidate()),
                                        "Sign",getSign("ActionType", ActionType,
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(Candidate.getId()),
                                                "CandidateId", Candidate.getCandidate() == null ? "" : getCandidateID(Candidate.getCandidate()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */

                                        if (result != null) {
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        } else {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
//                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //提问
                    if(question != null)
                    {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "9",
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(question.getId()),
                                        "Sign",getSign("ActionType", "9",
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(question.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */
//                        final String payInfo = AliPay.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01", "1") + "&sign=\"" + result.getSign() + "\"&" + AliPay.getSignType();

                                        if (result != null) {
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
//                                        finish();

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //我的提问
                    if(myQuestion != null)
                    {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "9",
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(myQuestion.getId()),
                                        "Sign",getSign("ActionType", "9",
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(myQuestion.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        /**
                                         * 完整的符合支付宝参数规范的订单信息
                                         */
//                        final String payInfo = AliPay.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01", "1") + "&sign=\"" + result.getSign() + "\"&" + AliPay.getSignType();

                                        if (result != null) {
                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 构造PayTask 对象
                                                    PayTask alipay = new PayTask(PayActivity.this);
                                                    // 调用支付接口，获取支付结果
                                                    String resultInfo = alipay.pay(result.getSign(), true);

                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = resultInfo;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };

                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        }
                                        else
                                        {
                                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
//                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                }
                else if(TypeofPay == WeixingPay)
                {
                    PlatformType = "2";

                    //乐游预约支付
                    if(guideTourOrder != null) {
                        Http.request(PayActivity.this, API.GUIDEPREPAY, new Object[]{String.valueOf(guideTourOrder.getId())}, Http.map(
                                "PaymentMethod","WXPAY"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }



                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //俄罗斯继续支付
                    if(mRussianTour != null) {
                        Http.request(PayActivity.this, API.RUSSIANTOURPREPAY, new Object[]{String.valueOf(mRussianTour.getTourId())}, Http.map(
                                "PaymentMethod","WXPAY"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }



                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }


                    //俄罗斯报名支付
                    if(russianTour != null) {
                        Http.request(PayActivity.this, API.RUSSIANTOURPREPAY, new Object[]{String.valueOf(russianTour.getId())}, Http.map(
                                "PaymentMethod","WXPAY"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }



                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //发单支付
                    if(orderDetails != null) {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "2",
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderDetails.getId()),
                                        "Sign",getSign("ActionType", "2",
                                                "PlatformType",PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderDetails.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //未支付的任务支付
                    if(orderDetailsPay != null)
                    {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "2",
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderDetailsPay.getId()),
                                        "Sign",getSign("ActionType", "2",
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderDetailsPay.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //接单支付
                    if(confirmedOrderDetail != null)
                    {
                        Http.request(PayActivity.this, API.CONFIRMEDORDERPAY, Http.map(
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "ConfirmedOrderId",String.valueOf(confirmedOrderDetail.getId()),
                                        "orderId",String.valueOf(confirmedOrderDetail.getOrder().getId())),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //接单未支付的支付
                    if(confirmedOrderDetailPay != null)
                    {
                        Http.request(PayActivity.this, API.CONFIRMEDORDERPAY, Http.map(
                                        "PlatformType", PlatformType,
                                        "Amount", String.valueOf(confirmedOrderDetailPay.getBid()),
                                        "ConfirmedOrderId",String.valueOf(confirmedOrderDetailPay.getId()),
                                        "orderId",String.valueOf(confirmedOrderDetailPay.getOrder().getId())),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //候选人补充付款
                    if(orderPay != null)
                    {
                        String ActionType = "";
                        //关闭订单
//                        if(orderPay.getOrderStatus().equals("3")) {
//                            ActionType = "3";
//                        }
//                        else if (orderPay.getOrderStatus().equals("5"))
//                        {
//                            ActionType = "5";
//                        }
//                        else if (orderPay.getOrderStatus().equals("11"))
//                        {
//                            ActionType = "11";
//                        }
//                        else
//                        {
//
//                        }
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", orderPay.getOrderStatus().toString(),
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderPay.getOrderId()),
                                        "CandidateId", String.valueOf(orderPay.getCandidateId()),
                                        "Sign",getSign("ActionType", orderPay.getOrderStatus().toString(),
                                                "PlatformType",PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderPay.getOrderId()),
                                                "CandidateId", String.valueOf(orderPay.getCandidateId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });



                    }//候选人补充付款
                    //候选人未支付补充付款
                    if(Candidate != null)
                    {
                        String ActionType = "";

                        if(Candidate.getOrderStatus().equals("2")) {
                            ActionType = "5";
                        }
                        else if (Candidate.getOrderStatus().equals("3"))
                        {
                            ActionType = "3";
                        }
                        else if (Candidate.getOrderStatus().equals("4"))
                        {
                            ActionType = "11";
                        }
                        else
                        {

                        }
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", ActionType,
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(Candidate.getId()),
                                        "CandidateId", Candidate.getCandidate() == null ? "" : getCandidateID(Candidate.getCandidate()),
                                        "Sign",getSign("ActionType", ActionType,
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(Candidate.getId()),
                                                "CandidateId", Candidate.getCandidate() == null ? "" : getCandidateID(Candidate.getCandidate()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //提问
                    if(question != null)
                    {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "9",
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(question.getId()),
                                        "Sign",getSign("ActionType", "9",
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(question.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //我的提问
                    if(myQuestion != null)
                    {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "9",
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(myQuestion.getId()),
                                        "Sign",getSign("ActionType", "9",
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(myQuestion.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        PayReq req = new PayReq();
                                        req.appId = result.getAppid();
                                        req.partnerId	= result.getPartnerid();
                                        req.prepayId = result.getPrepayid();
                                        req.nonceStr = result.getNoncestr();
                                        req.timeStamp	= result.getTimestamp();
                                        req.packageValue = result.getPackage();
                                        req.sign = result.getSign();

                                        api.sendReq(req);
                                        finish();

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                }
                else if(TypeofPay == YBPay)
                {
                    PlatformType = "3";

                    //乐游预约支付
                    if(guideTourOrder != null) {

                        Http.request(PayActivity.this, API.GUIDEPREPAY, new Object[]{String.valueOf(guideTourOrder.getId())}, Http.map(
                                "PaymentMethod","BALANCE"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        if(TourOrderSendActivity.instance !=null)
                                        {
                                            TourOrderSendActivity.instance.finish();
                                        }
                                        if(TourPersonDetailsActivity.instance !=null)
                                        {
                                            TourPersonDetailsActivity.instance.finish();
                                        }
                                        if(TourOrderDetailsActivity.instance !=null)
                                        {
                                            TourOrderDetailsActivity.instance.finish();
                                        }

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                                        finish();

                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //俄罗斯继续支付
                    if(mRussianTour != null) {
                        Http.request(PayActivity.this, API.RUSSIANTOURPREPAY, new Object[]{String.valueOf(mRussianTour.getTourId())}, Http.map(
                                "PaymentMethod","BALANCE"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                                        finish();

                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //俄罗斯报名支付
                    if(russianTour != null) {
                        Http.request(PayActivity.this, API.RUSSIANTOURPREPAY, new Object[]{String.valueOf(russianTour.getId())}, Http.map(
                                "PaymentMethod","BALANCE"
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(final OnlinePay result) {
                                        super.onSuccess(result);
                                        if(TourOrderSendActivity.instance !=null)
                                        {
                                            TourOrderSendActivity.instance.finish();
                                        }
                                        if(TourPersonDetailsActivity.instance !=null)
                                        {
                                            TourPersonDetailsActivity.instance.finish();
                                        }

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                                        finish();

                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //发单支付
                    if(orderDetails != null) {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "2",
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderDetails.getId()),
                                        "Sign",getSign("ActionType", "2",
                                                "PlatformType",PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderDetails.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);
                                        if(OrderSendActivity.instance != null) {
                                            OrderSendActivity.instance.finish();
                                        }
                                        if(QuestionSendActivity.instance != null) {
                                            QuestionSendActivity.instance.finish();
                                        }
                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(PayActivity.this, HomeActivity.class));
                                        finish();
                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //未支付的任务支付
                    if(orderDetailsPay != null)
                    {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "2",
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderDetailsPay.getId()),
                                        "Sign", getSign("ActionType", "2",
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderDetailsPay.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(PayActivity.this, MyMissionActivity.class));
                                        finish();

                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //接单支付
                    if(confirmedOrderDetail != null)
                    {
                        Http.request(PayActivity.this, API.CONFIRMEDORDERPAY, Http.map(
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "ConfirmedOrderId",String.valueOf(confirmedOrderDetail.getId()),
                                        "orderId",String.valueOf(confirmedOrderDetail.getOrder().getId())
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(PayActivity.this, HomeActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //接单未支付的支付
                    if(confirmedOrderDetailPay != null)
                    {
                        Http.request(PayActivity.this, API.CONFIRMEDORDERPAY, Http.map(
                                        "PlatformType", PlatformType,
                                        "Amount", String.valueOf(confirmedOrderDetailPay.getBid()),
                                        "ConfirmedOrderId",String.valueOf(confirmedOrderDetailPay.getId()),
                                        "orderId",String.valueOf(confirmedOrderDetailPay.getOrder().getId())
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }

                    //候选人补充付款
                    if(orderPay != null)
                    {
                        String ActionType = "";
                        //关闭订单
//                        if(orderPay.getOrderStatus().equals("1")) {
//                            ActionType = "3";
//                        }
//                        else if (orderPay.getOrderStatus().equals("0"))
//                        {
//                            ActionType = "5";
//                        }

                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", orderPay.getOrderStatus().toString(),
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(orderPay.getOrderId()),
                                        "CandidateId", String.valueOf(orderPay.getCandidateId()),
                                        "Sign",getSign("ActionType", orderPay.getOrderStatus().toString(),
                                                "PlatformType",PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(orderPay.getOrderId()),
                                                "CandidateId", String.valueOf(orderPay.getCandidateId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(PayActivity.this, MyMissionActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });



                    }//候选人补充付款
                    //候选人未支付补充付款
                    if(Candidate != null)
                    {
                        String ActionType = "";

                        if(Candidate.getOrderStatus().equals("2")) {
                            ActionType = "5";
                        }
                        else if (Candidate.getOrderStatus().equals("3"))
                        {
                            ActionType = "3";
                        }
                        else if (Candidate.getOrderStatus().equals("4"))
                        {
                            ActionType = "11";
                        }
                        else
                        {

                        }
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", ActionType,
                                        "PlatformType", PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(Candidate.getId()),
                                        "CandidateId", Candidate.getCandidate() == null ? "" : getCandidateID(Candidate.getCandidate()),
                                        "Sign",getSign("ActionType", ActionType,
                                                "PlatformType", PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(Candidate.getId()),
                                                "CandidateId", Candidate.getCandidate() == null ? "" : getCandidateID(Candidate.getCandidate()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(PayActivity.this, MyMissionActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //提问
                    if(question != null) {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "9",
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(question.getId()),
                                        "Sign",getSign("ActionType", "9",
                                                "PlatformType",PlatformType,
                                                "Amount", tv_showtotal.getText().toString(),
                                                "OrderId", String.valueOf(question.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);
                                        if(OrderSendActivity.instance != null) {
                                            OrderSendActivity.instance.finish();
                                        }
                                        if(QuestionSendActivity.instance != null) {
                                            QuestionSendActivity.instance.finish();
                                        }
                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(PayActivity.this, HomeActivity.class));
                                        finish();
                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                    //我的提问
                    if(myQuestion != null) {
                        Http.request(PayActivity.this, API.ORDERPAY, Http.map(
                                        "ActionType", "9",
                                        "PlatformType",PlatformType,
                                        "Amount", tv_showtotal.getText().toString(),
                                        "OrderId", String.valueOf(myQuestion.getId()),
                                        "Sign",getSign("ActionType", "9",
                                                        "PlatformType",PlatformType,
                                                        "Amount", tv_showtotal.getText().toString(),
                                                        "OrderId", String.valueOf(myQuestion.getId()))
                                ),
                                new Http.RequestListener<OnlinePay>() {
                                    @Override
                                    public void onSuccess(OnlinePay result) {
                                        super.onSuccess(result);

                                        Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(PayActivity.this, HomeActivity.class));
                                        finish();
                                    }


                                    @Override
                                    public void onFail(String code) {
                                        super.onFail(code);
                                    }
                                });
                    }
                }
                else
                {
                    Toast.makeText(PayActivity.this, "请选择一种支付方式", Toast.LENGTH_SHORT).show();
                }





                break;

            case R.id.tb_back:
                finish();
                break;

        }
    }

    protected String getSign(String ... args)
    {
        SortedMap<String,String> map = new TreeMap<String,String>();
        for(int i=0;i<args.length-1;i+=2) {
            map.put(args[i], args[i + 1]);
        }
        Iterator<String> iter = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        String key ="";
        while (iter.hasNext()) {
            key = iter.next();
            if( key != "Sign") {
                sb.append(key);
                sb.append("=");
                sb.append(map.get(key));
                sb.append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(AppConfig.MD5_secret);
        String re_md5 = encryptionMD5(sb.toString());
        return re_md5;
    }

    /**
     *
     * @param plainText
     *            明文
     * @return 32位密文
     */
    public String encryptionMD5(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("支付页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("支付页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

