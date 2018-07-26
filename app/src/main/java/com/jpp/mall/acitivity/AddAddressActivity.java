package com.jpp.mall.acitivity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AddAddressActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.submit_address)
    TextView submitAddress;

    @BindView(R.id.shop_et)
    EditText nameEt;

    @BindView(R.id.area_et)
    EditText phoneEt;

    @BindView(R.id.address_et)
    EditText selectArea;

    @BindView(R.id.idcard_et)
    EditText detailAddress;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
//
//        setContentView(R.layout.activity_add_address);
//        ButterKnife.bind(this);
//        ImmersionBar.with(this).init();
//        StackManager.getInstance().pushActivity(this);
//        initData();
//    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.submit_address)
    public void setSubmitAddress(View view){
        String name = nameEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();
        String area = selectArea.getText().toString().trim();
        String detail = detailAddress.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"请输入姓名",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"请输入电话",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(area)){
            Toast.makeText(this,"请输入地区",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(detail)){
            Toast.makeText(this,"请输入地址",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("phone",phone);
        intent.putExtra("area",area);
        intent.putExtra("detail",detail);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initData() {
        title.setText(R.string.add_address);
    }


}
