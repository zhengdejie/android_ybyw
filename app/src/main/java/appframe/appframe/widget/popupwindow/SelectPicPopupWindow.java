package appframe.appframe.widget.popupwindow;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import java.io.File;

import appframe.appframe.R;


/**
 * Created by Administrator on 2015/8/18.
 */
public class SelectPicPopupWindow extends PopupWindow {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private View mMenuView;
    private int PHOTO_GRAPH = 10;

    public SelectPicPopupWindow(final Activity context, final int img_SELECT_PHOTO)
    {
        super(context);
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_image,null);
        btn_take_photo = (Button)mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button)mMenuView.findViewById(R.id.btn_pick_photo);
        btn_cancel = (Button)mMenuView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        btn_pick_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                context.startActivityForResult(photoPickerIntent, img_SELECT_PHOTO);
            }
        });
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "temp.jpg")));
                context.startActivityForResult(intent, PHOTO_GRAPH+img_SELECT_PHOTO);
            }
        });
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        //this,setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
