package com.jpp.mall.acitivity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jpp.mall.BindIdCardActivity;
import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.bean.ShopInfoModel;
import com.jpp.mall.net.BaseEntity;
import com.jpp.mall.net.BaseObserver;
import com.jpp.mall.net.RetrofitFactory;
import com.jpp.mall.utils.Logger;
import com.jpp.mall.utils.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class ShopInfoActivity extends BaseActivity {
    private static final String TAG = "ShopInfoActivity";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.shopId)
    TextView shopId;
    @BindView(R.id.add_bank_card)
    RelativeLayout add_bank_card;
    @BindView(R.id.add_id_card)
    RelativeLayout add_id_card;
    @BindView(R.id.set_password)
    RelativeLayout set_password;


    @Override

    public int getLayoutId() {
        return R.layout.activity_shop_info;
    }
    @Override
    public void initData() {
        title.setText(R.string.shop_info);
        getShopInfo();
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.add_bank_card)
    public void enterBankCardList(View view){
        startActivity(new Intent(this,BankCardListActivity.class));
        overridePendingTransition(0,0);
    }
    @OnClick(R.id.add_id_card)
    public void enterToBindIdCard(View view){
        startActivity(new Intent(this,BindIdCardActivity.class));
        overridePendingTransition(0,0);
    }

    @OnClick(R.id.set_password)
    public void set_password(View view){
        startActivity(new Intent(this,SetPayPasswordActivity.class));
        overridePendingTransition(0,0);
    }

    public void getShopInfo() {
        String token = (String) SpUtils.get(this,"token","");
        Observable<BaseEntity<ShopInfoModel>> observable = RetrofitFactory.getInstance().getShopInfo(token);
        observable.compose(compose(this.<BaseEntity<ShopInfoModel>>bindToLifecycle())).subscribe(new BaseObserver<ShopInfoModel>(this) {
            @Override
            protected void onHandleSuccess(ShopInfoModel shopInfoModel) {
                // 保存用户信息等操作
                Logger.e(TAG, "onHandleSuccess:" + shopInfoModel.toString() );
                name.setText(shopInfoModel.shopInfo.name);
                phone.setText(shopInfoModel.phone);
                address.setText("" + shopInfoModel.shopInfo.fullAddress);
                shopId.setText(shopInfoModel.shopInfo.shopId);
            }
        });
    }
}
