package com.shenjing.dimension.dimension.base.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shenjing.dimension.R;


public class BaseNetworkRoundedImageView extends RoundedImageView{
    private String imageUrl;
    private boolean dontLoadSameUrl;
    
    private int currDisplayedDafaultResId;
    private int defaultResId;

    public BaseNetworkRoundedImageView(Context context) {
        super(context);
    }
    
    public BaseNetworkRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setDefaultDrawableRes(int res){
        defaultResId = res;
    }

    public void setDontLoadSameUrl(boolean dontLoadSameUrl){
        this.dontLoadSameUrl = dontLoadSameUrl;
    }

    public void setImageUrl(String url, String thumbUrl){
        setImageUrl(url, null, thumbUrl);
    }

    public void setImageUrl(String url, final OnGetImageSizeListener listener, final String thumbUrl){
//        if(url != null && url.equals(imageUrl)){
//            return;
//        }
        if(dontLoadSameUrl){
            if(url != null && url.equals(imageUrl) && (currDisplayedDafaultResId == defaultResId)){
                return;
            }
        }
        
        imageUrl = (url == null)? "" : url;

        currDisplayedDafaultResId = defaultResId;

        if(Build.VERSION.SDK_INT >= 17 && getContext() instanceof Activity){
            if(((Activity) getContext()).isDestroyed()){
                return;
            }
        }

        try {
            if(listener == null){

                RequestListener mRequestListener = new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                        return false;
                    }



                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.e(TAG,  "model:"+model+" isFirstResource: "+isFirstResource);
                        return false;
                    }


                };
                Glide.with(getContext()).load(thumbUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defaultResId).animate(R.anim.fade_in).listener(mRequestListener).into(this);


            }else {
                Glide.with(getContext()).load(thumbUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defaultResId).listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        listener.onGetImageSize(imageUrl, resource.getWidth(), resource.getHeight());
                        return false;
                    }
                }).animate(R.anim.fade_in).into(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImageUrl(String url, final String thumbUrl, int width, int height){
        if(dontLoadSameUrl){
            if(url != null && url.equals(imageUrl) && (currDisplayedDafaultResId == defaultResId)){
                return;
            }
        }
        imageUrl = (url == null)? "" : url;
        currDisplayedDafaultResId = defaultResId;
        if(Build.VERSION.SDK_INT >= 17 && getContext() instanceof Activity){
            if(((Activity) getContext()).isDestroyed()){
                return;
            }
        }
        try {
            Glide.with(getContext()).load(thumbUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).override(width, height).placeholder(defaultResId).animate(R.anim.fade_in).into(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public interface OnGetImageSizeListener{
        public void onGetImageSize(String url, int width, int height);
    }

}
