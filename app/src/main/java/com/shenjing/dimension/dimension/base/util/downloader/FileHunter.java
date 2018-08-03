package com.shenjing.dimension.dimension.base.util.downloader;

import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * Created by Tiny  on 2016/7/28 10:22.
 * Desc:
 */
public class FileHunter implements Runnable {

    Request request;

    Downloader downloader;

    Dispatcher dispatcher;

    Downloader.Result result;

    ArrayList<Request> attached;

    Future<?> future;

    boolean mCancel;

    public FileHunter(Request request, Downloader downloader, Dispatcher dispatcher) {
        this.request = request;
        this.downloader = downloader;
        this.dispatcher = dispatcher;
    }


    public void attach(Request request) {
        if (attached == null) {
            attached = new ArrayList<Request>();
        }
        attached.add(request);

    }

    public ArrayList<Request> getAttachedRequest() {
        return attached;
    }

    /**
     * 是否已经存在完全相同的request
     * 判断条件
     * @return
     */
    public boolean  contain(Request request){
        if(this.request.equals(request)){
            return true;
        }
        if(attached!=null){
            ArrayList<Request> requests=attached;
            for(Request r:requests){
                if(r.equals(request)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        if(mCancel){
           return;
        }
        Downloader.Result result = downloader.load(request.uri, request.outputPath, this);
        if(mCancel){
            return ;
        }
        this.result = result;
        if (result.result != null) {
            dispatcher.dispatchSuccess(this);
        } else {
            dispatcher.dispatchFailed(this);
        }
    }
    public void cancel(){
        if(!mCancel){
            if(future!=null){
                future.cancel(true);
            }
            if(request!=null){
                request.cancel();
                request=null;
            }
            if(attached!=null&&!attached.isEmpty()){
                for(Request request:attached){
                    request.cancel();
                }
                attached.clear();
            }

            mCancel=true;
        }
    }
}
