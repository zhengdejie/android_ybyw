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
}
