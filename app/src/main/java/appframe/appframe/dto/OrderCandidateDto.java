package appframe.appframe.dto;

import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016-07-25.
 */
public class OrderCandidateDto extends Http.BaseDto{
    public OrderDetails Order;
    public List<ConfirmedOrderDetail> CandidateList;

    public OrderDetails getOrder() {
        return Order;
    }

    public void setOrder(OrderDetails order) {
        Order = order;
    }

    public List<ConfirmedOrderDetail> getCandidateList() {
        return CandidateList;
    }

    public void setCandidateList(List<ConfirmedOrderDetail> candidateList) {
        CandidateList = candidateList;
    }
}
