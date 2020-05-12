package com.duanlu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/********************************
 * @name ResourceUtils.
 * @author 段露.
 * @company 浙江托普云农科技股份有限公司.
 * @createDate 2020/5/12 9:45.
 * @updateDate 2020/5/12 9:45.
 * @version V1.0.0
 * @describe Resources资源工具类.
 ********************************/
public class ResourceUtils {

    @ColorInt
    public static int getColor(@NonNull Context context, @ColorRes int colorResId) {
        return getColor(context, colorResId, context.getTheme());
    }

    @ColorInt
    public static int getColor(@NonNull Context context, @ColorRes int colorResId, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorResId);
        } else {
            return context.getResources().getColor(colorResId, theme);
        }
    }

    public Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableResId) {
        return getDrawable(context, drawableResId, context.getTheme());
    }

    public Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableResId, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(drawableResId);
        } else {
            return context.getResources().getDrawable(drawableResId, theme);
        }
    }

}
