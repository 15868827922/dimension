package com.zjlp.httpvolly.test;

import android.content.Context;
import android.os.HandlerThread;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.zjlp.httpvolly.BaseJsonRequest;
import com.zjlp.httpvolly.BaseRequestCallback;
import com.zjlp.httpvolly.VolleyConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tiny  on 2016/9/14 13:53.
 * Desc: 用于单元测试
 */

public class TestVolley {

    private static RequestQueue mQueue; //volley 请求队列

    private static final HandlerThread sThread = new HandlerThread("ApiTestThread");

    static {
        sThread.start();
    }

    public static void init(Context context) {
        mQueue = Volley.newRequestQueue(context, new TestResponseDelivery());
    }

    /**
     * Test
     *
     * @return
     */
    public static TestJsonRequest doPost(String url, JSONObject jsonRequest, boolean isNeedEncrypt, boolean isNeedSessionId) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject paramsJson = null;
        paramsJson = new JSONObject();
        try {
            if (jsonRequest == null) {
                jsonRequest = new JSONObject();
            }
            String jsonString = jsonRequest.toString();
            int versionCode = VolleyConfig.getVersionCode();
            if (isNeedEncrypt) {
                String encryptedString = BaseJsonRequest.getEncryptedString(jsonString, true);
                paramsJson.put("data", encryptedString);
            } else {
                paramsJson.put("data", jsonString);
            }
            paramsJson.put("versionCode", versionCode);
            paramsJson.put("device", 1);
            //这里为了使后台判断该请求来自jvm，而不是android设备
            //不做该处理后端解密不了数据
                paramsJson.put("source", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("reqUrl:" + url);
        System.out.println("postData:" + jsonRequest.toString());
        TestJsonRequest request = new TestJsonRequest(url, paramsJson, isNeedSessionId, future);
        mQueue.add(request);
        return request;
    }

    /**
     * Test
     *
     * @return
     */
    public static TestJsonRequest doPost(String url, JSONObject jsonRequest, BaseRequestCallback callback) {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JSONObject paramsJson = null;
        paramsJson = new JSONObject();
        try {
            if (jsonRequest == null) {
                jsonRequest = new JSONObject();
            }
            String jsonString = jsonRequest.toString();
            int versionCode = VolleyConfig.getVersionCode();
            if (true) {
                String encryptedString = BaseJsonRequest.getEncryptedString(jsonString, true);
                paramsJson.put("data", encryptedString);
            } else {
                paramsJson.put("data", jsonString);
            }
            paramsJson.put("versionCode", versionCode);
            paramsJson.put("device", 1);
            //这里为了使后台判断该请求来自jvm，而不是android设备
            //不做该处理后端解密不了数据
                paramsJson.put("source", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("reqUrl:" + url);
        System.out.println("postData:" + jsonRequest.toString());
        TestJsonRequest request = new TestJsonRequest(url, paramsJson, true, future);
        mQueue.add(request);
        HttpResult result = request.execute();
        if (result.isSuccessful()) {
            callback.onFinished(result.result());
        } else {
            callback.onFailed(result.errorResult());
        }

        return request;
    }

    public static <T> T request(String url, JSONObject params, ResultConverter<T> converter) throws Exception {

        TestJsonRequest request = TestVolley.doPost(url, params, true, true);
        HttpResult result = request.execute();
        if (result.isSuccessful()) {
            return converter.convert(result);
        } else {
            throw new RequestException(url, params,result.errorResult().getErrorCode());
        }
    }

    public static HttpResult request(String url, JSONObject params) throws Exception {

        return request(url, params, DEFAULT);
    }

    static ResultConverter<HttpResult> DEFAULT = new TestVolley.ResultConverter<HttpResult>() {
        @Override
        public HttpResult convert(HttpResult result) throws Exception {
            return result;
        }
    };

    public interface ResultConverter<T> {
        T convert(HttpResult result) throws Exception;
    }

    public abstract static class ListResultConverter<E> implements ResultConverter<ArrayList<E>> {

        String key;

        public ListResultConverter(String key) {
            this.key = key;
        }

        public ListResultConverter() {
            this.key = "datas";
        }

        @Override
        public ArrayList<E> convert(HttpResult result) throws Exception {
            ArrayList<E> arrayList = new ArrayList<E>();
            JSONObject jsonObject = result.result();
            JSONArray array = jsonObject.getJSONObject("data").getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                arrayList.add(convertJson(array.getJSONObject(i)));
            }
            return arrayList;
        }

        public abstract E convertJson(JSONObject element) throws Exception;

    }

    public static class BooleanResultConverter implements ResultConverter<Boolean> {
        @Override
        public Boolean convert(HttpResult result) throws Exception {
            return true;
        }
    }

    public abstract static class ObjectResultConverter<T> implements ResultConverter<T> {

        @Override
        public T convert(HttpResult result) throws Exception {
            JSONObject jsonObject = result.result();
            JSONObject data = jsonObject.getJSONObject("data");
            return fromJson(data);
        }

        public abstract T fromJson(JSONObject jsonObject);

    }


}
