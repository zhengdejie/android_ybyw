package appframe.appframe.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;

/**
 * Created by dashi on 15/6/21.
 */
public class ImageUtils {
    public static class LruImageCache implements ImageLoader.ImageCache {

        private static LruCache<String, Bitmap> mMemoryCache;

        private static LruImageCache lruImageCache;

        private LruImageCache(){
            // Get the Max available memory
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap){
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        public static LruImageCache instance(){
            if(lruImageCache == null){
                lruImageCache = new LruImageCache();
            }
            return lruImageCache;
        }

        @Override
        public Bitmap getBitmap(String arg0) {
            return mMemoryCache.get(arg0);
        }

        @Override
        public void putBitmap(String arg0, Bitmap arg1) {
            if(getBitmap(arg0) == null){
                mMemoryCache.put(arg0, arg1);
            }
        }

    }
    static ImageLoader imageLoader;
    public static ImageLoader getImageLoader(){
        if(imageLoader != null) return imageLoader;
        RequestQueue q = Volley.newRequestQueue(App.instance);
        LruImageCache lruImageCache = LruImageCache.instance();

        imageLoader = new ImageLoader(q,lruImageCache);
        return imageLoader;
    }
    public static void setImageUrl(NetworkImageView niv, String imageId, int defaultWidth, int defaultHeight){
        int w = niv.getWidth(), h = niv.getHeight();
        if(w <= 0) w = defaultWidth;
        if(h <= 0) h = defaultHeight;

        niv.setImageUrl(getImageUrl(imageId, w, h), ImageUtils.getImageLoader());
    }
    public static void setImageUrl(NetworkImageView niv, String imageId){
        int w = niv.getWidth(), h = niv.getHeight();
        if(w <= 0) w = 400;
        if(h <= 0) h = 400;

        niv.setImageUrl(getImageUrl(imageId, w, h), ImageUtils.getImageLoader());
    }

    public static String getImageUrl(String imageId, int w, int h){
        return AppConfig.QINIU_HOST + imageId + "?imageView2/0/w/" + w + "/h/" + h;
    }
}
