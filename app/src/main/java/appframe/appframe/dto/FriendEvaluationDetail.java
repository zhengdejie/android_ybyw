package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/12/22.
 */
public class FriendEvaluationDetail extends Http.BaseDto {

    public int Id;
    public UserDetail Praiser;
    public String Praise;
    public String Tags;
    public String CreatedAt;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public UserDetail getPraiser() {
        return Praiser;
    }

    public void setPraiser(UserDetail praiser) {
        Praiser = praiser;
    }

    public String getPraise() {
        return Praise;
    }

    public void setPraise(String praise) {
        Praise = praise;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
