package com.lyl.sms.smsforward;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Author: lyl
 * Date Created : 2018/7/27.
 */
public class MyUtils {

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param smsContent
     */
    public static void sendMessageBySysterm(String phoneNumber, String smsContent) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 4) {
            return;
        }
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(smsContent);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
        Log.d("SMSFORWARD", "给" + phoneNumber + "发送：" + smsContent);
    }


    /**
     * 判断是否拥有通知使用权
     *
     * @return
     */
    public static boolean notificationListenerEnable(Context context) {
        boolean enable = false;
        String packageName = context.getPackageName();
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (flat != null) {
            enable = flat.contains(packageName);
        }
        return enable;
    }

    /**
     * 跳转系统设置里的通知使用权页面
     *
     * @return
     */
    public static boolean gotoNotificationAccessSetting(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            try {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings" + "" + "" + "" + "" +
                        ".Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

}
