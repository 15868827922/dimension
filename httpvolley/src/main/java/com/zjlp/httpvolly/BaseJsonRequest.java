package com.zjlp.httpvolly;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zjlp.httpvolly.log.LPLog;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author caiyiping 
 * Time: 2014/12/11
 * 可以作为请求发送和接收的拦截器
 * 
 */
public class BaseJsonRequest implements ProgressCancelNotifer {

    private boolean needEncrypt;
    private boolean mNeedSessionId;
    private BaseRequestCallback callback;
    private JsonObjectRequest jsonObjectRequest;

    public BaseRequestCallback getCallback(){
        return callback;
    }

    public void removeCallback(){
        callback = null;
    }
    
    public static String getEncryptedString(String jsonString, boolean isStandard){
        /*String encryptedString = null;
        byte[] jsonBytes = jsonString.getBytes();
        if(jsonBytes.length >= 117){//1024的公钥长度最多只能加密117个字节的数据(1024/8-11)
            int stringLength = jsonString.length();
            int midOffset = stringLength / 2;
            String string1 = jsonString.substring(0, midOffset);
            String string2 = jsonString.substring(midOffset, stringLength);
            String encryptedString1 = getEncryptedString(string1, isStandard);
            String encryptedString2 = getEncryptedString(string2, isStandard);
            encryptedString = encryptedString1 + "," + encryptedString2;
        }else{
            //byte[] encryptedData = isStandard ? SecurityUtil.encryptDataStandard(jsonBytes,VolleyConfig.getVolleyConfig().isReleaseEnvironment()) : SecurityUtil.encryptData(jsonBytes,VolleyConfig.getVolleyConfig().isReleaseEnvironment());
            byte[] encryptedData = isStandard ? RSAUtils.encryptDataStandard(jsonBytes) : RSAUtils.encryptData(jsonBytes);
            encryptedString = Base64.encodeToString(encryptedData, Base64.DEFAULT);
        }*/
        return jsonString/*encryptedString*/;
    }

    public BaseJsonRequest(String url, JSONObject jsonRequest, BaseRequestCallback aCallback,
                           boolean aNeedEncrypt, boolean  isStandard, boolean aNeedSessionId) {
        this.callback = aCallback;
        needEncrypt = aNeedEncrypt;
        mNeedSessionId = aNeedSessionId;

        JSONObject encryptedJsonObject;
        if (needEncrypt) {
//            encryptedJsonObject = new JSONObject();
          /*  try {
//                String jsonString = jsonRequest.toString();
                int versionCode = VolleyConfig.getVersionCode();
//                String encryptedString = getEncryptedString(jsonString, isStandard);
//                encryptedJsonObject.put("data", encryptedString);
                jsonRequest.put("versionCode", versionCode);
                jsonRequest.put("device", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        } else {
            encryptedJsonObject = jsonRequest;
        }
        LPLog.print(getClass(), "reqUrl: " + url);
        LPLog.print(getClass(), "postData: " + jsonRequest.toString());
        LPResponseErrorListener responseErrorListener = new LPResponseErrorListener();
        jsonObjectRequest = new JsonObjectRequest(url, jsonRequest,
                new ResponseListener(), responseErrorListener) {
            
            @Override
            public void cancel() {// http请求被撤销的回调
                super.cancel();
                if (callback != null)
                    callback.onCancelled();
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
                if(mNeedSessionId){
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Cookie", HttpConstant.COOKIE_KEY_SESSION + "=" + VolleyConfig.getSessionId());
                    return headers;
                }
                
                return super.getHeaders();
            }
        };
        jsonObjectRequest.setShouldCache(false);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 6, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void notifyRequestStart() {// http请求开始的回调
        if (callback != null) {
            callback.onStarted(this);
        }
    }

    public JsonObjectRequest getJsonObjectRequest() {
        return jsonObjectRequest;
    }

    public boolean isNeedEncrypt() {
        return needEncrypt;
    }

    public void setNeedEncrypt(boolean needEncrypt) {
        this.needEncrypt = needEncrypt;
    }
    
    private static final String TAG = "LPJsonRequest";

    private class LPResponseErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError volleyError) {// http请求失败的回调
        	LPLog.print(getClass(), "onErrorResponse: " + volleyError.getMessage());
            if (callback != null){
                callback.onVollyError(volleyError);
            }
        }
    }

    private class ResponseListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject jsonObject) {// http请求成功的回调

            LPLog.print(getClass(), "onResponse: " + jsonObject.toString());
            if (callback != null){
                callback.onParse(jsonObject);
            }

        }
    }

    @Override
    public void notifyProgressCancel() {
            if(jsonObjectRequest != null){
            jsonObjectRequest.cancel();
        }
    }

}
