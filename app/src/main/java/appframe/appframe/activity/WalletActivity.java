package appframe.appframe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/10/14.
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {
    private TextView tb_title,tb_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        init();
    }

    protected void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title.setText("我的钱包");
        tb_back.setText("个人中心");
        tb_back.setOnClickListener(this);
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
