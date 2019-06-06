package utills;
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.Time;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

import com.kernal.plateid.PlateCfgParameter;
import com.kernal.plateid.RecogService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import view.ViewfinderView;


@SuppressLint({"NewApi"})
public class CameraFragment extends Fragment implements SurfaceHolder.Callback {
    private static final String TAG = CameraFragment.class.getSimpleName();
    private CameraManager cameraManager;
    private boolean hasSurface;
    private SurfaceView surfaceView;
    public int deviceIdx;
    private int iInitPlateIDSDK=-1;
    public RecogService.MyBinder recogBinder;
    private int imageformat = 6;// NV21 -->6
    private int bVertFlip = 0;
    private int bDwordAligned = 1;
    private FragmentBackValue callback;
    private boolean modle = false;
    private boolean ToastShow = true;
    private ViewfinderView myview;
	private int width, height;
	private FrameLayout frameLayout;
    //预览分辨率
    private int preW =1920,preH =1080;
    public ServiceConnection recogConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            recogConn = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            recogBinder = (RecogService.MyBinder) service;
            iInitPlateIDSDK = recogBinder.getInitPlateIDSDK();
            if (iInitPlateIDSDK != 0) {
                Toast.makeText(getActivity(),"错误码："+iInitPlateIDSDK,Toast.LENGTH_LONG).show();
            }
            PlateCfgParameter cfgparameter = new PlateCfgParameter();
            cfgparameter.armpolice = 4;
            cfgparameter.armpolice2 = 16;
            cfgparameter.embassy = 12;
            cfgparameter.individual = 0;
            // cfgparameter.nContrast = 9;
            cfgparameter.nOCR_Th = 0;
            cfgparameter.nPlateLocate_Th = 5;
            cfgparameter.onlylocation = 15;
            cfgparameter.tworowyellow = 2;
            cfgparameter.tworowarmy = 6;
            cfgparameter.szProvince = "";
            cfgparameter.onlytworowyellow = 11;
            cfgparameter.tractor = 8;
            cfgparameter.bIsNight = 1;
            //新能源车牌开启
            cfgparameter.newEnergy  = 24;
            //领事馆车牌开启;
            cfgparameter.consulate = 22;

            recogBinder.setRecogArgu(cfgparameter, imageformat, bVertFlip,
                    bDwordAligned);
            cameraManager.setData(recogBinder,iInitPlateIDSDK);
        }
    };
    public CameraFragment() {
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.cameraManager = new CameraManager(this.getActivity().getApplication(), this.getView());
        cameraManager.setFragment(this);
        cameraManager.setPreviewSize(preW,preH);
        Intent authIntent = new Intent(getActivity(),
                RecogService.class);
        getActivity().bindService(authIntent, recogConn, Service.BIND_AUTO_CREATE);
        
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View codeView = this.createViewFromCode(inflater, container, savedInstance);
        Window window = this.getActivity().getWindow();
        window.addFlags(128);
        
        
        return codeView;
    }
    //添加View
    @SuppressLint({"NewApi"})
    private View createViewFromCode(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        Display display = this.getActivity().getWindowManager().getDefaultDisplay();
        Boolean isPortrait = Boolean.valueOf(display.getWidth() < display.getHeight());
        //添加 surfaceView
        this.surfaceView = new CameraSurfaceView(this.getActivity(), isPortrait.booleanValue(),preW,preH);
         frameLayout = new FrameLayout(this.getActivity());
        LayoutParams frameLayoutpm = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(frameLayoutpm);
        frameLayout.addView(this.surfaceView, frameLayoutpm);
       
        //添加 扫描框
        setScreenSize(getActivity());
        myview = new ViewfinderView(getActivity(),width,height,true);
        frameLayoutpm= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(frameLayoutpm);
        frameLayout.addView( myview, frameLayoutpm );
        
        
        return frameLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (FragmentBackValue) getActivity();
       
    }

    //开启相机
    private boolean initCamera(SurfaceHolder surfaceHolder) {
        if(surfaceHolder == null) {
            throw new IllegalStateException("没有SurfaceHolder");
        }
    else {
            try {
                this.cameraManager.openDriver(surfaceHolder, this.deviceIdx);
            } catch (IOException var3) {
                LogUtil.E(TAG, var3.getMessage());
                return false;
            } catch (RuntimeException var4) {
                LogUtil.E(TAG, "camera init fail");
                return false;
            } catch (Exception var5) {
                LogUtil.E(TAG, "camera init fail");
                return false;
            }

            //预览分辨率


            return true;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
            SurfaceHolder surfaceHolder = this.surfaceView.getHolder();
            if(this.hasSurface) {
                boolean isOpen = this.initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(3);
            }

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(!this.hasSurface) {
            this.hasSurface = true;
            boolean isOpen = this.initCamera(holder);
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hasSurface = false;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recogBinder != null) {
            getActivity().unbindService(recogConn);
            recogBinder = null;

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        cameraManager.stopPreview();
        cameraManager.closeDriver();
       

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 改变状态  即根据重力感应获取的当前旋转状态，以便计算识别区域
     * @param Left
     * @param Right
     * @param Top
     * @param Bottom
     */
    public void ChangeState(boolean Left, boolean Right, boolean Top, boolean Bottom,boolean isPortrait){
        cameraManager.ChangeState(Left,Right,Top,Bottom);
    	frameLayout.removeView(myview);
		myview = new ViewfinderView(getActivity(),width,height,isPortrait);
		frameLayout.addView(myview);
    }

    /**
     *   当识别结束  向Activity传递结果
     * @param result
     * @param Currentdata
     */
    public void getRecogResult(String[] result,byte[] Currentdata){
        callback.SentPreviewSize(cameraManager.getPreviewSize().x,cameraManager.getPreviewSize().y);
        //modle识别模式 默认为false，即视频识别  改为true，则说明为拍照识别且点击了拍照按钮
            if(modle){
                cameraManager.stopPreview();
                if(Currentdata==null){
                    callback.SentfeedbackData(recogBinder.getRecogData());
                }else{
                    callback.SentfeedbackData(Currentdata);
                }
                callback.SendRecogResult(result);
            }else{
                if(Currentdata==null){
                    cameraManager.stopPreview();
                    callback.SentfeedbackData(recogBinder.getRecogData());
                    callback.SendRecogResult(result);
//                    saveNV21(recogBinder.getRecogData());
                }

            }


    }
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
        if (month < 10)
            str = String.valueOf(year) + "0" + String.valueOf(month);
        else {
            str = String.valueOf(year) + String.valueOf(month);
        }
        if (date < 10)
            str = str + "0" + String.valueOf(date + "_");
        else {
            str = str + String.valueOf(date + "_");
        }
        if (hour < 10)
            str = str + "0" + String.valueOf(hour);
        else {
            str = str + String.valueOf(hour);
        }
        if (minute < 10)
            str = str + "0" + String.valueOf(minute);
        else {
            str = str + String.valueOf(minute);
        }
        if (second < 10)
            str = str + "0" + String.valueOf(second);
        else {
            str = str + String.valueOf(second);
        }
//        str = str + String.valueOf(second);
        return str;
    }
    private static final String PATH = Environment
            .getExternalStorageDirectory().toString() + "/DCIM/Camera/";
    public void saveNV21(byte[] Currentdata){
        String strCaptureFilePath = PATH+ "test_" + pictureName() + ".nv21";
        File dir = new File(PATH);
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(strCaptureFilePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(Currentdata);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 判断是都点击了拍照按钮  默认false  点击拍照按钮修改为true
     * @param modle
     */
    public void setRecogModle(boolean modle){
        cameraManager.setRecogModle(modle);
        this.modle = modle;
    }

    /**
     *  错误码返回
     * @param nRet
     */
    public void getnRet(int nRet){
            if(ToastShow){
                Toast.makeText(getActivity(),"错误码："+nRet+"\n"+"请查阅开发手册！",Toast.LENGTH_LONG).show();
                ToastShow = false;
            }

    }

    /**
     * 根据seekbar位置 设置焦距
     * @param progress
     */
    public void setFocallength(int progress){
       cameraManager.setFocallength(progress);
    }

    /**
     * seekbar 单位长度对应的焦距值
     * @return
     */
    public double  getFocal(){

        return (double)cameraManager.getFocal();
    }

    /**
     * 获取识别区域坐标  返回给Activty
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void getArea(int left,int top ,int right,int bottom){
        callback.SentSensitivenesArea(left,top,right,bottom);
    }

    /**
     *  调整seekbar时  判断是否暂停执行识别函数  点击时暂停，调整完毕解除
     * @param issuspended
     */
    public void setRecogsuspended(boolean issuspended){
        cameraManager.setRecogsuspended(issuspended);
    }

    /**
     * 控制闪光灯
     */
    public void setFlash(){
        cameraManager.setTorch();
    }
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
    /**
     * 自定义回调接口  用于与Acticiry互动
     */
    public interface FragmentBackValue{
    /**
     * 识别结果  包括车牌号码  颜色等识别信息
     * @param result
     */
        public void SendRecogResult(String[] result);

    /**
     * 视频识别，识别成功后，识别核心会将识别的那帧数据返回
     * @param
     */
        public void SentfeedbackData(byte[] feedbackData);

    /**
     *
     * @param preWidth 预览分辨率宽
     * @param  prewheigth  预览分辨率高
     */
        public void SentPreviewSize(int preWidth, int prewheigth);

    /**
     *  敏感区域  常用于拍照识别时，未能识别成功，用之前计算出的真实的识别区域来裁切图片（即扫描框内区域）
     * @param
     */
        public void SentSensitivenesArea(int left, int top, int right, int bottom);
    }
}
