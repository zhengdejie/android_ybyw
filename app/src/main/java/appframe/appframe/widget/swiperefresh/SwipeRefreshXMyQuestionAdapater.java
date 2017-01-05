package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
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
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.CandidateActivity;
import appframe.appframe.activity.PayActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.utils.Http;

import static appframe.appframe.R.id.tv_delete;
import static appframe.appframe.R.id.tv_pay;
import static appframe.appframe.R.id.tv_status;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SwipeRefreshXMyQuestionAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<Question> questionDetails = new ArrayList<Question>();

    Intent intent = new Intent();
    Bundle bundle = new Bundle();



    public SwipeRefreshXMyQuestionAdapater(Context context, List<Question> questionDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.questionDetails = questionDetails;
    }

    @Override
    public int getCount() {
        if(questionDetails == null)
        {
            return 0;
        }
        return questionDetails.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_myquestion, null);
            mHolder = new ViewHolder();
            mHolder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
            mHolder.tv_bounty = (TextView)convertView.findViewById(R.id.tv_bounty);
            mHolder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
            mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
//            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.iv_avatar);
            mHolder.tv_pay = (TextView)convertView.findViewById(tv_pay);
            mHolder.tv_delete = (TextView)convertView.findViewById(tv_delete);
            mHolder.tv_status = (TextView)convertView.findViewById(tv_status);
            mHolder.rl_bottom = (RelativeLayout)convertView.findViewById(R.id.rl_bottom);


            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.drawable.listview_item_pressed);
        final Question item = questionDetails.get(position);

        if(item.getStatus() == 1)
        {
            mHolder.rl_bottom.setVisibility(View.VISIBLE);
            mHolder.tv_pay.setVisibility(View.VISIBLE);
            mHolder.tv_delete.setText("取消支付");
            mHolder.tv_status.setVisibility(View.GONE);
        }
        else if (item.getStatus() == 2)
        {
            mHolder.rl_bottom.setVisibility(View.VISIBLE);
            mHolder.tv_pay.setVisibility(View.GONE);
            mHolder.tv_delete.setText("取消提问");
            mHolder.tv_status.setVisibility(View.GONE);
        }
        else if(item.getStatus() == 4)
        {
            mHolder.tv_status.setVisibility(View.VISIBLE);
            mHolder.rl_bottom.setVisibility(View.GONE);
        }
        else
        {
            mHolder.tv_status.setVisibility(View.GONE);
            mHolder.rl_bottom.setVisibility(View.GONE);
        }

//        if(item.getPhotos() != null && item.getPhotos() != "") {
//            List<String> photoPath = new ArrayList<String>();
//            for (String photsCount : item.getPhotos().toString().split(",")) {
//                photoPath.add(photsCount);
//            }
//
//        }
        mHolder.tv_title.setText(item.getTitle());
        mHolder.tv_bounty.setText("￥" + String.valueOf(item.getBounty()));
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
        mHolder.tv_time.setText(item.getCreatedAt());
        mHolder.tv_content.setText(item.getContent());





        mHolder.tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(context, PayActivity.class);
                bundle.putSerializable("MyQuestion", item);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        mHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Http.request((Activity) context, API.COLSE_MYQUESTION, new Object[]{item.getId()}, new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        Toast.makeText(context,"取消成功",Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, context.getClass()));
                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                    }
                });
            }
        });


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return questionDetails.get(position);
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<Question> items ){
        for(Question question : items)
        {
            questionDetails.add(question);
        }
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_title,tv_content,tv_bounty,tv_time,tv_pay,tv_delete,tv_status;
        private RelativeLayout rl_bottom;
//        private com.android.volley.toolbox.NetworkImageView iv_avatar;

    }

}
