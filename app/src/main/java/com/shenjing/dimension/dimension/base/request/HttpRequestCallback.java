package com.shenjing.dimension.dimension.base.request;

import android.content.Context;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.MD5;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.shenjing.dimension.dimension.base.network.NetworkState;
import com.shenjing.dimension.dimension.base.request.aes.Base64Decoder;
import com.zjlp.httpvolly.BaseRequestCallback;
import com.zjlp.httpvolly.RequestError;

import org.json.JSONException;
import org.json.JSONObject;

import com.shenjing.dimension.R;
import com.zjlp.httpvolly.log.LPLog;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * Created by syj  on 2016/10/4 9:40.
 * Desc: openFire返回数据统一解析
 */
public class HttpRequestCallback extends BaseRequestCallback {

    //** edit  by liht on 2016/11/18 14:23
    // 用于处理Session过期的处理，具体事务交由外部处理
    // */
    private static OnSessionListner onSessionListner;
    public HttpRequestCallback(Context aContext) {
        super(aContext);
    }

    public static void setOnSessionListner(OnSessionListner onSessionListner){
        HttpRequestCallback.onSessionListner = onSessionListner;
    }

    @Override
    public void onParse(Object obj) {
        if (!(obj instanceof JSONObject)){
            return;
        }
        JSONObject jsonObject = (JSONObject) obj;
       /* try {
            jsonObject = jsonObject.getJSONObject("data");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }*/
        int responseCode = 0;
        String objStr = "";
        String msg = "";
//        responseCode = jsonObject.optInt("result");
        objStr = jsonObject.optString("result","");

        String encode = MD5.encode(getNowTime());
        if (objStr.contains(encode)){
            objStr.replace(encode,"");
        }

        objStr = Base64Decoder.decode(objStr);

        try {
            jsonObject = new JSONObject(objStr);
            responseCode = jsonObject.optInt("ret");
            msg = jsonObject.optString("msg");
            LPLog.print(getClass(), "onResponse: " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResponseCode(responseCode);
        if (responseCode == 200) {
            onFinished(jsonObject);
        }else if (responseCode == -99 || responseCode == 40000 || responseCode == 39999){
            onQuitApp(msg);
        }else {
            RequestError error = new RequestError();
            error.setErrorType(ResponseCodeConstant.PARAM_ERROR);
            error.setErrorCode(responseCode);
            error.setErrorReason(msg);
            setResponseCode(responseCode);
            if(TextUtils.isEmpty(error.getErrorReason())){
                if(ResponseCodeConstant.respCodeStrMap != null && ResponseCodeConstant.respCodeStrMap.get(responseCode) != 0){
                    error.setErrorReason(getContext().getString(ResponseCodeConstant.respCodeStrMap.get(responseCode)));
                }else if(responseCode != ResponseCodeConstant.Code_ShowMsgFromServer){
                    error.setUnknownErrorCode(true);
                    error.setErrorReason(getNetworkErrorString());
                }
            }
            error.setData(jsonObject.optString("data"));
            onFailed(error);
        }

    }

    public static String getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    @Override
    public void onVollyError(VolleyError volleyError) {
        int errorCode = volleyError.networkResponse == null? ResponseCodeConstant.CODE_LOCAL_ERROR_CONNECT_FAILED : volleyError.networkResponse.statusCode;
        RequestError error = new RequestError();
        error.setErrorType(ResponseCodeConstant.HTTP_ERROR);
        error.setErrorCode(errorCode);
        error.setUnknownErrorCode(true);
        error.setErrorReason(getNetworkErrorString());
        if(volleyError instanceof NoConnectionError){
            error.setNetNoConnect(true);
        }
        onFailed(error);

        try {
            //仅当连接正式环境时才做这个统计
            if(!LPLog.enable){
                boolean isNetworkConnected = false;
                String networkTypeName = "";
                NetworkInfo networkInfo = NetworkState.getNetworkInfo(getContext());
                if(networkInfo != null){
                    isNetworkConnected = networkInfo.isConnected();
                    networkTypeName = networkInfo.getTypeName() + "[" + networkInfo.getSubtypeName() + "]";
                }

            }
        } catch (Exception e) {}

    }

    private String filterUrlSession(String url){
        if(TextUtils.isEmpty(url)){
            return "";
        }
        int cutIndex = url.indexOf(";");
        return (cutIndex >= 0)? url.substring(0, cutIndex) : url;
    }

    public interface OnSessionListner{
        void onSessionFailed();
    }


    /**
     *  得到network_error 字符串  如果getContext 为空则直接返回字符串防止报错
     * @return
     */
    private String getNetworkErrorString () {

        if (getContext() == null) {
            return "网络不给力，请稍后再试";
        } else  {
            return getContext().getString(R.string.network_error);
        }

    }
}
