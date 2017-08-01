package appframe.appframe.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/5/31.
 */

public class Tools {
    private static final Point screenSize = new Point();

    /**
     *
     * @description 获取屏幕宽高
     * @update 2014年8月30日 下午4:51:48
     */
    public static Point getScreenSize(Context ctt)
    {
        if (ctt == null)
        {
            return screenSize;
        }
        WindowManager wm = (WindowManager) ctt.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null)
        {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display diplay = wm.getDefaultDisplay();
            if (diplay != null)
            {
                diplay.getMetrics(mDisplayMetrics);
                // }
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if (W * H > 0 && (W > screenSize.x || H > screenSize.y))
                {
                    screenSize.set(W, H);
                }
            }
        }
        return screenSize;
    }

    /**状态栏高度*/
    private static  int statusBarHeight = 0;
    /**
     *
     * @description 获取状态栏高度
     * @author zhongwr
     * @params
     * @return 返回状态栏高度
     * @update 2016年1月25日 下午8:53:31
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight <= 0) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        if (statusBarHeight <= 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
