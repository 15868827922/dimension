package com.shenjing.dimension.dimension.base.cathe;

import android.content.Context;
import android.content.SharedPreferences;

import com.shenjing.dimension.BuildConfig;


/**
 * Created by yons on 2018/3/29.
 * Desc:
 */

public class SharePreferenceUtil {

    public static final String SP_SETTING = "setting";
    private static final String KEY_HAND_SERVER = "handServer"; //手动配置的地址
    private static final String KEY_CHOOSED_SERVER = "choosedServer";

    private static final String KEY_IS_SHOW_LOG = "isShowLog";//是否显示日志的开关

    private static final String KEY_HOST_IS_HTTP = "host_is_http"; //上次选择的域名是否是http

    public static final String SP_UserInfo = "userinfo";
    public static final String SP_Key_UserName = "username";
    public static final String SP_KEY_USERID = "userid";
    public static final String SP_Key_Password = "password";
    public static final String SP_Key_UserInfo = "user_info";

    public static final String KEY_SETTING_GAME_VIBRATE = "settingGameVirate";
    public static final String KEY_SETTING_GAME_VIOCE = "settingGameVoice";
    public static final String KEY_SETTING_GAME_Effect = "settingGameEffect";


    public static final String SHOW_TIPS = "showTips";
    public static final String KEY_SHOWNOSUBBRACH = "showMainTip";



    public static final String KEY_SERVICE_MENU="serviceMenuList";//第三方服务菜单列表缓存

    public static void setKeyHandServer(Context context, String handServer){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        sp.edit().putString(KEY_HAND_SERVER, handServer).commit();
    }

    public static String getHandServer(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        return sp.getString(KEY_HAND_SERVER, null);
    }


    public static void setKeyChoosedServer(Context context, int choosedServer){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        sp.edit().putInt(KEY_CHOOSED_SERVER, choosedServer).commit();
    }

    public static int getChoosedServer(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        return sp.getInt(KEY_CHOOSED_SERVER, -1);
    }

    public static boolean getIsShowLog(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        return sp.getBoolean(KEY_IS_SHOW_LOG, BuildConfig.DEBUG);
    }

    public static void saveIsShowLog(Context context, boolean isShowLog){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_IS_SHOW_LOG, isShowLog).commit();
    }


    public static boolean getHostIsHttp(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        return sp.getBoolean(KEY_HOST_IS_HTTP, false);
    }

    public static void saveHostIsHttp(Context context, boolean isHttp){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_HOST_IS_HTTP, isHttp).commit();
    }

    public static String getSavedUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_UserInfo, Context.MODE_PRIVATE);
        return sp.getString(SP_Key_UserName, "15868827922");
    }

    public static void saveServiceMenuList(Context context, String text){
        if(context==null){
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, context.MODE_PRIVATE);
        sp.edit().putString(KEY_SERVICE_MENU,text).commit();
    }

/*    public static void saveUserNameAndPassword(Context context, String userName, long userId,  String password) {
        SharedPreferences sp = context.getSharedPreferences(SP_UserInfo, Context.MODE_PRIVATE);
        sp.edit().putString(SP_Key_UserName, userName).putLong(SP_KEY_USERID, userId).putString(SP_Key_Password, SecurityUtils.getEncryptPasswordText(context, password))
                .commit();
    }*/

    public static String getSavedUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_UserInfo, Context.MODE_PRIVATE);
        return sp.getString(SP_KEY_USERID, "");
    }

    public static void savedUserId(Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences(SP_UserInfo, Context.MODE_PRIVATE);
        sp.edit().putString(SP_KEY_USERID,  id).commit();
    }

    public static String getSavedPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_UserInfo, Context.MODE_PRIVATE);
        return sp.getString(SP_Key_Password, "");
    }

    public static void savePassword(Context context, String password) {
        SharedPreferences sp = context.getSharedPreferences(SP_UserInfo, Context.MODE_PRIVATE);
        sp.edit().putString(SP_Key_Password,  password).commit();
    }


    public static void saveUserName(Context context, String userName) {
        SharedPreferences sp = context.getSharedPreferences(SP_UserInfo, Context.MODE_PRIVATE);
        sp.edit().putString(SP_Key_UserName, userName).commit();
    }

    public static void setGameViratable(Context context, boolean isAble){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, Context.MODE_MULTI_PROCESS);
        sp.edit().putBoolean(KEY_SETTING_GAME_VIBRATE, isAble).commit();
    }

    public static boolean getGameViratable(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, Context.MODE_MULTI_PROCESS);
        return sp.getBoolean(KEY_SETTING_GAME_VIBRATE, false);
    }


    public static void setGameVoiceAble(Context context, boolean isAble){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, Context.MODE_MULTI_PROCESS);
        sp.edit().putBoolean(KEY_SETTING_GAME_VIOCE, isAble).commit();
    }


    public static boolean getGameVoiceAble(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, Context.MODE_MULTI_PROCESS);
        return sp.getBoolean(KEY_SETTING_GAME_VIOCE, true);
    }


    public static void setGameEffectAble(Context context, boolean isAble){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, Context.MODE_MULTI_PROCESS);
        sp.edit().putBoolean(KEY_SETTING_GAME_Effect, isAble).commit();
    }


    public static boolean getGameEffectAble(Context context){
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING, Context.MODE_MULTI_PROCESS);
        return sp.getBoolean(KEY_SETTING_GAME_Effect, true);
    }


    public static void saveShowMainTip(Context context, boolean showNoSubbrach){
        SharedPreferences sp = context.getSharedPreferences(SHOW_TIPS, Context.MODE_MULTI_PROCESS);
        sp.edit().putBoolean(KEY_SHOWNOSUBBRACH, showNoSubbrach).commit();
    }

    public static boolean getShowMainTip(Context context){
        SharedPreferences sp = context.getSharedPreferences(SHOW_TIPS, Context.MODE_MULTI_PROCESS);
        return sp.getBoolean(KEY_SHOWNOSUBBRACH,false);
    }

}
