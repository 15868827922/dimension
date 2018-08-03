package com.ghnor.imagecompressor.spec;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.IdRes;

import com.ghnor.imagecompressor.core.BatchCompressEngine;
import com.ghnor.imagecompressor.core.CompressEngineFactory;
import com.ghnor.imagecompressor.core.SingleCompressEngine;
import com.ghnor.imagecompressor.spec.calculation.Calculation;
import com.ghnor.imagecompressor.spec.decoration.Decoration;
import com.ghnor.imagecompressor.spec.options.FileCompressOptions;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ghnor on 2017/7/8.
 * ghnor.me@gmail.com
 */

public class CompressSourceLoader extends CompressComponent implements Creator<CompressSourceLoader>, Loader {

    private CompressSpecCreator mCompressSpecCreator;

    public CompressSourceLoader() {
        mCompressSpecCreator = new CompressSpecCreator();
    }

    public CompressSourceLoader(Activity activity) {
        mCompressSpecCreator = new CompressSpecCreator(activity);
    }

    public CompressSourceLoader(Fragment fragment) {
        mCompressSpecCreator = new CompressSpecCreator(fragment);
    }

    public CompressSourceLoader(android.support.v4.app.Fragment fragment) {
        mCompressSpecCreator = new CompressSpecCreator(fragment);
    }

    /**
     * file path
     * @param filePath
     * @return
     */
    @Override
    public SingleCompressEngine<String, String> load(String filePath) {
        return CompressEngineFactory.buildFilePathSingle(filePath, mCompressSpecCreator.create());
    }

    @Override
    public BatchCompressEngine<List<String>, String> load(String... filePath) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, filePath);
        return load(list);
    }

    /**
     * file
     * @param file
     * @return
     */
    @Override
    public SingleCompressEngine<File, File> load(File file) {
        return CompressEngineFactory.buildFileSingle(file, mCompressSpecCreator.create());
    }

    @Override
    public BatchCompressEngine<List<File>, File> load(File... file) {
        List<File> list = new ArrayList<>();
        Collections.addAll(list, file);
        return load(list);
    }

    /**
     * FileDescriptor
     * @param fileDescriptor
     * @return
     */
    @Override
    public SingleCompressEngine<FileDescriptor, FileDescriptor> load(FileDescriptor fileDescriptor) {
        return CompressEngineFactory.buildFileDescriptorSingle(fileDescriptor, mCompressSpecCreator.create());
    }

    @Override
    public BatchCompressEngine<List<FileDescriptor>, FileDescriptor> load(FileDescriptor... fileDescriptors) {
        List<FileDescriptor> list = new ArrayList<>();
        Collections.addAll(list, fileDescriptors);
        return load(list);
    }

    /**
     *
     * @param resId
     * @return
     */
    @Override
    public SingleCompressEngine<Integer, Integer> load(@IdRes Integer resId) {
        return CompressEngineFactory.buildResourceSingle(resId, mCompressSpecCreator.create());
    }

    @Override
    public BatchCompressEngine<List<Integer>, Integer> load(@IdRes Integer... resId) {
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, resId);
        return load(list);
    }

    /**
     * InputStream
     * @param is
     * @return
     */
    @Override
    public SingleCompressEngine<InputStream, InputStream> load(InputStream is) {
        return CompressEngineFactory.buildInputStreamSingle(is, mCompressSpecCreator.create());
    }

    @Override
    public BatchCompressEngine<List<InputStream>, InputStream> load(InputStream... is) {
        List<InputStream> list = new ArrayList<>();
        Collections.addAll(list, is);
        return load(list);
    }

    /**
     * byte[]
     * @param bytes
     * @return
     */
    @Override
    public SingleCompressEngine<byte[], byte[]> load(byte[] bytes) {
        return CompressEngineFactory.buildBytesSingle(bytes, mCompressSpecCreator.create());
    }

    @Override
    public BatchCompressEngine<List<byte[]>, byte[]> load(byte[]... bytes) {
        List<byte[]> list = new ArrayList<>();
        Collections.addAll(list, bytes);
        return load(list);
    }

    /**
     * bitmap
     * @param bitmap
     * @return
     */
    @Override
    public SingleCompressEngine<Bitmap, Bitmap> load(Bitmap bitmap) {
        return CompressEngineFactory.buildBitmapSingle(bitmap, mCompressSpecCreator.create());
    }

    @Override
    public BatchCompressEngine<List<Bitmap>, Bitmap> load(Bitmap... bitmap) {
        List<Bitmap> list = new ArrayList<>();
        Collections.addAll(list, bitmap);
        return load(list);
    }

    /**
     * List<T>
     * @param list
     * @return
     */
    @Override
    public <T extends List<I>, I> BatchCompressEngine<T, I> load(T list) {
        if (list != null && !list.isEmpty()) {
            Object object = list.get(0);
            if(object instanceof File) {
                return (BatchCompressEngine<T, I>) CompressEngineFactory.buildFileBatch((List<File>) list, mCompressSpecCreator.create());
            } else if (object instanceof FileDescriptor) {

            } else if (object instanceof String) {
                return (BatchCompressEngine<T, I>) CompressEngineFactory.buildFilePathBatch((List<String>) list, mCompressSpecCreator.create());
            } else if (object instanceof Uri) {

            } else if (object instanceof Integer) {
                return (BatchCompressEngine<T, I>) CompressEngineFactory.buildResourceBatch((List<Integer>)list, mCompressSpecCreator.create());
            } else if (object instanceof InputStream) {
                return (BatchCompressEngine<T, I>) CompressEngineFactory.buildInputStreamBatch((List<InputStream>) list, mCompressSpecCreator.create());
            } else if (object instanceof byte[]) {
                return (BatchCompressEngine<T, I>) CompressEngineFactory.buildBytesBatch((List<byte[]>) list, mCompressSpecCreator.create());
            } else if (object instanceof Bitmap) {
                return (BatchCompressEngine<T, I>) CompressEngineFactory.buildBitmapBatch((List<Bitmap>) list, mCompressSpecCreator.create());
            }
        }
        return null;
    }

    @Override
    public CompressSourceLoader compressSpec(CompressSpec compressSpec) {
        mCompressSpecCreator.compressSpec(compressSpec);
        return this;
    }

    @Override
    public CompressSourceLoader calculation(Calculation calculation) {
        mCompressSpecCreator.calculation(calculation);
        return this;
    }

    @Override
    public CompressSourceLoader addDecoration(Decoration decoration) {
        mCompressSpecCreator.addDecoration(decoration);
        return this;
    }

    @Override
    public CompressSourceLoader options(FileCompressOptions compressOptions) {
        mCompressSpecCreator.options(compressOptions);
        return this;
    }

    @Override
    public CompressSourceLoader bitmapConfig(Bitmap.Config config) {
        mCompressSpecCreator.bitmapConfig(config);
        return this;
    }

    @Override
    public CompressSourceLoader width(int width) {
        mCompressSpecCreator.width(width);
        return this;
    }

    @Override
    public CompressSourceLoader height(int height) {
        mCompressSpecCreator.height(height);
        return this;
    }

    @Override
    public CompressSourceLoader inSampleSize(int inSampleSize) {
        mCompressSpecCreator.inSampleSize(inSampleSize);
        return this;
    }

    @Override
    public CompressSourceLoader compressThreadNum(int n) {
        mCompressSpecCreator.compressThreadNum(n);
        return this;
    }

    @Override
    public CompressSourceLoader safeMemory(float safeMemory) {
        mCompressSpecCreator.safeMemory(safeMemory);
        return this;
    }

    @Override
    public CompressSourceLoader rootDirectory(File rootDirectory) {
        mCompressSpecCreator.rootDirectory(rootDirectory);
        return this;
    }

}
