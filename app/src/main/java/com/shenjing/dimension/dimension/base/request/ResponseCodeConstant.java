package com.shenjing.dimension.dimension.base.request;

import android.util.SparseIntArray;
import com.shenjing.dimension.R;

/**
 * Created by liht  on 2016/10/4 10:02.
 * Desc:
 */
public class ResponseCodeConstant {

    public static final int CODE_LOCAL_ERROR_CONNECT_FAILED = -1;     //网络请求失败

    public static final int Code_ShowMsgFromServer = -1000;
    public static final int Code_Logout = -99;

    public static final int Code_Success = 0;                  //请求成功



    //////////////////////////////////////请求的错误类型，分别是http本身的错误和服务内部错误///////////////////////////////////////////////
    public static final int HTTP_ERROR = 1;  //表示请求的时候发生了http错误，比如404,500
    public static final int PARAM_ERROR = 2; //表示请求的时候发生了参数错误，比如sessionId错误

    public static final int Code_PhoneNotExist = 40002;     //手机号码不存在

    public static final SparseIntArray respCodeStrMap = new SparseIntArray();
    static {
        respCodeStrMap.put(Code_PhoneNotExist, R.string.save);

    }


}