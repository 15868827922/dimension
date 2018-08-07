package com.shenjing.dimension.dimension.me.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.shenjing.dimension.R;

/**
 * Created by syj on 2018/5/4.
 * Desc:
 */

public class ConvertScoreTipDialog extends Dialog implements View.OnClickListener{



    private TextView tvContent;

    private TextView tvSure;


    private TextView tvGoCheck;

    public ConvertScoreTipDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        initView();
    }


    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_convert_score_tip);
        tvContent = (TextView) findViewById(R.id.tv_tip_content);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvGoCheck = (TextView) findViewById(R.id.tv_go_convert);
        tvSure.setOnClickListener(this);
        tvGoCheck.setOnClickListener(this);
        setDollInfo("十多个色三大方法", true);


    }



    public void setDollInfo(String tip, boolean isShowImage){
        Drawable drawableScore = getContext().getResources().getDrawable(R.mipmap.icon_score_num_tip);
        drawableScore.setBounds(0, 0, drawableScore.getIntrinsicWidth(), drawableScore.getIntrinsicHeight());

        SpannableString spannableString = new SpannableString("pi");
        ImageSpan imageSpan = new ImageSpan(drawableScore, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);



        tvContent.append(colourTxtTipBalance("确定要将物品兑换成","相应"));
        tvContent.append("积分");
        tvContent.append(spannableString);//将图片转为文字显示，这个是重点
        tvContent.append("？");

    }

    //文字变色的部分
    private Spanned colourTxtTipBalance(String tip,String mainTip) {
//        return Html.fromHtml("<font color='#3C3C3C'></font><font color='#FD9995'>" + tip +"</font><font color='#3C3C3C'>" + mainTip + "</font>");
        return Html.fromHtml("<font color='#000000'>"+ tip +"</font><font color='#E6002D'>" + mainTip + "</font>");
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure:
                dismiss();
                break;

            case R.id.tv_go_convert:
                dismiss();
                break;
        }
    }


}
