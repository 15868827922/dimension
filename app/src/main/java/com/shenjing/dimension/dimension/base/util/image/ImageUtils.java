package com.shenjing.dimension.dimension.base.util.image;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.ghnor.imagecompressor.ImageCompressor;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.util.downloader.FileDownloader;
import com.shenjing.dimension.dimension.base.util.file.FileUtils;
import com.zjlp.httpvolly.log.LPLog;
import com.shenjing.dimension.R;
public class ImageUtils {


    /**
     * 判断是否是gif图片
     * @param srcFileName
     * @return  result[0] 是否是图片  result[1] 是否是gif图片
     */
    public static boolean[] isImageFile(String srcFileName) {
        boolean [] result = new boolean[]{false,false};
        FileInputStream imgFile = null;
        byte[] b = new byte[10];
        int l = -1;
        try {
            imgFile = new FileInputStream(srcFileName);
            l = imgFile.read(b);
            imgFile.close();
            result[0] = true;
        } catch (Exception e) {
            return result;
        }
        if (l == 10) {
            byte b0 = b[0];
            byte b1 = b[1];
            byte b2 = b[2];
            if (b0 == (byte) 'G' && b1 == (byte) 'I' && b2 == (byte) 'F') {
                result[1] = true;
                return result;
            }
        }
        return result;
    }

    public static void saveBitmap(Context context, Bitmap bitmap, File dir, String filename, boolean toGallery){
        if(TextUtils.isEmpty(filename)){
            return;
        }
        if(dir == null){
            return;
        }else if(!dir.exists()){
            dir.mkdirs();
        }
        final File file =new File(dir, filename);

        try{
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)){
                out.flush();
                out.close();
            }

            if(toGallery){
                //把文件插入到系统图库
                notifyScanFile(context, file);
            }

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
                System.gc();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveImg(final Context context, File fromFile){
        if(FileUtils.getBestFaceChildDir(FileUtils.DIR_IMAGES) != null){
            final File toFile = new File(FileUtils.getBestFaceChildDir(FileUtils.DIR_IMAGES), System.currentTimeMillis() + ".jpg");
            FileUtils.fileChannelCopy(fromFile, toFile);
            //把文件插入到系统图库
            notifyScanFile(context, toFile);
        }
    }



   /* public static void saveImg(final Context context, final String imageUrl){
        if(!imageUrl.startsWith("http")){
            if(imageUrl.startsWith("file://")){
                ImageUtils.saveImg(context, new File(imageUrl.substring(7)));
            }else{
                ImageUtils.saveImg(context, new File(imageUrl));
            }
        }else{
            File file = FileDownloader.single(context).getCacheFile(imageUrl);
            if(file != null){
                ImageUtils.saveImg(context, file);
                Toast.makeText(context, "图片已保存至\"[根目录]/" + FileUtils.APP_DIR + "/" + FileUtils.DIR_IMAGES + "\"文件夹", Toast.LENGTH_LONG).show();
            }else{
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            File file = Glide.with(context).load(imageUrl).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                            ImageUtils.saveImg(context, file);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "图片已保存至\"[根目录]/" + FileUtils.APP_DIR + "/" + FileUtils.DIR_IMAGES + "\"文件夹", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }
    }*/

    /**
     * Insert an image and create a thumbnail for it.
     *
     * @param cr The content resolver to use
     * @param source The stream to use for the image
     * @param title The name of the image
     * @param description The description of the image
     * @return The URL to the newly created image, or <code>null</code> if the image failed to be stored
     *              for any reason.
     */
    public static final String insertImage(ContentResolver cr, Bitmap source,
                                           String title, String description) {
        ContentValues values = new ContentValues();
        values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DESCRIPTION, description);
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 90, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id,
                        Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                StoreThumbnail(cr, miniThumb, id, 50F, 50F, Images.Thumbnails.MICRO_KIND);
            } else {
                LPLog.print("", "insertImage---Failed to create thumbnail, removing original");
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            LPLog.print("", "insertImage---Failed to insert image");
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    /**
     * 扫描数据库，将图片同步到媒体中
     * @param context
     * @param file
     */
    public static void notifyScanFile(Context context, File file){
        if (file == null){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static void notifyScanFile(Context context, String filePath){
        if (TextUtils.isEmpty(filePath)){
            return;
        }
        notifyScanFile(context, new File(filePath));
    }

    private static final Bitmap StoreThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width, float height,
            int kind) {
        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true);

        ContentValues values = new ContentValues(4);
        values.put(Images.Thumbnails.KIND,     kind);
        values.put(Images.Thumbnails.IMAGE_ID, (int)id);
        values.put(Images.Thumbnails.HEIGHT,   thumb.getHeight());
        values.put(Images.Thumbnails.WIDTH,    thumb.getWidth());

        Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);

            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        }
        catch (FileNotFoundException ex) {
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }

    public static byte[] toByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 90, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Deprecated
    public static void compressBitmapToFile(Bitmap srcBitmap, int quality, int maxSide, int angle, File dstFile){
        compressBitmapToFile(srcBitmap, quality, maxSide, angle, dstFile, false);
    }

    @Deprecated
    public static void compressBitmapToFile(Bitmap src, int quality, File dstFile){
        if(src == null || dstFile == null){
            return;
        }

        //质量压缩
        try {
            if(!dstFile.exists()){
                dstFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(dstFile);
            src.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 保存图片到指定路径,按目标文件大小进行压缩，压缩到文件小于期望值
     * Save image with specified size
     *
     * @param filePath the image file save path 储存路径
     * @param bitmap   the image what be save   目标图片
     * @param size     the file size of image   期望大小 单位bite
     */
    public static void saveImage(String filePath, Bitmap bitmap, int quality, long size) {

        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!result.exists() && !result.mkdirs()) return;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = quality;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);

        while (stream.toByteArray().length > size) {
            options -= 6;
            if(options<0){
                break;
            }
            stream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
        }
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param srcBitmap
     * @param quality
     * @param maxSide 长或宽的最大值
     * @return
     */
    @Deprecated
    public static void compressBitmapToFile(Bitmap srcBitmap, int quality, int maxSide, int angle, File dstFile, boolean needBlur){
        if(srcBitmap == null || dstFile == null){
            return;
        }
        //尺寸压缩
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        Bitmap scaledBitmap;
        if(srcWidth <= maxSide && srcHeight <= maxSide){
            scaledBitmap = srcBitmap;
        }else{
            int dstWidth;
            int dstHeight;
            if(srcWidth > srcHeight){
                dstWidth = maxSide;
                dstHeight = dstWidth * srcHeight / srcWidth;
            }else{
                dstHeight = maxSide;
                dstWidth = dstHeight * srcWidth / srcHeight;
            }
            scaledBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);
        }
        if(needBlur){
            scaledBitmap = FastBlur.doBlur(scaledBitmap, 15, false);
        }
        //图片旋转
        Bitmap rotatedBitmap = null;
        if (scaledBitmap != null){
            rotatedBitmap = rotate(angle, scaledBitmap);
        }
        if(scaledBitmap != null && scaledBitmap != rotatedBitmap && scaledBitmap != srcBitmap  && !scaledBitmap.isRecycled()){
            scaledBitmap.recycle();
            scaledBitmap = null;
        }
        //质量压缩
        try {
            if(!dstFile.exists()){
                dstFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(dstFile);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(rotatedBitmap != null && rotatedBitmap != srcBitmap && !rotatedBitmap.isRecycled()){
            rotatedBitmap.recycle();
            rotatedBitmap = null;
        }
    }

    /**
     * 图片压缩、添加水印
     *
     * @param srcBitmap
     * @param angle
     * @param waterBitmap
     * @return
     */
    public static String compressBitmapWithWatermark(Bitmap srcBitmap, int angle, Bitmap waterBitmap) {
        if (srcBitmap == null) {
            return "";
        }

        //压缩前gc，防止OOM
        System.gc();
        //图片旋转
        Bitmap rotatedBitmap = rotate(angle, srcBitmap);
        //画水印
        rotatedBitmap = drawImageToBitmap(rotatedBitmap, waterBitmap);
        //压缩图片
        String compressedPath = null;
        try {
            compressedPath = ImageCompressor.with().load(rotatedBitmap).compressSync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rotatedBitmap != null && rotatedBitmap != srcBitmap && !rotatedBitmap.isRecycled()) {
            rotatedBitmap.recycle();
            rotatedBitmap = null;
            System.gc();
        }
        return compressedPath;
    }

    /***
     *  在图片上添加文字
     * @param bitmap
     * @param text
     * @return
     */
    public static Bitmap drawTextToBitmap(Bitmap bitmap, String text) {

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0X66FFFFFF);
        paint.setTextSize(bitmap.getWidth() / 12);
//        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

//        double diagonal = Math.sin(45) * (bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight());
//        while(bounds.width() + bounds.height() > diagonal){
//            textSize -= 5;
//            paint.setTextSize(textSize);
//            paint.getTextBounds(text, 0, text.length(), bounds);
//        }

        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() - bounds.height()) / 2;
        canvas.rotate(-30, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        canvas.drawText(text, x, y, paint);
        canvas.rotate(30, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        return bitmap;
    }


    /***
     *  在图片上添加水印
     * @param bitmap
     * @return
     */
    public static Bitmap drawImageToBitmap(Bitmap bitmap, Bitmap waterBitmap) {

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int rowNum = 3;
        int waterWidth = bitmap.getWidth();
        if(bitmap.getHeight() < bitmap.getWidth()){
            waterWidth = bitmap.getHeight();
        }
        waterWidth = waterWidth/rowNum;
        waterBitmap = zoomImg(waterBitmap, waterWidth, waterWidth);

        for(int i=0; i<rowNum; i++ ){
            for(int j=0; j<rowNum; j++){
                int x = i*waterWidth;
                int y = j*(bitmap.getHeight()/rowNum);
                canvas.drawBitmap(waterBitmap, x, y, paint);
            }
        }
        return bitmap;
    }


    /**  处理图片
 * @param bm 所要转换的bitmap
 * @param newWidth 新的宽
 * @param newHeight 新的高
 * @return 指定宽高的bitmap
 */
    public static Bitmap zoomImg(Bitmap bm, int newWidth , int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**

     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
     * @return  缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
    {
        if(null == bitmap || edgeLength <= 0)
        {
            return  null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > edgeLength && heightOrg > edgeLength)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try{
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            }
            catch(Exception e){
                return null;
            }
        }

        return result;
    }

    /**
     *
     * @param bitmap 图片源
     * @param needRecycle 是否回收bitmap
     * @param length 图片限制大小，单位为k
     * @return
     */
    public static byte[] toByteArrayInSize(Bitmap bitmap, boolean needRecycle, int length){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > length * 1024) {  //循环判断如果压缩后图片是否大于length kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        if (needRecycle) {
            bitmap.recycle();
        }
        return baos.toByteArray();
    }

    @Deprecated
    public static String compressBitmapToTempPath(Context context, String path, int quality, int maxSide, int angle){
        File dstFile = new File(FileUtils.getCacheDir(context, "tempImages"), "temp_" + System.currentTimeMillis() + ".jpg");
        if(!dstFile.exists()){
            try {
                dstFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = getBitmapFromFile(new File(path), maxSide);
        compressBitmapToFile(bitmap, quality, maxSide, angle, dstFile);
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return dstFile.getPath();
    }

    @Deprecated
    public static String compressBitmapToPath(Context context, String srcPath, int quality, int maxSide, int angle){
        File dstFile = new File(FileUtils.getDir(context, "Images"), "chat_send_" + System.currentTimeMillis() + ".jpg");
        if(!dstFile.exists()){
            try {
                dstFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = getBitmapFromFile(new File(srcPath), maxSide);
        compressBitmapToFile(bitmap, quality, maxSide, angle, dstFile);
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return dstFile.getPath();
    }

    @Deprecated
    public static String compressBitmapToPath(Context context, String srcPath, String dstPath, int quality, int maxSide, int angle){
        File dstFile = new File(dstPath);
        if(!dstFile.exists()){
            try {
                dstFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = getBitmapFromFile(new File(srcPath), maxSide);
        compressBitmapToFile(bitmap, quality, maxSide, angle, dstFile);
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return dstFile.getPath();
    }

    /**
     *
     * @param dst
     * @param dstMaxSide 用于计算SampleSize，为保证图片质量，实际的图片最大边可能大于dstMaxSide
     * @return
     */
    public static Bitmap getBitmapFromFile(File dst, int dstMaxSide) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (dstMaxSide > 0) {
                opts = new BitmapFactory.Options();         //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例

                opts.inSampleSize = Math.max(opts.outHeight, opts.outWidth) / dstMaxSide;
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
                opts.inDither = false;
                opts.inPreferredConfig = Config.ARGB_8888;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     *如果图片的长宽比大于2，且图片的长边大于2倍dstMaxSide，则认为该图是长图，不decode
     * @param dst
     * @param dstMaxSide 用于计算SampleSize，为保证图片质量，实际的图片最大边可能大于dstMaxSide
     * @return
     */
    public static Bitmap getLongBitmapFromFile(File dst, int dstMaxSide) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            int inSampleSize=1;
            if (dstMaxSide > 0) {
                opts = new BitmapFactory.Options();         //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                int w=opts.outWidth;
                int h=opts.outHeight;
                if(w*2<h&&h>dstMaxSide*2){
                    //认为是长图
                    //当长图的短边大于dstMaxSide，要做等比缩放
                    if(w>dstMaxSide){
                        inSampleSize = w / dstMaxSide;
                        return decode(dst.getPath(),inSampleSize);
                    }else{
                        //短边小于maxSize时，不需要缩放
                        return null;
                    }
                }
                inSampleSize = Math.max(opts.outHeight, opts.outWidth) / dstMaxSide;
            }
            try {
                return decode(dst.getPath(),inSampleSize);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Bitmap decode(String path, int inSampleSize){
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inSampleSize = inSampleSize;
        opts.inJustDecodeBounds = false;
        opts.inInputShareable = true;
        opts.inPurgeable = true;
        opts.inDither = false;
        opts.inPreferredConfig = Config.ARGB_8888;
        try {
            return  BitmapFactory.decodeFile(path,opts);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readImageDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotate(int angle , Bitmap bitmap) {
        if (bitmap == null){
            return null;
        }

        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @param config
     * @return
     */
    public static Bitmap readBitmap(Context context, int resId, Config config, int dstWidth, int dstHeight){
        BitmapFactory.Options opt = new BitmapFactory.Options();

        //解析图片宽高
        opt.inJustDecodeBounds = true;
        InputStream is = context.getResources().openRawResource(resId);
        BitmapFactory.decodeStream(is, null, opt);

        //设置DecodingOpts
        opt.inSampleSize = computeSampleSize(opt, -1, dstWidth * dstHeight);
        opt.inJustDecodeBounds = false;
        opt.inPreferredConfig = config;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is2 = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is2,null,opt);
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else{
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 :(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static void fetchShareThumb(final Context context, final String imageUrl, final int defaultResId, final OnFetchThumbListener listener){

        new Thread(new Runnable() {

            @Override
            public void run() {
                Bitmap defaultThumb = BitmapFactory.decodeResource(context.getResources(), defaultResId);
                if(TextUtils.isEmpty(imageUrl)){
                    if(listener != null){
                        listener.onFetchThumbFinished(defaultThumb);
                    }
                    return;
                }
                Bitmap thumb = null;
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
                    if(bitmap != null){
                        thumb = ImageUtils.centerSquareScaleBitmap(bitmap, 120);
                        if(bitmap != thumb){
                            bitmap.recycle();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(listener != null){
                    listener.onFetchThumbFinished(thumb == null? defaultThumb : thumb);
                }
            }
        }).start();

    }

    @Deprecated
    public static String compressBitmapToPath(Context context, String srcPath, int angle){

        return compressBitmapToPath(context,srcPath,angle,null);
    }

    @Deprecated
    public static String compressBitmapToPath(Context context, String srcPath, int angle, Integer maxLength) {

        if (maxLength == null || maxLength == 0) {
            File dstFile = new File(FileUtils.getDir(context, "Images"), "chat_send_" + System.currentTimeMillis() + ".jpg");
            return compressBitmapToPath(srcPath, dstFile, angle);
        } else {
            File dstFile = new File(FileDownloader.single(context).getDiskCache().getBaseDir(), "chat_send_" + System.currentTimeMillis() + ".png");
            return compressBitmapToPath(srcPath,dstFile,angle, CompressFormat.PNG,maxLength);//有设置maxLength 则为新的  默认采用png 防止黑色背景
        }

    }

    /**  edit by tiny on 2017/3/24 17:36 * */
    @Deprecated
    public static String compressBitmapToPath(String srcPath, File dstFile, int angle){

       return compressBitmapToPath(srcPath,dstFile,angle, CompressFormat.JPEG);
    }

    @Deprecated
    public static String compressBitmapToPath(String srcPath, File dstFile, int angle, CompressFormat format){
        return compressBitmapToPath(srcPath,dstFile,angle,format, null);
    }

    @Deprecated
    public static String compressBitmapToPath(String srcPath, File dstFile, int angle, CompressFormat format, Integer maxLength){
        boolean lengthExist = false;//标记有传maxLength  有传的话 宽高  将都不能超过该值
        if (maxLength == null || maxLength == 0){
            maxLength = 800;
        } else {
            lengthExist = true;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);

        if (lengthExist) {//新逻辑
            if (options.outHeight > options.outWidth) {
                if(options.outHeight > maxLength){
                    options.inSampleSize = options.outHeight / maxLength;
                }
            } else {
                if(options.outWidth > maxLength){
                    options.inSampleSize = options.outWidth / maxLength;
                }
            }

        } else {
            if(angle == 90 || angle == 270){
                if(options.outHeight > maxLength){
                    options.inSampleSize = options.outHeight / maxLength;
                }
            }else{
                if(options.outWidth > maxLength){
                    options.inSampleSize = options.outWidth / maxLength;
                }
            }
        }

        options.inJustDecodeBounds = false;
        options.inInputShareable = true;
        options.inPurgeable = true;
        options.inDither = false;
        options.inPreferredConfig = Config.ARGB_8888;

        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, options);
        if (lengthExist) {// 新逻辑
            if(bitmap.getHeight() > bitmap.getWidth()){
                if(bitmap.getHeight() > maxLength) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * maxLength / bitmap.getHeight(), maxLength, true);
                }
            }else{
                if(bitmap.getWidth() > maxLength) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, maxLength, bitmap.getHeight() * maxLength / bitmap.getWidth(), true);
                }
            }
        } else {
            if(angle == 90 || angle == 270){
                if(bitmap.getHeight() > maxLength) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * maxLength / bitmap.getHeight(), maxLength, true);
                }
            }else{
                if(bitmap.getWidth() > maxLength) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, maxLength, bitmap.getHeight() * maxLength / bitmap.getWidth(), true);
                }
            }
        }

        Bitmap rotatedBitmap = rotate(angle, bitmap);
        if(bitmap != null && bitmap != rotatedBitmap && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }

        try {
            if(!dstFile.exists()) {
                dstFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(dstFile);
            /**  edit by tiny on 2017/3/24 17:34 * */
            rotatedBitmap.compress(format, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(rotatedBitmap != null && !rotatedBitmap.isRecycled()){
            rotatedBitmap.recycle();
            rotatedBitmap = null;
        }

        return dstFile.getPath();
    }
    public static Bitmap getCombinedBitmaps(Context context, List<String> urlList, int defaultResId){
        int size = 1;
        if (urlList != null && urlList.size() != 0){
            size = urlList.size() > 9 ? 9 : urlList.size();
        }
        List<BitmapEntity> entityList = getBitmapEntitys(context, size);

        Bitmap[] bitmaps = new Bitmap[size];
        Bitmap defaultBitmap = null;
        for (int i = 0; i < size; i++){
            String url = (urlList != null && urlList.size() > i) ? urlList.get(i) : null;
            if (TextUtils.isEmpty(url)){
                if (defaultBitmap == null){
                    defaultBitmap = BitmapFactory.decodeResource(context.getResources(), defaultResId);
                }
                bitmaps[i] = defaultBitmap;
            }else{
                try {
                    bitmaps[i] = Glide.with(context).load(url).asBitmap().into((int)entityList.get(i).width, (int)entityList.get(i).height).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bitmaps[i] = ImageUtils.centerSquareScaleBitmap(bitmaps[i], (int) entityList.get(i).width);
        }
        Bitmap combineBitmap = getCombineBitmaps(entityList, bitmaps);

        if (defaultBitmap != null){
            defaultBitmap.recycle();
        }
        for (int i = 0; i < size; i++){
            if (bitmaps[i] != null){
                bitmaps[i].recycle();
            }
        }

        return combineBitmap;
    }

    private static Bitmap getCombineBitmaps(List<BitmapEntity> mEntityList, Bitmap... bitmaps) {
        Bitmap newBitmap = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawARGB(255, 0xF5, 0xF5, 0xF5);
        for (int i = 0; i < mEntityList.size(); i++) {
            mixtureBitmap(newBitmap, bitmaps[i], new PointF(mEntityList.get(i).x, mEntityList.get(i).y));
        }
        return newBitmap;
    }

    private static void mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return;
        }
        Canvas cv = new Canvas(first);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
    }



    public interface OnFetchThumbListener{
        public void onFetchThumbFinished(Bitmap thumb);
    }

    private static List<BitmapEntity> getBitmapEntitys(Context context, int count) {
        List<BitmapEntity> mList = new LinkedList<BitmapEntity>();
        String value = null;
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(context.getResources().openRawResource(R.raw.data));
            props.load(in);
            in.close();
            value = props.getProperty(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (value == null){
            return null;
        }
        String[] arr1 = value.split(";");
        int length = arr1.length;
        for (int i = 0; i < length; i++) {
            String content = arr1[i];
            String[] arr2 = content.split(",");
            BitmapEntity entity = null;
            entity = new BitmapEntity();
            entity.x = Float.valueOf(arr2[0]) + 2;
            entity.y = Float.valueOf(arr2[1]) + 2;
            entity.width = Float.valueOf(arr2[2]) - 4;
            entity.height = Float.valueOf(arr2[3]) - 4;
            mList.add(entity);
        }
        return mList;
    }

    public static boolean isLongBitmap(Context context, int width, int height){
        boolean result=false;
        if(width==0 || height==0){
            return result;
        }
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        int w=dm.widthPixels;
        int h=dm.heightPixels;
        //图片高大于屏幕高2倍且高至少是宽的2倍认为是长图
        if (height > width ) {
            float rh=(float)height/(float)h;//高和屏幕高比2倍
            float var= (float)height/(float)width;//高宽比
            result= (rh >= 2 &&var >= 2);
        } else {
            float var= (float)width/(float)height; //宽高比
            float rw = (float) width / (float) w;//宽和屏幕宽的比3倍以上
            result=(rw >= 3 && var >= 2);
        }
        LPLog.print(context.getClass(),"bitmap width: "+width+" height: "+ height+"  is a long bitmap ");
        return result;
    }

    static class BitmapEntity {
        public float x;
        public float y;
        float width;
        float height;
    }

}
