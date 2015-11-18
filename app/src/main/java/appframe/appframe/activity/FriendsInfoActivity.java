package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;

import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.LoginSampleHelper;

/**
 * Created by Administrator on 2015/8/21.
 */
public class FriendsInfoActivity extends BaseActivity implements View.OnClickListener{
    private ImageView img_avatar;
    private Button btn_sendmessage,btn_friendsestimate;
    private TextView tb_title,tb_back,tb_action,tv_name;
    private OrderDetails orderDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsinfo);
        init();
    }
    protected void init()
    {
        btn_sendmessage = (Button)findViewById(R.id.btn_sendmessage);
        tv_name = (TextView)findViewById(R.id.tv_name);
        btn_sendmessage.setOnClickListener(this);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_title.setText("帮友资料");
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("需求单");
        tb_back.setOnClickListener(this);
        tb_action = (TextView)findViewById(R.id.tb_action);
        Drawable drawable= getResources().getDrawable(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tb_action.setCompoundDrawables(drawable, null, null, null);
        tb_action.setOnClickListener(this);
        btn_friendsestimate =(Button)findViewById(R.id.btn_friendsestimate);
        btn_friendsestimate.setOnClickListener(this);
        Intent intent = this.getIntent();
        orderDetails = (OrderDetails)intent.getSerializableExtra("OrderDetails");
        tv_name.setText(orderDetails.getOrderer().getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_sendmessage:
                //Log.i("IM state", String.format("%s", App.mIMKit.getIMCore().getLoginState()));
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = String.valueOf(orderDetails.getOrderer().getId());// 消息接收者ID  testpro1  testpro2
                Intent intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_action:
                new PopupWindows(FriendsInfoActivity.this,tb_action);
                break;
            case R.id.btn_friendsestimate:
                startActivity(new Intent(this, EstimateActivity.class));
                break;
        }
    }


    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_finfo, null);
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

            Button addoneclassfriend = (Button) view
                    .findViewById(R.id.item_popupwindows_addoneclassfriend);
            Button viewrelationnet = (Button) view
                    .findViewById(R.id.item_popupwindows_viewrelationnet);
            Button addblacklist = (Button) view
                    .findViewById(R.id.item_popupwindows_addblacklist);
            Button btn_report = (Button) view
                    .findViewById(R.id.item_popupwindows_report);
            addoneclassfriend.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(FriendsInfoActivity.this, API.ADD_FDF, new Object[]{Auth.getCurrentUserId()},
                            Http.map("FriendId",String.valueOf(orderDetails.getOrderer().getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    Toast.makeText(FriendsInfoActivity.this, "已发送好友申请", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                }
            });
            viewrelationnet.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    startActivity(new Intent(FriendsInfoActivity.this, RelativenetActivity.class));
                    dismiss();
                }
            });
            addblacklist.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dismiss();
                }
            });

            btn_report.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dismiss();
                }
            });
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = this.getMenuInflater();
//        inflater.inflate(R.menu.menu_finfo, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
}
