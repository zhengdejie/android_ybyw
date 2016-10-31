package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2016-08-09.
 */
public class GridViewBigPictureAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<String> photoPath;

    public GridViewBigPictureAdapater(Context context,List<String> photoPath)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.photoPath = photoPath;
    }
    @Override
    public int getCount() {
        if(photoPath == null)
        {
            return 0;
        }
        return photoPath.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;

        if (convertView == null)
        {
            convertView = layoutInflater
                    .inflate(R.layout.gridview_item_bigpicture, null);
            mHolder = new ViewHolder();
            mHolder.iv_avatar = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.iv_avatar);

            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final String item = photoPath.get(position);

        ImageUtils.setImageUrl(mHolder.iv_avatar, item);

        return convertView;
    }



    @Override
    public Object getItem(int position) {
        return photoPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private com.android.volley.toolbox.NetworkImageView iv_avatar;
    }
}
