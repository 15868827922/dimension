package com.shenjing.dimension.dimension.base.util.downloader;

import android.net.Uri;
import android.support.annotation.MainThread;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Tiny  on 2016/7/27 17:37.
 * Desc:
 */
public class Request {


    long t;


    Uri uri;

    FileDownloader downloader;

    DownloadListener listener;

    String outputPath;


    WeakReference<?> reference;


    public Request(FileDownloader downloader, Uri url, Object target, DownloadListener listener, String outputPath) {
        t = System.currentTimeMillis();
        this.uri = url;
        this.downloader = downloader;
        this.listener = listener;
        this.outputPath = outputPath;
        reference=new WeakReference<Object>(target);
    }



    @MainThread
    public void complete(File file) {
        if (listener != null) {
            listener.onFinish(uri.toString(), file);
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o==null){
            return false;
        }
        if(!(o instanceof  Request)){
            return false;
        }
        Request request= (Request) o;
        Object target=reference.get();
        return this.uri.toString().equals(request.uri.toString())&&(target!=null&&target.equals(request.getTarget()));
    }
    public Object getTarget(){
        return reference.get();
    }

    @MainThread
    public void fail(Exception e) {
        if (listener != null) {
            listener.onError(uri.toString(), e);
        }
    }
    @MainThread
    public void cancel(){
        if (listener != null) {
            listener.onCancel();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append("uri ="+uri+" ");
        builder.append("output ="+outputPath+" ");
        Object target=reference.get();
        builder.append("target = "+ target == null ? " null" : target.toString());
        return builder.toString();
    }
}
