package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXSystemMessageAdapater;

/**
 * Created by Administrator on 2015/11/12.
 */
public class SystemMessageActivity extends BaseActivity implements View.OnClickListener{
    private TextView tb_title,tb_back;
    ListView lv_sysmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemmessage);
        init();
        initdata();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_sysmessage = (ListView)findViewById(R.id.lv_sysmessage);
        tb_back.setText("我的消息");
        tb_title.setText("系统通知");
        tb_back.setOnClickListener(this);

    }

    private void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));
        map.put("Type", "3");
        Http.request(SystemMessageActivity.this, API.GET_PUSHMESSAGE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<PushMessage>>() {
            @Override
            public void onSuccess( List<PushMessage> result) {
                super.onSuccess(result);

                lv_sysmessage.setAdapter(new SwipeRefreshXSystemMessageAdapater(SystemMessageActivity.this, result));

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