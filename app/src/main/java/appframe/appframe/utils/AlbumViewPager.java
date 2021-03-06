package appframe.appframe.utils;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import static appframe.appframe.utils.Auth.app;


/**
 * Created by Administrator on 2016-11-04.
 */

public class AlbumViewPager extends ViewPager implements ZoomImageView.OnMovingListener {
    /**  当前子控件是否处理拖动状态  */
    private boolean mChildIsBeingDragged=false;

    public AlbumViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(mChildIsBeingDragged)
            return false;
        return super.onInterceptTouchEvent(arg0);
    }
    @Override
    public void startDrag() {
        // TODO Auto-generated method stub
        mChildIsBeingDragged=true;
    }


    @Override
    public void stopDrag() {
        // TODO Auto-generated method stub
        mChildIsBeingDragged=false;
    }
}
