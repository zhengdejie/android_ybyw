package appframe.appframe.app;

import java.util.List;

import appframe.appframe.dto.APKVersion;
import appframe.appframe.dto.AnswerDetail;
import appframe.appframe.dto.AnswerDetailWithQuestionDetail;
import appframe.appframe.dto.AuthResult;
import appframe.appframe.dto.CommentDetailResponseDto;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.dto.ConfirmedOrderUndoCount;
import appframe.appframe.dto.ContactDetail;
import appframe.appframe.dto.FriendEvaluationDetail;
import appframe.appframe.dto.Friendship;
import appframe.appframe.dto.GuideOrderComments;
import appframe.appframe.dto.GuideTour;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.dto.MRussianTour;
import appframe.appframe.dto.MessageTypeCount;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderCandidateDto;
import appframe.appframe.dto.OrderCategory;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetailAndCount;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderReviewDetail;
import appframe.appframe.dto.OrderReviewDetailAndCount;
import appframe.appframe.dto.PhotoResponse;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.QuestionWithAnswers;
import appframe.appframe.dto.Recommend;
import appframe.appframe.dto.RecommendOrder;
import appframe.appframe.dto.RecommendOrderQuestion;
import appframe.appframe.dto.ReportCategory;
import appframe.appframe.dto.RussianPay;
import appframe.appframe.dto.RussianTour;
import appframe.appframe.dto.SearchOrderTagResponse;
import appframe.appframe.dto.SearchResult;
import appframe.appframe.dto.SelfEvaluationDetail;
import appframe.appframe.dto.OnlinePay;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserAskQuestion;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Http;

/**
 * Created by dashi on 15/6/11.
 */
public final class API {
    static String HOST = "api.ubangwang.com";          //192.168.31.123        //114.55.100.9   //测试服务器  42.96.152.105
    public static String API_BASE = "http://" + HOST + ":1337";
    public static String[] OFFICAL_HOSTS = new String[]{"localhost", HOST};

    public static final Http.API<AuthResult> USER_LOGIN = Http.API.post("/login.json", AuthResult.class);
    public static final Http.API<AuthResult> USER_REGISTER = Http.API.post("/register.json", AuthResult.class);
    public static final Http.API<UserDetail> USER_PROFILE = Http.API.get("/profile/%s.json", UserDetail.class);
    public static final Http.API<UserDetail> USER_PROFILE_UPDATE = Http.API.post("/profile/%s/update.json", UserDetail.class);
    public static final Http.API<OrderDetails> ORDER_SEND = Http.API.post("/order/place.json", OrderDetails.class);
    public static final Http.API<List<OrderDetails>> GET_SELFORDER = Http.API.getList("/order/self.json%s", OrderDetails.class);
    public static final Http.API<List<UserDetail>> GET_SECOND = Http.API.getList("/contact/%s/second.json", UserDetail.class);
    public static final Http.API<OrderDetailAndCount> GET_ORDER = Http.API.get("/order/friends.json%s", OrderDetailAndCount.class);
    public static final Http.API<UserDetail> EVALUATION_ORDER = Http.API.post("/review/order/make.json", UserDetail.class);
    public static final Http.API<List<OrderReviewDetail>> GET_ORDEREVALUATION = Http.API.getList("/review/order/%s.json", OrderReviewDetail.class);
    public static final Http.API<OrderReviewDetailAndCount> GET_ORDEREVALUATIONBYUSER = Http.API.get("/review/order/user/%s.json%s", OrderReviewDetailAndCount.class);
    public static final Http.API USER_CONTACT_UPLOAD = Http.API.postEmpty("/contact/upload.json");
    public static final Http.API<ConfirmedOrderDetail> ORDER_ACCEPT = Http.API.post("/order/%s/accept.json", ConfirmedOrderDetail.class);
    public static final Http.API<SearchResult> SEARCH_ORDER = Http.API.get("/order/search.json%s", SearchResult.class);
    public static final Http.API<SearchOrderTagResponse> SEARCH_ORDERTAG = Http.API.get("/ordertag/search.json%s", SearchOrderTagResponse.class);
    public static final Http.API<List<UserDetail>> SEARCH_USER = Http.API.getList("/profile/search.json%s", UserDetail.class);
    public static final Http.API USER_FEEDBACK = Http.API.postEmpty("/%s/feedback.json");
    public static final Http.API ORDER_MAKECOOMENT = Http.API.postEmpty("/Order/%s/makecomment.json");
    public static final Http.API<CommentDetailResponseDto> ORDER_GETCOOMENT = Http.API.get("/Order/%s/getComment.json%s", CommentDetailResponseDto.class);
    public static final Http.API ORDER_DELETECOOMENT = Http.API.deleteEmpty("/Order/DeleteComment.json%s");
    public static final Http.API<List<OrderDetails>> GET_FAVORITEORDER = Http.API.getList("/favoriteorder.json%s", OrderDetails.class);
    public static final Http.API ADD_FAVORITEORDER = Http.API.postEmpty("/favoriteorder/add.json");
    public static final Http.API DELETE_FAVORITEORDER = Http.API.deleteEmpty("/favoriteorder/%s.json");
    public static final Http.API<List<ConfirmedOrderDetail>> GET_CONFIRMEDORDER = Http.API.getList("/confirmedOrder/display.json%s", ConfirmedOrderDetail.class);
    public static final Http.API<OrderDetails> CONFIRMORDER = Http.API.post("/order/%s/confirm.json", OrderDetails.class);
    public static final Http.API<OrderCandidateDto> GET_CANDIDATE = Http.API.get("/order/%s/getcandidate.json", OrderCandidateDto.class);
    public static final Http.API<Token> GetQINIUUploadToken = Http.API.get("/photos/gettoken.json", Token.class);
    public static final Http.API<List<PushMessage>> GET_PUSHMESSAGE = Http.API.getList("/pushmessage/get.json%s", PushMessage.class);
    public static final Http.API<List<ContactDetail>> GET_YBFRIEND = Http.API.getList("/contact/getybfriends.json", ContactDetail.class);
    public static final Http.API<List<ContactDetail>> GET_MOBILEFRIEND = Http.API.getList("/contact/getmobilefriends.json", ContactDetail.class);
    public static final Http.API ADD_FDF = Http.API.postEmpty("/profile/%s/addFDF.json");
    public static final Http.API ACCEPT_ADDFDF = Http.API.postEmpty("/profile/%s/acceptAddFDF.json");
    public static final Http.API<List<Nearby>> GET_USERNEARBY = Http.API.getList("/usernearby/get.json%s", Nearby.class);
    public static final Http.API CLOSE_ORDER = Http.API.putEmpty("/order/close.json");
    public static final Http.API CANCEL_PAY = Http.API.postEmpty("/order/canceladditionalpay.json");
    public static final Http.API<List<MessageTypeCount>> GET_UNREAD = Http.API.getList("/pushmessage/getunread.json%s", MessageTypeCount.class);
    public static final Http.API MARK_ALLREAD = Http.API.putEmpty("/pushmessage/markallread.json");
    public static final Http.API<MessageTypeCount> HAS_UNREAD = Http.API.get("/pushmessage/hasunread.json", MessageTypeCount.class);
    public static final Http.API CHANGE_STATUS = Http.API.put("/confirmedOrder/%s/changestatus.json", ConfirmedOrderDetail.class);
    public static final Http.API ORDER_CLOSE = Http.API.postEmpty("/confirmedOrder/cancel.json");
    public static final Http.API<ConfirmedOrderDetail> REJECT_PAYMENT = Http.API.post("/confirmedOrder/%s/rejectpayment.json", ConfirmedOrderDetail.class);
    public static final Http.API<ConfirmedOrderDetail> REFUND_AGREE = Http.API.post("/confirmedOrder/%s/agreerefund.json", ConfirmedOrderDetail.class);
    public static final Http.API<ConfirmedOrderDetail> REFUND_DISAGREE = Http.API.post("/confirmedOrder/%s/rejectrefund.json", ConfirmedOrderDetail.class);
    public static final Http.API<ConfirmedOrderDetail> ORDER_CANCEL = Http.API.delete("/confirmedOrder/%s/cancelpending.json%s", ConfirmedOrderDetail.class);
    public static final Http.API RECOMMEND_ORDER = Http.API.postEmpty("/order/%s/referto.json");
    public static final Http.API<ConfirmedOrderDetail> ORDER_COMPLETE = Http.API.post("/confirmedOrder/%s/complete.json", ConfirmedOrderDetail.class);
    public static final Http.API<ConfirmedOrderDetail> ORDER_CONFIRMCOMPLETE = Http.API.post("/confirmedOrder/%s/confirmcomplete.json", ConfirmedOrderDetail.class);
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
    public static final Http.API<List<FriendEvaluationDetail>> GET_FEVALUATION = Http.API.getList("/profile/%s/friendEval.json%s", FriendEvaluationDetail.class);
    public static final Http.API<SelfEvaluationDetail> GET_SEVALUATION = Http.API.get("/profile/%s/selfeval.json", SelfEvaluationDetail.class);
    public static final Http.API<UserDetail> POST_SELFEVALUATION = Http.API.post("/profile/%s/selfeval.json", UserDetail.class);
    public static final Http.API<APKVersion> GET_APKVERSION = Http.API.get(AppConfig.QINIU_HOST + AppConfig.QINIU_APKVERSION, APKVersion.class);
    public static final Http.API<List<UserDetail>> GET_MIDDLEMAN = Http.API.getList("/contact/%s/middleman.json%s", UserDetail.class);
    public static final Http.API<Friendship> GET_RELATIONSHIP = Http.API.post("/getrelationship.json", Friendship.class);
    public static final Http.API UPDATE_FRIENDNICK = Http.API.postEmpty("/friendnick/%s.json");
    public static final Http.API<List<UserDetail>> GET_HOTSELLER = Http.API.getList("/topseller/get.json%s", UserDetail.class);
    public static final Http.API<List<ConfirmedOrderDetailWithFriend>> GET_FRIENDTRACE = Http.API.getList("/friendtrace/get.json%s", ConfirmedOrderDetailWithFriend.class);
    public static final Http.API ORDER_HURRYUP = Http.API.getEmpty("/confirmedOrder/%s/hurryup.json");
    public static final Http.API ORDER_DISPUTE = Http.API.postEmpty("/confirmedOrder/%s/dispute.json");
    public static final Http.API<ConfirmedOrderUndoCount> GET_ORDER_NUM = Http.API.get("/confirmedOrder/undo.json", ConfirmedOrderUndoCount.class);
    public static final Http.API<OnlinePay> TOPUP = Http.API.post("/user/%s/wirein.json", OnlinePay.class);
    public static final Http.API<OnlinePay> ORDERPAY = Http.API.post("/orderPay.json", OnlinePay.class);
    public static final Http.API<OnlinePay> CONFIRMEDORDERPAY = Http.API.post("/confirmedOrderPay.json",OnlinePay.class);
//    public static final Http.API ORDERPAY_YB = Http.API.postEmpty("/orderPay.json");
//    public static final Http.API CONFIRMEDORDERPAY_YB = Http.API.postEmpty("/confirmedOrderPay.json");
    public static final Http.API WITHDRAWAPPLY = Http.API.postEmpty("/user/%s/withdraw.json");
    public static final Http.API<List<ReportCategory>> GET_REPORTCATEGORY = Http.API.getList("/reportcategory.json", ReportCategory.class);
    public static final Http.API<List<OrderCategory>> GET_ORDERCATEGORY = Http.API.getList("/ordercategory.json", OrderCategory.class);
    public static final Http.API POST_REPORT = Http.API.postEmpty("/reportorder.json");
    public static final Http.API<AuthResult> FORGET_PASSWORD = Http.API.post("/forgetpassword.json", AuthResult.class);
    public static final Http.API<PhotoResponse> GET_PHOTORESPONSE = Http.API.get("/user/%s/verification.json", PhotoResponse.class);
    public static final Http.API CERTIFICATE_ID = Http.API.postEmpty("/user/%s/verification.json");
    public static final Http.API<Question> POST_QUESTION = Http.API.post("/question.json", Question.class);
    public static final Http.API<List<Question>> GET_QUESTION = Http.API.getList("/question.json%s", Question.class);
    public static final Http.API<QuestionWithAnswers> GET_ANSWERS = Http.API.get("/question/%s.json%s", QuestionWithAnswers.class);
    public static final Http.API MAKE_ANSWERS = Http.API.postEmpty("/question/%s.json");
    public static final Http.API ACCEPT_ANSWERS = Http.API.postEmpty("/question/%s/%s.json");
    public static final Http.API<List<Question>> GET_MYQUESTION = Http.API.getList("/profile/%s/question.json%s", Question.class);
    public static final Http.API<List<AnswerDetailWithQuestionDetail>> GET_MYANSWER = Http.API.getList("/profile/%s/answer.json", AnswerDetailWithQuestionDetail.class);
    public static final Http.API COLSE_MYQUESTION = Http.API.postEmpty("/question/%s/close.json");
    public static final Http.API<AnswerDetail> UPDATE_MYANSWER = Http.API.post("/question/%s/update.json", AnswerDetail.class);
    public static final Http.API<UserDetail> OPEN_TRADEHISTORY = Http.API.post("/profile/%s/confirmedOrderHistory.json",UserDetail.class);
    public static final Http.API<List<ConfirmedOrderDetail>> GET_CONFIRMEDORDERHISTORY = Http.API.getList("/profile/%s/confirmedOrderHistory.json%s", ConfirmedOrderDetail.class);
    public static final Http.API ADDFANS = Http.API.postEmpty("/fans/addFans.json");
    public static final Http.API DELETEFANS = Http.API.deleteEmpty("/fans/delete.json%s");
    public static final Http.API<List<OrderDetails>> GETOPENORDER = Http.API.getList("/order/getopenorderwithuserid.json%s", OrderDetails.class);
    public static final Http.API<List<UserDetail>> GETALLFOCUS = Http.API.getList("/fans/getAllFocus.json%s", UserDetail.class);
    public static final Http.API<List<UserDetail>> GETALLFANS = Http.API.getList("/fans/getAllFans.json%s", UserDetail.class);
    public static final Http.API DELETE_FRIENDEVALUATION = Http.API.deleteEmpty("/profile/DeleteFriendsEvaluation.json%s");
    public static final Http.API<OrderDetails> GETORDERBYID = Http.API.get("/order/%s.json", OrderDetails.class);
    public static final Http.API<QuestionWithAnswers> GET_QUESTIONBYID = Http.API.get("/question/%s.json", QuestionWithAnswers.class);
    public static final Http.API<ConfirmedOrderDetail> GET_CONFIRMEDORDERBYID = Http.API.get("/confirmedOrder/%s.json", ConfirmedOrderDetail.class);
    public static final Http.API<List<Recommend>> GET_ALLRECOMMENDATION = Http.API.getList("/recommend/all.json", Recommend.class);
    public static final Http.API<List<RecommendOrder>> GET_RECOMMENDORDERBYID = Http.API.getList("/recommendorder/%s.json", RecommendOrder.class);
    public static final Http.API<List<RecommendOrderQuestion>> GET_RECOMMENDORDERQUESTIONBYID = Http.API.getList("/recommendorderquestion/%s.json", RecommendOrderQuestion.class);
    public static final Http.API<List<UserAskQuestion>> GET_QUESTIONBYASKUSERID = Http.API.getList("/useraskquestion/ask/%s.json", UserAskQuestion.class);
    public static final Http.API<List<UserAskQuestion>> GET_QUESTIONBYANSWERUSERID = Http.API.getList("/useraskquestion/answer/%s.json", UserAskQuestion.class);
    public static final Http.API USERASKQUESTION = Http.API.postEmpty("/useraskquestion.json");
    public static final Http.API USERANSWERQUESTION = Http.API.postEmpty("/useranswerquestion.json");
    public static final Http.API<List<RussianTour>> RUSSIANTOUR = Http.API.getList("/russiantour.json",RussianTour.class);
    public static final Http.API RUSSIANTOURAPPLY = Http.API.postEmpty("/russiantour/%s/apply.json");
    public static final Http.API<OnlinePay> RUSSIANTOURPREPAY = Http.API.post("/russiantour/%s/prepay.json",OnlinePay.class);
    public static final Http.API<List<MRussianTour>> MYRUSSIANTOUR = Http.API.getList("/russiantour/mytours.json%s",MRussianTour.class);
    public static final Http.API RUSSIANTOURPREPAYFAIL = Http.API.postEmpty("/russiantour/%s/prepayfail.json");
    public static final Http.API<List<GuideTour>> GETALLGUIDE = Http.API.getList("/guidetour/guide.json%s",GuideTour.class);
    public static final Http.API<GuideTour> GETGUIDEBYID = Http.API.get("/guidetour/guide/%s.json", GuideTour.class);
    public static final Http.API<GuideTourOrder> GUIDEAPPLY = Http.API.post("/guidetour/guide/%s/apply.json",GuideTourOrder.class);
    public static final Http.API<List<GuideTourOrder>> GETALLGUIDEORDERFORUSER = Http.API.getList("/guidetour/guideapplication.json%s",GuideTourOrder.class);
    public static final Http.API<List<GuideTourOrder>> GETALLGUIDEORDERFORGUIDE = Http.API.getList("/guidetour/guideapplication/guide.json%s",GuideTourOrder.class);
    public static final Http.API<OnlinePay> GUIDEPREPAY = Http.API.post("/guidetour/guideapplication/%s/prepay.json",OnlinePay.class);
    public static final Http.API GUIDEACCEPT = Http.API.postEmpty("/guidetour/guideapplication/%s/accept.json");
    public static final Http.API GUIDEREJECT = Http.API.postEmpty("/guidetour/guideapplication/%s/reject.json");
    public static final Http.API GUIDECOMPLETE = Http.API.postEmpty("/guidetour/guideapplication/%s/complete.json");
    public static final Http.API GUIDECOMMENT = Http.API.postEmpty("/guidetour/guideapplication/%s/comment.json");
    public static final Http.API IFGUIDE = Http.API.getEmpty("/guidetour/guidetest.json");
    public static final Http.API<GuideOrderComments> GETGUIDECOMMENT = Http.API.get("/guidetour/guide/%s/apply.json%s", GuideOrderComments.class);
}
