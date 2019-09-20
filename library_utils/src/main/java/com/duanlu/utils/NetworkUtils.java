package com.duanlu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/********************************
 * @name NetworkUtils
 * @author 段露
 * @createDate 2017/9/6 13:54.
 * @updateDate 22017/9/6 13:54.
 * @version V1.0.0
 * @describe 网络相关辅助工具类.
 ********************************/
@SuppressWarnings({"unused", "WeakerAccess"})
public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public static final int RESULT_OPEN_SETTINGS = 0;

    @StringDef({NETWORK_WIFI, NETWORK_4G, NETWORK_3G, NETWORK_2G, NETWORK_UNKNOWN, NETWORK_NO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetworkType {

    }

    public static final String NETWORK_WIFI = "NETWORK_WIFI";
    public static final String NETWORK_4G = "NETWORK_4G";
    public static final String NETWORK_3G = "NETWORK_3G";
    public static final String NETWORK_2G = "NETWORK_2G";
    public static final String NETWORK_UNKNOWN = "NETWORK_UNKNOWN";
    public static final String NETWORK_NO = "NETWORK_NO";

    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;

    private NetworkUtils() {

    }

    /**
     * 判断网络是否连接
     *
     * @param context 上下文对象
     * @return 连接时返回true，否则返回false
     */
    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断网络是否连接,并且在连接时是否可以访问外网
     *
     * @param context 上下文对象
     * @return 连接true时返回true，否则返回false
     */
    public static boolean isConnectedWithPing(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    if (ping()) {//检查连接的网络能否访问外网
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接(该方法会判断是否有网络连接)
     *
     * @param context 上下文对象
     * @return 是wifi连接时返回true，否则返回false
     */
    public static boolean isWifi(@NonNull Context context) {
        int connectedType = getConnectedType(context);
        return connectedType != -1 && (connectedType == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 判断wifi是否打开,需添加权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     *
     * @param context 上下文对象
     * @return true是，false否
     */
    public static boolean getWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 打开或关闭wifi,需添加权限 <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
     *
     * @param context 上下文对象
     * @param enabled 打开或关闭
     */
    public static void setWifiEnabled(Context context, boolean enabled) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (enabled) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    /**
     * 打开或关闭移动数据,需添加权限 <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>
     * 注：这个权限貌似只有系统应用可以申请
     *
     * @param context 上下文对象
     * @param enabled 打开或关闭
     */
    public static void setDataEnabled(Context context, boolean enabled) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(tm, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络是否是4G
     *
     * @param context 上下文对象
     * @return true是，false否
     */
    public static boolean is4G(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断MOBILE是否可用(该方法会判断是否有网络连接)
     *
     * @param context 上下文对象
     * @return MOBILE可用时返回true，否则返回false
     */
    public static boolean isMobile(@NonNull Context context) {
        int connectedType = getConnectedType(context);
        return connectedType != -1 && connectedType == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 获取活动网络信息,需添加权限 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     *
     * @param context 上下文对象
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            return connectivityManager.getActiveNetworkInfo();
        }
        return null;
    }

    /**
     * 获取当前网络连接的类型信息
     *
     * @param context 上下文对象
     * @return 当前网络连接的类型信息,-1时获取信息失败,或没有网络连接
     */
    public static int getConnectedType(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null != networkInfo && networkInfo.isConnected()) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return networkInfo.getType();
                }
            }
        }
        return -1;
    }

    /**
     * 判断是否有外网连接(普通方法不能判断外网的网络是否连接，比如连接上局域网)
     *
     * @return 外网连接时返回true，否则返回false
     */
    public static boolean ping() {
        String result = null;
        String ip = "www.baidu.com";//ping的地址，可以换成任意一种可靠的外网
        try {
            Process process = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping网址3次
            //读取ping的内容，可以不加
            InputStream is = process.getInputStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String connect;
            while ((connect = buf.readLine()) != null) {
                sb.append(connect);
            }
            LogUtils.d(TAG, "--ping--result connect:" + sb.toString());
            //ping的状态
            int status = process.waitFor();
            if (status == 0) {
                result = "succeed";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            //e.printStackTrace();
            result = "IOException";
        } catch (InterruptedException e) {
            //e.printStackTrace();
            result = "InterruptedException";
        } finally {
            LogUtils.d(TAG, "--ping--result：" + result);
        }
        return false;
    }

    /**
     * 获取网络运营商名称(中国移动、如中国联通、中国电信)
     *
     * @param context 上下文对象
     * @return 网络运营商名称:中国移动、如中国联通、中国电信
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 获取当前网络类型
     *
     * @param context 上下文对象
     * @return 当前网络类型
     */
    public static String getNetworkType(Context context) {
        @NetworkType String netType = NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NETWORK_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NETWORK_3G;
                        } else {
                            netType = NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 获取IP地址,需添加权限 <uses-permission android:name="android.permission.INTERNET"/>
     *
     * @param useIPv4 是否是使用IPV4
     * @return IP地址
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取域名ip地址,需添加权限 <uses-permission android:name="android.permission.INTERNET"/>
     *
     * @param domain 域名
     * @return ip地址
     */
    public static String getDomainAddress(final String domain) {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            Future<String> fs = exec.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    InetAddress inetAddress;
                    try {
                        inetAddress = InetAddress.getByName(domain);
                        return inetAddress.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
            return fs.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
