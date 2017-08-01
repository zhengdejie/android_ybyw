package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017/6/7.
 */

public class GuideTour  extends Http.BaseDto implements Serializable {
    private int Id;
    private UserDetail User;
    private int GuideExperienceInDays;
    private int VerifiedTourGuide;
    private String Description;
    private String VoiceSampleLink;
    private String VideoSampleLink;
    private List<String> Photos;
    private double HourlyRatePrice;
    private double MinimumHours;
    private String Itinerary;
    private List<String> Features;
    private int GuideLevel;
    private String Occupation;
    private String Gender;
    private String TotalServingOrders;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public UserDetail getUser() {
        return User;
    }

    public void setUser(UserDetail user) {
        User = user;
    }

    public int getGuideExperienceInDays() {
        return GuideExperienceInDays;
    }

    public void setGuideExperienceInDays(int guideExperienceInDays) {
        GuideExperienceInDays = guideExperienceInDays;
    }

    public int getVerifiedTourGuide() {
        return VerifiedTourGuide;
    }

    public void setVerifiedTourGuide(int verifiedTourGuide) {
        VerifiedTourGuide = verifiedTourGuide;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getVoiceSampleLink() {
        return VoiceSampleLink;
    }

    public void setVoiceSampleLink(String voiceSampleLink) {
        VoiceSampleLink = voiceSampleLink;
    }

    public String getVideoSampleLink() {
        return VideoSampleLink;
    }

    public void setVideoSampleLink(String videoSampleLink) {
        VideoSampleLink = videoSampleLink;
    }

    public List<String> getPhotos() {
        return Photos;
    }

    public void setPhotos(List<String> photos) {
        Photos = photos;
    }

    public double getHourlyRatePrice() {
        return HourlyRatePrice;
    }

    public void setHourlyRatePrice(double hourlyRatePrice) {
        HourlyRatePrice = hourlyRatePrice;
    }

    public double getMinimumHours() {
        return MinimumHours;
    }

    public void setMinimumHours(double minimumHours) {
        MinimumHours = minimumHours;
    }

    public String getItinerary() {
        return Itinerary;
    }

    public void setItinerary(String itinerary) {
        Itinerary = itinerary;
    }

    public List<String> getFeatures() {
        return Features;
    }

    public void setFeatures(List<String> features) {
        Features = features;
    }

    public int getGuideLevel() {
        return GuideLevel;
    }

    public void setGuideLevel(int guideLevel) {
        GuideLevel = guideLevel;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getTotalServingOrders() {
        return TotalServingOrders;
    }

    public void setTotalServingOrders(String totalServingOrders) {
        TotalServingOrders = totalServingOrders;
    }
}
