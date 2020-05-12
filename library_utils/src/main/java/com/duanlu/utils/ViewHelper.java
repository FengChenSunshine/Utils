package com.duanlu.utils;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/********************************
 * @name ViewHelper
 * @author 段露
 * @createDate 2019/01/02 16:31.
 * @updateDate 2019/01/02 16:31.
 * @version V1.0.0
 * @describe View功能拓展帮助类.
 ********************************/
public class ViewHelper {

    private ViewHelper() {

    }

    public static void setViewOnClickListener(View.OnClickListener listener, View... views) {
        if (null == listener || null == views || 0 == views.length) return;
        for (View view : views) if (null != view) view.setOnClickListener(listener);
    }

    public static void setViewOnClickListener(ViewGroup parent, View.OnClickListener listener, @IdRes int... viewIds) {
        if (null == listener || null == parent || null == viewIds || 0 == viewIds.length) return;
        for (int id : viewIds) parent.findViewById(id).setOnClickListener(listener);
    }

    public static void setViewVisibility(int visibility, View... views) {
        if (null == views || 0 == views.length) return;
        for (View view : views) {
            if (null != view && view.getVisibility() != visibility) view.setVisibility(visibility);
        }
    }

    /**
     * 切换View显示状态,仅支持GONE和VISIBLE.
     */
    public static void toggleViewVisibility(View... views) {
        if (null == views || 0 == views.length) return;
        for (View view : views) {
            if (null != view) {
                view.setVisibility(View.VISIBLE == view.getVisibility() ? View.GONE : View.VISIBLE);
            }
        }
    }

    public static void setPadding(View view, int padding) {
        if (null == view) return;
        view.setPadding(padding, padding, padding, padding);
    }

    public static void setPaddingLeft(View view, int padding) {
        if (null == view || padding == view.getPaddingLeft()) return;
        view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingTop(View view, int padding) {
        if (null == view || padding == view.getPaddingTop()) return;
        view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingRight(View view, int padding) {
        if (null == view || padding == view.getPaddingRight()) return;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
    }

    public static void setPaddingBottom(View view, int padding) {
        if (null == view || padding == view.getPaddingBottom()) return;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    public static void setPaddingHorizontal(View view, int padding) {
        if (null == view || padding == view.getPaddingRight()) return;
        view.setPadding(padding, view.getPaddingTop(), padding, view.getPaddingBottom());
    }

    public static void setPaddingVertical(View view, int padding) {
        if (null == view || padding == view.getPaddingBottom()) return;
        view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), padding);
    }

    /**
     * 设置View左外边距.
     *
     * @param view   View.
     * @param margin 左外边距.
     */
    public static void setMarginLeft(View view, int margin) {
        ViewGroup.MarginLayoutParams mlp = getMarginLayoutParams(view);
        if (null != mlp) {
            mlp.leftMargin = margin;
            view.setLayoutParams(mlp);
        }
    }

    public static void setMarginTop(View view, int margin) {
        ViewGroup.MarginLayoutParams mlp = getMarginLayoutParams(view);
        if (null != mlp) {
            mlp.topMargin = margin;
            view.setLayoutParams(mlp);
        }
    }

    public static void setMarginRight(View view, int margin) {
        ViewGroup.MarginLayoutParams mlp = getMarginLayoutParams(view);
        if (null != mlp) {
            mlp.rightMargin = margin;
            view.setLayoutParams(mlp);
        }
    }

    public static void setMarginBottom(View view, int margin) {
        ViewGroup.MarginLayoutParams mlp = getMarginLayoutParams(view);
        if (null != mlp) {
            mlp.bottomMargin = margin;
            view.setLayoutParams(mlp);
        }
    }

    public static void setMargin(View view, int margin) {
        ViewGroup.MarginLayoutParams mlp = getMarginLayoutParams(view);
        if (null != mlp) {
            mlp.leftMargin = margin;
            mlp.topMargin = margin;
            mlp.rightMargin = margin;
            mlp.bottomMargin = margin;
            view.setLayoutParams(mlp);
        }
    }

    public static void setMargin(View view, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams mlp = getMarginLayoutParams(view);
        if (null != mlp) {
            mlp.leftMargin = leftMargin;
            mlp.topMargin = topMargin;
            mlp.rightMargin = rightMargin;
            mlp.bottomMargin = bottomMargin;
            view.setLayoutParams(mlp);
        }
    }

    /**
     * 获取MarginLayoutParams，类型不符合或为null时返回null.
     *
     * @param view 需要获取的View.
     * @return ViewGroup.MarginLayoutParams.
     */
    @Nullable
    public static ViewGroup.MarginLayoutParams getMarginLayoutParams(View view) {
        if (null == view) return null;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        return null == lp ? null : (lp instanceof ViewGroup.MarginLayoutParams) ? (ViewGroup.MarginLayoutParams) lp : null;
    }

    /**
     * 设置TextView或其子控件的LeftDrawable.
     *
     * @param textView TextView或其子控件.
     * @param drawable Drawable.
     */
    public static void setDrawableLeft(TextView textView, Drawable drawable) {
        if (null == textView) return;
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    public static void setDrawableTop(TextView textView, Drawable drawable) {
        if (null == textView) return;
        textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
    }

    public static void setDrawableRight(TextView textView, Drawable drawable) {
        if (null == textView) return;
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    public static void setDrawableBottom(TextView textView, Drawable drawable) {
        if (null == textView) return;
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
    }

    /**
     * 扩展View点击区域的范围.
     *
     * @param delegateView 需要扩展View点击区域的view.
     * @param area         需要扩展的尺寸.
     */
    public static void expandViewTouchArea(View delegateView, int area) {
        expandViewTouchArea(delegateView, area, area, area, area);
    }

    /**
     * 扩展View点击区域的范围.
     * 注意：
     * 1.此view必须要有parent.
     * 2.若自定义触摸范围超出parent大小时，超出的部分无效.
     * 3.一个parent只能设置一个child view的TouchDelegate，设置了多个时最后设置的生效.
     *
     * @param delegateView 需要扩展View点击区域的view.
     * @param left         左边需要扩展的尺寸.
     * @param top          顶部需要扩展的尺寸.
     * @param right        右边需要扩展的尺寸.
     * @param bottom       底部需要扩展的尺寸.
     */
    public static void expandViewTouchArea(final View delegateView, final int left, final int top, final int right, final int bottom) {
        if (null == delegateView) return;
        final View parent = (View) delegateView.getParent();
        if (null == parent) return;
        parent.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                //如果该方法执行的太早，会导致获取bound失败，因为UI界面尚未开始绘制时无法获得正确的坐标.
                delegateView.getHitRect(bounds);
                bounds.left -= left;
                bounds.top -= top;
                bounds.right += right;
                bounds.bottom += bottom;
                parent.setTouchDelegate(new TouchDelegate(bounds, delegateView));
            }
        });
    }

}