package com.duanlu.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;

/********************************
 * @name AppFolderManager
 * @author 段露
 * @createDate 2019/06/28 13:56
 * @updateDate 2019/06/28 13:56
 * @version V1.0.0
 * @describe 项目内文件夹管理.
 ********************************/
public class AppFolderManager {

    private static final String TAG = AppFolderManager.class.getSimpleName();

    public static final String FOLDER_NAME_LOG = "Log";
    public static final String FOLDER_NAME_CACHE = "Cache";
    public static final String FOLDER_NAME_BACKUPS = "Backups";
    public static final String FOLDER_NAME_PICTURE = "Picture";
    public static final String FOLDER_NAME_AVATAR = "Avatar";

    private volatile static AppFolderManager sInstance;

    private String mRootFolderName;
    private String mAuthority;

    private AppFolderManager() {

    }

    private void initialize(@NonNull String rootFolderName, @NonNull String authority) {
        if (CheckUtils.isNotEmpty(mRootFolderName)) {
            Log.w(TAG, "Please do not repeat initialization!!!");
        } else {
            this.mRootFolderName = rootFolderName;
            this.mAuthority = authority;
        }
    }

    public static void init(@NonNull String rootFolderName, @NonNull String authority) {
        getInstance().initialize(rootFolderName, authority);
    }

    public static AppFolderManager getInstance() {
        if (null == sInstance) {
            synchronized (AppFolderManager.class) {
                if (null == sInstance) {
                    sInstance = new AppFolderManager();
                }
            }
        }
        return sInstance;
    }

    public File getRootFolder() {
        if (CheckUtils.isEmpty(mRootFolderName)) {
            throw new RuntimeException("Please call the init method to initialize");
        }
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + mRootFolderName);
        FileUtils.createOrExistsDir(file);
        return file;
    }

    public File getLogFolder() {
        return getFolderFile(FOLDER_NAME_LOG);
    }

    @Nullable
    public String getLogFolderPath() {
        File file = getLogFolder();
        return null != file ? file.getAbsolutePath() : null;
    }

    public File getCacheFolder() {
        return getFolderFile(FOLDER_NAME_CACHE);
    }

    public File getBackupsFolder() {
        return getFolderFile(FOLDER_NAME_BACKUPS);
    }

    public File getPictureFolder() {
        return getFolderFile(FOLDER_NAME_PICTURE);
    }

    public File getAvatarFolder() {
        return getFolderFile(FOLDER_NAME_AVATAR);
    }

    private File getFolderFile(@NonNull String folderName) {
        File file = new File(getRootFolder(), folderName);
        FileUtils.createOrExistsDir(file);
        return file;
    }

    public String getAuthoritie() {
        return this.mAuthority;
    }

    public Uri getUriFromFile(Context context, File file) {
        return FileUtils.getUriFromFile(context, file, mAuthority);
    }

}