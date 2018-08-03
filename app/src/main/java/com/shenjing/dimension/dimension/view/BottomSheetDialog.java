package com.shenjing.dimension.dimension.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.shenjing.dimension.R;

/**
 * Created by tiny on 2016/5/19.
 */
public class BottomSheetDialog extends Dialog {

    public BottomSheetDialog(Context context) {
        super(context, R.style.BottomSheetDialog);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(wrapContentView(View.inflate(getContext(), layoutResID, null), null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(wrapContentView(view, null));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(wrapContentView(view, params));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        this.setCanceledOnTouchOutside(true);
    }

    private View wrapContentView(final View view, ViewGroup.LayoutParams lp) {
        final View root = View.inflate(getContext(), R.layout.bottom_sheet_root, null);
        final FrameLayout mContent = (FrameLayout) root.findViewById(R.id.content_dialog_action_sheet);
        if (lp == null) {
            mContent.addView(view);
        } else {
            mContent.addView(view, lp);
        }
        return root;
    }
}
