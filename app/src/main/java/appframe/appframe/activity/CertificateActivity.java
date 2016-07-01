package appframe.appframe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.*;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.ImgFiles;
import appframe.appframe.dto.PhotoResponse;
import appframe.appframe.dto.Token;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.utils.ImageUtils;
import appframe.appframe.utils.UploadUtils;
import appframe.appframe.utils.Utils;

/**
 * Created by Administrator on 2016/4/1.
 */
public class CertificateActivity extends BaseActivity implements View.OnClickListener {

    private TextView tb_title,tb_back,tv_certificate,tv_progress_content;
    private ImageView iv_positive,iv_reverse,iv_head;
    private static List<ImgFiles> fileList = new ArrayList<ImgFiles>();
    File front,back,frontwithface;
    boolean updateFront = false,updateBack = false,updateFrontwithface = false;
    private  static LinearLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected  void init()
    {
        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tv_certificate = (TextView)findViewById(R.id.tv_certificate);
        iv_positive = (ImageView)findViewById(R.id.iv_positive);
        iv_reverse = (ImageView)findViewById(R.id.iv_reverse);
        iv_head = (ImageView)findViewById(R.id.iv_head);
        progress_bar = (LinearLayout)findViewById(R.id.progress_bar);
        tv_progress_content = (TextView)findViewById(R.id.tv_progress_content);
        tb_back.setText("我的");
        tb_title.setText("实名认证");
        tv_progress_content.setText("正在加载");
        tb_back.setOnClickListener(this);
        tv_certificate.setOnClickListener(this);
        iv_positive.setOnClickListener(this);
        iv_reverse.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        progress_bar.setVisibility(View.VISIBLE);
        Http.request(CertificateActivity.this, API.GET_PHOTORESPONSE, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<PhotoResponse>() {
            @Override
            public void onSuccess(PhotoResponse result) {
                super.onSuccess(result);
                if(result !=null ) {
                    if (result.getFrontUrl() != null && !result.getFrontUrl().equals("")) {
                        new frontImageDownLoad().execute(result.getFrontUrl());
//                    iv_positive.setImageBitmap(Utils.getBitmapFromURL(result.getFrontUrl()));
                    }
                    if (result.getBackUrl() != null && !result.getBackUrl().equals("")) {
//                    iv_reverse.setImageBitmap(Utils.getBitmapFromURL(result.getBackUrl()));
                        new backImageDownLoad().execute(result.getBackUrl());
                    }
                    if (result.getFrontWithFaceUrl() != null && !result.getFrontWithFaceUrl().equals("")) {
//                    iv_head.setImageBitmap(Utils.getBitmapFromURL(result.getFrontWithFaceUrl()));
                        new frontWithFaceImageDownLoad().execute(result.getFrontWithFaceUrl());
                    }
                    progress_bar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFail(String code) {
                super.onFail(code);
                progress_bar.setVisibility(View.GONE);
            }
        });
    }


    class frontImageDownLoad extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            showImageView.setImageBitmap(null);
//            showProgressBar();//显示进度条提示框

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Bitmap b = Utils.getBitmapFromURL(params[0]);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result!=null){
                iv_positive.setImageBitmap(result);
//                dismissProgressBar();
//                showImageView.setImageBitmap(result);
            }
        }



    }

    class backImageDownLoad extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            showImageView.setImageBitmap(null);
//            showProgressBar();//显示进度条提示框

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Bitmap b = Utils.getBitmapFromURL(params[0]);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result!=null){
                iv_reverse.setImageBitmap(result);
//                dismissProgressBar();
//                showImageView.setImageBitmap(result);
            }
        }



    }

    class frontWithFaceImageDownLoad extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            showImageView.setImageBitmap(null);
//            showProgressBar();//显示进度条提示框

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Bitmap b = Utils.getBitmapFromURL(params[0]);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result!=null){
                iv_head.setImageBitmap(result);
//                dismissProgressBar();
//                showImageView.setImageBitmap(result);
            }
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tb_back:
                finish();
                break;
            case R.id.tv_certificate:
                tv_progress_content.setText("正在上传");
                if(!updateFront && !updateBack && !updateFrontwithface)
                {
                    Toast.makeText(CertificateActivity.this,"请选择图片进行上传",Toast.LENGTH_SHORT).show();
                }
                else {
                    progress_bar.setVisibility(View.VISIBLE);

                    if (updateBack) {
//                        Utils.saveBitmap(((BitmapDrawable) iv_reverse.getDrawable()).getBitmap(), back, 100);
                        ImgFiles imgFiles = new ImgFiles();
                        imgFiles.setFile(back);
                        imgFiles.setName("back");
                        fileList.add(imgFiles);

                    }
                    if (updateFront) {
//                        Utils.saveBitmap(((BitmapDrawable) iv_positive.getDrawable()).getBitmap(), front, 100);
                        ImgFiles imgFiles = new ImgFiles();
                        imgFiles.setFile(front);
                        imgFiles.setName("front");
                        fileList.add(imgFiles);

                    }
                    if (updateFrontwithface) {
//                        Utils.saveBitmap(((BitmapDrawable) iv_head.getDrawable()).getBitmap(), frontwithface, 100);
                        ImgFiles imgFiles = new ImgFiles();
                        imgFiles.setFile(frontwithface);
                        imgFiles.setName("frontwithface");
                        fileList.add(imgFiles);

                    }

                    uploadImg(this, fileList);
                }
//                for (ImgFiles imgFiles : fileList)
//                {
//                    uploadImg(this,imgFiles);
//                }
//                if(fileList.size() == 0)
//                {
//                    Toast.makeText(CertificateActivity.this,"请选择图片进行上传",Toast.LENGTH_SHORT).show();
//                }
//                else {

//                    for (ImgFiles file : fileList) {
//
//
//                    }
//                }

                break;
            case R.id.iv_positive:
                new PopupWindows_Picture_Positive(this,iv_positive,"positive");
                break;
            case R.id.iv_reverse:
                new PopupWindows_Picture_Reverse(this,iv_reverse,"reverse");
                break;
            case R.id.iv_head:
                new PopupWindows_Picture_Head(this,iv_head,"head");
                break;
        }
    }

//    public static void uploadImgs(final Activity cont,File photodata) {
//        try {
//
//
//
//            RequestParams params = new RequestParams();
//            params.setUseJsonStreamer(true);
//            params.put("Files", photodata);
//            params.put("Names", "12");//传输的字符数据
//            String url = String.format(API.API_BASE + "/user/%s/verification.json",Auth.getCurrentUserId());
//
//
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.addHeader("Authorization", "AppFrame " + Http.getAuthorizationToken());
//
//            client.post(url, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    Toast.makeText(cont, "上传成功", Toast.LENGTH_SHORT).show();
//                    cont.finish();
//                }
//
//                @Override
//                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                    Toast.makeText(cont, "上传失败", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public static void uploadImg(final Activity cont,List<ImgFiles> photodata) {
        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
//            photodata.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
//            baos.close();
//            byte[] buffer = baos.toByteArray();
////            System.out.println("图片的大小："+buffer.length);
//
//            //将图片的字节流数据加密成base64字符输出
//            String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
//
//            //photo=URLEncoder.encode(photo,"UTF-8");
            StringBuilder sb = new StringBuilder();
            RequestParams params = new RequestParams();
            sb.append("[");
            for (ImgFiles file : photodata)
            {
                sb.append( "\"" + file.getName() + "\"").append(",");
                if(file.getName().equals("front"))
                {
                    params.put("front", file.getFile());
                }
                if(file.getName().equals("back"))
                {
                    params.put("back", file.getFile());
                }
                if(file.getName().equals("frontwithface"))
                {
                    params.put("frontwithface", file.getFile());
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");


            params.put("Names", sb);//传输的字符数据
            String url = String.format(API.API_BASE + "/user/%s/verification.json",Auth.getCurrentUserId());


            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", "AppFrame " + Http.getAuthorizationToken());


//            client.post(url, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    Toast.makeText(cont, "上传成功", Toast.LENGTH_SHORT).show();
//                    cont.finish();
//                }
//
//                @Override
//                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
//                    Toast.makeText(cont, "上传失败", Toast.LENGTH_SHORT).show();
//                }
//            });
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    progress_bar.setVisibility(View.GONE);
                    Toast.makeText(cont, "上传成功", Toast.LENGTH_SHORT).show();
                    cont.finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progress_bar.setVisibility(View.GONE);
                    fileList.clear();
                    Toast.makeText(cont, "上传失败", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final int TAKE_PICTURE_POSITIVE = 100;
    private static final int SELECT_PHOTO_POSITIVE = 2;
    private static final int TAKE_PICTURE_REVERSE = 300;
    private static final int SELECT_PHOTO_REVERSE = 4;
    private static final int TAKE_PICTURE_HEAD = 500;
    private static final int SELECT_PHOTO_HEAD = 6;
    private String path = "";
    public class PopupWindows_Picture_Positive extends PopupWindow
    {

        public PopupWindows_Picture_Positive(final Context mContext, View parent, final String fileName)
        {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            RelativeLayout rl_photopopup = (RelativeLayout)view.findViewById(R.id.rl_photopopup);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            rl_photopopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            Button btn_camera = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button btn_photo = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button btn_cancel = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            btn_camera.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    takePhoto(fileName);
                    dismiss();
                }
            });
            btn_photo.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent photoPickerIntent = new Intent(Intent.CATEGORY_OPENABLE);
                    photoPickerIntent.setType("image/*");
                    if (Build.VERSION.SDK_INT <19) {
                        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    }else {
                        photoPickerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    }
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO_POSITIVE);
                    dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });

        }
    }

    public void takePhoto(String fileName)
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory()
                + "/mycertification/" , fileName + ".jpg");
        if (!vFile.exists())
        {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        }
        else
        {
            if (vFile.exists())
            {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);

        if(fileName.equals("positive")) {
            startActivityForResult(openCameraIntent, TAKE_PICTURE_POSITIVE);
        }
        else if(fileName.equals("reverse"))
        {
            startActivityForResult(openCameraIntent, TAKE_PICTURE_REVERSE);
        }
        else
        {
            startActivityForResult(openCameraIntent, TAKE_PICTURE_HEAD);
        }
    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        switch (requestCode) {
            case TAKE_PICTURE_POSITIVE:
                if (resultCode == RESULT_OK) {

                    front = new File(Environment.getExternalStorageDirectory()+ "/mycertification/","positive.jpg");
                    iv_positive.setImageBitmap(Utils.getResizedBitmap(front, Utils.dpToPx(80), Utils.dpToPx(80)));
                    updateFront = true;
//                    fileList.add(f);
//                    ImgFiles imgFiles = new ImgFiles();
//                    imgFiles.setFile(f);
//                    imgFiles.setName("front");
//                    fileList.add(imgFiles);

                }
                break;
            case TAKE_PICTURE_REVERSE:
                if (resultCode == RESULT_OK) {

                    back = new File(Environment.getExternalStorageDirectory()+ "/mycertification/","reverse.jpg");
                    iv_reverse.setImageBitmap(Utils.getResizedBitmap(back, Utils.dpToPx(80), Utils.dpToPx(80)));
                    updateBack = true;
//                    fileList.add(f);
//                    ImgFiles imgFiles = new ImgFiles();
//                    imgFiles.setFile(f);
//                    imgFiles.setName("back");
//                    fileList.add(imgFiles);
                }
                break;
            case TAKE_PICTURE_HEAD:
                if (resultCode == RESULT_OK) {

                    frontwithface = new File(Environment.getExternalStorageDirectory()+ "/mycertification/","head.jpg");
                    iv_head.setImageBitmap(Utils.getResizedBitmap(frontwithface, Utils.dpToPx(80), Utils.dpToPx(80)));
                    updateFrontwithface = true;
//                    fileList.add(f);
//                    ImgFiles imgFiles = new ImgFiles();
//                    imgFiles.setFile(f);
//                    imgFiles.setName("frontwithface");
//                    fileList.add(imgFiles);
                }
                break;

            case SELECT_PHOTO_POSITIVE:
                if (resultCode == Activity.RESULT_OK) {
                    updateFront = true;
                    Uri uri = imageReturnedIntent.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        iv_positive.setImageBitmap(bitmap);
                        front = Utils.uriToFile(uri);
//                        fileList.add(Utils.uriToFile(uri));
//                        ImgFiles imgFiles = new ImgFiles();
//                        imgFiles.setFile(Utils.uriToFile(uri));
//                        imgFiles.setName("front");
//                        fileList.add(imgFiles);
                    }
                    catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }

                }
                break;
            case SELECT_PHOTO_REVERSE:
                if (resultCode == Activity.RESULT_OK) {
                    updateBack = true;
                    Uri uri = imageReturnedIntent.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        iv_reverse.setImageBitmap(bitmap);
                        back = Utils.uriToFile(uri);
//                        fileList.add(Utils.uriToFile(uri));
//                        ImgFiles imgFiles = new ImgFiles();
//                        imgFiles.setFile(Utils.uriToFile(uri));
//                        imgFiles.setName("back");
//                        fileList.add(imgFiles);
                    }
                    catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }

                }
                break;
            case SELECT_PHOTO_HEAD:
                if (resultCode == Activity.RESULT_OK) {
                    updateFrontwithface = true;
                    Uri uri = imageReturnedIntent.getData();
                    ContentResolver cr = this.getContentResolver();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        iv_head.setImageBitmap(bitmap);
                        frontwithface = Utils.uriToFile(uri);
//                        fileList.add(Utils.uriToFile(uri));
//                        ImgFiles imgFiles = new ImgFiles();
//                        imgFiles.setFile(Utils.uriToFile(uri));
//                        imgFiles.setName("frontwithface");
//                        fileList.add(imgFiles);
                    }
                    catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }

                }
                break;
        }
    }

    public class PopupWindows_Picture_Reverse extends PopupWindow
    {

        public PopupWindows_Picture_Reverse(final Context mContext, View parent, final String fileName)
        {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            RelativeLayout rl_photopopup = (RelativeLayout)view.findViewById(R.id.rl_photopopup);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            rl_photopopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            Button btn_camera = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button btn_photo = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button btn_cancel = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            btn_camera.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    takePhoto(fileName);
                    dismiss();
                }
            });
            btn_photo.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent photoPickerIntent = new Intent(Intent.CATEGORY_OPENABLE);
                    photoPickerIntent.setType("image/*");
                    if (Build.VERSION.SDK_INT <19) {
                        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    }else {
                        photoPickerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    }
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO_REVERSE);
                    dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });

        }
    }



    public class PopupWindows_Picture_Head extends PopupWindow
    {

        public PopupWindows_Picture_Head(final Context mContext, View parent, final String fileName)
        {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            RelativeLayout rl_photopopup = (RelativeLayout)view.findViewById(R.id.rl_photopopup);

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setBackgroundDrawable(new BitmapDrawable());
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            rl_photopopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            Button btn_camera = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button btn_photo = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button btn_cancel = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            btn_camera.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    takePhoto(fileName);
                    dismiss();
                }
            });
            btn_photo.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent photoPickerIntent = new Intent(Intent.CATEGORY_OPENABLE);
                    photoPickerIntent.setType("image/*");
                    if (Build.VERSION.SDK_INT <19) {
                        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    }else {
                        photoPickerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    }
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO_HEAD);
                    dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileList.clear();
    }
}

