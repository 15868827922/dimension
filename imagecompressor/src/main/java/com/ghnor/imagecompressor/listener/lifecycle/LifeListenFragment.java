package com.ghnor.imagecompressor.listener.lifecycle;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by ghnor on 2017/9/10.
 * ghnor.me@gmail.com
 */

public class LifeListenFragment extends Fragment {
    private LifeListenerManager listenerManager;

    public void setLifeListenerManager(LifeListenerManager manager){
        listenerManager = manager;
    }

    public LifeListenerManager getLifeListenerManager(){
        return listenerManager;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        listenerManager.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        listenerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        listenerManager.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        listenerManager.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listenerManager.onDestroy();
        listenerManager = null;
    }
}
