package com.shenjing.dimension.dimension.base.util.downloader;

import android.net.Uri;

import java.io.IOException;

/**
 * Created by Tiny  on 2016/7/28 10:36.
 * Desc:
 */
public class DownloadException extends IOException {

    public DownloadException(int responseCode, Uri uri, String message) {
        this("download exception : responseCode = "+responseCode+" uri = "+uri +" message = "+message);
    }

    public DownloadException(String detailMessage) {
        super(detailMessage);
    }


}
