package com.horizon.gank.hgank.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.horizon.gank.hgank.Constants;

public class PermissionUtils {

    public static boolean checkPermissions(Context context, String[] permissions){
        if(permissions != null && permissions.length > 0){
            for (String string : permissions) {
                if(ContextCompat.checkSelfPermission(context, string) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    public static void requestPermissions(Activity activity, String[] permissions){
        if(permissions != null && permissions.length > 0){
            try {
                ActivityCompat.requestPermissions(activity, permissions, Constants.REQ_PERMISSIONS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
