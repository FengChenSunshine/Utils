package com.duanlu.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/********************************
 * @name PreferencesManager
 * @author 段露
 * @createDate 2017/9/6 13:52.
 * @updateDate 2017/9/6 13:52.
 * @version V1.0.0
 * @describe PreferencesManager管理类，提供get和put方法来重写SharedPreferences所提供的方法，更为实用和便捷.
 ********************************/
@SuppressWarnings({"unused", "WeakerAccess"})
public class PreferencesManager {

    private static final String TAG = "PreferencesManager";

    private Context mContext;
    private SharedPreferences preferences;
    private static final String DATA_URL = "/data/data/";
    private static final String SHARED_PREFS = "/shared_prefs";

    private static String shareName = "SHARE_DATA";
    private static final String THEME = "Theme";
    private static final String LANG = "Lang";

    private PreferencesManager instance;

    private PreferencesManager() {

    }

    /**
     * 构造方法
     *
     * @param context   上下文对象
     * @param shareName shareName
     */
    private PreferencesManager(Context context, String shareName) {
        mContext = context;
        preferences = context.getSharedPreferences(shareName,
                Context.MODE_PRIVATE);
    }

    /**
     * 得到单例模式的PreferencesManager对象
     *
     * @param context 上下文
     * @return PreferencesManager
     */
    public static PreferencesManager getInstance(@NonNull Context context) {
        return getInstance(context, shareName);
    }

    /**
     * 得到单例模式的PreferencesManager对象
     *
     * @param context   上下文
     * @param shareName 文件名称
     * @return PreferencesManager
     */
    public static PreferencesManager getInstance(@NonNull Context context, @NonNull String shareName) {
        return new PreferencesManager(context, shareName);
    }

    public boolean put(@NonNull String key, boolean value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putBoolean(key, value);
            return edit.commit();
        }
        return false;
    }

    public boolean put(@NonNull String key, @NonNull String value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putString(key, value);
            return edit.commit();
        }
        return false;
    }

    public boolean put(@NonNull String key, int value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putInt(key, value);
            return edit.commit();
        }
        return false;
    }

    public boolean put(@NonNull String key, float value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putFloat(key, value);
            return edit.commit();
        }
        return false;
    }

    public boolean put(@NonNull String key, long value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putLong(key, value);
            return edit.commit();
        }
        return false;
    }

    @SuppressLint("NewApi")
    public boolean put(@NonNull String key, @NonNull Set<String> value) {
        Editor edit = preferences.edit();
        if (edit != null) {
            edit.putStringSet(key, value);
            return edit.commit();
        }
        return false;
    }

    /**
     * 直接存放对象，反射将根据对象的属性作为key，并将对应的值保存。
     *
     * @param t 存放对象
     */
    @SuppressWarnings("rawtypes")
    public <T> boolean put(@NonNull T t) {
        try {
            String methodName;
            String saveValue = "";
            String fieldName;
            Editor edit = preferences.edit();
            Class cls = t.getClass();

            if (edit != null) {

                Method[] methods = cls.getDeclaredMethods();
                Field[] fields = cls.getDeclaredFields();

                for (Method method : methods) {
                    methodName = method.getName();
                    for (Field f : fields) {
                        fieldName = f.getName();
                        if (methodName.toLowerCase().contains(fieldName.toLowerCase())) {

                            Object value = method.invoke(t);
                            if (value != null && !TextUtils.isEmpty(String.valueOf(value))) {
                                saveValue = String.valueOf(value);
                            }

                            LogUtils.d(TAG, "key: " + fieldName + " value: " + saveValue);
                            edit.putString(fieldName, String.valueOf(saveValue));
                            break;
                        }
                    }
                }
                return edit.commit();
            }
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String get(@NonNull String key) {
        return preferences.getString(key, "");
    }

    public String get(@NonNull String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean get(@NonNull String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public int get(@NonNull String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float get(@NonNull String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public long get(@NonNull String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    @SuppressLint("NewApi")
    public Set<String> get(@NonNull String key, Set<String> defValue) {
        return preferences.getStringSet(key, defValue);
    }

    /**
     * 获取整个对象，跟put(T t)对应使用， 利用反射得到对象的属性，然后从preferences获取
     *
     * @param cls 需要获取的对象
     * @return 获取的对象
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public <T> Object get(@NonNull Class<T> cls) {
        Object obj = null;
        String fieldName;
        try {
            obj = cls.newInstance();
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                fieldName = f.getName();
                if (!"serialVersionUID".equals(fieldName)) {
                    f.setAccessible(true);
                    f.set(obj, get(f.getName()));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public int getTheme(int defThemeId) {
        return instance.get(THEME, defThemeId);
    }

    public void setTheme(int themeId) {
        instance.put(THEME, themeId);
    }

    public String getLanguage(String defLang) {
        return instance.get(LANG, defLang);
    }

    public void setLang(String Language) {
        instance.put(LANG, Language);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void clearAll() {
        try {
            String fileName = shareName + ".xml";
            StringBuilder path;
            path = new StringBuilder(DATA_URL).append(mContext.getPackageName()).append(SHARED_PREFS);
            File file = new File(path.toString(), fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
