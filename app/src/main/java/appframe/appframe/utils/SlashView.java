package appframe.appframe.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-10-26.
 */
public class SlashView extends TextView {

    public SlashView(Context context) {
        super(context);
    }

    public SlashView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        Paint p = new Paint();
        p.setColor(Color.parseColor("#cddfeb"));// 设置颜色
        p.setAntiAlias(true);//设置线条等图形的抗锯齿
        p.setStrokeWidth((float) 7.0);
        int x = Utils.dpToPx(25);
        int y = x * getMeasuredHeight() / getMeasuredWidth();

        canvas.drawLine(x, getMeasuredHeight() - y, getMeasuredWidth() - x, y, p);
        super.onDraw(canvas);
    }
}
