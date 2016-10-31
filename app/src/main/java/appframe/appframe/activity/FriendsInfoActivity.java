package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.PushMessage;
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
    private TextView tb_title,tb_back,tb_action,tv_name,tv_showdistrict,tv_friendsestimate,tv_comment,tv_nickname,tv_revenue,tv_cost,btn_sendmessage,tv_questionhistory,tv_buyservice,tv_sellservice,tv_fbnum,tv_openservice,tv_openhelpthem,tv_focus,tv_openquestion;
    private ImageView iv_member,iv_gender;
    private RelativeLayout rl_revenue,rl_cost;
    private OrderDetails orderDetails;
    private UserDetail userDetail,candidate;
    private UserBrief userBrief,userID;
    private Question question,questionDetails;
    private Nearby nearby;
    private PushMessage pushMessage;
    private String from,isfocus;
    private Intent activityIntent = new Intent();
    private final  int RESULT_CODE =1;
    private View view_divide5;
    private LinearLayout progress_bar;
//    TextView addoneclassfriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendsinfo);
        init();
    }
    protected void init()
    {
        btn_sendmessage = (TextView)findViewById(R.id.btn_sendmessage);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);
        btn_sendmessage.setOnClickListener(this);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_title.setText("帮友资料");
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("返回");
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
        tv_fbnum = (TextView)findViewById(R.id.tv_fbnum);
        iv_member = (ImageView)findViewById(R.id.iv_member);
        iv_gender = (ImageView)findViewById(R.id.iv_gender);
        tv_openservice = (TextView)findViewById(R.id.tv_openservice);
        tv_openhelpthem = (TextView)findViewById(R.id.tv_openhelpthem);
        tv_focus = (TextView)findViewById(R.id.tv_focus);
        view_divide5 = (View)findViewById(R.id.view_divide5);
        tv_openquestion = (TextView)findViewById(R.id.tv_openquestion);
        tv_comment.setOnClickListener(this);
        tv_questionhistory.setOnClickListener(this);
        tv_buyservice.setOnClickListener(this);
        tv_sellservice.setOnClickListener(this);
        tv_openservice.setOnClickListener(this);
        tv_openhelpthem.setOnClickListener(this);
        tv_focus.setOnClickListener(this);
        tv_openquestion.setOnClickListener(this);

        progress_bar.setVisibility(View.VISIBLE);
        Intent intent = this.getIntent();
        orderDetails = (OrderDetails)intent.getSerializableExtra("OrderDetails");
        if(orderDetails != null) {
            if(orderDetails.getNameAnonymity() == 1)
            {
                tv_name.setText("匿名");
                if(orderDetails.getOrderer().getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);
                }
                else
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
                tv_nickname.setVisibility(View.INVISIBLE);
                tv_fbnum.setText("友帮号: ");
                iv_member.setVisibility(View.GONE);
            }
            else {
                if (orderDetails.getOrderer().getFNickName() != null && !orderDetails.getOrderer().getFNickName().equals("")) {
                    tv_name.setText(orderDetails.getOrderer().getFNickName());
                    tv_nickname.setText("昵称: " + orderDetails.getOrderer().getName());
                    tv_nickname.setVisibility(View.VISIBLE);
                } else {
                    tv_name.setText(orderDetails.getOrderer().getName());
                }
                if (orderDetails.getOrderer().getYBAccount() != null && !orderDetails.getOrderer().getYBAccount().equals("")) {
                    tv_fbnum.setText("友帮号: " + orderDetails.getOrderer().getYBAccount());
                }
                else
                {
                    tv_fbnum.setText("友帮号: " );
                }
                if (orderDetails.getOrderer().getAvatar() != null) {
                    ImageUtils.setImageUrl(iv_showavatar, orderDetails.getOrderer().getAvatar());
                }
                else
                {
                    if(orderDetails.getOrderer().getGender().equals(getResources().getString(R.string.male)))
                    {
                        iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                        iv_gender.setImageResource(R.drawable.male);
                    }
                    else
                    {
                        iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                        iv_gender.setImageResource(R.drawable.female);
                    }
                }
            }

            if(orderDetails.getOrderer().isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText("￥" + String.valueOf(orderDetails.getOrderer().getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(orderDetails.getOrderer().isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText("￥" + String.valueOf(orderDetails.getOrderer().getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            tv_showdistrict.setText(orderDetails.getOrderer().getLocation());
            userBrief = orderDetails.getOrderer();

            if(orderDetails.getOrderer().getMember() == 1)
            {
                iv_member.setImageResource(R.drawable.idflag);
            }
            else if(orderDetails.getOrderer().getMember() == 2)
            {
                iv_member.setImageResource(R.drawable.shopflag);
            }
            else
            {
                iv_member.setVisibility(View.GONE);
            }
            progress_bar.setVisibility(View.GONE);
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
            if (userDetail.getYBAccount() != null && !userDetail.getYBAccount().equals("")) {
                tv_fbnum.setText("友帮号: " + userDetail.getYBAccount());
            }
            else
            {
                tv_fbnum.setText("友帮号: " );
            }
            if(userDetail.getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, userDetail.getAvatar());
            }
            else
            {
                if(userDetail.getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);
                }
                else
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
            }
            if(userDetail.isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText("￥" + String.valueOf(userDetail.getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(userDetail.isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText("￥" + String.valueOf(userDetail.getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            if(userDetail.getMember() == 1)
            {
                iv_member.setImageResource(R.drawable.idflag);
            }
            else if(userDetail.getMember() == 2)
            {
                iv_member.setImageResource(R.drawable.shopflag);
            }
            else
            {
                iv_member.setVisibility(View.GONE);
            }
            tv_showdistrict.setText(userDetail.getLocation());
//            UserID = String.valueOf(userDetail.getId());
            userBrief = userDetail;
            progress_bar.setVisibility(View.GONE);
        }
        nearby =  (Nearby)intent.getSerializableExtra("NearBy");
        if(nearby != null) {
            tv_name.setText(nearby.getName());
            if(nearby.getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, nearby.getAvatar());
            }
            else
            {
                if(nearby.getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);
                }
                else
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
            }
            if (nearby.getYBAccount() != null && !nearby.getYBAccount().equals("")) {
                tv_fbnum.setText("友帮号: " + nearby.getYBAccount());
            }
            else
            {
                tv_fbnum.setText("友帮号: " );
            }

            tv_showdistrict.setText(nearby.getLocation());
//            UserID = String.valueOf(nearby.getId());
            userBrief = nearby;
            if(nearby.isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText("￥" + String.valueOf(nearby.getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);

            }
            if(nearby.isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText("￥" + String.valueOf(nearby.getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            if(nearby.getMember() == 1)
            {
                iv_member.setImageResource(R.drawable.idflag);
            }
            else if(nearby.getMember() == 2)
            {
                iv_member.setImageResource(R.drawable.shopflag);
            }
            else
            {
                iv_member.setVisibility(View.GONE);
            }
            progress_bar.setVisibility(View.GONE);
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
            if (candidate.getYBAccount() != null && !candidate.getYBAccount().equals("")) {
                tv_fbnum.setText("友帮号: " + candidate.getYBAccount());
            }
            else
            {
                tv_fbnum.setText("友帮号: " );
            }

            if(candidate.getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, candidate.getAvatar());
            }
            else
            {
                if(candidate.getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);
                }
                else
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
            }
            tv_showdistrict.setText(candidate.getLocation());
            userBrief = candidate;
            if(candidate.isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText("￥" + String.valueOf(candidate.getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(candidate.isShowExpense())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText("￥" + String.valueOf(candidate.getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            if(candidate.getMember() == 1)
            {
                iv_member.setImageResource(R.drawable.idflag);
            }
            else if(candidate.getMember() == 2)
            {
                iv_member.setImageResource(R.drawable.shopflag);
            }
            else
            {
                iv_member.setVisibility(View.GONE);
            }
            progress_bar.setVisibility(View.GONE);
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
            if (question.getAsker().getYBAccount() != null && !question.getAsker().getYBAccount().equals("")) {
                tv_fbnum.setText("友帮号: " + question.getAsker().getYBAccount());
            }
            else
            {
                tv_fbnum.setText("友帮号: " );
            }
            if(question.getAsker().getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, question.getAsker().getAvatar());
            }
            else
            {
                if(question.getAsker().getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);
                }
                else
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
            }
            tv_showdistrict.setText(question.getAsker().getLocation());
            userBrief = question.getAsker();
            if(question.getAsker().isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText("￥" + String.valueOf(question.getAsker().getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(question.getAsker().isShowRevenue())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText("￥" + String.valueOf(question.getAsker().getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            if(question.getAsker().getMember() == 1)
            {
                iv_member.setImageResource(R.drawable.idflag);
            }
            else if(question.getAsker().getMember() == 2)
            {
                iv_member.setImageResource(R.drawable.shopflag);
            }
            else
            {
                iv_member.setVisibility(View.GONE);
            }
            progress_bar.setVisibility(View.GONE);
        }
        questionDetails = (Question)intent.getSerializableExtra("QuestionDetails");
        if(questionDetails != null) {

            if(questionDetails.getAsker().getFNickName() != null && !questionDetails.getAsker().getFNickName().equals(""))
            {
                tv_name.setText(questionDetails.getAsker().getFNickName());
                tv_nickname.setText("昵称:"+questionDetails.getAsker().getName());
                tv_nickname.setVisibility(View.VISIBLE);
            }
            else {
                tv_name.setText(questionDetails.getAsker().getName());
            }
            if (questionDetails.getAsker().getYBAccount() != null && !questionDetails.getAsker().getYBAccount().equals("")) {
                tv_fbnum.setText("友帮号: " + questionDetails.getAsker().getYBAccount());
            }
            else
            {
                tv_fbnum.setText("友帮号: " );
            }
            if(questionDetails.getAsker().getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, questionDetails.getAsker().getAvatar());
            }
            else
            {
                if(questionDetails.getAsker().getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);
                }
                else
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
            }
            tv_showdistrict.setText(questionDetails.getAsker().getLocation());
            userBrief = questionDetails.getAsker();
            if(questionDetails.getAsker().isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText("￥" + String.valueOf(questionDetails.getAsker().getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(questionDetails.getAsker().isShowRevenue())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText("￥" + String.valueOf(questionDetails.getAsker().getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            if(questionDetails.getAsker().getMember() == 1)
            {
                iv_member.setImageResource(R.drawable.idflag);
            }
            else if(questionDetails.getAsker().getMember() == 2)
            {
                iv_member.setImageResource(R.drawable.shopflag);
            }
            else
            {
                iv_member.setVisibility(View.GONE);
            }
            progress_bar.setVisibility(View.GONE);
        }
        pushMessage = (PushMessage)intent.getSerializableExtra("PushMessage");
        if(pushMessage != null)
        {
            if(pushMessage.getSender().getFNickName() != null && !pushMessage.getSender().getFNickName().equals(""))
            {
                tv_name.setText(pushMessage.getSender().getFNickName());
                tv_nickname.setText("昵称:"+pushMessage.getSender().getName());
                tv_nickname.setVisibility(View.VISIBLE);
            }
            else {
                tv_name.setText(pushMessage.getSender().getName());
            }
            if (pushMessage.getSender().getYBAccount() != null && !pushMessage.getSender().getYBAccount().equals("")) {
                tv_fbnum.setText("友帮号: " + pushMessage.getSender().getYBAccount());
            }
            else
            {
                tv_fbnum.setText("友帮号: " );
            }
            if(pushMessage.getSender().getAvatar()!=null) {
                ImageUtils.setImageUrl(iv_showavatar, pushMessage.getSender().getAvatar());
            }
            else
            {
                if(pushMessage.getSender().getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);
                }
                else
                {
                    iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
            }
            tv_showdistrict.setText(pushMessage.getSender().getLocation());
            userBrief = pushMessage.getSender();
            if(pushMessage.getSender().isShowRevenue())
            {
                rl_revenue.setVisibility(View.VISIBLE);
                tv_revenue.setText("￥" + String.valueOf(pushMessage.getSender().getTotalRevenue()));
            }
            else
            {
                rl_revenue.setVisibility(View.GONE);
            }
            if(pushMessage.getSender().isShowRevenue())
            {
                rl_cost.setVisibility(View.VISIBLE);
                tv_cost.setText("￥" + String.valueOf(pushMessage.getSender().getTotalExpense()));
            }
            else
            {
                rl_cost.setVisibility(View.GONE);
            }
            if(pushMessage.getSender().getMember() == 1)
            {
                iv_member.setImageResource(R.drawable.idflag);
            }
            else if(pushMessage.getSender().getMember() == 2)
            {
                iv_member.setImageResource(R.drawable.shopflag);
            }
            else
            {
                iv_member.setVisibility(View.GONE);
            }
            progress_bar.setVisibility(View.GONE);
        }

        if(intent.getStringExtra("From") != null) {
            from = intent.getStringExtra("From").toString();
        }

        //scan
//        Bundle bundle = intent.getExtras();
//        String string = bundle.getString("name");
        userID = (UserBrief)intent.getSerializableExtra("UserID");
        if(userID != null) {
//            String myUserID = intent.getStringExtra("UserID").toString();
            Http.request(FriendsInfoActivity.this, API.USER_PROFILE, new Object[]{String.valueOf(userID.getId())}, new Http.RequestListener<UserDetail>(){
                @Override
                public void onSuccess(UserDetail result) {
                    super.onSuccess(result);

                    tv_name.setText(result.getName());
                    if(result.getAvatar()!=null) {
                        ImageUtils.setImageUrl(iv_showavatar, result.getAvatar());
                    }
                    else
                    {
                        if(result.getGender().equals(getResources().getString(R.string.male))) {
                            iv_showavatar.setDefaultImageResId(R.drawable.maleavatar);
                        }
                        else
                        {
                            iv_showavatar.setDefaultImageResId(R.drawable.femaleavatar);
                        }
                    }
                    tv_showdistrict.setText(result.getLocation());
//                    UserID = String.valueOf(result.getId());
                    userBrief = result;

                    if (userBrief.isShowExpense() || userBrief.isShowRevenue()) {
                        view_divide5.setVisibility(View.VISIBLE);
                    } else {
                        view_divide5.setVisibility(View.GONE);
                    }

                    if(userBrief.isShowRevenue())
                    {
                        rl_revenue.setVisibility(View.VISIBLE);
                        tv_revenue.setText("￥" + String.valueOf(pushMessage.getSender().getTotalRevenue()));
                    }
                    else
                    {
                        rl_revenue.setVisibility(View.GONE);
                    }
                    if(userBrief.isShowRevenue())
                    {
                        rl_cost.setVisibility(View.VISIBLE);
                        tv_cost.setText("￥" + String.valueOf(pushMessage.getSender().getTotalExpense()));
                    }
                    else
                    {
                        rl_cost.setVisibility(View.GONE);
                    }
                    progress_bar.setVisibility(View.GONE);
                }

                @Override
                public void onFail(String code) {
                    super.onFail(code);
                    progress_bar.setVisibility(View.GONE);
                }
            });
        }
        else {
            if (userBrief.isFans()) {
                tv_focus.setText("取消关注");
            } else {
                tv_focus.setText("+关注");
            }

            if (userBrief.isShowExpense() || userBrief.isShowRevenue()) {
                view_divide5.setVisibility(View.VISIBLE);
            } else {
                view_divide5.setVisibility(View.GONE);
            }
            progress_bar.setVisibility(View.GONE);
        }
//        if(intent.getBooleanExtra("AddFriend",false))
        Bundle bundle = intent.getExtras();
        if(bundle.getBoolean("AddFriend"))
        {
            tv_focus.setText("添加好友");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_sendmessage:

                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = String.valueOf(userBrief.getId());// 消息接收者ID
                activityIntent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(activityIntent);
                break;
            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_action:
                new PopupWindows(FriendsInfoActivity.this,tb_action);
                break;
            case R.id.tv_friendsestimate:

                activityIntent.setClass(FriendsInfoActivity.this, EstimateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userBrief", userBrief);
                activityIntent.putExtras(bundle);
//                intent.putExtra("UserID",UserID);
                startActivity(activityIntent);
                break;
            case R.id.iv_showavatar:

                activityIntent.setClass(this, AvatarZoomActivity.class);
                if(iv_showavatar.getImageURL() != null && !iv_showavatar.getImageURL().equals("")) {
                    String[] ImageURL = iv_showavatar.getImageURL().substring(AppConfig.QINIU_HOST.length()).split("\\?");
                    activityIntent.putExtra("Avatar", ImageURL[0]);
                    startActivity(activityIntent);
                }
//                else
//                {
//                    intent.putExtra("Avatar", "");
//                }

                break;
            case R.id.tv_comment:
                activityIntent.setClass(this,EditFriendsNickNameActivity.class);
                activityIntent.putExtra("UserID",String.valueOf(userBrief.getId()));
                if(tv_nickname.getVisibility() == View.VISIBLE) {
                    String[] Name = tv_nickname.getText().toString().split(":");
                    activityIntent.putExtra("Name",Name[1]);
                    activityIntent.putExtra("FNickName", tv_name.getText().toString());
                }
                else
                {
                    activityIntent.putExtra("Name", tv_name.getText().toString());

                }
                startActivityForResult(activityIntent, RESULT_CODE);

                break;
            case R.id.tv_questionhistory:
                activityIntent.setClass(this, MyQuestionActivity.class);
                Bundle bundleQuestion = new Bundle();
                bundleQuestion.putSerializable("friendsinfo", userBrief);
                bundleQuestion.putString("questionstatus", "2");
                activityIntent.putExtras(bundleQuestion);
                startActivity(activityIntent);
                break;
            case R.id.tv_buyservice:
                activityIntent.setClass(this,TradeBuyHistoryActivity.class);
                activityIntent.putExtra("buyservice", "2");
                activityIntent.putExtra("name", userBrief.getName());
                activityIntent.putExtra("userID", String.valueOf(userBrief.getId()));
                startActivity(activityIntent);
                break;
            case R.id.tv_sellservice:
                activityIntent.setClass(this,TradeSellHistoryActivity.class);
                activityIntent.putExtra("sellservice", "1");
                activityIntent.putExtra("name", userBrief.getName());
                activityIntent.putExtra("userID", String.valueOf(userBrief.getId()));
                startActivity(activityIntent);
                break;

            case R.id.tv_openservice:
                activityIntent.setClass(this, OpenOrderActivity.class);
                activityIntent.putExtra("name", userBrief.getName());
                activityIntent.putExtra("userID", String.valueOf(userBrief.getId()));
                activityIntent.putExtra("type", "2");
                startActivity(activityIntent);
                break;

            case R.id.tv_openhelpthem:
                activityIntent.setClass(this, OpenOrderActivity.class);
                activityIntent.putExtra("name", userBrief.getName());
                activityIntent.putExtra("userID", String.valueOf(userBrief.getId()));
                activityIntent.putExtra("type", "1");
                startActivity(activityIntent);
                break;
            case R.id.tv_focus:


                if(tv_focus.getText().equals("+关注")) {
                        Http.request(FriendsInfoActivity.this, API.ADDFANS,
                                Http.map("Fans", String.valueOf(Auth.getCurrentUserId()),
                                        "Celebrity", String.valueOf(userBrief.getId())),
                                new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);
                                        Toast.makeText(FriendsInfoActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                        tv_focus.setText("取消关注");
                                    }
                                });
                    }
                else if(tv_focus.getText().equals("取消关注"))
                {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Id", String.valueOf(userBrief.getId()));
                    Http.request(FriendsInfoActivity.this, API.DELETEFANS,new Object[]{Http.getURL(map)},
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    Toast.makeText(FriendsInfoActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
                                    tv_focus.setText("+关注");
                                }
                            });
                }
                else if(tv_focus.getText().equals("添加好友"))
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
                }
                else
                {

                }
                break;
            case R.id.tv_openquestion:
                activityIntent.setClass(this, MyQuestionActivity.class);
                Bundle bundleques = new Bundle();
                bundleques.putSerializable("friendsinfo", userBrief);
                bundleques.putString("questionstatus", "1");
                activityIntent.putExtras(bundleques);
                startActivity(activityIntent);
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

//            TextView addoneclassfriend = (TextView) view
//                    .findViewById(R.id.item_popupwindows_addoneclassfriend);
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

//            addoneclassfriend.setText(isfocus);

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

//            addoneclassfriend.setOnClickListener(new View.OnClickListener()
//            {
//                public void onClick(View v)
//                {
////                    Http.request(FriendsInfoActivity.this, API.ADD_FDF, new Object[]{Auth.getCurrentUserId()},
////                            Http.map("FriendId",String.valueOf(userBrief.getId())),
////                            new Http.RequestListener<String>() {
////                                @Override
////                                public void onSuccess(String result) {
////                                    super.onSuccess(result);
////                                    Toast.makeText(FriendsInfoActivity.this, "已发送好友申请", Toast.LENGTH_SHORT).show();
////                                }
////                            });
//                    if(addoneclassfriend.getText().equals("+关注")) {
//                        Http.request(FriendsInfoActivity.this, API.ADDFANS,
//                                Http.map("Fans", String.valueOf(Auth.getCurrentUserId()),
//                                        "Celebrity", String.valueOf(userBrief.getId())),
//                                new Http.RequestListener<String>() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        super.onSuccess(result);
//                                        Toast.makeText(FriendsInfoActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
//                                        addoneclassfriend.setText("取消关注");
//                                    }
//                                });
//                    }
//                    else if(addoneclassfriend.getText().equals("取消关注"))
//                    {
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("Id", String.valueOf(userBrief.getId()));
//                        Http.request(FriendsInfoActivity.this, API.DELETEFANS,new Object[]{Http.getURL(map)},
//                                new Http.RequestListener<String>() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        super.onSuccess(result);
//                                        Toast.makeText(FriendsInfoActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
//                                        addoneclassfriend.setText("+关注");
//                                    }
//                                });
//                    }
//                    dismiss();
//                }
//            });
            viewrelationnet.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(Auth.getCurrentUserId() == userBrief.getId())
                    {
                        Toast.makeText(FriendsInfoActivity.this,"不能查看自己与自己的关系",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent();
                        intent.setClass(FriendsInfoActivity.this, RelativenetActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userBrief", userBrief);
                        intent.putExtras(bundle);
//                    intent.putExtra("UserID", String.valueOf(userBrief.getId()));
//                    intent.putExtra("Avatar", iv_showavatar.getImageURL());
//                    intent.putExtra("Name", tv_name.getText());
                        startActivity(intent);

                    }
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
                                    Toast.makeText(FriendsInfoActivity.this, "拉黑成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                }
            });

            btn_report.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent();
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
                                    Toast.makeText(FriendsInfoActivity.this, "屏蔽成功", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(FriendsInfoActivity.this, "屏蔽成功", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(FriendsInfoActivity.this, "取消屏蔽", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(FriendsInfoActivity.this, "取消屏蔽", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(FriendsInfoActivity.this, "取消拉黑", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dismiss();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("帮友资料页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("帮友资料页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = this.getMenuInflater();
//        inflater.inflate(R.menu.menu_finfo, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
}
