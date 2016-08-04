package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.BaseActivity;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/10/22.
 */
public class SwipeRefreshXNearbyAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Nearby> nearby;

    public SwipeRefreshXNearbyAdapater(Context context,List<Nearby> nearby)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.nearby = nearby;
    }
    @Override
    public int getCount() {
        if(nearby == null)
        {
            return 0;
        }
        return nearby.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.swiperefreshx_nearby, null);
            mHolder = new ViewHolder();
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            mHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.niv_avatar);
            mHolder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final Nearby item = nearby.get(position);
        mHolder.tv_name.setText(item.getName());
        mHolder.tv_distance.setText(getDistance(item.getDistance()));

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

    public String getDistance(double distance)
    {
        if(distance * 1000 <= 100)
        {
            return "100米以内";
        }
        else if(100 < distance * 1000 && distance * 1000 <= 200)
        {
            return "200米以内";
        }
        else if(200 < distance * 1000 && distance * 1000 <= 300)
        {
            return "300米以内";
        }
        else if(300 < distance * 1000 && distance * 1000 <= 400)
        {
            return "400米以内";
        }
        else if(400 < distance * 1000 && distance * 1000 <= 500)
        {
            return "500米以内";
        }
        else if(500 < distance * 1000 && distance * 1000 <= 600)
        {
            return "600米以内";
        }
        else if(600 < distance * 1000 && distance * 1000 <= 700)
        {
            return "700米以内";
        }
        else if(700 < distance * 1000 && distance * 1000 <= 800)
        {
            return "800米以内";
        }
        else if(800 < distance * 1000 && distance * 1000 <= 900)
        {
            return "900米以内";
        }
        else if(900 < distance * 1000 && distance * 1000 <= 1000)
        {
            return "1000米以内";
        }
        else if(1000 < distance * 1000 && distance * 1000 <= 2000)
        {
            return "2公里以内";
        }
        else
        {
            return "2公里以外";
        }
    }

    @Override
    public Object getItem(int position) {
        return nearby.get(position);
    }

    public  void addItems(List<Nearby> items ){
        for(Nearby item : items)
        {
            nearby.add(item);
        }
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name;
        private TextView tv_distance;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private ImageView iv_avatar;
    }
}
