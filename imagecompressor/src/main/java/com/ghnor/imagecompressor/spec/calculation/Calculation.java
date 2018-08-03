package com.ghnor.imagecompressor.spec.calculation;

/**
 * Created by ghnor on 2017/7/5.
 * ghnor.me@gmail.com
 */

public interface Calculation {

    int calculateInSampleSize(int srcWidth, int srcHeight);

    int calculateQuality(int srcWidth, int srcHeight, int targetWidth, int targetHeight);

}
