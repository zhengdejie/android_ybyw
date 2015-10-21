package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import appframe.appframe.R;
import appframe.appframe.utils.Auth;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXFriendEstimateAdapater;
import appframe.appframe.widget.tagview.Tag;
import appframe.appframe.widget.tagview.TagView;

/**
 * Created by Administrator on 2015/9/8.
 */
public class FriendEstimateActivity extends BaseActivity implements View.OnClickListener {
    SwipeRefreshX swipeRefresh;
    ListView listView;
    TextView tb_title,tb_back,tv_addtag;
    EditText edit_tag;
    TagView tagView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendestimate);
        init();
    }

    public void init()
    {
        edit_tag = (EditText)findViewById(R.id.edit_tag);
        tagView = (TagView)findViewById(R.id.tagview);
        tv_addtag = (TextView)findViewById(R.id.tv_addtag);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("我的口碑");
        tb_title.setText("好友评价");
        tb_back.setOnClickListener(this);
        tv_addtag.setOnClickListener(this);

        swipeRefresh = (SwipeRefreshX)findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        listView = (ListView)findViewById(R.id.lv_friendestimate);
        listView.setAdapter(new SwipeRefreshXFriendEstimateAdapater(this));

//        Http.request(this, API.GET_SELFORDER, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<OrderDetails>>() {
//            @Override
//            public void onSuccess(List<OrderDetails> result) {
//                super.onSuccess(result);
//
//                listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result));
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        //Toast.makeText(getActivity(), "df", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
//                    }
//                });
//
//            }
//        });

        // 设置下拉刷新监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Toast.makeText(FriendEstimateActivity.this, "refresh", Toast.LENGTH_SHORT).show();

            }
        });
        // 加载监听器
        swipeRefresh.setOnLoadListener(new SwipeRefreshX.OnLoadListener() {

            @Override
            public void onLoad() {

                Toast.makeText(FriendEstimateActivity.this, "load", Toast.LENGTH_SHORT).show();

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
            case R.id.tv_addtag:
                if (edit_tag.getText().toString()!=null&&!edit_tag.getText().toString().equals("")) {
                    String tagTitle= edit_tag.getText().toString();
                    Tag tag = new Tag(tagTitle);
                    tag.isDeletable=true;
                    tagView.addTag(tag);
                }
                break;
        }

    }
}
