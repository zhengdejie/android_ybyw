package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/8/5.
 */
public class UserContact extends Http.BaseDto{
    public String Name;
    public String Mobile;

    public UserContact(String name, String mobile) {
        Name = name;
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}