package com.duanlu.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.duanlu.utils.callbacks.SimpleActivityLifecycleCallbacks;

import java.util.Stack;

/********************************
 * @name StackManager
 * @author 段露
 * @createDate 2017/11/21 11:18.
 * @updateDate 2017/11/21 11:18.
 * @version V1.0.0
 * @describe Activity栈管理.
 ********************************/
@SuppressWarnings("unused")
public class StackManager {

    private static final String TAG = StackManager.class.getSimpleName();

    private static StackManager sInstance;

    private static Stack<Activity> mActivityStack;
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    private StackManager() {
        mActivityStack = new Stack<>();
    }

    private void initialize(@NonNull Application application) {
        if (null != mActivityLifecycleCallbacks) {
            Log.w(TAG, "Please do not repeat initialization!!!");
        } else {
            mActivityLifecycleCallbacks = new SimpleActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    addActivity(activity); //入栈.
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    removeActivity(activity);//出栈.
                }
            };
            application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
    }

    public static void init(@NonNull Application application) {
        getInstance().initialize(application);
    }

    public static StackManager getInstance() {
        if (null == sInstance) {
            synchronized (StackManager.class) {
                if (null == sInstance) {
                    sInstance = new StackManager();
                }
            }
        }
        return sInstance;
    }

    public boolean checkHasActivity(Activity activity) {
        return -1 != mActivityStack.search(activity);
    }

    public boolean checkHasActivity(Class clazz) {
        return checkHasActivity(clazz.getSimpleName());
    }

    public boolean checkHasActivity(String activityName) {
        if (TextUtils.isEmpty(activityName)) return false;

        int size = mActivityStack.size();
        Stack<Activity> activityStack = new Stack<>();
        activityStack.addAll(mActivityStack);

        for (int i = 0; i < size; i++) {
            if (activityStack.get(i).getClass().getSimpleName().equals(activityName)) {
                return true;
            }
        }
        return false;
    }

    public Activity queryActivity(String activityName) {
        if (CheckUtils.isNotEmpty(activityName)) {
            int size = mActivityStack.size();
            Stack<Activity> activityStack = new Stack<>();
            activityStack.addAll(mActivityStack);

            for (int i = 0; i < size; i++) {
                if (activityStack.get(i).getClass().getSimpleName().equals(activityName)) {
                    return activityStack.get(i);
                }
            }
        }
        return null;
    }

    /**
     * 添加Activity到栈
     *
     * @param activity 需要添加的Activity对象
     */
    public void addActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    /**
     * 获取顶层Activity
     *
     * @return 顶层Activity
     */
    public Activity getTop() {
        return mActivityStack.lastElement();
    }

    /**
     * 获取栈中的所有Activity
     *
     * @return 栈中的所有Activity
     */
    public Stack<Activity> getActivities() {
        return mActivityStack;
    }

    /**
     * 结束当前Activity（堆栈中后一个压入的Activity)
     */
    public void finishActivity() {
        this.finishActivity(mActivityStack.lastElement());
    }

    /**
     * 根据Class对象结束对应的Activity
     *
     * @param cls 需要结束的Activity的Class对象
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                this.finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 需要结束的Activity对象
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            removeActivity(activity);
        }
    }

    /**
     * 结束除开指定Activity的其他Activity
     *
     * @param cls Class
     */
    public void finishOtherActivity(Class<?> cls) {
        if (null == mActivityStack) return;

        for (Activity activity : mActivityStack) {
            if (!activity.getClass().equals(cls)) {
                this.finishActivity(activity);
            }
        }
    }

    /**
     * 结束除开栈顶的其他Activity
     */
    public void finishToPenultimateActivity() {
        int size = mActivityStack.size();
        for (int i = 0; i < size - 1; i++) {
            this.finishActivity(mActivityStack.get(i));
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : mActivityStack) {
            if (null != activity && !activity.isFinishing()) {
                activity.finish();
            }
        }
        //移出Stack中所有的元素
        mActivityStack.clear();
    }

    /**
     * 从栈中移除指定Activity
     *
     * @param activity 需要移除的Activity对象
     */
    public void removeActivity(Activity activity) {
        if (null != activity) mActivityStack.remove(activity);
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            this.finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 延迟delayMillis时间后关闭应用
     *
     * @param delayMillis 延迟时间
     */
    public void killApplication(int delayMillis) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                AppExit();
            }
        }, delayMillis);
    }

}
