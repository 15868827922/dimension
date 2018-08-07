package com.shenjing.dimension.dimension.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.shenjing.dimension.R;


public class TabScoreMallView extends LinearLayout implements OnClickListener {

    public static final int CONVERT_SCORE_POSITION = R.id.view_convert_score;
    public static final int DOLL_POSITION = R.id.view_doll;
    public static final int AROUND_MALL_POSITION = R.id.view_around_mall;
    public static final int MILKY_TEA_POSITION = R.id.view_milky_tea;

    private int mCurrPosition;


    private OnTabClickListener listener;

    private LinearLayout mTabContainer;

    private boolean isTouchAble = true;

    public TabScoreMallView(Context context) {
        super(context);
    }

    public TabScoreMallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_tab_score_mall, this);
        initView();
    }

    public TabScoreMallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    private void initView(){
        mTabContainer = (LinearLayout) findViewById(R.id.main_tab_container);
        findViewById(R.id.view_convert_score).setOnClickListener(this);
        findViewById(R.id.view_doll).setOnClickListener(this);
        findViewById(R.id.view_around_mall).setOnClickListener(this);
        findViewById(R.id.view_milky_tea).setOnClickListener(this);

    }
    
    public int getCurrPosition(){
        return mCurrPosition;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == CONVERT_SCORE_POSITION){
            if(listener != null){
                listener.onTabClick(CONVERT_SCORE_POSITION);
            }
            switchTab(R.id.view_convert_score_tip);
        }else if(viewId == DOLL_POSITION){
            if(listener != null){
                listener.onTabClick(DOLL_POSITION);
            }
            switchTab(R.id.view_doll_tip);
        }else if(viewId==AROUND_MALL_POSITION){
            if(listener!=null){
                listener.onTabClick(AROUND_MALL_POSITION);
            }
            switchTab(R.id.view_around_mall_tip);
        } else if (viewId == MILKY_TEA_POSITION) {
            if(listener!=null){
                listener.onTabClick(MILKY_TEA_POSITION);
            }
            switchTab(R.id.view_milky_tip);
        }
    }
    
    public void switchTab(int index){
        try {
            if (mCurrPosition != 0) {
                mTabContainer.findViewById(mCurrPosition).setSelected(false);
            }
            mTabContainer.findViewById(index).setSelected(true);
            mCurrPosition = index;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public interface OnTabClickListener{
        public void onTabClick(int position);
    }
    
    public void setOnTabClickListener(OnTabClickListener listener){
        this.listener = listener;
    }
    
   @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
       if(isTouchAble){
           return super.dispatchTouchEvent(ev);
       }else{
           return true;
       }
    }
   
   public void setViewClickable(boolean isTouchAble){
       this.isTouchAble = isTouchAble;
   }

}
