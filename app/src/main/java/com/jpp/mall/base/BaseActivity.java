package com.jpp.mall.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jpp.mall.R;
import com.jpp.mall.net.Utils;
import com.jpp.mall.utils.StackManager;
import com.jpp.mall.view.ProgressAlertDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/24.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    private ProgressAlertDialog alertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        doBeforeSetContentView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        ImmersionBar.with(this).init();
        StackManager.getInstance().pushActivity(this);
        initData();
    }

    public void doBeforeSetContentView(){}


    /**
     * 线程调度
     */
    protected <T> ObservableTransformer<T, T> compose(final LifecycleTransformer<T> lifecycle) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                // 可添加网络连接判断等
                                if (!Utils.isNetworkAvailable(BaseActivity.this)) {
                                    Toast.makeText(BaseActivity.this, R.string.toast_network_error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycle);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StackManager.getInstance().popActivity(this);
        ImmersionBar.with(this).destroy(); //必须调用该方法，防止内存泄漏
        doInDestory();
    }


    public abstract int getLayoutId();
    public abstract void initData();

    public void doInDestory(){}

    public void showLoadingDialog() {
        if(alertDialog == null){
            alertDialog = new ProgressAlertDialog(this);
        }
        alertDialog.show();
    }

    public void dimissLoadingDialog(){
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }

    public boolean isDialogShowing(){
        if(alertDialog != null){
            if(alertDialog.isShowing()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }



}
