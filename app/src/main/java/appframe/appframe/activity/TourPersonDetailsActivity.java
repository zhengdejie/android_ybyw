package appframe.appframe.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.mobileim.channel.itf.tribe.MemberLevelSettingPacker;
import com.alipay.sdk.app.PayTask;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.CommentDetailResponseDto;
import appframe.appframe.dto.GuideOrderComments;
import appframe.appframe.dto.GuideTour;
import appframe.appframe.dto.GuideTourOrder;
import appframe.appframe.dto.PayResult;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.LoginSampleHelper;
import appframe.appframe.utils.Player;
import appframe.appframe.widget.HorizontalScrollView.HorizontalScrollViewAdapter;
import appframe.appframe.widget.HorizontalScrollView.MyHorizontalScrollView;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXGuidComment;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderComment;

/**
 * Created by Administrator on 2017/5/12.
 */

public class TourPersonDetailsActivity  extends BaseActivity implements View.OnClickListener{

    TextView tb_title,tb_back,btn_ask,btn_sendorder,tv_voice,tv_name,tv_tour1location,tv_price,tv_ts,tv_numofevaluation,tv_guideorder;
//    tv_experince,tv_description,
    com.android.volley.toolbox.NetworkImageView iv_person;
    ImageView iv_play,iv_gender,iv_guideid;
    appframe.appframe.utils.CircleImageViewCustomer civ_avatar;
    RatingBar rb_totalvalue;
    VideoView vv_person;
    Intent intent = new Intent();
    FrameLayout fl_vedio;
//    Player player;
    MediaPlayer mediaPlayer;
    String VideoURL, VoiceURL;
//    private MyCount mc;
    private static final int SDK_PAY_FLAG = 1;
    private MyHorizontalScrollView mHorizontalScrollView;
    private HorizontalScrollViewAdapter mAdapter;
//    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
//            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
//            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h,
//            R.drawable.l));
    private ListView lv_evaluation;
    GuideTour guideTour;
    GuideTourOrder guideTourOrder;
    SwipeRefreshXGuidComment swipeRefreshXGuidComment;
    public static Activity instance = null;

    private Handler mHandler = new Handler();

    Runnable payRunnable = new Runnable() {

        @Override
        public void run() {

//                            Message msg = new Message();
//                            msg.what = SDK_PAY_FLAG;
//                            msg.obj = resultInfo;
////                                                    msg.setData(bundle);
//                            mHandler.sendMessage(msg);

            tv_voice.setText(timeParse(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
            if(tv_voice.getText().equals("00"))
            {
                mHandler.removeCallbacks(payRunnable);
            }
            else {
                mHandler.postDelayed(this, 1000);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourpersondetails);
        instance = this;
        initViews();
        initData();


    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            iv_person.setVisibility(View.VISIBLE);
            iv_play.setVisibility(View.VISIBLE);
//            vv_person.start();
        }
    }

    protected  void initDataVideo()
    {
        //网络视频
        String videoUrl = VideoURL;
        Uri uri = Uri.parse(videoUrl);
        //设置视频控制器
        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.INVISIBLE);
        vv_person.setMediaController(mediaController);

        //播放完成回调
        vv_person.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
        vv_person.setVideoURI(uri);
    }

    protected  void initDataMedia()
    {
        //音频
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.reset();
            String url = VoiceURL;
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();//prepare之后自动播放
            //mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tv_voice.setText(timeParse(mediaPlayer.getDuration()));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                tv_voice.setText(timeParse(mediaPlayer.getDuration()));
                mediaPlayer.seekTo(0);
//                mediaPlayer.reset();
//                try {
//                    mediaPlayer.prepare();
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
                mHandler.removeCallbacks(payRunnable);

            }
        });
    }

    protected  void initData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Page", "1");
        map.put("Size", "20");
        Http.request(TourPersonDetailsActivity.this, API.GETGUIDECOMMENT, new Object[]{guideTour.getId(),Http.getURL(map)},
                new Http.RequestListener<GuideOrderComments>() {
                    @Override
                    public void onSuccess(GuideOrderComments result) {
                        super.onSuccess(result);
                        if(result != null) {
                            if (result.getTotalComments() > 0) {
                                swipeRefreshXGuidComment = new SwipeRefreshXGuidComment(TourPersonDetailsActivity.this, result.getComments());
                                lv_evaluation.setAdapter(swipeRefreshXGuidComment);
                                setListViewHeightBasedOnChildren(lv_evaluation);
                                tv_numofevaluation.setText(String.format("全部评论(%d条)", result.getTotalComments()));
                            } else {
                                tv_numofevaluation.setText("全部评论(0条)");
                            }
                        }
                    }
                });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
//        sv_main.fullScroll(View.FOCUS_UP);
    }

    protected void initViews()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        btn_ask = (TextView)findViewById(R.id.btn_ask);
        btn_sendorder = (TextView)findViewById(R.id.btn_sendorder);
        tv_name = (TextView)findViewById(R.id.tv_name);
        vv_person = (VideoView)findViewById(R.id.vv_person);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_person = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.iv_person);
        fl_vedio = (FrameLayout) findViewById(R.id.fl_vedio);
        tv_voice = (TextView)findViewById(R.id.tv_voice);
//        tv_experince = (TextView)findViewById(R.id.tv_experince);
//        tv_description = (TextView)findViewById(R.id.tv_description);
        mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
        civ_avatar = (appframe.appframe.utils.CircleImageViewCustomer)findViewById(R.id.civ_avatar);
        rb_totalvalue = (RatingBar) findViewById(R.id.rb_totalvalue);
        tv_tour1location = (TextView)findViewById(R.id.tv_tour1location);
        tv_price = (TextView)findViewById(R.id.tv_price);
        tv_ts = (TextView)findViewById(R.id.tv_ts);
        iv_gender = (ImageView) findViewById(R.id.iv_gender);
        iv_guideid = (ImageView) findViewById(R.id.iv_guideid);
        lv_evaluation = (ListView) findViewById(R.id.lv_evaluation);
        tv_numofevaluation = (TextView)findViewById(R.id.tv_numofevaluation);
        tv_guideorder = (TextView)findViewById(R.id.tv_guideorder);


        guideTour = (GuideTour)getIntent().getSerializableExtra("TourGuid");
        guideTourOrder = (GuideTourOrder)getIntent().getSerializableExtra("GuideTourOrder");
        if(guideTourOrder != null)
        {
            guideTour = guideTourOrder.getGuide();
        }

        if(guideTour != null)
        {
            tv_name.setText(guideTour.getUser().getName());
            int experience = guideTour.getGuideExperienceInDays()/365;
            String experienceyear = String.valueOf(experience+"年");
//            tv_experince.setText(experienceyear);
//            tv_description.setText(guideTour.getDescription());
            VideoURL = AppConfig.QINIU_HOST + guideTour.getVideoSampleLink();
            VoiceURL = AppConfig.QINIU_HOST + guideTour.getVoiceSampleLink();
            if(guideTour.getVoiceSampleLink().equals("") || guideTour.getVoiceSampleLink() == null)
            {
                tv_voice.setVisibility(View.GONE);
            }
            else
            {
                tv_voice.setVisibility(View.VISIBLE);
                initDataMedia();
            }
            if(guideTour.getVideoSampleLink().equals("") || guideTour.getVideoSampleLink() == null)
            {
                fl_vedio.setEnabled(false);
                iv_play.setVisibility(View.INVISIBLE);
            }
            else{
                fl_vedio.setEnabled(true);
                iv_play.setVisibility(View.VISIBLE);
                initDataVideo();
            }
            ImageUtils.setImageUrl(iv_person, guideTour.getPhotos().get(0),"0");

            if(guideTour.getVerifiedTourGuide() == 1)
            {
                iv_guideid.setVisibility(View.VISIBLE);
            }
            else{
                iv_guideid.setVisibility(View.GONE);
            }

            tv_guideorder.setText(String.format("接单%s次",guideTour.getTotalServingOrders()));

            mAdapter = new HorizontalScrollViewAdapter(this, guideTour.getPhotos());

            if(guideTour.getUser().getAvatar() == null) {
                if (guideTour.getGender().equals(getResources().getString(R.string.male).toString())) {
                    civ_avatar.setDefaultImageResId(R.drawable.maleavatar);
                    iv_gender.setImageResource(R.drawable.male);

                } else {
                    civ_avatar.setDefaultImageResId(R.drawable.femaleavatar);
                    iv_gender.setImageResource(R.drawable.female);
                }
            }
            else {
                ImageUtils.setImageUrl(civ_avatar, guideTour.getUser().getAvatar());
                if (guideTour.getGender().equals(getResources().getString(R.string.male).toString())) {
                    iv_gender.setImageResource(R.drawable.male);

                } else {
                    iv_gender.setImageResource(R.drawable.female);
                }
            }


            rb_totalvalue.setRating((float) guideTour.getGuideLevel());
            tv_tour1location.setText(guideTour.getItinerary());
//            int total = (int)(guideTour.getHourlyRatePrice() * guideTour.getMinimumHours());
            StringBuilder sb = new StringBuilder();
            sb.append("￥").append(guideTour.getHourlyRatePrice()).append("元/小时");
            tv_price.setText(sb);
            StringBuilder sbFeatures = new StringBuilder();
            for(String features : guideTour.getFeatures())
            {
                sbFeatures.append( features +" ");
            }

            tv_ts.setText(sbFeatures.toString());
        }
        else
        {
            finish();
            Toast.makeText(this,"请重新加载乐游板块",Toast.LENGTH_SHORT).show();
        }

        lv_evaluation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        //添加滚动回调
//        mHorizontalScrollView
//                .setCurrentImageChangeListener(new CurrentImageChangeListener()
//                {
//                    @Override
//                    public void onCurrentImgChanged(int position,
//                                                    View viewIndicator)
//                    {
//                        mImg.setImageResource(mDatas.get(position));
//                        viewIndicator.setBackgroundColor(Color
//                                .parseColor("#AA024DA4"));
//                    }
//                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener()
        {

            @Override
            public void onClick(View view, int position)
            {
                Intent intent = new Intent();
                intent.setClass(TourPersonDetailsActivity.this, OrderDetailsViewPager.class);
                intent.putExtra("Position", String.valueOf(position));
                String Photos = "";
                for(String sb : guideTour.getPhotos())
                {
                    Photos += (sb + ",");
                }
                Photos.substring(0,Photos.length()-1);
                intent.putExtra("PhotoPath", Photos);
                startActivity(intent);
//                mImg.setImageResource(mDatas.get(position));
//                view.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });
        //设置适配器
        mHorizontalScrollView.initDatas(mAdapter,guideTour.getPhotos().size());

        tb_back.setText("返回");
        tb_title.setText("个人信息");
        tb_back.setOnClickListener(this);
        btn_ask.setOnClickListener(this);
        btn_sendorder.setOnClickListener(this);
        fl_vedio.setOnClickListener(this);
        tv_voice.setOnClickListener(this);

//        player = new Player(tv_voice);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.tb_back:
                finish();
                break;
            case R.id.btn_ask:
                LoginSampleHelper ls = LoginSampleHelper.getInstance();
//                String target = "3000";
                intent = ls.getIMKit().getChattingActivityIntent(String.valueOf(guideTour.getUser().getId()));
                startActivity(intent);
                break;
            case R.id.btn_sendorder:
                Intent intent = new Intent();
                intent.setClass(TourPersonDetailsActivity.this, TourOrderSendActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("TourGuid", guideTour);
                intent.putExtras(bundle);

                startActivity(intent);

                break;
            case R.id.fl_vedio:
//                if(!vv_person.isPlaying())
//                {
//                    iv_person.setVisibility(View.GONE);
//                    iv_play.setVisibility(View.GONE);
//                    vv_person.start();
//                }
//                else
//                {
//                    vv_person.pause();
//                    iv_person.setVisibility(View.GONE);
//                    iv_play.setVisibility(View.VISIBLE);
//
//                }
                Intent videoIntent = new Intent();
                videoIntent.setClass(TourPersonDetailsActivity.this, VideoFullScreenActivity.class);

                Bundle videoBundle = new Bundle();
                videoBundle.putSerializable("VideoURL", VideoURL);
                videoIntent.putExtras(videoBundle);

                startActivity(videoIntent);
                break;
            case R.id.tv_voice:

                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                }
                else
                {
                    mediaPlayer.start();

//                    mHandler.post(payRunnable);
                    mHandler.postDelayed(payRunnable, 1000);
                    // 必须异步调用
//                    Thread payThread = new Thread(payRunnable);
//                    payThread.start();
                }

                break;

        }
    }



    String timeParse(long duration) {
        String time = "" ;

        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;

        long second = Math.round((float)seconds/1000) ;

//        if( minute < 10 ){
//            time += "0" ;
//        }
//        time += minute+":" ;

        if( second < 10 ){
            time += "0" ;
        }
        time += second ;

        return time ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("找导游具体资质页"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!guideTour.getVoiceSampleLink().equals("") && guideTour.getVoiceSampleLink() != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.seekTo(0);
        }
//        mediaPlayer.pause();
//        mediaPlayer.seekTo(0);
        MobclickAgent.onPageEnd("找导游具体资质页"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}


