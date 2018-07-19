package com.shenjing.dimension.dimension.me;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.shenjing.dimension.R;


public class TabHostNewView extends LinearLayout implements OnClickListener {

    public static final int REST_POSITION = R.id.rest;
    public static final int GAME_POSITION = R.id.game;
    public static final int SUPPLY_POSITION = R.id.supply;
    public static final int ME_POSITION = R.id.me;

    private int mCurrPosition;


    private OnTabClickListener listener;

    private LinearLayout mTabContainer;

    private boolean isTouchAble = true;

    public TabHostNewView(Context context) {
        super(context);
    }

    public TabHostNewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_tab_new, this);
        initView();
    }

    public TabHostNewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    private void initView(){
        mTabContainer = (LinearLayout) findViewById(R.id.main_tab_container);
        findViewById(R.id.rest).setOnClickListener(this);
        findViewById(R.id.me).setOnClickListener(this);
        findViewById(R.id.game).setOnClickListener(this);
        findViewById(R.id.supply).setOnClickListener(this);

    }
    
    public int getCurrPosition(){
        return mCurrPosition;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.rest){
            if(listener != null){
                listener.onTabClick(REST_POSITION);
            }
            switchTab(REST_POSITION);
        }else if(viewId == R.id.me){
            if(listener != null){
                listener.onTabClick(ME_POSITION);
            }
            switchTab(ME_POSITION);
        }else if(viewId==R.id.game){
            if(listener!=null){
                listener.onTabClick(GAME_POSITION);
            }
            switchTab(GAME_POSITION);
        } else if (viewId == R.id.supply) {
            if(listener!=null){
                listener.onTabClick(SUPPLY_POSITION);
            }
            switchTab(SUPPLY_POSITION);
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
