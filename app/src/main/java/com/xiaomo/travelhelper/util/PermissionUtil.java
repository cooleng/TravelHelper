package com.xiaomo.travelhelper.util;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行权限工具类
 */

public class PermissionUtil {

    public static final int PERMISSIONS_REQUEST_LOCAL_IMG = 3124;
    public static final int PERMISSIONS_REQUEST_CAMERA_IMG = 3121;

    public static boolean isPermissions(AppCompatActivity context,String permission){

        // 权限未授权
        if(ContextCompat.checkSelfPermission(context,permission)
                != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    public static boolean isPermissions(AppCompatActivity context,String[] permissions){

        // 权限未授权
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(context,permission)
                    != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    public static void requestPermissionsLocalImg(AppCompatActivity context, String[] permission){
        requestPermission(context,permission,PERMISSIONS_REQUEST_LOCAL_IMG);
    }

    public static void requestPermissionsCameraImg(AppCompatActivity context, String[] permissions){

        requestPermission(context,permissions,PERMISSIONS_REQUEST_CAMERA_IMG);

    }

    public static void requestPermission(AppCompatActivity context, String[] permissions,int requestCode) {

        List<String> permissionTemp = new ArrayList<>();

        // 检查权限是否授权
        for(String permission : permissions){
            if(ContextCompat.checkSelfPermission(context,permission)
                    != PackageManager.PERMISSION_GRANTED){
                permissionTemp.add(permission);
            }
        }

        ActivityCompat.requestPermissions(context,permissionTemp.toArray(new String[permissionTemp.size()])
                ,requestCode);
    }




    public static boolean checkRequestResult(int[] grantResults){

        if(grantResults == null || grantResults.length == 0){
            return false;
        }

        boolean flag = true;
        for(int i=0;i< grantResults.length;i++){

            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                flag = false;
            }
        }
        return flag;
    }

}
