package com.shenjing.dimension.dimension.base.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Toast;


import com.shenjing.dimension.dimension.main.LPApplicationLike;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author caiyiping
 * time: 12.15
 * Activity相关的工具方法
 *
 */
public class ActivityUtil {

    public static void gotoActivity(Context context, Class<? extends Activity> activityClass, Bundle extras) {
        if(context == null || activityClass == null){
            return;
        }
        Intent intent = new Intent(context, activityClass);
        if(extras != null){
            intent.putExtras(extras);
        }
        if(context instanceof Activity){
            Utils.hideKeyboard((Activity)context);
        }
        if (!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
    
    public static void gotoActivity(Context context, Class<? extends Activity> activityClass, Bundle extras, int flags) {
        if(context == null || activityClass == null){
            return;
        }
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(flags);
        if(extras != null){
            intent.putExtras(extras);
        }
        if(context instanceof Activity){
            Utils.hideKeyboard((Activity)context);
        }
        context.startActivity(intent);
    }
    
    public static void gotoActivity(Context context, Class<? extends Activity> activityClass){
        gotoActivity(context, activityClass, null);
    }

    public static void gotoActivityForResult(Activity fromActivity, Class<? extends Activity> activityClass, Bundle extras, int requestCode){
        if(fromActivity == null || activityClass == null){
            return;
        }
        Intent intent = new Intent(fromActivity, activityClass);
        if(extras != null){
            intent.putExtras(extras);
        }
        Utils.hideKeyboard(fromActivity);
        fromActivity.startActivityForResult(intent, requestCode);
    }
    
    public static void gotoSystemImageBrowser(Context context, String imagePath){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        try{
            context.startActivity(intent);
        }catch(Exception e){
            Toast.makeText(context, "你的设备好像不支持查看图片", Toast.LENGTH_SHORT).show();
        }
    }

    /*public static void gotoBrowser(Context context, String url){
        if(TextUtils.isEmpty(url) || !URLManager.isBrowserOkUrl(url)){
            Toast.makeText(context, "无效的链接地址", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try{
            context.startActivityForResult(intent);
        }catch(Exception e){
            Toast.makeText(context, "你的设备好像不支持跳转网页", Toast.LENGTH_SHORT).show();
        }

    }*/

    public static void gotoDialPlate(Context context, String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        try{
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            if(context != null){
                Toast.makeText(context, "你的设备好像不支持拨打电话", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 添加新联系人
     * @param context
     * @param name
     * @param number
     */
    public static void gotoAddNewContact(Activity context, String name, String number){
        Intent addIntent = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts"));
        addIntent.setType("vnd.android.cursor.dir/person");
        addIntent.setType("vnd.android.cursor.dir/contact");
        addIntent.setType("vnd.android.cursor.dir/raw_contact");
        addIntent.putExtra(ContactsContract.Intents.Insert.NAME,name);
        addIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        context.startActivity(addIntent);
    }

    /**
     * 添加到已有联系人
     * @param context
     * @param number
     */
    public static void gotoEditContact(Activity context, String number){
        Intent oldConstantIntent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        oldConstantIntent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        oldConstantIntent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, 3);
        if(oldConstantIntent.resolveActivity(context.getPackageManager()) != null){
            context.startActivity(oldConstantIntent);
        }else{
            if(context != null){
                Toast.makeText(context, "你的设备好像不支持编辑联系人", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void gotoNewSMS(Context context, String phone, String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
        intent.putExtra("sms_body", message);
        try{
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            if(context != null){
                Toast.makeText(context, "你的设备好像不支持发送短信", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void pickImage(Activity activity, File toFile, int width, int height, int reqCode){
        Intent in = new Intent("android.intent.action.CROP");
        in.setDataAndType(Uri.fromFile(toFile), "image/*");
        in.putExtra("aspectX", width);// 裁剪框比例
        in.putExtra("aspectY", height);
        in.putExtra("outputX", width);// 输出图片大小
        in.putExtra("outputY", height);
        in.putExtra("return-data", false);
        in.putExtra("output", Uri.fromFile(toFile));
        try{
            activity.startActivityForResult(in, reqCode);
        }catch(Exception e){
            Toast.makeText(activity, "你的设备好像不支持选取图片", Toast.LENGTH_SHORT).show();
        }

    }

/*    public static void gotoMapNavi(final Context context, final Location toBaiduLocation, final String toAddress, final Location fromBaiduLocation){
        final Location gcjLocation = LocationUtils.convertBD09ToGCJ02(toBaiduLocation);
        final String itemBaidu = "百度地图";
        final String itemAmap = "高德地图";
        final String itemTencent = "腾讯地图";
        final String itemGoogleWeb = "谷歌地图(网页)";
        final List<String> itemList = new ArrayList<String>();
        if(Utils.isAppInstalled(context, "com.baidu.BaiduMap")){
            itemList.add(itemBaidu);
        }
        if(Utils.isAppInstalled(context, "com.autonavi.minimap")){
            itemList.add(itemAmap);
        }
        if(Utils.isAppInstalled(context, "com.tencent.map")){
            itemList.add(itemTencent);
        }
        itemList.add(itemGoogleWeb);
        String[] items = new String[itemList.size()];
        itemList.toArray(items);

        Dialog dialog = MenuDialog.createDialog(context, "使用以下方式找到路线", items, new MenuDialog.OnMenuItemClickListener() {

            @Override
            public void onMenuItemClick(int position) {
                try{
                    String item = itemList.get(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if(item.equals(itemBaidu)){
                        intent.setData(Uri.parse("bdapp://map/direction?destination=" + toBaiduLocation.getLatitude() + "," + toBaiduLocation.getLongitude()*//* + "|name:" + toAddress*//* + "&mode=driving&coord_type=bd09ll"));
                        context.startActivity(intent);
                    }else if(item.equals(itemAmap)){
                        String fromCoord = "";
                        Location fromGcjLocation;
                        if(fromBaiduLocation == null){
                            LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            fromGcjLocation = LocationUtils.convertWGS84ToGCJ02(new Location(location.getLatitude(), location.getLongitude()));
                        }else{
                            fromGcjLocation = LocationUtils.convertBD09ToGCJ02(fromBaiduLocation);
                        }
                        fromCoord = fromGcjLocation.getLatitude() + "," + fromGcjLocation.getLongitude();
                        intent.setData(Uri.parse("wechatnav://type=nav&fromcoord=" + fromCoord + "&tocoord=" + gcjLocation.getLatitude() + "," + gcjLocation.getLongitude() + "&to=" + toAddress));
                        context.startActivity(intent);
                    }else if(item.equals(itemTencent)){
                        String fromCoord = "";
                        Location fromGcjLocation;
                        if(fromBaiduLocation == null){
                            LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            fromGcjLocation = LocationUtils.convertWGS84ToGCJ02(new Location(location.getLatitude(), location.getLongitude()));
                        }else{
                            fromGcjLocation = LocationUtils.convertBD09ToGCJ02(fromBaiduLocation);
                        }
                        fromCoord = fromGcjLocation.getLongitude() + "," + fromGcjLocation.getLatitude();
                        intent.setData(Uri.parse("sosomap://type=nav&fromcoord=" + fromCoord + "&tocoord=" + gcjLocation.getLongitude() + "," + gcjLocation.getLatitude() + "&from=我的位置&to=" + toAddress));
//                        intent.setData(Uri.parse("sosomap://type=nav&fromcoord=120.208687,30.249088&tocoord=120.209213,30.247358&from=我的位置&to=浙江省杭州市江干去学源街e9909"));
                        context.startActivity(intent);
                    }*//*else if(item.equals(itemGoogleWeb)){
                        WebViewActivity.startActivityForResult(context, null, "http://www.google.cn/maps/dir//" + toAddress + "/@" + gcjLocation.getLatitude() + "," + gcjLocation.getLongitude() + ",13z", false);
                    }*//*
                }catch (Exception e){
                    Toast.makeText(context, "未找到可用的地图客户端，使用谷歌地图(网页)为你导航", Toast.LENGTH_LONG).show();
//                    WebViewActivity.startActivityForResult(context, null, "http://www.google.cn/maps/dir//" + toAddress, true);
                }
            }
        });
        dialog.show();
//        act=android.intent.action.VIEW dat=sosomap://type=nav&fromcoord=120.208687,30.249088&tocoord=120.209213,30.247358&from=杭州市江干区钱江路&to=浙江省杭州市江干区钱江路&referer=wx_client pkg=com.tencent.map cmp=com.tencent.map/.WelcomeActivity
//        act=android.intent.action.VIEW dat=wechatnav://type=nav&fromcoord=30.247377,120.209404&tocoord=30.247358,120.209213&from=杭州市江干区解放东路18号&to=浙江省杭州市江干区钱江路 pkg=com.autonavi.minimap cmp=com.autonavi.minimap/com.autonavi.map.activity.NewMapActivity

    }*/

    public static boolean isTopActivity(Activity activity){
        if(activity == null){
            return false;
        }
        boolean isTop = false;
        ActivityManager am = (ActivityManager)activity.getSystemService(Activity.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (cn.getClassName().equals(activity.getClass().getName())){
            isTop = true;
        }
        return isTop;
    }

    public static int getVersionCode(){
        int versionCode = 0;
        Context context = LPApplicationLike.getContext();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static int getVersionCode(Context context){
        int versionCode = 0;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static String getVersionName(){
        String versionName = "";
        Context context = LPApplicationLike.getContext();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 判断某个Activity是否在堆栈中
     * @param activityName
     * @return
     */
  /*  public static boolean isPageOnStack(String activityName){
        if(TextUtils.isEmpty(activityName)){
            return false;
        }
        try {
            Object activities = ReflectUtils.reflect(null, "android.app.ActivityThread#currentActivityThread().mActivities");
            if(activities != null){
                List<Activity> list = new ArrayList<Activity>();
                if (activities instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<Object, Object> arrayMap = (Map<Object, Object>) activities;
                    for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
                        Object value = entry.getValue();
                        Object o = ReflectUtils.reflect(value, "activity");
                        list.add((Activity) o);
                    }
                }
                if(list.size() > 0){
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getComponentName().getClassName().contains(activityName)){
                            return true;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }*/

}
