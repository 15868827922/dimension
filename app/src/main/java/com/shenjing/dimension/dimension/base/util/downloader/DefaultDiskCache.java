package com.shenjing.dimension.dimension.base.util.downloader;

import android.content.Context;
import android.os.Build;

import java.io.File;

/**
 * Created by Tiny  on 2016/7/28 9:34.
 * Desc:
 */
public class DefaultDiskCache implements DiskCache {


    static final String CACHE_DIR="file_cache";



    Context mContext;

    File mBaseDir;

    public DefaultDiskCache(Context context ) {
        this.mContext = context;
        mBaseDir=getBaseDir();
    }

    @Override
    public File getBaseDir() {
        checkBaseDir();
        return mBaseDir;
    }
    private void checkBaseDir(){
        if(mBaseDir==null){//系统版本4.4的sd卡不能使用   所以如果是4.4系统的手机缓冲到内部缓冲里面
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH){
                mBaseDir=new File(mContext.getCacheDir(),CACHE_DIR);

            } else {
                mBaseDir=new File(mContext.getExternalCacheDir(),CACHE_DIR);
            }

        }
        if(!mBaseDir.exists()){
            mBaseDir.mkdirs();
        }
    }

    @Override
    public File get(String url, FilenameGenerator generator) {
        checkBaseDir();
        String filename=generator.generate(url);
        File result=new File(mBaseDir,filename);
        if(result.exists()&&result.length()!=0){
            return result;
        }
        return null;
    }

    @Override
    public String createDiskFilePath(String filename) {
        return mBaseDir+"/"+filename;
    }
}
