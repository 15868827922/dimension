package com.shenjing.dimension.dimension.base.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.shenjing.dimension.dimension.main.LPApplicationLike;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Pattern;


public class Utils {

    public static final long KB=1024;
//    public static final int BUSINESS_SHOW_NOTIFICATION_ID = 80001;
    public static final long MB=KB*KB;
    public static final long GB=MB*MB;
    static final Pattern PATTERN_NUMBER_LETTER = Pattern.compile("^[A-Za-z0-9]+$");
    public static final Pattern AT_MEMBER_PATTERN = Pattern.compile("@\\[[^\\]]+\\]\\s{0,1}", Pattern.CASE_INSENSITIVE); //正则表达式比配字符串里是否含有@信息，如：@[18868823930]
    private static final int ONE_MINUTE = 60 * 1000;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final int ONE_DAY = 24 * ONE_HOUR;
    private static final int TWO_DAY = 2 * ONE_DAY;
    private static final long ONE_MONTH = 30 * (long)ONE_DAY;
    private static final String SEP1 = "#";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";
    /*public static boolean isAppRunning() {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) LPApplicationLike.getContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        if(list == null || list.size() == 0){
            return false;
        }
        for (RunningTaskInfo info : list) {
            String packageName = LPApplicationLike.getContext().getPackageName();
            if (info.topActivity.getClassName().indexOf(packageName) >= 0) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }*/

    //--------------Keyboard相关------------
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        try{
            if (context.getCurrentFocus() != null && context.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }else{
                imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }catch(Exception e){};
    }

    public static void hideKeyboard(Dialog dialog) {

        InputMethodManager imm = (InputMethodManager) dialog.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        Window window=dialog.getWindow();
        try{
            if (window.getCurrentFocus() != null && window.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(window.getCurrentFocus().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }else{
                imm.hideSoftInputFromWindow(window.getDecorView().getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }catch(Exception e){};
    }

    public static void showKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //--------------Keyboard相关------------

    //--------------dp转px------------
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
    }
    public static int dp2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    //--------------dp转px------------

    public static void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    //--------------通用对话框------------
    /*public static Dialog showFolderListDialog(final Context context, FolderAdapter mFolderAdapter, OnItemClickListener listener, CharSequence categoryText){
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_multi_image_folder);
        android.view.WindowManager.LayoutParams windowLp = dialog.getWindow().getAttributes();
        windowLp.width = (int)(DeviceInfo.getScreenWidth(context));
        dialog.getWindow().setAttributes(windowLp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.dialog_animation_300);

        ListView folderList = (ListView) dialog.findViewById(R.id.folderList);
        TextView category_btn = (TextView) dialog.findViewById(R.id.category_btn);
        category_btn.setText(categoryText);
        folderList.setAdapter(mFolderAdapter);
        int index = mFolderAdapter.getSelectIndex();
        index = index == 0 ? index : index - 1;
        folderList.setSelection(index);
        folderList.setOnItemClickListener(listener);
        category_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
    
    public static Dialog showSpinnerSelecterDialog(Context context, ArrayList<String> options, int selectedPosition,
            OnClickListener listener, LoopListener loopListener){
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_selector_list);
        android.view.WindowManager.LayoutParams windowLp = dialog.getWindow().getAttributes();
        windowLp.width = (int)(DeviceInfo.getScreenWidth(context));
        dialog.getWindow().setAttributes(windowLp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.dialog_animation);

        LoopView loopView = (LoopView) dialog.findViewById(R.id.loopView);
        dialog.findViewById(R.id.textCancel).setOnClickListener(listener);
        dialog.findViewById(R.id.textOK).setOnClickListener(listener);

        loopView.setListener(loopListener);
        //设置原始数据
        loopView.setList(options);
        //设置初始位置
        loopView.setPosition(selectedPosition);
        //设置字体大小
        loopView.setTextSize(22);

        dialog.show();
        return dialog;
    }
    
    public static Dialog showMenuDialog(final Context context, String[] menuStrs, OnMenuItemClickListener onMenuItemClickListener ){
        Dialog menuDialog = MenuDialog.createDialog(context, null, menuStrs, onMenuItemClickListener);
        menuDialog.show();
        return menuDialog;
    }*/
    //--------------通用对话框------------

    //--------------TextWatcher相关------------
    public static TextWatcher setInputPriceLimit(final EditText editText, final double maxValue) {
        return setDicimalAccuracyPriceLimit(2, editText, maxValue);
    }

    public static TextWatcher setDicimalAccuracyPriceLimit(final int decimalAccuracy, final EditText editText, final double maxValue) {
        if(editText == null){
            return null;
        }

        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > decimalAccuracy) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + decimalAccuracy+1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double inputValue = 0;
                try{
                    inputValue = Double.parseDouble(s.toString());
                }catch(Exception e){}
                if(inputValue > maxValue){
                    NumberFormat df = (NumberFormat) NumberFormat.getInstance();
                    df.setGroupingUsed(false);
                    df.setMaximumFractionDigits(decimalAccuracy);
                    String maxValueStr = df.format(maxValue);
                    editText.setText(maxValueStr);
                    editText.setSelection(maxValueStr.length());
                }
            }

        };
        editText.addTextChangedListener(textWatcher);
        return textWatcher;
    }
    public static TextWatcher setDicimalAccuracyPriceLimit(final int decimalAccuracy, final EditText editText, final double maxValue, final String toast) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    double price = Double.parseDouble(s.toString());
                    if (price > maxValue && maxValue >= 0) {
                        if (!TextUtils.isEmpty(toast)) {
                            Toast.makeText(editText.getContext(), toast, Toast.LENGTH_SHORT).show();
                        }


                        editText.setText(String.format("%." + decimalAccuracy + "f",maxValue) + "");
                        editText.setSelection(editText.getText().length());

                    }
                }
                String value[] = editText.getText().toString().split("\\.");
                if (value.length == 2 && value[1].length() > decimalAccuracy) {
                    editText.setText(value[0] + "." + value[1].substring(0,decimalAccuracy));
                    editText.setSelection(editText.getText().length());
                }
            }
        };
        editText.addTextChangedListener(textWatcher);
        return textWatcher;
    }
   //--------------TextWatcher相关------------

    //检测应手机中是否安装某应用(美团，百度地图，等)
    public static boolean isAppInstalled(Context context, String packageName){
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    //计算分页
    /**
    * @Title: setCurPageFromOne
    * @Description: (curPage从1开始)
    * @param offset
    * @param length
    * @return int    返回类型
    * @author Baojiang Yang
    * @date 2015-9-15 下午2:41:03
    */
    public static int setCurPageFromOne(int offset, int length) {
        return offset / length + 1;
    }

    public static String getCurrentActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        // get the info from the currently running task
        List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName();
    }

    //获取某控件在手机中的绝对位置
    public static Point getLocationOnWindow(View view){
        int[] loc=new int[2];
        view.getLocationInWindow(loc);
        return new Point(loc[0],loc[1]);
    }

    //计算相加多个dimen
    /**
     * 计算相加多个dimen
     * @param context
     * @param dimens
     * @return
     */
    public static int calculateDimens(Context context, int...dimens){
        Resources res=context.getResources();
        int result=0;
        for(int id:dimens){
            result=result+res.getDimensionPixelSize(id);
        }
        return result;
    }

    //用于判断app是否在前台运行
    /**
     * 用于判断app是否在前台运行
     * @return true 表示是前台  false 表示不在前台
     */
    public static boolean isAppForeground() {
        ActivityManager am = (ActivityManager) LPApplicationLike.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!TextUtils.isEmpty(topActivity.getPackageName()) &&
                    topActivity.getPackageName().equals(LPApplicationLike.getContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }
/*
    //用于判断手机是否锁屏
    *//**
     * 用于判断手机是否锁屏
     * @return true 表示是锁屏  false 表示没有锁屏【
     *//*
    public static boolean isScreenOn(){
        KeyguardManager mkeyguardManager = (KeyguardManager) LPApplicationLike.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        if(mkeyguardManager.inKeyguardRestrictedInputMode()){
            return false;
        }else{
            return true;
        }
    }
*/
    /**
     * 判断广播是否已注册
     * @param intent
     * @return
     */
    public static boolean isBroadCastRegistered(Context context, Intent intent){
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryBroadcastReceivers(intent, 0);
        if(resolveInfos != null && !resolveInfos.isEmpty()){
            //查询到相应的BroadcastReceiver
            return true;
        }
        return false;
    }

    //-----------------Web相关------------------
    /*public static void extractUrl2Link(TextView v) {
//      Pattern wikiWordMatcher = Pattern.compile("(((http\\:\\/\\/)|(https\\:\\/\\/)|(www\\.))[a-zA-Z0-9_\\.]+)");
        String mentionsScheme = String.format("%s/?%s=", WebViewActivity.SCHEMA, WebViewActivity.PARAM_UID);
        String check = "(((http|https)://)|(www.))(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        Pattern p = Pattern.compile(check);
        Linkify.addLinks(v, p, mentionsScheme);
    }
    public static void synCookieForOwnerUrl(){
        LPLog.print(Utils.class, "synCookieForOwnerUrl");
        UserInfo userInfo = LPApplicationLike.getInstance().getUserInfo();
        if (userInfo != null) {
            String castgc = userInfo.getCastgc();
            if (!TextUtils.isEmpty(castgc)) {
                List<String> tmp = new ArrayList<String>();
                tmp.addAll(Arrays.asList(URLManager.slFamilyUrls));
                tmp.add(DebugUtils.getCurrHost());
                CookieUtils.synCookie(LPApplicationLike.getContext(), tmp,
                        Constant.COOKIE_KEY_CASTGC + "=" + castgc,
                        Constant.COOKIE_KEY_SESSION + "=" + LPApplicationLike.getSessionId());
            }
        }
    }*/
    //-----------------Web相关------------------

}
