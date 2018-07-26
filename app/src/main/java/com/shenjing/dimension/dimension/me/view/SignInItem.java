package com.shenjing.dimension.dimension.me.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.shenjing.dimension.R;
public class SignInItem extends RelativeLayout{
    private View inflate;

    public SignInItem(Context context) {
        super(context);
        initView();
    }


    public SignInItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate = View.inflate(getContext(), R.layout.dialog_sign_in, this);
    }
}
