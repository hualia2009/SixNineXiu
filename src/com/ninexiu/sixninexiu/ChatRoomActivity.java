﻿package com.ninexiu.sixninexiu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.ant.liao.GifView;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.ninexiu.adapter.ChatAdapter;
import com.ninexiu.adapter.EmotionAdapter;
import com.ninexiu.adapter.FansAdapter;
import com.ninexiu.adapter.RecommandAdapter;
import com.ninexiu.adapter.SongAdapter;
import com.ninexiu.adapter.SongOrderdAdapter;
import com.ninexiu.adapter.ToChatAdapter;
import com.ninexiu.beans.ChatMessage;
import com.ninexiu.beans.DriverCar;
import com.ninexiu.beans.FansBean;
import com.ninexiu.beans.Father;
import com.ninexiu.beans.Gift;
import com.ninexiu.beans.Host;
import com.ninexiu.beans.RunTunnel;
import com.ninexiu.beans.Song;
import com.ninexiu.beans.User;
import com.ninexiu.client.ChatClient;
import com.ninexiu.client.ConstantUtil;
import com.ninexiu.customerview.MyWebView;
import com.ninexiu.customerview.PullListView;
import com.ninexiu.customerview.ScrollTextView;
import com.ninexiu.httputils.AsyncHttpClient;
import com.ninexiu.httputils.AsyncHttpResponseHandler;
import com.ninexiu.httputils.RequestParams;
import com.ninexiu.pullrefreshview.PullToRefreshBase;
import com.ninexiu.pullrefreshview.PullToRefreshBase.Mode;
import com.ninexiu.pullrefreshview.PullToRefreshListView;
import com.ninexiu.utils.AppConstants;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.FinalBitmap;
import com.ninexiu.utils.IUserDataChanged;
import com.ninexiu.utils.NLog;
import com.ninexiu.utils.PlayerViewController;
import com.ninexiu.utils.Utils;
import com.ninexiu.utils.VipSort;
import com.ninexiu.viewpagerindicator.CirclePageIndicator;
import com.umeng.analytics.MobclickAgent;

/*
 * 用户控制的接口有了.  进房间前获取下,  参数rid, uid. 
   MBGetRoomUserControl
获取用户元宝的:MBGetUserIngotData, 参数uid

 */
/*
 * 直播界面 有关聊天 送礼  
 * 
 */
public class ChatRoomActivity extends Activity {

	private static final String TAG = "ChatRoomActivity";
	private ChatClient 	client;
	private static final int MESSAGE_FAILED = 10;
	public  static  final int SERVER_CONNECTED=54;//连接服务器标识
	public static final int NORMAL_MESSAGE = 20;//正常消息标识
	protected static final int LOGIN_MESSAGE_FAIELD = 30;//登录失败消息标识
	protected static final int LOAD_MORE_USER=40;//加载更多的用户信息标识
	protected static final int DIALOG_CHOICE_SONG = 10;//选择歌曲提示框标识
	protected static final int READ_MESSAGE = 60;//读取消息标识
	protected static final int HAO_HUA_GIFT = 50;//显示豪华礼物
	protected static final int DISMISS_WEB_VIEW = 70;//移除显示豪华礼物的webview
	protected static final int SHOW_GROUP_GIFT = 80;//显示组合礼物
	protected static final int DISMISS_FLY_WINDOW = 81;//移除飞屏信息
	protected static final int SET_GIFT_GONE = 82;//设置礼物为空
	protected static final int DISMISS_YUAN_BAO = 83;//移除元宝
	protected static final int DIALOG_CHARGE = 11;//充值提示框
	protected static final int DIALOG_BUY_VIP =12;//购买vip提示框
	protected static final int SHUT_UP_MSG = 14;//禁言消息
	protected static final int CAN_EDIT_MSG = 2000;//禁言消息
	
	public static final int SERVER_ERROR = 120;//服务连接错误标识
	private PlayerViewController playerViewController;//视频播放器类
	public  String mChatServerAddress="180.97.80.108";//聊天服务器地址
//	public  String mChatServerAddress="10.0.114.235";//聊天服务器地址
	private AsyncImageLoader mGiftImageLoader;//加载图片夹在类
	private Father mHost;//主播
	private User mUser; //用户
	private ScrollTextView mScrollTextView;//跑道的view
	private int popHostInfoHeight;//
	private boolean mNeedUpdate;//软件版本更新变量
	private int money;//用户的钱
	private boolean IsOrderSong=true,IsOrderedSong;//用来表示用户是点击了可点歌曲列表还是以点歌曲列表
	private RelativeLayout ParentView;//
	private FrameLayout surfaceView; //显示选择礼物的anchor
	private FrameLayout GiftContainerTwo; //显示选择礼物的anchor
	private ArrayList<Gift> giftsHot=new ArrayList<Gift>(); //热门礼物集合 
	private ArrayList<Gift> giftsFunny=new ArrayList<Gift>(); //趣味礼物集合 
	private ArrayList<Gift> giftsVip=new ArrayList<Gift>(); //VIP礼物集合 
	private ArrayList<Gift> giftsStar=new ArrayList<Gift>(); //强星礼物集合 
	private ArrayList<Gift> giftsHaohua=new ArrayList<Gift>(); //豪华礼物集合 
	private ArrayList<Gift> giftsKunCun=new ArrayList<Gift>(); //库存礼物集合 
	private ArrayList<View> pagerViews=new ArrayList<View>();
	private ArrayList<Gift> mGroupGift=new ArrayList<Gift>(); //收到的组合礼物的集合
	private ArrayList<Gift> mHaoHuaGift=new ArrayList<Gift>();//收到的豪华礼物的集合
	private TextView[] tvGiftCageory=new TextView[7];
	CirclePageIndicator mIndicator,emotionIndicator;
	int[] tvGiftCateogryId=new int[]{R.id.tv_gift_hot,R.id.tv_gift_funny,
	                               R.id.tv_gift_vip, R.id.tv_gift_star,R.id.tv_gift_haohua,R.id.tv_gift_kuncun};
	private int[] fanId=new int[]{R.id.layout_this_fan,R.id.layout_month_fan,R.id.layout_super_fan};
	private View[] fanViews=new View[fanId.length];
	private ImageView tvPub,tvPrivate,tvAudice,tvFans;
	public  TextView tvTo;//礼物显示界面 显示对谁送礼
	private Dialog mDialog;
	private boolean mHasAtt=false;
	private KeyEvent keyEventDown=new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
	private ArrayList<Host> hostAtts=new ArrayList<Host>(); //所有用户关注过的主播列表
	private ListView mPublicListView;
	/** 私聊 */
//	private ListView mPrivateListView;
	private ArrayList<ChatMessage> publicMesg=new ArrayList<ChatMessage>(); //公聊消息
	private ArrayList<ChatMessage> privateMsg=new ArrayList<ChatMessage>();//私聊消息
	/** 公聊适配器 */
	private ChatAdapter chatMessageAdapter;
	/** 私聊适配器 */
	private ChatAdapter privateMessageAdapter;
	private UserAdapter mUserAdapter;
	private SongAdapter songAdapter;//该房间可点歌曲列表
	private SongOrderdAdapter orderdAdapter;//该房间已点歌曲列表
	private ImageView privateDot;
	private boolean mIsPrivate; //表示 用户是否点击了私聊
	private View chatView,emotionView,bottomMenuView;
	private ArrayList<Father> mAllUserData=new ArrayList<Father>();//所有用户的信息
	private ArrayList<Father> mUserDatas=new ArrayList<Father>();//真是用户的信息
	private ArrayList<Father> mManagerUser=new ArrayList<Father>(); //管理员用户列表 ,在踢人有用
	private ArrayList<Song> mSongs=new ArrayList<Song>(); //预设歌曲集合
	private ArrayList<Song> mOrderdSongs=new ArrayList<Song>(); //已点歌曲集合
	private ArrayList<Father> mToData=new ArrayList<Father>(); //对谁说的集合
	public ArrayList<Father> mToGiftData=new ArrayList<Father>(); //对谁送礼物的集合
	private ArrayList<DriverCar> mCarData=new ArrayList<DriverCar>(); //座驾类
	private ArrayList<RunTunnel> tunnels=new ArrayList<RunTunnel>(); //座驾类
	private HashMap<String, String> mGiftMaps;
	private EditText chatInput;
	private JSONObject receiverObj=new JSONObject(); //接收人信息 
	private ViewPager mPagerEmotion;
	private PopupWindow mShowGiftPop,mHostInfoPop;
	private int faceWidth,Chatface;
	private PullListView mRoomUserList;//房间里面的用户
	private ListView mlvRoomFans;//房间里面的粉丝
	private View footView;
	private View layoutYuan;
	private View ivVideoLoad;//视频加载的时候  显示的页面
	private View privateNoLogo;//当用户没有登录点击私聊框的时候显示
	private TextView tvPrivateTag;//私聊框显示的内容
	private int lastVisbileIndex=0;
	private int mPopChattoWidth,haoHuaWebHeight;
	private InputMethodManager mInputManager;
	private TextView mTvChatTo,tvYuanBaoCount; 
	private boolean mIsVip=false; //用来表示用户是否是VIP 
	private int mGroupCount;//来显示用户选中的组合礼物的个数
	private float mDownX,mUpx,mDistanx;
	private int pageCount, realPage;//歌曲总页数和当前页数
	private View layoutBottom,layoutFun;
	public int toUserId;//送礼接收人的ID
	private TextView tvBin,tvPoint;
	private Bitmap loadMap; //图片默认加载显示
	private YunBaoTask baoTask; //获取元宝的任务接口
	private TopFunTask funTask;//获取本场皇冠粉丝排行版
	private JSONObject jsonImpressObject=new JSONObject();
	private File haoHuaDir;//保存豪华礼物的跟目录
	private File giftDir;//保存礼物小图片和大图片的目录//用来随机显示礼物的位置 第一个容器
	private CheckBox privateCb;//选择是否是私聊
	private TextView tvAudiceNum,tvHostYuanBaoCount;
	private int hostYuanbaoCount;
	private int userNum;
	Random mRandom=new Random();
	private boolean fromTask=false;//用来表示用户是来自新手任务列表
	private boolean isBeShutUp;//来标示用户是否被禁言了
	private String taskId;//新手任务的ID号
//	private TextView mAllUserFoot;
	private FansBean fansBean;//粉丝榜类
	private int[] giftOneGravity=new int[]{
			Gravity.CENTER_VERTICAL,Gravity.TOP,
			Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, Gravity.CENTER, Gravity.RIGHT|Gravity.CENTER,
			Gravity.BOTTOM|Gravity.LEFT, Gravity.BOTTOM|Gravity.RIGHT};
	
	//用来随机显示礼物的位置 第二个容器
	private int[] giftTwoGravity=new int[]{Gravity.CENTER,Gravity.CENTER_VERTICAL,Gravity.CENTER_HORIZONTAL,Gravity.RIGHT,
				Gravity.RIGHT|Gravity.CENTER,Gravity.LEFT,Gravity.TOP
				};
		
	private ArrayList<Host> recommandHost=new ArrayList<Host>();
	private View videoRecomd;
	private GridView gvRecomd;
//	private AnimationDrawable drawable;
	private ArrayList<Father> rebertData=new ArrayList<Father>();
	private String roomid;
	private boolean canEdit = false;
	private LinearLayout tv_host_love;
	private ImageView room_back_btn;
	private View title;
	private View host_info,roomback_layoutView,roomhost_xinge;
	private TranslateAnimation titleInAnim,titleOutAnim,liveInAnim,liveOutAnim; 
	private boolean  isShow=false;
	private TextView roomhostname;
	private AsyncImageLoader imageLoader;
//	private TextView pubchat,prichat,audience,fans;
	private int[] text = new int[]{R.id.room_tab_pub_txt,R.id.room_tab_private_txt,R.id.room_tab_viewer_txt,
			R.id.room_tab_viewer_num_txt,R.id.room_tab_fans};
	private TextView[] selectViews = new TextView[text.length];
	private ImageView roomfans_one,roomfans_two,roomfans_three;
	private View roomuserbottom ;
	//管理员不加载列表
	private int loadflag=0;
	private boolean firstshowhost = true;
	private Timer timer;
	private TimerTask task;
	private int mUid;
	private Handler uiHandler=new Handler(){
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10086:
				NLog.e("test", "timer!!!!!!!!!");
					if (mUser!=null) {
							testsendMsgToServer(69);
					}
					else {
							testsendMsgToServer(69);
						}
				break;
			case CAN_EDIT_MSG://延迟10s才能点击输入框
				canEdit = true;
				break;
			case SERVER_CONNECTED:
				joinRoom();
				break;
				
			case SERVER_ERROR:
				Utils.MakeToast(getApplicationContext(), "聊天服务器异常");
				break;
			case SET_GIFT_GONE://去掉显示礼物的GifView
				Gift bean=(Gift)msg.obj;
				if(bean!=null){
					int whichContainer=Integer.valueOf(bean.getContainer());
					GifView view=bean.getGifView();
					view.startAnimation(AnimationUtils.loadAnimation(ChatRoomActivity.this, R.anim.alpha_out));
					view.setVisibility(View.GONE);
					//释放资源,避免过多的内存溢出
					view.destroy();
					if(whichContainer==0){
						surfaceView.removeView(view);
					}else{
						GiftContainerTwo.removeView(view);
					}
				}
				break;
				
			case SHUT_UP_MSG://30分钟后恢复禁言
				isBeShutUp=false;
				break;
				
			case DISMISS_YUAN_BAO:
				surfaceView.removeView((MyWebView)msg.obj);
				break;
			
			case DISMISS_FLY_WINDOW://消掉飞屏
				surfaceView.removeView((ScrollTextView)msg.obj);
				break;
			case SHOW_GROUP_GIFT://显示组合礼物
				MyWebView GroupWebView=getWebView();
				Gift giftGroup=(Gift) msg.obj;
				GroupWebView.loadUrl("file://"+giftGroup.getResource());
				RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, haoHuaWebHeight);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
//				FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.fill_parent, FrameLayout.LayoutParams.WRAP_CONTENT);
//				params.gravity=Gravity.CENTER;
				ParentView.addView(GroupWebView, params);
				mGroupGift.remove(giftGroup);
				uiHandler.sendMessageDelayed(uiHandler.obtainMessage(DISMISS_WEB_VIEW, GroupWebView), 22*1000);
				break;
			case HAO_HUA_GIFT://显示豪华礼物
				MyWebView myWebView=getWebView();
				Gift gift=(Gift) msg.obj;
				myWebView.loadUrl("file://"+gift.getResource());
				RelativeLayout.LayoutParams param=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, haoHuaWebHeight);
				param.addRule(RelativeLayout.CENTER_IN_PARENT);
	//			FrameLayout.LayoutParams param=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.fill_parent, FrameLayout.LayoutParams.WRAP_CONTENT);
	//			param.gravity=Gravity.CENTER;
				ParentView.addView(myWebView, param);
				mHaoHuaGift.remove(gift);
				if(gift.getGiftId().equals("100021")){
					uiHandler.sendMessageDelayed(uiHandler.obtainMessage(DISMISS_WEB_VIEW, myWebView), 35*1000);
				}else{
					uiHandler.sendMessageDelayed(uiHandler.obtainMessage(DISMISS_WEB_VIEW, myWebView), 22*1000);
				}
				break;
			
			case DISMISS_WEB_VIEW://移除显示豪华礼物的webview
				ParentView.removeView((MyWebView)msg.obj);
				if(mHaoHuaGift.size()>0){
					uiHandler.sendMessage(uiHandler.obtainMessage(HAO_HUA_GIFT, mHaoHuaGift.get(0)));
				}
				if(mGroupGift.size()>0){
					uiHandler.sendMessage(uiHandler.obtainMessage(SHOW_GROUP_GIFT, mGroupGift.get(0)));
				}
				break;
			
			case READ_MESSAGE:
				Exception e=(Exception) msg.obj;
				Utils.MakeToast(getApplicationContext(), e.getLocalizedMessage());	
				break;
			case MESSAGE_FAILED:
				Exception error=(Exception) msg.obj;
				Utils.MakeToast(getApplicationContext(), error.getLocalizedMessage());	
//				Utils.MakeToast(getApplicationContext(), "发送失败");
				break;
			case LOGIN_MESSAGE_FAIELD:
				Utils.MakeToast(getApplicationContext(), "进入房间失败");
				break;
			case NORMAL_MESSAGE: //正常的消息
						String incomeMsg=(String) msg.obj;
						try {
							JSONObject jsonObject=new JSONObject(incomeMsg);
							int type=jsonObject.getInt("type");
							JSONObject jdata = jsonObject.optJSONObject("data");
							switch (type) {
							case ConstantUtil.SOCKTYPE_INTO: //用户进入房间的消息
								JSONObject data=jsonObject.optJSONObject("data");
								String nickName=data.optString("nickname");
								String drive=data.optString("user_props_id");
								int id = Integer.parseInt(data.optString("uid"));
								Log.e("test", "id=="+id);
								if(Utils.isNotEmptyString(nickName)&&id>10000000){
									ChatMessage chatMessage=new ChatMessage();
									chatMessage.setSendTime(setMessageTime());
									chatMessage.setSendNickName(nickName);
								
								if(Utils.isNotEmptyString(drive)&&!drive.equals("0")){
									boolean isFound = false;
									for (int i = 0; i <mCarData.size(); i++) {
										DriverCar car=mCarData.get(i);
										if(car.getCarId().equals(drive)){
											isFound = true;
											chatMessage.setMessageContent("开着"+car.getCarName()+" [#car"+drive+"#] "+"进入房间");
											break;
										}
									}
									if(!isFound)
										chatMessage.setMessageContent("进入房间");

								}else{
									chatMessage.setMessageContent("进入房间");
								}
								publicMesg.add(chatMessage);
								chatMessageAdapter.notifyDataSetChanged();
								}
//								else{
//									chatMessage.setSendNickName("游客"+mRandom.nextInt(10000000));
//								}
								//Log.v("joinRoom", nickName+"进入房间");
								Father father=new Father();
								father.setAvatar(data.optString("avatar"));
								father.setCredit(data.optString("credit"));
								father.setNickName(data.optString("nickname"));
								father.setPlatform(data.optInt("platform"));
								father.setUid(data.optString("uid"));
								father.setUserType(data.optString("usertype"));
								father.setVipType(data.optString("viptype"));
								father.setWeath(data.optString("wealth"));
								father.setDetail(data.toString());
								//mToData.add(father);
								if(!mAllUserData.contains(father)){
									mAllUserData.add(father);
									userNum=userNum+1;
									tvAudiceNum.setText("("+userNum+")");
									//jin added
									if(mUserAdapter!=null)
										mUserAdapter.notifyDataSetChanged();
								}
								break;
							case ConstantUtil.SOCKTYPE_LEAVE://用户离开房间
								/*JSONObject leave=jsonObject.optJSONObject("data");
								String uid=leave.optString("uid");
								ChatMessage chatLeaveMsg=new ChatMessage();
								chatLeaveMsg.setSendTime(setMessageTime());
								if(uid.endsWith("1")){
									chatLeaveMsg.setSendNickName("游客");
								}else{
									for (int i = 0; i <mAllUserData.size(); i++) {
										Father fathers=mAllUserData.get(i);
										if(fathers.getUid().equals(uid)){
											mAllUserData.remove(fathers);
											chatLeaveMsg.setSendNickName(fathers.getNickName());
											userNum=userNum-1;
											if(userNum<0) userNum=0;
											tvAudiceNum.setText("("+userNum+")");
											break;
										}else{
											chatLeaveMsg.setSendNickName(uid);
										}
									}
								}
								chatLeaveMsg.setMessageContent("离开房间");
								publicMesg.add(chatLeaveMsg);
								chatMessageAdapter.notifyDataSetChanged();*/
								JSONObject leave=jsonObject.optJSONObject("data");
								String uid=leave.optString("uid");
								for (int i = 0; i <mAllUserData.size(); i++) {
									Father fathers=mAllUserData.get(i);
									if(fathers.getUid().equals(uid)){
										mAllUserData.remove(fathers);
										break;
									}
								}
								if(mUserAdapter!=null)
									mUserAdapter.notifyDataSetChanged();
								break;
							case 3://用户升级
								JSONObject jsonObject1=jsonObject.optJSONObject("data");
								String levelName=jsonObject1.optString("nickname");
								String level=jsonObject1.optString("wealthlevel");
								ChatMessage chatMessage2=new ChatMessage();
								chatMessage2.setSendTime("恭喜");
								chatMessage2.setSendNickName(levelName);
								chatMessage2.setMessageContent("升级到"+level+"富");
								if(mUser!=null){
									mUser.setWeath(jsonObject1.optString("wealth"));
//									Utils.saveUser(ChatRoomActivity.this, mUser); //重新更新用户的财富信息
//									ApplicationEx.get().getUserManager().getUser().setWeath(mUs);
								}
								/**
								 *  更新用户列表里面的用户的财富信息
								 */
								Father fat=new Father();
								fat.setUid(jsonObject1.optString("uid"));
								if(mAllUserData.contains(fat)){
									int index=mAllUserData.indexOf(fat);
									Father fat1=mAllUserData.get(index);
									fat1.setWeath(jsonObject1.optString("wealth"));
								}
								publicMesg.add(chatMessage2);
								chatMessageAdapter.notifyDataSetChanged();
								break;
								
							case 4://用户公聊
								//彩条  <img src="http://static.9xiu.com/www//img/color_bar/color_bar_1.gif">
								JSONObject jsonObject2=jsonObject.getJSONObject("data");
								JSONObject sendObject=jsonObject2.getJSONObject("userinfo");
								String message=jsonObject2.getString("msg");
								JSONObject receiverObject=jsonObject2.getJSONObject("recived");
								ChatMessage chatpublicMsg=new ChatMessage();
								chatpublicMsg.setSendTime(setMessageTime());
								chatpublicMsg.setSendNickName(sendObject.getString("nickname"));
								String toNickName=receiverObject.optString("nickname");
								
								if(toNickName.equals("null")||toNickName.equals("")){
									
								}else{
									chatpublicMsg.setSendAction("对");
									chatpublicMsg.setToNickName(toNickName);
								}
								if(message.contains("span")){
									int startIndex=message.indexOf(">");
									int endIndex=message.lastIndexOf("<");
									chatpublicMsg.setMessageContent(" 说:"+message.substring(startIndex+1, endIndex));
								}else{
									chatpublicMsg.setMessageContent(" 说:"+message);
								}
								
								if(message.indexOf("<img src=")!=-1){
									//[#mgift"+giftId+"#]
									int start=message.lastIndexOf("_");
									int end=message.indexOf(".gif");
									String str=message.substring(start+1,end);
									chatpublicMsg.setMessageContent(" 说: "+" [#colorbar"+str+"#] ");
								}
								
								publicMesg.add(chatpublicMsg);
								chatMessageAdapter.notifyDataSetChanged();
								if(!mIsPrivate){
									chatInput.setText("");
								}
								break;
								
							case 5: //用户私聊信息
								JSONObject jsonObject3=jsonObject.getJSONObject("data");
								JSONObject sendObj=jsonObject3.getJSONObject("userinfo");
								JSONObject receObj=jsonObject3.getJSONObject("recived");
								String uId1=sendObj.getString("uid");
								String uId2=receObj.getString("uid");
								if(mUser!=null){//过滤私聊消息
									if(uId1.equals(mUser.getUid())||uId2.equals(mUser.getUid())){
										String message3=jsonObject3.getString("msg");
										ChatMessage msgPrivate=new ChatMessage();
										msgPrivate.setSendTime(setMessageTime());
										msgPrivate.setSendNickName(sendObj.getString("nickname"));
										msgPrivate.setSendAction("对");
										msgPrivate.setToNickName(receObj.getString("nickname"));
										if(message3.contains("span")){
											int startIndex=message3.indexOf(">");
											int endIndex=message3.lastIndexOf("<");
											msgPrivate.setMessageContent(" 说:"+message3.substring(startIndex+1, endIndex));
										}else{
											msgPrivate.setMessageContent(" 说:"+message3);
										}
										if(message3.indexOf("<img src=")!=-1){
											//[#mgift"+giftId+"#]
											int start=message3.lastIndexOf("_");
											int end=message3.indexOf(".gif");
											String str=message3.substring(start+1,end);
											msgPrivate.setMessageContent(" 说: "+"[#colorbar"+str+"#]");
										}
										privateMsg.add(msgPrivate);
										privateMessageAdapter.notifyDataSetChanged();
										privateDot.setVisibility(View.VISIBLE);
										privateDot.startAnimation(AnimationUtils.loadAnimation(ChatRoomActivity.this, R.anim.light));
										if(mIsPrivate) chatInput.setText("");
		 							}
								}
								break;
		
							case 6://送礼
								JSONObject jsonObject4=jsonObject.optJSONObject("data");
								String nickNameGift=jsonObject4.optString("nickname");
								String receiverNickName=jsonObject4.optString("tonickname");
								String giftName=jsonObject4.optString("giftname");
								String giftId=jsonObject4.optString("gid");
							
//								int giftType=jsonObject4.optInt("gifttype");
								if(fromTask){//给心爱的主播送出一朵红玫瑰
									if(Integer.valueOf(giftId)==1){
										Utils.doUserTask(mUser, taskId,null);
									}
								}
								String totalPrice=jsonObject4.optString("totalprice");
								if(mUser!=null){
									Utils.UpdateUserInfo(mUser, ChatRoomActivity.this);
//									String toUid=jsonObject4.optString("touid");
//									if(toUid.equals(mUser.getUid()) || toUid.equals(jsonObject4.optString("uid"))){
//										mUser.setUserCoin(jsonObject4.optInt("tousercoins"));
//										mUser.setUserDian(jsonObject4.optInt("touserdian"));
//										Utils.UpdateUserInfo(mUser, ChatRoomActivity.this);
//									}else if(jsonObject4.optString("uid").equals(mUser.getUid())){
//										mUser.setUserCoin(jsonObject4.optInt("usercoins"));
//										mUser.setUserDian(jsonObject4.optInt("userdian"));
//									}
								}
								//mUser=Utils.isUserLogin(this);
//								Utils.saveUser(ChatRoomActivity.this, mUser);
								if(Integer.valueOf(totalPrice)>=50000){
									RunTunnel runTunnel=new RunTunnel();
									runTunnel.setCount(jsonObject4.optString("count"));
									runTunnel.setTime(jsonObject4.optString("time"));
									runTunnel.setGid(jsonObject4.optString("gid"));
									runTunnel.setGiftname(jsonObject4.optString("giftname"));
									runTunnel.setNickname(jsonObject4.optString("nickname"));
									runTunnel.setTonickname(jsonObject4.optString("tonickname"));
									runTunnel.setTousernum(jsonObject4.optString("tousernum"));
									runTunnel.setUsernum(jsonObject4.optString("usernum"));
									tunnels.add(0,runTunnel);
									setRuningWayData();
								}
								String count=jsonObject4.optString("count");
								int counts=1;
								if(Utils.isNotEmptyString(count)){
									counts=Integer.valueOf(count);
								}
								ChatMessage message2=new ChatMessage();
								message2.setSendTime(setMessageTime());
								message2.setSendNickName(nickNameGift);
								message2.setSendAction("向");
								message2.setToNickName(receiverNickName);
								message2.setMessageContent(" 送"+giftName+" [#mgift"+giftId+"#] "+count+"个");
								publicMesg.add(message2);
								chatMessageAdapter.notifyDataSetChanged();
								//String resType=mGiftMaps.get(giftId);
								String resType="null";
								try {
									resType=jsonObject4.getString("restype");
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								if(giftDir!=null){
								if(counts>=50){//组合礼物
									if(Utils.isNotEmptyString(resType)&&Integer.valueOf(resType)==2){
										if(haoHuaDir!=null){
											for (int i = 0; i < counts; i++) {
													String html=haoHuaDir.getAbsolutePath()+File.separator+"html"+File.separator+"mgift"+giftId+File.separator+"index.html";
													Gift giftHao=new Gift();
													giftHao.setResource(html);
													giftHao.setGiftId(giftId);
													mHaoHuaGift.add(giftHao);
													if(mHaoHuaGift.size()==1){
														uiHandler.sendMessage(uiHandler.obtainMessage(HAO_HUA_GIFT, giftHao));
													}else{
														uiHandler.sendMessageDelayed(uiHandler.obtainMessage(HAO_HUA_GIFT, giftHao), 22*1000);
													}
											}
										}
										
									}else{
										mGroupCount=counts;
										File sourceFile=new File(giftDir,"mgift"+giftId+".png");
										showGroupGift(sourceFile);
									}
								}else{
										if(Utils.isNotEmptyString(resType)&&Integer.valueOf(resType)==2){//豪华礼物
											if(haoHuaDir!=null){
												String html=haoHuaDir.getAbsolutePath()+File.separator+"html"+File.separator+"mgift"+giftId+File.separator+"index.html";
												Gift giftHao=new Gift();
												giftHao.setGiftId(giftId);
												giftHao.setResource(html);
												mHaoHuaGift.add(giftHao);
												if(mHaoHuaGift.size()==1){
														uiHandler.sendMessage(uiHandler.obtainMessage(HAO_HUA_GIFT, giftHao));
												}else{
														uiHandler.sendMessageDelayed(uiHandler.obtainMessage(HAO_HUA_GIFT, giftHao), 22*1000);
												}
											}
										}else{
											if(counts>10){
												counts=10;
											}
											if(giftId.equals("999")){//元宝礼物
												hostYuanbaoCount=hostYuanbaoCount+1;
												tvHostYuanBaoCount.setText(hostYuanbaoCount+"");
												MyWebView webView=getWebView();
												FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
												layoutParams.gravity=Gravity.CENTER;
												webView.setLayoutParams(layoutParams);
												surfaceView.addView(webView);
												webView.loadUrl("file:///android_asset/Gold/gold.html");
												uiHandler.sendMessageDelayed(uiHandler.obtainMessage(DISMISS_YUAN_BAO, webView),8000);
											}else{//普通gif礼物
												File targetFile=new File(giftDir,"mgift"+giftId+".gif");
												if(targetFile.exists()){
													showGiftOnScreen(targetFile.getAbsolutePath(),counts);
												}
											}
										}
									}
								}
								break;
								
							case ConstantUtil.SOCKTYPE_SADMIN://设置管理员
								JSONObject jsonObject5=jsonObject.optJSONObject("data");
								String setNickName=jsonObject5.optString("nickname");
								String beNickName=jsonObject5.optString("tonickname");
								String beUid=jsonObject5.optString("touid");
								Father manager=new Father();
								manager.setUid(beUid);
								manager.setNickName(beNickName);
								if(!mManagerUser.contains(manager)){
									mManagerUser.add(manager);
								}
								ChatMessage chatMessage5=new ChatMessage();
								chatMessage5.setSendTime(setMessageTime());
								chatMessage5.setSendAction("把");
								chatMessage5.setSendNickName(setNickName);
								chatMessage5.setToNickName(beNickName);
								chatMessage5.setMessageContent("设置为房间管理员");
								publicMesg.add(chatMessage5);
								chatMessageAdapter.notifyDataSetChanged();
							break;
							
							case ConstantUtil.SOCKTYPE_CADMIN://删除管理员
								JSONObject jsonObject9=jsonObject.optJSONObject("data");
								String delNickName=jsonObject9.optString("nickname");
								String beDelNickName=jsonObject9.optString("tonickname");
								String deUid=jsonObject9.optString("touid");
								Father deUser=new Father();
								deUser.setUid(deUid);
								deUser.setNickName(beDelNickName);
								if(mManagerUser.contains(deUser)){
									mManagerUser.remove(deUser);
								}
								ChatMessage chatMessage6=new ChatMessage();
								chatMessage6.setSendTime(setMessageTime());
								chatMessage6.setSendAction("被");
								chatMessage6.setSendNickName(beDelNickName);
								chatMessage6.setToNickName(delNickName);
								chatMessage6.setMessageContent("取消房间管理员资格");
								publicMesg.add(chatMessage6);
								chatMessageAdapter.notifyDataSetChanged();
								break;
								
							case ConstantUtil.SOCKTYPE_KICK://踢人
								JSONObject jsonObject6=jsonObject.optJSONObject("data");
								String kickName=jsonObject6.optString("nickname");
								String toKickOut=jsonObject6.optString("tonickname");
								String toUids=jsonObject6.optString("touid");
								if(mUser!=null){
									if(toUids.equals(mUser.getUid())){
										Utils.MakeToast(getApplicationContext(), "你被剔出该房间");
										finish();
									}
								}
								ChatMessage message6=new ChatMessage();
								message6.setSendTime(setMessageTime());
								message6.setSendNickName(toKickOut);
								message6.setSendAction("被");
								message6.setToNickName(kickName);
								message6.setMessageContent("剔出房间30分钟");
								publicMesg.add(message6);
								chatMessageAdapter.notifyDataSetChanged();
								for (int i = 0; i <mAllUserData.size(); i++) {
									Father fathers=mAllUserData.get(i);
									if(fathers.getUid().equals(toUids)){
										mAllUserData.remove(fathers);
										mUserAdapter.notifyDataSetChanged();
										break;
									}
								}
								
								break;
							
							case 20://禁言
									if(jdata.has("errornum")){
									int errornum = jdata.getInt("errornum");
									if(errornum == ConstantUtil.ERRORNUM_KICK){
										Utils.MakeToast(getApplicationContext(), "你被剔出该房间");
										finish();
									}
									else if(errornum == ConstantUtil.ERRORNUM_SAME)
										Log.v(TAG,"用户在此房间已经登录");
									else if(errornum == ConstantUtil.ERRORNUM_SADMIN)
										Log.v(TAG,"自己已经是管理员，回写一个错误提示");
									else if(errornum == ConstantUtil.ERRORNUM_BANNED){
										//// 被禁言
										//JSONObject jsonObject7=jsonObject.optJSONObject("data");
										String speakName= "";//mUser.getNickName();
										String nospeak=mUser.getNickName();
										ChatMessage message7=new ChatMessage();
										message7.setSendTime(setMessageTime());
										message7.setSendNickName(nospeak);
										message7.setSendAction("被");
										message7.setToNickName(speakName);
										message7.setMessageContent("禁言了");
										if(mUser!=null){
											if(mUser.getNickName().equals(nospeak)){
												if(Utils.isNotEmptyString(mUser.getVipType())){
													if(mUser.getVipType().equals("800003")){
														isBeShutUp=false;
													}
												}else{
													isBeShutUp=true;
												}
											}else{
												isBeShutUp=true;
												uiHandler.sendEmptyMessageDelayed(SHUT_UP_MSG, 30*60*1000);
											}
										}
										publicMesg.add(message7);
										chatMessageAdapter.notifyDataSetChanged();
									}
								}else{
									////中奖信息
									JSONObject jsonObject10=jsonObject.optJSONObject("data");
									String sendNickName=jsonObject10.optString("nickname");
									String rNickName=jsonObject10.optString("rnick");
									String giftNames=jsonObject10.optString("giftname");
									String giftIds=jsonObject10.optString("gid");
									String rewardType=jsonObject10.optString("rewardtype");//几倍大奖
									if(Integer.valueOf(rewardType)==500){
										String html=haoHuaDir.getAbsolutePath()+File.separator+"html"+File.separator+"effect_wealth_god"+File.separator+"index.html";
										Gift mGift = new Gift();
										mGift.setGiftId("0");
										mGift.setResource(html);
										uiHandler.sendMessage(uiHandler.obtainMessage(HAO_HUA_GIFT, mGift));
									}
								
									String rewardtime=jsonObject10.optString("rewardtime");//几次
									int reward=jsonObject10.optInt("totalprice");//总共的奖励
									String countss=jsonObject10.optString("count");
									ChatMessage message4=new ChatMessage();
									message4.setSendTime(setMessageTime());
									message4.setSendNickName(sendNickName);
									message4.setSendAction("在");
									message4.setToNickName(rNickName);
//									message4.setReward(reward);
//									message4.setRewardtype(rewardType);
									message4.setReward(true);
									message4.setMessageContent(" 直播间送"+giftNames+" [#mgift"+giftIds+"#] "+countss+"个,喜中"+rewardType+"倍大奖"+rewardtime+"次"+"奖励"+reward+"九点");
//									message4.setMessageContent("送"+giftNames+"[#mgift"+giftIds+"#]"+countss+"个,喜中");
//									message4.setRewardDes("倍大奖"+rewardtime+"次");
									publicMesg.add(message4);
									chatMessageAdapter.notifyDataSetChanged();
									String uidWard=jsonObject10.optString("uid");
									if(mUser!=null){
										if(uidWard.equals(mUser.getUid())){
											Utils.UpdateUserInfo(mUser, ChatRoomActivity.this);
//											mUser.setUserCoin(mUser.getUserCoin() + reward);
										}
									}
								}
								break;
							
							case ConstantUtil.SOCKTYPE_BANNED:
								JSONObject jsonObject7=jsonObject.optJSONObject("data");
								String speakBackName0=jsonObject7.optString("nickname");
								String noBackspeak0=jsonObject7.optString("tonickname");
								if(mUser!=null){
									if(mUser.getNickName().equals(noBackspeak0)){
										isBeShutUp=true;
										uiHandler.sendEmptyMessageDelayed(SHUT_UP_MSG, 30*60*1000);
									}
								}
								ChatMessage message7=new ChatMessage();
								message7.setSendTime(setMessageTime());
								message7.setSendNickName(noBackspeak0);
								message7.setSendAction("被");
								message7.setToNickName(speakBackName0);
								message7.setMessageContent("禁言了");
								publicMesg.add(message7);
								chatMessageAdapter.notifyDataSetChanged();
								break;
							case ConstantUtil.SOCKTYPE_REPLY://恢复禁言
								JSONObject jsonObject8=jsonObject.optJSONObject("data");
								String speakBackName=jsonObject8.optString("nickname");
								String noBackspeak=jsonObject8.optString("tonickname");
								if(mUser!=null){
									if(mUser.getNickName().equals(noBackspeak)){
										isBeShutUp=false;
									}
								}
								ChatMessage message8=new ChatMessage();
								message8.setSendTime(setMessageTime());
								message8.setSendNickName(noBackspeak);
								message8.setSendAction("被");
								message8.setToNickName(speakBackName);
								message8.setMessageContent("恢复发言了");
								publicMesg.add(message8);
								chatMessageAdapter.notifyDataSetChanged();
								break;
							
							case 13://点歌
								
								break;
								
							case 14: //处理点歌
								JSONObject jsonObject14=jsonObject.optJSONObject("data");
								String agree=jsonObject14.optString("deltype");
								String nickName9=jsonObject14.optString("nickname");
								if(mUser!=null){
									if(nickName9.equals(mUser.getNickName())){
										Utils.UpdateUserInfo(mUser, ChatRoomActivity.this);
									}
								}
								String musicName=jsonObject14.optString("musicname");
								String msgSong=jsonObject14.optString("msg");
								ChatMessage chatMessage3=new ChatMessage();
								chatMessage3.setSendTime(setMessageTime());
								if(!agree.equals("4")){
									chatMessage3.setMessageContent("主播同意 "+nickName9+"点歌:"+musicName+"给"+msgSong);
//									mUser.setUserCoin((Integer.valueOf(mUser.getWeath())-money));
									Utils.UpdateUserInfo(mUser, ChatRoomActivity.this);
								}else{
									chatMessage3.setMessageContent("主播不同意 "+nickName9+" 点歌:"+musicName+" 原因:"+msgSong);
								}
								publicMesg.add(chatMessage3);
								chatMessageAdapter.notifyDataSetChanged();
								break;
							case 255:
								Utils.MakeToast(ChatRoomActivity.this, "您已经登录此房间！");
								finish();
								break;
							case 15:// 喇叭
								
								break;
							
							case 16: //用户抢座
								
								break;
							
							case 17: //封号
								JSONObject jsonObject11=jsonObject.optJSONObject("data");
								String name=jsonObject11.optString("nickname");
								ChatMessage chatMessage4=new ChatMessage();
								chatMessage4.setSendTime(setMessageTime());
								chatMessage4.setMessageContent(name+"被封号了");
								publicMesg.add(chatMessage4);
								chatMessageAdapter.notifyDataSetChanged();
								break;
							
							case 19://飞屏
								JSONObject jsonObject19=jsonObject.optJSONObject("data");
								String flyNickName=jsonObject19.optString("nickname");
								String flyContent=jsonObject19.optString("content");
								ScrollTextView flyWindowTextView=new ScrollTextView(ChatRoomActivity.this);
								flyWindowTextView.pauseScroll();
								flyWindowTextView.setText(Html.fromHtml(getFlyMessage(flyNickName, flyContent)));
								flyWindowTextView.startScroll();
								FrameLayout.LayoutParams params2=new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,Gravity.CENTER);
								flyWindowTextView.setLayoutParams(params2);
								surfaceView.addView(flyWindowTextView);
								uiHandler.sendMessageDelayed(uiHandler.obtainMessage(DISMISS_FLY_WINDOW, flyWindowTextView), 5000);
								break;
							
						case 21:
							if(jdata.getInt("errornum") == ConstantUtil.ERRORNUM_KICK){
								Utils.MakeToast(getApplicationContext(), "你被剔出该房间");
								finish();
							}
							break;
						case 500:
							int errornum = jdata.getInt("errornum");
							if(errornum == 1){
								Utils.MakeToast(getApplicationContext(), "被禁言");
								isBeShutUp=false;
							}else if(errornum == 2){
								Utils.MakeToast(getApplicationContext(), "用户已经是管理员");
							}else if(errornum == 4){
								Utils.MakeToast(getApplicationContext(), "用户被踢出房间");
								finish();
							}else if(errornum == 8){
								Utils.MakeToast(getApplicationContext(), "用户已经在房间");
								joinRoom();
							}else if(errornum == 16){
								Utils.MakeToast(getApplicationContext(), "VIP 礼物普通用户不能送");
							}else if(errornum == 32){
								Utils.MakeToast(getApplicationContext(), "包裹不足");
							}else if(errornum == 64){
								Utils.MakeToast(getApplicationContext(), "余额不足");
							}else if(errornum == 128){
								Utils.MakeToast(getApplicationContext(), "送礼物失败（PHP处理业务失败");
							}else if(errornum == 256){
								Utils.MakeToast(getApplicationContext(), "用户未登陆");
							}
							break;
						  }
						} catch (Exception ex) {
							ex.printStackTrace();
					}
					break;
			}
		}
	};
	
	public void test(){
		String jiang = "{\"type\":20,\"data\":{\"isall\":1,\"uid\":\"10091358\",\"nickname\":\"testha0\",\"credit\":\"31273400\",\"wealth\":\"7329676\",\"usernum\":\"90001385\",\"count\":1,\"rnum\":\"90001005\",\"gid\":100014,\"giftname\":\"\u5e78\u8fd0\u6cf0\u8fea\u718a\",\"rnick\":\"zbzzbd1\",\"rcredit\":\"1248041672\",\"rid\":\"449\",\"rewardtype\":500,\"rewardtime\":1,\"totalprice\":1000}}";
		uiHandler.sendMessageDelayed(uiHandler.obtainMessage(ChatRoomActivity.NORMAL_MESSAGE, jiang),15000);
	}
	
	private IUserDataChanged iUserDataChanged;
	
	private Host getHost(Intent intent){
		Host host = new Host();
		Log.i("!!!!!!!!!!!!", ""+intent.getStringExtra("roomId"));
		host.setRoomId(intent.getStringExtra("roomId"));
		host.setIsPlay(intent.getStringExtra("isPlay"));
		host.setRoomTag(intent.getStringExtra("roomTag"));
		host.setUid(intent.getStringExtra("uid"));
		host.setNickName(intent.getStringExtra("nickName"));
		host.setAudice(intent.getStringExtra("audice"));
		host.setCredit(intent.getStringExtra("credit"));
		host.setUserNum(intent.getStringExtra("userNum"));
		host.setImpress(intent.getStringExtra("impress"));
		host.setAvatar(intent.getStringExtra("avatar"));
		return host;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.chat_room_port);
		haoHuaDir=Utils.getCachDir();
		giftDir=Utils.getGiftDir();
		mUser=ApplicationEx.get().getUserManager().getUser();
		ViewConfiguration configuration=ViewConfiguration.get(this);
		mDistanx=configuration.getScaledTouchSlop();
		initView();
		initAnim();
		
//		popHostInfoHeight=(int) getResources().getDimension(R.dimen.pop_host_infoheigth);
		Intent intent=getIntent();
		//mHost=(Father)intent.getSerializableExtra("host");
		mHost = (Father)getHost(intent);
		fromTask=intent.getBooleanExtra("fromtask", false);
		taskId=intent.getStringExtra("taskid");
//		mUser=Utils.isUserLogin(this);
		roomid = mHost.getRoomId();
		if(mUser!=null){
			getYuanCount();
			if(Utils.isNotEmptyString(mUser.getVipType())){
				if(Integer.valueOf(mUser.getVipType())>=800001){
					mIsVip=true;
				}
			}
			getHostAtt();
		}else{
			chatInput.setHint("登录后发言");
		}
//		drawable=(AnimationDrawable) ivVideoLoad.getBackground();
//		drawable.start();
		try {
			playerViewController = new PlayerViewController(this,ivVideoLoad);
			playerViewController.initPlayerView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if("1".equals(mHost.getIsPlay())){
//			Log.e("test", "tag=="+mHost.getRoomTag()+"id=="+mHost.getRoomId());
			playerViewController.startPlay(mHost.getRoomTag(), mHost.getRoomId());
//			NLog.e("test", "房间ID=="+mHost.getRoomId());
		 }else{
			getRecomandHost();
		}
		ConnectToServer(mChatServerAddress);

		mGiftImageLoader=new AsyncImageLoader();
//		chatMessageAdapter=new ChatMessageAdapter(true);
		chatMessageAdapter=new ChatAdapter(publicMesg, this,ParentView,mAllUserData,mManagerUser,mHost,mUser);
		mPublicListView.setAdapter(chatMessageAdapter);
		mPublicListView.setOnTouchListener(new ListTouchListener());
		privateMessageAdapter=new ChatAdapter(privateMsg,this,ParentView,mAllUserData,mManagerUser,mHost,mUser);
//		mPrivateListView.setAdapter(privateMessageAdapter);
		
//		mPrivateListView.setVisibility(View.VISIBLE);
//		mPublicListView.setVisibility(View.GONE);
		
//		privateMessageAdapter=new ChatMessageAdapter(false);
		
		mPopChattoWidth=(int) getResources().getDimension(R.dimen.pop_chat_to_with);
		haoHuaWebHeight=(int) getResources().getDimension(R.dimen.haohua_web_height);
		baoTask=new YunBaoTask();//用户请求爱心接口
		uiHandler.postDelayed(baoTask,5*60*1000);
		getJsonObject(); //获取用户等级json  
		LoadGiftData();//进来就下载用户礼物文件
		setRoomHostMsg();//根据主播信息 来设置房间的一些状态
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				getRobert();//获取机器人数据
			}
		}).start();
		getImPressObject();//获取主播性格表情的json文件
//		showHostInfoPop();
		getChatServerAddress(); //得到聊天服务器地址
		switchScreen(getResources().getConfiguration());
		tvYuanBaoCount.setText(getSharedPreferences(AppConstants.LOCAL_STORGE, MODE_PRIVATE).getString(AppConstants.LOCAL_YUANBAO_COUNT,"0"));
		uiHandler.sendMessageDelayed(uiHandler.obtainMessage(CAN_EDIT_MSG), 5000);
		
//		 * 定时向服务器发送消息
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 10086;
				uiHandler.sendMessage(message);
			}
		};
		timer.schedule(task, 2000, 30000);
//		test();
		iUserDataChanged = new IUserDataChanged() {
			
			@Override
			public void IUserStateChanged(User user) {
				mUser = user;
				if (tvBin != null) {
					tvBin.setVisibility(View.VISIBLE);
					tvBin.setText("九币:"+mUser.getUserCoin()); 
				}
				if (tvPoint != null) {
					tvPoint.setVisibility(View.VISIBLE);
					tvPoint.setText("九点:"+mUser.getUserDian());
				}
			}
		};
		ApplicationEx.get().getUserManager().addUserDataChangedListener(iUserDataChanged);
	}
	
	public JSONObject parseUser(){
		try {
			JSONObject sendObject=new JSONObject();
			sendObject.put("uid",mUser.getUid());
			sendObject.put("nickname",mUser.getNickName());
			sendObject.put("usernum",mUser.getUserNum());
			sendObject.put("viptype",Utils.isNotEmptyString(mUser.getVipType())?mUser.getVipType():"0");
			sendObject.put("wealth",mUser.getWeath());
			sendObject.put("credit",mUser.getCredit());
			sendObject.put("usertype",mUser.getUserType());
			sendObject.put("avatar",mUser.getAvatar());
			return sendObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void joinRoom(){
		Log.v(TAG,"joinRoom>>>");
		try {
			//Log.e("test","user data="+mUser.getUserDetail());
			JSONObject jsonObject=new JSONObject();
		     mUid=0;
		    String nickname;
			if(mUser!=null){
			    nickname = mUser.getNickName();
				jsonObject.put("uid",mUser.getUid());
				jsonObject.put("rid", roomid);
				jsonObject.put("platform", 1+"");
				jsonObject.put("usernum",mUser.getUserNum()==null?mUser.getUid():mUser.getUserNum());
				jsonObject.put("managerlevel", 0);
				jsonObject.put("user_props_id", mUser.getUser_props());
				//Log.v("test","usertype="+mUser.getUserType()+",credit="+mUser.getCredit()+",wealth="+mUser.getWeath()+",viptype="+mUser.getVipType());
				jsonObject.put("usertype",mUser.getUserType()) ;
				//jsonObject.put("aristocrat", mUser.getAvatar());
				jsonObject.put("wealth",mUser.getWeath());
				jsonObject.put("viptype", mUser.getVipType());
				jsonObject.put("nickname", nickname);
				jsonObject.put("avatar", "");
				//Log.e("test","------credit="+mUser.getCredit());
				jsonObject.put("credit", mUser.getCredit());
			}else{
				int max=10000000;
			    int min=1;
			    mUid = mRandom.nextInt(max)%(max-min+1) + min;
				nickname = "游客"+mUid;
				jsonObject.put("uid", mUid);
				jsonObject.put("rid", roomid);
				jsonObject.put("platform", 1+"");
				jsonObject.put("usernum", mUid+"");
				jsonObject.put("managerlevel", "0");
				jsonObject.put("user_props_id", "0");
				jsonObject.put("usertype", "0");
				//jsonObject.put("aristocrat", "");
				jsonObject.put("wealth", "0");
				jsonObject.put("viptype", 0);
				jsonObject.put("nickname", nickname);
				jsonObject.put("avatar", "");
				jsonObject.put("credit", "0");
			}
			sendMsgToServer("0", jsonObject);
			sendMsgToServer("1", jsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void ConnectToServer(String serverAddress){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Log.v(TAG,"ConnectToServer.....");
					client = new ChatClient();
					client.start("42.62.31.15", 8953,uiHandler);
//					client.start("10.0.201.30", 8953,uiHandler);
//					client.start("42.62.31.17", 8953, uiHandler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
	public void sendGiftAnimation(String giftId){
		String html=haoHuaDir.getAbsolutePath()+File.separator+"html"+File.separator+"temp"+giftId+File.separator+"index.html";
		Gift giftHao=new Gift();
		giftHao.setGiftId(giftId);
		giftHao.setResource(html);
		uiHandler.sendMessage(uiHandler.obtainMessage(HAO_HUA_GIFT, giftHao));
		//mHaoHuaGift.add(giftHao);
		/*if(mHaoHuaGift.size()==1){
				uiHandler.sendMessage(uiHandler.obtainMessage(HAO_HUA_GIFT, giftHao));
		}else{
				uiHandler.sendMessageDelayed(uiHandler.obtainMessage(HAO_HUA_GIFT, giftHao), 22*1000);
		}*/
	}
	/*
	 * 发送消息入场
	 */
	public void sendMsgToJoinRoom(String type,JSONObject jsonObject){
		try {
			JSONObject jsonObject2=new JSONObject();
			jsonObject2.put("type", type);
			jsonObject2.put("data", jsonObject.toString()+"/n");
    	    if(!client.sendmsg(jsonObject2.toString())){
    	    	ConnectToServer(mChatServerAddress);
    			Log.v("SendMessageToServer","Error connect,channel is closed");
    	    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送消息到服务器
	 * @param type
	 * @param jsonObject
	 */
	public void sendMsgToServer(String type,JSONObject jsonObject){
		try {
			JSONObject jsonObject2=new JSONObject();
			jsonObject2.put("type", type);
			jsonObject2.put("data", jsonObject);
			NLog.e("test", "jsonobject=="+jsonObject2.toString()+"\n");
    	    if(!client.sendmsg(jsonObject2.toString()+"\n")){
    	    	ConnectToServer(mChatServerAddress);
    			Log.v("SendMessageToServer","Error connect,channel is closed");
    	    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testsendMsgToServer(int type){
		try {
			JSONObject jsonObject2=new JSONObject();
			jsonObject2.put("type", type);
//			jsonObject2.put("data", jsonObject);
			if (mUser!=null) {
				jsonObject2.put("uid", mUser.getUid());
				jsonObject2.put("rid", roomid+"");
			}else {
				jsonObject2.put("uid", mUid+"");
				jsonObject2.put("rid", roomid+"");
			}
			NLog.e("test",jsonObject2.toString()+"");
    	    client.sendmsg(jsonObject2.toString()+"\n");
//    	    	ConnectToServer(mChatServerAddress);
//    			Log.v("SendMessageToServer","Error connect,channel is closed");
    	    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getRobert() {
		InputStream inputStream=getResources().openRawResource(R.raw.robot);
		String robert=Utils.getLineString(inputStream);
		rebertData.clear();
		try {
			JSONArray array=new JSONArray(robert);
			for (int i = 0; i < array.length(); i++) {
				String nickName=array.optString(i);
				Father father=new Father();
				father.setUid("0");
				father.setNickName(nickName);
				father.setWeath("0");
				father.setAvatar(i+"");
				father.setVipType("0");
				father.setPlatform(0);
				father.setUserType("0");
				JSONObject userObj=new JSONObject();
				userObj.put("uid", "0");
				userObj.put("nickname", nickName);
				userObj.put("wealth", "0");
				userObj.put("os", "0");
				userObj.put("avatar", "");
				userObj.put("viptype", "");
				father.setDetail(userObj.toString());
				rebertData.add(father);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if(playerViewController != null && playerViewController.isPlay())
			playerViewController.stopPlay();
	}
	
	/*
	 * 获取元宝数目
	 */
	private void getYuanCount() {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		asyncHttpClient.post(AppConstants.GET_USER_YUAN_COUNT, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							if(jsonObject2!=null){
								String yuanbaoCount=jsonObject2.optString("intIngot");
								tvYuanBaoCount.setText(yuanbaoCount);
								saveYuanbao(yuanbaoCount);
							}
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


	private void setRoomHostMsg() {
		doGetUserListTask();
		doGetRoomInfo();
		privateMsg.clear();
		publicMesg.clear();
		if(mUser!=null){
			//eyan removed 20131121
			//doGetUserShutUp();
			ChatMessage chatMessage2=new ChatMessage();
			chatMessage2.setSendTime(setMessageTime());
			chatMessage2.setNotice(true);
			chatMessage2.setMessageContent("[69xiu.com] 首次充值即得VIP会员豪华大礼包");
			publicMesg.add(chatMessage2);
			
			ChatMessage chatMessage=new ChatMessage();
			chatMessage.setSendTime(setMessageTime());
			chatMessage.setNotice(true);
			chatMessage.setMessageContent("[69xiu.com] 电脑观看效果更赞：www.69xiu.com");
			publicMesg.add(chatMessage);
		}else{
			ChatMessage chatMessage2=new ChatMessage();
			chatMessage2.setSendTime(setMessageTime());
			chatMessage2.setNotice(true);
			chatMessage2.setMessageContent("[69xiu.com]欢迎您进入直播间,注册或登录即可与主播聊天互动.\r\n[69xiu.com]首次充值更有VIP超级大礼包赠送 ");
			publicMesg.add(chatMessage2);
		}
		//eyan add 20131126
		//chatMessageAdapter.notifyDataSetChanged();
		toUserId=Integer.valueOf(mHost.getUid());
		ChatMessage chatMessage=new ChatMessage();
		chatMessage.setSendTime(setMessageTime());
		chatMessage.setSendNickName(mHost.getNickName());
		chatMessage.setSendAction("对");
		chatMessage.setToNickName("我");
		chatMessage.setMessageContent(" 说 :欢迎来到我的直播间支持我噢~");
		privateMsg.add(chatMessage);
		
		if(Utils.isNotEmptyString(mHost.getAudice())){
			userNum=Integer.valueOf(mHost.getAudice());
		}
		tvAudiceNum.setText("("+userNum+")");
		saveHostSee();
		mToData.clear();
		mToGiftData.clear();
		Father father=new Father();
		father.setNickName("所有人");
		mToData.add(father);
		mToData.add(mHost);
		mToGiftData.add(mHost);
	
		/*mScrollTextView.pauseScroll();
		mScrollTextView.setDuration(6000);
		mScrollTextView.setText("欢迎来"+mHost.getNickName()+"的直播间,喜欢我就加关注吧");
		mScrollTextView.startScroll();*/
		
		if(funTask!=null)
			uiHandler.removeCallbacks(funTask);
		funTask=new TopFunTask();
		uiHandler.postDelayed(funTask, 5*60*1000);
	}


	/*
	 * 获取房间信息 ,包括主播的元宝数目
	 */
	private void doGetRoomInfo() {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("rid", mHost.getRoomId());
		asyncHttpClient.post(AppConstants.GET_ROOM_INFO, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success){
						JSONObject jsonObject2=jsonObject.optJSONObject("retval");
						if(jsonObject2!=null){
							hostYuanbaoCount=jsonObject2.optInt("ingodcount");
							tvHostYuanBaoCount.setText(hostYuanbaoCount+"");
						}
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
	 * 来判断用户是否在该房间内被禁言了
	 */
//	ddd
	private void doGetUserShutUp() {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("rid", mHost.getRoomId());
		params.put("uid", mUser.getUid());
		asyncHttpClient.post(AppConstants.IS_BE_SHUTUP,params , new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							if(jsonObject2!=null){
								String kickUser=jsonObject2.optString("kickUser");
								String bannserUser=jsonObject2.optString("banUser");
								if("1".equals(kickUser)){
									Utils.MakeToast(getApplicationContext(), "你被剔出房间,暂时无法进入该房间");
									finish();
								}
								if("1".equals(bannserUser)){
									isBeShutUp=true; 
								}
							}
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


	/**
	 * 当前直播间没有主播直播   显示推荐的三个主播
	 */
	public void getRecomandHost() {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		asyncHttpClient.post(AppConstants.MOBILE_SHAKE,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					//解决从当前主播跳到推荐主播，主播信息没变的问题
					if (title.getVisibility()==0||host_info.getVisibility()==0) {
						title.setVisibility(8);
						host_info.setVisibility(8);
					}
					
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							JSONArray array=jsonObject.optJSONArray("retval");
							recommandHost.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject2=array.optJSONObject(i);
								Host host=new Host();
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
								recommandHost.add(host);
							}
						if(recommandHost.size()>0){
//							drawable.stop();
							ivVideoLoad.setVisibility(View.GONE);
							videoRecomd=findViewById(R.id.video_recomand_layout);
							videoRecomd.setVisibility(View.VISIBLE);
							gvRecomd=(GridView) videoRecomd.findViewById(R.id.gridView1);
							RecommandAdapter recommandAdapter=new RecommandAdapter(ChatRoomActivity.this, recommandHost,videoRecomd);
							gvRecomd.setAdapter(recommandAdapter);
						}
						}else{
							String msg=jsonObject.optString("msg");
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
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
			
		});
	}

	/*
	 * 接收到礼物的时候 显示礼物到屏幕上
	 */
	protected void showGiftOnScreen(String giftName,int count) {
		try {
			Random random=new Random();
			for (int i = 0; i < count; i++) {
				GifView gifView=new GifView(this);
				gifView.setGifImage(giftName);
				int whichContainer=random.nextInt(2);
				if(whichContainer==0){ //表示在第一个容器中
				int mGravityIndex=random.nextInt(giftOneGravity.length);
				FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.gravity=giftOneGravity[mGravityIndex];
				layoutParams.leftMargin=40;
				layoutParams.rightMargin=40;
				layoutParams.topMargin=40;
				layoutParams.bottomMargin=40;
				gifView.setLayoutParams(layoutParams);
				surfaceView.addView(gifView);
				Gift giftGif=new Gift();
				giftGif.setGifView(gifView);
				giftGif.setContainer(whichContainer+"");
				uiHandler.sendMessageDelayed(uiHandler.obtainMessage(SET_GIFT_GONE,giftGif), 6000);
			}else{
				int mGravityIndex=random.nextInt(giftTwoGravity.length);
				FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.gravity=giftTwoGravity[mGravityIndex];
				layoutParams.leftMargin=40;
				layoutParams.rightMargin=40;
				layoutParams.topMargin=40;
				layoutParams.bottomMargin=40;
				gifView.setLayoutParams(layoutParams);
				GiftContainerTwo.addView(gifView);
				Gift giftGif=new Gift();
				giftGif.setGifView(gifView);
				giftGif.setContainer(whichContainer+"");
				uiHandler.sendMessageDelayed(uiHandler.obtainMessage(SET_GIFT_GONE,giftGif), 6000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

	private void getImPressObject() {
		final File targetFile=Utils.getLevelJsonFile(getApplicationContext(),"impress.json");
		if(targetFile!=null){
			if(targetFile.length()>0){
				try {
					FileInputStream in=new FileInputStream(targetFile);
					String jsonString=Utils.getLineString(in);
					jsonImpressObject=new JSONObject(jsonString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if((System.currentTimeMillis()-targetFile.lastModified()>3*24*60*60*1000)){
					downloadJsonFile(targetFile,AppConstants.GET_IMPRESS_FILE);
				}
			}else{
				downloadJsonFile(targetFile,AppConstants.GET_IMPRESS_FILE);
			}
		}
	}
	
	private void getJsonObject() {
		final File targetFile=Utils.getLevelJsonFile(getApplicationContext(),"level.json");
		if(targetFile!=null){
			if(targetFile.length()>0){
				try {
					FileInputStream in=new FileInputStream(targetFile);
					String jsonString=Utils.getLineString(in);
					JSONObject jsonObject=new JSONObject(jsonString);
					JSONArray array=jsonObject.optJSONArray("wealth");
					Long[] wealth=new Long[array.length()]; //用户获得财富等级范围
					for (int i = 0; i < array.length(); i++) {
						wealth[i]=array.optLong(i);
					}
					JSONArray arrayLevel=jsonObject.optJSONArray("live");
					Long[] level=new Long[arrayLevel.length()]; //主播等级判断
					for (int i = 0; i < level.length; i++) {
						level[i]=arrayLevel.optLong(i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if((System.currentTimeMillis()-targetFile.lastModified()>7*24*60*60*1000)){
					downloadJsonFile(targetFile,AppConstants.GET_JSON_FILE);
				}
			}else{
				downloadJsonFile(targetFile,AppConstants.GET_JSON_FILE);
			}
		}
		
		
	}
	
	public void downloadJsonFile(final File targetFile, final String Url){
		//开启线程下载判断用户等级文件
		new Thread(new Runnable() {
			FileOutputStream fileOutputStream;
			InputStream inputStream;
			@Override
			public void run() {
				try {
					URL url=new URL(Url);
					HttpURLConnection connection=(HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(7*10000);
					fileOutputStream=new FileOutputStream(targetFile);
					if(connection.getResponseCode()==200){
						inputStream=connection.getInputStream();
						byte[] buff=new byte[1024];
						int length=0;
						while((length=inputStream.read(buff))!=-1){
							fileOutputStream.write(buff, 0, length);
						}
						
						if(Url.indexOf("impress.json")!=-1){
							FileInputStream in=new FileInputStream(targetFile);
							String jsonString=Utils.getLineString(in);
							jsonImpressObject=new JSONObject(jsonString);
						}
						
						if(Url.indexOf("level.json")!=-1){
//							FileInputStream in=new FileInputStream(targetFile);
//							String jsonString=Utils.getLineString(in);
//							JSONObject jsonObject=new JSONObject(jsonString);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(fileOutputStream!=null){
						try {
							fileOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if(inputStream!=null){
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
	
	public String getFlyMessage(String flyNickName,String flyMsg){
		String source="<div align=\"center\">"+"<font color=\"black\" size=\"40\">&nbsp;&nbsp;"+flyNickName+":"+"</font><font color=\"red\" size=\"40\"> "+"&nbsp;&nbsp;"+flyMsg+"</div>";
		return source;
	}
	
	/*
	 * 本地保存看过的主播列表
	 */
	public void saveHostSee(){
		ObjectInputStream inputStream=null;
		ObjectOutputStream objectOutputStream = null;
		File file =new File(this.getFilesDir(),AppConstants.HOST_HAS_SEEN);
		if(file.exists() && file.isDirectory())
			file.delete();
		try {
			if (file.exists()) {
				inputStream=new ObjectInputStream(openFileInput(AppConstants.HOST_HAS_SEEN));
				// ArrayList<Father> data=(ArrayList<Father>) inputStream.readObject();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				objectOutputStream=new ObjectOutputStream(openFileOutput(AppConstants.HOST_HAS_SEEN, MODE_PRIVATE));
				ArrayList<Father> datas=new ArrayList<Father>();
				datas.add(mHost);
				objectOutputStream.writeObject(datas);
				objectOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}finally{
				try {
				if(objectOutputStream!=null){
						objectOutputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(inputStream!=null){
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	class ListTouchListener implements View.OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX=event.getX();
				break;

			case MotionEvent.ACTION_UP:
				mUpx=event.getX();
				if(Math.abs(mUpx-mDownX)<mDistanx){
					try {
						if(getCurrentFocus()!=null){
							mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bottomMenuView.setVisibility(View.GONE);
					emotionView.setVisibility(View.GONE);
					chatView.setVisibility(View.GONE);
				}
				break;
			}
			return false;
		}
		
	}
	
	
	
	/**
	 * 获取房间内所有用户列表
	 */
	protected void doGetUserListTask() {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("rid", mHost.getRoomId());
		asyncHttpClient.post(AppConstants.GET_ALL_ROOM_USER, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				//Log.e("test","-------content="+content);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							JSONArray array=jsonObject2.optJSONArray("userlist");
							mAllUserData.clear();
							mManagerUser.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj=array.getJSONObject(i);
								Father father=new Father();
								//Log.e("testtest"," snickname="+obj.optString("nickname")+",onSuccess>>>"+obj.optString("avatar"));
								father.setAvatar(obj.optString("avatar"));
								father.setCredit(obj.optString("credit"));
								father.setNickName(obj.optString("nickname"));
								father.setPlatform(obj.optInt("platform"));
								father.setUid(obj.optString("uid"));
								father.setUserType(obj.optString("usertype"));
								father.setVipType(obj.optString("viptype"));
								father.setWeath(obj.optString("wealth"));
								father.setDetail(obj.toString());
								mAllUserData.add(father);
							}
							
							JSONArray array2=jsonObject2.optJSONArray("managerlist");
							for (int i = 0; i < array2.length(); i++) {
								JSONObject jsonObject3=array2.getJSONObject(i);
								Father father=new Father();
								father.setUid(jsonObject3.optString("uid"));
								father.setNickName(jsonObject3.optString("nickname"));
								mManagerUser.add(father);
							}
							
							new Thread(new UserSort()).start();
							
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
						
					} catch (JSONException e) {
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
	
	/**
	 * 显示所用用户列表的适配器
	 * @author mao
	 *
	 */
	
	
	class UserAdapter extends BaseAdapter{
		
		private Bitmap defaultBitmap;
		public boolean[] visibleStates;
		private AsyncImageLoader asyncImageLoader;
		
		public void setVisibleState(){
			visibleStates=new boolean[mUserDatas.size()];
		}
		
		
		public UserAdapter() {
			asyncImageLoader = new AsyncImageLoader();

			defaultBitmap = Utils.toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_logo));
		}


		@Override
		public int getCount() {
			return mUserDatas.size();
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
			 final UserViewHolder userViewHolder;
			Father father=mUserDatas.get(position);
			//Log.e("test","father nick="+father.getNickName()+",url="+father.getAvatar());
			if(convertView==null){
				userViewHolder=new UserViewHolder();
				convertView=getLayoutInflater().inflate(R.layout.all_user_list_item, null);
				userViewHolder.ivHead=(ImageView) convertView.findViewById(R.id.iv_header);
				userViewHolder.ivLevel=(ImageView) convertView.findViewById(R.id.iv_user_level);
				userViewHolder.tvName=(TextView) convertView.findViewById(R.id.user_nick_name);
				userViewHolder.subView=convertView.findViewById(R.id.room_user_menu);
				userViewHolder.viewSendGift=convertView.findViewById(R.id.view_send_gift);
				userViewHolder.viewChat=convertView.findViewById(R.id.view_chat);
				userViewHolder.viewShut=convertView.findViewById(R.id.view_shut);
				userViewHolder.viewBack=convertView.findViewById(R.id.view_back);
				userViewHolder.viewKickOut=convertView.findViewById(R.id.view_kick_out);
				userViewHolder.ivOs=(ImageView) convertView.findViewById(R.id.room_user_tag);
				userViewHolder.vipLevel=(ImageView) convertView.findViewById(R.id.iv_vip);
				userViewHolder.ivOperating=(ImageView) convertView.findViewById(R.id.iv_user_operating);
				convertView.setTag(userViewHolder);
			}else{
				userViewHolder=(UserViewHolder) convertView.getTag();
			}
				userViewHolder.ivOperating.setVisibility(View.VISIBLE);
				if(father.getUserType().equals("3")){
					userViewHolder.ivOperating.setImageResource(R.drawable.operating);
				}else if(father.getUserType().equals("2")){
					userViewHolder.ivOperating.setImageResource(R.drawable.operating);
				}else{
					userViewHolder.ivOperating.setVisibility(View.GONE);
				}
			if(visibleStates[position]){
				userViewHolder.subView.setVisibility(View.VISIBLE);
			}else{
				userViewHolder.subView.setVisibility(View.GONE);
			}
			userViewHolder.ivOs.setVisibility(View.VISIBLE);
			if(father.getPlatform()==1){
				userViewHolder.ivOs.setImageResource(R.drawable.room_user_icon_android);
			}else if(father.getPlatform()==2){
				userViewHolder.ivOs.setImageResource(R.drawable.room_user_mobile);
			}else{
				userViewHolder.ivOs.setVisibility(View.GONE);
			}
			userViewHolder.vipLevel.setVisibility(View.VISIBLE);
			if(Utils.isNotEmptyString(father.getVipType())){
				if(father.getVipType().equals("800002")){
					userViewHolder.vipLevel.setImageResource(R.drawable.vip_two);
				}else if(father.getVipType().equals("800001")){
					userViewHolder.vipLevel.setImageResource(R.drawable.vip_one);
				}else if(father.getVipType().equals("800003")){
					userViewHolder.vipLevel.setImageResource(R.drawable.vip_three);
				}else{
					userViewHolder.vipLevel.setVisibility(View.GONE);
				}
			}else{
				userViewHolder.vipLevel.setVisibility(View.GONE);
			}
			Bitmap map=asyncImageLoader.loadBitmap(father.getAvatar(), new AsyncImageLoader.ImageCallback() {
				
				@Override
				public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
					userViewHolder.ivHead.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
				}
			});
			if(map==null){
				if(defaultBitmap == null)
					defaultBitmap = Utils.toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
				userViewHolder.ivHead.setImageBitmap(defaultBitmap);
			}else{
				userViewHolder.ivHead.setImageBitmap(Utils.toRoundBitmap(map));
			}
//			finalBitmap.display(userViewHolder.ivHead, father.getAvatar(), loadMap);
		/*ImageLoader.getInstance().displayImage(father.getAvatar(), userViewHolder.ivHead, new ImageLoadingListener(){

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					((ImageView)view).setImageBitmap(defaultBitmap);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
					((ImageView)view).setImageBitmap(Utils.toRoundBitmap(loadedImage));
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {

					
				}
			});*/
			if(Utils.isNotEmptyString(father.getUserType())){
				if(father.getUserType().equals("1")){
					if(father.getUid().equals(mHost.getUid())){
						Utils.setHostLevel(father.getCredit(), userViewHolder.ivLevel);
					}else{
						Utils.setUserLevel(father.getWeath(), userViewHolder.ivLevel);
					}
				}else{
					Utils.setUserLevel(father.getWeath(), userViewHolder.ivLevel);
				}
			}
			userViewHolder.tvName.setText(father.getNickName());
			setRoomUserMenuListner(userViewHolder.viewBack,father);
			setRoomUserMenuListner(userViewHolder.viewChat,father);
			setRoomUserMenuListner(userViewHolder.viewKickOut,father);
			setRoomUserMenuListner(userViewHolder.viewSendGift,father);
			setRoomUserMenuListner(userViewHolder.viewShut,father);
			return convertView;
		}
		
		class UserViewHolder {
			public ImageView ivHead;//用户头像
			public ImageView ivLevel;//用户等级
			public ImageView ivOs;//用户所属平台
			public TextView tvName; //用户昵称
			public View subView; //点击之后显示的子菜单
			public View viewSendGift;//赠送礼物 
			public View viewChat;//开始聊天 
			public View viewShut;//禁言 
			public View viewBack;//恢复发言
			public View viewKickOut;//剔出30分钟
			public ImageView vipLevel;
			public ImageView ivOperating;//运营和巡管
		}
	}
	
	
	/**
	 * 点击用户显示用户子列表之后的功能菜单
	 * @param view
	 */
//	dddddd
	public void setRoomUserMenuListner(View view,final Father father){
		
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.view_send_gift: //送礼
					setPublic();
					showPopGift();
					if(!mToGiftData.contains(father)&&!(father.getUid().equals(mHost.getUid()))){
						mToGiftData.add(father);
					}
					if("0".equals(father.getUid())){
						toUserId=Integer.valueOf(mHost.getUid());
					}else{
						toUserId=Integer.valueOf(father.getUid());
					}
					tvTo.setText(father.getNickName());
					break;

				case R.id.view_chat: //聊天
					doChatWith(father);
					break;
				case R.id.view_shut://禁言
					if(mUser!=null){
						if("2".equals(mUser.getUserType())||"3".equals(mUser.getUserType())){
							doShutTask("10",father.getUid(),father.getNickName());
						}else{
							Father fathers=new Father();
							fathers.setUid(mUser.getUid());
							if(mManagerUser.contains(fathers)){
								if(!Utils.isVip(father.getVipType())){
									doShutTask("10",father.getUid(),father.getNickName());
								}else{
									Utils.MakeToast(getApplicationContext(), "不能禁言VIP用户");
								}
							}else{
								Utils.MakeToast(getApplicationContext(), "只有管理员才可以禁言");
							}
						}
					}
					break;
					
				case R.id.view_back://恢复发言
					if(mUser!=null){
						if("2".equals(mUser.getUserType())||"3".equals(mUser.getUserType())){
							doShutTask("11",father.getUid(),father.getNickName());
						}else{
							Father fathers=new Father();
							fathers.setUid(mUser.getUid());
							if(mManagerUser.contains(fathers)){
								doShutTask("11",father.getUid(),father.getNickName());
							}else{
								Utils.MakeToast(getApplicationContext(), "权限不够");
							}
						}
					}
					break;
					
				case R.id.view_kick_out://踢出房间
					if(mUser!=null){
						if("2".equals(mUser.getUserType())||"3".equals(mUser.getUserType())){
							doShutTask("9",father.getUid(),father.getNickName());
						}else{
							Father fathers=new Father();
							fathers.setUid(mUser.getUid());
							if(mManagerUser.contains(fathers)){
								if(!Utils.isVip(father.getVipType())){
									if(father.getUserType().equals("2")||father.getUserType().equals("3")){
										Utils.MakeToast(getApplicationContext(), "不能踢官方运营和巡管");
										return ;
									}
									doShutTask("9",father.getUid(),father.getNickName());
								}else{
									Utils.MakeToast(getApplicationContext(), "不能踢VIP用户");
								}
							}else{
								Utils.MakeToast(getApplicationContext(), "只有管理员才可以踢人");
							}
						}
					}
				break;
				
				}
			}
		});
	}
	
	
	public void doChatWith(Father father){
		setPublic();
//		mInputManager.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN); 
//		mInputManager.showSoftInput(chatInput, 0); 
		chatView.setVisibility(View.VISIBLE);
		mTvChatTo.setText(father.getNickName());
		if(!mToData.contains(father)&&!(father.getUid().equals(mHost.getUid()))){
			mToData.add(father);
		}
		try {
			receiverObj=new JSONObject(father.getDetail());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户    权限操作 操作类型 kickuser 表示踢出用户, banuser 表示禁言用户, replyuserchat表⽰示
		恢复用户发言.
	 */
	protected void doShutTask(String type,String toUid,String toNickName) {
		if(mUser!=null){
//			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
//			RequestParams params=new RequestParams();
//			params.put("uid", mUser.getUid());
//			params.put("token", mUser.getToken());
//			params.put("rid", mHost.getRoomId());
//			params.put("type", type);
//			params.put("touid", toUid);
//			params.put("tonickname", toNickName);
//			asyncHttpClient.post(AppConstants.USER_CONTROL, params, new AsyncHttpResponseHandler(){
//				
//			});
//		sendMs	getData(mUser.getUid(), toUid, mHost.getRoomId());
		sendMsgToServer(type, getData(mUser.getUid(), toUid, mHost.getRoomId()));
			
		}else{
			Utils.showLoginDialog(ChatRoomActivity.this, true);
		}
	}
	//踢人  禁言  恢复禁言
	
	public JSONObject getData(String uid, String touid, String rid) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("uid", uid);
			jo.put("touid", touid);
			jo.put("rid", rid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo;
	}

	/**
	 * 设置消息的发送时间
	 * @return
	 */
	public String setMessageTime(){
		Calendar calendar=Calendar.getInstance();
		String minute=String.valueOf(calendar.get(Calendar.MINUTE));
		if(minute.equals("0")){
			minute="00"; //整点的时候
		}
		if(minute.length()==1){
			minute="0"+minute;
		}
		String date=calendar.get(Calendar.HOUR_OF_DAY)+":"+minute;
		return date;
	}
	
	/**
	 * 来判断登录的用户是否关注过主播
	 * @return
	 */
	private void getHostAtt() {
		if(mUser!=null){
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		asyncHttpClient.post(AppConstants.GET_ALL_ATT_LIST, params,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
						JSONObject object=jsonObject.optJSONObject("retval");
						JSONArray array=object.optJSONArray("myfollowers");
						hostAtts.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2=array.getJSONObject(i);
							Host host=new Host();
							host.setUid(jsonObject2.optString("followuid")); //主播自己的ID
							host.setHostAttId(jsonObject2.optString("id"));
							hostAtts.add(host);
						}
						for (int i = 0; i < hostAtts.size(); i++) {
							Host host=hostAtts.get(i);
							if(host.getUid().equals(mHost.getUid())){
								mHasAtt=true;
								break;
							}else{
								mHasAtt=false;
							}
						}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
//				NLog.e("test", "mHasAtt=="+mHasAtt);
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		});
		}
	}
	
	//设置公聊私聊面板的点击监听事件
	private void setListener(View view){
		view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {

				case R.id.room_tab_pub_txt://公聊
					changeBg(R.id.room_tab_pub_txt);
					break;
					
				case R.id.room_tab_private_txt://私聊
					changeBg(R.id.room_tab_private_txt);
					break;
					
				case R.id.room_tab_viewer_txt://观众
					changeBg(R.id.room_tab_viewer_txt);
					break;
					
				case R.id.room_tab_viewer_num_txt://观众数量
					changeBg(R.id.room_tab_viewer_num_txt);
					break;
					
				case R.id.room_tab_fans://观众数量
					changeBg(R.id.room_tab_fans);
					break;
			}
		}
	});
		}

	
	private void changeBg(int id){
		for (int i = 0; i < text.length; i++) {
			if (id==text[i]) {
				selectViews[i].setTextColor(getResources().getColor(R.color.chat_bg_color));
			}else {
				selectViews[i].setTextColor(getResources().getColor(R.color.chat_bg_color_unselected));
			}
		}
	}
	private void initView() {
		roomuserbottom = findViewById(R.id.room_user_bottom);
		roomfans_one = (ImageView) findViewById(R.id.room_fans_oneid);
		roomfans_two =(ImageView) findViewById(R.id.room_fans_twoid);
		roomfans_three=(ImageView) findViewById(R.id.room_fans_threeid);
		imageLoader = new AsyncImageLoader();
		tv_host_love=(LinearLayout) findViewById(R.id.tv_host_love);
		tvHostYuanBaoCount=(TextView) findViewById(R.id.tv_host_receiver_count);
		ivVideoLoad=findViewById(R.id.iv_video_load);
		layoutYuan=findViewById(R.id.room_tab_yuanbao);
//		mAllUserFoot=(TextView) getLayoutInflater().inflate(R.layout.user_num_foot_layout, null);
		privateNoLogo=findViewById(R.id.layout_no_logo);
		tvPrivateTag=(TextView) findViewById(R.id.tv_no_logo);
		tvPrivateTag.setMovementMethod(LinkMovementMethod.getInstance());
		mlvRoomFans= (ListView) findViewById(R.id.lv_room_fans);
		tvAudiceNum=(TextView) findViewById(R.id.room_tab_viewer_num_txt);
		layoutFun=findViewById(R.id.layout_fan);
		privateCb=(CheckBox) findViewById(R.id.private_checkbox);
		tvYuanBaoCount=(TextView) findViewById(R.id.tv_yuanbao_count);
		bottomMenuView=findViewById(R.id.layout_bottom_menu);
		mInputManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mTvChatTo=(TextView) findViewById(R.id.chat_to);
		mScrollTextView=(ScrollTextView) findViewById(R.id.gift_marquee);
		ParentView=(RelativeLayout) findViewById(R.id.room_root);
		surfaceView=(FrameLayout) findViewById(R.id.surfaceview_layout);
		GiftContainerTwo=(FrameLayout) findViewById(R.id.layout_frame_two);
		
//		tvPub=(ImageView) findViewById(R.id.room_tab_pub_sel_arr);
//		tvPrivate=(ImageView) findViewById(R.id.room_tab_private_sel_arr);
//		tvAudice=(ImageView) findViewById(R.id.room_tab_viewer_sel_arr);
//		tvFans=(ImageView) findViewById(R.id.room_tab_fan_sel_arr);
//		
//		pubchat = (TextView) findViewById(R.id.room_tab_pub_txt);
//		prichat = (TextView) findViewById(R.id.room_tab_private_txt);
//		audience = (TextView) findViewById(R.id.room_tab_viewer_txt);
//		fans = (TextView) findViewById(R.id.room_tab_fans);
		for (int i = 0; i < text.length; i++) {
			selectViews[i]=(TextView) findViewById(text[i]);
//			setListener(selectViews[i]);
		}
		mPublicListView=(ListView) findViewById(R.id.chat_content_layout_public);
//		mPrivateListView=(ListView) findViewById(R.id.chat_content_layout_private);
		privateDot=(ImageView) findViewById(R.id.room_tab_private_msg_dot);
		privateDot.startAnimation(AnimationUtils.loadAnimation(this, R.anim.light));
		chatView=findViewById(R.id.chat_bar_layout);
		chatInput=(EditText) findViewById(R.id.chat_edit);
		emotionView=findViewById(R.id.layout_emotion);
		emotionIndicator=(CirclePageIndicator) findViewById(R.id.pager_indicator);
		mPagerEmotion=(ViewPager) findViewById(R.id.viewpager_emotion);
		faceWidth=(int) getResources().getDimension(R.dimen.face_width_height);
		Chatface=(int) getResources().getDimension(R.dimen.chat_panle_face_width_height);
		mRoomUserList=(PullListView) findViewById(R.id.room_user_layout);
		
		title = findViewById(R.id.room_back_layout);
		host_info = findViewById(R.id.room_host_info);
//		roomhostname =(TextView)findViewById(R.id.room_host_name);
//		room_back_btn = (ImageView)findViewById(R.id.room_back_btn);
		roomback_layoutView = findViewById(R.id.room_back_layout);
		roomhostname =(TextView) roomback_layoutView.findViewById(R.id.room_host_name);
		room_back_btn = (ImageView) roomback_layoutView.findViewById(R.id.room_back_btn);
		room_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		footView=getLayoutInflater().inflate(R.layout.list_bottom, null);
		footView.setVisibility(View.GONE);
		mRoomUserList.addFooterView(footView);
		mRoomUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mUserAdapter.visibleStates.length>0){
					if(mUserAdapter.visibleStates[position]){
						mUserAdapter.visibleStates[position]=false;
					}else{
						mUserAdapter.visibleStates[position]=true;;
					}
					for (int i = 0; i < mUserAdapter.visibleStates.length; i++) {
						if(i!=position){
							mUserAdapter.visibleStates[i]=false;
						}
					}
					mUserAdapter.notifyDataSetChanged();
					parent.setSelection(position);
				}
			}
		});
		
		//添加自动分页加载
		mRoomUserList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState==SCROLL_STATE_IDLE&&lastVisbileIndex==view.getAdapter().getCount()-1){
					footView.setVisibility(View.VISIBLE);
					uiHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
						
								loadMoreUserData();
							
							
						}
					}, 200);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastVisbileIndex=firstVisibleItem+visibleItemCount-1;
				if(totalItemCount==mUserDatas.size()){
					footView.setVisibility(View.GONE);
				}
				
			}
		});
		
		layoutBottom=findViewById(R.id.layout_bottom);
		loadMap=BitmapFactory.decodeResource(getResources(), R.drawable.kk_default_avatar_big_men);
		privateCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){ //切换到私聊
					setPrivate();
				}else{//切换到公聊
					setPublic();
					findViewById(R.id.room_tab_layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.room_chat_one_bg));
				}
				chatView.setVisibility(View.VISIBLE);
			}
		});
		
		for (int i = 0; i < fanId.length; i++) {
			fanViews[i]=findViewById(fanId[i]);
		}
	}

	/**
	 * 加载更多用户
	 */
	private void loadMoreUserData() {
		int j=mUserDatas.size()+20;
		int m =mManagerUser.size()+20;
		if(mUserDatas.size()>=mAllUserData.size()){
			footView.setVisibility(View.GONE);
		}else{
			if (loadflag==0) {
				for (int i = mUserDatas.size(); i<j&&i<mAllUserData.size(); i++) {
					mUserDatas.add(mAllUserData.get(i));
				}
			}else {
				for (int i = mUserDatas.size(); i < m&&i<mManagerUser.size(); i++) {
					mUserDatas.add(mManagerUser.get(i));
				}
			}
			
			
			
			footView.setVisibility(View.GONE);
			mUserAdapter.setVisibleState();
			mUserAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 用户点击了返回
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doReleaseWork();
	}
	
	private void doReleaseWork() {
		if(playerViewController!=null){
			playerViewController.stopPlay();
		}
		File file =new File(this.getApplication().getFilesDir(),AppConstants.HOST_HAS_SEEN);
		//file.deleteOnExit();
		if(!file.getParentFile().exists()){			
			file.mkdirs();
		}
		ObjectOutputStream objectOutputStream=null;
		ObjectInputStream objHost=null;
		try {
			ArrayList<Father> hostAtt = null;
			if(file.exists()){
				FileInputStream fileInputStream=openFileInput(AppConstants.HOST_HAS_SEEN);
				objHost=new ObjectInputStream(fileInputStream);
				hostAtt=(ArrayList<Father>)objHost.readObject();
				if(!hostAtt.contains(mHost)){
					hostAtt.add(0, mHost);
				}
			}else{
				hostAtt = new ArrayList<Father>();
				hostAtt.add(0, mHost);

			}
			if(hostAtt.size()>10){
				 ArrayList<Father> newData=new ArrayList<Father>(hostAtt.subList(0, 9)) ;
				 objectOutputStream=new ObjectOutputStream(openFileOutput(AppConstants.HOST_HAS_SEEN, MODE_PRIVATE));
				 objectOutputStream.writeObject(newData);
				 objectOutputStream.flush();

			}else{
				objectOutputStream=new ObjectOutputStream(openFileOutput(AppConstants.HOST_HAS_SEEN, MODE_PRIVATE));
				objectOutputStream.writeObject(hostAtt);
				objectOutputStream.flush();
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(objectOutputStream!=null){
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(objHost!=null){
				try {
					objHost.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/*
	 * 得到聊天服务器的地址
	 */
	private void getChatServerAddress() {
		File file =new File(ChatRoomActivity.this.getApplication().getFilesDir(),AppConstants.SERVER_IP_CACHE);//AppConstants.SERVER_RANK_CACHE
		if(file.exists() && file.isFile() && (System.currentTimeMillis()-file.lastModified()<1000*60*60*24*3 || !Utils.isNetwokAvailable(ChatRoomActivity.this))){
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String data="";
				String line = null;
				while(( line = br.readLine())!=null){
					data+=line;
				}
				br.close();
				//Log.v("ChatRoomActivity"," address ip has data");
				ParseIp(data);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}else if(Utils.isNetwokAvailable(ChatRoomActivity.this)){
			//Log.v("test","file not exist");
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			asyncHttpClient.post(AppConstants.GET_CHAT_SEVER_ADDRESS, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					//Log.v("serverIp", content);
					try {
						FileOutputStream fileOutputStream=openFileOutput(AppConstants.SERVER_IP_CACHE, MODE_PRIVATE);
						fileOutputStream.write(content.getBytes());
						fileOutputStream.flush();
						fileOutputStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					ParseIp(content);
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Utils.MakeToast(getApplicationContext(), "获取聊天服务器地址失败");
					//getChatServerAddress();
				}
			});
		}
	}
	
	public void ParseIp(String content){
		if(content!=null){
			try {
				JSONObject jsonObject=new JSONObject(content);
				boolean isSuccess=jsonObject.optBoolean("success");
				if(isSuccess==true){
					JSONObject jsonObject2=jsonObject.optJSONObject("retval");
					mChatServerAddress=jsonObject2.optString("ChatIp"); //取得聊天服务器地址
					//取跑道信息数据
					tunnels.clear();
				JSONArray array=jsonObject2.optJSONArray("richCostInfo");
				for (int i = 0; i < array.length(); i++) {
					RunTunnel runTunnel=new RunTunnel();
					JSONObject jsonObject3=array.optJSONObject(i);
					runTunnel.setCount(jsonObject3.optString("count"));
					runTunnel.setTime(jsonObject3.optString("time"));
					runTunnel.setGid(jsonObject3.optString("gid"));
					runTunnel.setGiftname(jsonObject3.optString("giftname"));
					runTunnel.setNickname(jsonObject3.optString("nickname"));
					runTunnel.setTonickname(jsonObject3.optString("tonickname"));
					runTunnel.setTousernum(jsonObject3.optString("tousernum"));
					runTunnel.setUsernum(jsonObject3.optString("usernum"));
					tunnels.add(runTunnel);
				}
				if(tunnels.size()>0){
					setRuningWayData();
				}
				int version=jsonObject2.optInt("version");
				SharedPreferences preferences=getSharedPreferences("gift_version",ChatRoomActivity.MODE_PRIVATE);
				int localVersion=preferences.getInt("version", 0);
				if(version>localVersion){
					mNeedUpdate=true;
				}else{
					mNeedUpdate=false;
				}
				preferences.edit().putInt("version", version).commit();
				//取组合礼物的模板
				String tempAddress=jsonObject2.optString("publicScriptHttp");
				Utils.downloadFile(haoHuaDir, tempAddress,mNeedUpdate);
				JSONArray groupArray=jsonObject2.optJSONArray("templatesHttp");
				for (int i = 0; i < groupArray.length(); i++) {
					JSONObject jsonObject3=groupArray.getJSONObject(i);
					String address=jsonObject3.optString("url");
					Utils.downloadFile(haoHuaDir, address,mNeedUpdate);
				}
				
				//获取车子信息
				JSONArray arrayCar=jsonObject2.optJSONArray("propsList");
				mCarData.clear();
				for (int i = 0; i < arrayCar.length(); i++) {
					JSONObject object=arrayCar.getJSONObject(i);
					DriverCar driverCar=new DriverCar();
					driverCar.setCarId(object.optString("id"));
					driverCar.setCarName(object.optString("name"));
					driverCar.setCarSrc(object.optString("src"));
					Utils.downloadFile(giftDir, driverCar.getCarSrc(),mNeedUpdate);
					mCarData.add(driverCar);
				}
				
				//获取财神爷的信息
				JSONObject arrayWealth=jsonObject2.optJSONObject("effect");
				if(arrayWealth!=null){
					String wealthUrl=arrayWealth.optString("url");
					Utils.downloadFile(haoHuaDir, wealthUrl,mNeedUpdate);
				}
				
			}else{
				String msg=jsonObject.optString("msg");
				Utils.MakeToast(getApplicationContext(), msg);
			}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 设置跑道
	 * @return
	 */
	public String setMarqueSource(String time,String sendNickName,String sendNumber,String toNickName,String toNumber,String giftName,String giftId,String giftNumber){
		String source="<div align=\"center\">"+"<font color=\"#ebeaea\" size=\"20\">&nbsp;&nbsp;"+time+"</font><font color=\"#9d409d\" size=\"20\"> "+"&nbsp;"+sendNickName+"</font><font color=\"#ff0084\" size=\"20\">"+"("+sendNumber+")"+"</font><font color=\"#ebeaea\" size=\"20\">&nbsp;&nbsp;"+"送给 "+"</font>" +
				 "<font color=\"#9D409D\" size=\"20\">&nbsp;"+toNickName+"</font>"+"<font color=\"#ff0084\" size=\"20\">"+"("+toNumber+")"+"</font>"+"<font color=\"#ebeaea\" size=\"20\">&nbsp;&nbsp;"+giftNumber+"个"+"<img src='"+giftId+"'/>"+giftName+"</a></div>";
		return source;
	}
	
	/**
	 * 设置跑道消息
	 */
	public void setRuningWayData(){
		mScrollTextView.setVisibility(0);
		mScrollTextView.setText("");
		mScrollTextView.pauseScroll();
		for (int i = 0; i < tunnels.size(); i++) {
			RunTunnel tunnel=tunnels.get(i);
			String source=setMarqueSource(tunnel.getTime(), tunnel.getNickname(), tunnel.getUsernum(), tunnel.getTonickname(), tunnel.getTousernum(), tunnel.getGiftname(),tunnel.getGid(), tunnel.getCount());
			Spanned span=Html.fromHtml(source, new Html.ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					Drawable drawable=null;
					if(giftDir!=null){
						File image=new File(giftDir, "mgift"+source+".png");
	    				if(image.exists()){
		    				drawable=Drawable.createFromPath(image.getAbsolutePath());
		    				//eyan add 20131126
		    				if(drawable != null)
		    					drawable.setBounds(0, 0, faceWidth, faceWidth);
		    				return drawable;
	    				}
					}
					return null;
				}
			}, null);
			mScrollTextView.append(span);
		}
		mScrollTextView.setDuration(15000*tunnels.size());
		mScrollTextView.startScroll();
	}
	
	

	
	/**
	 * 点击事件
	 * @param view
	 */
	public void onClick(View view){
		switch (view.getId()) {
//		case R.id.back_hall:
//			finish();
//			break;
		case R.id.room_user_normal_layout:
			loadflag =0;
			((ImageView)findViewById(R.id.room_user_normal)).setImageDrawable(getResources().getDrawable(R.drawable.room_user_user_select));
			((ImageView)findViewById(R.id.room_user_manager)).setImageDrawable(getResources().getDrawable(R.drawable.room_user_manager));
			if(mAllUserData.size()>20){
				mUserDatas=new ArrayList<Father>(mAllUserData.subList(0, 20));//首先取20个加载
			}else{
				mUserDatas=new ArrayList<Father>(mAllUserData);
			}
			mUserAdapter.notifyDataSetChanged();
			break;
		
		case R.id.room_user_manager_layout:
//			mRoomUserList.sets
			loadflag =1;
			((ImageView)findViewById(R.id.room_user_normal)).setImageDrawable(getResources().getDrawable(R.drawable.room_user_user));
			((ImageView)findViewById(R.id.room_user_manager)).setImageDrawable(getResources().getDrawable(R.drawable.room_user_manager_select));
			mUserDatas=new ArrayList<Father>(mManagerUser);
			mUserAdapter.notifyDataSetChanged();
			break;
			
		case R.id.layout_this_fan: //本场粉丝榜
//			changeFanBg(R.id.layout_this_fan);
			roomfans_one.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_oneselected));
			roomfans_two.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_twoimage));
			roomfans_three.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_threeimage));
			if(fansBean!=null){
				FansAdapter adapter=new FansAdapter(ChatRoomActivity.this, fansBean.getDayFan());
				mlvRoomFans.setAdapter(adapter);
			}
			break;
		case R.id.layout_month_fan://本月粉丝榜
//			changeFanBg(R.id.layout_month_fan);
			roomfans_one.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_oneimage));
			roomfans_two.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_twoselected));
			roomfans_three.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_threeimage));
			if(fansBean!=null){
				FansAdapter adapter=new FansAdapter(ChatRoomActivity.this, fansBean.getMonthFan());
				mlvRoomFans.setAdapter(adapter);
			}
			break;
			
		case  R.id.layout_super_fan://超级粉丝榜
//			changeFanBg(R.id.layout_super_fan);
			roomfans_one.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_oneimage));
			roomfans_two.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_twoimage));
			roomfans_three.setImageDrawable(getResources().getDrawable(R.drawable.room_fans_threeselected));
			if(fansBean!=null){
				FansAdapter adapter=new FansAdapter(ChatRoomActivity.this, fansBean.getSuperFan());
				mlvRoomFans.setAdapter(adapter);
			}
			break;
			
		case R.id.room_back_btn://返回按钮
			finish();
			if(playerViewController!=null)
			playerViewController.stopPlay();
			break;

		case R.id.room_tab_pub: //公聊面板
			findViewById(R.id.room_tab_layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.room_chat_one_bg));
//			changeBg(R.id.room_tab_pub_txt);
			setPublic();
			break;
			
		case R.id.room_tab_private://私聊面板
//			findViewById(R.id.room_tab_layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.room_chat_two_bg));
//			changeBg(R.id.room_tab_private_txt);
			setPrivate();
			break;
			
		case R.id.room_tab_viewer://观众面板
			roomuserbottom.setVisibility(0);
			findViewById(R.id.room_tab_layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.room_chat_three_bg));
			changeBg(R.id.room_tab_viewer_txt);
			if(GiftContainerTwo.getVisibility()==View.GONE){
				GiftContainerTwo.setVisibility(View.VISIBLE);
			}
			layoutFun.setVisibility(View.GONE);
			layoutBottom.setVisibility(View.GONE);
			layoutYuan.setVisibility(View.GONE);
			
			if (needUserSort) {
				needUserSort = false;
				new Thread(new UserSort()).start();
			}
			
//			UserSort();
//			Collections.sort(mAllUserData,new UserComparator());
			/*if(mAllUserData.size()>20){
				mUserDatas=new ArrayList<Father>(mAllUserData.subList(0, 20));//首先取20个加载
			}else{
				mUserDatas=new ArrayList<Father>(mAllUserData);
			}
			if(mUserAdapter==null){
				mUserAdapter=new UserAdapter();
				mRoomUserList.setAdapter(mUserAdapter);
			}else
				mUserAdapter.notifyDataSetChanged();
			mUserAdapter.setVisibleState();*/
			mPublicListView.setVisibility(View.GONE);
			mRoomUserList.setVisibility(View.VISIBLE);
			//mRoomUserList.setAdapter(mUserAdapter);
			break;
		
		case R.id.room_funny_rank: //粉丝吧榜
			findViewById(R.id.room_tab_layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.room_chat_four_bg));
			changeBg(R.id.room_tab_fans);
//			tvPub.setVisibility(View.GONE);
//			tvPrivate.setVisibility(View.GONE);
//			tvAudice.setVisibility(View.GONE);
//			tvFans.setVisibility(View.VISIBLE);
			doGetFanTask();
			break;
			
		case R.id.room_tab_money://元宝按钮
			doYunBaoTask();
			break;
			
			
		case R.id.room_tab_gift: //送礼按钮
			if(mShowGiftPop!=null){
				if(mShowGiftPop.isShowing()){
					mShowGiftPop.dismiss();
				}else{
					showPopGift();
					if(mUser!=null){
						tvBin.setText("九币:"+mUser.getUserCoin()); 
						tvPoint.setText("九点:"+mUser.getUserDian());
					}
				}
			}else{
				showPopGift();
			}
			break;
			
		case R.id.color_bar_btn: //彩条按钮
			showColorBarWindow();
			break;
			
		case R.id.chat_to: //选择对谁聊天
			showChatToPop();
			break;
			
		case R.id.plus_btn://点击加号之后的子菜单
			try {
				if(getCurrentFocus()!=null)
					mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			emotionView.setVisibility(View.GONE);
			if(mUser==null){
				Utils.showLoginDialog(this, true);
			}else{
				if(bottomMenuView.getVisibility()==View.VISIBLE){
					bottomMenuView.setVisibility(View.GONE);
				}else{
					bottomMenuView.setVisibility(View.VISIBLE);
				}
			}
			break;
			
		case R.id.emotion_btn: //表情菜单
			try {
				mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(mUser==null){
				Utils.showLoginDialog(this, true);
			}else{
			if(bottomMenuView.getVisibility()==View.VISIBLE){
				bottomMenuView.setVisibility(View.GONE);
			}
			if(emotionView.getVisibility()==View.VISIBLE){
				emotionView.setVisibility(View.GONE);
			}else{
				emotionView.setVisibility(View.VISIBLE);
				if(mPagerEmotion.getAdapter()==null){
					setEmotion();
				}
			}
			}
			break;
			
		case R.id.record_close: //语音按钮
			if(mUser==null){
				Utils.showLoginDialog(this, true);
			}else{
				showRecognitionDialog();
			}
			break;
			
		case R.id.send_btn: //发送按钮
			try {
				if(getCurrentFocus()!=null){
					mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			emotionView.setVisibility(View.GONE);
			if(mUser!=null){
				String inputMsg=chatInput.getText().toString().trim();
				if(publicMesg.size()>100){
					publicMesg.clear();
					//eyan add 20131126
				//	chatMessageAdapter.notifyDataSetChanged();
				}
				if(inputMsg.length()>0){
					String wealth=mUser.getWeath();
					//wealth="42000000";
					if(mIsPrivate){
						if(!isBeShutUp){
						if(Utils.isNotEmptyString(wealth)){
							if(Long.valueOf(wealth)>=42000){
								try {
									//私聊消息
									sendMessage(inputMsg,true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								Utils.MakeToast(getApplicationContext(), "三富以上才可以私聊");
							}
						}else{
							Utils.MakeToast(getApplicationContext(), "三富以上才可以私聊");
						}
						}else{
							Utils.MakeToast(getApplicationContext(), "你被禁言30分钟,剥夺话语权");
						}
					}else{
						if(Utils.isNotEmptyString(wealth)){
							if(!isBeShutUp){
							if(Long.valueOf(wealth)<=2000){
								if(inputMsg.length()<=15){
									try {
										//公聊消息
										sendMessage(inputMsg,false);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}else{
									Utils.MakeToast(getApplicationContext(), "一富以下不能超过15个字符");
								}
							}else{
								try {
									//公聊消息
									sendMessage(inputMsg,false);
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						}else{
							Utils.MakeToast(getApplicationContext(), "你被禁言30分钟,剥夺话语权");
						}
						}
					}
				}else{
					Utils.MakeToast(getApplicationContext(), "不能发送空消息");
				}
				
			}else{
				Utils.showLoginDialog(this, true);
			}
			
			break;
			
		case R.id.surfaceview://触摸视频界面
			if (firstshowhost) {
				showHostInfoPop();
				firstshowhost=false;
			}
			if (isShow) {
				startOutAnim();
				return;
			}
			startInAnim();
			break;
			
		
			
		case R.id.room_order_song: //点歌
			showOrderSongPop();
			break;
			
		case R.id.room_share: //分享
			OnekeyShare oks = new OnekeyShare();
			oks.setNotification(R.drawable.logo, "分享中...");
			oks.setTitle("69美女直播");
			oks.setTitleUrl("www.69xiu.com");
			oks.setText("快来看#69美女视频直播#,大家一起认识一下吧!");
			oks.setSilent(true);
			oks.setAddress("");
			oks.show(ChatRoomActivity.this);	
			break;
			
		case R.id.room_charge://充值
			Intent intentCharege=new Intent(this,ChargeActivity.class);
			startActivity(intentCharege);
			break;
			
		case R.id.iv_face_delete://删除输入内容
			chatInput.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);//模
			break;
		case R.id.chat_edit://输入框点击事件
//			mInputManager.showSoftInput(chatInput, 0);
			if(canEdit){
				if(mUser!=null){
					emotionView.setVisibility(View.GONE);
					bottomMenuView.setVisibility(View.GONE);
					chatView.setVisibility(View.VISIBLE);
				}else{
					Utils.showLoginDialog(this, true);
				}
			}else{
				Utils.MakeToast(ChatRoomActivity.this, "10秒后才能发言");
			}
			break;
		}
	}
	
	public void sendMessage(String inputMsg,boolean isPrivate){
		try {
			if(isPrivate){
				//私聊消息
				JSONObject object=new JSONObject();
				object.put("userinfo", parseUser());
				object.put("msg", inputMsg);
				object.put("uid", mUser.getUid());
				object.put("rid", roomid);
				JSONObject jo = new JSONObject();
				jo.put("uid",receiverObj.getString("uid"));
				jo.put("nickname",receiverObj.getString("nickname"));
				jo.put("usernum",receiverObj.getString("usernum"));
				//jo.put("viptype",receiverObj.getString("viptype"));
				//jo.put("wealth",receiverObj.getString("wealth"));
				jo.put("credit",receiverObj.getString("credit"));
				//jo.put("usertype",receiverObj.getString("usertype"));
				//jo.put("avatar",receiverObj.getString("avatar"));
				object.put("recived", jo);
				sendMsgToServer("5", object);
			}else{
				//公聊消息
				JSONObject object=new JSONObject();
				object.put("userinfo", parseUser());
				object.put("msg", inputMsg);
				object.put("uid", mUser.getUid());
				object.put("rid", roomid);
				if(receiverObj!=null && receiverObj.has("nickname")){
					JSONObject jo = new JSONObject();
					jo.put("uid",receiverObj.getString("uid"));
					jo.put("nickname",receiverObj.getString("nickname"));
					jo.put("usernum",receiverObj.getString("usernum"));
					//jo.put("viptype",receiverObj.getString("viptype"));
					//jo.put("wealth",receiverObj.getString("wealth"));
					jo.put("credit",receiverObj.getString("credit"));
					object.put("recived", jo);
				}else{
					object.put("recived", new JSONObject());
				}
				sendMsgToServer("4", object);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private int[]fansdrawable = new int[]{R.drawable.room_fans_oneimage,R.drawable.room_fans_twoimage,R.drawable.room_fans_threeimage};
//	private int[]fansdrawable_select = new int[]{R.drawable.room_fans_oneselected,R.drawable.room_fans_twoselected,R.drawable.room_fans_threeselected};
//	private int[]fansid = new int[]{R.id.room_fans_one};
//	private ImageView[] roomfansViews= new ImageView[]{R.id.room_};ds
	/**
	 * 点击粉丝榜的时候 改变背景
	 */
	public void changeFanBg(int id){
		for (int i = 0; i < fanId.length; i++) {
			if(id==fanId[i]){
//				fanViews[i].setBackgroundResource(R.drawable.room_fans_selected);
			}else{
//				fanViews[i].setBackgroundResource(R.drawable.room_funny);
			}
		}
	}
	
	
	/*
	 * 显示语音识别对话框
	 */
	private void showRecognitionDialog() {
		// 转写回调监听器.
		RecognizerDialogListener recognizeListener = new RecognizerDialogListener(){
				// 识别结果回调接口
					String text ="";
				@Override
				public void onResults(ArrayList<RecognizerResult> results, boolean arg1) {
					// 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加.
					for(int i = 0; i < results.size(); i++){
						text += results.get(i).text;
					}
					chatInput.setText(text);
				}
				// 会话结束回调接口.
				public void onEnd(SpeechError error) {
				// error为null表示会话成功，可在此处理text结果，error不为null，表示发生错误，对
				//话框停留在错误页面
				}
				};
		// 创建识别对话框,需传入正确appid
		RecognizerDialog isrDialog = new RecognizerDialog(this, "appid="+AppConstants.XUN_FEI);
		isrDialog.setEngine("sms",null,null);
		isrDialog.setListener(recognizeListener);
		isrDialog.show();
	}


	/**
	 * 获取粉丝榜
	 */
	private void doGetFanTask() {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mHost.getUid());
		params.put("rid",mHost.getRoomId());
		asyncHttpClient.post(AppConstants.GET_HOST_FAN, params,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				layoutFun.setVisibility(View.VISIBLE);
				GiftContainerTwo.setVisibility(View.GONE);
				layoutBottom.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							JSONArray array=jsonObject2.optJSONArray("fansday");
							fansBean=new FansBean();
							/**
							 * 本场数据榜
							 */
							ArrayList<Father> dayFan=new ArrayList<Father>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject3=array.getJSONObject(i);
								Father father=new Father();
								father.setTotal(jsonObject3.optString("total"));
								father.setAvatar(jsonObject3.optString("avatar"));
								father.setNickName(jsonObject3.optString("nickname"));
								father.setWeath(jsonObject3.optString("wealth"));
								dayFan.add(father);
							}
							fansBean.setDayFan(dayFan);
							
							/**
							 * 月榜数据
							 */
							ArrayList<Father> monthFan=new ArrayList<Father>();
							JSONArray array2=jsonObject2.optJSONArray("fansmonth");
							for (int i = 0; i < array2.length(); i++) {
								JSONObject jsonObject3=array2.getJSONObject(i);
								Father father=new Father();
								father.setTotal(jsonObject3.optString("total"));
								father.setAvatar(jsonObject3.optString("avatar"));
								father.setNickName(jsonObject3.optString("nickname"));
								father.setWeath(jsonObject3.optString("wealth"));
								monthFan.add(father);
							}
							fansBean.setMonthFan(monthFan);
							
							/**
							 * 超级榜
							 */
							
							ArrayList<Father> superFan=new ArrayList<Father>();
							JSONArray array3=jsonObject2.optJSONArray("fansall");
							for (int i = 0; i < array3.length(); i++) {
								JSONObject jsonObject3=array3.getJSONObject(i);
								Father father=new Father();
								father.setTotal(jsonObject3.optString("total"));
								father.setAvatar(jsonObject3.optString("avatar"));
								father.setNickName(jsonObject3.optString("nickname"));
								father.setWeath(jsonObject3.optString("wealth"));
								superFan.add(father);
							}
							fansBean.setSuperFan(superFan);
							FansAdapter adapter=new FansAdapter(ChatRoomActivity.this, fansBean.getDayFan());
							mlvRoomFans.setAdapter(adapter);
							
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Utils.MakeToast(getApplicationContext(), "数据格式异常");
					}
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
		});
	}


	/*
	 * 初始化礼物webView界面
	 */
	@SuppressLint("NewApi")
	public MyWebView getWebView() {
		 MyWebView mWebView=new MyWebView(ChatRoomActivity.this);
		mWebView.getSettings().setJavaScriptEnabled(true); 
		mWebView.getSettings().setLoadsImagesAutomatically(true);
		mWebView.setBackgroundColor(0);
		if(Build.VERSION.SDK_INT>=11){
			mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Log.v(TAG,"getWebView error");
				view.setVisibility(View.GONE);
			}
		});
		mWebView.clearCache(true); //清除缓存
		mWebView.clearHistory();//清除历史记录
		return mWebView;
	}
	
	/**
	 * 每隔五分钟执行该方法
	 * @author mao
	 *
	 */
	class YunBaoTask implements Runnable{
		@Override
		public void run() {
				if(mUser!=null){
					AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
					RequestParams params=new RequestParams();
					params.put("uid", mUser.getUid());
					params.put("rid", mHost.getRoomId());
					params.put("token", mUser.getToken());
					asyncHttpClient.post(AppConstants.GET_YUAN_BAO, params, new AsyncHttpResponseHandler(){
						@Override
						public void onStart() {
							super.onStart();
						}
						
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							if(content!=null){
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(content);
									boolean success=jsonObject.optBoolean("success");
									if(success==true){
										JSONObject jsonObject2=jsonObject.optJSONObject("retval");
										if(jsonObject2!=null){
											String yuanbaoCount=jsonObject2.optString("intIngot");
											tvYuanBaoCount.setText(yuanbaoCount);
											saveYuanbao(yuanbaoCount);
											uiHandler.postDelayed(baoTask, 5*1000*60);
										}
										
									}else{
										JSONObject jsonObject2=jsonObject.optJSONObject("retval");
										if(jsonObject2!=null){
											String yuanbaoCount=jsonObject2.optString("intIngot");
											tvYuanBaoCount.setText(yuanbaoCount);
										}
										uiHandler.removeCallbacks(baoTask);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
						
						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							Utils.MakeToast(getApplicationContext(), "连接超时");
						}
					});
				}
				
			}
			
		}
	
	private void saveYuanbao(String tvYuanBaoCount){
		getSharedPreferences(AppConstants.LOCAL_STORGE, MODE_PRIVATE).edit()
		.putString(AppConstants.LOCAL_YUANBAO_COUNT,tvYuanBaoCount)
		.commit();
	}
	/**
	 * 读取本场皇冠粉丝排行
	 * @author mao
	 *
	 */
	class TopFunTask implements Runnable{

		@Override
		public void run() {
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams  params=new RequestParams();
			params.put("uid", mHost.getUid());
			asyncHttpClient.post(AppConstants.GET_TOP_FUN, params,new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					if(content!=null){
						try {
							JSONObject jsonObject=new JSONObject(content);
							boolean success=jsonObject.optBoolean("success");
							if(success){
								JSONObject jsonObject2=jsonObject.optJSONObject("retval");
								if(jsonObject2!=null){
									ChatMessage chatMessage=new ChatMessage();
									chatMessage.setSendTime(setMessageTime());
									String nickName=jsonObject2.optString("nickname");
									chatMessage.setMessageContent("恭喜 "+nickName+" 成为本场皇冠粉丝");
									publicMesg.add(chatMessage);
									chatMessageAdapter.notifyDataSetChanged();
								}
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
	}

	public void leaveRoom(){
		if(client != null)
			client.disconnect();
	}
	
	public void finish(){
		super.finish();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(mHostInfoPop!=null){
			mHostInfoPop.dismiss();
		}
		switchScreen(newConfig);
	}
	
	public void switchScreen(Configuration newConfig){
		if(playerViewController!=null){
			if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
				setViewStatus(0); //可见
				setPublic();
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
				surfaceView.setLayoutParams(lp);
				playerViewController.setVideoSize(PlayerViewController.VIDEO_LAYOUT_ZOOM);
			}else{
				setViewStatus(8);//不可见
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
				surfaceView.setLayoutParams(lp);
				playerViewController.setVideoSize(PlayerViewController.VIDEO_LAYOUT_SCALE);
			}
		}
	}
	
	public void setViewStatus(int i){
		layoutBottom.setVisibility(i);
		mRoomUserList.setVisibility(i);
		GiftContainerTwo.setVisibility(i);
//		dividerView.setVisibility(i);
		findViewById(R.id.room_tab_gift).setVisibility(i);
		findViewById(R.id.room_tab_layout).setVisibility(i);
	}
	
	/**
	 * 赠送元宝
	 */
	private void doYunBaoTask() {
		if(mUser!=null){
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("uid", mUser.getUid());
			params.put("rid", mHost.getRoomId());
			params.put("token", mUser.getToken());
			asyncHttpClient.post(AppConstants.SEND_YUAN_BAO, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
				}
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					if(content!=null){
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(content);
							boolean success=jsonObject.optBoolean("success");
							if(success==true){
								JSONObject jsonObject2=jsonObject.optJSONObject("retval");
								String reset=jsonObject2.optString("intIngot");
								String sendCount=jsonObject2.optString("daySendIngotCount");//当天送出的元宝数目
								if(fromTask){
									if(sendCount.equals("10")){
										Utils.doUserTask(mUser, taskId,null);
									}
								}
								tvYuanBaoCount.setText(reset);
							}else{
								String message=jsonObject.optString("msg");
								Utils.MakeToast(getApplicationContext(), message);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Utils.MakeToast(getApplicationContext(), "连接超时");
				}
			});
		}else{
			Utils.showLoginDialog(this, true);
		}
	}

	/**
	 * 设置私聊面板的状态
	 */
	public void setPrivate() {
		findViewById(R.id.room_tab_layout).setBackgroundDrawable(getResources().getDrawable(R.drawable.room_chat_two_bg));
		changeBg(R.id.room_tab_private_txt);
		roomuserbottom.setVisibility(8);
		layoutYuan.setVisibility(View.VISIBLE);
		privateDot.setVisibility(View.GONE);
		privateDot.clearAnimation();
		mIsPrivate=true;
		layoutFun.setVisibility(View.GONE);
		chatView.setVisibility(View.GONE);
		layoutBottom.setVisibility(View.VISIBLE);
		bottomMenuView.setVisibility(View.GONE);
		mRoomUserList.setVisibility(View.GONE);
		privateCb.setChecked(true);
		
//		mPrivateListView.setVisibility(View.VISIBLE);
//		mPublicListView.setVisibility(View.GONE);
		
		if(mUser!=null){
			mPublicListView.setVisibility(View.VISIBLE);
//			privateMessageAdapter.notifyDataSetChanged();
			mPublicListView.setAdapter(privateMessageAdapter);
			mPublicListView.setSelection(privateMessageAdapter.getCount()-1);
		}else{
			mPublicListView.setVisibility(View.GONE);
			privateNoLogo.setVisibility(View.VISIBLE);
			String msg="欢迎您进入房间,快来和我一起聊天吧,请 注册 或 登录 ,与主播开始聊天吧";
			SpannableStringBuilder builder=new SpannableStringBuilder(msg);
			builder.setSpan(new PrivateSpan(new PrivateClick(1)), 20, 22, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			builder.setSpan(new PrivateSpan(new PrivateClick(2)), 25, 27, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			tvPrivateTag.setText(builder);
		}
//		tvPub.setVisibility(View.GONE);
//		tvPrivate.setVisibility(View.VISIBLE);
//		tvAudice.setVisibility(View.GONE);
//		tvFans.setVisibility(View.GONE);
		if(GiftContainerTwo.getVisibility()==View.GONE){
			GiftContainerTwo.setVisibility(View.VISIBLE);
		}
	}
	
	class PrivateClick implements View.OnClickListener{
		
		private int tag;
		
		public PrivateClick(int i) {
			this.tag=i;
		}

		@Override
		public void onClick(View v) {
			if(tag==1){
				Intent intent=new Intent(ChatRoomActivity.this,RegActivity.class);
				intent.putExtra("fromChatRoom", true);
				startActivity(intent);
			}else{
				Intent intent=new Intent(ChatRoomActivity.this,OnlyLoginActivity.class);
				intent.putExtra("fromChatRoom", true);
				startActivity(intent);
			}
			
		}
	}
	
	class PrivateSpan extends ClickableSpan{

		private final View.OnClickListener mListener;
		
		 public PrivateSpan(View.OnClickListener l){
			 this.mListener = l;
		 }
		  
		@Override
		public void onClick(View widget) {
			 this.mListener.onClick(widget);
		}
		
		@Override
		public void updateDrawState(TextPaint ds) {
			 super.updateDrawState(ds);
			 ds.setColor(Color.BLUE);
		}
	}
	
	/**
	 * 设置公聊面板的状态
	 */
	public void setPublic(){
//		findViewById(R.id.room_tab_layout).setBackground(getResources().getDrawable(R.drawable.room_chat_one_bg));
		changeBg(R.id.room_tab_pub_txt);
		privateCb.setChecked(false);
		roomuserbottom.setVisibility(8);
		layoutYuan.setVisibility(View.VISIBLE);
		privateNoLogo.setVisibility(View.GONE);
		layoutFun.setVisibility(View.GONE);
//		chatView.setVisibility(View.GONE);
		layoutBottom.setVisibility(View.VISIBLE);
		mIsPrivate=false;
		bottomMenuView.setVisibility(View.GONE);
		mPublicListView.setVisibility(View.VISIBLE);
		mRoomUserList.setVisibility(View.GONE);
		//chatMessageAdapter.notifyDataSetChanged();
		mPublicListView.setAdapter(chatMessageAdapter);
		mPublicListView.setSelection(chatMessageAdapter.getCount()-1);
		
//		mPrivateListView.setVisibility(View.GONE);
//		mPublicListView.setVisibility(View.VISIBLE);
		
		mPublicListView.setSelection(chatMessageAdapter.getCount()-1);
//		tvPub.setVisibility(View.VISIBLE);
//		tvPrivate.setVisibility(View.GONE);
//		tvAudice.setVisibility(View.GONE);
//		tvFans.setVisibility(View.GONE);
		if(GiftContainerTwo.getVisibility()==View.GONE){
			GiftContainerTwo.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 显示彩条框
	 */
	private void showColorBarWindow() {
		try {
			mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(emotionView.getVisibility()==View.VISIBLE){
			emotionView.setVisibility(View.GONE);
		}
		View contentView=getLayoutInflater().inflate(R.layout.room_pop_chat_to, null);
		ListView list=(ListView) contentView.findViewById(R.id.chat_list);
		String [] array=getResources().getStringArray(R.array.color_ban);
		final PopupWindow popupWindow=new PopupWindow(contentView, 270, 430);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		int y=chatView.getHeight()+findViewById(R.id.chat_edit_layout).getHeight();
		popupWindow.showAtLocation(ParentView, Gravity.BOTTOM|Gravity.CENTER,0, y);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				popupWindow.dismiss();
				//彩条  <img src="http://static.9xiu.com/www//img/color_bar/color_bar_1.gif>
				String inputMsg="<img src="+"http://static.9xiu.com/www//img/color_bar/color_bar_"+(position+1)+".gif>";
				try {
					if(mIsPrivate){
						//私聊消息
						/*JSONObject object=new JSONObject();
						//JSONObject sendObject=new JSONObject(mUser.getUserDetail());
						JSONObject sendObject=new JSONObject();
						sendObject.put("uid",mUser.getUid());
						sendObject.put("nickname",mUser.getNickName());
						object.put("userinfo", sendObject);
						object.put("msg", inputMsg);
						object.put("uid", mUser.getUid());
						object.put("rid", roomid);
						object.put("recived", receiverObj);
						sendMsgToServer("5", object);*/
						sendMessage(inputMsg,true);
					}else{
						//公聊消息
						/*JSONObject object=new JSONObject();
						//JSONObject sendObject=new JSONObject(mUser.getUserDetail());
						JSONObject sendObject=new JSONObject();
						sendObject.put("uid",mUser.getUid());
						sendObject.put("nickname",mUser.getNickName());
						sendObject.put("usernum",mUser.getUserNum());
						sendObject.put("viptype",mUser.getVipType());
						sendObject.put("wealth",mUser.getWeath());
						sendObject.put("credit",mUser.getCredit());
						sendObject.put("usertype",mUser.getUserType());
						sendObject.put("avatar",mUser.getAvatar());

						object.put("userinfo", sendObject);
						object.put("msg", inputMsg);
						object.put("uid", mUser.getUid());
						object.put("rid", roomid);
						//eyan shut
						//object.put("recived", receiverObj);
						object.put("recived", new JSONObject());
						sendMsgToServer("4", object);*/
						sendMessage(inputMsg,false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.room_pop_chat_to_item, array);
		list.setAdapter(adapter);
	}

	private boolean needUserSort = true;
	
	class UserSort implements Runnable {

		@Override
		public void run() {
			UserSort();
		}
		
	}
	
	/**
	 * 用户排序
	 */
	private void UserSort() {   
		/*排序规则：
		  主播 
		  运营
		  巡管
		  VIP3 && 10富以上
		  VIP2 && 10富以上
		  VIP1 && 10富以上
		  10富以上无VIP
		  VIP3 && 10富以下
		  VIP2 && 10富以下
		  VIP1 && 10富以下
		  无VIP && 10富以下*/
		ArrayList<Father> host=new ArrayList<Father>();//主播
		ArrayList<Father> operation=new ArrayList<Father>(); //运营
		ArrayList<Father> see=new ArrayList<Father>(); //巡官
		ArrayList<Father> wealthTen=new ArrayList<Father>(); //10富以上用户
		ArrayList<Father> wealthNine=new ArrayList<Father>(); //10富以下用户
		final ArrayList<Father> tempFathers = mAllUserData;
		for (int i = 0; i < mAllUserData.size(); i++) {
			Father father=mAllUserData.get(i);
			String userType=father.getUserType();
			String wealth=father.getWeath();
			if(Utils.isNotEmptyString(userType)){
				if(father.getUserType().equals("1")){
					if(father.getUid().equals(mHost.getUid())){
						host.add(father);
						continue;
					}
				}
				if(userType.equals("3")){
					operation.add(father);
					continue;
				}
				
				if(userType.equals("2")){
					see.add(father);
					continue;
				}
			}
			if(Utils.isNotEmptyString(wealth)){
				long wealths=Long.valueOf(wealth);
				if(wealths>=9742000){
					wealthTen.add(father);
				}else{
					wealthNine.add(father);
				}
			}
		}
		tempFathers.clear();
		tempFathers.addAll(host);
		tempFathers.addAll(operation);
		tempFathers.addAll(see);
		Collections.sort(wealthTen, new VipSort());
		tempFathers.addAll(wealthTen);
		Collections.sort(wealthNine, new VipSort());
		tempFathers.addAll(wealthNine);
		int rest=userNum-mAllUserData.size();
		if(rest>0){
			if(rebertData.size()>rest){
				tempFathers.addAll(new ArrayList<Father>(rebertData.subList(0, rest)));
			}else{
				tempFathers.addAll(rebertData);
			}
		}
		
		uiHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mAllUserData = tempFathers;
				if(mAllUserData.size()>20){
					mUserDatas=new ArrayList<Father>(mAllUserData.subList(0, 20));//首先取20个加载
				}else{
					mUserDatas=new ArrayList<Father>(mAllUserData);
				}
				if (!isFinishing()) {
					if(mUserAdapter==null){
						mUserAdapter=new UserAdapter();
						mRoomUserList.setAdapter(mUserAdapter);
					}else
						mUserAdapter.notifyDataSetChanged();
					mUserAdapter.setVisibleState();
					needUserSort = true;
				}
			}
		});
		
	}

	/**
	 * 显示用户点歌节目
	 */
	private void showOrderSongPop() {
		View contentView=getLayoutInflater().inflate(R.layout.room_order_song, null);
		View orderSong=contentView.findViewById(R.id.layout_order_song);
		TextView priceMoney=(TextView) contentView.findViewById(R.id.tv_song_price);
		int hostLevel=Utils.setHostLevel(mHost.getCredit(), new ImageView(this));
		if(hostLevel<=5){
			priceMoney.setText("5000九币");
			money=5000;
		}
		if(hostLevel>=6&&hostLevel<=10){
			priceMoney.setText("10000九币");
			money=10000;
		}
		if(hostLevel>=11){
			priceMoney.setText("15000九币");
			money=15000;
		}
		TextView tvRestMoney=(TextView) contentView.findViewById(R.id.tv_rest_money);
		final TextView tvFun=(TextView) contentView.findViewById(R.id.tv_fun);
		final PullToRefreshListView pullToRefreshListView=(PullToRefreshListView) contentView.findViewById(R.id.lv_order_song);
		pullToRefreshListView.setMode(Mode.BOTH);
		final ListView listView=pullToRefreshListView.getRefreshableView();
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setEmptyView(getLayoutInflater().inflate(R.layout.layout_empty_view, null));
		if(mUser!=null){
			tvRestMoney.setText(String.valueOf(mUser.getUserCoin()));
		}
		final View orderIndicator=contentView.findViewById(R.id.order_indicator);
		final View orderedIndicator=contentView.findViewById(R.id.ordered_indicator);
		Button btnOrder=(Button) contentView.findViewById(R.id.bt_choice_song);
		btnOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_CHOICE_SONG);
			}
		});
		Button btCharge=(Button) contentView.findViewById(R.id.bt_charge);
		btCharge.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ChatRoomActivity.this,ChargeActivity.class);
				startActivity(intent);
				
			}
		});
		
		//可点歌曲列表
		orderSong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				orderIndicator.setVisibility(View.VISIBLE);
				orderedIndicator.setVisibility(View.GONE);
				IsOrderSong=true;
				IsOrderedSong=false;
				tvFun.setVisibility(View.GONE);
				if(songAdapter!=null)
				listView.setAdapter(songAdapter);
				getOrderList(listView,pullToRefreshListView);
			}
		});
		//已点歌曲列表
		View orderedSong=contentView.findViewById(R.id.layout_ordered_song);
		orderedSong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				orderIndicator.setVisibility(View.GONE);
				orderedIndicator.setVisibility(View.VISIBLE);
				tvFun.setVisibility(View.VISIBLE);
				IsOrderSong=false;
				IsOrderedSong=true;
				if(orderdAdapter!=null)
				listView.setAdapter(orderdAdapter);
				getOrderedList(listView, pullToRefreshListView);
			}
		});
		
		getOrderList(listView,pullToRefreshListView);
		pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(IsOrderSong){
					getOrderList(listView,refreshView);
				}else{
					getOrderedList(listView,refreshView);
				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(IsOrderSong){
					LoadMoreOrderSong(refreshView);
				}else{
					loadMoreOrdedSong(refreshView);
				}
			}
		});
		
		PopupWindow popupWindow=new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		popupWindow.setAnimationStyle(R.style.popShowGiftAnimStyle);
		popupWindow.showAtLocation(ParentView, Gravity.BOTTOM, 0, 0);
	}
	
	
	
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CHOICE_SONG:
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("自选歌曲点唱");
			builder.setCancelable(false);
			builder.setMessage("输入你想听的歌曲名称");
			final EditText editText=new EditText(this);
			builder.setView(editText);
			builder.setPositiveButton("好", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String songName=editText.getText().toString().trim();
					if(songName.length()>0){
						Utils.showToSomeDialog(ChatRoomActivity.this, songName, mUser, mHost.getRoomId());
//						 Utils.OrderSong(ChatRoomActivity.this, songName, mUser, mHost.getRoomId());
					}else{
						Utils.MakeToast(getApplicationContext(), "请输入歌曲名");
					}
				}
			});
			builder.setNegativeButton("取消",null);
			return builder.show();
		case DIALOG_CHARGE:
			AlertDialog.Builder charge=new AlertDialog.Builder(this);
			charge.setTitle("提醒");
			charge.setCancelable(false);
			charge.setMessage("账户九币不足,是否充值?");
			charge.setPositiveButton("充值", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent=new Intent(ChatRoomActivity.this,ChargeActivity.class);
					startActivity(intent);
				}
			});
			charge.setNegativeButton("取消",null);
			return charge.show();
			
		case DIALOG_BUY_VIP:
			AlertDialog.Builder vip=new AlertDialog.Builder(this);
			vip.setTitle("提醒");
			vip.setCancelable(false);
			vip.setMessage("VIP表情,是否购买VIP?");
			vip.setPositiveButton("购买", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent=new Intent(ChatRoomActivity.this,ShopActivity.class);
					startActivity(intent);
				}
			});
			vip.setNegativeButton("取消",null);
			return vip.show();
		}
		return null;
	}
	
/**
 * 下拉加载更多主播预设的歌
 * @param refreshView 
 */
	protected void LoadMoreOrderSong(final PullToRefreshBase<ListView> refreshListView) {
		Log.v("realPage:pageCount", realPage+":"+pageCount);
		if(realPage<pageCount){
			AsyncHttpClient httpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("userNum", mHost.getUserNum());
			params.put("p", (realPage+1)+"");
			httpClient.post(AppConstants.GET_SONG_UNORDERED, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					Log.v("loadMore",content);
					if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							JSONArray array=jsonObject2.optJSONArray("list");
							for (int i = 0; i < array.length(); i++) {
								Song song=new Song();
								JSONObject jsonObject3=array.optJSONObject(i);
								song.setId(jsonObject3.optString("id"));
								song.setMusicName(jsonObject3.optString("musicname"));
								song.setOriginal(jsonObject3.optString("original"));
								mSongs.add(song);
							}
							songAdapter.notifyDataSetChanged();
							String page=jsonObject2.optString("pageCount");
							realPage=jsonObject2.optInt("realPage");
							if(Utils.isNotEmptyString(page)&&!page.equals("null")){
								pageCount=jsonObject2.optInt("pageCount");
							}else{
								pageCount=realPage;
							}
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
					refreshListView.onRefreshComplete();
			}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					refreshListView.onRefreshComplete();
				}
			});
		}else if(realPage==pageCount||pageCount==0){
			refreshListView.onRefreshComplete();
		}
	}

	/*
	 * 主播预设的点歌列表
	 */
	public void getOrderList(final ListView listView,final PullToRefreshBase<ListView> refreshListView){
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("usernum", mHost.getUserNum());
		params.put("p", 1+"");
		asyncHttpClient.post(AppConstants.GET_SONG_UNORDERED, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
//				Log.e("test", "orderSong content=="+content);
				if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							JSONArray array=jsonObject2.optJSONArray("list");
							mSongs.clear();
							if(array!=null){
								for (int i = 0; i < array.length(); i++) {
									Song song=new Song();
									JSONObject jsonObject3=array.optJSONObject(i);
									song.setId(jsonObject3.optString("id"));
									song.setMusicName(jsonObject3.optString("musicname"));
									song.setOriginal(jsonObject3.optString("original"));
									mSongs.add(song);
								}
								songAdapter=new SongAdapter(ChatRoomActivity.this, mSongs,mUser,mHost.getRoomId(),money);
								listView.setAdapter(songAdapter);
							}
							realPage=jsonObject2.optInt("realPage");
							String page=jsonObject2.optString("pageCount");
							if(Utils.isNotEmptyString(page)&&!page.equals("null")){
								pageCount=jsonObject2.optInt("pageCount");
							}else{
								pageCount=realPage;
							}
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					refreshListView.onRefreshComplete();
				}else{
					refreshListView.onRefreshComplete();
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				refreshListView.onRefreshComplete();
			}
		});
	}

	

	/**
	 * 得到本场以点歌曲列表
	 * @param pullToRefreshListView 
	 * @param listView 
	 */
	public void getOrderedList(final ListView listView, final PullToRefreshBase<ListView> pullToRefreshListView){
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("usernum", mHost.getUserNum());
		params.put("p", 1+"");
		asyncHttpClient.post(AppConstants.GET_SONG_ORDERED, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							JSONArray array=jsonObject2.optJSONArray("list");
							mOrderdSongs.clear();
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject3=array.getJSONObject(i);
								Song song=new Song();
								song.setMusicName(jsonObject3.optString("musicname"));
								song.setOrderName(jsonObject3.optString("nickname"));
								song.setStatus(jsonObject3.optString("status"));
								mOrderdSongs.add(song);
							}
							orderdAdapter=new SongOrderdAdapter(ChatRoomActivity.this, mOrderdSongs);
							listView.setAdapter(orderdAdapter);
							realPage=jsonObject2.optInt("realPage");
							String page=jsonObject2.optString("pageCount");
							if(Utils.isNotEmptyString(page)&&!page.equals("null")){
								pageCount=jsonObject2.optInt("pageCount");
							}else{
								pageCount=realPage;
							}
							Log.v("realPage:PageCount", realPage+":"+pageCount);
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				pullToRefreshListView.onRefreshComplete();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				pullToRefreshListView.onRefreshComplete();
			}
			
		});
		
	}
	
	/**
	 * 加载更多用户已点歌曲列表
	 */
	public void loadMoreOrdedSong(final PullToRefreshBase<ListView>  refreshListView){
		if(realPage<pageCount){
			AsyncHttpClient httpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("userNum", mHost.getUserNum());
			params.put("p", (realPage+1)+"");
			httpClient.post(AppConstants.GET_SONG_ORDERED, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							JSONArray array=jsonObject2.optJSONArray("list");
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject3=array.getJSONObject(i);
								Song song=new Song();
								song.setMusicName(jsonObject3.optString("musicname"));
								song.setOrderName(jsonObject3.optString("nickname"));
								song.setStatus(jsonObject3.optString("status"));
								mOrderdSongs.add(song);
							}
							orderdAdapter.notifyDataSetChanged();
							String page=jsonObject2.optString("pageCount");
							realPage=jsonObject2.optInt("realPage");
							if(Utils.isNotEmptyString(page)&&!page.equals("null")){
								pageCount=jsonObject2.optInt("pageCount");
							}else{
								pageCount=realPage;
							}
						}else{
							String msg=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
					refreshListView.onRefreshComplete();
			}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					refreshListView.onRefreshComplete();
				}
			});
		}else if(realPage==pageCount||pageCount==0){
			refreshListView.onRefreshComplete();
		}
	}
	
	private void showChatToPop() {
		try {
			mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(emotionView.getVisibility()==View.VISIBLE){
			emotionView.setVisibility(View.GONE);
		}
		bottomMenuView.setVisibility(View.GONE);
		View contentView=getLayoutInflater().inflate(R.layout.room_pop_chat_to, null);
		ListView list=(ListView) contentView.findViewById(R.id.chat_list);
		final PopupWindow popupWindow=new PopupWindow(contentView, mPopChattoWidth, WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		int y=chatView.getHeight()+findViewById(R.id.chat_edit_layout).getHeight();
		popupWindow.showAtLocation(ParentView, Gravity.BOTTOM|Gravity.LEFT,5, y);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					popupWindow.dismiss();
					mTvChatTo.setText(mToData.get(position).getNickName());
					String useDetail=(String) parent.getAdapter().getItem(position);
					if(Utils.isNotEmptyString(useDetail)){
						try {
							receiverObj=new JSONObject(useDetail);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						setPublic();
						receiverObj=new JSONObject();
					}
			}
		});
		ToChatAdapter adapter=new ToChatAdapter(this,mToData);
		list.setAdapter(adapter);
	}

	/**
	 * 显示表情
	 */
	private void setEmotion() {
		ArrayList<View> views=new ArrayList<View>();
		int size=AppConstants.EMOTION_ID.length/21;
		int res=AppConstants.EMOTION_ID.length%21>0?1:0;
		 size=size+res;
		for (int i = 0; i <size; i++) {
			GridView gridView=(GridView) getLayoutInflater().inflate(R.layout.room_emotion_gridview, null);
			EmotionAdapter adapter=new EmotionAdapter(this,i);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(parent.getAdapter().getItemId(position)>=48){ //VIP表情
						if(mIsVip){
							setEditWidthEmo(parent, position);
						}else{
							showDialog(DIALOG_BUY_VIP);
						}
					}else{
						setEditWidthEmo(parent, position);
					}
				}
			});
			views.add(gridView);
		}
		
		GiftPagerAdapter adapter=new GiftPagerAdapter(views);
		mPagerEmotion.setAdapter(adapter);
		emotionIndicator.setViewPager(mPagerEmotion);
	}


	public void setEditWidthEmo(AdapterView parent,int position){
		SpannableString spannableString=new SpannableString(parent.getAdapter().getItem(position).toString());
		Drawable drawable=getResources().getDrawable(AppConstants.EMOTION_ID[(int) parent.getAdapter().getItemId(position)]);
		if(drawable!=null){
			drawable.setBounds(0, 0, faceWidth, faceWidth);
			ImageSpan imageSpan=new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
			spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			chatInput.append(spannableString);
		}
	}
	 
	 public void setJoinMsg(){
		 joinRoom();
	 }
	 
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		firstshowhost = true;
//		mHost=(Father)intent.getSerializableExtra("host");
//		showHostInfoPop();
		leaveRoom();
		ConnectToServer(mChatServerAddress);
		if(intent.getBooleanExtra("self", false)){
			
			mHost=(Father)intent.getSerializableExtra("host");
			setRoomHostMsg();
			//eyan added
			roomid = mHost.getRoomId();
//			setJoinMsg();
		}else{
			ObjectInputStream objectIputStream;
			try {
//				objectIputStream = new ObjectInputStream(openFileInput(AppConstants.USER_INFO));
				mUser=ApplicationEx.get().getUserManager().getUser();
				if(Utils.isNotEmptyString(mUser.getVipType())){
					if(Integer.valueOf(mUser.getVipType())>=800001){
						mIsVip=true;
					}
				}
				publicMesg.remove(0);
				chatInput.setHint("");
				privateMessageAdapter.setUser(mUser);
				chatMessageAdapter.setUser(mUser);
				chatMessageAdapter.notifyDataSetChanged();
//				setJoinMsg();
				getHostAtt();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 实时更新用户的信息
	 */
	@Override
	protected void onResume() {
		try {
			super.onResume();
//			if(mUser!=null){
//			Utils.UpdateUserInfo(mUser, ChatRoomActivity.this);
//				User user=ApplicationEx.get().getUserManager().getUser();
//				if(user!=null){
//					mUser=user;
//				}
//			}
			/*
			 * 定时向服务器发送消息
			 * if (timer==null) {
				timer = new Timer();
				task = new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message message = new Message();
						message.what = 10086;
						uiHandler.sendMessage(message);
					}
				};
				timer.schedule(task, 5000, 10000);
			}*/
//			if (playerViewController==null) {
//				playerViewController = new PlayerViewController(this,ivVideoLoad);
//				playerViewController.initPlayerView();
//				playerViewController.startPlay(mHost.getRoomTag(), mHost.getRoomId());
//			}
			if(playerViewController != null && !playerViewController.isPlay())
				playerViewController.startPlay(mHost.getRoomTag(), mHost.getRoomId());
			MobclickAgent.onResume(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 显示送礼的popwindow
	 */
	public void showPopGift() {
		mGroupCount=1;//默认为一个
		try {
			if(getCurrentFocus()!=null)
				mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//礼物种类:0,1,2,9,6,3,5,4
		final View contentView=getLayoutInflater().inflate(R.layout.room_pop_gift_layout, null);
		final TextView giftNum=(TextView) contentView.findViewById(R.id.gift_num_edit);
		tvBin=(TextView) contentView.findViewById(R.id.cur_mon);
		tvPoint=(TextView) contentView.findViewById(R.id.cur_point);
		tvTo=(TextView) contentView.findViewById(R.id.send_to_edit);
		tvTo.setText(mToGiftData.get(0).getNickName());
		tvTo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View anchorView=contentView.findViewById(R.id.gift_amount_bg);
				View contentView=getLayoutInflater().inflate(R.layout.room_pop_chat_to, null);
				ListView list=(ListView) contentView.findViewById(R.id.chat_list);
				int width=(int) getResources().getDimension(R.dimen.pop_group_gift_width);
				final PopupWindow popupWindow=new PopupWindow(contentView, width, android.view.WindowManager.LayoutParams.WRAP_CONTENT);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setTouchable(true);
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
				popupWindow.showAtLocation(ParentView, Gravity.BOTTOM|Gravity.RIGHT,0, anchorView.getHeight());
				ToChatAdapter adapter=new ToChatAdapter(ChatRoomActivity.this,mToGiftData);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						popupWindow.dismiss();
						Father father=mToGiftData.get(position);
						tvTo.setText(father.getNickName());
						toUserId=Integer.valueOf(father.getUid());
					}
				});
			}
		});
		final int[] num=getResources().getIntArray(R.array.gift_group_count);
		giftNum.setText(num[0]+"");
		if(mUser!=null){
			tvBin.setVisibility(View.VISIBLE);
			tvPoint.setVisibility(View.VISIBLE);
			//Log.e("test",">>>>>>>coin=="+mUser.getUserCoin());
			tvBin.setText("九币:"+mUser.getUserCoin()); 
			tvPoint.setText("九点:"+mUser.getUserDian());
		}else{
			tvBin.setVisibility(View.INVISIBLE);
			tvPoint.setVisibility(View.INVISIBLE);
		}
		TextView tvCharge=(TextView) contentView.findViewById(R.id.tv_charge);
		tvCharge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ChatRoomActivity.this,ChargeActivity.class);
				startActivity(intent);
			}
		});
		
		giftNum.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				View anchorView=contentView.findViewById(R.id.gift_amount_bg);
				View groupGift=getLayoutInflater().inflate(R.layout.room_pop_chat_to, null);
				ListView listView=(ListView) groupGift.findViewById(R.id.chat_list);
				String[] array=getResources().getStringArray(R.array.gift_group);
				int width=(int) getResources().getDimension(R.dimen.pop_group_gift_width);
				int height=(int) getResources().getDimension(R.dimen.pop_group_gift_height);
				final PopupWindow popupWindow=new PopupWindow(groupGift,width,height);
				ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.room_pop_chat_to_item, array);
				listView.setAdapter(arrayAdapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						giftNum.setText(num[position]+"");
						mGroupCount=num[position];
						popupWindow.dismiss();
					}

				});
				popupWindow.setOutsideTouchable(true);
				popupWindow.setTouchable(true);
				popupWindow.setFocusable(true);
				popupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
				popupWindow.showAtLocation(ParentView, Gravity.BOTTOM|Gravity.LEFT, 0, anchorView.getHeight());
			}
		});
		final ViewPager pager=(ViewPager) contentView.findViewById(R.id.viewpager_scroller);
		mIndicator=(CirclePageIndicator) contentView.findViewById(R.id.pager_indicator);
//		int height=(int) getResources().getDimension(R.dimen.pop_gift_height);
		mShowGiftPop=new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
		mShowGiftPop.setOutsideTouchable(true);
		mShowGiftPop.setTouchable(true);
		mShowGiftPop.setFocusable(true);
		mShowGiftPop.setBackgroundDrawable(new ColorDrawable(-000000));
		mShowGiftPop.setAnimationStyle(R.style.popShowGiftAnimStyle);
		mShowGiftPop.showAtLocation(ParentView, Gravity.BOTTOM, 0, 0);
		for (int i = 0; i < tvGiftCateogryId.length; i++) {
			tvGiftCageory[i]=(TextView) contentView.findViewById(tvGiftCateogryId[i]);
			setGiftCheckedListener(tvGiftCageory[i],pager);
		}
		File file =new File(this.getFilesDir(),AppConstants.GIFT_CACHE);
		if(file.exists() && file.isDirectory())
			file.delete();
		
		try {
			if (file.exists()) {
				FileInputStream fileInputStream=openFileInput(AppConstants.GIFT_CACHE);
				JSONArray array=new JSONArray(Utils.getLineString(fileInputStream));
				parserGift(array, pager);
				return;
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			file.delete();
		}
		
		
		/*try {
			FileInputStream fileInputStream=openFileInput(AppConstants.GIFT_CACHE);
			JSONArray array=new JSONArray(Utils.getLineString(fileInputStream));
			parserGift(array, pager);
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		asyncHttpClient.post(AppConstants.GET_GIFT_LIST, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.getBoolean("success");
						if(success==true){
							JSONArray array=jsonObject.getJSONArray("retval");
							if(array!=null&&array.length()>0){
								FileOutputStream outputStream=openFileOutput(AppConstants.GIFT_CACHE, MODE_PRIVATE);
								outputStream.write(array.toString().getBytes());
								outputStream.flush();
								outputStream.close();
								parserGift(array, pager);
							}
						}else{
							String message=jsonObject.getString("msg");
							Utils.MakeToast(getApplicationContext(), message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
		});
	}
	
	
	/**
	 * 解析从服务器加载过来的数据
	 * @param array
	 */
	public void parserGift(JSONArray array,ViewPager pager){
		try {
			giftsFunny.clear();
			giftsHaohua.clear();
			giftsHot.clear();
			giftsStar.clear();
			giftsVip.clear();
			mGiftMaps=new HashMap<String, String>();
			for (int i = 0; i < array.length(); i++) {
				//0,1,2,9,6,3,5,4
				Gift gift=new Gift();
				JSONObject giftObject=array.getJSONObject(i);
				gift.setGiftId(giftObject.optString("gid"));
				gift.setIconUrl(giftObject.optString("iconUrl"));
				gift.setGiftName(giftObject.optString("giftname"));
				int giftType=Integer.valueOf(giftObject.optString("gifttype"));
				gift.setGiftType(giftType+"");
				gift.setPrice(giftObject.optString("price"));
				gift.setRate(giftObject.optString("rate"));
				gift.setResource(giftObject.optString("resource"));
				int resType=giftObject.optInt("restype");
				gift.setResourceType(resType+"");
				if(resType==2){
					if(Utils.isNotEmptyString(gift.getResource())){
						Utils.downloadFile(haoHuaDir,gift.getResource(),mNeedUpdate);//下载豪华礼物的html文件
					}
				}else{
					if(Utils.isNotEmptyString(gift.getResource())){
						Utils.downloadFile(giftDir,gift.getResource(),mNeedUpdate);//下载 普通礼物文件
					}
				}
				mGiftMaps.put(gift.getGiftId(), resType+"");
				switch (giftType) {
					case 0://热门
						giftsHot.add(gift);
						break;
	
					case 2://趣味
						giftsFunny.add(gift);
						break;
						
					case 3://VIP
						giftsVip.add(gift);
						break;
						
					case 4://强星
						giftsStar.add(gift);
						break;
						
					case 5://豪华
						giftsHaohua.add(gift);
						break;
					}
			}
			
			//---------------礼物全部解析完毕
			changeGift(giftsHot, pager,false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载礼物
	 */
   private void LoadGiftData() {
	   new Thread(new Runnable(){

		@Override
		public void run() {
				File file =new File(ChatRoomActivity.this.getApplication().getFilesDir(),AppConstants.SERVER_GIFT_CACHE);
				if(file.exists() && file.isFile() && (System.currentTimeMillis()-file.lastModified()<1000*60*60*24*1 || !Utils.isNetwokAvailable(ChatRoomActivity.this))){
					try {
						BufferedReader br = new BufferedReader(new FileReader(file));
						String data="";
						String line = null;
						while(( line = br.readLine())!=null){
							data+=line;
						}
						br.close();
						//Log.e("test","----data="+data);
						parseGift(data);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}else if(Utils.isNetwokAvailable(ChatRoomActivity.this)){
					AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
					asyncHttpClient.post(AppConstants.GET_GIFT_LIST, new AsyncHttpResponseHandler(){
						@Override
						public void onStart() {
							super.onStart();
						}
						
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							parseGift(content);
							if(content!=null){
								try {
									//Log.e("test","----data2="+content);
									FileOutputStream fileOutputStream=ChatRoomActivity.this.openFileOutput(AppConstants.SERVER_GIFT_CACHE, ChatRoomActivity.this.MODE_PRIVATE);
									fileOutputStream.write(content.getBytes());
									fileOutputStream.flush();
									fileOutputStream.close();
								} catch (Exception e) {
									e.printStackTrace();
								} 
							}
						}
						
						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							Utils.MakeToast(getApplicationContext(), "网络连接超时");
						}
					});
				}
		}
		   
	   })
	   .start();
	}

   private void parseGift(String content){
	   if(content!=null){
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(content);
				boolean success=jsonObject.getBoolean("success");
				if(success==true){
					JSONArray array=jsonObject.getJSONArray("retval");
					if(array!=null&&array.length()>0){
						mGiftMaps=new HashMap<String, String>();
						for (int i = 0; i < array.length(); i++) {
							//0,1,2,9,6,3,5,4
							Gift gift=new Gift();
							JSONObject giftObject=array.getJSONObject(i);
							int giftType=Integer.valueOf(giftObject.optString("gifttype"));
							gift.setGiftType(giftType+"");
							gift.setResource(giftObject.optString("resource"));
							gift.setIconUrl(giftObject.optString("iconUrl"));
							Utils.downloadFile(giftDir, gift.getIconUrl(),mNeedUpdate);
							int resType=giftObject.optInt("restype");
							gift.setResourceType(resType+"");
							mGiftMaps.put(gift.getGiftId(), resType+"");
							if(resType==2){
								if(Utils.isNotEmptyString(gift.getResource())){
									Utils.downloadFile(haoHuaDir,gift.getResource(),mNeedUpdate);//下载豪华礼物的html文件
//									NLog.e("test", "haohuaURL=="+gift.getResource());
								}
							}else{
								if(Utils.isNotEmptyString(gift.getResource())){
									Utils.downloadFile(giftDir,gift.getResource(),mNeedUpdate);//下载html文件
								}
							}
						}
					}
				}else{
					String message=jsonObject.getString("msg");
					Utils.MakeToast(getApplicationContext(), message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
   }
	/**
	 * 用来显示组合礼物
	 * @param num 
	 */
	private void showGroupGift(final File sourceFile) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(haoHuaDir!=null){
					try {
						String dir=haoHuaDir.getAbsolutePath();
//						Utils.unzip(dir+"/temp"+mGroupCount+".zip",dir+File.separator+"html");
						File targetFile=new File(dir+File.separator+"html/temp"+mGroupCount,"/.images/gift.png");
						if(targetFile.exists()&&sourceFile.exists()){
							targetFile.delete();
							targetFile.createNewFile();
							FileInputStream fileInputStream=new FileInputStream(sourceFile);
							Bitmap map=BitmapFactory.decodeStream(fileInputStream);
							FileOutputStream fileOutputStream2=new FileOutputStream(targetFile);
							map.compress(CompressFormat.PNG, 100, fileOutputStream2);
							fileOutputStream2.flush();
							fileOutputStream2.close();
							fileInputStream.close();
							String groupHtml=dir+File.separator+"html/temp"+mGroupCount+"/index.html";
							Gift gift=new Gift();
							gift.setResource(groupHtml);
							mGroupGift.add(gift);
							if(mGroupGift.size()==1){
								uiHandler.sendMessage(uiHandler.obtainMessage(SHOW_GROUP_GIFT, gift));
							}else{
								uiHandler.sendMessageDelayed(uiHandler.obtainMessage(SHOW_GROUP_GIFT, gift), 22*1000);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	public void setGiftCheckedListener(TextView tv, final ViewPager pager){
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeSelectBg(v.getId());
				switch (v.getId()) {
				case R.id.tv_gift_hot:
					changeGift(giftsHot, pager,false);
					break;

				case R.id.tv_gift_funny:
					changeGift(giftsFunny, pager,false);
					break;
				case R.id.tv_gift_vip:
					changeGift(giftsVip, pager,false);
					break;
				case R.id.tv_gift_star:
					changeGift(giftsStar, pager,false);
					break;
				case R.id.tv_gift_haohua:
					changeGift(giftsHaohua, pager,false);
					break;
				case R.id.tv_gift_kuncun:
					doGetStockTask(pager);
					break;
				}
			}
		});
	}
	
	
	/*
	 * 获取用户礼物库存
	 */
	protected void doGetStockTask(final ViewPager pager) {
		if(mUser!=null){
			AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("uid", mUser.getUid());
			params.put("token", mUser.getToken());
			asyncHttpClient.post(AppConstants.GET_GIFT_STOCK, params, new AsyncHttpResponseHandler(){
				@Override
				public void onStart() {
					super.onStart();
				}
				
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					if(content!=null){
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(content);
							boolean success=jsonObject.optBoolean("success");
							if(success==true){
								JSONArray array=jsonObject.optJSONArray("retval");
								giftsKunCun.clear();
								for (int i = 0; i < array.length(); i++) {
									JSONObject giftObject=array.getJSONObject(i);
									Gift gift=new Gift();
									gift.setGiftId(giftObject.optString("gid"));
									gift.setIconUrl(giftObject.optString("iconUrl"));
									gift.setGiftName(giftObject.optString("giftname"));
									gift.setPrice(giftObject.optString("price"));
									gift.setGiftType(giftObject.optString("gifttype"));
									gift.setRate(giftObject.optString("rate"));
									giftsKunCun.add(gift);
								}
								if(giftsKunCun.size()>0){
									changeGift(giftsKunCun, pager,true);
								}else{
									pagerViews.clear();
									pagerViews.add(new GridView(getApplicationContext()));
									GiftPagerAdapter pagerAdapter=new GiftPagerAdapter(pagerViews);
									pager.setAdapter(pagerAdapter);
									mIndicator.setViewPager(pager);
									Utils.MakeToast(getApplicationContext(), "你没有库存礼物");
								}
							}else{
								String msg=jsonObject.optString("msg");
								Utils.MakeToast(getApplicationContext(), msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
				}
			});
		}else{
			Utils.MakeToast(getApplicationContext(), "请先登录");
		}
	}

	/**
	 * 根据选择礼物类别来改变背景
	 * @param id
	 */
	public void changeSelectBg(int id){
		for (int i = 0; i < tvGiftCateogryId.length; i++) {
			if(id==tvGiftCateogryId[i]){
				tvGiftCageory[i].setBackgroundResource(R.drawable.gift_category);
			}else{
				tvGiftCageory[i].setBackgroundDrawable(new BitmapDrawable());
			}
		}
	}
	
	/**
	 * 根据用户选择不同的礼物类别 来显示不同的礼物
	 * @param kunCun 
	 */
	public void changeGift(ArrayList<Gift> giftData,ViewPager pager, final boolean kunCun){
		pagerViews.clear();
		pager.removeAllViews();
		int size=giftData.size()/8;
		int res=giftData.size()%8>0?1:0;
		size=size+res;
		for (int i = 0; i < size; i++) {
			View view=getLayoutInflater().inflate(R.layout.room_pop_gift_grid, null);
			GridView gridView=(GridView) view.findViewById(R.id.grid_gift);
			gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(mUser!=null){
						final Gift gift=(Gift) parent.getAdapter().getItem(position);
						if(kunCun){
							doChocieGiftTask(gift,true);
						}else{
						int userCoin=mUser.getUserCoin();
						int userDian=mUser.getUserDian();
						if((Long.valueOf(userDian)>=Long.valueOf(gift.getPrice()))||(Long.valueOf(userCoin)>=Long.valueOf(gift.getPrice()))){
							doChocieGiftTask(gift,false);
						}else{
							showDialog(DIALOG_CHARGE);
						}
					}
					}else{
						Utils.showLoginDialog(ChatRoomActivity.this, true);
					}
				}
			});
			GridGiftAdapter adapter=new GridGiftAdapter(i,giftData);
			gridView.setAdapter(adapter);
			pagerViews.add(gridView);
		}
		GiftPagerAdapter pagerAdapter=new GiftPagerAdapter(pagerViews);
		pager.setAdapter(pagerAdapter);
		mIndicator.setViewPager(pager);
	}
	
	/**
	 * 该方法来判断用户选择的是普通礼物  还是组合礼物   执行对应的方法
	 * @param gift
	 */
	public void doChocieGiftTask(final Gift gift,final boolean kunCun){
		doSendGiftTask(gift,mGroupCount+"",kunCun);
		mShowGiftPop.dismiss();
		setPublic();
	}
	
	/**
	 * 处理用户送礼
	 */
	protected void doSendGiftTask(Gift gift,String count,boolean isPackage) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("touid", toUserId+"");
		params.put("rid", mHost.getRoomId());
		params.put("gid", gift.getGiftId());
		params.put("token", mUser.getToken());
		params.put("count", count);
		if(isPackage==true){
			params.put("ispackage", 1+""); //用来区分是否是库存礼物
		}else{
			params.put("ispackage", 0+""); //用来区分是否是库存礼物
		}
	
		params.put("authkey", mUser.getToken());
		params.put("msg", "");
		params.put("nickname", mUser.getNickName());
		params.put("tonickname", "123");
		params.put("giftname", gift.getGiftName());
		asyncHttpClient.post(AppConstants.SEND_GIFT, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
//				Log.e("test", "content========"+content);
				if(content!=null){
					//Log.v("sendGiftMsg", content);
					try {
						JSONObject jsonObject=new JSONObject(content);
						boolean success=jsonObject.optBoolean("success");
						if(success==true){
							JSONObject jsonObject2=jsonObject.optJSONObject("retval");
							int userCoin=jsonObject2.optInt("usercoins");
							int userDian=jsonObject2.optInt("userdian");
							mUser.setUserCoin(userCoin);
							mUser.setUserDian(userDian);
//							Utils.saveUser(ChatRoomActivity.this, mUser);
							tvBin.setText("九币:"+mUser.getUserCoin()); 
							tvPoint.setText("九点："+mUser.getUserDian());
						//	Log.e("test",">>>>jiubi="+mUser.getUserCoin());
						}else{
							String msg=jsonObject.optString("msg");
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

	class GridGiftAdapter extends BaseAdapter{
		private ArrayList<Gift> mData;
		private FinalBitmap fb;
		
		public GridGiftAdapter(int i, ArrayList<Gift> data){
			fb = FinalBitmap.from(getApplicationContext());
			mData=new ArrayList<Gift>();
			for (int j = i*8; j <(i+1)*8&&j<data.size(); j++) {
				mData.add(data.get(j));
			}
		}
//		
//		public GridGiftAdapter() {
//		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final GiftViewHold viewHold;
			Gift gift=mData.get(position);
			if(convertView==null){
				viewHold=new GiftViewHold();
				convertView=getLayoutInflater().inflate(R.layout.room_pop_gift_item, null);
				viewHold.giftThumb=(ImageView) convertView.findViewById(R.id.gift_thumb);
				viewHold.tvGiftName=(TextView) convertView.findViewById(R.id.gift_name);
				viewHold.tvGiftPrice=(TextView) convertView.findViewById(R.id.gift_price);
				convertView.setTag(viewHold);
			}else{
				viewHold=(GiftViewHold) convertView.getTag();
			}
				viewHold.tvGiftName.setText(gift.getGiftName());
				viewHold.tvGiftPrice.setText(gift.getPrice());
				fb.display(viewHold.giftThumb, gift.getIconUrl(), R.drawable.room_gift);
				/*Bitmap map=mGiftImageLoader.loadBitmap(gift.getIconUrl(), new AsyncImageLoader.ImageCallback() {
					
					@Override
					public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
						viewHold.giftThumb.setImageBitmap(imageDrawable);
					}
				});
				if(map==null){
					viewHold.giftThumb.setImageResource(R.drawable.room_gift);
				}else{
					viewHold.giftThumb.setImageBitmap(map);
				}*/
			return convertView;
		}
		
		private  final class GiftViewHold{
			public ImageView giftThumb; //礼物图像
			public TextView  tvGiftName; //礼物名字
			public TextView tvGiftPrice;//礼物价格
		}
		
	}
	
	
	/**
	 * 礼物viewPager适配器
	 * @author Administrator
	 *
	 */
	class GiftPagerAdapter extends PagerAdapter{

		private ArrayList<View> views;
		public GiftPagerAdapter(ArrayList<View> views){
			
			this.views=views;
		}
		
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
			if(position<views.size()){
				((ViewPager) container).removeView(views.get(position));
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position));
			return views.get(position);
		}
	}
	
	/**
	 * 显示主播标签的窗体
	 */
	
	private void showHostInfoPop(){
		try {
			if(getCurrentFocus()!=null)
				mInputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextView xingGeOne=(TextView) host_info.findViewById(R.id.room_tv_actor_xingge_one);
		TextView xingGeTwo=(TextView) host_info.findViewById(R.id.room_tv_actor_xingge_two);
		TextView xingGeThree=(TextView) host_info.findViewById(R.id.room_tv_actor_xingge_three);
		String xingGeStr=mHost.getImpress();
		if(Utils.isNotEmptyString(xingGeStr)&&!xingGeStr.equals("null")){
			int index=xingGeStr.indexOf("|");
		if(index!=-1){
			xingGeOne.setText(jsonImpressObject.optString(xingGeStr.substring(0,index)));
			int lastIndex=xingGeStr.lastIndexOf("|");
			if(lastIndex!=-1&&lastIndex!=index){
				xingGeTwo.setText(jsonImpressObject.optString(xingGeStr.substring(index+1, lastIndex)));
				xingGeThree.setText(jsonImpressObject.optString(xingGeStr.substring(lastIndex+1, xingGeStr.length())));
			}else{
				xingGeTwo.setText(jsonImpressObject.optString(xingGeStr.substring(index+1, xingGeStr.length())));
				xingGeThree.setVisibility(View.GONE);
			}
		}else{
			xingGeOne.setText(jsonImpressObject.optString(xingGeStr));
			xingGeTwo.setVisibility(View.GONE);
			xingGeThree.setVisibility(View.GONE);
		}
		}else{
			xingGeOne.setVisibility(View.GONE);
			xingGeTwo.setVisibility(View.GONE);
			xingGeThree.setVisibility(View.GONE);
		}
		final ImageView iv=(ImageView) host_info.findViewById(R.id.iv_host_avatar);
//		com.ninexiu.utils.FinalBitmap.from(getApplicationContext()).display(iv, mHost.getAvatar(),R.drawable.loading_image);
		
		Bitmap map = imageLoader.loadBitmap(mHost.getAvatar(), new AsyncImageLoader.ImageCallback() {
			
			@Override
			public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
				// TODO Auto-generated method stub
				iv.setImageBitmap(Utils.toRoundBitmap(imageDrawable));
			}
		});
		if (map==null) {
			iv.setImageBitmap(Utils.toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)));
		}
		
		ImageView ivLevel=(ImageView) host_info.findViewById(R.id.host_level);
		
		Utils.setHostLevel(mHost.getCredit(), ivLevel);
		TextView tvName=(TextView) host_info.findViewById(R.id.room_host_name);
		tvName.setText(mHost.getNickName());
		roomhostname.setText(mHost.getNickName());
		final ImageView ivAtt=(ImageView) host_info.findViewById(R.id.iv_pay_host_att);
		if(mHasAtt){
			ivAtt.setImageResource(R.drawable.room_pay_unatt);
		}else{
			ivAtt.setImageResource(R.drawable.room_pay_atte);
		}
		ivAtt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mUser!=null){
					if(mHasAtt){
						doUnattTask(ivAtt);
					}else{
						doAttTask(ivAtt);
					}
				}else{
					Utils.showLoginDialog(ChatRoomActivity.this, true);
				}
			}
		});
	}
	
	
	/*
	 * 取消关注
	 */
	protected void doUnattTask(final ImageView ivAtt) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		String cancleId="";
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		for (int i = 0; i < hostAtts.size(); i++) {
			Host host=hostAtts.get(i);
			if(host.getUid().equals(mHost.getUid())){
				cancleId=host.getHostAttId();
				break;
			}
		}
		params.put("cancleId", cancleId);
		asyncHttpClient.post(AppConstants.PAY_HOST_UNATTE,params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(ChatRoomActivity.this,"操作中……");
				mDialog.show();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mDialog.dismiss();
				if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.getBoolean("success");
						if(success==true){
							mHasAtt=false;
							Utils.MakeToast(getApplicationContext(), "取消成功");
							ivAtt.setImageResource(R.drawable.room_pay_atte);
						}else{
							String message=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), message);
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
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
		});
	}

	/*
	 * 添加关注
	 */
	protected void doAttTask(final ImageView ivAtt) {
		AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("uid", mUser.getUid());
		params.put("token", mUser.getToken());
		params.put("follwerId", mHost.getUid());
		asyncHttpClient.post(AppConstants.PAY_HOST_ATTE, params,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mDialog=Utils.showProgressDialog(ChatRoomActivity.this,"操作中……");
				mDialog.show();
			}
			
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mDialog.dismiss();
				if(content!=null){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(content);
						boolean success=jsonObject.getBoolean("success");
						if(success==true){
							mHasAtt=true;
							Utils.MakeToast(getApplicationContext(), "关注成功");
							ivAtt.setImageResource(R.drawable.room_pay_unatt);
							getHostAtt();
							Utils.doUserTask(mUser, taskId,null);
						}else{
							String message=jsonObject.optString("msg");
							Utils.MakeToast(getApplicationContext(), message);
							mHasAtt=false;
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
				Utils.MakeToast(getApplicationContext(), "网络连接超时");
			}
		});
		
	}
	//初始化平移动画
	private void initAnim()
	  {
	    this.titleInAnim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1,
				Animation.RELATIVE_TO_SELF, 0);
	    this.titleInAnim.setDuration(500);
	    this.titleInAnim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				title.setVisibility(0);
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
	    this.liveInAnim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1,
				Animation.RELATIVE_TO_SELF, 0);
	    this.liveInAnim.setDuration(500);
	    this.liveInAnim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				host_info.setVisibility(0);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
	    this.titleOutAnim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, -1);
	    this.titleOutAnim.setDuration(1000);
	    this.titleOutAnim.setAnimationListener(new Animation.AnimationListener()
	    {
	      public void onAnimationEnd(Animation paramAnimation)
	      {
	        ChatRoomActivity.this.title.setVisibility(8);
	      }

	      public void onAnimationRepeat(Animation paramAnimation)
	      {
	      }

	      public void onAnimationStart(Animation paramAnimation)
	      {
//	    	  ChatRoomActivity.this.title.setVisibility(0);
	      }
	    });
	    this.liveOutAnim = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1);
	    this.liveOutAnim.setDuration(1000);
	    this.liveOutAnim.setAnimationListener(new Animation.AnimationListener()
	    {
	      public void onAnimationEnd(Animation paramAnimation)
	      {
	        ChatRoomActivity.this.host_info.setVisibility(8);
	      }

	      public void onAnimationRepeat(Animation paramAnimation)
	      {
	      }

	      public void onAnimationStart(Animation paramAnimation)
	      {
//	    	  ChatRoomActivity.this.host_info.setVisibility(0);
	      }
	    });
	  }
	
	
	//视频隐藏的动画
	private void startInAnim()
	  {
	    this.isShow = true;
//	    title.setVisibility(0);
//	    host_info.setVisibility(0);
	    title.startAnimation(this.titleInAnim);
	    host_info.startAnimation(this.liveInAnim);
	  }

	  private void startOutAnim()
	  {
	    this.isShow = false;
	    title.startAnimation(this.titleOutAnim);
	    host_info.startAnimation(this.liveOutAnim);
	  }
	  
	  
	  

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if(keyCode==KeyEvent.KEYCODE_BACK){
				 if(emotionView.getVisibility()==View.VISIBLE ){
					emotionView.setVisibility(View.GONE);
					return true;
				 }
				/* else{
					 leaveRoom();
					 if(playerViewController != null && !playerViewController.isPlay())
						playerViewController.startPlay(mHost.getRoomTag(), mHost.getRoomId());
						return super.onKeyDown(keyCode, event);
				 }*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		leaveRoom();
		if (timer!=null) {
			timer.cancel();
		}
		if(playerViewController != null && playerViewController.isPlay()) {
			playerViewController.stopPlay();
		}
		super.onDestroy();
		ApplicationEx.get().getUserManager().removeUserDataChangedListener(iUserDataChanged);
	}
}
