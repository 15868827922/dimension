package com.shenjing.dimension.dimension.me.model;

/**
 * Created by yons on 2018/5/21.
 * Desc:
 */

public class MessageInfo {


    /**
     * id : 4
     * uid : 182
     * title : a 啊啊啊
     * message : 多省点钱群多
     * is_read : 1
     */

    private String id;
    private String uid;
    private String title;
    private String message;
    private String is_read;  //0未读，1已读
    private String gift;  //如果gift值不为空,则显示领取礼物按钮
    private String gift_is_achieve;  //如果gift_is_achieve=0，显示领取礼物，为1显示已领取

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getGift_is_achieve() {
        return gift_is_achieve;
    }

    public void setGift_is_achieve(String gift_is_achieve) {
        this.gift_is_achieve = gift_is_achieve;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }
}
