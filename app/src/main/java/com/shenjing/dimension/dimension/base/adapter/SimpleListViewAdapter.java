package com.shenjing.dimension.dimension.base.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.holder.ListItemViewHolder;
import com.shenjing.dimension.dimension.base.holder.SimpleListViewHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tiny  on 2016/11/15 14:55.
 * Desc:
 */

public abstract class SimpleListViewAdapter<T,VH extends SimpleListViewAdapter.CommonViewHolder<T>> extends CommonAdapter<T> {


    private final HashMap<Integer,OnClickEvent<T>> mClickEvent=new HashMap<Integer, OnClickEvent<T>>();


    public SimpleListViewAdapter(Context mContext, List<T> mList, AdapterView mAdapterView) {
        super(mContext, mList, mAdapterView);
    }

    @Override
    protected void bindViewHolder(ListItemViewHolder<T> holder, View convertView, int mViewType) {
        super.bindViewHolder(holder, convertView, mViewType);
        VH vh= (VH) holder;
        HashMap<Integer,OnClickEvent<T>> events=mClickEvent;
        if(events.isEmpty()){
            return;
        }

        for (Map.Entry<Integer, OnClickEvent<T>> entry : events.entrySet()) {
            int id = entry.getKey();
            View view = vh.getView(id);
            view.setOnClickListener(mViewClickListener);
        }
    }

    private View.OnClickListener mViewClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int position= (Integer) v.getTag(R.id.rv_item_position);
            T item= (T) v.getTag(R.id.rv_item_tag);
            int id=v.getId();
            OnClickEvent<T> event= mClickEvent.get(id);
            if(event!=null){
                event.onViewClick(v,item,position);
            }
        }
    };

    @Override
    protected void bindViewData(ListItemViewHolder<T> holder, View convertView, int position, T mItem, int mViewType) {
        super.bindViewData(holder, convertView, position, mItem, mViewType);
        VH vh= (VH) holder;
        HashMap<Integer,OnClickEvent<T>> events=mClickEvent;
        if(events.isEmpty()){
            return;
        }
        Iterator<Map.Entry<Integer,OnClickEvent<T>>> iterator= events.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer,OnClickEvent<T>> entry=iterator.next();
            int id=entry.getKey();
            View view=vh.getView(id);
            view.setTag(R.id.rv_item_position,position);
            view.setTag(R.id.rv_item_tag,mItem);
        }
    }

    public void addClickEvent(int viewId,OnClickEvent<T> event){
        mClickEvent.put(viewId,event);
    }



    public abstract void convert(CommonViewHolder<T> viewHolder, int position, T mItem);

    public static class CommonViewHolder<T> extends SimpleListViewHolder<T> {

        SimpleListViewAdapter<T,? extends CommonViewHolder<T>> mAdapter;

        public CommonViewHolder(Context mContext, int layoutId, SimpleListViewAdapter<T, ? extends CommonViewHolder<T>> mAdapter) {
            super(mContext, layoutId);
            this.mAdapter = mAdapter;
        }

        @Override
        public void convert(SimpleListViewHolder<T> viewHolder, int position,T mItem) {
            CommonViewHolder<T> vh= (CommonViewHolder<T>) viewHolder;
            mAdapter.convert(vh,position,mItem);
        }


    }

    public interface ViewHolderFactory<T,VH extends SimpleListViewAdapter.CommonViewHolder<T>>{
        public VH createViewHolder(Context context, int viewType, SimpleListViewAdapter<T, VH> adapter);

    }
    public interface OnClickEvent<T>{
        void onViewClick(View view, T item, int position);
    }
}
