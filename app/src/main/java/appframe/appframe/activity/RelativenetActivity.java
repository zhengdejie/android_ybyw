package appframe.appframe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/10/14.
 */
public class RelativenetActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_myself,tv_oneclass,tv_twoclass,tb_title,tb_back;
    private ProgressBar pb_oneclass,pb_twoclass;
    private int iCount = 0;
    protected static final int ONE_GOON = 0x10000;
    protected static final int ONE_END = 0x10001;
    protected static final int TWO_GOON = 0x10002;
    protected static final int TWO_END = 0x10003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relativenet);
        init();
    }

    protected void init()
    {
        tv_myself = (TextView)findViewById(R.id.tv_myself);
        tv_oneclass = (TextView)findViewById(R.id.tv_oneclass);
        tv_twoclass = (TextView)findViewById(R.id.tv_twoclass);
        pb_oneclass =(ProgressBar)findViewById(R.id.pb_oneclass);
        pb_twoclass =(ProgressBar)findViewById(R.id.pb_twoclass);
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_title.setText("我和TA的关系网");
        tb_back.setText("需求单");
        tb_back.setOnClickListener(this);
        pb_oneclass.setVisibility(View.VISIBLE);
        pb_oneclass.setProgress(0);
        pb_twoclass.setProgress(0);



        //创建一个线程,每秒步长为5增加,到100%时停止
        Thread mThread = new Thread(new Runnable() {

            public void run() {

                for(int i = 0 ; i < 100; i++){
                    try{
                        iCount = i + 1;
                        if(i == 99)
                        {
                            Message msg = new Message();
                            msg.what = ONE_END;
                            mHandler.sendMessage(msg);
                        }
                        else {
                            Thread.sleep(30);
                            Message msg = new Message();
                            msg.what = ONE_GOON;
                            mHandler.sendMessage(msg);
                        }



                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        mThread.start();
    }

    //定义一个Handler
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {

                case ONE_GOON:
                    pb_oneclass.setProgress(iCount);
                    break;
                case ONE_END:
                    tv_oneclass.setVisibility(View.VISIBLE);
                    Thread mThread = new Thread(new Runnable() {

                        public void run() {

                            for(int i = 0 ; i < 100; i++){
                                try{
                                    iCount = i + 1;
                                    if(i == 99)
                                    {
                                        Message msg = new Message();
                                        msg.what = TWO_END;
                                        mHandler.sendMessage(msg);
                                    }
                                    else {
                                        Thread.sleep(30);
                                        Message msg = new Message();
                                        msg.what = TWO_GOON;
                                        mHandler.sendMessage(msg);
                                    }



                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                    mThread.start();
                    break;
                case TWO_GOON:
                    pb_twoclass.setVisibility(View.VISIBLE);
                    pb_twoclass.setProgress(iCount);
                    break;
                case TWO_END:
                    tv_twoclass.setVisibility(View.VISIBLE);
                    break;
            }
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
