package com.jpp.mall.acitivity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.LoginModel;
import com.jpp.mall.bean.User;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.net.Utils;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.MD5Util;
import com.jpp.mall.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.regeist)
    TextView regeist;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.reset)
    TextView resetPwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }
    @Override
    public void initData() {
//        String token = (String) SpUtils.get(this, "token","");
//        if(!TextUtils.isEmpty(token)){
//            enterMain(token);
//        }
    }



    public void enterMain(String token){
        startActivity(new Intent(this,MainAcitivty.class));
        finish();
        overridePendingTransition(0,0);
    }

    @OnClick(R.id.regeist)
    public void regeist(View view){
        startActivity(new Intent(this,RegeistActivity.class));
    }

    @OnClick(R.id.reset)
    public void forgetPwd(View view){
        Logger.e(TAG,"11111111111111");
        startActivity(new Intent(this,ForgetPwdActivity.class));
    }


    @OnClick(R.id.login)
    public void login(View view){
        if(isDialogShowing()){
            return;
        }
        String phone = etPhone.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"请输入手机号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.phone = phone;
        user.password = pwd;
        login(user);
    }


    private void login(User user) {
        showLoadingDialog();
        String uuid = Utils.getUUid(this);
        String md5Pwd = MD5Util.getStringMD5(user.password);//1827663624

        Observable<BaseEntity<LoginModel>> observable = RetrofitFactory.getInstance().login(user.phone, md5Pwd,uuid,"Android","2");
        observable.compose(compose(this.<BaseEntity<LoginModel>>bindToLifecycle())).subscribe(new BaseObserver<LoginModel>(this) {
            @Override
            protected void onHandleSuccess(LoginModel loginModel) {
                // 保存用户信息等操作
                String token = loginModel.token;
                Logger.e("token",token);
                SpUtils.put(LoginActivity.this,"token",token);
                enterMain(token);
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
