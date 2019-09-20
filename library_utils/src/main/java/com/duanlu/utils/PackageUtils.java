package com.duanlu.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

/********************************
 * @name PackageUtils
 * @author 段露
 * @createDate 2017/11/21 15:34.
 * @updateDate 22017/11/21 15:34.
 * @version V1.0.0
 * @describe APK信息.
 ********************************/
public class PackageUtils {

    private static final String TAG = "PackageUtils";

    /**
     * QQ包名
     */
    private static final String TENCENT_PACKAGE_NAME = "com.tencent.mobileqq";
    /**
     * 微信包名
     */
    private static final String WECHAT_PACKAGE_NAME = "com.tencent.mm";

    private PackageUtils() {

    }

    /**
     * 判断应用程序运行是否运行在后台
     *
     * @param context 上下文对象
     * @return true后台，false前台
     */
    public static boolean isOnForeground(@NonNull Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    LogUtils.i(TAG, "后台:" + appProcess.processName);
                    return true;
                } else {
                    LogUtils.i(TAG, "前台:" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取应用程序包名
     *
     * @param context 上下文对象
     * @return 应用程序包名
     */
    public static String getPackageName(@NonNull Context context) {
        return context.getPackageName();
    }

    /**
     * 获取应用程序版本名
     *
     * @param context 上下文对象
     * @return 应用程序版本名
     */
    public static String getVersionName(@NonNull Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "获取应用程序版本名失败:" + e.getMessage());
            return "";
        }
    }

    /**
     * 获取应用程序版本号
     *
     * @param context 上下文对象
     * @return 应用程序版本号
     */
    public static int getVersionCode(@NonNull Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "获取应用程序版本号失败:" + e.getMessage());
            return 0;
        }
    }

    /**
     * 获取应用程序名称
     *
     * @param context 上下文对象
     * @return 应用程序名称
     */
    public static String getAppName(@NonNull Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "获取应用程序名称失败:" + e.getMessage());
            return "";
        }
    }

    /**
     * 判断手机是否安装微信客户端
     *
     * @param context Context
     * @return true已安装，false未安装
     */
    public static boolean isInstanceWeChat(@NonNull Context context) {
        return isInstallApp(context, WECHAT_PACKAGE_NAME);
    }

    /**
     * 判断手机是否已经安装QQ客户端
     *
     * @return true已安装，false未安装
     */
    public static boolean isInstanceQQ(@NonNull Context context) {
        return isInstallApp(context, TENCENT_PACKAGE_NAME);
    }

    /**
     * 判断手机上有没有安装某一APP
     *
     * @param context     Context
     * @param packageName 需要判断的APP包名
     * @return true已安装，false未安装
     */
    public static boolean isInstallApp(@NonNull Context context, @NonNull String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        if (null != packageInfoList) {
            for (PackageInfo info : packageInfoList) {
                if (info.packageName.equalsIgnoreCase(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

}