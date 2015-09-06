package appframe.appframe.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.github.snowdream.android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.BuildConfig;
import appframe.appframe.widget.swiperefresh.SwipeRefreshWebView;

/**
 * Created by dashi on 15/4/28.
 */
public class WebViewCommonHandlers {

    static final String TAG = "WebViewCommonHandlers";
    public static final int REQUEST_CODE_START_NEW_WEBVIEW = 0xa13;



    @SuppressWarnings("deprecation")
    public static void onTabCreate(WebViewTab tab){
        WebSettings ws = tab.getWebView().getRefreshableView().getSettings();
        ws.setUserAgentString(ws.getUserAgentString() + " WebFrame/" + getWebFrameVersion());
        ws.setSavePassword(false);

        if(Utils.getIntegerConfig(tab.getWebViewUri(), "wf-no-pull-to-refresh") != 0){
            tab.getWebView().setMode(SwipeRefreshWebView.SWIPE_MODE_DISABLE);
        }else{
            tab.getWebView().setMode(SwipeRefreshWebView.SWIPE_MODE_ENABLE);
        }
    }
    public static void onTabSwitch(WebViewPage page, WebViewTab tab){
        if (!tab.hasRefreshed()) {
            tab.refreshWebView();
        }
        page.refreshActionButtons();
        page.refreshTitle();
    }
    public static String getWebFrameVersion(){
        return String.format("Android,%s,%d", BuildConfig.APPLICATION_ID, BuildConfig.VERSION_CODE);
    }
    public static Map<String,String> getHeadersForWebViewRequest(){
        HashMap<String,String> result = new HashMap<>();

        result.put("WF", getWebFrameVersion());

        if(Auth.isLoggedIn()) result.put("Authorization", "AppFrame " + Auth.getToken());


        return result;
    }

    public static boolean isTrustedHost(String host){
        if(host == null) return false;
        host = host.toLowerCase();
        String [] ohs = Http.getOfficalHosts();
        if(ohs != null)
            for(String s : ohs){
                if(s == null) continue;
                String l = s.toLowerCase();
                if(host.equals(l) || host.endsWith("." + l)) return true;
            }
        return false;
    }
    public static boolean isTrustedDomain(Uri uri){
        if(uri == null) return false;
        return isTrustedHost(uri.getHost());
    }

    public static void webViewBeginLoad(WebViewPage webPage, WebViewJavascriptBridge bridge, String url){
        WebViewTab tab = webPage.getTabWithBridge(bridge);
        if(tab == null) return;
        // 清除自定义view
        if(tab.getActionButtons().size() > 0) {
            tab.getActionButtons().clear();
            webPage.refreshActionButtons();
        }
    }
    public static interface WebViewTab{
        public WebViewCommonHandlers.WebViewPage getPage();
        public Activity getActivity();
        public void refreshWebView();

        public Uri getWebViewUri();
        public SwipeRefreshWebView getWebView();
        public WebViewJavascriptBridge getWebViewJavascriptBridge();
        public List<WebViewActionButtonDto> getActionButtons();
        public boolean hasRefreshed();
    }
    public static interface WebViewPage {
        public Activity getActivity();
        public Intent getWebViewResultIntent();

        public int getCurrentTabIndex();
        public WebViewTab getCurrentTab();
        public WebViewTab getTabWithBridge(WebViewJavascriptBridge bridge);
        public WebViewTab getTabWithWebView(SwipeRefreshWebView view);
        public void refreshActionButtons();
        public void refreshTitle();
    }
    public static class WebViewActionButtonDto {

        public String title;
        public int id;
    }
    public static class WebViewSetConfigDto{
        public String key;
        public String value;

    }
    public static class WebViewRequestLocationDto{
        public float accuracy;
        public double age;
    }
    public static class WebViewScanCodeDto{

    }
    public static class WebViewActionSheetDto{
        public String title;
        public boolean cancelable;
        public WebViewActionSheetButtonDto[] buttons;
    }

    public static class WebViewAlertDto{
        public String title;
        public String message;
        public String buttonCancel;
        public WebViewActionSheetButtonDto[] buttons;
    }

    public static class WebViewInputDto{
        public String title;
        public String inputDefault;
        public boolean inputReadonly;
        public String inputType;
        public String inputPlaceholder;
        public int inputMultiline;
        public String message;
        public String buttonOK;
        public String buttonCancel;
    }
    public static class WebViewActionSheetButtonDto{
        public String id;
        public String title;
    }
    public static class WebViewRequestLocationResultDto{
        public long timestamp;
        public double verticalAccuracy;
        public double horizontalAccuracy;
        public double course;
        public double speed;
        public double longitude;
        public double latitude;
        public double altitude;
        public String error;
        public WebViewRequestLocationResultDto(Location l){
            timestamp = l.getTime();
            if(l.hasAccuracy())
                horizontalAccuracy = verticalAccuracy = l.getAccuracy();
            if(l.hasBearing())
                course = l.getBearing();
            if(l.hasSpeed())
                speed = l.getSpeed();
            if(l.hasAltitude())
                altitude = l.getAltitude();
            longitude = l.getLongitude();
            latitude = l.getLatitude();
        }
        public WebViewRequestLocationResultDto(String error){
            this.error = error;
        }
    }
    public static Handler mHandler = new Handler();
    static boolean isTrustedTab(WebViewTab t){
        return t != null && isTrustedDomain(Uri.parse(t.getWebView().getUrl()));
    }
    public static void registerCommonHandles(final WebViewPage webPage, final WebViewJavascriptBridge mWvJsBridge) {

        WebViewJavascriptBridge.WVJBHandler openUrlHandler = new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if(!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge))) return;
                Log.i(TAG, data);
                JsonElement idJE = getJsonElementFromCallbackString(data, "data");
                String url = idJE.getAsString();
                UriHandler.handleUrl(webPage.getActivity(), Uri.parse(url), true, 0);
            }
        };
        mWvJsBridge.registerHandler("openUrl", openUrlHandler);
        mWvJsBridge.registerHandler("setActionButton", new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if(!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge))) return;
                Log.i(TAG, data);
                final WebViewActionButtonDto button = getDataFromCallbackString(data, WebViewActionButtonDto.class);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        addActionButton(webPage, mWvJsBridge, button);
                    }

                });
            }
        });
        mWvJsBridge.registerHandler("removeActionButton", new WebViewJavascriptBridge.WVJBHandler(){
            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if(!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge))) return;
                Log.i(TAG, data);
                JsonElement idJE = getJsonElementFromCallbackString(data, "data");
                final int id = idJE.getAsInt();
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        removeActionButton(webPage, mWvJsBridge, id);
                    }

                });
            }
        });
        mWvJsBridge.registerHandler("setWebViewAutoReload", new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if(!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge))) return;
                Log.i(TAG, data);
                JsonElement idJE = getJsonElementFromCallbackString(data, "data");
                String what = idJE == null || idJE.isJsonNull() ? "previous" : idJE.getAsString();
                // set auto refresh
                webPage.getWebViewResultIntent().putExtra("auto_reload", what);
            }
        });

        mWvJsBridge.registerHandler("getAppConfig", new WebViewJavascriptBridge.WVJBHandler(){

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if(!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge))) return;
                Log.i(TAG, data);
                JsonElement idJE = getJsonElementFromCallbackString(data, "data");
                final String key = idJE == null || idJE.isJsonNull() ? null : idJE.getAsString();
                webPage.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (key == null) {
                            jsCallback.callback("null", true);
                        } else {
                            jsCallback.callback(GsonHelper.getGson().toJson(Utils.readAppSettings(webPage.getActivity(), key)), true);
                        }
                    }
                });
            }
        });
        mWvJsBridge.registerHandler("setAppConfig", new WebViewJavascriptBridge.WVJBHandler(){

            @Override
            public void handle(String data, WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if(!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge))) return;
                Log.i(TAG, data);
                WebViewSetConfigDto setConfigDto = getDataFromCallbackString(data, WebViewSetConfigDto.class);
                if(setConfigDto == null || setConfigDto.key == null) return;
                Utils.writeAppSettings(webPage.getActivity(), setConfigDto.key, setConfigDto.value);

            }
        });
        mWvJsBridge.registerHandler("requestLocation", new WebViewJavascriptBridge.WVJBHandler(){

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if(!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge))) return;
                Log.i(TAG, data);

                WebViewRequestLocationDto requestLocationDto = getDataFromCallbackString(data, WebViewRequestLocationDto.class);
                if(requestLocationDto == null) requestLocationDto = new WebViewRequestLocationDto();
                if(requestLocationDto.accuracy <= 0.001f) requestLocationDto.accuracy = 100;
                if(requestLocationDto.age <= 0.001f) requestLocationDto.age = 30;
                long ageMilliseconds = (long)(requestLocationDto.age * 1000);
                final LocationManager mLocationManager = (LocationManager) webPage.getActivity().getSystemService(Context.LOCATION_SERVICE);

                Criteria criteria = new Criteria();
                String best = mLocationManager.getBestProvider(criteria, true);
                if(TextUtils.isEmpty(best)){

                    webPage.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jsCallback.callback(GsonHelper.getGson().toJson(new WebViewRequestLocationResultDto("无法定位")), true);
                        }
                    });
                    return;
                }
                //since you are using true as the second parameter, you will only get the best of providers which are enabled.
                final  Location location = mLocationManager.getLastKnownLocation(best);
                if(location.getTime() + ageMilliseconds > System.currentTimeMillis()){

                    webPage.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jsCallback.callback(GsonHelper.getGson().toJson(new WebViewRequestLocationResultDto(location)), true);
                        }
                    });
                    return;
                }
                mLocationManager.requestLocationUpdates(best, ageMilliseconds,
                        requestLocationDto.accuracy, new LocationListener() {

                            @Override
                            public void onLocationChanged(final Location location) {


                                webPage.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jsCallback.callback(GsonHelper.getGson().toJson(new WebViewRequestLocationResultDto(location)), true);

                                    }
                                });

                                mLocationManager.removeUpdates(this);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });
            }
        });

        mWvJsBridge.registerHandler("showActionSheet", new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if (!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge)))
                    return;
                Log.i(TAG, data);

                final WebViewActionSheetDto dto = getDataFromCallbackString(data, WebViewActionSheetDto.class);
                if(dto == null || dto.buttons == null || dto.buttons.length == 0) {

                    webPage.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jsCallback.callback("null", true);
                        }
                    });
                    return;
                }
                String[] items = new String[dto.buttons.length];
                for(int i=0;i<dto.buttons.length;i++)
                    items[i] = dto.buttons[i].title;
                AlertDialog.Builder ab = new AlertDialog.Builder(webPage.getActivity()).setTitle(dto.title).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {

                        webPage.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jsCallback.callback(dto.buttons[which].id, false);
                                dialog.dismiss();
                            }
                        });
                    }
                });
                ab.setCancelable(dto.cancelable);
                if(dto.cancelable){
                    ab.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            webPage.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsCallback.callback("null", true);
                                }
                            });
                        }
                    });
                }
                ab.show();
            }
        });

        mWvJsBridge.registerHandler("finish", new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if (!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge)))
                    return;
                Log.i(TAG, data);
                JsonElement dataE = getJsonElementFromCallbackString(data, "data");

                String dataJSON = (dataE == null || dataE.isJsonNull()) ? null : GsonHelper.getGson().toJson(dataE);
                if(dataJSON != null)
                    webPage.getWebViewResultIntent().putExtra("child_finish_data", dataJSON);
                webPage.getActivity().finish();
            }
        });
        mWvJsBridge.registerHandler("showAlert", new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if (!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge)))
                    return;
                Log.i(TAG, data);

                final WebViewAlertDto dto = getDataFromCallbackString(data, WebViewAlertDto.class);
                if(dto == null) {

                    webPage.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jsCallback.callback("null", true);
                        }
                    });
                    return;
                }
                String[] items = new String[(dto.buttons == null ? 0 : dto.buttons.length) + 1];
                if(dto.buttons != null)
                    for(int i=0;i<dto.buttons.length;i++)
                        items[i] = dto.buttons[i].title;

                if(TextUtils.isEmpty(dto.title)) dto.title = "请选择";
                if(TextUtils.isEmpty(dto.buttonCancel)) dto.buttonCancel = "取消";
                items[items.length-1] = dto.buttonCancel;
                AlertDialog.Builder ab = new AlertDialog.Builder(webPage.getActivity());
                if(items.length <= 2){

                    ab.setTitle(dto.title);
                    if(!TextUtils.isEmpty(dto.message))
                        ab.setMessage(dto.message);
                    if(items.length >= 2) {
                        ab.setPositiveButton(items[0], new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialog, int which) {

                                webPage.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jsCallback.callback(dto.buttons[0].id, false);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                    }

                    ab.setNegativeButton(items[items.length-1], new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            webPage.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsCallback.callback("null", true);
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }else{
                    if(!TextUtils.isEmpty(dto.message))
                        ab.setTitle(dto.title + "\n" + dto.message);
                    else
                        ab.setTitle(dto.title);
                    ab.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            webPage.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (dto.buttons != null && which < dto.buttons.length)
                                        jsCallback.callback(dto.buttons[which].id, false);
                                    else
                                        jsCallback.callback("null", true);
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }
                ab.setCancelable(true);
                ab.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        webPage.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jsCallback.callback("null", true);
                            }
                        });
                    }
                });

                ab.show();
            }
        });
        mWvJsBridge.registerHandler("showInput", new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if (!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge)))
                    return;
                Log.i(TAG, data);

                final WebViewInputDto dto = getDataFromCallbackString(data, WebViewInputDto.class);
                if(dto == null) {

                    webPage.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jsCallback.callback("null", true);
                        }
                    });
                    return;
                }
                if("date".equalsIgnoreCase(dto.inputType)){

                    Calendar c = Calendar.getInstance();
                    if(TextUtils.isEmpty(dto.inputDefault)){
                        dto.inputDefault = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
                    }

                    String[] ss = dto.inputDefault.split("-");
                    int year, month, day;
                    if(ss.length == 3){
                        year = Integer.parseInt(ss[0]);
                        month = Integer.parseInt(ss[1]) - 1;
                        day = Integer.parseInt(ss[2]);
                    }else{
                        year = c.get(Calendar.YEAR);
                        month = Integer.parseInt(ss[0]) - 1;
                        day = Integer.parseInt(ss[1]);
                    }

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(webPage.getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, final int year,
                                                      final int monthOfYear, final int dayOfMonth) {

                                    webPage.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsCallback.callback(
                                                    String.format("%04d-%02d-%02d",year ,(monthOfYear + 1) ,dayOfMonth), false);
                                        }
                                    });

                                }
                            }, year, month, day);
                    dpd.setCancelable(true);
                    dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            webPage.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsCallback.callback("null", true);
                                }
                            });
                        }
                    });
                    dpd.setTitle(dto.title);
                    dpd.setMessage(dto.message);
                    dpd.show();
                    return;
                }else if("time".equalsIgnoreCase(dto.inputType)){
                    if(TextUtils.isEmpty(dto.inputDefault)) dto.inputDefault = "13:00";

                    int hour = Integer.parseInt(dto.inputDefault.split(":")[0]);
                    int minute = Integer.parseInt(dto.inputDefault.split(":")[1]);
                    TimePickerDialog tp;
                    tp = new TimePickerDialog(webPage.getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, final int selectedHour, final int selectedMinute) {
                            webPage.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsCallback.callback(String.format("%d:%02d", selectedHour, selectedMinute), false);
                                }
                            });
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    tp.setTitle(dto.title);
                    tp.setMessage(dto.message);
                    tp.setCancelable(true);
                    tp.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            webPage.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jsCallback.callback("null", true);
                                }
                            });
                        }
                    });
                    tp.show();
                    return;
                }

                // Set up the input
                final EditText input = new EditText(webPage.getActivity());
                if("password".equalsIgnoreCase(dto.inputType))
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                else if("integer".equalsIgnoreCase(dto.inputType))
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                else if("phone".equalsIgnoreCase(dto.inputType))
                    input.setInputType(InputType.TYPE_CLASS_PHONE);
                else
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                if(!TextUtils.isEmpty(dto.inputDefault))
                    input.setText(dto.inputDefault);
                if(!TextUtils.isEmpty(dto.inputPlaceholder))
                    input.setHint(dto.inputPlaceholder);
                if(dto.inputMultiline > 1){
                    input.setSingleLine(false);
                    input.setLines(dto.inputMultiline);
                }else{
                    input.setSingleLine();
                }
                if(dto.inputReadonly)
                    input.setKeyListener(null);
                if(TextUtils.isEmpty(dto.buttonOK)) dto.buttonOK = "确定";
                if(TextUtils.isEmpty(dto.buttonCancel)) dto.buttonCancel = "取消";

                AlertDialog.Builder ab = new AlertDialog.Builder(webPage.getActivity()).setTitle(dto.title).setMessage(dto.message)
                        .setNegativeButton(dto.buttonCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {

                                webPage.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jsCallback.callback("null", true);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }).setPositiveButton(dto.buttonOK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {

                                webPage.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        jsCallback.callback(input.getText().toString(), false);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                ab.setView(input);

                ab.setCancelable(true);
                ab.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        webPage.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jsCallback.callback("null", true);
                            }
                        });
                    }
                });

                ab.show();
                if(!dto.inputReadonly)
                    input.requestFocus();
            }
        });
        mWvJsBridge.registerHandler("copyText", new WebViewJavascriptBridge.WVJBHandler() {

            @Override
            public void handle(String data, final WebViewJavascriptBridge.WVJBResponseCallback jsCallback) {
                if (!isTrustedTab(webPage.getTabWithBridge(mWvJsBridge)))
                    return;
                Log.i(TAG, data);
                JsonElement idJE = getJsonElementFromCallbackString(data, "data");
                String text = idJE == null || idJE.isJsonNull() ? null : idJE.getAsString();
                ClipboardManager clipboard = (ClipboardManager) webPage.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);
            }
        });
    }

    public static void beforeFinishWebView(WebViewPage webPage){
        Intent i = webPage.getWebViewResultIntent();
        webPage.getActivity().setResult(Activity.RESULT_OK, i);
    }
    static boolean isAutoRefresh(WebViewPage context){
        if(context == null) return false;
        SwipeRefreshWebView wv = context.getCurrentTab().getWebView();
        if(wv == null || wv.getRefreshableView() == null) return false;
        if(wv.getUrl() == null) return false;
        if(Utils.hasConfig(Uri.parse(wv.getUrl()), "wf-auto-reload")){
            return true;
        }
        return false;
    }
    public static void onStart(WebViewPage context){
        if(isAutoRefresh(context))
            context.getCurrentTab().refreshWebView();
        else {
            WebViewTab tab = context.getCurrentTab();
            if (!tab.hasRefreshed()) {
                tab.refreshWebView();
            }
        }
    }
    public static boolean onActivityResult(final WebViewPage webPage, int requestCode, int resultCode, Intent data){

        if(requestCode == REQUEST_CODE_START_NEW_WEBVIEW){
            String autoRefresh = data != null ? data.getStringExtra("auto_reload"): null;
            if("all".equals(autoRefresh) || "previous".equals(autoRefresh)){
                // refresh
                if("all".equals(autoRefresh)){
                    webPage.getWebViewResultIntent().putExtra("auto_reload", "all");
                }
                // 如果不是自动刷新，就刷新，否则在onStart里会刷新2次
                if(!isAutoRefresh(webPage))
                    webPage.getCurrentTab().refreshWebView();
            }
            String childFinishData = data != null ? data.getStringExtra("child_finish_data"): null;
            if(childFinishData != null){
                webPage.getCurrentTab().getWebViewJavascriptBridge().callHandler("onChildPageFinished", childFinishData, true);
            }
            return true;
        }
        return false;
    }

    static void removeActionButton(final WebViewPage webPage, final WebViewJavascriptBridge mWvJsBridge, int id){
        if(id == 0) return;

        WebViewTab tab = webPage.getTabWithBridge(mWvJsBridge);
        if(tab == null) return;
        List<WebViewActionButtonDto> buttons = tab.getActionButtons();
        for(int i=buttons.size()-1;i>-1;i--){
            if(buttons.get(i).id == id){
                buttons.remove(i);
            }
        }
        webPage.getActivity().invalidateOptionsMenu();
    }
    public static void onWebViewActionButtonClicked(final WebViewPage webPage, int id){
        WebViewTab tab = webPage.getCurrentTab();
        if(tab == null) return;
        List<WebViewActionButtonDto> buttons = tab.getActionButtons();
        for(int i=buttons.size()-1;i>-1;i--) {
            if (buttons.get(i).id == id) {
                tab.getWebViewJavascriptBridge().callHandler("onActionButtonClicked", Integer.toString(id), false);
                break;
            }
        }
    }
    static void addActionButton(final WebViewPage webPage, final WebViewJavascriptBridge mWvJsBridge, WebViewActionButtonDto button){
        if(button == null) return;

        WebViewTab tab = webPage.getTabWithBridge(mWvJsBridge);
        if(tab == null) return;
        if(button.id == 0)
            button.id = tab.getActionButtons().size() + 1;
        removeActionButton(webPage, mWvJsBridge, button.id);
        tab.getActionButtons().add(button);

        webPage.getActivity().invalidateOptionsMenu();
    }

    static Gson gson = GsonHelper.getGson();

    static <T> T getDataFromCallbackString(String string, Class<T> clazz) {
        JsonElement root = getJsonRootElementFromCallbackString(string);
        return getDataFromCallbackRootElement(root, clazz);
    }

    static <T> T getDataFromCallbackRootElement(JsonElement root, Class<T> clazz){
        if(!root.isJsonObject()) return null;
        JsonObject o = root.getAsJsonObject();
        if(!o.has("data")) return null;
        JsonElement data = o.get("data");
        return gson.fromJson(data, clazz);
    }

    static JsonElement getJsonRootElementFromCallbackString(String string){
        return getJsonElementFromCallbackString(string, null);
    }

    static JsonElement getJsonElementFromCallbackString(String string, String path){
        String [] paths = TextUtils.isEmpty(path) ? new String[]{}: path.split("\\.");
        JsonElement result = new JsonParser().parse(string);
        for(int i=0;i<paths.length;i++){
            if(!result.isJsonObject()) return null;
            JsonObject o = result.getAsJsonObject();
            String name = paths[i];
            if(!o.has(name)) return null;
            result = o.get(name);
        }
        return result;
    }

}
