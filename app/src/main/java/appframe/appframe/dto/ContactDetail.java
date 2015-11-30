package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/17.
 */
public class ContactDetail extends Http.BaseDto {
    private ContactInfo MobileContact;
    private UserDetail User;
    private int Type;
    private String sortLetters;
    public String Check = "UnCheck";

    public String getCheck() {
        return Check;
    }

    public void setCheck(String check) {
        Check = check;
    }

    public ContactInfo getMobileContact() {
        return MobileContact;
    }

    public void setMobileContact(ContactInfo mobileContact) {
        MobileContact = mobileContact;
    }

    public UserDetail getUser() {
        return User;
    }

    public void setUser(UserDetail user) {
        User = user;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
