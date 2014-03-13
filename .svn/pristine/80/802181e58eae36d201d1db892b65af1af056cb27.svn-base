package com.ninexiu.sixninexiu;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ninexiu.customerview.PullListView;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.SqliteHandler;
import com.umeng.analytics.MobclickAgent;

/**
 * 城市选择
 * @author mao
 *
 */
public class SelectCityActivity extends Activity {
	
	private  PullListView listViewProvice;
	private PullListView listViewCity;
	private SqliteHandler sqliteHandler;
	private ArrayList<String> mProvices,mCitys;
	private ArrayAdapter<String> cityAdapter;
	private String mSelectProvice,mSelectCity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_layout);
		listViewProvice=(PullListView) findViewById(R.id.list_provice);
		listViewCity=(PullListView) findViewById(R.id.list_city);
		sqliteHandler=new SqliteHandler(this);
		mProvices=sqliteHandler.queryProvince();
		mSelectProvice=mProvices.get(0);
		ArrayAdapter<String> ProviceAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mProvices);
		listViewProvice.setAdapter(ProviceAdapter);
		mCitys= sqliteHandler.queryCity(mProvices.get(0));
		mSelectCity=mCitys.get(0);
		cityAdapter=new ArrayAdapter<String>(SelectCityActivity.this, android.R.layout.simple_list_item_1,mCitys);
		listViewCity.setAdapter(cityAdapter);
		listViewCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectCity=mCitys.get(position);
				Intent intent = new Intent();
				intent.putExtra("provice", mSelectProvice);
				intent.putExtra("city", mSelectCity);
	    		setResult(RESULT_OK, intent);
	    		finish();
			}
		});
		listViewProvice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			mSelectProvice=mProvices.get(position);
			if(mSelectProvice.equals("北京")||mSelectProvice.equals("上海")||mSelectProvice.equals("天津")||mSelectProvice.equals("重庆")){
				mCitys= sqliteHandler.queryCity(mProvices.get(position));
			}else{
				mCitys= sqliteHandler.queryTown(mProvices.get(position));
			}
			 cityAdapter=new ArrayAdapter<String>(SelectCityActivity.this, android.R.layout.simple_list_item_1,mCitys);
			listViewCity.setAdapter(cityAdapter);
			}
		});
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteHandler.close();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			Intent intent = new Intent();
    		setResult(RESULT_CANCELED, intent);
    		finish();
		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
