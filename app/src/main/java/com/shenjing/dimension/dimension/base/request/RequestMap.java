package com.shenjing.dimension.dimension.base.request;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by tiny  on 2016/10/28 10:56.
 * Desc:
 */

public class RequestMap {


    HashMap<String,Request> map=new HashMap<String, Request>();



    public static RequestMap newInstance(){
        return new RequestMap();
    }

    private RequestMap() {
        this.map = new HashMap<String, Request>();
    }

    /**
     *
     * @param key
     * @param request
     */
    public void add(String key, Request request){
        cancel(key);
        map.put(key,request);
    }

    public void cancel(String url){
        Request request=map.get(url);
        if(request!=null&&!request.isCanceled()){
            request.cancel();
        }
    }
    public Request remove(String key){
       return map.remove(key);
    }
    public boolean contain(String key){
        return map.containsKey(key);
    }
    public void clear(){
        Set<Map.Entry<String,Request>> set=map.entrySet();
        for(Map.Entry<String,Request> entry:set){
            cancel(entry.getKey());
        }
        map.clear();
    }
    public static void cancelRequest(Request request){
        if(request!=null&&!request.isCanceled()){
            request.cancel();
        }
    }
}
