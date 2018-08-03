package com.shenjing.dimension.dimension.base.util.downloader;

import android.util.Log;

/**
 * Created by Tiny  on 2016/7/27 17:49.
 * Desc:
 */
public class Logger {

    static final String TAG=FileDownloader.class.getSimpleName();
    static boolean DEBUG=true;


    public static void d(String method, String message){
        if(DEBUG){
            Log.d(TAG,buildMessage(method,message));
        }
    }
    public static void d(String message){
        if(DEBUG){
            Log.d(TAG,buildMessage(null,message));
        }
    }
    public static void w(String method, String message){
        if(DEBUG){
            Log.w(TAG,buildMessage(method,message));
        }
    }
    public static void w(String message){
        if(DEBUG){
            Log.w(TAG,buildMessage(null,message));
        }
    }
    private static String buildMessage(String method, String message){
        StringBuilder builder=new StringBuilder();
        builder.append("Thread = ").append(Thread.currentThread().getName());
        if(method!=null){
            builder.append(" Method = ").append(method);
        }
        builder.append(" message = ").append(message);
        return builder.toString();
    }
}
