package com.shenjing.dimension.dimension.base.util.downloader;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tiny  on 2016/7/28 9:53.
 * Desc:
 */
public class Dispatcher {

    private static final int MESSAGE_SUCCESS = 101;
    private static final int MESSAGE_FAIL = 102;
    private static final int MESSAGE_PROGRESS = 103;


    FileDownloader downloader;

    ExecutorService executor;
    Downloader mDownloadImpl;

    Map<String, FileHunter> hunters;


    public Dispatcher(FileDownloader downloader, Downloader loader) {
        this.downloader = downloader;
        executor = Executors.newFixedThreadPool(3);
        this.mDownloadImpl = loader;
        hunters = new LinkedHashMap<String, FileHunter>();
        if (loader instanceof UrlConnectionDownloader) {
            ((UrlConnectionDownloader) loader).attach(this);
        }

    }

    public void submit(Request request) {
        String key = request.uri.toString();
        FileHunter loading = hunters.get(key);
        if (loading != null) {
            if (loading.contain(request)) {
                Logger.w("submit","Submit a exactly same task :"+request.toString());
                return;
            } else
                loading.attach(request);
            return;
        }
        FileHunter hunter = createHunter(request);
        hunter.future = executor.submit(hunter);
        hunters.put(request.uri.toString(), hunter);
    }

    public void success(FileHunter hunter) {
        Downloader.Result result = hunter.result;
        hunter.request.complete(result.result);
        ArrayList<Request> attached = hunter.getAttachedRequest();
        if (attached != null) {
            deliverResult(attached, result);
        }
        hunters.remove(hunter.request.uri.toString());
    }

    private void deliverResult(ArrayList<Request> requests, Downloader.Result result) {
        for (Request request : requests) {
            if (result.result != null) {
                request.complete(result.result);
            } else {
                request.fail(result.exception);
            }
        }
    }

    public void fail(FileHunter hunter) {
        Downloader.Result result = hunter.result;
        hunter.request.fail(result.exception);
        ArrayList<Request> attached = hunter.getAttachedRequest();
        if (attached != null) {
            deliverResult(attached, result);
        }
        hunters.remove(hunter.request.uri.toString());
    }
    public void cancel(String uri){
        String key=uri;
        FileHunter hunter=hunters.remove(key);
        if(hunter!=null){
            hunter.cancel();
            Logger.d("cancel","cancel uri = "+uri);
        }
    }

    public void dispatchSuccess(FileHunter hunter) {
        HANDLER.obtainMessage(MESSAGE_SUCCESS, hunter).sendToTarget();
    }

    public void dispatchFailed(FileHunter hunter) {
        HANDLER.obtainMessage(MESSAGE_FAIL, hunter).sendToTarget();
    }

    public void dispatchProgress(UrlConnectionDownloader.Progress progress) {
        HANDLER.obtainMessage(MESSAGE_PROGRESS, progress).sendToTarget();
    }

    private FileHunter createHunter(Request request) {
        return new FileHunter(request, mDownloadImpl, this);
    }

    static Handler HANDLER = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_SUCCESS: {
                    FileHunter hunter = (FileHunter) msg.obj;
                    hunter.dispatcher.success(hunter);
                }
                break;
                case MESSAGE_FAIL: {
                    FileHunter hunter = (FileHunter) msg.obj;
                    hunter.dispatcher.fail(hunter);

                }
                break;
                case MESSAGE_PROGRESS:
                    UrlConnectionDownloader.Progress progress = (UrlConnectionDownloader.Progress) msg.obj;
                    FileHunter hunter = progress.hunter;
                    if(hunter.mCancel){
                        return;
                    }
                    DownloadListener listener = hunter.request.listener;
                    if (listener != null) {
                        listener.onProgress(progress.total, progress.progress, progress.url);
                    }
                    ArrayList<Request> requests = hunter.getAttachedRequest();
                    int attached=0;
                    if (requests != null) {
                        for (Request q : requests) {
                            if (q.listener != null) {
                                q.listener.onProgress(progress.total, progress.progress, progress.url);
                            }
                        }
                        attached=requests.size();
                    }
                    break;
            }
        }
    };


}
