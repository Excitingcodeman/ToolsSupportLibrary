package com.gs.toolssupport.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.telephony.*;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import com.gs.toolssupport.storage.SharedPreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.gs.toolssupport.device.DeviceUtils.*;

/**
 * @author husky
 * create on 2019-05-16-16:58
 */
public class DeviceInfo {
    private final String TAG = this.getClass().getSimpleName();
    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private Context mContext;

    public DeviceInfo(Context context) {
        mContext = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    @SuppressLint("HardwareIds")
    public String getIMSI() {
        String imsi;
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            imsi = telephonyManager.getSubscriberId();
        } catch (Exception e) {
            imsi = "";
        }
        return imsi == null ? "" : imsi;
    }

    @SuppressLint("HardwareIds")
    public String getIMEI() {
        String imei;
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            imei = "";
        }
        return imei == null ? "" : imei;
    }

    /**
     * 获取经纬度
     * <p>
     * 默认从优先选择从网络获取
     *
     * @param lat 1：经度 2：纬度
     * @return
     */
    public String getLocation(String lat) {
        String provider;
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else {
            return "";
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            if ("1".equals(lat)) {
                // 经度
                return location.getLatitude() == 0 ? "" : location.getLatitude() + "";
            } else {
                // 纬度
                return location.getLongitude() == 0 ? "" : location.getLatitude() + "";
            }
        } else {
            return "";
        }
    }

    /**
     * 默认从网络返回
     *
     * @return
     */
    public String getLocationType() {
        return "NET";
    }

    /**
     * 是否越狱
     *
     * @return
     */
    public boolean isRoot() {
        if (isRootSystem1() || isRootSystem2()) {
            //TODO 可加其他判断 如是否装了权限管理的apk，大多数root 权限 申请需要app配合，也有不需要的，这个需要改su源码。因为管理su权限的app太多，无法列举所有的app，特别是国外的，暂时不做判断是否有root权限管理app
            //多数只要su可执行就是root成功了，但是成功后用户如果删掉了权限管理的app，就会造成第三方app无法申请root权限，此时是用户删root权限管理app造成的。
            //市场上常用的的权限管理app的包名   com.qihoo.permmgr  com.noshufou.android.su  eu.chainfire.supersu   com.kingroot.kinguser  com.kingouser.com  com.koushikdutta.superuser
            //com.dianxinos.superuser  com.lbe.security.shuame com.geohot.towelroot 。。。。。。
            return true;
        } else {
            return false;
        }
    }

    private boolean isRootSystem1() {
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private boolean isRootSystem2() {
        List<String> pros = getPath();
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i), "su");
                System.out.println("f.getAbsolutePath():" + f.getAbsolutePath());
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }

    /**
     * 基站信息
     *
     * @return
     */
    public String getBaseStnInfo() {

        int mcc = -1, mnc = -1, lac = -1, cellId = -1;
        // 返回值MCC + MNC
        try {
            String operator = telephonyManager.getNetworkOperator();
            mcc = Integer.parseInt(operator.substring(0, 3));
            mnc = Integer.parseInt(operator.substring(3));

            // 中国移动和中国联通获取LAC、CID的方式
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return " MCC = " + mcc + " MNC = " + mnc + " LAC = " + lac + " CID = " + cellId;
            }
            GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
            lac = location.getLac();
            cellId = location.getCid();
        } catch (Exception e) {
            return " MCC = " + mcc + " MNC = " + mnc + " LAC = " + lac + " CID = " + cellId;
        }

        return " MCC = " + mcc + " MNC = " + mnc + " LAC = " + lac + " CID = " + cellId;
    }

    /**
     * 基站位置信息
     *
     * @return
     */
    public String getBaseStnLocation() {
        return "";
    }

    /**
     * 是否触屏
     *
     * @return
     */
    public boolean isSupportTouchscreen() {
        return true;
    }

    /**
     * 应用标志bundle identifier
     *
     * @return
     */
    public String getBundleId() {
        return "";
    }

    /**
     * 屏幕颜色位数
     *
     * @return
     */
    public String getColorBits() {
        return "";
    }

    /**
     * 手机内应用个数
     *
     * @return
     */
    public String getApplicationsNumber() {
        PackageManager pm = mContext.getPackageManager();
        //获取手机中所有安装的应用集合
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        return applicationInfos == null
                ? ""
                : applicationInfos.size() + "";
    }

    //获取基站信息
    public List<String> getTowerInfo(Context context) {
        int mcc = -1;
        int mnc = -1;
        int lac = -1;
        int cellId = -1;
        int rssi = -1;
        String operator = telephonyManager.getNetworkOperator();
        mcc = Integer.parseInt(operator.substring(0, 3));
        List<String> list = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        List<CellInfo> infos = telephonyManager.getAllCellInfo();
        for (CellInfo info : infos) {
            if (info instanceof CellInfoCdma) {
                CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
                CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
                mnc = cellIdentityCdma.getSystemId();
                lac = cellIdentityCdma.getNetworkId();
                cellId = cellIdentityCdma.getBasestationId();
                CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                rssi = cellSignalStrengthCdma.getCdmaDbm();
            } else if (info instanceof CellInfoGsm) {
                CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                mnc = cellIdentityGsm.getMnc();
                lac = cellIdentityGsm.getLac();
                cellId = cellIdentityGsm.getCid();
                CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
                rssi = cellSignalStrengthGsm.getDbm();
            } else if (info instanceof CellInfoLte) {
                CellInfoLte cellInfoLte = (CellInfoLte) info;
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                mnc = cellIdentityLte.getMnc();
                lac = cellIdentityLte.getTac();
                cellId = cellIdentityLte.getCi();
                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                rssi = cellSignalStrengthLte.getDbm();
            } else if (info instanceof CellInfoWcdma) {
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) info;
                CellIdentityWcdma cellIdentityWcdma = null;
                CellSignalStrengthWcdma cellSignalStrengthWcdma = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                    mnc = cellIdentityWcdma.getMnc();
                    lac = cellIdentityWcdma.getLac();
                    cellId = cellIdentityWcdma.getCid();
                    cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                    rssi = cellSignalStrengthWcdma.getDbm();
                }
            } else {
                return null;
            }
            String tower = String.valueOf(mcc) + "#" + String.valueOf(mnc) + "#" + String.valueOf(lac)
                    + "#" + String.valueOf(cellId) + "#" + String.valueOf(rssi);
            list.add(tower);
        }
        if (list.size() > 6) {
            list = list.subList(0, 5);
        } else if (list.size() < 3) {
            int need = 3 - list.size();
            for (int i = 0; i < need; i++) {
                list.add("");
            }
        }
        return list;

    }

    /**
     * 手机内照片个数
     *
     * @return
     */
    @SuppressLint("Recycle")
    public String getPhotosNumber() {
        int i = 0;
        try {
            Cursor cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    //获取图片的名称
                    i++;
                }
            }
            return i + "";
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public String getDeviceInfo() {
        StringBuffer strDeviceInfo = new StringBuffer();
        strDeviceInfo.append("{");
        strDeviceInfo.append("\"appRandom\":" + "\"" + DeviceUtils.getAppRandom() + "\",");
        strDeviceInfo.append("\"imsi\":" + "\"" + getIMSI() + "\",");
        strDeviceInfo.append("\"mac\":" + "\"" + getMac() + "\",");
        strDeviceInfo.append("\"imei\":" + "\"" + getIMEI() + "\",");
        strDeviceInfo.append("\"country\":" + "\"" + getCountry() + "\",");
        strDeviceInfo.append("\"language\":" + "\"" + getLanguage() + "\",");
        strDeviceInfo.append("\"timeZone\":" + "\"" + getTimeZone() + "\",");
        strDeviceInfo.append("\"locationLat\":" + "\"" + getLocation("1") + "\",");
        strDeviceInfo.append("\"locationLon\":" + "\"" + getLocation("2") + "\",");
        strDeviceInfo.append("\"locationType\":" + "\"" + getLocationType() + "\",");
        strDeviceInfo.append("\"osName\":" + "\"" + getOsName() + "\",");
        strDeviceInfo.append("\"osVersion\":" + "\"" + getOsVersion() + "\",");
        strDeviceInfo.append("\"isRoot\":" + "\"" + isRoot() + "\",");
        strDeviceInfo.append("\"gmtTime\":" + "\"" + getGmtTime() + "\",");
        strDeviceInfo.append("\"size\":" + "\"" + getSize() + "\",");
        strDeviceInfo.append("\"resolution\":" + "\"" + getResolution() + "\",");
        strDeviceInfo.append("\"brand\":" + "\"" + getBrand() + "\",");
        strDeviceInfo.append("\"phoneModel\":" + "\"" + getPhoneModel() + "\",");
        strDeviceInfo.append("\"wifi\":" + "\"" + getWifi() + "\",");
        strDeviceInfo.append("\"wifiMac\":" + "\"" + getWifiMac() + "\",");
        strDeviceInfo.append("\"baseStnInfo\":" + "\"" + getBaseStnInfo() + "\",");
        strDeviceInfo.append("\"baseStnLocation\":" + "\"" + getBaseStnLocation() + "\",");
        strDeviceInfo.append("\"serialNumber\":" + "\"" + getSerialNumber() + "\",");
        strDeviceInfo.append("\"deviceNum\":" + "\"" + SharedPreferencesUtil.getInstance().getString("device_Id", "") + "\",");
        strDeviceInfo.append("\"supportTouchscreen\":" + "\"" + isSupportTouchscreen() + "\",");
        strDeviceInfo.append("\"mobileCountryCode\":" + "\"" + getMobileCountryCode() + "\",");
        strDeviceInfo.append("\"mobileNetworkCode\":" + "\"" + getMobileNetworkCode() + "\",");
        strDeviceInfo.append("\"batteryLevel\":" + "\"" + getBatteryLevel() + "\",");
        strDeviceInfo.append("\"appVersion\":" + "\"" + getAppVersion() + "\",");
        strDeviceInfo.append("\"bundleId\":" + "\"" + getBundleId() + "\",");
        strDeviceInfo.append("\"colorBits\":" + "\"" + getColorBits() + "\",");
        strDeviceInfo.append("\"photosNumber\":" + "\"" + getPhotosNumber() + "\",");
        strDeviceInfo.append("\"applicationsNumber\":" + "\"" + getApplicationsNumber() + "\"");
        strDeviceInfo.append("}");
        Log.d(TAG, "encode之前：" + strDeviceInfo.toString());
        return strDeviceInfo.toString();
    }
}
