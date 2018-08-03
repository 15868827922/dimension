package com.ghnor.imagecompressor.listener.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghnor on 2017/9/10.
 * ghnor.me@gmail.com
 */

public class LifeAttachManager {
    private static final String FRAGMENT_TAG = "LifeListenFragment";

    private static volatile LifeAttachManager mInstance;

    private List<LifeListenerManager> mLifeListenerManagers;

    private LifeAttachManager() {
        mLifeListenerManagers = new ArrayList<>();
    }

    public static LifeAttachManager getInstance(){
        if(null == mInstance){
            synchronized (LifeAttachManager.class){
                if(null == mInstance){
                    mInstance = new LifeAttachManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 开始监听生命周期
     */
    public void attach(Activity activity, OnLifeListener listener){

    }

    public void attach(Fragment fragment, OnLifeListener listener) {

    }

    public void attach(android.support.v4.app.Fragment fragment, OnLifeListener listener) {

    }

    public void addOnLifeListener(OnLifeListener listener) {
        for (LifeListenerManager manager : mLifeListenerManagers) {
            manager.addLifeListener(listener);
        }
    }

    public void removeOnLifeListener(OnLifeListener listener) {
        for (LifeListenerManager manager : mLifeListenerManagers) {
            manager.removeLifeListener(listener);
        }
    }

    /**
     * 找到指定的Activity绑定的空白Fragment,如果没有则会自动绑定一个
     * @param activity
     * @return
     */
    public LifeListenFragment getLifeListenerFragment(Activity activity){
        FragmentManager fm = activity.getFragmentManager();
        return findFragment(fm);
    }

    public LifeListenFragment getLifeListenerFragment(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            FragmentManager fm = fragment.getChildFragmentManager();
            return findFragment(fm);
        }
        return null;
    }

    public LifeListenSupportFragment getLifeListenSupportFragment(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.FragmentManager fm = fragment.getChildFragmentManager();
        return findSupportFragment(fm);
    }

    /**
     * 找到用于监听生命周期的空白的Fragment
     */
    private LifeListenFragment findFragment(FragmentManager fm){
        LifeListenFragment current = (LifeListenFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {//没有找到，则新建
            current = new LifeListenFragment();
            fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();//添加Fragment
        }
        return current;
    }

    private LifeListenSupportFragment findSupportFragment(android.support.v4.app.FragmentManager fm) {
        LifeListenSupportFragment current = (LifeListenSupportFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {//没有找到，则新建
            current = new LifeListenSupportFragment();
            fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();//添加Fragment
        }
        return current;
    }

    /**
     * 用于获取管理对应Fragment的ActLifeListenerManager
     * @param fragment
     * @return
     */
    private LifeListenerManager findLifeListenerManager(LifeListenFragment fragment){
        LifeListenerManager manager = fragment.getLifeListenerManager();
        if (null == manager){
            manager = new LifeListenerManager();
        }
        fragment.setLifeListenerManager(manager);
        return manager;
    }
}
