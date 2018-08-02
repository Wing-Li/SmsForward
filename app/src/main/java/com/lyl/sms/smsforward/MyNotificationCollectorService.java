package com.lyl.sms.smsforward;

import android.app.Notification;
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

        if (!TextUtils.isEmpty(notification.tickerText)) {
            // 获取通知内容
            String content = notification.tickerText.toString();
            if (!TextUtils.isEmpty(content)) {

                boolean isContains = true;
                // 如果要监听的词有多个，就先分割
                if (LINNER_TEXT.contains(",")) {
                    String[] strings = LINNER_TEXT.split(",");
                    // 用“,”分割之后遍历，只要有一个不包含，就是 false
                    for (int i = 0; i < strings.length; i++) {
                        if (!content.contains(strings[i])) {
                            isContains = false;
                            break;
                        }
                    }

                } else {
                    // 要监听的词只有一个
                    isContains = content.contains(LINNER_TEXT);
                }

                if (isContains) {
                    MyUtils.sendMessageBySysterm(PHONE, content);
                }
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
