package appframe.appframe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import appframe.appframe.R;

/**
 * Created by Administrator on 2017/5/9.
 */

public class XCTSFragment extends BaseFragment {
    public View onLoadView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root;
        root  = inflater.inflate(R.layout.fragment_xcts, null);

        return root;
    }
}
