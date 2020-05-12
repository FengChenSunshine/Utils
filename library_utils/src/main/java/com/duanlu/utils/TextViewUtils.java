package com.duanlu.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/********************************
 * @name TextViewUtils
 * @author 段露
 * @createDate 2019/3/18  14:26.
 * @updateDate 2019/3/18  14:26.
 * @version V1.0.0
 * @describe TextView相关功能帮助类.
 ********************************/
public class TextViewUtils {

    private static final String TAG = "TextViewUtils";

    private TextViewUtils() {

    }

    public static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    public static String getString(TextView textView) {
        return null == textView ? "" : textView.getText().toString();
    }

    public static String getStringTrim(TextView textView) {
        return getString(textView).trim();
    }

    /**
     * 复制内容到系统剪切板.
     */
    public static boolean copy2Clipboard(Context context, TextView textView) {
        return copy2Clipboard(context, getStringTrim(textView));
    }

    /**
     * 复制内容到系统剪切板.
     *
     * @param text 需要复制的内容.
     * @return true复制成功, false复制失败.
     */
    public static boolean copy2Clipboard(Context context, String text) {
        if (null == context || CheckUtils.isEmpty(text)) return false;
        ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (null == cbm) return false;
        cbm.setPrimaryClip(ClipData.newPlainText(null, text));
        return true;
    }

    /**
     * 设置下划线.
     */
    public static void setUnderline(TextView textView) {
        //下划线.
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿.
        textView.getPaint().setAntiAlias(true);
    }

    /**
     * 设置中划线.
     */
    public static void setStrikeThrough(TextView textView) {
        //中划线并加清晰.
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 清除下划线、中划线.
     */
    public static void clearAllLine(TextView textView) {
        //取消设置的线(中划线、下划线).
        textView.getPaint().setFlags(0);
    }

    /**
     * 使换行键失效,禁止换行.
     */
    public static void invalidateEnter(EditText editText) {
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return KeyEvent.KEYCODE_ENTER == event.getKeyCode();
            }
        });
    }

    /**
     * 关键字高亮.
     *
     * @param keyword        关键字.
     * @param content        内容.
     * @param highlightColor 高亮颜色.
     */
    public static SpannableStringBuilder highlightKey(String keyword, String content, @ColorInt int highlightColor) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        keyword = escapeExprSpecialWord(keyword);
        content = escapeExprSpecialWord(content);
        if (CheckUtils.isNotEmpty(keyword) && CheckUtils.isNotEmpty(content) && content.contains(keyword)) {
            try {
                Pattern pattern = Pattern.compile(keyword);
                Matcher matcher = pattern.matcher(builder);
                int start;
                int end;
                while (matcher.find()) {
                    start = matcher.start();
                    end = matcher.end();
                    builder.setSpan(new ForegroundColorSpan(highlightColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (Exception e) {
                LogUtils.i(TAG, e.toString());
            }
        }
        return builder;
    }

    /**
     * 特殊字符转义.
     */
    private static String escapeExprSpecialWord(String text) {
        if (CheckUtils.isNotEmpty(text)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String s : fbsArr) {
                if (text.contains(s)) {
                    text = text.replace(s, "\\" + s);
                }
            }
        }
        return text;
    }

}
