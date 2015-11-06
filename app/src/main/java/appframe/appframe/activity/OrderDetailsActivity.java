package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.NoCopySpan;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderComment;

/**
 * Created by Administrator on 2015/8/14.
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener{

    private ImageView img_avatar;
    private TextView tv_name,tv_title,tv_money,tv_time,tv_location,tv_type,tv_status,tv_content,tv_range,tv_deadline,tv_require,tv_paymethod,tb_back,tb_action,tb_title,tv_comment;
    private ImageButton imgbtn_conversation,imgbtn_call;
    private Button btn_select,btn_estimate,btn_comment;
    private String OrderID,Tel, hasTopOrder, Entrance;
    private ListView lv_ordercomment;
    OrderDetails orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_avatar:
                startActivity(new Intent(this,FriendsInfoActivity.class));
                break;
            case R.id.imgbtn_conversation:

                Intent intent = App.mIMKit.getChattingActivityIntent("102");
                startActivity(intent);
                break;
            case R.id.imgbtn_call:
                if(!Tel.equals("")) {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Tel)); //直接拨打电话android.intent.action.CALL
                    startActivity(phoneIntent);
                }
                else
                {
                    Toast.makeText(OrderDetailsActivity.this,"该用户不是用手机注册",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tb_action:
                new PopupWindows(OrderDetailsActivity.this,tb_action);
                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_select:
                Http.request(this, API.ORDER_ACCEPT, new Object[]{OrderID}, new Http.RequestListener<UserDetail>() {
                    @Override
                    public void onSuccess(UserDetail result) {
                        super.onSuccess(result);


                    }
                });
                break;
            case R.id.btn_estimate:
                startActivity(new Intent(this,OrderEstimateActivity.class));
                break;
            case R.id.btn_comment:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //final EditText comment = new EditText(this);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_leavemessage, (ViewGroup) findViewById(R.id.dialog));
                final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                final CheckBox cb_anonymous = (CheckBox)layout.findViewById(R.id.cb_anonymous);
                builder.setTitle("留言").setView(
                        layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Http.request(OrderDetailsActivity.this, API.ORDER_MAKECOOMENT, new Object[]{OrderID},Http.map(
                                        "Commentator",String.valueOf(Auth.getCurrentUserId()),
                                        "Comment",comment.getText().toString(),
                                        "Anonymity",String.valueOf(cb_anonymous.isChecked())),
                                new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);
                                Http.request(OrderDetailsActivity.this, API.ORDER_GETCOOMENT, new Object[]{OrderID},
                                        new Http.RequestListener<List<OrderComment>>() {
                                            @Override
                                            public void onSuccess(List<OrderComment> result) {
                                                super.onSuccess(result);
                                                if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
                                                    lv_ordercomment.setAdapter(new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), true));
                                                }
                                                else
                                                {
                                                    lv_ordercomment.setAdapter(new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), false));
                                                }
                                                setListViewHeightBasedOnChildren(lv_ordercomment);
                                                tv_comment.setText(String.format("留言（%d条）", result != null ? result.size() : 0));
                                            }
                                        });

                            }
                        });
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
        }
    }

    public void init()
    {
        img_avatar = (ImageView)findViewById(R.id.img_avatar);
        imgbtn_conversation = (ImageButton)findViewById(R.id.imgbtn_conversation);
        imgbtn_call = (ImageButton)findViewById(R.id.imgbtn_call);
        tv_title =(TextView)findViewById(R.id.tv_title);
        tv_money =(TextView)findViewById(R.id.tv_money);
        tv_location =(TextView)findViewById(R.id.tv_location);
        tv_type =(TextView)findViewById(R.id.tv_type);
        tv_content =(TextView)findViewById(R.id.tv_content);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);
        btn_select = (Button)findViewById(R.id.btn_select);
        tv_time = (TextView)findViewById(R.id.tv_time);
        tv_status = (TextView)findViewById(R.id.tv_status);
        tv_require = (TextView)findViewById(R.id.tv_require);
        tv_paymethod = (TextView)findViewById(R.id.tv_paymethod);
        tv_deadline = (TextView)findViewById(R.id.tv_deadline);
        tv_name = (TextView)findViewById(R.id.tv_name);
        btn_estimate = (Button)findViewById(R.id.btn_estimate);
        btn_comment = (Button)findViewById(R.id.btn_comment);
        lv_ordercomment = (ListView)findViewById(R.id.lv_ordercomment);
        tv_comment = (TextView)findViewById(R.id.tv_comment);

        img_avatar.setOnClickListener(this);
        imgbtn_conversation.setOnClickListener(this);
        imgbtn_call.setOnClickListener(this);
        btn_select.setOnClickListener(this);
        btn_estimate.setOnClickListener(this);
        btn_comment.setOnClickListener(this);

        Intent intent = this.getIntent();
        orderDetails=(OrderDetails)intent.getSerializableExtra("OrderDetails");
        hasTopOrder = intent.getStringExtra("hasTopOrder") == null ? null : intent.getStringExtra("hasTopOrder");
        Entrance = intent.getStringExtra("Entrance") == null ? null : intent.getStringExtra("Entrance");
        tv_title.setText(orderDetails.getTitle().toString());
        tv_money.setText(String.valueOf(orderDetails.getBounty()));
        tv_location.setText(orderDetails.getAddress().toString());
        tv_type.setText(orderDetails.getCategory().toString());
        tv_content.setText(orderDetails.getContent().toString());
        tv_time.setText(orderDetails.getCreatedAt().toString());
        tv_status.setText(orderDetails.getOrderStatus().toString());
        tv_require.setText(orderDetails.getRequest().toString());
        tv_paymethod.setText(orderDetails.getPaymentMethod().toString());
        tv_deadline.setText(orderDetails.getDeadline().toString());
        tv_name.setText(orderDetails.getOrderer().getName().toString());
        Tel = orderDetails.getOrderer().getMobile() == null ? "" : orderDetails.getOrderer().getMobile().toString();
        OrderID = String.valueOf(orderDetails.getId());
        //Log.i("OrderDetailsID--",String.valueOf(orderDetails.getId()));
        tb_title.setText("需求单");
        tb_back.setText("友帮");
        Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tb_action.setCompoundDrawables(drawable, null, null, null);
        tb_action.setOnClickListener(this);
        tb_back.setOnClickListener(this);

        Http.request(OrderDetailsActivity.this, API.ORDER_GETCOOMENT, new Object[]{OrderID},
                new Http.RequestListener<List<OrderComment>>() {
                    @Override
                    public void onSuccess(List<OrderComment> result) {
                        super.onSuccess(result);
                        if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
                            lv_ordercomment.setAdapter(new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), true));
                        } else {
                            lv_ordercomment.setAdapter(new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), false));
                        }
                        setListViewHeightBasedOnChildren(lv_ordercomment);
                        tv_comment.setText(String.format("留言（%d条）",result != null ? result.size() : 0));
                    }
                });

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

    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_order, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAsDropDown(parent, 0,0);
            update();

            Button btn_viewfdinfo = (Button) view
                    .findViewById(R.id.item_popupwindows_viewfdinfo);
            final Button btn_collectorder = (Button) view
                    .findViewById(R.id.item_popupwindows_collectorder);
            final Button btn_toporder = (Button) view
                    .findViewById(R.id.item_popupwindows_toporder);
            Button btn_reportorder = (Button) view
                    .findViewById(R.id.item_popupwindows_reportorder);
            if(hasTopOrder != null && hasTopOrder.equals("1"))
            {
                btn_toporder.setText("取消置顶");
            }
            else
            {
                btn_toporder.setText("置顶本单");
            }
            if(Entrance != null && Entrance.equals("mycollect"))
            {
                btn_collectorder.setText("取消收藏");
            }
            else
            {
                btn_collectorder.setText("收藏本单");
            }
//            Button btn_viewrelationnet = (Button) view
//                    .findViewById(R.id.item_popupwindows_viewrelationnet);

            btn_viewfdinfo.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    startActivity(new Intent(OrderDetailsActivity.this, FriendsInfoActivity.class));
                    dismiss();
                }
            });
            btn_collectorder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(btn_collectorder.getText().equals("收藏本单")) {
                        Http.request(OrderDetailsActivity.this, API.ADD_FAVORITEORDER, Http.map(
                                "UserId", String.valueOf(Auth.getCurrentUserId()),
                                "OrderId", OrderID
                        ), new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);

                                Toast.makeText(OrderDetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else
                    {
                        Http.request(OrderDetailsActivity.this, API.DELETE_FAVORITEORDER, new Object[]{OrderID}, new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);

                                Toast.makeText(OrderDetailsActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    dismiss();
                }
            });
            btn_toporder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(btn_toporder.getText().equals("取消置顶"))
                    {
                        SharedPreferences.Editor e = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE).edit();
                        e.remove("OrderDetails").commit();
                        btn_toporder.setText("置顶本单");
                        Toast.makeText(OrderDetailsActivity.this, "取消置顶成功", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        SharedPreferences sp = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE);
                        String OrderDetails = sp.getString("OrderDetails", null);
                        if (!TextUtils.isEmpty(OrderDetails))
                        {
                            btn_toporder.setEnabled(false);
                        }
                        else
                        {
                            SharedPreferences.Editor e = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE).edit();
                            e.putString("OrderDetails", GsonHelper.getGson().toJson(orderDetails)).commit();
                            Toast.makeText(OrderDetailsActivity.this, "置顶成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                    dismiss();
                }
            });

            btn_reportorder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dismiss();
                }
            });
//            btn_viewrelationnet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(OrderDetailsActivity.this, RelativenetActivity.class));
//                    dismiss();
//                }
//            });
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = this.getMenuInflater();
//        inflater.inflate(R.menu.menu_order,menu);
//        MenuItem addItem = menu.findItem(R.id.action_order);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId())
//        {
//            case android.R.id.home:
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return true;
//            default: return super.onOptionsItemSelected(item);
//        }
//
//    }
}
