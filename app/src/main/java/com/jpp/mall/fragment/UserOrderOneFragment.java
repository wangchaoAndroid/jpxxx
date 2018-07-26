package com.jpp.mall.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.jpp.mall.R;
import com.jpp.mall.acitivity.UserOrderDetailActivity;
import com.jpp.mall.adapter.UserOrderAdapter;
import com.jpp.mall.bean.SellerOrder;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.ProgressAlertDialog;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;

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

public class UserOrderOneFragment extends Fragment implements UserOrderAdapter.OnItemClickListener, UserOrderAdapter.OnPayListener, UserOrderAdapter.OnDeleteListener {
    @BindView(R.id.seller_order_rv)
    RecyclerView sellerOrderRv;
    private static final String TAG = "UserOrderOneFragment";
    private List<SellerOrder.Data> sellerOrders = new ArrayList<>();
    private UserOrderAdapter userOrderAdapter;
    private String token;
    private Dialog mDialog;

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
        userOrderAdapter = new UserOrderAdapter(sellerOrders,getActivity());
        sellerOrderRv.setAdapter(userOrderAdapter);
        userOrderAdapter.setOnItemClickListener(this);
        userOrderAdapter.setOnPayListener(this);
        userOrderAdapter.setOnDeleteListener(this);
        token = (String) SpUtils.get(getActivity(),"token","");
        getUserOrder(token,"1");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()){
            getUserOrder(token,"1");
        }
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(getActivity(), UserOrderDetailActivity.class);
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

    public void getUserOrder(String token ,String status) {
        showLoadingDialog();
        Observable<SellerOrder> observable = RetrofitFactory.getInstance().getMemberOrders(token,status);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SellerOrder>() {
                    @Override
                    public void accept(SellerOrder sellerOrder) throws Exception {
                        if(sellerOrder != null){
                            Logger.e(TAG,sellerOrder.toString());
                            sellerOrders.clear();
                            sellerOrders.addAll(sellerOrder.data);
                            userOrderAdapter.notifyDataSetChanged();
                        }
                        dimissLoadingDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                      //  Log.e(TAG,throwable.getMessage());
                        dimissLoadingDialog();
                    }
                });

    }




    /**
     * 确认接单
     * @param pos
     */
    @Override
    public void onPay(final int pos) {
        showLoadingDialog();
        String orderId = sellerOrders.get(pos).orderId;
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().acceptOrder(token,orderId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(getActivity()) {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        dimissLoadingDialog();
                        sellerOrders.remove(pos);
                        userOrderAdapter.notifyDataSetChanged();
                        ToastUtils.showShortToast(getActivity(),"确认接单");
                    }

                    @Override
                    protected void onHandleError(String msg,int code) {
                        super.onHandleError(msg,code);
                        dimissLoadingDialog();
                    }
                });

    }


    /**
     * 拒绝订单
     * @param pos
     */
    @Override
    public void onDelete(int pos) {
        deleteOrder(pos);
    }


    public void deleteOrder( int pos){
        showRefuseDialog(pos);
        //refuse(pos);
    }

    private void showRefuseDialog(final int pos) {
        mDialog = new Dialog(getActivity(), R.style.common_dialog_style);
        // setContentView可以设置为一个View也可以简单地指定资源ID
        mDialog.setContentView(R.layout.dialog_refuse);
        View decorView = mDialog.getWindow().getDecorView();
        final EditText editText = (EditText) decorView.findViewById(R.id.input);
        decorView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
            }
        });

        decorView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputStr = editText.getText().toString().trim();
                if(TextUtils.isEmpty(inputStr)){
                    ToastUtils.showShortToast(getActivity(),"请输入拒绝理由");
                }else {
                    refuse(pos,inputStr);
                }
            }
        });
        mDialog.show();//要放在最前面
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8); // 宽度
        lp.alpha = 0.9f; // 透明度
// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
// dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp); // WindowManager m = getWindowManager();

    }

    public void refuse(final int pos,String inputStr){
        showLoadingDialog();
        String orderId = sellerOrders.get(pos).orderId;
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().refuseOrder(token,orderId,inputStr);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(getActivity()) {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        dimissLoadingDialog();
                        sellerOrders.remove(pos);
                        userOrderAdapter.notifyDataSetChanged();
                        ToastUtils.showShortToast(getActivity(),"订单已取消");
                        if(mDialog != null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    protected void onHandleError(String msg,int code) {
                        super.onHandleError(msg,code);
                        dimissLoadingDialog();
                        Logger.e(TAG,msg + "");
                        if(mDialog != null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                    }
                });

    }
}
