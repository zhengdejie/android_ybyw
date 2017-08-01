package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017/7/20.
 */

public class GuideComments extends Http.BaseDto implements Serializable{
    private int GuideId;
    private UserBrief Commentator;
    private String Comment;
    private String CreatedAt;
    private int Stars;

    public int getGuideId() {
        return GuideId;
    }

    public void setGuideId(int guideId) {
        GuideId = guideId;
    }

    public UserBrief getCommentator() {
        return Commentator;
    }

    public void setCommentator(UserBrief commentator) {
        Commentator = commentator;
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

    public int getStars() {
        return Stars;
    }

    public void setStars(int stars) {
        Stars = stars;
    }
}
