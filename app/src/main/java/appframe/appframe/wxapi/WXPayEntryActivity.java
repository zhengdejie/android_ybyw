package appframe.appframe.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//import com.alibaba.mobileimexternal.ui.fundamental.widget.TextViewWithWhiteShadow;


import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import appframe.appframe.R;
import appframe.appframe.activity.HomeActivity;
import appframe.appframe.activity.OrderSendActivity;
import appframe.appframe.activity.PayActivity;
import appframe.appframe.activity.QuestionSendActivity;
import appframe.appframe.activity.TourOrderDetailsActivity;
import appframe.appframe.activity.TourOrderSendActivity;
import appframe.appframe.activity.TourPersonDetailsActivity;
import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;
import appframe.appframe.utils.Utils;

/**
 * Created by Administrator on 2016/3/28.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private TextView tv_result;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpayentry);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);

        TextView tv_result = (TextView)findViewById(R.id.tv_result);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.app_tip);
//            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//            builder.show();
            if(resp.errCode == 0) {
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
                Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(WXPayEntryActivity.this, HomeActivity.class));
                finish();
            }
            else if(resp.errCode == -2) {
                Toast.makeText(WXPayEntryActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(WXPayEntryActivity.this, HomeActivity.class));
                finish();
            }
            else {
                Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(WXPayEntryActivity.this, HomeActivity.class));
                finish();
            }
        }
    }
}