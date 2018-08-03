package com.ghnor.imagecompressor.core;

import android.graphics.BitmapFactory;

import com.ghnor.imagecompressor.callback.Callback;
import com.ghnor.imagecompressor.callback.CallbackDispatcher;
import com.ghnor.imagecompressor.callback.DefaultCallbackDispatcher;
import com.ghnor.imagecompressor.common.BitmapOptionsCompat;
import com.ghnor.imagecompressor.common.MemoryUtil;
import com.ghnor.imagecompressor.executor.WorkThreadExecutor;
import com.ghnor.imagecompressor.listener.cancel.CancelListenerHolder;
import com.ghnor.imagecompressor.listener.cancel.OnCancelListener;
import com.ghnor.imagecompressor.listener.lifecycle.LifeAttachManager;
import com.ghnor.imagecompressor.listener.lifecycle.SimpleOnLifeListener;
import com.ghnor.imagecompressor.spec.CompressComponent;
import com.ghnor.imagecompressor.spec.CompressSpec;

import java.util.concurrent.Callable;

/**
 * Created by ghnor on 2017/9/4.
 * ghnor.me@gmail.com
 */

public abstract class CompressEngine<T, I, R, C> extends CompressComponent<T> implements Runnable, OnCancelListener {

    protected CallbackDispatcher<R> mCallbackDispatcher;

    protected boolean mTasksContinue = true;

    public CompressEngine(T t, CompressSpec spec) {
        source = t;
        compressSpec = spec;
        CancelListenerHolder.getInstance().addOnCancelListener(this);
        LifeAttachManager.getInstance().attach(compressSpec.activity, new DefaultOnListener());
        LifeAttachManager.getInstance().attach(compressSpec.fragment, new DefaultOnListener());
        LifeAttachManager.getInstance().attach(compressSpec.supportFragment, new DefaultOnListener());
    }

    public R compressSync() throws Exception {
        return impl(true);
    }

    public void compress(Callback<R> callback) {
        mCallbackDispatcher = new DefaultCallbackDispatcher<>(callback);
        WorkThreadExecutor.getInstance().execute(this);
    }

    Callable<C> $(I i) {
        BitmapFactory.Options decodeBoundsOptions = BitmapOptionsCompat.getDefaultDecodeBoundsOptions();

        getDecodeOptions(i, decodeBoundsOptions);

        compressSpec.options.inSampleSize =
                compressSpec.calculation.calculateInSampleSize(
                        decodeBoundsOptions.outWidth,
                        decodeBoundsOptions.outHeight);

        compressSpec.options.quality =
                compressSpec.calculation.calculateQuality(
                        decodeBoundsOptions.outWidth,
                        decodeBoundsOptions.outHeight,
                        decodeBoundsOptions.outWidth / compressSpec.options.inSampleSize,
                        decodeBoundsOptions.outHeight / compressSpec.options.inSampleSize);

        while (!MemoryUtil.memoryEnough(
                decodeBoundsOptions.outWidth / compressSpec.options.inSampleSize,
                decodeBoundsOptions.outHeight / compressSpec.options.inSampleSize,
                decodeBoundsOptions.inPreferredConfig,
                compressSpec.safeMemory));

        Callable<C> callable = getCallable(i);
        return callable;
    }

    @Override
    public void run() {
        try {
            if (mTasksContinue) {
                impl(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        CancelListenerHolder.getInstance().removeOnCancelListener(this);
        mTasksContinue = false;
        WorkThreadExecutor.getInstance().removeAllTasks();
        if (mCallbackDispatcher != null) {
            mCallbackDispatcher.cancel();
        }
    }

    private class DefaultOnListener extends SimpleOnLifeListener {
        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }

    protected abstract BitmapFactory.Options getDecodeOptions(I i, BitmapFactory.Options options);
    protected abstract Callable<C> getCallable(I i);
    protected abstract R impl(boolean sync) throws Exception;
}
