package appframe.appframe.dto;

import appframe.appframe.utils.Http;

/**
 * Created by dashi on 15/6/11.
 */
public class AuthResult extends Http.BaseDto {
    public String Token;
    public UserDetail User;
}

