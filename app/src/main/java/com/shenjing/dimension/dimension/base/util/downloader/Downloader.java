package com.shenjing.dimension.dimension.base.util.downloader;

import android.net.Uri;

import java.io.File;

/**
 * Created by Tiny  on 2016/7/27 17:41.
 * Desc:
 */
public interface Downloader {

    Result load(Uri url, String outputPath, FileHunter hunter)  ;



    static class Result{
        File result;
        int resultCode;
        Exception exception;
    }

}

