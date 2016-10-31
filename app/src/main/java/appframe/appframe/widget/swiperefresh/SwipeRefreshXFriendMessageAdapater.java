package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import appframe.appframe.dto.PushMessage;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/11/17.
 */
public class SwipeRefreshXFriendMessageAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<PushMessage> pushMessages ;

    public SwipeRefreshXFriendMessageAdapater(Context context,List<PushMessage> pushMessages)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.pushMessages = pushMessages;
    }
    @Override
    public int getCount() {
        return pushMessages == null ? 0 : pushMessages.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.swiperefreshx_friendmessage, null);
            mHolder = new ViewHolder();
            mHolder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            mHolder.tv_accept = (TextView) convertView.findViewById(R.id.tv_accept);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final PushMessage item = pushMessages.get(position);
        mHolder.tv_message.setText(item.getContent());
        mHolder.tv_name.setText(item.getSender().getName());

        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);

        if(item.getSender().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
        }

        if(item.getSender().getAvatar() != null && !item.getSender().getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getSender().getAvatar());


        if(item.getFriendRequestAccepted() == 1)
        {
            mHolder.tv_accept.setBackgroundColor(Color.GRAY);
        }
        else {
            mHolder.tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Http.request((Activity) context, API.ACCEPT_ADDFDF, new Object[]{Auth.getCurrentUserId()},
                            Http.map("FriendId", String.valueOf(item.getSender().getId()),
                                    "MessageId", String.valueOf(item.getId())),
                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    Toast.makeText(context, "添加一度朋友成功", Toast.LENGTH_SHORT).show();
                                    mHolder.tv_accept.setBackgroundColor(Color.GRAY);
                                }
                            });
                }
            });
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return pushMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<PushMessage> items ){
        for(PushMessage question : items)
        {
            pushMessages.add(question);
        }
        notifyDataSetChanged();

    }
    static class ViewHolder
    {
        private TextView tv_message,tv_accept,tv_name;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}
