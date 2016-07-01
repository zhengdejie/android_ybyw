package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016-06-29.
 */
public class SearchResult extends Http.BaseDto implements Serializable {
    private List<OrderDetails> Orders;
    private List<Question> Questions;

    public List<OrderDetails> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrderDetails> orders) {
        Orders = orders;
    }

    public List<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<Question> questions) {
        Questions = questions;
    }
}
