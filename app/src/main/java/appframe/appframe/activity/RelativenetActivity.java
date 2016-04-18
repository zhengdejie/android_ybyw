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
        tb_back.setText("需求单");
        tb_back.setOnClickListener(this);

    }

    protected void initdata()
    {
        tv_myname.setText(Auth.getCurrentUser().getName());
        if(Auth.getCurrentUser().getAvatar() != null && !Auth.getCurrentUser().getAvatar().equals("")) {
            ImageUtils.setImageUrl(iv_myavatar, Auth.getCurrentUser().getAvatar());
        }
        tv_yourname.setText(getIntent().getStringExtra("Name"));
        if(getIntent().getStringExtra("Avatar") !=null && !getIntent().getStringExtra("Avatar").equals("")) {
            ImageUtils.setImageUrl(iv_youravatar, getIntent().getStringExtra("Avatar"));
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("FriendId", getIntent().getStringExtra("UserID"));
        Http.request(RelativenetActivity.this, API.GET_RELATIONSHIP, Http.map("FriendId",getIntent().getStringExtra("UserID").toString()),

                new Http.RequestListener<Friendship>() {
                    @Override
                    public void onSuccess(Friendship result) {
                        super.onSuccess(result);
                        if( result.getType() == 1 )
                        {
                            tv_relation.setText("一度朋友");
                        }
                        else if( result.getType() == 2 )
                        {
                            tv_relation.setText("二度朋友");
                        }
                        else
                        {
                            tv_relation.setText("陌生人");
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
