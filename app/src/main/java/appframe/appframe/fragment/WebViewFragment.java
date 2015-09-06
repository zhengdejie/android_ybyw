package appframe.appframe.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import appframe.appframe.widget.swiperefresh.SwipeRefreshWebView;

/**
 * Created by dashi on 15/6/21.
 */
public class WebViewFragment extends BaseFragment {
    public SwipeRefreshWebView webView;

    public void loadView(Context context){
        webView = new SwipeRefreshWebView(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(webView == null)
            loadView(inflater.getContext());
        return webView;
    }
}
