package com.jpp.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpp.mall.R;
import com.jpp.mall.acitivity.OrderDetailActivity;
import com.jpp.mall.adapter.SellerOrderAdapterRv2;
import com.jpp.mall.bean.SellerOrder;
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

public class SellerOrderTwoFragment extends Fragment implements SellerOrderAdapterRv2.OnItemClickListener {
    @BindView(R.id.seller_order_rv)
    RecyclerView sellerOrderRv;
    private static final String TAG = "SellerOrderTwoFragment";

    private List<SellerOrder.Data> sellerOrders = new ArrayList<>();
    private SellerOrderAdapterRv2 sellerOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_seller_order_one,container,false);
        ButterKnife.bind(this,view);
        initData();
        return view;

    }

    private void initData() {
        String token = (String) SpUtils.get(getActivity(),"token","");
        getOrder(token,"1");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sellerOrderRv.setLayoutManager(layoutManager);
        sellerOrderAdapter = new SellerOrderAdapterRv2(sellerOrders,getActivity());
        sellerOrderRv.setAdapter(sellerOrderAdapter);
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
                        sellerOrders.clear();
                        sellerOrders.addAll(sellerOrder.data);
                        sellerOrderAdapter.notifyDataSetChanged();
                        Logger.e(TAG,sellerOrder.toString());
                        dimissLoadingDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                       // Log.e(TAG,throwable.getMessage());
                        dimissLoadingDialog();
                    }
                });

    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderCode",sellerOrders.get(pos).orderCode);
        intent.putExtra("orderId",sellerOrders.get(pos).orderId);
        intent.putExtra("type",2);
        startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()){
            String token = (String) SpUtils.get(getActivity(),"token","");
            getOrder(token,"1");
        }
    }
}
