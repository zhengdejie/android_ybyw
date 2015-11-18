package appframe.appframe.dto;

import com.alibaba.sdk.android.session.model.User;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/12.
 */
public class PushMessage extends Http.BaseDto {
    private String Title;
    private String Content ;
    private String CreatedAt ;
    private int Status ;
    private int ReceiverID ;
    private int Type;
    private OrderDetails Order;
    private UserDetail Sender;

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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(int receiverID) {
        ReceiverID = receiverID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public OrderDetails getOrder() {
        return Order;
    }

    public void setOrder(OrderDetails order) {
        Order = order;
    }

    public UserDetail getSender() {
        return Sender;
    }

    public void setSender(UserDetail sender) {
        Sender = sender;
    }
}
