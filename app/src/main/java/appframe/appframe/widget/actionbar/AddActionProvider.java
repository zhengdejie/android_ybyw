package appframe.appframe.widget.actionbar;

import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.SearchView;

import appframe.appframe.R;
import appframe.appframe.activity.OrderSendActivity;

/**
 * Created by Administrator on 2015/8/5.
 */
public class AddActionProvider extends ActionProvider {

    Context context;
    public AddActionProvider(Context context){
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
        subMenu.add("扫一扫").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Intent intent = new Intent(context,CaptureActivity.this);
                return false;
            }
        });
        subMenu.add("发需求单").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.putExtra("demand","demand");
                intent.setClass(context, OrderSendActivity.class);
                context.startActivity(intent);
                return false;
            }
        });
        subMenu.add("发自荐单").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                intent.putExtra("self","self");
                intent.setClass(context, OrderSendActivity.class);
                context.startActivity(intent);
                return false;
            }
        });
        subMenu.add("搜索").setIcon(R.drawable.ic_search_black_24dp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {

                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {

                        return true;
                    }
                });
                return false;
            }
        });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
