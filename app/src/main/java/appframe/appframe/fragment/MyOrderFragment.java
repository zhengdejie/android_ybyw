package appframe.appframe.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.snowdream.android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.activity.ConfirmOrderDetailsActivity;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.ConfirmedOrderUndoCount;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXConfirmedOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/8/8.
 */
public class MyOrderFragment extends BaseFragment implements View.OnClickListener{
    ListView proListView;
    TextView tb_title,tb_back,tv_apply,tv_progess,tv_done;
    public static TextView tab_progess,tab_done,tab_close,tab_apply;
    View root,bottomLine_progress,bottomLine_done,bottomLine_close,bottomLine_apply;
    LinearLayout tabtop;
    LinearLayout progress_bar;
//    boolean require_selected =true;

    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_myorder, null);
        init();
        return root;
    }


    public void init()
    {
        proListView = (ListView) root.findViewById(R.id.proListView);
        tab_progess = (TextView) root.findViewById(R.id.tab_progess);
        tab_done = (TextView) root.findViewById(R.id.tab_done);
        tab_close = (TextView) root.findViewById(R.id.tab_close);
        tab_apply = (TextView) root.findViewById(R.id.tab_apply);
        tb_back = (TextView) root.findViewById(R.id.tb_back);
        tb_title = (TextView) root.findViewById(R.id.tb_title);
        tv_apply = (TextView) root.findViewById(R.id.tv_apply);
        tv_progess = (TextView) root.findViewById(R.id.tv_progess);
        tv_done = (TextView) root.findViewById(R.id.tv_done);
//        tv_recommand = (TextView) root.findViewById(R.id.tv_recommand);
//        tv_action = (TextView) root.findViewById(R.id.tv_action);
        bottomLine_progress = (View) root.findViewById(R.id.bottomLine_progress);
        bottomLine_done = (View) root.findViewById(R.id.bottomLine_done);
        bottomLine_close = (View) root.findViewById(R.id.bottomLine_close);
        bottomLine_apply = (View) root.findViewById(R.id.bottomLine_apply);
        tabtop = (LinearLayout) root.findViewById(R.id.tabtop);
        progress_bar = (LinearLayout)root.findViewById(R.id.progress_bar);

        tb_title.setText("订单");
//        tv_recommand.setText("任务");
        tb_back.setVisibility(View.GONE);
//        tv_action.setVisibility(View.GONE);

//        tv_require.setBackgroundResource(R.drawable.titlebar_order_left_selected);
//        tv_recommand.setBackgroundResource(R.drawable.titlebar_order_right_unselected);

        tab_apply.setOnClickListener(this);
        tab_progess.setOnClickListener(this);
        tab_done.setOnClickListener(this);
        tab_close.setOnClickListener(this);
//        tv_require.setOnClickListener(this);
//
//        tv_recommand.setOnClickListener(this);
        proListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ConfirmOrderDetailsActivity.class);
                ConfirmedOrderDetail orderDetails = (ConfirmedOrderDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ConfirmOrderDetails", orderDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        closeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), OrderDetailsActivity.class);
//                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("OrderDetails", orderDetails);
//                bundle.putString("From", "MyOrder");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onLoadData() {
        Http.request(getActivity(), API.GET_ORDER_NUM, new Http.RequestListener<ConfirmedOrderUndoCount>() {
            @Override
            public void onSuccess(ConfirmedOrderUndoCount result) {
                super.onSuccess(result);

                tv_apply.setText(String.valueOf(result.getPendingCount()));
                tv_progess.setText(String.valueOf(result.getOngoingCount()));
                tv_done.setText(String.valueOf(result.getUnReviewedCount()));

            }
        });

        tab_apply.performClick();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.tv_require:
//                tv_require.setBackgroundResource(R.drawable.titlebar_order_left_selected);
//                tv_recommand.setBackgroundResource(R.drawable.titlebar_order_right_unselected);
//                tabtop.setVisibility(View.VISIBLE);
//                require_selected = true;
//                tab_apply.performClick();
//
//                break;
//            case R.id.tv_recommand:
//                tv_require.setBackgroundResource(R.drawable.titlebar_order_left_unselected);
//                tv_recommand.setBackgroundResource(R.drawable.titlebar_order_right_selected);
//                tabtop.setVisibility(View.GONE);
//                proListView.setAdapter(null);
//                progress_bar.setVisibility(View.VISIBLE);
//                require_selected =false;
//
//                Http.request(getActivity(), API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
//                    @Override
//                    public void onSuccess(List<OrderDetails> result) {
//                        super.onSuccess(result);
//                        progress_bar.setVisibility(View.GONE);
//                        proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_DELETE));
//
//
//                    }
//
//                    @Override
//                    public void onFail(String code) {
//                        super.onFail(code);
//                        progress_bar.setVisibility(View.GONE);
//                    }
//                });
//
//                break;
            case R.id.tab_apply:
                setTabApply(true);
                setTabPrgess(false);
                setTabDone(false);
                setTabClose(false);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                Map<String, String> map_apply = new HashMap<String, String>();
                map_apply.put("Status", "3");
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map_apply)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        proListView.setAdapter(new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_APPLY));


                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.tab_progess:
                setTabApply(false);
                setTabPrgess(true);
                setTabDone(false);
                setTabClose(false);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                Map<String, String> map = new HashMap<String, String>();
                map.put("Status", "1");
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        proListView.setAdapter(new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_PROGRESS));


                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.tab_done:
                setTabApply(false);
                setTabPrgess(false);
                setTabDone(true);
                setTabClose(false);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                Map<String, String> map_done = new HashMap<String, String>();
                map_done.put("Status", "2");
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map_done)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        proListView.setAdapter(new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_DONE));


                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                break;
            case R.id.tab_close:
                setTabApply(false);
                setTabPrgess(false);
                setTabDone(false);
                setTabClose(true);
                proListView.setAdapter(null);
                progress_bar.setVisibility(View.VISIBLE);
                Map<String, String> map_close = new HashMap<String, String>();
                map_close.put("Status", "0");
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Object[]{Http.getURL(map_close)}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);
                        progress_bar.setVisibility(View.GONE);
                        proListView.setAdapter(new SwipeRefreshXConfirmedOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_CLOSE));


                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        progress_bar.setVisibility(View.GONE);
                    }
                });

                break;
        }

    }

    protected void setTabApply(boolean isSelected)
    {
        if(isSelected)
        {
            tab_apply.setTextColor(getResources().getColor(R.color.green));
            bottomLine_apply.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_apply.setTextColor(getResources().getColor(R.color.font_black_content));
            bottomLine_apply.setVisibility(View.INVISIBLE);

        }
    }

    protected void setTabPrgess(boolean isSelected)
    {
        if(isSelected)
        {
            tab_progess.setTextColor(getResources().getColor(R.color.green));
            bottomLine_progress.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_progess.setTextColor(getResources().getColor(R.color.font_black_content));
            bottomLine_progress.setVisibility(View.INVISIBLE);

        }
    }
    protected void setTabDone(boolean isSelected)
    {
        if(isSelected)
        {
            tab_done.setTextColor(getResources().getColor(R.color.green));
            bottomLine_done.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_done.setTextColor(getResources().getColor(R.color.font_black_content));
            bottomLine_done.setVisibility(View.INVISIBLE);

        }
    }
    protected void setTabClose(boolean isSelected)
    {
        if(isSelected)
        {
            tab_close.setTextColor(getResources().getColor(R.color.green));
            bottomLine_close.setVisibility(View.VISIBLE);

        }
        else
        {
            tab_close.setTextColor(getResources().getColor(R.color.font_black_content));
            bottomLine_close.setVisibility(View.INVISIBLE);

        }
    }

}
