package appframe.appframe.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017/6/7.
 */

public class GuideTourOrder extends Http.BaseDto implements Serializable {
    private int Id;
    private GuideTour Guide;
    private UserDetail Applicant;
    private int Status;
    private Rendezvous Rendezvous;
    private String StartTime;
    private String EndTime;
    private int BuyGuideMeals;
    private String UserRequest;
    private int Hours;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public GuideTour getGuide() {
        return Guide;
    }

    public void setGuide(GuideTour guide) {
        Guide = guide;
    }

    public UserDetail getApplicant() {
        return Applicant;
    }

    public void setApplicant(UserDetail applicant) {
        Applicant = applicant;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public appframe.appframe.dto.Rendezvous getRendezvous() {
        return Rendezvous;
    }

    public void setRendezvous(appframe.appframe.dto.Rendezvous rendezvous) {
        Rendezvous = rendezvous;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public int getBuyGuideMeals() {
        return BuyGuideMeals;
    }

    public void setBuyGuideMeals(int buyGuideMeals) {
        BuyGuideMeals = buyGuideMeals;
    }

    public String getUserRequest() {
        return UserRequest;
    }

    public void setUserRequest(String userRequest) {
        UserRequest = userRequest;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(int hours) {
        Hours = hours;
    }
}
