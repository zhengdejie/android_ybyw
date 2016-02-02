package appframe.appframe.dto;

import java.io.Serializable;
import java.util.Date;

import appframe.appframe.utils.Http;

/**
 * Created by dashi on 15/6/11.
 */
public class UserBrief extends Http.BaseDto implements Serializable {

    public int Id;
    public String Name;
    public String Avatar;
    public String Mobile;
    public Date CreatedAt;
    public String Gender;
    public Date UpdatedAt;
    public String Location;
    public double TotalPoint;
    public String Signature;
    public double AvgServicePoint;
    public double AvgAttitudePoint;
    public double AvgCharacterPoint;
    public int TotalNumberOfOrder;
    public String FNickName;
    public int Member;
    public int CompletedNumberOfOrder;

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

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public double getTotalPoint() {
        return TotalPoint;
    }

    public void setTotalPoint(double totalPoint) {
        TotalPoint = totalPoint;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public double getAvgServicePoint() {
        return AvgServicePoint;
    }

    public void setAvgServicePoint(double avgServicePoint) {
        AvgServicePoint = avgServicePoint;
    }

    public double getAvgAttitudePoint() {
        return AvgAttitudePoint;
    }

    public void setAvgAttitudePoint(double avgAttitudePoint) {
        AvgAttitudePoint = avgAttitudePoint;
    }

    public double getAvgCharacterPoint() {
        return AvgCharacterPoint;
    }

    public void setAvgCharacterPoint(double avgCharacterPoint) {
        AvgCharacterPoint = avgCharacterPoint;
    }

    public int getTotalNumberOfOrder() {
        return TotalNumberOfOrder;
    }

    public void setTotalNumberOfOrder(int totalNumberOfOrder) {
        TotalNumberOfOrder = totalNumberOfOrder;
    }

    public String getFNickName() {
        return FNickName;
    }

    public void setFNickName(String FNickName) {
        this.FNickName = FNickName;
    }

    public int getMember() {
        return Member;
    }

    public void setMember(int member) {
        Member = member;
    }

    public int getCompletedNumberOfOrder() {
        return CompletedNumberOfOrder;
    }

    public void setCompletedNumberOfOrder(int completedNumberOfOrder) {
        CompletedNumberOfOrder = completedNumberOfOrder;
    }
}
