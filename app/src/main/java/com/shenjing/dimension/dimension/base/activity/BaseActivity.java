package com.shenjing.dimension.dimension.base.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.fragment.BaseFragment;
import com.shenjing.dimension.dimension.base.image.LPNetworkRoundedImageView;
import com.shenjing.dimension.dimension.base.util.OnDoubleClickListener;
import com.shenjing.dimension.dimension.base.util.Utils;


public class BaseActivity extends FundmentalActivity implements OnClickListener{

    public final static int ID_RIGHT_IMAGE = R.id.right_title_btn;
    public final static int ID_LEFT_IMAGE = R.id.back;
    public final static int ID_RIGHT_TEXT = R.id.right_title_text_btn;
    public final static int ID_RIGHT_BTN_RED = R.id.right_title_btn_red;
    public final static int ID_LEFT_TEXT = R.id.left_title_text_btn;
    public final static int ID_TITLE_BAR = R.id.titleBar;
    public final static int ID_RIGHT_LAYOUT = R.id.right_title_layout;

/*    public final static int ID_RADIO_GROUP = R.id.radioGroupStatus;
    public final static int ID_RADIO_LEFT = R.id.radioLeft;
    public final static int ID_RADIO_RIGHT = R.id.radioRight;*/
    public ImageView imageLeft;
    protected TextView rightTitleTextBtn;
    protected Button rightRedButton;
    protected TextView leftTitleTextBtn;
    protected TextView textTitle;
    protected RelativeLayout rightTitleLayout;
    protected RadioButton radioLeftButton;
    protected RadioButton radioRightButton;
    private ImageView imageViewRight;
    private ImageView imageViewOfTitle;
    private LPNetworkRoundedImageView rightNetworkImage;
    private ViewGroup titleBar;
//    protected LPSearchView titleSearchView;
    private View dividerTitleBar;
    /** edit  by liht on 2016/8/11 15:18 记录上次titlebarColor，在弹窗后需要进行复原 */
    private int lastTitleBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.page_base);

        imageViewRight = (ImageView) findViewById(ID_RIGHT_IMAGE);
        rightNetworkImage= (LPNetworkRoundedImageView) findViewById(R.id.right_title_btn_network);
        imageLeft = (ImageView) findViewById(ID_LEFT_IMAGE);
        rightTitleTextBtn = (TextView) findViewById(ID_RIGHT_TEXT);
        rightRedButton = (Button) findViewById(ID_RIGHT_BTN_RED);
        leftTitleTextBtn = (TextView) findViewById(ID_LEFT_TEXT);

        rightTitleLayout = (RelativeLayout) findViewById(ID_RIGHT_LAYOUT);
        textTitle = (TextView)findViewById(R.id.title);
        imageViewOfTitle = (ImageView) findViewById(R.id.imgRightTitle);

        titleBar = (ViewGroup) findViewById(R.id.titleBar);
//        titleSearchView = (LPSearchView) findViewById(R.id.titleSearchView);
        dividerTitleBar = findViewById(R.id.divider_bottom_bar);

//        radioLeftButton = (RadioButton) findViewById(ID_RADIO_LEFT);
//        radioRightButton = (RadioButton) findViewById(ID_RADIO_RIGHT);
        setBackable(true);
        textTitle.setOnTouchListener(new OnDoubleClickListener() {

            @Override
            public void onDoubleClick(View v) {
                onTitleDoubleClick();
            }
        });


        setTitleBarColor(getResources().getColor(R.color.title_bar_color));
    }

    protected void setSearchTitle(){
        titleBar.findViewById(R.id.normalTitle).setVisibility(View.GONE);
//        titleSearchView.setVisibility(View.VISIBLE);
//        titleSearchView.requestFocus();
    }

    protected void onTitleDoubleClick(){}
    
    protected void setBackable(boolean backable){
        if(imageLeft == null){
            return;
        }
        if(backable){
            imageLeft.setVisibility(View.VISIBLE);
            imageLeft.setOnClickListener(this);
        }else{
            imageLeft.setVisibility(View.GONE);
        }
    }

    public void setCustomTitleView(View view){
        this.titleBar.addView(view);
    }
    public void setCustomTitleView(View view, LayoutParams params){
        this.titleBar.addView(view,params);
    }
    public void showRightTextBtnImg(int res){
        RelativeLayout rightTitleLayout = (RelativeLayout) findViewById(R.id.right_title_layout);
        rightTitleLayout.setVisibility(View.VISIBLE);
        TextView rightTitleText = (TextView)rightTitleLayout.findViewById(R.id.right_title_text);
        rightTitleText.setText(res);
    }

    public void setRightLayoutButtonClickListener(OnClickListener clickListener){
        rightTitleLayout.setOnClickListener(clickListener);
    }

    public void showRightTextBtn(int res){
        rightTitleTextBtn.setText(res);
        rightTitleTextBtn.setVisibility(View.VISIBLE);
    }
    
    public void showRightTextBtn(String rightText){
        rightTitleTextBtn.setText(rightText);
        rightTitleTextBtn.setVisibility(View.VISIBLE);
    }

    public void showRightTextBtn(CharSequence rightText){
        rightTitleTextBtn.setText(rightText);
        rightTitleTextBtn.setVisibility(View.VISIBLE);
    }

    public void showOrHideRightRedBtn(boolean isShow){
        rightRedButton.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setRightRedBtnText(String text){
        rightRedButton.setText(text);
    }

    public void setRightRedBtnEnabled(boolean isEnabled){
        rightRedButton.setEnabled(isEnabled);
    }

    public void showLeftTextBtn(CharSequence rightText){
        leftTitleTextBtn.setText(rightText);
        leftTitleTextBtn.setVisibility(View.VISIBLE);
    }
    
    public boolean getRightTextBtnEnabled(){
        return rightTitleTextBtn.isEnabled();
    }

    public boolean getRightRedBtnEnabled(){
        return rightRedButton.isEnabled();
    }

    public void setRightTextBtnEnabled(boolean enabled){
        rightTitleTextBtn.setEnabled(enabled);
        rightTitleTextBtn.setTextColor(enabled ? getResources().getColor(R.color.text_black) : getResources().getColor(R.color.unit_text_tv_title_3));
    }

    public void setRightTextColor(int color){
        rightTitleTextBtn.setTextColor(color);
    }

    public void setRightTextAlpha(float value){
        rightTitleTextBtn.setAlpha(value);
    }

    public void setTitleTextColor(int color){
        textTitle.setTextColor(color);
    }

    public void setTilteTextSize(int titleTextSize){
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(titleTextSize));
    }

    public void hideRightTextBtn(){
    	rightTitleTextBtn.setVisibility(View.GONE);
    }

    public void hideLeftTextBtn(){
        leftTitleTextBtn.setVisibility(View.GONE);
    }

    public ImageView getRightTitleImageView(){
        return imageViewRight;
    }

    public LPNetworkRoundedImageView getRightNetWorkButtonView(){
        return rightNetworkImage;
    }
    public void setRightTextButtonClickListener(OnClickListener clickListener){
        rightTitleTextBtn.setOnClickListener(clickListener);
    }

    public void setRightRedBtnClickListener(OnClickListener clickListener){
        rightRedButton.setOnClickListener(clickListener);
    }

    public void setLeftTextButtonClickListener(OnClickListener clickListener){
        leftTitleTextBtn.setOnClickListener(clickListener);
    }
    public void setRightNetImageButtonClickListener(OnClickListener listener){
        rightNetworkImage.setOnClickListener(listener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back){
            onBack();
        }
    }

    protected void onBack(){
        Utils.hideKeyboard(this);
        this.finish();
    }
    
    public void hideRightTitleButton(){
        imageViewRight.setVisibility(View.INVISIBLE);
    }
    
    public void showRightTitleButton(){
        imageViewRight.setVisibility(View.VISIBLE);
    }
    
    public void setRightTileButtonImage(int drawableId){
        rightNetworkImage.setVisibility(View.GONE);
        imageViewRight.setVisibility(View.VISIBLE);
        imageViewRight.setImageResource(drawableId);
    }
    public void setRightTitleButtonImage(String url, int defaultRes, float radius){
        imageViewRight.setVisibility(View.GONE);
        rightNetworkImage.setVisibility(View.VISIBLE);
        rightNetworkImage.setCornerRadius(radius);
        rightNetworkImage.setDefaultDrawableRes(defaultRes);
        rightNetworkImage.setDontLoadSameUrl(false);
        imageViewRight.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rightNetworkImage.setImageUrl(url);
    }


    public void setRightTitleButtonClickListener(OnClickListener clickListener){
        imageViewRight.setOnClickListener(clickListener);
    }
    
    public void hideLeftTitleButton(){
        imageLeft.setVisibility(View.INVISIBLE);
        hideLeftTextBtn();
    }

    public void hideLeftTitleCountLayout(){
        findViewById(R.id.leftTitleLayout).setVisibility(View.INVISIBLE);
    }
    public void hideRightNetImageButton(){
        rightNetworkImage.setVisibility(View.GONE);
    }

    public void setLeftTitleButtonImage(int drawableId){
        imageLeft.setVisibility(View.VISIBLE);
        imageLeft.setImageResource(drawableId);
    }
    
    public void setLeftTitleButtonClickListener(OnClickListener clickListener){
        imageLeft.setOnClickListener(clickListener);
    }
    
    public void setLeftTitleLayoutButtonClickListener(int drawableId, OnClickListener clickListener){
        findViewById(R.id.leftTitleLayout).setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.leftTitleImg)).setImageResource(drawableId);
        if(clickListener != null){
            findViewById(R.id.leftTitleLayout).setOnClickListener(clickListener);
        }
    }

  /*  public void setLeftTitleLayoutNewCount(int newMsgCount){
        NewCustomCountView newCountView = (NewCustomCountView)findViewById(R.id.newTitleCountView);
        if(newMsgCount == 0){
            newCountView.setVisibility(View.GONE);
        }else{
            newCountView.setCount(newMsgCount);
            newCountView.setVisibility(View.VISIBLE);
        }
    }*/
    
    public void setLeftTitleLayoutVisible(){
        View leftTitleLayout = findViewById(R.id.leftTitleLayout);
        if (leftTitleLayout != null){
            leftTitleLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setRadioTileButton(String leftText, String rightText){
    	setTitleText("");
//    	findViewById(ID_RADIO_GROUP).setVisibility(View.VISIBLE);
    	radioLeftButton.setText(leftText);
    	if(leftText.length() > 4){
    		radioLeftButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.common_font_sw320dp_of_12));
    	}
    	radioRightButton.setText(rightText);
    	if(rightText.length() > 4){
    		radioRightButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.common_font_sw320dp_of_12));
    	}

    }
    
    public void setRadioTileButton(String leftText, int leftTextSizeId, String rightText, int rightTextSizeId){
    	setTitleText("");
//    	findViewById(ID_RADIO_GROUP).setVisibility(View.VISIBLE);
    	radioLeftButton.setText(leftText);
    	radioLeftButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(leftTextSizeId));
    	radioRightButton.setText(rightText);
    	radioRightButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(rightTextSizeId));
    }
    
    @Override
    public void setContentView(int layoutResID) {
//        View view = LayoutInflater.from(this).inflate(layoutResID, null);
//        setContentView(view);
        LayoutInflater.from(this).inflate(layoutResID, (ViewGroup)findViewById(R.id.mainContainer), true);
    }
    
    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }
    
    @Override
    public void setContentView(View view, LayoutParams params) {
        FrameLayout mainContainer = (FrameLayout)findViewById(R.id.mainContainer);
        if(mainContainer.getChildCount() > 0){
            mainContainer.removeAllViews();
        }
        if(params == null){
            mainContainer.addView(view);
        }else{
            mainContainer.addView(view, params);
        }
    }

    public void setTitleText(CharSequence text) {
        textTitle.setText(text);
        hideImageViewOfTitle();
    }
    
    public void setTitleText(int resid) {
        textTitle.setText(resid);
        hideImageViewOfTitle();
    }

    public void setImageViewOfTitle(int resid){
        imageViewOfTitle.setImageResource(resid);
        showImageViewOfTitle();
    }

    public void showImageViewOfTitle(){
        imageViewOfTitle.setVisibility(View.VISIBLE);
    }
    public void hideImageViewOfTitle(){
        imageViewOfTitle.setVisibility(View.GONE);
    }

    public void setTitleAndImgLayoutOnClicklistener(){
        findViewById(R.id.titleAndImgLayout).setOnClickListener(this);
    }
    
    public void setTitleBarVisible(boolean visible) {
        findViewById(R.id.titleBar).setVisibility(visible? View.VISIBLE : View.GONE);
        setStatusBarContainerVisibility(visible);
    }

    public void setTitleDividerVisibility(int visible){
        dividerTitleBar.setVisibility(visible);
    }

    public void setTitleBarColor(int color){
        titleBar.setBackgroundColor(color);
        int titleBarColor = getResources().getColor(R.color.title_bar_color);
        if (color == titleBarColor){
            setStatusBgResource(R.drawable.shape_gradient_bg);
        }else{
            setStatusBackGroundColor(color);
        }
        lastTitleBarColor = color;
        dividerTitleBar.setVisibility(/*color == titleBarColor ? View.VISIBLE : */View.GONE);
    }

    protected int getTitleBarHeight() {
        return titleBar.getLayoutParams().height;
    }

    /** edit  by liht on 2016/8/11 10:14
     * 设置title栏为白色
     * @param toWhite true white, false black
     * */
    public void setNavTextWhite(boolean toWhite){
        textTitle.setTextColor(getResources().getColor(toWhite ? R.color.white : R.color.text_black));
        rightTitleTextBtn.setTextColor(getResources().getColor(toWhite ? R.color.white : R.color.text_black));
        imageLeft.setImageResource(toWhite ? R.mipmap.icon_nav_back_white : R.mipmap.icon_nav_back);
    }

    protected void setContentFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainContainer, fragment);
        transaction.commit();
    }

    protected void disableBaseBackground() {
        findViewById(R.id.baseContainer).setBackgroundDrawable(null);
    }
    
    public void setDark(boolean dark) {
        findViewById(R.id.darkHover).setVisibility(dark? View.VISIBLE : View.GONE);
        setStatusBackGroundColor(dark ? 0x50000000 : lastTitleBarColor);
    }






    public void showSingleText(int resId){
        findViewById(R.id.mainContainer).setVisibility(View.INVISIBLE);
        TextView baseTextTip = (TextView)findViewById(R.id.baseTextTip);
        baseTextTip.setVisibility(View.VISIBLE);
        baseTextTip.setText(resId);
    }
    
    public void dismissSingleText(){
        findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
        findViewById(R.id.baseTextTip).setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public String tag(){
        return String.valueOf(hashCode());
    }

    protected void addFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commitAllowingStateLoss();
    }
    protected void replaceFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commitAllowingStateLoss();
    }

    protected void replaceFragment(BaseFragment fragment, int viewId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(viewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commitAllowingStateLoss();
    }


    public void addFragment(Fragment fragment, int viewId){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(viewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public <T extends Fragment> T getFragmentByTag(String tag) {
        return (T) getSupportFragmentManager().findFragmentByTag(tag);
    }

}
