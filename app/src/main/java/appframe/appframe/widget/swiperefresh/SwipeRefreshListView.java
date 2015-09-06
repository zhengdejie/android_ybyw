package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import appframe.appframe.R;


public class SwipeRefreshListView extends SwipeRefreshAdapterViewBase<ListView> {

    protected View mLoadMoreBar;
    protected ProgressBar mLoadMorePb;

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SwipeRefreshListView(Context context) {
        super(context);

        init();
    }

    private void init() {
        mLoadMoreBar = LayoutInflater.from(getContext()).inflate(
                R.layout.layout_loading_more, null);
        mLoadMorePb = (ProgressBar) mLoadMoreBar.findViewById(R.id.swipe_load_more_pb);
        createRefreshableView().setAdapter(new SwipeRefreshListViewAdapater(getContext()));
        getRefreshableView().setDivider(null);
    }

    @Override
    protected ListView createRefreshableView() {
        return new InternalListView(getContext());
    }

    @Override
    protected void showChildLoadMoreBar(boolean show) {
        if(show)
            mLoadMorePb.setVisibility(View.VISIBLE);
        else
            mLoadMorePb.setVisibility(View.GONE);
    }

    @Override
    protected void removeFooterView() {
        InternalListView lv = ((InternalListView)getRefreshableView());
        lv.removeFooterView();
    }

    @Override
    protected void addFooterView() {
        InternalListView lv = ((InternalListView)getRefreshableView());
        lv.addFooterView();
    }

    //////////////////////Internal ListView///////////////////////////////////
    /**
     * Intercept setAdapter() to add footer view for wrap ListView
     * */
    private class InternalListView extends ListView {

        private boolean mAddedFooterView = false;

        public InternalListView(Context context, AttributeSet attrs,
                int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public InternalListView(Context context) {
            super(context);
        }

        @Override
        public void setAdapter(ListAdapter adapter) {
            if(mHasLoadMore && mLoadMoreBar != null && !mAddedFooterView)
                addFooterView();

            super.setAdapter(adapter);

            if(adapter != null) {
                adapter.registerDataSetObserver(new DataSetObserver() {

                    @Override
                    public void onChanged() {
                        super.onChanged();

                        postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                if(mAddedFooterView) {
                                    if(getLastVisiblePosition() + 1 >= getAdapter().getCount()) {
                                        showLoadMoreBar(false);
                                    } else {
                                        showLoadMoreBar(true);
                                    }
                                }
                            }
                        }, getLastVisiblePosition() < 0 ? 10 : 0);
                    }
                });
            }
        }

        public void removeFooterView() {
            if(mAddedFooterView) {
                mAddedFooterView = false;

                removeFooterView(mLoadMoreBar);
            }
        }

        public void addFooterView() {
            if(!mAddedFooterView) {
                mAddedFooterView = true;

                addFooterView(mLoadMoreBar, null, false);
            }
        }
    }

}
