package com.ninexiu.sixninexiu;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ninexiu.crop.CropImageActivity;
import com.ninexiu.sixninexiu.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户修改头像的界面
 * 
 * @author Administrator
 * 
 */
public class SelectUserHeadActivity extends Activity {

	private static String TAG="ChooseUserHead";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	private static String localTempImageFileName = "";
	public static final String IMAGE_PATH = "nineShow";
	public static final File FILE_SDCARD = Environment
            .getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD,IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
            "images/screenshots");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_select_head);
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

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.btn_camera:
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				try {
					localTempImageFileName = "";
					localTempImageFileName = String.valueOf((new Date()).getTime()) + ".png";
					File filePath = FILE_PIC_SCREENSHOT;
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(filePath, localTempImageFileName);
					// localTempImgDir和localTempImageFileName是自己定义的名字
					Uri u = Uri.fromFile(f);
					intent.putExtra(ImageColumns.ORIENTATION, 0);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
					startActivityForResult(intent, FLAG_CHOOSE_PHONE);
				} catch (ActivityNotFoundException e) {
					//
				}
			}else{
				Toast.makeText(this, "检测到sdcard不存在", Toast.LENGTH_LONG).show();
			}
			break;

		case R.id.btn_select_image:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, FLAG_CHOOSE_IMG);
			break;

		case R.id.btn_cancel:
			finish();
			break;

		case R.id.view_layout_head:
			finish();
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaColumns.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(this, "图片没找到", 0).show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor.getColumnIndex(MediaColumns.DATA));
					cursor.close();
					Log.i(TAG,"path=" + path);
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Log.i(TAG,"path=" + uri.getPath());
					Intent intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			File f = new File(FILE_PIC_SCREENSHOT,localTempImageFileName);
			Intent intent = new Intent(this, CropImageActivity.class);
			intent.putExtra("path", f.getAbsolutePath());
			startActivityForResult(intent, FLAG_MODIFY_FINISH);
		}else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				Log.i(TAG, "截取到的图片路径是 = " + path);
//				head.setImageBitmap(b);
				Intent datas=new Intent();
				datas.putExtra("url", path);
				setResult(RESULT_OK, datas);
				finish();
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}

