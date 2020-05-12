package com.duanlu.utils;

import android.app.Activity;
import android.app.Application;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;

import com.duanlu.utils.callbacks.SimpleActivityLifecycleCallbacks;

import java.util.ArrayList;
import java.util.List;

/********************************
 * @name AppForegroundBackgroundManager
 * @author 段露
 * @createDate 2019/08/05 17:10
 * @updateDate 2019/08/05 17:10
 * @version V1.0.0
 * @describe 应用程序前后台改变监听.
 ********************************/
public final class AppForegroundBackgroundManager {

    private static final String TAG = "AppForegroundBackgroundManager";

    private int mCount;//计数器.
    private List<Observer<Boolean>> mObservers;

    private static AppForegroundBackgroundManager sInstance;

    public static AppForegroundBackgroundManager getInstance() {
        if (null == sInstance) {
            synchronized (AppForegroundBackgroundManager.class) {
                if (null == sInstance) {
                    sInstance = new AppForegroundBackgroundManager();
                }
            }
        }
        return sInstance;
    }

    private AppForegroundBackgroundManager() {
        this.mObservers = new ArrayList<>(1);
    }

    /**
     * 初始化应用程序前后台改变监听.
     *
     * @param observer Observer.onChanged(isForeground);true表示应用程序切换到前台,false后台.
     */
    public static void init(@NonNull Application application, @NonNull Observer<Boolean> observer) {
        getInstance().initialize(application).register(observer);
    }

    private AppForegroundBackgroundManager initialize(@NonNull Application application) {
        application.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks() {
            @Override
            public void onActivityStarted(Activity activity) {
                mCount++;
                if (mCount - 1 == 0) {//应用切换到前台.
                    dispatchChanged(true);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mCount--;
                if (mCount == 0) {//应用切换到后台.
                    dispatchChanged(false);
                }
            }
        });
        return this;
    }

    public void register(Observer<Boolean> observer) {
        this.mObservers.add(observer);
    }

    public boolean unregister(Observer<Boolean> observer) {
        return this.mObservers.remove(observer);
    }

    private void dispatchChanged(boolean isForeground) {
        LogUtils.i(TAG, String.format("应用程序前后台改变：%s", isForeground ? "前台" : "后台"));
        List<Observer<Boolean>> observers = mObservers;
        for (Observer<Boolean> observer : observers) {
            observer.onChanged(isForeground);
        }
    }

}