package appframe.appframe.dto;

import java.io.Serializable;
import java.util.List;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2016/3/28.
 */
public class Friendship extends Http.BaseDto implements Serializable {
    private int Type;
    private List<UserDetail> Midman;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public List<UserDetail> getMidman() {
        return Midman;
    }

    public void setMidman(List<UserDetail> midman) {
        Midman = midman;
    }
}
