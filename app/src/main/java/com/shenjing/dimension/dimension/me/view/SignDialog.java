package com.shenjing.dimension.dimension.me.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import com.shenjing.dimension.R;
public class SignDialog extends Dialog implements View.OnClickListener{


    public SignDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
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
        findViewById(R.id.view_main).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_main:
                dismiss();
                break;
        }
    }
}
