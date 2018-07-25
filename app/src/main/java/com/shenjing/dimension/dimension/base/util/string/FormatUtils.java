package com.shenjing.dimension.dimension.base.util.string;

import android.content.Context;
import com.shenjing.dimension.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/8/30.
 */

public class FormatUtils {

    public static String formatWithRMB(Context context, String money){
        return context.getString(R.string.rmb_symbol, money);
    }

    public static String formatPrice(Long priceByCent){
        String price = "";
        if(priceByCent != null){
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
            try {
                price = decimalFormat.format((double)priceByCent / 100D);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return price;
    }

    /**
     * 超过10000时，已万为计数单位
     * @param number
     * @param scale 保留几位小数
     * @return
     */
    public static String getCutOutDecimal(int scale, int number){
        if (number >= 10000){
            BigDecimal decimalFormat = new BigDecimal((float) number / 10000f);
            BigDecimal bigDecimal = decimalFormat.setScale(scale, BigDecimal.ROUND_DOWN);
            return bigDecimal.toString() + "万";
        }else {
            return String.valueOf(number);
        }
    }
}
