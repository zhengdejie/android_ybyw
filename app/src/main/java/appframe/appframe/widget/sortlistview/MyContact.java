package appframe.appframe.widget.sortlistview;

import android.app.Activity;
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
    private TextView dialog,tb_title,tb_back;
    private ContactAdapter adapter;
    private ClearEditText mClearEditText;

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
        setContentView(R.layout.activity_sortlistview);
        initViews();
    }

    private void initViews() {

        tb_title = (TextView)findViewById(R.id.tb_title);
        tb_back = (TextView)findViewById(R.id.tb_back);
        tb_back.setText("个人中心");
        tb_title.setText("我的好友");
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        Http.request(MyContact.this, API.GET_CONTACT, new Http.RequestListener<List<ContactDetail>>() {
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
//				listView.setAdapter(new SwipeRefreshXOrderAdapater(getActivity(), result));
//				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//						Intent intent = new Intent();
//						intent.setClass(getActivity(), OrderDetailsActivity.class);
//						OrderDetails orderDetails = new OrderDetails();
//						orderDetails.setTitle(result.get(position).getTitle());
//						orderDetails.setContent(result.get(position).getContent());
//						orderDetails.setCategory(result.get(position).getCategory());
//						orderDetails.setBounty(result.get(position).getBounty());
//						orderDetails.setPosition(result.get(position).getPosition());
//						Bundle bundle = new Bundle();
//						bundle.putSerializable("OrderDetails", orderDetails);
//						intent.putExtras(bundle);
//						startActivity(intent);
//					}
//				});

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
    private void filterData(String filterStr){
        List<ContactDetail> filterDateList = new ArrayList<ContactDetail>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            filterDateList.clear();
            for(ContactDetail sortModel : SourceDateList){
                String name = sortModel.getMobileContact().getName();
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
