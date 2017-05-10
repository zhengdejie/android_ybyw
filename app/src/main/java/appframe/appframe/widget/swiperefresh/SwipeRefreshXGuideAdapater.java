package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.Recommend;

import static com.alibaba.mobileim.YWChannel.getResources;

/**
 * Created by Administrator on 2017-01-05.
 */

public class SwipeRefreshXGuideAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Recommend> guildList;

    public SwipeRefreshXGuideAdapater(Context context, List<Recommend> guildList)
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

        final SwipeRefreshXGuideAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_guide, null);
            mHolder = new SwipeRefreshXGuideAdapater.ViewHolder();
            mHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.iv_backgroud = (ImageView) convertView.findViewById(R.id.iv_backgroud);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXGuideAdapater.ViewHolder) convertView.getTag();
        }

        final Recommend item = guildList.get(position);

        mHolder.tv_title.setText(item.getTitle());
        mHolder.tv_content.setText(item.getContent());
//        int resID = getResources().getIdentifier("police_station", "drawable", "appframe.appframe");
//        mHolder.iv_backgroud.setImageResource(resID);
//        mHolder.iv_backgroud.setBackgroundDrawable(getResources().getDrawable(R.drawable.police_station));

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
        private TextView tv_content,tv_title;
        private ImageView iv_backgroud;
    }
}

