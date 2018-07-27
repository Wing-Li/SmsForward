package com.lyl.sms.smsforward;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.Toast;

public class MyNotificationCollectorService extends NotificationListenerService {

    public static String PHONE = "";
    public static String LINNER_TEXT = "";

    public static boolean isConnected;

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }

        Bundle extras = notification.extras;
        if (extras != null) {
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            String content = extras.getString(Notification.EXTRA_TEXT, "");
            if (!TextUtils.isEmpty(content) && content.contains(LINNER_TEXT)) {
                MyUtils.sendMessageBySysterm(PHONE, content);
            }
        }
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        isConnected = true;
        Toast.makeText(this, "连接成功", Toast.LENGTH_LONG).show();
    }
}
