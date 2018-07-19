package com.shenjing.dimension.dimension.base.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.graphics.BitmapCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shenjing.dimension.R;


@SuppressLint("AppCompatCustomView")
public class BaseNetworkImageView extends ImageView {
    private String imageUrl;
    private boolean dontLoadSameUrl;

    private int currDisplayedDafaultResId;
    private int defaultResId = R.color.default_load_color;

    OnGetImageSizeListener mImageListener;

    public BaseNetworkImageView(Context context) {
        super(context);
    }
    
    public BaseNetworkImageView(Context context, AttributeSet attrs) {
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
    
    public void setImageUrl(String url, OnGetImageSizeListener listener, final String thumbUrl){
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
        if(listener==null){
            listener=defaultListener;
        }
        this.mImageListener=listener;
        Glide.with(getContext()).load(thumbUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(defaultResId).listener(mGlideListener).animate(R.anim.fade_in).into(this);
    }

    public void setImageUrl(String url, final String thumbUrl, int width, int height){
        this.mImageListener=defaultListener;
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
        Glide.with(getContext()).load(thumbUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).override(width, height).placeholder(defaultResId).animate(R.anim.fade_in)
                .listener(mGlideListener)
                .into(this);
    }

    private RequestListener<String,Bitmap> mGlideListener=new RequestListener<String, Bitmap>() {
        @Override
        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
            return false;
        }


        @Override
        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if(mImageListener!=null){
                mImageListener.onGetImageSize(imageUrl, resource.getWidth(), resource.getHeight());
            }
            if(resource!=null){
                Log.d("BaseNetworkImageView","onResourceReady Bitmap : "+resource.getWidth()+"x"+resource.getHeight()+" bitmap size = "+ toMemorySize(resource));
            }
            return false;
        }
    };


    OnGetImageSizeListener defaultListener=new OnGetImageSizeListener(){
        @Override
        public void onGetImageSize(String url, int width, int height) {

        }
    };
    public static String toMemorySize(Bitmap bitmap){
        long size= BitmapCompat.getAllocationByteCount(bitmap);
        return size/1024+"k";
    }



    public interface OnGetImageSizeListener{
        public void onGetImageSize(String url, int width, int height);
    }

}
