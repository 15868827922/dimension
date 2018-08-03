package com.zjlp.httpvolly.test;

import org.json.JSONObject;

/**
 * Created by tiny  on 2016/9/14 13:53.
 * Desc: 用于单元测试
 */

public class RequestException extends Exception {

    public RequestException(String url, JSONObject params) {
        super("Request Failed : url = "+url+",params = "+params.toString());
    }

    public RequestException(String url, JSONObject params,int errCode) {
        super("Request Failed : url = "+url+"  ,  params = "+params.toString()+" , errorCode = "+errCode);
    }
}
