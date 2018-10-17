package com.jpp.mall.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.OrderDetailAdapter;
import com.jpp.mall.adapter.SellerOrderAdapterRv;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.OrdelDetail;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.pingplusplus.ui.CHANNELS;
import com.pingplusplus.ui.ChannelListener;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailActivity extends BaseActivity implements OrderDetailAdapter.OnItemClickListener, SellerOrderAdapterRv.OnItemClickListener {
    private static final String TAG = "OrderDetailActivity";
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
    @BindView(R.id.seller_orderRv)
    RecyclerView seller_orderRv;

    @BindView(R.id.order_code)
    TextView order_code;
    @BindView(R.id.order_time)
    TextView order_time;
    @BindView(R.id.order_pay_time)
    TextView order_pay_time;
    @BindView(R.id.order_complete_time)
    TextView order_complete_time;
    @BindView(R.id.sum)
    TextView sum;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.delete_order)
    TextView delete_order;
    @BindView(R.id.tools)
    RelativeLayout tools;
    @BindView(R.id.wuliu_company)
    TextView wuliu_company;
    @BindView(R.id.wuliu_num)
    TextView wuliu_num;
    private String orderCode;
    private String orderId;
    private int type;


    private List<OrdelDetail.Detail> details = new ArrayList<>();
    private OrderDetailAdapter orderDetailAdapter;
    private String token;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_order_detail);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initData() {
        title.setText(R.string.order_detail);
        Intent intent = getIntent();
        orderCode = intent.getStringExtra("orderCode");
        orderId = intent.getStringExtra("orderId");
        type = intent.getIntExtra("type",0);
        if(type == 1){

        }else if(type == 2){
            tools.setVisibility(View.GONE);
        }else if(type == 3){
            delete_order.setVisibility(View.GONE);
            pay.setText("确认收货");
        }else if(type == 4){
            tools.setVisibility(View.GONE);
        }
        token = (String) SpUtils.get(this,"token","");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        seller_orderRv.setLayoutManager(layoutManager);
        orderDetailAdapter = new OrderDetailAdapter(details,this);
        orderDetailAdapter.setOnItemClickListener(this);
        seller_orderRv.setAdapter(orderDetailAdapter);
//        orderDetailAdapter.setOnPayListener(this);
//        orderDetailAdapter.setOnDeleteListener(this);
        getOrderDetail();

    }
    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @Override
    public void onItemClick(int pos) {

    }

    @OnClick(R.id.pay)
    public void pay(View view){
        if(type == 1){
            //CHANNELS.UPACP 银联
            CHANNELS[] channels = new CHANNELS[]{CHANNELS.ALIPAY, CHANNELS.WX,CHANNELS.UPACP};
            PingppUI.enableChannels(channels);

// 参数一: context 上下文对象
// 参数二: ChannelListener 选择渠道回调类
            PingppUI.showPaymentChannels(this, new ChannelListener() {
                @Override public void selectChannel(String channel) {
                    // channel 为用户选择的支付渠道
                    Log.e(TAG, "selectChannel: " + channel );
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
                    getPayCharge(orderCode,token,payType);

                }
            });
        }else if(type == 4){

        }

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
                        order_complete_time.setText(""+ ordelDetail.completeTime);
                        sum.setText("合计:￥"+ ordelDetail.sumPrice);
                        wuliu_company.setText(ordelDetail.expressInfo.expressName + "");
                        wuliu_num.setText(ordelDetail.expressInfo.expressNumber + "");
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

    @OnClick(R.id.delete_order)
    public void delete(View view){
        deleteOrder();
    }

    public void deleteOrder(){
        showLoadingDialog();
        Observable<BaseEntity> observable = RetrofitFactory.getInstance().removeOrder(token,orderId);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver(this) {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        dimissLoadingDialog();
                        ToastUtils.showShortToast(OrderDetailActivity.this,"删除成功");
                        OrderDetailActivity.this.finish();
                    }

                    @Override
                    protected void onHandleError(String msg,int code) {
                        super.onHandleError(msg,code);
                        Logger.e(TAG,msg);
                        dimissLoadingDialog();
                    }
                });
    }


    /**
     * 服务端获取 charge/order
     * @return
     */
    public void getPayCharge(String orderCode, String token, int payType) {
        Observable<BaseEntity<String>> observable = RetrofitFactory.getInstance().getPayInfo(orderCode,payType,token);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    protected void onHandleSuccess(String s) {
                        // Log.e(TAG, payInfoModel.toString());
                        pay(OrderDetailActivity.this, s);
                    }
                });
    }


    /**
     * @param context
     * @param data  charge/order 字符串
     *              PaymentHandler 支付结果回调类
     */
    public void pay(Activity context , String data ){
        //Pingpp.createPayment(context, data);

        PingppUI.createPay(context, data, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                int code = data.getExtras().getInt("code");
                String result = data.getExtras().getString("result");
                ToastUtils.showShortToast(OrderDetailActivity.this,""+ result);
//                if(code == 1){
//                    details.remove(pos);
//                    orderDetailAdapter.notifyItemRemoved(pos);
//                }else {
//                    ToastUtils.showShortToast(OrderDetailActivity.this,""+ result);
//                }
                //Log.e(TAG, "handlePaymentResult: "+ code + "---" + result );
            }
        });
    }

}
