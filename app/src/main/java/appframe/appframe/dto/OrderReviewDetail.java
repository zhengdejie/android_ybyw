package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/9/15.
 */
public class OrderReviewDetail extends Http.BaseDto{
    private String Title;
    private String Content;
    private String CommontatorName;

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

    public String getCommontatorName() {
        return CommontatorName;
    }

    public void setCommontatorName(String commontatorName) {
        CommontatorName = commontatorName;
    }
}
