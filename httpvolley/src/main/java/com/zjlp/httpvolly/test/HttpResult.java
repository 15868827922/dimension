package com.zjlp.httpvolly.test;


import com.zjlp.httpvolly.RequestError;

import org.json.JSONObject;

/**
 * Created by tiny  on 2016/9/14 13:53.
 * Desc: 用于单元测试
 */

public class HttpResult {

    private RequestError error;
    private JSONObject result;
    private boolean isSuccessful;

    public static HttpResult success(JSONObject json) {
        return new HttpResult(json);
    }

    public static HttpResult error(RequestError error) {
        return new HttpResult(error);
    }

    HttpResult(RequestError error) {
        this.error = error;
        isSuccessful = false;
    }

    HttpResult(JSONObject result) {
        this.result = result;
        isSuccessful = true;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public JSONObject result() {
        return result;
    }

    public RequestError errorResult() {
        return error;
    }

}

