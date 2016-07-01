package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SwipeRefreshXOrderComment extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<OrderComment> orderComments = new ArrayList<OrderComment>();
//    TextView tv_name,tv_ordercomment,tv_time;
//    ImageButton ib_delete;
    String userID;
    boolean authority_delete;

    public SwipeRefreshXOrderComment(Context context,List<OrderComment> orderComments,String userID,boolean authority_delete)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderComments = orderComments;
        this.userID = userID;
        this.authority_delete =authority_delete;
    }
    @Override
    public int getCount() {
        if(orderComments == null)
        {
            return 0;
        }
        return orderComments.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_ordercomment, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_ordercomment = (TextView) convertView.findViewById(R.id.tv_ordercomment);
            mHolder.ib_delete = (ImageButton)convertView.findViewById(R.id.ib_delete);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final OrderComment item = orderComments.get(position);


        if(item.getUser().getAvatar() != null && !item.getUser().getAvatar().equals(""))
        {
            ImageUtils.setImageUrl(mHolder.iv_avatar, item.getUser().getAvatar());
        }
        else
        {
            if(item.getUser().getGender().equals(context.getResources().getString(R.string.male).toString()))
            {
                mHolder.iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
            }
            else
            {
                mHolder.iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
            }

        }
        mHolder.tv_time.setText(item.getCreatedAt());
        mHolder.tv_ordercomment.setText(item.getComment());
        if(item.isAnonymity())
        {
            mHolder.tv_name.setText("*****:");
        }
        else {
            mHolder.tv_name.setText(item.getUser().getName() + ":");
        }
        if(authority_delete) {
            mHolder.ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("UserId", userID);
                    map.put("CommentId", String.valueOf(item.getId()));
                    Http.request((Activity) context, API.ORDER_DELETECOOMENT, new Object[]{Http.getURL(map)}, new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);

                            orderComments.remove(position);
                            notifyDataSetChanged();
                            TextView tv_comment = (TextView) ((Activity) context).findViewById(R.id.tv_comment);
                            tv_comment.setText(String.format("留言（%d条）", orderComments != null ? orderComments.size() : 0));
                        }
                    });

                }
            });
        }
        else
        {
            mHolder.ib_delete.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return orderComments.get(position);
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<OrderComment> items ){
        for(OrderComment order : items)
        {
            orderComments.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name,tv_ordercomment,tv_time;
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
        private ImageButton ib_delete;
    }
}
