package com.duanlu.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

/********************************
 * @name ScreenUtils
 * @author 段露
 * @createDate 2018/03/16 16:54.
 * @updateDate 2018/03/16 16:54.
 * @version V1.0.0
 * @describe 屏幕相关操作工具类.
 ********************************/
public class ScreenUtils {

    private ScreenUtils() {

    }

    /**
     * 判断屏幕是否是锁屏状态
     *
     * @return true 屏幕是黑的或屏幕是黑的，false屏幕是黑的或者未知。
     */
    public static boolean isLockScreen(@NonNull Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (null == km) return false;
        if (Build.VERSION_CODES.JELLY_BEAN <= Build.VERSION.SDK_INT) {//4.1之后
            return km.isKeyguardLocked();
        } else {//4.1之前
            return km.inKeyguardRestrictedInputMode();
        }
    }

}