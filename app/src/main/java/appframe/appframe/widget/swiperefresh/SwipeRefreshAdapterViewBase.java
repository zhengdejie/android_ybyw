package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * SwipeRefreshAdapterViewBase
 * */
public abstract class SwipeRefreshAdapterViewBase<T extends AbsListView> extends SwipeRefreshBase<T>
        implements AbsListView.OnScrollListener{

    private OnLoadMoreListener mListener;
    private OnScrollListener mChildOnScrollListener;

    protected boolean mHasLoadMore = false;
    private int mLastVisibleItem = 0;

    public SwipeRefreshAdapterViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SwipeRefreshAdapterViewBase(Context context) {
        super(context);

        init();
    }

    private void init() {
        // set a new OnScrollListener for refreshable view
        getRefreshableView().setOnScrollListener(this);
    }

    /**
     * set load more mode, if true, OnLoadMoreListener will be call
     * 
     * @param loadMore True has load more bar, false not
     * */
    public void setHasLoadMore(boolean loadMore) {
        showLoadMoreBar(loadMore);
        mHasLoadMore = loadMore;
    }

    /**
     * show or hide load more bar
     * 
     * @param show True show or false hide
     * */
    public void showLoadMoreBar(boolean show) {
        if(show) {
            addFooterView();
            showChildLoadMoreBar(true);
        } else {
            showChildLoadMoreBar(false);
            removeFooterView();
        }
    }

    protected abstract void showChildLoadMoreBar(boolean show);
    protected abstract void removeFooterView();
    protected abstract void addFooterView();

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * if you want use AbsListView.OnScrollListener, set here
     * */
    public void setOnScrollListener(OnScrollListener listener) {
        mChildOnScrollListener = listener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        mLastVisibleItem = firstVisibleItem + visibleItemCount - 1;

        if(mChildOnScrollListener != null)
            mChildOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mHasLoadMore && scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && mLastVisibleItem == view.getCount() - 1
                && mListener != null)
            mListener.onLoadMore();

        if(mChildOnScrollListener != null)
            mChildOnScrollListener.onScrollStateChanged(view, scrollState);
    }

}
