package com.jpp.mall.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jpp.mall.Contanst;
import com.jpp.mall.R;
import com.jpp.mall.adapter.SureOrderAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.ExpressModel;
import com.jpp.mall.bean.PlaceOrderModel;
import com.jpp.mall.bean.ShopInfoModel;
import com.jpp.mall.bean.SureOrderModer;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;
import com.pingplusplus.ui.PaymentHandler;
import com.pingplusplus.ui.PingppUI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SureOrderActivity extends BaseActivity implements  SureOrderAdapter.OnItemClickListener {
    private static final String TAG = "SureOrderActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sumbit_tv)
    TextView submitTv;
    @BindView(R.id.recpet_name)
    TextView nameEt;
    @BindView(R.id.input_phone)
    TextView phoneEt;
    @BindView(R.id.input_address)
    TextView addressEt;
    @BindView(R.id.orderRv)
    RecyclerView orderRv;
    public SureOrderModer sureOrderModer;

    @BindView(R.id.wechat_pay_iv)
    ImageView wechat_pay_iv;

    @BindView(R.id.ali_pay_iv)
    ImageView ali_pay_iv;

    @BindView(R.id.union_pay_iv)
    ImageView union_pay_iv;

    @BindView(R.id.sum)
    TextView sum;

    @BindView(R.id.wechat_pay)
    RelativeLayout wechat_pay;
    @BindView(R.id.ali_pay)
    RelativeLayout ali_pay;

    @BindView(R.id.union_pay)
    RelativeLayout union_pay;

    @BindView(R.id.epress_rl)
    RelativeLayout epress_rl;
    @BindView(R.id.ss_tv)
    TextView ss_tv;
    private String mPayCharge;

    private String expressId; //物流id

    private List<ExpressModel> expressModelList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_sure_order;
    }
    @Override
    public void initData() {
        title.setText(R.string.sure_order);
        getShopInfo();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        orderRv.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        if(intent.hasExtra(Contanst.GOODS_BY_DETAIL)){
            sureOrderModer = (SureOrderModer) intent.getSerializableExtra(Contanst.GOODS_BY_DETAIL);
        }else if(intent.hasExtra(Contanst.GOODS_BY_CART)){
            sureOrderModer = (SureOrderModer) intent.getSerializableExtra(Contanst.GOODS_BY_CART);
        }else if(intent.hasExtra(Contanst.GOODS_BY_DETAIL_ROUND)){
            sureOrderModer = (SureOrderModer) intent.getSerializableExtra(Contanst.GOODS_BY_DETAIL_ROUND);
        }
        double sumMoney  = culSumMoney(sureOrderModer);
        sum.setText("合计:¥"+ sumMoney);
        SureOrderAdapter sellerOrderAdapter = new SureOrderAdapter(sureOrderModer,SureOrderActivity.this);
        orderRv.setAdapter(sellerOrderAdapter);
    }

    public double culSumMoney(SureOrderModer sureOrderModer ){
        List<SureOrderModer.CartOrder> data = sureOrderModer.data;
        double sumMoney = 0;
        for (int i = 0; i < data.size(); i++) {
            sumMoney  += data.get(i).price * data.get(i).num;
        }
        Logger.e(TAG,"sumMoney" + sumMoney);
        return sumMoney;
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.sumbit_tv)
    public void submit(View view) {
        submitOrder();
    }

    @OnClick(R.id.epress_rl)
    public void getExpressList(View view) {
        Observable<BaseEntity<List<ExpressModel>>> observable = RetrofitFactory.getInstance().getExpressList();
        observable.compose(compose(this.<BaseEntity<List<ExpressModel>>>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<ExpressModel>>(this) {
                    @Override
                    protected void onHandleSuccess(List<ExpressModel> expressModels) {
                        if(expressModels != null && !expressModels.isEmpty()){
                            expressModelList.clear();
                            expressModelList.addAll(expressModels);
                            List<String> expressNames = new ArrayList<>();
                            for(ExpressModel expressModel : expressModels){
                                expressNames.add(expressModel.expressName);
                            }
                            showPickerView(expressNames);
                        }
                    }
                });
    }

    /**
     * 提交订单 ，完成之后跳转支付
     */
    public void submitOrder(){
        try {
            if(TextUtils.isEmpty(expressId)){
                ToastUtils.showShortToast(this,"请选择物流方式");
                return;
            }
            String json = "";
            if (sureOrderModer != null) {
                List<SureOrderModer.CartOrder> data = sureOrderModer.data;
                JSONArray jsonArray = new JSONArray();
                for (SureOrderModer.CartOrder cartOrder : data) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("goodsId", cartOrder.goodsId);
                    jsonObject.put("num", cartOrder.num);
                    jsonObject.put("skuId", cartOrder.skuId);
                    jsonArray.put(jsonObject);
                }
                json = jsonArray.toString();
            }
            final String token = (String) SpUtils.get(SureOrderActivity.this, "token", "");
            if(isDialogShowing()){
                return;
            }
            Logger.e(TAG,json);
            showLoadingDialog();//29DF8145F74DD1AC2234E8BF08539970
            Observable<BaseEntity<PlaceOrderModel>> observable = RetrofitFactory.getInstance().placeOrder(token
                    , json, shopInfoM.shopInfo.name, shopInfoM.phone, shopInfoM.shopInfo.fullAddress, 0, shopInfoM.shopInfo.shopId,"",expressId);
            observable.compose(compose(this.<BaseEntity<PlaceOrderModel>>bindToLifecycle()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<PlaceOrderModel>(this) {
                        @Override
                        protected void onHandleSuccess(PlaceOrderModel placeOrderModel) {
                            Logger.e(TAG, placeOrderModel.toString());
                            dimissLoadingDialog();
                            ToastUtils.showShortToast(SureOrderActivity.this, getResources().getString(R.string.submit_order_success));
                            getPayCharge(placeOrderModel.orderCode,token,payType);
                        }

                        @Override
                        protected void onHandleError(String msg, int code) {
                            super.onHandleError(msg, code);
                            dimissLoadingDialog();
                        }
                    });
        } catch (Exception e) {
            ToastUtils.showShortToast(SureOrderActivity.this, getResources().getString(R.string.submit_order_exception));
        }
    }


    /**
     * 服务端获取 charge/order
     * @return
     */
    public void getPayCharge(String orderCode,String token,int payType) {
        Observable<BaseEntity<String>> observable = RetrofitFactory.getInstance().getPayInfo(orderCode,payType,token);
        observable.compose(compose(this.<BaseEntity<String>>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    protected void onHandleSuccess(String s) {
                        // Log.e(TAG, payInfoModel.toString());
                        pay(SureOrderActivity.this, s);
                    }
                });
    }

    /**
     * @param context
     * @param data  charge/order 字符串
     *              PaymentHandler 支付结果回调类
     */
    public void pay(final Activity context , String data){
//        Pingpp.createPayment(context, data);
        PingppUI.createPay(context, data, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                int code = data.getExtras().getInt("code");
                String result = data.getExtras().getString("result");
                Logger.e(TAG, "handlePaymentResult: "+ code + "---" + result );
                if(code == 1){ //成功
                    startActivity(new Intent(SureOrderActivity.this,MainAcitivty.class));
                    SureOrderActivity.this.finish();
                }else {
                    ToastUtils.showShortToast(context,""+ result);
                }
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
    }

    private  int payType = 100;
    @OnClick(R.id.wechat_pay)
    public void clickWechatPay(View view){
        wechat_pay_iv.setImageResource(R.drawable.bt_radialon);
        ali_pay_iv.setImageResource(R.drawable.bt_radialoff);
        union_pay_iv.setImageResource(R.drawable.bt_radialoff);
        payType = 100;
    }

    @OnClick(R.id.ali_pay)
    public void clickAlipay(View view){
        ali_pay_iv.setImageResource(R.drawable.bt_radialon);
        wechat_pay_iv.setImageResource(R.drawable.bt_radialoff);
        union_pay_iv.setImageResource(R.drawable.bt_radialoff);
        payType = 200;
    }

    @OnClick(R.id.union_pay)
    public void clickUnionpay(View view){
        union_pay_iv.setImageResource(R.drawable.bt_radialon);
        wechat_pay_iv.setImageResource(R.drawable.bt_radialoff);
        ali_pay_iv.setImageResource(R.drawable.bt_radialoff);
        payType = 300;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
//                    ToastUtils.showShortToast(this,"支付成功");
//                    SureOrderActivity.this.finish();
//                }else if("fail".equals(result)){
//                    ToastUtils.showShortToast(this,"取消支付");
//                }else if("cancel".equals(result)){
//                    ToastUtils.showShortToast(this,"支付失败");
//                }else if("invalid".equals(result)){
//                    ToastUtils.showShortToast(this,"支付插件未安装");
//                }else if("unknown".equals(result)){
//                    ToastUtils.showShortToast(this,"支付失败");
//                }
//
//                startActivity(new Intent(SureOrderActivity.this,MainAcitivty.class));
//                SureOrderActivity.this.finish();
//               // showMsg(result, errorMsg, extraMsg);
//            }
//        }
//    }

    private ShopInfoModel shopInfoM;

    public void getShopInfo() {
        String token = (String) SpUtils.get(this,"token","");
        Observable<BaseEntity<ShopInfoModel>> observable = RetrofitFactory.getInstance().getShopInfo(token);
        observable.compose(compose(this.<BaseEntity<ShopInfoModel>>bindToLifecycle())).subscribe(new BaseObserver<ShopInfoModel>(this) {
            @Override
            protected void onHandleSuccess(ShopInfoModel shopInfoModel) {
                // 保存用户信息等操作
                shopInfoM = shopInfoModel;
                Logger.e(TAG, "onHandleSuccess:" + shopInfoModel.toString() );
                nameEt.setText("收货人:"  + shopInfoModel.shopInfo.name);
                phoneEt.setText(shopInfoModel.phone);
                addressEt.setText("收获地址:" + shopInfoModel.shopInfo.fullAddress);
                // shopId.setText(shopInfoModel.shopInfo.shopId);
            }
        });
    }

    /**
     * 弹出选择器
     */
    private void showPickerView(final List<String> expressModels) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                expressId = expressModelList.get(options1).expressId;
                ss_tv.setText("选择物流方式:"+ expressModelList.get(options1).expressName);
            }
        })
                .setTitleText("选择物流方式")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(16)
                .build();

        pvOptions.setPicker(expressModels);//一级选择器
        //pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }



}
