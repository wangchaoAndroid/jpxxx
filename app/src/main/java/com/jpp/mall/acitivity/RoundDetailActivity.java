package com.jpp.mall.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.jpp.mall.Contanst;
import com.jpp.mall.R;
import com.jpp.mall.adapter.RoundDetailSkuAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.CommonModel;
import com.jpp.mall.bean.RoundDetailModel;
import com.jpp.mall.bean.SureOrderModer;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.view.AmountView;
import com.jpp.mall.view.citypickerview.style.citylist.Toast.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RoundDetailActivity extends BaseActivity implements RoundDetailSkuAdapter.OnItemClickListener {
    private static final String TAG = "DetailActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add_cart)
    TextView addCart;
    @BindView(R.id.componet_name)
    TextView componet_name;
    @BindView(R.id.sum)
    TextView sum;
    @BindView(R.id.sure_order)
    TextView sureOrder;
    @BindView(R.id.goods_imgs)
    ImageView goodsImgs;
    @BindView(R.id.enter_cart)
    ImageView enterCart;
    @BindView(R.id.sku_rv)
    RecyclerView sku_rv;
    @BindView(R.id.amout_view)
    AmountView amountView;

    private RoundDetailModel detailModeld;

    private List<RoundDetailModel.AttrName> attrNames = new ArrayList<>();
    private List<RoundDetailModel.AttrValue> attrValues = new ArrayList<>();


    private String goodsId;
    private List<String> tempSkids  = new ArrayList<>();
    private List<RoundDetailModel.Sku> skuList = new ArrayList<>() ;
    private RoundDetailSkuAdapter detailSkuAdapter;
    private int num = 1;
    private Map<String,String> des = new LinkedHashMap<>();
    private Map<Integer,String> skus = new LinkedHashMap<>();

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_detail_round);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_round;
    }
    @Override
    public void initData() {
        Intent intent = getIntent();
        goodsId = intent.getStringExtra("goodsId");
        String token = (String) SpUtils.get(this, "token", "");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        sku_rv.setLayoutManager(linearLayoutManager);
        sku_rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        detailSkuAdapter = new RoundDetailSkuAdapter(attrNames, this);
        sku_rv.setAdapter(detailSkuAdapter);
        detailSkuAdapter.setOnItemClickListener(this);
        getDetail2(token,goodsId);
        amountView.setGoods_storage(100000);
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                //Toast.makeText(getApplicationContext(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
                num = amount;
                RoundDetailModel.Sku subSkus = getSkuID();
                if(subSkus != null){
                    sum.setText("合计 : ￥" + amount *subSkus.price );
                }else {
                    sum.setText("合计 : ￥" + amount *detailModeld.price );
                }
            }
        });
    }


    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    /**
     * 获取skuid
     * @return
     */
    public RoundDetailModel.Sku   getSkuID(){
        RoundDetailModel.Sku  sub = null;
        if(tempSkids != null){
            Arrays.sort(tempSkids.toArray());// 排序
            for (int i = 0; i < skuList.size(); i++) {
                RoundDetailModel.Sku subSkus = skuList.get(i);
                String valueIds = subSkus.valueIds;
                String[] split = valueIds.split(",");
                Arrays.sort(split);// 排序
                if(Arrays.equals(tempSkids.toArray(),split)){
                    sub = subSkus;
                    break;
                }
            }
        }
        return sub;
    }

    @OnClick(R.id.sure_order)
    public void sureOrder(View view){
        for (int i = 0; i < tempSkids.size(); i++) {
            if(tempSkids.get(i) == null){
                ToastUtils.showLongToast(this,"请选择" + attrNames.get(i).name);
                return;
            }
        }
        RoundDetailModel.Sku subSkus = getSkuID();
        String skuID = "";
        if(subSkus != null){
            skuID = subSkus.skuId;
        }
        if(TextUtils.isEmpty(skuID)){
            ToastUtils.showLongToast(this,"商品不存在，请重新选择参数");
            return;
        }
        SureOrderModer sureOrderModer = new SureOrderModer();
        List<SureOrderModer.CartOrder> data = new ArrayList<>();
        SureOrderModer.CartOrder cartOrder = sureOrderModer.new CartOrder();
        double price = 0;
        if(subSkus != null){
            price = subSkus.price;
        }else {
            price = detailModeld.price;
        }

        cartOrder.pic = detailModeld.pic;
        cartOrder.name = detailModeld.name;
        cartOrder.skuId = skuID;
        cartOrder.goodsId = detailModeld.goodsId;
        cartOrder.price = price;
        Set<String> strings = des.keySet();
        List<String> desc = new ArrayList<>();
        for(String s : strings){
            desc.add(s + ":" + des.get(s));
        }
        cartOrder.desc = desc;
        cartOrder.num = num;
        data.add(cartOrder);
        sureOrderModer.data = data;
        Intent intent =  new Intent(RoundDetailActivity.this,SureOrderActivity.class);
        intent.putExtra(Contanst.GOODS_BY_DETAIL_ROUND,sureOrderModer);
        startActivity(intent);
        RoundDetailActivity.this.finish();
    }

    @OnClick(R.id.enter_cart)
    public void enterCart(View view){
        startActivity(new Intent(RoundDetailActivity.this,CartActivity.class));
    }

    @OnClick(R.id.add_cart)
    public void addCart(View view){
        if(detailModeld == null){
            ToastUtils.showLongToast(this,"获取商品信息失败");
            return;
        }
        RoundDetailModel.Sku subSkus = getSkuID();
        String skuID = "";
        if(subSkus != null){
            skuID = subSkus.skuId;
        }
        if(TextUtils.isEmpty(skuID)){
            ToastUtils.showLongToast(this,"商品不存在，请重新选择参数");
            return;
        }
        String token = (String) SpUtils.get(this, "token", "");
        Observable<CommonModel> observable = RetrofitFactory.getInstance().addCarts(token, num, skuID,detailModeld.goodsId);
        observable.compose(compose(this.<CommonModel>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel) throws Exception {
                        if(commonModel == null)return;
                        Logger.e(TAG, commonModel.toString());
                        if ("1".equals(commonModel.code)) {
                            Toast.makeText(RoundDetailActivity.this, "添加成功，在购物车等待亲", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RoundDetailActivity.this, commonModel.msg, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                    }
                });
    }

    public void getDetail2(String token ,String goodsId) {
        showLoadingDialog();
        Observable<BaseEntity<RoundDetailModel>> observable = RetrofitFactory.getInstance().getRoundDetail1(token,goodsId);
        observable.compose(compose(this.<BaseEntity<RoundDetailModel>>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RoundDetailModel>(this) {
                    @Override
                    protected void onHandleSuccess(RoundDetailModel roundDetailModel) {
                        dimissLoadingDialog();
                        if(roundDetailModel != null){
                            detailModeld = roundDetailModel;
                            Glide.with(RoundDetailActivity.this).load(roundDetailModel.pic).into(goodsImgs);
                            componet_name.setText(roundDetailModel.name);
                            attrNames.clear();
                            attrNames.addAll(roundDetailModel.attrNames);
                            detailSkuAdapter.notifyDataSetChanged();;
                            skuList.clear();
                            skuList.addAll( roundDetailModel.skuList);
                        }
                    }

                    @Override
                    protected void onHandleError(String msg, int code) {
                        super.onHandleError(msg, code);
                        dimissLoadingDialog();
                    }
                });
    }

    @Override
    public void onItemClick(int pos) {
        attrValues.clear();
        RoundDetailModel.AttrName attrName = attrNames.get(pos);
        attrValues.addAll(attrName.attrValues);
        showPickerViewSku(attrName.name,attrValues,pos);
    }


    private void showPickerViewSku(final String attrName ,final List<RoundDetailModel.AttrValue> attrValues,final  int pos) {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                RoundDetailModel.AttrValue attrValue = attrValues.get(options1);
                String tx = attrValue.getPickerViewText();
                if(attrName.contains(":")){
                    attrNames.get(pos).name = attrName.substring(0,attrName.indexOf(":")) + ":" + tx;
                }else {
                    attrNames.get(pos).name = attrName  + ":" + tx;
                }
                detailSkuAdapter.notifyItemChanged(pos,pos);
                String valueId = attrValue.valueId;
                skus.put(pos,valueId);
                Set<Integer> integers = skus.keySet();
                tempSkids.clear();
                for(Integer i : integers){
                    String value = skus.get(i).trim();
                    tempSkids.add(value);
                }
                RoundDetailModel.Sku subSkus = getSkuID();

                if(subSkus != null){
                    Logger.e(TAG,subSkus.toString() + "");
                    sum.setText("合计 : ￥" + num *subSkus.price );
                }
            }
        })
                .setTitleText( "参数选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(attrValues);//一级选择器
        pvOptions.show();
    }
}
