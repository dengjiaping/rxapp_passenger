package com.mxingo.passenger.util;

import android.os.Build;

/**
 * Created by zhouwei on 2017/7/10.
 */

public class DevInfo {

    /**
     * 得到UUID
     */
    public static String getInfo() {
        try {
            String devInfo = " phoneInfo:" +
                    "Product: " + Build.PRODUCT
                    + ", VERSION.RELEASE: " + Build.VERSION.RELEASE
                    + ", BRAND: " + Build.BRAND
                    + ", DEVICE: " + Build.DEVICE;
            return devInfo;

        } catch (Exception e) {
            return "";
        }
    }
}
