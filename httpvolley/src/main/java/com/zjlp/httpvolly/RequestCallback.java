package com.zjlp.httpvolly;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RequestCallback {
    
    void onStarted(ProgressCancelNotifer progressCancelNotifer);

    void onFinished(JSONObject jsonObject);

    void onFailed(RequestError error);
    
    void onCancelled();

    void onParse(Object object);

    void onVollyError(VolleyError volleyError);
}
