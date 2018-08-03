package com.ghnor.imagecompressor.task;

import android.graphics.Bitmap;

import com.ghnor.imagecompressor.core.FileCompressor;
import com.ghnor.imagecompressor.spec.CompressSpec;

import java.io.File;
import java.io.InputStream;

/**
 * Created by ghnor on 2017/7/8.
 * ghnor.me@gmail.com
 */

public class CompressCallableTasks {

    private CompressCallableTasks() {
        throw new RuntimeException("can not be a instance");
    }

    public static final class FileCompressCallable extends CompressCallable<File> {
        public FileCompressCallable(File file, CompressSpec compressSpec) {
            super(file, compressSpec);
        }

        @Override
        String call(File file, CompressSpec compressSpec) {
            return FileCompressor.compress(file, compressSpec);
        }
    }

    public static final class FilePathCompressCallable extends CompressCallable<String> {
        public FilePathCompressCallable(String s, CompressSpec compressSpec) {
            super(s, compressSpec);
        }

        @Override
        String call(String filePath, CompressSpec compressSpec) {
            return FileCompressor.compress(filePath, compressSpec);
        }
    }

    public static final class ResourceCompressCallable extends CompressCallable<Integer> {

        public ResourceCompressCallable(Integer integer, CompressSpec compressSpec) {
            super(integer, compressSpec);
        }

        @Override
        String call(Integer integer, CompressSpec compressSpec) {
            return FileCompressor.compress(integer, compressSpec);
        }
    }

    public static final class InputStreamCompressCallable extends CompressCallable<InputStream> {
        public InputStreamCompressCallable(InputStream inputStream, CompressSpec compressSpec) {
            super(inputStream, compressSpec);
        }

        @Override
        String call(InputStream inputStream, CompressSpec compressSpec) {
            return FileCompressor.compress(inputStream, compressSpec);
        }
    }

    public static final class BytesCompressCallable extends CompressCallable<byte[]> {
        public BytesCompressCallable(byte[] bytes, CompressSpec compressSpec) {
            super(bytes, compressSpec);
        }

        @Override
        String call(byte[] bytes, CompressSpec compressSpec) {
            return FileCompressor.compress(bytes, compressSpec);
        }
    }

    public static final class BitmapCompressCallable extends CompressCallable<Bitmap> {
        public BitmapCompressCallable(Bitmap bitmap, CompressSpec compressSpec) {
            super(bitmap, compressSpec);
        }

        @Override
        String call(Bitmap bitmap, CompressSpec compressSpec) {
            return FileCompressor.compress(bitmap, compressSpec);
        }
    }

}
