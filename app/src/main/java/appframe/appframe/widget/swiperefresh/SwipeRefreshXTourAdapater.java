package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import java.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.dto.RussianTour;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SwipeRefreshXTourAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<GuideTourOrder> guideTourOrderList = new ArrayList<GuideTourOrder>();
    String From = "";


    public SwipeRefreshXTourAdapater(Context context, List<GuideTourOrder> guideTourOrderList)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.guideTourOrderList = guideTourOrderList;

    }
    public SwipeRefreshXTourAdapater(Context context, List<GuideTourOrder> guideTourOrderList, String From)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.guideTourOrderList = guideTourOrderList;
        this.From = From;
    }

    @Override
    public int getCount() {
        if(guideTourOrderList == null)
        {
            return 0;
        }
        return guideTourOrderList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SwipeRefreshXTourAdapater.ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_tour, null);
            mHolder = new SwipeRefreshXTourAdapater.ViewHolder();
            mHolder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            mHolder.iv_project = (appframe.appframe.utils.CircleImageViewCustomer) convertView.findViewById(R.id.iv_project);
            mHolder.iv_avatar = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (SwipeRefreshXTourAdapater.ViewHolder) convertView.getTag();
        }

        final GuideTourOrder item = guideTourOrderList.get(position);

        if(From.equals("Guide"))
        {
            mHolder.tv_name.setText(item.getApplicant().getName());

            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.iv_project.setVisibility(View.INVISIBLE);
            if(item.getApplicant().getGender().equals(context.getResources().getString(R.string.male).toString()))
            {
                mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
            }
            else
            {
                mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
            }
            if(item.getApplicant().getAvatar() != null && !item.getApplicant().getAvatar().equals(""))
            {
                mHolder.iv_avatar.setVisibility(View.INVISIBLE);
                mHolder.iv_project.setVisibility(View.VISIBLE);
            }
            else
            {
                mHolder.iv_avatar.setVisibility(View.VISIBLE);
                mHolder.iv_project.setVisibility(View.INVISIBLE);
            }

            ImageUtils.setImageUrl(mHolder.iv_project, item.getApplicant().getAvatar());

        }
        else if(From.equals("User"))
        {
            mHolder.tv_name.setText(item.getGuide().getUser().getName());

            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.iv_project.setVisibility(View.INVISIBLE);
            if(item.getGuide().getUser().getGender().equals(context.getResources().getString(R.string.male).toString()))
            {
                mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
            }
            else
            {
                mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
            }
            if(item.getGuide().getUser().getAvatar() != null && !item.getGuide().getUser().getAvatar().equals(""))
            {
                mHolder.iv_avatar.setVisibility(View.INVISIBLE);
                mHolder.iv_project.setVisibility(View.VISIBLE);
            }
            else
            {
                mHolder.iv_avatar.setVisibility(View.VISIBLE);
                mHolder.iv_project.setVisibility(View.INVISIBLE);
            }

            ImageUtils.setImageUrl(mHolder.iv_project, item.getGuide().getUser().getAvatar());
        }
        else
        {

        }
        if(item.getRendezvous().getAddressInString().contains(","))
        {
            mHolder.tv_location.setText(item.getRendezvous().getAddressInString().split(",")[0]);
        }
        else
        {
            mHolder.tv_location.setText(item.getRendezvous().getAddressInString());
        }

//        String endTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getEndTime());
        String endTime = item.getEndTime().split(" ")[1];
        mHolder.tv_time.setText(item.getStartTime() + "-" + endTime);

        mHolder.tv_money.setText(String.valueOf(item.getGuide().getHourlyRatePrice() * item.getHours()));
        if(item.getStatus() == 100 || item.getStatus() == 202 ||item.getStatus() == 201)
        {
            mHolder.tv_status.setText("申请中");
        }
        else if(item.getStatus() == 300)
        {
            mHolder.tv_status.setText("等待确认");
        }
        else if(item.getStatus() == 400)
        {
            mHolder.tv_status.setText("进行中");
        }
        else if(item.getStatus() == 401)
        {
            mHolder.tv_status.setText("已取消");
        }
        else if(item.getStatus() == 500)
        {
            mHolder.tv_status.setText("等待评价");
        }
        else if(item.getStatus() == 600)
        {
            mHolder.tv_status.setText("已完成");
        }
        else
        {

        }


//        mHolder.tv_time.setText(item.getCreatedAt());
//        mHolder.tv_ordercomment.setText(item.getContent());


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return guideTourOrderList.get(position);
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<GuideTourOrder> items ){
        for(GuideTourOrder order : items)
        {
            guideTourOrderList.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name,tv_location,tv_time,tv_money,tv_status;
        private de.hdodenhof.circleimageview.CircleImageView iv_avatar;
        private appframe.appframe.utils.CircleImageViewCustomer iv_project;
    }
}

