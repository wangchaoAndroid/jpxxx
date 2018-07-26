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
import com.jpp.mall.acitivity.UserOrderDetailActivity;
import com.jpp.mall.adapter.UserOrderAdapter3;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.net.RetrofitFactory;
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

public class UserOrderThreeFragment extends Fragment implements UserOrderAdapter3.OnItemClickListener {
    private static final String TAG = "UserOrderThreeFragment";
    @BindView(R.id.seller_order_rv)
    RecyclerView sellerOrderRv;
    private List<SellerOrder.Data> sellerOrders = new ArrayList<>();
    private UserOrderAdapter3 userOrderAdapter3;

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
        userOrderAdapter3 = new UserOrderAdapter3(sellerOrders,getActivity());
        sellerOrderRv.setAdapter(userOrderAdapter3);
        userOrderAdapter3.setOnItemClickListener(this);
        String token = (String) SpUtils.get(getActivity(),"token","");
        getUserOrder(token,"3");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()){
            String token = (String) SpUtils.get(getActivity(),"token","");
            getUserOrder(token,"3");
        }
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), UserOrderDetailActivity.class);
        intent.putExtra("orderCode",sellerOrders.get(pos).orderCode);
        intent.putExtra("orderId",sellerOrders.get(pos).orderId);
        intent.putExtra("type",3);
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
                        userOrderAdapter3.notifyDataSetChanged();
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

}
