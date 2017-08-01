package appframe.appframe.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import appframe.appframe.R;
import appframe.appframe.activity.HomeActivity;
import appframe.appframe.app.AppConfig;
import appframe.appframe.utils.FileUtil;
import appframe.appframe.utils.NotificationUtils;

/**
 * Created by Administrator on 2015/12/29.
 */
public class UpdateAPKService extends Service{
    private  int NOTIFICATION_ID =143434245;
    private NotificationManager nm;
    private Notification notification;
    private NotificationCompat.Builder cBuilder;
    private Notification.Builder nBuilder;
    int requestCode = (int) SystemClock.uptimeMillis();
    private static final int FLAG = PendingIntent.FLAG_CANCEL_CURRENT;

    int smallIcon = R.drawable.logomini;

    private static final int TIMEOUT = 10 * 1000;// 超时
    private static final String down_url = AppConfig.QINIU_HOST +"app-debug.apk";
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FileUtil.createFile("ybw");
        createNotification();
        createThread();
        return super.onStartCommand(intent, flags, startId);
    }

    public void createThread() {
        /***
         * 更新UI
         */
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWN_OK:
                        // 下载完成，点击安装
                        Uri uri = null;
                        if(Build.VERSION.SDK_INT >=24)
                        {
                            uri = FileProvider.getUriForFile(UpdateAPKService.this, "appframe.appframe.android7.fileprovider", FileUtil.updateFile);
                        }
                        else
                        {
                            uri = Uri.fromFile(FileUtil.updateFile);
                        }
//                        Uri uri = Uri.fromFile(FileUtil.updateFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        intent.setDataAndType(uri,
                                "application/vnd.android.package-archive");

                        startActivity(intent);
                        nm.cancel(NOTIFICATION_ID);
//                        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateAPKService.this, requestCode, intent, FLAG);
//
//                        cBuilder.setContentText("下载完成，请点击安装").setProgress(0, 0, false);
//                        cBuilder.setContentIntent(pendingIntent);// 该通知要启动的Intent
//                        sent();

                        stopSelf();
                        break;
                    case DOWN_ERROR:
                        cBuilder.setContentText("下载失败").setProgress(0, 0, false);
                        stopSelf();
                        break;

                    default:
                        stopSelf();
                        break;
                }

            }

        };

        final Message message = new Message();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    long downloadSize = downloadUpdateFile(down_url,
                            FileUtil.updateFile.toString());
                    if (downloadSize > 0) {
                        // 下载成功
                        message.what = DOWN_OK;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message.what = DOWN_ERROR;
                    handler.sendMessage(message);
                }

            }
        }).start();
    }

    public void createNotification()
    {
        // 获取系统服务来初始化对象
        nm = (NotificationManager)getSystemService(Activity.NOTIFICATION_SERVICE);
        cBuilder = new NotificationCompat.Builder(this);
        Intent returnIntent = new Intent(this, HomeActivity.class);
        setCompatBuilder(returnIntent, smallIcon, "正在下载最新版本", "下载最新版本", "正在下载");
    }

    /**
     * 设置在顶部通知栏中的各种信息
     *
     * @param intent
     * @param smallIcon
     * @param ticker
     */
    private void setCompatBuilder(Intent intent, int smallIcon, String ticker,
                                  String title, String msg) {
        // 如果当前Activity启动在前台，则不开启新的Activity。
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 当设置下面PendingIntent.FLAG_UPDATE_CURRENT这个参数的时候，常常使得点击通知栏没效果，你需要给notification设置一个独一无二的requestCode
        // 将Intent封装进PendingIntent中，点击通知的消息后，就会启动对应的程序
        PendingIntent pIntent = PendingIntent.getActivity(this,
                requestCode, intent, FLAG);

        cBuilder.setContentIntent(pIntent);// 该通知要启动的Intent

        cBuilder.setSmallIcon(smallIcon);// 设置顶部状态栏的小图标
        cBuilder.setTicker(ticker);// 在顶部状态栏中的提示信息

        cBuilder.setContentTitle(title);// 设置通知中心的标题
        cBuilder.setContentText(msg);// 设置通知中心中的内容
        cBuilder.setWhen(System.currentTimeMillis());

        /*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
         * 不设置的话点击消息后也不清除，但可以滑动删除
         */
        cBuilder.setAutoCancel(true);
        // 将Ongoing设为true 那么notification将不能滑动删除
        // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
         * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
         */
        cBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        /*
         * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
         * Notification.DEFAULT_SOUND：系统默认铃声。
         * Notification.DEFAULT_VIBRATE：系统默认震动。
         * Notification.DEFAULT_LIGHTS：系统默认闪光。
         * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
         */
        cBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
    }

    private void sent() {
        notification = cBuilder.build();
        // 发送该通知
        nm.notify(NOTIFICATION_ID, notification);
    }
    /***
     * 下载文件
     *
     * @return
     * @throws MalformedURLException
     */
    public long downloadUpdateFile(String down_url, String file)
            throws Exception {
        int down_step = 5;// 提示step
        int totalSize;// 文件总大小
        int downloadCount = 0;// 已经下载好的大小
        int updateCount = 0;// 已经上传的文件大小
        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }
        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
        byte buffer[] = new byte[1024];
        int readsize = 0;
        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
            /**
             * 每次增张5%
             */
            if (updateCount == 0
                    || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                // 参数：1.最大进度， 2.当前进度， 3.是否有准确的进度显示
                cBuilder.setProgress(100, updateCount, false);
                sent();
            }

        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return downloadCount;

    }
}
