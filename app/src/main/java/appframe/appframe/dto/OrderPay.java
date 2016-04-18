package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/3/2.
 */
public class OrderPay extends Http.BaseDto implements Serializable {
    private  int OrderId;
    private double Amount;
    private String CandidateId;
    private String OrderStatus;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getCandidateId() {
        return CandidateId;
    }

    public void setCandidateId(String candidateId) {
        CandidateId = candidateId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
