package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/9/15.
 */
public class OrderReviewDetail extends Http.BaseDto{
    private String Title;
    private String Content;
    private String CreatedAt;
    private UserDetail User;
    private OrderDetails Order;
    private double ServicePoint;
    private double AttitudePoint;
    private double CharacterPoint;
    private String Photos;

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

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public UserDetail getUser() {
        return User;
    }

    public void setUser(UserDetail user) {
        User = user;
    }

    public OrderDetails getOrder() {
        return Order;
    }

    public void setOrder(OrderDetails order) {
        Order = order;
    }

    public double getServicePoint() {
        return ServicePoint;
    }

    public void setServicePoint(double servicePoint) {
        ServicePoint = servicePoint;
    }

    public double getAttitudePoint() {
        return AttitudePoint;
    }

    public void setAttitudePoint(double attitudePoint) {
        AttitudePoint = attitudePoint;
    }

    public double getCharacterPoint() {
        return CharacterPoint;
    }

    public void setCharacterPoint(double characterPoint) {
        CharacterPoint = characterPoint;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }
}
