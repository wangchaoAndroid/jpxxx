package com.jpp.mall.acitivity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpp.mall.R;
import com.jpp.mall.base.BaseActivity;
import com.jpp.mall.utils.SpUtils;
import com.jpp.mall.utils.StackManager;
import com.tencent.android.tpush.XGPushManager;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.shop_info)
    TextView shopInfo;
    @BindView(R.id.help_center)
    TextView helpCenter;
    @BindView(R.id.commoent)
    TextView commoent;
    @BindView(R.id.service_log)
    TextView serviceLog;
    @BindView(R.id.credibility_querry)
    TextView credibilityQuerry;
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.next_look)
    TextView nextLook;


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }
    @Override
    public void initData() {
        title.setText("设置");
    }

    @OnClick(R.id.shop_info)
    public void querryShopInfo(View view){
        startActivity(new Intent(SettingActivity.this,ShopInfoActivity.class));
    }

    @OnClick(R.id.help_center)
    public void helpCenter(View view){

    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }

    @OnClick(R.id.commoent)
    public void commoent(View view){

    }

    @OnClick(R.id.service_log)
    public void serviceLog(View view){

    }


    @OnClick(R.id.next_look)
    public void nextLook(View view){
        startActivity(new Intent(this,NextLevelActivity.class));
        overridePendingTransition(0,0);
    }

    @OnClick(R.id.credibility_querry)
    public void credibilityQuerry(View view){
        startActivity(new Intent(this,CredibilityQuerryActivity.class));
    }

    @OnClick(R.id.logout)
    public void logout(View view){
        final Dialog dialog = new Dialog(this,R.style.common_dialog_style);
        View dialogView = View.inflate(this,R.layout.dialog_logout,null);
        dialog.setContentView(dialogView);
        dialog.show();
        Window window = dialog.getWindow();
        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        dialogView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XGPushManager.unregisterPush(SettingActivity.this);
                SpUtils.put(SettingActivity.this,"token","");
                StackManager.getInstance().popAllActivity();
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
            }
        });
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        window.setAttributes(attributes);

    }
}
