package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.FriendEvaluationDetail;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/9/8.
 */
public class SwipeRefreshXOrderEstimateAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<OrderReviewDetail> orderReviewDetails;
    TextView tv_content,tv_title,tv_name;

    public SwipeRefreshXOrderEstimateAdapater(Context context,List<OrderReviewDetail> orderReviewDetails)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderReviewDetails = orderReviewDetails;
    }
    @Override
    public int getCount() {
        if(orderReviewDetails == null)
        {
            return 0;
        }
        return orderReviewDetails.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_orderestimate, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
//            mHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            mHolder.gridView = (GridView)convertView.findViewById(R.id.gridview);
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final OrderReviewDetail item = orderReviewDetails.get(position);
        mHolder.tv_name.setText(item.getUser().getName());
        mHolder.tv_time.setText(item.getCreatedAt());
        mHolder.tv_content.setText(item.getContent());
        mHolder.tv_title.setText("评价来自订单标题为"+item.getOrder().getTitle());
//        mHolder.ratingBar.setRating((float) (item.getAttitudePoint() + item.getCharacterPoint() + item.getServicePoint()) / 3);

        if(item.getPhotos() != null && item.getPhotos() != "") {
            List<String> photoPath = new ArrayList<String>();
            for (String photsCount : item.getPhotos().toString().split(",")) {
                photoPath.add(photsCount);
            }
            mHolder.gridView.setAdapter(new OrderDetailsGridViewAdapater(context,photoPath));
            mHolder.gridView.setVisibility(View.VISIBLE);
//            mHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent();
//                    intent.setClass(context, AvatarZoomActivity.class);
//                    intent.putExtra("Avatar", (String)parent.getAdapter().getItem(position));
//                    context.startActivity(intent);
//                }
//            });
        }
        else
        {
            mHolder.gridView.setVisibility(View.GONE);
        }
        if(item.getUser().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
        }
        else
        {
            mHolder.iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
        }
        if(item.getUser().getAvatar() != null && !item.getUser().getAvatar().equals(""))
        {
            ImageUtils.setImageUrl(mHolder.iv_avatar, item.getUser().getAvatar());
        }
        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return orderReviewDetails.get(position);
    }

    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<OrderReviewDetail> items ){
        for(OrderReviewDetail order : items)
        {
            orderReviewDetails.add(order);
        }
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name,tv_content,tv_time,tv_title;
//        private RatingBar ratingBar;
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
        private GridView gridView;;
    }
}
