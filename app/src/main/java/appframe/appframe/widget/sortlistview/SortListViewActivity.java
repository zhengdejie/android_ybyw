package appframe.appframe.widget.sortlistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import appframe.appframe.R;

//import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import appframe.appframe.activity.OrderDetailsActivity;
import appframe.appframe.app.API;
import appframe.appframe.dto.OrderDetails;
import appframe.appframe.dto.UserDetail;
import appframe.appframe.utils.Auth;
import appframe.appframe.utils.Http;
import appframe.appframe.widget.sortlistview.SideBar.OnTouchingLetterChangedListener;
import appframe.appframe.widget.swiperefresh.SwipeRefreshXOrderAdapater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SortListViewActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog,tb_title,tb_back;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sortexpandlistview);
		initViews();
	}

	private void initViews() {

		tb_title = (TextView)findViewById(R.id.tb_title);
		tb_back = (TextView)findViewById(R.id.tb_back);
		tb_back.setText("个人中心");
		tb_title.setText("扩展人脉");
		tb_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
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

		Http.request(SortListViewActivity.this, API.GET_SECOND, new Object[]{Auth.getCurrentUserId()}, new Http.RequestListener<List<UserDetail>>() {
			@Override
			public void onSuccess(final List<UserDetail> result) {
				super.onSuccess(result);

				SourceDateList = filledData(result);
				Collections.sort(SourceDateList, pinyinComparator);
				adapter = new SortAdapter(SortListViewActivity.this, SourceDateList);
				sortListView.setAdapter(adapter);
				sortListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
						Toast.makeText(getApplication(), ((SortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
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
	private List<SortModel> filledData(List<UserDetail> date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date.get(i).Name.toString());
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date.get(i).Name.toString());
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
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
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
