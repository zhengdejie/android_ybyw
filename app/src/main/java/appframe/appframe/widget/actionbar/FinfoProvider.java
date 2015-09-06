package appframe.appframe.widget.actionbar;

import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import appframe.appframe.R;
import appframe.appframe.activity.FriendsInfoActivity;
import appframe.appframe.activity.OrderSendActivity;

/**
 * Created by Administrator on 2015/8/14.
 */
public class FinfoProvider extends ActionProvider {
    Context context;
    public FinfoProvider(Context context){
        super(context);
        this.context = context;
    }


    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add("添加TA为一度好友").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("查看我和他关系网").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("加入黑名单").setIcon(R.drawable.ic_search_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("举报").setIcon(R.drawable.ic_search_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
