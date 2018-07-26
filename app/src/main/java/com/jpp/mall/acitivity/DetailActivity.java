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
import com.jpp.mall.adapter.DetailSkuAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.CommonModel;
import com.jpp.mall.bean.DetailModel;
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

public class DetailActivity extends BaseActivity implements DetailSkuAdapter.OnItemClickListener {
    private static final String TAG = "DetailActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.componet_name)
    TextView componet_name;
    @BindView(R.id.add_cart)
    TextView addCart;
    @BindView(R.id.sum)
    TextView sum;
    @BindView(R.id.sure_order)
    TextView sureOrder;
    @BindView(R.id.goods_imgs)
    ImageView goodsImgs;
    @BindView(R.id.enter_cart)
    ImageView enterCart;
    @BindView(R.id.select_component)
    TextView select_component;
    @BindView(R.id.sku_rv)
    RecyclerView sku_rv;
    @BindView(R.id.amout_view)
    AmountView amountView;

    private Map<String,String> des = new LinkedHashMap<>();

    private String modelId;
    private int num = 1;
    private List<DetailModel.Part> parts = new ArrayList<>();
    private List<DetailModel.AttrName> attrNames = new ArrayList<>();
    private List<DetailModel.AttrValue> attrValues = new ArrayList<>();
    private DetailSkuAdapter detailSkuAdapter;
    private DetailModel currentModel;
    private DetailModel.Part currentPart;
    private List<DetailModel.SubSkus> skuList = new ArrayList<>();
    private Map<Integer,String> skus = new LinkedHashMap<>();
    private List<String> tempSkids  = new ArrayList<>();


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_detail);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }
    @Override
    public void initData() {
        Intent intent = getIntent();
        modelId = intent.getStringExtra("modelId");
        sku_rv.setLayoutManager(new LinearLayoutManager(this));
        sku_rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        detailSkuAdapter = new DetailSkuAdapter(attrNames, this);
        sku_rv.setAdapter(detailSkuAdapter);
        String token = (String) SpUtils.get(this, "token", "");
        getDetail2(token,modelId);
        detailSkuAdapter.setOnItemClickListener(this);
        amountView.setGoods_storage(100000);
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                //Toast.makeText(getApplicationContext(), "Amount=>  " + amount, Toast.LENGTH_SHORT).show();
                num = amount;
                if(currentPart != null){
                    DetailModel.SubSkus subSkus = getSkuID();
                    if(subSkus != null){
                        sum.setText("合计 : ￥" + amount *subSkus.price );
                    }else {
                        sum.setText("合计 : ￥" + amount *currentPart.price );
                    }
                }
            }
        });
    }

    @OnClick(R.id.select_component)
    public void selectComponent(View view){
        //条件选择器
        showPickerView();
    }

    /**
     * 弹出选择器
    */
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if(parts != null && !parts.isEmpty()){
                    DetailModel.Part part = parts.get(options1);
                    currentPart = part;
                    String tx = part.getPickerViewText();
                    attrNames.clear();
                    attrNames.addAll(part.attrNames);
                    //Toast.makeText(DetailActivity.this, tx, Toast.LENGTH_SHORT).show();
                    select_component.setText("选择部件:" + tx );
                    detailSkuAdapter.notifyDataSetChanged();
                    sum.setText("￥" + part.price);
                    skuList.clear();
                    skuList.addAll(part.skuList);
                    //tempSkids =  new String[attrNames.size()];;
                    //Log.e(TAG,"tempSkids" + attrNames.size());
                }

            }
        })
                .setTitleText("部件选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(parts);//一级选择器
        //pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        //pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    /**
     * 获取skuid
     * @return
     */
    public DetailModel.SubSkus  getSkuID(){

        DetailModel.SubSkus sub = null;
        if(tempSkids != null){
            String [] temp = (String[]) tempSkids.toArray(new String[0]);
            Arrays.sort(temp);// 排序
            for (int i = 0; i < skuList.size(); i++) {
                DetailModel.SubSkus subSkus = skuList.get(i);
                String valueIds = subSkus.valueIds;
                String[] split = valueIds.split(",");
                Arrays.sort(split);// 排序
                Log.e(TAG,temp + "");
                Log.e(TAG,split+ "");
                if(Arrays.equals(temp,split)){
                    sub = subSkus;
                    break;
                }
            }
        }
        return sub;
    }

    @OnClick(R.id.sure_order)
    public void sureOrder(View view){
        if(currentPart == null ){
            ToastUtils.showLongToast(this,"请选择部件");
            return;
        }
        for (int i = 0; i < tempSkids.size(); i++) {
            if(tempSkids.get(i) == null){
                ToastUtils.showLongToast(this,"请选择" + attrNames.get(i).name);
                return;
            }
        }
        DetailModel.SubSkus subSkus = getSkuID();
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
            price = currentPart.price;
        }
        cartOrder.pic = currentModel.pic;
        cartOrder.name = currentModel.name ;
        cartOrder.price = price;
        Set<String> strings = des.keySet();
        List<String> desc = new ArrayList<>();
        for(String s : strings){
            desc.add(s + ":" + des.get(s));
        }
        cartOrder.desc = desc;
        cartOrder.num = num;
        cartOrder.goodsId = currentPart.goodsId;
        cartOrder.skuId = skuID;
        data.add(cartOrder);
        sureOrderModer.data = data;
        Intent intent =  new Intent(DetailActivity.this,SureOrderActivity.class);
        intent.putExtra(Contanst.GOODS_BY_DETAIL,sureOrderModer);
        startActivity(intent);
        DetailActivity.this.finish();
    }

    @OnClick(R.id.enter_cart)
    public void enterCart(View view){
        startActivity(new Intent(DetailActivity.this,CartActivity.class));
        DetailActivity.this.finish();
    }

    @OnClick(R.id.add_cart)
    public void addCart(View view){
        if(currentPart == null){
            ToastUtils.showShortToast(DetailActivity.this,"部件未选择");
            return;
        }
        DetailModel.SubSkus subSkus = getSkuID();
        String skuID = "";
        if(subSkus != null){
            skuID = subSkus.skuId;
        }
        if(TextUtils.isEmpty(skuID)){
            ToastUtils.showLongToast(this,"商品不存在，请重新选择参数");
            return;
        }
        String token = (String) SpUtils.get(this, "token", "");
        showLoadingDialog();
        Observable<CommonModel> observable = RetrofitFactory.getInstance().addCarts(token, num, skuID,currentPart.goodsId);
        observable.compose(compose(this.<CommonModel>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommonModel>() {
                    @Override
                    public void accept(CommonModel commonModel) {
                        dimissLoadingDialog();
                        if(commonModel == null)return;
                        Logger.e(TAG, commonModel.toString());
                        if ("1".equals(commonModel.code)) {
                            Toast.makeText(DetailActivity.this, "添加成功，在购物车等待亲", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DetailActivity.this, commonModel.msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                       // Logger.e(TAG, throwable.getMessage());
                        dimissLoadingDialog();
                    }
                });
    }

    public void getDetail2(String token ,String modelId) {
        showLoadingDialog();
        Observable<BaseEntity<DetailModel>> observable = RetrofitFactory.getInstance().getDetail1(token,modelId);
        observable.compose(compose(this.<BaseEntity<DetailModel>>bindToLifecycle()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<DetailModel>(this) {
                    @Override
                    protected void onHandleSuccess(DetailModel detailModel) {
                        if(detailModel != null){
                            currentModel = detailModel;
                            parts.clear();
                            parts.addAll(detailModel.parts);
                            Logger.e(TAG,detailModel.toString());
                            Glide.with(DetailActivity.this).load(detailModel.pic).into(goodsImgs);
                            componet_name.setText(detailModel.name);
                        }
                        dimissLoadingDialog();
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
        DetailModel.AttrName attrName = attrNames.get(pos);
        attrValues.addAll(attrName.attrValues);
        showPickerViewSku(attrName.name,attrValues,pos);
    }

    private void showPickerViewSku(final String attrName ,final List<DetailModel.AttrValue> attrValues,final  int pos) {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                DetailModel.AttrValue attrValue = attrValues.get(options1);
                String tx = attrValue.getPickerViewText();
                if(attrName.contains(":")){
                    attrNames.get(pos).name = attrName.substring(0,attrName.indexOf(":")) + ":" + tx;
                }else {
                    attrNames.get(pos).name = attrName  + ":" + tx;
                }
                String s = attrNames.get(pos).name.split(":")[0];
                des.put(s,tx);
                detailSkuAdapter.notifyItemChanged(pos,pos);
                String valueId = attrValue.valueId;
                skus.put(pos,valueId);
                Set<Integer> integers = skus.keySet();
                tempSkids.clear();
                for(Integer i : integers){
                    String value = skus.get(i).trim();
                    tempSkids.add(value);
                }
                DetailModel.SubSkus subSkus = getSkuID();

                if(subSkus != null){
                    Log.e(TAG,subSkus.toString() + "");
                    sum.setText("合计 : ￥" + num *subSkus.price );
                }
            }
        })
                .setTitleText("参数选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(attrValues);//一级选择器
        pvOptions.show();
    }
}
