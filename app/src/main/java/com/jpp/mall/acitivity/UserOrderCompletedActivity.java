package com.jpp.mall.acitivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserOrderCompletedActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_user_order_completed);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        initData();
//    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_order_completed;
    }
    @Override
    public void initData() {
        title.setText(R.string.order_detail);
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

}
