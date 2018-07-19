package com.shenjing.dimension.dimension.base.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.shenjing.dimension.dimension.base.activity.FundmentalActivity;

import java.lang.reflect.Field;

/**
 * Created by ZZQ on 2017/9/15.
 */

public class StatusBarUtils {

    private static int statusBarHeight = 0;

    /**
     * 获取状态栏高度
     * @param context 上下文
     * @return int 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight != 0){
            return statusBarHeight;
        }
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int height = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        statusBarHeight = 75;
        return statusBarHeight;
    }

    /**
     * 显示隐藏状态栏，全屏不变，只在有全屏时有效
     * @param enable
     */
    public static void setStatusBarVisibility(Activity activity, boolean enable) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (enable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        activity.getWindow().setAttributes(lp);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if(activity instanceof FundmentalActivity){
            ((FundmentalActivity)activity).setStatusBarState(enable);
        }
    }

    /**
     * 如果rootView设置了fitSystemWindow=true，则会与当前的状态栏方式产生冲突。
     * 若不设置fitSystemWindow=true,则在某些页面上键盘弹出后无法将输入框往上顶。
     * @param rootView
     */
    public static void fitSystemWindow(View rootView){
        if (rootView != null){
            //4.4以上的状态栏才能设置
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootView.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin,
                        layoutParams.topMargin - StatusBarUtils.getStatusBarHeight(rootView.getContext()),
                        layoutParams.rightMargin,
                        layoutParams.bottomMargin);
                rootView.setLayoutParams(layoutParams);
            }
        }
    }
}
