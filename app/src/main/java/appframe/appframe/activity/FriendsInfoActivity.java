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

import java.util.HashMap;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;

/**
 * Created by Administrator on 2015/8/21.
 */
public class FriendsInfoActivity extends BaseActivity implements View.OnClickListener{
    //private ImageView img_avatar;
    private com.android.volley.toolbox.NetworkImageView iv_showavatar;
    private Button btn_sendmessage;
    private TextView tb_title,tb_back,tb_action,tv_name,tv_showdistrict,tv_friendsestimate;
    private OrderDetails orderDetails;
    private UserDetail userDetail;
    private Nearby nearby;
    private String from, UserID;
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
        iv_showavatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_showavatar);
        iv_showavatar.setOnClickListener(this);
        tv_showdistrict = (TextView)findViewById(R.id.tv_showdistrict);
        Drawable drawable= getResources().getDrawable(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tb_action.setCompoundDrawables(drawable, null, null, null);
        tb_action.setOnClickListener(this);
        tv_friendsestimate =(TextView)findViewById(R.id.tv_friendsestimate);
        tv_friendsestimate.setOnClickListener(this);
        Intent intent = this.getIntent();
        orderDetails = (OrderDetails)intent.getSerializableExtra("OrderDetails");
        if(orderDetails != null) {
            tv_name.setText(orderDetails.getOrderer().getName());
            ImageUtils.setImageUrl(iv_showavatar, orderDetails.getOrderer().getAvatar());
            tv_showdistrict.setText(orderDetails.getOrderer().getLocation());
            UserID = String.valueOf(orderDetails.getOrderer().getId());
        }
        userDetail =  (UserDetail)intent.getSerializableExtra("UserDetail");
        if(userDetail != null) {
            tv_name.setText(userDetail.getName());
            ImageUtils.setImageUrl(iv_showavatar, userDetail.getAvatar());
            tv_showdistrict.setText(userDetail.getLocation());
            UserID = String.valueOf(userDetail.getId());
        }
        nearby =  (Nearby)intent.getSerializableExtra("NearBy");
        if(nearby != null) {
            tv_name.setText(nearby.getName());
            ImageUtils.setImageUrl(iv_showavatar, nearby.getAvatar());
            tv_showdistrict.setText(nearby.getLocation());
            UserID = String.valueOf(nearby.getId());
        }
        if(intent.getStringExtra("From") != null) {
            from = intent.getStringExtra("From").toString();
        }
        if(intent.getStringExtra("UserID") != null) {
            String myUserID = intent.getStringExtra("UserID").toString();
            Http.request(FriendsInfoActivity.this, API.USER_PROFILE, new Object[]{myUserID}, new Http.RequestListener<UserDetail>(){
                @Override
                public void onSuccess(UserDetail result) {
                    super.onSuccess(result);

                    tv_name.setText(result.getName());
                    ImageUtils.setImageUrl(iv_showavatar, result.getAvatar());
                    tv_showdistrict.setText(result.getLocation());
                    UserID = String.valueOf(result.getId());
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_sendmessage:

                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = UserID;// 消息接收者ID
                Intent intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_action:
                new PopupWindows(FriendsInfoActivity.this,tb_action);
                break;
            case R.id.tv_friendsestimate:
                Intent myIntent = new Intent();
                myIntent.setClass(FriendsInfoActivity.this,EstimateActivity.class);
                myIntent.putExtra("UserID",UserID);
                startActivity(myIntent);
                break;
            case R.id.iv_showavatar:
                Intent mIntent = new Intent();
                mIntent.setClass(this, AvatarZoomActivity.class);
                String[] ImageURL = iv_showavatar.getImageURL().substring(AppConfig.QINIU_HOST.length()).split("\\?");
                mIntent.putExtra("Avatar", ImageURL[0]);
                startActivity(mIntent);
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
            showAsDropDown(parent, 0, 0);
            update();


            LinearLayout lly_whocannotsee = (LinearLayout) view.findViewById(R.id.lly_whocannotsee);
            LinearLayout lly_cannotsee = (LinearLayout) view.findViewById(R.id.lly_cannotsee);
            LinearLayout lly_blacklist = (LinearLayout) view.findViewById(R.id.lly_blacklist);
            LinearLayout lly_whocannotseecancle = (LinearLayout) view.findViewById(R.id.lly_whocannotseecancle);
            LinearLayout lly_cannotseecancle = (LinearLayout) view.findViewById(R.id.lly_cannotseecancle);
            LinearLayout lly_removeblacklist = (LinearLayout) view.findViewById(R.id.lly_removeblacklist);

            if(from != null && !from.equals("") && from.equals("FIRSTCLASS"))
            {
                lly_whocannotsee.setVisibility(View.VISIBLE);
                lly_cannotsee.setVisibility(View.VISIBLE);
                lly_blacklist.setVisibility(View.VISIBLE);
                lly_whocannotseecancle.setVisibility(View.GONE);
                lly_cannotseecancle.setVisibility(View.GONE);
                lly_removeblacklist.setVisibility(View.GONE);
            }
            else if(from != null && !from.equals("") &&from.equals("WHOCANNOTSEE"))
            {
                lly_whocannotseecancle.setVisibility(View.VISIBLE);
                lly_whocannotsee.setVisibility(View.GONE);
                lly_cannotsee.setVisibility(View.GONE);
                lly_blacklist.setVisibility(View.GONE);
                lly_cannotseecancle.setVisibility(View.GONE);
                lly_removeblacklist.setVisibility(View.GONE);
            }
            else if(from != null && !from.equals("") &&from.equals("CANNOTSEE"))
            {
                lly_whocannotseecancle.setVisibility(View.GONE);
                lly_whocannotsee.setVisibility(View.GONE);
                lly_cannotsee.setVisibility(View.GONE);
                lly_blacklist.setVisibility(View.GONE);
                lly_cannotseecancle.setVisibility(View.VISIBLE);
                lly_removeblacklist.setVisibility(View.GONE);
            }
            else if(from != null && !from.equals("") &&from.equals("BLACKLIST"))
            {
                lly_whocannotseecancle.setVisibility(View.GONE);
                lly_whocannotsee.setVisibility(View.GONE);
                lly_cannotsee.setVisibility(View.GONE);
                lly_blacklist.setVisibility(View.GONE);
                lly_cannotseecancle.setVisibility(View.GONE);
                lly_removeblacklist.setVisibility(View.VISIBLE);
            }
            else
            {

            }
            Button addoneclassfriend = (Button) view
                    .findViewById(R.id.item_popupwindows_addoneclassfriend);
            Button viewrelationnet = (Button) view
                    .findViewById(R.id.item_popupwindows_viewrelationnet);
            Button addblacklist = (Button) view
                    .findViewById(R.id.item_popupwindows_addblacklist);
            Button btn_report = (Button) view
                .findViewById(R.id.item_popupwindows_report);
            Button btn_whocannotsee = (Button) view
                    .findViewById(R.id.item_popupwindows_whocannotsee);
            Button btn_cannotsee = (Button) view
                    .findViewById(R.id.item_popupwindows_cannotsee);
            Button btn_whocannotseecancle = (Button) view
                    .findViewById(R.id.item_popupwindows_whocannotseecancle);
            Button btn_cannotseecancle = (Button) view
                    .findViewById(R.id.item_popupwindows_cannotseecancle);
            Button btn_removeblacklist = (Button) view
                    .findViewById(R.id.item_popupwindows_removeblacklist);
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
                    Http.request(FriendsInfoActivity.this, API.ADD_BLACKLIST, new Object[]{Auth.getCurrentUserId()},
                            Http.map("BlockIds", String.valueOf(userDetail.getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    //Toast.makeText(FriendsInfoActivity.this, "已屏蔽该好友", Toast.LENGTH_SHORT).show();
                                }
                            });
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
            btn_cannotsee.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(FriendsInfoActivity.this, API.POST_NOTSEEB,
                            Http.map("userBId",String.valueOf(userDetail.getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    //Toast.makeText(FriendsInfoActivity.this, "已屏蔽该好友", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                }
            });
            btn_whocannotsee.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(FriendsInfoActivity.this, API.POST_NOTLETBSEE,
                            Http.map("userBId",String.valueOf(userDetail.getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    //Toast.makeText(FriendsInfoActivity.this, "已屏蔽该好友", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                }
            });
            btn_whocannotseecancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Http.request(FriendsInfoActivity.this, API.POST_NOTLETBSEECANCLE,
                            Http.map("userBId",String.valueOf(userDetail.getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    //Toast.makeText(FriendsInfoActivity.this, "已屏蔽该好友", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                }
            });
            btn_cannotseecancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Http.request(FriendsInfoActivity.this, API.POST_NOTSEEBCANCLE,
                            Http.map("userBId",String.valueOf(userDetail.getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    //Toast.makeText(FriendsInfoActivity.this, "已屏蔽该好友", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                }
            });
            btn_removeblacklist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("BlockIds", String.valueOf(userDetail.getId()));
                    Http.request(FriendsInfoActivity.this, API.REMOVE_BLACKLIST, new Object[]{Auth.getCurrentUserId(),Http.getURL(map)},

                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    //Toast.makeText(FriendsInfoActivity.this, "已屏蔽该好友", Toast.LENGTH_SHORT).show();
                                }
                            });
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
