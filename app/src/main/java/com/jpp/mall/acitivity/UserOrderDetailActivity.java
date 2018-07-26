package com.jpp.mall.acitivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jpp.mall.R;
import com.jpp.mall.adapter.OrderDetailAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.OrdelDetail;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 订单类型 0，平台订单（纯购买传都0） 1：到店维修 2：自主维修 3：上门维修
 */
public class UserOrderDetailActivity extends BaseActivity {
    private static final String TAG = "UserOrderDetailActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recpet_name)
    TextView nameEt;
    @BindView(R.id.input_phone)
    TextView phoneEt;
    @BindView(R.id.input_address)
    TextView addressEt;
    @BindView(R.id.order_code)
    TextView order_code;
    @BindView(R.id.order_time)
    TextView order_time;
    @BindView(R.id.order_pay_time)
    TextView order_pay_time;
    @BindView(R.id.order_complete_time)
    TextView order_complete_time;
    @BindView(R.id.seller_orderRv)
    RecyclerView seller_orderRv;
    @BindView(R.id.order_type)
    TextView orderType;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_phone)
    TextView user_phone;
    @BindView(R.id.user_avater)
    ImageView user_avater;
    @BindView(R.id.bottom_tool)
    RelativeLayout bottomTool;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.delete_order)
    TextView delete_order;
    @BindView(R.id.memberInfo_layout)
    LinearLayout memberInfo_layout;
    private String orderCode;
    private String orderId;
    private String token;
    private OrderDetailAdapter orderDetailAdapter;
    private List<OrdelDetail.Detail> details = new ArrayList<>();
    private int type;
    private Dialog mDialog;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_user_order_detail);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_order_detail;
    }
    @Override
    public void initData() {
        title.setText(R.string.order_detail);
        Intent intent = getIntent();
        orderCode = intent.getStringExtra("orderCode");
        orderId = intent.getStringExtra("orderId");
        token = (String) SpUtils.get(this,"token","");
        type = intent.getIntExtra("type",0);
        if(type == 1){

        }else if(type == 2){
            delete_order.setVisibility(View.GONE);
            pay.setText("处理完成");
        }else if(type == 3){
            bottomTool.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        seller_orderRv.setLayoutManager(layoutManager);
        orderDetailAdapter = new OrderDetailAdapter(details,this);
        seller_orderRv.setAdapter(orderDetailAdapter);
        getOrderDetail();
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    public void getOrderDetail(){
        showLoadingDialog();
        Observable<BaseEntity<OrdelDetail>> observable = RetrofitFactory.getInstance().getOrderDetial(token,orderId);
        observable.compose(compose(this.<BaseEntity<OrdelDetail>>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<OrdelDetail>(this) {
                    @Override
                    protected void onHandleSuccess(OrdelDetail ordelDetail) {
                        dimissLoadingDialog();
                        Logger.e(TAG, "onHandleSuccess: " + ordelDetail.toString() );
                        nameEt.setText("收货人:"  + ordelDetail.addressInfo.receiver);
                        phoneEt.setText(ordelDetail.addressInfo.phone);
                        addressEt.setText("收获地址:" + ordelDetail.addressInfo.address);
                        order_code.setText("" + ordelDetail.orderCode);
                        order_pay_time.setText(""+ ordelDetail.payTime);
                        order_time.setText(""+ ordelDetail.createTime);
                        orderType.setText("" + getOrderType(ordelDetail.orderType));
                        order_complete_time.setText(""+ ordelDetail.completeTime);
                        if(ordelDetail.memberInfo != null){
                            user_name.setText("" + ordelDetail.memberInfo.name);
                            user_phone.setText("" + ordelDetail.memberInfo.phone);
                            Glide.with(UserOrderDetailActivity.this).load(ordelDetail.memberInfo.pic).into(user_avater);
                        }else {
                            memberInfo_layout.setVisibility(View.GONE);
                        }
                        details.clear();
                        details.addAll(ordelDetail.details);
                        orderDetailAdapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onHandleError(String msg, int code) {
                        super.onHandleError(msg, code);
                        dimissLoadingDialog();
                    }
                });


    }

    /**
     * 订单类型 0，平台订单（纯购买传都0） 1：到店维修 2：自主维修 3：上门维修
     * @param type
     * @return
     */
    public String getOrderType(int type){
        if(type == 0){
            return "平台订单";
        }else if(type == 1){
            return "到店维修";
        }else if(type == 2){
            return "自主维修";
        }else if(type == 3){
            return "上门维修";
        }
        return  "";
    }



    @OnClick(R.id.pay)
    public void pay(View view){
        switch (type){
            case 1:
                showLoadingDialog();
                Observable<BaseEntity> observable = RetrofitFactory.getInstance().acceptOrder(token,orderId);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver(this) {
                            @Override
                            protected void onHandleSuccess(Object o) {
                                dimissLoadingDialog();
                                ToastUtils.showShortToast(UserOrderDetailActivity.this,"已接单");
                                UserOrderDetailActivity.this.finish();
                            }

                            @Override
                            protected void onHandleError(String msg, int code) {
                                super.onHandleError(msg, code);
                                dimissLoadingDialog();
                            }
                        });

                break;
            case 2:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent,3);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    sureOrder(result);
                   // Toast.makeText(UserOrderDetailActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(UserOrderDetailActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void sureOrder(String code) {
        if (orderId != null) {
            showLoadingDialog();
            String token = (String) SpUtils.get(this, "token", "");
            Observable<BaseEntity> observable1 = RetrofitFactory.getInstance().confirmOrder(token, orderId, code);
            observable1.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver(this) {
                        @Override
                        protected void onHandleSuccess(Object o) {
                            dimissLoadingDialog();
                            ToastUtils.showShortToast( UserOrderDetailActivity.this,"处理完成");
                            UserOrderDetailActivity.this.finish();
                        }

                        @Override
                        protected void onHandleError(String msg, int code) {
                            super.onHandleError(msg, code);
                            dimissLoadingDialog();
                        }
                    });
        }
    }



    @OnClick(R.id.delete_order)
    public void delete(View view){
        switch (type){
            case 1:
                showRefuseDialog();
                break;

        }
    }


    private void showRefuseDialog() {
        mDialog = new Dialog(this, R.style.common_dialog_style);
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
                    ToastUtils.showShortToast(UserOrderDetailActivity.this,"请输入拒绝理由");
                }else {
                    refuse(inputStr);
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

    public void refuse(String inputStr){
        showLoadingDialog();
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().refuseOrder(token,orderId,inputStr);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(this) {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        ToastUtils.showShortToast( UserOrderDetailActivity.this,"订单已取消");
                        if(mDialog != null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                        dimissLoadingDialog();
                        UserOrderDetailActivity.this.finish();
                    }

                    @Override
                    protected void onHandleError(String msg,int code) {
                        super.onHandleError(msg,code);
                        Logger.e(TAG,msg);
                        if(mDialog != null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }
                    }
                });

    }




}
