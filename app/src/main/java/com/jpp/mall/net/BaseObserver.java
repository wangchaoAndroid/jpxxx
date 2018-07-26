package com.jpp.mall.net;

import android.content.Context;
import android.widget.Toast;

import com.jpp.mall.utils.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/4/24.
 */

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {

    private static final String TAG = "BaseObserver";
    private Context mContext;


    protected BaseObserver(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(BaseEntity<T> value) {

        if(value != null){
            Logger.e(TAG,value.isSuccess() + "---" + value.getCode() + "---" + value.getMsg() + "---" + value.getData());
        }
        if (value.isSuccess()) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getMsg(),value.getCode());
        }
    }


    @Override
    public void onError(Throwable e) {
        Logger.e(TAG, "error:" + e.toString());
    }

    @Override
    public void onComplete() {
        Logger.d(TAG, "onComplete");
    }


    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg,int code) {
        Toast.makeText(mContext, msg,Toast.LENGTH_SHORT).show();
    }
}

