package scret.com.msx7.josn.effectdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.core.util.DialogUtils;

import java.lang.reflect.Field;

import scret.com.msx7.josn.effectdemo.effect.Anim3Activity;
import scret.com.msx7.josn.effectdemo.effect.AnimShapeRectCircle;
import scret.com.msx7.josn.effectdemo.effect.Shape2Activity;

//import android.support.v7.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startActivity(new Intent(this, AnimShapeRectCircle.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, Shape2Activity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(this, Anim3Activity.class));
                break;
            case R.id.btn4:
//                startActivity(new Intent(this, Anim3Activity.class));
//                count++;
//                sendToXiaoMi(this, count);
                DialogUtils.ShowDialog("标题","内容",this);
//                try {
//                ShortcutBadger.setBadge(getApplicationContext(), count);
//                } catch (ShortcutBadgeException e) {
//                    e.printStackTrace();
//                } finally {
//                }
                break;
            case R.id.btn5:
                startActivity(new Intent(this, Activity3.class));
                break;
            case R.id.btn6:
                startActivity(new Intent(this, Activity4.class));
                break;
        }
    }

    public static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";
    public static final String EXTRA_UPDATE_APP_COMPONENT_NAME = "android.intent.extra.update_application_component_name";
    public static final String EXTRA_UPDATE_APP_MSG_TEXT = "android.intent.extra.update_application_message_text";

    @SuppressWarnings("rawtypes")
    private static void sendToXiaoMi(Context context, int count) {
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        boolean isMiUIV6 = true;
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context);
            builder.setContentTitle("您有" + count + "未读消息");
            builder.setTicker("您有" + count + "未读消息");
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setDefaults(Notification.DEFAULT_LIGHTS);
            notification = builder.build();
            Class miuiNotificationClass = Class
                    .forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField(
                    "messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, count);// 设置信息数
            field = notification.getClass().getField("extraNotification");
            field.setAccessible(true);
            field.set(notification, miuiNotification);
        } catch (Exception e) {
            e.printStackTrace();
            // miui 6之前的版本
            isMiUIV6 = false;
            Intent localIntent = new Intent("android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra("android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + context.getClass().getName());
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", count);
            context.sendBroadcast(localIntent);
        } finally {
            if (notification != null && isMiUIV6) { // miui6以上版本需要使用通知发送
                nm.notify(101010, notification);
            }
        }
    }

    protected String getEntryActivityName() {
        ComponentName componentName = getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent();
        return componentName.getClassName();
    }
}
