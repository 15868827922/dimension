package com.shenjing.dimension.dimension.base.request.get;

import android.content.Context;

import com.android.volley.Request;
import com.shenjing.dimension.dimension.base.request.HttpRequestCallback;
import com.shenjing.dimension.dimension.base.request.URLManager;
import com.shenjing.dimension.dimension.main.LPApplicationLike;
import com.zjlp.httpvolly.HttpService;

import org.json.JSONException;
import org.json.JSONObject;


public class AccountService {
    
    public static Request login(String userName, String password, boolean isShowLoadingView, HttpRequestCallback callback){
        CookieUtils.removeAllCookie(LPApplicationLike.getContext());
        String requestURL = URLManager.getRequestURL(URLManager.Method_Login);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", userName);
            jsonObject.put("user_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return HttpService.doPost(requestURL, jsonObject, callback, true, isShowLoadingView, false);
    }

    public static Request register(String userName, String password, boolean isShowLoadingView, HttpRequestCallback callback){
        CookieUtils.removeAllCookie(LPApplicationLike.getContext());
        String requestURL = URLManager.getRequestURL(URLManager.Method_Register);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", userName);
            jsonObject.put("user_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return HttpService.doPost(requestURL, jsonObject, callback, true, isShowLoadingView, false);
    }

    public static Request reLogByWeChat(String unionid, String openid, String nickname, String headimgurl, String gender, boolean isShowLoadingView, HttpRequestCallback callback){
        CookieUtils.removeAllCookie(LPApplicationLike.getContext());
        String requestURL = URLManager.getRequestURL(URLManager.Method_Log_Chat);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("unionid", unionid);
            jsonObject.put("openid", openid);
            jsonObject.put("nickname", nickname);
            jsonObject.put("headimgurl", headimgurl);
            jsonObject.put("sex", gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return HttpService.doPost(requestURL, jsonObject, callback, true, isShowLoadingView, false);
    }


    public static Request reLogByQQ(String unionid, String openid, String nickname, String headimgurl, String gender, boolean isShowLoadingView, HttpRequestCallback callback){
        CookieUtils.removeAllCookie(LPApplicationLike.getContext());
        String requestURL = URLManager.getRequestURL(URLManager.Method_Log_QQ);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oathLoginType", "TxLogin");
            jsonObject.put("union_id", unionid);
            jsonObject.put("open_id", openid);
            jsonObject.put("nickname", nickname);
            jsonObject.put("headimgurl", headimgurl);
            jsonObject.put("sex", gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return HttpService.doPost(requestURL, jsonObject, callback, true, isShowLoadingView, false);
    }
    

    
    /**
     * 登出，仅包含登出逻辑，不含页面跳转
     * @param context
     * @param reLogin 是否需要重新登陆
     */
    public static void logout(Context context, boolean reLogin){
//        LPCacheUtil.exit(LPApplicationLike.getContext());
        /*UserInfo userInfo = LPApplicationLike.getInstance().getUserInfo();
        if (!reLogin){
//            IMProxy.reqDisconnect(context);
            IMServiceClient.disConnectXmpp(context);
            IMBackgroundService.setUserInfoAndSessionId(context, new BasicUserInfo("", 0), "");
//            IMProxy.reqSetLPAccountLogin(context, "");
            IMManager.getInstance().destroy();
            PushService.stopWork();
            CheckSpeakService.closeCheckSpeak(context);
            LPPushMessageReceiver.isRegistToServerFinished = false;
            LPPushMessageReceiver.pushTryRegistTimes = 0;
            MainProcessService.requestLoginOut(context);
            LPApplicationLike.getInstance().setUserInfo(null);
            LPApplicationLike.getInstance().setCardInfo(null);
            ChatUserListManager.init().clear();
            LPApplicationLike.getInstance().setCurrChatUserName(null);
            FriendOperateUtils.reqFriendListType = 0;
            FriendOperateUtils.reqFriendListCount = 0;
            FriendOperateUtils.reqAllFriendListCount = 0;
            CommunityService.destroy();
            IMServiceClient.setCurrentChatUser(context,null);
            SharePreferenceUtil.saveIMCurrUserId(context, 0);
            SharePreferenceUtil.setLoginAccountLpNo(context, "");
            FriendInfoFetcher.clear();
            CookieUtils.removeAllCookie(context.getApplicationContext());
            SharePreferenceUtil.saveAutoLogin(context, reLogin);
            SharePreferenceUtil.savePassword(context, "");
            HotSearchData.getItemList().clear();
            ChatSearchSource.clearChatSource();
        }else{
            //还需继续排查什么情况下这里的userInfo会为null
            if(userInfo != null){
                CookieUtils.removeAllCookie(context.getApplicationContext());
                LoginActivity.LoginRequestCallback loginRequestCallback = new LoginActivity.LoginRequestCallback(context, userInfo, true);
                loginRequestCallback.setForSessionExpiredAndReLogin(true);
                login(userInfo.getUserName(), userInfo.getPassword(), true, loginRequestCallback);
            }
        }*/
    }


}
