package com.shenjing.dimension.dimension.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class LimitHeightListView extends ListView {
    private int maxHeight;

    public LimitHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public LimitHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public LimitHeightListView(Context context) {
        super(context);
    }
    
    public void setMaxHeight(int height){
        maxHeight = height;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    
        //set your custom height. AT_MOST means it can be as tall as needed,
        //up to the specified size.
    
        int height = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
    
        super.onMeasure(widthMeasureSpec,height);               
    }
}
