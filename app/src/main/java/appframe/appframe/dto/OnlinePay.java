package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/2/3.
 */
public class OnlinePay extends Http.BaseDto{
    public String Appid;
    public String Noncestr;
    public String Package;
    public String Partnerid;
    public String Prepayid;
    public String Timestamp;
    public String Sign;
    public int PlatformType;

    public String getAppid() {
        return Appid;
    }

    public void setAppid(String appid) {
        Appid = appid;
    }

    public String getNoncestr() {
        return Noncestr;
    }

    public void setNoncestr(String noncestr) {
        Noncestr = noncestr;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getPartnerid() {
        return Partnerid;
    }

    public void setPartnerid(String partnerid) {
        Partnerid = partnerid;
    }

    public String getPrepayid() {
        return Prepayid;
    }

    public void setPrepayid(String prepayid) {
        Prepayid = prepayid;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public int getPlatformType() {
        return PlatformType;
    }

    public void setPlatformType(int platformType) {
        PlatformType = platformType;
    }
}
