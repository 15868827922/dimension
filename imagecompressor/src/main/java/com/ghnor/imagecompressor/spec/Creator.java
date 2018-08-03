package com.ghnor.imagecompressor.spec;

import android.graphics.Bitmap;

import com.ghnor.imagecompressor.spec.calculation.Calculation;
import com.ghnor.imagecompressor.spec.decoration.Decoration;
import com.ghnor.imagecompressor.spec.options.FileCompressOptions;

import java.io.File;

/**
 * Created by ghnor on 2017/8/26.
 * ghnor.me@gmail.com
 */

public interface Creator<T> {
    T compressSpec(CompressSpec compressSpec);
    T calculation(Calculation calculation);
    T addDecoration(Decoration decoration);
    T options(FileCompressOptions compressOptions);
    T bitmapConfig(Bitmap.Config config);
    T width(int width);
    T height(int height);
    T inSampleSize(int inSampleSize);
    T compressThreadNum(int n);
    T safeMemory(float safeMemory);
    T rootDirectory(File rootDirectory);
}
