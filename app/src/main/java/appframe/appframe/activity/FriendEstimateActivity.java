package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.FriendEvaluationDetail;
import appframe.appframe.dto.OrderComment;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendEstimateAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXMyMissionAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderComment;
import appframe.appframe.widget.tagview.Tag;
import appframe.appframe.widget.tagview.TagView;

/**
 * Created by Administrator on 2015/9/8.
 */
public class FriendEstimateActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_addtag,tb_action,tv_empty;
    EditText edit_tag;
    TagView tagView;
    String userID;
    int page = 1;
    SwipeRefreshXFriendEstimateAdapater swipeRefreshXFriendEstimateAdapater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendestimate);
        init();
    }

    public void init()
    {
//        edit_tag = (EditText)findViewById(R.id.edit_tag);
//        tagView = (TagView)findViewById(R.id.tagview);
//        tv_addtag = (TextView)findViewById(R.id.tv_addtag);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_action = (TextView)findViewById(R.id.tb_action);
        tv_empty = (TextView)findViewById(R.id.tv_empty);
        tb_back.setText("TA的口碑");
        tb_title.setText("好友评价");
        Drawable drawable = getResources().getDrawable(R.drawable.add);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tb_action.setCompoundDrawables(null, null, drawable, null);
        tb_back.setOnClickListener(this);
        tb_action.setOnClickListener(this);
        userID = getIntent().getStringExtra("UserID");
//        tv_addtag.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_friendestimate);

        getFriendEvaluation();

        // 下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", "1");
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(FriendEstimateActivity.this, API.GET_FEVALUATION, new Object[]{userID, Http.getURL(map)}, new Http.RequestListener<List<FriendEvaluationDetail>>() {
                    @Override
                    public void onSuccess(List<FriendEvaluationDetail> result) {
                        super.onSuccess(result);
                        page = 1;
                        swipeRefresh.setRefreshing(false);
                        swipeRefreshXFriendEstimateAdapater = new SwipeRefreshXFriendEstimateAdapater(FriendEstimateActivity.this, result);
                        listView.setAdapter(swipeRefreshXFriendEstimateAdapater);
                        if (result != null && result.size() != 0) {
                            tv_empty.setVisibility(View.GONE);
                        } else {
                            tv_empty.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                page++;
                Map<String, String> map = new HashMap<String, String>();
                map.put("Page", String.valueOf(page));
                map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
                Http.request(FriendEstimateActivity.this, API.GET_FEVALUATION, new Object[]{userID, Http.getURL(map)}, new Http.RequestListener<List<FriendEvaluationDetail>>() {
                    @Override
                    public void onSuccess(List<FriendEvaluationDetail> result) {
                        super.onSuccess(result);

                        swipeRefresh.setLoading(false);
                        if (result != null) {

                            loadMore(swipeRefreshXFriendEstimateAdapater, result);
                        }

                    }

                    @Override
                    public void onFail(String code) {
                        super.onFail(code);
                        swipeRefresh.setLoading(false);
                    }
                });


            }
        });
    }

    public void getFriendEvaluation()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", String.valueOf(AppConfig.ORDER_SIZE));
        Http.request(this, API.GET_FEVALUATION, new Object[]{userID,Http.getURL(map)}, new Http.RequestListener<List<FriendEvaluationDetail>>() {
            @Override
            public void onSuccess(List<FriendEvaluationDetail> result) {
                super.onSuccess(result);
                swipeRefreshXFriendEstimateAdapater = new SwipeRefreshXFriendEstimateAdapater(FriendEstimateActivity.this, result);
                listView.setAdapter(swipeRefreshXFriendEstimateAdapater);
                if(result != null && result.size() != 0) {
                    tv_empty.setVisibility(View.GONE);
                }
                else {
                    tv_empty.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void loadMore(SwipeRefreshXFriendEstimateAdapater adapater, List<FriendEvaluationDetail> orderDetailses) {
        adapater.addItems(orderDetailses);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.tb_action:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_leavemessage, (ViewGroup) findViewById(R.id.dialog));
                final EditText comment = (EditText)layout.findViewById(R.id.et_message);
                final CheckBox cb_anonymous = (CheckBox)layout.findViewById(R.id.cb_anonymous);
                cb_anonymous.setVisibility(View.GONE);
                builder.setTitle("评价").setView(
                        layout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!comment.getText().toString().equals("")) {
                            Http.request(FriendEstimateActivity.this, API.FRIENDS_EVALUATION, new Object[]{userID}, Http.map(
                                            "Praise", comment.getText().toString()),
                                    new Http.RequestListener<String>() {
                                        @Override
                                        public void onSuccess(String result) {
                                            super.onSuccess(result);
                                            getFriendEvaluation();

                                        }
                                    });
                            //swipeRefreshXFriendEstimateAdapater.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(FriendEstimateActivity.this,"评论不能为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
//            case R.id.tv_addtag:
//                if (edit_tag.getText().toString()!=null&&!edit_tag.getText().toString().equals("")) {
//                    String tagTitle= edit_tag.getText().toString();
//                    Tag tag = new Tag(tagTitle);
//                    tag.isDeletable=true;
//                    tagView.addTag(tag);
//                }
//                break;
        }

    }
}
