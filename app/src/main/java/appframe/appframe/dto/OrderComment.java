package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/4.
 */
public class OrderComment extends Http.BaseDto implements Serializable {
    private int Id ;
    private UserDetail User;
    private String Comment;
    private String CreatedAt;
    private boolean Anonymity;


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

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public boolean isAnonymity() {
        return Anonymity;
    }

    public void setAnonymity(boolean anonymity) {
        Anonymity = anonymity;
    }
}
