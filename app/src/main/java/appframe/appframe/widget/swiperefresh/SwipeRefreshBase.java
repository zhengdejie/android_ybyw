package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * A FrameLayout to wrap SwipeRefresh parent
 * */
public abstract class SwipeRefreshBase<T extends View> extends FrameLayout {


    private VerticalSwipeRefreshLayout mSwipeParent;
    private T mRefreshableView;
    private OnRefreshListener mListener;

    public SwipeRefreshBase(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    public SwipeRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public SwipeRefreshBase(Context context) {
        super(context);

        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        //add swipe parent to myself
        mSwipeParent = new VerticalSwipeRefreshLayout(context, attrs);
        addView(mSwipeParent, -1, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mSwipeParent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if(mListener != null)
                    mListener.onRefresh();
            }
        });
        mSwipeParent.setProgressViewOffset(true, 0, 100);

        mRefreshableView = createRefreshableView();

        if(mRefreshableView != null)
            addRefreshableView(mRefreshableView);
    }
    public OnRefreshListener getOnRefreshListener(){
    	return mListener;
    }
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public void setRefreshing(boolean refresh) {
        mSwipeParent.setRefreshing(refresh);
    }

    /**
     * do refreshing action
     * 
     * @param delayMillis The time to delay
     * */
    public void doRefreshing(long delayMillis) {
        if(mListener != null) {
            setRefreshing(true);
            if(delayMillis > 0)
                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListener.onRefresh();
                    }
                }, delayMillis);
            else
                mListener.onRefresh();
        }
    }

    protected abstract T createRefreshableView();

    public T getRefreshableView() {
        return mRefreshableView;
    }

    // add child view to swipe parent
    private void addRefreshableView(View view) {
        mSwipeParent.addView(view, -1, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /////////////////////////Listener////////////////////////////
    public interface OnRefreshListener {
        public void onRefresh();
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

}
