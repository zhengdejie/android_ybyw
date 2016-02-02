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
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SwipeRefreshXFriendShopsAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<UserDetail> friendshops;

    public SwipeRefreshXFriendShopsAdapater(Context context, List<UserDetail> friendshops)
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
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final UserDetail item = friendshops.get(position);
        mHolder.tv_name.setText(item.getName());
        mHolder.iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
        mHolder.iv_avatar.setErrorImageResId(R.drawable.default_avatar);
        ImageUtils.setImageUrl(mHolder.iv_avatar, item.getAvatar());
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
        private TextView tv_name;
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
    }
}
