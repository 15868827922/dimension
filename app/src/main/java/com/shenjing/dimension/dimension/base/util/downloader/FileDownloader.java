package com.shenjing.dimension.dimension.base.util.downloader;

import android.content.Context;
import android.net.Uri;


import com.shenjing.dimension.BuildConfig;

import java.io.File;

import static com.shenjing.dimension.dimension.base.util.downloader.Utils.checkMain;


/**
 * Created by Tiny  on 2016/7/27 17:29.
 * Desc:多文件下载工具
 */
public class FileDownloader {


    static FileDownloader mDownloader;


    Downloader downloader;
    FilenameGenerator filenameGenerator;
    boolean debug;

    DiskCache mDiskCache;

    Dispatcher mDispatcher;


    public FileDownloader(Downloader downloader, FilenameGenerator filenameGenerator, DiskCache cache, boolean debug ) {
        this.downloader = downloader;
        this.filenameGenerator = filenameGenerator;
        this.debug = debug;
        this.mDiskCache = cache;
        mDispatcher = new Dispatcher(this,downloader);
    }

    public DiskCache getDiskCache() {
        return mDiskCache;
    }

    public static FileDownloader single(Context context) {
        if (mDownloader == null) {
            synchronized (FileDownloader.class) {
                if (mDownloader == null) {
                    mDownloader = new Builder(context).build();
                }
            }
        }
        return mDownloader;
    }

    public void download(String url) {
        download(url, null,null);
    }
    public void download(String url, DownloadListener listener) {
        download(url,null,listener);
    }

    /**
     *
     * @param url
     * @param listener
     * @param target
     */
    public boolean download(String url, Object target, DownloadListener listener) {

        return download(url, target,null,listener);
    }

    public boolean download(String url, FilenameGenerator generator, DownloadListener listener) {

        return download(url, null, generator, listener);
    }

    /**
     *
     * @param url
     * @param target
     * @param generator 用于生成自定义的文件名
     * @param listener  下载监听
     * @return
     */
    public boolean download(String url, Object target, FilenameGenerator generator, DownloadListener listener) {
        checkMain();
        FilenameGenerator gen= generator;
        if(gen==null){
            gen=this.filenameGenerator;
        }
        File result = mDiskCache.get(url,gen);
        if (result != null) {
            listener.onFinish(url,result);
            return true;
        }
        String filename=gen.generate(url);
        Request request = new Request(this, Uri.parse(url), target,(listener) , mDiskCache.createDiskFilePath(filename));
        mDispatcher.submit(request);
        return false;
    }

    public File getCacheFile(String url){
       return getCacheFile(url, null);
    }
    public File getCacheFile(String url, FilenameGenerator generator){
        if(generator==null){
            generator=filenameGenerator;
        }
        return mDiskCache.get(url,generator);
    }
    public String generateDiskCacheFilePath(String url){
        return new File(mDiskCache.getBaseDir(),filenameGenerator.generate(url)).getAbsolutePath();
    }


    public void cancel(String uri){
        mDispatcher.cancel(uri);
    }

    public static void init(FileDownloader downloader) {
        synchronized (FileDownloader.class) {
            if (mDownloader == null) {
                throw new IllegalStateException("FileDownloader instance has already exists");
            }
        }
        mDownloader = downloader;
    }

    public static class Builder {

        Downloader downloader;
        FilenameGenerator filenameGenerator;
        Boolean debug;

        Context context;

        DiskCache cache;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder setDownloader(Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        public Builder setFilenameGenerator(FilenameGenerator filenameGenerator) {
            this.filenameGenerator = filenameGenerator;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public FileDownloader build() {

            if (filenameGenerator == null) {
                filenameGenerator = new MD5FilenameGenerator();
            }
            if (debug == null) {
                debug = BuildConfig.DEBUG;
            }
            if (cache == null) {
                cache = new DefaultDiskCache(context);
            }
            if (downloader == null) {
                downloader = new UrlConnectionDownloader();
            }
            return new FileDownloader(downloader, filenameGenerator, cache, debug);
        }
    }


}
