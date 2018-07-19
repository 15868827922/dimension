package com.shenjing.dimension.dimension.base.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.shenjing.dimension.MainActivity;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.util.PermissionManager;
import com.shenjing.dimension.dimension.base.util.StatusBarUtils;
import com.shenjing.dimension.dimension.base.util.Utils;

import java.util.List;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.SwipeBackUtils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 所有的Activity类必须继承于该类，APP基础类，比BaseActivity再深入一层
 * Created by LJ on 2016/7/12.
 */
public class FundmentalActivity extends AppCompatActivity implements SwipeBackActivityBase {

    /**
     * 从左往右滑可引起返回操作的最小距离
     * */
    private static final int MIN_LENGTH_TO_FLING_BACK = 100;
    /**
     * 从左往右滑动引起返回操作的最小速率
     * 创建者:little
     * 创建时间:2017/2/24 0024 9:16
     */
    private static final int MIN_VELOCITY_X_TO_FLING_BACK = 50;


    protected Context mContext;
    private Toast toast;
//    private Request reqUploadMsg;
/*    protected long mPageResumeTime;
    protected long mPagePauseTime;
    private static long appResumeTime;*/

//    protected GestureDetector detector;
    /**
     * 用于判断是否需要从左往右滑动处理，当listener和viewpager都为空时，是唯一判断条件
     * 创建者:little
     * 创建时间:2017/2/24 0024 9:27
     */
    protected boolean isAbleFlingBack = false;

    /**
     * 用于自定义处理从左往右滑动事件监听
     * 创建者:little
     * 创建时间:2017/2/24 0024 9:25
     */
    protected FlingBackListener flingBackListener;
    /**
     * 用于页面内含有viewpager控件的滑动处理
     * 创建者:little
     * 创建时间:2017/2/24 0024 9:17
     */
    protected ViewPager baseViewPager;
    private View meizuStatusBarContainer;

    protected boolean isNeedStatusTrans = true;//是否去除系统状态栏,当系统状态栏存在的时候，我们自定义的状态栏则不能显示


    private SwipeBackActivityHelper mHelper;
    private SwipeBackLayout swipeBackLayout;

//    protected PageRequestManager prm; // 请求管理器

//    protected List<Request> requests;
    protected Handler pageEventHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            try {
                onMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void onMessage(Message msg){};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* RtEnv.put(RtEnv.KEY_CURR_ACT,this);
        RtEnv.put(RtEnv.KEY_CURR_PAGR_HANDLER,pageEventHandler);
        prm = new PageRequestManager();*/
        if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isNeedStatusTrans) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        super.setContentView(R.layout.page_fundmental);

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        swipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                Utils.hideKeyboard(FundmentalActivity.this);
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
            }

            @Override
            public void onScrollOverThreshold() {
            }
        });

        /**
         * 应用中StatusBar在5.0以下和5.0以上采用了两种不同的方式，由于在5.0以上StatusBar绘制在我们的视图上
         * 所以5.0以上我们在布局中指定了一个View来绘制StatusBar - meizuStatusBarContainer，在5.0以下我们隐藏这个View
         *
         * 如果继承BaseActivity，BaseActivity中已经处理好StatusBar状态，无需另外处理
         * 如果继承FundmentalActivity，一般调用setStatusBarContainerVisibility(false)方法来隐藏基类布局中的View
         *      让StatusBar绘制在子类的布局上，并且可以通过StatusBarUtils.setStatusBarVisibility(this, true)
         *      来控制StatusBar是否显示
         */
        meizuStatusBarContainer = findViewById(R.id.meizuStatusBarContainer);
        if (!isNeedStatusTrans || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            meizuStatusBarContainer.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) meizuStatusBarContainer.getLayoutParams();
            params.height = StatusBarUtils.getStatusBarHeight(this);
            meizuStatusBarContainer.setLayoutParams(params);
        }
        //当系统状态栏不存在时，将布局往上顶
        if (isNeedStatusTrans){
            View baseContainerWithoutStatusBar = findViewById(R.id.baseContainerWithoutStatusBar);
            StatusBarUtils.fitSystemWindow(baseContainerWithoutStatusBar);
        }

        mContext = this;

        /** edit  by liht on 2017/2/28 下面两个条件共同决定了设备处于被回收 16:36 */
      /*  if (LPApplicationLike.getInstance().isRecycled){
            if (!(this instanceof SplashActivity)){
                LPApplicationLike.getInstance().isRecycled = false;
                LPApplicationLike.getInstance().setUserInfo(new UserInfo());
                LPApplicationLike.getInstance().setCardInfo(new CardInfo());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SplashActivity.startActivityForResult(mContext, 1000);
                        finish();
                        overridePendingTransition(R.anim.alpha_show_very_fast, R.anim.alpha_keep_show_100);
                    }
                }, 10);

            }
        }*/

        swipeBackLayout.setEnableGesture(!isTaskRoot() && !isActivityDisableBackForLeft());
    }


    private boolean isActivityDisableBackForLeft(){
        if(this instanceof MainActivity ){
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    public void setNeedStatusTrans(boolean isNeedStatusTrans){
        this.isNeedStatusTrans = isNeedStatusTrans;
    }

    /**
     * 控制statusBarContainer视图显示与否
     * （不是控制statusBar的显示与否，其在StatusBarUtils中设置）
     * @param visibility
     */
    public void setStatusBarContainerVisibility(boolean visibility){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            findViewById(R.id.meizuStatusBarContainer).setVisibility(visibility ? View.VISIBLE : View.GONE);
        }
    }

    protected void setStatusBgResource(int resId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            findViewById(R.id.meizuStatusBarContainer).setBackgroundResource(resId);
        }
    }

    protected void setStatusBackGroundColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            findViewById(R.id.meizuStatusBarContainer).setBackgroundColor(color);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, (ViewGroup)findViewById(R.id.baseContainerWithoutStatusBar), true);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        FrameLayout mainContainer = (FrameLayout)findViewById(R.id.baseContainerWithoutStatusBar);
        if(mainContainer.getChildCount() > 0){
            mainContainer.removeAllViews();
        }
        if(params == null){
            mainContainer.addView(view);
        }else{
            mainContainer.addView(view, params);
        }
    }


    @Deprecated
    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Deprecated
    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* RtEnv.put(RtEnv.KEY_CURR_ACT,this);
        RtEnv.put(RtEnv.KEY_CURR_PAGR_HANDLER,pageEventHandler);
//        mPageResumeTime = StsUtil.currentTimeMillis();
        MobclickAgent.onResume(this);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mPagePauseTime = StsUtil.currentTimeMillis();
//        MobclickAgent.onPause(this);

    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putSerializable(SAVED_USERINFO, LPApplicationLike.getInstance().getUserInfo());
//        outState.putSerializable(SAVED_CARDINFO, LPApplicationLike.getInstance().getCardInfo());
//        outState.putSerializable(SAVED_CONFIGURATION, LPApplicationLike.getInstance().getConfiguration());
//        outState.putSerializable(SAVED_CHATUSERLIST, (Serializable) LPApplicationLike.getInstance().getChatUserList());
//        outState.putSerializable(SAVED_STATE_MANAGER, (Serializable) StateManager.getInstance());
//        outState.putString(SAVED_ServerURL_Images_Production, URLManager.ServerURL_Images_Production);
//        outState.putString(SAVED_IM_SERVER_ADDRESS, IMConenction.imServerAddress);
//        outState.putInt(SAVED_IM_SERVER_PORT, IMConenction.imServerPort);
//        outState.putString(SAVED_IM_SERVER_NAME, IMConenction.imServerName);
//        outState.putString(SAVED_IM_DEFAULT_PASSWORD, IMConenction.imDefaultPassword);
    }

    /**
     * 请求权限回调
     * Created by LJ on 2016/7/12.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (this instanceof PermissionManager.OnPermissonRequestListner){
            PermissionManager.OnPermissonRequestListner listner = (PermissionManager.OnPermissonRequestListner) this;
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listner.onPermissonRequestSucceed(requestCode);
            }else{
                listner.onPermissonRequestFailed(requestCode);
            }
        }
    }

    public void toast(String text){
        toast(text, Toast.LENGTH_SHORT);
    }

    protected void toast(String text, int duration){
        if (toast == null){
            toast = Toast.makeText(mContext, text, duration);
        }else{
            toast.setText(text);
        }
        toast.show();
    }

    protected void toast(int resstr){
        if (toast == null){
            toast = Toast.makeText(mContext, resstr, Toast.LENGTH_SHORT);
        }else{
            toast.setText(resstr);
        }
        toast.show();
    }

    /**
     * 判断APP当前是否在前台运行
     * Created by LJ on 2016/7/12.
     */
    private boolean isAppForeground(){
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable) ;
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.hideKeyboard(FundmentalActivity.this);
        SwipeBackUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1 == null || e2 == null){
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            float xScrollLength = e2.getRawX() - e1.getRawX();
            float yScrollLength = e2.getRawY() - e1.getRawY();

            //当listener为空，viewpager为空时，由isAbleFlingBack控制是否可滑动
            boolean isAbleFlingBackNormal = (isAbleFlingBack && flingBackListener == null && baseViewPager == null);
            //当页面内含有viewpager并且已经通过setBaseViewPager方法设置，然后viewpager滑动到第一页时可以滑动返回
            boolean isAbleFlingBackWithViewPager = (baseViewPager != null && baseViewPager.getCurrentItem() == 0);
            //当页面设置了listener，由listener去控制可否滑动
            boolean isAbleFlingBackSelf = (flingBackListener != null && flingBackListener.isAbleFling());

            if (isAbleFlingBackNormal || isAbleFlingBackWithViewPager || isAbleFlingBackSelf) {
                //当从左往右滑动距离大于最小距离，且绝对速率大于最小所需速率，并且左右滑动距离大于上下滑动距离，
                // 并且不是栈顶activity以及不是mainpageactivity这个页面时可以满足从左往右滑动
                if (xScrollLength > MIN_LENGTH_TO_FLING_BACK && Math.abs(velocityX) > MIN_VELOCITY_X_TO_FLING_BACK
                        && Math.abs(xScrollLength) > Math.abs(yScrollLength) && !isTaskRoot()
                        && !(FundmentalActivity.this instanceof MainActivity)) {
                    //当页面使用了自定义事件，则从左往右滑动事件由页面内操作决定
                    if(isAbleFlingBackSelf){
                        flingBackListener.flingOperate();
                        return true;
                    }else{
                        flingBackWithFinish();
                        return true;
                    }
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    protected void setFlingBackListener(FlingBackListener flingBackListener){
        this.flingBackListener = flingBackListener;
    }

    protected void setBaseViewPager(ViewPager baseViewPager){
        this.baseViewPager = baseViewPager;
    }

    /**
     * 用于自定义页面滑动事件
     * 创建者:little
     * 创建时间:2017/2/24 0024 9:35
     */
    public interface FlingBackListener {
        /**
         * 当页面选择自定义滑动事件时，可通过该返回值控制是否有滑动事件
         */
        boolean isAbleFling();
        /**
         * 页面自定义滑动事件处理
         */
        void flingOperate();
    }
    /**
     * 滑动返回上一个页面的默认操作
     * 创建者:little
     * 创建时间:2017/2/24 0024 9:34
     */

    protected void flingBackWithFinish(){
        Utils.hideKeyboard(FundmentalActivity.this);
        FundmentalActivity.this.finish();
    }

    /**
     * 设置页面是否可以滑动，仅当listener和viewpager为空时可以通过该变量进行控制
     * 创建者:little
     * 创建时间:2017/2/24 0024 9:33
     */
    protected void setAbleFlingBack(boolean isAbleFlingBack){
        this.isAbleFlingBack = isAbleFlingBack;
    }

  /*  public PageRequestManager getPrm() {
        return prm;
    }*/

    /**
     * 在隐藏或者展示StatusBar的时候调用这个方法，适配5.0以下的手机
     */
    public void setStatusBarState(boolean isShow){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) meizuStatusBarContainer.getLayoutParams();
            params.height = StatusBarUtils.getStatusBarHeight(this);
            meizuStatusBarContainer.setLayoutParams(params);
            if(isShow){
                meizuStatusBarContainer.setVisibility(View.VISIBLE);
            }else{
                meizuStatusBarContainer.setVisibility(View.GONE);
            }
        }
    }


}
