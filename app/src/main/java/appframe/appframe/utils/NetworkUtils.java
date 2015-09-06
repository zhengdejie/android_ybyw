package appframe.appframe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by dashi on 15/6/11.
 */
public class NetworkUtils {

    public static final String NAME_NETWORK_WIFI ="wifi";
    public static final String NAME_NETWORK_NONE = "unknow";
    public static final String NAME_NETWORK_MOBILE = "2g\3g\4g";

    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;


    static int mNetWorkState = NetworkUtils.NETWORK_NONE;
    static long mNetWorkStateTime = 0;

    public static int getGlobalNetworkState(Context context){
        long now = System.currentTimeMillis();
        // 最多 5秒更新一次
        if(now - mNetWorkStateTime > 5000 && context != null){
            mNetWorkStateTime = now;
            mNetWorkState = NetworkUtils.getNetworkState(context);
        }
        return mNetWorkState;
    }

    public static boolean isWifi(Context context){
        int network = getNetworkState(context);
        if(NETWORK_WIFI == network){
            return true;
        }
        return false;
    }

    public static boolean is3G(Context context){
        int network = getNetworkState(context);
        if(NETWORK_MOBILE == network){
            return true;
        }
        return false;
    }

    public static boolean hasNetwork(Context context){
        int network = getNetworkState(context);
        if(NETWORK_NONE == network){
            return true;
        }
        return false;
    }


    public static int getNetworkState(Context context){
        try{
            ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);

            //Wifi
            NetworkInfo.State state = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState();
            if(state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING){
                return NETWORK_WIFI;
            }

            //3G
            state = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState();
            if(state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING){
                return NETWORK_MOBILE;
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return NETWORK_NONE;
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
    public static String getWifiIP(Context c) {
        String ip = null;
        if(getNetworkState(c) == NETWORK_WIFI) {
            WifiManager wifiMgr = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
            if (!wifiMgr.isWifiEnabled()) {
                wifiMgr.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        }

        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public static boolean isNetworkAvaliable(Context context){
        if (getNetworkState(context) == NetworkUtils.NETWORK_NONE) {
            //Toast.makeText(context, "没有网络连接",
            //		Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}