package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2016/6/12.
 */
public class SwipeRefreshXExpandFriendsAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<UserDetail> userDetails;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();

    public SwipeRefreshXExpandFriendsAdapater(Context context,List<UserDetail> userDetails)
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
                    .inflate(R.layout.swiperefreshx_expandfriends, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final UserDetail item = userDetails.get(position);
        mHolder.tv_name.setText(item.getName());

        mHolder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Http.request((Activity) context, API.ADD_FDF, new Object[]{Auth.getCurrentUserId()},
                        Http.map("FriendId", String.valueOf(item.getId())),
                        new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);
                                Toast.makeText(context, "已发送好友申请", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

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

        mHolder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(context, FriendsInfoActivity.class);
                bundle.putSerializable("UserDetail", item);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        mHolder.niv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(context, FriendsInfoActivity.class);
                bundle.putSerializable("UserDetail", item);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }



    @Override
    public Object getItem(int position) {
        return userDetails.get(position);
    }

    public  void addItems(List<UserDetail> items ){
        for(UserDetail item : items)
        {
            userDetails.add(item);
        }
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name,tv_add;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}

