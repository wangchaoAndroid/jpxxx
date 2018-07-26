package com.jpp.mall.acitivity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.adapter.SellerOrderPageAdapter;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.fragment.SellerOrderFourFragment;
import com.jpp.mall.fragment.SellerOrderOneFragment;
import com.jpp.mall.fragment.SellerOrderThreeFragment;
import com.jpp.mall.fragment.SellerOrderTwoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SellerOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "SellerOrderActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.seller_order_vp)
    ViewPager sellerOrderVp;
    @BindView(R.id.seller_rg)
    RadioGroup orderRg;
    @BindView(R.id.order_pay)
    RadioButton orderPay;
    @BindView(R.id.deliver_rb)
    RadioButton delevierRb;
    @BindView(R.id.recept_rb)
    RadioButton receptRb;
    @BindView(R.id.complete_rb)
    RadioButton completeRb;

    @Override
    public int getLayoutId() {
        return R.layout.activity_seller_order;
    }
    @Override
    public void initData() {
        title.setText(R.string.seller_order);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SellerOrderOneFragment());
        fragments.add(new SellerOrderTwoFragment());
        fragments.add(new SellerOrderThreeFragment());
        fragments.add(new SellerOrderFourFragment());
        SellerOrderPageAdapter sellerOrderPageAdapter = new SellerOrderPageAdapter(getSupportFragmentManager(),fragments);
        sellerOrderVp.setAdapter(sellerOrderPageAdapter);
        orderPay.setChecked(true);
        orderRg.setOnCheckedChangeListener(this);
        sellerOrderVp.setOnPageChangeListener(this);
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.order_pay:
                sellerOrderVp.setCurrentItem(0);
                break;
            case R.id.deliver_rb:
                sellerOrderVp.setCurrentItem(1);
                break;
            case R.id.recept_rb:
                sellerOrderVp.setCurrentItem(2);
                break;
            case R.id.complete_rb:
                sellerOrderVp.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                orderPay.setChecked(true);
                break;
            case 1:
                delevierRb.setChecked(true);
                break;
            case 2:
                receptRb.setChecked(true);
                break;
            case 3:
                completeRb.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



}
