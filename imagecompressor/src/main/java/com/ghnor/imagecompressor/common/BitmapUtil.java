package com.ghnor.imagecompressor.common;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;

/**
 * Created by ghnor on 2017/6/23.
 * email: ghnor.me@gmail.com
 * desc:
 */

public class BitmapUtil {

    private static final int ALPHA_8_BYTES_PER_PIXEL = 1;

    private static final int ARGB_4444_BYTES_PER_PIXEL = 2;

    private static final int ARGB_8888_BYTES_PER_PIXEL = 4;

    private static final int RGB_565_BYTES_PER_PIXEL = 2;

    public static long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0L;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            try {
                return bitmap.getAllocationByteCount();
            } catch (NullPointerException e) {
                //ignore...
            }
        }
        return bitmap.getByteCount();
    }

    public static long getSizeInBytes(int width, int height, Bitmap.Config config) {
        if (width <= 0 || height <= 0)
            return 0L;
        if (config == null)
            config = Bitmap.Config.ARGB_8888;
        switch (config) {
            case ARGB_8888:
                return ARGB_8888_BYTES_PER_PIXEL * width * height;
            case ALPHA_8:
                return ALPHA_8_BYTES_PER_PIXEL * width * height;
            case ARGB_4444:
                return ARGB_4444_BYTES_PER_PIXEL * width * height;
            case RGB_565:
                return RGB_565_BYTES_PER_PIXEL * width * height;
            default:
                return 0L;
        }
    }

//    public static long getBitmapSizeInBytes(Bitmap bitmap) {
//        if (bitmap == null || bitmap.isRecycled())
//            return 0L;
//        Bitmap.Config config = bitmap.getConfig();
//        switch (config) {
//            case ARGB_8888:
//                return ARGB_8888_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
//            case ALPHA_8:
//                return ALPHA_8_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
//            case ARGB_4444:
//                return ARGB_4444_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
//            case RGB_565:
//                return RGB_565_BYTES_PER_PIXEL * getSizeInBytes(bitmap);
//        }
//        throw new RuntimeException("this bitmap config is not supported!");
//    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (bitmap == null || bitmap.isRecycled())
            return bitmap;
        if (degree != 90 && degree != 180 && degree != 270)
            return bitmap;
        float pointX = bitmap.getWidth() / 2;
        float pointY = bitmap.getHeight() / 2;
        Matrix matrix = new Matrix();
        matrix.setRotate(degree, pointX, pointY);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return result;
    }
}
