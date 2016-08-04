package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private TextView tv_myname,tv_yourname,tb_title,tb_back,tv_relation;
    private com.android.volley.toolbox.NetworkImageView iv_myavatar,iv_youravatar;
    private GridView gridview;
    private UserBrief userBrief;


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
        tv_relation = (TextView) findViewById(R.id.tv_relation);
        gridview =(GridView)findViewById(R.id.gridview);
        iv_myavatar = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_myavatar);
        iv_youravatar = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_youravatar);
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        tb_title.setText("我和TA的关系网");
        tb_back.setText("返回");
        tb_back.setOnClickListener(this);

    }

    protected void initdata()
    {
        Intent getIntent = this.getIntent();
        userBrief = (UserBrief)getIntent.getSerializableExtra("userBrief");
        tv_myname.setText(Auth.getCurrentUser().getName());
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
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("FriendId", String.valueOf(userBrief.getId()));
        Http.request(RelativenetActivity.this, API.GET_RELATIONSHIP, Http.map("FriendId",String.valueOf(userBrief.getId())),

                new Http.RequestListener<Friendship>() {
                    @Override
                    public void onSuccess(Friendship result) {
                        super.onSuccess(result);
                        if( result.getType() == 1 )
                        {
                            tv_relation.setText("您和TA的关系为一度朋友");
                        }
                        else if( result.getType() == 2 )
                        {
                            tv_relation.setText("您和TA的关系为二度朋友");
                        }
                        else
                        {
                            tv_relation.setText("您和TA的关系为陌生人");
                        }

                        gridview.setAdapter(new RelativenetGridViewAdapater(RelativenetActivity.this, result.getMidman()));
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

        }
    }




}
