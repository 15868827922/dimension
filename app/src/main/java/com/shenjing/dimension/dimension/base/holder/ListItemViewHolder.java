package com.shenjing.dimension.dimension.base.holder;

import android.content.Context;
import android.view.View;

import com.shenjing.dimension.dimension.base.adapter.BaseItemViewHelper;


/**
 * Created by Administrator on 2015/3/26.
 */
public abstract class ListItemViewHolder<T> extends BaseItemViewHelper<T> {


    public ListItemViewHolder(Context mContext) {
        super(mContext);
    }


    public  abstract  void bindView(View convertView, int mViewType);

    public  abstract  void bindData(int position, T mItem, int mViewType);}
