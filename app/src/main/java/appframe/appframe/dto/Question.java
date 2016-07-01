package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/5/18.
 */
public class Question extends Http.BaseDto implements Serializable {
    private int Id;
    private String CreatedAt;
    public UserBrief Asker;
    public String Title;
    public String Content;
    public String Photos;
    public AnswerDetail AcceptedAnswer;
    public boolean IsActive;
    public double Bounty;
    public int Status;
    public int TotalAnswers;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public UserBrief getAsker() {
        return Asker;
    }

    public void setAsker(UserBrief asker) {
        Asker = asker;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public AnswerDetail getAcceptedAnswer() {
        return AcceptedAnswer;
    }

    public void setAcceptedAnswer(AnswerDetail acceptedAnswer) {
        AcceptedAnswer = acceptedAnswer;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setIsActive(boolean isActive) {
        IsActive = isActive;
    }

    public double getBounty() {
        return Bounty;
    }

    public void setBounty(double bounty) {
        Bounty = bounty;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getTotalAnswers() {
        return TotalAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        TotalAnswers = totalAnswers;
    }
}
