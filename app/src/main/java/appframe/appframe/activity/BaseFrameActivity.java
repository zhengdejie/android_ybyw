package appframe.appframe.activity;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by dashi on 15/6/21.
 */
public class BaseFrameActivity extends FragmentActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Tag","BaseFrameActivity......onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Tag","BaseFrameActivity......onStop");
    }
}
