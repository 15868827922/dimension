package com.ghnor.imagecompressor.spec.options;

import android.graphics.Bitmap;

import static com.ghnor.imagecompressor.spec.CompressSpec.DEFAULT_BITMAP_CONFIG;
import static com.ghnor.imagecompressor.spec.CompressSpec.DEFAULT_IN_SAMPLE_SIZE;

/**
 * Created by ghnor on 2017/8/27.
 * ghnor.me@gmail.com
 */

public class BitmapCompressOptions extends CompressOptions {
    /**
     * By default,using ARGB_8888.
     * <p>
     * You can also consider using RGB_565,it can save half of memory maxSize.
     */
    public Bitmap.Config bitmapConfig = DEFAULT_BITMAP_CONFIG;

    /**
     * The width of the bitmap.
     * <p>
     * If the value is zero,the default compression maximum width is the screen width or {@link CompressSpec#}.
     */
    public int width;

    /**
     * The height of the bitmap.
     * <p>
     * If the value is zero,the default compression maximum height is the screen height or {@link CompressSpec#}.
     */
    public int height;

    /**
     * Whether to keep the bitmap width and height of the sample maxSize.
     * <p>
     * Suggestion to false, can save the bitmap memory after decode.
     * <p>
     * Otherwise the bitmap width and height will remain the same,also compress in the compression process.
     */
    public boolean isKeepSampling = false;

    public int inSampleSize = DEFAULT_IN_SAMPLE_SIZE;
}
