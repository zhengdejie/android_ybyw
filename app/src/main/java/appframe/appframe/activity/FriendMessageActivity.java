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
import appframe.appframe.dto.AnswerDetailWithQuestionDetail;
import appframe.appframe.dto.PushMessage;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendMessageAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXSystemMessageAdapater;

/**
 * Created by Administrator on 2015/11/17.
 */
public class FriendMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back,tv_empty;
    ListView lv_friendmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendmessage);
        init();
        initdata();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        lv_friendmessage = (ListView)findViewById(R.id.lv_friendmessage);
        tb_back.setText("我的消息");
        tb_title.setText("好友通知");
        tb_back.setOnClickListener(this);

        lv_friendmessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(FriendMessageActivity.this, FriendsInfoActivity.class);

                PushMessage pushMessage = (PushMessage) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PushMessage", pushMessage);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

    }

    private void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));
        map.put("Type", "2");
        Http.request(FriendMessageActivity.this, API.GET_PUSHMESSAGE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<PushMessage>>() {
            @Override
            public void onSuccess(List<PushMessage> result) {
                super.onSuccess(result);

                lv_friendmessage.setAdapter(new SwipeRefreshXFriendMessageAdapater(FriendMessageActivity.this, result));

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
