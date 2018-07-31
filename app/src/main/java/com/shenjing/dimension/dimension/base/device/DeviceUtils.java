package com.shenjing.dimension.dimension.base.device;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by LinXH on 2017/8/30.
 */

public class DeviceUtils {

    public static void shortVibrate(Context context){
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(28);
    }



}
