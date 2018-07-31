package com.shenjing.dimension.dimension.base.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.widget.Toast;


import com.shenjing.dimension.dimension.base.device.DeviceInfo;

import java.util.HashSet;

/**
 * Created by liht  on 2016/9/26 15:09.
 * Desc:
 */
public class NetworkChangedReceiver extends BroadcastReceiver {

    private boolean mLastState = true;
    private static HashSet<NetworkChangeListener> networkChangeListenerList;

    /**
     * 添加监听对象，必须由UI线程调用，否则有异步风险
     * @param networkChangeListener
     */
    public static void addNetworkChangeListener(NetworkChangeListener networkChangeListener) throws Exception {
        if (Looper.myLooper() != Looper.getMainLooper()){
            throw new Exception("必须由UI线程调用");
        }
        if (networkChangeListener == null) {
            return;
        }
        if (networkChangeListenerList == null) {
            networkChangeListenerList = new HashSet<NetworkChangeListener>();
        }
        if (!networkChangeListenerList.contains(networkChangeListener)) {
            NetworkChangedReceiver.networkChangeListenerList.add(networkChangeListener);
        }
    }

    /**
     * 删除监听对象，必须由UI线程调用，否则有异步风险
     * @param networkChangeListener
     */
    public static void deleteNetworkChangeListener(NetworkChangeListener networkChangeListener) throws Exception {
        if (Looper.myLooper() != Looper.getMainLooper()){
            throw new Exception("必须由UI线程调用");
        }

        if (networkChangeListener == null || networkChangeListenerList == null) {
            return;
        }
        if (networkChangeListenerList.contains(networkChangeListener)) {
            NetworkChangedReceiver.networkChangeListenerList.remove(networkChangeListener);
            if (networkChangeListenerList.size() == 0) {
                networkChangeListenerList = null;
            }
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.getState() == NetworkInfo.State.CONNECTED && info.isAvailable() && info.isConnected()){
                isAvailable = true;
            }
            if(networkChangeListenerList != null && info != null){
                for (NetworkChangeListener networkChangeListener : networkChangeListenerList) {
                    if (networkChangeListener != null) {
                        networkChangeListener.notifyNetworkType(info.getType());
                    }
                }
            }
        }

        if (isAvailable){
            if (!mLastState){
                mLastState = true;
                if (networkChangeListenerList != null) {
                    for (NetworkChangeListener networkChangeListener : networkChangeListenerList) {
                        if (networkChangeListener != null) {
                            networkChangeListener.notifyChange(true);
                        }
                    }
                }
            }
        }else{
            if (mLastState){
                mLastState = false;
                if (networkChangeListenerList != null) {
                    for (NetworkChangeListener networkChangeListener : networkChangeListenerList) {
                        if (networkChangeListener != null) {
                            networkChangeListener.notifyChange(false);
                        }
                    }
                }
                //从连接到断开，需要进行提示
                if (!DeviceInfo.isAppIsInBackground(context)){
                    Toast.makeText(context, "你的手机网络不太顺畅", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public interface NetworkChangeListener{
        void notifyChange(boolean isConnected);
        void notifyNetworkType(int networkType);
    }
}
