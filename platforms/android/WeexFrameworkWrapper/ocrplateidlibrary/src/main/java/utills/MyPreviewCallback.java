//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package utills;

import android.graphics.Bitmap;
import android.hardware.Camera;

import com.kernal.plateid.PlateRecognitionParameter;
import com.kernal.plateid.RecogService;

public final class MyPreviewCallback implements Camera.PreviewCallback {
    private final CameraConfigurationManager configManager;
    private int iInitPlateIDSDK=-1;
    private RecogService.MyBinder recogBinder;
    private String[] fieldvalue = new String[14];
    //向左旋转
    private boolean Rotate_left = false;
    //正向旋转
    private boolean Rotate_top = true;
    //向右旋转
    private boolean Rotate_right = false;
    //倒置旋转
    private boolean Rotate_bottom = false;
    private int nRet=-2;
    private CameraFragment fragment;
    boolean moddle = true;
    private PlateRecognitionParameter prp = new PlateRecognitionParameter();
    private boolean issuspended = false;
    MyPreviewCallback(CameraConfigurationManager configManager) {
        this.configManager = configManager;

    }
    void getRecogModle(boolean moddle){
        this.moddle = moddle;
    }
    void  setRecogResult(String[] recogResult,int nRet,byte[] Currentdata){
            if (nRet!=0){
                fragment.getnRet(nRet);
            }else{
                if(recogResult[0]!=null&&!recogResult[0].equals("")){
                    fragment.getRecogResult(recogResult,null);
                }else {
                    fragment.getArea(prp.plateIDCfg.left, prp.plateIDCfg.top, prp.plateIDCfg.right, prp.plateIDCfg.bottom);
                    fragment.getRecogResult(recogResult, Currentdata);
                }
            }

    }
    void getData(RecogService.MyBinder service, int iInitPlateIDSDK){
        this.iInitPlateIDSDK = iInitPlateIDSDK;
        recogBinder = service;
    }
    void ChangeState(boolean Left,boolean Right,boolean Top,boolean Bottom){
        this.Rotate_left = Left;
        this.Rotate_right = Right;
        this.Rotate_top = Top;
        this.Rotate_bottom = Bottom;
    }
    void getFragment(CameraFragment fragment){
            this.fragment = fragment;
    }
    public void setRecogsuspended(boolean issuspended){
        this.issuspended = issuspended;

    }
    Bitmap bitmap;
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(issuspended){
            return;
        }
        if(iInitPlateIDSDK==0){
            prp.height =  configManager.cameraResolution.y;
            prp.width =  configManager.cameraResolution.x;
            // 开发码
            prp.devCode = Devcode.DEVCODE;
            prp.picByte = data;
            prp.plateIDCfg.scale=1; //其值为0  不缩放
                                    //其值为1   缩放一倍
                                    //其值为2   缩放两倍  一般用于较大分辨率，起码是1920*1080以上
            if (Rotate_left) {
                // 通知识别核心,识别前图像应先旋转的角度
                prp.plateIDCfg.bRotate = 0;
                setHorizontalRegion();
                Rotate_left = false;
            } else if (Rotate_top) {
                prp.plateIDCfg.bRotate = 1;
                setLinearRegion();
                Rotate_top = false;
            } else if (Rotate_right) {
                prp.plateIDCfg.bRotate = 2;
                setHorizontalRegion();
                Rotate_right = false;
            } else if (Rotate_bottom) {
                prp.plateIDCfg.bRotate = 3;
                setLinearRegion();
                Rotate_bottom = false;
            }
            if(moddle){
                fieldvalue = recogBinder.doRecogDetail(prp);
                nRet = recogBinder.getnRet();
                setRecogResult(fieldvalue,nRet,data);
            }
        }
    }

    /**
     * 设置横屏时的真实识别区域
     * configManager.cameraResolution.x：预览分辨率的宽
     * configManager.cameraResolution.y：预览分辨率的高
     */
	private void setHorizontalRegion() {
		prp.plateIDCfg.left =configManager.cameraResolution.x/4;
		prp.plateIDCfg.top = configManager.cameraResolution.y/4;
		prp.plateIDCfg.right =configManager.cameraResolution.x/4+configManager.cameraResolution.x/2;
		prp.plateIDCfg.bottom =configManager.cameraResolution.y-configManager.cameraResolution.y/4;
//		System.out.println("横屏时   左  ："+prp.plateIDCfg.left+"   右  ："+prp.plateIDCfg.right+"     高："+prp.plateIDCfg.top+"    底："+prp.plateIDCfg.bottom);
	}

    /**
     * 设置竖屏时的真实识别区域
     * configManager.cameraResolution.x：预览分辨率的宽
     * configManager.cameraResolution.y：预览分辨率的高
     */
	private void setLinearRegion() {
        prp.plateIDCfg.left = configManager.cameraResolution.y/24;
        prp.plateIDCfg.top = configManager.cameraResolution.x/4;
        prp.plateIDCfg.right = configManager.cameraResolution.y/24+configManager.cameraResolution.y*11/12;
        prp.plateIDCfg.bottom =  configManager.cameraResolution.x/4+configManager.cameraResolution.x/3;
//		 System.out.println("竖屏时      左  ："+prp.plateIDCfg.left+"   右  ："+prp.plateIDCfg.right+"     高："+prp.plateIDCfg.top+"    底："+prp.plateIDCfg.bottom);
	}

}
