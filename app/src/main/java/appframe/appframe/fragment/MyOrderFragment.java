package appframe.appframe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.snowdream.android.util.Log;

import java.util.List;

import appframe.appframe.R;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/8/8.
 */
public class MyOrderFragment extends BaseFragment {
    ListView proListView,closeListView;
    TextView tb_title,tb_back;
    View root;
    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_myorder, null);
        init();
        return root;
    }


    public void init()
    {
        proListView = (ListView) root.findViewById(R.id.proListView);
        closeListView = (ListView) root.findViewById(R.id.closeListView);
        tb_back = (TextView) root.findViewById(R.id.tb_back);
        tb_title = (TextView) root.findViewById(R.id.tb_title);
        tb_title.setText("我的发单");
        tb_back.setVisibility(View.GONE);
    }

    @Override
    protected void onLoadData() {

        Http.request(getActivity(), API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
            @Override
            public void onSuccess(List<OrderDetails> result) {
                super.onSuccess(result);

                proListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result));
                proListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(), "df", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
                    }
                });

            }
        });
        Http.request(getActivity(), API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
            @Override
            public void onSuccess(List<OrderDetails> result) {
                super.onSuccess(result);

                closeListView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result));
                closeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(), "df", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
                    }
                });

            }
        });
    }
}
