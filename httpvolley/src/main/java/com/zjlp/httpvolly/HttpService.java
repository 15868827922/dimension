package com.zjlp.httpvolly;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zjlp.httpvolly.test.TestVolley;

import org.json.JSONObject;

/**
 * 
 * @author caiyiping
 * Time: 2014/12/11
 * Desc: 用来提供HTTP请求服务
 *
 */
public class HttpService {
    /**
     * 是否在单元测试状态
     */
    private static boolean TEST;


    public static void test(boolean debug) {
        TEST = debug;
    }
    public static boolean isTest(){
        return TEST;
    }
    private static HttpService instance;

    private RequestQueue queue; //volley 请求队列
//    private RequestQueue minorQueue; //低优先级的请求队列

    private HttpService() {

        queue = Volley.newRequestQueue(VolleyConfig.getContext(), 0);
        //创建多个RequestQueue会同时发送多个请求，尼玛，不能这样干！！！
//        minorQueue = Volley.newRequestQueue(LPApplication.getInstance());
    }

    public synchronized static HttpService getInstance() {
        if(instance == null){
            instance = new HttpService();
        }



        return instance;
    }
    
    @SuppressWarnings("rawtypes")
    public static Request doGet(String url, Response.Listener<String> respListener, ErrorListener errorListener){
        return HttpService.getInstance().performGet(url, respListener, errorListener);
    }

    /**
     * 暴露给外部的接口，外部发Http Get请求可以使用这个方法
     * @param url
     * @param callback
     * @param isNeedEncrypt
     * @param isStandard 是否标准加密，注由于Android默认加密与JAVA不一致，true-Java加密， false-Android加密
     * @param showLoadingDialog
     */
    @SuppressWarnings("rawtypes")
    public static Request doGet(String url, BaseRequestCallback callback, boolean isNeedEncrypt, boolean isStandard,
                                boolean showLoadingDialog, boolean isNeedSessionId) {
        return HttpService.getInstance().performGet(url, callback, isNeedEncrypt, isStandard, showLoadingDialog,isNeedSessionId);
    }

    @SuppressWarnings("rawtypes")
    public static Request doPost(String url, JSONObject jsonObject, BaseRequestCallback callback,
                                 boolean isNeedEncrypt, boolean showLoadingDialog,boolean isNeedSessionId) {
        return doPostStandard(url, jsonObject, callback, isNeedEncrypt, false, showLoadingDialog,isNeedSessionId);
    }

    /**
     * 暴露给外部的接口，外部发Http Post请求可以使用这个方法
     * @param url
     * @param jsonObject
     * @param callback
     * @param isNeedEncrypt
     * @param isStandard 是否标准加密，注由于Android默认加密与JAVA不一致，true-Java加密， false-Android加密
     * @param showLoadingDialog
     */
    @SuppressWarnings("rawtypes")
    public static Request doPostStandard(String url, JSONObject jsonObject, BaseRequestCallback callback,
            boolean isNeedEncrypt, boolean isStandard, boolean showLoadingDialog,boolean isNeedSessionId) {
        return HttpService.getInstance().performPost(url, jsonObject, callback, isNeedEncrypt, isStandard,
                showLoadingDialog,isNeedSessionId);
    }

    
////////////////////////////////////////////私有方法////////////////////////////////////////////////////////
    
    @SuppressWarnings("rawtypes")
    private Request performGet(String url, Response.Listener<String> respListener, ErrorListener errorListener){
        StringRequest request = new StringRequest(Request.Method.GET, url, respListener, errorListener);
        queue.add(request);
        return request;
    }
    
    @SuppressWarnings("rawtypes")
    private Request performGet(String url, BaseRequestCallback callback, boolean isNeedEncrypt, boolean isStandard,
                               boolean showLoadingDialog, boolean isNeedSessionId) {
        return sendRequest(url, new JSONObject(), callback, isNeedEncrypt, isStandard, showLoadingDialog,isNeedSessionId);
    }

    @SuppressWarnings("rawtypes")
    private Request performPost(String url, JSONObject jsonObject, BaseRequestCallback callback,
            boolean isNeedEncrypt, boolean isStandard, boolean showLoadingDialog, boolean isNeedSessionId) {
        return sendRequest(url, jsonObject, callback, isNeedEncrypt, isStandard, showLoadingDialog,isNeedSessionId);
    }

    /**
     * 发请求最终都是调用这个方法
     * @param url
     * @param jsonObject
     * @param callback
     * @param isNeedEncrypt
     * @param showLoadingDialog
     */
    @SuppressWarnings("rawtypes")
    private Request sendRequest(String url, JSONObject jsonObject, BaseRequestCallback callback,
            boolean isNeedEncrypt, boolean isStandard, boolean showLoadingDialog,boolean isNeedSessionId) {
        if(isTest()){
            return TestVolley.doPost(url,jsonObject,callback);
        }
//        if(isNeedSessionId){
//            url = url + ";jsessionid=" + VolleyConfig.getSessionId();
//        }

        if(callback != null){
            callback.setShowLoadingDialog(showLoadingDialog);
            callback.reqUrl = url;
        }
        FakeX509TrustManager.allowAllSSL();
        baseJsonRequest = new BaseJsonRequest(url, jsonObject, callback,isNeedEncrypt, isStandard, isNeedSessionId);

        baseJsonRequest.notifyRequestStart();
        Request request = queue.add(baseJsonRequest.getJsonObjectRequest());
        return request;
    }

    BaseJsonRequest baseJsonRequest;

    public void onActivityDestroy(Activity a){
        if(baseJsonRequest != null && baseJsonRequest.getCallback() != null) {
            if(baseJsonRequest.getCallback().getContext() == a){
                baseJsonRequest.removeCallback();
            }
        }
    }
    
    /**
     * 获取下拉刷新失败的提示文字，目前暂时未用到error变量，均做统一提示，这么写是为了方便后期拓展
     * @param error 错误信息，如requestError或Exception等
     * @return
     */
    public static String getSimpleRefreshFailedToastMsg(Object error){
        return "网络不给力，请稍后再试";
    }
    
    public static String getSimpleLoadMoreFailedToastMsg(Object error){
        return "网络不给力，请稍后再试";
    }

//    private static class HttpServiceClassHolder {
//        public static HttpService httpService = new HttpService();
//    }
}
