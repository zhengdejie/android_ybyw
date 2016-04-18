package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.Nearby;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.ReportCategory;
import appframe.appframe.dto.UserBrief;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.widget.swiperefresh.RelativenetGridViewAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshX;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXNearbyAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXReportAdapater;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back;
    private Button btn_report;
    private EditText et_report;
    ListView listView;
    SwipeRefreshXNearbyAdapater adapater;
    OrderDetails orderDetails;
    List<ReportCategory> reportCategories;
    private UserBrief userBrief;
    String orderID ="",userID="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
        initdata();
    }

    protected void init() {
        btn_report = (Button) findViewById(R.id.btn_report);
        tb_title = (TextView) findViewById(R.id.tb_title);
        tb_back = (TextView) findViewById(R.id.tb_back);
        et_report = (EditText) findViewById(R.id.et_report);
        tb_title.setText("举报");
//        tb_back.setText("任务详情");
        tb_back.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.lv_report);
        Intent intent = this.getIntent();
        orderDetails = (OrderDetails)intent.getSerializableExtra("OrderDetails");
        if(orderDetails != null)
        {
            orderID = String.valueOf(orderDetails.getId());
        }
        userBrief = (UserBrief)intent.getSerializableExtra("FriendsInfo");
        if(userBrief != null)
        {
            userID = String.valueOf(userBrief.getId());
        }
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Nearby nearby = (Nearby) parent.getAdapter().getItem(position);
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                intent.setClass(ReportActivity.this, FriendsInfoActivity.class);
//                bundle.putSerializable("NearBy", nearby);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

    }

    protected void initdata()
    {

        Http.request(ReportActivity.this, API.GET_REPORTCATEGORY,

                new Http.RequestListener<List<ReportCategory>>() {
                    @Override
                    public void onSuccess(List<ReportCategory> result) {
                        super.onSuccess(result);
                        reportCategories = result;
                        listView.setAdapter(new SwipeRefreshXReportAdapater(ReportActivity.this,result));
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
            case R.id.btn_report:
                if(isSelected()) {
                    Http.request(ReportActivity.this, API.POST_REPORT,
                            Http.map("OrderId", orderID.equals("") ? "":orderID,
                                    "ReportCategory", getReportId(),
                                    "Content",et_report.getText().toString(),
                                    "ReporteeId",userID.equals("") ? "" :userID),


                            new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);

//                                listView.setAdapter(new SwipeRefreshXReportAdapater(ReportActivity.this,result));
                                    Toast.makeText(ReportActivity.this, "投诉成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
                else
                {
                    Toast.makeText(ReportActivity.this,"至少选择一个投诉理由",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private String getReportId()
    {
        String ReceivedID ="" ;
        for(ReportCategory reportCategory : reportCategories)
        {
            if(reportCategory.getCheck().equals("Checked"))
            {
                ReceivedID = String.valueOf(reportCategory.getId());
            }
        }

        return ReceivedID;
    }

    private boolean isSelected()
    {
        for(ReportCategory reportCategory : reportCategories)
        {
            if(reportCategory.getCheck().equals("Checked"))
            {
                return true;
            }
        }

        return false;
    }


}

