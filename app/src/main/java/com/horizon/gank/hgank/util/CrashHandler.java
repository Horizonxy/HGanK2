package com.horizon.gank.hgank.util;

import android.app.Activity;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 未捕获异常处理
 * @author Administrator
 */
public class CrashHandler implements UncaughtExceptionHandler {

	 /** CrashHandler实例 */  
    private static CrashHandler INSTANCE;  
    
	private Activity aty;
	
	 /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {  
    }  
  
    /** 获取CrashHandler实例 ,单例模式 */  
    public static CrashHandler getInstance() {  
        if (INSTANCE == null)  
            INSTANCE = new CrashHandler();  
        return INSTANCE;  
    }  
    
    public void init(Activity aty) {  
    	this.aty = aty;
    	Thread.setDefaultUncaughtExceptionHandler(this);  
    }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		System.exit(0);
	}
	
	/** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑 
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false 
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return true;  
        }  
  
//		new Handler(Looper.getMainLooper()).post(new Runnable() {
//			@Override
//			public void run() {
//				ToastUtils.show(aty, "抱歉，程序出错了...");
//			}
//		});
        
        return true;  
    }  

}
