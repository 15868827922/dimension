package com.shenjing.dimension.dimension.base.request;

/**
 * Created by yons on 2018/3/29.
 * Desc:
 */

import com.shenjing.dimension.dimension.base.cathe.SharePreferenceUtil;
import com.shenjing.dimension.dimension.main.LPApplicationLike;

import java.lang.reflect.Field;


public class URLManager {

    public static final String HTTP_STR = "http://";
    public static final String HTTPS_STR = "http://";

    /**
     * 切换协议
     */
    public static void switchProtocol(){
        try {
            String oldProtocol = PROTOCOL_DEFAULT;
            PROTOCOL_DEFAULT = (oldProtocol.equals(HTTP_STR))?(HTTPS_STR):(HTTP_STR);
            Field[] fields = URLManager.class.getDeclaredFields();
            for (int i=0;i<fields.length;i++){
                try {
                    boolean oldAccessible = fields[i].isAccessible();
                    fields[i].setAccessible(true);
                    Object val = fields[i].get(null);
                    if (val instanceof String) {
                        if (val.equals(HTTP_STR) || val.equals(HTTPS_STR)) {
                            continue;
                        }
                        String oldVal = (String) val;
                        String newVal = oldVal.replaceFirst(oldProtocol, PROTOCOL_DEFAULT);
                        fields[i].set(null, newVal);
                    }
                    fields[i].setAccessible(oldAccessible);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回可切换为的协议头
     * @return
     */
    public static String canSwitchTo(){
        if (URLManager.PROTOCOL_DEFAULT.equals(URLManager.HTTP_STR)){
            return URLManager.HTTPS_STR;
        } else if (URLManager.PROTOCOL_DEFAULT.equals(URLManager.HTTPS_STR)){
            return URLManager.HTTP_STR;
        } else {
            return URLManager.PROTOCOL_DEFAULT;
        }
    }

    /**
     * 得到默认请求协议，默认开发包中是http协议，release包中是https协议
     * @return
     */
    private static String getDefaultProtocol(){
        if(BuildConfig.DEBUG){
            boolean isHttp = SharePreferenceUtil.getHostIsHttp(LPApplicationLike.getContext());
            return  isHttp ? HTTP_STR : HTTPS_STR;
        }else{
            return HTTPS_STR;
        }
    }

    public static String PROTOCOL_DEFAULT = getDefaultProtocol();
    public static int SelectedServerType = 1;                                           // 当前选择的服务器类型
    public static final int ServerType_QA = 0;                                                // 测试服务器的类型id
    public static final int ServerType_Production = 1;                                         // 正式服务器的类型id





    /****************************************************
     *
     *
     * 以下《可变基础域名》定义区，约定格式为：
     *
     *                 public static final String  SL_.* = ".*"; // comments
     *
     *
     * **************************************************/
    public static String SL_WWW_DOMAIN = "zhaijidi.zhongmawan.com";
    public static String SL_IMAGE_DOMAIN = "image.o2osl.com";
    public static String SL_IM_DOMAIN = "im.o2osl.com";










    /****************************************************
     *
     *
     * 以下《环境基础地址》定义区，约定格式为：
     *
     *                 public static final String  ServerURL_.* = ".*"; // comments
     *
     *
     * **************************************************/
    public static String ServerURL_QA = ""; // 测试服务器地址(测试)
    public static String ServerURL_Production = PROTOCOL_DEFAULT+SL_WWW_DOMAIN;   // 正式服务器地址+"/assistant"

    public static String ServerURL_Images_QA = "";          // 图片服务器地址(测试)
    public static String ServerURL_OF_QINIU_ChatImages_QA = PROTOCOL_DEFAULT+"7xqoq7.com2.z0.glb.qiniucdn.com";  // 图片服务器地址(测试,七牛)

    public static String ServerURL_Images_Production_Deprecated = PROTOCOL_DEFAULT+SL_WWW_DOMAIN+":26680/fileservice";  // 图片服务器地址(正式)
    public static String ServerURL_Images_Production = PROTOCOL_DEFAULT+SL_IMAGE_DOMAIN;  // 图片服务器地址(正式)

    public static String ServerURL_Resource_QA = "";          // 资源服务器地址(测试)
    public static String ServerURL_Resource_Production = PROTOCOL_DEFAULT+SL_WWW_DOMAIN+"";  // 资源服务器地址(正式)

    public static String ServerURL_Offline_Msg_QA = "";          // 拉取离线服务器地址(开发)
    public static String ServerURL_Offline_Msg_Production = PROTOCOL_DEFAULT+SL_IM_DOMAIN+":8777";  // 拉取离线服务器地址(正式)

    public static String ServerURL_IM_FRIEND_REQUEST_QA = "";//好友改造测试
    public static String ServerURL_IM_FRIEND_REQUEST_Production = PROTOCOL_DEFAULT+SL_IM_DOMAIN+":7777";//好友改造（正式）



    public static String ServerURL_IMAGE_ALIOSS = "https://zhaijidi.oss-cn-shenzhen.aliyuncs.com/";//好友改造（正式）




    private static String getSelectedSeverURL() {
        String selectedServerURL;
        switch (SelectedServerType) {
            case ServerType_QA:
                selectedServerURL = ServerURL_QA;
                break;
            case ServerType_Production:
                selectedServerURL = ServerURL_Production;
                break;
            default:
                selectedServerURL = ServerURL_Production;
                break;
        }
        return selectedServerURL;
    }


    //新接口命名规范 去除assistant   ，比如： URL: {baseurl}/ds/ass/user/userTagsAndBusinessTags.json;jsessionid={sessionId}
  /*  public static String getSimpleRequestUrl(IInterface method) {
        if(UrlPatchManager.hasPatch(method)){
            LPLog.print(URLManager.class,"hook UrlPatchManager :"+method.url);
            return UrlPatchManager.hook(method);
        }
        if (null == method) {
            return null;
        }
        String requestUrl = getSelectedSeverURL();
        if (requestUrl.endsWith("/assistant")) {
            requestUrl = requestUrl.substring(0,requestUrl.length() - "/assistant".length());
        }
        return requestUrl + method;
    }*/

    public static String getRequestURL(String method) {
        if (null == method) {
            return null;
        }
        return getSelectedSeverURL()+method;
    }



    public static String METHOD_FEED_BACK = "/getFeedBackCate";    //意见反馈
    public static String METHOD_COMMIT_FEED_BACK = "/submitFeedBack";    //提交意见反馈
    public static String METHOD_GET_GRAB_BANNER = "/banner";    //获取抓周边banner
    public static String Method_Login = "/login";    //登录
    public static String Method_Register = "/reg";    //注册
    public static String Method_Log_Chat = "/WxLogin";    //注册
    public static String Method_Log_QQ = "/TxLogin";    //注册
    public static String Method_Add_Game_Complain = "/addGameComplain";    //添加申诉

    public static String Method_Edit_User_Info = "/modifyUserInfo"; //修改个人资料

    /*  ----------------  Live  ---------------------*/
    public static String Method_Live_Login = "/index.php?svc=account&cmd=login"; //直播登录
    public static String Method_Live_Create_Room = "/sxb?svc=live&cmd=create"; //创建房间
    public static String Method_Live_Send_Heart = "/index.php?svc=live&cmd=heartbeat"; //心跳
    public static String Method_Live_Report_Room = "/sxb?svc=live&cmd=reportroom"; //上报房间信息
    public static String Method_Live_Cache_Liver = "/cacheLiveInfo"; //缓存主播信息——开始直播之后调用
    public static String Method_Live_Exit_Room = "/sxb?svc=live&cmd=exitroom"; //退出房间
    public static String Method_Live_ForeshowList = "/forenotice"; //直播预约列表


    /*  ----------------  setting  ---------------------*/

    public static String Method_Get_Verification_Code = "/get_sms_code?"; //获取验证码
    public static String Method_Check_Verification_Code = "/check_sms_code?"; //验证验证码
    public static String Method_Bind_Phone = "/bindPhone"; //修改绑定手机号
    public static String Method_Modify_Pass = "/modifyPass"; //修改密码
    public static String Method_Check_Phone = "/getThePhoneNumHasBind"; //验证手机号是否存在


    public static String Method_Get_Push_messasge_status = "/getPushStatus"; //获取是否接受消息推送
    public static String Method_Set_Push_messasge = "/changePushStatus"; //修改是否接受消息推送


    /*  ----------------  User  ---------------------*/
    public static String Method_User_CardInfo = "/myUCenter";    //用户的个人信息
    public static String Method_Has_Sign = "/has_sign";    //是否签到
    public static String Method_Go_Sign = "/user_sign";    //签到
    public static String Method_Follow_list = "/followList";    //关注列表
    public static String Method_Follow = "/follow";    //关注
    public static String Method_Cancel_Follow = "/cancelFollow";    //取消关注
    public static String Method_Message_List = "/get_user_message ";    //消息列表
    public static String Method_Active_List = "/act_list";    //活动列表
    public static String Method_Read_Message = "/read_message";    //活动列表
    public static String Method_GET_GIFT_Message = "/get_gift";    //领取礼物

    /*  ----------------  抓周边  ---------------------*/
    public static String Method_Address_list = "/addressList";    //地址列表
    public static String Method_Add_Address_list = "/addAddress";    //添加地址
    public static String Method_Change_Address_list = "/editAddress";    //修改地址
    public static String Method_Delete_Address_list = "/delAddress";    //修改地址


    /*  ----------------  充值  ---------------------*/
    public static String Method_Recharge_list = "/rechargeList";    //充值列表
    public static String Method_Trade_list = "/getBalanceRecord";    //充值列表
    public static String Method_Go_Recharge = "/createOrder";    //充值


    /*  ----------------  游戏  ---------------------*/
    public static String Method_Record_list = "/gameRecordList";    //游戏记录
    public static String Method_Winning_Record_list = "/getWinningRecord";    //抓中游戏记录
    public static String Method_My_Basket_list = "/userDolls";    //我的篮子
    public static String Method_Convert_Doll = "/sell_doll";    //兑换积分
    public static String Method_Complain_list = "/getComplainCate";    //申述列表
    public static String Method_Around_grad_list = "/getDetail";    //抓周边
    public static String Method_Doll_service = "/doll_server";    //获取开始游戏是否可以
    public static String Method_Line_Up = "/LineUp";    //请求进入队列
    public static String Method_Mail_List = "/ApplyForMailingList";    //邮寄的列表
    public static String Method_Mail_Commit = "/ApplyForMailing";    //邮寄
    public static String Method_Mail_Commit_Pay = "/editMailingStat";    //请求支付

    /*  ---------------------   积分商城   -------------------*/
    public static String Method_Score_shop_list= "/shop_list";    //积分商城列表
    public static String Method_Score_shop_index= "/shop_index";    //商城列表
    public static String Method_Convert_Score= "/get_goods";    //兑换积分
    public static String Method_Convert_List= "/shopping_log";    //兑换积分列表



}

