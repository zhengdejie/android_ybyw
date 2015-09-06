package appframe.appframe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import appframe.appframe.R;
import appframe.appframe.widget.swiperefresh.SwipeRefreshListViewAdapater;

/**
 * Created by Administrator on 2015/8/8.
 */
public class MyOrderFragment extends BaseFragment {
    ListView proListView,closeListView;
    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_myorder, null);
        proListView = (ListView) root.findViewById(R.id.proListView);
        proListView.setAdapter(new SwipeRefreshListViewAdapater(getActivity()));
        closeListView = (ListView) root.findViewById(R.id.closeListView);
        closeListView.setAdapter(new SwipeRefreshListViewAdapater(getActivity()));
        return root;
    }
    
}
