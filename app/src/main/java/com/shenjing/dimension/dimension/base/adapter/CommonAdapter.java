package com.shenjing.dimension.dimension.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;


import com.shenjing.dimension.dimension.base.holder.ListItemViewHolder;

import java.util.List;

/**
 * 保证声明的泛型T和所用的ListItemViewHolder声明的泛型一致
 * Created by Administrator on 2015/3/26.
 */
public abstract  class CommonAdapter<T> extends BaseAdapter {


    final Context mContext;

     List<T> mList;
     AdapterView mAdapterView;

    int mViewTypeCount=-1;

    public CommonAdapter(Context mContext, List<T> mList) {
       this(mContext,mList,null);
    }

    public void setViewTypeCount(int count){
        this.mViewTypeCount=count;
    }
    /**
     *
     * @param mContext
     * @param mList T和CommonAdapter 声明的泛型T一致
     * @param mAdapterView ListView 或者GridView
     */
    public CommonAdapter(Context mContext, List<T> mList, AdapterView mAdapterView) {
        this.mContext = mContext;
        this.mList = mList;
        this.mAdapterView = mAdapterView;

    }

    @Override
    public int getViewTypeCount() {
        if(mViewTypeCount>1){
            return mViewTypeCount;
        }
        return super.getViewTypeCount();
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public List<T> replaceList(List<T> list){
        List<T> old=this.mList;
        mList=list;
        notifyDataSetChanged();
        return old;
    }

    @Override
    public int getItemViewType(int position) {
        T item=getItem(position);
        if(item instanceof IViewTypeResolver){
            return ((IViewTypeResolver) item).getViewType();
        }
        return super.getItemViewType(position);
    }

    public List<T> getList(){
        return mList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemViewHolder<T> mHelper;
        int mViewType=getItemViewType(position);
        if (convertView == null) {
            mHelper = getViewItemInstanceByViewType(mContext,mViewType);
            mHelper.setAdapterView(mAdapterView);
            convertView = View.inflate(mContext, mHelper.getLayoutId(), null);
            mHelper.setItemView(convertView);
            bindViewHolder(mHelper, convertView, mViewType);
            convertView.setTag(mHelper);
        } else {
            mHelper = (ListItemViewHolder<T>) convertView.getTag();
        }
        T mItem = (T) getItem(position);
        bindViewData(mHelper, convertView, position, mItem, mViewType);
        return convertView;
    }
    public void add(T obj){
        mList.add(obj);
    }
    public void add(T obj,int index){
        mList.add(index,obj);
    }
    protected void bindViewHolder(ListItemViewHolder<T> mHelper, View convertView, int mViewType){
        mHelper.bindView(convertView, mViewType);
    }
    protected void bindViewData(ListItemViewHolder<T> mHelper, View convertView, int position, T mItem, int mViewType){
        mHelper.bindData(position, mItem, mViewType);
    }

    public abstract  ListItemViewHolder<T> getViewItemInstanceByViewType(Context context, int mViewType);


    /**
     * 这个position是数据mList里的position
     * 如果有header要做偏移校正
     *
     * @param position
     */
    public void updateItemView(int position) {
        AdapterView mAdapterView = this.mAdapterView;
        int first = mAdapterView.getFirstVisiblePosition();
        T model = getItem(position);
        int viewType = mAdapterView.getAdapter().getItemViewType(position);
        ListItemViewHolder<T> itemView = (ListItemViewHolder<T>) mAdapterView.getChildAt(position - first).getTag();
        itemView.bindData(position, model, viewType);
    }
    public void setAdapterView(AdapterView adapterView){
        this.mAdapterView=adapterView;
    }
    public void updateData(List<T> data){
        this.mList = data;
        notifyDataSetChanged();
    }



}
