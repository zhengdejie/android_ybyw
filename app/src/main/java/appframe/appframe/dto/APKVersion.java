package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/12/28.
 */
public class APKVersion extends Http.BaseDto{
    private int VersionCode;

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }
}
