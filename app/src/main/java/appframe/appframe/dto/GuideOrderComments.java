package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2017/7/20.
 */

public class GuideOrderComments extends Http.BaseDto implements Serializable {
    private int TotalComments;
    private List<GuideComments> Comments;

    public int getTotalComments() {
        return TotalComments;
    }

    public void setTotalComments(int totalComments) {
        TotalComments = totalComments;
    }

    public List<GuideComments> getComments() {
        return Comments;
    }

    public void setComments(List<GuideComments> comments) {
        Comments = comments;
    }
}
