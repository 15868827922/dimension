package com.shenjing.dimension.dimension.base.util.downloader;

import android.os.Looper;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tiny  on 2016/7/28 9:46.
 * Desc:
 */
class Utils {


    static final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
    static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; // 20s
    static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s


    public static void checkMain() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalThreadStateException("method should run in main thread");
        }
    }

    public static void checkNotMain() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalThreadStateException("method should run in main thread");
        }
    }

    public static File createNewFileWithDirs(String path) throws IOException {
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        if (!file.createNewFile()) {
            System.out.println("文件创建失败！");
        }

        return file;
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
