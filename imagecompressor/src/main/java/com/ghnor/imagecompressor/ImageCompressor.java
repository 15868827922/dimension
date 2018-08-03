package com.ghnor.imagecompressor;

import com.ghnor.imagecompressor.listener.cancel.CancelListenerHolder;
import com.ghnor.imagecompressor.spec.CompressSourceLoader;

/**
 * Created by ghnor on 2017/7/8.
 * ghnor.me@gmail.com
 */

public class ImageCompressor {

    private static String TAG = "ImageCompressor";

    public static CompressSourceLoader with() {
        return new CompressSourceLoader();
    }

//    public static CompressSourceLoader with(Activity activity) {
////        LifeAttachManager.getInstance().attach(activity);
//        return new CompressSourceLoader(activity);
//    }
//
//    public static CompressSourceLoader with(Fragment fragment) {
////        LifeAttachManager.getInstance().attach(fragment);
//        return new CompressSourceLoader(fragment);
//    }
//
//    public static CompressSourceLoader with(android.support.v4.app.Fragment fragment) {
////        LifeAttachManager.getInstance().attach(fragment);
//        return new CompressSourceLoader(fragment);
//    }

    public static void cancel() {
        CancelListenerHolder.getInstance().cancel();
    }
}