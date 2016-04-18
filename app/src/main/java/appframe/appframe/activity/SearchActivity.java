package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_back;
    private EditText et_search;
    private ListView lv_mysearch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }
    private  void init()
    {
        et_search = (EditText)findViewById(R.id.et_search);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_mysearch = (ListView)findViewById(R.id.lv_mysearch);
        tb_back.setText("友帮");
        tb_back.setOnClickListener(this);
        lv_mysearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, OrderDetailsActivity.class);
                OrderDetails orderDetails = (OrderDetails) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderDetails", orderDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        et_search.addTextChangedListener(textWatcher);

    }

    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if(!et_search.getText().toString().equals("")) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("KeyWord", URLEncoder.encode(et_search.getText().toString()));
                Http.request(SearchActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(final List<OrderDetails> result) {
                        super.onSuccess(result);


                        lv_mysearch.setAdapter(new SwipeRefreshXOrderAdapater(SearchActivity.this, result, AppConfig.ORDERSTATUS_MAIN));

                    }
                });
            }
            else
            {
                List<OrderDetails> result = new ArrayList<OrderDetails>();
                lv_mysearch.setAdapter(new SwipeRefreshXOrderAdapater(SearchActivity.this, result, AppConfig.ORDERSTATUS_MAIN));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
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
