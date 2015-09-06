package appframe.appframe.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.w3c.dom.Text;

import appframe.appframe.fragment.BaseFragment;

/**
 * Created by Administrator on 2015/8/20.
 */
public class BaiduLocation {
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    public TextView txt_location;
    public Vibrator mVibrator;

    public BaiduLocation(Context context)
    {
        mLocationClient = new LocationClient(context.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mVibrator =(Vibrator)context.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void setOption()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append(location.getAddrStr());
            txt_location.setText(sb.toString());
        }
    }
}
