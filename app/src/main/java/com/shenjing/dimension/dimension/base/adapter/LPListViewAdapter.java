package com.shenjing.dimension.dimension.base.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;


import com.shenjing.dimension.dimension.base.holder.ListItemViewHolder;
import com.shenjing.dimension.dimension.base.image.LPNetworkImageView;
import com.shenjing.dimension.dimension.base.image.LPNetworkRoundedImageView;

import java.util.List;

/**
 * Created by tiny  on 2016/11/15 15:05.
 * Desc:封装过的adapter，用于ListView
 */

public abstract class LPListViewAdapter<T> extends SimpleListViewAdapter<T,LPListViewAdapter.LPViewHolder<T>> {



    private final SparseIntArray mViewTypes=new SparseIntArray();


    public LPListViewAdapter(Context mContext, List<T> mList, AdapterView mAdapterView, int layoutId) {
        super(mContext, mList, mAdapterView);
        mViewTypes.put(0,layoutId);
    }

    @Override
    public ListItemViewHolder<T> getViewItemInstanceByViewType(Context context, int viewType) {
        return new LPViewHolder<T>(context, mViewTypes.get(viewType),this);
    }

    @Override
    public void convert(CommonViewHolder<T> viewHolder, int position, T mItem) {
        LPViewHolder<T> vh= (LPViewHolder<T>) viewHolder;
        onConvert(vh,position,mItem);
    }

    public abstract void onConvert(LPViewHolder<T> viewHolder, int position, T mItem);

    public void addViewHolderType(int type, int layoutId){
        mViewTypes.put(type,layoutId);
    }


    public static class LPViewHolder<T> extends SimpleListViewAdapter.CommonViewHolder<T>{


        public LPViewHolder(Context mContext, int layoutId, SimpleListViewAdapter<T, ? extends CommonViewHolder<T>> mAdapter) {
            super(mContext, layoutId, mAdapter);
        }

        public SimpleListViewAdapter.CommonViewHolder<T> setImageUrl(int id, String url, int defaultDrawable){
            View view=getView(id);
            if(view instanceof LPNetworkImageView){
                LPNetworkImageView imageView= (LPNetworkImageView) view;
                imageView.setDefaultDrawableRes(defaultDrawable);
                imageView.setDontLoadSameUrl(true);
                imageView.setImageUrl(url);
            }
            if(view instanceof LPNetworkRoundedImageView){
                LPNetworkRoundedImageView imageView= (LPNetworkRoundedImageView) view;
                imageView.setDefaultDrawableRes(defaultDrawable);
                imageView.setDontLoadSameUrl(true);
                imageView.setImageUrl(url);
            }
            return this;
        }

      /*  public SimpleListViewAdapter.CommonViewHolder<T> setProfileView(int id, String url, boolean isKOL, boolean isShopCertify, boolean isCertify){
            View view=getView(id);
            if(view instanceof SquareProfileView){
                SquareProfileView profileView = (SquareProfileView) view;
                profileView.setProfileImageUrl(url);
                profileView.setRealIcon(isKOL, isShopCertify, isCertify);
            }
            return this;
        }*/
    }


}
