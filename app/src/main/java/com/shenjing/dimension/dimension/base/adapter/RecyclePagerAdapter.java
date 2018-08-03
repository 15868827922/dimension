package com.shenjing.dimension.dimension.base.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


import com.zjlp.httpvolly.log.LPLog;

import java.util.Stack;

public abstract class RecyclePagerAdapter extends PagerAdapter {
    private Stack<View> mRecycledViewStack = new Stack<View>();
    
    protected abstract View getView(View convertView, int position);
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        
        LPLog.print(getClass(), "recycled_count:" + mRecycledViewStack.size() + "  convertview:" + mRecycledViewStack.isEmpty());
        
        View view = getView(mRecycledViewStack.isEmpty()? null: mRecycledViewStack.pop(), position);
        
        
        container.addView(view);
        return view;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        ViewPager pager = (ViewPager) container;
        View recycledView = (View) view;
        pager.removeView(recycledView);
        if(mRecycledViewStack.size() < 2){
            mRecycledViewStack.push(recycledView);
        }
        
    }
}
