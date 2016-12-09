package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.aop.Advice;
import com.alibaba.mobileim.aop.Pointcut;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.CommentDetailResponseDto;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.ConfirmedOrderDetailWithFriend;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.PayResult;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.ChattingOperationCustomSample;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.widget.sortlistview.FirstClassFriends;
import appframe.appframe.widget.swiperefresh.GridViewBigPictureAdapater;
import appframe.appframe.widget.swiperefresh.OrderDetailsGridViewAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyMissionAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderComment;
import appframe.appframe.widget.tagview.Tag;

/**
 * Created by Administrator on 2015/8/14.
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener{

    //private ImageView img_avatar;
    private TextView tv_name,tv_title,tv_money,tv_location,tv_type,tv_status,tv_content,tv_deadline,tv_require,tv_paymethod,tb_back,tb_action,tb_title,tv_comment,
            tv_originalprice,tv_bargain,btn_select,btn_comment,btn_recommend;
    appframe.appframe.widget.tagview.TagView tv_tags;
    private ImageView imgbtn_conversation,imgbtn_call;
    private ScrollView sv_main;
    com.android.volley.toolbox.NetworkImageView iv_avatar;
//    private Button btn_select,btn_comment,btn_recommend;
    private EditText et_price;
    int page = 1;
    private String OrderID,Tel, hasTopOrder, Entrance;
    private ListView lv_ordercomment;
    private Drawable icon;
//    private LinearLayout lly_photos;
    private GridView gridView;
    private RatingBar rb_totalvalue;
    OrderDetails orderDetails;
    ConfirmedOrderDetailWithFriend confirmedOrderDetailWithFriend;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    String Bid;
    Intent getintent;
//    SwipeRefreshX swipeRefresh;
    SwipeRefreshXOrderComment swipeRefreshXOrderComment;
    private static final int SDK_PAY_FLAG = 1;
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case SDK_PAY_FLAG: {
//                    PayResult payResult = new PayResult((String) msg.obj);
//                    /**
//                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
//                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
//                     * docType=1) 建议商户依赖异步通知
//                     */
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//
//                    String resultStatus = payResult.getResultStatus();
//                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        Toast.makeText(OrderDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // 判断resultStatus 为非"9000"则代表可能支付失败
//                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                        if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(OrderDetailsActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(OrderDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                    break;
//                }
////                case SDK_CHECK_FLAG: {
////                    Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
////                    break;
////                }
//                default:
//                    break;
//            }
//        };
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);

//////////////////-----------来自接单通知------------------///////////////////////////////////
        getintent = this.getIntent();
        if(getintent.getStringExtra("OrderIdFromPushDemoReceiver") != null)
        {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("Id", getintent.getStringExtra("OrderIdFromPushDemoReceiver"));
            Http.request(this, API.GETORDERBYID, new Object[]{getintent.getStringExtra("OrderIdFromPushDemoReceiver")},

                    new Http.RequestListener<OrderDetails>() {
                        @Override
                        public void onSuccess(OrderDetails result) {
                            super.onSuccess(result);

                            orderDetails = result;
                            init();
                        }
                    });

        }
        else
        {
            init();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_avatar:
                if(orderDetails.getNameAnonymity() == 1)
                {}
                else {
                    intent.setClass(OrderDetailsActivity.this, FriendsInfoActivity.class);
                    bundle.putSerializable("OrderDetails", orderDetails);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.imgbtn_conversation:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
                String target = String.valueOf(orderDetails.getOrderer().getId());
                intent = ls.getIMKit().getChattingActivityIntent(target,LoginSampleHelper.APP_KEY);
                startActivity(intent);
                break;
            case R.id.imgbtn_call:
                if(orderDetails.getPhoneAnonymity() == 1)
                {
                    Toast.makeText(OrderDetailsActivity.this,"对方没提供电话号码",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Tel)); //直接拨打电话android.intent.action.CALL
                    startActivity(phoneIntent);
                }
//                else
//                {
//                    Toast.makeText(OrderDetailsActivity.this,"该用户不是用手机注册",Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.tb_action:
                new PopupWindows(OrderDetailsActivity.this,tb_action);
                break;
            case R.id.tb_back:
                finish();
                /////////接单通知走这里/////////////////
                if(getintent.getStringExtra("OrderIdFromPushDemoReceiver") != null) {
                    startActivity(new Intent(this, HomeActivity.class));
                }
                break;
            case R.id.btn_select:
                if(btn_select.getText().equals("候选接单人"))
                {

                    intent.setClass(OrderDetailsActivity.this, CandidateActivity.class);
                    //Bundle bundle = new Bundle();
                    bundle.putSerializable("OrderDetails", orderDetails);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_bargain, (ViewGroup) findViewById(R.id.dialog));
                    tv_originalprice = (TextView)layout.findViewById(R.id.tv_originalprice);
                    tv_bargain = (TextView)layout.findViewById(R.id.tv_bargain);
                    et_price = (EditText)layout.findViewById(R.id.et_price);
                    icon = getResources().getDrawable(R.drawable.ic_task_status_list_check);
                    tv_originalprice.setOnClickListener(this);
                    tv_bargain.setOnClickListener(this);
                    et_price.setOnClickListener(this);
                    et_price.addTextChangedListener(textWatcher);
//                    rg_bargain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(RadioGroup group, int checkedId) {
//                            if(rb_originalprice.getId() == checkedId)
//                            {
//                                rb_bargain.setChecked(false);
//                                et_price.setEnabled(false);
//                                Bid = String.valueOf(orderDetails.getBounty());
//                            }
//                            else if(rb_bargain.getId() == checkedId)
//                            {
//                                rb_originalprice.setChecked(false);
//                                et_price.setEnabled(true);
//                                Bid = et_price.getText().toString();
//                            }
//                        }
//                    });



//                    builder.setMessage("确认接单吗?");
                    builder.setTitle("提示").setView(layout).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!et_price.getText().toString().equals("")) {
                                if (Double.parseDouble(et_price.getText().toString()) <= 0.00) {
                                    Toast.makeText(OrderDetailsActivity.this, "金额不能小于0.01元", Toast.LENGTH_SHORT).show();
                                } else {
                                    Http.request(OrderDetailsActivity.this, API.ORDER_ACCEPT, new Object[]{OrderID}, Http.map("Bid", et_price.getText().toString()), new Http.RequestListener<ConfirmedOrderDetail>() {
                                        @Override
                                        public void onSuccess(ConfirmedOrderDetail result) {
                                            super.onSuccess(result);
                                            if (orderDetails.getType() == 1) {
                                                intent.setClass(OrderDetailsActivity.this, PayActivity.class);
                                                bundle.putSerializable("ConfirmedOrderDetail", result);
                                                intent.putExtras(bundle);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(OrderDetailsActivity.this, "申请已发出,请等待单主回复", Toast.LENGTH_SHORT).show();
//                                                try {
//                                                    Thread.sleep(10000);
//                                                } catch (InterruptedException e) {
//                                                    return;
//                                                }
//                                                finish();
                                            }

                                        }
                                    });
                                    dialog.dismiss();
                                }
                            }
                            else
                            {
                                Http.request(OrderDetailsActivity.this, API.ORDER_ACCEPT, new Object[]{OrderID}, new Http.RequestListener<ConfirmedOrderDetail>() {
                                    @Override
                                    public void onSuccess(ConfirmedOrderDetail result) {
                                        super.onSuccess(result);
                                        if (orderDetails.getType() == 1) {
                                            intent.setClass(OrderDetailsActivity.this, PayActivity.class);
                                            bundle.putSerializable("ConfirmedOrderDetail", result);
                                            intent.putExtras(bundle);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(OrderDetailsActivity.this, "申请已发出,请等待单主回复", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                dialog.dismiss();
                            }

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                    }).show();


                }
                break;
//            case R.id.btn_estimate:
//                startActivity(new Intent(this,OrderEstimateActivity.class));
//                break;
            case R.id.btn_comment:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //final EditText comment = new EditText(this);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_leavemessage, (ViewGroup) findViewById(R.id.dialog));
                final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                final CheckBox cb_anonymous = (CheckBox)layout.findViewById(R.id.cb_anonymous);
                builder.setTitle("留言").setView(
                        layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!comment.getText().toString().equals("")) {
                            Http.request(OrderDetailsActivity.this, API.ORDER_MAKECOOMENT, new Object[]{OrderID}, Http.map(
                                            "Commentator", String.valueOf(Auth.getCurrentUserId()),
                                            "Comment", comment.getText().toString(),
                                            "Anonymity", String.valueOf(cb_anonymous.isChecked())),
                                    new Http.RequestListener<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            super.onSuccess(result);
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("Page", "1");
                                            map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                                            Http.request(OrderDetailsActivity.this, API.ORDER_GETCOOMENT, new Object[]{OrderID,Http.getURL(map)},
                                                    new Http.RequestListener<CommentDetailResponseDto>() {
                                                        @Override
                                                        public void onSuccess(CommentDetailResponseDto result) {
                                                            super.onSuccess(result);
                                                            if(result!=null) {
                                                                if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
                                                                    swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), true);
                                                                    lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
                                                                } else {
                                                                    swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), false);
                                                                    lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
                                                                }
                                                                setListViewHeightBasedOnChildren(lv_ordercomment);
                                                                tv_comment.setText(String.format("留言%d条 (点击查看全部)", result.getTotalCount()));
                                                            }
                                                            else
                                                            {
                                                                tv_comment.setText("留言0条 (点击查看全部)");
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(OrderDetailsActivity.this,"评论不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.btn_recommend:
                intent.setClass(OrderDetailsActivity.this, FirstClassFriends.class);
                //Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            break;
            case R.id.tv_originalprice:
                tv_originalprice.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                et_price.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                et_price.setFocusable(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_price.getWindowToken(), 0); //强制隐藏键盘
                et_price.setText("");
                break;
            case R.id.tv_bargain:
                tv_originalprice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                et_price.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                et_price.setFocusable(true);
                et_price.setFocusableInTouchMode(true);
                et_price.requestFocus();
                et_price.requestFocusFromTouch();

                break;
            case R.id.et_price:
                tv_originalprice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                et_price.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
                et_price.setFocusable(true);
                et_price.setFocusableInTouchMode(true);
                et_price.requestFocus();
                et_price.requestFocusFromTouch();
                break;
            case R.id.tv_comment:
                intent.setClass(OrderDetailsActivity.this, OrderCommentAllActivity.class);
                //Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    public void init()
    {
        //img_avatar = (ImageView)findViewById(R.id.img_avatar);
        //lly_photos = (LinearLayout)findViewById(R.id.lly_photos);
        gridView =(GridView)findViewById(R.id.gridview);
        imgbtn_conversation = (ImageView)findViewById(R.id.imgbtn_conversation);
        imgbtn_call = (ImageView)findViewById(R.id.imgbtn_call);
        sv_main = (ScrollView)findViewById(R.id.sv_main);

        tv_title =(TextView)findViewById(R.id.tv_title);
        tv_money =(TextView)findViewById(R.id.tv_money);
        tv_location =(TextView)findViewById(R.id.tv_location);
        tv_type =(TextView)findViewById(R.id.tv_type);
        tv_content =(TextView)findViewById(R.id.tv_content);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);
        btn_select = (TextView)findViewById(R.id.btn_select);
//        tv_time = (TextView)findViewById(R.id.tv_time);
//        tv_status = (TextView)findViewById(R.id.tv_status);
        tv_require = (TextView)findViewById(R.id.tv_require);
        tv_paymethod = (TextView)findViewById(R.id.tv_paymethod);
        tv_deadline = (TextView)findViewById(R.id.tv_deadline);
        tv_name = (TextView)findViewById(R.id.tv_name);
        btn_comment = (TextView)findViewById(R.id.btn_comment);
        lv_ordercomment = (ListView)findViewById(R.id.lv_ordercomment);
        tv_comment = (TextView)findViewById(R.id.tv_comment);
//        tv_moneyunit = (TextView)findViewById(R.id.tv_moneyunit);
//        tv_range = (TextView)findViewById(R.id.tv_range);
        btn_recommend = (TextView)findViewById(R.id.btn_recommend);
        iv_avatar = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.iv_avatar);
        rb_totalvalue = (RatingBar)findViewById(R.id.rb_totalvalue);
        tv_tags = (appframe.appframe.widget.tagview.TagView)findViewById(R.id.tv_tags);


        iv_avatar.setOnClickListener(this);
        imgbtn_conversation.setOnClickListener(this);
        imgbtn_call.setOnClickListener(this);
        btn_select.setOnClickListener(this);
        btn_comment.setOnClickListener(this);
        btn_recommend.setOnClickListener(this);
        tv_comment.setOnClickListener(this);
//        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);
//
//        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        //////////////////-----------来自接单通知------------------///////////////////////////////////
        if(getintent.getStringExtra("OrderIdFromPushDemoReceiver") != null)
        {
            //orderDetails之前赋值过了
        }
        else {
            orderDetails = (OrderDetails) getintent.getSerializableExtra("OrderDetails");
            if (orderDetails == null) {
                confirmedOrderDetailWithFriend = (ConfirmedOrderDetailWithFriend) getintent.getSerializableExtra("ConfirmedOrderDetailWithFriend");
                orderDetails = confirmedOrderDetailWithFriend.getOrder();

            }
        }

        if(orderDetails.getOrderStatus().equals("1"))
        {
            btn_select.setVisibility(View.INVISIBLE);
            btn_recommend.setVisibility(View.INVISIBLE);
        }

        if(orderDetails.getLocationAnonymity() == 1) {
            tv_location.setText("");
        }
        else
        {
            if(orderDetails.getUserLocation() != null && orderDetails.getUserLocation().getProvince() != null && orderDetails.getUserLocation().getCity() != null && orderDetails.getUserLocation().getDistrict() != null) {
                tv_location.setText("地址:" + orderDetails.getUserLocation().getProvince() + orderDetails.getUserLocation().getCity() + orderDetails.getUserLocation().getDistrict());
            }
            else
            {
                tv_location.setText("地址:未知");
            }

        }
//        if( orderDetails.getType() == 1 )
//        {
//            tv_moneyunit.setText("索 ￥");
//        }
//        else
//        {
//            tv_moneyunit.setText("赏 ￥");
//        }
        if((getintent.getStringExtra("From") != null && getintent.getStringExtra("From").equals("MyOrder")) || orderDetails.getOrderer().getId() == Auth.getCurrentUserId())
        {
            btn_select.setText("候选接单人");
        }
        hasTopOrder = getintent.getStringExtra("hasTopOrder") == null ? null : getintent.getStringExtra("hasTopOrder");
        Entrance = getintent.getStringExtra("Entrance") == null ? null : getintent.getStringExtra("Entrance");
        tv_title.setText(orderDetails.getTitle().toString());
        tv_money.setText("￥" + String.valueOf(orderDetails.getBounty()));

        tv_type.setText(orderDetails.getCategory().toString());
        tv_content.setText(orderDetails.getContent().toString());
//        tv_time.setText(orderDetails.getCreatedAt().toString());
//        tv_status.setText(orderDetails.getOrderStatus().toString());
        if(orderDetails.getTags() != null && !orderDetails.getTags().equals(""))
        {
            for(String tagTitle : orderDetails.getTags().split(","))
            {
                tv_tags.setVisibility(View.VISIBLE);
                Tag tag = new Tag(tagTitle);
                tag.layoutColor = getResources().getColor(R.color.bg_gray);
                tag.radius = 10f;
                tv_tags.addTag(tag);
            }
        }
//        tv_tags.setText(orderDetails.getTags());

        tv_require.setText(orderDetails.getRequest() == null ? "" : orderDetails.getRequest().toString());
        tv_paymethod.setText(orderDetails.getPaymentMethod().toString());
        tv_deadline.setText(orderDetails.getDeadline().toString());
//        iv_avatar.setDefaultImageResId(R.drawable.default_avatar);
        if(orderDetails.getOrderer().getGender().equals(getResources().getString(R.string.male).toString()))
        {
            iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
        }
        else
        {
            iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
        }
        if(orderDetails.getNameAnonymity() == 1)
        {
            tv_name.setText("匿名");
            if(orderDetails.getOrderer().getGender().equals(getResources().getString(R.string.male).toString()))
            {
                iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
            }
            else
            {
                iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
            }

        }
        else {
            if (orderDetails.getOrderer().getFNickName() != null && !orderDetails.getOrderer().getFNickName().equals("")) {
                tv_name.setText(orderDetails.getOrderer().getFNickName());
            } else {
                tv_name.setText(orderDetails.getOrderer().getName().toString());
            }
            if(orderDetails.getOrderer().getAvatar() != null) {
                ImageUtils.setImageUrl(iv_avatar, orderDetails.getOrderer().getAvatar().toString());
            }
            else
            {
                if(orderDetails.getOrderer().getGender().equals(getResources().getString(R.string.male).toString()))
                {
                    iv_avatar.setDefaultImageResId(R.drawable.maleavatar);
                }
                else
                {
                    iv_avatar.setDefaultImageResId(R.drawable.femaleavatar);
                }
            }
        }
        if(orderDetails.getType() == 1) {
            rb_totalvalue.setRating((float)orderDetails.getOrderer().getTotalWorkerPoint());
            tb_title.setText("助人单");
            tv_money.setTextColor(getResources().getColor(R.color.green));
        }
        if(orderDetails.getType() == 2) {
            rb_totalvalue.setRating((float) orderDetails.getOrderer().getTotalBossPoint());
            tb_title.setText("求助单");
            tv_money.setTextColor(Color.RED);
        }
        SpannableString ss = new SpannableString( "￥" + String.valueOf(orderDetails.getBounty()));
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_money.setText(ss);



        if(orderDetails.getPhotos() != null && orderDetails.getPhotos() != "") {
            List<String> photoPath = new ArrayList<String>();
            for (String photsCount : orderDetails.getPhotos().toString().split(",")) {
                photoPath.add(photsCount);
            }
            gridView.setAdapter(new OrderDetailsGridViewAdapater(OrderDetailsActivity.this,photoPath));
            gridView.setVisibility(View.VISIBLE);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent();
//                    intent.setClass(OrderDetailsActivity.this, AvatarZoomActivity.class);
//                    intent.putExtra("Avatar", (String)parent.getAdapter().getItem(position));
//                    startActivity(intent);
                    Intent intent = new Intent();
                    intent.setClass(OrderDetailsActivity.this, OrderDetailsViewPager.class);
                    intent.putExtra("Position", String.valueOf(position));
                    intent.putExtra("PhotoPath", orderDetails.getPhotos().toString());
                    startActivity(intent);
                }
            });
        }
        Tel = orderDetails.getOrderer().getMobile() == null ? "" : orderDetails.getOrderer().getMobile().toString();
        OrderID = String.valueOf(orderDetails.getId());
//        tv_range.setText(setRange(orderDetails.getVisibility()));
        //Log.i("OrderDetailsID--",String.valueOf(orderDetails.getId()));

        tb_back.setText("友帮");
        Drawable drawable = getResources().getDrawable(R.drawable.moreoverflow_normal_holo_light);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tb_action.setCompoundDrawables(null, null, drawable, null);
        tb_action.setOnClickListener(this);
        tb_back.setOnClickListener(this);

        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        Http.request(OrderDetailsActivity.this, API.ORDER_GETCOOMENT, new Object[]{OrderID,Http.getURL(map)},
                new Http.RequestListener<CommentDetailResponseDto>() {
                    @Override
                    public void onSuccess(CommentDetailResponseDto result) {
                        super.onSuccess(result);
                        if(result != null) {
                            if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
                                swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), true);
                                lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
                            } else {
                                swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), false);
                                lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
                            }
                            setListViewHeightBasedOnChildren(lv_ordercomment);
                            tv_comment.setText(String.format("留言%d条 (点击查看全部)", result.getTotalCount()));
                        }
                        else
                        {
                            tv_comment.setText("留言0条 (点击查看全部)");
                        }
                    }
                });

//        // 下拉刷新监听器
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Page", "1");
//                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
//                Http.request(OrderDetailsActivity.this, API.ORDER_GETCOOMENT, new Object[]{OrderID,Http.getURL(map)},
//                        new Http.RequestListener<CommentDetailResponseDto>() {
//                            @Override
//                            public void onSuccess(CommentDetailResponseDto result) {
//                                super.onSuccess(result);
//                                page = 1;
//                                swipeRefresh.setRefreshing(false);
//                                if(result!=null) {
//                                    if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
//                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), true);
//                                        lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
//                                    } else {
//                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result.getList(), String.valueOf(Auth.getCurrentUserId()), false);
//                                        lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
//                                    }
//                                    setListViewHeightBasedOnChildren(lv_ordercomment);
//                                    tv_comment.setText(String.format("留言%d条,点击查看全部", result.getTotalCount()));
//                                }
//                                else
//                                {
//                                    tv_comment.setText("留言0条,点击查看全部");
//                                }
//                            }
//
//                            @Override
//                            public void onFail(String code) {
//                                super.onFail(code);
//                                swipeRefresh.setRefreshing(false);
//                            }
//                        });
//
//            }
//        });
//        // 加载监听器
//        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {
//
//            @Override
//            public void onLoad() {
//
//                page++;
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("Page", String.valueOf(page));
//                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
//                Http.request(OrderDetailsActivity.this, API.ORDER_GETCOOMENT, new Object[]{OrderID, Http.getURL(map)},
//                        new Http.RequestListener<CommentDetailResponseDto>() {
//                            @Override
//                            public void onSuccess(CommentDetailResponseDto result) {
//                                super.onSuccess(result);
//
//                                swipeRefresh.setLoading(false);
//                                if (result != null) {
//
//                                    loadMore(swipeRefreshXOrderComment, result.getList());
////                                    if (Auth.getCurrentUserId() == orderDetails.getOrderer().getId()) {
////                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), true);
////                                        lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
////                                    } else {
////                                        swipeRefreshXOrderComment = new SwipeRefreshXOrderComment(OrderDetailsActivity.this, result, String.valueOf(Auth.getCurrentUserId()), false);
////                                        lv_ordercomment.setAdapter(swipeRefreshXOrderComment);
////                                    }
//                                    setListViewHeightBasedOnChildren(lv_ordercomment);
//                                    tv_comment.setText(String.format("留言%d条,点击查看全部", result.getTotalCount()));
//                                }
//
//                                else
//                                {
//                                    tv_comment.setText("留言0条,点击查看全部");
//                                }
//                            }
//
//                            @Override
//                            public void onFail(String code) {
//                                super.onFail(code);
//                                swipeRefresh.setLoading(false);
//                            }
//                        });
//
//
//
//            }
//        });

    }
    private void loadMore(SwipeRefreshXOrderComment adapater, List<OrderComment> orderDetailses) {
        adapater.addItems(orderDetailses);
    }
    private String setRange(int range)
    {
        StringBuilder sb = new StringBuilder();

        if( (range & 1) == 1)
        {
            sb.append(",").append("一度");
        }
        if((range & 2) == 2)
        {
            sb.append(",").append("二度");
        }
        if((range & 4) == 4)
        {
            sb.append(",").append("陌生人");
        }

        return sb.deleteCharAt(0).toString();
    }

    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + 3);
                    et_price.setText(s);
                    et_price.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                et_price.setText(s);
                et_price.setSelection(2);
            }

            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    et_price.setText(s.subSequence(0, 1));
                    et_price.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
//        sv_main.fullScroll(View.FOCUS_UP);
    }


    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.popupwindow_order, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAsDropDown(parent, 0,0);
            update();

            TextView btn_viewfdinfo = (TextView) view
                    .findViewById(R.id.item_popupwindows_viewfdinfo);
            final TextView btn_collectorder = (TextView) view
                    .findViewById(R.id.item_popupwindows_collectorder);
            final TextView btn_toporder = (TextView) view
                    .findViewById(R.id.item_popupwindows_toporder);
            TextView btn_reportorder = (TextView) view
                    .findViewById(R.id.item_popupwindows_reportorder);
            RelativeLayout rl_viewfdinfo = (RelativeLayout) view.findViewById(R.id.rl_viewfdinfo);

            if(orderDetails.getNameAnonymity() == 1)
            {
                rl_viewfdinfo.setVisibility(View.GONE);
            }
            else
            {
                rl_viewfdinfo.setVisibility(View.VISIBLE);
            }

            if(hasTopOrder != null && hasTopOrder.equals("1"))
            {
                btn_toporder.setText("取消置顶");
            }
            else
            {
                btn_toporder.setText("置顶本单");
            }
            if(Entrance != null && Entrance.equals("mycollect"))
            {
                btn_collectorder.setText("取消收藏");
            }
            else
            {
                btn_collectorder.setText("收藏本单");
            }
//            Button btn_viewrelationnet = (Button) view
//                    .findViewById(R.id.item_popupwindows_viewrelationnet);

            btn_viewfdinfo.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    //startActivity(new Intent(OrderDetailsActivity.this, FriendsInfoActivity.class));
                    intent.setClass(OrderDetailsActivity.this, FriendsInfoActivity.class);
                    bundle.putSerializable("OrderDetails", orderDetails);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dismiss();
                }
            });
            btn_collectorder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(btn_collectorder.getText().equals("收藏本单")) {
                        Http.request(OrderDetailsActivity.this, API.ADD_FAVORITEORDER, Http.map(
                                "UserId", String.valueOf(Auth.getCurrentUserId()),
                                "OrderId", OrderID
                        ), new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);

                                Toast.makeText(OrderDetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else
                    {
                        Http.request(OrderDetailsActivity.this, API.DELETE_FAVORITEORDER, new Object[]{OrderID}, new Http.RequestListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);

                                Toast.makeText(OrderDetailsActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                    dismiss();
                }
            });
            btn_toporder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(btn_toporder.getText().equals("取消置顶"))
                    {
                        SharedPreferences.Editor e = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE).edit();
                        e.remove("OrderDetails").commit();
                        btn_toporder.setText("置顶本单");
                        Toast.makeText(OrderDetailsActivity.this, "取消置顶成功", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        SharedPreferences.Editor e = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE).edit();
                        e.remove("OrderDetails");
                        e.putString("OrderDetails", GsonHelper.getGson().toJson(orderDetails)).commit();
                        Toast.makeText(OrderDetailsActivity.this, "置顶成功", Toast.LENGTH_SHORT).show();
//                        SharedPreferences sp = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE);
//                        String OrderDetails = sp.getString("OrderDetails", null);
//                        if (!TextUtils.isEmpty(OrderDetails))
//                        {
//                            btn_toporder.setEnabled(false);
//                        }
//                        else
//                        {
//                            SharedPreferences.Editor e = getSharedPreferences("TOPORDER", Context.MODE_PRIVATE).edit();
//                            e.putString("OrderDetails", GsonHelper.getGson().toJson(orderDetails)).commit();
//                            Toast.makeText(OrderDetailsActivity.this, "置顶成功", Toast.LENGTH_SHORT).show();
//                        }
                    }
                    dismiss();
                }
            });

            btn_reportorder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    intent.setClass(OrderDetailsActivity.this, ReportActivity.class);
                    bundle.putSerializable("OrderDetails", orderDetails);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dismiss();
                }
            });
//            btn_viewrelationnet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(OrderDetailsActivity.this, RelativenetActivity.class));
//                    dismiss();
//                }
//            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("任务详情页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("任务详情页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = this.getMenuInflater();
//        inflater.inflate(R.menu.menu_order,menu);
//        MenuItem addItem = menu.findItem(R.id.action_order);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId())
//        {
//            case android.R.id.home:
//                Intent intent = new Intent(this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return true;
//            default: return super.onOptionsItemSelected(item);
//        }
//
//    }
}
