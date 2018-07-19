package com.shenjing.dimension.dimension.base.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

/**
 * Created by LJ on 2016/6/24.
 */
public class PermissionManager {

    public static final int PERMISSION_CAMERA = 100;
    public static final int PERMISSION_RECORD_AUDIO = 101;
    public static final int PERMISSION_FINE_LOCATION = 102;
    public static final int PERMISSION_READ_CONTACTS = 103;
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 104;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 105;
    public static final int PERMISSION_SYSTEM_ALERT_WINDOW = 106;

    private static PermissionManager inst;
    private OnPermissonRequestListner onPermissonRequestListner;

    public static PermissionManager getInstance()
    {
        if (inst == null){
            inst = new PermissionManager();
        }
        return inst;
    }
    public void setOnPermissonRequestListner(OnPermissonRequestListner onPermissonRequestListner){
        this.onPermissonRequestListner = onPermissonRequestListner;
    }

    public void requestPermisison(Activity activity, int permissonType) {
        String permission = getPermissionStringByType(permissonType);
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, permissonType);
        }else{
            if (onPermissonRequestListner != null){
                onPermissonRequestListner.onPermissonRequestTip(permissonType);
            }
        }
    }

    public boolean isPermissionGranted(Context context, int permissonType) {

        boolean result = true;
        String permission = getPermissionStringByType(permissonType);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int targetSdkVersion = 19;

            try {
                PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                targetSdkVersion = info.applicationInfo.targetSdkVersion;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (targetSdkVersion >= Build.VERSION_CODES.M) {

                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }

    public void toastPermission(Context context, int permissonType){
        String permissionString = "";
        switch (permissonType) {
            case PERMISSION_CAMERA:
                permissionString = "相机";
                break;
            case PERMISSION_RECORD_AUDIO:
                permissionString = "麦克风";
                break;
            case PERMISSION_FINE_LOCATION:
                permissionString = "位置信息";
                break;
            case PERMISSION_READ_CONTACTS:
                permissionString = "读取手机通讯录";
                break;
            case PERMISSION_READ_EXTERNAL_STORAGE:
                permissionString = "读存储空间";
                break;
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                permissionString = "写存储空间";
                break;
        }

        String content = "请到设置-应用-" + getAppName(context) + "-权限中开启"+ permissionString + "权限，以正常使用相应功能";
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public String getAppName(Context context){
        try{
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.applicationInfo.loadLabel(packageManager).toString();

        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    private String getPermissionStringByType(int permission){
        switch (permission){
            case PERMISSION_CAMERA:
                return Manifest.permission.CAMERA;
            case PERMISSION_RECORD_AUDIO:
                return Manifest.permission.RECORD_AUDIO;
            case PERMISSION_FINE_LOCATION:
                return Manifest.permission.ACCESS_FINE_LOCATION;
            case PERMISSION_READ_CONTACTS:
                return Manifest.permission.READ_CONTACTS;
            case PERMISSION_READ_EXTERNAL_STORAGE:
                return Manifest.permission.READ_EXTERNAL_STORAGE;
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                return Manifest.permission.WRITE_EXTERNAL_STORAGE;
            case PERMISSION_SYSTEM_ALERT_WINDOW:
                return Manifest.permission.SYSTEM_ALERT_WINDOW;
            default:
                return "";
        }
    }

    private PermissionManager(){}

    public interface OnPermissonRequestListner{
        public void onPermissonRequestSucceed(int permissionType);
        public void onPermissonRequestFailed(int permissionType);
        public void onPermissonRequestTip(int permissionType);
    }

}
