package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.AvatarZoomActivity;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.QuestionDetailsActivity;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2016/5/18.
 */
public class SwipeRefreshXQuestionAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Question> questionList = new ArrayList<Question>();
    //    TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name;
//    com.android.volley.toolbox.NetworkImageView iv_avatar;
    //Button btn_estimate;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    String from;
    int orderType;
    //    LinearLayout ll_receiver,ll_button;
    boolean hasTopOrder;

    public SwipeRefreshXQuestionAdapater(Context context, List<Question> questionList)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.questionList = questionList;
        this.from = from;
    }

    @Override
    public int getCount() {
        if(questionList == null)
        {
            return 0;
        }
        return questionList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_question, null);
            mHolder = new ViewHolder();
            mHolder.txt_title = (TextView)convertView.findViewById(R.id.txt_title);
            mHolder.txt_bounty = (TextView)convertView.findViewById(R.id.txt_bounty);
            mHolder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
            mHolder.tv_answerunm = (TextView)convertView.findViewById(R.id.tv_answerunm);

//            mHolder.txt_tag = (TextView)convertView.findViewById(R.id.txt_tag);
//            mHolder.btn_estimate = (Button)convertView.findViewById(R.id.btn_estimate);
//            mHolder.btn_finish = (Button)convertView.findViewById(R.id.btn_finish);
//            mHolder.btn_candidate = (Button)convertView.findViewById(R.id.btn_candidate);
//            mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            mHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
            mHolder.rb_totalvalue = (RatingBar)convertView.findViewById(R.id.rb_totalvalue);
            mHolder.tv_pay = (TextView)convertView.findViewById(R.id.tv_pay);
//            mHolder.ll_button = (LinearLayout)convertView.findViewById(R.id.ll_button);
            mHolder.tv_numofconforder = (TextView)convertView.findViewById(R.id.tv_numofconforder);
            mHolder.gridView = (GridView)convertView.findViewById(R.id.gridview);
            mHolder.iv_gender = (ImageView)convertView.findViewById(R.id.iv_gender);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.drawable.listview_item_pressed);
        final Question item = questionList.get(position);

        mHolder.tv_answerunm.setText(item.getTotalAnswers() +"条回答");
        if(item.getPhotos() != null && item.getPhotos() != "") {
            List<String> photoPath = new ArrayList<String>();
            for (String photsCount : item.getPhotos().toString().split(",")) {
                photoPath.add(photsCount);
            }
            mHolder.gridView.setAdapter(new OrderDetailsGridViewAdapater(context,photoPath));
            mHolder.gridView.setVisibility(View.VISIBLE);
            mHolder.gridView.setClickable(false);
            mHolder.gridView.setPressed(false);
            mHolder.gridView.setEnabled(false);
//            mHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    Intent intent = new Intent();
////                    intent.setClass(context, AvatarZoomActivity.class);
////                    intent.putExtra("Avatar", (String)parent.getAdapter().getItem(position));
////                    context.startActivity(intent);
//
//                    Intent intent = new Intent();
//                    intent.setClass(context, QuestionDetailsActivity.class);
////                    Question questionDetails = (Question) parent.getAdapter().getItem(position);
//                    bundle.putSerializable("Question", item);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//            });
        }
        else
        {
            mHolder.gridView.setVisibility(View.GONE);
        }
        mHolder.txt_title.setText(item.getTitle());
        mHolder.tv_content.setText(item.getContent());
//        if(item.getType() == 1)
//        {
//            mHolder.txt_bounty.setTextColor(context.getResources().getColor(R.color.green));
//        }
//        else
//        {
//            mHolder.txt_bounty.setTextColor(Color.RED);
//        }
        mHolder.txt_bounty.setTextColor(Color.RED);
        SpannableString ss = new SpannableString( "￥" + String.valueOf(item.getBounty()));
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHolder.txt_bounty.setText(ss);
//        mHolder.txt_type.setText("类别:" + item.getCategory());
//        mHolder.txt_tag.setText("");
//        if( item.getTags() != null ) {
//            mHolder.txt_tag.setText("标签:" + item.getTags());
//        }
//        else
//        {
//            mHolder.txt_tag.setText("");
//        }
//        mHolder.txt_location.setText("");
//        if(item.getLocationAnonymity() == 1) {
//            mHolder.txt_location.setText("");
//        }
//        else
//        {
//            mHolder.txt_location.setText("地址:" + item.getAddress());
//        }
//        mHolder.tv_time.setText(item.getCreatedAt());
        mHolder.tv_name.setText("");

        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);

        if(item.getAsker().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
            mHolder.iv_gender.setImageDrawable(context.getResources().getDrawable(R.drawable.male));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
            mHolder.iv_gender.setImageDrawable(context.getResources().getDrawable(R.drawable.female));
        }

        if(item.getAsker().getAvatar() != null && !item.getAsker().getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getAsker().getAvatar());


        if (item.getAsker().getFNickName() != null && !item.getAsker().getFNickName().equals("")) {
            mHolder.tv_name.setText(item.getAsker().getFNickName());
        } else {
            mHolder.tv_name.setText(item.getAsker().getName());
        }

//        if(item.getNameAnonymity() == 1)
//        {
//            mHolder.tv_name.setText("匿名");
//            mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
//        }
//        else {
//            if (item.getOrderer().getFNickName() != null && !item.getOrderer().getFNickName().equals("")) {
//                mHolder.tv_name.setText(item.getOrderer().getFNickName());
//            } else {
//                mHolder.tv_name.setText(item.getOrderer().getName());
//            }
//            if(item.getOrderer().getAvatar() != null && !item.getOrderer().getAvatar().equals(""))
//            {
//                ImageUtils.setImageUrl(mHolder.iv_avatar, item.getOrderer().getAvatar());
//            }
//        }
        mHolder.rb_totalvalue.setRating((float) item.getAsker().getTotalBossPoint());
//        if(orderType == 1) {
//            mHolder.rb_totalvalue.setRating((float) item.getOrderer().getTotalWorkerPoint());
//        }
//        else if(orderType==2) {
//            mHolder.rb_totalvalue.setRating((float) item.getOrderer().getTotalBossPoint());
//        }
//        else
//        {
//
//        }

//        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);

//        ImageUtils.setImageUrl(mHolder.iv_avatar, item.getOrderer().getAvatar());
//        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
//        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);
//        if(item.getPaymentMethod().equals("线上支付"))
//        {
//            mHolder.tv_pay.setText("已支付");
//        }
//        else
//        {
//            mHolder.tv_pay.setText("未支付");
//        }
        if(item.getAcceptedAnswer() != null)
        {
            mHolder.tv_pay.setText("已赏");
        }
        else
        {
            mHolder.tv_pay.setText("悬赏中");
        }
        mHolder.tv_numofconforder.setText(String.format("(友帮了%d次)", item.getAsker().getCompletedNumberOfOrder()));
//        mHolder.btn_finish.setVisibility(View.GONE);
//        mHolder.btn_candidate.setVisibility(View.GONE);
//        switch (from)
//        {
//            case AppConfig.ORDERSTATUS_APPLY:
//                mHolder.btn_finish.setVisibility(View.GONE);
//                mHolder.btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
//                break;
//            case AppConfig.ORDERSTATUS_PROGRESS:
//                mHolder.btn_finish.setVisibility(View.GONE);
//                mHolder.btn_estimate.setText(context.getResources().getString(R.string.cancel_appley));
//                break;
//            case AppConfig.ORDERSTATUS_DONE:
//                mHolder.btn_finish.setVisibility(View.GONE);
//                mHolder.btn_estimate.setText(context.getResources().getString(R.string.estimate));
//                break;
//            case AppConfig.ORDERSTATUS_CLOSE:
//                mHolder.btn_finish.setVisibility(View.GONE);
//                mHolder.btn_estimate.setText(context.getResources().getString(R.string.close));
//                break;
//            case AppConfig.ORDERSTATUS_DELETE:
//                mHolder.btn_estimate.setText("取消任务");
//                mHolder.btn_finish.setVisibility(View.INVISIBLE);
//                if(item.getPaymentMethod().equals("线上支付") == true && item.getType() == 2 && item.getBossPaid() == 0) {
//                    mHolder.btn_finish.setVisibility(View.VISIBLE);
//                    if(item.getCandidate() != null && item.getCandidate().size() != 0)
//                    {
////                        mHolder.btn_candidate.setVisibility(View.VISIBLE);
//                        mHolder.btn_finish.setText("补充付款");
//                        mHolder.btn_estimate.setText("取消付款");
//                    }
//                    else
//                    {
//                        mHolder.btn_finish.setText("支付");
//                        mHolder.btn_estimate.setText("取消任务");
//                    }
//                }
//                if(item.getCandidate() != null && item.getCandidate().size() != 0)
//                {
//                    mHolder.btn_candidate.setVisibility(View.VISIBLE);
//                }
////                mHolder.btn_estimate.setText(context.getResources().getString(R.string.delete));
//
//                mHolder.tv_pay.setVisibility(View.INVISIBLE);
//                break;
//            case AppConfig.ORDERSTATUS_MAIN:
//                mHolder.btn_finish.setVisibility(View.GONE);
//                mHolder.btn_estimate.setVisibility(View.GONE);
//                mHolder.ll_button.setVisibility(View.GONE);
//                break;
//        }



        mHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(context, FriendsInfoActivity.class);
                bundle.putSerializable("Question", item);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });



//        mHolder.btn_finish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mHolder.btn_finish.getText().equals("支付")) {
//                    intent.setClass(context, PayActivity.class);
//                    bundle.putSerializable("OrderDetailsPay", item);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//                else
//                {
//                    intent.setClass(context, PayActivity.class);
//                    bundle.putSerializable("Candidate", item);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//            }
//        });
//
//        mHolder.btn_candidate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(context, CandidateActivity.class);
//                bundle.putSerializable("Candidate", item);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });
//
//        mHolder.btn_estimate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (from)
//                {
//                    case AppConfig.ORDERSTATUS_APPLY:
//                        Http.request((Activity) context, API.ORDER_CANCEL,new Object[]{String.valueOf(item.getId())},
//                                Http.map("UserId",String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        super.onSuccess(result);
//                                        orderDetails.remove(item);
//                                        notifyDataSetChanged();
//                                    }
//                                });
//                        break;
//                    case AppConfig.ORDERSTATUS_PROGRESS:
//                        Http.request((Activity) context, API.ORDER_CANCEL, new Object[]{String.valueOf(item.getId())},
//                                Http.map("UserId", String.valueOf(Auth.getCurrentUserId())), new Http.RequestListener<String>() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        super.onSuccess(result);
//                                        orderDetails.remove(item);
//                                        notifyDataSetChanged();
//                                    }
//                                });
//                        break;
//                    case AppConfig.ORDERSTATUS_DONE:
//                        context.startActivity(new Intent(context, OrderCommentActivity.class));
//                        break;
//                    case AppConfig.ORDERSTATUS_CLOSE:
//                        Http.request((Activity) context, API.CHANGE_STATUS, new Object[]{String.valueOf(item.getId())},
//                                Http.map("Status", "0"), new Http.RequestListener<String>() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        super.onSuccess(result);
//                                        orderDetails.remove(item);
//                                        notifyDataSetChanged();
//                                    }
//                                });
//                        break;
//                    case AppConfig.ORDERSTATUS_DELETE:
//                        if(mHolder.btn_estimate.getText().equals("取消任务")) {
//                            Http.request((Activity) context, API.CLOSE_ORDER, Http.map("Id", String.valueOf(item.getId())), new Http.RequestListener<String>() {
//                                @Override
//                                public void onSuccess(String result) {
//                                    super.onSuccess(result);
//                                    orderDetails.remove(item);
//                                    notifyDataSetChanged();
//                                }
//                            });
//                        }
//                        else if(mHolder.btn_estimate.getText().equals("取消付款"))
//                        {
//                            Http.request((Activity) context, API.CANCEL_PAY, Http.map("OrderId", String.valueOf(item.getId())), new Http.RequestListener<String>() {
//                                @Override
//                                public void onSuccess(String result) {
//                                    super.onSuccess(result);
////                                    orderDetails.remove(item);
////                                    notifyDataSetChanged();
//                                }
//                            });
//                        }
//                        else
//                        {}
//                        break;
//                    case AppConfig.ORDERSTATUS_MAIN:
//                        //btn_estimate.setVisibility(View.GONE);
//                        break;
//                }
//
//            }
//        });

        if(hasTopOrder  &&  position == 0) {
            convertView.setBackgroundColor(Color.GREEN);
        }
        else
        {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return questionList.get(position);
    }

    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<Question> items ){
        for(Question question : items)
        {
            questionList.add(question);
        }
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        //        private TextView txt_title,txt_bounty,txt_type,txt_location,tv_time,tv_name,tv_pay,tv_numofconforder,txt_tag;
        private TextView txt_title,txt_bounty,tv_name,tv_pay,tv_numofconforder,tv_content,tv_answerunm;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        //        private Button btn_estimate,btn_finish,btn_candidate;
        private RatingBar rb_totalvalue;
        //        private LinearLayout ll_button;
        private GridView gridView;
        private ImageView iv_gender,iv_avatar;
    }




}
