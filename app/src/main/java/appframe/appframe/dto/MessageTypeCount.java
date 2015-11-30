package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/20.
 */
public class MessageTypeCount extends Http.BaseDto{
    public int Type;
    public int Count;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
