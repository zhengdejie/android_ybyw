package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017/6/7.
 */

public class Rendezvous extends Http.BaseDto implements Serializable {
    private double Longitude;
    private double Latitude;
    private String AddressInString;

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getAddressInString() {
        return AddressInString;
    }

    public void setAddressInString(String addressInString) {
        AddressInString = addressInString;
    }
}
