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
import com.jpp.mall.fragment.UserOrderOneFragment;
import com.jpp.mall.fragment.UserOrderThreeFragment;
import com.jpp.mall.fragment.UserOrderTwoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.user_order_vp)
    ViewPager userOrderVp;

    @BindView(R.id.usr_order_rg)
    RadioGroup userOrderGroup;

    @BindView(R.id.pending_user)
    RadioButton peandingUser;

    @BindView(R.id.processing_user)
    RadioButton processingUser;

    @BindView(R.id.completed_user)
    RadioButton completedUser;


    @Override
    public int getLayoutId() {
        return R.layout.activity_user_order;
    }
    @Override
    public void initData() {
        title.setText(R.string.user_order);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new UserOrderOneFragment());
        fragments.add(new UserOrderTwoFragment());
        fragments.add(new UserOrderThreeFragment());
        SellerOrderPageAdapter sellerOrderPageAdapter = new SellerOrderPageAdapter(getSupportFragmentManager(),fragments);
        userOrderVp.setAdapter(sellerOrderPageAdapter);
        peandingUser.setChecked(true);
        userOrderGroup.setOnCheckedChangeListener(this);
        userOrderVp.setOnPageChangeListener(this);
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.pending_user:
                userOrderVp.setCurrentItem(0);
                break;
            case R.id.processing_user:
                userOrderVp.setCurrentItem(1);
                break;
            case R.id.completed_user:
                userOrderVp.setCurrentItem(2);
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
                peandingUser.setChecked(true);
                break;
            case 1:
                processingUser.setChecked(true);
                break;
            case 2:
                completedUser.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
