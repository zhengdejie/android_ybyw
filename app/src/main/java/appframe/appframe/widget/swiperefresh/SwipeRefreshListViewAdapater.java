package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/8/7.
 */
public class SwipeRefreshListViewAdapater extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    public SwipeRefreshListViewAdapater(Context context)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.swiperefreshlistview_adapter, null);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View makeItemView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater.inflate(R.layout.swiperefreshlistview_adapter, null);


        return itemView;
    }
}
