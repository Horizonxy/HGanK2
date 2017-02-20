package com.horizon.gank.hgank.util;

import android.content.Context;

import com.horizon.gank.hgank.BuildConfig;

/**
 * Created by Administrator on 2016/8/22.
 */
public class AppUtils {

    public static void initCarsh(Context context){
        if(!BuildConfig.DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(context);
        }
    }
}
