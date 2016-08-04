package appframe.appframe.widget.sortlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.dto.ContactDetail;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2015/11/25.
 */
public class FirstClassAdapter extends BaseAdapter implements SectionIndexer {
    private List<ContactDetail> list = null;
    private Context mContext;

    public FirstClassAdapter(Context mContext, List<ContactDetail> list) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.firstclassfriends_item, null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.cb_firstclass = (CheckBox) view.findViewById(R.id.cb_firstclass);
            viewHolder.iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            viewHolder.niv_avatar = (com.android.volley.toolbox.NetworkImageView) view.findViewById(R.id.niv_avatar);
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
        if(mContent.getUser() != null)
        {
            sb.append(mContent.getUser().getName());
        }
        if(list.get(position).getMobileContact() != null)
        {
            sb.append("(").append(mContent.getMobileContact().getName()).append(")");
        }

        viewHolder.iv_avatar.setVisibility(View.VISIBLE);
        viewHolder.niv_avatar.setVisibility(View.INVISIBLE);
        if(mContent.getUser().getGender().equals(mContext.getResources().getString(R.string.male).toString()))
        {
            viewHolder.iv_avatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.maleavatar));

        }
        else
        {
            viewHolder.iv_avatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.femaleavatar));

        }

        if(mContent.getUser().getAvatar() != null && !mContent.getUser().getAvatar().equals(""))
        {
            viewHolder.iv_avatar.setVisibility(View.INVISIBLE);
            viewHolder.niv_avatar.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.iv_avatar.setVisibility(View.VISIBLE);
            viewHolder.niv_avatar.setVisibility(View.INVISIBLE);
        }
        ImageUtils.setImageUrl(viewHolder.niv_avatar, mContent.getUser().getAvatar());

        viewHolder.tv_name.setText(sb.toString());
        viewHolder.cb_firstclass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mContent.setCheck("Checked");
                } else {
                    mContent.setCheck("UnCheck");
                }

            }
        });
        return view;

    }



    final static class ViewHolder {
        TextView tvLetter;
        TextView tv_name;
        CheckBox cb_firstclass;
        ImageView iv_avatar;
        com.android.volley.toolbox.NetworkImageView niv_avatar;
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

