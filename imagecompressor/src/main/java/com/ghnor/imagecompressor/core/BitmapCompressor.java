package com.ghnor.imagecompressor.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.TypedValue;

import com.ghnor.imagecompressor.common.ApplicationLoader;
import com.ghnor.imagecompressor.common.BitmapOptionsCompat;
import com.ghnor.imagecompressor.common.Degrees;
import com.ghnor.imagecompressor.spec.CompressSpec;
import com.ghnor.imagecompressor.spec.decoration.Decoration;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ghnor on 2017/6/22.
 * email: ghnor.me@gmail.com
 * desc:
 */

public class BitmapCompressor {

    public static Bitmap compress(File file, CompressSpec compressSpec) {
        return compress(file.getAbsolutePath(), compressSpec);
    }

    public static Bitmap compress(String filePath, CompressSpec compressSpec) {
        Bitmap result;

        BitmapFactory.Options decodeOptions = compressPre(compressSpec);

        result = BitmapFactory.decodeFile(filePath, decodeOptions);
        result = Degrees.handle(result, filePath);

        result = compressOver(result, compressSpec);

        return result;
    }

    public static Bitmap compress(FileDescriptor fileDescriptor, CompressSpec compressSpec) {
        Bitmap result;

        BitmapFactory.Options decodeOptions = compressPre(compressSpec);

        result = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, decodeOptions);
        result = Degrees.handle(result, fileDescriptor);

        result = compressOver(result, compressSpec);

        return result;
    }

    public static Bitmap compress(int resId, CompressSpec compressSpec) {
        // for drawable resource, get the original maxSize of resources,without scaling.
        Resources resources = ApplicationLoader.getApplication().getResources();
        InputStream is = null;

        Bitmap result = null;

        try {
            is = resources.openRawResource(resId, new TypedValue());
            BitmapFactory.Options decodeOptions = compressPre(compressSpec);

            result = BitmapFactory.decodeStream(is, null, decodeOptions);
            result = Degrees.handle(result, resId);

            result = compressOver(result, compressSpec);

            return result;
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignore...
                }
            }
        }
    }

    public static Bitmap compress(InputStream is, CompressSpec compressSpec) {
        Bitmap result;

        BitmapFactory.Options decodeOptions = compressPre(compressSpec);

        result = BitmapFactory.decodeStream(is, null, decodeOptions);
        result = Degrees.handle(result, is);

        result = compressOver(result, compressSpec);

        return result;
    }

    public static Bitmap compress(byte[] bytes, CompressSpec compressSpec) {
        Bitmap result;

        BitmapFactory.Options decodeOptions = compressPre(compressSpec);

        result = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, decodeOptions);
        result = Degrees.handle(result, bytes);

        result = compressOver(result, compressSpec);

        return result;
    }

    public static Bitmap compress(Bitmap bitmap, CompressSpec compressSpec) {
        Bitmap result = null;

        result = matrixCompress(bitmap, compressSpec.options.inSampleSize, false);

        result = compressOver(result, compressSpec);

        return result;
    }

    private static BitmapFactory.Options compressPre(CompressSpec compressSpec) {
        BitmapFactory.Options decodeOptions = BitmapOptionsCompat.getDefaultDecodeOptions();
        decodeOptions.inPreferredConfig = compressSpec.options.bitmapConfig;
        decodeOptions.inSampleSize = compressSpec.options.inSampleSize;
        return decodeOptions;
    }

    private static Bitmap compressOver(Bitmap bitmap, CompressSpec compressSpec) {
        for (Decoration decoration : compressSpec.decorations) {
            bitmap = decoration.onDraw(bitmap);
        }
        return bitmap;
    }

    private static Bitmap customCompress(Bitmap source, int targetWidth, int targetHeight, boolean recycle) {
        Matrix matrix = new Matrix();

        float bitmapWidth = source.getWidth();
        float bitmapHeight = source.getHeight();

        float bitmapAspect = bitmapWidth / bitmapHeight;
        float viewAspect = (float) targetWidth / (float) targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeight;
            if (scale < .9F || scale > 1F) {
                matrix.setScale(scale, scale);
            } else {
                matrix = null;
            }
        } else {
            float scale = targetWidth / bitmapWidth;
            if (scale < .9F || scale > 1F) {
                matrix.setScale(scale, scale);
            } else {
                matrix = null;
            }
        }

        Bitmap temp;
        if (matrix != null) {
            temp = Bitmap.createBitmap(source, 0, 0,
                    source.getWidth(), source.getHeight(), matrix, true);
        } else {
            temp = source;
        }

        if (recycle && temp != source) {
            source.recycle();
        }

        int dx = Math.max(0, temp.getWidth() - targetWidth);
        int dy = Math.max(0, temp.getHeight() - targetHeight);

        Bitmap result = Bitmap.createBitmap(
                temp,
                dx / 2,
                dy / 2,
                targetWidth,
                targetHeight);

        if (result != temp && (recycle || temp != source)) {
            temp.recycle();
        }

        return result;
    }

    private static Bitmap matrixCompress(Bitmap bitmap, int inSampleSize, boolean recycle) {
        if (bitmap == null)
            return null;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        if (inSampleSize > 1) {
            Matrix matrix = new Matrix();
            matrix.postScale(1.0f/(float)inSampleSize, 1.0f/(float)inSampleSize);
            Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            if (recycle) {
                bitmap.recycle();
                bitmap = null;
            }
            return result;
        } else {
            return bitmap;
        }
    }

}
