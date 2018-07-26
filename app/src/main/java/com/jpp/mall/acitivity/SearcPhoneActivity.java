package com.jpp.mall.acitivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SearcPhoneActivity extends BaseActivity {
    private static final String TAG = "RegeistActivity";
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;


    @Override
    public int getLayoutId() {
        return R.layout.activity_searc_phone;
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @Override
    public void initData() {
        title.setText(R.string.search_phone);
    }
}
