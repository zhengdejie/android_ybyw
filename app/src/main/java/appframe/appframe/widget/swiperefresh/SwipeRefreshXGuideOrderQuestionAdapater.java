package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.GuideOrderDetailsActivity;
import appframe.appframe.activity.GuideOrderQuestionActivity;
import appframe.appframe.dto.RecommendOrderQuestion;

/**
 * Created by Administrator on 2017-01-11.
 */

public class SwipeRefreshXGuideOrderQuestionAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<RecommendOrderQuestion> guildList;

    public SwipeRefreshXGuideOrderQuestionAdapater(Context context, List<RecommendOrderQuestion> guildList)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.guildList = guildList;
    }
    @Override
    public int getCount() {
        if(guildList == null)
        {
            return 0;
        }
        return guildList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SwipeRefreshXGuideOrderQuestionAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_guideorderquestion, null);
            mHolder = new SwipeRefreshXGuideOrderQuestionAdapater.ViewHolder();
            mHolder.tv_view = (TextView) convertView.findViewById(R.id.tv_view);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXGuideOrderQuestionAdapater.ViewHolder) convertView.getTag();
        }

        final RecommendOrderQuestion item = guildList.get(position);

        mHolder.tv_title.setText(item.getQuestion());
        mHolder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(context, GuideOrderQuestionActivity.class);
                bundle.putSerializable("RecommendOrderQuestion", item);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });

        return convertView;

    }
    //    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
//    public  void addItems(List<ConfirmedOrderDetailWithFriend> items ){
//        for(ConfirmedOrderDetailWithFriend order : items)
//        {
//            guildList.add(order);
//        }
//        notifyDataSetChanged();
//
//    }
    @Override
    public Object getItem(int position) {
        return guildList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_title,tv_view;
    }
}


