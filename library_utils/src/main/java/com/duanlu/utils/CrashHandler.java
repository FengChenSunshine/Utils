package com.duanlu.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.lang.Thread.UncaughtExceptionHandler;

/********************************
 * @name CrashHandler
 * @author 段露
 * @createDate 2019/6/20 11:19.
 * @updateDate 2019/6/20 11:19.
 * @version V1.0.0
 * @describe 全局异常捕获.
 ********************************/
public class CrashHandler {

    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    //CrashHandler实例
    private static CrashHandler sInstance;
    //程序的Context对象
    private Context mApplicationContext;

    private CustomUncaughtExceptionHandler mInnerUncaughtExceptionHandler;
    private CustomUncaughtExceptionHandler mCustomUncaughtExceptionHandler;

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandler();
                }
            }
        }
        return sInstance;
    }

    private CrashHandler() {

    }

    /**
     * 初始化.
     */
    public void init(@NonNull Context context, CustomUncaughtExceptionHandler handler) {
        mApplicationContext = context.getApplicationContext();
        this.mCustomUncaughtExceptionHandler = handler;

        ensureInnerHandler();

        captureMainException();
        captureThreadException();
    }

    private void ensureInnerHandler() {
        if (null != mInnerUncaughtExceptionHandler) return;
        mInnerUncaughtExceptionHandler = new CustomUncaughtExceptionHandler() {
            @Override
            public boolean handleMainThreadException(Context applicationContext, Thread thread, Throwable e) {

                if (null != mCustomUncaughtExceptionHandler) {
                    return mCustomUncaughtExceptionHandler.handleMainThreadException(mApplicationContext, thread, e);
                }

                if (mDefaultUncaughtExceptionHandler != null) {
                    //如果用户没有处理则让系统默认的异常处理器来处理
                    mDefaultUncaughtExceptionHandler.uncaughtException(thread, e);
                } else {//退出应用程序.
                    StackManager.getInstance().AppExit();
                }
                return true;
            }

            @Override
            public boolean handleOtherThreadException(Context applicationContext, Thread thread, Throwable e) {

                if (null != mCustomUncaughtExceptionHandler) {
                    return mCustomUncaughtExceptionHandler.handleOtherThreadException(mApplicationContext, thread, e);
                }

                if (mDefaultUncaughtExceptionHandler != null) {
                    //如果用户没有处理则让系统默认的异常处理器来处理
                    mDefaultUncaughtExceptionHandler.uncaughtException(thread, e);
                } else {//退出应用程序.
                    StackManager.getInstance().AppExit();
                }
                return true;
            }
        };
    }

    private void captureMainException() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Exception e) {
                        ensureInnerHandler();
                        mInnerUncaughtExceptionHandler.handleMainThreadException(mApplicationContext, Thread.currentThread(), e);
                    }
                }
            }
        });
    }

    private void captureThreadException() {
        //获取系统默认的UncaughtException处理器.
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置设置为自定义处理器.
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                ensureInnerHandler();
                mInnerUncaughtExceptionHandler.handleOtherThreadException(mApplicationContext, Thread.currentThread(), e);
            }
        });
    }

    public interface CustomUncaughtExceptionHandler {

        boolean handleMainThreadException(Context applicationContext, Thread thread, Throwable e);

        boolean handleOtherThreadException(Context applicationContext, Thread thread, Throwable e);
    }

}