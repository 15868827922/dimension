package com.ghnor.imagecompressor.listener.lifecycle;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghnor on 2017/9/10.
 * ghnor.me@gmail.com
 */

public class LifeListenerManager {
    List<OnLifeListener> listeners = new ArrayList<>();

    public void addLifeListener(OnLifeListener listener){
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeLifeListener(OnLifeListener listener){
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        for (OnLifeListener listener : listeners){
            listener.onCreate(savedInstanceState);
        }
    }


    public void onStart() {
        for (OnLifeListener listener : listeners){
            listener.onStart();
        }
    }


    public void onResume() {
        for (OnLifeListener listener : listeners){
            listener.onResume();
        }
    }


    public void onPause() {
        for (OnLifeListener listener : listeners){
            listener.onPause();
        }
    }


    public void onStop() {
        for (OnLifeListener listener : listeners){
            listener.onStop();
        }
    }


    public void onDestroy() {
        //倒过来循环防止删除时有影响
        for(int i = listeners.size()-1; i >= 0; i--){
            OnLifeListener listener = listeners.get(i);
            listener.onDestroy();
            removeLifeListener(listener);
            listener = null;
        }
    }
}
