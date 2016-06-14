package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.UserBrief;
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
    private TextView tb_title,tb_back,tb_action,tv_name,tv_showdistrict,tv_friendsestimate,tv_comment,tv_nickname,tv_revenue,tv_cost,btn_sendmessage,tv_questionhistory,tv_buyservice,tv_sellservice;
    private RelativeLayout rl_revenue,rl_cost;
    private OrderDetails orderDetails;
    private UserDetail userDetail,candidate;
    private UserBrief userBrief;
    private Question question;
    private Nearby nearby;
    private String from;
    private Intent intent = new Intent();
    private final  int RESULT_CODE =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsinfo);
        init();
    }
    protected void init()
    {
        btn_sendmessage = (TextView)findViewById(R.id.btn_sendmessage);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);
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
        Drawable drawable= getResources().getDrawable(R.drawable.moreoverflow_normal_holo_light);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tb_action.setCompoundDrawables(null, null, drawable, null);
        tb_action.setOnClickListener(this);
        tv_friendsestimate =(TextView)findViewById(R.id.tv_friendsestimate);
        tv_friendsestimate.setOnClickListener(this);
        tv_comment = (TextView)findViewById(R.id.tv_comment);
        tv_revenue = (TextView)findViewById(R.id.tv_revenue);
        tv_cost = (TextView)findViewById(R.id.tv_cost);
        rl_revenue = (RelativeLayout)findViewById(R.id.rl_revenue);
        rl_cost = (RelativeLayout)findViewById(R.id.rl_cost);
        tv_questionhistory = (TextView)findViewById(R.id.tv_questionhistory);
        tv_buyservice = (TextView)findViewById(R.id.tv_buyservice);
        tv_sellservice = (TextView)findViewById(R.id.tv_sellservice);
        tv_comment.setOnClickListener(this);
        tv_questionhistory.setOnClickListener(this);
        tv_buyservice.setOnClickListener(this);
        tv_sellservice.setOnClickListener(this);

        Intent intent = this.getIntent();
        orderDetails = (OrderDetails)intent.getSerializableExtra("OrderDetails");
        if(orderDetails != null) {
            if(orderDetails.getNameAnonymity() == 1)
            {
                tv_name.setText("匿名");
                iv_showavatar.setDefaultImageResId(R.drawable.default_avatar);
                tv_nickname.setVisibility(View.INVISIBLE);
            }
            else {
                if (orderDetails.getOrderer().getFNickName() != null && !orderDetails.getOrderer().getFNickName().equals("")) {
                    tv_name.setText(orderDetails.getOrderer().getFNickName());
                    tv_nickname.setText("昵称:" + orderDetails.getOrderer().getName());
                    tv_nickname.setVisibility(View.VISIBLE);
                } else {
                    tv_name.setText(orderDetails.getOrderer().getName());
                }
                if (orderDetails.getOrderer().getAvatar() != null) {
                    ImageUtils.setImageUrl(iv_showavatar, orderDetails.getOrderer().getAvatar());
                }
            }
            if(orderDetails.getOrderer().isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText(String.valueOf(orderDetails.getOrderer().getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(orderDetails.getOrderer().isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText(String.valueOf(orderDetails.getOrderer().getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            tv_showdistrict.setText(orderDetails.getOrderer().getLocation());
            userBrief = orderDetails.getOrderer();
//            UserID = String.valueOf(orderDetails.getOrderer().getId());
//            AvgServicePoint = String.valueOf(orderDetails.getOrderer().getAvgServicePoint());
//            AvgAttitudePoint = String.valueOf(orderDetails.getOrderer().getAvgAttitudePoint());
//            AvgCharacterPoint = String.valueOf(orderDetails.getOrderer().getAvgCharacterPoint());
//            TotalNumberOfOrder = String.valueOf(orderDetails.getOrderer().getTotalNumberOfOrder());
        }
        userDetail =  (UserDetail)intent.getSerializableExtra("UserDetail");
        if(userDetail != null) {

            if(userDetail.getFNickName() != null && !userDetail.getFNickName().equals(""))
            {
                tv_name.setText(userDetail.getFNickName());
                tv_nickname.setText("昵称:"+userDetail.getName());
                tv_nickname.setVisibility(View.VISIBLE);
            }
            else {
                tv_name.setText(userDetail.getName());
            }

            if(userDetail.getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, userDetail.getAvatar());
            }

            if(userDetail.isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText(String.valueOf(userDetail.getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(userDetail.isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText(String.valueOf(userDetail.getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            tv_showdistrict.setText(userDetail.getLocation());
//            UserID = String.valueOf(userDetail.getId());
            userBrief = userDetail;
        }
        nearby =  (Nearby)intent.getSerializableExtra("NearBy");
        if(nearby != null) {
            tv_name.setText(nearby.getName());
            if(nearby.getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, nearby.getAvatar());
            }
            tv_showdistrict.setText(nearby.getLocation());
//            UserID = String.valueOf(nearby.getId());
            userBrief = nearby;
            if(nearby.isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText(String.valueOf(nearby.getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);

            }
            if(nearby.isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText(String.valueOf(nearby.getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
        }
        candidate = (UserDetail)intent.getSerializableExtra("Candidate");
        if(candidate != null) {

            if(candidate.getFNickName() != null && !candidate.getFNickName().equals(""))
            {
                tv_name.setText(candidate.getFNickName());
                tv_nickname.setText("昵称:"+candidate.getName());
                tv_nickname.setVisibility(View.VISIBLE);
            }
            else {
                tv_name.setText(candidate.getName());
            }

            if(candidate.getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, candidate.getAvatar());
            }
            tv_showdistrict.setText(candidate.getLocation());
            userBrief = candidate;
            if(candidate.isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText(String.valueOf(candidate.getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(candidate.isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText(String.valueOf(candidate.getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
        }
        question = (Question)intent.getSerializableExtra("Question");
        if(question != null) {

            if(question.getAsker().getFNickName() != null && !question.getAsker().getFNickName().equals(""))
            {
                tv_name.setText(question.getAsker().getFNickName());
                tv_nickname.setText("昵称:"+question.getAsker().getName());
                tv_nickname.setVisibility(View.VISIBLE);
            }
            else {
                tv_name.setText(question.getAsker().getName());
            }

            if(question.getAsker().getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, question.getAsker().getAvatar());
            }
            tv_showdistrict.setText(question.getAsker().getLocation());
            userBrief = question.getAsker();
            if(question.getAsker().isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText(String.valueOf(question.getAsker().getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(question.getAsker().isShowRevenue())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText(String.valueOf(question.getAsker().getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
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
                    if(result.getAvatar()!=null) {
                        ImageUtils.setImageUrl(iv_showavatar, result.getAvatar());
                    }
                    tv_showdistrict.setText(result.getLocation());
//                    UserID = String.valueOf(result.getId());
                    userBrief = result;
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
                String target = String.valueOf(userBrief.getId());// 消息接收者ID
                intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_action:
                new PopupWindows(FriendsInfoActivity.this,tb_action);
                break;
            case R.id.tv_friendsestimate:

                intent.setClass(FriendsInfoActivity.this, EstimateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userBrief", userBrief);
                intent.putExtras(bundle);
//                intent.putExtra("UserID",UserID);
                startActivity(intent);
                break;
            case R.id.iv_showavatar:

                intent.setClass(this, AvatarZoomActivity.class);
                if(iv_showavatar.getImageURL() != null && !iv_showavatar.getImageURL().equals("")) {
                    String[] ImageURL = iv_showavatar.getImageURL().substring(AppConfig.QINIU_HOST.length()).split("\\?");
                    intent.putExtra("Avatar", ImageURL[0]);
                    startActivity(intent);
                }
//                else
//                {
//                    intent.putExtra("Avatar", "");
//                }

                break;
            case R.id.tv_comment:
                intent.setClass(this,EditFriendsNickNameActivity.class);
                intent.putExtra("UserID",String.valueOf(userBrief.getId()));
                if(tv_nickname.getVisibility() == View.VISIBLE) {
                    String[] Name = tv_nickname.getText().toString().split(":");
                    intent.putExtra("Name",Name[1]);
                    intent.putExtra("FNickName", tv_name.getText().toString());
                }
                else
                {
                    intent.putExtra("Name", tv_name.getText().toString());

                }
                startActivityForResult(intent, RESULT_CODE);

                break;
            case R.id.tv_questionhistory:
                startActivity(intent.setClass(this, MyQuestionActivity.class));
                break;
            case R.id.tv_buyservice:
                intent.setClass(this,TradeHistoryActivity.class);
                intent.putExtra("buyservice", "2");
                startActivity(intent);
                break;
            case R.id.tv_sellservice:
                intent.setClass(this,TradeHistoryActivity.class);
                intent.putExtra("sellservice", "1");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RESULT_CODE:
                switch (resultCode)
                {
                    case RESULT_OK:
                        if(data.getStringExtra("NickName").equals(""))
                        {
                            tv_name.setText(data.getStringExtra("Name"));
                            tv_nickname.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            tv_name.setText(data.getStringExtra("NickName"));
                            tv_nickname.setText("昵称:" + data.getStringExtra("Name"));
                            tv_nickname.setVisibility(View.VISIBLE);
                        }

                    break;
                }
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


            RelativeLayout rl_whocannotsee = (RelativeLayout) view.findViewById(R.id.rl_whocannotsee);
            RelativeLayout rl_cannotsee = (RelativeLayout) view.findViewById(R.id.rl_cannotsee);
            RelativeLayout rl_blacklist = (RelativeLayout) view.findViewById(R.id.rl_blacklist);
            RelativeLayout rl_cannotseecancle = (RelativeLayout) view.findViewById(R.id.rl_cannotseecancle);
            RelativeLayout rl_whocannotseecancle = (RelativeLayout) view.findViewById(R.id.rl_whocannotseecancle);
            RelativeLayout rl_removeblacklist = (RelativeLayout) view.findViewById(R.id.rl_removeblacklist);

            TextView addoneclassfriend = (TextView) view
                    .findViewById(R.id.item_popupwindows_addoneclassfriend);
            TextView viewrelationnet = (TextView) view
                    .findViewById(R.id.item_popupwindows_viewrelationnet);
            TextView addblacklist = (TextView) view
                    .findViewById(R.id.item_popupwindows_addblacklist);
            TextView btn_report = (TextView) view
                    .findViewById(R.id.item_popupwindows_report);
            TextView btn_whocannotsee = (TextView) view
                    .findViewById(R.id.item_popupwindows_whocannotsee);
            TextView btn_cannotsee = (TextView) view
                    .findViewById(R.id.item_popupwindows_cannotsee);
            TextView btn_whocannotseecancle = (TextView) view
                    .findViewById(R.id.item_popupwindows_whocannotseecancle);
            TextView btn_cannotseecancle = (TextView) view
                    .findViewById(R.id.item_popupwindows_cannotseecancle);
            TextView btn_removeblacklist = (TextView) view
                    .findViewById(R.id.item_popupwindows_removeblacklist);
            View view_report = (View)view.findViewById(R.id.view_popupwindows_report);
            View view_addblacklist = (View)view.findViewById(R.id.view_popupwindows_addblacklist);
            View view_whocannotsee = (View)view.findViewById(R.id.view_popupwindows_whocannotsee);
            View view_whocannotseecancle = (View)view.findViewById(R.id.view_popupwindows_whocannotseecancle);
            View view_cannotsee = (View)view.findViewById(R.id.view_popupwindows_cannotsee);
            View view_cannotseecancle = (View)view.findViewById(R.id.view_popupwindows_cannotseecancle);
            View view_removeblacklist = (View)view.findViewById(R.id.view_popupwindows_removeblacklist);

            if(from != null && !from.equals("") && from.equals("FIRSTCLASS"))
            {
                rl_whocannotsee.setVisibility(View.VISIBLE);
                view_whocannotsee.setVisibility(View.VISIBLE);
                rl_cannotsee.setVisibility(View.VISIBLE);
                view_cannotsee.setVisibility(View.VISIBLE);
                rl_blacklist.setVisibility(View.VISIBLE);
                view_addblacklist.setVisibility(View.VISIBLE);
                rl_whocannotseecancle.setVisibility(View.GONE);
                view_whocannotseecancle.setVisibility(View.GONE);
                rl_cannotseecancle.setVisibility(View.GONE);
                view_cannotseecancle.setVisibility(View.GONE);
                rl_removeblacklist.setVisibility(View.GONE);
                view_removeblacklist.setVisibility(View.GONE);
                view_report.setVisibility(View.VISIBLE);
            }
            else if(from != null && !from.equals("") &&from.equals("WHOCANNOTSEE"))
            {
                rl_whocannotseecancle.setVisibility(View.VISIBLE);
                view_whocannotseecancle.setVisibility(View.VISIBLE);
                rl_whocannotsee.setVisibility(View.GONE);
                view_whocannotsee.setVisibility(View.GONE);
                rl_cannotsee.setVisibility(View.GONE);
                view_cannotsee.setVisibility(View.GONE);
                rl_blacklist.setVisibility(View.GONE);
                view_addblacklist.setVisibility(View.GONE);
                rl_cannotseecancle.setVisibility(View.GONE);
                view_cannotseecancle.setVisibility(View.GONE);
                rl_removeblacklist.setVisibility(View.GONE);
                view_removeblacklist.setVisibility(View.GONE);
                view_report.setVisibility(View.VISIBLE);
            }
            else if(from != null && !from.equals("") &&from.equals("CANNOTSEE"))
            {
                rl_whocannotseecancle.setVisibility(View.GONE);
                rl_whocannotsee.setVisibility(View.GONE);
                rl_cannotsee.setVisibility(View.GONE);
                rl_blacklist.setVisibility(View.GONE);
                rl_cannotseecancle.setVisibility(View.VISIBLE);
                rl_removeblacklist.setVisibility(View.GONE);
                view_whocannotseecancle.setVisibility(View.GONE);
                view_whocannotsee.setVisibility(View.GONE);
                view_cannotsee.setVisibility(View.GONE);
                view_addblacklist.setVisibility(View.GONE);
                view_cannotseecancle.setVisibility(View.VISIBLE);
                view_removeblacklist.setVisibility(View.GONE);
                view_report.setVisibility(View.VISIBLE);
            }
            else if(from != null && !from.equals("") &&from.equals("BLACKLIST"))
            {
                rl_whocannotseecancle.setVisibility(View.GONE);
                rl_whocannotsee.setVisibility(View.GONE);
                rl_cannotsee.setVisibility(View.GONE);
                rl_blacklist.setVisibility(View.GONE);
                rl_cannotseecancle.setVisibility(View.GONE);
                rl_removeblacklist.setVisibility(View.VISIBLE);
                view_whocannotseecancle.setVisibility(View.GONE);
                view_whocannotsee.setVisibility(View.GONE);
                view_cannotsee.setVisibility(View.GONE);
                view_addblacklist.setVisibility(View.GONE);
                view_cannotseecancle.setVisibility(View.GONE);
                view_removeblacklist.setVisibility(View.VISIBLE);
                view_report.setVisibility(View.VISIBLE);
            }
            else
            {

            }

            addoneclassfriend.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(FriendsInfoActivity.this, API.ADD_FDF, new Object[]{Auth.getCurrentUserId()},
                            Http.map("FriendId",String.valueOf(userBrief.getId())),
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
                    Intent intent = new Intent();
                    intent.setClass(FriendsInfoActivity.this,RelativenetActivity.class);
                    intent.putExtra("UserID", String.valueOf(userBrief.getId()));
                    intent.putExtra("Avatar", iv_showavatar.getImageURL());
                    intent.putExtra("Name", tv_name.getText());
                    startActivity(intent);
                    dismiss();
                }
            });
            addblacklist.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(FriendsInfoActivity.this, API.ADD_BLACKLIST, new Object[]{Auth.getCurrentUserId()},
                            Http.map("BlockIds", String.valueOf(userBrief.getId())),
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
                    Bundle bundle = new Bundle();
                    intent.setClass(FriendsInfoActivity.this, ReportActivity.class);
                    bundle.putSerializable("FriendsInfo", userBrief);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dismiss();
                }
            });
            btn_cannotsee.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Http.request(FriendsInfoActivity.this, API.POST_NOTSEEB,
                            Http.map("userBId",String.valueOf(userBrief.getId())),
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
                            Http.map("userBId",String.valueOf(userBrief.getId())),
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
                            Http.map("userBId",String.valueOf(userBrief.getId())),
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
                            Http.map("userBId",String.valueOf(userBrief.getId())),
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
