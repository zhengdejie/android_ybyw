package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/9.
 */
public class ConfirmedOrderDetail extends Http.BaseDto implements Serializable {
    private int Id;
    private OrderDetails Order;
    private UserDetail Poster;
    private UserDetail Receiver;
    private String CreatedAt;
    private String CompleteDate;
    private int Type;
    private int Status;
    private UserDetail ServiceProvider;
    private UserDetail ServiceReceiver;
    private double Bid;
    private String ConfirmedOrderNumber;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public OrderDetails getOrder() {
        return Order;
    }

    public void setOrder(OrderDetails order) {
        Order = order;
    }

    public UserDetail getPoster() {
        return Poster;
    }

    public void setPoster(UserDetail poster) {
        Poster = poster;
    }

    public UserDetail getReceiver() {
        return Receiver;
    }

    public void setReceiver(UserDetail receiver) {
        Receiver = receiver;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getCompleteDate() {
        return CompleteDate;
    }

    public void setCompleteDate(String completeDate) {
        CompleteDate = completeDate;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public UserDetail getServiceProvider() {
        return ServiceProvider;
    }

    public void setServiceProvider(UserDetail serviceProvider) {
        ServiceProvider = serviceProvider;
    }

    public UserDetail getServiceReceiver() {
        return ServiceReceiver;
    }

    public void setServiceReceiver(UserDetail serviceReceiver) {
        ServiceReceiver = serviceReceiver;
    }

    public double getBid() {
        return Bid;
    }

    public void setBid(double bid) {
        Bid = bid;
    }

    public String getConfirmedOrderNumber() {
        return ConfirmedOrderNumber;
    }

    public void setConfirmedOrderNumber(String confirmedOrderNumber) {
        ConfirmedOrderNumber = confirmedOrderNumber;
    }
}
