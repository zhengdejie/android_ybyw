package appframe.appframe.widget.sortlistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
 * Created by Administrator on 2015/11/25.
 */
public class FirstClassFriends extends Activity {
    private ListView sortListView;
    private TextView tb_title,tb_back,btn_recommand;
    private FirstClassAdapter adapter;
    private ClearEditText mClearEditText;
    private OrderDetails orderDetails;
    List<ContactDetail> contactDetails = new ArrayList<ContactDetail>();
    /**
     * ����ת����ƴ������
     */
    private CharacterParser characterParser;
    private List<ContactDetail> SourceDateList;

    /**
     * ����ƴ��������ListView�����������
     */
    private PinyinComparator_ContactDetail pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sortfirstclasslistview);
        initViews();
    }

    private void initViews() {

        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        btn_recommand = (TextView)findViewById(R.id.btn_recommand);
        tb_back.setText("任务");
        tb_title.setText("推荐好友");
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = this.getIntent();
        orderDetails=(OrderDetails)intent.getSerializableExtra("OrderDetails");
        btn_recommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected()) {
                    Http.request(FirstClassFriends.this, API.RECOMMEND_ORDER, new Object[]{orderDetails.getId()}, Http.map(
                            "Friends", getFriendId()

                    ), new Http.RequestListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            super.onSuccess(result);
                            Toast.makeText(FirstClassFriends.this, "推荐成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFail(String code) {
                            super.onFail(code);
                            Toast.makeText(FirstClassFriends.this, "推荐失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(FirstClassFriends.this,"至少选择一名推荐人",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //ʵ��������תƴ����
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator_ContactDetail();



        sortListView = (ListView) findViewById(R.id.country_lvcountry);

        Http.request(FirstClassFriends.this, API.GET_YBFRIEND, new Http.RequestListener<List<ContactDetail>>() {
            @Override
            public void onSuccess(final List<ContactDetail> result) {
                super.onSuccess(result);

                SourceDateList = filledData(result);
                contactDetails = SourceDateList;
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new FirstClassAdapter(FirstClassFriends.this, SourceDateList);
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
                filterData(s.toString());
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

    private String getFriendId()
    {
        StringBuilder ReceivedID = new StringBuilder();
        for(ContactDetail contactDetail : contactDetails)
        {
            if(contactDetail.getCheck().equals("Checked"))
            {
                ReceivedID.append(",").append(contactDetail.getUser().getId());
            }
        }

        return ReceivedID.deleteCharAt(0).toString();
    }

    private boolean isSelected()
    {
        for(ContactDetail contactDetail : contactDetails)
        {
            if(contactDetail.getCheck().equals("Checked"))
            {
                return true;
            }
        }

        return false;
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
            sortModel.setCheck("UnCheck");
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

    /**
     * ����������е�ֵ���������ݲ�����ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<ContactDetail> filterDateList = new ArrayList<ContactDetail>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(ContactDetail sortModel : SourceDateList){
                String name = sortModel.getUser().getName();
                if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                    filterDateList.add(sortModel);
                }
            }
        }

        // ����a-z��������
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

}

