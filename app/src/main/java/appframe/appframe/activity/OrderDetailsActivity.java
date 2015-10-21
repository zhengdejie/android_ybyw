package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.NoCopySpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import appframe.appframe.R;
import appframe.appframe.app.App;
import appframe.appframe.com.google.zxing.client.android.CaptureActivity;
import appframe.appframe.dto.OrderDetails;

/**
 * Created by Administrator on 2015/8/14.
 */
public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener{

    private ImageView img_avatar;
    private TextView tv_name,tv_title,tv_money,tv_time,tv_location,tv_type,tv_status,tv_content,tv_range,tv_deadline,tv_require,tv_paymethod,tb_back,tb_action,tb_title;
    private ImageButton imgbtn_conversation,imgbtn_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.img_avatar:
                startActivity(new Intent(this,FriendsInfoActivity.class));
                break;
            case R.id.imgbtn_conversation:

                Intent intent = App.mIMKit.getChattingActivityIntent("102");
                startActivity(intent);
                break;
            case R.id.imgbtn_call:
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15205010280")); //直接拨打电话android.intent.action.CALL
                startActivity(phoneIntent);
                break;
            case R.id.tb_action:
                new PopupWindows(OrderDetailsActivity.this,tb_action);
                break;
            case R.id.tb_back:
                finish();
                break;
        }
    }

    public void init()
    {
        img_avatar = (ImageView)findViewById(R.id.img_avatar);
        imgbtn_conversation = (ImageButton)findViewById(R.id.imgbtn_conversation);
        imgbtn_call = (ImageButton)findViewById(R.id.imgbtn_call);
        tv_title =(TextView)findViewById(R.id.tv_title);
        tv_money =(TextView)findViewById(R.id.tv_money);
        tv_location =(TextView)findViewById(R.id.tv_location);
        tv_type =(TextView)findViewById(R.id.tv_type);
        tv_content =(TextView)findViewById(R.id.tv_content);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tb_title = (TextView)findViewById(R.id.tb_title);

        img_avatar.setOnClickListener(this);
        imgbtn_conversation.setOnClickListener(this);
        imgbtn_call.setOnClickListener(this);

        Intent intent = this.getIntent();
        OrderDetails orderDetails=(OrderDetails)intent.getSerializableExtra("OrderDetails");
        tv_title.setText(orderDetails.getTitle().toString());
        tv_money.setText(String.valueOf(orderDetails.getBounty()));
        tv_location.setText(orderDetails.getPosition().toString());
        tv_type.setText(orderDetails.getCategory().toString());
        tv_content.setText(orderDetails.getContent().toString());
        tb_title.setText("需求单");
        tb_back.setText("友帮");
        Drawable drawable= getResources().getDrawable(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tb_action.setCompoundDrawables(drawable, null, null, null);
        tb_action.setOnClickListener(this);
        tb_back.setOnClickListener(this);
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

            Button btn_viewfdinfo = (Button) view
                    .findViewById(R.id.item_popupwindows_viewfdinfo);
            Button btn_collectorder = (Button) view
                    .findViewById(R.id.item_popupwindows_collectorder);
            Button btn_toporder = (Button) view
                    .findViewById(R.id.item_popupwindows_toporder);
            Button btn_reportorder = (Button) view
                    .findViewById(R.id.item_popupwindows_reportorder);
//            Button btn_viewrelationnet = (Button) view
//                    .findViewById(R.id.item_popupwindows_viewrelationnet);

            btn_viewfdinfo.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    startActivity(new Intent(OrderDetailsActivity.this, FriendsInfoActivity.class));
                    dismiss();
                }
            });
            btn_collectorder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dismiss();
                }
            });
            btn_toporder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

                    dismiss();
                }
            });

            btn_reportorder.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {

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
