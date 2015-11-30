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
import appframe.appframe.dto.OrderReviewDetail;

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
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final Nearby item = nearby.get(position);
        mHolder.tv_name.setText(item.getName());
        mHolder.tv_distance.setText(getDistance(item.getDistance()));
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
        else
        {
            return "2公里以内";
        }
    }

    @Override
    public Object getItem(int position) {
        return nearby.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_name;
        private TextView tv_distance;
    }
}
