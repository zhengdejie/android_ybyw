package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.CandidateActivity;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.ReportCategory;
import appframe.appframe.utils.Arith;
import appframe.appframe.utils.ImageUtils;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SwipeRefreshXReportAdapater extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    //    CheckBox cb_candidate;
    List<ReportCategory> reportCategories;
    HashMap<Integer, Boolean> isSelected;
//    TextView tv_content,tv_title,tv_name;

    public SwipeRefreshXReportAdapater(Context context,List<ReportCategory> reportCategories)
    {
        this.context =context;
        this.layoutInflater = LayoutInflater.from(context);
        this.reportCategories = reportCategories;
        unCheckAll();
        //initData();
    }

//    private void initData() {
//        for (UserDetail userDetail : userDetails) {
//            Type type = Type.UnCheck;
//            typeList.add(type);
//        }
//        cb_candidate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    typeList.set(position, Type.Checked);
//                } else {
//                    typeList.set(position, Type.UnCheck);
//                }
//
//            }
//        });
//    }

    @Override
    public int getCount() {
        if(reportCategories == null)
        {
            return 0;
        }
        return reportCategories.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.swiperefreshx_report, null);
            mHolder = new ViewHolder();
            mHolder.tv_reportname = (TextView) convertView.findViewById(R.id.tv_reportname);
            mHolder.cb_report = (CheckBox)convertView.findViewById(R.id.cb_report);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final ReportCategory item = reportCategories.get(position);


        mHolder.tv_reportname.setText(item.getCategoryName());

        mHolder.cb_report.setChecked(isSelected.get(position));
        mHolder.cb_report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    unCheckAllExceptTheone(position);
                    SwipeRefreshXReportAdapater.this.notifyDataSetChanged();

                    item.setCheck("Checked");

                } else {
                    item.setCheck("UnCheck");

                }

            }
        });

        return convertView;
    }

    public void unCheckAll()
    {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < reportCategories.size(); i++) {
            isSelected.put(i, false);
        }
    }

    public void unCheckAllExceptTheone(int position)
    {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < reportCategories.size(); i++) {
            if(i != position) {
                isSelected.put(i, false);
            }
            else
            {
                isSelected.put(i, true);
            }
        }
    }

    @Override
    public Object getItem(int position) {
        return reportCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder
    {
        private TextView tv_reportname;
        private CheckBox cb_report;
    }
}
