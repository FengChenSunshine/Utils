package com.duanlu.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
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

    private static String sRootFolderName;
    private static String sAuthority;

    private AppFolderManager() {

    }

    public static void init(@NonNull String rootFolderName, @NonNull String authority) {
        if (CheckUtils.isNotEmpty(sRootFolderName)) {
            Log.w(TAG, "Please do not repeat initialization!!!");
        } else {
            sRootFolderName = rootFolderName;
            sAuthority = authority;
        }
    }

    public static File getRootFolder() {
        if (CheckUtils.isEmpty(sRootFolderName)) {
            throw new RuntimeException("Please call the init method to initialize");
        }
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + sRootFolderName);
        FileUtils.createOrExistsDir(file);
        return file;
    }

    public static File getLogFolder() {
        return getFolderFile(FOLDER_NAME_LOG);
    }

    public static File getCacheFolder() {
        return getFolderFile(FOLDER_NAME_CACHE);
    }

    public static File getBackupsFolder() {
        return getFolderFile(FOLDER_NAME_BACKUPS);
    }

    public static File getPictureFolder() {
        return getFolderFile(FOLDER_NAME_PICTURE);
    }

    public static File getAvatarFolder() {
        return getFolderFile(FOLDER_NAME_AVATAR);
    }

    private static File getFolderFile(@NonNull String folderName) {
        File file = new File(getRootFolder(), folderName);
        FileUtils.createOrExistsDir(file);
        return file;
    }

    public static String getAuthoritie() {
        return sAuthority;
    }

    public static Uri getUriFromFile(Context context, File file) {
        return FileUtils.getUriFromFile(context, file, sAuthority);
    }

}