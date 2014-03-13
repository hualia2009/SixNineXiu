package com.ninexiu.sixninexiu.fragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninexiu.sixninexiu.NineShowMainActivity.IRightTabSelected;
import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.ComposerAdapter;
import com.ninexiu.utils.ComposerAdapter.INduoaComposerAdapter;
import com.ninexiu.utils.PinnedListView;

@SuppressLint("ValidFragment")
public class RightMenuFragment extends Fragment {
	private IRightTabSelected iTabSelected;
	private PinnedListView mPinnedListView;
	ComposerAdapter<AnchorInfo> mAdapter;

	public RightMenuFragment() {
	}

	public RightMenuFragment(IRightTabSelected iTabSelected) {
		this.iTabSelected = iTabSelected;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.right_menu_layout, null);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPinnedListView.setPinnedHeaderView(getActivity().getLayoutInflater().inflate(
				R.layout.item_section_header_view, mPinnedListView, false));
		mAdapter = new ComposerAdapter<AnchorInfo>(getActivity(), R.layout.item_right_menu_title,
				new RightMenuAdapter());
		mAdapter.initList(getDatas());
		mPinnedListView.setAdapter(mAdapter);
	}

	private List<Pair<String, List<AnchorInfo>>> getDatas() {
		List<Pair<String, List<AnchorInfo>>> datas = new ArrayList<Pair<String, List<AnchorInfo>>>();
		List<AnchorInfo> list = new ArrayList<AnchorInfo>();
		list.add(new AnchorInfo("超星", "roomnum"));
		list.add(new AnchorInfo("巨星", "credit"));
		list.add(new AnchorInfo("红星", "jointime"));
		list.add(new AnchorInfo("星星", "jointime"));
		datas.add(new Pair<String, List<AnchorInfo>>("69群星", list));
		try {
			InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(
					"impress.json"));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String result = "";
			while ((line = bufReader.readLine()) != null) {
				result += line;
			}

			JSONObject jo = new JSONObject(result);
			List<AnchorInfo> list2 = new ArrayList<AnchorInfo>();
			for (int i = 1; i <= 20; i++) {
				list2.add(new AnchorInfo(jo.getString(String.valueOf(i)), String.valueOf(i)));
			}
			datas.add(new Pair<String, List<AnchorInfo>>("猜你喜欢", list2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}

	private void initView(View view) {
		mPinnedListView = (PinnedListView) view.findViewById(R.id.pinnedlistview);
	}

	public interface HostSelectedListener {
		public void hostTypeSelected(int type);
	}

	class AnchorInfo {
		String title;
		String type;

		public AnchorInfo(String title, String type) {
			super();
			this.title = title;
			this.type = type;
		}
	}

	class RightMenuAdapter implements INduoaComposerAdapter<AnchorInfo> {
		private int selectedIndex;
		@Override
		public void configurePinnedHeader(View header, int position) {
			TextView sectionHeader = (TextView) header;
			sectionHeader.setText(mAdapter.getSections()[mAdapter.getSectionForPosition(position)]);
		}

		@Override
		public void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
			final TextView sectionTitle = (TextView) view
					.findViewById(R.id.common_mg_section_header);
			if (displaySectionHeader) {
				sectionTitle.setVisibility(View.VISIBLE);
				sectionTitle.setText(mAdapter.getSections()[mAdapter
						.getSectionForPosition(position)]);
			} else {
				sectionTitle.setVisibility(View.GONE);
			}
		}

		@Override
		public View getCompositeView(final int position, final View convertView, ViewGroup parent,
				final AnchorInfo value) {
			TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
			titleTextView.setText(value.title);
			final View rightItemView = convertView.findViewById(R.id.right_item);
			if (selectedIndex == position) {
				rightItemView.setSelected(true);
			} else {
				rightItemView.setSelected(false);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectedIndex = position;
					rightItemView.setSelected(true);
					iTabSelected.setSelected(value.type);
					mAdapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}

	}

}
