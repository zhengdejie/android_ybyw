package appframe.appframe.widget.HorizontalScrollView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2017/5/26.
 */

public class HorizontalScrollViewAdapter
{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<String> mDatas)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount()
    {
        return mDatas.size();
    }

    public Object getItem(int position)
    {
        return mDatas.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.activity_index_gallery_item, parent, false);
            viewHolder.mImg = (com.android.volley.toolbox.NetworkImageView) convertView
                    .findViewById(R.id.id_index_gallery_item_image);
//            viewHolder.mText = (TextView) convertView
//                    .findViewById(R.id.id_index_gallery_item_text);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String item = mDatas.get(position);
//        viewHolder.mImg.setImageResource(mDatas.get(position));
        ImageUtils.setImageUrl(viewHolder.mImg, item);
//        viewHolder.mText.setText("some info ");

        return convertView;
    }

    private class ViewHolder
    {
//        ImageView mImg;
        com.android.volley.toolbox.NetworkImageView mImg;
//        TextView mText;
    }

}
