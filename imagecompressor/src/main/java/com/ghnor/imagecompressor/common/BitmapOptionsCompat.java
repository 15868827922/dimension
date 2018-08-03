package com.ghnor.imagecompressor.common;

import android.graphics.BitmapFactory;
import android.os.Build;

import static com.ghnor.imagecompressor.spec.CompressSpec.DEFAULT_DECODE_BUFFER_SIZE;

/**
 * Created by ghnor on 2017/6/23.
 * email: ghnor.me@gmail.com
 * desc:
 */

public class BitmapOptionsCompat {

    public static BitmapFactory.Options getDefaultDecodeBoundsOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inTempStorage = new byte[DEFAULT_DECODE_BUFFER_SIZE];
        return options;
    }

    public static BitmapFactory.Options getDefaultDecodeOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        options.inMutable = true;
        options.inTempStorage = new byte[DEFAULT_DECODE_BUFFER_SIZE];
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // after and include kitkat,ashmem heap memory included in app total memory.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                // decode the image into a 'purgeable' bitmap that lives on the ashmem heap.
                options.inPurgeable = true;
                // enable copy of of bitmap to enable purgeable decoding by filedescriptor.
                options.inInputShareable = true;
            } else {
                options.inPurgeable = false;
                options.inInputShareable = false;
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //optimize the bitmap display quality
            options.inDither = true;
        }
        return options;
    }
}
