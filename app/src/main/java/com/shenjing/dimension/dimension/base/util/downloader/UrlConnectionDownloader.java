package com.shenjing.dimension.dimension.base.util.downloader;

import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tiny  on 2016/7/27 17:52.
 * Desc:
 */
public class UrlConnectionDownloader implements Downloader {

    static final String TEMP_SUFFIX = ".temp";

    Dispatcher dispatcher;

    public void attach(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    protected HttpURLConnection openConnection(Uri path) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(path.toString()).openConnection();
        connection.setConnectTimeout(Utils.DEFAULT_CONNECT_TIMEOUT_MILLIS);
        connection.setReadTimeout(Utils.DEFAULT_READ_TIMEOUT_MILLIS);
        return connection;
    }

    @Override
    public Result load(Uri uri, String outputPath, FileHunter hunter) {
        HttpURLConnection connection = null;
        FileOutputStream fos = null;
        InputStream is = null;
        Result result = new Result();
        try {
            connection = openConnection(uri);
            is = connection.getInputStream();
            int code = connection.getResponseCode();
            if (code >= 300) {
                result.exception = new DownloadException(code, uri, connection.getResponseMessage());
            }
            result.resultCode = code;
            int contentLength = connection.getContentLength();
            File output = Utils.createNewFileWithDirs(getTempFilePath(outputPath));
            fos = new FileOutputStream(output);
            byte[] buffer = new byte[4 * 1024];
            int len;
            int progress = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                progress = progress + len;
                dispatcher.dispatchProgress(new Progress(hunter, contentLength, progress, uri.toString()));
            }
            fos.flush();
            File renameTo = new File(outputPath);
            output.renameTo(renameTo);
            result.result = renameTo;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result.exception = e;
        } finally {
            Utils.close(fos);
            Utils.close(is);
        }
        result.result = null;
        return result;
    }

    private String getTempFilePath(String output) {
        return output + TEMP_SUFFIX;
    }

    static class Progress {
        FileHunter hunter;
        long total;
        long progress;
        String url;

        public Progress(FileHunter hunter, long total, long progress, String url) {
            this.hunter = hunter;
            this.total = total;
            this.progress = progress;
            this.url = url;
        }
    }
}
