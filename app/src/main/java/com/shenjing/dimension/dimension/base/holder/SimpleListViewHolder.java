package com.shenjing.dimension.dimension.base.holder;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by tiny  on 2016/11/1 14:59.
 * Desc:
 */

public abstract class SimpleListViewHolder<T> extends ListItemViewHolder<T> {

    private int layoutId;
    SparseArray<View> mViewCached=new SparseArray<View>();

    public SimpleListViewHolder(Context mContext, int layoutId) {
        super(mContext);
        this.layoutId=layoutId;
    }
    @Override
    public void bindView(View convertView, int mViewType) {

    }

    @Override
    public void bindData(int position, T mItem, int mViewType) {
            convert(this,position,mItem);
    }

    @Override
    public int getLayoutId() {
        return layoutId;
    }

   

    public abstract void convert( SimpleListViewHolder<T> viewHolder, int position,T mItem);

    public SimpleListViewHolder<T> setText(int id,String text){
        TextView textView=getView(id);
        textView.setText(text);
        return this;
    }
    public SimpleListViewHolder<T> setVisible(int id,int visible){
        View view=getView(id);
        view.setVisibility(visible);
        return this;
    }

    public SimpleListViewHolder<T> setSelect(int id,boolean select){
        View view=getView(id);
        view.setSelected(select);
        return this;
    }

    public SimpleListViewHolder<T> setViewBackground(int id,int drawableId){
        View view=getView(id);
        view.setBackgroundResource(drawableId);
        return this;
    }
    public SimpleListViewHolder<T> setImageDrawable(int id,int drawableId){
        ImageView imageView=getView(id);
        imageView.setImageResource(drawableId);
        return this;
    }

    public <T extends View> T getView(int viewId) {
        T view = (T) mViewCached.get(viewId);
        if (view == null) {
            view= (T) mItemView.findViewById(viewId);
            mViewCached.put(viewId,view);
        }
        return view;
    }
    
}
