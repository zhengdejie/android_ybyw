package appframe.appframe.widget.swiperefresh;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.FriendEvaluationDetail;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_friendestimate, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_evaluation = (TextView) convertView.findViewById(R.id.tv_evaluation);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            mHolder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
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

        mHolder.iv_avatar.setVisibility(View.VISIBLE);
        mHolder.niv_avatar.setVisibility(View.INVISIBLE);

        if(item.getPraiser().getGender().equals(context.getResources().getString(R.string.male).toString()))
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.maleavatar));
        }
        else
        {
            mHolder.iv_avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.femaleavatar));
        }

        if(item.getPraiser().getAvatar() != null && !item.getPraiser().getAvatar().equals(""))
        {
            mHolder.iv_avatar.setVisibility(View.INVISIBLE);
            mHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.iv_avatar.setVisibility(View.VISIBLE);
            mHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(mHolder.niv_avatar, item.getPraiser().getAvatar());

        if(item.getPraiser().getId() == Auth.getCurrentUserId())
        {
            mHolder.tv_delete.setVisibility(View.VISIBLE);
        }
        else
        {
            mHolder.tv_delete.setVisibility(View.GONE);
        }

        mHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提示").setMessage("是否确认删除").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("FriendEvalId", String.valueOf(item.getId()));
                        Http.request((Activity)context, API.DELETE_FRIENDEVALUATION, new Object[]{Http.getURL(map)},

                                new Http.RequestListener<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        super.onSuccess(result);

                                        friendEvaluationDetails.remove(position);
                                        notifyDataSetChanged();
//                                        Toast.makeText(context, "已屏蔽该好友", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

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
        private TextView tv_name,tv_evaluation,tv_time,tv_delete;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}
