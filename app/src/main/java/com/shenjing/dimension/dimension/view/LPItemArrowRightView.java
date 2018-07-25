package com.shenjing.dimension.dimension.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.BaseKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenjing.dimension.R;

/**
 * 带向右箭头的公用控件
 */
public class LPItemArrowRightView extends RelativeLayout {

    private TextView textTitle, textSecTitle;
    private EditText edtSecTitle;
    private ImageView imvLeftImage, imvThumRightImage;
    private View mRedDot; // 中间小红点
    private View itemLayout, setUpView, arrowRight;

    /**
     * 样式类型说明：
     * 1、TYPE_TITLE_ONLY --包含 左侧黑体文本
     * 2、TYPE_TITLE_WITH_TEXTINFO -- 包含 左侧黑体文本、右侧提示文本
     * 3、TYPE_TITLE_WITH_EDITTEXT -- 包含 左侧黑体文本、右侧输入框
     * 4、TYPE_TITLE_WITH_THUMIMAGE -- 包含 左侧黑体文本、右侧缩略图
     * 4、TYPE_TITLE_HAS_LEFTIMAFE -- 包含 左侧黑体文本、左侧文本前的图片
     * 注：所有的type都包含向右箭头
     */
    public enum ShowStyleType{
        TYPE_TITLE_ONLY,
        TYPE_TITLE_WITH_TEXTINFO,
        TYPE_TITLE_WITH_EDITTEXT,
        TYPE_TITLE_WITH_THUMIMAGE,
        TYPE_TITLE_HAS_LEFTIMAFE,
    }

    public LPItemArrowRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LPItemArrowRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);

    }

    public void setSecTitleText(String secText){
        if(secText == null){
            return;
        }
        if(style == ShowStyleType.TYPE_TITLE_WITH_TEXTINFO){
            textSecTitle.setText(secText);
        }else if(style == ShowStyleType.TYPE_TITLE_WITH_EDITTEXT){
            edtSecTitle.setText(secText);
        }

    }

    public void setSecTitleColor(int id){
        textSecTitle.setTextColor(getResources().getColor(id));
    }

    public String getSecTitleText(){
        if(style == ShowStyleType.TYPE_TITLE_WITH_TEXTINFO){
            return TextUtils.isEmpty(textSecTitle.getText()) ? "" : textSecTitle.getText().toString();
        }else if(style == ShowStyleType.TYPE_TITLE_WITH_EDITTEXT){
            return TextUtils.isEmpty(edtSecTitle.getText()) ? "" : edtSecTitle.getText().toString();
        }
        return TextUtils.isEmpty(textSecTitle.getText()) ? "" : textSecTitle.getText().toString();
    }

    public void setSecInputHint(String hintText){
       if(!TextUtils.isEmpty(hintText) && style == ShowStyleType.TYPE_TITLE_WITH_EDITTEXT){
           edtSecTitle.setHint(hintText);
        }
    }

    public void setSecInputLength(int maxLength){
       if(maxLength >0 && style == ShowStyleType.TYPE_TITLE_WITH_EDITTEXT){
           edtSecTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
    }

    public void setSecInputMaxLines(int maxLines){
       if(maxLines >0 && style == ShowStyleType.TYPE_TITLE_WITH_EDITTEXT){
           edtSecTitle.setMaxLines(maxLines);
        }
    }

    public void setSecInputKeyListener(BaseKeyListener listener){
        edtSecTitle.setKeyListener(listener);
    }

    public void setTitleText(String titleText){
        if(TextUtils.isEmpty(titleText))
            return;
        textTitle.setText(titleText);
    }

    public void setLeftImage(int resId){
        imvLeftImage.setImageResource(resId);
    }

    public void setThumImage(int resId){
        imvThumRightImage.setImageResource(resId);
    }

    private void initView(Context context, AttributeSet attrs){

        LayoutInflater.from(getContext()).inflate(R.layout.view_lp_item_arrow_right, this);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textSecTitle = (TextView) findViewById(R.id.textSecTitle);
        edtSecTitle = (EditText) findViewById(R.id.edtSecTitle);
        imvThumRightImage = (ImageView) findViewById(R.id.imvThumRightImage);
        imvLeftImage = (ImageView) findViewById(R.id.imvLeftImage);
        arrowRight = findViewById(R.id.arrowRight);
        itemLayout = findViewById(R.id.itemLayout);
        setUpView = findViewById(R.id.setUpView);
        mRedDot = findViewById(R.id.red_dot);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.itemWithRightArrow);
        initItemStyle(typedArray.getInt(R.styleable.itemWithRightArrow_itemStyle, 0));
        setTitleText(typedArray.getString(R.styleable.itemWithRightArrow_titleText));
        setSecTitleColor(typedArray.getResourceId(R.styleable.itemWithRightArrow_secTextColor, R.color.unit_text_tv_title_3));

//        if(style == ShowStyleType.TYPE_TITLE_WITH_TEXTINFO){
            setSecTitleText(typedArray.getString(R.styleable.itemWithRightArrow_secText));
//        }else if(style == ShowStyleType.TYPE_TITLE_HAS_LEFTIMAFE){
            setLeftImage(typedArray.getResourceId(R.styleable.itemWithRightArrow_headerImage, 0));
//        }else if(style == ShowStyleType.TYPE_TITLE_WITH_THUMIMAGE){
            setThumImage(typedArray.getResourceId(R.styleable.itemWithRightArrow_defaultThumImage, 0));
            setSecInputHint(typedArray.getString(R.styleable.itemWithRightArrow_inputHint));
            setSecInputLength(typedArray.getInt(R.styleable.itemWithRightArrow_inputLenght, 0));
            setSecInputMaxLines(typedArray.getInt(R.styleable.itemWithRightArrow_maxLines, 0));
//        }

        setItemEnable(typedArray.getBoolean(R.styleable.itemWithRightArrow_itemEnable, true));
    }

    public TextView getTextSecTitle() {
        return textSecTitle;
    }

    ShowStyleType style = ShowStyleType.TYPE_TITLE_ONLY;
    public void initItemStyle(int style){
        if(style == ShowStyleType.TYPE_TITLE_ONLY.ordinal()){
            //默认状态
            this.style = ShowStyleType.TYPE_TITLE_ONLY;
        }else if(style == ShowStyleType.TYPE_TITLE_WITH_TEXTINFO.ordinal()){
            this.style = ShowStyleType.TYPE_TITLE_WITH_TEXTINFO;
            textSecTitle.setVisibility(VISIBLE);
            setUpView.setVisibility(GONE);
        }else if(style == ShowStyleType.TYPE_TITLE_WITH_EDITTEXT.ordinal()){
            this.style = ShowStyleType.TYPE_TITLE_WITH_EDITTEXT;
            edtSecTitle.setVisibility(VISIBLE);
            arrowRight.setVisibility(GONE);
            setUpView.setVisibility(GONE);
        }else if(style == ShowStyleType.TYPE_TITLE_WITH_THUMIMAGE.ordinal()){
            this.style = ShowStyleType.TYPE_TITLE_WITH_THUMIMAGE;
            imvThumRightImage.setVisibility(VISIBLE);
        }else if(style == ShowStyleType.TYPE_TITLE_HAS_LEFTIMAFE.ordinal()){
            this.style = ShowStyleType.TYPE_TITLE_HAS_LEFTIMAFE;
            imvLeftImage.setVisibility(VISIBLE);
        }
    }

    public void setItemEnable(boolean enable){
        itemLayout.setEnabled(enable);
        itemLayout.setBackgroundResource(enable ? R.drawable.unit_selector_bg_view : R.color.white);
        if(style != ShowStyleType.TYPE_TITLE_WITH_EDITTEXT){
            arrowRight.setVisibility(enable ? VISIBLE : GONE);
        }else{
            edtSecTitle.setEnabled(enable);
        }
    }
    public void setArrowVisibility (int visibility) {
        if (arrowRight != null) {
            arrowRight.setVisibility(visibility);
        }
    }

    public void showRedDot(boolean b) {
        if (b) {
            mRedDot.setVisibility(VISIBLE);
        } else {
            mRedDot.setVisibility(GONE);
        }
    }

    public EditText getEditText(){
        return edtSecTitle;
    }

}
