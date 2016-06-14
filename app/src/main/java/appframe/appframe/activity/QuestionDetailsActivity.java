package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AnswerDetail;
import appframe.appframe.dto.AnswerDetailWithQuestionDetail;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.QuestionWithAnswers;
import appframe.appframe.dto.SelfEvaluationDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXAnswerAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderComment;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXQuestionAdapater;

/**
 * Created by Administrator on 2016/5/18.
 */
public class QuestionDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_back,tb_title,tb_action,tv_name,tv_title,tv_content,tv_money,tv_comment,btn_comment,tv_acceptedname,tv_acceptcontent,tv_acceptedtime;
    private ImageView imgbtn_conversation,imgbtn_call;
    private com.android.volley.toolbox.NetworkImageView iv_avatar,iv_acceptedavatar;
    private RatingBar rb_totalvalue;
    private RelativeLayout rl_accepted;
    private ListView lv_ordercomment;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    Question question;
    String Tel;
    boolean hasAccept = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questiondetails);
        init();
    }

    private void init()
    {
        iv_avatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_avatar);
        iv_acceptedavatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_acceptedavatar);
        imgbtn_conversation = (ImageView)findViewById(R.id.imgbtn_conversation);
        imgbtn_call = (ImageView)findViewById(R.id.imgbtn_call);
        rb_totalvalue = (RatingBar)findViewById(R.id.rb_totalvalue);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_content = (TextView)findViewById(R.id.tv_content);
        tv_money = (TextView)findViewById(R.id.tv_money);
        tv_comment = (TextView)findViewById(R.id.tv_comment);
        btn_comment = (TextView)findViewById(R.id.btn_comment);
        lv_ordercomment = (ListView)findViewById(R.id.lv_ordercomment);
        tv_acceptedname = (TextView)findViewById(R.id.tv_acceptedname);
        tv_acceptcontent = (TextView)findViewById(R.id.tv_acceptcontent);
        tv_acceptedtime = (TextView)findViewById(R.id.tv_acceptedtime);
        rl_accepted = (RelativeLayout)findViewById(R.id.rl_accepted);

        tb_back.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
        imgbtn_conversation.setOnClickListener(this);
        imgbtn_call.setOnClickListener(this);
        btn_comment.setOnClickListener(this);
        rl_accepted.setOnClickListener(this);

        tb_action.setVisibility(View.GONE);
        tb_title.setText("问答");
        tb_back.setText("友帮");

        lv_ordercomment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(QuestionDetailsActivity.this, AnswerDetailsActivity.class);
                AnswerDetail answerDetails = (AnswerDetail) parent.getAdapter().getItem(position);
                bundle.putSerializable("AnswerDetail", answerDetails);
                bundle.putString("QuestionID", String.valueOf(question.getId()));
                bundle.putBoolean("hasAccept",hasAccept);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        Intent intent = this.getIntent();
        if(intent.getSerializableExtra("Question") != null)
        {
            question = (Question) intent.getSerializableExtra("Question");

        }
        if(intent.getSerializableExtra("MyAnswer") != null)
        {
            question = (Question) intent.getSerializableExtra("MyAnswer");

        }
        if(intent.getSerializableExtra("QuestionMessage") != null)
        {
            question = (Question) intent.getSerializableExtra("QuestionMessage");

        }


        if(question.getAcceptedAnswer() != null)
        {
            hasAccept = true;
            rl_accepted.setVisibility(View.VISIBLE);
            tv_acceptedname.setText(question.getAcceptedAnswer().getAnswerer().getName());
            tv_acceptcontent.setText(question.getAcceptedAnswer().getContent());
            tv_acceptedtime.setText(question.getAcceptedAnswer().getUpdatedAt());
            if(question.getAcceptedAnswer().getAnswerer().getAvatar() != null && !question.getAcceptedAnswer().getAnswerer().getAvatar().equals(""))
            {
                ImageUtils.setImageUrl(iv_acceptedavatar, question.getAcceptedAnswer().getAnswerer().getAvatar());
            }
            else
            {
                if(question.getAcceptedAnswer().getAnswerer().getGender().equals(getResources().getString(R.string.male)))
                {
                    iv_acceptedavatar.setDefaultImageResId(R.drawable.maleavatar);
                }
                else
                {
                    iv_acceptedavatar.setDefaultImageResId(R.drawable.femaleavatar);
                }

            }
        }

        if(question.getAsker().getId() == Auth.getCurrentUserId())
        {
            btn_comment.setVisibility(View.GONE);
        }

        if(question.getAsker().getAvatar() != null && !question.getAsker().getAvatar().equals(""))
        {
            ImageUtils.setImageUrl(iv_avatar, question.getAsker().getAvatar());
        }
        else
        {
            if(question.getAsker().getGender().equals(getResources().getString(R.string.male)))
            {
                iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
            }
            else
            {
                iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
            }

        }

        Tel = question.getAsker().getMobile() == null ? "" : question.getAsker().getMobile().toString();

        tv_name.setText(question.getAsker().getName().toString());
        tv_title.setText(question.getTitle().toString());
        tv_content.setText(question.getContent().toString());
        tv_money.setText(String.valueOf(question.getBounty()));


    }

    @Override
    protected void onResume() {
        super.onResume();

        Http.request(QuestionDetailsActivity.this, API.GET_ANSWERS, new Object[]{question.getId()},
                new Http.RequestListener<QuestionWithAnswers>() {
                    @Override
                    public void onSuccess(QuestionWithAnswers result) {
                        super.onSuccess(result);
                        if (result != null) {
                            lv_ordercomment.setAdapter(new SwipeRefreshXAnswerAdapater(QuestionDetailsActivity.this, result.getAnswerDetails()));

                            setListViewHeightBasedOnChildren(lv_ordercomment);
                        }
                        if (question.getAcceptedAnswer() != null) {
                            tv_comment.setText(String.format("(%d条）回答", result != null ? result.getAnswerDetails().size() + 1 : 1));
                        } else {
                            tv_comment.setText(String.format("(%d条）回答", result != null ? result.getAnswerDetails().size() : 0));
                        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                intent.setClass(QuestionDetailsActivity.this, FriendsInfoActivity.class);
                bundle.putSerializable("QuestionDetails", question);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.imgbtn_conversation:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = String.valueOf(question.getAsker().getId());
                intent = ls.getIMKit().getChattingActivityIntent(target);
                startActivity(intent);
                break;
            case R.id.imgbtn_call:
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Tel)); //直接拨打电话android.intent.action.CALL
                startActivity(phoneIntent);
//                else
//                {
//                    Toast.makeText(OrderDetailsActivity.this,"该用户不是用手机注册",Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.btn_comment:
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
                            Http.request(QuestionDetailsActivity.this, API.MAKE_ANSWERS, new Object[]{question.getId()}, Http.map(
                                            "Content", comment.getText().toString()),

                                    new Http.RequestListener<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            super.onSuccess(result);
                                            btn_comment.setVisibility(View.GONE);
//                                            Http.request(QuestionDetailsActivity.this, API.GET_ANSWERS, new Object[]{question.getId()},
//                                                    new Http.RequestListener<QuestionWithAnswers>() {
//                                                        @Override
//                                                        public void onSuccess(QuestionWithAnswers result) {
//                                                            super.onSuccess(result);
//
//                                                            lv_ordercomment.setAdapter(new SwipeRefreshXAnswerAdapater(QuestionDetailsActivity.this, result.getAnswerDetails()));
//
//                                                            setListViewHeightBasedOnChildren(lv_ordercomment);
//                                                            if(question.getAcceptedAnswer() != null) {
//                                                                tv_comment.setText(String.format("(%d条）回答", result != null ? result.getAnswerDetails().size() + 1 : 1));
//                                                            }
//                                                            else
//                                                            {
//                                                                tv_comment.setText(String.format("(%d条）回答", result != null ? result.getAnswerDetails().size() : 0));
//                                                            }
//
//                                                        }
//
//                                                        @Override
//                                                        public void onFail(String code) {
//                                                            super.onFail(code);
//                                                            Toast.makeText(QuestionDetailsActivity.this, code, Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });

                                        }

                                        @Override
                                        public void onFail(String code) {
                                            super.onFail(code);
                                            Toast.makeText(QuestionDetailsActivity.this, code, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(QuestionDetailsActivity.this, "答案不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.rl_accepted:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(QuestionDetailsActivity.this, AnswerDetailsActivity.class);
                AnswerDetail answerDetails = question.getAcceptedAnswer();
                bundle.putSerializable("AnswerDetail", answerDetails);
                bundle.putString("QuestionID", String.valueOf(question.getId()));
                bundle.putBoolean("hasAccept",hasAccept);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tb_back:
                finish();
                break;
        }
    }
}
