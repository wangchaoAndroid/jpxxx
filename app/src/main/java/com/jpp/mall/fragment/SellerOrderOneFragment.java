package com.jpp.mall.fragment;

import android.app.Activity;
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
import com.jpp.mall.adapter.SellerOrderAdapterRv;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.ProgressAlertDialog;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.pingplusplus.ui.CHANNELS;
import com.pingplusplus.ui.ChannelListener;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

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

public class SellerOrderOneFragment extends Fragment implements SellerOrderAdapterRv.OnItemClickListener, SellerOrderAdapterRv.OnPayListener, SellerOrderAdapterRv.OnDeleteListener {
    private static final String TAG = "SellerOrderOneFragment";
    @BindView(R.id.seller_order_rv)
    RecyclerView sellerOrderRv;
    private List<SellerOrder.Data> sellerOrders = new ArrayList<>();
    private SellerOrderAdapterRv sellerOrderAdapter;
    private String token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_seller_order_one,container,false);
        ButterKnife.bind(this,view);
        initData();
        return view;

    }

    private void initData() {
        token = (String) SpUtils.get(getActivity(),"token","");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sellerOrderRv.setLayoutManager(layoutManager);
        sellerOrderAdapter = new SellerOrderAdapterRv(sellerOrders,getActivity());
        sellerOrderAdapter.setOnItemClickListener(this);
        sellerOrderRv.setAdapter(sellerOrderAdapter);
        sellerOrderAdapter.setOnPayListener(this);
        sellerOrderAdapter.setOnDeleteListener(this);
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint() returned: " + isVisibleToUser + "---" + isResumed());
        if(isVisibleToUser && isResumed()){
            getOrder(token,"0");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrder(token,"0");
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderCode",sellerOrders.get(pos).orderCode);
        intent.putExtra("orderId",sellerOrders.get(pos).orderId);
        intent.putExtra("type",1);
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
                        Logger.e(TAG,"" + sellerOrder.toString());
                        dimissLoadingDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        Log.e(TAG,throwable.getMessage());
                        dimissLoadingDialog();
                    }
                });

    }

    @Override
    public void onPay(final int pos) {
        //CHANNELS.UPACP 银联
        CHANNELS[] channels = new CHANNELS[]{CHANNELS.ALIPAY, CHANNELS.WX,CHANNELS.UPACP};
        PingppUI.enableChannels(channels);

// 参数一: context 上下文对象
// 参数二: ChannelListener 选择渠道回调类
        PingppUI.showPaymentChannels(getActivity(), new ChannelListener() {
            @Override public void selectChannel(String channel) {
                // channel 为用户选择的支付渠道
                Logger.e(TAG, "selectChannel: " + channel );
                int payType = 0;
                switch (channel){
                    case "alipay":
                        payType = 200;
                        break;
                    case "wx":
                        payType = 100;
                        break;
                    case "upacp":
                        payType = 300;
                        break;
                }
                getPayCharge(sellerOrders.get(pos).orderCode,token,payType, pos);
            }
        });
    }



    /**
     * 服务端获取 charge/order
     * @return
     */
    public void getPayCharge(String orderCode, String token, int payType, final int pos) {
        Observable<BaseEntity<String>> observable = RetrofitFactory.getInstance().getPayInfo(orderCode,payType,token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(String s) {
                        // Log.e(TAG, payInfoModel.toString());
//                        Gson gson = new Gson();
//                        String s = gson.toJson(payInfoModel);
                        pay(getActivity(), s,pos);
                    }
                });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //支付页面返回处理
//        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                String result = data.getExtras().getString("pay_result");
//            /* 处理返回值
//             * "success" - 支付
//             * 成功
//             * "fail"    - 支付失败
//             * "cancel"  - 取消支付
//             * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
//             * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
//             */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                Logger.e(TAG, "onActivityResult: "+ errorMsg + "---" + extraMsg + "---" +  result);
//                if("success".equals(result)){
//                    ToastUtils.showShortToast(getActivity(),"支付成功");
//                }else if("fail".equals(result)){
//                    ToastUtils.showShortToast(getActivity(),"取消支付");
//                }else if("cancel".equals(result)){
//                    ToastUtils.showShortToast(getActivity(),"支付失败");
//                }else if("invalid".equals(result)){
//                    ToastUtils.showShortToast(getActivity(),"支付插件未安装");
//                }else if("unknown".equals(result)){
//                    ToastUtils.showShortToast(getActivity(),"支付失败");
//                }
//
//                // showMsg(result, errorMsg, extraMsg);
//            }
//        }
//    }


    /**
     * @param context
     * @param data  charge/order 字符串
     *              PaymentHandler 支付结果回调类
     */
    public void pay(Activity context , String data , final int pos){
        //Pingpp.createPayment(context, data);

        PingppUI.createPay(context, data, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                int code = data.getExtras().getInt("code");
                String result = data.getExtras().getString("result");
                if(code == 1){
                    sellerOrders.remove(pos);
                    sellerOrderAdapter.notifyItemRemoved(pos);
                }else {
                    ToastUtils.showShortToast(getActivity(),""+ result);
                }
                Logger.e(TAG, "handlePaymentResult: "+ code + "---" + result );
            }
        });
    }


    @Override
    public void onDelete(int pos) {
        deleteOrder(pos);
    }

    public void deleteOrder(final int pos){
        showLoadingDialog();
        String orderId = sellerOrders.get(pos).orderId;
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().removeOrder(token,orderId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(getActivity()) {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        dimissLoadingDialog();
                        sellerOrders.remove(pos);
                        sellerOrderAdapter.notifyDataSetChanged();
                        ToastUtils.showShortToast(getActivity(),"删除成功");
                    }

                    @Override
                    protected void onHandleError(String msg,int code) {
                        super.onHandleError(msg,code);
                        Logger.e(TAG,msg + "");
                        dimissLoadingDialog();
                    }
                });

    }
}
