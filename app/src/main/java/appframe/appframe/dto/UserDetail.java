package appframe.appframe.dto;

import java.io.Serializable;

/**
 * Created by dashi on 15/6/11.
 */
public class UserDetail extends UserBrief implements Serializable {
    public String Check = "UnCheck";

    public String getCheck() {
        return Check;
    }

    public void setCheck(String check) {
        Check = check;
    }
}
