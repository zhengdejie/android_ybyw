package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.OrderCategory;

/**
 * Created by Administrator on 2016/3/23.
 */
public class CategoryAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<OrderCategory> orderCategories;

    public CategoryAdapater(Context context, List<OrderCategory> orderCategories)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.orderCategories = orderCategories;
    }
    @Override
    public int getCount() {
        if(orderCategories == null)
        {
            return 0;
        }
        return orderCategories.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_ordercategory, null);
            mHolder = new ViewHolder();
            mHolder.tv_category = (TextView) convertView.findViewById(R.id.tv_category);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        final OrderCategory item = orderCategories.get(position);
        mHolder.tv_category.setText(item.getCategoryName());

        return convertView;

    }

    @Override
    public Object getItem(int position) {
        return orderCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_category;
//        private com.android.volley.toolbox.NetworkImageView iv_avatar;
    }
}


