package com.ninexiu.sixninexiu.fragment;

import com.ninexiu.sixninexiu.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.TextView;

public class BaseFragment extends Fragment {

	private LayoutInflater mLayoutInflater;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLayoutInflater = getLayoutInflater(savedInstanceState);
		TextView title = (TextView) getView().findViewById(R.id.title_text);
		if (null != title && getTopTitle() > 0) {
			title.setText(getTopTitle());
		}
	}

	public void setBundle(Bundle bundle) {
	}
	
	public LayoutInflater getLayoutInflater() {
		return mLayoutInflater;
	}
	
	/**
	 * 设置顶部标题
	 * @return
	 */
	public int getTopTitle() {
		return 0;
	}
	
}
