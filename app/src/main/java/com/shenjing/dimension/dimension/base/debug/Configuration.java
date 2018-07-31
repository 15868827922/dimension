package com.shenjing.dimension.dimension.base.debug;

import android.text.TextUtils;

import com.shenjing.dimension.dimension.base.cathe.SharePreferenceUtil;
import com.shenjing.dimension.dimension.base.request.URLManager;
import com.shenjing.dimension.dimension.main.LPApplicationLike;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tiny  on 2016/12/19 10:55.
 * Desc:
 */

public class  Configuration implements Serializable {
    static {
        loadConfigurations();
    }
    static ArrayList<Configuration> mConfigurations;

    static void loadConfigurations(){
        if (mConfigurations == null) {
            mConfigurations = new ArrayList<Configuration>();
        }
        if (mConfigurations!=null){
            mConfigurations.clear();
        }
        mConfigurations.add(new Configuration(0,
                "线上环境",
                URLManager.ServerURL_Production,
                URLManager.ServerURL_Resource_Production,
                URLManager.ServerURL_Images_Production,
                URLManager.ServerURL_Offline_Msg_Production,
                URLManager.ServerURL_IM_FRIEND_REQUEST_Production));
        String strServer = SharePreferenceUtil.getHandServer(LPApplicationLike.getContext());
        if (!TextUtils.isEmpty(strServer)){
            try {
                JSONObject jsonObject = new JSONObject(strServer);
                String serverHostUrl = jsonObject.optString("serverHostUrl");
                String serverResourceUrl = jsonObject.optString("serverResourceUrl");
                String serverImageUrl = jsonObject.optString("serverImageUrl");
                String serverOfflineMsgUrl = jsonObject.optString("serverOfflineMsgUrl");
                String serverIMFriendUrl = jsonObject.optString("serverIMFriendUrl");
                mConfigurations.add(new Configuration(1,"手动配置",serverHostUrl, serverResourceUrl, serverImageUrl, serverOfflineMsgUrl, serverIMFriendUrl));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            mConfigurations.add(new Configuration(1, "手动配置", URLManager.PROTOCOL_DEFAULT+"192.168.0.120", URLManager.PROTOCOL_DEFAULT+"192.168.0.120/", URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com", URLManager.PROTOCOL_DEFAULT+"172.168.18.2:9898", URLManager.PROTOCOL_DEFAULT+"172.168.18.2:7878"));
        }
        mConfigurations.add(new Configuration(2,"138stable环境",URLManager.PROTOCOL_DEFAULT+"www.g-jia.cn/assistant",URLManager.PROTOCOL_DEFAULT+"www.g-jia.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"114.215.209.138:8989",URLManager.PROTOCOL_DEFAULT+"114.215.209.138:7878"));
        mConfigurations.add(new Configuration(3,"o2s测试环境",URLManager.PROTOCOL_DEFAULT+"www.o2s.com.cn/assistant",URLManager.PROTOCOL_DEFAULT+"www.o2s.com.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"115.28.214.229:8989",URLManager.PROTOCOL_DEFAULT+"115.28.214.229:7878"));
        mConfigurations.add(new Configuration(4,"cs138测试环境",URLManager.PROTOCOL_DEFAULT+"cs138.g-jia.net/assistant",URLManager.PROTOCOL_DEFAULT+"cs138.g-jia.net",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"114.215.209.138:8989",URLManager.PROTOCOL_DEFAULT+"114.215.209.138:7878"));
        mConfigurations.add(new Configuration(5,"cs170测试环境",URLManager.PROTOCOL_DEFAULT+"cs170.g-jia.cn/assistant",URLManager.PROTOCOL_DEFAULT+"cs170.g-jia.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"114.215.191.170:8989",URLManager.PROTOCOL_DEFAULT+"114.215.191.170:7878"));
        mConfigurations.add(new Configuration(6,"cslp测试环境",URLManager.PROTOCOL_DEFAULT+"cslp.g-jia.net/assistant",URLManager.PROTOCOL_DEFAULT+"cslp.g-jia.net",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"172.168.17.5:8989",URLManager.PROTOCOL_DEFAULT+"172.168.17.5:7878"));
        mConfigurations.add(new Configuration(7,"i-ole测试环境",URLManager.PROTOCOL_DEFAULT+"www.i-ole.com/assistant",URLManager.PROTOCOL_DEFAULT+"www.i-ole.com",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"114.55.66.168:8989",URLManager.PROTOCOL_DEFAULT+"114.55.66.168:7878"));
        mConfigurations.add(new Configuration(8,"107测试环境",URLManager.PROTOCOL_DEFAULT+"g-jia.cn/assistant",URLManager.PROTOCOL_DEFAULT+"g-jia.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"114.55.60.107:8989",URLManager.PROTOCOL_DEFAULT+"114.55.60.107:7878"));
        mConfigurations.add(new Configuration(9,"231测试环境",URLManager.PROTOCOL_DEFAULT+"cs231.g-jia.cn/assistant",URLManager.PROTOCOL_DEFAULT+"cs231.g-jia.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"101.37.79.231:8989",URLManager.PROTOCOL_DEFAULT+"101.37.79.231:7878"));
        mConfigurations.add(new Configuration(10,"csnb32测试环境",URLManager.PROTOCOL_DEFAULT+"csnb32.g-jia.cn/assistant",URLManager.PROTOCOL_DEFAULT+"csnb32.g-jia.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"192.168.2.31:8989",URLManager.PROTOCOL_DEFAULT+"192.168.2.31:7878"));
        mConfigurations.add(new Configuration(11,"cs50测试环境",URLManager.PROTOCOL_DEFAULT+"cs50.g-jia.cn/assistant",URLManager.PROTOCOL_DEFAULT+"cs50.g-jia.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"120.26.66.50:8989",URLManager.PROTOCOL_DEFAULT+"120.26.66.50:7878"));
        mConfigurations.add(new Configuration(12,"cs33测试环境",URLManager.PROTOCOL_DEFAULT+"cs33.g-jia.cn/assistant",URLManager.PROTOCOL_DEFAULT+"cs33.g-jia.cn",URLManager.PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com",URLManager.PROTOCOL_DEFAULT+"120.26.63.33:8989",URLManager.PROTOCOL_DEFAULT+"120.26.63.33:7878"));
    }

    /**
     * 重新配置。切换http或https协议
     * @param oldProtocol
     * @param newProtocol
     */
    public static void reconfig(String oldProtocol, String newProtocol){
        for (int i=0;i<mConfigurations.size();i++){
            Configuration tmp = mConfigurations.get(i);
            tmp.serverHostUrl = tmp.serverHostUrl.replace(oldProtocol,newProtocol);
            tmp.serverImageUrl = tmp.serverImageUrl.replace(oldProtocol,newProtocol);
            //tmp.serverIMFriendUrl = tmp.serverIMFriendUrl.replace(oldProtocol,newProtocol);  // 好友这一块目前不支持http请求
            tmp.serverOfflineMsgUrl = tmp.serverOfflineMsgUrl.replace(oldProtocol,newProtocol);
            tmp.serverResourceUrl = tmp.serverResourceUrl.replace(oldProtocol,newProtocol);
        }
    }

    public static String[] getServerNames(){
        String[] Servers = new String[mConfigurations.size()];
        for (int i = 0; i < mConfigurations.size(); i ++){
            Servers[i] = mConfigurations.get(i).name;
        }
        return Servers;
    }


    public int id;
    public String name;
    public String serverHostUrl;
    public String serverResourceUrl;
    public String serverImageUrl;
    public String serverOfflineMsgUrl;
    public String serverIMFriendUrl;

    public Configuration(int id,String name ) {
        this.id=id;
        this.name=name;
    }

    public Configuration(int id, String name, String serverHostUrl, String serverResourceUrl, String serverImageUrl, String serverOfflineMsgUrl, String serverIMFriendUrl) {
        this.id=id;
        this.name=name;
        this.serverHostUrl = serverHostUrl;
        this.serverResourceUrl = serverResourceUrl;
        this.serverImageUrl = serverImageUrl;
        this.serverOfflineMsgUrl = serverOfflineMsgUrl;
        this.serverIMFriendUrl = serverIMFriendUrl;
    }


    public static Configuration getConfigurationById(int id){
        ArrayList<Configuration> configurations=mConfigurations;
        for(Configuration configuration:configurations){
            if(configuration.id==id){
                return configuration;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if(id==-1){
            return serverHostUrl;
        }
        return name;
    }

    public static Configuration getConfigurationByIndex(int position){
        return mConfigurations.get(position);
    }
}
