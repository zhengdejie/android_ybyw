package appframe.appframe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.PayResult;
import appframe.appframe.dto.OnlinePay;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/2/22.
 */
public class TopupActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_title,tb_back,tv_alipay,tv_weixing;
    private EditText et_yuer;
    private IWXAPI api;
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
                        Toast.makeText(TopupActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(TopupActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(TopupActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_topup);
        init();
    }
    protected  void init()
    {
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_alipay = (TextView)findViewById(R.id.tv_alipay);
        tv_weixing = (TextView)findViewById(R.id.tv_weixing);
        et_yuer = (EditText)findViewById(R.id.et_yuer);
        tb_back.setText("返回");
        tb_title.setText("充值");

        tb_back.setOnClickListener(this);
        tv_alipay.setOnClickListener(this);
        tv_weixing.setOnClickListener(this);
        et_yuer.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    et_yuer.setText(s);
                    et_yuer.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                et_yuer.setText(s);
                et_yuer.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    et_yuer.setText(s.subSequence(0, 1));
                    et_yuer.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_alipay:
                if (Double.parseDouble(et_yuer.getText().toString()) <= 1) {
                    Toast.makeText(TopupActivity.this, "金额不能小于1元", Toast.LENGTH_SHORT).show();
                }
                else {
                    Http.request(TopupActivity.this, API.TOPUP, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "PlatformType", "1",
                            "Amount", et_yuer.getText().toString()), new Http.RequestListener<OnlinePay>() {
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
                                    PayTask alipay = new PayTask(TopupActivity.this);
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

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
                        }
                    });
                }
                break;
            case R.id.tv_weixing:
                if (Double.parseDouble(et_yuer.getText().toString()) <= 1) {
                    Toast.makeText(TopupActivity.this, "金额不能小于1元", Toast.LENGTH_SHORT).show();
                }
                else {
                    Http.request(TopupActivity.this, API.TOPUP, new Object[]{Auth.getCurrentUserId()}, Http.map(
                            "PlatformType", "2",
                            "Amount", et_yuer.getText().toString()), new Http.RequestListener<OnlinePay>() {
                        @Override
                        public void onSuccess(final OnlinePay result) {
                            super.onSuccess(result);

                            PayReq req = new PayReq();
                            req.appId = result.getAppid();
                            req.partnerId = result.getPartnerid();
                            req.prepayId = result.getPrepayid();
                            req.nonceStr = result.getNoncestr();
                            req.timeStamp = result.getTimestamp();
                            req.packageValue = result.getPackage();
                            req.sign = result.getSign();

                            api.sendReq(req);
                        }

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
                        }
                    });
                }
                break;
        }
    }
}

