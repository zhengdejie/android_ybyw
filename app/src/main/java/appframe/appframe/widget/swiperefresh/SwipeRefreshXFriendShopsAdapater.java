package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SwipeRefreshXFriendShopsAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ConfirmedOrderDetailWithFriend> friendshops;

    public SwipeRefreshXFriendShopsAdapater(Context context, List<ConfirmedOrderDetailWithFriend> friendshops)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.friendshops = friendshops;
    }
    @Override
    public int getCount() {
        if(friendshops == null)
        {
            return 0;
        }
        return friendshops.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_friendshops, null);
            mHolder = new ViewHolder();
            mHolder.tv_myname = (TextView) convertView.findViewById(R.id.tv_myname);
            mHolder.tv_yourname = (TextView) convertView.findViewById(R.id.tv_yourname);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_yourtitle = (TextView) convertView.findViewById(R.id.tv_yourtitle);

            mHolder.niv_myavatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_myavatar);
            mHolder.niv_youravatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_youravatar);
            mHolder.iv_myavatar = (ImageView) convertView.findViewById(R.id.iv_myavatar);
            mHolder.iv_youravatar = (ImageView) convertView.findViewById(R.id.iv_youravatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final ConfirmedOrderDetailWithFriend item = friendshops.get(position);
//        mHolder.iv_myavatar.setDefaultImageResId(R.drawable.default_avatar);
//        mHolder.iv_youravatar.setDefaultImageResId(R.drawable.default_avatar);
        mHolder.tv_yourtitle.setVisibility(View.VISIBLE);
//        mHolder.tv_title.setVisibility(View.VISIBLE);
        if(item.getFirstDegreeFriendId() == item.getServiceProvider().getId())
        {
            mHolder.tv_myname.setText(item.getServiceProvider().getName());
            if(item.getServiceProvider().getAvatar() != null && !item.getServiceProvider().getAvatar().equals("")) {
                mHolder.iv_myavatar.setVisibility(View.INVISIBLE);
                mHolder.niv_myavatar.setVisibility(View.VISIBLE);
            }
            else
            {
                mHolder.iv_myavatar.setVisibility(View.VISIBLE);
                mHolder.niv_myavatar.setVisibility(View.INVISIBLE);
                if(item.getServiceProvider().getGender().equals(context.getResources().getString(R.string.male).toString()))
                {
                    mHolder.iv_myavatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
                }
                else
                {
                    mHolder.iv_myavatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
                }
            }
            ImageUtils.setImageUrl(mHolder.niv_myavatar, item.getServiceProvider().getAvatar());
            mHolder.tv_yourname.setText(item.getServiceReceiver().getName());
            if(item.getServiceReceiver().getAvatar() != null && !item.getServiceReceiver().getAvatar().equals("")) {
                mHolder.iv_youravatar.setVisibility(View.INVISIBLE);
                mHolder.niv_youravatar.setVisibility(View.VISIBLE);
            }
            else
            {
                mHolder.iv_youravatar.setVisibility(View.VISIBLE);
                mHolder.niv_youravatar.setVisibility(View.INVISIBLE);
                if(item.getServiceReceiver().getGender().equals(context.getResources().getString(R.string.male).toString()))
                {
                    mHolder.iv_youravatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
                }
                else
                {
                    mHolder.iv_youravatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
                }
            }
            ImageUtils.setImageUrl(mHolder.niv_youravatar, item.getServiceReceiver().getAvatar());

            if(item.getFirstDegreeFriendId() == item.getPoster().getId())
            {
                mHolder.tv_title.setText("发布了求助 " + item.getOrder().getTitle() +", 被以下用户接受");
//                mHolder.tv_title.setVisibility(View.VISIBLE);
                mHolder.tv_yourtitle.setVisibility(View.INVISIBLE);
            }
            else
            {
//                mHolder.tv_title.setVisibility(View.INVISIBLE);
                mHolder.tv_title.setText("服务了以下用户");
//                mHolder.tv_title.setVisibility(View.VISIBLE);
                mHolder.tv_yourtitle.setVisibility(View.VISIBLE);
                mHolder.tv_yourtitle.setText(item.getOrder().getTitle());
            }
        }
        else if(item.getFirstDegreeFriendId() == item.getServiceReceiver().getId())
        {
            if(item.getServiceReceiver().getAvatar() != null && !item.getServiceReceiver().getAvatar().equals("")) {
                mHolder.iv_myavatar.setVisibility(View.INVISIBLE);
                mHolder.niv_myavatar.setVisibility(View.VISIBLE);
            }
            else
            {
                mHolder.iv_myavatar.setVisibility(View.VISIBLE);
                mHolder.niv_myavatar.setVisibility(View.INVISIBLE);
                if(item.getServiceReceiver().getGender().equals(context.getResources().getString(R.string.male).toString()))
                {
                    mHolder.iv_myavatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
                }
                else
                {
                    mHolder.iv_myavatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
                }
            }
            mHolder.tv_myname.setText(item.getServiceReceiver().getName());
            ImageUtils.setImageUrl(mHolder.niv_myavatar, item.getServiceReceiver().getAvatar());
            if(item.getServiceProvider().getAvatar() != null && !item.getServiceProvider().getAvatar().equals("")) {
                mHolder.iv_youravatar.setVisibility(View.INVISIBLE);
                mHolder.niv_youravatar.setVisibility(View.VISIBLE);
            }
            else
            {
                mHolder.iv_youravatar.setVisibility(View.VISIBLE);
                mHolder.niv_youravatar.setVisibility(View.INVISIBLE);
                if(item.getServiceProvider().getGender().equals(context.getResources().getString(R.string.male).toString()))
                {
                    mHolder.iv_youravatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
                }
                else
                {
                    mHolder.iv_youravatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
                }
            }
            mHolder.tv_yourname.setText(item.getServiceProvider().getName());
            ImageUtils.setImageUrl(mHolder.niv_youravatar, item.getServiceProvider().getAvatar());

            if(item.getFirstDegreeFriendId() == item.getPoster().getId())
            {
                mHolder.tv_title.setText("发布了求助 " + item.getOrder().getTitle() +", 被以下用户接受");
//                mHolder.tv_title.setVisibility(View.VISIBLE);
                mHolder.tv_yourtitle.setVisibility(View.INVISIBLE);
            }
            else
            {
//                mHolder.tv_title.setVisibility(View.INVISIBLE);
                mHolder.tv_title.setText("服务了以下用户");
                mHolder.tv_yourtitle.setVisibility(View.VISIBLE);
                mHolder.tv_yourtitle.setText(item.getOrder().getTitle());
            }
        }
        else
        {

        }

        mHolder.tv_time.setText(item.getCompleteDatewithouttime());

        return convertView;

    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<ConfirmedOrderDetailWithFriend> items ){
        for(ConfirmedOrderDetailWithFriend order : items)
        {
            friendshops.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public Object getItem(int position) {
        return friendshops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_myname,tv_yourname,tv_time,tv_title,tv_yourtitle;
        private com.android.volley.toolbox.NetworkImageView niv_myavatar,niv_youravatar;
        private ImageView iv_myavatar,iv_youravatar;
    }
}
