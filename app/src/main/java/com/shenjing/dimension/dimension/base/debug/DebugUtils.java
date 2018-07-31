package com.shenjing.dimension.dimension.base.debug;

import android.content.Context;

import com.shenjing.dimension.dimension.base.request.URLManager;


public class DebugUtils {
    
    public static final String DEBUG_PASSWORD = "kissevan";

    private static Configuration mCurrentConfiguration;

    public static void init(Configuration config){
        mCurrentConfiguration =config;
    }

    public static void showDebugDialog(Context context){
        DebugActivity.startActivity(context, mCurrentConfiguration);
    }

    public static void setSelectServer(Configuration configuration){
        if(configuration.id!=mCurrentConfiguration.id){
            mCurrentConfiguration=configuration;
        }
    }

    public static String getCurrHost(){
        if (mCurrentConfiguration!=null){
            String tmp = new String(mCurrentConfiguration.serverHostUrl);
            tmp = tmp.replace(URLManager.PROTOCOL_DEFAULT,"");
            tmp = tmp.substring(0,tmp.indexOf("/assistant"));
            return tmp;
        }
        return null;
    }
}
