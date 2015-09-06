package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/8/5.
 */
public class UserContact extends Http.BaseDto{
    public String ContactName;
    public String ContactMobile;

    public UserContact(String contactName, String contactMobile) {
        ContactName = contactName;
        ContactMobile = contactMobile;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactMobile() {
        return ContactMobile;
    }

    public void setContactMobile(String contactMobile) {
        ContactMobile = contactMobile;
    }
}