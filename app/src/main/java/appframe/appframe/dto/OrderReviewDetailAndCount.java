package appframe.appframe.dto;

import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/1/13.
 */
public class OrderReviewDetailAndCount extends Http.BaseDto {
    private List<OrderReviewDetail> orderReviewDetails;
    private int Count;
    private int AllCount;
    private int GoodCount;
    private int MediumCount;
    private int BadCount;
    private UserBrief User;

    public List<OrderReviewDetail> getOrderReviewDetails() {
        return orderReviewDetails;
    }

    public void setOrderReviewDetails(List<OrderReviewDetail> orderReviewDetails) {
        this.orderReviewDetails = orderReviewDetails;
    }


    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public int getGoodCount() {
        return GoodCount;
    }

    public void setGoodCount(int goodCount) {
        GoodCount = goodCount;
    }

    public int getMediumCount() {
        return MediumCount;
    }

    public void setMediumCount(int mediumCount) {
        MediumCount = mediumCount;
    }

    public int getBadCount() {
        return BadCount;
    }

    public void setBadCount(int badCount) {
        BadCount = badCount;
    }

    public UserBrief getUser() {
        return User;
    }

    public void setUser(UserBrief user) {
        User = user;
    }
}
