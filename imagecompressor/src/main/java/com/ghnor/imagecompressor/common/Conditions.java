package com.ghnor.imagecompressor.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ghnor.imagecompressor.spec.CompressSpec;

import java.io.File;

/**
 * Created by ghnor on 2017/6/23.
 * email: ghnor.me@gmail.com
 * desc:
 */

public final class Conditions {

    private static final String TAG = "Conditions";

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a file with a cache audio name in the private cache directory.
     *
     * @param context
     *     A context.
     */
    public static File getImageCacheFile(Context context) {
        if (getImageCacheDir(context) != null) {
            return new File(getImageCacheDir(context) + "/" + System.currentTimeMillis() + (int) (Math.random() * 100));
        }
        return null;
    }

    /**
     * Returns a directory with a default name in the private cache directory of the application to
     * use to store retrieved audio.
     *
     * @param context
     *     A context.
     *
     * @see #getImageCacheDir(Context, String)
     */
    @Nullable
    public static File getImageCacheDir(Context context) {
        return getImageCacheDir(context, CompressSpec.DEFAULT_DISK_CACHE_DIR);
    }

    /**
     * Returns a directory with the given name in the private cache directory of the application to
     * use to store retrieved media and thumbnails.
     *
     * @param context
     *     A context.
     * @param cacheName
     *     The name of the subdirectory in which to store the cache.
     *
     * @see #getImageCacheDir(Context)
     */
    @Nullable
    public static File getImageCacheDir(Context context, String cacheName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            result.mkdirs();
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

}
