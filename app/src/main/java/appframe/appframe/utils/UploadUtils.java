package appframe.appframe.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.github.snowdream.android.util.Log;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appframe.appframe.app.App;
import appframe.appframe.app.AppConfig;
import appframe.appframe.dto.UserContact;

/**
 * Created by dashi on 15/6/21.
 */
public class UploadUtils {
    final static String TAG = "UploadUtils";
    public interface Callback{
        void done(String id);
    }
    public static void uploadFile(File f, final Callback cb, String QINIU_UPLOAD_TOKEN){

        UploadManager um = new UploadManager();
        //Toast.makeText(App.instance, "上传中...", Toast.LENGTH_SHORT).show();
        um.put(f, String.format("%s_%s.jpg",String.valueOf(Auth.getCurrentUserId()), UUID.randomUUID().toString()), QINIU_UPLOAD_TOKEN, new UpCompletionHandler() {

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
    public static void uploadImage(File f, final Callback cb,String QINIU_UPLOAD_TOKEN){

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
        uploadFile(fileToUpload, cb, QINIU_UPLOAD_TOKEN);
    }

    public static List<UserContact> uploadContact(Context context)
    {
        List<UserContact> contactsList = new ArrayList<UserContact>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //At least one phone number
            if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0)
            {
                int ContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String ContactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Cursor cursorPhone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactID, null, null);
                while(cursorPhone.moveToNext())
                {
                    String ContactMobile = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Pattern pattern = Pattern.compile("^\\d{11}$");
                    Matcher matcher = pattern.matcher(ContactMobile.replace(" ",""));
                    if(matcher.matches()) {
                        if(ContactName != null && !ContactName.equals("") && ContactMobile != null && !ContactMobile.equals("")) {
                            UserContact userContact = new UserContact(ContactName, ContactMobile.replace(" ",""));
                            contactsList.add(userContact);
                        }
                    }
                }
                cursorPhone.close();
            }

        }
        cursor.close();
        return contactsList;
    }
}
