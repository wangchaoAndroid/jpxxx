package com.jpp.mall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jpp.mall.R;
import com.jpp.mall.acitivity.UserOrderDetailActivity;
import com.jpp.mall.adapter.UserOrderAdapter2;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.ProgressAlertDialog;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/26.
 */

public class UserOrderTwoFragment extends Fragment implements UserOrderAdapter2.OnItemClickListener, UserOrderAdapter2.OnPayListener {
    private static final String TAG = "UserOrderTwoFragment";
    @BindView(R.id.seller_order_rv)
    RecyclerView sellerOrderRv;
    private List<SellerOrder.Data> sellerOrders = new ArrayList<>();
    private UserOrderAdapter2 userOrderAdapter2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_seller_order_one,container,false);
        ButterKnife.bind(this,view);
        initData();
        return view;
    }

    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sellerOrderRv.setLayoutManager(layoutManager);
        userOrderAdapter2 = new UserOrderAdapter2(sellerOrders,getActivity());
        sellerOrderRv.setAdapter(userOrderAdapter2);
        userOrderAdapter2.setOnItemClickListener(this);
        userOrderAdapter2.setOnPayListener(this);
        String token = (String) SpUtils.get(getActivity(),"token","");
        getUserOrder(token,"2");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()){
            String token = (String) SpUtils.get(getActivity(),"token","");
            getUserOrder(token,"2");
        }
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), UserOrderDetailActivity.class);
        intent.putExtra("orderCode",sellerOrders.get(pos).orderCode);
        intent.putExtra("orderId",sellerOrders.get(pos).orderId);
        intent.putExtra("type",2);
        startActivity(intent);
    }

    private ProgressAlertDialog alertDialog;

    public void showLoadingDialog() {
        if(alertDialog == null){
            alertDialog = new ProgressAlertDialog(getActivity());
        }
        alertDialog.show();
    }

    public void dimissLoadingDialog(){
        if(alertDialog != null){
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

    public void getUserOrder(String token ,String status) {
        showLoadingDialog();
        Observable<SellerOrder> observable = RetrofitFactory.getInstance().getMemberOrders(token,status);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SellerOrder>() {
                    @Override
                    public void accept(SellerOrder sellerOrder) throws Exception {
                        sellerOrders.clear();
                        sellerOrders.addAll(sellerOrder.data);
                        userOrderAdapter2.notifyDataSetChanged();
                        dimissLoadingDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                     //   Log.e(TAG,throwable.getMessage());
                        dimissLoadingDialog();
                    }
                });

    }

    @Override
    public void onPay(int pos) {
        data = sellerOrders.get(pos);
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent,2);
    }

    private SellerOrder.Data data;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    sureOrder(result);
                   // Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void sureOrder(String code){
        if(data != null){
            showLoadingDialog();
            String token = (String) SpUtils.get(getActivity(),"token","");
            String orderId = data.orderId;
            Observable<BaseEntity> observable1 = RetrofitFactory.getInstance().confirmOrder(token,orderId, code);
            observable1.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(getActivity()) {
                        @Override
                        protected void onHandleSuccess(Object o) {
                            sellerOrders.remove(data);
                            userOrderAdapter2.notifyDataSetChanged();
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


}
