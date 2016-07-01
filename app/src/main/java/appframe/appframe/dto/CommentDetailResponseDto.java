package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016-06-27.
 */
public class CommentDetailResponseDto extends Http.BaseDto implements Serializable {
    private List<OrderComment> list;
    private int TotalCount;

    public List<OrderComment> getList() {
        return list;
    }

    public void setList(List<OrderComment> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
