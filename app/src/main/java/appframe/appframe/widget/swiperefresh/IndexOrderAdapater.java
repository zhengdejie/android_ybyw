package appframe.appframe.widget.swiperefresh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.OrderDetails;

/**
 * Created by Administrator on 2017/6/6.
 */

public class IndexOrderAdapater extends RecyclerView.Adapter<IndexOrderAdapater.ViewHolder>{

    public List<OrderDetails> list;
    public IndexOrderAdapater(List<OrderDetails> list)
    {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_indexorder,parent,false);
        IndexOrderAdapater.ViewHolder vh = new IndexOrderAdapater.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(IndexOrderAdapater.ViewHolder mHolder, int position) {
        mHolder.txt_title.setText(list.get(position).getTitle());

//        holder.categoryName.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_title,txt_bounty,txt_type,txt_location,tv_name,tv_pay,tv_numofconforder,tv_content,tv_fans,tv_focus;
        appframe.appframe.widget.tagview.TagView tv_tags;
        private com.android.volley.toolbox.NetworkImageView niv_avatar;
        private RatingBar rb_totalvalue;
        private GridView gridView;
        private ImageView iv_gender,iv_avatar;

        public ViewHolder(View convertView){
            super(convertView);
            txt_title = (TextView)convertView.findViewById(R.id.txt_title);
            txt_bounty = (TextView)convertView.findViewById(R.id.txt_bounty);
            tv_content = (TextView)convertView.findViewById(R.id.tv_content);
            txt_type = (TextView)convertView.findViewById(R.id.txt_type);
            txt_location = (TextView)convertView.findViewById(R.id.txt_location);
            tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            niv_avatar = (com.android.volley.toolbox.NetworkImageView)convertView.findViewById(R.id.niv_avatar);
            iv_avatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
            rb_totalvalue = (RatingBar)convertView.findViewById(R.id.rb_totalvalue);
            tv_pay = (TextView)convertView.findViewById(R.id.tv_pay);
            tv_numofconforder = (TextView)convertView.findViewById(R.id.tv_numofconforder);
            gridView = (GridView)convertView.findViewById(R.id.gridview);
            iv_gender = (ImageView)convertView.findViewById(R.id.iv_gender);
            tv_tags = (appframe.appframe.widget.tagview.TagView)convertView.findViewById(R.id.tv_tags);
            tv_focus = (TextView)convertView.findViewById(R.id.tv_focus);
        }
    }
}
