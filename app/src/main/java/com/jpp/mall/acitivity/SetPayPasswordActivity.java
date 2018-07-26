package com.jpp.mall.acitivity;

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
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.MD5Util;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class SetPayPasswordActivity extends BaseActivity {
    private static final String TAG = "RegeistActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sure)
    TextView sure;


    @BindView(R.id.et_auth_code)
    EditText etAuthCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.auth_code_get)
    TextView authCodeGet;
    private String auth_code;
    private Timer timer;

    @Override
    public void initData() {
        title.setText(R.string.set_pwd);
        authCodeGet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    }

    @OnClick(R.id.back)
    public void back(View view) {
        finish();
    }

    private int currentTimer = 60;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_pay_password;
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
                    if (time == 0) {
                        authCodeGet.setText("获取验证码");
                        currentTimer = 60;
                        timer.cancel();

                    } else {
                        authCodeGet.setText(time + "s");
                    }

                }
            });

        }
    }

    @OnClick(R.id.sure)
    public void onSure(View view){
        String token = (String) SpUtils.get(this,"token","");
        String authCode = etAuthCode.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.showShortToast(this,"请输入密码");
            return;
        }
        if(pwd.length() != 6){
            ToastUtils.showShortToast(this,"支付密码必须为6位数字");
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            ToastUtils.showShortToast(this,"请输入验证码");
            return;
        }
        Logger.e(TAG, MD5Util.getStringMD5(pwd));
        Logger.e(TAG, authCode + "");
        Logger.e(TAG, token + "");
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().setupPayPassword(token,authCode, MD5Util.getStringMD5(pwd));
        observable.compose(compose(this.<BaseEntity>bindToLifecycle())).subscribe(new BaseObserver(this) {
            @Override
            protected void onHandleSuccess(Object o) {
                ToastUtils.showShortToast(SetPayPasswordActivity.this,"设置成功");
                SetPayPasswordActivity.this.finish();
            }

            @Override
            protected void onHandleError(String msg, int code) {
                super.onHandleError(msg, code);

            }
        });
    }

    @OnClick(R.id.auth_code_get)
    public void getAuthCode(View view) {
        Log.e(TAG, "---" + currentTimer);
        if (currentTimer > 0 && currentTimer < 60) {
            return;
        }

        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        Observable<BaseEntity> observable = RetrofitFactory.getInstance().sendMsg(phone);
        observable.compose(compose(this.<BaseEntity>bindToLifecycle())).subscribe(new BaseObserver(this) {
            @Override
            protected void onHandleSuccess(Object o) {
                timer = new Timer();
                timer.schedule(new TimeRunnable(currentTimer), 0, 1000);
            }

            @Override
            protected void onHandleError(String msg, int code) {
                super.onHandleError(msg, code);

            }
        });
    }
}
