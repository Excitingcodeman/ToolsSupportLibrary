package com.gs.toolssupport.money;

import com.gs.toolssupport.R;
import com.gs.toolssupport.resources.ResourcesUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author husky
 * create on 2019-05-16-17:36
 */
public class MoneyFormatUtil {

    public static final BigDecimal ONE_WAN = new BigDecimal("10000");

    public static String formatMoney(BigDecimal bigDecimal) {

        if (null != bigDecimal) {
            return bigDecimal.setScale(2, RoundingMode.HALF_UP).toString();
        }
        return "--";

    }

    public static String formatMoney(BigDecimal bigDecimal, String defaultValue) {

        if (null != bigDecimal) {
            return bigDecimal.setScale(2, RoundingMode.HALF_UP).toString();
        }
        return defaultValue;
    }


    public static String changeWan(BigDecimal money) {
        if (null != money) {
            if (money.compareTo(ONE_WAN) >= 0) {
                return money.divide(ONE_WAN).setScale(1, RoundingMode.DOWN).toString() + ResourcesUtils.getString(R.string.ten_thousand);
            } else {
                return money.setScale(2,RoundingMode.HALF_UP).toString();
            }
        }

        return "--";
    }
}
