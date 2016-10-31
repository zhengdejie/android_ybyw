package appframe.appframe.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

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
        DisplayMetrics displayMetrics = niv.getContext().getResources()
                .getDisplayMetrics();

        ViewGroup.LayoutParams lp = niv.getLayoutParams();

        int width = niv.getWidth();// 获取niv的实际宽度
        if (width <= 0)
        {
            width = lp.width;// 获取niv在layout中声明的宽度
        }
        if (width <= 0)
        {
            // width = imageView.getMaxWidth();// 检查最大值
            //width = niv.getMaxWidth();
            width = niv.getMeasuredWidth();
        }
        if (width <= 0)
        {
            width = displayMetrics.widthPixels;
        }
        int height = niv.getHeight();// 获取imageview的实际高度
        if (height <= 0)
        {
            height = lp.height;// 获取imageview在layout中声明的宽度
        }
        if (height <= 0)
        {
            //height = getImageViewFieldValue(imageView, "mMaxHeight");// 检查最大值
            //height = niv.getMaxHeight();
            height = niv.getMeasuredHeight();
        }
        if (height <= 0)
        {
            height = displayMetrics.heightPixels;
        }

        niv.setImageUrl(getImageUrl(imageId, width, height), ImageUtils.getImageLoader());
    }

    public static void setImageUrl(NetworkImageView niv, String imageId, String type){
        DisplayMetrics displayMetrics = niv.getContext().getResources()
                .getDisplayMetrics();

        ViewGroup.LayoutParams lp = niv.getLayoutParams();

        int width = niv.getWidth();// 获取niv的实际宽度
        if (width <= 0)
        {
            width = lp.width;// 获取niv在layout中声明的宽度
        }
        if (width <= 0)
        {
            // width = imageView.getMaxWidth();// 检查最大值
            //width = niv.getMaxWidth();
            width = niv.getMeasuredWidth();
        }
        if (width <= 0)
        {
            width = displayMetrics.widthPixels;
        }
        int height = niv.getHeight();// 获取imageview的实际高度
        if (height <= 0)
        {
            height = lp.height;// 获取imageview在layout中声明的宽度
        }
        if (height <= 0)
        {
            //height = getImageViewFieldValue(imageView, "mMaxHeight");// 检查最大值
            //height = niv.getMaxHeight();
            height = niv.getMeasuredHeight();
        }
        if (height <= 0)
        {
            height = displayMetrics.heightPixels;
        }

        niv.setImageUrl(getImageUrl(imageId, width, height,"0"), ImageUtils.getImageLoader());
    }

    public static String getImageUrl(String imageId, int w, int h){
        return AppConfig.QINIU_HOST + imageId + "?imageView2/1/w/" + w + "/h/" + h;
    }
    public static String getImageUrl(String imageId, int w, int h,String type){
        return AppConfig.QINIU_HOST + imageId + "?imageView2/0/w/" + w + "/h/" + h;
    }
    public static String getImageUrl(String imageId){
        return AppConfig.QINIU_HOST + imageId + "?imageView2/1";
    }
}
