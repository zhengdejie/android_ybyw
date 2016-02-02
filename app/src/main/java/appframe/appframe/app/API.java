package appframe.appframe.app;

import java.util.List;

import appframe.appframe.dto.APKVersion;
import appframe.appframe.dto.AuthResult;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.ConfirmedOrderUndoCount;
import appframe.appframe.dto.ContactDetail;
import appframe.appframe.dto.FriendEvaluationDetail;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetailAndCount;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.dto.OrderReviewDetailAndCount;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.dto.SelfEvaluationDetail;
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
    public static final Http.API<OrderDetailAndCount> GET_ORDER = Http.API.get("/order/friends.json%s", OrderDetailAndCount.class);
    public static final Http.API<UserDetail> EVALUATION_ORDER = Http.API.post("/review/order/make.json", UserDetail.class);
    public static final Http.API<List<OrderReviewDetail>> GET_ORDEREVALUATION = Http.API.getList("/review/order/%s.json", OrderReviewDetail.class);
    public static final Http.API<OrderReviewDetailAndCount> GET_ORDEREVALUATIONBYUSER = Http.API.get("/review/order/user/%s.json%s", OrderReviewDetailAndCount.class);
    public static final Http.API USER_CONTACT_UPLOAD = Http.API.postEmpty("/contact/upload.json");
    public static final Http.API<UserDetail> ORDER_ACCEPT = Http.API.post("/order/%s/accept.json", UserDetail.class);
    public static final Http.API<List<OrderDetails>> SEARCH_ORDER = Http.API.getList("/order/search.json%s", OrderDetails.class);
    public static final Http.API<List<UserDetail>> SEARCH_USER = Http.API.getList("/profile/search.json%s", UserDetail.class);
    public static final Http.API USER_FEEDBACK = Http.API.postEmpty("/%s/feedback.json");
    public static final Http.API ORDER_MAKECOOMENT = Http.API.postEmpty("/Order/%s/makecomment.json");
    public static final Http.API<List<OrderComment>> ORDER_GETCOOMENT = Http.API.getList("/Order/%s/getComment.json", OrderComment.class);
    public static final Http.API ORDER_DELETECOOMENT = Http.API.deleteEmpty("/Order/DeleteComment.json%s");
    public static final Http.API<List<OrderDetails>> GET_FAVORITEORDER = Http.API.getList("/favoriteorder.json", OrderDetails.class);
    public static final Http.API ADD_FAVORITEORDER = Http.API.postEmpty("/favoriteorder/add.json");
    public static final Http.API DELETE_FAVORITEORDER = Http.API.deleteEmpty("/favoriteorder/%s.json");
    public static final Http.API<List<ConfirmedOrderDetail>> GET_CONFIRMEDORDER = Http.API.getList("/confirmedOrder/display.json%s", ConfirmedOrderDetail.class);
    public static final Http.API CONFIRMORDER = Http.API.postEmpty("/order/%s/confirm.json");
    public static final Http.API<List<UserDetail>> GET_CANDIDATE = Http.API.getList("/order/%s/getcandidate.json", UserDetail.class);
    public static final Http.API<Token> GetQINIUUploadToken = Http.API.get("/photos/gettoken.json", Token.class);
    public static final Http.API<List<PushMessage>> GET_PUSHMESSAGE = Http.API.getList("/pushmessage/get.json%s", PushMessage.class);
    public static final Http.API<List<ContactDetail>> GET_YBFRIEND = Http.API.getList("/contact/getybfriends.json", ContactDetail.class);
    public static final Http.API<List<ContactDetail>> GET_MOBILEFRIEND = Http.API.getList("/contact/getmobilefriends.json", ContactDetail.class);
    public static final Http.API ADD_FDF = Http.API.postEmpty("/profile/%s/addFDF.json");
    public static final Http.API ACCEPT_ADDFDF = Http.API.postEmpty("/profile/%s/acceptAddFDF.json");
    public static final Http.API<List<Nearby>> GET_USERNEARBY = Http.API.getList("/usernearby/get.json%s", Nearby.class);
    public static final Http.API CLOSE_ORDER = Http.API.putEmpty("/order/close.json");
    public static final Http.API<List<MessageTypeCount>> GET_UNREAD = Http.API.getList("/pushmessage/getunread.json%s", MessageTypeCount.class);
    public static final Http.API MARK_ALLREAD = Http.API.putEmpty("/pushmessage/markallread.json");
    public static final Http.API<MessageTypeCount> HAS_UNREAD = Http.API.get("/pushmessage/hasunread.json", MessageTypeCount.class);
    public static final Http.API CHANGE_STATUS = Http.API.put("/confirmedOrder/%s/changestatus.json", ConfirmedOrderDetail.class);
    public static final Http.API REJECT_PAYMENT = Http.API.post("/confirmedOrder/%s/rejectpayment.json", ConfirmedOrderDetail.class);
    public static final Http.API REFUND_AGREE = Http.API.post("/confirmedOrder/%s/agreerefund.json", ConfirmedOrderDetail.class);
    public static final Http.API REFUND_DISAGREE = Http.API.post("/confirmedOrder/%s/rejectrefund.json", ConfirmedOrderDetail.class);
    public static final Http.API ORDER_CANCEL = Http.API.deleteEmpty("/confirmedOrder/%s/cancelpending.json%s");
    public static final Http.API RECOMMEND_ORDER = Http.API.postEmpty("/order/%s/referto.json");
    public static final Http.API ORDER_COMPLETE = Http.API.post("/confirmedOrder/%s/complete.json", ConfirmedOrderDetail.class);
    public static final Http.API ORDER_CONFIRMCOMPLETE = Http.API.post("/confirmedOrder/%s/confirmcomplete.json", ConfirmedOrderDetail.class);
    public static final Http.API<List<UserDetail>> GET_NOTSEEB = Http.API.getList("/userBlock/getnotseeb.json%s", UserDetail.class);
    public static final Http.API<List<UserDetail>> GET_NOTLETBSEE = Http.API.getList("/userBlock/getnotletbsee.json%s", UserDetail.class);
    public static final Http.API<List<UserDetail>> GET_BLACKLIST = Http.API.getList("/user/%s/getblacklist.json", UserDetail.class);
    public static final Http.API POST_NOTSEEB = Http.API.postEmpty("/userBlock/notSeeB.json");
    public static final Http.API POST_NOTLETBSEE = Http.API.postEmpty("/userBlock/notLetBSee.json");
    public static final Http.API ADD_BLACKLIST = Http.API.postEmpty("/user/%s/addtoblacklist.json");
    public static final Http.API POST_NOTSEEBCANCLE = Http.API.postEmpty("/userBlock/cancelNotSeeB.json");
    public static final Http.API POST_NOTLETBSEECANCLE = Http.API.postEmpty("/userBlock/cancelNotLetBSee.json");
    public static final Http.API REMOVE_BLACKLIST = Http.API.deleteEmpty("/user/%s/removefromblacklist.json%s");
    public static final Http.API CHECK_OLDPASSWORD = Http.API.postEmpty("/user/checkoldpassword.json");
    public static final Http.API FRIENDS_EVALUATION = Http.API.postEmpty("/profile/%s/friendEval.json");
    public static final Http.API<List<FriendEvaluationDetail>> GET_FEVALUATION = Http.API.getList("/profile/%s/friendEval.json", FriendEvaluationDetail.class);
    public static final Http.API<SelfEvaluationDetail> GET_SEVALUATION = Http.API.get("/profile/%s/selfeval.json", SelfEvaluationDetail.class);
    public static final Http.API<UserDetail> POST_SELFEVALUATION = Http.API.post("/profile/%s/selfeval.json", UserDetail.class);
    public static final Http.API<APKVersion> GET_APKVERSION = Http.API.get(AppConfig.QINIU_HOST + AppConfig.QINIU_APKVERSION, APKVersion.class);
    public static final Http.API<List<UserDetail>> GET_MIDDLEMAN = Http.API.getList("/contact/%s/middleman.json%s", UserDetail.class);
    public static final Http.API UPDATE_FRIENDNICK = Http.API.postEmpty("/friendnick/%s.json");
    public static final Http.API<List<UserDetail>> GET_HOTSELLER = Http.API.getList("/topseller/get.json%s", UserDetail.class);
    public static final Http.API ORDER_HURRYUP = Http.API.getEmpty("/confirmedOrder/%s/hurryup.json");
    public static final Http.API ORDER_DISPUTE = Http.API.postEmpty("/confirmedOrder/%s/dispute.json");
    public static final Http.API<ConfirmedOrderUndoCount> GET_ORDER_NUM = Http.API.get("/confirmedOrder/undo.json",ConfirmedOrderUndoCount.class);
}
