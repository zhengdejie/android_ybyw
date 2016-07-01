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
import appframe.appframe.dto.FriendEvaluationDetail;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/9/8.
 */
public class SwipeRefreshXFriendEstimateAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<FriendEvaluationDetail> friendEvaluationDetails;

    public SwipeRefreshXFriendEstimateAdapater(Context context, List<FriendEvaluationDetail> friendEvaluationDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.friendEvaluationDetails = friendEvaluationDetails;
    }
    @Override
    public int getCount() {
        if(friendEvaluationDetails == null)
        {
            return 0;
        }
        return friendEvaluationDetails.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_friendestimate, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_evaluation = (TextView) convertView.findViewById(R.id.tv_evaluation);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final FriendEvaluationDetail item = friendEvaluationDetails.get(position);
        mHolder.tv_name.setText(item.getPraiser().getName());
        mHolder.tv_time.setText(item.getCreatedAt());
        mHolder.tv_evaluation.setText(item.getPraise());
        if(item.getPraiser().getAvatar()!=null) {
            ImageUtils.setImageUrl(mHolder.iv_avatar, item.getPraiser().getAvatar());
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return friendEvaluationDetails.get(position);
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<FriendEvaluationDetail> items ){
        for(FriendEvaluationDetail order : items)
        {
            friendEvaluationDetails.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name,tv_evaluation,tv_time;
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
    }
}
