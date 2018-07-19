package com.shenjing.dimension.dimension.base.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class LPNetworkImageView extends BaseNetworkImageView {

    public LPNetworkImageView(Context context) {
        super(context);
    }
    
    public LPNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setImageUrl(String url){
        int vSize = 0;
        ViewGroup.LayoutParams lp = getLayoutParams();
        if(lp != null){
            if(lp.width > 0 && lp.height > 0 && lp.width == lp.height){
                vSize = lp.height;
            }
        }
        setImageUrl(url, null, NetworkThumbUtils.getDesireThumbUrlAndCache(ImageUrlManager.getFixedShowImageUrl(url), vSize));
    }
    public void setImageUrl(String url, int size){
        setImageUrl(url, null, NetworkThumbUtils.getDesireThumbUrlAndCache(ImageUrlManager.getFixedShowImageUrl(url), size));
    }
    public void setImageUrl(String url, int width, int height){
        if(width>0&&height>0){
            setImageUrl(url,  NetworkThumbUtils.getDesireThumbUrlAndCache(ImageUrlManager.getFixedShowImageUrl(url), width),width,height);
        }else {
            setImageUrl(url);
        }
    }

}
