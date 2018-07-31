package com.shenjing.dimension.dimension.me.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.shenjing.dimension.R;
public class SignDialog extends Dialog implements View.OnClickListener{


    private Context mContext;

    private View mViewMain;

    public SignDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.mContext = context;
        initView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_sign_in);
        setCanceledOnTouchOutside(true);
        mViewMain = findViewById(R.id.view_main);
        mViewMain.setOnClickListener(this);
        findViewById(R.id.btn_close_dialog).setOnClickListener(this);
        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.player_double_click_animation);
        mViewMain.startAnimation(animation);//开始动画
        animation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {//动画结束
//                imageView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_main:
            case R.id.btn_close_dialog:
                dismiss();
                break;
        }
    }
}
