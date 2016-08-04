package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.PayActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.AnswerDetailWithQuestionDetail;
import appframe.appframe.dto.Question;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SwipeRefreshXMyAnswerAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<AnswerDetailWithQuestionDetail> answerDetailWithQuestionDetails = new ArrayList<AnswerDetailWithQuestionDetail>();

    Intent intent = new Intent();
    Bundle bundle = new Bundle();



    public SwipeRefreshXMyAnswerAdapater(Context context, List<AnswerDetailWithQuestionDetail> answerDetailWithQuestionDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.answerDetailWithQuestionDetails = answerDetailWithQuestionDetails;
    }

    @Override
    public int getCount() {
        if(answerDetailWithQuestionDetails == null)
        {
            return 0;
        }
        return answerDetailWithQuestionDetails.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_myanswer, null);
            mHolder = new ViewHolder();
            mHolder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            mHolder.tv_bounty = (TextView)convertView.findViewById(R.id.tv_bounty);
            mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
//            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
            mHolder.tv_answer = (TextView)convertView.findViewById(R.id.tv_answer);



            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.drawable.listview_item_pressed);
        final AnswerDetailWithQuestionDetail item = answerDetailWithQuestionDetails.get(position);

//        if(item.getPhotos() != null && item.getPhotos() != "") {
//            List<String> photoPath = new ArrayList<String>();
//            for (String photsCount : item.getPhotos().toString().split(",")) {
//                photoPath.add(photsCount);
//            }
//
//        }
        mHolder.tv_title.setText(item.getQuestion().getTitle());
        mHolder.tv_bounty.setText("￥" + String.valueOf(item.getQuestion().getBounty()));
//        if(item.getType() == 1)
//        {
//            mHolder.txt_bounty.setTextColor(context.getResources().getColor(R.color.green));
//        }
//        else
//        {
//            mHolder.txt_bounty.setTextColor(Color.RED);
//        }
//        SpannableString ss = new SpannableString( "￥" + String.valueOf(item.getBounty()));
//        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mHolder.txt_bounty.setText(ss);
//        mHolder.txt_type.setText("所属类目 : " + item.getCategory());
//
//        mHolder.txt_location.setText("");
//        if(item.getLocationAnonymity() == 1) {
//            mHolder.txt_location.setText("");
//        }
//        else
//        {
//            mHolder.txt_location.setText("地址:" + item.getAddress());
//        }
        mHolder.tv_time.setText(item.getQuestion().getCreatedAt());
        mHolder.tv_answer.setText(item.getAnswer().getContent());





        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return answerDetailWithQuestionDetails.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_title,tv_bounty,tv_time,tv_answer;
//        private com.android.volley.toolbox.NetworkImageView iv_avatar;

    }

}

