package com.jpp.mall.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.utils.SpUtils;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

public class SplashActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void doBeforeSetContentView() {
        super.doBeforeSetContentView();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            finish();
            return;
        }
//        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
//        if (click != null) {
//            if(!StackManager.getInstance().containClass(NotifyCenterActivity.class)){
//                Intent i = new Intent(this, NotifyCenterActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
//                startActivity(i);
//            }
//            finish();
//            return;
//        }
    }

    @Override
    public void initData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 String token = (String) SpUtils.get(SplashActivity.this, "token","");
                if(!TextUtils.isEmpty(token)){
                    enterMain(token);
                }else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    SplashActivity.this.finish();
                }


            }
        },2000);
    }

    public void enterMain(String token){
        startActivity(new Intent(this,MainAcitivty.class));
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XGPushManager.onActivityStoped(this);
    }

    /**
     * 设置全屏
     *
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 取消全屏
     *
     * @param activity
     */
    public static void cancelFullScreen(Activity activity) {
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
