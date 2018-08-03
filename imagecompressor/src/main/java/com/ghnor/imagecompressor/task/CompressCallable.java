package com.ghnor.imagecompressor.task;


import com.ghnor.imagecompressor.spec.CompressSpec;

import java.util.concurrent.Callable;

/**
 * Created by ghnor on 2017/7/8.
 * ghnor.me@gmail.com
 */

public abstract class CompressCallable<T> implements Callable<String> {

    CompressSpec mCompressSpec;
    T mT;

    public CompressCallable(T t, CompressSpec compressSpec) {
        this.mT = t;
        this.mCompressSpec = compressSpec;
    }

    @Override
    public String call() throws Exception {
        return call(mT, mCompressSpec);
    }

    abstract String call(T t, CompressSpec compressSpec);
}
