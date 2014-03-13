package com.ninexiu.sixninexiu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.network.tools.toolbox.VolleyLog;
import com.ninexiu.beans.AdInfo;
import com.ninexiu.beans.Host;
import com.ninexiu.customerview.CustomViewpager;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.pullrefreshview.PullToRefreshBase;
import com.ninexiu.pullrefreshview.PullToRefreshBase.Mode;
import com.ninexiu.pullrefreshview.PullToRefreshBase.OnRefreshListener2;
import com.ninexiu.pullrefreshview.PullToRefreshListView;
import com.ninexiu.sixninexiu.AdActivity;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.ChatRoomActivity;
import com.ninexiu.sixninexiu.NewTaskActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.KeyValuePair;
import com.ninexiu.utils.MoccaPreferences;
import com.ninexiu.utils.ThumbnailUtils;
import com.ninexiu.utils.Utils;

/**
 * 直播大厅界面
 */
public class ContentFragment extends BaseFragment {
	public static final String PARAMANCHORTYPE = "param_anchor_type";
	private Button mTopButtonLeft;
	private Button mTopButtonCenter;
	private Button mTopButtonRight;
	private Bitmap mDefaultImage;
	/** 主播类型 roomnum:超级主播, credit:巨星主播,jointime:闪亮新星); */
	private String[] mAuthorTypesStrings = new String[] { "roomnum", "credit", "jointime" };
	/** 用于缓存首页数据，切换tab时不需要重新获取数据，first 页码，second 数据 */
	private Map<String, KeyValuePair<Integer, List<Host>>> mAuthorMap = new HashMap<String, KeyValuePair<Integer, List<Host>>>();
	private AnthorPage mAnthorPage;
	private View mLoadingView;
	private View mRetryView;
	private LoadHostData mLoadHost;
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoadHost = new LoadHostData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.cotent_layout, container, false);
			initViews(view);
		} else {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mDefaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		mTopButtonLeft.setOnClickListener(mOnClickListener);
		mTopButtonCenter.setOnClickListener(mOnClickListener);
		mTopButtonRight.setOnClickListener(mOnClickListener);
		mRetryView.setOnClickListener(mOnClickListener);
		mTopButtonLeft.setSelected(true);
		mAnthorPage = new AnthorPage((PullToRefreshListView) getView()
				.findViewById(R.id.lv_content));
		setLoadingView(true);
		mAnthorPage.initDatas();
	}

	private void setLoadingView(boolean isLoading) {
		if (isLoading) {
			mLoadingView.setVisibility(View.VISIBLE);
		} else {
			mLoadingView.setVisibility(View.GONE);
		}
	}

	private void setRetryView(boolean isRetry) {
		if (isRetry) {
			mRetryView.setVisibility(View.VISIBLE);
		} else {
			mRetryView.setVisibility(View.GONE);
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int mCurrentTabIndex = 0;
			if (v == mRetryView) {
				setLoadingView(true);
				setRetryView(false);
				mAnthorPage.initDatas();
				return;
			}
			if (v == mTopButtonLeft) {
				mCurrentTabIndex = 0;
				if (!mTopButtonLeft.isSelected()) {
					setTopTabSelected(0);
				}
			} else if (v == mTopButtonCenter) {
				mCurrentTabIndex = 1;
				if (!mTopButtonCenter.isSelected()) {
					setTopTabSelected(1);
				}
			} else if (v == mTopButtonRight) {
				mCurrentTabIndex = 2;
				if (!mTopButtonRight.isSelected()) {
					setTopTabSelected(2);
				}
			} else if (v == mRetryView) {

			}
			mAnthorPage.selectTab(mCurrentTabIndex);
		}
	};

	private void initViews(View view) {
		mTopButtonLeft = (Button) view.findViewById(R.id.pop_anchor);
		mTopButtonCenter = (Button) view.findViewById(R.id.recommendation_anchor);
		mTopButtonRight = (Button) view.findViewById(R.id.new_anchor);
		mLoadingView = view.findViewById(R.id.l_loadingview);
		mRetryView = view.findViewById(R.id.l_retryview);
		view.findViewById(R.id.task_view).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ApplicationEx.get().getUserManager().getUser()!=null){
					Intent intent=new Intent(getActivity(),NewTaskActivity.class);
					intent.putExtra("live", getLiveHost());
					startActivity(intent);
				}else{
					Utils.showLoginDialog(getActivity(),false);
				}
			}
		});
	}

	/**
	 * 获取在线主播
	 */
	private ArrayList<Host> getLiveHost() {
		ArrayList<Host> temp = new ArrayList<Host>();
		if(mAdapter != null) {
			List<Host> list = mAdapter.getDatas();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIsPlay().equals("1")) {
					temp.add(list.get(i));
				}
			}
		}
		return temp;
	}
	
	@Override
	public void setBundle(Bundle bundle) {
		super.setBundle(bundle);
		String type = bundle.getString(PARAMANCHORTYPE);
		try {
			// 更新当前数据
			mAnthorPage.updateTab(Integer.parseInt(type));
		} catch (Exception e) {
			String authorType = bundle.getString(PARAMANCHORTYPE);
			// 切换tab
			for (int i = 0; i < mAuthorTypesStrings.length; i++) {
				if (mAuthorTypesStrings[i].equals(authorType)) {
					mAnthorPage.selectTab(i);
					setTopTabSelected(i);
					return;
				}
			}
		}
	}

	/**
	 * 切换tab状态
	 * 
	 * @param index
	 */
	private void setTopTabSelected(int index) {
		if (index == 0) {
			mTopButtonLeft.setSelected(true);
			mTopButtonCenter.setSelected(false);
			mTopButtonRight.setSelected(false);
		} else if (index == 1) {
			mTopButtonLeft.setSelected(false);
			mTopButtonCenter.setSelected(true);
			mTopButtonRight.setSelected(false);
		} else if (index == 2) {
			mTopButtonLeft.setSelected(false);
			mTopButtonCenter.setSelected(false);
			mTopButtonRight.setSelected(true);
		}
	}

	private AuthorAdapter mAdapter;
	class AnthorPage {
		/** 当前主播类型（大类型，不包含小类型） */
		String mAuthorType;
		private ListView mListView;
		private PullToRefreshListView refreshListView;
		/** 广告进度条 */
		private View[] adPlans;
		private int mCurrentTabId = 0;
		/** 广告自动播放 */
		private final int PARAMAUTOPLAY = 0;
		private final int PLAYDELAY = 5000;
		private CustomViewpager pager;
		private int mCurrentPageIndex;
		
		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == PARAMAUTOPLAY) {
					if (mCurrentPageIndex == adPlans.length - 1) {
						mCurrentPageIndex = -1;
					}
					pager.setCurrentItem(++mCurrentPageIndex);
					mHandler.sendEmptyMessageDelayed(PARAMAUTOPLAY, PLAYDELAY);
				}
			}
			
		};

		public AnthorPage(PullToRefreshListView refreshListView) {
			this.refreshListView = refreshListView;
			mListView = refreshListView.getRefreshableView();
			refreshListView.setMode(Mode.BOTH);
			mListView = refreshListView.getRefreshableView();
		}

		public void initDatas() {
			loadAds();
		}

		/**
		 * 选中的tab
		 * 
		 * @param tabId
		 *            点击的tab id
		 */
		public void selectTab(int tabId) {
			mCurrentTabId = tabId;
			mAuthorType = mAuthorTypesStrings[tabId];
//			loadFileCacheAnchors(mAuthorType);
			if (mAuthorMap.containsKey(mAuthorType)) {
				// 切换tab的时候，查看缓存中的页码
				setAnchorAdapter(mAuthorMap.get(mAuthorType).second, 1);
			} else {
				loadAnchors(mAuthorType, tabId, 1);
			}
			refreshListView.setOnRefreshListener(new OnRefresh(mAuthorType));
		}

		/**
		 * 选中的主播类型，从右边点击跳转识别
		 */
		public void selectTab(String authorType) {
			for (int i = 0; i < mAuthorTypesStrings.length; i++) {
				if (mAuthorTypesStrings[i].equals(authorType)) {
					selectTab(i);
					return;
				}
			}
			selectTab(0);
		}

		/**
		 * 更新当前页面
		 */
		public void updateTab(int rightMenuId) {
			setAnchorAdapter(parserAnchorList(String.valueOf(rightMenuId)), 1);
		}

		View headerView;
		private View getHeadAdViewByViewPager(List<AdInfo> adInfos) {
			headerView = LayoutInflater.from(getActivity()).inflate(
					R.layout.layout_home_ad_viewpager, null);
			pager = (CustomViewpager) headerView.findViewById(R.id.pager);
			pager.setmPager(pager);
			pager.setAdapter(new ImagePagerAdapter(adInfos, getActivity()));
			mHandler.sendEmptyMessageDelayed(PARAMAUTOPLAY, PLAYDELAY);
			pager.setOnPageChangeListener(mOnPageChangeListener);
			initAdPlan((LinearLayout) headerView.findViewById(R.id.plan), adInfos.size());
			return headerView;
		}

		private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				mCurrentPageIndex = arg0;
				mHandler.removeMessages(PARAMAUTOPLAY);
				mHandler.sendEmptyMessageDelayed(PARAMAUTOPLAY, PLAYDELAY);
				setAdPlanSelected(arg0);
			}
		};

		private void initAdPlan(LinearLayout linearLayout, int size) {
			linearLayout.removeAllViews();
			adPlans = new View[size];
			for (int i = 0; i < size; i++) {
				View view = getActivity().getLayoutInflater().inflate(R.layout.item_plan, null);
				adPlans[i] = view;
				linearLayout.addView(view);
			}
			setAdPlanSelected(0);
		}

		private void setAdPlanSelected(int position) {
			for (int i = 0; i < adPlans.length; i++) {
				if (i == position) {
					adPlans[i].setSelected(true);
				} else {
					adPlans[i].setSelected(false);
				}
			}
		}

		/**
		 * 通过分类过滤主播列表
		 * 
		 * @param category
		 */
		private List<Host> parserAnchorList(String category) {
			List<Host> hosts = new ArrayList<Host>();
			List<Host> temp = mAuthorMap.get(mAuthorType).second;
			for (int i = 0; i < temp.size(); i++) {
				if (temp.get(i).getImpress().indexOf(category) >= 0) {
					hosts.add(temp.get(i));
				}
			}
			return hosts;
		}

		private void loadAnchors(final String anchorType, final int tabIndex, final int pageNumber) {
			mLoadHost.loadAnchors(new ILoadHost<Pair<Boolean, List<Host>>>() {

				@Override
				public void loadHosts(Pair<Boolean, List<Host>> response) {
					if (null == getActivity()) {
						return;
					}
					refreshListView.onRefreshComplete();
					// 如果返回数据为null，则显示重试
					if (response.second == null && null != getActivity()) {
						setLoadingView(false);
						if (response.first || mAdapter == null || mAdapter.getDatas().size() == 0 ) {
							setRetryView(true);
						} else {
							setRetryView(false);
						}
						return;
					}
					// response.first 是否为文件缓存数据，如果是文件缓存数据，则不缓存到内存
					if (null != response && !response.first && response.second.size() > 0) {
						VolleyLog.e("tab=%s,pageNumber=%s,list=%s", tabIndex, pageNumber,
								response.second.size());
						cacheData(pageNumber, anchorType, response.second);
					}
					if (tabIndex != mCurrentTabId) {
						return;
					}
					if (null != getActivity() && null != response) {
						setAnchorAdapter(response.second, pageNumber);
					}
					setLoadingView(false);
					setRetryView(false);
				}
			}, anchorType, tabIndex, pageNumber);
		}

		class OnRefresh implements OnRefreshListener2<ListView> {
			String authorType;

			public OnRefresh(String authorType) {
				super();
				this.authorType = authorType;
			}

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadAds();
				loadAnchors(authorType, mCurrentTabId, 1);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadAds();
				// 如果当前是缓存数据，则首先刷新第一页数据
				int pageSize = 0;
				if (mAuthorMap.containsKey(mAuthorType)) {
					pageSize = mAuthorMap.get(mAuthorType).first;
				}
				loadAnchors(authorType, mCurrentTabId, ++pageSize);
			}
		}

		private void loadAds() {
			mLoadHost.loadAds(new ILoadHost<List<AdInfo>>() {
				
				@Override
				public void loadHosts(List<AdInfo> t) {
					if (t != null) {
						if (null != headerView) {
							mListView.removeHeaderView(headerView);
						}
						try {
							mListView.addHeaderView(getHeadAdViewByViewPager(t));
						} catch (Exception e) {
						}
					}
					// 因为headerview要在setadapter之前设置
					selectTab(mCurrentTabId);
				}
			});
		}
		
		/**
		 * 缓存数据
		 * 
		 * @param response
		 */
		private void cacheData(int position, String mAuthorType, List<Host> response) {
			if (mAuthorMap.containsKey(mAuthorType) && position != 1) {
				// 记录当前页数
				mAuthorMap.get(mAuthorType).first++;
				// 缓存数据
				mAuthorMap.get(mAuthorType).second.addAll(response);
			} else {
				mAuthorMap.put(mAuthorType, new KeyValuePair<Integer, List<Host>>(1, response));
			}
		}

		private void setAnchorAdapter(List<Host> response, final int pageNumber) {
			if (response != null) {
				if (mAdapter == null) {
					mAdapter = new AuthorAdapter(response);
					mListView.setAdapter(mAdapter);
				} else {
					// 第一加载或者切换tab时，用resetList重新设置数据
					if (pageNumber == 1) {
						mAdapter.resetList(response);
					} else {
						mAdapter.resetList(mAuthorMap.get(mAuthorType).second);
					}
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	public final class ImagePagerAdapter extends PagerAdapter {
		private final List<AdInfo> mImages;
		private final LayoutInflater mInflater;

		public ImagePagerAdapter(List<AdInfo> images, Context context) {
			mInflater = LayoutInflater.from(context);
			mImages = images;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mImages.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = mInflater.inflate(R.layout.item_home_ad, container, false);
			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			ThumbnailUtils.loadImage(mImages.get(position).imageUrl, mDefaultImage, ApplicationEx
					.get().getBitmapLoader(), icon, 480, 220, 0, false);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AdInfo info = mImages.get(position);
					if (info.adType != null && info.adType.length() > 6) {
						Intent intent2 = new Intent(getActivity(), AdActivity.class);
						String targetUrl;
						if (info.adType.contains("&amp;")) {
							targetUrl = info.adType.replace("&amp;", "&");
						} else {
							targetUrl = info.adType;
						}
						intent2.putExtra("url", targetUrl);
						startActivity(intent2);
					} else {
						if (null != mAdapter && mAdapter.getCount() > 0) {
							Random random = new Random();
							Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
							ArrayList<Host> list = getLiveHost();
							if (list.size() > 0) {
								intent.putExtra("host", list.get(random.nextInt(list.size())));
								startActivity(intent);
							}
						}
					}
				}
			});
			container.addView(view);
			return view;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	/**
	 * 广告适配器
	 */
	class AdAdapter extends BaseAdapter {
		private List<AdInfo> list;

		public AdAdapter(List<AdInfo> list) {
			super();
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.item_home_ad, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ThumbnailUtils.loadImage(list.get(position).imageUrl, mDefaultImage, ApplicationEx
					.get().getBitmapLoader(), holder.icon, 480, 220, 0, false);
			return convertView;
		}

		class ViewHolder {
			ImageView icon;
		}

	}

	/**
	 * 直播大厅适配器
	 */
	class AuthorAdapter extends BaseAdapter {
		private List<Host> hostDatas = new ArrayList<Host>();

		public AuthorAdapter(List<Host> hostDatas) {
			super();
			this.hostDatas = hostDatas;
		}

		public void resetList(List<Host> hostDatas) {
			this.hostDatas = hostDatas;
		}

		public List<Host> getDatas() {
			return hostDatas;
		}

		@Override
		public int getCount() {
			if (hostDatas.size() % 2 == 0) {
				return hostDatas.size() / 2;
			} else {
				return hostDatas.size() / 2 + 1;
			}
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ItemHolder holder;
			if (view == null) {
				holder = new ItemHolder();
				view = getActivity().getLayoutInflater().inflate(R.layout.item_direct, null);
				initViews(holder.leftHolder, view.findViewById(R.id.item_left),
						getHost(position, true));
				initViews(holder.rightHolder, view.findViewById(R.id.item_right),
						getHost(position, false));
				view.setTag(holder);
			} else {
				holder = (ItemHolder) view.getTag();
			}

			if (getHost(position, false) == null) {
				view.findViewById(R.id.item_right).setVisibility(View.INVISIBLE);
			} else {
				view.findViewById(R.id.item_right).setVisibility(View.VISIBLE);
			}

			view.findViewById(R.id.item_left).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (getHost(position, true) == null) {
						return;
					}
					Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
					intent.putExtra("host", getHost(position, true));
					startActivity(intent);
				}
			});
			view.findViewById(R.id.item_right).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (getHost(position, true) == null) {
						return;
					}
					Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
					intent.putExtra("host", getHost(position, false));
					startActivity(intent);
				}
			});
			initItem(getHost(position, true), holder.leftHolder);
			initItem(getHost(position, false), holder.rightHolder);
			return view;
		}

		private void initViews(ViewHolder holder, View view, final Host host) {
			if (host == null) {
				return;
			}
			holder.room_thumb = (ImageView) view.findViewById(R.id.icon);
			holder.room_name = (TextView) view.findViewById(R.id.name);
			holder.room_mem_count = (TextView) view.findViewById(R.id.watching);
			holder.room_play_icon = (ImageView) view.findViewById(R.id.play);
		}

		private Host getHost(int position, boolean isLeftItem) {
			if (isLeftItem) {
				return hostDatas.get(2 * position);
			} else {
				if (hostDatas.size() > (2 * position + 1)) {
					return hostDatas.get(2 * position + 1);
				} else {
					return null;
				}
			}
		}

		private void initItem(Host host, ViewHolder holder) {
			if (host == null) {
				return;
			}
			holder.room_mem_count.setText(String.format(getString(R.string.watching),
					host.getAudice()));
			holder.room_name.setText(host.getNickName());
			if (host.getIsPlay().equals("1")) {
				holder.room_play_icon.setVisibility(View.VISIBLE);
			} else {
				holder.room_play_icon.setVisibility(View.GONE);
			}
			ThumbnailUtils.loadImage(host.getHostImage(), mDefaultImage, ApplicationEx.get()
					.getBitmapLoader(), holder.room_thumb, 150, 150, 0, false);
		}

		class ItemHolder {
			ViewHolder leftHolder = new ViewHolder();
			ViewHolder rightHolder = new ViewHolder();
		}

		class ViewHolder {
			public ImageView room_thumb;// 房间头像
			public TextView room_mem_count;// 房间人数
			public TextView room_name; // 房间名字
			public ImageView room_play_icon; // 表示该房间正在直播
			public View parent;
		}

	}

	interface ILoadHost<T> {
		void loadHosts(T t);
	}
	
	/*interface ILoadAds {
		void loadHosts(List<AdInfo> list);
	}*/
	
	/**
	 * 数据加载
	 */
	class LoadHostData {
		/** 是否已经加载成功 */
		private boolean adLoaded;
		/** 正在加载中 */
		private boolean adLoading = false;
		private Map<String, Boolean> isCachedLoaded = new HashMap<String, Boolean>();
		
		/**
		 * 加载本地数据
		 */
		private List<Host> loadFileCacheAnchors(final String anchorType) {
			List<Host> list = new ArrayList<Host>();
			if (anchorType.equals(mAuthorTypesStrings[0])) {
				list = mLoadHost.parserData(MoccaPreferences.POPANCHORDATA.get());
			} else if (anchorType.equals(mAuthorTypesStrings[1])) {
				list = mLoadHost.parserData(MoccaPreferences.RECOMMENDANCHORDATA.get());
			} else if (anchorType.equals(mAuthorTypesStrings[2])) {
				list = mLoadHost.parserData(MoccaPreferences.NEWANCHORDATA.get());
			}
			isCachedLoaded.put(anchorType, true);
			return list;
		}
		
		/**
		 * 加载广告数据，本机及网络数据，正常会回调两次
		 * @param iLoadAds
		 */
		public void loadAds(final ILoadHost<List<AdInfo>> iLoadAds) {
			if (adLoaded || adLoading) {
				return;
			}
			adLoading = true;
			iLoadAds.loadHosts(parserAds(MoccaPreferences.HOMEADSDATA.get()));
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			asyncHttpClient.post(AppConstants.GET_AD_URL, new AsyncHttpResponseHandler(){
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					List<AdInfo> list = parserAds(content);
					if (null != list) {
						MoccaPreferences.HOMEADSDATA.put(content);
						adLoading = false;
						adLoaded = true;
					}
					iLoadAds.loadHosts(parserAds(content));
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					iLoadAds.loadHosts(null);
				}
			});
		}
		
		private List<AdInfo> parserAds(String jsonString) {
			List<AdInfo> list = new ArrayList<AdInfo>();
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				if (jsonObject.optBoolean("success")) {
					JSONArray jaArray = jsonObject.getJSONArray("retval");
					for (int i = 0; i < jaArray.length(); i++) {
						JSONObject jo = jaArray.getJSONObject(i);
						AdInfo info = new AdInfo();
						info.imageUrl = jo.getString("imgurl");
						info.adType = jo.getString("advurl");
						list.add(info);
					}
				} else {
					list = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				list = null;
			}
			return list;
		}
		
		/**
		 * 首先加载缓存数据，然后加载网络数据，有缓存数据的时候会回调两次
		 * @param iLoadHost
		 * @param anchorType
		 * @param tabIndex
		 * @param pageNumber
		 */
		public void loadAnchors(final ILoadHost<Pair<Boolean, List<Host>>> iLoadHost, final String anchorType,
				final int tabIndex, final int pageNumber) {
			// 加载缓存数据，如果没有缓存则不回调刷新
			if (!isCachedLoaded.containsKey(anchorType)) {
				List<Host> list = loadFileCacheAnchors(anchorType);
				if (null != list) {
					iLoadHost.loadHosts(new Pair<Boolean, List<Host>>(true, list));
				}
			}
			loadAnchor(iLoadHost, anchorType, tabIndex, pageNumber);
		}
		
		private void loadAnchor(final ILoadHost<Pair<Boolean, List<Host>>> iLoadHost, final String anchorType,
				final int tabIndex, final int pageNumber) {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("type", anchorType);
			params.put("p", String.valueOf(pageNumber));
			asyncHttpClient.post(AppConstants.GET_HOST_LIST, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							saveToFile(anchorType, content);
							iLoadHost.loadHosts(new Pair<Boolean, List<Host>>(false, parserData(content)));
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							iLoadHost.loadHosts(new Pair<Boolean, List<Host>>(false, null));
						}

					});
		}

		/**
		 * 保存数据到本地
		 * @param anchorType
		 * @param result
		 */
		private void saveToFile(String anchorType, String result) {
			if (TextUtils.isEmpty(result)) {
				return;
			}
			if (anchorType.equals(mAuthorTypesStrings[0])) {
				MoccaPreferences.POPANCHORDATA.put(result);
			} else if (anchorType.equals(mAuthorTypesStrings[1])) {
				MoccaPreferences.RECOMMENDANCHORDATA.put(result);
			} else if (anchorType.equals(mAuthorTypesStrings[2])) {
				MoccaPreferences.NEWANCHORDATA.put(result);
			}
		}

		/**
		 * 解析主播列表
		 * @param jsonString
		 * @return
		 */
		private List<Host> parserData(String jsonString) {
			JSONObject jsonObject;
			List<Host> list = new ArrayList<Host>();
			try {
				jsonObject = new JSONObject(jsonString);
				boolean isSuccess = jsonObject.optBoolean("success");
				if (isSuccess == true) {
					JSONArray array = jsonObject.optJSONArray("retval");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject2 = array.optJSONObject(i);
						Host host = new Host();
						host.setNickName(jsonObject2.optString("nickname"));
						host.setRoomId(jsonObject2.optString("rid"));
						host.setAudice(jsonObject2.optString("roomcount"));
						host.setRoomTag(jsonObject2.optString("room_ext1"));
						host.setIsPlay(jsonObject2.optString("openstatic"));
						host.setImpress(jsonObject2.optString("impress"));
						host.setHostImage(jsonObject2.optString("mobilepic"));
						host.setUid(jsonObject2.optString("uid"));
						host.setWeath(jsonObject2.optString("wealth"));
						host.setCredit(jsonObject2.optString("credit"));
						host.setUserType(jsonObject2.optString("usertype"));
						host.setUserNum(jsonObject2.optString("usernum"));
						host.setDetail(jsonObject2.toString());
						host.setAvatar(jsonObject2.optString("avatar"));
						list.add(host);
					}
				} else {
					list = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				list = null;
			}
			return list;
		}
	}

}
