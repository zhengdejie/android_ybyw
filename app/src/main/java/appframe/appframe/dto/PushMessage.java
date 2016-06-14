package appframe.appframe.dto;



import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/12.
 */
public class PushMessage extends Http.BaseDto {
    private int Id;
    private String Title;
    private String Content ;
    private String CreatedAt ;
    private int Status ;
    private int ReceiverID ;
    private int Type;
    private OrderDetails Order;
    private UserDetail Sender;
    private ConfirmedOrderDetail ConfirmedOrder;
    private int FriendRequestAccepted;
    private int ObjectId;
    private Question ObjectDetail;

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

    public ConfirmedOrderDetail getConfirmedOrder() {
        return ConfirmedOrder;
    }

    public void setConfirmedOrder(ConfirmedOrderDetail confirmedOrder) {
        ConfirmedOrder = confirmedOrder;
    }

    public int getFriendRequestAccepted() {
        return FriendRequestAccepted;
    }

    public void setFriendRequestAccepted(int friendRequestAccepted) {
        FriendRequestAccepted = friendRequestAccepted;
    }

    public int getObjectId() {
        return ObjectId;
    }

    public void setObjectId(int objectId) {
        ObjectId = objectId;
    }

    public Question getObjectDetail() {
        return ObjectDetail;
    }

    public void setObjectDetail(Question objectDetail) {
        ObjectDetail = objectDetail;
    }
}
