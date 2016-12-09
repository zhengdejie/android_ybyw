package appframe.appframe.widget.dropdownmenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/5/28.
 */
public class DropdownListItemView extends TextView {
    public DropdownListItemView(Context context) {
        this(context,null);
    }

    public DropdownListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DropdownListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(CharSequence text,boolean checked){
        setText(text);
        if (checked){
//            setTextColor(Color.rgb(56, 171, 228));
//            setTextColor(getResources().getColor(R.color.green));
            Drawable icon = getResources().getDrawable(R.drawable.ic_task_status_list_check);
            setCompoundDrawablesWithIntrinsicBounds(null,null,icon,null);
//            Drawable icon = getResources().getDrawable(R.drawable.shape_tagview);
//            setCompoundDrawablesWithIntrinsicBounds(null,null,null,icon);
        }else{
            setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
    }


}
