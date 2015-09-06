package appframe.appframe.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.snowdream.android.util.Log;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.fragment.WebViewFragment;
import appframe.appframe.widget.swiperefresh.SwipeRefreshWebView;
import appframe.appframe.utils.Utils;
import appframe.appframe.utils.WebViewCommonHandlers;
import appframe.appframe.utils.WebViewJavascriptBridge;


public class FramedWebViewActivity extends BaseFrameActivity implements WebViewCommonHandlers.WebViewPage {


    class TabsAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

        public TabsAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return tabs.get(position).fragment;
        }

        @Override
        public int getIconResId(int position) { return tabs.get(position).iconResId; }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).name;
        }
    }
    static class NonSwitchableTab{
        String name;
        int iconResId;
        int iconSelectedResId;
        Uri uri;
        NonSwitchableTab(Uri u){
            uri = u;
        }
    }
    public static class FramedWebViewTab implements WebViewCommonHandlers.WebViewTab {
        String name;
        String title;
        int iconResId;
        int iconSelectedResId;
        FramedWebViewActivity page;
        WebViewFragment fragment;
        WebViewJavascriptBridge webViewJavascriptBridge;
        Uri uri;
        public FramedWebViewTab(FramedWebViewActivity page, Uri uri, WebViewFragment fragment){
            this.page = page;
            this.uri = uri;
            this.fragment = fragment;
            if(this.fragment == null){
                // create new webView
                this.fragment = new WebViewFragment();
                this.fragment.loadView(page);
            }
            WebViewCommonHandlers.onTabCreate(this);

        }
        List<WebViewCommonHandlers.WebViewActionButtonDto> actionButtons = new ArrayList<WebViewCommonHandlers.WebViewActionButtonDto>();


        boolean _hasRefreshed = false;
        public boolean hasRefreshed(){
            return _hasRefreshed;
        }

        public WebViewCommonHandlers.WebViewPage getPage(){
            return page;
        }
        @Override
        public Activity getActivity() {
            return page;
        }

        @Override
        public void refreshWebView() {
            _hasRefreshed = true;

            fragment.webView.config(getWebViewJavascriptBridge(), getWebViewUri().toString());
        }

        @Override
        public Uri getWebViewUri() {
            return uri;
        }

        @Override
        public SwipeRefreshWebView getWebView() {
            return fragment.webView;
        }

        @Override
        public WebViewJavascriptBridge getWebViewJavascriptBridge() {
            if (webViewJavascriptBridge == null){

                webViewJavascriptBridge = new WebViewJavascriptBridge(this, fragment.webView.getRefreshableView(),
                        new WebChromeClient(){
                            @Override
                            public boolean onConsoleMessage(ConsoleMessage cm) {
                                if(cm != null)
                                    Log.i(cm.message());
                                return false;
                            }

                            @Override
                            public boolean onJsAlert(WebView view, String url, String message,
                                                     JsResult result) {

                                Toast.makeText(page, message, Toast.LENGTH_SHORT).show();
                                result.confirm();
                                return true;
                            }
                            @Override
                            public void onReceivedTitle(WebView view, String title) {
                                FramedWebViewTab tab = (FramedWebViewTab)page.getCurrentTab();
                                if(tab.getWebView().getRefreshableView() == view){
                                    tab.title = title;
                                    page.refreshTitle();
                                }

                            }
                            @Override
                            public void onProgressChanged(WebView view, int newProgress) {
                                super.onProgressChanged(view, newProgress);
                                if(newProgress >= 100){
                                    fragment.webView.setRefreshing(false);
                                }
                            }
                        });
                WebViewCommonHandlers.registerCommonHandles(page, webViewJavascriptBridge);
            }
            return webViewJavascriptBridge;
        }

        @Override
        public List<WebViewCommonHandlers.WebViewActionButtonDto> getActionButtons() {
            return actionButtons;
        }
    }
    Uri uri;


    boolean isTab;
    int currentTab;
    List<FramedWebViewTab> tabs;
    List<NonSwitchableTab> nonSwitchableTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uri = getIntent().getData();
        if(uri == null){
            finish();
        }

        initTabs();


    }

    @Override
    public void finish() {
        WebViewCommonHandlers.beforeFinishWebView(this);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for(WebViewCommonHandlers.WebViewActionButtonDto ab : getCurrentTab().getActionButtons()){
            MenuItem mi = menu.add(Menu.NONE, ab.id, Menu.NONE, ab.title);
            mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        WebViewCommonHandlers.onWebViewActionButtonClicked(this, id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(WebViewCommonHandlers.onActivityResult(this, requestCode, resultCode, data)) return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        WebViewCommonHandlers.onStart(this);

    }

    // WebViewCommonHandlers.WebViewPage

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public int getCurrentTabIndex() {
        return currentTab;
    }

    @Override
    public WebViewCommonHandlers.WebViewTab getCurrentTab() {
        return tabs.get((currentTab + tabs.size()) % tabs.size());
    }

    @Override
    public WebViewCommonHandlers.WebViewTab getTabWithBridge(WebViewJavascriptBridge bridge){
        if(bridge == null) return null;
        for(int i=0;i<tabs.size();i++){
            if(tabs.get(i).getWebViewJavascriptBridge() == bridge)
                return tabs.get(i);
        }
        return null;
    }

    @Override
    public WebViewCommonHandlers.WebViewTab getTabWithWebView(SwipeRefreshWebView view){

        if(view == null) return null;
        for(int i=0;i<tabs.size();i++){
            if(tabs.get(i).getWebView() == view)
                return tabs.get(i);
        }
        return null;
    }

    @Override
    public void refreshActionButtons() {
        invalidateOptionsMenu();
    }

    @Override
    public void refreshTitle() {
        getActionBar().setTitle(((FramedWebViewTab)getCurrentTab()).title);
    }

    Intent mResultIntent;
    @Override
    public Intent getWebViewResultIntent() {
        if(mResultIntent == null)
            mResultIntent = new Intent();
        return mResultIntent;
    }

    public Uri getWebViewUri(){
        return uri;
    }

    // misc

    int iconNameToResId(String name){
        if(TextUtils.isEmpty(name)) return 0;
        return getResources().getIdentifier(name, "drawable", this.getPackageName());
    }



    void configTabs(String tabSchema){
        String [] tabArray = tabSchema.split(";");
        int j = 0, k = 0;
        for (int i=0;i<tabArray.length;i++){
            // TAB 格式
            // <URL>|<TITLE>|<IMAGE>|<SELECTED_IMAGE>
            String [] config = tabArray[i].split(",");
            Uri u = Uri.parse(config[0]);

            if(!u.getScheme().toLowerCase().startsWith("http")){
                // not switchable
                if(nonSwitchableTabs.size() <= k){
                    nonSwitchableTabs.add(new NonSwitchableTab(u));
                }else{
                    nonSwitchableTabs.get(k).uri = u;
                }
                NonSwitchableTab t = nonSwitchableTabs.get(k);

                if (config.length >= 2) {
                    t.name = config[1];
                    if (config.length > 2)
                        t.iconResId = iconNameToResId(config[2]);

                    if (config.length > 3)
                        t.iconSelectedResId = iconNameToResId(config[3]);
                }
                k ++;
                continue;
            }
            // tabs
            if (tabs.size() <= j){
                tabs.add(new FramedWebViewTab(this, u, null));

            }else{
                tabs.get(i).uri = u;
            }
            FramedWebViewTab tab = tabs.get(j);

            if (config.length >= 2) {
                tab.name = config[1];
                if (config.length > 2)
                    tab.iconResId = iconNameToResId(config[2]);

                if (config.length > 3)
                    tab.iconSelectedResId = iconNameToResId(config[3]);
            }

            j ++;
        }

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = pager.getAdapter();
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
    int getLayoutResId(){
        String layout = Utils.getStringConfig(uri, "wf-layout");
        if(layout != null){
            int r = getResources().getIdentifier("activity_" + layout.toLowerCase(), "layout", this.getPackageName());
            return r;
        }
        return  isTab ? R.layout.activity_framed_web_view_tab : R.layout.activity_framed_web_view_single;
    }
    void initTabs(){
        String tabsScheme = Utils.getStringConfig(uri, "wf-tabs");

        tabs = new ArrayList<>();
        nonSwitchableTabs = new ArrayList<>();

        if(tabsScheme == null){
            setContentView(getLayoutResId());
            // no-tabs
            isTab = false;
            currentTab = 0;

            tabs.add(new FramedWebViewTab(this, uri, (WebViewFragment)getSupportFragmentManager().findFragmentById(R.id.fragment)));

            return;
        }

        isTab = true;
        setContentView(getLayoutResId());


        configTabs(tabsScheme);

        /*
        View v = findViewById(R.id.tabTrigger);
        if(v != null){
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(nonSwitchableTabs.size() == 0) return;
                    UriHandler.handleUrl(FramedWebViewActivity.this, nonSwitchableTabs.get(0).uri, false, 0);

                }
            });
        }
        */

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new TabsAdapter());

        PageIndicator tabs = (PageIndicator) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTab = position;

                WebViewCommonHandlers.WebViewTab tab = getCurrentTab();

                WebViewCommonHandlers.onTabSwitch(FramedWebViewActivity.this, tab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        currentTab = Utils.getIntegerConfig(uri, "wf-tab-default", 0) % pager.getAdapter().getCount();
        pager.setCurrentItem(currentTab);
    }
}
