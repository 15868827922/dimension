package com.ghnor.imagecompressor.core;

import android.graphics.Bitmap;
import android.support.annotation.IdRes;

import com.ghnor.imagecompressor.common.FileUtil;
import com.ghnor.imagecompressor.common.Logger;
import com.ghnor.imagecompressor.spec.CompressSpec;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ghnor on 2017/6/20.
 * email: ghnor.me@gmail.com
 * desc:
 */

public class FileCompressor {

    public static String compress(String filePath, CompressSpec compressSpec) {
        String result = null;
        Bitmap bitmap = null;

        bitmap = BitmapCompressor.compress(filePath, compressSpec);
        result = compressPri(bitmap, compressSpec);

        return result;
    }

    public static String compress(File file, CompressSpec compressSpec) {
        return compress(file.getAbsolutePath(), compressSpec);
    }

    public static String compress(FileDescriptor fileDescriptor, CompressSpec compressSpec) {
        String result = null;
        Bitmap bitmap = null;

        bitmap = BitmapCompressor.compress(fileDescriptor, compressSpec);
        result = compressPri(bitmap, compressSpec);
        return result;
    }

    public static String compress(@IdRes int resId, CompressSpec compressSpec) {
        String result = null;
        Bitmap bitmap = null;

        bitmap = BitmapCompressor.compress(resId, compressSpec);
        result = compressPri(bitmap, compressSpec);
        return result;
    }

    public static String compress(InputStream is, CompressSpec compressSpec) {
        String result = null;
        Bitmap bitmap = null;

        bitmap = BitmapCompressor.compress(is, compressSpec);
        result = compressPri(bitmap, compressSpec);
        return result;
    }

    public static String compress(byte[] bytes, CompressSpec compressSpec) {
        String result = null;
        Bitmap bitmap = null;

        bitmap = BitmapCompressor.compress(bytes, compressSpec);
        result = compressPri(bitmap, compressSpec);
        return result;
    }

    public static String compress(Bitmap bitmap, CompressSpec compressSpec) {
        String result = null;
        Bitmap b = null;

        b = BitmapCompressor.compress(bitmap, compressSpec);
        result = compressPri(b, compressSpec);
        return result;
    }

    private static String compressPri(Bitmap bitmap, CompressSpec compressSpec) {
        int quality = compressSpec.options.quality;
        String outfile = null;
        float size = compressSpec.options.maxSize;

        if (quality < 0 || quality > 100)
            quality = CompressSpec.DEFAULT_QUALITY;

        if (bitmap.hasAlpha()) {
            outfile = FileUtil.generateCompressOutfileFormatPNG(compressSpec.rootDirectory, null).getAbsolutePath();
        } else {
            outfile = FileUtil.generateCompressOutfileFormatJPEG(compressSpec.rootDirectory, null).getAbsolutePath();
        }

        boolean isSuccess = compress(bitmap, outfile, quality);

        if (size > 0 && isSuccess) {
            float outfileSize = (float) FileUtil.getSizeInBytes(outfile) / (float) 1024;
            while (outfileSize > size) {
                if (quality <= 25)
                    break;
                quality -= 5;
                isSuccess = compress(bitmap, outfile, quality);
                if (!isSuccess)
                    break;
                outfileSize = (float) FileUtil.getSizeInBytes(outfile) / (float) 1024;
            }
        }

        Logger.i("compress quality: " + quality);

        bitmap.recycle();
        bitmap = null;
//        System.gc();

        return outfile;
    }

    private static boolean compress(Bitmap bitmap, String outfile, int quality) {
        if (bitmap == null || bitmap.isRecycled())
            return false;

        if (bitmap.hasAlpha()) {
            return compress(bitmap, outfile, quality, Bitmap.CompressFormat.PNG);
        } else {
            return compress(bitmap, outfile, quality, Bitmap.CompressFormat.JPEG);
        }
    }

    private static boolean compress(Bitmap bitmap, String outfile, int quality, Bitmap.CompressFormat format) {
        boolean isSuccess = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outfile);
            isSuccess = bitmap.compress(format, quality, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //avoid v6.0+ occur crash without permission
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

}
