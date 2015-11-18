package appframe.appframe.app;

import java.util.List;

import appframe.appframe.dto.AuthResult;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.ContactDetail;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserContact;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Http;

/**
 * Created by dashi on 15/6/11.
 */
public final class API {
    static String HOST = "192.168.31.123";   //42.96.152.105       //192.168.31.123
    public static String API_BASE = "http://" + HOST + ":1337";
    public static String[] OFFICAL_HOSTS = new String[]{"localhost", HOST};

    public static final Http.API<AuthResult> USER_LOGIN = Http.API.post("/login.json", AuthResult.class);
    public static final Http.API<AuthResult> USER_REGISTER = Http.API.post("/register.json", AuthResult.class);
    public static final Http.API<UserDetail> USER_PROFILE = Http.API.get("/profile/%s.json", UserDetail.class);
    public static final Http.API<UserDetail> USER_PROFILE_UPDATE = Http.API.post("/profile/%s/update.json", UserDetail.class);
    public static final Http.API<UserDetail> ORDER_SEND = Http.API.post("/order/place.json", UserDetail.class);
    public static final Http.API<List<OrderDetails>> GET_SELFORDER = Http.API.getList("/order/self.json", OrderDetails.class);
    public static final Http.API<List<UserDetail>> GET_SECOND = Http.API.getList("/contact/%s/second.json", UserDetail.class);
    public static final Http.API<List<OrderDetails>> GET_ORDER = Http.API.getList("/order/friends.json%s", OrderDetails.class);
    public static final Http.API<UserDetail> EVALUATION_ORDER = Http.API.post("/review/order/make.json", UserDetail.class);
    public static final Http.API<List<OrderReviewDetail>> GET_ORDEREVALUATION = Http.API.getList("/review/order/%s.json", OrderReviewDetail.class);
    public static final Http.API USER_CONTACT_UPLOAD = Http.API.postEmpty("/contact/upload.json");
    public static final Http.API<UserDetail> ORDER_ACCEPT = Http.API.post("/order/%s/accept.json", UserDetail.class);
    public static final Http.API<List<OrderDetails>> SEARCH_ORDER = Http.API.getList("/order/search.json%s", OrderDetails.class);
    public static final Http.API USER_FEEDBACK = Http.API.postEmpty("/%s/feedback.json");
    public static final Http.API ORDER_MAKECOOMENT = Http.API.postEmpty("/Order/%s/makecomment.json");
    public static final Http.API<List<OrderComment>> ORDER_GETCOOMENT = Http.API.getList("/Order/%s/getComment.json", OrderComment.class);
    public static final Http.API ORDER_DELETECOOMENT = Http.API.deleteEmpty("/Order/DeleteComment.json%s");
    public static final Http.API<List<OrderDetails>> GET_FAVORITEORDER = Http.API.getList("/favoriteorder.json", OrderDetails.class);
    public static final Http.API ADD_FAVORITEORDER = Http.API.postEmpty("/favoriteorder/add.json");
    public static final Http.API DELETE_FAVORITEORDER = Http.API.deleteEmpty("/favoriteorder/%s.json");
    public static final Http.API<List<ConfirmedOrderDetail>> GET_CONFIRMEDORDER = Http.API.getList("/confirmedOrder/display.json", ConfirmedOrderDetail.class);
    public static final Http.API CONFIRMORDER = Http.API.postEmpty("/order/%s/confirm.json");
    public static final Http.API<List<UserDetail>> GET_CANDIDATE = Http.API.getList("/order/%s/getcandidate.json", UserDetail.class);
    public static final Http.API<Token> GetQINIUUploadToken = Http.API.get("/photos/gettoken.json", Token.class);
    public static final Http.API<List<PushMessage>> GET_PUSHMESSAGE = Http.API.getList("/pushmessage/get.json%s", PushMessage.class);
    public static final Http.API<List<ContactDetail>> GET_CONTACT = Http.API.getList("/contact/mobile.json", ContactDetail.class);
    public static final Http.API ADD_FDF = Http.API.postEmpty("/profile/%s/addFDF.json");
    public static final Http.API ACCEPT_ADDFDF = Http.API.postEmpty("/profile/%s/acceptAddFDF.json");
    public static final Http.API<List<Nearby>> GET_USERNEARBY = Http.API.getList("/usernearby/get.json%s", Nearby.class);
}
