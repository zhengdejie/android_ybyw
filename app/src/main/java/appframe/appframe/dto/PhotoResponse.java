package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/4/11.
 */
public class PhotoResponse extends Http.BaseDto implements Serializable {

    public String FrontUrl;
    public String BackUrl;
    public String FrontWithFaceUrl;

    public String getFrontUrl() {
        return FrontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        FrontUrl = frontUrl;
    }

    public String getBackUrl() {
        return BackUrl;
    }

    public void setBackUrl(String backUrl) {
        BackUrl = backUrl;
    }

    public String getFrontWithFaceUrl() {
        return FrontWithFaceUrl;
    }

    public void setFrontWithFaceUrl(String frontWithFaceUrl) {
        FrontWithFaceUrl = frontWithFaceUrl;
    }
}
