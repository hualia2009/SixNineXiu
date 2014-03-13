package com.ninexiu.sixninexiu;

import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ninexiu.sixninexiu.R;
import com.ninexiu.utils.Utils;
import com.umeng.analytics.MobclickAgent;

public class AdActivity extends BaseActivity {

	private WebView webView;
	private Dialog mDialog;
	private WebSettings webSettings;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_deatil_layout);
		webView=(WebView) findViewById(R.id.webView1);
		url=getIntent().getStringExtra("url");
		mDialog=Utils.showProgressDialog(this, "加载中...");
		mDialog.show();
//		Bundle args=new Bundle();
//		args.putString("msg", "加载中...");
//		showDialog(SHOW_PROGRESS_DILAOG, args);
		tvTitle.setText("活动详细");
		webSettings=webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("UTF-8");
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url!=null){
					view.loadUrl(url);
				}
				return true;
			}
		});
		
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress==100){
					if(mDialog.isShowing()){
						mDialog.dismiss();
					}
//					dismissDialog(SHOW_PROGRESS_DILAOG);
				}
			}
		});
	}
	
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
