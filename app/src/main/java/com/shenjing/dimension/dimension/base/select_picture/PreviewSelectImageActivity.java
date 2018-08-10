package com.shenjing.dimension.dimension.base.select_picture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.shenjing.dimension.dimension.base.activity.FundmentalActivity;
import com.shenjing.dimension.dimension.base.device.DeviceInfo;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.base.util.StatusBarUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.view.LPDialogFactory;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.InputStreamBitmapDecoderFactory;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PreviewSelectImageActivity extends FundmentalActivity implements OnPageChangeListener, OnClickListener {

    private static final String STATE_POSITION = "STATE_POSITION";

//    private static final String EXTRA_IMAGE_URLS = "images";
    private static final String EXTRA_DEFAULT_POSITION = "image_index";
    private static final String EXTRA_DEFAULT_IMG = "image_default";
    private static final String EXTRA_TAKE_PHOTO = "isTakePhoto";
    private static final String EXTRA_PICINFO_LIST = "picInfoList";
    private static final String EXTRA_IS_PREVIEW = "isPreview";
    private static final String EXTRA_IS_SHOW_ORIGINAL_LAYOUT = "isShowOriginalLayout";

    
    public static final String EXTRA_SELECT_PICINFO_LIST = "selectImageUrls";
//    public static final String EXTRA_IS_SEND_PIC_RIGHTNOW = "isSendPicRightNow";

    private HackyViewPager pager;

    private int lastPosition;
    
    private int defaultPosition;
    private int defaultImg;
    
    private ArrayList<Image> imgInfoList;
    private ArrayList<Image> selectImgInfoList;

    private View titleBarWithStatusBarContainer;
    private Animation outToUpAnim;
    private Animation inFromUpAnim;
    private Animation inFromBottomAnim;
    private Animation outToBottomAnim;
    
    private View selectImgLayout;
    private ImageView selectOriginalImg;
    private TextView imgSizeText;
    private ImageView selectImg;
    private View selectLayout;
    private View originalLayout;
    private Button rightTextBtn;

    private boolean isTakePhoto;
    private int enableSelectCount;
    private boolean isPreview;
    private boolean isShowOriginalSize;
    private boolean isShowOriginalLayout;

    private int whichActFrom; // 1-来自聊天页面

    public static void startActivityForResult(Activity activity, ArrayList<Image> picInfoList, int defaultPosition, boolean isTakePhoto, int reqCode) {
        Bundle extras = new Bundle();
        extras.putSerializable(EXTRA_PICINFO_LIST, picInfoList);
        extras.putInt(EXTRA_DEFAULT_POSITION, defaultPosition);
        extras.putBoolean(EXTRA_TAKE_PHOTO, isTakePhoto);
        ActivityUtil.gotoActivityForResult(activity, PreviewSelectImageActivity.class, extras, reqCode);
    }
    
    public static void startActivtyForResult(Fragment fragment, int defaultPosition, int enableSelectCount,
                                             int reqCode, boolean isShowOriginalLayout, int whichActFrom) {
        startActivtyForResult(fragment, null, defaultPosition, enableSelectCount, false, reqCode, isShowOriginalLayout, whichActFrom);
    }

    public static void startActivtyForResult(Fragment fragment, ArrayList<Image> selectImgInfoList, int defaultPosition, int enableSelectCount,
                                             boolean isPreview, int reqCode, boolean isShowOriginalLayout, int whichActFrom) {
        Bundle extras = new Bundle();
        extras.putSerializable(EXTRA_SELECT_PICINFO_LIST, selectImgInfoList);
        extras.putInt(EXTRA_DEFAULT_POSITION, defaultPosition);
        extras.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, enableSelectCount);
        extras.putBoolean(EXTRA_IS_PREVIEW, isPreview);
        extras.putBoolean(EXTRA_IS_SHOW_ORIGINAL_LAYOUT, isShowOriginalLayout);
        extras.putInt(MultiImageSelectorFragment.EXTRA_WHICH_ACT_FROM, whichActFrom);
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(fragment.getActivity(), PreviewSelectImageActivity.class);
        fragment.startActivityForResult(intent, reqCode);
//        ActivityUtil.gotoActivityForResult(activity, SendPictureViewActivity.class, extras, reqCode);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initData(savedInstanceState);

       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        setContentView(R.layout.page_view_send_picture);
//        setStatusBarContainerVisibility(false);
//
        StatusBarUtils.setStatusBarVisibility(this, true);

        if(imgInfoList.isEmpty() && defaultImg != 0){

        }else{
            defaultImg = R.color.unit_color_loadimv_main;
        }

        titleBarWithStatusBarContainer = findViewById(R.id.titleBarWithStatusBarContainer);
        inFromUpAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_from_up);
        outToUpAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_to_up);
        inFromBottomAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_from_bottom);
        outToBottomAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_to_bottom);
        outToUpAnim.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {}
            
            @Override
            public void onAnimationRepeat(Animation animation) {}
            
            @Override
            public void onAnimationEnd(Animation animation) {
                titleBarWithStatusBarContainer.setVisibility(View.INVISIBLE);
            }
        });
        outToBottomAnim.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {

            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                selectLayout.setVisibility(View.INVISIBLE);
            }
        });
        
        
        pager = (HackyViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter(imgInfoList, this));
        pager.setCurrentItem(defaultPosition);
        
        originalLayout = findViewById(R.id.originalLayout);
        selectImgLayout = findViewById(R.id.selectImgLayout);
        selectOriginalImg = (ImageView) findViewById(R.id.selectOriginalImg);
        imgSizeText = (TextView) findViewById(R.id.imgSizeText);
        selectImg = (ImageView) findViewById(R.id.selectImg);
        selectLayout = findViewById(R.id.selectLayout);
        rightTextBtn = (Button) findViewById(R.id.right_title_btn);
        
       /* if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            View statusBarContainer = findViewById(R.id.statusBarContainer);
            statusBarContainer.setVisibility(View.VISIBLE);
            int statusBarHeight = StatusBarUtils.getStatusBarHeight(getApplicationContext());
            
            View titleBar = findViewById(R.id.titleBar);
            
            LayoutParams redTitleLayoutParams = (LayoutParams) titleBar.getLayoutParams();
            redTitleLayoutParams.topMargin = statusBarHeight;
            titleBar.setLayoutParams(redTitleLayoutParams);

            LayoutParams statusBarContainerParams = (LayoutParams) statusBarContainer.getLayoutParams();
            statusBarContainerParams.height = statusBarHeight;

            View rootView = findViewById(R.id.rootView);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootView.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,
                    layoutParams.topMargin + StatusBarUtils.getStatusBarHeight(rootView.getContext()),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
            rootView.setLayoutParams(layoutParams);
        }*/

        
        pager.setOnPageChangeListener(this);
        
        titleBarWithStatusBarContainer.setVisibility(View.VISIBLE);
        selectLayout.setVisibility(View.VISIBLE);
        if(!isTakePhoto){
            if(selectImgInfoList == null || selectImgInfoList.isEmpty()){
                rightTextBtn.setText(MultiImageSelectorActivity.getBntNameByFrom(whichActFrom) );
            }else{
                rightTextBtn.setText((MultiImageSelectorActivity.getBntNameByFrom(whichActFrom) + "(")+selectImgInfoList.size() + "/"+enableSelectCount+")");
            }
            selectImgLayout.setVisibility(View.VISIBLE);
        }else{
            selectImgLayout.setVisibility(View.GONE);
        }
        selectImgLayout.setOnClickListener(this);
        findViewById(R.id.right_title_btn).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        originalLayout.setOnClickListener(this);
        selectOriginalImg.setImageResource( R.color.transparent);
        if(isTakePhoto || !selectImgInfoList.isEmpty()){
            imgSizeText.setText("原图("+returnFileSize(imgInfoList.get(lastPosition).length)+")");
        }else{
            imgSizeText.setText("原图");
        }
        if(selectImgInfoList == null || selectImgInfoList.isEmpty() || isTakePhoto){
            rightTextBtn.setText(MultiImageSelectorActivity.getBntNameByFrom(whichActFrom) );
        }
        setResult(RESULT_CANCELED);
        if(!isTakePhoto){
            for(Image image : selectImgInfoList){
                if(imgInfoList.get(lastPosition).equals(image)){
                    selectImg.setBackgroundResource(R.mipmap.icon_check_goods_select);
                    break;
                }
            }
        }
        
        originalLayout.setVisibility(isShowOriginalLayout  || isTakePhoto ? View.VISIBLE : View.GONE);
//        setAbleFlingBack(false);
//        setSwipeBackEnable(false);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.selectImgLayout){
            selectOrUnSelectPicInfo();
        }else if(v.getId() == R.id.right_title_btn){
            completeSelect();
        }else if(v.getId() == R.id.back){
            finishSelectByBack();
        }else if(v.getId() == R.id.originalLayout){
            if(selectImgInfoList == null){
                selectImgInfoList = new ArrayList<Image>();
            }
            if(selectImgInfoList.isEmpty()){
                selectImgInfoList.add(imgInfoList.get(lastPosition));
                selectImg.setBackgroundResource(R.mipmap.icon_check_goods_select);
                if(selectImgInfoList == null || selectImgInfoList.isEmpty()){
                    rightTextBtn.setText(MultiImageSelectorActivity.getBntNameByFrom(whichActFrom));
                }else{
                    rightTextBtn.setText((MultiImageSelectorActivity.getBntNameByFrom(whichActFrom) + "(")+selectImgInfoList.size() + "/"+enableSelectCount+")");
                }
                imgSizeText.setText("原图("+returnFileSize(imgInfoList.get(lastPosition).length)+")");
            }
            selectOriginalImg.setImageResource(R.color.transparent);
            isShowOriginalSize = true;
        }
    }
    
    private void initData(Bundle savedInstanceState){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            defaultPosition = bundle.getInt(EXTRA_DEFAULT_POSITION, 0);
            defaultImg = bundle.getInt(EXTRA_DEFAULT_IMG,R.color.unit_color_loadimv_main);
            isTakePhoto = bundle.getBoolean(EXTRA_TAKE_PHOTO);
            isPreview = bundle.getBoolean(EXTRA_IS_PREVIEW);
            whichActFrom = bundle.getInt(MultiImageSelectorFragment.EXTRA_WHICH_ACT_FROM, 2);
//            resultImgSiezMapList = (ArrayList<HashMap<String, String>>) bundle.getSerializable(EXTRA_IMAGE_URL_SIZE_MAP_LIST);
            enableSelectCount = bundle.getInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT);
            imgInfoList = (ArrayList<Image>) bundle.getSerializable(EXTRA_PICINFO_LIST);
            if(!isPreview){
                if(imgInfoList == null){
                    imgInfoList = (ArrayList<Image>) MultiImageSelectorActivity.getImages();
                }
            }
            if(isPreview || isTakePhoto){
                if(isPreview){
                    selectImgInfoList = (ArrayList<Image>) bundle.getSerializable(EXTRA_SELECT_PICINFO_LIST);
                    imgInfoList = new ArrayList(selectImgInfoList);
                }else{
                    selectImgInfoList = new ArrayList(imgInfoList);
                }
                if(selectImgInfoList ==null || selectImgInfoList.isEmpty()){
                    selectImgInfoList = new ArrayList<Image>();
                }
            }else{
                selectImgInfoList = (ArrayList<Image>) bundle.getSerializable(EXTRA_SELECT_PICINFO_LIST);
                if(selectImgInfoList ==null || selectImgInfoList.isEmpty()){
                    selectImgInfoList = new ArrayList<Image>();
                }
            }

            isShowOriginalLayout = bundle.getBoolean(EXTRA_IS_SHOW_ORIGINAL_LAYOUT);
        }
        if (savedInstanceState != null) {
            defaultPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        lastPosition = defaultPosition;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private ArrayList<Image> images;
        private LayoutInflater inflater;
        private Context mContext;

        ImagePagerAdapter(ArrayList<Image> images, Context context) {
            this.images = images;
            this.mContext = context;
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            PhotoView photoView = (PhotoView) ((View)object).findViewById(R.id.image);
            if(photoView != null){
                photoView.setImageDrawable(null);
            }
            System.gc();
            
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, final int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
            imageLayout.setId(position);

            final PhotoView photoView = (PhotoView) imageLayout.findViewById(R.id.image);
            final LargeImageView largeImageView = (LargeImageView) imageLayout.findViewById(R.id.large_image);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            
            //modify by xionghoumiao 图片文件路径
            String uri = imgInfoList.get(position).path;
            
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                
                @Override
                public void onPhotoTap(View arg0, float arg1, float arg2) {
                    showOrHideTitleBar();
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            }) ;
            largeImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrHideTitleBar();
                }
            });

            if(!uri.contains("http://live.smwawa.com")){
                if(!uri.startsWith("file:"))
                    uri = "file://" + uri;
            }

            spinner.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);
            largeImageView.setVisibility(View.GONE);

            Glide.with(mContext).load(uri).asBitmap().override(DeviceInfo.getScreenWidth(mContext), DeviceInfo.getScreenHeight(mContext)).placeholder(defaultImg).listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    spinner.setVisibility(View.GONE);
                    photoView.setVisibility(View.VISIBLE);
                    largeImageView.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    spinner.setVisibility(View.GONE);
                    return false;
                }
            }).into(new SimpleTarget<Bitmap>(DeviceInfo.getScreenWidth(mContext), DeviceInfo.getScreenHeight(mContext)) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    float whRatio = (float) resource.getWidth() / (float) resource.getHeight();
                    if(whRatio > 2.0f || whRatio < 0.5f) {
                        photoView.setVisibility(View.GONE);
                        largeImageView.setVisibility(View.VISIBLE);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                        largeImageView.setImage(new InputStreamBitmapDecoderFactory(isBm));
                    } else {
                        largeImageView.setVisibility(View.GONE);
                        photoView.setVisibility(View.VISIBLE);
                        photoView.setImageBitmap(resource);
                    }
                }
            });

            view.addView(imageLayout, 0);
            return imageLayout;
        }
        

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        View imageLayout = pager.findViewById(lastPosition);
        if (imageLayout != null) {
            PhotoView photoView = (PhotoView) imageLayout.findViewById(R.id.image);
            if (photoView != null) {
                photoView.setScale(1.0f, 0, 0, true);
                if(isShowOriginalLayout){
                    imgSizeText.setText(isShowOriginalSize|| !selectImgInfoList.isEmpty()? "原图("+returnFileSize(imgInfoList.get(position).length)+")" : "原图");
                }
                if(!isTakePhoto){
                    selectImgLayout.setVisibility(View.VISIBLE);
                    selectImg.setBackgroundResource(R.mipmap.icon_check_goods_default);
                    for(Image image : selectImgInfoList){
                        if(image.equals(imgInfoList.get(position))){
                            selectImg.setBackgroundResource(R.mipmap.icon_check_goods_select);
                            break;
                        }
                    }
                    if(selectImgInfoList == null || selectImgInfoList.isEmpty()){
                        rightTextBtn.setText(MultiImageSelectorActivity.getBntNameByFrom(whichActFrom));
                    }else{
                        rightTextBtn.setText((MultiImageSelectorActivity.getBntNameByFrom(whichActFrom) + "(")+selectImgInfoList.size() + "/"+enableSelectCount+")");
                    }
                }else{
                    selectImgLayout.setVisibility(View.GONE);
                }
            }
        }
        lastPosition = position;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pager != null){
            for(int i = 0; i < pager.getChildCount(); i ++){
                View child = pager.getChildAt(i);
                PhotoView photoView = (PhotoView)child.findViewById(R.id.image);
                if(photoView != null){
                    photoView.setImageDrawable(null);
                }
            }
        }
        System.gc();
    }
    
    
    
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.scale_fade_out);
    }
    
    private void showOrHideTitleBar(){
        if(titleBarWithStatusBarContainer.getVisibility() == View.VISIBLE){
            titleBarWithStatusBarContainer.startAnimation(outToUpAnim);
            selectLayout.startAnimation(outToBottomAnim);
            findViewById(R.id.right_title_btn).setOnClickListener(null);
            findViewById(R.id.back).setOnClickListener(null);
            StatusBarUtils.setStatusBarVisibility(this, false);
        }else{
            titleBarWithStatusBarContainer.setVisibility(View.VISIBLE);
            selectLayout.setVisibility(View.VISIBLE);
            selectLayout.startAnimation(inFromBottomAnim);
            titleBarWithStatusBarContainer.startAnimation(inFromUpAnim);
            findViewById(R.id.right_title_btn).setOnClickListener(this);
            findViewById(R.id.back).setOnClickListener(this);
            StatusBarUtils.setStatusBarVisibility(this, true);
        }
    }

    @SuppressLint("StringFormatMatches")
    private void selectOrUnSelectPicInfo(){
        boolean isSelectOrUnSelect = false;
        for(Image image : selectImgInfoList){
            if(image.equals(imgInfoList.get(lastPosition))){
                isSelectOrUnSelect = true;
                selectImgInfoList.remove(image);
                selectImg.setBackgroundResource(R.mipmap.icon_check_goods_default);
                break;
            }
        }
        if(!isSelectOrUnSelect){
            if(selectImgInfoList.size() >= enableSelectCount){
                String text = getString(R.string.msg_amount_limit);
                showPromptDialog(String.format(text, enableSelectCount));
                return;
            }
            selectImgInfoList.add(imgInfoList.get(lastPosition));
            selectImg.setBackgroundResource(R.mipmap.icon_check_goods_select);
        }
        if(selectImgInfoList.size() == 0){
            rightTextBtn.setText(MultiImageSelectorActivity.getBntNameByFrom(whichActFrom));
        }else{
            rightTextBtn.setText((MultiImageSelectorActivity.getBntNameByFrom(whichActFrom) + "(")+selectImgInfoList.size() + "/"+enableSelectCount+")");
        }
        
    }

    private void showPromptDialog(String info) {

        LPDialogFactory.Builder builder = new LPDialogFactory.Builder(this)
                .setContent(info)
                .setRightText("知道了")
                .setListener(new LPDialogFactory.OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.build().show();
    }

    @Override
    public void onBackPressed() {
        if(!isTakePhoto){
            finishSelectByBack();
        }
        super.onBackPressed();
    }
    private String returnFileSize(String size){
        String finalSize = size;
        int sizeInt = Integer.parseInt(size);
        if(sizeInt < 1024){
            finalSize = new DecimalFormat("#0.00").format((double)sizeInt / 1024)+"K";
        }else if(sizeInt < 1024000){
            finalSize =  new DecimalFormat("#0").format(sizeInt / 1024) + "K";
        }else{
            finalSize = new DecimalFormat("#0.00").format((double)sizeInt / 1048576)+"M";
        }
        return finalSize;
    }
    private void completeSelect(){
        if(selectImgInfoList.isEmpty()){
            selectOrUnSelectPicInfo();
        }
        Intent intent = new Intent();
        ArrayList<String> arrayList = new ArrayList<String>();
        for(Image image : selectImgInfoList){
            arrayList.add(image.path);
        }
        intent.putExtra(EXTRA_SELECT_PICINFO_LIST, selectImgInfoList);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_PERFORM_ADD_PIC_URLS, arrayList);
        setResult(RESULT_OK, intent);
        finish();
    }
    private void finishSelectByBack(){
        Intent intent = new Intent();
        ArrayList<String> arrayList = new ArrayList<String>();
        for(Image image : selectImgInfoList){
            arrayList.add(image.path);
        }
        intent.putExtra(EXTRA_SELECT_PICINFO_LIST, selectImgInfoList);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_PERFORM_ADD_PIC_URLS, arrayList);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
