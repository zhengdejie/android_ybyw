package appframe.appframe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/8/7.
 */
public class EstimateFragment extends BaseFragment{
    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_estimate, null);
        return root;
    }
}
