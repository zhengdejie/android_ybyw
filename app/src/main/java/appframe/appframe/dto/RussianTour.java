package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017/5/17.
 */

public class RussianTour extends Http.BaseDto implements Serializable {

    private int Id;
    private String Name;
    private String Description;
    private String ApplicationDeadline;
    private String TourTotalCost;
    private String ApplicationDeposit;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getApplicationDeadline() {
        return ApplicationDeadline;
    }

    public void setApplicationDeadline(String applicationDeadline) {
        ApplicationDeadline = applicationDeadline;
    }

    public String getTourTotalCost() {
        return TourTotalCost;
    }

    public void setTourTotalCost(String tourTotalCost) {
        TourTotalCost = tourTotalCost;
    }

    public String getApplicationDeposit() {
        return ApplicationDeposit;
    }

    public void setApplicationDeposit(String applicationDeposit) {
        ApplicationDeposit = applicationDeposit;
    }
}
