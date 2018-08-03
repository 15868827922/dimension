package com.zjlp.httpvolly.test;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;
import com.zjlp.httpvolly.HttpConstant;
import com.zjlp.httpvolly.RequestError;
import com.zjlp.httpvolly.VolleyConfig;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tiny  on 2016/9/14 13:53.
 * Desc: 用于单元测试
 */
public class TestJsonRequest extends com.android.volley.toolbox.JsonObjectRequest {


    private boolean mNeedSessionId;

    private RequestFuture<JSONObject> mFuture;


    public TestJsonRequest(String url, JSONObject jsonRequest, boolean needSessionId, RequestFuture<JSONObject> future) {
        super(url, jsonRequest, future, future);
        this.mNeedSessionId = needSessionId;
        mFuture = future;

    }

    @Override
    public void cancel() {// http请求被撤销的回调
        super.cancel();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mNeedSessionId) {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Cookie", HttpConstant.COOKIE_KEY_SESSION + "=" + VolleyConfig.getSessionId());
            return headers;
        }

        return super.getHeaders();
    }

    public HttpResult execute()  {

        JSONObject jsonResponse = null;
        try {
            jsonResponse = mFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("onResponse: " + jsonResponse.toString());
        JSONObject jsonObject;
        int responseCode = 0;
        String msg = "";
        jsonObject = jsonResponse.optJSONObject("data");
        responseCode = jsonObject.optInt("code");
        msg = jsonObject.optString("msg", "");
        if (responseCode == 0) {
            return HttpResult.success(jsonResponse.optJSONObject("data"));
        } else {
            RequestError error = new RequestError();
            error.setErrorCode(responseCode);
            return HttpResult.error(error);
        }
    }
}
