package com.duanlu.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/********************************
 * @name KeyBoardUtils
 * @author 段露
 * @createDate 2017/11/21 13:49
 * @updateDate 2017/11/21 13:49
 * @version V1.0.0
 * @describe 软键盘相关工具类.
 ********************************/
public class KeyBoardUtils {

    private KeyBoardUtils() {

    }

    /**
     * 打开软键盘.
     *
     * @param view 需要打开软键盘的View.
     */
    public static void openKeyBoard(@NonNull Context context, @NonNull View view) {
        getInputMethodManager(context).showSoftInput(view, 0);
    }

    /**
     * 关闭软键盘.
     *
     * @param view 需要关闭软键盘的View.
     */
    public static void closeKeyBoard(@NonNull Context context, @NonNull View view) {
        getInputMethodManager(context).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 关闭软键盘.
     *
     * @param activity 需要关闭软键盘的Activity.
     */
    public static void closeKeyBoard(@NonNull Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (null == view) return;
        IBinder iBinder = view.getWindowToken();
        getInputMethodManager(activity).hideSoftInputFromWindow(iBinder, 0);
    }

    /**
     * 切换软键盘状态(打开<==>关闭).
     */
    public static void toggleKeyBoard(@NonNull Context context) {
        getInputMethodManager(context).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private static InputMethodManager getInputMethodManager(@NonNull Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

}