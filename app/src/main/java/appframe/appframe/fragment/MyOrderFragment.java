package appframe.appframe.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.snowdream.android.util.Log;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/8/8.
 */
public class MyOrderFragment extends BaseFragment implements View.OnClickListener{
    ListView proListView;
    TextView tv_require,tv_recommand,tv_back,tv_action,tab_progess,tab_done,tab_close;
    View root,bottomLine_progress,bottomLine_done,bottomLine_close;
    List<OrderDetails> listPro = new ArrayList<OrderDetails>();
    List<OrderDetails> listDone = new ArrayList<OrderDetails>();
    List<OrderDetails> listClose = new ArrayList<OrderDetails>();
    LinearLayout tabtop;

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
        tv_back = (TextView) root.findViewById(R.id.tv_back);
        tv_require = (TextView) root.findViewById(R.id.tv_require);
        tv_recommand = (TextView) root.findViewById(R.id.tv_recommand);
        tv_action = (TextView) root.findViewById(R.id.tv_action);
        bottomLine_progress = (View) root.findViewById(R.id.bottomLine_progress);
        bottomLine_done = (View) root.findViewById(R.id.bottomLine_done);
        bottomLine_close = (View) root.findViewById(R.id.bottomLine_close);
        tabtop = (LinearLayout) root.findViewById(R.id.tabtop);

        tv_require.setText("订单");
        tv_recommand.setText("任务");
        tv_back.setVisibility(View.GONE);
        tv_action.setVisibility(View.GONE);

        tab_progess.setOnClickListener(this);
        tab_done.setOnClickListener(this);
        tab_close.setOnClickListener(this);
        tv_require.setOnClickListener(this);
        tab_progess.performClick();
        tv_recommand.setOnClickListener(this);
        proListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), OrderDetailsActivity.class);
                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                bundle.putString("From", "MyOrder");
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

//        Http.request(getActivity(), API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
//            @Override
//            public void onSuccess(List<OrderDetails> result) {
//                super.onSuccess(result);
//
//                List<OrderDetails> listPro = new ArrayList<OrderDetails>();
//                List<OrderDetails> listClose = new ArrayList<OrderDetails>();
//
//                for(OrderDetails od : result)
//                {
//                    if(od.getOrderStatus().equals("进行中"))
//                    {
//                        listPro.add(od);
//                    }
//                    else
//                    {
//                        listClose.add(od);
//                    }
//                }
//                proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listPro, AppConfig.ORDERSTATUS_CLOSE));
//                closeListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listClose, AppConfig.ORDERSTATUS_CLOSE));
//
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_require:
                tv_require.setBackgroundColor(getResources().getColor(R.color.green));
                tv_recommand.setBackgroundColor(Color.WHITE);
                tabtop.setVisibility(View.VISIBLE);
                tab_progess.performClick();
//                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
//                    @Override
//                    public void onSuccess(List<ConfirmedOrderDetail> result) {
//                        super.onSuccess(result);
//                        List<OrderDetails> listPro = new ArrayList<OrderDetails>();
//                        List<OrderDetails> listClose = new ArrayList<OrderDetails>();
//                        if( result != null ) {
//                            for (ConfirmedOrderDetail od : result) {
//                                if (od.getOrder().getOrderStatus().equals("进行中")) {
//                                    listPro.add(od.getOrder());
//                                } else {
//                                    listClose.add(od.getOrder());
//                                }
//                            }
//                        }
//                        proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listPro, AppConfig.ORDERSTATUS_CLOSE));
//                        //closeListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listClose, AppConfig.ORDERSTATUS_CLOSE));
//
//
//                    }
//                });
                break;
            case R.id.tv_recommand:
                tv_require.setBackgroundColor(Color.WHITE);
                tv_recommand.setBackgroundColor(getResources().getColor(R.color.green));
                tabtop.setVisibility(View.GONE);


                Http.request(getActivity(), API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(List<OrderDetails> result) {
                        super.onSuccess(result);

                        proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result, AppConfig.ORDERSTATUS_CLOSE));


                    }
                });
                break;
            case R.id.tab_progess:
                setTabPrgess(true);
                setTabDone(false);
                setTabClose(false);
                Http.request(getActivity(), API.GET_CONFIRMEDORDER, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
                    @Override
                    public void onSuccess(List<ConfirmedOrderDetail> result) {
                        super.onSuccess(result);

                        listPro.clear();
                        listDone.clear();
                        listClose.clear();
                        if (result != null) {
                            for (ConfirmedOrderDetail od : result) {
                                if (od.getStatus() == 1) {
                                    listPro.add(od.getOrder());
                                } else if(od.getStatus() == 2){
                                    listDone.add(od.getOrder());
                                }
                                else
                                {
                                    listClose.add(od.getOrder());
                                }
                            }
                        }
                        proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listPro, AppConfig.ORDERSTATUS_CLOSE));
                        //closeListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listClose, AppConfig.ORDERSTATUS_CLOSE));


                    }
                });
                break;
            case R.id.tab_done:
                setTabPrgess(false);
                setTabDone(true);
                setTabClose(false);
                proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listDone, AppConfig.ORDERSTATUS_CLOSE));
                break;
            case R.id.tab_close:
                setTabPrgess(false);
                setTabDone(false);
                setTabClose(true);
                proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), listClose, AppConfig.ORDERSTATUS_CLOSE));
                break;
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
