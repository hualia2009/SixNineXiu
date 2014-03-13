package com.ninexiu.sixninexiu.fragment;

import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.Father;
import com.ninexiu.beans.Host;
import com.ninexiu.beans.User;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.ChatRoomActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.Utils;

/**
 * 我看过的主播列表
 * 
 * @author mao
 * 
 */
public class TraceHostSeeFragment extends BaseFragment {
	private PullListView listView;
	private ArrayList<Father> datas = new ArrayList<Father>();
	private ArrayList<Host> hostAtts = new ArrayList<Host>();
	// private FinalBitmap bitmapLoad;
	private AsyncImageLoader asyncImageLoader;
	private User mUser;
	private TraceAdapter adapter;
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews(getView());
	}

	@Override
	public int getTopTitle() {
		return R.string.looked_anchor;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.trace_layout, null);
		return view;
	}

	private void initViews(View view) {
		listView = (PullListView) view.findViewById(R.id.lv_trace);
		View emptyView = getLayoutInflater().inflate(R.layout.layout_empty_view, null);
		TextView tvIndicator = (TextView) emptyView.findViewById(R.id.tv_empty);
		tvIndicator.setText("观看记录为空");
		listView.setEmptyView(emptyView);
		mUser = ApplicationEx.get().getUserManager().getUser();
		// bitmapLoad=Utils.getBitmapLoad(this,false);
		asyncImageLoader = new AsyncImageLoader();
		if (mUser != null) {
			getHostAtt();
		}
		getLocalAttHost();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (parent.getAdapter() != null) {
					Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
					intent.putExtra("host", (Father) parent.getAdapter().getItem(position));
					startActivity(intent);
				}
			}
		});
	}

	private void getLocalAttHost() {
		try {
			ObjectInputStream objHost = new ObjectInputStream(getActivity().openFileInput(
					AppConstants.HOST_HAS_SEEN));
			datas = (ArrayList<Father>) objHost.readObject();
			adapter = new TraceAdapter();
			listView.setAdapter(adapter);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void getHostAtt() {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		asyncHttpClient.post(AppConstants.GET_ALL_ATT_LIST, params, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (content != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success = jsonObject.optBoolean("success");
						if (success == true) {
							JSONObject object = jsonObject.optJSONObject("retval");
							JSONArray array = object.optJSONArray("myfollowers");
							hostAtts.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject2 = array.getJSONObject(i);
								Host host = new Host();
								host.setUid(jsonObject2.optString("followuid")); // 主播自己的ID
								host.setHostAttId(jsonObject2.optString("id"));
								host.setAvatar(jsonObject2.optString("avatar"));
								host.setNickName(jsonObject2.optString("nickname"));
								host.setUserNum(jsonObject2.optString("usernum"));
								host.setWeath(jsonObject2.optString("wealth"));
								host.setRoomTag(jsonObject2.optString("room_ext1"));
								host.setRoomId(jsonObject2.optString("rid"));
								hostAtts.add(host);
							}
							adapter.notifyDataSetChanged();
						} else {
							String msg = jsonObject.optString("msg");
							Utils.MakeToast(getActivity(), msg);
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

	class TraceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Father getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final HostAttHolder holder;
			final Father host = datas.get(position);
			if (convertView == null) {
				holder = new HostAttHolder();
				convertView = getLayoutInflater().inflate(R.layout.host_att_list_item_two, null);
				holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_att_head);
				holder.ivLevel = (ImageView) convertView.findViewById(R.id.iv_host_level);
				holder.tvName = (TextView) convertView.findViewById(R.id.tv_nickname);
				holder.ivWealth = (ImageView) convertView.findViewById(R.id.iv_userlever);
				// holder.tvNum=(TextView)
				// convertView.findViewById(R.id.tv_liang);
				holder.btCancle = (ImageView) convertView.findViewById(R.id.iv_cancel);
				convertView.setTag(holder);
			} else {
				holder = (HostAttHolder) convertView.getTag();
			}
			// bitmapLoad.display(holder.ivHead, host.getAvatar());
			Bitmap map = asyncImageLoader.loadBitmap(host.getAvatar(),
					new AsyncImageLoader.ImageCallback() {
						@Override
						public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
							holder.ivHead.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
						}
					});
			if (map == null) {
				holder.ivHead.setImageBitmap(Utils.toRoundBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.loading_image)));
			} else {
				holder.ivHead.setImageBitmap(Utils.toRoundBitmap(map));
			}
			Utils.setHostLevel(host.getCredit(), holder.ivLevel);
			holder.tvName.setText(host.getNickName());
			if (Utils.isNotEmptyString(host.getWeath())) {
				Utils.setUserLevel(host.getWeath(), holder.ivWealth);
			}
			// holder.tvNum.setText(host.getUserNum());
			final boolean hasAtt = hasAtten(host);
			if (hasAtt) {
				holder.btCancle.setImageResource(R.drawable.host_att_tag);
			} else {
				holder.btCancle.setImageResource(R.drawable.host_att_plus);
			}
			holder.btCancle.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (hasAtt) {
						doUnAttTask(host);
					} else {
						doAttTask(host);
					}
				}
			});
			return convertView;
		}

		public class HostAttHolder {
			public ImageView ivHead;
			public ImageView ivLevel;
			public ImageView ivWealth;
			public TextView tvName;
			public TextView tvNum;
			public ImageView btCancle;
		}

	}

	public boolean hasAtten(Father host) {
		for (int i = 0; i < hostAtts.size(); i++) {
			Host host2 = hostAtts.get(i);
			if (host2.getUid().equals(host.getUid())) {
				return true;
			}
		}
		return false;
	}

	public void doUnAttTask(final Father host) {
		if (mUser != null) {
			String cancelId = "";
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("uid", mUser.getUid());
			params.put("token", mUser.getToken());
			for (int i = 0; i < hostAtts.size(); i++) {
				Host hosts = hostAtts.get(i);
				if (hosts.getUid().equals(host.getUid())) {
					cancelId = hosts.getHostAttId();
					break;
				}
			}
			params.put("cancleId", cancelId);
			asyncHttpClient.post(AppConstants.PAY_HOST_UNATTE, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							super.onStart();
							mDialog = Utils.showProgressDialog(getActivity(), "操作中……");
							mDialog.show();
						}

						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							mDialog.dismiss();
							if (content != null) {
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(content);
									boolean success = jsonObject.getBoolean("success");
									if (success == true) {
										Utils.MakeToast(getActivity(), "取消成功");
										getHostAtt();
									} else {
										String message = jsonObject.optString("msg");
										Utils.MakeToast(getActivity(), message);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							mDialog.dismiss();
							Utils.MakeToast(getActivity(), "网络连接超时");
						}
					});
		} else {
			Utils.MakeToast(getActivity(), "请先登录");
		}

	}

	/*
	 * 添加关注
	 */
	protected void doAttTask(final Father host) {
		if (mUser != null) {
			AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("uid", mUser.getUid());
			params.put("token", mUser.getToken());
			params.put("follwerId", host.getUid());
			asyncHttpClient.post(AppConstants.PAY_HOST_ATTE, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							super.onStart();
							mDialog = Utils.showProgressDialog(getActivity(), "操作中……");
							mDialog.show();
						}

						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							mDialog.dismiss();
							if (content != null) {
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(content);
									boolean success = jsonObject.getBoolean("success");
									if (success == true) {
										Utils.MakeToast(getActivity(), "关注成功");
										getHostAtt();
									} else {
										String message = jsonObject.optString("msg");
										Utils.MakeToast(getActivity(), message);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							mDialog.dismiss();
							Utils.MakeToast(getActivity(), "网络连接超时");

						}
					});
		} else {
			Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
			intent.putExtra("host", host);
			startActivity(intent);
		}
	}

}
