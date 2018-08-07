package com.shenjing.dimension.dimension.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.shenjing.dimension.R;


/**
 * Created by Administrator on 2015/6/30.
 */
public class SquareView extends FrameLayout {

    public static final int SPEC_WIDTH=0;
    public static final int SPEC_HEIGHT=1;


    /**
     * 如果是byWith ，那height=width * ratio
     * 如果是byHeight，那width=height * ratio
     */
    int spec;
    /**
     * 比例
     */
    float ratio;

    public SquareView(Context context) {
        this(context, null);
    }

    public SquareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.SquareView);
        spec=a.getInt(R.styleable.SquareView_spec,0);
        ratio=a.getFloat(R.styleable.SquareView_ratio,1);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int originWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originHeight = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.EXACTLY;
        int widthSpec;
        int heightSpec;
        if(this.spec==SPEC_WIDTH){
            widthSpec=widthMeasureSpec;
             heightSpec = (MeasureSpec.makeMeasureSpec((int) (originWidth*ratio), mode));
        }else{
            heightSpec=heightMeasureSpec;
            widthSpec =  (MeasureSpec.makeMeasureSpec((int) (originHeight*ratio), mode));
        }
        super.onMeasure(widthSpec, heightSpec);
    }

    /**
     *
     * @param byWhat
     */
    public void setSpec(int byWhat){
        if(this.spec!=byWhat){
            this.spec=byWhat;
            requestLayout();
        }
    }
    public  void setRatio(float ratio){
        if(this.ratio!=ratio){
            this.ratio=ratio;
            requestLayout();
        }
    }
}
