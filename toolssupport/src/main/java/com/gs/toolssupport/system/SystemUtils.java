package com.gs.toolssupport.system;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.NotificationManagerCompat;
import com.gs.toolssupport.GlobalInit;
import com.gs.toolssupport.R;
import com.gs.toolssupport.resources.ResourcesUtils;

/**
 * @author husky
 * create on 2019-05-16-20:20
 */
public class SystemUtils {

    public static String COMMON_LABEL = "HUSKY_LABEL";

    /**
     * 复制
     *
     * @param text
     */
    public static void copyToClipBoard(String text) {
        ClipboardManager cm = (ClipboardManager) GlobalInit.application.getSystemService(
                Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(COMMON_LABEL, text));
        Toast.makeText(GlobalInit.application, ResourcesUtils.getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
    }


    /**
     * 打开目录
     *
     * @param context
     * @param url     应用路径
     */
    public static void openInBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(GlobalInit.application, ResourcesUtils.getString(R.string.no_app_can_use), Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 开启通知
     *
     * @param mActivity 当前页面
     */
    public static void openNotifications(final Activity mActivity) {
        NotificationManagerCompat notification = NotificationManagerCompat.from(mActivity);
        boolean isEnabled = notification.areNotificationsEnabled();
        if (!isEnabled) {
            //引导开启通知
            Intent intent = new Intent();
            try {
                String packageName = mActivity.getApplicationContext().getPackageName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int uid = mActivity.getApplicationInfo().uid;
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                    intent.putExtra("app_package", packageName);
                    intent.putExtra("app_uid", uid);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + packageName));
                } else {
                    intent.setAction(Settings.ACTION_SETTINGS);
                }
            } catch (Exception e) {
                intent.setAction(Settings.ACTION_SETTINGS);
            }
            mActivity.startActivity(intent);


        }
    }

}
