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
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2016-07-29.
 */
public class SwipeRefreshXMyfansAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<UserDetail> userDetails;

    public SwipeRefreshXMyfansAdapater(Context context,List<UserDetail> userDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.userDetails = userDetails;
    }
    @Override
    public int getCount() {
        if(userDetails == null)
        {
            return 0;
        }
        return userDetails.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.swiperefreshx_myfans, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
//            mHolder.tv_mobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final UserDetail item = userDetails.get(position);
        mHolder.tv_name.setText(item.getName());
//        mHolder.tv_mobile.setText(item.getMobile());

        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);

        if(item.getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
        }

        if(item.getAvatar() != null && !item.getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getAvatar());


        return convertView;
    }



    @Override
    public Object getItem(int position) {
        return userDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<UserDetail> items ){
        for(UserDetail order : items)
        {
            userDetails.add(order);
        }
        notifyDataSetChanged();

    }
    static class ViewHolder
    {
        private TextView tv_name;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}


