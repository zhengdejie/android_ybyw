package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.fragment.MyOrderFragment;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderMessageAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXSystemMessageAdapater;

/**
 * Created by Administrator on 2015/11/12.
 */

public class OrderMessageActivity extends BaseActivity implements View.OnClickListener{
    private TextView tb_title,tb_back,tv_empty;
    ListView lv_ordermessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordermessage);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        lv_ordermessage = (ListView)findViewById(R.id.lv_ordermessage);
        tb_back.setText("我的消息");
        tb_title.setText("订单通知");
        tb_back.setOnClickListener(this);
        lv_ordermessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushMessage pushMessage = (PushMessage) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                if(pushMessage.getType() == 4)
                {
                    intent.setClass(OrderMessageActivity.this, OrderDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("OrderDetails", pushMessage.getOrder());
                    intent.putExtras(bundle);
                }
                else
                {
                    intent.setClass(OrderMessageActivity.this, ConfirmOrderDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ConfirmOrderDetails", pushMessage.getConfirmedOrder());
                    intent.putExtras(bundle);
                }

                startActivity(intent);

            }
        });
    }

    private void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));

        map.put("Type", "1");
        Http.request(OrderMessageActivity.this, API.GET_PUSHMESSAGE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<PushMessage>>() {
            @Override
            public void onSuccess(List<PushMessage> result) {
                super.onSuccess(result);

                lv_ordermessage.setAdapter(new SwipeRefreshXOrderMessageAdapater(OrderMessageActivity.this, result));
                if(result != null && result.size() != 0) {
                    tv_empty.setVisibility(View.GONE);
                }
                else {
                    tv_empty.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;

        }

    }
}