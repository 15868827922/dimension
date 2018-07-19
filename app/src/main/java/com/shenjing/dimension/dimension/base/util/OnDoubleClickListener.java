package com.shenjing.dimension.dimension.base.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class OnDoubleClickListener implements OnTouchListener {
    private static final long DOUBLE_CLICK_INTERVAL = 300;
    
    private long clickTime;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(MotionEvent.ACTION_DOWN == event.getAction()){
            if (clickTime > 0 && System.currentTimeMillis() - clickTime < DOUBLE_CLICK_INTERVAL) {
                clickTime = 0;
                onDoubleClick(v);
            } else {
                clickTime = System.currentTimeMillis();
            }
        }  
        return false;
    }
    
    public abstract void onDoubleClick(View v);

}
