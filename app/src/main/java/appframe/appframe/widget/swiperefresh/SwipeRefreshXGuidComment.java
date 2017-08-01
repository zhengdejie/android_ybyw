package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.GuideComments;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

import static appframe.appframe.R.id.iv_avatar;
import static appframe.appframe.R.id.niv_avatar;

/**
 * Created by Administrator on 2017/7/20.
 */

public class SwipeRefreshXGuidComment extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<GuideComments> guideComments = new ArrayList<GuideComments>();
//    Intent intent = new Intent();
//    Bundle bundle = new Bundle();
    //    TextView tv_name,tv_ordercomment,tv_time;
//    ImageButton ib_delete;
//    String userID;
//    boolean authority_delete;

    public SwipeRefreshXGuidComment(Context context,List<GuideComments> guideComments)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.guideComments = guideComments;

    }
    @Override
    public int getCount() {
        if(guideComments == null)
        {
            return 0;
        }
        return guideComments.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SwipeRefreshXGuidComment.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_guidecomment, null);
            mHolder = new SwipeRefreshXGuidComment.ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_ordercomment = (TextView) convertView.findViewById(R.id.tv_ordercomment);
            mHolder.rb_evaluation = (RatingBar) convertView.findViewById(R.id.rb_evaluation);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.niv_avatar = (appframe.appframe.utils.CircleImageViewCustomer) convertView.findViewById(niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(iv_avatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXGuidComment.ViewHolder) convertView.getTag();
        }

        final GuideComments item = guideComments.get(position);


        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);

        if(item.getCommentator().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
        }




        mHolder.tv_time.setText(item.getCreatedAt());
        mHolder.tv_ordercomment.setText(item.getComment());
        mHolder.rb_evaluation.setRating(item.getStars());

        mHolder.tv_name.setText(item.getCommentator().getName());

        if(item.getCommentator().getAvatar() != null && !item.getCommentator().getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getCommentator().getAvatar());





        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return guideComments.get(position);
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<GuideComments> items ){
        for(GuideComments order : items)
        {
            guideComments.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name,tv_ordercomment,tv_time;
        private appframe.appframe.utils.CircleImageViewCustomer niv_avatar;

        private ImageView iv_avatar;
        private RatingBar rb_evaluation;
    }
}
