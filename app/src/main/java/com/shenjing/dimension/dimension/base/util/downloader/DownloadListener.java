package com.shenjing.dimension.dimension.base.util.downloader;

import java.io.File;

/**
 * Created by Tiny  on 2016/7/27 17:38.
 * Desc:
 */
public interface DownloadListener {

    void onError(String url, Exception e);

    void onProgress(long total, long progress, String url);

    void onFinish(String url, File file);

    void onCancel();

}
