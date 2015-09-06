package appframe.appframe.widget.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import appframe.appframe.utils.WebViewJavascriptBridge;


public class SwipeRefreshWebView extends SwipeRefreshBase<WebView> {

    public static final int SWIPE_MODE_DISABLE = -1;
    public static final int SWIPE_MODE_ENABLE = 1;

    public SwipeRefreshWebView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwipeRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRefreshWebView(Context context) {
        super(context);
    }
    
    protected void onInit(){
    	
    }

    @Override
    protected WebView createRefreshableView() {
        return new WebView(getContext());
    }

    private String mUrl;
    private WebViewJavascriptBridge mBridge;
    private int mMode = SWIPE_MODE_ENABLE;

    public String getUrl() {
        return mUrl;
    }

    public void setMode(int mode) {
        mMode = mode;

        setEnabled(mode == SWIPE_MODE_ENABLE);
        
        if(mode == SWIPE_MODE_ENABLE){
	        this.setOnRefreshListener(new OnRefreshListener() {
	        	@Override
	        	public void onRefresh() {
	        		if(mBridge != null && mUrl != null)
	        			mBridge.loadUrl(mUrl);
	              	}
			});
        }
    }

    public int getMode() {
        return mMode;
    }

    public void config(WebViewJavascriptBridge b, String url) {
        mUrl = url;
        mBridge = b;
        if (getMode() != SWIPE_MODE_DISABLE) {
            b.clearUrl();
            doRefreshing(10);
            return;
        }
        b.loadUrl(url);
    }

}
