package com.shenjing.dimension.dimension.main;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.shenjing.dimension.dimension.base.cathe.SharePreferenceUtil;
import com.shenjing.dimension.dimension.base.debug.Configuration;
import com.shenjing.dimension.dimension.base.debug.DebugUtils;
import com.shenjing.dimension.dimension.base.image.ImageUrlManager;
import com.shenjing.dimension.dimension.base.request.URLManager;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.zjlp.httpvolly.VolleyConfig;


/**
 * Created by SYJ on 2018/3/12.
 * Desc:
 */

public class LPApplicationLike extends MultiDexApplication{


    private static LPApplicationLike mApplication;

    /*private UserInfo userInfo;

    private UserCardInfo cardInfo;

    private LiveRoomInfo roomInfo;
    public static IWXAPI mWxApi;*/


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

//        LPCacheUtil.init(LPApplicationLike.getContext(), null);
        VolleyConfig volleyConfig = new VolleyConfig();
        volleyConfig.setContext(mApplication.getApplicationContext());
        volleyConfig.setVersionCode(ActivityUtil.getVersionCode());
        VolleyConfig.initConfig(volleyConfig);
        configuration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

       /* OssManager.getInstance().init(getContext(),Constants.OSS_ENDPOINT,Constants.OSS_BucketName,Constants.OSS_AccessKey,Constants.OSS_SecretKey);
        //初始化APP
        InitBusinessHelper.initApp(LPApplicationLike.getContext());
        UMConfigure.init(this, Constants.UMENG_APPKEY,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
//        Config.DEBUG = true;
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo(Constants.SinaAppId, Constants.SinaSecret,"http://sns.whalecloud.com/sina2/callback");
        //微信
        PlatformConfig.setWeixin(Constants.WeixinAppId, Constants.WeixinSecret);
        //QQ
        PlatformConfig.setQQZone(Constants.QQAppId, Constants.QQSecret);
        UMShareAPI.get(this);//初始化sd*/

        MultiDex.install(this);
        //蒲公英版本更新
//        PgyCrashManager.register(); //推荐使用
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        android.support.multidex.MultiDex.install(this);
    }

    public static Context getContext(){
        return mApplication.getApplicationContext();
    }

    public static LPApplicationLike getInstance() {
        return mApplication;
    }

   /* public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    public UserCardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(UserCardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }
    *//**
     * 返回当前登录用户的userName
     * @return
     *//*
    public static String getUserName() {
        String userName = "";
        if (getInstance().getUserInfo() != null) {
            userName = getInstance().getUserInfo().getUser_name();
        }
        if(userName == null){
            userName = "";
        }
        return userName;
    }

    public static String getUserId(){
        String userId = "";
        if (getInstance().getUserInfo() != null) {
            userId = getInstance().getUserInfo().getId();
        }
        return userId;
    }


    public static String getUserToken(){
        String userToken = "";
        if (getInstance().getUserInfo() != null) {
            userToken = getInstance().getUserInfo().getUser_token();
        }
        return userToken;
    }

    public static String getNickName() {
        String nickNameStr = "";
        UserInfo userInfo = getInstance().getUserInfo();
        if (userInfo != null) {
            nickNameStr =userInfo.getUser_nickname();
            if (TextUtils.isEmpty(nickNameStr)){
                nickNameStr = userInfo.getUser_name();
            }
        }
        return nickNameStr;
    }

    public LiveRoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(LiveRoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }
*/
    private void configuration(){

        int serverId = SharePreferenceUtil.getChoosedServer(getContext());
        if (serverId != -1){
            URLManager.SelectedServerType = (serverId == 0 ? 1 : 0);
        }

        Configuration config;
        if(URLManager.SelectedServerType==URLManager.ServerType_Production){
            config= Configuration.getConfigurationByIndex(0);
            VolleyConfig.getVolleyConfig().setReleaseEnvironment(true);
        }else {
            config= Configuration.getConfigurationById(serverId);
            VolleyConfig.getVolleyConfig().setReleaseEnvironment(false);
        }
        DebugUtils.init(config);
        URLManager.ServerURL_QA = config.serverHostUrl;
        URLManager.ServerURL_Images_QA = config.serverImageUrl;
        URLManager.ServerURL_Resource_QA = config.serverResourceUrl;
        URLManager.ServerURL_Offline_Msg_QA = config.serverOfflineMsgUrl;
        URLManager.ServerURL_IM_FRIEND_REQUEST_QA = config.serverIMFriendUrl;

        ImageUrlManager.initialize(URLManager.ServerURL_Images_QA,URLManager.ServerURL_OF_QINIU_ChatImages_QA,URLManager.ServerURL_Images_Production_Deprecated,URLManager.ServerURL_Images_Production);
    }
}
