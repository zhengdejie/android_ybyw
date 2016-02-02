package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderCandidate;

/**
 * Created by Administrator on 2015/11/9.
 */
public class CandidateActivity extends BaseActivity implements View.OnClickListener{
    private TextView tb_title,tb_back;
    private ListView lv_candidate ;
    private Button btn_receivedandclose,btn_receivedandsave;
    private List<UserDetail> list_candidate = new ArrayList<UserDetail>();
    OrderDetails orderDetails;

    //Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);
        init();
        initData();
    }
    private  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        lv_candidate = (ListView)findViewById(R.id.lv_candidate);
        btn_receivedandclose =(Button)findViewById(R.id.btn_receivedandclose);
        btn_receivedandsave =(Button)findViewById(R.id.btn_receivedandsave);
        tb_back.setText("需求单");
        tb_title.setText("接单候选人");
        tb_back.setOnClickListener(this);
        btn_receivedandclose.setOnClickListener(this);
        btn_receivedandsave.setOnClickListener(this);
        Intent intent = this.getIntent();
        orderDetails=(OrderDetails)intent.getSerializableExtra("OrderDetails");
        lv_candidate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CandidateActivity.this, FriendsInfoActivity.class);
                UserDetail userDetail = (UserDetail)parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Candidate", userDetail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    private  void initData()
    {
        Http.request(this, API.GET_CANDIDATE, new Object[]{orderDetails.getId()}, new Http.RequestListener<List<UserDetail>>() {
            @Override
            public void onSuccess(final List<UserDetail> result) {
                super.onSuccess(result);
                list_candidate = result;
                lv_candidate.setAdapter(new SwipeRefreshXOrderCandidate(CandidateActivity.this, result));

            }

            @Override
            public void onFail(String code) {
                super.onFail(code);

            }
        });

    }

    private String getReceiverId()
    {
        StringBuilder ReceivedID = new StringBuilder();
        for(UserDetail userDetail : list_candidate)
        {
            if(userDetail.getCheck().equals("Checked"))
            {
                ReceivedID.append(",").append(userDetail.getId());
            }
        }

        return ReceivedID.deleteCharAt(0).toString();
    }

    private boolean isSelected()
    {
        for(UserDetail userDetail : list_candidate)
        {
            if(userDetail.getCheck().equals("Checked"))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_receivedandclose:
                if(isSelected()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("确认让TA们接单并关闭单子吗?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Http.request(CandidateActivity.this, API.CONFIRMORDER, new Object[]{orderDetails.getId()}, Http.map(
                                    "ReceiverId", getReceiverId(),
                                    "OrderType", String.valueOf(orderDetails.getType()),
                                    "IsCancel", "TRUE"
                            ), new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);


                                }

                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);

                                }
                            });
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                else
                {
                    Toast.makeText(CandidateActivity.this,"至少选择一名接单人",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_receivedandsave:
                if(isSelected()) {
                    AlertDialog.Builder buildersave = new AlertDialog.Builder(this);
                    buildersave.setMessage("确认让TA们接单并保留单子吗?");
                    buildersave.setTitle("提示");
                    buildersave.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Http.request(CandidateActivity.this, API.CONFIRMORDER, new Object[]{orderDetails.getId()}, Http.map(
                                    "ReceiverId", getReceiverId(),
                                    "OrderType", String.valueOf(orderDetails.getType()),
                                    "IsCancel", "FALSE"
                            ), new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);


                                }

                                @Override
                                public void onFail(String code) {
                                    super.onFail(code);

                                }
                            });
                            dialog.dismiss();
                            finish();
                        }
                    });
                    buildersave.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    buildersave.create().show();
                }
                else
                {
                    Toast.makeText(CandidateActivity.this,"至少选择一名接单人",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
