package com.ninexiu.sixninexiu.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexiu.beans.User;
import com.ninexiu.sixninexiu.ApplicationEx;
import com.ninexiu.sixninexiu.ChargeActivity;
import com.ninexiu.sixninexiu.NineShowMainActivity;
import com.ninexiu.sixninexiu.NineShowMainActivity.ITabSelected;
import com.ninexiu.sixninexiu.OnlyLoginActivity;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.sixninexiu.RegActivity;
import com.ninexiu.sixninexiu.ShopActivity;
import com.ninexiu.sixninexiu.UserInfoActivity;
import com.ninexiu.slidingmenu.SlidingMenu;
import com.ninexiu.utils.AsyncImageLoader;
import com.ninexiu.utils.IUserManager;
import com.ninexiu.utils.ThumbnailUtils;
import com.ninexiu.utils.Utils;

@SuppressLint("ValidFragment")
public class LeftMenuFragment extends Fragment {

	private int[] id = new int[] { R.id.menu_left_hall, R.id.menu_left_shop, R.id.menu_left_shake,
			R.id.menu_left_rank, R.id.menu_left_recharge, R.id.menu_left_setting,
			R.id.menu_left_hostsee, R.id.menu_left_hostatte };
	private int[] text = new int[] { R.id.left_hall_text, R.id.left_shop_text,
			R.id.left_shake_text, R.id.left_rank_text, R.id.left_charge_text,
			R.id.left_setting_text, R.id.left_hostsee_text, R.id.left_hostatt_text };
	private View[] selectView = new View[id.length];
	private TextView[] textViews = new TextView[text.length];
	private SlidingMenu slidingMenu;
	private View login, register, headImageView, view, bt_setting;
	private User mUser = null;
	private AsyncImageLoader imageLoader;
	private ImageView userhead;
	private View leftLayout, leftlayout2;
	private TextView username;
	private ImageView userLevel;

	private ITabSelected iTabSelected;
	private Bitmap mDefaultImage;
	private IUserManager iUserManager;

	public LeftMenuFragment() {
	}

	public LeftMenuFragment(ITabSelected iTabSelected) {
		this.iTabSelected = iTabSelected;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.left_meun_layout, null);
		mUser = ApplicationEx.get().getUserManager().getUser();
		userhead = (ImageView) view.findViewById(R.id.iv_user_head);
		for (int i = 0; i < id.length; i++) {
			selectView[i] = view.findViewById(id[i]);
			textViews[i] = (TextView) view.findViewById(text[i]);
		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageLoader = new AsyncImageLoader();
		login = view.findViewById(R.id.left_login);
		register = view.findViewById(R.id.left_register);
		headImageView = view.findViewById(R.id.iv_user_head);
		bt_setting = view.findViewById(R.id.bt_setting);
		leftLayout = view.findViewById(R.id.left_linearlayout);
		leftlayout2 = view.findViewById(R.id.left_linearlayout_two);
		username = (TextView) view.findViewById(R.id.left_username);
		userLevel = (ImageView) view.findViewById(R.id.left_userlevel);
		setListener(userhead);
		setListener(login);
		setListener(register);
		setListener(headImageView);
		setListener(bt_setting);
		for (int j = 0; j < id.length; j++) {
			setListener(selectView[j]);
		}
		// selectView[0].setBackgroundColor(getResources().getColor(R.color.left_menu_pressed));
		selectView[0].setBackgroundDrawable(getResources().getDrawable(R.drawable.right_selected));
		textViews[0].setTextColor(getResources().getColor(R.color.left_text_select));

		mUser = ApplicationEx.get().getUserManager().getUser();
		mDefaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		iUserManager = new IUserManager() {
			
			@Override
			public void IUserStateChanged() {
				initDatas();
			}
		};
		ApplicationEx.get().getUserManager().add(iUserManager);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		slidingMenu = ((NineShowMainActivity) activity).slidingMenu;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ApplicationEx.get().getUserManager().remove(iUserManager);
	}

	public void setListener(View view) {
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.menu_left_shake:// 点击摇一摇
					changeBg(R.id.menu_left_shake);
					iTabSelected.setSelected(ShakingFragment.class);
					slidingMenu.showContent();
					break;

				case R.id.menu_left_setting: // 设置界面
					changeBg(R.id.menu_left_setting);
					iTabSelected.setSelected(SettingFragment.class);
					slidingMenu.showContent();
					break;

				case R.id.menu_left_rank:// 排行榜界面
					changeBg(R.id.menu_left_rank);
					iTabSelected.setSelected(RankFragment.class);
					slidingMenu.showContent();
					break;

				case R.id.menu_left_recharge:// 充值界面
					changeBg(R.id.menu_left_recharge);
					// TODO 跳转Activity界面。改成Fragment需要修改直播间界面
					Intent intentCharge = new Intent(getActivity(), ChargeActivity.class);
					startActivity(intentCharge);
//					iTabSelected.setSelected(ChargeFragment.class);
//					slidingMenu.showContent();
					break;

				case R.id.menu_left_shop: // 商城界面
					changeBg(R.id.menu_left_shop);
					Intent intentShop = new Intent(getActivity(), ShopActivity.class);
					startActivity(intentShop);
					break;

				case R.id.menu_left_hall: // 直播大厅
					changeBg(R.id.menu_left_hall);
					iTabSelected.setSelected(ContentFragment.class);
					slidingMenu.showContent();
					break;
				case R.id.menu_left_hostsee:
					changeBg(R.id.menu_left_hostsee);
					iTabSelected.setSelected(TraceHostSeeFragment.class);
					slidingMenu.showContent();
					break;

				case R.id.menu_left_hostatte:
					changeBg(R.id.menu_left_hostatte);
					iTabSelected.setSelected(HostHasAttFragment.class);
					slidingMenu.showContent();
					break;

				case R.id.left_login:
					Intent intentLogin = new Intent(getActivity(), OnlyLoginActivity.class);
					startActivity(intentLogin);
					break;
				case R.id.left_register:
					Intent regIntent = new Intent(getActivity(), RegActivity.class);
					startActivity(regIntent);
					break;
				// 点击头像
				case R.id.iv_user_head:
					if (mUser != null) {
						Intent userIntent = new Intent(getActivity(), UserInfoActivity.class);
						userIntent.putExtra("ishead", true);
						userIntent.putExtra("user", mUser);
						startActivity(userIntent);
					} else {
						Utils.MakeToast(getActivity(), "请先登录，再修改信息");
					}
					break;

				case R.id.bt_setting:
					if (mUser != null) {
						Intent setintent = new Intent(getActivity(), UserInfoActivity.class);
						setintent.putExtra("user", mUser);
						startActivity(setintent);
					} else {
						Utils.MakeToast(getActivity(), "请先登录，再修改信息");
					}
					break;

				}
			}
		});

	}

	public void changeBg(int selectId) {
		for (int i = 0; i < id.length; i++) {
			if (selectId == id[i]) {
				selectView[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.right_selected));
				textViews[i].setTextColor(getResources().getColor(R.color.left_text_select));
			} else {
				selectView[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.right_unselected));
				textViews[i].setTextColor(getResources().getColor(R.color.text_gray_color));
			}
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	private void initDatas() {
		mUser = ApplicationEx.get().getUserManager().getUser();
		if (mUser != null) {
			ThumbnailUtils.loadImage(mUser.getAvatar(), mDefaultImage, ApplicationEx.get()
					.getBitmapLoader(), userhead, 150, 150, 0, true);
			leftLayout.setVisibility(8);
			leftlayout2.setVisibility(0);
			username.setText(mUser.getNickName());
			Utils.setUserLevel(mUser.getWeath(), userLevel);
		} else {
			leftLayout.setVisibility(0);
			leftlayout2.setVisibility(8);
			userhead.setImageBitmap(Utils.toRoundBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.loading_image)));
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initDatas();
	}

}
