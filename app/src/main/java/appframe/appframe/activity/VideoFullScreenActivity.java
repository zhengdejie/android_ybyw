package appframe.appframe.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import appframe.appframe.R;
import appframe.appframe.utils.PackageUtils;
import appframe.appframe.utils.UriHandler;

/**
 * Created by Administrator on 2017/6/10.
 */

public class VideoFullScreenActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_close,iv_play;
    private RelativeLayout fl_vedio;
    private appframe.appframe.widget.VideoView.MyVideoView vv_person;
    String VideoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videofullscreen);
        init();
        initDataVideo();
    }
    protected  void init()
    {
        fl_vedio = (RelativeLayout)findViewById(R.id.fl_vedio);
        vv_person = (appframe.appframe.widget.VideoView.MyVideoView)findViewById(R.id.vv_person);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_play = (ImageView)findViewById(R.id.iv_play);

        fl_vedio.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        iv_play.setOnClickListener(this);

    }

    protected  void initDataVideo()
    {
        //网络视频
        String videoUrl = (String)getIntent().getSerializableExtra("VideoURL");
        Uri uri = Uri.parse(videoUrl);
        //设置视频控制器
        MediaController mediaController = new MediaController(this);
        mediaController.setVisibility(View.INVISIBLE);
        vv_person.setMediaController(mediaController);

        //播放完成回调
        vv_person.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
        vv_person.setVideoURI(uri);
        vv_person.start();
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {

            iv_play.setVisibility(View.VISIBLE);
//            vv_person.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fl_vedio:
                if(!vv_person.isPlaying())
                {
                    iv_play.setVisibility(View.GONE);
                    vv_person.start();
                }
                else
                {
                    vv_person.pause();
                    iv_play.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.iv_play:
                if(!vv_person.isPlaying())
                {
                    iv_play.setVisibility(View.GONE);
                    vv_person.start();
                }
                else
                {
                    vv_person.pause();
                    iv_play.setVisibility(View.VISIBLE);

                }
                break;

            case R.id.iv_close:
                finish();
                break;

            default:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关于页面"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于页面"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
