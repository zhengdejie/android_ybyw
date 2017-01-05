package appframe.appframe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.widget.swiperefresh.OrderDetailsViewPagerAdapter;
import appframe.appframe.widget.swiperefresh.ViewPagerAdapter;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.R.attr.mode;

/**
 * Created by Administrator on 2016-09-19.
 */
public class OrderDetailsViewPager extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private OrderDetailsViewPagerAdapter vpAdapter;
    private List<View> views;
    private Intent getIntent;

    //引导图片资源
//    private static final int[] pics = { R.drawable.navigationone,
//            R.drawable.navigationtwo, R.drawable.navigationthree,
//            R.drawable.navigationfour };

    //底部小店图片
    private ImageView[] dots ;

    //记录当前选中位置
    private int currentIndex;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetailsviewpager);
        getIntent = this.getIntent();
        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //初始化引导图片列表

        for (String photsCount : getIntent.getStringExtra("PhotoPath").toString().split(","))
        {
            appframe.appframe.utils.PhotoView iv = new appframe.appframe.utils.PhotoView(this);
//            com.android.volley.toolbox.NetworkImageView iv = new com.android.volley.toolbox.NetworkImageView(this);
            iv.setLayoutParams(mParams);
            ImageUtils.setImageUrl(iv, photsCount,"1");
            iv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

                @Override
                public void onPhotoTap(View arg0, float arg1, float arg2) {
                    finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                    finish();
                }
            });
            views.add(iv);
        }

        vp = (ViewPager) findViewById(R.id.viewpager);

        //初始化Adapter
        vpAdapter = new OrderDetailsViewPagerAdapter(OrderDetailsViewPager.this, views);
        vp.setAdapter(vpAdapter);
        //绑定回调
        vp.setOnPageChangeListener(this);
        vp.setCurrentItem(Integer.parseInt(getIntent.getStringExtra("Position")));
        //初始化底部小点
//        initDots();

    }



    //    private void initDots() {
//        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
//
//        dots = new ImageView[pics.length];
//
//        //循环取得小点图片
//        for (int i = 0; i < pics.length; i++) {
//            dots[i] = (ImageView) ll.getChildAt(i);
//            dots[i].setEnabled(true);//都设为灰色
//            dots[i].setOnClickListener(this);
//            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
//        }
//
//        currentIndex = 0;
//        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
//    }


    /**
     *设置当前的引导页
     */
//    private void setCurView(int position)
//    {
//        if (position < 0 || position >= pics.length) {
//            return;
//        }
//
//        vp.setCurrentItem(position);
//    }

    /**
     *这只当前引导小点的选中
     */
//    private void setCurDot(int positon)
//    {
//        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
//            return;
//        }
//
//        dots[positon].setEnabled(false);
//        dots[currentIndex].setEnabled(true);
//
//        currentIndex = positon;
//    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
//        setCurDot(arg0);
    }

    @Override
    public void onClick(View v) {

    }
}

