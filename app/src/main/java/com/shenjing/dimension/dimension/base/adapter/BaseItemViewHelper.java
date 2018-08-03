package com.shenjing.dimension.dimension.base.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by tiny on 2015/4/11.
 */
public abstract class BaseItemViewHelper<T>   {
    protected Context mContext;
    protected AdapterView mAdapterView;
    protected View mItemView;

    public BaseItemViewHelper(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }


    public AdapterView getAdapterView() {
        return mAdapterView;
    }

    public View getItemView() {
        return mItemView;
    }

    public void setItemView(View mItemView) {
    this.mItemView=mItemView;
    }

    public void setAdapterView(AdapterView mAdapterView) {
        this.mAdapterView = mAdapterView;
    }
    
    public TextView findTextView(int id) {
        return (TextView) (getItemView().findViewById(id));
    }
    public View findViewById(int id){
        return getItemView().findViewById(id);
    }

    public ImageView findImageView(int id) {
        return (ImageView) (getItemView().findViewById(id));
    }
    public void setDefaultImage(ImageView mImageView) {
        Drawable loading=getLoadingDrawable();
        if(loading!=null){
            mImageView.setImageDrawable(loading);
        }else
        mImageView.setImageResource(getLoadingImageRes(mImageView.getId()));
    }

    /**
     * 如果要对不同的ImageView提供不同的loading Image ，重写该方法
     *
     * @param id ImageView的id
     * @return
     */
    protected int getLoadingImageRes(int id) {
        return 0;
    }

    public Drawable getLoadingDrawable(){

        return null;
    }
    public abstract int getLayoutId();

    public Resources getResources(){
        return mContext.getResources();
    }
    public String getString(int id){
        return getResources().getString(id);
    }


}
