package com.duanlu.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;

import java.lang.reflect.Method;

/********************************
 * @name ActivityHelp
 * @author 段露
 * @createDate 2019/3/6  16:11.
 * @updateDate 2019/3/6  16:11.
 * @version V1.0.0
 * @describe Activity功能扩展.
 ********************************/
public class ActivityHelp {

    private ActivityHelp() {

    }

    /**
     * 参考：https://blog.csdn.net/starry_eve/article/details/82777160.
     *
     * @return true透明主题.
     */
    public static boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = activity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            return false;
//            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

}