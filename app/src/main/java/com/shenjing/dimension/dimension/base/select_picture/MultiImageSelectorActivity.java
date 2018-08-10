package com.shenjing.dimension.dimension.base.select_picture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;


import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.FundmentalActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorActivity extends FundmentalActivity implements MultiImageSelectorFragment.Callback,View.OnClickListener{

    /** 最大图片选择数，int类型，默认5 */
    private static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 已选择图片数，int类 */
    private static final String EXTRA_SELECTED_PIC_NUM = "selected_pic_num";
    /** 图片选择模式，默认多选 */
    private static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
    
    public static final String EXTRA_PERFORM_ADD_PIC_URLS = "performAddPicUrls";
    public static final String EXTRA_SELECT_PICINFO_LIST = "selectPicInfoList";

    public static final int WHICH_FROM_CHAT = 1;
    public static final int WHICH_FROM_OTHERS = 2;
    public static final int WHICH_FROM_CUSTOM_EMOJI = 3;
    public static final int WHICH_FROM_COMMUNITY = 4;

    /** 单选 */
    private static final int MODE_SINGLE = 0;
    /** 多选 */
    private static final int MODE_MULTI = 1;
    
    /** 用于判断是否从聊天页面进入 */
    public static final String EXTRA_WHICH_ACT_FROM = "whichActivityFrom";  //

    private ArrayList<String> resultList = new ArrayList<String>();
    private int mDefaultCount;
    private int mSelectedPicNum;
    private int mSelectMode;

    private TextView mTvRight;
    /**
     * 用于判断从哪个入口进入选择图片
     * 1.从聊天入口进入
     * 2. 暂时包括除聊天之外的其他所有入口
     * 3 为添加表情页进入CustomEmojiManagerActivity
     * 4 社群发布商品链接
     */
    private int whichActFrom;

    private static List<Image> images = new ArrayList<Image>();

    public static void startActivityForMulti(Activity activity, int selectedPicNum, int selectCount, int whichFrom, int req){
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_SELECT_COUNT, selectCount);
        extras.putInt(EXTRA_SELECT_MODE, MODE_MULTI);
        extras.putInt(EXTRA_WHICH_ACT_FROM, whichFrom);
        extras.putInt(EXTRA_SELECTED_PIC_NUM, selectedPicNum);
        ActivityUtil.gotoActivityForResult(activity, MultiImageSelectorActivity.class, extras, req);
    }

    public static void startActivityForSingle(Activity activity, int req){
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SELECT_MODE, MODE_SINGLE);
        bundle.putInt(EXTRA_SELECT_COUNT, 1);
        bundle.putInt(EXTRA_WHICH_ACT_FROM, 2);
        bundle.putInt(EXTRA_SELECTED_PIC_NUM, 0);
        ActivityUtil.gotoActivityForResult(activity, MultiImageSelectorActivity.class, bundle, req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images.clear();
        setContentView(R.layout.page_mulitiimage_selectort);
        Bundle extra = getIntent().getExtras();
        mDefaultCount = extra.getInt(EXTRA_SELECT_COUNT, 5);
        mSelectedPicNum = extra.getInt(EXTRA_SELECTED_PIC_NUM, 0);
        mSelectMode = extra.getInt(EXTRA_SELECT_MODE, 1);
        whichActFrom = extra.getInt(EXTRA_WHICH_ACT_FROM, 2);

        findViewById(R.id.fl_title_left).setOnClickListener(this);
        mTvRight = (TextView) findViewById(R.id.tv_title_right);
        findViewById(R.id.fl_title_right).setOnClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), getIntent().getExtras()))
                .commit();

        // 完成按钮
        if(mSelectMode == MODE_SINGLE){
            mTvRight.setVisibility(View.GONE);
        }else {
            mTvRight.setVisibility(View.VISIBLE);
            if(resultList == null || resultList.size()<=0){
                mTvRight.setText(getBntNameByFrom(whichActFrom));
                setRightTextBtnEnabled(false);
            }else{
                mTvRight.setText((getBntNameByFrom(whichActFrom) + "(" )+resultList.size()+"/"+(mDefaultCount-mSelectedPicNum)+")");
                setRightTextBtnEnabled(true);
            }
        }
    }

    public void setRightTextBtnEnabled(boolean enabled){

        if (enabled){
            mTvRight.setEnabled(true);
            mTvRight.setTextColor(getResources().getColor(R.color.white));
        }else {
            mTvRight.setEnabled(false);
            mTvRight.setTextColor(getResources().getColor(R.color.unit_text_tv_title_2));
        }
    }

    /**
     * 对whichActFrom的文案显示做统一处理
     * @return
     */
    public static String getBntNameByFrom (int whichActFrom) {
        switch (whichActFrom) {
            case 1:
                return "发送";
            case 2:
                return "完成";
            case 3:
                return "使用";
            case 4:
                return "确定";
            default:
                return "完成";
        }

    }
    
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fl_title_left){
            setResult(RESULT_CANCELED);
            finish();
        }else if(v.getId() == R.id.fl_title_right){
            if(resultList != null && resultList.size() >0){
                Intent photoData = new Intent();
                photoData.putStringArrayListExtra(EXTRA_PERFORM_ADD_PIC_URLS, resultList);
                // 如果是社群发送商品链接，完成后直接打开商品链接设置界面
                setResult(RESULT_OK, photoData);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_PERFORM_ADD_PIC_URLS, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if(resultList.size() > 0){
            mTvRight.setText((getBntNameByFrom(whichActFrom) + "(")+resultList.size()+"/"+(mDefaultCount-mSelectedPicNum)+")");
            if(!mTvRight.isEnabled()){
                setRightTextBtnEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
            mTvRight.setText((getBntNameByFrom(whichActFrom) + "(")+resultList.size()+"/"+(mDefaultCount-mSelectedPicNum)+")");
        }else{
            mTvRight.setText((getBntNameByFrom(whichActFrom) + "(")+resultList.size()+"/"+(mDefaultCount-mSelectedPicNum)+")");
        }
        // 当为选择图片时候的状态
        if(resultList.size() == 0){
            mTvRight.setText(getBntNameByFrom(whichActFrom));
            setRightTextBtnEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if(imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
    @Override
    public void onRemoveAllSelect() {
        resultList.clear();
        mTvRight.setText(getBntNameByFrom(whichActFrom));
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRightTextBtnEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static List<Image> getImages(){
        return images;
    }

    public static void setImages(List<Image> imageList) {
        images = imageList;
    }
}
