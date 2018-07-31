package com.lyl.sms.smsforward;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static String SP_PHONE = "SP_PHONE";
    public static String SP_LINNER_TEXT = "SP_LINNER_TEXT";

    Context mContext;

    TextView listenerContent;
    EditText phoneEdt;
    EditText contentEdt;
    Button startBtn;
    Button permissionBtn;

    Button checkConnectedBtn;
    TextView checkConnectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        mContext = this;

        initViews();
        initEvent();
        checkMyPermission();
    }

    /**
     * 初始化布局
     */
    private void initViews() {
        listenerContent = findViewById(R.id.listener_content);
        phoneEdt = findViewById(R.id.phone);
        contentEdt = findViewById(R.id.content);
        startBtn = findViewById(R.id.start);
        permissionBtn = findViewById(R.id.permission);
        checkConnectedBtn = findViewById(R.id.checkConnected);
        checkConnectedText = findViewById(R.id.checkConnectedText);

        // 有默认值给默认值
        String phone = (String) SPUtil.get(mContext, SP_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            MyNotificationCollectorService.PHONE = phone;
            phoneEdt.setText(phone);
        }
        String content = (String) SPUtil.get(mContext, SP_LINNER_TEXT, "");
        if (!TextUtils.isEmpty(content)) {
            MyNotificationCollectorService.LINNER_TEXT = content;
            contentEdt.setText(content);
        }
    }

    /**
     * 设置监听
     */
    private void initEvent() {
        // 重置默认值
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyNotificationCollectorService.PHONE = phoneEdt.getText().toString().trim();
                MyNotificationCollectorService.LINNER_TEXT = contentEdt.getText().toString().trim();

                SPUtil.put(mContext, SP_PHONE, MyNotificationCollectorService.PHONE);
                SPUtil.put(mContext, SP_LINNER_TEXT, MyNotificationCollectorService.LINNER_TEXT);

                String[] strings = MyNotificationCollectorService.LINNER_TEXT.split(",");

                StringBuilder str = new StringBuilder("你要监听的内容为：");
                for (int i = 0; i < strings.length; i++) {
                    str.append(strings[i]).append(" / ");
                }

                listenerContent.setText(str.toString());

                Toast.makeText(MainActivity.this, "开始监听", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置权限
        permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                mContext.startActivity(intent);
            }
        });

        // 检查监听
        checkConnectedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyNotificationCollectorService.isConnected){
                    checkConnectedText.setText("正常");
                } else {
                    checkConnectedText.setText("正在连接...");
                }
            }
        });
    }

    /**
     * 检查权限
     */
    private void checkMyPermission() {
        if (!MyUtils.notificationListenerEnable(this)) {
            MyUtils.gotoNotificationAccessSetting(this);
        } else {
            toggleNotificationListenerService(this);
        }
    }

    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, MyNotificationCollectorService.class), PackageManager
                .COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(context, MyNotificationCollectorService.class), PackageManager
                .COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

}
