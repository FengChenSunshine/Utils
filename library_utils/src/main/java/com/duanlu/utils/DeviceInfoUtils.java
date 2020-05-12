package com.duanlu.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import java.util.UUID;

/********************************
 * @name DeviceInfoUtils
 * @author 段露
 * @createDate 2017/11/21 15:34.
 * @updateDate 2017/11/21 15:34.
 * @version V1.0.0
 * @describe 设备信息帮助类.
 ********************************/
public class DeviceInfoUtils {

    private static final String TAG = "DeviceInfoUtils";

    private DeviceInfoUtils() {

    }

    /**
     * 获取设备id,在使用该方法前应该检查Manifest.permission.READ_PHONE_STATE权限
     *
     * @param context Context
     * @return 设备id
     */
    @RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        } else {
            return telephonyManager.getDeviceId();
        }
    }

    /**
     * 获取显示屏参数
     *
     * @return 显示屏参数
     */
    public static String getDisplay() {
        return Build.DISPLAY;
    }

    /**
     * 获取手机制造商
     *
     * @return 手机制造商
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号(eg : = MI 6)
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 当前手机系统版本号(eg : 7.1.1)
     */
    public static String getRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取CPU指令集
     *
     * @return CPU指令集
     */
    public static String getCPU_ABI() {
        return Build.CPU_ABI;
    }

    /**
     * 获取设备参数
     *
     * @return 设备参数
     */
    public static String getDevice() {
        return Build.DEVICE;
    }

    /**
     * 获取硬件名称
     *
     * @return 硬件名称
     */
    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 获取Android系统定制商
     *
     * @return Android系统定制商(eg : Xiaomi)
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取Android版本
     *
     * @return Android版本
     */
    public static int getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取UUID作为设备唯一ID
     *
     * @return UUID
     * UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法。
     * UUID(Universally Unique Identifier)全局唯一标识符,是指在一台机器上生成的数字，
     * 它保证对在同一时空中的所有机器都是唯一的，是由一个十六位的数字组成,表现出来的 形式。
     * 由以下几部分的组合：当前日期和时间(UUID的第一个部分与时间有关，
     * 如果你在生成一个UUID之后，过几秒又生成一个UUID，则第一个部分不 同，其余相同)，
     * 时钟序列，全局唯一的IEEE机器识别号（如果有网卡，从网卡获得，没有网卡以其他方式获得），
     * UUID的唯一缺陷在于生成的结果串会比较长。
     */
    public static String getUniquePsuedoID() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("86")//35
//                //主板名称
//                .append(Build.BOARD.length() % 10)//MSM8974
//                //android系统定制商
//                .append(Build.BRAND.length() % 10)//Xiaomi
//                //CPU 和ABI的本地代码指令集
//                .append(Build.CPU_ABI.length() % 10)//armeabi-v7a
//                //设备参数
//                .append(Build.DEVICE.length() % 10)//cancro
//                //显示屏参数
//                .append(Build.DISPLAY.length() % 10)//MMB29M
//                .append(Build.HOST.length() % 10)//c3-miui-ota-bd42.bj
//                //修改版本列表
//                .append(Build.ID.length() % 10)//MMB29M
//                //硬件厂商
//                .append(Build.MANUFACTURER.length() % 10)//Xiaomi
//                //获取手机型号 版本
//                .append(Build.MODEL.length() % 10)//MI 4C
//                //手机厂商
//                .append(Build.PRODUCT.length() % 10)//cancro_wc_lte
//                //描述Build的标签
//                .append(Build.TAGS.length() % 10)//release-keys
//                //Build的类型
//                .append(Build.TYPE.length() % 10)//user
//                .append(Build.USER.length() % 10);//builder
//
//        //sb.toString();//7616696653247
//        String serial = Build.SERIAL;//9a264ae0
//        if (TextUtils.isEmpty(serial)) serial = "serial_cheweiejia";
//
//        String uniquePsuedoID = new UUID(sb.toString().hashCode(), serial.hashCode()).toString();
//        LogUtils.d("UUID=" + sb.toString() + "-->serial=" + serial + "-->uniquePsuedoID=" + uniquePsuedoID);
        return UUID.randomUUID().toString();//00000000-20f6-88f2-ffff-ffff84c3d164
    }

}
