package com.shenjing.dimension.dimension.base.request.get;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.util.List;

public class CookieUtils {

    public static void synCookie(final Context context, final List<String> urls, String... cookies) {
        if(cookies == null){
            return;
        }
        /** edit  by liht on 2016/10/27 9:12
         *  修改成在子线程中执行，减少耗时，同时去除对外部对象的引用，避免内存泄露
         * */
        final String[] cookiesArray = cookies.clone();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CookieSyncManager.createInstance(context);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();//移除
                for (String cookieUrl : urls){
                    for(String cookie : cookiesArray){
                        cookieManager.setCookie(cookieUrl + "/", cookie);
                    }
                }
                CookieSyncManager.getInstance().sync();
            }
        }).start();

    }

    /**
     * 同步添加cookie
     * @param context
     * @param url
     * @param cookies
     */
    public static void asynCookie(final Context context, String url, String... cookies) {
        if(cookies == null){
            return;
        }
        final String[] cookiesArray = cookies.clone();
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        for(String cookie : cookiesArray){
            cookieManager.setCookie(url, cookie);
        }
        CookieSyncManager.getInstance().sync();
    }

    public static String getStoredCookie(Context context, String url) {
        if(TextUtils.isEmpty(url)){
            return null;
        }
        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeSessionCookie();//移除
        return cookieManager.getCookie(url);
    }

    public static void removeAllCookie(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CookieSyncManager.createInstance(context);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                CookieSyncManager.getInstance().sync();
            }
        }).start();

    }

}
