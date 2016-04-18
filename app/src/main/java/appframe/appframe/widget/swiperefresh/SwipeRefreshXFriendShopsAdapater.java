package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.dto.Nearby;
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
            mHolder.iv_myavatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.iv_myavatar);
            mHolder.iv_youravatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.iv_youravatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final ConfirmedOrderDetailWithFriend item = friendshops.get(position);
        mHolder.iv_myavatar.setDefaultImageResId(R.drawable.default_avatar);
        mHolder.iv_youravatar.setDefaultImageResId(R.drawable.default_avatar);

        if(item.getFirstDegreeFriendId() == item.getServiceProvider().getId())
        {
            mHolder.tv_myname.setText(item.getServiceProvider().getName());
            ImageUtils.setImageUrl(mHolder.iv_myavatar, item.getServiceProvider().getAvatar());
            mHolder.tv_yourname.setText(item.getServiceReceiver().getName());
            ImageUtils.setImageUrl(mHolder.iv_youravatar, item.getServiceReceiver().getAvatar());
        }
        else if(item.getFirstDegreeFriendId() == item.getServiceReceiver().getId())
        {
            mHolder.tv_myname.setText(item.getServiceReceiver().getName());
            ImageUtils.setImageUrl(mHolder.iv_myavatar, item.getServiceReceiver().getAvatar());
            mHolder.tv_yourname.setText(item.getServiceProvider().getName());
            ImageUtils.setImageUrl(mHolder.iv_youravatar, item.getServiceProvider().getAvatar());
        }
        else
        {

        }


        return convertView;

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
        private TextView tv_myname,tv_yourname;
        private com.android.volley.toolbox.NetworkImageView iv_myavatar,iv_youravatar;
    }
}
