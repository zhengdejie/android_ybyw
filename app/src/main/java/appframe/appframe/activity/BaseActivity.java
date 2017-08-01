package appframe.appframe.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import appframe.appframe.app.App;

/**
 * Created by dashi on 15/6/20.
 */
public class BaseActivity extends Activity {
    public static MenuItem createMenuItem(Menu menu, int code, String text){
        MenuItem mi = menu.add(Menu.NONE, code, Menu.NONE, text);
        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return mi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Tag","BaseActivity......onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Tag","BaseActivity......onStop");
    }
}
