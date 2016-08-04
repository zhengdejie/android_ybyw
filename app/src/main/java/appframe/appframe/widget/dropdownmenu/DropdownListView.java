package appframe.appframe.widget.dropdownmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import appframe.appframe.R;

/**
 * Created by Administrator on 2015/5/28.
 */
public class DropdownListView extends ScrollView {
    public LinearLayout linearLayout;

    public DropdownItemObject current;

    public List<DropdownItemObject> currentMulti = new ArrayList<DropdownItemObject>();

    List<? extends DropdownItemObject> list;

    public DropdownButton button;

    public DropdownListView(Context context) {
        this(context, null);
    }

    public DropdownListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropdownListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.dropdown_tab_list, this,true);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
    }


    public void flush() {
        for (int i = 0, n = linearLayout.getChildCount(); i < n; i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof DropdownListItemView) {
                DropdownListItemView itemView = (DropdownListItemView) view;
                DropdownItemObject data = (DropdownItemObject) itemView.getTag();
                if (data == null) {
                    return;
                }
                boolean checked = data == current;
                String suffix = data.getSuffix();
                itemView.bind(TextUtils.isEmpty(suffix) ? data.text : data.text + suffix, checked);
                if (checked) button.setText(data.text);
            }

        }
    }

    public void flushMulti() {
        for (int i = 0, n = linearLayout.getChildCount(); i < n; i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof DropdownListItemView) {
                DropdownListItemView itemView = (DropdownListItemView) view;
                DropdownItemObject data = (DropdownItemObject) itemView.getTag();
                if (data == null) {
                    return;
                }
                boolean checked = false;
                for(DropdownItemObject ddio : currentMulti)
                {
                    if( data == ddio)
                    {
                        checked = true;
                    }
                }
                //boolean checked = data == current;
                String suffix = data.getSuffix();
                itemView.bind(TextUtils.isEmpty(suffix) ? data.text : data.text + suffix, checked);
                button.setText("筛选");
            }

        }
    }

    public void bind(List<? extends DropdownItemObject> list,
                     DropdownButton button,
                     final Container container,
                     int selectedId, final int multiSelect
    ) {
        current = null;
        this.list = list;
        this.button = button;
        LinkedList<View> cachedDividers = new LinkedList<>();
        LinkedList<DropdownListItemView> cachedViews = new LinkedList<>();
        for (int i = 0, n = linearLayout.getChildCount(); i < n; i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof DropdownListItemView) {
                cachedViews.add((DropdownListItemView) view);
            } else {
                cachedDividers.add(view);
            }
        }

        linearLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        boolean isFirst = true;
        for (DropdownItemObject item : list) {
            if (isFirst) {
                isFirst = false;
            } else {
                View divider = cachedDividers.poll();
                if (divider == null) {
                    divider = inflater.inflate(R.layout.dropdown_tab_list_divider, linearLayout, false);
                }
                linearLayout.addView(divider);
            }
            DropdownListItemView view = cachedViews.poll();
            if (view == null) {
                view = (DropdownListItemView) inflater.inflate(R.layout.dropdown_tab_list_item, linearLayout, false);
            }
            view.setTag(item);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DropdownItemObject data = (DropdownItemObject) v.getTag();
                    if (data == null) return;
                    if (multiSelect == 0) {
                        DropdownItemObject oldOne = current;
                        current = data;
                        flush();
                        container.hide();
                        if (oldOne != current) {
                            container.onSelectionChanged(DropdownListView.this);
                        }
                    } else {
                        if (currentMulti.size() == 0) {
                            currentMulti.add(data);
                        } else {
                            DropdownItemObject tempMulti = new DropdownItemObject();

                            for (DropdownItemObject ddto : currentMulti) {
                                if (ddto.id == data.id) {
                                    tempMulti = ddto;
                                }
                            }
                            if(tempMulti.id == 0)
                            {
                                currentMulti.add(data);
                            }
                            else
                            {
                                currentMulti.remove(tempMulti);
                            }
                        }
                        flushMulti();
                    }
                }
            });
            linearLayout.addView(view);
            if (item.id == selectedId && current == null) {
                current = item;
            }

        }
        if (multiSelect == 1) {
            View divider = cachedDividers.poll();
            if (divider == null) {
                divider = inflater.inflate(R.layout.dropdown_tab_list_divider, linearLayout, false);
            }
            linearLayout.addView(divider);

            TextView btn_ok = new TextView(getContext());
            //此处相当于布局文件中的Android:layout_gravity属性
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200,100);
            lp.gravity = Gravity.RIGHT;
            lp.setMargins(10, 10, 10, 10);
            btn_ok.setLayoutParams(lp);

            //此处相当于布局文件中的Android：gravity属性
            btn_ok.setGravity(Gravity.CENTER);
//            Drawable drawable = getResources().getDrawable(R.drawable.shape_tagview);
            btn_ok.setBackgroundResource(R.drawable.shape_tagview);


            btn_ok.setText("确定");

            btn_ok.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_clicked));
            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    container.hide();
                    container.onSelectionChanged(DropdownListView.this);
                }
            });
            linearLayout.addView(btn_ok);
        }
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVisibility() == VISIBLE) {
                    container.hide();
                } else {
                    container.show(DropdownListView.this);
                }
            }
        });

        if (current == null && list.size() > 0) {
            current = list.get(0);
        }
        if (multiSelect == 0) {
            flush();
        }
        else
        {
            flushMulti();
        }
    }


    public static interface Container {
        void show(DropdownListView listView);

        void hide();

        void onSelectionChanged(DropdownListView view);
    }


}
