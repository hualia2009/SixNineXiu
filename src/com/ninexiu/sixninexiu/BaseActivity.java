package com.ninexiu.sixninexiu;

import com.ninexiu.sixninexiu.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends Activity {
	public TextView tvTitle;
	public InputMethodManager methodManager;
//	public static final int SHOW_PROGRESS_DILAOG=100;
	public ImageView imageView;
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		tvTitle=(TextView) findViewById(R.id.title_text);
		methodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	}
	
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.left_bt:
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			try {
				if(getCurrentFocus()!=null)
				methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	/*@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case SHOW_PROGRESS_DILAOG:
			Dialog dialog=new Dialog(this, R.style.loading_dialog);
			dialog.setCanceledOnTouchOutside(false);
			View view=LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
			dialog.setContentView(view);
			TextView tv=(TextView) view.findViewById(R.id.tipTextView);
			tv.setText(args.getString("msg"));
			 imageView=(ImageView) view.findViewById(R.id.img);
			imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_animation));
			return dialog;

		default:
			break;
		}
		return null;
	}
	
	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		super.onPrepareDialog(id, dialog, args);
		imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_animation));
	}*/
	
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
