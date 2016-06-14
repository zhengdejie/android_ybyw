package appframe.appframe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import appframe.appframe.dto.ConfirmedOrderDetail;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.OrderPay;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderCandidate;

/**
 * Created by Administrator on 2015/11/9.
 */
public class CandidateActivity extends BaseActivity implements View.OnClickListener{
    private TextView tb_title,tb_back;
    public static TextView tv_showtotal;
    private ListView lv_candidate ;
    private Button btn_receivedandclose,btn_receivedandsave;
    private List<ConfirmedOrderDetail> list_candidate = new ArrayList<ConfirmedOrderDetail>();
    OrderDetails orderDetails,orderCandidate;
    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    int orderID;
    String orderType,orderStatus,paymentMethod;
    double bounty = 0.0;

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
        tv_showtotal = (TextView)findViewById(R.id.tv_showtotal);
        tb_back.setText("需求单");
        tb_title.setText("接单候选人");
        tb_back.setOnClickListener(this);
        btn_receivedandclose.setOnClickListener(this);
        btn_receivedandsave.setOnClickListener(this);
        Intent intent = this.getIntent();
        orderDetails=(OrderDetails)intent.getSerializableExtra("OrderDetails");
        orderCandidate=(OrderDetails)intent.getSerializableExtra("Candidate");

        if(orderDetails != null)
        {
            orderID = orderDetails.getId();
            orderType = String.valueOf(orderDetails.getType());
            bounty = orderDetails.getBounty();
            orderStatus = orderDetails.getOrderStatus();
            paymentMethod = orderDetails.getPaymentMethod();
        }
        if(orderCandidate != null)
        {
            orderID = orderCandidate.getId();
            orderType = String.valueOf(orderCandidate.getType());
            bounty = orderCandidate.getBounty();
            orderStatus = orderCandidate.getOrderStatus();
            paymentMethod = orderCandidate.getPaymentMethod();
        }

        lv_candidate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CandidateActivity.this, FriendsInfoActivity.class);
                ConfirmedOrderDetail userDetail = (ConfirmedOrderDetail) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Candidate", userDetail.getReceiver());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    private  void initData()
    {
        Http.request(this, API.GET_CANDIDATE, new Object[]{orderID}, new Http.RequestListener<List<ConfirmedOrderDetail>>() {
            @Override
            public void onSuccess(final List<ConfirmedOrderDetail> result) {
                super.onSuccess(result);
                if (result != null) {
                    for (ConfirmedOrderDetail confirmedOrderDetail : result) {
                        list_candidate.add(confirmedOrderDetail);
                    }

                    lv_candidate.setAdapter(new SwipeRefreshXOrderCandidate(CandidateActivity.this, result));
                }

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
        for(ConfirmedOrderDetail confirmedOrderDetail : list_candidate)
        {
            if(confirmedOrderDetail.getReceiver().getCheck().equals("Checked"))
            {
                ReceivedID.append(",").append(confirmedOrderDetail.getReceiver().getId());
            }
        }

        return ReceivedID.deleteCharAt(0).toString();
    }

    private double getTotal()
    {
        double total = 0.0;
        for(ConfirmedOrderDetail confirmedOrderDetail : list_candidate)
        {
            if(confirmedOrderDetail.getReceiver().getCheck().equals("Checked"))
            {
                total += confirmedOrderDetail.getBid();
            }
        }

        return total;
    }

    private boolean isSelected()
    {
        for(ConfirmedOrderDetail confirmedOrderDetail : list_candidate)
        {
            if(confirmedOrderDetail.getReceiver().getCheck().equals("Checked"))
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
                            Http.request(CandidateActivity.this, API.CONFIRMORDER, new Object[]{orderID}, Http.map(
                                    "ReceiverId", getReceiverId(),
                                    "OrderType", orderType,
                                    "HasCancel", "TRUE",
                                    "Total",String.valueOf(getTotal())
                            ), new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);
                                    if( getTotal() > bounty) {
                                        OrderPay orderPay = new OrderPay();
                                        orderPay.setOrderId(orderID);
                                        orderPay.setAmount(getTotal() - bounty);
                                        orderPay.setCandidateId(getReceiverId());
                                        orderPay.setOrderStatus(orderStatus);
                                        intent.setClass(CandidateActivity.this, PayActivity.class);
                                        bundle.putSerializable("OrderPay", orderPay);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
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
                            Http.request(CandidateActivity.this, API.CONFIRMORDER, new Object[]{orderID}, Http.map(
                                    "ReceiverId", getReceiverId(),
                                    "OrderType", orderType,
                                    "HasCancel", "FALSE",
                                    "Total",String.valueOf(getTotal())
                            ), new Http.RequestListener<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    super.onSuccess(result);

                                    if( (getTotal() + bounty) > bounty && paymentMethod.equals("线上支付")) {
                                        OrderPay orderPay = new OrderPay();
                                        orderPay.setOrderId(orderID);
                                        orderPay.setAmount(getTotal());
                                        orderPay.setCandidateId(getReceiverId());
                                        orderPay.setOrderStatus(orderStatus);
                                        intent.setClass(CandidateActivity.this, PayActivity.class);
                                        bundle.putSerializable("OrderPay", orderPay);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
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
