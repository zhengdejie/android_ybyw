package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/11/4.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back;
    private ListView lv_mycollect ;
    private EditText et_title,et_content;
    private Button bt_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_mycollect = (ListView)findViewById(R.id.lv_mycollect);
        et_title = (EditText)findViewById(R.id.et_title);
        et_content = (EditText)findViewById(R.id.et_content);
        et_content = (EditText)findViewById(R.id.et_content);
        bt_send = (Button)findViewById(R.id.bt_send);
        tb_back.setText("友帮");
        tb_title.setText("帮助与反馈");
        tb_back.setOnClickListener(this);
        bt_send.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.bt_send:
                Http.request(FeedbackActivity.this, API.USER_FEEDBACK, new Object[]{Auth.getCurrentUserId()},Http.map(
                        "Title", et_title.getText().toString(),
                        "Content",et_content.getText().toString()
                ), new Http.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);




                    }
                });
                break;
        }

    }
}
