package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AnswerDetail;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.QuestionWithAnswers;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXAnswerAdapater;

/**
 * Created by Administrator on 2016/5/19.
 */
public class AnswerDetailsActivity extends BaseActivity implements View.OnClickListener{

    private TextView tb_back,tb_title,tb_action,tv_name,tv_title,tv_accept;
    private ImageView imgbtn_conversation,imgbtn_call;
    private com.android.volley.toolbox.NetworkImageView iv_avatar;
    private RatingBar rb_totalvalue;
//    private String QuestionID;
//    boolean hasAccept = false;

    Intent intent = new Intent();
    AnswerDetail answerDetail;
    Question question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answerdetail);
        init();
    }

    private void init()
    {
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_accept = (TextView)findViewById(R.id.tv_accept);
        imgbtn_conversation = (ImageView)findViewById(R.id.imgbtn_conversation);
        imgbtn_call = (ImageView)findViewById(R.id.imgbtn_call);
        rb_totalvalue = (RatingBar)findViewById(R.id.rb_totalvalue);
        iv_avatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_avatar);

        tb_back.setOnClickListener(this);
        imgbtn_conversation.setOnClickListener(this);
        imgbtn_call.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
        tv_accept.setOnClickListener(this);

        tb_action.setVisibility(View.GONE);
        tb_back.setText("问答");
        tb_title.setText("答案");

        Intent intent = this.getIntent();
        answerDetail = (AnswerDetail)intent.getSerializableExtra("AnswerDetail");
        question = (Question)intent.getSerializableExtra("Question");
//        QuestionID = intent.getStringExtra("QuestionID");
//        hasAccept = intent.getBooleanExtra("hasAccept",false);


        if(question.getAcceptedAnswer() != null || question.getAsker().getId() != Auth.getCurrentUserId())
        {
            tv_accept.setVisibility(View.GONE);
        }

        tv_name.setText(answerDetail.getAnswerer().getName().toString());
        tv_title.setText(answerDetail.getContent().toString());

        if(answerDetail.getAnswerer().getId() == Auth.getCurrentUserId())
        {
            tv_accept.setVisibility(View.VISIBLE);
            tv_accept.setText("修改答案");
        }

        if(answerDetail.getAnswerer().getAvatar() != null && !answerDetail.getAnswerer().getAvatar().equals(""))
        {
            ImageUtils.setImageUrl(iv_avatar, answerDetail.getAnswerer().getAvatar());
        }
        else
        {
            if(answerDetail.getAnswerer().getGender().equals(getResources().getString(R.string.male)))
            {
                iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
            }
            else
            {
                iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_back:
                finish();
                break;
            case R.id.imgbtn_conversation:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = String.valueOf(answerDetail.getAnswerer().getId());
                intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.imgbtn_call:
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + answerDetail.getAnswerer().getMobile() == null ? "" : answerDetail.getAnswerer().getMobile().toString())); //直接拨打电话android.intent.action.CALL
                startActivity(phoneIntent);
                break;
            case R.id.tv_accept:
                if(tv_accept.getText().equals("修改答案"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //final EditText comment = new EditText(this);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_dispute, (ViewGroup) findViewById(R.id.dialog));
                    final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                    builder.setTitle("添加答案").setView(
                            layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!comment.getText().toString().equals("")) {
                                Http.request(AnswerDetailsActivity.this, API.UPDATE_MYANSWER, new Object[]{question.getId()}, Http.map(
                                                "Content", comment.getText().toString()),

                                        new Http.RequestListener<AnswerDetail>() {
                                            @Override
                                            public void onSuccess(AnswerDetail result) {
                                                super.onSuccess(result);
                                                Toast.makeText(AnswerDetailsActivity.this, "修改答案成功", Toast.LENGTH_SHORT).show();
                                                tv_title.setText(result.getContent().toString());
                                            }

                                            @Override
                                            public void onFail(String code) {
                                                super.onFail(code);
                                                Toast.makeText(AnswerDetailsActivity.this, code, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(AnswerDetailsActivity.this, "答案不能为空", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
                else {
                    Http.request(AnswerDetailsActivity.this, API.ACCEPT_ANSWERS, new Object[]{question.getId(), answerDetail.getId()},

                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    Toast.makeText(AnswerDetailsActivity.this, "您已接受他的答案", Toast.LENGTH_SHORT).show();
                                    tv_accept.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);
                                    Toast.makeText(AnswerDetailsActivity.this, code, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;
        }
    }
}
