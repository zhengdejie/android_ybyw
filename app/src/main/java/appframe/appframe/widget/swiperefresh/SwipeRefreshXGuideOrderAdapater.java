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
import appframe.appframe.dto.RecommendOrder;
import appframe.appframe.utils.ImageUtils;

import static appframe.appframe.R.id.tv_workingage;
import static com.alibaba.mobileim.YWChannel.getResources;

/**
 * Created by Administrator on 2017-01-09.
 */

public class SwipeRefreshXGuideOrderAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<RecommendOrder> guildList;

    public SwipeRefreshXGuideOrderAdapater(Context context, List<RecommendOrder> guildList)
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

        final SwipeRefreshXGuideOrderAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_guideorder, null);
            mHolder = new SwipeRefreshXGuideOrderAdapater.ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_workingage = (TextView) convertView.findViewById(R.id.tv_workingage);
            mHolder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            mHolder.iv_backgroud = (ImageView) convertView.findViewById(R.id.iv_backgroud);
            mHolder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
            mHolder.civ_avatar = (appframe.appframe.utils.CircleImageViewCustomer) convertView.findViewById(R.id.civ_avatar);
//            mHolder.iv_honor = (ImageView) convertView.findViewById(R.id.iv_honor);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXGuideOrderAdapater.ViewHolder) convertView.getTag();
        }

        final RecommendOrder item = guildList.get(position);

        mHolder.tv_title.setText(item.getTitle());
        mHolder.tv_name.setText(item.getUserDetail().getName());
//        int resID = getResources().getIdentifier("ic_launcher", "drawable", "appframe.appframe");
//        mHolder.iv_backgroud.setImageResource(resID);
        ImageUtils.setImageUrl(mHolder.civ_avatar, item.getUserDetail().getAvatar());
        mHolder.tv_workingage.setText("工："+item.getWorkingyears() +"年");
        mHolder.tv_position.setText("职业: "+item.getPosition());

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
        private TextView tv_title,tv_name,tv_workingage,tv_position;
        private ImageView iv_backgroud,iv_sex,iv_honor;
        private appframe.appframe.utils.CircleImageViewCustomer civ_avatar;
    }
}

