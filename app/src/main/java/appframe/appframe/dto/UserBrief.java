package appframe.appframe.dto;

import java.util.Date;

import appframe.appframe.utils.Http;

/**
 * Created by dashi on 15/6/11.
 */
public class UserBrief extends Http.BaseDto{

    public int Id;
    public String Name;
    public String Avatar;
    public String Mobile;
    public Date CreatedAt;

    public Date UpdatedAt;
}
