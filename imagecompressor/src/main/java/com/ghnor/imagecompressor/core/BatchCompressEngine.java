package com.ghnor.imagecompressor.core;

import com.ghnor.imagecompressor.executor.CompressExecutor;
import com.ghnor.imagecompressor.spec.CompressSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by ghnor on 2017/9/7.
 * ghnor.me@gmail.com
 */

public abstract class BatchCompressEngine<T extends List<I>, I> extends CompressEngine<T, I, List<String>, String> {

    private List<Future<String>> mFutureList;

    private List<String> mCompressResultList;

    public BatchCompressEngine(T t, CompressSpec spec) {
        super(t, spec);
        mFutureList = new ArrayList<>();
        mCompressResultList = new ArrayList<>();
    }

    @Override
    protected List<String> impl(boolean sync) throws Exception {
        for (int i = 0; i < source.size() && mTasksContinue; i++) {
            Callable<String> callable = $(source.get(i));

            if (sync) {
                mCompressResultList.add(callable.call());
            } else {
                CompressExecutor.getInstance().setPoolSize(compressSpec.compressThreadNum);
                Future future = CompressExecutor.getExecutor().submit(callable);
                mFutureList.add(future);
            }
        }

        if (sync) {
            return mCompressResultList;
        } else {
            dispatchResultList();
        }
        return null;
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mFutureList != null && !mFutureList.isEmpty()) {
            for (Future future : mFutureList) {
                if (future != null) {
                    future.cancel(true);
                }
            }
        }
    }

    private void dispatchResultList() {
        for (Future<String> future : mFutureList) {
            String compressResult = null;
            try {
                compressResult = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            mCompressResultList.add(compressResult);
        }
        mCallbackDispatcher.dispatch(mCompressResultList);
    }
}
