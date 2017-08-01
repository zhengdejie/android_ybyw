package appframe.appframe.widget.slide.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/5/27.
 */

public class SlideRootFrameLayout extends FrameLayout {

    /**
     * Callbacks for TouchInterceptionFrameLayout.
     */
    public interface TouchInterceptionListener {
        /**
         * Determines whether the layout should intercept this event.
         *
         * @param ev     motion event
         * @param moving true if this event is ACTION_MOVE type
         * @param diffX  difference between previous X and current X, if moving is true
         * @param diffY  difference between previous Y and current Y, if moving is true
         * @return true if the layout should intercept
         */
        boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY);

        /**
         * Called if the down motion event is intercepted by this layout.
         *
         * @param ev motion event
         */
        void onDownMotionEvent(MotionEvent ev);

        /**
         * Called if the move motion event is intercepted by this layout.
         *
         * @param ev    motion event
         * @param diffX difference between previous X and current X
         * @param diffY difference between previous Y and current Y
         */
        void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY);

        /**
         * Called if the up (or cancel) motion event is intercepted by this layout.
         *
         * @param ev motion event
         */
        void onUpOrCancelMotionEvent(MotionEvent ev, boolean isIntercepting);
    }

    private boolean mIntercepting;
    private boolean mDownMotionEventPended;
    private boolean mBeganFromDownMotionEvent;
    private boolean mChildrenEventsCanceled;
    private PointF mInitialPoint;
    private MotionEvent mPendingDownMotionEvent;
    private TouchInterceptionListener mTouchInterceptionListener;

    public SlideRootFrameLayout(Context context) {
        super(context);
    }

    public SlideRootFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideRootFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setScrollInterceptionListener(TouchInterceptionListener listener) {
        mTouchInterceptionListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mTouchInterceptionListener == null) {
            return false;
        }

        // In here, we must initialize touch state variables
        // and ask if we should intercept this event.
        // Whether we should intercept or not is kept for the later event handling.
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mInitialPoint = new PointF(ev.getX(), ev.getY());
                mPendingDownMotionEvent = MotionEvent.obtainNoHistory(ev);
                mDownMotionEventPended = true;
                mIntercepting = mTouchInterceptionListener.shouldInterceptTouchEvent(ev, false, 0, 0);
                mBeganFromDownMotionEvent = mIntercepting;
                mChildrenEventsCanceled = false;
                return mIntercepting;
            case MotionEvent.ACTION_MOVE:
                // ACTION_MOVE will be passed suddenly, so initialize to avoid exception.
                if (mInitialPoint == null) {
                    mInitialPoint = new PointF(ev.getX(), ev.getY());
                }

                // diffX and diffY are the origin of the motion, and should be difference
                // from the position of the ACTION_DOWN event occurred.
                float diffX = ev.getX() - mInitialPoint.x;
                float diffY = ev.getY() - mInitialPoint.y;
                mIntercepting = mTouchInterceptionListener.shouldInterceptTouchEvent(ev, true, diffX, diffY);
                return mIntercepting;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mTouchInterceptionListener != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mInitialPoint = new PointF(ev.getX(), ev.getY());
                    MotionEvent event = MotionEvent.obtainNoHistory(mPendingDownMotionEvent);
                    event.setLocation(ev.getX(), ev.getY());
                    mTouchInterceptionListener.onDownMotionEvent(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float diffX = ev.getX() - mInitialPoint.x;
                    float diffY = ev.getY() - mInitialPoint.y;
                    mTouchInterceptionListener.onMoveMotionEvent(ev, diffX, diffY);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mBeganFromDownMotionEvent = false;
                    mTouchInterceptionListener.onUpOrCancelMotionEvent(ev,mIntercepting);

                    // Children's touches should be canceled regardless of
                    // whether or not this layout intercepted the consecutive motion events.
                    /*if (!mChildrenEventsCanceled) {
                        mChildrenEventsCanceled = true;
                        if (mDownMotionEventPended) {
                            mDownMotionEventPended = false;
                            MotionEvent event1 = MotionEvent.obtainNoHistory(mPendingDownMotionEvent);
                            event1.setLocation(ev.getX(), ev.getY());
                            duplicateTouchEventForChildren(ev, event1);
                        } else {
                            duplicateTouchEventForChildren(ev);
                        }
                    }*/
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private MotionEvent obtainMotionEvent(MotionEvent base, int action) {
        MotionEvent ev = MotionEvent.obtainNoHistory(base);
        ev.setAction(action);
        return ev;
    }

    /**
     * Duplicate touch events to child views.
     * We want to dispatch a down motion event and the move events to
     * child views, but calling dispatchTouchEvent() causes StackOverflowError.
     * Therefore we do it manually.
     *
     * @param ev            motion event to be passed to children
     * @param pendingEvents pending events like ACTION_DOWN. This will be passed to the children before ev
     */
    private void duplicateTouchEventForChildren(MotionEvent ev, MotionEvent... pendingEvents) {
        if (ev == null) {
            return;
        }
        for (int i = getChildCount() - 1; 0 <= i; i--) {
            View childView = getChildAt(i);
            if (childView != null) {
                Rect childRect = new Rect();
                childView.getHitRect(childRect);//获取view的点击范围
                MotionEvent event = MotionEvent.obtainNoHistory(ev);//获取这个事件的副本，不包含历史信息
                if (!childRect.contains((int) event.getX(), (int) event.getY())) {//点击范围是不是这个view
                    continue;
                }
                float offsetX = -childView.getLeft();
                float offsetY = -childView.getTop();
                boolean consumed = false;
                if (pendingEvents != null) {
                    for (MotionEvent pe : pendingEvents) {
                        if (pe != null) {
                            MotionEvent peAdjusted = MotionEvent.obtainNoHistory(pe);
                            peAdjusted.offsetLocation(offsetX, offsetY);
                            consumed |= childView.dispatchTouchEvent(peAdjusted);
                        }
                    }
                }
                event.offsetLocation(offsetX, offsetY);
                consumed |= childView.dispatchTouchEvent(event);
                if (consumed) {
                    break;
                }
            }
        }
    }
}
