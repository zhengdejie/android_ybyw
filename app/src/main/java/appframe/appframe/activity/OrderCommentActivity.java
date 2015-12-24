package appframe.appframe.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.tagview.Tag;
import appframe.appframe.widget.tagview.TagView;

/**
 * Created by Administrator on 2015/9/8.
 */
public class OrderCommentActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_addtag,tb_back,tb_title;
    private EditText edit_tag,et_content;
    private TagView tagView;
    private Button btn_evaluate;
    private RatingBar rb_service,rb_attitude,rb_personality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordercomment);
        init();
    }
    protected void init()
    {
        rb_attitude = (RatingBar)findViewById(R.id.rb_attitude);
        tv_addtag = (TextView)findViewById(R.id.tv_addtag);
        edit_tag = (EditText)findViewById(R.id.edit_tag);
        tagView = (TagView)findViewById(R.id.tagview);
        btn_evaluate = (Button)findViewById(R.id.btn_evaluate);
        et_content = (EditText)findViewById(R.id.et_content);
        rb_service = (RatingBar)findViewById(R.id.rb_service);
        rb_attitude = (RatingBar)findViewById(R.id.rb_attitude);
        rb_personality = (RatingBar)findViewById(R.id.rb_personality);
        tv_addtag.setOnClickListener(this);
        btn_evaluate.setOnClickListener(this);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title.setText("评价");
        tb_back.setText("我的发单");
        tb_back.setOnClickListener(this);
//        rb_attitude.getRating(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_addtag:
                if (edit_tag.getText().toString()!=null&&!edit_tag.getText().toString().equals("")) {
                    String tagTitle= edit_tag.getText().toString();
                    Tag tag = new Tag(tagTitle);
                    tag.isDeletable=true;
                    tagView.addTag(tag);

                }
                break;
            case R.id.btn_evaluate:
//                Http.request(this, API.EVALUATION_ORDER, Http.map(
//                        "OrderId", String.valueOf(11),
//                        "Content", et_content.getText().toString(),
//                        "Commentator", String.valueOf(Auth.getCurrentUserId()),
//                        "ServicePoint", String.valueOf(rb_service.getRating()),
//                        "AttitudePoint", String.valueOf(rb_attitude.getRating()),
//                        "CharacterPoint", String.valueOf(rb_personality.getRating()),
//                        "Tags", "11"
//                        ), new Http.RequestListener<UserDetail>() {
//                    @Override
//                    public void onSuccess(UserDetail result) {
//                        super.onSuccess(result);
//                        //Toast.makeText(OrderCommentActivity.this,"评论成功",Toast.LENGTH_SHORT);
//                        startActivity(new Intent(OrderCommentActivity.this,HomeActivity.class));
//                    }
//                });
                Http.request(this, API.EVALUATION_ORDER, Http.map(
                        "ConfirmedOrderId", getIntent().getStringExtra("ConfirmedOrderId"),
                        "Content", et_content.getText().toString(),
                        "Commentator", String.valueOf(Auth.getCurrentUserId()),
                        "ServicePoint", String.valueOf((int)(rb_service.getRating())),
                        "AttitudePoint", String.valueOf((int)(rb_attitude.getRating())),
                        "CharacterPoint", String.valueOf((int)(rb_personality.getRating())),
                        "Tags", "11"
                ), new Http.RequestListener<UserDetail>() {
                    @Override
                    public void onSuccess(UserDetail result) {
                        super.onSuccess(result);

                    }
                });
                break;
            case R.id.tb_back:
                finish();
                break;

        }
    }
}
