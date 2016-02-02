package appframe.appframe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2015/12/28.
 */
public class PackageUtils {

    private Context context;
    private PackageManager manager;
    private PackageInfo info;

    public PackageUtils(Context context) {

        this.context = context;
        init();
    }

    public void init() {
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getVersion() {
        return info.versionCode;
    }

    public String getVersionName() {
        return info.versionName;
    }

    public boolean isUpgradeVersion(int oldVersion, int newVersion) {
        return oldVersion < newVersion ? true : false;
    }
}
