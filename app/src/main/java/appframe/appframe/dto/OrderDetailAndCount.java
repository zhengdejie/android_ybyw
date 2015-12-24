package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/12/15.
 */
public class OrderDetailAndCount extends Http.BaseDto implements Serializable {
    private List<OrderDetails> OrderList;
    private int Count;

    public List<OrderDetails> getOrderList() {
        return OrderList;
    }

    public void setOrderList(List<OrderDetails> orderList) {
        OrderList = orderList;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
