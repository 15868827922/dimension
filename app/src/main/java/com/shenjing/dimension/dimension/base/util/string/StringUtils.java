package com.shenjing.dimension.dimension.base.util.string;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.shenjing.dimension.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/28.
 */

public class StringUtils {

    static final Pattern PATTERN_NUMBER_LETTER = Pattern.compile("^[A-Za-z0-9]+$");



    public static String getString (Integer integer) {

        return integer == null ? "" : integer + "" ;
    }
    public static String getString (String string) {

        return TextUtils.isEmpty(string) ? "" : string;
    }

    public static void copyToClipBoard(Context context, String text){
        if(TextUtils.isEmpty(text) || context == null){
            return;
        }
        ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(text);
        Toast.makeText(context, R.string.copy_tp_clip_finished, Toast.LENGTH_SHORT).show();
    }

    public static String getDistanceString (Integer m) {
        if (m == null) {
            m = 0;
        }
        if (Math.abs(m) >= 100000) {//大于一百公里
            return "100公里外";
        } else if (Math.abs(m) >= 1000) {//大于等于1公里
            String result = "";
            result += m /1000;
            int ft = (m % 1000) / 100;
            if (ft != 0) {
                result += "." + ft;
            }
            return result + "公里";
        }

        return m + "米";
    }


    /*** *号指定替换，字符串
     * @param text
     * @param startIndex 起始位置 从0开始
     * @param length     替换长度
     * @return
     */
    public static String getHiddenText(String text, int startIndex, int length){
        if(TextUtils.isEmpty(text)){
            return text;
        }
        if(text.length() < startIndex || text.length() < (startIndex + length)){
            return text;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++){
            sb.append("*");
        }
        return text.substring(0, startIndex) + sb.toString() + text.substring(startIndex + length);
    }


    //--------------字符校验相关------------
    /**
     * 字符检验
     * 当string中有除  中文  数字  字母以外的字符时  返回false
     * @param string
     * @return
     */
    public static boolean checkInput (String string) {

        if (!TextUtils.isEmpty(string)) {
            String temp = string.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
            return temp.length() == string.length();
        }

        return true;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String text){
        return TextUtils.isEmpty(text)|| TextUtils.isEmpty(text.replace(" ",""));
    }

    public static boolean isEmptyOrZero(String value){
        return (TextUtils.isEmpty(value)||value.equals("0"));
    }

    /**
     * 判断是否只包含数字和字母
     * @param text
     * @return
     */
    public static boolean isNumberAndLetter(String text){
        return PATTERN_NUMBER_LETTER.matcher(text).matches();
    }
    //判断是否是国际号码
    public static boolean isInternalPhoneNumber(String text){
        if(TextUtils.isEmpty(text)){
            return false;
        }
        return text.matches("\\d{1,}-\\d{1,}");
    }

    public static boolean isPhoneNumber(String text){
        if(TextUtils.isEmpty(text) || text.length() != 11){
            return false;
        }
        return text.matches("^[1][3,4,5,7,8][0-9]{9}$");
    }

    //判断是普通账号或者国际号码
    public static boolean isLPUserName(String text){
        return isPhoneNumber(text) || isInternalPhoneNumber(text);
    }

    public static String getHiddenPhone(String originalPhone){
        if(TextUtils.isEmpty(originalPhone)){
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        int firstShowCount = 0;
        int lastShowCount = 0;
        if(originalPhone.length() >= 11){
            firstShowCount = 3;
            lastShowCount = 3;
        }else if(originalPhone.length() >= 8){
            firstShowCount = 2;
            lastShowCount = 3;
        }else if(originalPhone.length() >= 6){
            firstShowCount = 1;
            lastShowCount = 2;
        }else if(originalPhone.length() >= 4){
            firstShowCount = 1;
            lastShowCount = 1;
        }else if(originalPhone.length() >= 3){
            lastShowCount = 1;
        }
        for(int i = 0; i < originalPhone.length() - firstShowCount - lastShowCount; i ++){
            sb.append("*");
        }
        return originalPhone.substring(0, firstShowCount) + sb.toString() + originalPhone.substring(originalPhone.length() - lastShowCount, originalPhone.length());
    }

    //号码隐位规则
    /**
     * 国内手机号码和国际号码隐藏(针对于“-”后边)
     * @param cell
     * @return
     */
    public static String getPhoneNumHiddenText(String cell){
        if (TextUtils.isEmpty(cell)) {
            return "";
        }
        //11位正常的国内手机号码
        if (cell.length() == 11) {
            return StringUtils.getHiddenText(cell, 3, 4);
        }else if (cell.length()<6) {
            //1-5 位直接返回
            return cell;
        }else{
            //6-10位  前3位显示，第4-6位隐藏，后面都显示
            StringBuffer sBuffer=new StringBuffer(cell.substring(0,3));
            sBuffer.append("***").append(cell.substring(6, cell.length()));
            return sBuffer.toString();
        }
    }
   //--------------电话号码相关----------------

    //--------------身份证相关----------------
    /**
     *  根据身份证号判断是否成年 不检查身份证是否为空
     * @param idCard
     * @param age
     * @return
     */
    public static boolean checkIDCardAdult(String idCard, int age){
        String date = "";
        if(idCard.length() == 15){
            date = "19" + idCard.substring(6, 12);
        }else if(idCard.length() == 18){
            date = idCard.substring(6, 14);
        }

        Calendar birth = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        try {
            Date d = new SimpleDateFormat("yyyyMMdd").parse(date);
            birth.setTimeInMillis(d.getTime());
            birth.add(Calendar.YEAR, age);
            return now.after(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    //--------------身份证相关----------------

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0 ; i < length; ++i){
            int number = random.nextInt(str.length());//[0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isNumber(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
