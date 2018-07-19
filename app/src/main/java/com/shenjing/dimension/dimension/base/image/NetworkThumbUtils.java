package com.shenjing.dimension.dimension.base.image;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * 
 * @author chenhualiang
 * 网络图片的缩略图路径转换
 *
 */
public class NetworkThumbUtils {

    private static final int[] ACCEPT_THUMB_SIZES = {60, 100, 160, 200, 300, 400, 500, 640};

    /**
     * 临时的图片url缓存   只能存储100个值  ，目前用来保存  压缩图片的url路径
     *
     * key图片原图的url
     * value 压缩过的url 路径
     *
     * 做个临时缓存的写法是为了解耦，只保存100个键值对 并且存储采用lru原则  可以防止内存内存溢出 和 内存缓存被存分利用
     */
    private static LruCache<String ,String> lruCache4ThumbUrl = null;

    public synchronized static LruCache<String, String> getLruCache() {
        if (lruCache4ThumbUrl == null) {
            lruCache4ThumbUrl = new LruCache<String ,String>(100) {
                @Override
                protected int sizeOf(String key, String value) {
                    return 1;
                }
            };
        }
        return lruCache4ThumbUrl;
    }

    public static String getDesireThumbUrlAndCache(String originalImgUrl, int size){
        String result = getDesireThumbUrl(originalImgUrl, size);
        if (!TextUtils.isEmpty(originalImgUrl) && !originalImgUrl.equals(result)) {
            getLruCache().put(originalImgUrl, result);
        }
        return result;
    }
    
    public static String getDesireThumbUrl(String originalImgUrl, int size){
        if(TextUtils.isEmpty(originalImgUrl) || size <= 0
                || !originalImgUrl.startsWith("http")
                || originalImgUrl.contains("?")
                || originalImgUrl.contains("/resources/upload/temp/")){
            return originalImgUrl;
        }
//        if(originalImgUrl.startsWith(ImageUrlManager.ServerURL_Images_QA)){
//            originalImgUrl = originalImgUrl.replaceFirst(ImageUrlManager.ServerURL_Images_QA, ImageUrlManager.ServerURL_OF_QINIU_ChatImages_QA);
//        }
        if(originalImgUrl.startsWith(ImageUrlManager.ServerURL_Images_Production)){
            int desireSize = 0;
            for(int i = 0; i < ACCEPT_THUMB_SIZES.length; i ++){
                if(size <= ACCEPT_THUMB_SIZES[i]){
                    desireSize = ACCEPT_THUMB_SIZES[i];
                    break;
                }
            }
            if(desireSize == 0){
                return originalImgUrl;
            }else{
                return originalImgUrl + "_q" + desireSize + "x" + desireSize;
            }
        }
        return originalImgUrl;
    }
}
