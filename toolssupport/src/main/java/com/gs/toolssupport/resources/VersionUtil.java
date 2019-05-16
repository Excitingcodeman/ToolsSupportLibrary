package com.gs.toolssupport.resources;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.gs.toolssupport.GlobalInit;

/**
 * @author husky
 * create on 2019-05-16-17:07
 */
public class VersionUtil {

    /**
     * @return 版本名
     */
    public static String getVersionName() {
        if (null == getPackageInfo()) {
            return "";
        }
        return getPackageInfo().versionName;
    }

    /**
     * @return 版本号
     */
    public static int getVersionCode() {
        if (null == getPackageInfo()) {
            return -1;
        }
        return getPackageInfo().versionCode;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager pm = GlobalInit.application.getPackageManager();
            pi = pm.getPackageInfo(GlobalInit.application.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static String getAppMetaData(String key) {
        if (GlobalInit.application == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = GlobalInit.application.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(GlobalInit.application.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }

}
