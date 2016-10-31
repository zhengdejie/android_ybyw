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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.Question;
import appframe.appframe.dto.SearchResult;
import appframe.appframe.utils.GsonHelper;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXQuestionAdapater;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_back,tv_empty,tv_service,tv_question,tv_questionmore,tv_servicenmore;
    private EditText et_search;
    private ListView lv_mysearch,lv_myquestion;
    private RelativeLayout rl_service,rl_question;
    Intent intent= new Intent();


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
        lv_myquestion = (ListView)findViewById(R.id.lv_myquestion);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        tv_service = (TextView)findViewById(R.id.tv_service);
        tv_question = (TextView)findViewById(R.id.tv_question);
        rl_service = (RelativeLayout)findViewById(R.id.rl_service);
        rl_question = (RelativeLayout)findViewById(R.id.rl_question);
        tv_questionmore = (TextView)findViewById(R.id.tv_questionmore);
        tv_servicenmore = (TextView)findViewById(R.id.tv_servicenmore);
        tb_back.setText("友帮");
        tb_back.setOnClickListener(this);
        tv_servicenmore.setOnClickListener(this);
        tv_questionmore.setOnClickListener(this);
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
        lv_myquestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, QuestionDetailsActivity.class);
                Question question = (Question) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Question", question);
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
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(SearchActivity.this, API.SEARCH_ORDER, new Object[]{Http.getURL(map)}, new Http.RequestListener<SearchResult>() {
                    @Override
                    public void onSuccess(final SearchResult result) {
                        super.onSuccess(result);
                        if(result != null)
                        {
                            tv_empty.setVisibility(View.GONE);
                            if(result.getOrders() != null && !result.getOrders().isEmpty()) {
                                rl_service.setVisibility(View.VISIBLE);
                                List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
                                for(int i = 0; i < ((result.getOrders().size() > 2) ? 2 :result.getOrders().size()); i++)
                                {
                                    orderDetailsList.add(result.getOrders().get(i));
                                }

                                lv_mysearch.setAdapter(new SwipeRefreshXOrderAdapater(SearchActivity.this, orderDetailsList, AppConfig.ORDERSTATUS_MAIN));
                                if(result.getOrders().size() > 2)
                                {
                                    tv_servicenmore.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    tv_servicenmore.setVisibility(View.GONE);
                                }
                                setListViewHeightBasedOnChildren(lv_mysearch);
                            }
                            else
                            {
                                rl_service.setVisibility(View.GONE);
                            }
                            if(result.getQuestions() != null && !result.getQuestions().isEmpty())
                            {
                                rl_question.setVisibility(View.VISIBLE);
                                List<Question> questionList = new ArrayList<Question>();
                                for(int i = 0; i < ((result.getQuestions().size() > 2) ? 2 :result.getQuestions().size()); i++)
                                {
                                    questionList.add(result.getQuestions().get(i));
                                }
                                lv_myquestion.setAdapter(new SwipeRefreshXQuestionAdapater(SearchActivity.this,questionList));
                                if(result.getQuestions().size() > 2)
                                {
                                    tv_questionmore.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    tv_questionmore.setVisibility(View.GONE);
                                }
                                setListViewHeightBasedOnChildren(lv_myquestion);
                            }
                            else
                            {
                                rl_question.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            tv_empty.setVisibility(View.VISIBLE);
                        }


                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                    }
                });
            }
            else
            {
                rl_service.setVisibility(View.GONE);
                rl_question.setVisibility(View.GONE);
                tv_questionmore.setVisibility(View.GONE);
                tv_servicenmore.setVisibility(View.GONE);
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
            case R.id.tv_servicenmore:

                intent.setClass(this,ServiceMoreActivity.class);
                intent.putExtra("edittext",et_search.getText().toString());
                startActivity(intent);
                break;
            case R.id.tv_questionmore:

                intent.setClass(this,QuestionMoreActivity.class);
                intent.putExtra("edittext",et_search.getText().toString());
                startActivity(intent);
                break;
        }

    }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("搜索页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("搜索页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
