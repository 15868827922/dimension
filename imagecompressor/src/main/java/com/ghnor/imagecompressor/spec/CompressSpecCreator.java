package com.ghnor.imagecompressor.spec;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;

import com.ghnor.imagecompressor.spec.calculation.Calculation;
import com.ghnor.imagecompressor.spec.decoration.Decoration;
import com.ghnor.imagecompressor.spec.options.FileCompressOptions;

import java.io.File;

/**
 * Created by ghnor on 2017/7/8.
 * ghnor.me@gmail.com
 */

public class CompressSpecCreator implements Creator<CompressSpecCreator> {

    private CompressSpec mCompressSpec;

    public CompressSpecCreator() {
        mCompressSpec = CompressSpec.newInstance();
    }

    public CompressSpecCreator(Activity activity) {
        this();
//        mCompressSpec.activity = activity;
    }

    public CompressSpecCreator(Fragment fragment) {
        this();
//        mCompressSpec.fragment = fragment;
    }

    public CompressSpecCreator(android.support.v4.app.Fragment fragment) {
        this();
//        mCompressSpec.supportFragment = fragment;
    }

    public CompressSpec create() {
        return mCompressSpec;
    }

    @Override
    public CompressSpecCreator compressSpec(CompressSpec compressSpec) {
        return null;
    }

    @Override
    public CompressSpecCreator calculation(Calculation calculation) {
        mCompressSpec.calculation = calculation;
        return this;
    }

    @Override
    public CompressSpecCreator addDecoration(Decoration decoration) {
        mCompressSpec.decorations.add(decoration);
        return this;
    }

    @Override
    public CompressSpecCreator options(FileCompressOptions compressOptions) {
        mCompressSpec.options = compressOptions;
        return this;
    }

    @Override
    public CompressSpecCreator bitmapConfig(Bitmap.Config config) {
        mCompressSpec.options.bitmapConfig = config;
        return this;
    }

    @Override
    public CompressSpecCreator width(int width) {
        mCompressSpec.options.width = width;
        return this;
    }

    @Override
    public CompressSpecCreator height(int height) {
        mCompressSpec.options.height = height;
        return this;
    }

    @Override
    public CompressSpecCreator inSampleSize(int inSampleSize) {
        mCompressSpec.options.inSampleSize = inSampleSize;
        return this;
    }

    @Override
    public CompressSpecCreator compressThreadNum(int n) {
        mCompressSpec.compressThreadNum = n;
        return this;
    }

    @Override
    public CompressSpecCreator safeMemory(float safeMemory) {
        mCompressSpec.safeMemory = safeMemory;
        return this;
    }

    @Override
    public CompressSpecCreator rootDirectory(File rootDirectory) {
        mCompressSpec.rootDirectory = rootDirectory;
        return this;
    }
}
