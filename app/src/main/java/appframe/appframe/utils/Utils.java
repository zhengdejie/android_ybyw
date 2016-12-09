package appframe.appframe.utils;

/**
 * Created by dashi on 15/6/21.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.github.snowdream.android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appframe.appframe.BuildConfig;
import appframe.appframe.activity.LoginActivity;
import appframe.appframe.activity.SplashActivity;
import appframe.appframe.app.API;
import appframe.appframe.app.App;
import appframe.appframe.dto.UserLocation;

import static android.content.Context.ACTIVITY_SERVICE;

public final class Utils {
    static final String TAG = "Utils";
    static String getStringConfigFromUri(Uri uri, String name){
        if(uri == null) return null;
        String quoted = Pattern.quote(name);
        quoted = quoted.replaceAll("^\\\\Q", "");
        quoted = quoted.replaceAll("\\\\E$", "");
        quoted = quoted.replaceAll("%", "%%");
        String pattern = String.format("(^|.*[\\?\\&\\#])%s\\=([^\\&]*).*$", quoted);
        Pattern p = Pattern.compile(pattern);
        String uriStr = uri.toString();
        Matcher m = p.matcher(uriStr);
        if(m.matches()){
            try {
                return URLDecoder.decode(m.group(2), "utf-8");
            } catch (UnsupportedEncodingException e) {
                return m.group(2);
            }
        }
        return null;
    }
    public static boolean hasConfig(Uri uri, String name){
        return getStringConfigFromUri(uri, name) != null;
    }
    public static String getStringConfig(Uri uri, String name){
        return getStringConfig(uri, name, null);
    }
    public static String getStringConfig(Uri uri, String name,String defaultValue){
        String r = getStringConfigFromUri(uri, name);
        return r == null ? defaultValue : r;
    }
    public static int getIntegerConfig(Uri uri, String name){
        return getIntegerConfig(uri, name, 0);
    }
    public static int getIntegerConfig(Uri uri, String name, int defaultValue){
        String r = getStringConfigFromUri(uri, name);
        return r == null ? defaultValue : Integer.valueOf(r);
    }
    public static PackageInfo getPackageInfo(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "cannot read PackageInfo", e);
            return null;
        }
    }
    public static String readMetaData(Context context, String name){
        try {
            ApplicationInfo ai = App.instance.getPackageManager().getApplicationInfo(App.instance.getPackageName(), PackageManager.GET_META_DATA);
            if(BuildConfig.DEBUG){
                Object o = ai.metaData.get(String.format("DEBUG-%s", name));
                if(o != null) return o.toString();
            }
            Object o = ai.metaData.get(name);
            if( o == null) return null;
            return o.toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "cannot read settings: " + name, e);
            return null;
        }
    }
    public static String readAppSettings(Context context, String name){
        SharedPreferences sp = context.getSharedPreferences("WF", Context.MODE_PRIVATE);
        String s = sp.getString(name, null);
        if(s == null) s = readMetaData(context, name);
        return s;
    }
    public static void writeAppSettings(Context context, String name, String value){
        SharedPreferences sp = context.getSharedPreferences("WF", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(name, value);
        if(!edit.commit()) edit.apply();
    }
    @SuppressLint("NewApi")
    public static File uriToFile(Uri contentUri) {
        if(isExternalStorageDocument(contentUri)) {
            String docId = DocumentsContract.getDocumentId(contentUri);
            String[] split = docId.split(":");
            String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
            }
            return new File("");
        }
        else if (isDownloadsDocument(contentUri)) {
            String id = DocumentsContract.getDocumentId(contentUri);
            Uri fileUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = {
                    column
            };

            try {
                cursor = App.instance.getContentResolver().query(fileUri, projection, null, null,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return new File(cursor.getString(index));
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return new File("");
//            return getDataColumn(App.instance, fileUri, null, null);
//            if (contentUri == null) return null;
//            if (!contentUri.getScheme().equalsIgnoreCase("content"))
//                return new File(contentUri.getPath());
//            Cursor cursor = null;
//            try {
//                String[] proj = {MediaStore.Images.Media.DATA};
//                cursor = App.instance.getContentResolver().query(contentUri, proj, null, null, null);
//                cursor.moveToFirst();
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//                return new File(cursor.getString(column_index));
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
        }
        else if(isMediaDocument(contentUri))
        {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(contentUri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = App.instance.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(filePath);
        }
        else
        {
            return new File("");
        }
    }

    /**
     **
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static Bitmap getResizedBitmap(File imagePath, int targetW, int targetH) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //inJustDecodeBounds = true <-- will not load the bitmap into memory
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath(), bmOptions);
        return(bitmap);
    }
    public static boolean saveBitmap(Bitmap bmp, File f, int quality){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, out);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "cannot save bitmap", e);
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static int dpToPx(float dp){
        if(dp == 0) return 0;
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.instance.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
//            Log.e("src",src);
            URL url = new URL(API.API_BASE + src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "AppFrame " + Http.getAuthorizationToken());
            connection.setRequestProperty("Content-Type", "image/jpeg");
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public static Bitmap getBitmapFromQINIUURL(String src) {
        try {
//            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public static String toJSV(UserLocation userLocation)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{Province:");
        sb.append(userLocation.getProvince()).append(",City:");
        sb.append(userLocation.getCity()).append(",District:");
        sb.append(userLocation.getDistrict()).append(",Street:");
        sb.append(userLocation.getStreet()).append(",StreetNumber:");
        sb.append(userLocation.getStreetNumber()).append("}");
        return sb.toString();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

//    public static List<ActivityManager.RunningTaskInfo> getActivities(Context ctx) {
//        ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(10);
//        return list;
//    }
//
//    public static boolean hasOrderSendActivity(Context ctx)
//    {
//        for(ActivityManager.RunningTaskInfo info : getActivities(ctx))
//        {
//
//            if(info.baseActivity.getClassName().equals("appframe.appframe.activity.OrderSendActivity"))
//            {
//                return true;
//            }
//        }
//        return false;
//    }

}
