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

/**
 * Created by Administrator on 2015/10/22.
 */
public class SwipeRefreshXFriendShopsAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<String> friendshops = new ArrayList<String>();
    TextView tv_content,tv_title,tv_name;

    public SwipeRefreshXFriendShopsAdapater(Context context, List<String> friendshops)
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
        convertView = layoutInflater.inflate(R.layout.swiperefreshx_friendshops, null);
//        tv_title = (TextView)convertView.findViewById(R.id.tv_title);
//        tv_content = (TextView)convertView.findViewById(R.id.tv_content);
//        tv_name = (TextView)convertView.findViewById(R.id.tv_name);
//        tv_title.setText(orderReviewDetails.get(position).getTitle());
//        tv_content.setText(orderReviewDetails.get(position).getContent());
//        tv_name.setText(orderReviewDetails.get(position).getCommontatorName());

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
}
