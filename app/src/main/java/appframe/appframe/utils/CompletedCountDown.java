package appframe.appframe.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/3/29.
 */
public class CompletedCountDown extends CountDownTimer {
    TextView textView;
    public CompletedCountDown(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }
    @Override
    public void onFinish() {
        textView.setVisibility(View.GONE);
    }
    @Override
    public void onTick(long millisUntilFinished) {
        SimpleDateFormat sdf = new SimpleDateFormat("还剩 dd 天 HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(millisUntilFinished);
        String text = sdf.format(date);

        textView.setText(text);

    }
}
