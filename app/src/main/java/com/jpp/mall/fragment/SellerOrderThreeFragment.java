package com.jpp.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.acitivity.OrderDetailActivity;
import com.jpp.mall.adapter.SellerOrderAdapterRv3;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.ProgressAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/26.
 */

public class SellerOrderThreeFragment extends Fragment implements SellerOrderAdapterRv3.OnPayListener, SellerOrderAdapterRv3.OnItemClickListener {
    private static final String TAG = "SellerOrderThreeFragmen";
    @BindView(R.id.seller_order_rv)
    RecyclerView sellerOrderRv;

    private List<SellerOrder.Data> sellerOrders = new ArrayList<>();
    private SellerOrderAdapterRv3 sellerOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_seller_order_one,container,false);
        ButterKnife.bind(this,view);
        initData();
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()){
            String token = (String) SpUtils.get(getActivity(),"token","");
            getOrder(token,"2");
        }
    }

    private void initData() {
        String token = (String) SpUtils.get(getActivity(),"token","");
        getOrder(token,"2");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sellerOrderRv.setLayoutManager(layoutManager);
        sellerOrderAdapter = new SellerOrderAdapterRv3(sellerOrders,getActivity());
        sellerOrderRv.setAdapter(sellerOrderAdapter);
        sellerOrderAdapter.setOnPayListener(this);
        sellerOrderAdapter.setOnItemClickListener(this);
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

    public void getOrder(String token ,String status) {
        showLoadingDialog();
        Observable<SellerOrder> observable = RetrofitFactory.getInstance().getSellerOrder(token,status);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SellerOrder>() {
                    @Override
                    public void accept(SellerOrder sellerOrder) throws Exception {
                        if(sellerOrder != null){
                            sellerOrders.clear();
                            sellerOrders.addAll(sellerOrder.data);
                            sellerOrderAdapter.notifyDataSetChanged();
                            Logger.e(TAG,"" + sellerOrder.toString());
                        }
                        dimissLoadingDialog();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dimissLoadingDialog();
                    }
                });

    }

    @Override
    public void onPay(final int pos) {
        showLoadingDialog();
        String token = (String) SpUtils.get(getActivity(),"token","");
        String orderId = sellerOrders.get(pos).orderId;
        Observable<BaseEntity> observable1 = RetrofitFactory.getInstance().confirmOrderShop(token,orderId
        );
        observable1.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(getActivity()) {
            @Override
            protected void onHandleSuccess(Object o) {
                sellerOrders.remove(pos);
                sellerOrderAdapter.notifyItemRemoved(pos);
                dimissLoadingDialog();
            }

            @Override
            protected void onHandleError(String msg, int code) {
                super.onHandleError(msg, code);
                Logger.e(TAG,""+ code + "---" + msg);
                dimissLoadingDialog();
            }
        });

    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderCode",sellerOrders.get(pos).orderCode);
        intent.putExtra("orderId",sellerOrders.get(pos).orderId);
        intent.putExtra("type",3);
        startActivity(intent);
    }
}
