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
}
