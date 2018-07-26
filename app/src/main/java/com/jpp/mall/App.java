package com.jpp.mall;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.support.multidex.MultiDex;

import com.jpp.mall.utils.Utils;
import com.pingplusplus.android.Pingpp;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by Administrator on 2018/4/24.
 */

public class App extends Application {
    private static Context context;

    public static Context getAppContext(){
        return context;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        context = base;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Pingpp.DEBUG = false;
        XGPushConfig.enableDebug(this,false);
        XGPushConfig.enableOtherPush(this,false);
        Utils.init(this);

        ZXingLibrary.initDisplayOpinion(this);
        init();
        //注意在3.2.2 版本信鸽对账号绑定和解绑接口进行了升级具体详情请参考API文档。
//        XGPushManager.bindAccount(getApplicationContext(), "XINGE");
//        XGPushManager.setTag(this,"XINGE");
    }

    public void init(){
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setSound(
//                RingtoneManager.getActualDefaultRingtoneUri(
//                        context, RingtoneManager.TYPE_NOTIFICATION)) // 设置声音
                // setSound(
                 Uri.parse("android.resource://" + getPackageName()
                 + "/" + R.raw.jpp))
                .setDefaults(Notification.DEFAULT_VIBRATE) // 振动
                .setFlags(Notification.FLAG_AUTO_CANCEL); // 是否可清除
        // 设置自定义通知layout,通知背景等可以在layout里设置
        //build.setLayoutId(R.layout.layout_notification);
        // 设置自定义通知内容id
        //build.setLayoutTextId(R.id.ssid);
        // 设置自定义通知标题id
        build.setLayoutTitleId(R.id.title);
        // 设置自定义通知图片id
        build.setLayoutIconId(R.id.icon);
        // 设置自定义通知图片资源
        build.setLayoutIconDrawableId(R.mipmap.ic_launcher);
        // 设置状态栏的通知小图标
        build.setIcon(R.mipmap.ic_launcher);
        // 设置时间id
        build.setLayoutTimeId(R.id.time);
        // 若不设定以上自定义layout，又想简单指定通知栏图片资源
        build.setNotificationLargeIcon(R.mipmap.ic_launcher);
        // 客户端保存build_id
        XGPushManager.setDefaultNotificationBuilder(context, build);
    }

}
