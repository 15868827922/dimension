package com.ghnor.imagecompressor.core;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ghnor.imagecompressor.common.ApplicationLoader;
import com.ghnor.imagecompressor.spec.CompressSpec;
import com.ghnor.imagecompressor.task.CompressCallableTasks;

import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by ghnor on 2017/9/6.
 * ghnor.me@gmail.com
 */

public final class CompressEngineFactory<T> {

    private CompressEngineFactory() {
        throw new RuntimeException("can not be a instance");
    }

    public static SingleCompressEngine<String, String> buildFilePathSingle(String filePath, CompressSpec compressSpec) {
        return new SingleCompressEngine<String, String>(filePath, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(String filePath, BitmapFactory.Options options) {
                BitmapFactory.decodeFile(filePath, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(String filePath) {
                return new CompressCallableTasks.FilePathCompressCallable(filePath, compressSpec);
            }
        };
    }

    public static BatchCompressEngine<List<String>, String> buildFilePathBatch(List<String> filePaths, CompressSpec compressSpec) {
        return new BatchCompressEngine<List<String>, String>(filePaths, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(String filePath, BitmapFactory.Options options) {
                BitmapFactory.decodeFile(filePath, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(String filePath) {
                return new CompressCallableTasks.FilePathCompressCallable(filePath, compressSpec);
            }
        };
    }

    public static SingleCompressEngine<File, File> buildFileSingle(File file, CompressSpec compressSpec) {
        return new SingleCompressEngine<File, File>(file, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(File file, BitmapFactory.Options options) {
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(File file) {
                return new CompressCallableTasks.FileCompressCallable(file, compressSpec);
            }
        };
    }

    public static BatchCompressEngine<List<File>, File> buildFileBatch(List<File> files, CompressSpec compressSpec) {
        return new BatchCompressEngine<List<File>, File>(files, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(File file, BitmapFactory.Options options) {
                BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(File file) {
                return new CompressCallableTasks.FileCompressCallable(file, compressSpec);
            }
        };
    }

    public static SingleCompressEngine<FileDescriptor, FileDescriptor> buildFileDescriptorSingle(FileDescriptor fileDescriptor, CompressSpec compressSpec) {
        return new SingleCompressEngine<FileDescriptor, FileDescriptor>(fileDescriptor, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(FileDescriptor fileDescriptor, BitmapFactory.Options options) {
                BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(FileDescriptor fileDescriptor) {
                return null;
            }
        };
    }

    public static BatchCompressEngine<List<FileDescriptor>, FileDescriptor> buildFileDescriptorBatch(List<FileDescriptor> fileDescriptors, CompressSpec compressSpec) {
        return new BatchCompressEngine<List<FileDescriptor>, FileDescriptor>(fileDescriptors, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(FileDescriptor fileDescriptor, BitmapFactory.Options options) {
                return null;
            }

            @Override
            protected Callable<String> getCallable(FileDescriptor fileDescriptor) {
                return null;
            }
        };
    }

    public static SingleCompressEngine<Integer, Integer> buildResourceSingle(Integer integer, CompressSpec compressSpec) {
        return new SingleCompressEngine<Integer, Integer>(integer, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(Integer integer, BitmapFactory.Options options) {
                Resources resources = ApplicationLoader.getApplication().getResources();
                BitmapFactory.decodeResource(resources, integer, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(Integer integer) {
                return new CompressCallableTasks.ResourceCompressCallable(integer, compressSpec);
            }
        };
    }

    public static BatchCompressEngine<List<Integer>, Integer> buildResourceBatch(List<Integer> integers, CompressSpec compressSpec) {
        return new BatchCompressEngine<List<Integer>, Integer>(integers, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(Integer integer, BitmapFactory.Options options) {
                Resources resources = ApplicationLoader.getApplication().getResources();
                BitmapFactory.decodeResource(resources, integer, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(Integer integer) {
                return new CompressCallableTasks.ResourceCompressCallable(integer, compressSpec);
            }
        };
    }

    public static SingleCompressEngine<InputStream, InputStream> buildInputStreamSingle(InputStream inputStream, CompressSpec compressSpec) {
        return new SingleCompressEngine<InputStream, InputStream>(inputStream, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(InputStream inputStream, BitmapFactory.Options options) {
                BitmapFactory.decodeStream(inputStream, null, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(InputStream inputStream) {
                return new CompressCallableTasks.InputStreamCompressCallable(inputStream, compressSpec);
            }
        };
    }

    public static BatchCompressEngine<List<InputStream>, InputStream> buildInputStreamBatch(List<InputStream> inputStreams, CompressSpec compressSpec) {
        return new BatchCompressEngine<List<InputStream>, InputStream>(inputStreams, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(InputStream inputStream, BitmapFactory.Options options) {
                BitmapFactory.decodeStream(inputStream, null, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(InputStream inputStream) {
                return new CompressCallableTasks.InputStreamCompressCallable(inputStream, compressSpec);
            }
        };
    }

    public static SingleCompressEngine<byte[], byte[]> buildBytesSingle(byte[] bytes, CompressSpec compressSpec) {
        return new SingleCompressEngine<byte[], byte[]>(bytes, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(byte[] bytes, BitmapFactory.Options options) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(byte[] bytes) {
                return new CompressCallableTasks.BytesCompressCallable(bytes, compressSpec);
            }
        };
    }

    public static BatchCompressEngine<List<byte[]>, byte[]> buildBytesBatch(List<byte[]> bytes, CompressSpec compressSpec) {
        return new BatchCompressEngine<List<byte[]>, byte[]>(bytes, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(byte[] bytes, BitmapFactory.Options options) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                return options;
            }

            @Override
            protected Callable<String> getCallable(byte[] bytes) {
                return new CompressCallableTasks.BytesCompressCallable(bytes, compressSpec);
            }
        };
    }

    public static SingleCompressEngine<Bitmap, Bitmap> buildBitmapSingle(Bitmap bitmap, CompressSpec compressSpec) {
        return new SingleCompressEngine<Bitmap, Bitmap>(bitmap, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(Bitmap bitmap, BitmapFactory.Options options) {
                options.outWidth = bitmap.getWidth();
                options.outHeight = bitmap.getHeight();
                options.inPreferredConfig = bitmap.getConfig();
                return options;
            }

            @Override
            protected Callable<String> getCallable(Bitmap bitmap) {
                return new CompressCallableTasks.BitmapCompressCallable(bitmap, compressSpec);
            }
        };
    }

    public static BatchCompressEngine<List<Bitmap>, Bitmap> buildBitmapBatch(List<Bitmap> bitmaps, CompressSpec compressSpec) {
        return new BatchCompressEngine<List<Bitmap>, Bitmap>(bitmaps, compressSpec) {
            @Override
            protected BitmapFactory.Options getDecodeOptions(Bitmap bitmap, BitmapFactory.Options options) {
                options.outWidth = bitmap.getWidth();
                options.outHeight = bitmap.getHeight();
                options.inPreferredConfig = bitmap.getConfig();
                return options;
            }

            @Override
            protected Callable<String> getCallable(Bitmap bitmap) {
                return new CompressCallableTasks.BitmapCompressCallable(bitmap, compressSpec);
            }
        };
    }

}
