package com.shenjing.dimension.dimension.base.image;

import android.text.TextUtils;


/**
 * Created by asus  on 2016/10/13 16:42.
 * Desc:
 */

public class ImageUrlManager {

    public static String ServerURL_Images_QA = "";          // 图片服务器地址(测试)
    public static String ServerURL_OF_QINIU_ChatImages_QA = "";  // 图片服务器地址(测试,七牛)
    public static String ServerURL_Images_Production_Deprecated = "";  // 图片服务器地址(正式)
    public static String ServerURL_Images_Production = "";  // 图片服务器地址(正式)


    /**
     *
     * @param image
     * @param qiniuChatImageUrl
     * @param imageDeprecated
     * @param product
     */
    public static void initialize(String image, String qiniuChatImageUrl, String imageDeprecated, String product){
        ServerURL_Images_QA=image;
        ServerURL_OF_QINIU_ChatImages_QA=qiniuChatImageUrl;
        ServerURL_Images_Production_Deprecated=imageDeprecated;
        ServerURL_Images_Production=product;
    }


    /**
     * 展示图片时的映射
     * @param originUrl
     * @return
     */
    public static String getFixedShowImageUrl(String originUrl){
        if(TextUtils.isEmpty(originUrl)){
            return originUrl;
        }else{
            return originUrl.replaceFirst(ServerURL_Images_Production_Deprecated, ServerURL_Images_Production);
        }
    }

}
