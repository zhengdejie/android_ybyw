package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import appframe.appframe.activity.HomeActivity;
import appframe.appframe.activity.RegisterActivity;
import appframe.appframe.activity.ViewPagerActivity;
import appframe.appframe.utils.Auth;

/**
 * Created by Administrator on 2016-06-17.
 */

public class ViewPagerAdapter extends PagerAdapter {

    //界面列表
    private List<View> views;
    Context context;

    public ViewPagerAdapter (Context context,List<View> views){
        this.context = context;
        this.views = views;
    }

//    //销毁arg1位置的界面
//    @Override
//    public void destroyItem(View arg0, int arg1, Object arg2) {
//        ((ViewPager) arg0).removeView(views.get(arg1));
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        ((ViewPager) container).removeView(views.get(position));
    }



    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    //获得当前界面数
    @Override
    public int getCount() {
        if (views != null)
        {
            return views.size();
        }

        return 0;
    }


//    //初始化arg1位置的界面
//    @Override
//    public Object instantiateItem(View arg0, int arg1) {
//
//        ((ViewPager) arg0).addView(views.get(arg1), 0);
//
//        return views.get(arg1);
//    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        return super.instantiateItem(container, position);
        ((ViewPager) container).addView(views.get(position), 0);
//        views.get(position).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(position == 3) {
//                    SharedPreferences.Editor e = context.getSharedPreferences("firstviewpager", Context.MODE_PRIVATE).edit();
//                    e.putString("viewed", "true").commit();
//                    if (Auth.isLoggedIn()) {
//                        Intent i = new Intent(context, HomeActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(i);
//                    } else {
//
//                        Intent i = new Intent(context, RegisterActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(i);
//                    }
//                }
//
//            }
//        });

        return views.get(position);
    }

    //判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }
}
