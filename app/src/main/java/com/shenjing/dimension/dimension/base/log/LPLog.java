package com.shenjing.dimension.dimension.base.log;

import android.util.Log;

public class LPLog {
    public static boolean enable = true;

    public static boolean enable_speed_test = true;

    public static void initLogConfig(boolean logEnable, boolean speedLogEnable){
        enable = logEnable;
        enable_speed_test = speedLogEnable;
    }

    public static void print(Class claxx, String msg){
        if(enable){
            Log.v(LPLog.class.getSimpleName() + "_" + (claxx == null? "" : claxx.getSimpleName()), msg);
        }
    }

    public static void print(String tag, String msg){
        if(enable){
            Log.v(tag + "", msg);
        }
    }

    public static void printSpeedLog(String tag, String msg, long startProcessTimeMillis){
        if(enable && enable_speed_test){
            Log.v(LPLog.class.getSimpleName() + "_" + tag, msg + " (耗时" + (System.currentTimeMillis() - startProcessTimeMillis) + "毫秒)");
        }
    }
    public static void warn(Class claxx, String message){
        if(enable && enable_speed_test){
            Log.w(LPLog.class.getSimpleName() + "_" + (claxx == null? "" : claxx.getSimpleName()) , message);
        }
    }
}
