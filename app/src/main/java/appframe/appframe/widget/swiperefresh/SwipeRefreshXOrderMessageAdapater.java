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
import appframe.appframe.activity.BaseActivity;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.PushMessage;

/**
 * Created by Administrator on 2015/11/12.
 */
public class SwipeRefreshXOrderMessageAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<PushMessage> pushMessages ;

    public SwipeRefreshXOrderMessageAdapater(Context context,List<PushMessage> pushMessages)
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
                    .inflate(R.layout.swiperefreshx_ordermessage, null);
            mHolder = new ViewHolder();
            mHolder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final PushMessage item = pushMessages.get(position);
        mHolder.tv_message.setText(item.getContent());

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
        private TextView tv_message;
    }
}
