package appframe.appframe.widget.swiperefresh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;

/**
 * Created by Administrator on 2017/4/28.
 */

public class IndexCategoryAdapater extends RecyclerView.Adapter<IndexCategoryAdapater.ViewHolder>{

    public List<String> list;
    public IndexCategoryAdapater(List<String> list)
    {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_indexcategory,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.iv_category.setImageResource(R.drawable.icon_activity);
        holder.categoryName.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public ImageView iv_category;
        public ViewHolder(View view){
            super(view);
            categoryName = (TextView)view.findViewById(R.id.tv_category);
            iv_category = (ImageView)view.findViewById(R.id.iv_category);
        }
    }
}
