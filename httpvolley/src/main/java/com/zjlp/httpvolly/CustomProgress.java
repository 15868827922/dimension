package com.zjlp.httpvolly;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class CustomProgress extends Dialog {

    private static CustomProgress dialog;
    
    private ProgressCancelNotifer progressCancelNotifer;

    public CustomProgress(Context context) {
        super(context);
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 给Dialog设置提示信息
     * 
     * @param message
     */
    public static void setMessage(CharSequence message) {
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        if (message != null && message.length() > 0) {
            dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    /**
     * 弹出自定义ProgressDialog
     * 
     * @param context
     *            上下文
     * @param message
     *            提示
     * @param aProgressCancelNotifer
     *            按下返回键监听
     * @return
     */
    public static CustomProgress showLoadingDialog(Context context, CharSequence message, ProgressCancelNotifer aProgressCancelNotifer) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
                dialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        dialog = new CustomProgress(context, R.style.Custom_Progress);
        
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_custom);
        if (TextUtils.isEmpty(message)) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        dialog.setCanceledOnTouchOutside(false);
        // 按返回键是否取消
        dialog.setCancelable(true);
        dialog.setProgressCancelNotifer(aProgressCancelNotifer);
        // 监听返回键处理
        dialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface aDialog) {
                if(dialog != null && dialog.getProgressCancelNotifer() != null){
                    dialog.getProgressCancelNotifer().notifyProgressCancel();
                }
                dialog = null;
            }
        });
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        try{
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return dialog;
    }
    
    
    public static CustomProgress showCustomLoadingDialog(Context context, int progressBg, CharSequence message, ProgressCancelNotifer aProgressCancelNotifer) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
                dialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        dialog = new CustomProgress(context, R.style.Custom_Progress);
        
        dialog.setTitle("");
        dialog.setContentView(progressBg);
        if (TextUtils.isEmpty(message)) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
        }
        dialog.setCanceledOnTouchOutside(false);
        // 按返回键是否取消
        dialog.setCancelable(true);
        dialog.setProgressCancelNotifer(aProgressCancelNotifer);
        // 监听返回键处理
        dialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface aDialog) {
                if(dialog != null && dialog.getProgressCancelNotifer() != null){
                    dialog.getProgressCancelNotifer().notifyProgressCancel();
                }
                dialog = null;
            }
        });
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        try{
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return dialog;
    }

    public static void dismissLoadingDialog() {
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                dialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isLoadingDialogShowing() {
        return dialog != null && dialog.isShowing();
    }

    public ProgressCancelNotifer getProgressCancelNotifer() {
        return progressCancelNotifer;
    }

    public void setProgressCancelNotifer(ProgressCancelNotifer progressCancelNotifer) {
        this.progressCancelNotifer = progressCancelNotifer;
    }

}
