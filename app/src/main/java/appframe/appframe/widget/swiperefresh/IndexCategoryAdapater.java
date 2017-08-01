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

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int[] imagID = new int[]{
                R.drawable.icon_share,
                R.drawable.icon_co,
                R.drawable.icon_travel,
                R.drawable.icon_activity,
                R.drawable.icon_second,
                R.drawable.icon_art,
                R.drawable.icon_work,
                R.drawable.icon_fix,
                R.drawable.icon_life,
                R.drawable.icon_find
        };
        holder.iv_category.setImageResource(imagID[position]);
        holder.categoryName.setText(list.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });


        }

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
