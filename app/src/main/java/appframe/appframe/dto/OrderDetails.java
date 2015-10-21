package appframe.appframe.dto;

import java.io.Serializable;
import java.util.Date;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/8/28.
 */
public class OrderDetails extends Http.BaseDto implements Serializable {
    private int Id ;
    private String Title;
    private String Content;
    private String Position;
    private Date CreatedAt;
    private String Category;
    private String OrderStatus;
    private int Visibility;
    private Date Deadline;
    private String PaymentMethod;
    private float Bounty;
    private UserDetail Orderer;
    private UserDetail Orderee;




    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public int getVisibility() {
        return Visibility;
    }

    public void setVisibility(int visibility) {
        Visibility = visibility;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date deadline) {
        Deadline = deadline;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public float getBounty() {
        return Bounty;
    }

    public void setBounty(float bounty) {
        Bounty = bounty;
    }

    public UserDetail getOrderer() {
        return Orderer;
    }

    public void setOrderer(UserDetail orderer) {
        Orderer = orderer;
    }

    public UserDetail getOrderee() {
        return Orderee;
    }

    public void setOrderee(UserDetail orderee) {
        Orderee = orderee;
    }


}
