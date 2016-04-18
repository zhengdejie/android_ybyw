package appframe.appframe.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/8/28.
 */
public class OrderDetails extends Http.BaseDto implements Serializable {
    private int Id ;
    private String Title;
    private double Bounty;
    private String Address;
    private String Category;
    private String Content;
    private String Photos;
    private int Visibility;
    private int NameAnonymity;
    private int LocationAnonymity;
    private int PhoneAnonymity;
    private int Type;
    private String Deadline;
    private String request;
    private String PaymentMethod;
    private String CreatedAt;
    private String UpdatedAt;
    private String AcceptedAt;
    private String OrderStatus;
    private UserDetail Orderer;
    private UserDetail Orderee;
    private List<ConfirmedOrderDetail> Candidate;
    private int  BossPaid;
    private String Tags;



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

    public double getBounty() {
        return Bounty;
    }

    public void setBounty(double bounty) {
        Bounty = bounty;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public int getVisibility() {
        return Visibility;
    }

    public void setVisibility(int visibility) {
        Visibility = visibility;
    }

    public int getNameAnonymity() {
        return NameAnonymity;
    }

    public void setNameAnonymity(int nameAnonymity) {
        NameAnonymity = nameAnonymity;
    }

    public int getLocationAnonymity() {
        return LocationAnonymity;
    }

    public void setLocationAnonymity(int locationAnonymity) {
        LocationAnonymity = locationAnonymity;
    }

    public int getPhoneAnonymity() {
        return PhoneAnonymity;
    }

    public void setPhoneAnonymity(int phoneAnonymity) {
        PhoneAnonymity = phoneAnonymity;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public String getAcceptedAt() {
        return AcceptedAt;
    }

    public void setAcceptedAt(String acceptedAt) {
        AcceptedAt = acceptedAt;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
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

    public List<ConfirmedOrderDetail> getCandidate() {
        return Candidate;
    }

    public void setCandidate(List<ConfirmedOrderDetail> candidate) {
        Candidate = candidate;
    }

    public int getBossPaid() {
        return BossPaid;
    }

    public void setBossPaid(int bossPaid) {
        BossPaid = bossPaid;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }
}
