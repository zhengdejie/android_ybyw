package appframe.appframe.dto;

import java.io.Serializable;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/4/5.
 */
public class ConfirmedOrderDetailWithFriend extends ConfirmedOrderDetail implements Serializable {
    private int FirstDegreeFriendId;

    public int getFirstDegreeFriendId() {
        return FirstDegreeFriendId;
    }

    public void setFirstDegreeFriendId(int firstDegreeFriendId) {
        FirstDegreeFriendId = firstDegreeFriendId;
    }
}
