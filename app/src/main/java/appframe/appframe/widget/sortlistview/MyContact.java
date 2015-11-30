package appframe.appframe.widget.sortlistview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import appframe.appframe.R;
import appframe.appframe.app.API;
import appframe.appframe.dto.ContactDetail;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;

/**
 * Created by Administrator on 2015/11/17.
 */
public class MyContact extends Activity {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog,tv_back,tv_action,tv_require,tv_recommand;
    private ContactAdapter adapter;
    private ClearEditText mClearEditText;

    /**
     * ����ת����ƴ������
     */
    private CharacterParser characterParser;
    private List<ContactDetail> SourceDateList;
    private List<ContactDetail> MyContactList;

    /**
     * ����ƴ��������ListView�����������
     */
    private PinyinComparator_ContactDetail pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortlistview);
        initViews();
    }

    private void initViews() {

        //tb_title = (TextView)findViewById(R.id.tb_title);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_action = (TextView)findViewById(R.id.tv_action);
        tv_require = (TextView)findViewById(R.id.tv_require);
        tv_recommand  = (TextView)findViewById(R.id.tv_recommand);
        tv_require.setText("已注册");
        tv_recommand.setText("未注册");
        tv_back.setText("个人中心");
        Drawable img = getResources().getDrawable(R.drawable.ic_ab_back_holo_light_am);
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        tv_back.setCompoundDrawables(img, null, null, null);
        //tb_title.setText("我的好友");
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_action.setVisibility(View.GONE);
        //ʵ��������תƴ����
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator_ContactDetail();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //�����Ҳഥ������
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //����ĸ�״γ��ֵ�λ��
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
//		sortListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//									int position, long id) {
//				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
//				Toast.makeText(getApplication(), ((SortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
//			}
//		});

        tv_require.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_require.setBackgroundColor(getResources().getColor(R.color.green));
                tv_recommand.setBackgroundColor(Color.WHITE);

                Http.request(MyContact.this, API.GET_YBFRIEND, new Http.RequestListener<List<ContactDetail>>() {
                    @Override
                    public void onSuccess(final List<ContactDetail> result) {
                        super.onSuccess(result);

                        SourceDateList = filledData(result);
                        Collections.sort(SourceDateList, pinyinComparator);
                        adapter = new ContactAdapter(MyContact.this, SourceDateList);
                        sortListView.setAdapter(adapter);
                        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                //����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
                                //ContactDetail orderDetails = (ContactDetail)parent.getAdapter().getItem(position);
                                //Toast.makeText(MyContact.this,(((ContactDetail) parent.getAdapter().getItem(position)).getType()), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                //http
            }
        });

        tv_recommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_require.setBackgroundColor(Color.WHITE);
                tv_recommand.setBackgroundColor(getResources().getColor(R.color.green));
                Http.request(MyContact.this, API.GET_MOBILEFRIEND, new Http.RequestListener<List<ContactDetail>>() {
                    @Override
                    public void onSuccess(final List<ContactDetail> result) {
                        super.onSuccess(result);

                        MyContactList = filledData_MobileName(result);
                        Collections.sort(MyContactList, pinyinComparator);
                        adapter = new ContactAdapter(MyContact.this, MyContactList);
                        sortListView.setAdapter(adapter);
                        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                //����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
                                //ContactDetail orderDetails = (ContactDetail)parent.getAdapter().getItem(position);
                                //Toast.makeText(MyContact.this,(((ContactDetail) parent.getAdapter().getItem(position)).getType()), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                //http
            }
        });

        tv_require.performClick();

        //SourceDateList = filledData(getResources().getStringArray(R.array.date));

        // ����a-z��������Դ����
//		Collections.sort(SourceDateList, pinyinComparator);
//		adapter = new SortAdapter(this, SourceDateList);
//		sortListView.setAdapter(adapter);


        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //�������������ֵ�ĸı�����������
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
                ColorDrawable colorDrawable= (ColorDrawable) tv_require.getBackground();//获取背景颜色
                if(colorDrawable.getColor() == Color.WHITE)
                {
                    filterData(s.toString(),"noregis");

                }
                else
                {
                    filterData(s.toString(),"isregis");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * ΪListView�������
     * @param date
     * @return
     */
    private List<ContactDetail> filledData(List<ContactDetail> date){
        List<ContactDetail> mSortList = new ArrayList<ContactDetail>();

        for(int i=0; i<date.size(); i++){
            ContactDetail sortModel = new ContactDetail();
            sortModel.setMobileContact(date.get(i).getMobileContact());
            sortModel.setUser(date.get(i).getUser());
            sortModel.setType(date.get(i).getType());
            //����ת����ƴ��
            String pinyin = characterParser.getSelling(date.get(i).getUser() != null ? date.get(i).getUser().getName().toString() : "#");
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    private List<ContactDetail> filledData_MobileName(List<ContactDetail> date){
        List<ContactDetail> mSortList = new ArrayList<ContactDetail>();

        for(int i=0; i<date.size(); i++){
            ContactDetail sortModel = new ContactDetail();
            sortModel.setMobileContact(date.get(i).getMobileContact());
            sortModel.setUser(date.get(i).getUser());
            sortModel.setType(date.get(i).getType());
            //����ת����ƴ��
            String pinyin = characterParser.getSelling(date.get(i).getMobileContact() != null ? date.get(i).getMobileContact().getName().toString() : "#");
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * ����������е�ֵ���������ݲ�����ListView
     * @param filterStr
     */
    private void filterData(String filterStr,String type){
        List<ContactDetail> filterDateList = new ArrayList<ContactDetail>();

        if(type.equals("noregis")) {

            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = MyContactList;
            } else {
                filterDateList.clear();
                for (ContactDetail sortModel : MyContactList) {
                    String name = sortModel.getMobileContact().getName();
                    if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
            }
        }
        else
        {
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = SourceDateList;
            } else {
                filterDateList.clear();
                for (ContactDetail sortModel : SourceDateList) {
                    String name = sortModel.getUser().getName();
                    if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
            }
        }

        // ����a-z��������
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}
