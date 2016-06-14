package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/5/18.
 */
public class AnswerDetail extends Http.BaseDto implements Serializable {
    public UserBrief Answerer;
    public int Id;
    public String Content;
    public String CreatedAt;
    public String UpdatedAt;

    public UserBrief getAnswerer() {
        return Answerer;
    }

    public void setAnswerer(UserBrief answerer) {
        Answerer = answerer;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }
}
