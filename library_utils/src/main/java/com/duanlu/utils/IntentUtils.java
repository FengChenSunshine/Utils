package com.duanlu.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/********************************
 * @name IntentUtils
 * @author 段露
 * @createDate 2017/11/23 17:50.
 * @updateDate 2019/08/05 10:07.
 * @version V2.0.0
 * @describe Activity跳转、获取Intent意图工具类.
 ********************************/
@SuppressWarnings({"unused"})
public final class IntentUtils {

    private static final String TAG = "IntentUtils";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SYSTEM_PICK_IMAGE, SYSTEM_PICK_VIDEO, SYSTEM_PICK_PERSON})
    public @interface SystemPick {

    }

    public static final int SYSTEM_PICK_IMAGE = 0x01;//选择照片(直接打开系统选择相册界面).
    public static final int SYSTEM_PICK_VIDEO = 0x02;//选择视频(直接打开系统选择视频界面).
    public static final int SYSTEM_PICK_PERSON = 0x03;//选择联系人.

    private IntentUtils() {

    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////-------------------打电话、发短信、发邮件-------------------//////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取打电话(跳到拨号界面)意图.
     *
     * @param mobile 电话号码.
     * @return 打电话(跳到拨号界面)意图.
     */
    public static Intent getDialPhoneIntent(@NonNull String mobile) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
    }

    /**
     * 获取拨打电话意图.
     * 需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}.
     *
     * @param phoneNumber 电话号码.
     * @return 拨打电话意图.
     */
    public static Intent getCallPhoneIntent(@NonNull String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 获取跳至发送短信界面的意图.
     *
     * @param phoneNumber 接收号码.
     * @param content     短信内容.
     */
    public static Intent getSendSmsIntent(@NonNull String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        intent.putExtra("sms_body", content);
        return intent;
    }

    /**
     * 获取发送邮件Intent.
     *
     * @param receive 所有"主送人"邮件地址.
     */
    public static Intent getSendEmailIntent(String[] receive) {
        return getSendEmailIntent(receive, null, null, "", "", null);
    }

    /**
     * 获取发送邮件Intent.
     *
     * @param receive       所有"主送人"邮件地址.
     * @param copyReceive   所有"抄送人"邮件地址.
     * @param BCC           所有"密件抄送人"邮件地址.
     * @param subject       邮件主题.
     * @param content       邮件正文.
     * @param attachmentUri 邮件附件.
     */
    public static Intent getSendEmailIntent(String[] receive, String[] copyReceive, String[] BCC, String subject, String content, Uri attachmentUri) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        if (CheckUtils.isNotEmpty(receive)) { //所有"主送人"邮件地址.
            intent.putExtra(Intent.EXTRA_EMAIL, receive);
        }
        if (CheckUtils.isNotEmpty(copyReceive)) {//所有"抄送人"邮件地址.
            intent.putExtra(Intent.EXTRA_CC, copyReceive);
        }
        if (CheckUtils.isNotEmpty(BCC)) {//所有"密件抄送人"邮件地址.
            intent.putExtra(Intent.EXTRA_BCC, BCC);
        }
        if (CheckUtils.isNotEmpty(subject)) { //邮件主题.
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (CheckUtils.isNotEmpty(content)) {//邮件正文.
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }
        if (null != attachmentUri) {//邮件附件.
            intent.putExtra(Intent.EXTRA_STREAM, attachmentUri);
        }
        return intent;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////--------------------------选择相关--------------------------//////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取调用系统相机拍照.
     *
     * @param activity Activity.
     * @param outUri   输出路径url.
     */
    public static void getCaptureIntent(Activity activity, @NonNull Uri outUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//传递权限.
    }

    /**
     * 获取打开系统相册意图.
     *
     * @return 打开系统相册意图.
     * @deprecated use getPickIntent(SYSTEM_PICK_IMAGE).
     */
    @Deprecated
    public static Intent getAlbumIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        return intent;
    }

    /**
     * 获取调用系统选择(照片、视频、联系人)界面.
     *
     * @param pickType 选择类型.
     */
    public static Intent getPickIntent(@SystemPick int pickType) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        switch (pickType) {
            case SYSTEM_PICK_IMAGE://选择照片(直接打开系统选择相册界面).
                intent.setType("vnd.android.cursor.dir/image");
                break;
            case SYSTEM_PICK_VIDEO://选择视频(直接打开系统选择视频界面).
                intent.setType("vnd.android.cursor.dir/video");
                break;
            case SYSTEM_PICK_PERSON://选择联系人.
                intent.setType("vnd.android.cursor.dir/person");//直接跳转到系统选择联系人界面.
                //intent.setType("vnd.android.cursor.dir/phone");//直接跳转到系统选择联系人界面.
                //intent.setType("vnd.android.cursor.dir/contact");//直接跳转到系统选择联系人界面.
                //intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                //intent.setData(ContactsContract.Contacts.CONTENT_URI);
                break;
        }
        return intent;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////--------------------------分享相关--------------------------//////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取调用系统分享功能分享文本意图.
     *
     * @param content 需要分享的文本.
     * @return 调用系统分享功能分享文本意图.
     */
    public static Intent getSystemShareTextIntent(@NonNull String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("text/plain");
        return intent;
    }

    /**
     * 获取调用系统分享功能分享图片意图.
     *
     * @param uri 需要分享的图片的uri.
     * @return 调用系统分享功能分享图片意图.
     */
    public static Intent getSystemSharePictureIntent(@NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 获取调用系统分享功能分享Uri意图.
     *
     * @param uri 需要分享的uri.
     * @return 调用系统分享功能分意图.
     */
    public static Intent getSystemShareUriIntent(@NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("*/*");
        return intent;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////--------------------------启动相关--------------------------//////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取根据包名启动APP意图.
     *
     * @param context     Context.
     * @param packageName 需要启动的APP的包名.
     * @return 根据包名启动APP意图.
     */
    public static Intent getLaunchAppIntent(@NonNull Context context, @NonNull String packageName) {
        try {
            return context.getPackageManager().getLaunchIntentForPackage(packageName);
        } catch (Exception e) {
            LogUtils.e(TAG, "启动应用：" + packageName + "失败");
            return null;
        }
    }

    /**
     * 获取根据包名和类名启动对应组件的意图.
     *
     * @param packageName 需要启动组件的包名.
     * @param className   需要启动组件的全类名.
     * @param bundle      Bundle.
     * @return 根据包名和类名启动对应组件的意图.
     */
    public static Intent getComponentIntent(@NonNull String packageName, @NonNull String className, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (null != bundle) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return intent;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////-----------------------安装、卸载相关-----------------------//////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取安装App(支持6.0)的意图.
     *
     * @param file 文件.
     * @return 安装App(支持6.0 、 7.0 、 8.0)的意图.
     */
    public static Intent getInstallAppIntent(@NonNull Context context, @NonNull File file, String authority) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri contentUri = FileUtils.getUriFromFile(context, file, authority);
        String type;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(contentUri, type);
        return intent;
    }

    /**
     * 获取根据包名卸载APP意图.
     *
     * @param packageName 需要卸载的APP的包名.
     * @return 根据包名卸载APP意图.
     */
    public static Intent getUninstallAppIntent(@NonNull String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        } catch (Exception e) {
            LogUtils.e(TAG, "卸载应用：" + packageName + "失败");
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////--------------------------系统相关--------------------------//////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取调用系统浏览器打开uri意图.
     *
     * @param uri Uri链接.
     * @return 调用系统浏览器打开uri意图.
     */
    public static Intent getSystemBrowserIntent(@NonNull Uri uri) {
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    /**
     * 获取根据包名打开对应APP设置详情界面意图.
     *
     * @param packageName 需要打开设置详情的应用包名.
     * @return 根据包名打开对应APP设置详情界面意图.
     */
    public static Intent getSystemAppSettingsDetailsIntent(@NonNull String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * 获取所有应用设置列表界面(可能和从手机设置里进去的不是一个界面，比如在小米手机上).
     * 其它： https://www.jianshu.com/p/7145c2544ef4.
     * https://www.jianshu.com/p/deb57569262c.
     */
    public static Intent getSystemAppSettingsIntent() {
        return new Intent(Settings.ACTION_APPLICATION_SETTINGS);
    }

    /**
     * 获取打开系统网络设置界面.
     */
    public static Intent getSystemNetWorkSetting(@NonNull Context context) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //判断手机系统的版本  即API大于10 就是3.0或以上版本.
            intent = new Intent(Settings.ACTION_SETTINGS);
        } else {//3.0以下.
            intent = new Intent();
            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(cn);
            intent.setAction("android.intent.action.VIEW");
        }
        return intent;
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////----------------------------其它----------------------------//////////
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取关机意图.
     * 需添加权限 {@code <uses-permission android:name="android.permission.SHUTDOWN"/>}.
     *
     * @return 关机意图.
     */
    public static Intent getShutdownIntent() {
        Intent intent = new Intent(Intent.ACTION_SHUTDOWN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * 获取返回到后台运行的Intent.
     *
     * @return 返回到后台运行的Intent.
     */
    public static Intent getBackHomeIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        return intent;
    }

    /**
     * 判断一个Intent是否有应用响应.
     *
     * @param intent 需要启动的Intent.
     * @return true响应，false无响应.
     */
    public static boolean hasResponse(@NonNull Context context, Intent intent) {
        return null != intent && context.getApplicationContext()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    /**
     * 获取重新启动当前APP意图.
     */
    public static Intent getRestartAppIntent(@NonNull Context context) {
        return context.getPackageManager().getLaunchIntentForPackage("");
    }

    /**
     * 延迟millis时间后根据包名启动APP.
     *
     * @param millis      延迟时间(单位：毫秒).
     * @param packageName APP包名.
     */
    public static void pendingRestartApp(@NonNull Context context, long millis, @NonNull String packageName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + millis, pendingIntent);
    }

    public static void startActivity(Context context, Intent intent) {
        if (hasResponse(context, intent)) {
            context.startActivity(intent);
        }
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        if (hasResponse(activity, intent)) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        if (null != fragment && null != fragment.getContext() && hasResponse(fragment.getContext(), intent)) {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

}