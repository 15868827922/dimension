package com.zjlp.httpvolly;

import android.os.Parcel;
import android.os.Parcelable;

public class RequestError implements Parcelable {

    private int errorType;
    private int errorCode;
    private String errorReason;
    private boolean isUnknownErrorCode;
    private boolean isNetNoConnect;
    private String data;
    public int getErrorType() {
        return errorType;
    }
    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }
    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorReason() {
        return errorReason;
    }
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public boolean isUnknownErrorCode() {
        return isUnknownErrorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**非业务错误，主要为网络错误等情形*/
    public void setUnknownErrorCode(boolean unknownErrorCode) {
        isUnknownErrorCode = unknownErrorCode;
    }

    /** 用于判断是否无网络的请求*/
    public boolean isNetNoConnect() {
        return isNetNoConnect;
    }

    public void setNetNoConnect(boolean netNoConnect) {
        isNetNoConnect = netNoConnect;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errorType);
        dest.writeInt(this.errorCode);
        dest.writeString(this.errorReason);
        dest.writeByte(this.isUnknownErrorCode ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNetNoConnect ? (byte) 1 : (byte) 0);
        dest.writeString(this.data);
    }

    public RequestError() {
    }

    protected RequestError(Parcel in) {
        this.errorType = in.readInt();
        this.errorCode = in.readInt();
        this.errorReason = in.readString();
        this.isUnknownErrorCode = in.readByte() != 0;
        this.isNetNoConnect = in.readByte() != 0;
        this.data = in.readString();
    }

    public static final Parcelable.Creator<RequestError> CREATOR = new Parcelable.Creator<RequestError>() {
        @Override
        public RequestError createFromParcel(Parcel source) {
            return new RequestError(source);
        }

        @Override
        public RequestError[] newArray(int size) {
            return new RequestError[size];
        }
    };
}
