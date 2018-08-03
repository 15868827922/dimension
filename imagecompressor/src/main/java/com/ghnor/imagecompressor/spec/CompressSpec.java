package com.ghnor.imagecompressor.spec;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;

import com.ghnor.imagecompressor.spec.calculation.Calculation;
import com.ghnor.imagecompressor.spec.calculation.DefaultCalculation;
import com.ghnor.imagecompressor.spec.decoration.Decoration;
import com.ghnor.imagecompressor.spec.options.FileCompressOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghnor on 2017/6/21.
 * email: ghnor.me@gmail.com
 * desc:
 */

public class CompressSpec {
    private static final String TAG = "CompressSpec";

    public static final String DEFAULT_DISK_CACHE_DIR = "compress_disk_cache";

    public static final int DEFAULT_QUALITY = 50;

    public static final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    public static final int DEFAULT_DECODE_BUFFER_SIZE = 16 * 1024;

    public static final int DEFAULT_IN_SAMPLE_SIZE = 1;

    public static final float DEFAULT_SAFE_MEMORY = (float) (1.0 / 2.0);

    public static int DEFAULT_COMPRESS_THREAD_NUM;

    static {
        DEFAULT_COMPRESS_THREAD_NUM = Runtime.getRuntime().availableProcessors() + 1;
    }

    public Activity activity;

    public Fragment fragment;

    public android.support.v4.app.Fragment supportFragment;

    public Calculation calculation;

    public List<Decoration> decorations;

    public FileCompressOptions options;

    public int compressThreadNum;

    public float safeMemory;

    public File rootDirectory;

    private CompressSpec() {
        calculation = new DefaultCalculation();
        decorations = new ArrayList<>();
        options = new FileCompressOptions();
        compressThreadNum = DEFAULT_COMPRESS_THREAD_NUM;
        safeMemory = DEFAULT_SAFE_MEMORY;
    }

    public static CompressSpec newInstance(CompressSpec compressSpec) {
        return compressSpec;
    }

    public static CompressSpec newInstance() {
        return new CompressSpec();
    }

    public static CompressSpec getInstance(CompressSpec compressSpec) {
        ConfigurationHolder.sInstance = compressSpec;
        return ConfigurationHolder.sInstance;
    }

    public static CompressSpec getInstance() {
        return ConfigurationHolder.sInstance;
    }

    private static class ConfigurationHolder {
        private static CompressSpec sInstance = new CompressSpec();
    }
}
