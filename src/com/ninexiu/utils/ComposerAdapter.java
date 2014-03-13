package com.ninexiu.utils;

import java.util.List;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ComposerAdapter<T> extends AdapterComposite {
	List<Pair<String, List<T>>> mAllComposerData;

	private Context mContext;

	private INduoaComposerAdapter<T> iCallBack;

	private LayoutInflater mInflater;

	private int mResId;

	private boolean isEnableForItem;

	public ComposerAdapter(Context context, int resId, INduoaComposerAdapter<T> callBack) {
		mContext = context;
		iCallBack = callBack;
		mResId = resId;
		mInflater = LayoutInflater.from(mContext);
	}

	public ComposerAdapter(Context context, List<Pair<String, List<T>>> list, int resId,
			INduoaComposerAdapter<T> callBack) {
		mContext = context;
		iCallBack = callBack;
		mResId = resId;
		mInflater = LayoutInflater.from(mContext);
		mAllComposerData = list;
	}

	public void initList(List<Pair<String, List<T>>> list) {
		mAllComposerData = list;
	}

	/**
	 * 设置Item是否可以点击
	 * 
	 * @param isEnableForItem
	 */
	public void setEnable(boolean isEnableForItem) {
		this.isEnableForItem = isEnableForItem;
	}

	@Override
	public boolean isEnabled(int arg0) {
		if (isEnableForItem) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 重新设置数据
	 * 
	 * @param list
	 */
	public void resetComposerList(List<Pair<String, List<T>>> list) {
		mAllComposerData = list;
	}

	public List<Pair<String, List<T>>> getAllComposerList() {
		return mAllComposerData;
	}

	@Override
	public int getCount() {
		int res = 0;
		for (int i = 0; i < mAllComposerData.size(); i++) {
			res += mAllComposerData.get(i).second.size();
		}
		return res;
	}

	@Override
	public T getItem(int position) {
		int c = 0;
		for (int i = 0; i < mAllComposerData.size(); i++) {
			if (position >= c && position < c + mAllComposerData.get(i).second.size()) {
				return mAllComposerData.get(i).second.get(position - c);
			}
			c += mAllComposerData.get(i).second.size();
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	protected void onNextPageRequested(int page) {

	}

	@Override
	protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
		iCallBack.bindSectionHeader(view, position, displaySectionHeader);
	}

	@Override
	public View getCompositeView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(mResId, parent, false);
		}

		iCallBack.getCompositeView(position, convertView, parent, getItem(position));

		return convertView;
	}

	@Override
	public void configurePinnedHeader(View header, int position) {
		iCallBack.configurePinnedHeader(header, position);
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0) {
			section = 0;
		}
		if (section >= mAllComposerData.size()) {
			section = mAllComposerData.size() - 1;
		}
		int c = 0;
		for (int i = 0; i < mAllComposerData.size(); i++) {
			if (section == i) {
				return c;
			}
			c += mAllComposerData.get(i).second.size();
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		int c = 0;
		for (int i = 0; i < mAllComposerData.size(); i++) {
			if (position >= c 
					&& position < c + mAllComposerData.get(i).second.size()) {
				return i;
			}
			c += mAllComposerData.get(i).second.size();
		}
		return -1;
	}

	@Override
	public String[] getSections() {
		String[] res = new String[mAllComposerData.size()];
		for (int i = 0; i < mAllComposerData.size(); i++) {
			res[i] = mAllComposerData.get(i).first;
		}
		return res;
	}

	public interface INduoaComposerAdapter<T> {
		void configurePinnedHeader(View header, int position);
		void bindSectionHeader(View view, int position, boolean displaySectionHeader);
		View getCompositeView(int position, View convertView, ViewGroup parent, T value);
	}
}
