package appframe.appframe.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import java.util.HashMap;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.HomeActivity;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.fragment.PersonFragment;

public class PushDemoReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
//    public static StringBuilder payloadData = new StringBuilder();

    NotificationUtils admain;
    static int NOTIFICATION_ID = 13565400;
    Intent intent;
    String ticker = "您有新消息";
    int smallIcon = R.drawable.logomini;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);

//                    Log.d("GetuiSdkDemo", "receiver payload : " + data);
                    //Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
                    // 设置点击后启动的activity

                    //intent.putExtra("pushmessage","push");
                    if(AppConfig.RECEIVE_NOTIFICATION) {
                        admain = new NotificationUtils(context, NOTIFICATION_ID);
                        final String[] message = data.split("/");

//                        RemoteViews rv = new RemoteViews(context.getPackageName(),
//                                R.layout.notification);
//                        rv.setTextViewText(R.id.tv_title, message[0]);
//                        rv.setTextViewText(R.id.tv_content, message[1]);
//                        rv.setTextViewText(R.id.tv_time, String.valueOf(System.currentTimeMillis()));
//                        admain.view_notification(rv,intent,smallIcon, ticker);
                        if(!message[2].equals("0"))
                        {
                            Intent odIntent = new Intent();
                            Bundle odBundle = new Bundle();
                            odIntent.setClass(context, OrderDetailsActivity.class);
                            odBundle.putSerializable("OrderIdFromPushDemoReceiver", message[2]);

                            odBundle.putString("From", "MyOrder");
                            odIntent.putExtras(odBundle);
                            admain.normal_notification(odIntent, smallIcon, ticker, message[0],message[1]);
//                            Map<String, String> map = new HashMap<String, String>();
//                            map.put("Id", message[2]);
//                            Http.request((Activity) App.getContext(), API.GETORDERBYID, new Object[]{Http.getURL(map)},
//
//                                    new Http.RequestListener<OrderDetails>() {
//                                        @Override
//                                        public void onSuccess(OrderDetails result) {
//                                            super.onSuccess(result);
//                                            Intent odIntent = new Intent();
//                                            Bundle odBundle = new Bundle();
//                                            odIntent.setClass(context, OrderDetailsActivity.class);
//                                            odBundle.putSerializable("OrderDetails", result);
//
//                                            odBundle.putString("From", "Order");
//                                            odIntent.putExtras(odBundle);
//                                            admain.normal_notification(odIntent, smallIcon, ticker, message[0],message[1]);
//
//                                        }
//                                    });


                        }
                        else
                        {
                            intent = new Intent(context, HomeActivity.class);
                            admain.normal_notification(intent, smallIcon, ticker, message[0],message[1]);
                        }



                        if(message[1].contains("您收到"))
                        {
                            String[] content = message[1].split("，");
                            Toast toast = Toast.makeText(context, content[1], Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }
                    }
//                    payloadData.append(data);
//                    payloadData.append("\n");
                    if(HomeActivity.tv_unread != null) {
                        HomeActivity.tv_unread.setVisibility(View.VISIBLE);
                    }
                    if(PersonFragment.tv_unread != null) {
                        PersonFragment.tv_unread.setVisibility(View.VISIBLE);
                    }

                }
                break;

            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
//                String cid = bundle.getString("clientid");
//                if (GetuiSdkDemoActivity.tView != null) {
//                    GetuiSdkDemoActivity.tView.setText(cid);
//                }
                break;

            case PushConsts.THIRDPART_FEEDBACK:
                /*
                 * String appid = bundle.getString("appid"); String taskid =
                 * bundle.getString("taskid"); String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result"); long timestamp =
                 * bundle.getLong("timestamp");
                 * 
                 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " +
                 * taskid); Log.d("GetuiSdkDemo", "actionid = " + actionid); Log.d("GetuiSdkDemo",
                 * "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
                break;

            default:
                break;
        }
    }
}
