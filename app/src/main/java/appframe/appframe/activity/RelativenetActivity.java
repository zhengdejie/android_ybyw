package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.Friendship;
import appframe.appframe.dto.UserBrief;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.widget.swiperefresh.RelativenetGridViewAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendShopsAdapater;

/**
 * Created by Administrator on 2015/10/14.
 */
public class RelativenetActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_myname,tv_yourname,tb_title,tb_back,tv_relation,tv_hername,tv_onerelation,tv_tworelation,v_center;
    private com.android.volley.toolbox.NetworkImageView iv_myavatar,iv_youravatar,iv_heravatar;
    private ImageView iv_oneclass,iv_twoclass,iv_questionmark,iv_zuojianju,iv_youjianju,iv_backgroud;
    private UserBrief userBrief;
    private appframe.appframe.utils.SlashView slashview;
    private List<UserDetail> middleMan;
    private int currentMiddle = 0;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    private int relationShip = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relativenet);
        init();
        initdata();
    }

    protected void init() {
        tv_myname = (TextView) findViewById(R.id.tv_myname);
        tv_yourname = (TextView) findViewById(R.id.tv_yourname);
        tv_hername = (TextView) findViewById(R.id.tv_hername);
        v_center = (TextView) findViewById(R.id.v_center);
        tv_onerelation = (TextView) findViewById(R.id.tv_onerelation);
        tv_tworelation = (TextView) findViewById(R.id.tv_tworelation);
        iv_oneclass = (ImageView)findViewById(R.id.iv_oneclass);
        iv_twoclass = (ImageView)findViewById(R.id.iv_twoclass);
        iv_backgroud = (ImageView)findViewById(R.id.iv_backgroud);
        iv_questionmark = (ImageView)findViewById(R.id.iv_questionmark);
        iv_zuojianju = (ImageView)findViewById(R.id.iv_zuojianju);
        iv_youjianju = (ImageView)findViewById(R.id.iv_youjianju);
        slashview = (appframe.appframe.utils.SlashView)findViewById(R.id.slashview);
        iv_myavatar = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_myavatar);
        iv_youravatar = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_youravatar);
        iv_heravatar = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_heravatar);
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tb_title.setText("我和TA的关系网");
        tb_back.setText("返回");
        tb_back.setOnClickListener(this);
        iv_youjianju.setOnClickListener(this);
        iv_zuojianju.setOnClickListener(this);
        iv_myavatar.setOnClickListener(this);
        iv_youravatar.setOnClickListener(this);
        iv_heravatar.setOnClickListener(this);
    }

    protected void initdata()
    {
        Intent getIntent = this.getIntent();
        userBrief = (UserBrief)getIntent.getSerializableExtra("userBrief");
        if(Auth.getCurrentUser().getAvatar() != null && !Auth.getCurrentUser().getAvatar().equals("")) {
            ImageUtils.setImageUrl(iv_myavatar, Auth.getCurrentUser().getAvatar());
        }
        else
        {
            if(Auth.getCurrentUser().getGender().equals(getResources().getString(R.string.male).toString()))
            {
                iv_myavatar.setDefaultImageResId(R.drawable.maleavatar);
            }
            else
            {
                iv_myavatar.setDefaultImageResId(R.drawable.femaleavatar);
            }
        }


//        Map<String, String> map = new HashMap<String, String>();
//        map.put("FriendId", String.valueOf(userBrief.getId()));
        Http.request(RelativenetActivity.this, API.GET_RELATIONSHIP, Http.map("FriendId", String.valueOf(userBrief.getId())),

                new Http.RequestListener<Friendship>() {
                    @Override
                    public void onSuccess(Friendship result) {
                        super.onSuccess(result);
                        if (result.getType() == 1) {
                            relationShip = 1;
                            tv_yourname.setText(userBrief.getName());
                            if(userBrief.getAvatar() !=null && !userBrief.getAvatar().equals("")) {
                                ImageUtils.setImageUrl(iv_youravatar, userBrief.getAvatar());
                            }
                            else
                            {
                                if(userBrief.getGender().equals(getResources().getString(R.string.male).toString()))
                                {
                                    iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                                }
                                else
                                {
                                    iv_youravatar.setDefaultImageResId(R.drawable.femaleavatar);
                                }
                            }
                            v_center.setVisibility(View.VISIBLE);
                            iv_oneclass.setVisibility(View.VISIBLE);
                            tv_onerelation.setText("您和Ta是一度好友");
                            tv_onerelation.setVisibility(View.VISIBLE);

                        }
                        else if (result.getType() == 2) {
                            relationShip = 2;
                            middleMan = result.getMidman();
                            if(middleMan.size() > 1 )
                            {
                                iv_youjianju.setVisibility(View.VISIBLE);
                                iv_zuojianju.setVisibility(View.VISIBLE);
                            }
                            tv_yourname.setText(middleMan.get(0).getName());
                            //没注册的用户
                            if(middleMan.get(0).getId() == 0 )
                            {
                                iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                            }
                            //已注册的用户
                            else {
                                if (middleMan.get(0).getAvatar() != null && !middleMan.get(0).getAvatar().equals("")) {
                                    ImageUtils.setImageUrl(iv_youravatar, middleMan.get(0).getAvatar());
                                } else {

                                    if (middleMan.get(0).getGender().equals(getResources().getString(R.string.male).toString())) {
                                        iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                                    } else {
                                        iv_youravatar.setDefaultImageResId(R.drawable.femaleavatar);
                                    }
                                }
                            }

                            tv_hername.setVisibility(View.VISIBLE);
                            iv_heravatar.setVisibility(View.VISIBLE);
                            tv_hername.setText(userBrief.getName());
                            if(userBrief.getAvatar() !=null && !userBrief.getAvatar().equals("")) {
                                ImageUtils.setImageUrl(iv_heravatar, userBrief.getAvatar());
                            }
                            else
                            {
                                if(userBrief.getGender().equals(getResources().getString(R.string.male).toString()))
                                {
                                    iv_heravatar.setDefaultImageResId(R.drawable.maleavatar);
                                }
                                else
                                {
                                    iv_heravatar.setDefaultImageResId(R.drawable.femaleavatar);
                                }
                            }
                            iv_backgroud.setVisibility(View.VISIBLE);
                            v_center.setVisibility(View.VISIBLE);
                            slashview.setVisibility(View.VISIBLE);
                            iv_twoclass.setVisibility(View.VISIBLE);
                            tv_tworelation.setText(String.format("您和Ta是二度关系,你们的中间人是%s",middleMan.get(0).getName()));
                            tv_tworelation.setVisibility(View.VISIBLE);
                        }
                        else {
                            relationShip = 3;
                            tv_yourname.setText(userBrief.getName());
                            if(userBrief.getAvatar() !=null && !userBrief.getAvatar().equals("")) {
                                ImageUtils.setImageUrl(iv_youravatar, userBrief.getAvatar());
                            }
                            else
                            {
                                if(userBrief.getGender().equals(getResources().getString(R.string.male).toString()))
                                {
                                    iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                                }
                                else
                                {
                                    iv_youravatar.setDefaultImageResId(R.drawable.femaleavatar);
                                }
                            }
                            iv_questionmark.setVisibility(View.VISIBLE);
                            tv_onerelation.setText("您和Ta是陌生人");
                            tv_onerelation.setVisibility(View.VISIBLE);

                        }

//                        gridview.setAdapter(new RelativenetGridViewAdapater(RelativenetActivity.this, result.getMidman()));
                    }
                });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.iv_youjianju:
                currentMiddle = ((currentMiddle + 1) > (middleMan.size() - 1 )) ? 0 : (currentMiddle + 1);

                tv_yourname.setText(middleMan.get(currentMiddle).getName());
                //未注册
                if(middleMan.get(currentMiddle).getId() == 0)
                {
                    iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                }
                //已注册
                else {
                    if (middleMan.get(currentMiddle).getAvatar() != null && !middleMan.get(currentMiddle).getAvatar().equals("")) {
                        ImageUtils.setImageUrl(iv_youravatar, middleMan.get(currentMiddle).getAvatar());
                    } else {
                        if (middleMan.get(currentMiddle).getGender().equals(getResources().getString(R.string.male).toString())) {
                            iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                        } else {
                            iv_youravatar.setDefaultImageResId(R.drawable.femaleavatar);
                        }
                    }
                }
                tv_tworelation.setText(String.format("您和Ta是二度关系,你们的中间人是%s",middleMan.get(currentMiddle).getName()));
                break;
            case R.id.iv_zuojianju:
                currentMiddle = ((currentMiddle - 1) < 0 ) ? middleMan.size() - 1 : (currentMiddle - 1);

                tv_yourname.setText(middleMan.get(currentMiddle).getName());
                //未注册
                if(middleMan.get(currentMiddle).getId() == 0)
                {
                    iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                }
                //已注册
                else {
                    if (middleMan.get(currentMiddle).getAvatar() != null && !middleMan.get(currentMiddle).getAvatar().equals("")) {
                        ImageUtils.setImageUrl(iv_youravatar, middleMan.get(currentMiddle).getAvatar());
                    } else {
                        if (middleMan.get(currentMiddle).getGender().equals(getResources().getString(R.string.male).toString())) {
                            iv_youravatar.setDefaultImageResId(R.drawable.maleavatar);
                        } else {
                            iv_youravatar.setDefaultImageResId(R.drawable.femaleavatar);
                        }
                    }
                }
                tv_tworelation.setText(String.format("您和Ta是二度关系,你们的中间人是%s",middleMan.get(currentMiddle).getName()));
                break;
            case R.id.iv_myavatar:
                intent.setClass(RelativenetActivity.this, FriendsInfoActivity.class);
                UserBrief ub = new UserBrief();
                ub.setId(Auth.getCurrentUserId());
                bundle.clear();
                bundle.putSerializable("UserID", ub);
                intent.replaceExtras(bundle);
//                intent.putExtra("UserID", String.valueOf(Auth.getCurrentUserId()));
                startActivity(intent);

                break;
            case R.id.iv_youravatar:
                if(relationShip == 1 || relationShip == 3)
                {
                    intent.setClass(RelativenetActivity.this, FriendsInfoActivity.class);
                    bundle.clear();
                    bundle.putSerializable("Relativenet", userBrief);
                    intent.replaceExtras(bundle);
                    startActivity(intent);
                }
                else if (relationShip ==2 )
                {
                    if(middleMan.get(currentMiddle).getId() == 0 )
                    {
                        Toast.makeText(this,"该用户还未注册,快去邀请他吧",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        intent.setClass(RelativenetActivity.this, FriendsInfoActivity.class);
                        bundle.clear();
                        bundle.putSerializable("UserDetail", middleMan.get(currentMiddle));
                        intent.replaceExtras(bundle);
                        startActivity(intent);
                    }
                }
                else
                {

                }
                break;
            case R.id.iv_heravatar:
                intent.setClass(RelativenetActivity.this, FriendsInfoActivity.class);
                bundle.clear();
                bundle.putSerializable("Relativenet", userBrief);
                intent.replaceExtras(bundle);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关系网页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关系网页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }



}
