package appframe.appframe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import appframe.appframe.R;
import appframe.appframe.activity.BaseActivity;

/**
 * Created by dashi on 15/6/21.
 */
public class BaseFragment extends Fragment {

    public static MenuItem createMenuItem(Menu menu, int code, String text){
        return BaseActivity.createMenuItem(menu, code, text);
    }
    public void createOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_add,menu);
        MenuItem addItem = menu.findItem(R.id.action_add);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        return false;
    }

    // fragment 默认再创建时就加载view，有时这太慢了，使用一下方法来lazyload fragment 的 view 和数据

    private boolean loadDataCalled;
    protected boolean viewVisible;
    public boolean isViewVisible(){
        return viewVisible;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadDataCalled = false;
        View result = onLoadView(inflater, container, savedInstanceState);
        if (!loadDataCalled && viewVisible) {
            loadDataCalled = true;
            onLoadData();
        }
        return result;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        viewVisible = isVisibleToUser;
        if(isVisibleToUser){
            if(getView() == null){
            }else{
                if(!loadDataCalled){
                    loadDataCalled = true;
                    onLoadData();
                }
            }

        }
    }

    protected View onLoadView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        return null;
    }
    /*
     * 只在view可见时载入data
     * http://stackoverflow.com/questions/24161160/setuservisiblehint-called-before-oncreateview-in-fragment
     */
    protected void onLoadData(){

    }
}
