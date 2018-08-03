package com.ghnor.imagecompressor.listener.cancel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghnor on 2017/9/6.
 * ghnor.me@gmail.com
 */

public class CancelListenerHolder {

    private List<OnCancelListener> mOnCancelListeners;

    private CancelListenerHolder() {
        mOnCancelListeners = new ArrayList<>();
    }

    private static class Instance {
        private static CancelListenerHolder instance = new CancelListenerHolder();
    }

    public static CancelListenerHolder getInstance() {
        return Instance.instance;
    }

    public void addOnCancelListener(OnCancelListener onCancelListener) {
        mOnCancelListeners.add(onCancelListener);
    }

    public void removeOnCancelListener(OnCancelListener onCancelListener) {
        mOnCancelListeners.remove(onCancelListener);
    }

    public void cancel() {
        try {
            for (OnCancelListener onCancelListener : mOnCancelListeners) {
                try {
                    onCancelListener.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
