package com.ninexiu.sixninexiu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ninexiu.adapter.GiftRankAdapter;
import com.ninexiu.adapter.RankAdapter;
import com.ninexiu.beans.Father;
import com.ninexiu.beans.Gift;
import com.ninexiu.beans.RankBean;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.FileUtil;
import com.ninexiu.utils.Utils;
/**
 * 排行榜界面
 * @author mao
 *
 */
public class RankActivity extends BaseActivity {
	private PullListView listView;
	private Dialog mDialog;
	private TextView tvTag;
	private RankBean starBean=new RankBean(); //明星TOP 10
	private RankBean RichBean=new RankBean();//富豪TOP 10
	private RankBean GiftBean=new RankBean();//礼物之星 TOP 10  type 3:
	private boolean mIsStar=true,mIsRich=false,mIsGift=false;
	private TranslateAnimation _TranslateAnimation;
	private int moveStep,mCurrentLeft;
	private View rankBottom,rankGiftBottom; 
	private ArrayList<Gift> prvious=new ArrayList<Gift>();
	private ArrayList<Gift> current=new ArrayList<Gift>();
	private TextView rankButton;
	private TextView daytext,weektext,monthtext,alltext,thistext,preText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rank_layout);
		initViews();
		initRankPopWindow();
		moveStep=getWindowManager().getDefaultDisplay().getWidth()/4;
		getRankData(0+"");
		
	}
	
	private String[] rankString= new String[]{"明星排行榜","富豪排行榜","礼物排行榜"};
	public void initRankPopWindow(){
		rankButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View contentView=getLayoutInflater().inflate(R.layout.rank_poplayout, null);
				final PopupWindow popupWindow=new PopupWindow(contentView, (int)getResources().getDimension(R.dimen.rank_pop_width),(int)getResources().getDimension(R.dimen.rank_pop_height));
				popupWindow.setOutsideTouchable(true);
				popupWindow.setTouchable(true);
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
				popupWindow.showAsDropDown(rankButton);
				ListView listView=(ListView) contentView.findViewById(R.id.rank_pop_listview);
				RankPopAdapter adapter = new RankPopAdapter(RankActivity.this, rankString);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(
							AdapterView<?> parent,
							View view, int position, long id) {
						rankButton.setText(rankString[position]);
						switch (position) {
						case 0:
							getRankData(position+"");
							mIsStar=true;
							mIsGift=false;
							mIsRich=false;
							rankBottom.setVisibility(View.VISIBLE);
							rankGiftBottom.setVisibility(View.GONE);
							daytext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_day_select));
							weektext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_week_unselect));
							monthtext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_month_unselect));
							alltext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_all_unselect));
							break;

						case 1:
							getRankData(position+"");
							mIsStar=false;
							mIsGift=false;
							mIsRich=true;
							rankBottom.setVisibility(View.VISIBLE);
							rankGiftBottom.setVisibility(View.GONE);
							daytext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_day_select));
							weektext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_week_unselect));
							monthtext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_month_unselect));
							alltext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_all_unselect));
							break;
							
						case 2:
							getGiftRankData(3+"");
							mIsStar=false;
							mIsGift=true;
							mIsRich=false;
							rankBottom.setVisibility(View.GONE);
							rankGiftBottom.setVisibility(View.VISIBLE);
							thistext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_thisterm_selet));
							preText.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_preterm_unselect));
							break;
						}
						popupWindow.dismiss();
					}
				});
			}
		});
	}
	
	private void initViews(){
		listView=(PullListView) findViewById(R.id.lv_rank);
		rankButton = (TextView) findViewById(R.id.rank_topbtn);
		rankBottom=findViewById(R.id.rank_bottom);
		rankGiftBottom=findViewById(R.id.layout_super_bottom);
		daytext = (TextView) findViewById(R.id.tv_day_rank);
		weektext = (TextView) findViewById(R.id.tv_week_rank);
		monthtext = (TextView) findViewById(R.id.tv_month_rank);
		alltext = (TextView) findViewById(R.id.tv_super_rank);
		thistext = (TextView) findViewById(R.id.tv_this_term);
		preText = (TextView) findViewById(R.id.tv_privous_term);
	}
	
	class RankPopAdapter extends BaseAdapter{
		Context context;
		String[] strings;
		public RankPopAdapter(Context context,String[] str){
			this.context = context;
			this.strings = str;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return rankString.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextHolder textHolder;
			if (convertView==null) {
				textHolder = new TextHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.rank_popitem, null);
				textHolder.textView = (TextView) convertView.findViewById(R.id.rank_poptext);
			}else {
				textHolder = (TextHolder) convertView.getTag();
			}
			textHolder.textView.setText(strings[position]);
			return convertView;
		}
		class TextHolder {
			TextView textView;
		}
	}
	
	public String restoreData(String content,String type){
		File file =new File(this.getApplication().getFilesDir(),AppConstants.SERVER_RANK_CACHE+"_"+type);
		boolean isValid = false;
		if(file.exists() && System.currentTimeMillis()-file.lastModified()<1000*60*60*24*3)
			isValid = true;
		if(file.exists() && !Utils.isNetwokAvailable(this))
			isValid = true;
		if(content==null){
			if(isValid){
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String data="";
					String line = null;
					while(( line = br.readLine())!=null){
						data+=line;
					}
					br.close();
					return data;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			return null;
		}else{
			try {
				FileOutputStream fileOutputStream=openFileOutput(AppConstants.SERVER_RANK_CACHE+"_"+type, MODE_PRIVATE);
				fileOutputStream.write(content.getBytes());
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return null;
		}
	}
	
	private void getRankData(final String type) {
		//String data = restoreData(type);
		//排行类型, 为0表示名星榜, 为1表示富豪榜, 为2表示人气榜, 默认为名星榜.
		AsyncHttpClient httpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("type", type);
		httpClient.post(AppConstants.GET_RANK_LIST, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(RankActivity.this, "数据加载中...");
				mDialog.show();
//				Bundle bundle=new Bundle();
//				bundle.putString("msg", "数据加载中...");
//				showDialog(SHOW_PROGRESS_DILAOG,bundle);
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
//				dismissDialog(SHOW_PROGRESS_DILAOG);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.getBoolean("success");
						if(success==true){
							JSONObject jsonObject2=jsonObject.getJSONObject("retval");
							JSONArray dayArray=jsonObject2.getJSONArray("day");
							
							//日榜数据
							ArrayList<Father> dayData=new ArrayList<Father>();
							for (int i = 0; i < dayArray.length(); i++) {
								JSONObject jsonObject3=dayArray.getJSONObject(i);
								dayData.add(ParseRankData(jsonObject3));
							}
							
							//周榜数据
							ArrayList<Father> weekData=new ArrayList<Father>();
							JSONArray weekArray=jsonObject2.getJSONArray("week");
							for (int i = 0; i < weekArray.length(); i++) {
								JSONObject jsonObject3=weekArray.getJSONObject(i);
								weekData.add(ParseRankData(jsonObject3));
							}
							
							//月榜数据
							ArrayList<Father> monthData=new ArrayList<Father>();
							JSONArray monthArray=jsonObject2.getJSONArray("month");
							for (int i = 0; i < monthArray.length(); i++) {
								JSONObject jsonObject3=monthArray.getJSONObject(i);
								monthData.add(ParseRankData(jsonObject3));
							}
							
							
							//超级榜数据 
							ArrayList<Father> superData=new ArrayList<Father>();
							JSONArray superArray=jsonObject2.getJSONArray("general");
							for (int i = 0; i < superArray.length(); i++) {
								JSONObject jsonObject3=superArray.getJSONObject(i);
								superData.add(ParseRankData(jsonObject3));
							}
							
							if(type.equals("0")){
								starBean.setDayRank(dayData);
								starBean.setWeekRank(weekData);
								starBean.setMonthRank(monthData);
								starBean.setSuperRank(superData);
								RankAdapter adapter=new RankAdapter(RankActivity.this, starBean.getDayRank(),true);
								listView.setAdapter(adapter);
								listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
										Intent intent=new Intent(RankActivity.this,ChatRoomActivity.class);
										intent.putExtra("host", (Father)parent.getAdapter().getItem(position));
										startActivity(intent);
									}
									
								});
							}
							
							if(type.equals("1")){
								RichBean.setDayRank(dayData);
								RichBean.setWeekRank(weekData);
								RichBean.setMonthRank(monthData);
								RichBean.setSuperRank(superData);
								RankAdapter adapter=new RankAdapter(RankActivity.this, RichBean.getDayRank(),false);
								listView.setAdapter(adapter);
								listView.setOnItemClickListener(null);
							}
							
							if(type.equals("2")){
								GiftBean.setDayRank(dayData);
								GiftBean.setWeekRank(weekData);
								GiftBean.setMonthRank(monthData);
								GiftBean.setSuperRank(superData);
								RankAdapter adapter=new RankAdapter(RankActivity.this, GiftBean.getDayRank(),false);
								listView.setAdapter(adapter);
								listView.setOnItemClickListener(null);
							}
							
						}else{
							String msg=jsonObject.getString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
//				dismissDialog(SHOW_PROGRESS_DILAOG);
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
		});
		
	}

	/**
	 * 点击事件
	 * @param view
	 */
	public void onClick(View view){
		switch (view.getId()) {
		
		case R.id.rank_back:
			finish();
			break;
		/*case R.id.tv_star_rank:
			startAnimation(moveStep*0);
			getRankData(0+"");
			mIsStar=true;
			mIsGift=false;
			mIsRich=false;
			rankBottom.setVisibility(View.VISIBLE);
			rankGiftBottom.setVisibility(View.GONE);
			changeBack(R.id.tv_star_rank);
			tvTag.setWidth(moveStep);
			((TextView)findViewById(R.id.tv_star_rank)).setTextColor(getResources().getColor(R.color.white));
			((TextView)findViewById(R.id.tv_rich_rank)).setTextColor(getResources().getColor(R.color.black));
			((TextView)findViewById(R.id.tv_gift_rank)).setTextColor(getResources().getColor(R.color.black));
			break;

		case R.id.tv_rich_rank:
			startAnimation(moveStep*0);
			getRankData(1+"");
			mIsStar=false;
			mIsGift=false;
			mIsRich=true;
			rankBottom.setVisibility(View.VISIBLE);
			rankGiftBottom.setVisibility(View.GONE);
			changeBack(R.id.tv_rich_rank);
			tvTag.setWidth(moveStep);
			((TextView)findViewById(R.id.tv_star_rank)).setTextColor(getResources().getColor(R.color.black));
			((TextView)findViewById(R.id.tv_rich_rank)).setTextColor(getResources().getColor(R.color.white));
			((TextView)findViewById(R.id.tv_gift_rank)).setTextColor(getResources().getColor(R.color.black));
			break;
			
		case R.id.tv_gift_rank:
			startAnimation(moveStep*0);
			listView.setOnItemClickListener(null);
			getGiftRankData(3+"");
			mIsStar=false;
			mIsGift=true;
			mIsRich=false;
			rankBottom.setVisibility(View.GONE);
			rankGiftBottom.setVisibility(View.VISIBLE);
			tvTag.setWidth(moveStep*2);
			changeBack(R.id.tv_gift_rank);
			((TextView)findViewById(R.id.tv_star_rank)).setTextColor(getResources().getColor(R.color.black));
			((TextView)findViewById(R.id.tv_rich_rank)).setTextColor(getResources().getColor(R.color.black));
			((TextView)findViewById(R.id.tv_gift_rank)).setTextColor(getResources().getColor(R.color.white));
			break;
			*/
		case R.id.tv_day_rank:
//			startAnimation(moveStep*0);
			daytext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_day_select));
			weektext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_week_unselect));
			monthtext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_month_unselect));
			alltext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_all_unselect));
			if(mIsGift){
				RankAdapter adapter=new RankAdapter(this, GiftBean.getDayRank(),false);
				listView.setAdapter(adapter);
			}else if(mIsStar){
				RankAdapter adapter=new RankAdapter(this, starBean.getDayRank(),true);
				listView.setAdapter(adapter);
			}else if(mIsRich){
				RankAdapter adapter=new RankAdapter(this, RichBean.getDayRank(),false);
				listView.setAdapter(adapter);
			}
			
			break;
			
		case R.id.tv_week_rank:
			daytext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_day_unselect));
			weektext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_week_select));
			monthtext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_month_unselect));
			alltext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_all_unselect));
			if(mIsGift){
				RankAdapter adapter=new RankAdapter(this, GiftBean.getWeekRank(),false);
				listView.setAdapter(adapter);
			}else if(mIsStar){
				RankAdapter adapter=new RankAdapter(this, starBean.getWeekRank(),true);
				listView.setAdapter(adapter);
			}else if(mIsRich){
				RankAdapter adapter=new RankAdapter(this, RichBean.getWeekRank(),false);
				listView.setAdapter(adapter);
			}
			break;
			
		case R.id.tv_month_rank:
			daytext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_day_unselect));
			weektext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_week_unselect));
			monthtext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_month_select));
			alltext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_all_unselect));
			if(mIsGift){
				RankAdapter adapter=new RankAdapter(this, GiftBean.getMonthRank(),false);
				listView.setAdapter(adapter);
			}else if(mIsStar){
				RankAdapter adapter=new RankAdapter(this, starBean.getMonthRank(),true);
				listView.setAdapter(adapter);
			}else if(mIsRich){
				RankAdapter adapter=new RankAdapter(this, RichBean.getMonthRank(),false);
				listView.setAdapter(adapter);
			}
			break;
		case R.id.tv_super_rank:
			daytext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_day_unselect));
			weektext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_week_unselect));
			monthtext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_month_unselect));
			alltext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_all_select));
			if(mIsGift){
				RankAdapter adapter=new RankAdapter(this, GiftBean.getSuperRank(),false);
				listView.setAdapter(adapter);
			}else if(mIsStar){
				RankAdapter adapter=new RankAdapter(this, starBean.getSuperRank(),true);
				listView.setAdapter(adapter);
			}else if(mIsRich){
				RankAdapter adapter=new RankAdapter(this, RichBean.getSuperRank(),false);
				listView.setAdapter(adapter);
			}
			break;
			
		case R.id.tv_this_term:
			thistext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_thisterm_selet));
			preText.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_preterm_unselect));
			GiftRankAdapter adapter=new GiftRankAdapter(RankActivity.this, current);
			listView.setAdapter(adapter);
			break;
			
		case R.id.tv_privous_term:
			thistext.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_thisterm_unselect));
			preText.setBackgroundDrawable(getResources().getDrawable(R.drawable.rank_preterm_select));
			try {
				GiftRankAdapter adapterPrv=new GiftRankAdapter(RankActivity.this, prvious);
				listView.setAdapter(adapterPrv);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
	
	
	/**
	 * 获取礼物之星的排名
	 * @param string
	 */
	private void getGiftRankData(String string) {
		AsyncHttpClient httpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("type", string);
		httpClient.post(AppConstants.GET_RANK_LIST, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(RankActivity.this, "数据加载中...");
				mDialog.show();
			}
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mDialog.dismiss();
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.getBoolean("success");
						if(success){
							//current
							//previous
							JSONObject jsonObject2=jsonObject.getJSONObject("retval");
							JSONArray array=jsonObject2.getJSONArray("previous");
							 prvious.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject3=array.getJSONObject(i);
								Gift gift=new Gift();
								gift.setIconUrl(jsonObject3.getString("iconUrl"));
								gift.setReceiveName(jsonObject3.getString("nickname"));
								gift.setCredit(jsonObject3.getString("credit"));
								gift.setGiftCount(jsonObject3.getString("totalnum"));
								prvious.add(gift);
								
							}
							
							JSONArray array2=jsonObject2.getJSONArray("current");
							 current.clear();
							for (int i = 0; i < array2.length(); i++) {
								JSONObject jsonObject3=array2.getJSONObject(i);
								Gift gift=new Gift();
								gift.setIconUrl(jsonObject3.getString("iconUrl"));
								gift.setReceiveName(jsonObject3.getString("nickname"));
								gift.setCredit(jsonObject3.getString("credit"));
								gift.setGiftCount(jsonObject3.getString("totalnum"));
								current.add(gift);
							}
						GiftRankAdapter adapter=new GiftRankAdapter(RankActivity.this, current);
						listView.setAdapter(adapter);
							
						}else{
							String msg=jsonObject.getString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
			
		});
		
	}

	/*
	 * 改变头部
	 */
	private void changeBack(int tvStarRank) {
		/*for (int i = 0; i < tvids.length; i++) {
			if(tvids[i]==tvStarRank){
				tvViews[i].setBackgroundResource(R.drawable.rank_top_selected);
			}else{
				tvViews[i].setBackgroundColor(Color.TRANSPARENT);
			}
		}*/
	}

	public Father ParseRankData(JSONObject jsonObject3){
		try {
			Father father=new Father();
			father.setNickName(jsonObject3.optString("nickname"));
			father.setRoomId(jsonObject3.has("rid")?jsonObject3.getString("rid"):"0");
			father.setRoomTag(jsonObject3.optString("room_ext1"));
			father.setUid(jsonObject3.optString("uid"));
			father.setUserNum(jsonObject3.optString("usernum"));
			father.setWeath(jsonObject3.optString("wealth"));
			father.setUserType(jsonObject3.has("usertype")?jsonObject3.getString("usertype"):"0");
			father.setCredit(jsonObject3.has("credit")?jsonObject3.optString("credit"):"0");
			father.setDetail(jsonObject3.toString());
			father.setIsPlay(jsonObject3.optString("openstatic"));
			father.setAvatar(jsonObject3.optString("avatar"));
			return father;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void startAnimation(int moveDistance){
		AnimationSet _AnimationSet=new AnimationSet(true);
		_TranslateAnimation = new TranslateAnimation(mCurrentLeft, moveDistance, 0f, 0f);
		_AnimationSet.addAnimation(_TranslateAnimation);
		_AnimationSet.setFillBefore(false);
		_AnimationSet.setFillAfter(true);
		_AnimationSet.setDuration(100);
		tvTag.startAnimation(_AnimationSet);
		mCurrentLeft=moveDistance;
	}

}
