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
public class OrderProvider extends ActionProvider {
    Context context;
    public OrderProvider(Context context){
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
        subMenu.add("查看帮友资料").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                context.startActivity(new Intent(context, FriendsInfoActivity.class));
                return false;
            }
        });
        subMenu.add("收藏本单").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //context.startActivity(new Intent(context, OrderSendActivity.class));
                return false;
            }
        });
        subMenu.add("置顶本单").setIcon(R.drawable.ic_search_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        subMenu.add("举报本单").setIcon(R.drawable.ic_search_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
