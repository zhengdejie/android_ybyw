package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

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
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final PushMessage item = pushMessages.get(position);
        mHolder.tv_message.setText(item.getContent());
        mHolder.tv_title.setText(item.getTitle());
        mHolder.tv_time.setText(item.getCreatedAt());
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

    static class ViewHolder
    {
        private TextView tv_message,tv_accept,tv_title,tv_time;
    }
}
