package com.ninexiu.sixninexiu.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.ninexiu.beans.Host;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.ChatRoomActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.ShakeListener;
import com.ninexiu.utils.ThumbnailUtils;
import com.ninexiu.utils.Utils;

/**
 * 摇一摇界面
 * 
 * @author mao
 * 
 */
public class ShakingFragment extends BaseFragment implements OnClickListener {
	private Vibrator mVibrator;
	private ShakeListener mShakeListener;
	private ImageView ivShake;
	private AsyncImageLoader asyncImageLoader;
	private int count = 0;
	OnekeyShare oks;
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 101) {
				ImageView[] icons = new ImageView[] { host_one, host_two, host_three };
				for (int i = 0; i < shakHost.size(); i++) {
					setAnchorImage(shakHost.get(i).getAvatar(), icons[i]);
				}
			}
			if (msg.what == 100) {
				if (oks != null && !oks.isFinish()) {
					return;
				} else {
					oks = new OnekeyShare();
					oks.setImagePath((String) msg.obj);
					oks.setNotification(R.drawable.logo, "分享中...");
					oks.setTitle("九秀美女直播");
					oks.setTitleUrl("www.9xiu.com");
					oks.setText("快来#九秀美女视频直播#和美女主播一起K歌互动,大家一起认识一下吧!");
					oks.setSilent(true);
					oks.show(getActivity());
				}
			}
		}
	};

	private void setAnchorImage(String url, ImageView icon) {
		ThumbnailUtils.loadImage(url, mDefaultImage, ApplicationEx.get().getBitmapLoader(), icon,
				100, 100, 0, true);
	}

	// private SoundPool soundPool;
	private ArrayList<Host> shakHost = new ArrayList<Host>();
	private View shakeDesciption;
	private View shakReusultView;
	private JSONObject jsonImpressObject;
	private PullListView listView;
	private File target;
	private ImageView host_one, host_two, host_three;
	private Bitmap mDefaultImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mDefaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.loading_image);
		initViews(getView());
	}

	@Override
	public int getTopTitle() {
		return R.string.shark_it_off;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shaking_layout, null);
		return view;
	}

	private void initViews(View view) {
		view.findViewById(R.id.tv_shake_not_like).setOnClickListener(mOnClickListener);
		asyncImageLoader = new AsyncImageLoader();
		host_one = (ImageView) view.findViewById(R.id.shak_host_one);
		host_two = (ImageView) view.findViewById(R.id.shak_host_two);
		host_three = (ImageView) view.findViewById(R.id.shake_host_three);
		host_one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shakHost.size() > 0) {
					Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
					intent.putExtra("host", shakHost.get(0));
					startActivity(intent);
				}
			}
		});
		shakeDesciption = view.findViewById(R.id.layout_shake_descrption);
		shakReusultView = view.findViewById(R.id.layout_shake_host);
		ivShake = (ImageView) view.findViewById(R.id.iv_shake);
		mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		// soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		// soundId=soundPool.load(this, R.raw.shaking_music, 1);
		mShakeListener = new ShakeListener(getActivity());
		getImPressObject();
		mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				mShakeListener.stop();
				startVibrato(); // 开始 震动
				// soundPool.play(soundId, 0.5f, 0.5f, 0, 0, 1);
				ivShake.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake_x));
				// 注释摇一摇功能
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						getHost();
						mVibrator.cancel();
						mShakeListener.start();
						ivShake.clearAnimation();
					}
				}, 2000);
			}
		});
	}

	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_shake_not_like:
				shakReusultView.setDrawingCacheEnabled(true);
				final Bitmap screenMap = shakReusultView.getDrawingCache();
				if (screenMap != null) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							File dir = Utils.getGiftDir();
							FileOutputStream fileOutputStream = null;
							if (dir != null) {
								target = new File(dir, System.currentTimeMillis() + ".png");
								try {
									if (!target.exists()) {
										target.createNewFile();
									}
									fileOutputStream = new FileOutputStream(target);
									screenMap
											.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
									mHandler.sendMessage(mHandler.obtainMessage(100,
											target.getAbsolutePath()));
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (fileOutputStream != null) {
										try {
											fileOutputStream.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					}).start();
				}
				break;

			default:
				break;
			}
		}
	};
	
	public void onClick(View view) {
		
	}

	@Override
	public void onPause() {
		super.onPause();
		mShakeListener.stop();
	}

	@Override
	public void onResume() {
		super.onResume();
		mShakeListener.start();
	}

	/**
	 * 手机震动的方法
	 */
	public void startVibrato() { // 定义震动
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // 第一个｛｝里面是节奏数组，
																	// 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}

	private void getImPressObject() {
		final File targetFile = Utils.getLevelJsonFile(getActivity(), "impress.json");
		if (targetFile != null) {
			if (targetFile.length() > 0) {
				try {
					FileInputStream in = new FileInputStream(targetFile);
					String jsonString = Utils.getLineString(in);
					jsonImpressObject = new JSONObject(jsonString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ((System.currentTimeMillis() - targetFile.lastModified() > 7 * 24 * 60 * 60
						* 1000)) {
					downloadJsonFile(targetFile, AppConstants.GET_IMPRESS_FILE);
				}
			} else {
				downloadJsonFile(targetFile, AppConstants.GET_IMPRESS_FILE);
			}

		}
	}

	public void downloadJsonFile(final File targetFile, final String Url) {
		// 开启线程下载判断用户等级文件
		new Thread(new Runnable() {
			FileOutputStream fileOutputStream;
			InputStream inputStream;

			@Override
			public void run() {
				try {
					URL url = new URL(Url);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(7 * 10000);
					fileOutputStream = new FileOutputStream(targetFile);
					if (connection.getResponseCode() == 200) {
						inputStream = connection.getInputStream();
						byte[] buff = new byte[1024];
						int length = 0;
						while ((length = inputStream.read(buff)) != -1) {
							fileOutputStream.write(buff, 0, length);
						}

						if (Url.indexOf("impress.json") != -1) {
							FileInputStream in = new FileInputStream(targetFile);
							String jsonString = Utils.getLineString(in);
							jsonImpressObject = new JSONObject(jsonString);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fileOutputStream != null) {
						try {
							fileOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();

	}

	/**
	 * 随机获取摇一摇主播列表
	 */
	private void getHost() {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.post(AppConstants.MOBILE_SHAKE, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (content != null) {
					try {
						JSONObject jsonObject = new JSONObject(content);
						boolean success = jsonObject.optBoolean("success");
						if (success == true) {
							JSONArray array = jsonObject.optJSONArray("retval");
							shakHost.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject2 = array.optJSONObject(i);

								// NLog.e("test",
								// "shakHost=="+jsonObject2.optString("avatar").toString());
								Host host = new Host();
								host.setNickName(jsonObject2.optString("nickname"));
								host.setRoomId(jsonObject2.optString("rid"));
								host.setAudice(jsonObject2.optString("roomcount"));
								host.setRoomTag(jsonObject2.optString("room_ext1"));
								host.setIsPlay(jsonObject2.optString("openstatic"));
								host.setImpress(jsonObject2.optString("impress"));
								host.setHostImage(jsonObject2.optString("mobilepic"));
								host.setUid(jsonObject2.optString("uid"));
								host.setCredit(jsonObject2.optString("credit"));
								host.setWeath(jsonObject2.optString("wealth"));
								host.setUserType(jsonObject2.optString("usertype"));
								host.setUserNum(jsonObject2.optString("usernum"));
								host.setDetail(jsonObject2.toString());
								host.setAvatar(jsonObject2.optString("avatar"));
								shakHost.add(host);
							}
							if (shakHost.size() > 0) {
								// for (int i = 0; i < shakHost.size(); i++) {
								// NLog.e("test",
								// "shakHost=="+shakHost.get(i).getAvatar().toString());
								// }

								shakeDesciption.setVisibility(View.GONE);
								shakReusultView.setVisibility(View.VISIBLE);
								mHandler.sendEmptyMessage(101);

								// ShakeListAdapter adapter=new
								// ShakeListAdapter(ShakingActivity.this,
								// shakHost, jsonImpressObject);
								// listView.setAdapter(adapter);
							}
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
				Utils.MakeToast(getActivity(), "网络连接超时");
			}

		});

	}

}
