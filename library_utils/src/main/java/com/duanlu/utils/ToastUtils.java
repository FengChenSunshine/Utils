package com.duanlu.utils;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/********************************
 * @name ToastUtils
 * @author 段露
 * @createDate 2019/3/6  14:20.
 * @updateDate 2019/3/6  14:20.
 * @version V1.0.0
 * @describe Toast工具类.
 ********************************/
public class ToastUtils {

    private static long mLastShowTime = 0L;
    private static String mLastShowMessage = null;

    private ToastUtils() {

    }

    public static void showDefaultToastWithPic(@NonNull Context context, @NonNull String msg, @DrawableRes int drawableRes) {
        if (!TextUtils.isEmpty(msg) || null != context.getResources().getDrawable(drawableRes)) {
            Toast toast = makeToast(context, msg);
            if (null != toast) {
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastView = (LinearLayout) toast.getView();
                ImageView ivToast = new ImageView(context);
                ivToast.setImageResource(drawableRes);
                toastView.addView(ivToast, 0);
                toast.show();
            }
        }
    }

    public static void showToastOnce(@NonNull Context context, @StringRes int stringRes) {
        showToastOnce(context, context.getString(stringRes));
    }

    public static void showToastDefault(@NonNull Context context, @NonNull String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast toast = makeToast(context, msg);
            if (null != toast) toast.show();
        }
    }

    public static void showToastOnce(@NonNull Context context, @NonNull String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (msg.equals(mLastShowMessage)) {
                if (System.currentTimeMillis() - mLastShowTime > 3000L) {
                    showToastDefault(context, msg);
                    mLastShowTime = System.currentTimeMillis();
                }
            } else {
                showToastDefault(context, msg);
                mLastShowMessage = msg;
                mLastShowTime = System.currentTimeMillis();
            }
        }
    }

    @Nullable
    private static Toast makeToast(@NonNull Context context, @StringRes int msgResId) {
        return makeToast(context, context.getString(msgResId));
    }

    @Nullable
    private static Toast makeToast(Context context, @NonNull String msg) {
        return CheckUtils.isEmpty(msg) ? null : Toast.makeText(context, msg, msg.length() > 6 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
    }

}