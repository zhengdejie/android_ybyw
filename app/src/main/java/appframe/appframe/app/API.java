package appframe.appframe.app;

import appframe.appframe.dto.AuthResult;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Http;

/**
 * Created by dashi on 15/6/11.
 */
public final class API {
    static String HOST = "192.168.31.124";
    public static String API_BASE = "http://" + HOST + ":1337";
    public static String[] OFFICAL_HOSTS = new String[]{"localhost", HOST};

    public static final Http.API<AuthResult> USER_LOGIN = Http.API.post("/login.json", AuthResult.class);
    public static final Http.API<AuthResult> USER_REGISTER = Http.API.post("/register.json", AuthResult.class);
    public static final Http.API<UserDetail> USER_PROFILE = Http.API.get("/profile/%s.json", UserDetail.class);
    public static final Http.API<UserDetail> USER_PROFILE_UPDATE = Http.API.post("/profile/%s/update.json", UserDetail.class);
    public static final Http.API<UserDetail> ORDER_SEND = Http.API.post("/order/%s/place.json",UserDetail.class);
    public static final Http.API<AuthResult> USER_CONTACT_UPLOAD = Http.API.post("/contactupload.json", AuthResult.class);
}
