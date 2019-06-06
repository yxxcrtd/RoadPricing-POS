package com.kernal.plateid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.format.Time;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import utills.CameraFragment;
import view.VerticalSeekBar;
import view.ViewfinderView;

/**
 * 
 * 
 * 项目名称：plate_id_sample_service 类名称：MemoryCameraActivity 类描述： 视频扫描界面 扫描车牌并识别
 * （与视频流的拍照识别同一界面） 创建人：张志朋 创建时间：2016-1-29 上午10:55:28 修改人：user 修改时间：2016-1-29
 * 上午10:55:28 修改备注：
 * 
 * @version
 * 
 */
public class MemoryCameraActivity extends Activity  implements CameraFragment.FragmentBackValue{
	private static final String PATH = Environment
			.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
	private ImageButton back_btn, flash_btn, back, take_pic;
	private ViewfinderView myview;
	private RelativeLayout re;
	private int width, height;
	private int preWidth,preHeight;
	private String number = "", color = "";
	private Bitmap bitmap, bitmap1;
	private Vibrator mVibrator;
	private PlateRecognitionParameter prp = new PlateRecognitionParameter();;
	private boolean recogType=true;// 记录进入此界面时是拍照识别还是视频识别 true:视频识别 false:拍照识别
	private String path;// 圖片保存的路徑
	private SensorManager sensorManager;
	private float x,y,z;
	//向左旋转
	private boolean Rotate_left = false;
	//正向旋转
	private boolean Rotate_top = true;
	//向右旋转
	private boolean Rotate_right = false;
	//倒置旋转
	private boolean Rotate_bottom = false;
	private CameraFragment fragment;
	private byte[] feedbackData;
	private int[] areas = new int[4];
	private SeekBar seekBar;
	private VerticalSeekBar verticalSeekBar;
	private LayoutParams layoutParams;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_carmera);
		recogType = getIntent().getBooleanExtra("camera", false);
		RecogService.initializeType = recogType;
		setScreenSize(this);
		findiew();
		initRecogView();
	}

	/**
	 *
	 * @param result  识别结果
	 *          result[0]  车牌号码；result[1] 车牌颜色
	 */
	@Override
	public void SendRecogResult(String[] result) {
		getResult(result);
	}

	/**
	 *
	 * @param feedbackData  被识别的帧数据
	 */
	@Override
	public void SentfeedbackData(byte[] feedbackData) {
		this.feedbackData = feedbackData;
	}

	/***
	 *  获取预览分辨率宽高（以便保存图片时使用）
	 * @param preWidth 预览分辨率宽
	 * @param  prewheigth  预览分辨率高
	 */
	@Override
	public void SentPreviewSize(int preWidth, int prewheigth) {
		this.preWidth =  preWidth;
		this.preHeight = prewheigth;
	}

	/**
	 *  识别区域坐标值  即扫描框内的区域  在未识别到车牌时，用来保存区域内图像
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	@Override
	public void SentSensitivenesArea(int left,int top ,int right,int bottom) {
		this.areas[0] =left;
		this.areas[1] =top;
		this.areas[2] =right;
		this.areas[3] =bottom;

	}
	void initRecogView(){

//		myview = new ViewfinderView(this,width,height,true);
//		re.addView(myview);
		GetScreenDirection();
		//recogType  =  false  拍照识别
		if (!recogType) {
			fragment.setRecogModle(false);
			// 拍照按钮
			take_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					fragment.setRecogModle(true);
				}

			});
		}
	}
	@SuppressLint("NewApi")
	private void findiew() {
		// TODO Auto-generated method stub
		flash_btn = (ImageButton) findViewById(R.id.flash_camera);
		back = (ImageButton) findViewById(R.id.back);
		take_pic = (ImageButton) findViewById(R.id.take_pic_btn);
		re = (RelativeLayout) findViewById(R.id.memory);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
		fragment = (CameraFragment) getFragmentManager().findFragmentById(R.id.sampleFragment);
		if (recogType) {
			take_pic.setVisibility(View.GONE);
		} else {
			take_pic.setVisibility(View.VISIBLE);
		}
		int back_w;
		int back_h;
		int flash_w;
		int flash_h;
		int Fheight;
		int take_h;
		int take_w;
		back.setVisibility(View.VISIBLE);
		back_h = (int) (height * 0.066796875);
		back_w = (int) (back_h * 1);
		layoutParams = new LayoutParams(back_w, back_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
				RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		layoutParams.topMargin = (int) (height*0.025);
		layoutParams.leftMargin = (int) (width * 0.050486111111111111111111111111111);
		back.setLayoutParams(layoutParams);

		flash_h = (int) (height * 0.066796875);
		flash_w = (int) (flash_h * 1);
		layoutParams = new LayoutParams(flash_w, flash_h);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
				RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);

		layoutParams.topMargin = (int) (height*0.025);
		layoutParams.rightMargin = (int) (width * 0.050486111111111111111111111111111);
		flash_btn.setLayoutParams(layoutParams);

		take_h = (int) (height * 0.105859375);
		take_w = (int) (take_h * 1);
		layoutParams = new LayoutParams(take_w, take_h);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
				RelativeLayout.TRUE);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		layoutParams.bottomMargin = (int) (height * 0.025);
		take_pic.setLayoutParams(layoutParams);

		layoutParams = new LayoutParams(width*23/24, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.topMargin = (height*2/3);
		seekBar.setLayoutParams(layoutParams);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				fragment.setFocallength((int)(fragment.getFocal()*progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				fragment.setRecogsuspended(true);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				fragment.setRecogsuspended(false);
			}
		});
		layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, height*7/12);
		layoutParams.leftMargin = (width/10);
		layoutParams.topMargin = ( height * 5/24);
		verticalSeekBar.setLayoutParams(layoutParams);
		verticalSeekBar.getFragment(fragment);
		verticalSeekBar.setVisibility(View.GONE);
		verticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				fragment.setFocallength((int)(fragment.getFocal()*progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				fragment.setRecogsuspended(true);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				fragment.setRecogsuspended(false);
			}
		});
				// 竖屏状态下返回按钮
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub\
				/*Intent intent  = new Intent("kernal.plateid.MainActivity");
				startActivity(intent);*/
				finish();
			}
		});
		// 闪光灯监听事件
		flash_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// b = true;
				// TODO Auto-generated method stub
				fragment.setFlash();

			}

		});
	}

	/**
	 * 根据重力感应  获取屏幕状态
	 */
	public void GetScreenDirection(){
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
	}
	private SensorEventListener listener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			x = event.values[SensorManager.DATA_X];
			y = event.values[SensorManager.DATA_Y];
			z = event.values[SensorManager.DATA_Z];
			if(x>7){   //&&y<7
				if(!Rotate_left){
					System.out.println("向左旋转");
					Rotate_bottom = false;
					Rotate_right = false;
					Rotate_top = false;
					Rotate_left =  true;
					rotateAnimation(90,90,take_pic,flash_btn,back);
					ChangView(MemoryCameraActivity.this,false);
				}

			}else if(x<-7){  //&&y<7
				if(!Rotate_right){
					System.out.println("向右旋转");
					Rotate_bottom = false;
					Rotate_right = true;
					Rotate_top = false;
					Rotate_left =  false;
					rotateAnimation(-90,90,take_pic,flash_btn,back);
					ChangView(MemoryCameraActivity.this,false);
				}

			}else if(y<-7){  //&&x<7&&x>-7
				if(!Rotate_bottom){
					System.out.println("倒置旋转");
					Rotate_bottom = true;
					Rotate_right = false;
					Rotate_top = false;
					Rotate_left =  false;
					rotateAnimation(180,90,take_pic,flash_btn,back);
					ChangView(MemoryCameraActivity.this,true);
				}
			}else if(y>7){
				if(!Rotate_top){
					System.out.println("竖屏状态");
					Rotate_bottom = false;
					Rotate_right = false;
					Rotate_top =true;
					Rotate_left =  false;
					rotateAnimation(0,0,take_pic,flash_btn,back);
					ChangView(MemoryCameraActivity.this,true);
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	/***
	 * 旋转动画
	 * @param toDegrees
	 * 旋转的结束角度
	 * @param
	 *
	 */
	private void rotateAnimation(int toDegrees,int toDegrees2, View view1,View view2,View view3 ) {
		view1.animate().rotation(toDegrees).setDuration(500).start();
		view2.animate().rotation(toDegrees).setDuration(500).start();
		view3.animate().rotation(toDegrees2).setDuration(500).start();
	}

	/**
	 *
	 * @param context   改变屏幕布局  根据横竖屏状态修改布局
	 * @param isPortrait
     */
	public void ChangView(Context context,boolean isPortrait){
//		re.removeView(myview);
//		myview = null;
//		myview = new ViewfinderView(context,width,height,isPortrait);
//		re.addView(myview);
		fragment.ChangeState(Rotate_left,Rotate_right,Rotate_top,Rotate_bottom,isPortrait);
		if(Rotate_left){
			seekBar.setVisibility(View.GONE);
			verticalSeekBar.setVisibility(View.VISIBLE);
			layoutParams.leftMargin = (width/10);
			verticalSeekBar.setLayoutParams(layoutParams);
		}else if(Rotate_right){
			seekBar.setVisibility(View.GONE);
			verticalSeekBar.setVisibility(View.VISIBLE);
			layoutParams.leftMargin = (width*4/5);
			verticalSeekBar.setLayoutParams(layoutParams);
		}else{
			seekBar.setVisibility(View.VISIBLE);
			verticalSeekBar.setVisibility(View.GONE);
		}

	}
	/**
	 * 拿到结果之后的处理逻辑
	 * @Title: getResult
	 * @Description: TODO(获取结果)
	 * @param @param fieldvalue 调用识别接口返回的数据
	 * @return void 返回类型
	 * @throwsbyte[]picdata
	 */

	public void getResult(String[] fieldvalue) {
			if (fieldvalue[0] != null && !fieldvalue[0].equals("")) {
				PlateidExist(  fieldvalue);
			} else{
			// 未检测到车牌时执行下列代码
				PlateidNoExist(fieldvalue);
			}
	}

	/**
	 * 识别到车牌之后的处理方法
	 */
	public void PlateidExist( String[] fieldvalue){
		String []resultString =  fieldvalue[0].split(";");
		int lenght = resultString.length;
		Intent intent = new Intent();
		if (lenght == 1) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inPurgeable = true;
			options.inInputShareable = true;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int Height = 0, Width = 0;
			if (Rotate_top || Rotate_bottom) {
				Height = preWidth;
				Width = preHeight;
			} else if (Rotate_left || Rotate_right) {
				Height = preHeight;
				Width = preWidth;
			}
			YuvImage yuvimage = new YuvImage(feedbackData,
					ImageFormat.NV21, Width, Height,
					null);
			yuvimage.compressToJpeg(new Rect(0, 0,
					Width, Height), 100, baos);

			bitmap = BitmapFactory.decodeByteArray(
					baos.toByteArray(), 0, baos.size(),
					options);
			path = savePicture(bitmap);
			mVibrator = (Vibrator) getApplication()
					.getSystemService(
							Service.VIBRATOR_SERVICE);
			mVibrator.vibrate(100);
			number = fieldvalue[0];
			color = fieldvalue[1];
			int left = Integer.valueOf(fieldvalue[7]);
			int top = Integer.valueOf(fieldvalue[8]);
			int w = Integer.valueOf(fieldvalue[9])
							- Integer.valueOf(fieldvalue[7]);
			int h = Integer.valueOf(fieldvalue[10])
							- Integer.valueOf(fieldvalue[8]);

			intent.putExtra("number", number);
			intent.putExtra("color", color);
			intent.putExtra("path", path);
			intent.putExtra("left", left);
			intent.putExtra("top", top);
			intent.putExtra("width", w);
			intent.putExtra("height", h);
			intent.putExtra("recogType", recogType);

		}else {
			String itemString = "";
			mVibrator = (Vibrator) getApplication()
							.getSystemService(
									Service.VIBRATOR_SERVICE);
			mVibrator.vibrate(100);
			for (int i = 0; i < lenght; i++) {
				itemString = fieldvalue[0];
				resultString = itemString.split(";");
				number += resultString[i] + ";\n";
				color += resultString[i] + ";\n";
				itemString = fieldvalue[11];
				resultString = itemString.split(";");
			}
			intent.putExtra("number", number);
			intent.putExtra("color", color);
			intent.putExtra("time", resultString);
			intent.putExtra("recogType", recogType);
		}
		setResult(RESULT_OK,intent);
		MemoryCameraActivity.this.finish();
	}
	/**
	 * 未识别到车牌之后的处理方法
	 */
	public void PlateidNoExist( String[] fieldvalue){
//		if (!recogType){
			//预览识别执行下列代码 不是预览识别 不做处理等待下一帧
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inPurgeable = true;
			options.inInputShareable = true;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			YuvImage yuvimage = new YuvImage(feedbackData, ImageFormat.NV21, preWidth, preHeight, null);
			yuvimage.compressToJpeg(new Rect(0, 0, preWidth, preHeight), 100, baos);
			bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size(), options);
			Matrix matrix = new Matrix();
			matrix.reset();
			if (Rotate_left){
				matrix.setRotate(0);
			}else if (Rotate_top) {
				matrix.setRotate(90);
			} else if (Rotate_right) {
				matrix.setRotate(180);
			} else if (Rotate_bottom) {
				matrix.setRotate(270);
			}
			bitmap1 = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix,
					true);
			path = savePicture(bitmap1);
			mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
			mVibrator.vibrate(100);


			number = fieldvalue[0];
			color = fieldvalue[3];
			if (fieldvalue[0] == null) {
				number = "";
			}
			if (fieldvalue[1] == null) {
				color = "";
			}
			int left = this.areas[0];
			int top = this.areas[1];
			int w = this.areas[2] - this.areas[0];
			int h = this.areas[3] - this.areas[1];
			//返回结果
			Intent intent = new Intent();
			intent.putExtra("number", number);
			intent.putExtra("color", color);
			intent.putExtra("path", path);
			intent.putExtra("left", left);
			intent.putExtra("top", top);
			intent.putExtra("width", w);
			intent.putExtra("height", h);
			intent.putExtra("time", fieldvalue[11]);
			intent.putExtra("recogType", recogType);
			setResult(RESULT_OK,intent);
			finish();
//		}
	}
	/**
	 *
	 * @param bitmap   保存图片
	 * @return
	 */
	public String savePicture(Bitmap bitmap) {
		String strCaptureFilePath = PATH + "plateID_" + pictureName() + ".jpg";
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(strCaptureFilePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strCaptureFilePath;
	}

	/**
	 *
	 * @return  获取系统时间  为图片命名
	 */
	public String pictureName() {
		String str = "";
		Time t = new Time();
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		if (month < 10) {
			str = String.valueOf(year) + "0" + String.valueOf(month);
		}else {
			str = String.valueOf(year) + String.valueOf(month);
		}
		if (date < 10) {
			str = str + "0" + String.valueOf(date + "_");
		}else {
			str = str + String.valueOf(date + "_");
		}
		if (hour < 10) {
			str = str + "0" + String.valueOf(hour);
		}else {
			str = str + String.valueOf(hour);
		}
		if (minute < 10) {
			str = str + "0" + String.valueOf(minute);
		}else {
			str = str + String.valueOf(minute);
		}
		if (second < 10) {
			str = str + "0" + String.valueOf(second);
		}else {
			str = str + String.valueOf(second);
		}
		return str;
	}

	/**
	 *
	 * @Title: setScreenSize
	 * @Description:   获取屏幕真实分辨率，不受虚拟按键影响
	 * @param @param context 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressLint("NewApi")
	private void setScreenSize(Context context) {
		int x, y;
		WindowManager wm = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE));
		Display display = wm.getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point screenSize = new Point();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				display.getRealSize(screenSize);
				x = screenSize.x;
				y = screenSize.y;
			} else {
				display.getSize(screenSize);
				x = screenSize.x;
				y = screenSize.y;
			}
		} else {
			x = display.getWidth();
			y = display.getHeight();
		}
		width = x;
		height = y;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//Intent intent  = new Intent("kernal.plateid.MainActivity");
			//startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (sensorManager != null) {
			sensorManager.unregisterListener(listener);
		}
	}
}
