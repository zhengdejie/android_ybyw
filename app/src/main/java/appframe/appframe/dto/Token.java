package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/16.
 */
public class Token extends Http.BaseDto implements Serializable {
    public String UpToken;

    public String getUpToken() {
        return UpToken;
    }

    public void setUpToken(String upToken) {
        UpToken = upToken;
    }
}
