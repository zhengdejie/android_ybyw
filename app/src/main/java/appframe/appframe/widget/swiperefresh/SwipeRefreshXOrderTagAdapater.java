package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.SearchOrderTagResponse;

/**
 * Created by Administrator on 2016/3/9.
 */
public class SwipeRefreshXOrderTagAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<String> tag;

    public SwipeRefreshXOrderTagAdapater(Context context, List<String> tag)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.tag = tag;
    }
    @Override
    public int getCount() {
        if(tag == null)
        {
            return 0;
        }
        return tag.size();
    }
    //此方法通过activity给view的item添加值。通过notifyDataSetChanged 刷新界面
    public  void addItems(List<String> items ){
        for(String order : items)
        {
            tag.add(order);
        }
        notifyDataSetChanged();

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_ordertag, null);
            mHolder = new ViewHolder();
            mHolder.tv_tagname = (TextView) convertView.findViewById(R.id.tv_tagname);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final String item = tag.get(position);
        mHolder.tv_tagname.setText(item);

        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return tag.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_tagname;
//        private com.android.volley.toolbox.NetworkImageView iv_avatar;
    }
}

