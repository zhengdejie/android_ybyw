package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017-01-17.
 */

public class RecommendOrder extends Http.BaseDto implements Serializable {
    public int RecommendId;
    public UserDetail UserDetail;
    public String Title;
    public String Content;
    public String Position;
    public String Workingyears;
    public String ServiceContent;
    public String WorkingPictures;
    public String Selfevaluation;

    public int getRecommendId() {
        return RecommendId;
    }

    public void setRecommendId(int recommendId) {
        RecommendId = recommendId;
    }

    public appframe.appframe.dto.UserDetail getUserDetail() {
        return UserDetail;
    }

    public void setUserDetail(appframe.appframe.dto.UserDetail userDetail) {
        UserDetail = userDetail;
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

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getWorkingyears() {
        return Workingyears;
    }

    public void setWorkingyears(String workingyears) {
        Workingyears = workingyears;
    }

    public String getServiceContent() {
        return ServiceContent;
    }

    public void setServiceContent(String serviceContent) {
        ServiceContent = serviceContent;
    }

    public String getWorkingPictures() {
        return WorkingPictures;
    }

    public void setWorkingPictures(String workingPictures) {
        WorkingPictures = workingPictures;
    }

    public String getSelfevaluation() {
        return Selfevaluation;
    }

    public void setSelfevaluation(String selfevaluation) {
        Selfevaluation = selfevaluation;
    }
}
