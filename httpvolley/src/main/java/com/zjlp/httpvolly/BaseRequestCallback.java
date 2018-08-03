package com.zjlp.httpvolly;

import android.content.Context;

import org.json.JSONObject;

/**
 * 
 * @author caiyiping Time: 2014/12/11 Desc:HTTP请求回调
 *         需要注意的是，如果HttpRequestCallback被继承，子类复写onStarted, onFinished,
 *         onCancelled, onFailed等回调方法时，一定要调用super.onXXXX()方法来促使父类的回调方法会被调用
 *
 */
public abstract class BaseRequestCallback implements RequestCallback {

	private boolean showLoadingDialog;
	private Context context;
    String reqUrl;

	protected int responseCode;

	public BaseRequestCallback(Context aContext) {
		context = aContext;
	}

	public void onStarted(ProgressCancelNotifer progressCancelNotifer) {
		if (showLoadingDialog&&context!=null) {
			CustomProgress.showLoadingDialog(context, null, progressCancelNotifer);
		}
	}

	public void onFinished(JSONObject jsonObject) {
		if (showLoadingDialog) {
			CustomProgress.dismissLoadingDialog();
		}
	}

	public void onFailed(RequestError error) {
		if (showLoadingDialog) {
			CustomProgress.dismissLoadingDialog();
		}

	}

	public void onCancelled() {
		if (showLoadingDialog) {
			CustomProgress.dismissLoadingDialog();
		}
	}

	public void onQuitApp(String message){
		if (showLoadingDialog) {
			CustomProgress.dismissLoadingDialog();
		}
	}

	public boolean isShowLoadingDialog() {
		return showLoadingDialog;
	}

	public void setShowLoadingDialog(boolean showLoadingDialog) {
		this.showLoadingDialog = showLoadingDialog;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

    public String getReqUrl() {
        return reqUrl;
    }
}
