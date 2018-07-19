package com.shenjing.dimension.dimension.base.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class LPNetworkRoundedImageView extends BaseNetworkRoundedImageView {

    public LPNetworkRoundedImageView(Context context) {
        super(context);
    }
    
    public LPNetworkRoundedImageView(Context context, AttributeSet attrs) {
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
    public void setImageUrl(String url, int width, int height){
        int vSize = 0;
        ViewGroup.LayoutParams lp = getLayoutParams();
        if(lp != null){
            if(lp.width > 0 && lp.height > 0 && lp.width == lp.height){
                vSize = lp.height;
            }
        }
        setImageUrl(url, NetworkThumbUtils.getDesireThumbUrlAndCache(ImageUrlManager.getFixedShowImageUrl(url), vSize), width, height);
    }
}
