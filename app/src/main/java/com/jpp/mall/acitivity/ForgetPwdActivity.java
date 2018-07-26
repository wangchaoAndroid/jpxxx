package com.jpp.mall.acitivity;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.User;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.MD5Util;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class ForgetPwdActivity extends BaseActivity {
    private static final String TAG = "ForgetPwdActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.next)
    TextView next;

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_auth_code)
    EditText etAuthCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.auth_code_get)
    TextView authCodeGet;

    private String auth_code;
    private Timer timer;


    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initData() {
        title.setText(R.string.reset_pwd);
        authCodeGet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    }

    private int currentTimer = 60;
    @OnClick(R.id.auth_code_get)
    public void getAuthCode(View view){
        Log.e(TAG,"---" + currentTimer);
        if(currentTimer > 0  && currentTimer < 60){
            return;
        }

        String phone = etPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
            return;
        }

        Observable<BaseEntity> observable = RetrofitFactory.getInstance().sendMsg(phone);
        observable.compose(compose(this.<BaseEntity>bindToLifecycle())).subscribe(new BaseObserver(this) {
            @Override
            protected void onHandleSuccess(Object o) {
                timer = new Timer();
                timer.schedule(new TimeRunnable(currentTimer),0,1000);
            }

            @Override
            protected void onHandleError(String msg, int code) {
                super.onHandleError(msg, code);

            }
        });
    }

    public class TimeRunnable extends TimerTask {

        private int time;

        public TimeRunnable(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            time--;
            currentTimer = time;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(time == 0){
                        authCodeGet.setText("获取验证码");
                        currentTimer = 60;
                        timer.cancel();

                    }else {
                        authCodeGet.setText(time + "s");
                    }

                }
            });

        }
    }

    @OnClick(R.id.next)
    public void next(View view){
//        startActivity(new Intent(RegeistActivity.this,RegeistActivity2.class));
        //
        String phone = etPhone.getText().toString().trim();
        String authCode = etAuthCode.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"请输入新密码",Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.phone = phone;
        user.password= pwd;
        reset(user,authCode);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
    }

    private void reset(final User user, String authCode) {
        if(isDialogShowing()){
            return;
        }
        showLoadingDialog();
        String md5Pwd= MD5Util.getStringMD5(user.password);
        Observable<BaseEntity> observable1 = RetrofitFactory.getInstance(). forgetPwd(user.phone,md5Pwd,authCode,"2");
        observable1.compose(compose(this.<BaseEntity>bindToLifecycle())).subscribe(new BaseObserver(this) {
            @Override
            protected void onHandleSuccess(Object o) {
                // 保存用户信息等操作
                Logger.e(TAG,user.toString());
                Toast.makeText(ForgetPwdActivity.this,"重置成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetPwdActivity.this,LoginActivity.class));
                finish();
                dimissLoadingDialog();
            }

            @Override
            protected void onHandleError(String msg, int code) {
                super.onHandleError(msg, code);
                dimissLoadingDialog();
            }
        });


    }


}
