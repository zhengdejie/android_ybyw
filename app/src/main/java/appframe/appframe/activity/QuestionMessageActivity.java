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
import appframe.appframe.dto.PushMessage;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderMessageAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXQuestionMessageAdapater;

/**
 * Created by Administrator on 2016/6/7.
 */
public class QuestionMessageActivity extends BaseActivity implements View.OnClickListener{
    private TextView tb_title,tb_back;
    ListView lv_questionmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionmessage);
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
        lv_questionmessage = (ListView)findViewById(R.id.lv_questionmessage);
        tb_back.setText("我的消息");
        tb_title.setText("问答通知");
        tb_back.setOnClickListener(this);
        lv_questionmessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushMessage pushMessage = (PushMessage) parent.getAdapter().getItem(position);
                Intent intent = new Intent();
                intent.setClass(QuestionMessageActivity.this, OrderDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("QuestionMessage", pushMessage.getObjectDetail());
                intent.putExtras(bundle);


                startActivity(intent);

            }
        });
    }

    private void initdata()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ReceiverID", String.valueOf(Auth.getCurrentUserId()));

        map.put("Type", "5");
        Http.request(QuestionMessageActivity.this, API.GET_PUSHMESSAGE, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<PushMessage>>() {
            @Override
            public void onSuccess(List<PushMessage> result) {
                super.onSuccess(result);

                lv_questionmessage.setAdapter(new SwipeRefreshXQuestionMessageAdapater(QuestionMessageActivity.this, result));

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
