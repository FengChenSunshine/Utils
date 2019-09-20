package com.duanlu.utils;

import java.util.Collection;
import java.util.Map;

/********************************
 * @name CheckUtils
 * @author 段露
 * @createDate 2019/8/5 9:10
 * @updateDate 2019/8/5 9:10
 * @version V1.0.0
 * @describe 参数检验工具类.
 ********************************/
public class CheckUtils {

    private CheckUtils() {

    }

    public static boolean isBlank(CharSequence charSequence) {
        if (charSequence == null) {
            return true;
        } else {
            for (int i = 0; i < charSequence.length(); ++i) {
                if (!Character.isWhitespace(charSequence.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence charSequence) {
        return !isBlank(charSequence);
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> boolean isEmpty(T[] tArr) {
        return tArr == null || tArr.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] tArr) {
        return !isEmpty(tArr);
    }
}
