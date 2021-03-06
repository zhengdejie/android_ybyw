package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.AnswerDetail;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2016/5/18.
 */
public class SwipeRefreshXAnswerAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<AnswerDetail> answerDetailList = new ArrayList<AnswerDetail>();



    public SwipeRefreshXAnswerAdapater(Context context, List<AnswerDetail> answerDetailList)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.answerDetailList = answerDetailList;

    }
    @Override
    public int getCount() {
        if(answerDetailList == null)
        {
            return 0;
        }
        return answerDetailList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_answer, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_ordercomment = (TextView) convertView.findViewById(R.id.tv_ordercomment);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final AnswerDetail item = answerDetailList.get(position);

        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);

        if(item.getAnswerer().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
        }

        if(item.getAnswerer().getAvatar() != null && !item.getAnswerer().getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getAnswerer().getAvatar());


        mHolder.tv_name.setText(item.getAnswerer().getName());
        mHolder.tv_time.setText(item.getCreatedAt());
        mHolder.tv_ordercomment.setText(item.getContent());


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return answerDetailList.get(position);
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<AnswerDetail> items ){
        for(AnswerDetail order : items)
        {
            answerDetailList.add(order);
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
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}

