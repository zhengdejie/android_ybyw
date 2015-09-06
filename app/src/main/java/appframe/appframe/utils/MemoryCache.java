package appframe.appframe.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.util.LruCache;

import com.github.snowdream.android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryCache {
    static class CacheObject{
        Object data;
        long time;
        public CacheObject(Object data, long time){
            this.data = data;
            if(time < 0)
                time = System.currentTimeMillis();
            this.time = time;
        }
    }
    public static class Bytes {
        public byte[] data;
        public Bytes(byte[] bytes){
            data = bytes;
        }
    }

    static LruCache<String,CacheObject> cache = new LruCache<String,CacheObject>(200){

        @Override
        protected int sizeOf (String key, CacheObject value){
            return 1;
        }
    };



    static HashMap<String,Object> cacheToWriteToDisk = new HashMap<String,Object>();
    static final String CONFIG_CACHE_KEY_FORMAT = "MemoryCache:%s";
    static final int MEMORY_BUFFER_COUNT = 20;

    static final Gson gsonBg = new Gson();
    static class WriteCacheToDiskTask extends AsyncTask<HashMap<String,Object> , Void, Void> {

        @Override
        protected Void doInBackground(HashMap<String,Object> ... params) {
            if(params.length == 0 || params[0] == null) return null;
            for(Map.Entry<String,Object> e : params[0].entrySet()){
                String s = String.format(CONFIG_CACHE_KEY_FORMAT, e.getKey());
                Object v = e.getValue();
                String json;

                if(v != null && v instanceof Bytes){

                    ConfigCache.setUrlCache(((Bytes)v).data, s);
                }else{

                    if(v instanceof String){
                        json = (String)v;
                    }else if(v instanceof JsonElement){
                        JsonElement je = (JsonElement)v;
                        json = gsonBg.toJson(je);
                    }else{
                        json = gsonBg.toJson(v);
                    }

                    ConfigCache.setUrlCache(json, s);
                }
            }
            return null;
        }

    }
    static final Gson gson = new Gson();
    @SuppressWarnings("unchecked")
    static CacheObject getRaw(String url, Class<?> dtoClass){

        CacheObject o = cache.get(url);
        if(o != null){
            return o;
        }
        String s = String.format(CONFIG_CACHE_KEY_FORMAT, url);

        String str = null;
        Bytes bytes = null;
        if(dtoClass == Bytes.class){
            byte[] bs = ConfigCache.getUrlCacheBytes(s);
            if(bs != null)
                bytes = new Bytes(bs);
        }else{
            str = ConfigCache.getUrlCache(s);
        }


        long time = (str == null && bytes == null) ? -1 : ConfigCache.getUrlCacheTime(s);

        if(time < 0){
            o = new CacheObject(null, System.currentTimeMillis());
        }else if(bytes != null){
            o = new CacheObject(bytes, time);
        }else{
            try{
                if(dtoClass == String.class){
                    o = new CacheObject(str, time);
                }else{
                    JsonElement rootElement = new JsonParser().parse(str);
                    if(rootElement == null){
                        o = new CacheObject(null, time);
                    }else{
                        if(rootElement.isJsonObject()){
                            // object
                            o = new CacheObject( gson.fromJson(rootElement, dtoClass), time);

                        }else if(rootElement.isJsonArray()){
                            // array
                            JsonArray dataArray = rootElement.getAsJsonArray();
                            int count = dataArray.size();
                            @SuppressWarnings("rawtypes")
                            List list = new ArrayList(count);
                            for(int i=0;i<count;i++)
                                list.add(gson.fromJson(dataArray.get(i), dtoClass));

                            o = new CacheObject(list, time);
                        }else{
                            Log.e("MemoryCache", "error paring cached data");
                            o = new CacheObject(null, time);
                        }
                    }
                }
            }catch(Exception e){
                Log.e("MemoryCache", "parse error " + str, e);
                o = new CacheObject(null, time);
            }
        }
        cache.put(url, o);
        return o;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getList(String url, Class<T> c){
        CacheObject o = getRaw(url, c);
        if(o == null || o.data == null || ! (o.data instanceof List)) return null;
        return (List<T>)o.data;
    }
    @SuppressWarnings("unchecked")
    public static <T> T getObject(String url, Class<T> c){
        CacheObject o = getRaw(url, c);
        if(o == null || o.data == null || o.data instanceof List) return null;
        return (T)o.data;
    }
    @SuppressWarnings("unchecked")
    public static <T> T get(String url, Class<T> c){
        CacheObject o = getRaw(url, c);
        if(o == null || o.data == null) return null;
        return (T)o.data;
    }
    public static void clearCache(String url){
        ConfigCache.clearCache(String.format(CONFIG_CACHE_KEY_FORMAT, url));
        cache.remove(url);
    }
    public static void clearCache(java.util.regex.Pattern pattern){
        ConfigCache.clearCache(pattern);
        cache.evictAll();
    }
    public static void clearAllCache(){
        ConfigCache.clearAllCache();
        cache.evictAll();
    }
    public static long getCacheAge(String url){
        return System.currentTimeMillis() - getCacheTime(url);
    }
    public static long getCacheTime(String url){
        CacheObject o = cache.get(url);
        if(o == null)
            return ConfigCache.getUrlCacheTime(String.format(CONFIG_CACHE_KEY_FORMAT, url));
        return o.time;
    }
    static final Handler postWriteCacheToDiskHandler = new Handler();
    static final Runnable postWriteCacheToDiskRunnable = new Runnable(){

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            if(cacheToWriteToDisk.size() == 0) return;
            HashMap<String,Object> writeToDisk = new HashMap<String,Object>(cacheToWriteToDisk);
            new WriteCacheToDiskTask().execute(writeToDisk);
            cacheToWriteToDisk.clear();
        }

    };
    public interface DTOModifier<T>{
        public void modify(T o);
    }
    @SuppressWarnings("unchecked")
    public static <T> void getAndModify(String url, Class<T> dtoClass, DTOModifier<T> modifier){
        CacheObject co = getRaw(url, dtoClass);
        // 型号不对
        if(co == null || co.data == null) return;
        modifier.modify((T)co.data);
        put(url, co.data, null);
    }
    public static <T> void put(String url, T data, JsonElement je){
        cache.put(url, new CacheObject(data, System.currentTimeMillis()));

        if(je != null)
            cacheToWriteToDisk.put(url, je);
        else
            cacheToWriteToDisk.put(url, data);
        postWriteCacheToDiskHandler.removeCallbacks(postWriteCacheToDiskRunnable);
        if(cacheToWriteToDisk.size() > MEMORY_BUFFER_COUNT){
            // 直接写入
            postWriteCacheToDiskRunnable.run();
        }else{
            // 一秒后写入
            postWriteCacheToDiskHandler.postDelayed(postWriteCacheToDiskRunnable, 1000);
        }
    }
    public static boolean isCacheUpToDate(long age){
        return ConfigCache.isCacheUpToDate(age);
    }
    public static <T> void putList(String url, List<T> data, JsonElement je){
        put(url, data, je);
    }

    /**
     * add item to cache list
     * */
    public static <T> void addItemToList(String url, Class<T> c, T t) {
        addItemToList(url, c, t, -1);
    }

    /**
     * add item to cache list
     *
     * @param url The key of cache list
     * @param c The Class for item object
     * @param t The item object
     * @param budget The budget for cache list, if list size more than budget, the oldest item will be removed
     * */
    public static <T> void addItemToList(String url, Class<T> c, T t, int budget) {
        List<T> list = getList(url, c);
        if(list == null)
            list = new ArrayList<T>();
        if(budget > 0 && list.size() >= budget) {
            List<T> temp = new ArrayList<T>();
            for(int i = 0; i < budget - 1; i++)
                temp.add(list.get(list.size() - budget + 1 + i));
            list = temp;
        }

        list.add(t);
        putList(url, list, null);
    }

}