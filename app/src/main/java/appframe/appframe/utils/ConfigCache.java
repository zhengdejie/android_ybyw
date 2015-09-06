package appframe.appframe.utils;

import android.content.Context;

import com.github.snowdream.android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import appframe.appframe.BuildConfig;

public class ConfigCache {
    private static final String TAG = ConfigCache.class.getSimpleName();

    static final int CONFIG_CACHE_MOBILE_PASSDUE  = 604800000;  //7 day
    static final int CONFIG_CACHE_WIFI_PASSDUE    = 86400000;   //1 day

    static final int CLEAN_MIN_COUNT = 1000; // 最少1000个cache才会开始尝试清cache


    // DEBUG下刷新频繁点，方便调试
    static final int CONFIG_CACHE_MOBILE_UPTODATE  = BuildConfig.DEBUG ? 6000 : 600000;  //10min
    static final int CONFIG_CACHE_WIFI_UPTODATE    = BuildConfig.DEBUG ? 6000 : 60000;   //1 minute


    static Context applicationContext;
    public static void init(Context _applicationContext){
        applicationContext = _applicationContext;
    }
    /*
     * age 是否已经过期
     * 注意，对于一个cache有三种状态，
     * 	1. up to date, 不需要刷新；
     * 	2. 未过期，但需要刷新；
     * 	3. 已过期 passdue
     */
    public static final boolean isCachePassdue(long age){
        if(age < 0) return true;
        if(NetworkUtils.getGlobalNetworkState(applicationContext) == NetworkUtils.NETWORK_WIFI) {
            return age > CONFIG_CACHE_WIFI_PASSDUE;
        } else {
            return age > CONFIG_CACHE_MOBILE_PASSDUE;
        }
    }
    public static final boolean isCacheUpToDate(long age){
        if(age < 0) return false;
        if(NetworkUtils.getGlobalNetworkState(applicationContext) == NetworkUtils.NETWORK_MOBILE) {
            return age < CONFIG_CACHE_MOBILE_UPTODATE;
        } else {
            return age < CONFIG_CACHE_WIFI_UPTODATE;
        }
    }

    public static boolean isUrlCached(String url){
        if(url == null) return false;

        File dir = getCacheFolder();
        if(dir == null) return false;
        File file = new File(dir.getAbsolutePath() + "/" + urlToFileName(url));
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }
    public static long getUrlCacheAge(String url){
        if(url == null) return -1;

        File dir = getCacheFolder();
        if(dir == null) return -1;
        File file = new File(dir.getAbsolutePath() + "/" + urlToFileName(url));
        if (file.exists() && file.isFile()) {
            long expiredTime = System.currentTimeMillis() - file.lastModified();
            return expiredTime;
        }
        return -1;
    }
    public static long getUrlCacheTime(String url){
        if(url == null) return -1;

        File dir = getCacheFolder();
        if(dir == null) return -1;
        File file = new File(dir.getAbsolutePath() + "/" + urlToFileName(url));
        if (file.exists() && file.isFile()) {
            return file.lastModified();
        }
        return -1;
    }
    public static byte[] getUrlCacheBytes(String url){
        if (url == null) {
            return null;
        }

        byte[] result = null;
        File dir = getCacheFolder();
        if(dir == null) return null;
        File file = new File(dir.getAbsolutePath() + "/" + urlToFileName(url));
        if (file.exists() && file.isFile()) {
            long expiredTime = System.currentTimeMillis() - file.lastModified();
            Log.d(TAG, file.getAbsolutePath() + " expiredTime:" + expiredTime / 60000 + "min");
            //1. in case the system time is incorrect (the time is turn back long ago)
            //2. when the network is invalid, you can only read the cache
            if (expiredTime < 0) {
                return null;
            }
            if(isCachePassdue(expiredTime))
                return null;

            try {
                result = readBytes(file);
            } catch (IOException e) {
                Log.e(TAG, "read bytes failed", e);
            }
        }
        return result;
    }
    public static String getUrlCache(String url) {
        byte [] bytes = getUrlCacheBytes(url);
        if(bytes != null) return new String(bytes);
        return null;
    }
    public static void clearCache(String url){
        setUrlCache((byte[])null, url);
    }
    static File cacheFolder = null;
    static File getCacheFolder(){
        if(cacheFolder == null)
            cacheFolder = new File(applicationContext.getCacheDir().getAbsolutePath() + "/configCache");

        if(!cacheFolder.exists()) cacheFolder.mkdirs();
        return cacheFolder;
    }

    static String urlToFileName(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    static String fileNameToUrl(String fileName){
        try {
            return URLDecoder.decode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    public static void clearCacheIfTooLarge(){

        File dir = getCacheFolder();
        if(dir == null) return;
        File [] files = dir.listFiles();
        if(files == null || files.length < CLEAN_MIN_COUNT)
            return;
        long lastModifiedMin = Long.MAX_VALUE, lastModifiedMax = Long.MIN_VALUE;
        for(File f : files){
            lastModifiedMin = Math.min(f.lastModified(), lastModifiedMin);
            lastModifiedMax = Math.max(f.lastModified(), lastModifiedMin);
        }
        long lastModifiedToDelete = (lastModifiedMin + lastModifiedMax) / 2;
        for(File f : files){
            if(f == null || f.getName().equals(".") || f.getName().equals("..")) continue;
            if(f.lastModified() < lastModifiedToDelete){

                try{
                    if(!f.delete()) f.deleteOnExit();
                }catch(Exception e){
                    Log.d(TAG, "delete " + f.getAbsolutePath() + " data failed!", e);
                }
            }
        }
    }
    public static void clearAllCache(){
        clearCache((java.util.regex.Pattern)null);
    }
    /*
     * 扫描所有cache，匹配则清空
     */
    public static void clearCache(java.util.regex.Pattern pattern){

        File dir = getCacheFolder();
        if(dir == null) return;
        File [] files = dir.listFiles();
        if(files == null || files.length <= 0)
            return;
        for(File f : files){
            if(f == null || f.getName().equals(".") || f.getName().equals("..")) continue;
            if(pattern != null){
                String url = fileNameToUrl(f.getName());
                if(!pattern.matcher(url).matches()){
                    continue;
                }
            }else{
                // 删除所有
            }
            try{
                if(!f.delete()) f.deleteOnExit();
            }catch(Exception e){
                Log.d(TAG, "delete " + f.getAbsolutePath() + " data failed!", e);
            }
        }
    }
    public static void setUrlCache(byte[] data, String url){

        File dir = getCacheFolder();
        if(dir == null) return;
        File file = new File(dir.getAbsolutePath() + "/" + urlToFileName(url));
        if(data == null || data.length == 0){
            try{
                if(!file.delete()) file.deleteOnExit();
            }catch(Exception e){
                Log.d(TAG, "delete " + file.getAbsolutePath() + " data failed!", e);
            }
        }else{
            try {
                //创建缓存数据到磁盘，就是创建文件
                writeBytes(file, data);
            } catch (Exception e) {
                Log.d(TAG, "write " + file.getAbsolutePath() + " data failed!", e);
            }
        }
    }
    public static void setUrlCache(String data, String url) {
        if(data == null){
            setUrlCache((byte[])null, url);
            return;
        }
        setUrlCache(data.getBytes(), url);
    }
    public static void writeBytes(File file, byte[] bytes) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(bytes);
        } finally {
            if(out != null) {
                out.close();
            }
        }
    }
    public static byte[] readBytes(File file) throws IOException {
        int size = (int) file.length();
        try {
            byte[] bytes = new byte[size];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return bytes;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "read bytes failed", e);
        } catch (IOException e) {
            Log.e(TAG, "read bytes failed", e);
        }
        return null;
    }
}