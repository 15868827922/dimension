package com.shenjing.dimension.dimension.base.util.file;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;

import com.zjlp.httpvolly.log.LPLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class FileUtils {
    public static final long KB=1024;
    public static final long MB=KB*KB;
    public static final String ZIP_SUFFIX = ".zip";
    public static final String TMP_SUFFIX = ".tmp";
    public static final String APK_SUFFIX = ".apk";
    public static final String JPG_SUFFIX = ".jpg";
    public static final String FILE_URL_SUFFIX = "file://";
    public static final String APP_DIR = "Shenjing";
    public static final String DIR_IMAGES = "Images";
    public static final String DIR_VIDEO = "Video";

    private static final String DIR_BESTFACE_CACHE = "cache";
    private static final String DIR_BESTFACE_DATA = "data";


    public static String getFileUrl(String path) {
        return FILE_URL_SUFFIX + path;
    }

    public static File getDir(Context context, String dirName) {
        File dir = new File(getAppDir(context), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getBestFaceChildDir(String dirName) {
        File dir = new File(getBestFaceDir(), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getBestFaceDir(){
        String state = Environment.getExternalStorageState();
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if(externalStorageDirectory == null){
                externalStorageDirectory = Environment.getRootDirectory();
            }
            dir = new File(externalStorageDirectory, APP_DIR);
        } else {
            dir = new File(Environment.getRootDirectory(), APP_DIR);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getAppDir(Context context) {
        String state = Environment.getExternalStorageState();
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            dir = context.getExternalFilesDir(null);
        } else {
            dir = context.getFilesDir();
        }
        if(dir == null){
            dir = getBestFaceChildDir(DIR_BESTFACE_DATA);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getAppCacheDir(Context context) {
        String state = Environment.getExternalStorageState();
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            dir = context.getExternalCacheDir();
        } else {
            dir = context.getCacheDir();
        }
        if(dir == null){
            dir = getBestFaceChildDir(DIR_BESTFACE_CACHE);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File createNewFileWithDirs(String path){
        File file = new File(path);
        if(!file.exists()){
            if(!file.getParentFile().mkdirs()){
                System.out.println("创建父目录失败！");
            }
            try{
                if(!file.createNewFile()){
                    System.out.println("文件创建失败！");
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if(files!=null){
                    for (File fileTmp : files) {
                        deleteFile(fileTmp);
                    }
                }
                deleteRenameFile(file);
            } else {
                // file.delete();
                deleteRenameFile(file);
            }
        }
    }

    private static void deleteRenameFile(File file) {
        File fileTo = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(fileTo);
        fileTo.delete();
        LPLog.print(FileUtils.class, "删除文件成功");
    }

    public static boolean unZip(ZipFile zipFile, File parentDir) {
        InputStream zis = null;
        FileOutputStream fos = null;
        try {
            if (!parentDir.exists()) {
                parentDir.mkdir();
            }

            Enumeration<ZipEntry> enums = (Enumeration<ZipEntry>) zipFile.entries();

            File entryFile = null;
            String entryName = null;
            ZipEntry entry = null;

            while (enums.hasMoreElements()) {
                entry = enums.nextElement();
                entryName = entry.getName();
                // Logs.d("FileUtils", "entryName " + entryName);
                entryName = entryName.replace("\\", File.separator);
                entryFile = new File(parentDir, entryName);

                if (entry.isDirectory()) {
                    entryFile.mkdir();
                } else {
                    zis = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(entryFile);
                    byte[] buf = new byte[1024];
                    int len = -1;
                    while ((len = zis.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        fos.flush();
                    }
                    zis.close();
                    fos.close();
                }
            }
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                    zis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static boolean isFileExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getHtmlUrl(Context context, String subPath) {
        String externalHtmlPath = getAppDir(context).getAbsolutePath() + File.separator + subPath;
        String htmlUrl;
        if (isFileExists(externalHtmlPath)) {
            htmlUrl = "file://" + externalHtmlPath;
        } else {
            htmlUrl = "file:///android_asset/" + subPath;
        }
        return htmlUrl;
    }

    public static String readStringInputStream(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StringBuffer readAssetsText(Context context, String fileName) {
        if(TextUtils.isEmpty(fileName)){
            return null;
        }
        StringBuffer json = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(isr);
            char[] tempchars  = new char[1024];
            int charread = 0;
            while ((charread = br.read(tempchars)) != -1) {
                json.append(tempchars);
            }
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getFileByImageUri(Context context, Uri imageUri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(imageUri, filePathColumn, null, null,
                null);
        if(cursor != null){
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            return new File(picturePath);
        }else{
            return new File(imageUri.getPath());
        }
    }

    public static File getCacheDir(Context context, String dirName) {
        File dir = new File(getAppCacheDir(context), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String getFilePath(String fileUrl){
        if(fileUrl == null){
            return null;
        }
        if(fileUrl.startsWith(FILE_URL_SUFFIX)){
            return fileUrl.substring(FILE_URL_SUFFIX.length());
        }
        return fileUrl;
    }

    public static boolean isLocalFile(String url){
        if(url == null) return false;
        return url.startsWith(FILE_URL_SUFFIX);
    }

    public static File getFileByUri(Context context, Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, new String[] { Images.ImageColumns._ID, Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            LPLog.print(context.getClass(), "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    public static void fileChannelCopy(File fromFile, File toFile) {
        if(!fromFile.exists()){
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(fromFile);
            fo = new FileOutputStream(toFile);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(fi, in, fo, out);
            LPLog.print(FileUtils.class, "文件Copy结束");
        }
    }



    public static ArrayList<File> getChildFilesByDir(File dir){
        ArrayList<File> result=new ArrayList<File>();
        if(dir==null||!dir.exists()){
            return  result;
        }
        File[] childs= dir.listFiles();
        if(childs == null){
            return result;
        }
        for(File file:childs){
            if(file.isDirectory()){
                result.addAll(getChildFilesByDir(file));
            }else {
                result.add(file);
            }
        }
        return result;
    }
    public static boolean isExist(File file){
        return file!=null&&file.exists();
    }
    public static File getInternalDir(Context context){
        return context.getFilesDir().getParentFile();
    }
    public static File getWebViewCacheDir(Context context){
        return new File(getInternalDir(context),"app_webview");
    }

    public static File getDefaultCacheDirectory(Context context) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = context.getExternalFilesDir(null);
        }
        file = file == null ? context.getFilesDir() : file;

        file = new File(file.getParentFile(), "cache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 保存Bitmap到文件中
     *
     * @param bitmap
     * @param filePath
     */
    public static void saveBitmap(Bitmap bitmap, String filePath) {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

   /* public static void saveRemoteImg2File(final Context context, final String url, final String fileName, final boolean targetSdCard) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(bitmap == null || bitmap.isRecycled()){
                    return;
                }
                ImageUtils.saveBitmap(context, bitmap, targetSdCard ? FileUtils.getDir(context, FileUtils.DIR_IMAGES) : FileUtils.getCacheDir(context, FileUtils.DIR_IMAGES), fileName, false);
            }
        }).start();
    }*/
}
