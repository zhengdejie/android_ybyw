package appframe.appframe.utils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.github.snowdream.android.util.Log;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;

/**
 * Created by dashi on 15/6/21.
 */
public class UploadUtils {
    final static String TAG = "UploadUtils";
    public interface Callback{
        void done(String id);
    }
    public static void uploadFile(File f, final Callback cb){

        UploadManager um = new UploadManager();
        Toast.makeText(App.instance, "上传中...", Toast.LENGTH_SHORT).show();
        um.put(f, String.format("%s.jpg", UUID.randomUUID().toString()), AppConfig.QINIU_UPLOAD_TOKEN, new UpCompletionHandler() {

            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {

                if (responseInfo != null && responseInfo.isOK()) {
                    //Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show();
                    cb.done(s);

                    return;
                }
                Toast.makeText(App.instance, "上传失败", Toast.LENGTH_SHORT).show();
                cb.done(null);
            }
        }, null);
    }
    public static void uploadImage(File f, final Callback cb){

        File resizedFile = f;
        File fileToUpload = f;
        if(f.length() > 500 * 1024){
            // 如果文件大于500k

            Bitmap bm = Utils.getResizedBitmap(f, AppConfig.MAX_BITMAP_WIDTH, AppConfig.MAX_BITMAP_HEIGHT);
            try {
                resizedFile = File.createTempFile("wf-", ".resized");
            } catch (IOException e) {
                Log.e(TAG, "cannot create tmp file for resize", e);
                cb.done(null);
                return;
            }
            Utils.saveBitmap(bm, resizedFile, 90);
            fileToUpload = resizedFile;
        }
        uploadFile(fileToUpload, cb);
    }
}
