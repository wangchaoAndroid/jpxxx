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
import com.jpp.mall.bean.RegeistModel;
import com.jpp.mall.bean.User;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.net.Utils;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.MD5Util;
import com.jpp.mall.utils.SpUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class RegeistActivity extends BaseActivity {
    private static final String TAG = "RegeistActivity";
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
    @BindView(R.id.et_pwd_re)
    EditText etPwdRe;


    @BindView(R.id.auth_code_get)
    TextView authCodeGet;

    private String auth_code;
    private Timer timer;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//
//        setContentView(R.layout.activity_regeist);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }


   @Override
    public void initData() {
        title.setText(R.string.regeist);
        authCodeGet.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
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


//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                if(timer  !=null){
//                    authCodeGet.setText("获取验证码");
//                    timer.cancel();
//                }
//            }
//        });

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
        String pwdRe = etPwdRe.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(authCode)){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pwdRe)){
            Toast.makeText(this,"请再次输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwdRe) && !pwd.equals(pwdRe)){
            Toast.makeText(this,"两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.phone = phone;
        user.password= pwd;
        regeist(user,authCode);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_regeist;
    }

    private void regeist(final User user, String authCode) {
        if(isDialogShowing()){
            return;
        }
        showLoadingDialog();
        String uuid = Utils.getUUid(this);
        String md5Pwd= MD5Util.getStringMD5(user.password);
        Observable<BaseEntity<RegeistModel>> observable1 = RetrofitFactory.getInstance(). regeist(user.phone,md5Pwd,"Android",authCode,uuid,"2");
        observable1.compose(compose(this.<BaseEntity<RegeistModel>>bindToLifecycle())).subscribe(new BaseObserver<RegeistModel>(RegeistActivity.this) {
            @Override
            protected void onHandleSuccess(RegeistModel user) {
                // 保存用户信息等操作
                Logger.e(TAG,user.toString());
                SpUtils.put(RegeistActivity.this,"token",user.token);
                Toast.makeText(RegeistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegeistActivity.this,RegeistActivity2.class));
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
