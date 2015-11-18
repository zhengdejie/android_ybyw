package appframe.appframe.widget.sortlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.ContactDetail;

/**
 * Created by Administrator on 2015/11/17.
 */
public class ContactAdapter extends BaseAdapter implements SectionIndexer {
    private List<ContactDetail> list = null;
    private Context mContext;

    public ContactAdapter(Context mContext, List<ContactDetail> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * ��ListView���ݷ����仯ʱ,���ô˷���������ListView
     * @param list
     */
    public void updateListView(List<ContactDetail> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final ContactDetail mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.mycontact_item, null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //����position��ȡ���������ĸ��Char asciiֵ
        int section = getSectionForPosition(position);

        //�����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        StringBuilder sb = new StringBuilder();
        if(list.get(position).getMobileContact() != null)
        {
            sb.append(list.get(position).getMobileContact().getName());

        }
        if(list.get(position).getUser() != null)
        {
            sb.append("(").append(list.get(position).getUser().getName()).append(")");
        }

        viewHolder.tv_name.setText(sb.toString());
        if(this.list.get(position).getType() == 1)
        {
            viewHolder.tv_remark.setText("一度好友");
        }
        else if(this.list.get(position).getType() == 2)
        {
            viewHolder.tv_remark.setText("已加入友帮");
        }
        else
        {
            viewHolder.tv_remark.setText("未加入友帮");
        }
        return view;

    }



    final static class ViewHolder {
        TextView tvLetter;
        TextView tv_name;
        TextView tv_remark;
    }


    /**
     * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
